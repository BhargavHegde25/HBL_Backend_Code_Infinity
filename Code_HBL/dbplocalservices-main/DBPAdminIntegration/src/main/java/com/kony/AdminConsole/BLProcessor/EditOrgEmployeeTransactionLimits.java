package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class EditOrgEmployeeTransactionLimits implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
            String reqErrorCode = requestInstance.getParameter(DBPConstants.DBP_ERROR_CODE_KEY);
            if (StringUtils.isNotBlank(reqErrorCode)) {
                Result res = new Result();
                Record rec = new Record();
                rec.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, reqErrorCode, "string"));
                rec.setId("Limits_attr");
                res.addRecord(rec);
                return res;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        String customer_id = requestInstance.getParameter("id");
        String customer_role = requestInstance.getParameter("Role_id");
        String customer_service_limits = requestInstance.getParameter("services");
        JSONObject getResponse = createTransactionLimits(customer_id, customer_role, customer_service_limits,
                requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = createTransactionLimits(customer_id, customer_role, customer_service_limits, requestInstance);
        }

        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        Record record = new Record();
        String errorCode = HelperMethods.getFieldValue(processedResult, DBPConstants.DBP_ERROR_CODE_KEY);

        if (!(getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0 || StringUtils.isNotBlank(errorCode))) {
            record = setSuccessMsgwithCode("Successful", "3400");
        } else {
            record = setValidationMsgwithCode("Error while creating transaction limits.", "12415");
        }
        record.setId("Limits_attr");
        processedResult.addRecord(record);
        return processedResult;
    }

    public JSONObject createTransactionLimits(String customer_id, String customer_role, String customer_service_limits,
            DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();

        postParametersMap.put("Customer_id", customer_id);
        postParametersMap.put("Role_id", customer_role);
        postParametersMap.put("services", customer_service_limits);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "editOrgEmployeeServiceLimits");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

    private Record setValidationMsgwithCode(String message, String code) {
        Record record = new Record();
        Param validionMsg = new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, message,
                DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        record.addParam(validionMsg);
        Param status = new Param(DBPConstants.DBP_ERROR_CODE_KEY, code, "int");
        record.addParam(status);
        return record;
    }

    private Record setSuccessMsgwithCode(String message, String code) {
        Record record = new Record();
        record.addParam(new Param("success", message, "string"));
        return record;
    }
}
