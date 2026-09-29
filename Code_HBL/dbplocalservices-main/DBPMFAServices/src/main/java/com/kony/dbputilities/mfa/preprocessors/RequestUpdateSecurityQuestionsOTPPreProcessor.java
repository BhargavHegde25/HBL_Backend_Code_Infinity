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
import com.kony.dbputilities.mfa.PreLoginUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class RequestUpdateSecurityQuestionsOTPPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    PreLoginUtil mfaUtil = null;

    PayloadHandler requestPayloadHandler = null;
    PayloadHandler responsePayloadHandler = null;
    FabricRequestChain requestChain = null;
    JsonObject mfaAttributes = null;

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();
        JsonObject resultJson = new JsonObject();
        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());
        this.requestPayloadHandler = requestManager.getPayloadHandler();
        this.responsePayloadHandler = responseManager.getPayloadHandler();
        if (requestPayloadHandler.getPayloadAsJson() == null) {
            ErrorCodeEnum.ERR_10585.setErrorCode(resultJson);
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

        String serviceKey = null;
        String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;

        if (isMFAPresent) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)
                    && !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            }
        }

        if ((StringUtils.isBlank(serviceKey) && !serviceName.equals(MFAConstants.SERVICE_ID_PRELOGIN))) {
            ErrorCodeEnum.ERR_10586.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        this.requestChain = requestChain;

        mfaUtil = new PreLoginUtil(requestManager, serviceKey);

        if (!mfaUtil.isAppActionValid()) {
            ErrorCodeEnum.ERR_10585.setErrorCode(resultJson, mfaUtil.getDbpErrMsg());
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (!mfaUtil.isValidMFA()) {
            ErrorCodeEnum.ERR_10586.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (!mfaUtil.isValidOnlyServiceKey(serviceName)) {
            ErrorCodeEnum.ERR_10587.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        String MFAtype = MFAConstants.SECURE_ACCESS_CODE;
        JsonObject mfajson = new JsonObject();

        String mfaservice_userid = mfaUtil.getMFAUserId();

        if (MFAtype.equals(MFAConstants.SECURE_ACCESS_CODE)) {
            if (StringUtils.isNotBlank(mfaservice_userid) && mfaAttributes.has("OTP")
                    && mfaAttributes.get("OTP").isJsonObject()
                    && isValidPayload(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName)) {
                requestChain.execute();
                return;
            } else {
                mfaUtil.updateUserId();
                mfajson = mfaUtil.setserviceMFAAttributes();
                responsePayloadHandler.updatePayloadAsJson(mfajson);
                return;
            }
        }

        ErrorCodeEnum.ERR_10588.setErrorCode(resultJson);
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
                jsonObject.add("Phone", referenceIdMappings.get(OTP.get(MFAConstants.PHONE).getAsString()));
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

}