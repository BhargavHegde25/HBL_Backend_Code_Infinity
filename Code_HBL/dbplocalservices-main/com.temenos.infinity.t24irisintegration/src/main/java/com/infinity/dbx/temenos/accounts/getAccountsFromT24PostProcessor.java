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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.konylabs.middleware.dataobject.ResultToJSON;
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

    @SuppressWarnings("deprecation")
	@Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);
        String loginUserId = request.getParameter(TemenosConstants.PARAM_LOGINUSERID);
        String explicitCoreCustomerIdList = request.getParameter("explicitCoreCustomerIdList");
        List<Record> accountRecords = new ArrayList<Record>();
        diagnostic.debug("Transact Accounts Response:###"+ ResultToJSON.convert(result) );
        ResultToJSON.convert(result);
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
                
        JsonArray jsonarray = parseRecordsForNAPNew(accountRecords,explicitCoreCustomerIdList,temenosUtils,request);
        Result NAPResult = newAccountProcessing(jsonarray,loginUserId, request);
        
        String accountsString = NAPResult.getParamValueByName("accounts");
        String newAccounts = NAPResult.getParamValueByName("newAccounts");
        
        if(StringUtils.isBlank(accountsString)) {
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset("Accounts"));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }
        
        Result finalResult = processT24Data(accountRecords, request, accountsString, newAccounts, loginUserId);    
        
        diagnostic.debug("Transact Accounts finalResult Response:###"+ ResultToJSON.convert(finalResult) );
        return finalResult;
    }

	private Result processT24Data(List<Record> accountTypeRecords,DataControllerRequest request,
			String accountsString,String newAccounts,String loginUserId) {
		Map<String, String> dbpConfigurations = BundleConfigurationHandler.fetchBundleConfigurations("DBP", request);
		alert.prepareError("dbpConfigurations:"+dbpConfigurations).log();
		Map<String, String> transferFlagDetails = getTransferSupportedFlagDetails(dbpConfigurations, request);
				
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
        if (accountTypeRecords == null || accountTypeRecords.isEmpty()) {
            alert.prepareError("Accounts empty return result").log();
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset(DS_ACCOUNTS));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);

        String backendId = getCoreBackendId(request);
        String defaultAcc = getCustomerDefaultAcc(backendId);
        diagnostic.prepareDebug("defaultAcc###"+ defaultAcc).log();
        
		diagnostic.prepareDebug("backendId product id###"+ backendId).log();
		
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
                if (temenosUtils.accountTypesMap.containsKey(accountType)) {
                    accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
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
						e.printStackTrace();
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
                
                diagnostic.prepareDebug("Description::::" + product.getParamValueByName("productDescription")).log();
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
                        String supportTransferFromFlag = getTransferSupportFlag(transferFlagDetails,product.getParamValueByName("categoryId"), "Dr", request);
                        diagnostic.prepareDebug("getTransferSupportFlag supportTransferFromFlag::::" + supportTransferFromFlag).log();
                        String supportTransferToFlag =getTransferSupportFlag(transferFlagDetails,product.getParamValueByName("categoryId"), "Cr", request);
                        diagnostic.prepareDebug("getTransferSupportFlag supportTransferToFlag::::" + supportTransferToFlag).log();
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
					if (!isAccountHasNoAccess(dbpConfigurations,productIdVal, request)) {
						/*** Setting new default account if customer doesn't have default account  ***/
						if (ACCOUNT_TYPE_SAVINGS.equalsIgnoreCase(accountType) || ACCOUNT_TYPE_CHECKING.equalsIgnoreCase(accountType)) {
						   if(StringUtils.isBlank(defaultAcc) && StringUtils.isNotBlank(accountId)) {
							   alert.prepareError("HBL:exisisting default accountId:"+defaultAcc+",and new default accountId:"+accountId+",for user:"+loginUserId).log();
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
    			diagnostic.prepareDebug("customerDetailsRecordList  product ::"+customerDetailsRecordList).log();
    			
    			if (customerDetailsRecordList == null || customerDetailsRecordList.size() > 1) {
    				diagnostic.prepareDebug("customerDetailsRecordList if loop product").log();
    				for (Record customerDetailsRecord : customerDetailsRecordList) {
    					if (backendId.equalsIgnoreCase(customerDetailsRecord.getParamValueByName("customerId"))) {
    						String customerId = customerDetailsRecord.getParamValueByName("customerId");
    						String roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
    						String customerRole = customerDetailsRecord.getParamValueByName("customerRole");
    						diagnostic.prepareDebug("customerDetailsRecordList customerId product##"+ customerId).log();
    						diagnostic.prepareDebug("customerDetailsRecordList roleDisplayName product##"+ roleDisplayName).log();
    						diagnostic.prepareDebug("customerDetailsRecordList customerRole product##"+ customerRole).log();
    						
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
						diagnostic.prepareDebug("customerDetailsRecordList roleDisplayName product Else##" + roleDisplayName)
								.log();
						diagnostic.prepareDebug("customerDetailsRecordList customerRole product## Else" + customerRole)
								.log();

						product.addStringParam("roleDisplayName", roleDisplayName);
						product.addStringParam("customerRole", customerRole);
					}
				}
    			
    			
            }
        }
        if (!accounts.isEmpty() && StringUtils.isNotBlank(loginUserId) && StringUtils.isBlank(Membership_id)) {
            Gson gson = new Gson();
            String gsonAccounts = gson.toJson(accounts);
            temenosUtils.insertIntoSession(SESSION_ATTRIB_ACCOUNT, gsonAccounts, request);
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
		diagnostic.prepareDebug("customerID ##" + customerID);
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
			diagnostic.prepareDebug("Default Accounts" + accounts);
			JSONObject acc = accounts.getJSONObject(0);
			defaultAccId = acc.getString("Account_id");
			diagnostic.prepareDebug("defaultAccId:::" + defaultAccId);
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception caught while getCustomerDefaultAcc", e);
		}
		return defaultAccId;
	}
	
	private HashMap<String, String> getTransferSupportedFlagDetails(Map<String, String> dbpConfigurations,DataControllerRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> map = new HashMap<String, String>();
        try {
			map = mapper.readValue(dbpConfigurations.get("TRANSFER_SUPPORTED_ACCOUNTS"), HashMap.class);
			alert.prepareError("map:"+map).log();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return map;
	}
	
	private String getTransferSupportFlag(Map<String, String> transferFlagDetails, String productId, String supportType, DataControllerRequest request) {
		String isSupported = "0";

		diagnostic.prepareDebug("getTransferSupportFlag productId::::" + productId).log();
		diagnostic.prepareDebug("getTransferSupportFlag supportType::::" + supportType).log();

		//JSONObject supportedAccs = ArrangementsUtils.getBundleConfigurations(TemenosConstants.ACCOUNT_TYPE_BUNDLE_NAME,
			//	"TRANSFER_SUPPORTED_ACCOUNTS", request);
		
		if(null != transferFlagDetails && transferFlagDetails.containsKey(productId)) {
			String supportedKeyVal = transferFlagDetails.get(productId);
			if (StringUtils.isNotBlank(supportedKeyVal)) {
				isSupported = (supportedKeyVal.contains(supportType)) ? "1" : "0";
			} else {
				isSupported = "0";
			}

			return isSupported;
		}
		

		/*JSONObject supportedAccs = new JSONObject(transferFlagDetails.get("TRANSFER_SUPPORTED_ACCOUNTS"));
		JSONObject configData = new JSONObject();
		String data = "";
		if (supportedAccs != null) {
			JSONArray configurations = supportedAccs.optJSONArray(TemenosConstants.CONFIGURATIONS);
			if (configurations != null && configurations.length() > 0) {
				configData = configurations.optJSONObject(0);
				if (configData.has(TemenosConstants.DBP_CONFIG_TABLE_VALUE))
					data = configData.getString(TemenosConstants.DBP_CONFIG_TABLE_VALUE);
			}
		}
		diagnostic.prepareDebug("getTransferSupportFlag data::::" + data).log();

		JSONObject dataobj = new JSONObject(data);
		String supportedKeyVal = dataobj.has(productId) && dataobj.get(productId)!=null? dataobj.getString(productId):"";
		diagnostic.prepareDebug("getTransferSupportFlag dataobj::::" + dataobj.toString()).log();
		diagnostic.prepareDebug("getTransferSupportFlag supportedKeyVal::::" + supportedKeyVal).log();
		if (StringUtils.isNotBlank(supportedKeyVal)) {
			isSupported = (supportedKeyVal.contains(supportType)) ? "1" : "0";
		} else {
			isSupported = "0";
		}*/

		return isSupported;
	}

	
	private boolean isAccountHasNoAccess(Map<String, String> dbpConfigurations, String productId, DataControllerRequest request) {
		boolean accHasAccess = false;
 
		try {
		diagnostic.prepareDebug("getTransferSupportFlag productId::::" + productId).log();
		String supportedAccs = null != dbpConfigurations.get("ACCOUNTS_TYPES_NOACCESS")
				? (dbpConfigurations.get("ACCOUNTS_TYPES_NOACCESS"))
				: null;
		alert.prepareError("supportedAccs:"+supportedAccs).log();
		
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = mapper.readValue(supportedAccs, HashMap.class);

        if (map.containsKey(productId)) {
			accHasAccess = true;
		}
				
				//ArrangementsUtils.getBundleConfigurations(TemenosConstants.ACCOUNT_TYPE_BUNDLE_NAME,"ACCOUNTS_TYPES_NOACCESS", request);
		/*JSONObject configData = new JSONObject();
		String data = "";
		if (supportedAccs != null) {
			JSONArray configurations = supportedAccs.optJSONArray(TemenosConstants.CONFIGURATIONS);
			if (configurations != null && configurations.length() > 0) {
				configData = configurations.optJSONObject(0);
				if (configData.has(TemenosConstants.DBP_CONFIG_TABLE_VALUE))
					data = configData.getString(TemenosConstants.DBP_CONFIG_TABLE_VALUE);
			}
		}
		diagnostic.prepareDebug("getTransferSupportFlag data::::" + data).log();
 
		JSONObject dataobj = new JSONObject(data);
		if (dataobj.has(productId)) {
			accHasAccess = true;
		}*/
		}catch (Exception e) {
			e.printStackTrace();
		}
		return accHasAccess;
	}
	private Result newAccountProcessing(JsonArray jsonarray,String loginUserId,DataControllerRequest request) {
		HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("accounts", jsonarray.toString());
        inputParams.put("customerId", loginUserId);
		request.addRequestParam_("accounts", jsonarray.toString());
        request.addRequestParam_("customerId", loginUserId);

        diagnostic.prepareDebug("input params in GetAccountDetailsByAccountIdListPreProcessor::::" + inputParams.toString()).log();
        Result newAccountProcessing = new Result();
		try {
			newAccountProcessing = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
			        SERVICE_BACKEND_PRODUCTSERVICE, OP_NEW_ACCOUNT_PROCESSING, true);
		} catch (Exception e) {
			
			alert.prepareError(e.toString()).log();
		}
        return newAccountProcessing;
	}

	private JsonArray parseRecordsForNAP(List<Record> accountRecords,String explicitCoreCustomerIdList, TemenosUtils temenosUtils) {
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
			diagnostic.prepareDebug("customerDetailsRecordList ::"+customerDetailsRecordList).log();
			if (customerDetailsRecordList == null || customerDetailsRecordList.size() > 1) {
				
				diagnostic.prepareDebug("customerDetailsRecordList if loop").log();
			} else {
				diagnostic.prepareDebug("customerDetailsRecordList else loop").log();
				for (Record customerDetailsRecord : customerDetailsRecordList) {
					customerId = customerDetailsRecord.getParamValueByName("customerId");
					roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
				}
			}
            if (StringUtils.isNotBlank(customerId) && !explicitCoreCustomerIdList.contains(" "+customerId+" ")) {
                String accountType = record.getParamValueByName("productId");
                if (temenosUtils.accountTypesMap.containsKey(accountType)) {
                    accountType = temenosUtils.accountTypesMap.get(accountType);
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
	
	private JsonArray parseRecordsForNAPNew(List<Record> accountRecords,String explicitCoreCustomerIdList, TemenosUtils temenosUtils, DataControllerRequest request) {
		JsonArray jsonarray = new JsonArray();
		String customerId = "";
		String roleDisplayName = "";
		String backendId = getCoreBackendId(request);
		diagnostic.prepareDebug("backendId ###"+ backendId).log();
		for (Record product : accountRecords) {
			List<Record> products = product.getDatasetById(DS_PRODUCTS) != null
                    ? product.getDatasetById(DS_PRODUCTS).getAllRecords()
                    : null;
            for (Record record : products) { 
			List<Record> customerDetailsRecordList = record.getDatasetById("customerDetails") != null
					? record.getDatasetById("customerDetails").getAllRecords() : null;
			diagnostic.prepareDebug("customerDetailsRecordList ::"+customerDetailsRecordList).log();
			if (customerDetailsRecordList == null || customerDetailsRecordList.size() > 1) {
				diagnostic.prepareDebug("customerDetailsRecordList if loop").log();
				for (Record customerDetailsRecord : customerDetailsRecordList) {
					if (backendId.equalsIgnoreCase(customerDetailsRecord.getParamValueByName("customerId"))) {
						customerId = customerDetailsRecord.getParamValueByName("customerId");
						roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
						diagnostic.prepareDebug("customerDetailsRecordList customerId ##"+ customerId).log();
						diagnostic.prepareDebug("customerDetailsRecordList roleDisplayName ##"+ roleDisplayName).log();
						break;
					}
				}
				
			} else {
				diagnostic.prepareDebug("customerDetailsRecordList else loop").log();
				for (Record customerDetailsRecord : customerDetailsRecordList) {
					customerId = customerDetailsRecord.getParamValueByName("customerId");
					roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
				}
			}
            if (StringUtils.isNotBlank(customerId) && !explicitCoreCustomerIdList.contains(" "+customerId+" ")) {
                String accountType = record.getParamValueByName("productId");
                if (temenosUtils.accountTypesMap.containsKey(accountType)) {
                    accountType = temenosUtils.accountTypesMap.get(accountType);
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
        diagnostic.prepareDebug("HBL:updateDefaultAccount response:"+ResultToJSON.convert(result)).log();
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
        diagnostic.prepareDebug("HBL:getIdFromCustomerAccounts response:"+ResultToJSON.convert(existingAccounts)).log();
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
