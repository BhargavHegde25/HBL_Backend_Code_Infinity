package com.temenos.infinity.api.visaintegrationapi.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.visaintegrationapi.backenddelegate.api.VisaIntegrationApiAPIBackendDelegate;
import com.temenos.infinity.api.visaintegrationapi.config.VisaIntegrationApiAPIServices;
import com.temenos.infinity.api.visaintegrationapi.constants.ErrorCodeEnum;
import com.temenos.infinity.api.visaintegrationapi.constants.VisaIntegrationConstants;
import com.temenos.infinity.api.visaintegrationapi.dto.VisaDTO;
import com.temenos.infinity.api.visaintegrationapi.utils.VisaIntegrationUtils;

public class VisaIntegrationApiAPIBackendDelegateImpl implements VisaIntegrationApiAPIBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public VisaDTO linkCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException {

		VisaDTO responseObjectDTO = new VisaDTO();

		JsonElement jsonElement = null;
		JsonObject responseObject = null;
		DBXResult response = null;

		String requestPayload = VisaIntegrationUtils.getRequestBodyForLinkingCard(visaDTO);

//		JSONObject requestBody = new JSONObject();
//		if ((visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_GOOGLEPAY))
//				|| (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_SAMSUNGPAY))) {
//			requestBody.put("vCardID", visaDTO.getvCardID());
//			requestBody.put("deviceID", visaDTO.getDeviceID());
//			requestBody.put("clientCustomerID", visaDTO.getClientCustomerId());
//			if (StringUtils.isNotBlank(visaDTO.getPaymentAccountReference())) {
//				requestBody.put("paymentAccountReference", visaDTO.getPaymentAccountReference());
//			}
//		}
//
//		if (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_APPLEPAY)) {
//			requestBody.put("vCardID", visaDTO.getvCardID());
//			requestBody.put("deviceCert", visaDTO.getDeviceCert());
//			requestBody.put("nonceSignature", visaDTO.getNonceSignature());
//			requestBody.put("nonce", visaDTO.getNonce());
//			if (StringUtils.isNotBlank(visaDTO.getPaymentAccountReference())) {
//				requestBody.put("paymentAccountReference", visaDTO.getPaymentAccountReference());
//			}
//		}
//
//		// Set Input Parameters for create Order service
//		Map<String, Object> inputMap = new HashMap<>();
//		inputMap.put("requestBody", requestBody.toString());

		// Making a call to order request API
//		String linkCardresponseObject = null;
//		JSONObject responseObject = new JSONObject();
		if ((visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_GOOGLEPAY))) {
//			try {
//				linkCardresponseObject = DBPServiceExecutorBuilder.builder()
//						.withServiceId(VisaIntegrationApiAPIServices.LINKCARDWITHGOOGLEPAY.getServiceName())
//						.withOperationId(VisaIntegrationApiAPIServices.LINKCARDWITHGOOGLEPAY.getOperationName())
//						.withRequestParameters(inputMap).withRequestHeaders(headerMap).build().getresponseObject();
//
//			} catch (Exception e) {
//				alert.prepareError("Unable to create user account settings order " + e).log();
//				throw new ApplicationException(ErrorCodeEnum.ERR_20053);
//			}

			response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST,
					VisaIntegrationConstants.PARAM_GOOGLEPAY_URL, requestPayload, headerMap);
		}
		if ((visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_SAMSUNGPAY))) {
//			try {
//				linkCardresponseObject = DBPServiceExecutorBuilder.builder()
//						.withServiceId(VisaIntegrationApiAPIServices.LINKCARDWITHSAMSUNGPAY.getServiceName())
//						.withOperationId(VisaIntegrationApiAPIServices.LINKCARDWITHSAMSUNGPAY.getOperationName())
//						.withRequestParameters(inputMap).withRequestHeaders(headerMap).build().getresponseObject();
//
//			} catch (Exception e) {
//				alert.prepareError("Unable to create user account settings order " + e).log();
//				throw new ApplicationException(ErrorCodeEnum.ERR_20053);
//			}

			response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST,
					VisaIntegrationConstants.PARAM_SAMSUNGPAY_URL, requestPayload, headerMap);
		}
		if ((visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_APPLEPAY))) {
//			try {
//				linkCardresponseObject = DBPServiceExecutorBuilder.builder()
//						.withServiceId(VisaIntegrationApiAPIServices.LINKCARDWITHAPPLEPAY.getServiceName())
//						.withOperationId(VisaIntegrationApiAPIServices.LINKCARDWITHAPPLEPAY.getOperationName())
//						.withRequestParameters(inputMap).withRequestHeaders(headerMap).build().getresponseObject();
//
//			} catch (Exception e) {
//				alert.prepareError("Unable to create user account settings order " + e).log();
//				throw new ApplicationException(ErrorCodeEnum.ERR_20053);
//			}			

			response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST,
					VisaIntegrationConstants.PARAM_APPLEPAY_URL, requestPayload, headerMap);
		}

		jsonElement = new JsonParser().parse((String) response.getResponse());
		responseObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
				: jsonElement.getAsJsonArray().get(0).getAsJsonObject();

		if ((responseObject.has("responseStatus") || responseObject.has("errorResponse"))) {
			JsonObject obj = null;

			if (responseObject.has("responseStatus")) {
				obj = responseObject.getAsJsonObject("responseStatus");
				responseObjectDTO.setErrorMessage(obj.get("message").getAsString());
				responseObjectDTO.setCode(obj.get("code").getAsString());
				if(obj.has("details"))
				{
					String details=obj.get("details").toString();
					responseObjectDTO.setErrorMessage((obj.get("message").getAsString())+details);
				}
			}

			if (responseObject.has("errorResponse")) {
				obj = responseObject.getAsJsonObject("errorResponse");
				responseObjectDTO.setErrorMessage(obj.get("message").getAsString());
				responseObjectDTO.setCode(obj.get("status").getAsString());
				if(obj.has("details"))
				{
					String details=obj.get("details").toString();
					responseObjectDTO.setErrorMessage((obj.get("message").getAsString())+details);
				}
			}

		} else {
			if ((visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_GOOGLEPAY))
					|| (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_SAMSUNGPAY))) {

				if (responseObject.has("opaquePaymentCard")) {
					responseObjectDTO.setOpaquePaymentCard(responseObject.get("opaquePaymentCard").getAsString());
				}
				if (responseObject.has("last4")) {
					responseObjectDTO.setLast4(responseObject.get("last4").getAsString());
				}
				if (responseObject.has("vCardID")) {
					responseObjectDTO.setCardID(responseObject.get("vCardID").getAsString());
				}
				if (responseObject.has("paymentAccountReference")) {
					responseObjectDTO
							.setPaymentAccountReference(responseObject.get("paymentAccountReference").getAsString());
				}
				if (responseObject.has("encMobileNumber")) {
					responseObjectDTO.setEncMobileNumber(responseObject.get("encMobileNumber").getAsString());
				}
				if (responseObject.has("encAddress")) {
					responseObjectDTO.setEncAddress(responseObject.get("encAddress").getAsString());
				}
				if (responseObject.has("cardType")) {
					responseObjectDTO.setCardType(responseObject.get("cardType").getAsString());
				}
				if (responseObject.has("expirationDate")) {
					JsonObject obj = responseObject.getAsJsonObject("expirationDate");
					responseObjectDTO.setExpiryMonth(obj.get("month").getAsString());
					responseObjectDTO.setExpiryYear(obj.get("year").getAsString());
				}

			}
			if ((visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_APPLEPAY))) {
				if (responseObject.has("vCardID")) {
					responseObjectDTO.setCardID(responseObject.get("vCardID").getAsString());
				}
				if (responseObject.has("encryptedPassData")) {
					responseObjectDTO.setEncryptedPassData(responseObject.get("encryptedPassData").getAsString());
				}
				if (responseObject.has("activationData")) {
					responseObjectDTO.setActivationData(responseObject.get("activationData").getAsString());
				}
				if (responseObject.has("paymentAccountReference")) {
					responseObjectDTO
							.setPaymentAccountReference(responseObject.get("paymentAccountReference").getAsString());
				}
				if (responseObject.has("ephemeralPublicKey")) {
					responseObjectDTO.setEphemeralPublicKey(responseObject.get("ephemeralPublicKey").getAsString());
				}
			}

		}
		return responseObjectDTO;
	}

	@Override
	public VisaDTO enrollCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException {
		VisaDTO responseObjectDTO = new VisaDTO();

		JsonElement jsonElement = null;
		JsonObject responseObject = null;

		DBXResult response = null;
		String requestPayload = visaDTO.getEncCard();
		response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST,
				VisaIntegrationConstants.PARAM_ENROLLCARD_URL, requestPayload, headerMap);
		jsonElement = new JsonParser().parse((String) response.getResponse());

		responseObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
				: jsonElement.getAsJsonArray().get(0).getAsJsonObject();

		if ((responseObject.has("errorResponse"))) {
			JsonObject obj = responseObject.getAsJsonObject("errorResponse");
			responseObjectDTO.setErrorMessage(obj.get("message").getAsString());
			responseObjectDTO.setCode(obj.get("status").getAsString());
			if(obj.has("details"))
			{
				String details=obj.get("details").toString();
				responseObjectDTO.setErrorMessage((obj.get("message").getAsString())+details);
			}

		} else {

			if (responseObject.has("last4")) {
				responseObjectDTO.setLast4(responseObject.get("last4").getAsString());
			}
			if (responseObject.has("vCardID")) {
				responseObjectDTO.setCardID(responseObject.get("vCardID").getAsString());
			}
			if (responseObject.has("createdDateTime")) {
				responseObjectDTO.setCreatedDateTime(responseObject.get("createdDateTime").getAsString());
			}
			if (responseObject.has("updatedDateTime")) {
				responseObjectDTO.setUpdatedDateTime(responseObject.get("updatedDateTime").getAsString());
			}
			if (responseObject.has("paymentAccountReference")) {
				responseObjectDTO
						.setPaymentAccountReference(responseObject.get("paymentAccountReference").getAsString());
			}
			if (responseObject.has("expirationDate")) {
				JsonObject obj = responseObject.getAsJsonObject("expirationDate");
				responseObjectDTO.setExpiryMonth(obj.get("month").getAsString());
				responseObjectDTO.setExpiryYear(obj.get("year").getAsString());
			}
		}
		return responseObjectDTO;
	}

}