package com.kony.AdminConsole.BLProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateOutageMessage implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
            String createdby = requestInstance.getParameter("createdby");
            String messageText = requestInstance.getParameter("messageText");
            String modifiedby = requestInstance.getParameter("modifiedby");
            String service_id = requestInstance.getParameter("service_id");
            String status_id = requestInstance.getParameter("status_id");

            JSONObject getResponse = createOutageMessage(createdby, messageText, modifiedby, service_id, status_id,
                    requestInstance);

            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                String authToken = AdminConsoleOperations.login(requestInstance);
                ServiceConfig.setValue("Auth_Token", authToken);
                getResponse = createOutageMessage(createdby, messageText, modifiedby, service_id, status_id,
                        requestInstance);
            }
            Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
            return processedResult;

        } catch (Exception e) {
            Result res = new Result();
            res.addParam(new Param("errmsgg", e.getMessage(), "string"));
            return res;
        }

    }

    public JSONObject createOutageMessage(String createdby, String messageText, String modifiedby, String service_id,
            String status_id, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("createdby", createdby);
        postParametersMap.put("MessageText", messageText);
        postParametersMap.put("modifiedby", modifiedby);
        postParametersMap.put("Service_id", service_id);
        postParametersMap.put("Status_id", status_id);
        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "createOutageMessage");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
