package com.temenos.dbx.eum.product.contract.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.product.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoreCustomerBackendDelegateImpl implements CoreCustomerBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    /*
	 * This backenddelagate method is created to get the customer details from T24
	 * in bulk
	 */

	@Override
	public DBXResult searchCoreCustomers(List<MembershipDTO> membershipDTOs, Map<String, Object> headersMap)
			throws ApplicationException {
		DBXResult responseDTO = new DBXResult();
		if (membershipDTOs == null || membershipDTOs.size() == 0)
			return responseDTO;
		Map<String, Object> inputParams = new HashMap<>();
		/*Generating space */
		StringBuilder combinedids = new StringBuilder();
		String id = membershipDTOs.get(0).getId();
		String legalEntityId = membershipDTOs.get(0).getCompanyLegalUnit();
		for (MembershipDTO menbershipDTO : membershipDTOs) {
			combinedids.append(" " + menbershipDTO.getId());
		}
		inputParams.put("_id", id);
		inputParams.put("_combinedids", combinedids);
		inputParams.put("_legalEntityId", legalEntityId);
		JsonObject response = new JsonObject();
		try {
			String isIntegrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
			if (StringUtils.isNotBlank(isIntegrated) && isIntegrated.equalsIgnoreCase("true")) {
				response = searchCoreCustomerT24(inputParams, headersMap);
			}
			if (JSONUtil.hasKey(response, "records")) {
				if (response.get("records").getAsJsonArray().size() > 0) {
					responseDTO.setResponse(response.get("records").getAsJsonArray());
				}
			} else {
				alert.prepareError("CoreCustomerBackendDelegateImpl  :  Backend response is not appropriate " + response).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_10756);
			}
		} catch (Exception e) {
			alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers"
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10756);
		}
		return responseDTO;
	}

    
    public JsonObject searchCoreCustomerT24(Map<String, Object> procParams, Map<String, Object> headersMap) {
        addT24Headers(headersMap, (String) procParams.get("_id"), (String) procParams.get("_legalEntityId"));
        if (procParams.containsKey("_combinedids")) {
			procParams.put("_id", procParams.get("_combinedids"));
		}
        return ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISUSER_INTEGRATION_SERVICE,
                null, OperationName.SEARCH_CORE_CUSTOMER,
                procParams, headersMap);
    }
   
    public JsonObject getCoreRelativeCustomerT24(MembershipDTO membershipDTO, Map<String, Object> headersMap) {
        JsonObject response = new JsonObject();
        JsonArray records = new JsonArray();
        addT24Headers(headersMap, membershipDTO.getId(), membershipDTO.getCompanyLegalUnit());
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("partyId", StringUtils.isNotBlank(membershipDTO.getId()) ? membershipDTO.getId() : "");
        JsonObject partyRelationResponse =
                ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISUSER_INTEGRATION_SERVICE,
                        null, OperationName.GET_PARTY_RELATIONS,
                        inputParams, headersMap);
        if (partyRelationResponse != null && JSONUtil.hasKey(partyRelationResponse, DBPDatasetConstants.DATASET_RECORDS)
                && partyRelationResponse.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                && partyRelationResponse.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
            for (JsonElement jsonelement : partyRelationResponse.get(DBPDatasetConstants.DATASET_RECORDS)
                    .getAsJsonArray()) {
                if (StringUtils.isNotBlank(JSONUtil.getString(jsonelement.getAsJsonObject(), "coreCustomerId"))) {
                    inputParams.clear();
                    inputParams.put("_id", JSONUtil.getString(jsonelement.getAsJsonObject(), "coreCustomerId"));
                    JsonObject coreCustomerInfo =
                            ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISUSER_INTEGRATION_SERVICE,
                                    null, OperationName.SEARCH_CORE_CUSTOMER,
                                    inputParams, headersMap);
                    if (coreCustomerInfo != null
                            && JSONUtil.hasKey(coreCustomerInfo, DBPDatasetConstants.DATASET_RECORDS)
                            && coreCustomerInfo.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                            && coreCustomerInfo.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                        JsonObject coreCustomer = new JsonObject();
                        JsonObject customerInfo = coreCustomerInfo.get(DBPDatasetConstants.DATASET_RECORDS)
                                .getAsJsonArray().get(0).getAsJsonObject();
                        int sectorId = -1;
                        if(JSONUtil.hasKey(customerInfo,"sectorId") &&
                                StringUtils.isNotBlank(JSONUtil.getString(customerInfo,"sectorId")))
                                sectorId = Integer.parseInt(JSONUtil.getString(customerInfo,"sectorId"));
                        if (sectorId > 2000) continue;
                        for (Entry<String, JsonElement> entrySet : coreCustomerInfo
                                .get(DBPDatasetConstants.DATASET_RECORDS)
                                .getAsJsonArray().get(0)
                                .getAsJsonObject().entrySet()) {
                            coreCustomer.add(entrySet.getKey(), entrySet.getValue());
                        }
                        coreCustomer.addProperty("relationshipId",
                                JSONUtil.getString(jsonelement.getAsJsonObject(), "relationshipId"));
                        coreCustomer.addProperty("relationshipName",
                                JSONUtil.getString(jsonelement.getAsJsonObject(), "relationshipName"));
                        records.add(coreCustomer);
                    }
                }
            }
        }
        response.add(DBPDatasetConstants.DATASET_RECORDS, records);
        return response;
    }
    
    
    
       

    @Override
    public DBXResult getCoreCustomerAccounts(MembershipDTO membershipDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult responseDTO = new DBXResult();
        Map<String, String> integratedToBase = getAccountTypeMapping(headersMap);
        try {
            List<AllAccountsViewDTO> accounts = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            params.put(DBPUtilitiesConstants.FILTER,
                    "Membership_id" + DBPUtilitiesConstants.EQUAL + membershipDTO.getId());
            JsonObject response = new JsonObject();
            String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
            String integrationType = EnvironmentConfigurationsHandler
                    .getServerProperty("ARRANGEMENTS_BACKEND");
            if (StringUtils.isNotBlank(integrationType) && "MS".equalsIgnoreCase(integrationType)) {
                response = getCoreCustomerAccountsArrangements(membershipDTO, integratedToBase, headersMap);
            } else if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")
            		&& StringUtils.isNotBlank(integrationType) && "T24".equalsIgnoreCase(integrationType)) {
                response = getCoreCustomerAccountsT24(membershipDTO, integratedToBase, headersMap);
            }
            else {
                response = ServiceCallHelper.invokeServiceAndGetJson(params, headersMap,
                        URLConstants.ALLACCOUNTSVIEW_GET);
            }
            if (null != response
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                    && response.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW).isJsonArray()) {
                JsonArray array = response.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                        .getAsJsonArray();
                for (JsonElement element : array) {
                    if (element.isJsonObject()) {
                        accounts.add((AllAccountsViewDTO) DTOUtils
                                .loadJsonObjectIntoObject(element.getAsJsonObject(), AllAccountsViewDTO.class, true));
                    }
                }
            }
            if (accounts.size() > 0)
                responseDTO.setResponse(accounts);
        } catch (Exception e) {
            alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10762);
        }
        return responseDTO;
    }

    private JsonObject getCoreCustomerAccountsArrangements(MembershipDTO membershipDTO,
            Map<String, String> integratedToBase,
            Map<String, Object> headersMap) {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("coreCustomerId",
        		membershipDTO.getCompanyLegalUnit() + "-" + membershipDTO.getId());
        JsonObject customerAccounts =
                ServiceCallHelper.invokeServiceAndGetJson("ArrangementsMicroServicesJava",
                        null, "getAccountsByCoreCustomerIdSearch",
                        inputParams, headersMap);
        JsonArray coreCustomerAccounts = new JsonArray();
        if (customerAccounts != null && JSONUtil.hasKey(customerAccounts, "arrangements")
                && customerAccounts.get("arrangements").isJsonArray()
                && customerAccounts.get("arrangements").getAsJsonArray().size() > 0) {
            for (JsonElement jsonelement : customerAccounts.get("arrangements").getAsJsonArray()) {
                JsonObject account = new JsonObject();

                String linkedReference = JSONUtil.getString(jsonelement.getAsJsonObject(), "linkedReference");
                if ("true".equalsIgnoreCase(JSONUtil.getString(jsonelement.getAsJsonObject(), "externalIndicator"))) {
                    account.addProperty("Account_id", linkedReference);

                } else {
                    String[] linkedReferenceArray = linkedReference.split("-");
                    String accountId = null;
                    if (linkedReferenceArray != null && linkedReferenceArray.length == 2)
                        accountId = linkedReferenceArray[1];
                    account.addProperty("Account_id", accountId);
                }
                String productDescription = "";
                if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), "productDescription")) {
                    productDescription = JSONUtil.getString(jsonelement.getAsJsonObject(), "productDescription");
                } else {
                    productDescription = JSONUtil.getString(jsonelement.getAsJsonObject(), "shortTitle");
                }
              //account specific details bug new code
                if (!jsonelement.getAsJsonObject().has("product") || !integratedToBase
                        .containsKey(JSONUtil.getString(jsonelement.getAsJsonObject(), "product")))
                    continue;
                account.addProperty("AccountName", productDescription);
                account.addProperty("accountType",
                        integratedToBase.get(JSONUtil.getString(jsonelement.getAsJsonObject(), "product")));
                account.addProperty("Type_id",
                        HelperMethods.getAccountsTypes().get(JSONUtil.getString(account, "accountType")));
                account.addProperty("accountStatus",
                        StringUtils.isNotBlank(JSONUtil.getString(jsonelement.getAsJsonObject(), "arrangementStatus"))
                                ? JSONUtil.getString(jsonelement.getAsJsonObject(), "arrangementStatus")
                                : "Active");
                account.addProperty("arrangementId",
                        JSONUtil.getString(jsonelement.getAsJsonObject(), "arrangementId"));

                String productline = JSONUtil.getString(jsonelement.getAsJsonObject(), "productLine");
                account.addProperty("productId", JSONUtil.getString(jsonelement.getAsJsonObject(), "product"));

                JsonArray customerDetails = new JsonArray();
                if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), "roles")
                        && jsonelement.getAsJsonObject().get("roles").isJsonArray()
                        && jsonelement.getAsJsonObject().get("roles").getAsJsonArray().size() > 0) {
                    customerDetails = jsonelement.getAsJsonObject().get("roles").getAsJsonArray();
                    for (JsonElement customerInfo : customerDetails) {
                        String partyId = JSONUtil.getString(customerInfo.getAsJsonObject(), "partyId");
                        String[] partIdArray = partyId.split("-");
                        if (membershipDTO.getId().equalsIgnoreCase(partIdArray[1])) {
                            String shortNameLanguageDescription = "";
                            try {
                                JsonObject extensionData =
                                        customerInfo.getAsJsonObject().get("extensionData").getAsJsonObject();
                                shortNameLanguageDescription = extensionData.get("shortName").getAsJsonArray().get(0)
                                        .getAsJsonObject().get("languageDescription").getAsString();
                            } catch (Exception e) {

                            }
                            JsonObject accountholder = new JsonObject();
                            accountholder.addProperty("username", shortNameLanguageDescription);
                            accountholder.addProperty("fullname", shortNameLanguageDescription);
                            account.addProperty("AccountHolder", accountholder.toString());
                            account.addProperty("ownership", StringUtils.capitalize(
                                    JSONUtil.getString(customerInfo.getAsJsonObject(), "partyRole")));
                            account.addProperty("Membership_id", membershipDTO.getId());

                            coreCustomerAccounts.add(account);
                        }

                }
            }
        }
        }
        JsonObject response = new JsonObject();
        response.add(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW, coreCustomerAccounts);
        return response;
    }

    public JsonObject getCoreCustomerAccountsT24(MembershipDTO membershipDTO, Map<String, String> integratedToBase,
            Map<String, Object> headersMap) {
        JsonObject response = new JsonObject();
        addT24Headers(headersMap, membershipDTO.getId(), membershipDTO.getCompanyLegalUnit());
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("customerId", membershipDTO.getId());



        JsonObject customerAccounts =
                ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISACCOUNTS_INTEGRATION_SERVICE,
                        null, OperationName.GET_CORE_CUSTOMER_ACCOUNTS,
                        inputParams, headersMap);


        JsonArray coreCustomerAccounts = new JsonArray();
        if (customerAccounts != null && JSONUtil.hasKey(customerAccounts, DBPDatasetConstants.DATASET_RECORDS)
                && customerAccounts.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                && customerAccounts.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
            for (JsonElement jsonelement : customerAccounts.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray()) {
                JsonObject account = new JsonObject();
                account.addProperty("Account_id", JSONUtil.getString(jsonelement.getAsJsonObject(), "accountId"));
                account.addProperty("AccountName", JSONUtil.getString(jsonelement.getAsJsonObject(), "acccountName"));
                account.addProperty("accountType",
                        integratedToBase.get(JSONUtil.getString(jsonelement.getAsJsonObject(), "productId")));
                account.addProperty("Type_id",
                        HelperMethods.getAccountsTypes().get(JSONUtil.getString(account, "accountType")));
                String accountStatus = JSONUtil.getString(jsonelement.getAsJsonObject(), "accountStatus");
                account.addProperty("accountStatus", StringUtils.isNotBlank(accountStatus) ? accountStatus : "Active");
                JsonObject accountholder = new JsonObject();
                accountholder.addProperty("username",
                        JSONUtil.getString(jsonelement.getAsJsonObject(), "customerName"));
                accountholder.addProperty("fullname",
                        JSONUtil.getString(jsonelement.getAsJsonObject(), "customerName"));
                account.addProperty("AccountHolder", accountholder.toString());
                account.addProperty("arrangementId",
                        JSONUtil.getString(jsonelement.getAsJsonObject(), "arrangementId"));

                account.addProperty("productId", JSONUtil.getString(jsonelement.getAsJsonObject(), "productId"));
                /*if (portfolioAccounts != null && JSONUtil.hasKey(portfolioAccounts, DBPDatasetConstants.DATASET_RECORDS)
                        && portfolioAccounts.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                        && portfolioAccounts.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                    for (JsonElement jsonelement1 : portfolioAccounts.get(DBPDatasetConstants.DATASET_RECORDS)
                            .getAsJsonArray()) {
                        String portfolioId = JSONUtil.getString(jsonelement1.getAsJsonObject(), "portfolioId");
                        account.addProperty("portfolioId", portfolioId);
                        account.addProperty("isPortfolioAccount",
                                JSONUtil.getString(jsonelement1.getAsJsonObject(), "isPortfolioAccount"));
                        account.addProperty("portfolioName",
                                JSONUtil.getString(jsonelement1.getAsJsonObject(), "accountName"));
                        account.addProperty("portfolioProduct",
                                JSONUtil.getString(jsonelement1.getAsJsonObject(), "managedAccount"));
                        if (!(portfolioId == null)) {
                            Map<String, String> acctportfolio = new HashMap();
                            acctportfolio.put(portfolioId, accountId);
                            String linkedaccount = acctportfolio.get(portfolioId);
                            account.addProperty("Account_id", linkedaccount);
                        }
                    }
                }*/

                JsonArray customerDetails = new JsonArray();
                if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), "customerDetails")
                        && jsonelement.getAsJsonObject().get("customerDetails").isJsonArray()
                        && jsonelement.getAsJsonObject().get("customerDetails").getAsJsonArray().size() > 0) {
                    customerDetails = jsonelement.getAsJsonObject().get("customerDetails").getAsJsonArray();
                    for (JsonElement customerInfo : customerDetails) {
                        if (membershipDTO.getId().equalsIgnoreCase(
                                JSONUtil.getString(customerInfo.getAsJsonObject(), "customer"))
                                && "YES".equalsIgnoreCase(
                                        JSONUtil.getString(customerInfo.getAsJsonObject(), "beneficialOwner"))) {
                            //account.addProperty("ownership",
                                    //JSONUtil.getString(customerInfo.getAsJsonObject(), "roleDisplayName"));
                        	account.addProperty("ownership",
                                    JSONUtil.getString(customerInfo.getAsJsonObject(), "customerRole"));
                            account.addProperty("Membership_id",
                                    JSONUtil.getString(customerInfo.getAsJsonObject(), "customer"));
                            coreCustomerAccounts.add(account);
                        }
                    }
                }
            }
        }
        response.add(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW, coreCustomerAccounts);
        return response;
    }

    @Override
    public MembershipDTO getMembershipDetails(String membershipId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException {
        MembershipDTO dto = null;
        JsonObject membershipJson = null;
        try {
            if (StringUtils.isBlank(membershipId)) {
                return null;
            }
            final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
            Map<String, Object> inputParams = new HashMap<>();
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                inputParams.put("Cif", membershipId);
                String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                String operationName = OperationName.GET_COMPANY_DETAILS;
                headerMap.put("companyId", legalEntityId);
                membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                        inputParams, headerMap);
            } else {
                String filter = "id" + DBPUtilitiesConstants.EQUAL + membershipId;
                inputParams.put(DBPUtilitiesConstants.FILTER, filter);
                membershipJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                        URLConstants.MEMBERSHIP_GET);
            }
            if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_MEMBERSHIP)
                    && membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).isJsonArray()) {
                JsonArray array = membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                dto = (MembershipDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), MembershipDTO.class,
                        false);
            }
            if (null != dto && StringUtils.isNotBlank(dto.getAddressId())) {
                AddressBusinessDelegate addressBD = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(AddressBusinessDelegate.class);
                AddressDTO addressDTO = addressBD.getAddressDetails(dto.getAddressId(), headerMap);
                dto.setAddressDTO(addressDTO);
            } else if (null != dto && StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                String operationName = OperationName.GET_COMPANY_ADDRESS;
                headerMap.put("companyId", legalEntityId);
                JsonObject addressJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                        inputParams, headerMap);

                if (JSONUtil.isJsonNotNull(addressJson)
                        && JSONUtil.hasKey(addressJson, DBPDatasetConstants.DATASET_ADDRESS)
                        && addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).isJsonArray()) {
                    JsonArray addressArray =
                            addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).getAsJsonArray();

                    JsonElement element = addressArray.size() > 0 ? addressArray.get(0) : new JsonObject();
                    AddressDTO addressDTO = (AddressDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                            AddressDTO.class, true);
                    dto.setAddressDTO(addressDTO);
                }

            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10238);
        }
        return dto;
    }

   

    public void addT24Headers(Map<String, Object> headersMap, String id, String legalEntityId) {
        HelperMethods.addJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);
        
        if(StringUtils.isNotBlank(legalEntityId)) {
        	headersMap.put("companyId", legalEntityId);
        } else {
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setBackendId(id);
        backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();

      //  if (headersMap.get("companyId") != null && StringUtils.isBlank(headersMap.get("companyId").toString())) {
            if (backendIdentifierDTO != null && StringUtils.isNotBlank(backendIdentifierDTO.getCompanyLegalUnit())) {
                headersMap.put("companyId", backendIdentifierDTO.getCompanyLegalUnit());
                
            } else {
            	/*This is for fall back..*/
                headersMap.put("companyId",
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
            }
       // }
        }
    }

    @Override
    public List<AllAccountsViewDTO> getCoreCustomerAccounts(List<MembershipDTO> membershipList,
            Map<String, Object> headersMap) throws ApplicationException {
        List<AllAccountsViewDTO> accounts = new ArrayList<>();
        Map<String, String> integratedToBase = getAccountTypeMapping(headersMap);
        try {

            JsonObject response = new JsonObject();
            String legalEntityId = null;
            final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
            String integrationType = EnvironmentConfigurationsHandler
                    .getServerProperty("ARRANGEMENTS_BACKEND");
            if (StringUtils.isNotBlank(integrationType) && "MS".equalsIgnoreCase(integrationType)) {
                JsonArray coreCustomerAccounts = new JsonArray();
                JsonObject arrangementaccounts = null;
                for (MembershipDTO dto : membershipList) {

                    arrangementaccounts = getCoreCustomerAccountsArrangements(dto, integratedToBase, headersMap);
                    if (null != arrangementaccounts
                            && JSONUtil.hasKey(arrangementaccounts, DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                            && arrangementaccounts.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW).isJsonArray()) {
                        JsonArray array = arrangementaccounts.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                                .getAsJsonArray();
                        coreCustomerAccounts.addAll(array);
                    }
                }

                response.add(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW, coreCustomerAccounts);
            } else if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")
            		&& StringUtils.isNotBlank(integrationType) && "T24".equalsIgnoreCase(integrationType)) {

                MembershipDTO membershipDTO = new MembershipDTO();
                StringBuilder corecustomers = new StringBuilder();
                for (MembershipDTO dto : membershipList) {
                    corecustomers.append(dto.getId()).append(" ");
                    legalEntityId = dto.getCompanyLegalUnit();
                }
                if (StringUtils.isNotBlank(corecustomers)) {
                    corecustomers.replace(corecustomers.length() - 1, corecustomers.length(), "");
                }
                membershipDTO.setId(corecustomers.toString());
                membershipDTO.setCompanyLegalUnit(legalEntityId);
                response = getCoreCustomerAccountsT24(membershipDTO, integratedToBase, headersMap);
            } else {
                Map<String, Object> params = null;

                JsonArray coreCustomerAccounts = new JsonArray();
                JsonObject standaloneAccounts = null;
                for (MembershipDTO dto : membershipList) {
                    params = new HashMap<>();
                    params.put(DBPUtilitiesConstants.FILTER,
                            "Membership_id" + DBPUtilitiesConstants.EQUAL + dto.getId());
                    standaloneAccounts = ServiceCallHelper.invokeServiceAndGetJson(params, headersMap,
                            URLConstants.ALLACCOUNTSVIEW_GET);
                    if (null != standaloneAccounts
                            && JSONUtil.hasKey(standaloneAccounts, DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                            && standaloneAccounts.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW).isJsonArray()) {
                        JsonArray array = standaloneAccounts.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                                .getAsJsonArray();
                        coreCustomerAccounts.addAll(array);
                    }
                }

                response.add(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW, coreCustomerAccounts);
            }
            if (null != response
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                    && response.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW).isJsonArray()) {
                JsonArray array = response.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                        .getAsJsonArray();
                for (JsonElement element : array) {
                    if (element.isJsonObject()) {
                        accounts.add((AllAccountsViewDTO) DTOUtils
                                .loadJsonObjectIntoObject(element.getAsJsonObject(), AllAccountsViewDTO.class, true));
                    }
                }
            }

        } catch (Exception e) {
            alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10762);
        }
        return accounts;
    }

    public Map<String, String> getAccountTypeMapping(Map<String, Object> headersMap) {
        String accountTypeProperties = BundleConfigurationHandler
                .fetchConfigurationValueOnKey(BundleConfigurationHandler.BUNDLEID_DBP, "ACCOUNT_TYPES", headersMap);
        Map<String, String> integratedToBase = new HashMap<>();
        try {
            integratedToBase = new ObjectMapper().readValue(accountTypeProperties, HashMap.class);
        } catch (JsonProcessingException e) {
        }
        return integratedToBase;
    }

	@Override
	public DBXResult searchCoreCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		
		DBXResult responseDTO = new DBXResult();
		Map<String, Object> procParams = new HashMap<>();
		procParams.put("_id", StringUtils.isNotBlank(membershipDTO.getId()) ? membershipDTO.getId() : "");
		procParams.put("_name", StringUtils.isNotBlank(membershipDTO.getName()) ? membershipDTO.getName() : "");
		procParams.put("_email", StringUtils.isNotBlank(membershipDTO.getEmail()) ? membershipDTO.getEmail() : "");
		procParams.put("_phone", StringUtils.isNotBlank(membershipDTO.getPhone()) ? membershipDTO.getPhone() : "");
		procParams.put("_dateOfBirth",
				StringUtils.isNotBlank(membershipDTO.getDateOfBirth()) ? membershipDTO.getDateOfBirth() : "");
		procParams.put("_status", StringUtils.isNotBlank(membershipDTO.getStatus()) ? membershipDTO.getStatus() : "");
		procParams.put("_city",
				(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getCityName()))
						? membershipDTO.getAddress().getCityName()
						: "");
		procParams.put("_country",
				(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getCountry()))
						? membershipDTO.getAddress().getCountry()
						: "");
		procParams.put("_zipCode",
				(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getZipCode()))
						? membershipDTO.getAddress().getZipCode()
						: "");
		if (StringUtils.isNotBlank(membershipDTO.getTaxId())) {
			procParams.put("_taxId", StringUtils.isNotBlank(membershipDTO.getTaxId()) ? membershipDTO.getTaxId() : "");
		}
		if (StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit())) {
			procParams.put("_legalEntityId",
					StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit()) ? membershipDTO.getCompanyLegalUnit()
							: "");
		}

		JsonObject response = new JsonObject();
		try {
			String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
			if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
				response = searchCoreCustomerT24(procParams, headersMap);
			} else {
				response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
						URLConstants.MEMBERSHIP_CUSTOMER_SEARCH_PROC);
			}
			diagnostic.prepareDebug("partycust" + response).log();
			if (JSONUtil.hasKey(response, "records")) {
				if (response.get("records").getAsJsonArray().size() > 0) {
					responseDTO.setResponse(response.get("records").getAsJsonArray());
				}
			} else {
				alert.prepareError("CoreCustomerBackendDelegateImpl : Backend response is not appropriate " + response).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_10756);
			}
		} catch (Exception e) {
			alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers"
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10756);
		}
		return responseDTO;

	}



	@Override
	public DBXResult getCoreRelativeCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
			throws ApplicationException {

		DBXResult responseDTO = new DBXResult();
		Map<String, Object> procParams = new HashMap<>();
		procParams.put("_id", StringUtils.isNotBlank(membershipDTO.getId()) ? membershipDTO.getId() : "");
		procParams.put("_legalEntityId",
				StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit()) ? membershipDTO.getCompanyLegalUnit() : "");
		JsonObject response = new JsonObject();
		try {
			String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
			if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
				response = getCoreRelativeCustomerT24(membershipDTO, headersMap);
			} else {
				response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
						URLConstants.MEMBERSHIP_RELATIVE_CUSTOMERS_GET_PROC);
			}
			if (JSONUtil.hasKey(response, "records")) {
				if (response.get("records").getAsJsonArray().size() > 0) {
					responseDTO.setResponse(response.get("records").getAsJsonArray());
				}
			} else {
				alert.prepareError("CoreCustomerBackendDelegateImpl : Backend response is not appropriate " + response).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_10759);
			}
		} catch (Exception e) {
			alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers", e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10759);
		}
		return responseDTO;

	}



	@Override
	public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, String legalEntityId,
			Map<String, Object> headerMap) throws ApplicationException {

		MembershipDTO dto = new MembershipDTO();
		JsonObject membershipJson = null;
		try {
			if (StringUtils.isBlank(taxID) || StringUtils.isBlank(companyName)) {
				return null;
			}
			final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
			Map<String, Object> inputParams = new HashMap<>();
			if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
				inputParams.put("Taxid", taxID);
				inputParams.put("companyName", companyName);
				String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
				String operationName = OperationName.GET_COMPANYID_FROM_TAXID;
				headerMap.put("companyId", legalEntityId);
				membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName, inputParams,
						headerMap);
				if (null != membershipJson && membershipJson.has("companyId")) {
					dto.setId(membershipJson.get("companyId").getAsString());

				}
			} else {

				String filter = "taxId" + DBPUtilitiesConstants.EQUAL + taxID + DBPUtilitiesConstants.AND + "name"
						+ DBPUtilitiesConstants.EQUAL + "'" + companyName + "'";
				inputParams.put(DBPUtilitiesConstants.FILTER, filter);
				membershipJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.MEMBERSHIP_GET);

				if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_MEMBERSHIP)
						&& membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).isJsonArray()) {
					JsonArray array = membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).getAsJsonArray();
					JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
					dto = (MembershipDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
							MembershipDTO.class, false);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error Occured for getMemberShipDetails",e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10238);
		}
		return dto;
	}
}