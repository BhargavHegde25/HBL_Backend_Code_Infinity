package com.kony.AdminConsole.BLProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyCustomerSecurityQuestions implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
            Map<String, Object> inputParams = CommonUtilities.getInputMapFromInputArray(inputArray);
            String userName = (String) inputParams.get("userName");

            JSONArray securityQuestions = new JSONArray(requestInstance.getParameter("securityQuestions"));

            JSONObject getResponse = verifyCustomerSecurityQuestions(userName, securityQuestions, requestInstance);

            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                String authToken = AdminConsoleOperations.login(requestInstance);
                ServiceConfig.setValue("Auth_Token", authToken);
                getResponse = verifyCustomerSecurityQuestions(userName, securityQuestions, requestInstance);
            }
            Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
            return processedResult;

        } catch (Exception e) {
            Result res = new Result();
            res.addParam(new Param("errmsgg", e.getMessage(), "string"));
            return res;
        }

    }

    public JSONObject verifyCustomerSecurityQuestions(String userName, JSONArray securityQuestions,
            DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("userName", userName);
        postParametersMap.put("securityQuestions", securityQuestions);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "verifyCustomerSecurityQuestions");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
