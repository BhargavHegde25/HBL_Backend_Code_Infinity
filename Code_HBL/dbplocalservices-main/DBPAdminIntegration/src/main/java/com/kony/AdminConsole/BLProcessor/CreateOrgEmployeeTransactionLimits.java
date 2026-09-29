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

public class CreateOrgEmployeeTransactionLimits implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Map<String, Object> inputparams = CommonUtilities.getInputMapFromInputArray(inputArray);

        try {
            String reqErrorCode = requestInstance.getParameter(DBPConstants.DBP_ERROR_CODE_KEY);
            if (reqErrorCode.equals("10064") || reqErrorCode.equals("12406") || reqErrorCode.equals("12407")
                    || reqErrorCode.equals("12408")) {
                Result res = new Result();
                Record rec = new Record();
                setValidationMsgwithCode("SECURITY ERROR - NOT AUTHORIZED", reqErrorCode);
                rec.setId("Limits_attr");
                res.addRecord(rec);
                return res;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        String customer_id1 = requestInstance.getParameter("id");
        String customer_id2 = (String) inputparams.get("id");

        if (CommonUtilities.isEmptyString(customer_id1)) {
            customer_id1 = customer_id2;
        }

        if (CommonUtilities.isEmptyString(customer_id1)) {
            Result ValidationError = new Result();
            Record record = new Record();
            setValidationMsgwithCode("User id not available to create limits.", "12405");
            record.setId("Limits_attr");
            ValidationError.addRecord(record);
            return ValidationError;
        }

        String customer_role = requestInstance.getParameter("Role_id");
        if (CommonUtilities.isEmptyString(customer_role)) {
            customer_role = (String) inputparams.get("Role_id");
        }

        if (CommonUtilities.isEmptyString(customer_role)) {
            Result ValidationError = new Result();
            Record record = new Record();
            setValidationMsgwithCode("No role available in the request to proceed.", "12413");
            record.setId("Limits_attr");
            ValidationError.addRecord(record);
            return ValidationError;
        }

        String customer_service_limits = requestInstance.getParameter("services");

        JSONObject getResponse = createTransactionLimits(customer_id1, customer_role, customer_service_limits,
                requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = createTransactionLimits(customer_id1, customer_role, customer_service_limits,
                    requestInstance);
        }

        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        Record record = new Record();
        String errorCode = HelperMethods.getFieldValue(processedResult, DBPConstants.DBP_ERROR_CODE_KEY);
        if (StringUtils.isBlank(errorCode)) {
            if (processedResult.getParamByName(DBPConstants.DBP_ERROR_CODE_KEY) != null) {
                errorCode = processedResult.getParamValueByName(DBPConstants.DBP_ERROR_CODE_KEY);
            }
        }

        if (!(getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0 || StringUtils.isNotBlank(errorCode))) {
            record = setSuccessMsgwithCode("Successful", "");
        } else {
            String error = "Error while creating transaction limits.";
            if (getResponse.has("errmsg")) {
                error = error + " " + getResponse.getString("errmsg");
            } else if (getResponse.has(DBPConstants.DBP_ERROR_MESSAGE_KEY)) {
                error = error + " " + getResponse.getString(DBPConstants.DBP_ERROR_MESSAGE_KEY);
            }
            record = setValidationMsgwithCode(error, "12414");
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
                new HashMap<>(), "createOrgEmployeeServiceLimits");
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
        Param validionMsg = new Param("success", message, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        record.addParam(validionMsg);
        return record;
    }

}
