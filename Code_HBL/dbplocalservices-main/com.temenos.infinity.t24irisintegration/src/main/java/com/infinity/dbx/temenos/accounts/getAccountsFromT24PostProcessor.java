package com.infinity.dbx.temenos.accounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbl.infinity.accounts.perf.BundleConfigCache;
import com.hbl.infinity.accounts.perf.GetListPerfConstants;
import com.hbl.infinity.accounts.perf.GetListTimer;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.createOrgEmployeeAccounts;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.eum.product.constants.OperationName;
import com.temenos.dbx.eum.product.constants.ServiceId;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class getAccountsFromT24PostProcessor extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	/** Thread-safe once configured; shared to avoid building a mapper per call. */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        GetListTimer timer = GetListTimer.start(GetListPerfConstants.COMPONENT_T24_POST);
        try {
            return processAccounts(result, request, timer);
        } finally {
            timer.finish();
        }
    }

    private Result processAccounts(Result result, DataControllerRequest request, GetListTimer timer) {
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);
        // One reference for the whole request: the singleton field can be reassigned by concurrent requests.
        Map<String, String> accountTypes = temenosUtils.accountTypesMap;
        timer.mark("accountTypes");
        String loginUserId = request.getParameter(TemenosConstants.PARAM_LOGINUSERID);
        String explicitCoreCustomerIdList = request.getParameter("explicitCoreCustomerIdList");
        List<Record> accountRecords = new ArrayList<Record>();
        if (result != null && result.getAllDatasets().size() > 0 && result.getDatasetById("Accounts") != null &&
                    result.getDatasetById("Accounts").getAllRecords().size() > 0) {
            	accountRecords = result.getDatasetById("Accounts").getAllRecords();
        }
        if(accountRecords.size()<1) {
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset("Accounts"));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }
        String backendId = getCoreBackendId(request);

        JsonArray jsonarray = parseRecordsForNAPNew(accountRecords,explicitCoreCustomerIdList,accountTypes,backendId);
        Result NAPResult = newAccountProcessing(jsonarray,loginUserId, request);
        timer.mark("newAccountProcessing");

        String accountsString = NAPResult.getParamValueByName("accounts");
        String newAccounts = NAPResult.getParamValueByName("newAccounts");

        if(StringUtils.isBlank(accountsString)) {
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset("Accounts"));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }

        return processT24Data(accountRecords, request, accountsString, newAccounts, loginUserId, accountTypes,
                backendId, timer);
    }

	private Result processT24Data(List<Record> accountTypeRecords,DataControllerRequest request,
			String accountsString,String newAccounts,String loginUserId, Map<String, String> accountTypes,
			String backendId, GetListTimer timer) {
		// Static Admin configuration: served from a TTL cache (HBL_BUNDLE_CONFIG_TTL_SECONDS, 0 = off) instead of
		// calling Admin.BundleConfifurations on every request. Failed loads are never cached.
		Map<String, String> dbpConfigurations = BundleConfigCache.fetchBundleConfigurations(
				BundleConfigurationHandler.BUDLENAME_DBP, request);
		timer.mark("bundleConfig");
		Map<String, String> transferFlagDetails = getTransferSupportedFlagDetails(dbpConfigurations);
		Map<String, Object> noAccessProducts = getNoAccessProducts(dbpConfigurations);

        HashMap<String, Account> accounts = new HashMap<String, Account>();
        List<Record> accountFinals = new ArrayList<Record>();
        Map<String, String> newAcntCoreCustomerId = new HashMap<>();
        HashSet<String> newAccountCoreCustomerIdList = new HashSet<>();
        if (StringUtils.isNotBlank(newAccounts)) {
            String[] accountsInfo = newAccounts.split("\\|");
            for (int i = 0; i < accountsInfo.length; i++) {
                String[] singleAccountInfo = accountsInfo[i].split(":");
                newAcntCoreCustomerId.put(singleAccountInfo[1], singleAccountInfo[0]);
                newAccountCoreCustomerIdList.add(singleAccountInfo[0]);
            }
        }
        Map<String, Set> coreCustomerActions = new HashMap<String, Set>();
        String Membership_id = request.getParameter("Membership_id") != null
                ? request.getParameter("Membership_id").toString()
                : "";
        String Actions = request.getParameter("actions") != null
                ? request.getParameter("actions").toString()
                : "";
        
        if(StringUtils.isNotBlank(loginUserId) && !"false".equals(Actions)) {
            coreCustomerActions = fetchDefaultAccountActions(request, loginUserId, newAccountCoreCustomerIdList);
        }
        timer.mark("defaultAccountActions");
        if (accountTypeRecords == null || accountTypeRecords.isEmpty()) {
            alert.prepareError("Accounts empty return result").log();
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset(DS_ACCOUNTS));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }

        TemenosUtils temenosUtils = TemenosUtils.getInstance();

        String defaultAcc = getCustomerDefaultAcc(backendId);
        timer.mark("defaultAccount");
        boolean debugEnabled = diagnostic.isDebugEnabled();

        for (Record record : accountTypeRecords) {
            List<Record> products = record.getDatasetById(DS_PRODUCTS) != null
                    ? record.getDatasetById(DS_PRODUCTS).getAllRecords()
                    : null;
            for (Record product : products) {
                JsonObject accountHolderjson = new JsonObject();
                String accountId = CommonUtils.getParamValue(product, "accountId");
               
                if(accountsString.contains(accountId)) {
                try {
                    if (newAcntCoreCustomerId.containsKey(accountId)) {
                        product.addStringParam("actions",
                                coreCustomerActions.get(newAcntCoreCustomerId.get(accountId)).toString());
                        product.addParam("isNew", "true");
                    }
                    else {
                        product.addParam("isNew", "false");
                    }
                } catch (Exception e) {
                    alert.prepareError("Exception occured while fetching the corecustomer account level default actions"
                            + e.getMessage()).log();
                }
                String accountHolder = CommonUtils.getParamValue(product, PARAM_ACCOUNT_HOLDER);
                accountHolderjson.addProperty(PARAM_USERNAME, accountHolder);
                accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
                product.addStringParam(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString());

                if (StringUtils.isNotBlank(product.getParamValueByName("portfolioId")))
                    product.addStringParam("isPortFolioAccount", Boolean.TRUE.toString());
                else
                    product.addStringParam("isPortFolioAccount", Boolean.FALSE.toString());

                String accountType = product.getParamValueByName(PARAM_ACCOUNT_TYPE);
                if (accountTypes.containsKey(accountType)) {
                    accountType = accountTypes.get(accountType);// getDBXAccountType(accountType);
                }
                product.addStringParam("IBAN", product.getParamValueByName("accountIBAN"));
                //String favouriteStatus = product.getParamValueByName("favouriteStatus");
				//if (StringUtils.isNotBlank(favouriteStatus) && favouriteStatus.equalsIgnoreCase("1")) {
                if (StringUtils.isNotBlank(defaultAcc) && defaultAcc.equalsIgnoreCase(accountId)) {
					product.addStringParam("isDefaultAccount", "true");
					ServicesManager sm;
					try {
						sm = request.getServicesManager();
						ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
						String HBL_IMAGES_APP_URL = paramHelper.getServerProperty("HBL_IMAGES_APP_URL");
						String accountCardImageUrl = HBL_IMAGES_APP_URL+""+"/accountdashboard/accountcard.png";
						product.addStringParam("IBAN", accountCardImageUrl);
					} catch (AppRegistryException e) {
						alert.prepareError("Unable to read HBL_IMAGES_APP_URL", e).log();
					}
					
				}else {
					product.addStringParam("isDefaultAccount", "false");
				}
				
                product.addStringParam("product", product.getParamValueByName("productId"));
                product.addStringParam("bankName", product.getParamValueByName("bankName"));
                product.addStringParam("accountCategory", product.getParamValueByName("categoryId"));
                product.addStringParam("productGroup", product.getParamValueByName("productId"));
                product.addStringParam("typeDescription", accountType);
                product.addStringParam("description", product.getParamValueByName("productDescription"));

                product.addStringParam("account_id",accountId);
                String customerReference = CommonUtils.getParamValue(product, CUSTOMER_REFERENCE);
                if (!"".equalsIgnoreCase(customerReference)) {
                    product.addStringParam(NICKNAME, customerReference);
                    product.addStringParam(DISPLAY_NAME, customerReference);
                } else {
                    String accountName = product.getParamValueByName(PARAM_ACC_NAME) != ""
                            ? product.getParamValueByName(PARAM_ACC_NAME)
                            : "";
                    if (StringUtils.isNotBlank(accountName)) {
                        product.addStringParam(NICKNAME, accountName);
                        product.addStringParam(DISPLAY_NAME, accountName);
                    }
                }
                String currencyId = product.getParamValueByName(CURRENCY_CODE);
                if (accountType != null && !"".equalsIgnoreCase(accountType)) {
                    String principalBalance = product.getParamValueByName(PRINCIPAL_BALANCE);
                   
                    
                    if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType)) {
                        product.addStringParam(AVAILABLE_BALANCE, principalBalance);
                    }
                    if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType) || ACCOUNT_TYPE_LOAN.equalsIgnoreCase(accountType) || ACCOUNT_TYPE_MORTGAGE.equalsIgnoreCase(accountType)) {
                    product.addStringParam(PARAM_SUPPORT_BILLPAY, NO);
                    }
                    else 
                    {
                        if(currencyId.equalsIgnoreCase("NPR")){
                           product.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
                        }else {
                           product.addStringParam(PARAM_SUPPORT_BILLPAY, NO);
                        }
                    }
                    
                    String availableFunds = product.getParamValueByName(AVAILABLE_FUNDS);
                    if (ACCOUNT_TYPE_SAVINGS.equalsIgnoreCase(accountType) || ACCOUNT_TYPE_CHECKING.equalsIgnoreCase(accountType)) {
                        product.addStringParam(AVAILABLE_BALANCE, availableFunds);
                    }
                    
                   /* else {
                    if(currencyId.equalsIgnoreCase("NPR")){
                       product.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
                    }else {
                       product.addStringParam(PARAM_SUPPORT_BILLPAY, NO);
                    }
                    }
                    */
                    product.addParam(PARAM_ACCOUNT_TYPE, accountType);
                    if (!"".equalsIgnoreCase(loginUserId)) {
                    	
                        product.addStringParam(PARAM_SUPPORT_CHECKS, YES);
                        product.addStringParam(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES);
                        String supportTransferFromFlag = getTransferSupportFlag(transferFlagDetails,product.getParamValueByName("categoryId"), "Dr");
                        String supportTransferToFlag =getTransferSupportFlag(transferFlagDetails,product.getParamValueByName("categoryId"), "Cr");
                        product.addStringParam(PARAM_TRANSFER_SOURCE_ACCOUNT, supportTransferFromFlag);
                        product.addStringParam(PARAM_TRANSFER_DESTINATION_ACCOUNT, supportTransferToFlag);

                        Account account = TemenosUtils.copyToAccount(Account.class, product);
                        accounts.put(account.getAccountId(), account);
                    }

                    String estatement = CommonUtils.getParamValue(product, STATEMENT);
                    if (StringUtils.isNotBlank(estatement) && StringUtils.equalsIgnoreCase(estatement, ESTATEMENT)) {
                        product.addStringParam(PARAM_ESTATEMENTENABLE, "true");
                    } else
                        product.addStringParam(PARAM_ESTATEMENTENABLE, "false");
                    
                    
                    /**** remove accounts which are no access ***/
                    String productIdVal = product.getParamValueByName("productId");
					if (!isAccountHasNoAccess(noAccessProducts, productIdVal)) {
						/*** Setting new default account if customer doesn't have default account  ***/
						if (ACCOUNT_TYPE_SAVINGS.equalsIgnoreCase(accountType) || ACCOUNT_TYPE_CHECKING.equalsIgnoreCase(accountType)) {
						   if(StringUtils.isBlank(defaultAcc) && StringUtils.isNotBlank(accountId)) {
							   if (debugEnabled) {
								   diagnostic.prepareDebug("HBL: no default account, setting new default account for user").log();
							   }
							   try {
								   boolean isUpdated = updateDefaultAccount(loginUserId, accountId, request);
								   if(isUpdated) {
									   defaultAcc=accountId;
									   product.addStringParam("isDefaultAccount", "true");
								   }
							   }catch (Exception e) {
								   alert.prepareError("Failed to update default account for this user:"+loginUserId).log();
							   }
						   }
						}
					accountFinals.add(product);
					}
                }
                }
                
                
                
                List<Record> customerDetailsRecordList = product.getDatasetById("customerDetails") != null
    					? product.getDatasetById("customerDetails").getAllRecords() : null;

    			if (customerDetailsRecordList == null || customerDetailsRecordList.size() > 1) {
    				for (Record customerDetailsRecord : customerDetailsRecordList) {
    					if (backendId.equalsIgnoreCase(customerDetailsRecord.getParamValueByName("customerId"))) {
    						String roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
    						String customerRole = customerDetailsRecord.getParamValueByName("customerRole");

    						product.addStringParam("roleDisplayName", roleDisplayName);
    						product.addStringParam("customerRole", customerRole);
    						break;
    					}
    				}
    				
    				/*** If it is joint account then remove it from the response. It doesn't required to display ****/
    				accountFinals.remove(product);
    				
				} else {
					for (Record customerDetailsRecord : customerDetailsRecordList) {
						String roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
						String customerRole = customerDetailsRecord.getParamValueByName("customerRole");

						product.addStringParam("roleDisplayName", roleDisplayName);
						product.addStringParam("customerRole", customerRole);
					}
				}
    			
    			
            }
        }
        timer.mark("mapAccounts");
        if (!accounts.isEmpty() && StringUtils.isNotBlank(loginUserId) && StringUtils.isBlank(Membership_id)) {
            Gson gson = new Gson();
            String gsonAccounts = gson.toJson(accounts);
            temenosUtils.insertIntoSession(SESSION_ATTRIB_ACCOUNT, gsonAccounts, request);
            timer.mark("sessionWrite");
        }

        Result finalResult = new Result();
        Dataset ds = new Dataset(DS_ACCOUNTS);
        ds.addAllRecords(accountFinals);
        finalResult.addDataset(ds);
        finalResult.addOpstatusParam(0);
        finalResult.addHttpStatusCodeParam(200);
        return finalResult;
	}
	
	private String getCustomerDefaultAcc(String customerID) {
		JSONArray accounts = new JSONArray();
		String defaultAccId = "";

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "coreCustomerId" + DBPUtilitiesConstants.EQUAL + customerID
				+ DBPUtilitiesConstants.AND + "FavouriteStatus" + DBPUtilitiesConstants.EQUAL + "1");
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
					.withRequestParameters(inputParams).build().getResponse();
			JSONObject responseJSON = new JSONObject(response);
			accounts = responseJSON.getJSONArray("customeraccounts");
			JSONObject acc = accounts.getJSONObject(0);
			defaultAccId = acc.getString("Account_id");
		} catch (Exception e) {
			// No default account (empty list) is the common case here; nothing to log.
		}
		return defaultAccId;
	}

	/**
	 * Parses the TRANSFER_SUPPORTED_ACCOUNTS bundle entry. As before, a missing entry makes Jackson throw
	 * IllegalArgumentException, which is not caught and fails the request; invalid JSON yields an empty map.
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> getTransferSupportedFlagDetails(Map<String, String> dbpConfigurations) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
			map = OBJECT_MAPPER.readValue(dbpConfigurations.get("TRANSFER_SUPPORTED_ACCOUNTS"), HashMap.class);
		} catch (JsonProcessingException e) {
			alert.prepareError("Invalid TRANSFER_SUPPORTED_ACCOUNTS bundle configuration", e).log();
		}
        return map;
	}
	
	private String getTransferSupportFlag(Map<String, String> transferFlagDetails, String productId, String supportType) {
		String isSupported = "0";
		if(null != transferFlagDetails && transferFlagDetails.containsKey(productId)) {
			String supportedKeyVal = transferFlagDetails.get(productId);
			if (StringUtils.isNotBlank(supportedKeyVal)) {
				isSupported = (supportedKeyVal.contains(supportType)) ? "1" : "0";
			} else {
				isSupported = "0";
			}
		}
		return isSupported;
	}

	/**
	 * Parses the ACCOUNTS_TYPES_NOACCESS bundle entry once per request. Returns null when the configuration map or
	 * the entry is missing or not valid JSON; every product then counts as accessible, the same result the
	 * per-product parse gave before.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getNoAccessProducts(Map<String, String> dbpConfigurations) {
		try {
			return OBJECT_MAPPER.readValue(dbpConfigurations.get("ACCOUNTS_TYPES_NOACCESS"), HashMap.class);
		} catch (Exception e) {
			alert.prepareError("Unable to read ACCOUNTS_TYPES_NOACCESS bundle configuration", e).log();
			return null;
		}
	}

	private boolean isAccountHasNoAccess(Map<String, Object> noAccessProducts, String productId) {
		return noAccessProducts != null && noAccessProducts.containsKey(productId);
	}
	private Result newAccountProcessing(JsonArray jsonarray,String loginUserId,DataControllerRequest request) {
		HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("accounts", jsonarray.toString());
        inputParams.put("customerId", loginUserId);
		request.addRequestParam_("accounts", jsonarray.toString());
        request.addRequestParam_("customerId", loginUserId);

        Result newAccountProcessing = new Result();
		try {
			newAccountProcessing = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
			        SERVICE_BACKEND_PRODUCTSERVICE, OP_NEW_ACCOUNT_PROCESSING, true);
		} catch (Exception e) {
			
			alert.prepareError(e.toString()).log();
		}
        return newAccountProcessing;
	}

	private JsonArray parseRecordsForNAPNew(List<Record> accountRecords,String explicitCoreCustomerIdList,
			Map<String, String> accountTypes, String backendId) {
		JsonArray jsonarray = new JsonArray();
		String customerId = "";
		String roleDisplayName = "";
		for (Record product : accountRecords) {
			List<Record> products = product.getDatasetById(DS_PRODUCTS) != null
                    ? product.getDatasetById(DS_PRODUCTS).getAllRecords()
                    : null;
            for (Record record : products) { 
			List<Record> customerDetailsRecordList = record.getDatasetById("customerDetails") != null
					? record.getDatasetById("customerDetails").getAllRecords() : null;
			if (customerDetailsRecordList == null || customerDetailsRecordList.size() > 1) {
				for (Record customerDetailsRecord : customerDetailsRecordList) {
					if (backendId.equalsIgnoreCase(customerDetailsRecord.getParamValueByName("customerId"))) {
						customerId = customerDetailsRecord.getParamValueByName("customerId");
						roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
						break;
					}
				}
				
			} else {
				for (Record customerDetailsRecord : customerDetailsRecordList) {
					customerId = customerDetailsRecord.getParamValueByName("customerId");
					roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
				}
			}
            if (StringUtils.isNotBlank(customerId) && !explicitCoreCustomerIdList.contains(" "+customerId+" ")) {
                String accountType = record.getParamValueByName("productId");
                if (accountTypes.containsKey(accountType)) {
                    accountType = accountTypes.get(accountType);
                } else {
                    accountType = null;
                }
                if (StringUtils.isBlank(accountType))
                    continue;
                if (StringUtils.isBlank(record.getParamValueByName("accountId")))
                    continue;
                if (StringUtils.isBlank(customerId))
                    continue;
                if (StringUtils.isBlank(record.getParamValueByName("accountName")))
                    continue;
                if (StringUtils.isBlank(record.getParamValueByName("arrangementId")))
                    continue;
                if (StringUtils.isBlank(roleDisplayName))
                    continue;
                JsonObject json = new JsonObject();
                json.addProperty("accountId", record.getParamValueByName("accountId"));
                json.addProperty("customerId", customerId);
                json.addProperty("accountType", accountType);
                json.addProperty("accountName", record.getParamValueByName("accountName"));
                json.addProperty("arrangementId", record.getParamValueByName("arrangementId"));
                json.addProperty("roleDisplayName", record.getParamValueByName("roleDisplayName"));
                jsonarray.add(json);
            }
            }
        }
		return jsonarray;
	}
	
	public String getCoreBackendId(DataControllerRequest dcreq)  {		
    	String backendId = null;
		try {
			
			if (dcreq.getServicesManager().getIdentityHandler() != null) {
				Map<String, Object> userAttributesMap = dcreq.getServicesManager().getIdentityHandler().getUserAttributes();               
				String backendIdentifier = (String)userAttributesMap.get("backendIdentifiers");
				if(diagnostic.isDebugEnabled()){
					diagnostic.prepareDebug("backendIdentifier is" + backendIdentifier).log();
				}
				if(StringUtils.isNotBlank(backendIdentifier)) { 				
					backendId = getCoreIDFromJson(backendIdentifier);				
				}
			}
			else
			{
				alert.prepareError("NULL IDENTITYHANDLER").log();
			}
			
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();	
			
		}
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("backendId is" + backendId).log();
		}
		return backendId;

	}
	
	protected static String getCoreIDFromJson(String backendIdentifier) {
		String backendId = null;
		JsonObject backendIdentifiersJSON = new JsonParser().parse(backendIdentifier).getAsJsonObject();
		if(backendIdentifiersJSON.entrySet().size() == 1) {
			for ( Entry<String, JsonElement> entry : backendIdentifiersJSON.entrySet()) {
				backendId = getBackendIdFromCoreType(backendIdentifiersJSON, entry.getKey());
			}
			if(diagnostic.isDebugEnabled()){
				diagnostic.prepareDebug("backendId is" + backendId).log();
			}
		}else {
			String coreType = null;
			try {
				coreType = EnvironmentConfigurationsHandler.getServerAppProperty("ALERTS_CORETYPE");
			} catch (Exception e) {
				alert.prepareError("ALERTS_CORETYPE is not available" ,e).log();
				
			}
			if(coreType == null)
			{
				alert.prepareError("ALERTS_CORETYPE is not available").log();
			}
			else
			{
				if(StringUtils.isNotEmpty(coreType) && backendIdentifiersJSON.has(coreType)){					
					backendId = getBackendIdFromCoreType(backendIdentifiersJSON,coreType);
				}
				if(diagnostic.isDebugEnabled()){
					diagnostic.prepareDebug("backendId is" + backendId).log();
				}
			}
			
		}
		return backendId;
	}

	protected static String getBackendIdFromCoreType(JsonObject backendIdentifiersJSON, String key) {
		JsonArray backendTypeObj = backendIdentifiersJSON.get(key).getAsJsonArray();
		String backendId = null;
		if(backendTypeObj.size() > 0) {
			backendId  =	backendTypeObj.get(0).getAsJsonObject().get("BackendId").getAsString();
		}
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("backendId is" + backendId).log();
		}
		return backendId;
	}	
	
	@SuppressWarnings("rawtypes")
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
                Result defaultactions = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
                        "dbpRbLocalServicesdb", "dbxdb_fetch_default_account_actions_proc", true);
                String actions = defaultactions.getDatasetById("records").getAllRecords().get(0)
                        .getParamValueByName("defaultAccountActions");
                coreCustomerActions.put(coreCustomerId, new HashSet<>(Arrays.asList(StringUtils.split(actions, ","))));
            }
            return coreCustomerActions;
        } catch (Exception e) {
        	alert.prepareError(e.toString()).log();
        }
        return null;
    }
	private boolean updateDefaultAccount(String userId, String accountId, DataControllerRequest dcRequest)
            throws HttpCallException {
		 Result result = new Result();
		 boolean updated=false;
		 Map<String, String> inputParams = new HashMap<String, String>();
			        inputParams.put("Account_id", accountId);
			        inputParams.put("FavouriteStatus", "1");
			        inputParams.put("Customer_id", userId);
        if(getIdFromCustomerAccounts(inputParams, dcRequest, result)) {
        result=HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_UPDATE);
        if (StringUtils.isNotBlank(result.getParamValueByName("updatedRecords"))) {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
					return true;
        }
        }
		return updated;
    }
	private boolean getIdFromCustomerAccounts( Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) throws HttpCallException {
        String filter = DBPUtilitiesConstants.CUSTOMER_ID + DBPUtilitiesConstants.EQUAL + inputParams.get("Customer_id") +DBPUtilitiesConstants.AND + DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL +inputParams.get("Account_id");
        createOrgEmployeeAccounts accountsHelper = new createOrgEmployeeAccounts();
        Result existingAccounts = accountsHelper.getExistingAccounts(filter, inputParams.get("Customer_id"), dcRequest);
        if (!HelperMethods.hasRecords(existingAccounts)) {
            HelperMethods.setValidationMsgwithCode(ErrorConstants.INVALID_ACCOUNT_NUMBER, ErrorCodes.ERROR_SEARCHING_RECORD,result);
            alert.error("hbl::user:"+inputParams.get("Customer_id")+" doesn't have this account:"+inputParams.get("Account_id"));
            return false;
        }
        Record accountRecord = existingAccounts.getAllDatasets().get(0).getAllRecords().get(0);
        String id = HelperMethods.getFieldValue(accountRecord, DBPUtilitiesConstants.UN_ID);
        if(StringUtils.isNotBlank(id)) {
        inputParams.put("id", id);
        return true;
        }
        return false;
    }
}
