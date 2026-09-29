package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAllAlerts implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        String alertTypeName = requestInstance.getParameter("alertTypeName");
        String alertTypeId = requestInstance.getParameter("alertTypeId");

        Map<String, Object> inputparams = CommonUtilities.getInputMapFromInputArray(inputArray);
        String username = (String) inputparams.get("userName");

        JSONObject getResponse = getAllAlerts(username, alertTypeName, alertTypeId, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = getAllAlerts(username, alertTypeName, alertTypeId, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject getAllAlerts(String userName, String alertTypeName, String alertTypeId,
            DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("userName", userName);
        postParametersMap.put("alertTypeName", alertTypeName);
        postParametersMap.put("alertTypeId", alertTypeId);
        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "getAllAlerts");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
