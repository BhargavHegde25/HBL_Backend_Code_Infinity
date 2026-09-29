package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.AdminConsole.Utilities.URLConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetUnreadMessageCount implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result finalResult = new Result();

        String username = null;
        String customer_id = null;

        Map<String, Object> inputParams = CommonUtilities.getInputMapFromInputArray(inputArray);
        username = inputParams.get("username").toString();
        customer_id = inputParams.get("customer_id").toString();

        if (StringUtils.isNotBlank(customer_id)) {
            finalResult = getUnreadMessageCountDB(customer_id, requestInstance);

            if (finalResult != null && finalResult.getNameOfAllParams().contains("unreadMessageCount")) {
                return finalResult;
            }
        }

        JSONObject getResponse = getUnreadMessageCount(username, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = getUnreadMessageCount(username, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject getUnreadMessageCount(String username, DataControllerRequest dcRequest) {
        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("username", username);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "getUnreadMessageCount");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

    public Result getUnreadMessageCountDB(String customer_id, DataControllerRequest dcRequest) {
        Result result = new Result();
        Result countResult = new Result();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_customerId", customer_id);

        try {
            countResult = HelperMethods.invokeServiceAndGetResult(dcRequest, inputParams,
                    HelperMethods.getHeaders(dcRequest), URLConstants.GETUNREADMESSAGESCOUNT_PROC_GET);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }

        if (HelperMethods.hasRecords(countResult)) {
            String unreadMessageCount = HelperMethods.getFieldValue(countResult, "messageCount");

            if (StringUtils.isBlank(unreadMessageCount)) {
                return null;
            } else {
                result.addParam(new Param("unreadMessageCount", unreadMessageCount, "String"));
                return result;
            }
        } else {
            return null;
        }

    }

}
