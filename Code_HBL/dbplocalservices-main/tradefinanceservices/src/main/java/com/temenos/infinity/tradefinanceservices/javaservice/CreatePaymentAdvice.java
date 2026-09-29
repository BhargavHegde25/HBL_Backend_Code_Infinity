/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.PaymentAdviceResource;

public class CreatePaymentAdvice implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		PaymentAdviceResource paymentAdviceCreateOrder = DBPAPIAbstractFactoryImpl
				.getResource(PaymentAdviceResource.class);
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Result result = null;
		try {
			PaymentAdviceDTO paymentadvice = constructPayload(inputParams);
			result = paymentAdviceCreateOrder.createPaymentAdvice(paymentadvice, request);
		} catch (Exception e) {
			return ErrorCodeEnum.ERRTF_29064.setErrorCode(new Result(), "Failed to Create Payment Advices");
		}
		return result;
	}

	private PaymentAdviceDTO constructPayload(Map<String, Object> inputParams) {
		PaymentAdviceDTO paymentAdvice = new PaymentAdviceDTO();
		try {
			paymentAdvice = JSONUtils.parse(new JSONObject(inputParams).toString(), PaymentAdviceDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the input params: ", e).log();
		}
		return paymentAdvice;
	}

}
