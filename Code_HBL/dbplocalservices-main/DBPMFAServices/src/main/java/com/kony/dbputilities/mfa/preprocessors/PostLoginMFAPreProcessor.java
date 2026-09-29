package com.kony.dbputilities.mfa.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class PostLoginMFAPreProcessor  implements ObjectServicePreProcessor, ObjectProcessorTask{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();
        PostLoginMFAUtil mfaUtil = null;

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

        mfaUtil = new PostLoginMFAUtil(requestManager, HelperMethods.getCustomerIdFromSession(requestManager));

        if (mfaUtil.MFABypassed(requestpayload)) {
            requestChain.execute();
            return;
        }

        boolean isMFAPresent = false;
        try {
            JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);

            if (mfaElement.isJsonObject()) {
                mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
            } else {
                JsonParser parser = new JsonParser();
                mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
            }

            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
            requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            isMFAPresent = true;
        } catch (Exception e) {

            alert.prepareError(e.getMessage()).log();
        }

        String serviceKey = null;
        String serviceName = null;
        
        OperationData operationData = requestManager.getServicesManager().getOperationData();
        String serviceId = operationData.getServiceId();
        String objectId = operationData.getObjectId();
        String operationId = operationData.getOperationId();
        String appendedString = String.join("_", serviceId, objectId, operationId );

        serviceName = mfaUtil.getValidServiceName(requestpayload, appendedString.toLowerCase());

        if (isMFAPresent) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)
                    && !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            }
        } else {
            mfaAttributes = new JsonObject();
        }

        if (StringUtils.isBlank(serviceName)) {
            ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
        requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);

        requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
        requestPayloadHandler.updatePayloadAsJson(requestpayload);


        if (StringUtils.isBlank(serviceKey)) {
            mfaUtil.getMfaModeforRequest();
        } else {
            mfaUtil.getMFaModeforRequestfromDB(serviceKey);
        }
        
        if (!mfaUtil.isAppActionValid() || ((mfaUtil.mfaConfigurationUtil != null) && !mfaUtil.mfaConfigurationUtil.isValidMFA())) {
            mfaUtil.setError(resultJson, ErrorCodeEnum.ERR_10501.getErrorCodeAsString(),
                    mfaUtil.getDbpErrMsg() + "  " + ErrorConstants.UNABLE_TO_DETERMINE_C360_CONFIGURATION);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if ((mfaUtil.mfaConfigurationUtil == null) || ((!mfaUtil.isMFARequired(requestpayload)) && StringUtils.isBlank(serviceKey))) {
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
            handleOTP(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                    responsePayloadHandler, responsePayloadHandler, requestChain);
            return;
        }
        
        diagnostic.prepareDebug("mfaAttributes ##"+mfaAttributes.toString() ).log();
        if (MFAtype.equals(MFAConstants.TRANSACTION_PIN) && null != mfaAttributes && mfaAttributes.has("PIN")
                && mfaAttributes.get("PIN").isJsonObject()) {
        	diagnostic.prepareDebug("Inside transaction Pin##");
        	validatePIN(mfaAttributes.get("PIN").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                    responsePayloadHandler, requestPayloadHandler, requestChain);
            return;
        }

        if (MFAtype.equals(MFAConstants.SECURITY_QUESTIONS) && mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)
                && mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).isJsonArray()) {
            handleSecurityQuestions(mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).getAsJsonArray(), serviceKey,
                    serviceName, mfaUtil, responsePayloadHandler, responsePayloadHandler, requestChain);
            return;
        }

        ErrorCodeEnum.ERR_10503.setErrorCode(resultJson);

        responsePayloadHandler.updatePayloadAsJson(resultJson);

        return;

    }
    
    private void validatePIN(JsonObject PIN, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler,
            FabricRequestChain requestChain) {
    	diagnostic.prepareDebug("validatePIN ##"+ PIN.toString()).log();
    	JsonObject resultJson = new JsonObject();
        if (!PIN.has(MFAConstants.PIN)) {
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        } else {
            resultJson = mfaUtil.verifyPIN(PIN);
            if (!resultJson.has("success")) {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return;
            }
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        requestJson.remove("mfaMode");
        requestChain.execute();
    }

    private void handleSecurityQuestions(JsonArray jsonArray, String serviceKey, String serviceName,
            PostLoginMFAUtil mfaUtil, PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler,
            FabricRequestChain requestChain) {

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

    private void handleOTP(JsonObject OTP, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler,
            FabricRequestChain requestChain) {

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

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
            throws Exception {
        PostLoginMFAUtil mfaUtil = null;

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
            return false;
        }

        JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
        String serviceName = requestPayload.has(MFAConstants.SERVICE_NAME) && !requestPayload.get(MFAConstants.SERVICE_NAME).isJsonNull() ? requestPayload.get(MFAConstants.SERVICE_NAME).getAsString() : null;
        
        mfaUtil = new PostLoginMFAUtil(requestManager, HelperMethods.getCustomerIdFromSession(requestManager));
        
        if(StringUtils.isBlank(serviceName)) {
            OperationData operationData = requestManager.getServicesManager().getOperationData();
            String serviceId = operationData.getServiceId();
            String objectId = operationData.getObjectId();
            String operationId = operationData.getOperationId();
            String appendedString = String.join("_", serviceId, objectId, operationId );
            serviceName = mfaUtil.getValidServiceName(requestPayload, appendedString.toLowerCase());
        }
        
        boolean isMFAPresent = false;
        try {
            JsonElement mfaElement = requestPayload.get(MFAConstants.MFA_ATTRIBUTES);

            if (mfaElement.isJsonObject()) {
                mfaAttributes = requestPayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
            } else {
                JsonParser parser = new JsonParser();
                mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
            }

            requestPayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
            requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
            isMFAPresent = true;
        } catch (Exception e) {

            alert.prepareError(e.getMessage()).log();
        }

        String serviceKey = null;

        if (isMFAPresent) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)
                    && !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            }
        } else {
            mfaAttributes = new JsonObject();
        }

        if (StringUtils.isBlank(serviceName)) {
            ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
        requestPayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);

        requestPayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
        requestPayloadHandler.updatePayloadAsJson(requestPayload);

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(requestManager);
        
        if ((mfaUtil.MFABypassed(requestPayload, requestManager) || !HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) && StringUtils.isBlank(serviceKey)) {
            return true;
        }

        if (StringUtils.isBlank(serviceKey)) {
            mfaUtil.getMfaModeforRequest();
        } else {
            
            if (!mfaUtil.isValidServiceKey(serviceKey, serviceName)) {
                ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);

                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
            mfaUtil.getMFaModeforRequestfromDB(serviceKey);
        }
        
        if (!mfaUtil.isAppActionValid() || ((mfaUtil.mfaConfigurationUtil != null) && !mfaUtil.mfaConfigurationUtil.isValidMFA())) {
            mfaUtil.setError(resultJson, ErrorCodeEnum.ERR_10501.getErrorCodeAsString(),
                    mfaUtil.getDbpErrMsg() + "  " + ErrorConstants.UNABLE_TO_DETERMINE_C360_CONFIGURATION);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        if ((mfaUtil.mfaConfigurationUtil == null) || ((!mfaUtil.isMFARequired(requestPayload)) && StringUtils.isBlank(serviceKey))) {
            return true;
        }

        if (StringUtils.isBlank(serviceKey)) {
            resultJson = mfaUtil.setserviceMFAAttributes();
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        String MFAtype = mfaUtil.getMFAType();

        if (MFAtype.equals(MFAConstants.SECURE_ACCESS_CODE) && mfaAttributes.has("OTP")
                && mfaAttributes.get("OTP").isJsonObject()) {
            return handleOTP(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                    responsePayloadHandler, requestPayloadHandler);
        }
        
        if (MFAtype.equals(MFAConstants.TRANSACTION_PIN) && mfaAttributes.has("PIN")
                && mfaAttributes.get("PIN").isJsonObject()) {
            return validatePIN(mfaAttributes.get("PIN").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                    responsePayloadHandler, requestPayloadHandler);
        }

        if (MFAtype.equals(MFAConstants.SECURITY_QUESTIONS) && mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)
                && mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).isJsonArray()) {
            return handleSecurityQuestions(mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).getAsJsonArray(), serviceKey,
                    serviceName, mfaUtil, responsePayloadHandler, requestPayloadHandler);
        }

        ErrorCodeEnum.ERR_10503.setErrorCode(resultJson);

        responsePayloadHandler.updatePayloadAsJson(resultJson);

        return false;
    }

    private boolean handleSecurityQuestions(JsonArray jsonArray, String serviceKey, String serviceName,
            PostLoginMFAUtil mfaUtil, PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler) {

        JsonObject resultJson = new JsonObject();

        resultJson = mfaUtil.verifySecurityQuestions(jsonArray);
        if (!resultJson.has("success")) {
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        JsonObject requestJson = mfaUtil.getRequestPayloadWithoutMFAAttributes(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        return true;
    }

    private boolean handleOTP(JsonObject OTP, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler) {

        JsonObject resultJson = new JsonObject();
        if (!OTP.has(MFAConstants.OTP_OTP)) {
            resultJson = mfaUtil.requestOTP(OTP, resultJson, serviceKey, true);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        } else {
            resultJson = mfaUtil.verifyOTP(OTP);
            if (!resultJson.has("success")) {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
        }

        JsonObject requestJson = mfaUtil.getRequestPayloadWithoutMFAAttributes(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        return true;
    }
    
    private boolean validatePIN(JsonObject PIN, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler) {
    	diagnostic.prepareDebug("validatePIN ##"+ PIN.toString());
    	JsonObject resultJson = new JsonObject();
        if (!PIN.has(MFAConstants.PIN)) {
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        } else {
            resultJson = mfaUtil.verifyPIN(PIN);
            if (!resultJson.has("success")) {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
        }

        JsonObject requestJson = mfaUtil.getRequestPayloadWithoutMFAAttributes(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        return true;
    }
        
}
