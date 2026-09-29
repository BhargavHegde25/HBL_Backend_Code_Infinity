package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.utils.ConvertJsonToResult;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerAccountAlertPreference implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        JSONArray accounts =
                GetCustomerAlertTypePreference.getAccountsForLoggedinUser(requestInstance, processedResult);
        if (accounts == null) {
            ErrorCodeEnum.ERR_11025.setErrorCode(processedResult);
            return processedResult;
        }
        String getResponse = getCustomerAccountAlertPreference(requestInstance, accounts);
        processedResult = ConvertJsonToResult.convert(getResponse);
        return processedResult;
    }

    public String getCustomerAccountAlertPreference(DataControllerRequest dcRequest, JSONArray accounts) {
        String username = HelperMethods.getUsernameFromSession(dcRequest);
        HashMap<String, Object> customHeaderParameters = new HashMap<>();
        customHeaderParameters.put("Accept-Language", dcRequest.getHeader("Accept-Language"));
        customHeaderParameters.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("username", username);
        if (accounts != null) {
            postParametersMap.put("accounts", accounts.toString());
        }

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                customHeaderParameters, "getCustomerAccountAlertPreference");

        return getResponseString;
    }

}
