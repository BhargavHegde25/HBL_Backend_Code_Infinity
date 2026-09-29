package com.kony.dbputilities.mfa.preprocessors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.scaintegration.helper.GetConfigParams;
import com.kony.scaintegration.task.ProcessSCA;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class OwnAccTransferMFAPreProcessor implements ObjectServicePreProcessor {
    private static final Logger LOG = LogManager.getLogger(OwnAccTransferMFAPreProcessor.class);

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
    	ProcessSCA obj = new ProcessSCA();
    	 PayloadHandler requestPayloadHandler = null;
    	 requestPayloadHandler = requestManager.getPayloadHandler();
    	 JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
         LOG.debug("MFA: requestpayload:"+requestpayload);
         String isValidate="";
         if(requestpayload.has("validate")) {
         	isValidate= requestpayload.get("validate")!=null?requestpayload.get("validate").getAsString():"";
         }
		if (!obj.process(requestManager, responseManager))
			return;
		/*
		 * Skip MFA in Transfer Validation flow
		 */
		if (!isValidate.equalsIgnoreCase("true") && !Boolean.parseBoolean(GetConfigParams.getIsScaEnabled())) {
			LOG.debug("MFA");
        PostLoginMFAUtil mfaUtil = null;
        PayloadHandler responsePayloadHandler = null;
        JsonObject mfaAttributes = null;
        JsonObject resultJson = new JsonObject();
        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());
        responsePayloadHandler = responseManager.getPayloadHandler();
        if (requestPayloadHandler.getPayloadAsJson() == null) {
            ErrorCodeEnum.ERR_10620.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }
        boolean isMFAPresent = false;
        if (requestpayload.has(MFAConstants.MFA_ATTRIBUTES)) {
            try {
                JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);

                if (mfaElement.isJsonObject()) {
                    mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
                } else {
                    JsonParser parser = new JsonParser();
                    mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
                }
                mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, MFAConstants.SERVICE_ID_TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE);
                isMFAPresent = true;
            } catch (Exception e) {

                LOG.error(e.getMessage());
            }
        } else {
            mfaAttributes = new JsonObject();
            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, MFAConstants.SERVICE_ID_TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE);
            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
        }

        requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
        requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);

        mfaUtil = new PostLoginMFAUtil(requestManager);

        String serviceKey = null;
        String serviceName = null;
        LOG.debug("isMFAPresent ##"+ isMFAPresent);
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

        LOG.debug("serviceKey ##"+ serviceKey);
        if (StringUtils.isBlank(serviceKey)) {
        	LOG.debug("serviceKey In if##"+ serviceKey);
            mfaUtil.getMfaModeforRequest();
        } else {
        	LOG.debug("serviceKey In else##"+ serviceKey);
            mfaUtil.getMFaModeforRequestfromDB(serviceKey, serviceName);
        }
        LOG.debug("mfautil ##"+ mfaUtil.toString());
        
        
        if (!mfaUtil.isAppActionValid() || ((mfaUtil.mfaConfigurationUtil != null) && !mfaUtil.mfaConfigurationUtil.isValidMFA())) {
            mfaUtil.setError(resultJson, ErrorCodeEnum.ERR_10621.getErrorCodeAsString(),
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
            ErrorCodeEnum.ERR_10622.setErrorCode(resultJson);

            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        String MFAtype = mfaUtil.getMFAType();

        if (MFAtype.equals(MFAConstants.SECURE_ACCESS_CODE) && null != mfaAttributes && mfaAttributes.has("OTP")
                && mfaAttributes.get("OTP").isJsonObject()) {
            handleOTP(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                    responsePayloadHandler, requestPayloadHandler, requestChain);
            return;
        }
        
		if (MFAtype.equals(MFAConstants.TRANSACTION_PIN)) {
			String channel = "";
			JSONObject reportingParamsJson = mfaUtil.getReportingParams(requestManager);
			if (reportingParamsJson != null) {
				channel = reportingParamsJson.optString("chnl");
			}
			LOG.debug("MFA Flow - Detected channel:::" + channel);

			// Biometric validation if channel is MB and Biometric object is present
			if ("mobile".equalsIgnoreCase(channel) && mfaAttributes != null && mfaAttributes.has("Biometric")
					&& mfaAttributes.get("Biometric").isJsonObject()) {
				LOG.debug("MFA Flow - Executing biometric authentication for channel:::MB");
				handleBiometricAuth(mfaAttributes.get("Biometric").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
						responsePayloadHandler, requestPayloadHandler, requestChain, requestManager);
				return;
			}

			// Fallback to PIN validation
			if (mfaAttributes != null && mfaAttributes.has("PIN") && mfaAttributes.get("PIN").isJsonObject()) {
				LOG.debug("MFA Flow - Executing PIN authentication for channel:::" + channel);
				validatePIN(mfaAttributes.get("PIN").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
						responsePayloadHandler, requestPayloadHandler, requestChain);
				return;
			}
			LOG.warn("MFA Flow - No valid MFA method found for channel:::" + channel);
		}
        
        if (MFAtype.equals(MFAConstants.SECURITY_QUESTIONS) && null != mfaAttributes
                && mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)
                && mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).isJsonArray()) {
            handleSecurityQuestions(mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).getAsJsonArray(), serviceKey,
                    serviceName, mfaUtil, responsePayloadHandler, requestPayloadHandler, requestChain);
            return;
        }

        ErrorCodeEnum.ERR_10623.setErrorCode(resultJson);

        responsePayloadHandler.updatePayloadAsJson(resultJson);

        return;
		}

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
        requestJson.remove("mfaMode");
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
        requestJson.remove("mfaMode");
        requestChain.execute();
    }
    private void validatePIN(JsonObject PIN, String serviceKey, String serviceName, PostLoginMFAUtil mfaUtil,
            PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler,
            FabricRequestChain requestChain) {
    	
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

	private void handleBiometricAuth(JsonObject Biometric, String serviceKey, String serviceName,
			PostLoginMFAUtil mfaUtil, PayloadHandler responsePayloadHandler, PayloadHandler requestPayloadHandler,
			FabricRequestChain requestChain, FabricRequestManager requestManager) {

		JsonObject resultJson = new JsonObject();
		if (!Biometric.has(MFAConstants.BIOMETRIC_STATUS)) {
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			return;
		} else {
			resultJson = mfaUtil.verifyBiometric(Biometric, requestManager);
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

}
