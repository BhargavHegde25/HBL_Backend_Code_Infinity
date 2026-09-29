package com.temenos.auth.admininteg.operation;

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

public class IsDeviceRegistered implements JavaService2 {

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
                String username = requestInstance.getParameter("UserName");

                JSONObject getResponse = isDeviceRegistered(did, username, requestInstance);

                if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                    String authToken = AdminConsoleOperations.login(requestInstance);
                    ServiceConfig.setValue("Auth_Token", authToken);
                    getResponse = isDeviceRegistered(did, username, requestInstance);
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

    public JSONObject isDeviceRegistered(String did, String username, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("Device_id", did);
        postParametersMap.put("username", username);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "isDeviceRegistered");
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
