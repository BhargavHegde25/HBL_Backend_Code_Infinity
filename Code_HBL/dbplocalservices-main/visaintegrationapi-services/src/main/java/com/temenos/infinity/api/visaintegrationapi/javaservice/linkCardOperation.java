package com.temenos.infinity.api.visaintegrationapi.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.visaintegrationapi.dto.VisaDTO;
import com.temenos.infinity.api.visaintegrationapi.constants.ErrorCodeEnum;
import com.temenos.infinity.api.visaintegrationapi.resource.api.VisaIntegrationApiResource;
import com.temenos.infinity.api.visaintegrationapi.constants.VisaIntegrationConstants;
import com.temenos.infinity.api.visaintegrationapi.utils.VisaIntegrationUtils;

public class linkCardOperation implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			// Initializing of AccountTransactions through Abstract factory method
			VisaIntegrationApiResource visaintegrationapiResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(VisaIntegrationApiResource.class);
			VisaDTO visaDTO = constructPayLoad(request);

			VisaIntegrationUtils visaIntegrationUtils = new VisaIntegrationUtils();

			// Set Header Map
			HashMap<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("x-pay-token", VisaIntegrationUtils.getXpayToken(visaDTO));
			headerMap.put("content-type", "application/json");

			result = visaintegrationapiResource.linkCard(visaDTO, headerMap, request);
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}

		return result;
	}

	public static VisaDTO constructPayLoad(DataControllerRequest request) {
		VisaDTO visaDTO = new VisaDTO();

		String paymentService = request.getParameter("paymentService") != null ? request.getParameter("paymentService")
				: "";

		if (paymentService.contentEquals(VisaIntegrationConstants.PARAM_GOOGLEPAY)
				|| paymentService.contentEquals(VisaIntegrationConstants.PARAM_SAMSUNGPAY)) {
			String cardID = request.getParameter("cardID") != null ? request.getParameter("cardID") : "";
			String deviceID = request.getParameter("deviceID") != null ? request.getParameter("deviceID") : "";
			String clientCustomerID = request.getParameter("clientCustomerID") != null
					? request.getParameter("clientCustomerID")
					: "";
			if (request.containsKeyInRequest("paymentAccountReference")) {
				String paymentAccountReference = request.getParameter("paymentAccountReference") != null
						? request.getParameter("paymentAccountReference")
						: "";
				visaDTO.setPaymentAccountReference(paymentAccountReference);
			}
			visaDTO.setCardID(cardID);
			visaDTO.setDeviceID(deviceID);
			visaDTO.setClientCustomerId(clientCustomerID);
		}
		if (paymentService.contentEquals(VisaIntegrationConstants.PARAM_APPLEPAY)) {
			String cardID = request.getParameter("cardID") != null ? request.getParameter("cardID") : "";
			String deviceCert = request.getParameter("deviceCert") != null ? request.getParameter("deviceCert") : "";
			String nonceSignature = request.getParameter("nonceSignature") != null
					? request.getParameter("nonceSignature")
					: "";
			String nonce = request.getParameter("nonce") != null ? request.getParameter("nonce") : "";
			if (request.containsKeyInRequest("paymentAccountReference")) {
				String paymentAccountReference = request.getParameter("paymentAccountReference") != null
						? request.getParameter("paymentAccountReference")
						: "";
				visaDTO.setPaymentAccountReference(paymentAccountReference);
			}
			visaDTO.setCardID(cardID);
			visaDTO.setDeviceCert(deviceCert);
			visaDTO.setNonceSignature(nonceSignature);
			visaDTO.setNonce(nonce);
		}

		visaDTO.setPaymentService(paymentService);
		visaDTO.setOperation(VisaIntegrationConstants.PARAM_OPERATION_LINKCARD);

		return visaDTO;
	}
}
