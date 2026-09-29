package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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

public class GetAllEntitlements implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result processedResult = new Result();
        try {
            Map<String, Object> inputparams = CommonUtilities.getInputMapFromInputArray(inputArray);
            String username = (String) inputparams.get("userName");
            JSONObject getResponse = getAllEntitlements(username, requestInstance);
            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                String authToken = AdminConsoleOperations.login(requestInstance);
                ServiceConfig.setValue("Auth_Token", authToken);
                getResponse = getAllEntitlements(username, requestInstance);
            }
            processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
            return processedResult;
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return processedResult;
        }
    }

    public JSONObject getAllEntitlements(String userName, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("userName", userName);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "getAllEntitlements");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
