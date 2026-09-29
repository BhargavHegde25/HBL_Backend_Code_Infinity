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
import com.temenos.infinity.api.visaintegrationapi.utils.VisaIntegrationUtils;
import com.temenos.infinity.api.visaintegrationapi.constants.VisaIntegrationConstants;

public class EnrollCardOperation implements JavaService2 {

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

			String payLoad = visaIntegrationUtils.getRequestBodyForEncryption(visaDTO);
			visaDTO.setEncCard(visaIntegrationUtils.getEncryptedPayload(
					VisaIntegrationConstants.PARAM_mleServerPublicCertificatePath, payLoad,
					VisaIntegrationConstants.KEY_ID));

			// Set Header Map
			HashMap<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("x-pay-token", VisaIntegrationUtils.getXpayToken(visaDTO));
			headerMap.put("content-type", "application/json");

			result = visaintegrationapiResource.enrollCard(visaDTO, headerMap,request);
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}

		return result;
	}

	public static VisaDTO constructPayLoad(DataControllerRequest request) {
		VisaDTO visaDTO = new VisaDTO();

		String cardID = request.getParameter("cardID") != null ? request.getParameter("cardID") : "";
		visaDTO = VisaIntegrationUtils.setCardDetails(cardID);
		String clientCustomerId = request.getParameter("clientCustomerId") != null
				? request.getParameter("clientCustomerId")
				: "";
		String vCustomerIDForPartner = request.getParameter("vCustomerIDForPartner") != null
				? request.getParameter("vCustomerIDForPartner")
				: "";
		String vClientIDForPartner = request.getParameter("vClientIDForPartner") != null
				? request.getParameter("vClientIDForPartner")
				: "";
		String paymentAccountReference = request.getParameter("paymentAccountReference") != null
				? request.getParameter("paymentAccountReference")
				: "";

		if (request.containsKeyInRequest("billingAddress")) {

			JsonArray address = new JsonParser().parse(request.getParameter("billingAddress")).getAsJsonArray();

			if (address.toString().contains("name")) {
				String name = address.get(0).getAsJsonObject().get("name").getAsString() != null
						? address.get(0).getAsJsonObject().get("name").getAsString()
						: "";
				visaDTO.setName(name);
			}
			if (address.toString().contains("line1")) {
				String addrLine1 = address.get(0).getAsJsonObject().get("line1").getAsString() != null
						? address.get(0).getAsJsonObject().get("line1").getAsString()
						: "";
				visaDTO.setAddrLine1(addrLine1);
			}
			if (address.toString().contains("line2")) {
				String addrLine2 = address.get(0).getAsJsonObject().get("line2").getAsString() != null
						? address.get(0).getAsJsonObject().get("line2").getAsString()
						: "";
				visaDTO.setAddrLine2(addrLine2);
			}
			if (address.toString().contains("line3")) {
				String addrLine3 = address.get(0).getAsJsonObject().get("line3").getAsString() != null
						? address.get(0).getAsJsonObject().get("line3").getAsString()
						: "";
				visaDTO.setAddrLine3(addrLine3);
			}
			if (address.toString().contains("city")) {
				String city = address.get(0).getAsJsonObject().get("city").getAsString() != null
						? address.get(0).getAsJsonObject().get("city").getAsString()
						: "";
				visaDTO.setCity(city);
			}
			if (address.toString().contains("state")) {
				String state = address.get(0).getAsJsonObject().get("state").getAsString() != null
						? address.get(0).getAsJsonObject().get("state").getAsString()
						: "";
				visaDTO.setState(state);
			}
			if (address.toString().contains("countryCode")) {
				String countryCode = address.get(0).getAsJsonObject().get("countryCode").getAsString() != null
						? address.get(0).getAsJsonObject().get("countryCode").getAsString()
						: "";
				visaDTO.setCountryCode(countryCode);
			}
			if (address.toString().contains("postalCode")) {
				String postalCode = address.get(0).getAsJsonObject().get("postalCode").getAsString() != null
						? address.get(0).getAsJsonObject().get("postalCode").getAsString()
						: "";
				visaDTO.setPostalCode(postalCode);
			}
		}
		
		String paymentService = request.getParameter("paymentService") != null ? request.getParameter("paymentService")
				: "";

		visaDTO.setvCustomerIDForPartner(vCustomerIDForPartner);
		visaDTO.setvClientIDForPartner(vClientIDForPartner);
		visaDTO.setClientCustomerId(clientCustomerId);
		visaDTO.setPaymentAccountReference(paymentAccountReference);
		visaDTO.setOperation(VisaIntegrationConstants.PARAM_OPERATION_ENROLLCARD);
		visaDTO.setPaymentService(paymentService);
		visaDTO.setCardID(cardID);
		return visaDTO;
	}
}
