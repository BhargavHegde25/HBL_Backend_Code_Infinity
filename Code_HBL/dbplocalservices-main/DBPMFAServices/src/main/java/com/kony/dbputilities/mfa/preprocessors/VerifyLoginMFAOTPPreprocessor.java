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
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class VerifyLoginMFAOTPPreprocessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();
        PayloadHandler requestPayloadHandler = null;
        PayloadHandler responsePayloadHandler = null;
        JsonObject mfaAttributes = null;
        JsonObject resultJson = new JsonObject();
        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());
        requestPayloadHandler = requestManager.getPayloadHandler();
        responsePayloadHandler = responseManager.getPayloadHandler();
        if (requestPayloadHandler.getPayloadAsJson() == null) {
            ErrorCodeEnum.ERR_10512.setErrorCode(resultJson);
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

            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, MFAConstants.SERVICE_ID_LOGIN);

            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
            requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            isMFAPresent = true;
        } catch (Exception e) {

            alert.prepareError(e.getMessage()).log();
        }

        String serviceKey = null;
        String serviceName = null;
        if (isMFAPresent) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)
                    && !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            }
            if (mfaAttributes.has(MFAConstants.SERVICE_NAME)
                    && !mfaAttributes.get(MFAConstants.SERVICE_NAME).isJsonNull()) {
                serviceName = mfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();
            }
        }

        if ((StringUtils.isBlank(serviceKey) && !MFAConstants.SERVICE_ID_LOGIN.equals(serviceName))) {
            ErrorCodeEnum.ERR_10512.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);

            return;
        }
        if (null != mfaAttributes && !mfaAttributes.isJsonNull()) {
            requestpayload = getPayload(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName);
        }

        requestPayloadHandler.updatePayloadAsJson(requestpayload);
        requestChain.execute();

    }

    private JsonObject getPayload(JsonObject OTP, String serviceKey, String serviceName) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
        jsonObject.addProperty("Otp", OTP.get(MFAConstants.OTP_OTP).getAsString());
        if (OTP.has(MFAConstants.SECURITY_KEY)) {
            jsonObject.addProperty(MFAConstants.SECURITY_KEY, OTP.get(MFAConstants.SECURITY_KEY).getAsString());
        }
        jsonObject.addProperty(MFAConstants.SERVICE_NAME, serviceName);
        return jsonObject;
    }

}
