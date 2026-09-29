package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.ConvertJsonToResult;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerAlertCategoryPreference implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        String accountId = requestInstance.getParameter("AccountId");
        String accountTypeId = requestInstance.getParameter("AccountTypeId");

        String getResponse = getCustomerAlertCategoryPreference(requestInstance, accountId, accountTypeId);
        JSONObject getResponseAsJSON = CommonUtilities.getStringAsJSONObject(getResponse);
        if (getResponseAsJSON.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = getCustomerAlertCategoryPreference(requestInstance, accountId, accountTypeId);
        }
        try {
            processedResult = ConvertJsonToResult.convert(getResponse);
        } catch (Exception e) {
            processedResult = getErrorResult(e);
            return processedResult;
        }
        return processedResult;
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

    public String getCustomerAlertCategoryPreference(DataControllerRequest dcRequest, String accountId,
            String accountTypeId) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        HashMap<String, Object> customHeaderParameters = new HashMap<>();
        customHeaderParameters.put("Accept-Language", dcRequest.getHeader("Accept-Language"));
        customHeaderParameters.put("Accept-Language", ContentType.APPLICATION_JSON.getMimeType());

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("CustomerId", userId);
        if (StringUtils.isBlank(accountId)) {
            accountId = "";
        }
        if (StringUtils.isBlank(accountTypeId)) {
            accountTypeId = "";
        }
        postParametersMap.put("AccountId", accountId);
        postParametersMap.put("AccountTypeId", accountTypeId);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                customHeaderParameters, "getCustomerAlertCategoryPreference");
        return getResponseString;
    }

}
