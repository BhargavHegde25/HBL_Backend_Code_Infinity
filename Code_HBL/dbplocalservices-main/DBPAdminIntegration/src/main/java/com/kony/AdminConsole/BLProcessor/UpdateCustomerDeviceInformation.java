package com.kony.AdminConsole.BLProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

public class UpdateCustomerDeviceInformation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result processedResult = new Result();
        try {
            String reportingParams = requestInstance.getHeader("X-Kony-ReportingParams");
            if (StringUtils.isNotBlank(reportingParams)) {
                JSONObject reportingParamsJson = new JSONObject(
                        URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));

                String did = reportingParamsJson.optString("did");
                Map<String, Object> inputParams = CommonUtilities.getInputMapFromInputArray(inputArray);
                String username = (String) inputParams.get("UserName");

                String status_id = "SID_DEVICE_DE-REGISTERED";
                String lastUsedIp = requestInstance.getHeader("CLIENT_IP");
                if(StringUtils.isBlank(lastUsedIp)) {
                	try {
                		lastUsedIp = HelperMethods.getDecyptedClientIpAddressFromCache(requestInstance);
                	} catch (Exception e) {}
                } else {
                	lastUsedIp = HelperMethods.decryptClientIp(lastUsedIp);
                }
                String LastLoginTime = getCurrentTimeStamp();

                JSONObject getResponse = updateCustomerDeviceInfo(did, username, status_id, lastUsedIp, LastLoginTime,
                        requestInstance);

                if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                    String authToken = AdminConsoleOperations.login(requestInstance);
                    ServiceConfig.setValue("Auth_Token", authToken);
                    getResponse = updateCustomerDeviceInfo(did, username, status_id, lastUsedIp, LastLoginTime,
                            requestInstance);
                }
                processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
                return processedResult;
            }

        } catch (Exception e) {
            Result res = new Result();
            res.addParam(new Param("errmsgg", e.getMessage(), "string"));
            return res;
        }

        return processedResult;
    }

    public JSONObject updateCustomerDeviceInfo(String did, String username, String status_id, String lastUsedIp,
            String LastLoginTime, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("Device_id", did);
        postParametersMap.put("username", username);
        postParametersMap.put("Status_id", status_id);
        postParametersMap.put("LastUsedIp", lastUsedIp);
        postParametersMap.put("LastLoginTime", LastLoginTime);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "customerUpdateDeviceInformation");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

    public static String getCurrentTimeStamp() {
        return getFormattedTimeStamp(new Date(), null);
    }

    public static String getFormattedTimeStamp(Date dt, String format) {
        String dtFormat = "yyyy-MM-dd'T'hh:mm:ss";
        if (StringUtils.isNotBlank(format)) {
            dtFormat = format;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
        return formatter.format(dt);
    }

}
