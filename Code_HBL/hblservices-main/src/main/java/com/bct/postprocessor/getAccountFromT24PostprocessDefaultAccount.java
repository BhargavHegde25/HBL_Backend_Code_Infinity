package com.bct.postprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class getAccountFromT24PostprocessDefaultAccount extends BasePostProcessor implements AccountsConstants {

    private static final Logger logger = LogManager.getLogger(getAccountFromT24PostprocessDefaultAccount.class);

    @SuppressWarnings("deprecation")
	@Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);
        String loginUserId = request.getParameter(TemenosConstants.PARAM_LOGINUSERID);
        String explicitCoreCustomerIdList = request.getParameter("explicitCoreCustomerIdList");
        List<Record> accountRecords = new ArrayList<Record>();
        logger.debug("Result: ##"+ ResultToJSON.convert(result));
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
        logger.debug("backendId ##"+ backendId);
        JsonArray jsonarray = parseRecordsForNAP(accountRecords,explicitCoreCustomerIdList,temenosUtils,backendId);
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
        return finalResult;
    }

	private Result processT24Data(List<Record> accountTypeRecords,DataControllerRequest request,
			String accountsString,String newAccounts,String loginUserId) throws DBPApplicationException, AppRegistryException {
		 ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String HBL_IMAGES_APP_URL = paramHelper.getServerProperty("HBL_IMAGES_APP_URL");
			String accountCardImageUrl = HBL_IMAGES_APP_URL+""+"/accountdashboard/accountcard.png";
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
        
        if(StringUtils.isNotBlank(loginUserId)) {
            coreCustomerActions = fetchDefaultAccountActions(request, loginUserId, newAccountCoreCustomerIdList);
        }
        if (accountTypeRecords == null || accountTypeRecords.isEmpty()) {
            logger.error("Accounts empty return result");
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset(DS_ACCOUNTS));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);
        Integer AccCount = getCustomerAccountCount(request.getParameter(TemenosConstants.PARAM_LOGINUSERID));
        logger.error("Customer Total Accounts: "+ AccCount);
        for (Record record : accountTypeRecords) {
            List<Record> products = record.getDatasetById(DS_PRODUCTS) != null
                    ? record.getDatasetById(DS_PRODUCTS).getAllRecords()
                    : null;
            for (Record product : products) {
            	JsonObject accountHolderjson = new JsonObject();
                String accountId = CommonUtils.getParamValue(product, "accountId");
                String customerId = request.getParameter(TemenosConstants.PARAM_LOGINUSERID);
                logger.debug("accountId *****"+ accountId);
                logger.debug("customerId *****"+ customerId);
                String defaultAcc = getUserDefaultAccounts(customerId);
                logger.debug("defaultAcc *****"+ defaultAcc);
                logger.debug("accountsString: "+ accountsString);
                if(accountsString.contains(accountId) && defaultAcc.equalsIgnoreCase(accountId)) {
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
                    logger.error("Exception occured while fetching the corecustomer account level default actions"
                            + e.getMessage());
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
                product.addStringParam("IBAN", accountCardImageUrl);
                product.addStringParam("product", product.getParamValueByName("productId"));
                product.addStringParam("bankName", product.getParamValueByName("bankName"));
                product.addStringParam("accountCategory", product.getParamValueByName("categoryId"));
                product.addStringParam("productGroup", product.getParamValueByName("productId"));
                product.addStringParam("typeDescription", accountType);
                product.addStringParam("account_id",accountId);
                //product.addStringParam("accountCardImage", accountCardImageUrl);
                product.addStringParam("bsbNum",AccCount.toString());
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

                if (accountType != null && !"".equalsIgnoreCase(accountType)) {
                    String principalBalance = product.getParamValueByName(PRINCIPAL_BALANCE);
                    if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType)) {
                        product.addStringParam(AVAILABLE_BALANCE, principalBalance);
                    }
                    product.addParam(PARAM_ACCOUNT_TYPE, accountType);
                    if (!"".equalsIgnoreCase(loginUserId)) {
                        product.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
                        product.addStringParam(PARAM_SUPPORT_CHECKS, YES);
                        product.addStringParam(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES);
                        product.addStringParam(PARAM_TRANSFER_SOURCE_ACCOUNT, YES);
                        product.addStringParam(PARAM_TRANSFER_DESTINATION_ACCOUNT, YES);

                        Account account = TemenosUtils.copyToAccount(Account.class, product);
                        accounts.put(account.getAccountId(), account);
                    }

                    String estatement = CommonUtils.getParamValue(product, STATEMENT);
                    if (StringUtils.isNotBlank(estatement) && StringUtils.equalsIgnoreCase(estatement, ESTATEMENT)) {
                        product.addStringParam(PARAM_ESTATEMENTENABLE, "true");
                    } else
                        product.addStringParam(PARAM_ESTATEMENTENABLE, "false");
                    accountFinals.add(product);
                }
                }else {
                	logger.debug("In else of processT24Data ***");
                }
            }
        }
        if (!accounts.isEmpty() && StringUtils.isNotBlank(loginUserId)) {
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

	private Result newAccountProcessing(JsonArray jsonarray,String loginUserId,DataControllerRequest request) {
		HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("accounts", jsonarray.toString());
        inputParams.put("customerId", loginUserId);
		request.addRequestParam_("accounts", jsonarray.toString());
        request.addRequestParam_("customerId", loginUserId);

        logger.debug("input params in GetAccountDetailsByAccountIdListPreProcessor::::" + inputParams.toString());
        Result newAccountProcessing = new Result();
		try {
			newAccountProcessing = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
			        SERVICE_BACKEND_PRODUCTSERVICE, OP_NEW_ACCOUNT_PROCESSING, true);
		} catch (Exception e) {
			
			logger.error(e);
		}
        return newAccountProcessing;
	}

	 public String getCoreBackendId(DataControllerRequest dcreq)  {		
	    	String backendId = null;
			try {
				
				if (dcreq.getServicesManager().getIdentityHandler() != null) {
					Map<String, Object> userAttributesMap = dcreq.getServicesManager().getIdentityHandler().getUserAttributes();               
					String backendIdentifier = (String)userAttributesMap.get("backendIdentifiers");
					if(logger.isDebugEnabled()){
						logger.debug("backendIdentifier is" + backendIdentifier);
					}
					if(StringUtils.isNotBlank(backendIdentifier)) { 				
						backendId = getCoreIDFromJson(backendIdentifier);				
					}
				}
				else
				{
					logger.error("NULL IDENTITYHANDLER");
				}
				
			} catch (Exception e) {
				logger.error(e);	
				
			}
			if(logger.isDebugEnabled()){
				logger.debug("backendId is" + backendId);
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
				if(logger.isDebugEnabled()){
					logger.debug("backendId is" + backendId);
				}
			}else {
				String coreType = null;
				try {
					coreType = EnvironmentConfigurationsHandler.getServerAppProperty("ALERTS_CORETYPE");
				} catch (Exception e) {
					logger.error("ALERTS_CORETYPE is not available" + e);
					logger.error(e);
				}
				if(coreType == null)
				{
					logger.error("ALERTS_CORETYPE is not available");
				}
				else
				{
					if(StringUtils.isNotEmpty(coreType) && backendIdentifiersJSON.has(coreType)){					
						backendId = getBackendIdFromCoreType(backendIdentifiersJSON,coreType);
					}
					if(logger.isDebugEnabled()){
						logger.debug("backendId is" + backendId);
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
			if(logger.isDebugEnabled()){
				logger.debug("backendId is" + backendId);
			}
			return backendId;
		}	
	private JsonArray parseRecordsForNAP(List<Record> accountRecords,String explicitCoreCustomerIdList, TemenosUtils temenosUtils, String backendId) {
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
			logger.debug("customerDetailsRecordList ::"+customerDetailsRecordList);
			if (customerDetailsRecordList == null || customerDetailsRecordList.size() > 1) {
				logger.debug("backendId ##"+ backendId);
				logger.debug("customerDetailsRecordList if loop");
				for (Record customerDetailsRecord : customerDetailsRecordList) {
					logger.debug("customerId in loop ##"+ customerDetailsRecord.getParamValueByName("customerId"));
					if(backendId.equalsIgnoreCase(customerDetailsRecord.getParamValueByName("customerId"))) {
						customerId = customerDetailsRecord.getParamValueByName("customerId");
						roleDisplayName = customerDetailsRecord.getParamValueByName("roleDisplayName");
					}
				}
			} else {
				logger.debug("customerDetailsRecordList else loop");
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
        	logger.error(e);
        }
        return null;
    }
	
	public String getUserDefaultAccounts(String customerID) {

		JSONArray accounts = new JSONArray();
		String defaultAccId = "";
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID
				+ DBPUtilitiesConstants.AND + "FavouriteStatus" + DBPUtilitiesConstants.EQUAL + "1");

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			accounts = responseJSON.getJSONArray("customeraccounts");
			
			logger.debug("Default Accounts"+ accounts);
			JSONObject acc = accounts.getJSONObject(0);
			defaultAccId = acc.getString("Account_id");
			logger.debug("defaultAccId:::"+defaultAccId);
			
		} catch (Exception e) {
			logger.error("Exception caught while getUserDefaultAccounts", e);
		}
		return defaultAccId;

	}
	
	public Integer getCustomerAccountCount(String customerID) throws DBPApplicationException {
		JSONArray accounts = new JSONArray();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID);
		
		String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
				.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
				.withRequestParameters(inputParams).build().getResponse();

		JSONObject responseJSON = new JSONObject(response);
		accounts = responseJSON.getJSONArray("customeraccounts");
		int length = accounts.length();

		logger.debug("Customer Accounts Length:::" + length);
		return length;
	}
	
}