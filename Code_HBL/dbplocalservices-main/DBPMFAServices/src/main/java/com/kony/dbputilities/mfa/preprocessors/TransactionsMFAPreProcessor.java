package com.kony.dbputilities.mfa.preprocessors;

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
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class TransactionsMFAPreProcessor implements ObjectServicePreProcessor, ObjectProcessorTask {
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

        mfaUtil = new PostLoginMFAUtil(requestManager);
        if ((requestpayload.has("validate") && requestpayload.get("validate").getAsString().trim().equalsIgnoreCase("true")) 
        		|| mfaUtil.MFABypassed(requestpayload)) {
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

        if (isMFAPresent) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)
                    && !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            }
            else {
                serviceName = mfaUtil.getValidServiceName(requestpayload, appendedString.toLowerCase());
                if(serviceName == null) {
                    ErrorCodeEnum.ERR_10501.setErrorCode(resultJson);
                    responsePayloadHandler.updatePayloadAsJson(resultJson);
                    return;
                }
                mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
                requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
            }

        }

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

        if ( (mfaUtil.mfaConfigurationUtil == null) || ((!mfaUtil.isMFARequired(requestpayload)) && StringUtils.isBlank(serviceKey))) {
            requestChain.execute();
            return;
        }

        String mfaType = mfaUtil.getMFAType();

        String state = MFAConstants.REQUEST;
        if (mfaAttributes.has("OTP")) {
            JsonObject OTP = new JsonObject();
            if (mfaAttributes.get("OTP").isJsonObject()) {
                OTP = mfaAttributes.get("OTP").getAsJsonObject();
            }

            if (OTP.has(MFAConstants.OTP_OTP)) {
                state = MFAConstants.VERIFY;
            } else if (OTP.has(MFAConstants.SECURITY_KEY)) {
                state = MFAConstants.RESEND;
            } else {
                state = MFAConstants.REQUEST;
            }
        } else if (mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)) {
            state = MFAConstants.VERIFY;
        }

        if (StringUtils.isBlank(serviceKey)) {
            resultJson = mfaUtil.setserviceMFAAttributes();
            addState(resultJson, state, mfaType);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (!mfaUtil.isValidServiceKey(serviceKey, serviceName)) {
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
            addState(resultJson, state, mfaType);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (mfaType.equals(MFAConstants.SECURE_ACCESS_CODE) && mfaAttributes.has("OTP")
                && mfaAttributes.get("OTP").isJsonObject()) {
            handleOTP(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                    responsePayloadHandler, requestPayloadHandler, requestChain, state, mfaType);
            return;
        }

        if (mfaType.equals(MFAConstants.SECURITY_QUESTIONS) && mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)
                && mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).isJsonArray()) {
            handleSecurityQuestions(mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).getAsJsonArray(), serviceKey,
                    serviceName, mfaUtil, responsePayloadHandler, requestPayloadHandler, requestChain, state, mfaType);
            return;
        }

        ErrorCodeEnum.ERR_10503.setErrorCode(resultJson);

        addState(resultJson, state, mfaType);
        responsePayloadHandler.updatePayloadAsJson(resultJson);

        return;

    }

    private void handleSecurityQuestions(JsonArray jsonArray, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler,
            FabricRequestChain requestChain, String state, String mfaType) {

        JsonObject resultJson = new JsonObject();

        resultJson = mfaUtil.verifySecurityQuestions(jsonArray);
        if (!resultJson.has("success")) {
            addState(resultJson, state, mfaType);
            resultJson.addProperty(ErrorCodeEnum.OPSTATUS_CODE, 0);
            resultJson.addProperty(ErrorCodeEnum.HTTPSTATUS_CODE, 0);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        addState(resultJson, state, mfaType);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        requestChain.execute();
    }

    protected boolean handleSecurityQuestions(JsonArray jsonArray, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler, String state, String mfaType) {

        JsonObject resultJson = new JsonObject();
        resultJson = mfaUtil.verifySecurityQuestions(jsonArray);
        if (!resultJson.has("success")) {
            addState(resultJson, state, mfaType);
            resultJson.addProperty(ErrorCodeEnum.OPSTATUS_CODE, 0);
            resultJson.addProperty(ErrorCodeEnum.HTTPSTATUS_CODE, 0);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        addState(resultJson, state, mfaType);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        return true;
    }

    private void handleOTP(JsonObject OTP, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler,
            FabricRequestChain requestChain, String state, String mfaType) {

        JsonObject resultJson = new JsonObject();
        if (!OTP.has(MFAConstants.OTP_OTP)) {
            resultJson = mfaUtil.requestOTP(OTP, resultJson, serviceKey, true);
            resultJson.addProperty(ErrorCodeEnum.OPSTATUS_CODE, 0);
            resultJson.addProperty(ErrorCodeEnum.HTTPSTATUS_CODE, 0);
            addState(resultJson, state, mfaType);
            JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
            addState(requestJson, state, mfaType);
            requestJson.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
            requestPayloadHandler.updatePayloadAsJson(requestJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        } else {
            resultJson = mfaUtil.verifyOTP(OTP);
            if (!resultJson.has("success")) {
                resultJson.addProperty(ErrorCodeEnum.OPSTATUS_CODE, 0);
                resultJson.addProperty(ErrorCodeEnum.HTTPSTATUS_CODE, 0);
                JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
                addState(requestJson, state, mfaType);
                requestJson.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
                requestPayloadHandler.updatePayloadAsJson(requestJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return;
            }
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        addState(resultJson, state, mfaType);
        addState(requestJson, state, mfaType);
        requestJson.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        requestChain.execute();
    }

    protected boolean handleOTP(JsonObject OTP, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler, String state, String mfaType) {
        JsonObject resultJson = new JsonObject();
        if (!OTP.has(MFAConstants.OTP_OTP)) {
            resultJson = mfaUtil.requestOTP(OTP, resultJson, serviceKey, true);
            resultJson.addProperty(ErrorCodeEnum.OPSTATUS_CODE, 0);
            resultJson.addProperty(ErrorCodeEnum.HTTPSTATUS_CODE, 0);
            addState(resultJson, state, mfaType);
            JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
            addState(requestJson, state, mfaType);
            requestJson.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
            requestPayloadHandler.updatePayloadAsJson(requestJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        } else {
            resultJson = mfaUtil.verifyOTP(OTP);
            if (!resultJson.has("success")) {
                resultJson.addProperty(ErrorCodeEnum.OPSTATUS_CODE, 0);
                resultJson.addProperty(ErrorCodeEnum.HTTPSTATUS_CODE, 0);
                JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
                addState(requestJson, state, mfaType);
                requestJson.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
                requestPayloadHandler.updatePayloadAsJson(requestJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        addState(resultJson, state, mfaType);
        addState(requestJson, state, mfaType);
        requestJson.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        return true;
    }

    protected void addState(JsonObject jsonObject, String state, String mfaType) {
        jsonObject.addProperty("mfaState", state);
        jsonObject.addProperty("mfaType", mfaType);
    }

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
            throws Exception {

        JsonObject resultJson = new JsonObject();
        PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
        PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
        String state = MFAConstants.REQUEST;
        String mfaType = "";
        try {
            PostLoginMFAUtil mfaUtil = null;

            JsonObject mfaAttributes = null;

            OperationData operationData = requestManager.getServicesManager().getOperationData();
            String serviceId = operationData.getServiceId();
            String objectId = operationData.getObjectId();
            String operationId = operationData.getOperationId();
            String appendedString = String.join("_", serviceId, objectId, operationId );
            responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                    ContentType.APPLICATION_JSON.getMimeType());

            if (requestPayloadHandler.getPayloadAsJson() == null) {
                ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }

            JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

            mfaUtil = new PostLoginMFAUtil(requestManager);

            if ((requestpayload.has("validate") && requestpayload.get("validate").getAsString().trim().equalsIgnoreCase("true")) 
            		|| mfaUtil.MFABypassed(requestpayload)) {
                return true;
            }

            try {
                JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);

                if (mfaElement != null && mfaElement.isJsonObject()) {
                    mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
                } else if(mfaElement != null) {
                    JsonParser parser = new JsonParser();
                    mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
                }
                else {
                    mfaAttributes = new JsonObject();
                }
            } catch (Exception e) {

                alert.prepareError(e.getMessage()).log();
            }

            String serviceKey = null;

            String serviceName = null;


            if (JSONUtil.hasKey(mfaAttributes, MFAConstants.SERVICE_KEY)) {
                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            }
            
            serviceName = mfaUtil.getValidServiceName(requestpayload,appendedString.toLowerCase());
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
                return false;
            }

            if ((mfaUtil.mfaConfigurationUtil == null) || ((!mfaUtil.isMFARequired(requestpayload)) && StringUtils.isBlank(serviceKey))) {
                return true;
            }

            mfaType = mfaUtil.getMFAType();

            if (mfaAttributes.has("OTP")) {
                JsonObject OTP = new JsonObject();
                if (mfaAttributes.get("OTP").isJsonObject()) {
                    OTP = mfaAttributes.get("OTP").getAsJsonObject();
                }

                if (OTP.has(MFAConstants.OTP_OTP)) {
                    state = MFAConstants.VERIFY;
                } else if (OTP.has(MFAConstants.SECURITY_KEY)) {
                    state = MFAConstants.RESEND;
                } else {
                    state = MFAConstants.REQUEST;
                }
            } else if (mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)) {
                state = MFAConstants.VERIFY;
            }

            if (StringUtils.isBlank(serviceKey)) {
                resultJson = mfaUtil.setserviceMFAAttributes();
                addState(resultJson, state, mfaType);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }

            if (!mfaUtil.isValidServiceKey(serviceKey, serviceName)) {
                ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
                addState(resultJson, state, mfaType);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }

            if (mfaType.equals(MFAConstants.SECURE_ACCESS_CODE) && mfaAttributes.has("OTP")
                    && mfaAttributes.get("OTP").isJsonObject()) {
                return handleOTP(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                        responsePayloadHandler, requestPayloadHandler, state, mfaType);
            }

            if (mfaType.equals(MFAConstants.SECURITY_QUESTIONS) && mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)
                    && mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).isJsonArray()) {
                return handleSecurityQuestions(mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).getAsJsonArray(),
                        serviceKey, serviceName, mfaUtil, responsePayloadHandler, requestPayloadHandler, state,
                        mfaType);
            }

            ErrorCodeEnum.ERR_10503.setErrorCode(resultJson);

            addState(resultJson, state, mfaType);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        } catch (Exception e) {
            alert.prepareError("Exception while determining MFA",e).log();
        }

        ErrorCodeEnum.ERR_10503.setErrorCode(resultJson);
        addState(resultJson, state, mfaType);
        responsePayloadHandler.updatePayloadAsJson(resultJson);
        return false;
    }

}
