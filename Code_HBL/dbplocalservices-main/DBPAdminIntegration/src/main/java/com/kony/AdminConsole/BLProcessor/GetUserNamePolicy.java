package com.kony.AdminConsole.BLProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetUserNamePolicy implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
            Map<String, Object> map = new HashMap<>();
            Iterator<String> iterator = requestInstance.getParameterNames();
            String key;

            while (iterator.hasNext()) {
                key = iterator.next();
                map.put(key, requestInstance.getParameter(key));
            }

            JSONObject getResponse = updateAlerts(map, requestInstance);

            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                String authToken = AdminConsoleOperations.login(requestInstance);
                ServiceConfig.setValue("Auth_Token", authToken);
                getResponse = updateAlerts(map, requestInstance);
            }
            Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
            return processedResult;
        } catch (Exception e) {
            Result res = new Result();
            res.addParam(new Param("errmsgg", e.getMessage(), "string"));
            return res;
        }
    }

    public JSONObject updateAlerts(Map<String, Object> map, DataControllerRequest dcRequest) {

        Map<String, Object> customHeaders = new HashMap<>();
        customHeaders.put("Accept-Language", dcRequest.getHeader("Accept-Language"));

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, map, new HashMap<>(),
                "getusernamepolicy");
        return CommonUtilities.getStringAsJSONObject(getResponseString);

    }

}
