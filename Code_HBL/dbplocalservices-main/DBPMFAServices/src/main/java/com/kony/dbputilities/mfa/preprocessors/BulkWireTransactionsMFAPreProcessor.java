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
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class BulkWireTransactionsMFAPreProcessor extends TransactionsMFAPreProcessor{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	 @Override
	    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
	            throws Exception {
			Log4j2Configurator.getInstance();
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
	            String operationId = "";
	            responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
	                    ContentType.APPLICATION_JSON.getMimeType());

	            if (requestPayloadHandler.getPayloadAsJson() == null) {
	                ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
	                responsePayloadHandler.updatePayloadAsJson(resultJson);
	                return false;
	            }

	            JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject(); 
	            mfaUtil = new PostLoginMFAUtil(requestManager);
	            if (mfaUtil.MFABypassed(requestpayload)) {
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
	            String appendedString = null;
	            if (JSONUtil.hasKey(mfaAttributes, MFAConstants.SERVICE_KEY)) {
	                serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
	            }else {
	            	String firstTransactionType = requestpayload.get("firstTransactionType").toString();
		            firstTransactionType = firstTransactionType.replaceAll("\"","");
		            if(firstTransactionType.contains("Domestic")) {
		            	operationId	 = "domesticwiretransfer";
		            }else {
		            	operationId = "internationalwiretransfer";
		            }
		            appendedString = String.join("_", serviceId, objectId, operationId );
		            serviceName = mfaUtil.getValidServiceName(requestpayload,appendedString.toLowerCase());
	            }
	            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
	            requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
	            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
	            requestPayloadHandler.updatePayloadAsJson(requestpayload);
            
	            if (StringUtils.isBlank(serviceKey)) { 
	                mfaUtil.getMfaModeforRequest();
	                     
		            if (!mfaUtil.isAppActionValid() || ((mfaUtil.mfaConfigurationUtil != null) && !mfaUtil.mfaConfigurationUtil.isValidMFA())) {
		                mfaUtil.setError(resultJson, ErrorCodeEnum.ERR_10501.getErrorCodeAsString(),
		                        mfaUtil.getDbpErrMsg() + "  " + ErrorConstants.UNABLE_TO_DETERMINE_C360_CONFIGURATION);
		                responsePayloadHandler.updatePayloadAsJson(resultJson);
		                return false;
		            }
		            if ((mfaUtil.mfaConfigurationUtil == null) || ((!mfaUtil.isMFARequired(requestpayload)) && StringUtils.isBlank(serviceKey))) {
		            	Boolean checkMFAForAnotherService = false;
		                if(operationId.equalsIgnoreCase("domesticwiretransfer")) {
		            		 int internationalTran = Integer.parseInt(requestpayload.get("numOfInternationalTransactions").toString());
		            		 if(internationalTran != 0) {
		            			 checkMFAForAnotherService = true;
		            			 operationId =  "internationalwiretransfer";
		            		 }
				         }else {
				        	 int domesticTran = Integer.parseInt(requestpayload.get("numOfDomesticTransactions").toString());
		            		 if(domesticTran != 0) {
		            			 checkMFAForAnotherService = true;
		            			 operationId =  "domesticwiretransfer";
				          }
				         }
		                if(checkMFAForAnotherService) {
		                appendedString = String.join("_", serviceId, objectId, operationId );
		                serviceName = mfaUtil.getValidServiceName(requestpayload,appendedString.toLowerCase());     
		 	            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
		 	            requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
		 	            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
		 	            requestPayloadHandler.updatePayloadAsJson(requestpayload);
		                mfaUtil.getMfaModeforRequest();       
		             }else {
		            	 return true;
		             }
				 }
		        }else {
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
	            if(serviceName == null) {
	            	serviceName = mfaUtil.getMFAServiceUtil().getMfaServiceDTO().getServiceName();
	            	mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
		            requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
		            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
		            requestPayloadHandler.updatePayloadAsJson(requestpayload);
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
