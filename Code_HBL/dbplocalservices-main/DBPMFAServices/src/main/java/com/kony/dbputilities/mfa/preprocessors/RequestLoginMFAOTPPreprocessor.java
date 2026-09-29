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
import com.kony.dbputilities.mfa.LoginMFAUtil;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.utils.OTPUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class RequestLoginMFAOTPPreprocessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();
        LoginMFAUtil mfaUtil = null;

        PayloadHandler requestPayloadHandler = null;
        PayloadHandler responsePayloadHandler = null;
        JsonObject mfaAttributes = null;
        JsonObject resultJson = new JsonObject();
        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());
        requestPayloadHandler = requestManager.getPayloadHandler();
        responsePayloadHandler = responseManager.getPayloadHandler();
        if (requestPayloadHandler.getPayloadAsJson() == null) {
            ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
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

        mfaUtil = new LoginMFAUtil(requestManager);

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
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (!mfaUtil.getMFaModeforRequestfromDB(serviceKey, serviceName)) {
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);

            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (!mfaUtil.isAppActionValid()) {
            ErrorCodeEnum.ERR_10501.setErrorCode(resultJson, mfaUtil.getDbpErrMsg());
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }
        if (!mfaUtil.isValidServiceKey(serviceKey, serviceName)) {
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);

            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        String MFAtype = mfaUtil.mfaConfigurationUtil.getMFAType();

        if (MFAtype.equals(MFAConstants.SECURE_ACCESS_CODE) && null != mfaAttributes && mfaAttributes.has("OTP")
                && mfaAttributes.get("OTP").isJsonObject()) {
            if (isValidPayload(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                    requestPayloadHandler)) {
                requestChain.execute();
                return;
            }
        }

        ErrorCodeEnum.ERR_10503.setErrorCode(resultJson);
        responsePayloadHandler.updatePayloadAsJson(resultJson);
        return;
    }

    private boolean isValidPayload(JsonObject OTP, String serviceKey, String serviceName, LoginMFAUtil mfaUtil,
            PayloadHandler requestPayloadHandler) {

        JsonObject referenceIdMappings = mfaUtil.getSecurityQuestionsJsonObjectFromDB();
        if (OTPUtil.isValidPhone(OTP, false, referenceIdMappings)
                && OTPUtil.isValidEmail(OTP, false, referenceIdMappings)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
            if (OTP.has(MFAConstants.PHONE)) {
                jsonObject.addProperty("Phone", referenceIdMappings.get(OTP.get("phone").getAsString()).getAsString());
            }
            if (OTP.has(MFAConstants.EMAIL)) {
                jsonObject.addProperty("Email", referenceIdMappings.get(OTP.get("email").getAsString()).getAsString());
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

}