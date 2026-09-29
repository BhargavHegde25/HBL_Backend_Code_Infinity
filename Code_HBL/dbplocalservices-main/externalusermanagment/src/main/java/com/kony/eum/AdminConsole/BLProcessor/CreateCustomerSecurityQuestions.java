package com.kony.eum.AdminConsole.BLProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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

public class CreateCustomerSecurityQuestions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {

            Map<String, Object> inputMap = CommonUtilities.getInputMapFromInputArray(inputArray);
            String userName = (String) inputMap.get("userName");

            JSONArray securityQuestions = new JSONArray(requestInstance.getParameter("securityQuestions"));
            JSONObject getResponse = createCustomerSecurityQuestions(userName, securityQuestions, requestInstance);

            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                String authToken = AdminConsoleOperations.login(requestInstance);
                ServiceConfig.setValue("Auth_Token", authToken);
                getResponse = createCustomerSecurityQuestions(userName, securityQuestions, requestInstance);
            }
            Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
            return processedResult;

        } catch (Exception e) {
            Result res = new Result();
            StringWriter stringWriterInstance = new StringWriter();
            PrintWriter printWriterInstance = new PrintWriter(stringWriterInstance);
            alert.prepareError(e.toString()).log();
            String stackTraceAsString = stringWriterInstance.toString();
            res.addParam(new Param("errmsgg", e.getMessage(), "string"));
            res.addParam(new Param("trace", stackTraceAsString, "string"));
            return res;
        }

    }

    public JSONObject createCustomerSecurityQuestions(String userName, JSONArray securityQuestions,
            DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("userName", userName);
        if (securityQuestions != null) {
            postParametersMap.put("securityQuestions", securityQuestions.toString());
        }

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "createCustomerSecurityQuestions");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
