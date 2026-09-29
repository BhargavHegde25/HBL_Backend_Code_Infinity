package com.kony.dbputilities.customersecurityservices.postprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;

public class CheckIfUserExistsPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        JsonObject responsePayloadJson = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        JsonParser jsonparser = new JsonParser();

        JsonObject requestPayload = null;
        JsonObject addressInfo = null;
        JsonArray backenIdenInfo = null;
        JsonObject personalInfo = null;

        String addresInfoString = null;
        String backendIdentString = null;
        String payloadString = null;
        String personalInfoString = null;

        String isUserEnrolled = null;
        String isUserExists = null;

        if (JSONUtil.hasKey(responsePayloadJson, "isUserEnrolled")) {
            isUserEnrolled = responsePayloadJson.get("isUserEnrolled").getAsString();
        }
        if (JSONUtil.hasKey(responsePayloadJson, "isUserExists")) {
            isUserExists = responsePayloadJson.get("isUserExists").getAsString();
        }
        if (JSONUtil.hasKey(responsePayloadJson, "addressInformation")) {
            addresInfoString = responsePayloadJson.get("addressInformation").getAsString();
            addressInfo = jsonparser.parse(addresInfoString).getAsJsonObject();
        }
        if (JSONUtil.hasKey(responsePayloadJson, "personalInformation")) {
            personalInfoString = responsePayloadJson.get("personalInformation").getAsString();
            personalInfo = jsonparser.parse(personalInfoString).getAsJsonObject();
        }
        if (JSONUtil.hasKey(responsePayloadJson, "backendIdentifierInfo")) {
            backendIdentString = responsePayloadJson.get("backendIdentifierInfo").getAsString();
            backenIdenInfo = jsonparser.parse(backendIdentString).getAsJsonArray();
        }
        if (JSONUtil.hasKey(responsePayloadJson, "requestPayload")) {
            payloadString = responsePayloadJson.get("requestPayload").getAsString();
            requestPayload = jsonparser.parse(payloadString).getAsJsonObject();
        }

        updateResponsePayload(isUserEnrolled, isUserExists, addressInfo, personalInfo, backenIdenInfo, requestPayload,
                requestManager,
                responseManager);
    }

    private void updateResponsePayload(String isUserEnrolled, String isUserExists, JsonObject addressInfo,
            JsonObject personalInfo, JsonArray backenIdenInfo, JsonObject requestPayload,
            FabricRequestManager requestManager, FabricResponseManager responseManager) {
        JsonObject resultJson = new JsonObject();
        JsonParser jsonparser = new JsonParser();
        JsonObject responsePayloadJson = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

        if (!StringUtils.isBlank(isUserEnrolled) && "false".equals(isUserEnrolled)) {
            String commString = responsePayloadJson.get("communication").getAsString();
            JsonObject communication = jsonparser.parse(commString).getAsJsonObject();
            String serviceKey = updateDataInMFAService(requestManager, communication, requestPayload, null, null, null);
            if (StringUtils.isNotBlank(serviceKey)) {
                resultJson.addProperty("serviceKey", serviceKey);
                resultJson.addProperty("isUserEnrolled", "false");
                resultJson.addProperty("result", "User Not Enrolled");
                responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
            } else {
                ErrorCodeEnum.ERR_10540.setErrorCode(resultJson);
                responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
            }
        } else if (StringUtils.isBlank(isUserEnrolled) && "true".equals(isUserExists)) {
            String commString = responsePayloadJson.get("coreCommunication").getAsString();
            JsonObject corecommmunication = jsonparser.parse(commString).getAsJsonObject();
            String serviceKey = updateDataInMFAService(requestManager, corecommmunication, requestPayload, addressInfo,
                    backenIdenInfo, personalInfo);
            if (StringUtils.isNotBlank(serviceKey)) {
                resultJson.addProperty("serviceKey", serviceKey);
                resultJson.addProperty("isUserEnrolled", "false");
                resultJson.addProperty("result", "User Not Enrolled");
                responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
            } else {
                ErrorCodeEnum.ERR_10540.setErrorCode(resultJson);
                responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
            }
        } else if (!StringUtils.isBlank(isUserEnrolled) && "true".equals(isUserEnrolled)) {
            resultJson.addProperty("isUserEnrolled", "true");
            resultJson.addProperty("result", "User Already Enrolled");
            responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
        } else {

            resultJson.addProperty("errmsg", "Please provide valid Details.");
            ErrorCodeEnum.ERR_10630.setErrorCode(resultJson);
            responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
        }

    }

    private String updateDataInMFAService(FabricRequestManager requestManager, JsonObject communication,
            JsonObject requestPayload, JsonObject addressInfo, JsonArray backenIdentifierInfo,
            JsonObject personalInfo) {
        String serviceKey = HelperMethods.getNewId();
        String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        String retryCount = "0";
        String Createddts = HelperMethods.getCurrentTimeStamp();

        if (JSONUtil.isJsonNull(communication) || JSONUtil.isJsonNull(requestPayload)) {
            return null;
        }

        JsonObject json = new JsonObject();
        json.add("communication", communication);
        json.add("requestPayload", requestPayload);
        if (!JSONUtil.isJsonNull(addressInfo)) {
            json.add("addressInformation", addressInfo);
        }
        if (!JSONUtil.isJsonNull(personalInfo)) {
            json.add("personalInformation", personalInfo);
        }
        if (!JSONUtil.isJsonNull(backenIdentifierInfo)) {
            json.add("backendIdentifierInfo", backenIdentifierInfo);
        }

        String payload = json.toString();

        Map<String, String> mfaservice = new HashMap<>();
        mfaservice.put("serviceKey", serviceKey);
        mfaservice.put("serviceName", serviceName);
        mfaservice.put("Createddts", Createddts);
        mfaservice.put("retryCount", retryCount);
        try {
            payload = CryptoText.encrypt(payload);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }

        mfaservice.put("payload", payload);

        Result mfaserviceResult = new Result();
        mfaserviceResult = HelperMethods.callApi(requestManager, mfaservice, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_CREATE);
        if (HelperMethods.hasRecords(mfaserviceResult)) {
            return HelperMethods.getFieldValue(mfaserviceResult, "serviceKey");
        }
        return null;
    }

}
