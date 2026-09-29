package com.temenos.auth.admininteg.operation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.URLConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.kony.dbputilities.utils.HttpCallException;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetPasswordLockoutSettings implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);
        String customer_id = inputmap.get("customer_id");
        try {
            Result result = new Result();

            JSONObject response = getPasswordLockoutSettings(requestInstance);
            if (response != null) {
                try {
                    JSONObject innerJson = response.getJSONArray("passwordlockoutsettings").getJSONObject(0);
                    try {
                        if (innerJson.has("id")) {
                            innerJson.remove("id");
                            innerJson.remove("lastmodifiedts");
                            innerJson.remove("softdeleteflag");
                            innerJson.remove("createdts");
                            innerJson.remove("createdby");
                            innerJson.remove("modifiedby");
                            innerJson.remove("synctimestamp");
                        }
                    } catch (Exception e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    response = new JSONObject();
                    response.put("passwordlockoutsettings", innerJson);
                } catch (Exception e) {
                    alert.prepareError(e.getMessage()).log();
                    response = null;
                }
            }

            String lastUpdatedDate = getLastUpdatedDate(requestInstance, requestInstance.getParameter("UserName"),
                    customer_id);

            Result processedResult = HelperMethods.constructResultFromJSONObject(response);
            if (StringUtils.isNotBlank(lastUpdatedDate)) {
                evaluateRulesNupdateResponse(processedResult, lastUpdatedDate);
                return processedResult;
            }
            result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, "10044", "String"));
            result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, "User doesn't exist in DBX.", "String"));
            return result;
        } catch (Exception e) {
            Result res = new Result();

            StringWriter stringWriterInstance = new StringWriter();
            PrintWriter printWriterInstance = new PrintWriter(stringWriterInstance);
            

            res.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, e.getMessage(), "String"));
            return res;
        }
    }

    private void evaluateRulesNupdateResponse(Result processedResult, String lastUpdatedDate) {
        Record rec = processedResult.getRecordById("passwordlockoutsettings");
        String count = rec.getParam("passwordValidity").getValue();

        int passwordValidity = 0;
        int passwordExpiryWarningThreshold = 0;
        try {
            passwordValidity = Integer.parseInt(count);
        } catch (NumberFormatException e) {
            alert.prepareError(e.getMessage()).log();
        }

        count = rec.getParam("passwordExpiryWarningThreshold").getValue();
        try {
            passwordExpiryWarningThreshold = Integer.parseInt(count);
        } catch (NumberFormatException e) {
            alert.prepareError(e.getMessage()).log();
        }

        Date fromDate = HelperMethods.getFormattedTimeStamp(lastUpdatedDate);
        long diff = new Date().getTime() - fromDate.getTime();
        int diffInDays = (int) (diff / (1000 * 60 * 60 * 24));
        int validity = passwordValidity - diffInDays;

        if ((validity > 0 && passwordExpiryWarningThreshold >= validity) || validity <= 0) {
            rec.addParam(new Param("passwordExpiryWarningRequired", "true", "string"));
            rec.addParam(new Param("passwordExpiryRemainingDays", String.valueOf(validity), "string"));
        } else {
            rec.addParam(new Param("passwordExpiryWarningRequired", "false", "string"));
        }
    }

    public String getLastUpdatedDate(DataControllerRequest requestInstance, String userName, String customerId)
            throws HttpCallException {
        HashMap<String, Object> hashMap = new HashMap<>();

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(customerId)) {
            Map<String, String> userdetails = HelperMethods.getUserFromIdentityService(requestInstance);
            userName = userdetails.get("UserName");
            customerId = userdetails.get("customer_id");
        }

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(customerId)) {
            return null;
        }

        if (StringUtils.isNotBlank(customerId)) {
            hashMap.put("$filter", "Customer_id eq " + customerId);
            hashMap.put("$orderby", "createdts desc");
            hashMap.put("$top", "1");
            hashMap.put("$skip", "0");
            Result result = HelperMethods.invokeServiceAndGetResult(requestInstance, hashMap,
                    HelperMethods.getHeaders(requestInstance), URLConstants.PASSWORD_HISTORY_GET);

            if (HelperMethods.hasRecords(result)) {
                return HelperMethods.getFieldValue(result, "createdts");
            }

        }
        return null;
    }

    public JSONObject getPasswordLockoutSettings(DataControllerRequest dcRequest) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String response = HelperMethods.invokeServiceAndGetString(dcRequest, hashMap,
                    HelperMethods.getHeaders(dcRequest), URLConstants.PASSWORDLOCKOUTSETTINGS_GET);
            return CommonUtilities.getStringAsJSONObject(response);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }
    }

}
