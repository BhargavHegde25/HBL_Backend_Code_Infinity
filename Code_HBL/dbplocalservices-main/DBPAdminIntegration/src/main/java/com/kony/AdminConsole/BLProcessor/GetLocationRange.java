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

public class GetLocationRange implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        String currLatitude = requestInstance.getParameter("currLatitude");
        String currLongitude = requestInstance.getParameter("currLongitude");
        String radius = requestInstance.getParameter("radius");

        JSONObject getResponse = getLocationRange(currLatitude, currLongitude, radius, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = getLocationRange(currLatitude, currLongitude, radius, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject getLocationRange(String currLatitude, String currLongitude, String radius,
            DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("currLatitude", currLatitude);
        postParametersMap.put("currLongitude", currLongitude);
        postParametersMap.put("radius", radius);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "getLocationRange");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
