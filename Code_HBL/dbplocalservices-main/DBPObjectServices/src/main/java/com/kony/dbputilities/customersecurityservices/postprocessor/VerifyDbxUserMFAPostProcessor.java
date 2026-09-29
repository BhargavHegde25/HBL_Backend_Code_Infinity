package com.kony.dbputilities.customersecurityservices.postprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.dataobject.Result;

public class VerifyDbxUserMFAPostProcessor implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
            throws Exception {
        PayloadHandler responsepayload = responseManager.getPayloadHandler();
        Log4j2Configurator.getInstance();
        if (responsepayload == null) {
            return true;
        }

        JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

        if (jsonObject.has(DBPUtilitiesConstants.VALIDATION_ERROR)
                || jsonObject.has(DBPConstants.DBP_ERROR_MESSAGE_KEY)) {
            responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
            return true;
        }
        JsonObject resultJson = new JsonObject();
        String isUserExists = jsonObject.get("isUserExists").getAsString();

        if ("true".equalsIgnoreCase(isUserExists)) {

            String serviceKey = updateDataInMFAService(requestManager, jsonObject);
            if (StringUtils.isNotBlank(serviceKey)) {
                jsonObject.addProperty("serviceKey", serviceKey);
            } else {
                ErrorCodeEnum.ERR_10539.setErrorCode(resultJson);
                responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
                return true;
            }
        }
        responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
        return true;
    }

    private String updateDataInMFAService(FabricRequestManager requestManager, JsonObject jsonObject) {
        String serviceKey = HelperMethods.getNewId();
        String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        String retryCount = "0";
        String createddts = HelperMethods.getCurrentTimeStamp();

        JsonArray jsonarray = jsonObject.get("user_attributes").getAsJsonArray();
        StringBuilder string = new StringBuilder();
        JsonArray userAttributes = new JsonArray();
        for (JsonElement jsonelement : jsonarray) {
            JsonObject json = new JsonObject();
            json.addProperty("Status_id", jsonelement.getAsJsonObject().get("Status_id").getAsString());
            json.addProperty("id", jsonelement.getAsJsonObject().get("id").getAsString());
            json.addProperty("UserName", jsonelement.getAsJsonObject().get("UserName").getAsString());
            userAttributes.add(json);
        }
        int size = jsonarray.size();

        for (int i = 0; i < jsonarray.size(); i++) {
            JsonObject json = jsonarray.get(i).getAsJsonObject();
            string.append(json.get("UserName").getAsString());

            if (i != size - 1) {
                string.append(",");
            }
        }
        String payload = string.toString();

        Map<String, String> mfaservice = new HashMap<>();
        mfaservice.put("serviceKey", serviceKey);
        mfaservice.put("serviceName", serviceName);
        mfaservice.put("Createddts", createddts);
        mfaservice.put("retryCount", retryCount);
        try {
            mfaservice.put("payload", CryptoText.encrypt(payload));
            mfaservice.put("securityQuestions", CryptoText.encrypt(userAttributes.toString()));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        Result mfaserviceResult = HelperMethods.callApi(requestManager, mfaservice,
                HelperMethods.getHeaders(requestManager), URLConstants.MFA_SERVICE_CREATE);
        if (HelperMethods.hasRecords(mfaserviceResult)) {
            return HelperMethods.getFieldValue(mfaserviceResult, "serviceKey");
        }
        return null;
    }

}
