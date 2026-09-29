package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceCallHelper;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.utils.ConvertJsonToResult;
import com.kony.dbputilities.utils.DBPUtilitiesConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerAlertTypePreference implements JavaService2 {

    private static final String FAILURE_REASON = "FailureReason";

	@Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        String alertCategoryId = null;
        try {
            alertCategoryId = requestInstance.getParameter("AlertCategoryId");
        } catch (Exception e) {
            processedResult = getErrorResult(e);
            return processedResult;
        }

        if (alertCategoryId == null) {
            processedResult = getErrorResult(9002, "AlertCategoryId cannot be empty");
            return processedResult;
        }

        String accountId = requestInstance.getParameter("AccountId");
        String accountTypeId = requestInstance.getParameter("AccountTypeId");
        JSONArray accounts = null;
        if (StringUtils.isNotBlank(accountId)) {
            accounts = getAccountsForLoggedinUser(requestInstance, processedResult);
            if (accounts == null) {
                ErrorCodeEnum.ERR_11025.setErrorCode(processedResult);
                return processedResult;
            }
        }
        
        String customerTypeStr = null;
        String userId = HelperMethods.getUserIdFromSession(requestInstance);
        if (StringUtils.isNotBlank(userId)) {
            customerTypeStr = getUserTypeForLoggedinUser(requestInstance, processedResult,userId);
            if (customerTypeStr == null) {
                ErrorCodeEnum.ERR_10335.setErrorCode(processedResult);
                return processedResult;
            }
        }else {        	
        	     ErrorCodeEnum.ERR_10530.setErrorCode(processedResult);
                return processedResult;            
        }

        String getResponse =
                getCustomerAlertTypePreference(requestInstance, alertCategoryId, accountId, accountTypeId, accounts,customerTypeStr);
        JSONObject getResponseAsJSON = CommonUtilities.getStringAsJSONObject(getResponse);
        if (getResponseAsJSON.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse =
                    getCustomerAlertTypePreference(requestInstance, alertCategoryId, 
                    		accountId, accountTypeId, null, customerTypeStr);
        }
        processedResult = ConvertJsonToResult.convert(getResponse);
        return processedResult;

    }

    public static String getUserTypeForLoggedinUser(DataControllerRequest requestInstance, Result processedResult,
			String userId) {
    	String customerType = null;
    	Map<String, Object> cusSearchInputMap = new HashMap<>();
        cusSearchInputMap.put("_id", userId);
        cusSearchInputMap.put("_pageOffset", "0");
        cusSearchInputMap.put("_pageSize","10");
        cusSearchInputMap.put("_sortVariable", "name");
        cusSearchInputMap.put("_sortDirection", "ASC");
        cusSearchInputMap.put( "_searchType", "CUSTOMER_SEARCH");
        cusSearchInputMap.put( "_legalEntityId", LegalEntityUtil.getCurrentLegalEntityIdFromCache(requestInstance));
        
                       
    	 Result customerSearchRes =
                 ServiceCallHelper.invokeServiceAndGetResult(
                		 requestInstance, cusSearchInputMap, null, "dbpCustomerSearchOperation");
         
         try {
             if(customerSearchRes != null && ( customerSearchRes.getParamValueByName("opstatus") != null
     				|| Integer.parseInt(customerSearchRes.getParamValueByName("opstatus")) == 0) ) {
            	 
            	 if(customerSearchRes.getRecordById("customerbasicinfo_view") != null) {
            		 customerType = customerSearchRes.getRecordById("customerbasicinfo_view").getParamValueByName("CustomerType_id"); 
            	 }
            	 
             } else {
            	String errmsgFailed = "Fetching customerType failed " ;
            	if(customerSearchRes != null) {
            		errmsgFailed= errmsgFailed +
            				customerSearchRes.getParamValueByName("errmsg") != null ? customerSearchRes.getParamValueByName("errmsg") :
            		 customerSearchRes.getParamValueByName("dbperrmsg") ;
            	}
            		 
            	 processedResult.addParam(new Param(FAILURE_REASON,  errmsgFailed));
             }
         } catch (Exception e) {
             processedResult.addParam(new Param(FAILURE_REASON, "Fetching customerType failed"));
            
         }
         
         return customerType;
       
	}

	private static Result getErrorResult(Throwable ex) {
        String errorMsg = ex.getMessage();
        if (errorMsg == null) {
            StackTraceElement ste = ex.getStackTrace()[0];
            errorMsg = ex.getClass().getName() + "' thrown in " + ste.getClassName() + "." + ste.getMethodName();
        }
        return getErrorResult(9001, errorMsg);
    }

    private static Result getErrorResult(int errorNumber, String errorMsg) {
        Result result = new Result();
        result.addParam(new Param("errornumber", Integer.toString(errorNumber)));
        result.addParam(new Param("errormsg", errorMsg));
        result.addParam(new Param("success", "false"));
        return result;
    }

    public String getCustomerAlertTypePreference(DataControllerRequest dcRequest, String alertCategoryId,
            String accountId, String accountTypeId, JSONArray accounts, String customerTypeStr) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        HashMap<String, Object> customHeaderParameters = new HashMap<>();
        customHeaderParameters.put("Accept-Language", dcRequest.getHeader("Accept-Language"));
        customHeaderParameters.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("CustomerId", userId);
        postParametersMap.put("AlertCategoryId", alertCategoryId);
        postParametersMap.put(DBPUtilitiesConstants.PARAM_CUSTOMER_TYPE_STR,
        										customerTypeStr);
        if (StringUtils.isBlank(accountId)) {
            accountId = "";
        }
        if (StringUtils.isBlank(accountTypeId)) {
            accountTypeId = "";
        }
        postParametersMap.put("AccountId", accountId);
        postParametersMap.put("AccountTypeId", accountTypeId);
        if (accounts != null) {
            postParametersMap.put("accounts", accounts.toString());
        }

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                customHeaderParameters, "getCustomerAlertTypePreference");

        return getResponseString;
    }

    public static JSONArray getAccountsForLoggedinUser(DataControllerRequest dataControllerRequest,
            Result processedResult) {
        String accounts =
                ServiceCallHelper.invokeServiceAndGetString(dataControllerRequest, null, null, "getCustomerAccountsReorg");
        JSONObject accountsJSON;
        try {
            accountsJSON = new JSONObject(accounts);
        } catch (Exception e) {
            processedResult.addParam(new Param(FAILURE_REASON, accounts));
            return null;
        }
        if (accountsJSON == null || !accountsJSON.has("opstatus") || accountsJSON.getInt("opstatus") != 0
                || !accountsJSON.has("Accounts")) {
            processedResult.addParam(new Param(FAILURE_REASON, accounts));
            return null;
        }
        return accountsJSON.getJSONArray("Accounts");
    }

}
