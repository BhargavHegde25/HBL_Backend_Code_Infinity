package com.kony.dbputilities.customerfeedbackpostprocessor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

import eu.bitwalker.useragentutils.UserAgent;

public class CreateUserFeedbackPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {

        JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        JsonObject response = new JsonObject();
        String userName = "";
        String description = "";
        if (jsonObject.isJsonNull()) {
            response.addProperty(DBPConstants.DBP_ERROR_MESSAGE_KEY, "Internal communication Error");
            responseManager.getPayloadHandler().updatePayloadAsJson(response);
            return;
        }
        if (!jsonObject.isJsonNull() && jsonObject.has(DBPConstants.DBP_ERROR_CODE_KEY)) {
            response.addProperty(DBPConstants.DBP_ERROR_CODE_KEY,
                    jsonObject.get(DBPConstants.DBP_ERROR_CODE_KEY).getAsString());
            response.addProperty(DBPConstants.DBP_ERROR_MESSAGE_KEY,
                    jsonObject.get(DBPConstants.DBP_ERROR_MESSAGE_KEY).getAsString());
            responseManager.getPayloadHandler().updatePayloadAsJson(response);
            return;
        }
        if (jsonObject.has("feedback") && !jsonObject.get("feedback").isJsonNull()) {
            JsonArray feedback = jsonObject.get("feedback").getAsJsonArray();
            description = feedback.get(0).getAsJsonObject().toString();
        }
        if (jsonObject.has("UserName") && !jsonObject.get("UserName").isJsonNull()) {
            userName = jsonObject.get("UserName").getAsString();
        }

        response.addProperty("success", "success");

        String reportingParams = HelperMethods.getHeadersWithReportingParams(requestManager)
                .get(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        String devicename = null, os = null;
        String did = null;
        String channel_id = null;
        String appId = null;
        UserAgent userAgent = UserAgent
                .parseUserAgentString(requestManager.getHeadersHandler().getHeader("User-Agent"));

        if (StringUtils.isNotBlank(reportingParams)) {
            JSONObject reportingParamsJson = new JSONObject(
                    URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));

            did = reportingParamsJson.optString("did");
            channel_id = reportingParamsJson.optString("chnl");
            appId = reportingParamsJson.optString("aid");

            if (channel_id.equalsIgnoreCase("desktop")) {
                StringBuilder sb = new StringBuilder();
                sb.append(userAgent.getBrowser().getName());
                devicename = sb.toString();
                os = userAgent.getOperatingSystem().getName();
            } else {
                devicename = reportingParamsJson.optString("dm");
                os = reportingParamsJson.optString("plat") + " " + reportingParamsJson.optString("os");
            }
        }

        Map<String, String> input = new HashMap<>();
        input.put("logType", "CustomerActivity");
        input.put("moduleName", "Feedback");
        input.put("activityType", "Feedback activity");
        input.put("channel", channel_id);
        input.put("device", devicename);
        input.put("operatingSystem", os);
        input.put("deviceId", did);
        input.put("appId", appId);
        input.put("userName", userName);
        input.put("description", description);
        input.put("status", "Successful");
        input.put("eventts", String.valueOf(new Date().getTime()));
        input.put("createdBy", userName);

        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Kony-Log-Services-API-Access-Token", URLFinder.getPathUrl("LogServiceAccessToken"));
            headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            HelperMethods.callApiJson(requestManager, input, headers, URLConstants.LOGER_URL);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
        return;
    }

}