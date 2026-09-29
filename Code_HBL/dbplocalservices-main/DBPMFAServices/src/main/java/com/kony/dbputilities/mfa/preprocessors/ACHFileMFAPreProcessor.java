package com.kony.dbputilities.mfa.preprocessors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
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

public class ACHFileMFAPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();
        alert.prepareError("--- ACHFileMFAPreProcessor: Entered").log();
		
    	PostLoginMFAUtil mfaUtil = null;
    	JsonObject resultJson = new JsonObject();
        JsonObject mfaAttributes = new JsonObject();
        String serviceKey = null;
        String serviceName = null;

        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());
        PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
        PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
        if (requestPayloadHandler.getPayloadAsJson() == null) {
            ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
        
        String loggedInUserID = HelperMethods.getCustomerIdFromSession(requestManager);
        
        try {
            JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);

            if (mfaElement != null) {
                if (mfaElement.isJsonObject()) {
                    mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
                } else {
                    JsonParser parser = new JsonParser();
                    mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
                }
                requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
                requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            }

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        boolean isRequestOriginal = isRequestOriginal(requestpayload, requestManager, responsePayloadHandler);
        boolean isRequestForMFA = isRequestForMFA(requestpayload, mfaAttributes);

        alert.prepareError("IsRequestOriginal / isRequestForMFA : " + isRequestOriginal + " / " + isRequestForMFA).log();

        if (isRequestOriginal) {
            requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
            serviceName = MFAConstants.ACH_FILE_UPLOAD;
            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
            requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
            requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
        }

        if (isRequestForMFA) {
            serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            Result exisingresult = getMFAServiceRecord(serviceKey, requestManager);
            if (!HelperMethods.hasRecords(exisingresult)) {
                ErrorCodeEnum.ERR_13000.setErrorCode(resultJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return;
            } else if (loggedInUserID.equals(HelperMethods.getFieldValue(exisingresult, MFAConstants.MFA_USER_ID))) {
                serviceName = HelperMethods.getFieldValue(exisingresult, MFAConstants.SERVICE_NAME);
                mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
                requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
                requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
                requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            } else {
                ErrorCodeEnum.ERR_13003.setErrorCode(resultJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return;
            }

        }
        
        if (!(isRequestOriginal ^ isRequestForMFA)) {
            ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        mfaUtil = new PostLoginMFAUtil(requestManager, loggedInUserID);

        if (StringUtils.isBlank(serviceKey)) {
            alert.prepareError("---- ACH ---- New request.").log();
            mfaUtil.getMfaModeforRequest();
        } else {
            alert.prepareError("---- ACH ---- OLD request.").log();
            mfaUtil.getMFaModeforRequestfromDB(serviceKey);
        }

        if (!mfaUtil.isAppActionValid() || ((mfaUtil.mfaConfigurationUtil != null) && !mfaUtil.mfaConfigurationUtil.isValidMFA())) {
            ErrorCodeEnum.ERR_10501.setErrorCode(resultJson, mfaUtil.getDbpErrMsg());
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if ((mfaUtil.mfaConfigurationUtil == null) || (!mfaUtil.isMFARequired(requestpayload)) && StringUtils.isBlank(serviceKey)) {
            requestChain.execute();
            return;
        }

        if (StringUtils.isBlank(serviceKey)) {
            resultJson = mfaUtil.setserviceMFAAttributes();
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (!mfaUtil.isValidServiceKey(serviceKey, serviceName)) {
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        String MFAtype = mfaUtil.getMFAType();

        if (MFAtype.equals(MFAConstants.SECURE_ACCESS_CODE) && mfaAttributes.has("OTP")
                && mfaAttributes.get("OTP").isJsonObject()) {
            handleOTP(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, requestPayloadHandler,
                    responsePayloadHandler, requestChain, mfaUtil);
            return;
        }

        if (MFAtype.equals(MFAConstants.SECURITY_QUESTIONS) && mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)
                && mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).isJsonArray()) {
            handleSecurityQuestions(mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).getAsJsonArray(), serviceKey,
                    serviceName, requestPayloadHandler, responsePayloadHandler, requestChain, mfaUtil);
            return;
        }

        ErrorCodeEnum.ERR_13001.setErrorCode(resultJson);
        responsePayloadHandler.updatePayloadAsJson(resultJson);

        return;

    }

    private void handleSecurityQuestions(JsonArray jsonArray, String serviceKey, String serviceName,
            PayloadHandler requestPayloadHandler, PayloadHandler responsePayloadHandler,
            FabricRequestChain requestChain, PostLoginMFAUtil mfaUtil) {
        JsonObject resultJson = new JsonObject();

        resultJson = mfaUtil.verifySecurityQuestions(jsonArray);
        if (!resultJson.has("success")) {
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        requestChain.execute();
    }

    private void handleOTP(JsonObject OTP, String serviceKey, String serviceName, PayloadHandler requestPayloadHandler,
            PayloadHandler responsePayloadHandler, FabricRequestChain requestChain, PostLoginMFAUtil mfaUtil) {
        JsonObject resultJson = new JsonObject();
        if (!OTP.has(MFAConstants.OTP_OTP)) {
            resultJson = mfaUtil.requestOTP(OTP, resultJson, serviceKey, true);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        } else {
            resultJson = mfaUtil.verifyOTP(OTP);
            if (!resultJson.has("success")) {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return;
            }
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        requestChain.execute();
    }

    private boolean isRequestOriginal(JsonObject requestPayload, FabricRequestManager requestManager,
            PayloadHandler responsePayloadHandler) {
        boolean isRequestOriginal = false;
        if (requestPayload.has(MFAConstants.ACH_FILE_CONTENTS)) {
            isRequestOriginal = requestPayload.get(MFAConstants.ACH_FILE_CONTENTS).isJsonNull() ? false : true;
            if (isRequestOriginal) {
                isRequestOriginal = StringUtils
                        .isNotBlank(requestPayload.get(MFAConstants.ACH_FILE_CONTENTS).getAsString());
            }
        }

        return isRequestOriginal;
    }

    private boolean isRequestForMFA(JsonObject requestPayload, JsonObject mfaAttributes) {
        boolean isRequestForMFA = false;
        if (requestPayload.has(MFAConstants.MFA_ATTRIBUTES)) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)) {
                isRequestForMFA = mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull() ? false : true;
            }
        }
        return isRequestForMFA;
    }
    
    private Result getMFAServiceRecord(String serviceKey, FabricRequestManager requestManager) {

        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey;
        Result result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_GET);
        return result;
    }
    
    
    
}
