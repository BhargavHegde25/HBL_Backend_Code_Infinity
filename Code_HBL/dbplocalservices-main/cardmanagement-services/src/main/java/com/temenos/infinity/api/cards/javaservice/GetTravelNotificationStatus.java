package com.temenos.infinity.api.cards.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.temenos.infinity.api.cards.utils.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetTravelNotificationStatus implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
        String CardNumbers = requestInstance.getParameter("CardNumbers");
        Map<String, Object> inputparams = com.temenos.infinity.api.cards.utils.HelperMethods.getInputMapFromInputArray(inputArray);
        String username = (String) inputparams.get("userName");
        JSONObject getResponse = getTravelNotificationStatus(CardNumbers, username, requestInstance);
        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = getTravelNotificationStatus(CardNumbers, username, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject getTravelNotificationStatus(String CardNumbers, String username,
            DataControllerRequest dcRequest) {
        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("CardNumbers", CardNumbers);
        postParametersMap.put("Username", username);
        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "getTravelNotificationStatus");
        return com.temenos.infinity.api.cards.utils.HelperMethods.getStringAsJSONObject(getResponseString);
    }

}
