package com.kony.dbputilities.mfa.preprocessors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PreLoginSMEnrollUtil;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.dataobject.Result;

public class RequestEnrollOTPPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    PreLoginSMEnrollUtil mfaUtil = null;

    PayloadHandler requestPayloadHandler = null;
    PayloadHandler responsePayloadHandler = null;

    FabricRequestChain requestChain = null;

    JsonObject mfaAttributes = null;

    String serviceKeycreatedTime = null;
    String userId = null;

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();

        JsonObject resultJson = new JsonObject();
        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());

        this.requestPayloadHandler = requestManager.getPayloadHandler();
        this.responsePayloadHandler = responseManager.getPayloadHandler();

        if (requestPayloadHandler.getPayloadAsJson() == null || requestPayloadHandler.getPayloadAsJson().isJsonNull()) {
            ErrorCodeEnum.ERR_10526.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

        boolean isMFAPresent = false;

        try {

            JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);
            if (mfaElement.isJsonObject()) {
                mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
            } else {
                JsonParser parser = new JsonParser();
                mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
            }
            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, MFAConstants.SERVICE_ID_PRELOGIN);
            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
            requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            isMFAPresent = true;

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (!isMFAPresent) {
            ErrorCodeEnum.ERR_10526.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        String serviceKey = null;
        String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        String communicationString = null;

        if (isMFAPresent) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)
                    && !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            }
        }

        if ((StringUtils.isBlank(serviceKey) && !serviceName.equals(MFAConstants.SERVICE_ID_PRELOGIN))) {
            ErrorCodeEnum.ERR_10527.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        communicationString = getCommunication(requestManager, serviceKey, serviceName);

        if (StringUtils.isBlank(communicationString)) {
            ErrorCodeEnum.ERR_10528.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        JsonParser parser = new JsonParser();
        JsonObject communication = parser.parse(communicationString).getAsJsonObject();

        mfaUtil = new PreLoginSMEnrollUtil(requestManager, communication, serviceKey, serviceKeycreatedTime);
        this.requestChain = requestChain;

        if (!(mfaUtil.isAppActionValid() && mfaUtil.isValidMFA())) {
            ErrorCodeEnum.ERR_10529.setErrorCode(resultJson, mfaUtil.getDbpErrMsg());
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (mfaUtil.isServiceKeyExpired()) {
            ErrorCodeEnum.ERR_10527.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        String MFAtype = mfaUtil.getMFAType();
        JsonObject mfajson = new JsonObject();

        if (MFAtype.equals(MFAConstants.SECURE_ACCESS_CODE)) {
            if (mfaAttributes.has("OTP") && mfaAttributes.get("OTP").isJsonObject()) {
                mfaUtil.setUserID(userId);
                if (StringUtils.isNotBlank(userId) && mfaUtil.isValidServiceKey()) {
                    if (isValidPayload(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName)) {
                        requestChain.execute();
                        return;
                    } else {
                        ErrorCodeEnum.ERR_10527.setErrorCode(resultJson);
                        responsePayloadHandler.updatePayloadAsJson(resultJson);
                    }
                }
            } else {
                if (mfaUtil.isValidMFA() && !mfaAttributes.has("OTP")) {
                    if (!mfaUtil.updateUserIdStatus()) {
                        ErrorCodeEnum.ERR_10530.setErrorCode(resultJson);
                        responsePayloadHandler.updatePayloadAsJson(resultJson);
                        return;
                    }
                    mfajson = mfaUtil.setserviceMFAAttributes();
                    responsePayloadHandler.updatePayloadAsJson(mfajson);
                    return;
                }
            }
        }

        ErrorCodeEnum.ERR_10526.setErrorCode(resultJson);
        responsePayloadHandler.updatePayloadAsJson(resultJson);
        return;
    }

    private boolean isValidPayload(JsonObject OTP, String serviceKey, String serviceName) {
        JsonObject referenceIdMappings = mfaUtil.getReferenceIdMappingsFromDB(serviceKey);
        if (mfaUtil.isValidPhone(OTP, false, referenceIdMappings)
                && mfaUtil.isValidEmail(OTP, false, referenceIdMappings)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
            if (OTP.has(MFAConstants.PHONE)) {
                jsonObject.add("Phone", referenceIdMappings.get(OTP.get("phone").getAsString()));
            }
            if (OTP.has(MFAConstants.EMAIL)) {
                jsonObject.add("Email", referenceIdMappings.get(OTP.get(MFAConstants.EMAIL).getAsString()));
            }

            jsonObject.addProperty(MFAConstants.SERVICE_NAME, serviceName);

            if (OTP.has(MFAConstants.SECURITY_KEY)) {
                jsonObject.addProperty(MFAConstants.SECURITY_KEY, OTP.get(MFAConstants.SECURITY_KEY).getAsString());
            }

            requestPayloadHandler.updatePayloadAsJson(jsonObject);
            return true;
        }

        return false;
    }

    public String getCommunication(FabricRequestManager requestManager, String serviceKey, String serviceName) {

        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + "serviceName" + DBPUtilitiesConstants.EQUAL + serviceName;
        Result result = HelperMethods.callGetApi(requestManager, filterQuery, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_GET);

        if (!HelperMethods.hasRecords(result)) {
            return null;
        }

        serviceKeycreatedTime = HelperMethods.getFieldValue(result, "Createddts");
        userId = HelperMethods.getFieldValue(result, "User_id");

        String payload = HelperMethods.getFieldValue(result, "payload");

        try {
            if (StringUtils.isNotBlank(payload)) {
                payload = CryptoText.decrypt(payload);
            } else {
                return null;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject payloadjson = parser.parse(payload).getAsJsonObject();
        if (payloadjson.has("communication")) {
            JsonObject commJson = payloadjson.get("communication").getAsJsonObject();
            return commJson.toString();
        }
        return null;
    }

}