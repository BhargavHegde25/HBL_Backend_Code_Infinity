package com.temenos.infinity.api.arrangements.resource.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.temenos.infinity.api.arrangements.businessdelegate.api.ArrangementsBusinessDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.arrangements.prop.AccountsCountProperties;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

/**
 * 
 * @author smugesh
 * @version 1.0 Extends the {@link AccountsResource}
 */
public class ArrangementsResourceImpl implements ArrangementsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final int CACHE_IN_SECONDS = (1 * 30 * 60); // 30 mins

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Result getArrangementAccountsForAdmin(String backendUserId, String customerType, String customerID,
            String productLineId, String Account_id, String CompanyId, DataControllerRequest request, String authToken)
            throws ApplicationException {

        Result result = new Result();
        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
        }
        ArrangementsBusinessDelegate AccountsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);

        ArrangementsDTO inputPayloadDTO = new ArrangementsDTO();
        String username = request.getParameter(TemenosConstants.PARAM_USERNAME) != null
                ? request.getParameter(TemenosConstants.PARAM_USERNAME).toString()
                : "";

        List<ArrangementsDTO> accountsDTO = null;

        inputPayloadDTO.setCustomerType(customerType);
        inputPayloadDTO.setAccount_id(Account_id);
        inputPayloadDTO.setCustomerID(customerID);
        inputPayloadDTO.setUserName(username);
        try {
            if (StringUtils.isNotBlank(customerType)) {
                if (!customerType.equals("TYPE_ID_RETAIL") && !customerType.equals("TYPE_ID_PROSPECT")) {
                    accountsDTO = AccountsDelegateInstance.getBusinessUserArrangements(inputPayloadDTO, request);
                } else {
                    if (backendUserId.contains("-")) {
                        inputPayloadDTO.setBackendUserId(backendUserId);
                    } else {
                        CompanyId = StringUtils.isNotEmpty(CompanyId) ? CompanyId
                                : ServerConfigurations.AMS_COMPANYID.getValue();
                        inputPayloadDTO.setBackendUserId(CompanyId + "-" + backendUserId);
                    }
                    ArrayList resp = AccountsDelegateInstance.getArrangements(inputPayloadDTO, request, authToken, Boolean.TRUE);
                    accountsDTO = (List<ArrangementsDTO>) resp.get(1);
                    
                }
            } else {
                return ErrorCodeEnum.ERR_20047.setErrorCode(new Result());
            }
        } catch (Exception e) {
            alert.prepareError("Unable to fetch records from Backend" + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }

        JSONArray accountsDTOArray = new JSONArray(accountsDTO);
        for (int i = 0; i < accountsDTOArray.length(); i++) {
            JSONObject arrangement = accountsDTOArray.getJSONObject(i);
            if (arrangement.has("account_id")) {
                String id = arrangement.getString("account_id");
                arrangement.put("Account_id", id);
                arrangement.remove("account_id");
            }
            if (arrangement.has("membership_id")) {
                String memberShipId = arrangement.getString("membership_id");
                if (StringUtils.isNotBlank(memberShipId)) {
                    arrangement.put("Membership_id", memberShipId);
                    arrangement.remove("membership_id");
                }
            }
            if (arrangement.has("taxId")) {
                String taxId = arrangement.getString("taxId");
                if (StringUtils.isNotBlank(taxId)) {
                    arrangement.put("TaxId", taxId);
                    arrangement.remove("taxId");
                }
            }
        }

        JSONObject responseObj = new JSONObject();
        responseObj.put("Accounts", accountsDTOArray);
        result = JSONToResult.convert(responseObj.toString());

        return result;
    }

    // Implementing the get Accounts method
    @SuppressWarnings({ "null", "unchecked", "rawtypes", "deprecation" })
	@Override
    public Result getArrangementAccounts(String backendUserId, String customerType, String customerID,
            String productLineId, String Account_id, String CompanyId, DataControllerRequest request, String authToken)
            throws ApplicationException {

        Result result = new Result();
        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
        }
        ArrangementsBusinessDelegate AccountsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);

        ArrangementsDTO inputPayloadDTO = new ArrangementsDTO();
        String username = request.getParameter(TemenosConstants.PARAM_USERNAME) != null
                ? request.getParameter(TemenosConstants.PARAM_USERNAME).toString()
                : "";
        Boolean singleCoreCustFlow = Boolean.FALSE;
        String Membership_id = request.getParameter(TemenosConstants.Membership_id) != null
                ? request.getParameter(TemenosConstants.Membership_id).toString()
                : "";
        String Actions = request.getParameter(TemenosConstants.Actions) != null
                ? request.getParameter(TemenosConstants.Actions).toString()
                : "";
        
        if ( StringUtils.isNotBlank(Membership_id)) {
        	backendUserId =Membership_id;
            singleCoreCustFlow = Boolean.TRUE;
        }

        List<ArrangementsDTO> accountsDTO = null;
        List <ArrangementsDTO> portfolioDTO = new ArrayList<ArrangementsDTO>();
        String flow = request.getParameter("_flow") != null
                ? request.getParameter("_flow").toString():"";
        inputPayloadDTO.setCustomerType(customerType);
        inputPayloadDTO.setAccount_id(Account_id);
        inputPayloadDTO.setCustomerID(customerID);
        inputPayloadDTO.setUserName(username);
        String ArrangementResponse = null;
        Map<String,ArrangementsDTO> redesignAcc = new HashMap<>();
        try {
            if (StringUtils.isNotBlank(customerType) && customerType.equals("TYPE_ID_PROSPECT")) {
                if (backendUserId.contains("-")) {
                    inputPayloadDTO.setBackendUserId(backendUserId);
                } else {
                    CompanyId = StringUtils.isNotEmpty(CompanyId) ? CompanyId
                            : ServerConfigurations.AMS_COMPANYID.getValue();
                    inputPayloadDTO.setBackendUserId(CompanyId + "-" + backendUserId);
                }
                ArrayList resp = AccountsDelegateInstance.getArrangements(inputPayloadDTO, request, authToken,Boolean.FALSE);
                ArrangementResponse = (String) resp.get(0);
                accountsDTO = (List<ArrangementsDTO>) resp.get(1);
                int i=0;
                while(i<accountsDTO.size()) {
                	ArrangementsDTO portfolio = accountsDTO.get(i);
                	if(StringUtils.isNotBlank(portfolio.getAccountType())) {
                	if (portfolio.getAccountType().equals("Investment")) {
                		portfolioDTO.add(portfolio);
                		accountsDTO.remove(i);
                	}else i++;
                	}
                	else i++;
                }
            } else {
                boolean isImplicitBackendId = false;
                accountsDTO = new ArrayList<>();
                if (StringUtils.isBlank(CompanyId)) {
                    CompanyId = ServerConfigurations.AMS_COMPANYID.getValue();
                }
                String accountsString = "";
                JsonArray accountsjsonarray = new JsonArray();
                
                /*Existing flows other than getList to avoid regression*/
				if (StringUtils.isBlank(flow) || !"getList".equalsIgnoreCase(flow)) {
                    if (StringUtils.isNotBlank(backendUserId)) {
                        
                    	
                    	
                    	if (backendUserId.contains("-")) {
                            inputPayloadDTO.setBackendUserId(backendUserId);
                        } else {
                            CompanyId = StringUtils.isNotEmpty(CompanyId) ? CompanyId
                                    : ServerConfigurations.AMS_COMPANYID.getValue();
                            inputPayloadDTO.setBackendUserId(CompanyId + "-" + backendUserId);
                        }
                        
                        
                        ArrayList resp =
                                AccountsDelegateInstance.getArrangements(inputPayloadDTO, request, authToken,singleCoreCustFlow);
                        ArrangementResponse = (String) resp.get(0);
                        
                        
                        List<ArrangementsDTO> accountsDTO1 = (List<ArrangementsDTO>) resp.get(1);
                        
                        
                        int i=0;
                        while(i<accountsDTO1.size()) {
                        	ArrangementsDTO portfolio = accountsDTO1.get(i);
                        	redesignAcc.put(portfolio.getAccount_id(), portfolio);
                        	if(StringUtils.isNotBlank(portfolio.getAccountType())) {
                        	if (portfolio.getAccountType().equals("Investment")) {
                        		portfolioDTO.add(portfolio);
                        		accountsDTO1.remove(i);
                        	}else i++;
                        	}
                        	else i++;
                        }
                        for (ArrangementsDTO arrangementsDTO : accountsDTO1) {
                            if (Boolean.parseBoolean(arrangementsDTO.getExternalIndicator())
                                    || "Yes".equals(arrangementsDTO.getExternalIndicator())) {
                                accountsDTO.add(arrangementsDTO);
                            } else {
                                JsonObject json = new JsonObject();
                                json = getArrangementJson(arrangementsDTO);
                                if (json != null) {
                                    json.addProperty("customerId", backendUserId);
                                    accountsjsonarray.add(json);
                                    accountsString += JSONUtil.getString(json, "accountId") + " ";
                                }
                            }
                        }
                    }
                }
                else
                {
					/* Fixing getList flow to read data from contractcustomers table and get accounts*/
                	List<ArrangementsDTO> accountsDTO1 = new ArrayList<>();
					List<String> customerContracts = readContractCustomerData(customerID, CompanyId, request);
					for (String coreCustomer : customerContracts) {
						inputPayloadDTO.setBackendUserId(CompanyId + "-" + coreCustomer);
						ArrayList resp = AccountsDelegateInstance.getArrangements(inputPayloadDTO, request, authToken,
								singleCoreCustFlow);
						ArrangementResponse = (String) resp.get(0);

						accountsDTO1.addAll((List<ArrangementsDTO>) resp.get(1));
					}

					int i = 0;
					while (i < accountsDTO1.size()) {
						ArrangementsDTO portfolio = accountsDTO1.get(i);
						redesignAcc.put(portfolio.getAccount_id(), portfolio);
						if (StringUtils.isNotBlank(portfolio.getAccountType())) {
							if (portfolio.getAccountType().equals("Investment")) {
								portfolioDTO.add(portfolio);
								accountsDTO1.remove(i);
							} else
								i++;
						} else
							i++;
					}
					for (ArrangementsDTO arrangementsDTO : accountsDTO1) {
						if (Boolean.parseBoolean(arrangementsDTO.getExternalIndicator())
								|| "Yes".equals(arrangementsDTO.getExternalIndicator())) {
							accountsDTO.add(arrangementsDTO);
						} else {
							JsonObject json = new JsonObject();
							json = getArrangementJson(arrangementsDTO);
							if (json != null) {
								json.addProperty("customerId", backendUserId);
								accountsjsonarray.add(json);
								accountsString += JSONUtil.getString(json, "accountId") + " ";
							}
						}
					}

				}
                
                Map<String, ArrangementsDTO> implicitCIFAccountsInfo = new HashMap<String, ArrangementsDTO>();
                Map<String, String> newAcntCoreCustomerId = new HashMap<String, String>();
                HashSet<String> newAccountCoreCustomerIdList = new HashSet<String>();
                Map<String, Set> coreCustomerActions = new HashMap<String, Set>();
                if (StringUtils.isNotBlank(customerID)) {
                    Map<String, Object> inputParams = new HashMap<String, Object>();
                    if (!singleCoreCustFlow) {
                    inputParams.put("$filter", "customerId eq " + customerID + " and autoSyncAccounts eq 1");
                    request.addRequestParam_("$filter",
                            "customerId eq " + customerID + " and companyLegalUnit eq "+ CompanyId+ " and autoSyncAccounts eq 1");
                    String contractCustomersString = DBPServiceExecutorBuilder.builder()
                            .withServiceId(ArrangementsAPIServices.DBXDB_CONTRACT_CUSTOMERS.getServiceName())
                            .withOperationId(ArrangementsAPIServices.DBXDB_CONTRACT_CUSTOMERS.getOperationName())
                            .withRequestParameters(inputParams).withRequestHeaders(request.getHeaderMap())
                            .withDataControllerRequest(request)
                            .build().getResponse();
                    JsonObject contractCustomers =
                            new JsonParser().parse(contractCustomersString).getAsJsonObject();
                    Set<String> implicitCustomers = new HashSet<>();
                    if (contractCustomers != null && contractCustomers.has("contractcustomers")
                            && contractCustomers.get("contractcustomers") != null
                            && contractCustomers.get("contractcustomers").isJsonArray()
                            && contractCustomers.get("contractcustomers").getAsJsonArray().size() > 0) {
                        for (JsonElement jsonelement : contractCustomers.get("contractcustomers")
                                .getAsJsonArray()) {
                            implicitCustomers.add(
                                    jsonelement.getAsJsonObject().get("coreCustomerId").getAsString());
                        }
                        // if (StringUtils.isNotBlank(backendUserId) && implicitCustomers.contains(backendUserId)) {
                        // implicitCustomers.remove(backendUserId);
                        // isImplicitBackendId = true;
                        // }
                        alert.prepareError("Printing the implicit customer:" + implicitCustomers).log();
                    }

                    if (!implicitCustomers.isEmpty()) {
                        for (String coreCustomerId : implicitCustomers) {
                            inputPayloadDTO.setBackendUserId(CompanyId + "-" + coreCustomerId);
                            ArrayList resp =
                                    AccountsDelegateInstance.getArrangements(inputPayloadDTO, request, authToken,Boolean.TRUE);
                            ArrangementResponse = (String) resp.get(0);
                            List<ArrangementsDTO> accountsDTO1 = (List<ArrangementsDTO>) resp.get(1);
                            for (ArrangementsDTO arrangementsDTO : accountsDTO1) {
                                if (!Boolean.parseBoolean(arrangementsDTO.getExternalIndicator())
                                        || "False".equals(arrangementsDTO.getExternalIndicator())) {
                                    implicitCIFAccountsInfo.put(arrangementsDTO.getAccount_id(), arrangementsDTO);
                                    JsonObject json = new JsonObject();
                                    if (json != null) {
                                        json = getArrangementJson(arrangementsDTO);
                                        json.addProperty("customerId", coreCustomerId);
                                        accountsjsonarray.add(json);
                                    }
                                }
                            }
                        }
                    }
                	}
                    inputParams = new HashMap<String, Object>();
                    inputParams.put("accounts", accountsjsonarray.toString());
                    inputParams.put("customerId", customerID);
                    request.addRequestParam_("accounts", accountsjsonarray.toString());
                    request.addRequestParam_("customerId", customerID);
                    String validAccountsList = DBPServiceExecutorBuilder.builder()
                            .withServiceId(
                                    ArrangementsAPIServices.DBPPRODUCTSERVICES_NEWACCOUNTPROCESSING
                                            .getServiceName())
                            .withOperationId(
                                    ArrangementsAPIServices.DBPPRODUCTSERVICES_NEWACCOUNTPROCESSING
                                            .getOperationName())
                            .withRequestParameters(inputParams).withRequestHeaders(request.getHeaderMap())
                            .withDataControllerRequest(request)
                            .build().getResponse();
                    JsonObject dashBoardAccounts = new JsonParser().parse(validAccountsList).getAsJsonObject();
                    accountsString =
                            dashBoardAccounts.get("accounts") != null
                                    ? dashBoardAccounts.get("accounts").getAsString()
                                    : "";
                    String newAccounts = dashBoardAccounts.get("newAccounts") != null
                            ? dashBoardAccounts.get("newAccounts").getAsString()
                            : "";
                    if (StringUtils.isNotBlank(newAccounts)) {
                        String[] accountsInfo = newAccounts.split("\\|");
                        for (int i = 0; i < accountsInfo.length; i++) {
                            String[] singleAccountInfo = accountsInfo[i].split(":");
                            newAcntCoreCustomerId.put(singleAccountInfo[1], singleAccountInfo[0]);
                            newAccountCoreCustomerIdList.add(singleAccountInfo[0]);
                        }
                    }
                    if (!"false".equals(Actions)) {
                    coreCustomerActions =
                            fetchDefaultAccountActions(request, customerID, newAccountCoreCustomerIdList);
                    }
                }
                String[] validAccountsArray = accountsString.split(" ");
                StringBuilder bulkAccountsString = new StringBuilder();
                for (int i = 0; i < validAccountsArray.length; i++) {
                	if (!singleCoreCustFlow ) {
                    if (implicitCIFAccountsInfo.containsKey(validAccountsArray[i]))
                        accountsDTO.add(implicitCIFAccountsInfo.get(validAccountsArray[i]));
                    else {
                        if (!bulkAccountsString.toString().isEmpty()) {
                            bulkAccountsString.append(" ");
                        }
                        bulkAccountsString.append(CompanyId).append("-");
                        bulkAccountsString.append(validAccountsArray[i]);
                    }
                	}
                	else if (singleCoreCustFlow && redesignAcc.containsKey(validAccountsArray[i])) {
                		 accountsDTO.add(redesignAcc.get(validAccountsArray[i]));
                	}
                }

                if (StringUtils.isNotBlank(bulkAccountsString.toString())) {
                    inputPayloadDTO.setAccount_id(bulkAccountsString.toString());
                    ArrangementsBusinessDelegate arrangementsBusinessDelegateImpl =
                            DBPAPIAbstractFactoryImpl.getBusinessDelegate(ArrangementsBusinessDelegate.class);
                    List<ArrangementsDTO> accountIdBasedArrangements =
                            arrangementsBusinessDelegateImpl.getArrangementBulkOverview(ArrangementResponse,inputPayloadDTO,
                                    request, authToken);
                    for (ArrangementsDTO dto : accountIdBasedArrangements) {
                        if (newAcntCoreCustomerId.containsKey(dto.getAccount_id())) {
                        	if(coreCustomerActions!=null && coreCustomerActions.get(newAcntCoreCustomerId.get(dto.getAccount_id()))!=null) {
                                dto.setActions(coreCustomerActions.get(newAcntCoreCustomerId.get(dto.getAccount_id()))
                                        .toString());
                        	}

                            dto.setIsNew("true");
                        } else {
                            dto.setIsNew("false");
                        }
                        accountsDTO.add(dto);
                    }
                }
            }
            accountsDTO.addAll(portfolioDTO);
            String mockMortgageResponse = ServerConfigurations.MOCK_MORTGAGE_RESPONSE.getValue();
            if(mockMortgageResponse != null && mockMortgageResponse.equalsIgnoreCase("Yes")) {
            	List<ArrangementsDTO> mortgageDTOs = getMortgages();
            	accountsDTO.addAll(mortgageDTOs);
            }
        } catch (Exception e) {
            alert.prepareError("Unable to fetch records from Backend", e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }

        JSONArray accountsDTOArray = new JSONArray(accountsDTO);

        JSONObject responseObj =
                new JSONObject();
        responseObj.put("Accounts", accountsDTOArray);
        result = JSONToResult.convert(responseObj.toString());
        alert.prepareError("Accounts  " + accountsDTOArray.toString()).log();
        return result;
    }

	private List<String> readContractCustomerData(String customerId, String companyId, DataControllerRequest request)
			throws DBPApplicationException {

		List<String> customerContracts = new ArrayList<String>();
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("$filter", "customerId eq " + customerId + " and companyLegalUnit eq " + companyId);
		request.addRequestParam_("$filter", "customerId eq " + customerId + " and companyLegalUnit eq " + companyId);

		alert.prepareError("Input params " + inputParams).log();

		 String contractCustomersString = DBPServiceExecutorBuilder.builder()
                 .withServiceId(ArrangementsAPIServices.DBXDB_CONTRACT_CUSTOMERS.getServiceName())
                 .withOperationId(ArrangementsAPIServices.DBXDB_CONTRACT_CUSTOMERS.getOperationName())
                 .withRequestParameters(inputParams).withRequestHeaders(request.getHeaderMap())
                 .withDataControllerRequest(request)
                 .build().getResponse();
		Result coreCustomers = JSONToResult.convert(contractCustomersString);
		alert.prepareError(
				"****************************************** coreCustomers : " + ResultToJSON.convert(coreCustomers))
				.log();
		if (coreCustomers != null && coreCustomers.getAllDatasets().size() > 0
				&& coreCustomers.getDatasetById("contractcustomers").getAllRecords().size() > 0) {
			alert.prepareError("Records in contractcustomers ::::").log();
			for (Record record : coreCustomers.getDatasetById("contractcustomers").getAllRecords()) {
				customerContracts.add(record.getParamValueByName("coreCustomerId"));
			}
		}
		return customerContracts;

	}

	private JsonObject getArrangementJson(ArrangementsDTO arrangementsDTO) {
        JsonObject json = new JsonObject();
        if (StringUtils.isBlank(arrangementsDTO.getAccount_id())
                || StringUtils.isBlank(arrangementsDTO.getAccountType())
                || StringUtils.isBlank(arrangementsDTO.getAccountName())
                || StringUtils.isBlank(arrangementsDTO.getArrangementId())
                || StringUtils.isBlank(arrangementsDTO.getPartyRole())) {
            return null;
        }
        json.addProperty("accountId", arrangementsDTO.getAccount_id());
        json.addProperty("customerId", arrangementsDTO.getMembership_id());
        json.addProperty("accountType", arrangementsDTO.getAccountType());
        json.addProperty("accountName", arrangementsDTO.getAccountName());
        json.addProperty("arrangementId", arrangementsDTO.getArrangementId());
        json.addProperty("roleDisplayName", arrangementsDTO.getPartyRole());
        return json;
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    public Map<String, Set> fetchDefaultAccountActions(DataControllerRequest request, String customerId,
            HashSet<String> coreCustomerIdList) {
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Set> coreCustomerActions = new HashMap<>();
        try {
            for (String coreCustomerId : coreCustomerIdList) {
                inputParams = new HashMap<>();
                inputParams.put("_userId", customerId);
                inputParams.put("_coreCustomerId", coreCustomerId);
                request.addRequestParam_("_userId", customerId);
                request.addRequestParam_("_coreCustomerId", coreCustomerId);
                String defaultActionsString = DBPServiceExecutorBuilder.builder()
                        .withServiceId(
                                ArrangementsAPIServices.DBXDB_FETCH_DEFAULT_ACTIONS.getServiceName())
                        .withOperationId(
                                ArrangementsAPIServices.DBXDB_FETCH_DEFAULT_ACTIONS.getOperationName())
                        .withRequestParameters(inputParams).withRequestHeaders(request.getHeaderMap())
                        .withDataControllerRequest(request)
                        .build().getResponse();
                JsonObject defaultActionsJson = new JsonParser().parse(defaultActionsString).getAsJsonObject();
                String actions = defaultActionsJson.get("defaultAccountActions").getAsString();
                coreCustomerActions.put(coreCustomerId, new HashSet<>(Arrays.asList(StringUtils.split(actions, ","))));
            }
            return coreCustomerActions;
        } catch (Exception e) {
            alert.prepareError(e.toString().toString()).log();
        }
        return coreCustomerActions;
    }

    // Implementing the get Account Overview method
    @SuppressWarnings("deprecation")
	@Override
    public Result getAccountOverview(String backendUserId, String customerType, String customerID, String productLineId,
            String Account_id, DataControllerRequest request, String authToken) throws ApplicationException {

        Result result = new Result();

        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
        }

        ArrangementsBusinessDelegate AccountsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);

        ArrangementsDTO inputPayloadDTO = new ArrangementsDTO();

        List<ArrangementsDTO> accountsDTO = null;
        inputPayloadDTO.setCustomerType(customerType);
        
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        String mortgageString = null;
        if(Account_id.equalsIgnoreCase("MORT12345") || Account_id.equalsIgnoreCase("122877")|| Account_id.equalsIgnoreCase("122878")) {
            
            try {          mortgageString = Executor.invokePassThroughServiceAndGetString((InfinityServices)ArrangementsAPIServices.MOCKMORTGAGEMS_GETMORTGAGEDETAILS, inputMap, headerMap);
            alert.prepareError("AMS Response getDetails" + mortgageString).log();
          } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
          } 
          JSONArray mortgages = new JSONArray(mortgageString);
          List<ArrangementsDTO> mDTOs = new ArrayList<>();
          ArrangementsDTO mDTO = new ArrangementsDTO();
          JsonObject priAccHolder = new JsonObject();
          for(int i=0;i<mortgages.length();i++) {
        	  JSONObject mortgage = mortgages.getJSONObject(i);
        	  if(mortgage.get("accountID").equals(Account_id)) {
        		  String accHolderString = mortgage.get("accountHolder").toString();
        		  JsonObject accHolder = new JsonParser().parse(accHolderString).getAsJsonObject();
        		  priAccHolder.addProperty("username", accHolder.has("username") ? accHolder.get("username").getAsString() :"");
                  priAccHolder.addProperty("fullname", accHolder.has("fullname") ? accHolder.get("fullname").getAsString() :"");
                  mDTO.setAccountHolder(priAccHolder.toString());
        		  mDTO.setAccount_id(mortgage.has("accountID") ? mortgage.get("accountID").toString() : "");
        		  mDTO.setIBAN(mortgage.has("IBAN") ? mortgage.get("IBAN").toString() : "");
        		  mDTO.setOriginalAmount(mortgage.has("originalAmount") ? mortgage.get("originalAmount").toString() : "");
        		  mDTO.setNextPaymentAmount(mortgage.has("nextPaymentAmount") ? mortgage.get("nextPaymentAmount").toString() : "");
        		  mDTO.setPaymentDue(mortgage.has("NextPaymentDue") ? mortgage.get("NextPaymentDue").toString() : "");
        		  mDTO.setOutstandingBalance(mortgage.has("outstandingBalance") ? mortgage.get("outstandingBalance").toString() : "");
        		  mDTO.setPaidInstallmentsCount(mortgage.has("paidInstallmentsCount") ? mortgage.get("paidInstallmentsCount").toString() : "");
        		  mDTO.setOverDueInstallmentsCount(mortgage.has("overDueInstallmentsCount") ? mortgage.get("overDueInstallmentsCount").toString() : "");
        		  mDTO.setFutureInstallmentsCount(mortgage.has("futureInstallmentsCount") ? mortgage.get("futureInstallmentsCount").toString() : "");
        		  mDTO.setInterestPaidYTD(mortgage.has("interestPaidYTD") ? mortgage.get("interestPaidYTD").toString() : "");
        		  mDTO.setLastPaymentAmount(mortgage.has("lastPaymentAmount") ? mortgage.get("lastPaymentAmount").toString() : "");
        		  mDTO.setLastPaymentDate(mortgage.has("lastPaymentDate") ? mortgage.get("lastPaymentDate").toString() : "");
        		  mDTO.setRePaymentFrequency(mortgage.has("rePaymentFrequency") ? mortgage.get("rePaymentFrequency").toString() : "");
        		  mDTO.setInterestRate(mortgage.has("interestRate") ? mortgage.get("interestRate").toString() : "");
        		  mDTO.setSanctionedDate(mortgage.has("sanctionedDate") ? mortgage.get("sanctionedDate").toString() : "");
        		  mDTO.setMaturityDate(mortgage.has("maturityDate") ? mortgage.get("maturityDate").toString() : "");
        		  mDTO.setTermAmount(mortgage.has("sanctionedAmount") ? mortgage.get("sanctionedAmount").toString() : "");
        		  mDTO.setAccountType(mortgage.has("accountType") ? mortgage.get("accountType").toString() : "");
        		  mDTO.setTypeDescription(mortgage.has("accountType") ? mortgage.get("accountType").toString() : "");

        		  mDTO.setAccountName(mortgage.has("accountName") ? mortgage.get("accountName").toString() : "");
        		  mDTO.setArrangementId(mortgage.has("arrangementId") ? mortgage.get("arrangementId").toString() : "");
        		  mDTO.setBankname(mortgage.has("bankName") ? mortgage.get("bankName").toString() : "");
        		  mDTO.setCompanyCode(mortgage.has("companyId") ? mortgage.get("companyId").toString() : "");
        		  mDTO.setCurrencyCode(mortgage.has("currencyCode") ? mortgage.get("currencyCode").toString() : "");
        		  mDTO.setOpeningDate(mortgage.has("openingDate") ? mortgage.get("openingDate").toString() : "");
        		  mDTO.setDisplayName(mortgage.has("displayName") ? mortgage.get("displayName").toString() : "");
        		  mDTO.setExternalIndicator(mortgage.has("externalIndicator") ? mortgage.get("externalIndicator").toString() : "");
        		  mDTO.setNickName(mortgage.has("nickName") ? mortgage.get("nickName").toString() : "");
        		  mDTO.setPrincipalValue(mortgage.has("principalValue") ? mortgage.get("principalValue").toString() : "");
        		  mDTO.setProcessingTime(mortgage.has("processingTime") ? mortgage.get("processingTime").toString() : "");
        		  mDTO.setProduct(mortgage.has("productId") ? mortgage.get("productId").toString() : "");
        		  mDTO.setStatusDesc(mortgage.has("statusDesc") ? mortgage.get("statusDesc").toString() : "");
        		  mDTO.setSupportBillPay(mortgage.has("supportBillPay") ? mortgage.get("supportBillPay").toString() : "");
        		  mDTO.setSupportChecks(mortgage.has("supportChecks") ? mortgage.get("supportChecks").toString() : "");
        		  mDTO.setSupportTransferFrom(mortgage.has("supportTransferFrom") ? mortgage.get("supportTransferFrom").toString() : "");
        		  mDTO.setSupportTransferTo(mortgage.has("supportTransferTo") ? mortgage.get("supportTransferTo").toString() : "");
        		  mDTO.setAvailableBalance(mortgage.has("availableBalance") ? Double.valueOf(mortgage.get("availableBalance").toString()).doubleValue() : 0.0);
        		  mDTO.setCurrentBalance(mortgage.has("currentBalance") ? Double.valueOf(mortgage.get("currentBalance").toString()).doubleValue() : 0.0);
        		  mDTO.setDividendLastPaidAmount(mortgage.has("dividendLastPaidAmount") ? Double.valueOf(mortgage.get("dividendLastPaidAmount").toString()).doubleValue() : 0.0);
        		  mDTO.setDividendPaidYTD(mortgage.has("dividendPaidYTD") ? Double.valueOf(mortgage.get("dividendPaidYTD").toString()).doubleValue() : 0.0);
        		  mDTO.setPendingDeposit(mortgage.has("pendingDeposit") ? Double.valueOf(mortgage.get("pendingDeposit").toString()).doubleValue() : 0.0);
        		  mDTO.setPendingWithdrawal(mortgage.has("pendingWithdrawal") ? Double.valueOf(mortgage.get("pendingWithdrawal").toString()).doubleValue() : 0.0);
        		  mDTOs.add(mDTO);
        	  }
          }
        	JSONArray accountsDTOArray = new JSONArray(mDTOs);

            JSONObject responseObj = new JSONObject();
            responseObj.put("Accounts", accountsDTOArray);
            result = JSONToResult.convert(responseObj.toString());
            return result;
            }
          

       // String CompanyId = ArrangementsUtils.getUserAttributeFromIdentity(request, TemenosConstants.COMPANYID);
        String CompanyId = CommonUtils.getCompanyId(request);
        try {
            if (StringUtils.isNotBlank(customerType)) {
                    String AccIdWithCompany = StringUtils.EMPTY;
                    String featureActionsInCache = (String)MemoryManager.getFromCache(
        					DBPUtilitiesConstants.ACCOUNTS_POSTLOGIN_CACHE_KEY + CompanyId);
                    alert.prepareError("featureActionsInCache" + featureActionsInCache).log();  
                    try {
                        if (Account_id.contains("-")) {
                            AccIdWithCompany = Account_id;
                        } else {
                            AccIdWithCompany = ArrangementsUtils.getAccountIdWithCompanyFromCache(Account_id, request);
                            
                            
                            
                        }
                    } catch (Exception e) {
                        alert.prepareError("Unable to fetch account id with company from cache" + e).log();
                        return ErrorCodeEnum.ERR_20056.setErrorCode(new Result());
                    }
                    if (StringUtils.isNotBlank(AccIdWithCompany))
                        inputPayloadDTO.setAccount_id(AccIdWithCompany);
                    if (StringUtils.isNotBlank(backendUserId)){
                        if (backendUserId.contains("-")) {
                            inputPayloadDTO.setBackendUserId(backendUserId);
                        } else {
                            CompanyId = StringUtils.isNotEmpty(CompanyId) ? CompanyId
                                    : ServerConfigurations.AMS_COMPANYID.getValue();
                            inputPayloadDTO.setBackendUserId(CompanyId + "-" + backendUserId);
                        }
                    }
                    accountsDTO = AccountsDelegateInstance.getArrangementOverview(inputPayloadDTO, request, authToken);
            } else {
                return ErrorCodeEnum.ERR_20047.setErrorCode(new Result());
            }
        } catch (Exception e) {
            alert.prepareError("Unable to fetch records " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
        JSONArray accountsDTOArray = new JSONArray(accountsDTO);

        JSONObject responseObj = new JSONObject();
        responseObj.put("Accounts", accountsDTOArray);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    @Override
    public Result getArrangementPreviewAccounts(String did, String userName, String backendUserId, String customerType,
            String productLine, String customerID, DataControllerRequest request, String authToken)
            throws ApplicationException {
        Result result = new Result();
        ArrangementsDTO inputPayloadDTO = new ArrangementsDTO();
        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
        }
        ArrangementsBusinessDelegate AccountsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);

        List<ArrangementsDTO> accountsDTO = null;
        if (StringUtils.isNotBlank(did) && StringUtils.isNotBlank(userName)) {
            inputPayloadDTO.setBackendUserId(backendUserId);
            inputPayloadDTO.setDeviceId(did);
            inputPayloadDTO.setCustomerID(customerID);
            accountsDTO = AccountsDelegateInstance.getBusinessUserArrangementPreview(inputPayloadDTO, request);
        }
        if (accountsDTO == null || accountsDTO.size() == 0) {
            JSONArray accountsDTOArray = new JSONArray();
            JSONObject responseObj = new JSONObject();
            responseObj.put("Accounts", accountsDTOArray);
            result = JSONToResult.convert(responseObj.toString());
            result.getAllDatasets().get(0).setId("records");
            return result;
        } else {
            String CompanyId = ArrangementsUtils.getUserAttributeFromIdentity(request, TemenosConstants.COMPANYID);
            try {
                CompanyId = StringUtils.isNotEmpty(CompanyId) ? CompanyId
                        : ServerConfigurations.AMS_COMPANYID.getValue();
            } catch (Exception e) {
                alert.prepareError("Error while fetching companyId").log();
            }
            String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
            if (ARRANGEMENTS_BACKEND.equals("t24")) {
            	try {
            		HashMap<String, Object> headerParams = new HashMap<String, Object>();
            		HashMap<String, Object> inputParams = new HashMap<String, Object>();
            		inputParams.put("loginUserId","PreLogin-"+customerID);
            		request.addRequestParam_("loginUserId","PreLogin-"+customerID);
    				String accounts = DBPServiceExecutorBuilder.builder()
                            .withServiceId("ArrangementT24ISAccounts")
                            .withOperationId("getAccountsByCoreCustomerIdList")
                            .withRequestParameters(inputParams).withRequestHeaders(headerParams)
                            .withDataControllerRequest(request).build().getResponse();
    				JSONObject accJson = new JSONObject(accounts);
    				JSONArray accountsRes = accJson.getJSONArray("Accounts");
    				JSONObject records = new JSONObject();
    				records.put("records", accountsRes);
    				return JSONToResult.convert(records.toString());
                } catch (Exception e) {
                	alert.prepareError("Error in resource layer::",e).log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_20041);
                }
        	}
            else {
            result = getArrangementAccounts(backendUserId, customerType, customerID, productLine, null, CompanyId,
                    request, authToken);
            result.getAllDatasets().get(0).setId("records");
            return result;
            }
        }
    }
    private List<ArrangementsDTO> getMortgages() {
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        String mortgageString = null;
        try {          mortgageString = Executor.invokePassThroughServiceAndGetString((InfinityServices)ArrangementsAPIServices.MOCKMORTGAGEMS_FETCHMORTGAGEACCOUNT, inputMap, headerMap);
          alert.prepareError("AMS Response" + mortgageString).log();
        } catch (Exception e) {
          alert.prepareError("Unable to fetch Arrangements " + e).log();
        } 

        List<ArrangementsDTO> mortgagesDTOs = new ArrayList<>();
        JSONArray mortgages = new JSONArray(mortgageString);
        for(int i=0;i<mortgages.length();i++) {
        JSONObject mortgage = mortgages.getJSONObject(i);
        ArrangementsDTO mortgageDTO = new ArrangementsDTO();
        String companyId = mortgage.has("company") ? mortgage.getJSONObject("company").getString("companyReference") : "";
        String aid = mortgage.has("linkedReference") ? mortgage.getString("linkedReference") : "";
        String accountId = aid.replace(companyId + "-", "");
        mortgageDTO.setAccount_id(accountId);
        mortgageDTO.setAccountType("mortgageFacility");
        mortgageDTO.setTypeDescription("mortgageFacility");
        mortgageDTO.setCompanyCode(companyId);
        mortgageDTO.setCurrencyCode(mortgage.has("currency") ? mortgage.getString("currency") : "");
        mortgageDTO.setProductGroup(mortgage.has("productGroup") ? mortgage.getString("productGroup") : "");
        mortgageDTO.setProduct(mortgage.has("product") ? mortgage.getString("product") : "");
        mortgageDTO.setArrangementId(accountId);
        mortgageDTO.setServiceType(mortgage.has("productDescription") ? mortgage.getString("productDescription") : "");
        mortgageDTO.setAccountName(mortgage.has("shortTitle") ? mortgage.getString("shortTitle") : "");
        mortgageDTO.setPartyRole("Owner");
        mortgageDTO.setOutstandingBalance(mortgage.has("outstandingBalance") ? mortgage.getString("outstandingBalance") : "");
        mortgageDTO.setProcessingTime(mortgage.has("processDate") ? mortgage.getString("processDate"):"");
        mortgageDTO.setNickName(mortgage.has("shortTitle") ? mortgage.getString("shortTitle") : "");
        mortgagesDTOs.add(mortgageDTO);
        }
        return mortgagesDTOs;
      }

    @Override
    public Result getUserDetailsFromDBX(String userName, DataControllerRequest request) throws ApplicationException {
        Result result = new Result();
        ArrangementsDTO inputPayloadDTO = new ArrangementsDTO();

        ArrangementsBusinessDelegate AccountsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);

        List<ArrangementsDTO> accountsDTO = null;
        inputPayloadDTO.setUserName(userName);
        accountsDTO = AccountsDelegateInstance.getUserDetailsFromDBX(inputPayloadDTO, request);
        JSONArray accountsDTOArray = new JSONArray(accountsDTO);

        JSONObject responseObj = new JSONObject();
        responseObj.put("Accounts", accountsDTOArray);
        result = JSONToResult.convert(responseObj.toString());
        result.getAllDatasets().get(0).setId("records");
        return result;
    }

    @Override
    public Result getAccountDetailsForCombinedStatements(String accountID, String customerType, String authToken,
            String companyId, DataControllerRequest request)
            throws Exception {

        ArrangementsBusinessDelegate AccountsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);

        String AccIdWithCompanyId = accountID;
        if (accountID != null && !accountID.contains("-")) {
            if (StringUtils.isNotBlank(companyId)) {
                AccIdWithCompanyId = companyId + "-" + accountID;
            } else {
                try {
                    AccIdWithCompanyId = ArrangementsUtils.getAccountIdWithCompanyFromCache(accountID, request);
                } catch (Exception e) {
                    alert.prepareError("Unable to get account with company id from cache " + e).log();
                    return ErrorCodeEnum.ERR_20046.setErrorCode(new Result());
                }
            }

        }
        JSONObject accounDetails = new JSONObject();
        
    	String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
    	if (ARRANGEMENTS_BACKEND.equals("t24")) {	 
     //   if (!customerType.equals("TYPE_ID_RETAIL") && !customerType.equals("TYPE_ID_PROSPECT")) {
            accounDetails = AccountsDelegateInstance.getAccountDetailForCombinedStatementsfromT24(accountID,
                    customerType, authToken);

        } else {
            accounDetails = AccountsDelegateInstance.getAccountDetailForCombinedStatements(AccIdWithCompanyId,
                    customerType, authToken);
        }
        Result result = JSONToResult.convert(accounDetails.toString());
        return result;
    }

	@Override
	public Result getSimulationResults(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		ArrangementsBusinessDelegate SimulatedResultsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);
		Result simultedResults = SimulatedResultsDelegateInstance.getSimulatedResults(methodID, inputArray, request, response);
		return simultedResults;
	}
	
	@Override
	public Result createAndGetPayOffSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		ArrangementsBusinessDelegate SimulatedResultsDelegateInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ArrangementsBusinessDelegate.class);
		Result simultedResults = SimulatedResultsDelegateInstance.createAndGetPayOffSimulatedResults(methodID, inputArray, request, response);
		return simultedResults;
	}
	
	public Result getCoreCustomerIdsAndAccounts(DataControllerRequest request,String customerId) throws ApplicationException{
		Result result = new Result();
		if (StringUtils.isNotBlank(customerId)) {
			
			String REDESIGN_CUSTID = ServerConfigurations.REDESIGN_CUSTID.getValueIfExists();
	        if (customerId.equals(REDESIGN_CUSTID)) {
	        	try {
	        		HashMap<String, Object> headerParams = new HashMap<String, Object>();
	        		HashMap<String, Object> inputParams = new HashMap<String, Object>();
					String accounts = DBPServiceExecutorBuilder.builder()
	                        .withServiceId("mockServices")
	                        .withOperationId("getCoreCustomerIdsAndAccounts")
	                        .withRequestParameters(inputParams).withRequestHeaders(headerParams)
	                        .withDataControllerRequest(request).build().getResponse();
					Result resultMock = JSONToResult.convert(accounts);
					resultMock.addStringParam("reDesignFlow", "true");
					return resultMock;
	            } catch (Exception e) {
	            	alert.prepareError(e.toString()).log();            
	                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
	            }
	        }
	        
	        
            Map<String, Object> inputParams = new HashMap<String, Object>();
            String companyLegalUnit = CommonUtils.getCompanyId(request);
            inputParams.put("customerId",customerId);
            inputParams.put("companyLegalUnit",companyLegalUnit );
            request.addRequestParam_("customerId",customerId);
            request.addRequestParam_("companyLegalUnit",companyLegalUnit);
            try {
				String contractCustomersString = DBPServiceExecutorBuilder.builder()
				        .withServiceId(ArrangementsAPIServices.GETCORECUSTOMERIDSANDACCOUNTS.getServiceName())
				        .withOperationId(ArrangementsAPIServices.GETCORECUSTOMERIDSANDACCOUNTS.getOperationName())
				        .withRequestParameters(inputParams).withRequestHeaders(request.getHeaderMap())
				        .withDataControllerRequest(request)
				        .build().getResponse();
				
				JSONObject contractCustomers = new JSONObject(contractCustomersString);
				JSONArray coreCust = contractCustomers.getJSONArray("records");
				int accCount = 0;
				    String reDesignFlow="false";
                String showUpgradePopup = "";
                if (coreCust.length()>1) {
                    for (int i=0;i<coreCust.length();i++) {
                        JSONObject core = coreCust.getJSONObject(i);
                        accCount+=Integer.parseInt(core.getString("accountsCount"));
                    }
                    
                    AccountsCountProperties accountsCountProperties = new AccountsCountProperties(request);
                    int count = Integer.parseInt(AccountsCountProperties.getValue("config_value"));
                    if(count <= accCount) {
                        result = JSONToResult.convert(contractCustomers.toString());
                        reDesignFlow="true";
                    }
                }
                if (StringUtils.isNotBlank(customerId)) {
                    String filter = "id" + DBPUtilitiesConstants.EQUAL + customerId;
                    Result user = HelperMethods.callGetApi(request, filter, HelperMethods.getHeaders(request),
                                URLConstants.CUSTOMER_GET);
                    String   isHeavyUser=  HelperMethods.getFieldValue(user, "isHeavyUser");
                    //  Boolean  isHeavyUserValue = Boolean.valueOf(isHeavyUser);                       
                    if( Boolean.valueOf(isHeavyUser) == false && Boolean.valueOf(reDesignFlow)==true)
                    {
                        showUpgradePopup="upgrade";
                          deleteCustomViews(request, customerId);
                    }
                    else if( Boolean.valueOf(isHeavyUser) == true && Boolean.valueOf(reDesignFlow)==false)
                    {
                        showUpgradePopup="downgrade";
                          deleteCustomViews(request, customerId);
                    }
                    else
                        showUpgradePopup="nochange";    
                    Map<String, String> updateInput = new HashMap<>();
                    updateInput.put("id",customerId);
                    updateInput.put("isHeavyUser", reDesignFlow);
                    diagnostic.prepareDebug("input data for update default entity : "+updateInput).log();
                    HelperMethods.callApiAsync(request, updateInput, HelperMethods.getHeaders(request), URLConstants.CUSTOMER_UPDATE);
                    
               }
               
			   result.addStringParam("reDesignFlow", reDesignFlow);
			   result.addStringParam("showUpgradePopup",showUpgradePopup);
			   return result;				
			} catch (DBPApplicationException | HttpCallException e) {
				// TODO Auto-generated catch block
				alert.prepareError(e.getMessage()).log();
			}
		}
		return result;
	}
	
	private void deleteCustomViews(DataControllerRequest request, String customerId) throws HttpCallException {
	    String filter = "customerId eq " + customerId;
	    Result user = HelperMethods.callGetApi(request, filter, HelperMethods.getHeaders(request), URLConstants.CUSTOMVIEW_DELETE);
	  }
	
	public Result updateCoreCustomerFavoriteStatus(DataControllerRequest request,String customerId) throws ApplicationException{
		Result result = new Result();
		if (StringUtils.isNotBlank(customerId)) {
			
            Map<String, Object> inputParams = new HashMap<String, Object>();
            String companyLegalUnit = CommonUtils.getCompanyId(request);
            String coreCustomerId = request.getParameter("coreCustomerId");
            String favoriteStatus = request.getParameter("favoriteStatus");
            inputParams.put("customer_Id",customerId);
            inputParams.put("legalEntityId",companyLegalUnit );
            inputParams.put("coreCustomer_Id", coreCustomerId);
            inputParams.put("fav_status", favoriteStatus);
            
            
            request.addRequestParam_("customer_Id",customerId);
            request.addRequestParam_("legalEntityId",companyLegalUnit);
            request.addRequestParam_("coreCustomer_Id",coreCustomerId);
            request.addRequestParam_("fav_status",favoriteStatus);
            
            diagnostic.prepareDebug("Request to update core customer fav status : "+customerId+","+companyLegalUnit+","+coreCustomerId+","+favoriteStatus).log();
            
            try {
				String responseString = DBPServiceExecutorBuilder.builder()
				        .withServiceId(ArrangementsAPIServices.UPDATE_CORECUSTOMERID_FAV_STATUS.getServiceName())
				        .withOperationId(ArrangementsAPIServices.UPDATE_CORECUSTOMERID_FAV_STATUS.getOperationName())
				        .withRequestParameters(inputParams).withRequestHeaders(request.getHeaderMap())
				        .withDataControllerRequest(request)
				        .build().getResponse();
				diagnostic.prepareDebug("Response from update core customer fav status : "+responseString).log();
				
			} catch (DBPApplicationException e) {
				// TODO Auto-generated catch block
				alert.prepareError(e.getMessage()).log();
				alert.prepareError("update core customer fav status failed: "+e.getMessage()).log();
			}
		}
		return result;
		
	}
	
	
	
	
	

	

	@Override
	public Result getAccountLevelPermissionsforCoreCustomerIds(DataControllerRequest request, String customerId) throws HttpCallException, ApplicationException {
		
		  Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
	        String loginUserId = null;
	        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
	            loginUserId = HelperMethods.getCustomerIdFromSession(request);
	     	        }
	        if(StringUtils.isNotBlank(loginUserId)) {
	         try {
				return   addAccountLevelPermissionsandUpdateCacheArray(request,loginUserId,customerId);
			} catch (HttpCallException | ApplicationException | com.kony.dbp.exception.ApplicationException e) {
				alert.prepareError(e.getMessage()).log();
			}
		
		
		
		
	}
			return null;
	
	
	}

	private Result addAccountLevelPermissionsandUpdateCacheArray(DataControllerRequest request,String loginUserId, String customerIds) throws HttpCallException, ApplicationException, com.kony.dbp.exception.ApplicationException {
		
		// TODO Auto-generated method stub
		String[] customerIdList = customerIds.split(" ");
		 Set<String> customerIdListSet = new LinkedHashSet<>(Arrays.asList(customerIdList));
				JsonArray accountList= null;
		ArrayList<Map<String, String>> resultset=getServiceDefinitionsAndContractsAndCustomerGroups(request,loginUserId, customerIdListSet);
		  Map<String, String> serviceDefinitions = resultset.get(0);
			Map<String, String> contracts = resultset.get(1);
			Map<String, String> customerGroups = resultset.get(2);
			
			
		
		Result result = getCoreCustomerAccountsDetails(request,loginUserId, customerIdListSet,accountList,serviceDefinitions,contracts,customerGroups);
		
		
		return result;
		
	}

	private ArrayList<Map<String, String>> getServiceDefinitionsAndContractsAndCustomerGroups(DataControllerRequest request, String customerId,
			Set<String> customerIdListSet) throws HttpCallException {
		
		Map<String, String> serviceDefinitions = new HashMap<>();
		Map<String, String> contracts = new HashMap<>();
		Map<String, String> customerGroups = new HashMap<>();
		Map<String, String> serviceDefinition = new HashMap<String, String>();
		
		for (String coreCustomerId : customerIdListSet) {
			if (!serviceDefinitions.containsKey(coreCustomerId)) {
				String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId;
				Map<String, Object> input = new HashMap<String, Object>();
				input.put(DBPUtilitiesConstants.FILTER, filter);

				JsonObject response = com.kony.dbputilities.util.HelperMethods.callApiJson(request, input,
						com.kony.dbputilities.util.HelperMethods.getHeaders(request),
						URLConstants.CONTRACTCORECUSTOMER_GET);

				if (JSONUtil.isJsonNotNull(response)
						&& JSONUtil.hasKey(response, DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS)
						&& response.get(DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS).isJsonArray()) {

					JsonArray customers = JSONUtil.getJsonArrary(response, DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS);
					for (JsonElement element : customers) {
						JsonObject contractCoreCustomer = element.getAsJsonObject();
						String contractId = contractCoreCustomer.has(InfinityConstants.contractId)
								&& !contractCoreCustomer.get(InfinityConstants.contractId).isJsonNull()
										? contractCoreCustomer.get(InfinityConstants.contractId).getAsString()
										: null;
						if (StringUtils.isNotBlank(contractId) && !serviceDefinition.containsKey(contractId)) {
							contracts.put(coreCustomerId, contractId);
							filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId;
							input = new HashMap<String, Object>();
							input.put(DBPUtilitiesConstants.FILTER, filter);
							JsonObject contractResponse = com.kony.dbputilities.util.HelperMethods.callApiJson(request,
									input, com.kony.dbputilities.util.HelperMethods.getHeaders(request),
									URLConstants.CONTRACT_GET);
							if (JSONUtil.isJsonNotNull(contractResponse)
									&& JSONUtil.hasKey(contractResponse, DBPDatasetConstants.DATASET_CONTRACT)
									&& contractResponse.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
								JsonArray contractArray = contractResponse.get(DBPDatasetConstants.DATASET_CONTRACT)
										.getAsJsonArray();
								for (JsonElement contractelement : contractArray) {
									String serviceDefinitionId = JSONUtil.getString(contractelement.getAsJsonObject(),
											"servicedefinitionId");
									serviceDefinition.put(contractId, serviceDefinitionId);
								}
							}
						}
						serviceDefinitions.put(coreCustomerId, serviceDefinition.get(contractId));
					}
				}
			}

			if (!customerGroups.containsKey(coreCustomerId)) {
				String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId
						+ DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL
						+ customerId;
				Map<String, Object> input = new HashMap<String, Object>();
				input.put(DBPUtilitiesConstants.FILTER, filter);
				JsonObject jsonResponse = com.kony.dbputilities.util.HelperMethods.callApiJson(request, input,
						com.kony.dbputilities.util.HelperMethods.getHeaders(request), URLConstants.CUSTOMER_GROUP_GET);
				if (jsonResponse.has(DBPDatasetConstants.DATASET_CUSTOMERGROUP)) {
					JsonElement jsonElement = jsonResponse.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP);
					if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
						JsonArray jsonArray = jsonElement.getAsJsonArray();
						JsonObject contract = jsonArray.get(0).getAsJsonObject();
						String groupId = contract.has(InfinityConstants.Group_id)
								&& !contract.get(InfinityConstants.Group_id).isJsonNull()
										? contract.get(InfinityConstants.Group_id).getAsString()
										: null;
						customerGroups.put(coreCustomerId, groupId);
					}
				}
			}
			
					
		}
		
		ArrayList<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
		result.add(serviceDefinition);
		result.add(contracts);
		result.add(customerGroups);
		return result;
		
		
		// TODO Auto-generated method stub
		
	}
	
	private Result getCoreCustomerAccountsDetails(DataControllerRequest request,String customerId, Set<String> customerIdListSet, JsonArray accountList, Map<String, String> serviceDefinitions, Map<String, String> contracts, Map<String, String> customerGroups) throws HttpCallException, ApplicationException, com.kony.dbp.exception.ApplicationException {
		
		  Map<String, Map<String, String>> accountDetailsMap = new HashMap<>();
	        if (!customerIdListSet.isEmpty()) {

	            StringBuilder coreCustomersListCSV = new StringBuilder();
	            Map<String, Object> input = new HashMap<String, Object>();

	            for (String corecustomerId : customerIdListSet) {
	                if (StringUtils.isBlank(coreCustomersListCSV)) {
	                    coreCustomersListCSV.append(corecustomerId);
	                } else {
	                    coreCustomersListCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(corecustomerId);
	                }
	                
	               

	            }
	            input.put("_coreCustomerIdList", coreCustomersListCSV.toString());
	            input.put("_customerId", customerId);

	            JsonObject response =
	                    com.kony.dbputilities.util.HelperMethods.callApiJson(request, input,
	                            com.kony.dbputilities.util.HelperMethods.getHeaders(request),
	                            URLConstants.CORECUSTOMER_ACCOUNTS_DETAILS_GET_PROC);

	            if (JSONUtil.isJsonNotNull(response)
	                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS) &&
	                    response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {

	                JsonArray accountDetails = JSONUtil.getJsonArrary(response, DBPDatasetConstants.DATASET_RECORDS);
	                accountList = JSONUtil.getJsonArrary(response, DBPDatasetConstants.DATASET_RECORDS);
	                for (JsonElement element : accountDetails) {
	                    JsonObject accountRecord = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
	                    String accountId = JSONUtil.getString(accountRecord, InfinityConstants.accountId);
	                    String coreCustomerId = JSONUtil.getString(accountRecord, InfinityConstants.coreCustomerId);
	                    String coreCustomerName = JSONUtil.getString(accountRecord, InfinityConstants.coreCustomerName);
	                                      Map<String, String> detailsMap = new HashMap<String, String>();
	                    detailsMap.put(InfinityConstants.coreCustomerId, coreCustomerId);
	                    detailsMap.put(InfinityConstants.coreCustomerName, coreCustomerName);
	    
	                    accountDetailsMap.put(accountId, detailsMap);

	                }
	            }
	        }
	   Result result   =  getAccountActions(request,customerId, customerIdListSet ,accountDetailsMap ,accountList,serviceDefinitions,contracts,customerGroups);
	   result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		 JsonObject cacheJson = new JsonObject();
		    cacheJson.add("Accounts", (JsonElement)accountList);
		 MemoryManager.saveIntoCache("ACCOUNTS" + customerId + "_" + customerIdListSet.toArray()[0], cacheJson
			        .toString(), 1800);

	        return result;
		
		
	}
	
	 private String convertHasetToJsonArrayString(Set<String> addedActions) {
	        JsonArray actionsList = new JsonArray();
	        for (String action : addedActions) {
	            actionsList.add(action);
	        }
	        return actionsList.toString();
	    }

	private Result getAccountActions(DataControllerRequest request, String customerId, Set<String> customerIdListSet, Map<String, Map<String, String>> accountsDetails, JsonArray accountList,Map<String, String> serviceDefinitions, Map<String, String> contracts, Map<String, String> customerGroups) throws HttpCallException, ApplicationException, com.kony.dbp.exception.ApplicationException {
		// TODO Auto-generated method stub
		  Map<String, Set<String>> accountLevelActions = new HashMap<>();
		  Map<String, String> inputParams = new HashMap<>();
		  Set<String> usedCoreCustomers = new HashSet<>();
	        inputParams.put("_userId", customerId);

	        for (String coreCustomerId : customerIdListSet) {

	            if (!usedCoreCustomers.contains(coreCustomerId)) {
	                inputParams.put("_coreCustomerId", coreCustomerId);
	                JsonObject resultObject =
	                        com.kony.dbputilities.util.HelperMethods.callApiJson(request, inputParams,
	                                com.kony.dbputilities.util.HelperMethods.getHeaders(request),
	                                URLConstants.USER_ACCOUNTACTIONS_GET_PROC);

	                if (JSONUtil.isJsonNotNull(resultObject)
	                        && JSONUtil.hasKey(resultObject, DBPDatasetConstants.DATASET_RECORDS) &&
	                        resultObject.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
	                    usedCoreCustomers.add(coreCustomerId);
	                    JsonArray actionsArray = JSONUtil.getJsonArrary(resultObject, DBPDatasetConstants.DATASET_RECORDS);
	                    for (JsonElement element : actionsArray) {
	                        JsonObject actionRecord = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
	                        String accountId =
	                                actionRecord.has("Account_id") ? actionRecord.get("Account_id").getAsString() : "";
	                        String userId =
	                                actionRecord.has("Customer_id") ? actionRecord.get("Customer_id").getAsString() : "";
	                        String actionId =
	                                actionRecord.has("Action_id") ? actionRecord.get("Action_id").getAsString() : "";

	                        if (customerId.equalsIgnoreCase(userId) && StringUtils.isNotBlank(accountId)
	                                && StringUtils.isNotBlank(actionId) && StringUtils.isNotBlank(userId)) {
	                            if (accountLevelActions.containsKey(accountId)) {
	                                accountLevelActions.get(accountId).add(actionId);
	                            } else {
	                                Set<String> actions = new HashSet<>();
	                                actions.add(actionId);
	                                accountLevelActions.put(accountId, actions);
	                            }

	                        }
	                    }

	                }
	            }
	            getAccountLevelNewActions(customerId, contracts.get(coreCustomerId), coreCustomerId,
						customerGroups.get(coreCustomerId), serviceDefinitions.get(coreCustomerId),
						accountLevelActions, accountsDetails,request); 
			
	        }
	        

	     return   addPermissionsToAccountRecords(request, accountLevelActions, accountsDetails,accountList);
		
	}

	private void getAccountLevelNewActions(String customerId, String contractId, String coreCustomerId, String groupId,
			String serviceDefinitionId,
			Map<String, Set<String>> accountLevelActions, Map<String, Map<String, String>> accountsDetails, DataControllerRequest request) throws ApplicationException, com.kony.dbp.exception.ApplicationException {
		Map<String, Map<String, Map<String, Boolean>>> globalActions = new HashMap<String, Map<String, Map<String, Boolean>>>();
		Map<String, Map<String, Map<String, Map<String, Boolean>>>> accountActions = new HashMap<String, Map<String, Map<String, Map<String, Boolean>>>>();
		ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ContractBackendDelegate.class);
		String legalEntityId = null;
		try {
		    IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
		    Map<String, Object> userAttributes = identityHandler.getUserAttributes();
		   
		    if(userAttributes != null && userAttributes.size() >0) {
		    	legalEntityId = (String)userAttributes.get("legalEntityId");
			} 
				  else { legalEntityId = (String)userAttributes.get("companyId"); }
				 
		    
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    alert.prepareError(e.toString()).log();
		}
		/*
		 * FeatureActionLimitsDTO actionLimitsDTO =
		 * backendDelegate.getRestrictiveFeatureActionLimits( serviceDefinitionId, "",
		 * groupId, coreCustomerId, "",
		 * com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager.
		 * getHeadersHandler()), false, "");
		 */
		
		FeatureActionLimitsDTO actionLimitsDTO = backendDelegate.getRestrictiveFeatureActionLimits(
				serviceDefinitionId, "", groupId, coreCustomerId, "",
				request.getHeaderMap(),
				false, "",legalEntityId);
		
		
		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
				+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
				+ contractId + DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId
				+ DBPUtilitiesConstants.EQUAL + coreCustomerId;
		Map<String, String> input = new HashMap<String, String>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject response = new JsonObject();
		
		try {
			response = com.kony.dbputilities.util.HelperMethods.callApiJson(request,
					input, com.kony.dbputilities.util.HelperMethods.getHeaders(request),
					URLConstants.EXCLUDED_CUSTOMER_ACTION_LIMITS_GET);
		} catch (HttpCallException e) {
			
			alert.prepareError(e.toString()).log();
		}
		JsonArray jsonArray = new JsonArray();
		if (response.has(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION)) {
			JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				jsonArray = jsonElement.getAsJsonArray();
			}
		}

		accountActions.put(coreCustomerId,new HashMap<String, Map<String, Map<String, Boolean>>>());
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();

			String featureId = jsonObject2.has(InfinityConstants.featureId)
					&& !jsonObject2.get(InfinityConstants.featureId).isJsonNull()
							? jsonObject2.get(InfinityConstants.featureId).getAsString().trim()
							: null;
			String actionId = jsonObject2.has(InfinityConstants.Action_id)
					&& !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
							? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
							: null;
			if (StringUtils.isBlank(actionId)) {
				actionId = jsonObject2.has(InfinityConstants.action_id)
						&& !jsonObject2.get(InfinityConstants.action_id).isJsonNull()
								? jsonObject2.get(InfinityConstants.action_id).getAsString().trim()
								: null;
			}
			String accountId = jsonObject2.has(InfinityConstants.Account_id)
					&& !jsonObject2.get(InfinityConstants.Account_id).isJsonNull()
							? jsonObject2.get(InfinityConstants.Account_id).getAsString()
							: null;
			if (StringUtils.isBlank(accountId)) {
				accountId = jsonObject2.has(InfinityConstants.account_id)
						&& !jsonObject2.get(InfinityConstants.account_id).isJsonNull()
								? jsonObject2.get(InfinityConstants.account_id).getAsString()
								: null;
			}

			if (actionLimitsDTO.getGlobalLevelActions().contains(actionId)) {
				if (!globalActions.containsKey(coreCustomerId)) {
					globalActions.put(coreCustomerId, new HashMap<String, Map<String, Boolean>>());
				}
				if (!actionLimitsDTO.getFeatureaction().containsKey(featureId)) {
					continue;
				}
				if (!globalActions.get(coreCustomerId).containsKey(featureId)) {
					globalActions.get(coreCustomerId).put(featureId, new HashMap<String, Boolean>());
				}
				if (!actionLimitsDTO.getFeatureaction().get(featureId).contains(actionId)) {
					continue;
				}
				globalActions.get(coreCustomerId).get(featureId).put(actionId, true);
			} else {
				if (actionLimitsDTO.getAccountLevelActions().contains(actionId)) {
					if (!actionLimitsDTO.getFeatureaction().containsKey(featureId)
						|| !actionLimitsDTO.getFeatureaction().get(featureId).contains(actionId)
						|| !actionLimitsDTO.getAccountLevelActions().contains(actionId)) {
						continue;
					}
					if (!accountActions.get(coreCustomerId).containsKey(accountId)) {
						accountActions.get(coreCustomerId).put(accountId,
								new HashMap<String, Map<String, Boolean>>());
					}
					
					if (!accountActions.get(coreCustomerId).get(accountId).containsKey(featureId)) {
						accountActions.get(coreCustomerId).get(accountId).put(featureId,
								new HashMap<String, Boolean>());
					}
					accountActions.get(coreCustomerId).get(accountId).get(featureId).put(actionId, true);
				}
			}
		}
		
		if (actionLimitsDTO.getNewFeatureAction() != null) {
			Map<String, Set<String>> featureActions = actionLimitsDTO.getNewFeatureAction();
			for (String feature : featureActions.keySet()) {
				for (String action : featureActions.get(feature)) {
					if (actionLimitsDTO.getMonetaryActions().contains(action)
							|| actionLimitsDTO.getAccountLevelActions().contains(action)) {
						for (String accountId : accountActions.get(coreCustomerId).keySet()) {
							if(!accountActions.get(coreCustomerId).get(accountId).containsKey(feature)
									|| !accountActions.get(coreCustomerId).get(accountId).get(feature).containsKey(action)) {
								accountLevelActions.get(accountId).add(action);
							}
						}
					}
				}
			}
		}

		Callable<Result> callable = new Callable<Result>() {
			public Result call() {
				try {
					createCustomerActionEntries(customerId, contractId, coreCustomerId, serviceDefinitionId,
							groupId, request, actionLimitsDTO, globalActions, accountActions);
				} catch (Exception e) {
				}
				return new Result();
			}

			private void createCustomerActionEntries(String customerId, String contractId, String coreCustomerId,
					String serviceDefinitionId, String groupId, DataControllerRequest request,
					FeatureActionLimitsDTO featureActionDTO, Map<String, Map<String, Map<String, Boolean>>> globalActions, Map<String, Map<String, Map<String, Map<String, Boolean>>>> accountActions) {
				
				Map<String, String> input = new HashMap<String, String>();
				input.put(DBPUtilitiesConstants.FILTER, filter);
				JsonObject response = new JsonObject();
				try {
					response = com.kony.dbputilities.util.HelperMethods.callApiJson(request,
							input, com.kony.dbputilities.util.HelperMethods.getHeaders(request),
							URLConstants.CUSTOMER_ACTION_LIMITS_GET);
				} catch (HttpCallException e) {
					
					alert.prepareError(e.toString()).log();
				}
				JsonArray jsonArray = new JsonArray();
				if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
					JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
					if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
						jsonArray = jsonElement.getAsJsonArray();
					}
				}

				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();

					String featureId = jsonObject2.has(InfinityConstants.featureId)
							&& !jsonObject2.get(InfinityConstants.featureId).isJsonNull()
									? jsonObject2.get(InfinityConstants.featureId).getAsString().trim()
									: null;
					String actionId = jsonObject2.has(InfinityConstants.Action_id)
							&& !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
									? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
									: null;
					if (StringUtils.isBlank(actionId)) {
						actionId = jsonObject2.has(InfinityConstants.action_id)
								&& !jsonObject2.get(InfinityConstants.action_id).isJsonNull()
										? jsonObject2.get(InfinityConstants.action_id).getAsString().trim()
										: null;
					}
					String accountId = jsonObject2.has(InfinityConstants.Account_id)
							&& !jsonObject2.get(InfinityConstants.Account_id).isJsonNull()
									? jsonObject2.get(InfinityConstants.Account_id).getAsString()
									: null;
					if (StringUtils.isBlank(accountId)) {
						accountId = jsonObject2.has(InfinityConstants.account_id)
								&& !jsonObject2.get(InfinityConstants.account_id).isJsonNull()
										? jsonObject2.get(InfinityConstants.account_id).getAsString()
										: null;
					}

					if (featureActionDTO == null) {
						continue;
					}
					
					if (!featureActionDTO.getFeatureaction().containsKey(featureId)
							|| !featureActionDTO.getFeatureaction().get(featureId).contains(actionId)) {
							continue;
						}

					if (featureActionDTO.getGlobalLevelActions().contains(actionId)) {
						if (!globalActions.containsKey(coreCustomerId)) {
							globalActions.put(coreCustomerId, new HashMap<String, Map<String, Boolean>>());
						}
						if (!globalActions.get(coreCustomerId).containsKey(featureId)) {
							globalActions.get(coreCustomerId).put(featureId, new HashMap<String, Boolean>());
						}
						globalActions.get(coreCustomerId).get(featureId).put(actionId, true);
					} else {
						if (featureActionDTO.getAccountLevelActions().contains(actionId)) {
							
							if (!accountActions.containsKey(coreCustomerId)) {
								accountActions.put(coreCustomerId,
										new HashMap<String, Map<String, Map<String, Boolean>>>());
							}
							if (!accountActions.get(coreCustomerId).containsKey(accountId)) {
								accountActions.get(coreCustomerId).put(accountId,
										new HashMap<String, Map<String, Boolean>>());
							}
							if (!accountActions.get(coreCustomerId).get(accountId).containsKey(featureId)) {
								accountActions.get(coreCustomerId).get(accountId).put(featureId,
										new HashMap<String, Boolean>());
							}
							accountActions.get(coreCustomerId).get(accountId).get(featureId).put(actionId, true);
						}
					}
				}
				

				input = new HashMap<String, String>();
				String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId
						+ DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL
						+ customerId + DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
						+ contractId ;
				input.put(DBPUtilitiesConstants.FILTER, filter);
				try {
					response = com.kony.dbputilities.util.HelperMethods.callApiJson(request,
							input, com.kony.dbputilities.util.HelperMethods.getHeaders(request),
							URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_GET);
				} catch (HttpCallException e) {
					
					alert.prepareError(e.toString()).log();
				} 
				Map<String, Map<String, Double>> limitsMap = new HashMap<String, Map<String,Double>>();
				Map<String, Map<String, String>> limitsIds = new HashMap<String, Map<String,String>>();
				if (response.has(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS)) {
					JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS);
					if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
						for (JsonElement element : jsonElement.getAsJsonArray()) {
							String limitTypeId = element.getAsJsonObject().get(InfinityConstants.LimitType_id)
									.getAsString();
							String limitGroupId = element.getAsJsonObject().get(InfinityConstants.limitGroupId)
									.getAsString();
							String value = element.getAsJsonObject().get(InfinityConstants.value).getAsString();
							
							String id = element.getAsJsonObject().get(InfinityConstants.id).getAsString();
							
							if(StringUtils.isNotBlank(limitGroupId)) {
								if (!limitsMap.containsKey(limitGroupId)) {
									limitsMap.put(limitGroupId, new HashMap<String, Double>());
								}
								if (!limitsIds.containsKey(limitGroupId)) {
									limitsIds.put(limitGroupId, new HashMap<String, String>());
								}
								
								limitsIds.get(limitGroupId).put(limitTypeId, id);
								
								try{
									limitsMap.get(limitGroupId).put(limitTypeId, Double.parseDouble(value));
								}catch (Exception e) {
								}
							}
						}
					}
				}

				if (featureActionDTO.getNewFeatureAction() != null) {
					Map<String, Set<String>> featureActions = featureActionDTO.getNewFeatureAction();
					ActionLimitsDTO dto = new ActionLimitsDTO();
					for (String feature : featureActions.keySet()) {
						for (String action : featureActions.get(feature)) {
							dto = new ActionLimitsDTO();
							dto.setContractId(contractId);
							dto.setCustomerId(customerId);
							dto.setCoreCustomerId(coreCustomerId);
							dto.setFeatureId(feature);
							dto.setActionId(action);
							dto.setRoleId(groupId);
							dto.setAccountLevel(featureActionDTO.getAccountLevelActions().contains(action));
							dto.setMonetory(featureActionDTO.getMonetaryActions().contains(action));

							if (featureActionDTO.getGlobalLevelActions().contains(action)) {
								if (globalActions.containsKey(coreCustomerId) 
										&& globalActions.get(coreCustomerId).containsKey(feature)
										&& globalActions.get(coreCustomerId).get(feature).containsKey(action)) {
									continue;
								}
								
								addActions(dto, request.getHeaderMap());
							} else if (featureActionDTO.getAccountLevelActions().contains(action)) {
								for (String accountId : accountLevelActions.keySet()) {
									dto.setAccountId(accountId);
									
									if(!coreCustomerId.equals(accountsDetails.get(accountId).get(InfinityConstants.coreCustomerId))) {
										continue;
									}
									if (accountActions.containsKey(coreCustomerId) && 
											accountActions.get(coreCustomerId).containsKey(accountId)
											&& accountActions.get(coreCustomerId).get(accountId).containsKey(feature)
											&& accountActions.get(coreCustomerId).get(accountId).get(feature)
											.containsKey(action)) {
										continue;
									}

									if(!featureActionDTO.getMonetaryActions().contains(action)) {
										addActions(dto, request.getHeaderMap());
									}
								
									if(featureActionDTO.getMonetaryActions().contains(action) && featureActionDTO.getNewMonetaryActionLimits().containsKey(feature)
											&& featureActionDTO.getNewMonetaryActionLimits().get(feature).containsKey(action)) {

										Map<String, String> limitMap = featureActionDTO.getNewMonetaryActionLimits()
												.get(feature).get(action);
										
										String limitGroupId = featureActionDTO.getActionsInfo().get(action)
												.get(InfinityConstants.limitGroupId).getAsString();
										dto.setLimitGroupId(limitGroupId);
										for (String limitTypeId : limitMap.keySet()) {
											double value = Double.parseDouble((limitMap.containsKey(limitTypeId)
													&& StringUtils.isNotBlank(limitMap.get(limitTypeId)))
															? limitMap.get(limitTypeId)
															: "0.0");
											if(!limitsMap.containsKey(limitGroupId)) {
												limitsMap.put(limitGroupId, new HashMap<String, Double>());
											}
											if(!limitsMap.get(limitGroupId).containsKey(limitTypeId)) {
												limitsMap.get(limitGroupId).put(limitTypeId, Double.valueOf("0.0"));
											}
											
//												if (limitTypeId.equals(InfinityConstants.DAILY_LIMIT)) {
//													dto.setDailyLimitValue(value);
//													limitsMap.get(limitGroupId).put(limitTypeId, limitsMap.get(limitGroupId).get(limitTypeId)+value);
//												} else if (limitTypeId.equals(InfinityConstants.WEEKLY_LIMIT)) {
//													dto.setWeeklyLimitValue(value);
//													limitsMap.get(limitGroupId).put(limitTypeId, limitsMap.get(limitGroupId).get(limitTypeId)+value);
//												} else if (limitTypeId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
//													dto.setMaxTransactionLimitValue(value);
//													limitsMap.get(limitGroupId).put(limitTypeId, Math.max(limitsMap.get(limitGroupId).get(limitTypeId), value));
//												}
										}
										addActions(dto, request.getHeaderMap());
									}
								}
							}
						}
					}
				}
				
				updateLimitGroups(request, customerId, coreCustomerId, contractId, limitsMap, limitsIds);
				
				
				
				
			}

			private void updateLimitGroups(DataControllerRequest request, String customerId,
					String coreCustomerId, String contractId, Map<String, Map<String, Double>> limitsMap,
					Map<String, Map<String, String>> limitsIds) {
				
				
				Map<String, String> input = new HashMap<String, String>();
				
				for(String limitGroupId : limitsMap.keySet()) {
					
					for(String limitTypeId : limitsMap.get(limitGroupId).keySet()) {
						input = new HashMap<String, String>();
						input.put(InfinityConstants.Customer_id, customerId);
						input.put(InfinityConstants.contractId, contractId);
						input.put(InfinityConstants.coreCustomerId, coreCustomerId);
						input.put(InfinityConstants.limitGroupId, limitGroupId);
						input.put(InfinityConstants.LimitType_id, limitTypeId);
						input.put(InfinityConstants.value, limitsMap.get(limitGroupId).get(limitTypeId)+"");
						
						if(!limitsIds.containsKey(limitGroupId) || !limitsIds.get(limitGroupId).containsKey(limitTypeId)) {
							input.put(InfinityConstants.id, limitsIds.get(limitGroupId).get(limitTypeId));
							try {
								com.kony.dbputilities.util.HelperMethods.callApiJson(request,
										input, com.kony.dbputilities.util.HelperMethods.getHeaders(request),
										URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_CREATE);
							} catch ( Exception e) {
								alert.prepareError("Error occured",e).log();
							} 
						}
						else {
							input.put(InfinityConstants.id, com.kony.dbputilities.util.HelperMethods.getNewId());
							try {
								/*com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager,
										input, com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
										URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_UPDATE);*/
							} catch ( Exception e) {
								alert.prepareError("Error occured",e).log();
							} 
						}
					}
				}
			}

			private void addActions(ActionLimitsDTO dto, Map<String, Object> headerMap) {
				LimitsAndPermissionsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(LimitsAndPermissionsBackendDelegate.class);
				backendDelegate.addActionsToCustomer(dto, headerMap);
			}
		};
		try {
			ThreadExecutor.getExecutor().execute(callable);
		} catch (Exception e) {
			alert.prepareError("ThreadExecutor : Exception occured while adding new featureActions ", e).log();
		}
		

		
			
	}

	private Result addPermissionsToAccountRecords(DataControllerRequest request,
			Map<String, Set<String>> accountLevelActions, Map<String, Map<String, String>> accountsDetails, JsonArray accountsJson) {
		
		JsonArray accountsArray = accountsJson;
	    List<ArrangementsDTO> accountsDTO = new ArrayList<>();
	    String mockAccountClosureList = ServerConfigurations.MOCK_ACCOUNT_CLOSURE_LIST.getValueIfExists();
	    for (JsonElement accountObject : accountsArray) {
	    	 ArrangementsDTO accountDTO = new ArrangementsDTO();
	      JsonObject account = accountObject.isJsonObject() ? accountObject.getAsJsonObject() : new JsonObject();
	      String accountId = account.has("Account_id") ? account.get("Account_id").getAsString() : account.get("accountId").getAsString();
	      if (StringUtils.isNotBlank(accountId)) {
	        account.addProperty("accountID", accountId);
	        account.addProperty("Account_id", accountId);
	        account.addProperty("account_id", accountId);
	        accountDTO.setAccount_id(accountId);
	        if (accountLevelActions.containsKey(accountId)) {
	          String actionsString = convertHasetToJsonArrayString(accountLevelActions.get(accountId));
	          account.addProperty("actions", actionsString);
	          accountDTO.setActions(actionsString);
	        } else if (!JSONUtil.hasKey(account, "actions")) {
	          account.addProperty("actions", "[]");
	        } 
	        if (accountsDetails != null && accountsDetails.containsKey(accountId)) {
	          Map<String, String> details = accountsDetails.get(accountId);
	          String membershipId = details.get("coreCustomerId");
	          String membershipName = details.get("coreCustomerName");
	          if (StringUtils.isNotBlank(membershipId)) {
	            account.addProperty("Membership_id", membershipId);
	            account.addProperty("coreCustomerId", membershipId);
	            accountDTO.setMembership_id(membershipId);
	          } 
	          if (StringUtils.isNotBlank(membershipName)) {
	            account.addProperty("MembershipName", membershipName);
	            account.addProperty("coreCustomerName", membershipName);
	          } 
	        } 
	      } 
	      accountsDTO.add(accountDTO);
	    } 
	    accountsJson = accountsArray;
	    JSONArray accountsDTOArray = new JSONArray(accountsDTO);
	    JSONObject responseObj = new JSONObject();
	    responseObj.put("Accounts", accountsDTOArray);
	    Result result = JSONToResult.convert(responseObj.toString());
	    return result;
		
	}
	
	@Override
public Result getLatestBalances(String Account_id,  String authToken)
		throws ApplicationException {

	Result result = new Result();
	ArrangementsBusinessDelegate arrangementsBusinessDelegateImpl = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ArrangementsBusinessDelegate.class);
     try {
		List<ArrangementsDTO> accountIdBasedArrangements =
		         arrangementsBusinessDelegateImpl.getLatestBalances(Account_id,authToken);
		 JSONArray accountsDTOArray = new JSONArray(accountIdBasedArrangements);

	        JSONObject responseObj =
	                new JSONObject();
	        responseObj.put("Accounts", accountsDTOArray);
	        result = JSONToResult.convert(responseObj.toString());
	        diagnostic.prepareDebug("Accounts Latest Balances " + accountsDTOArray.toString()).log();
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		alert.prepareError(e.toString()).log();
	}
     
	return result;
}
	

	
}
	
