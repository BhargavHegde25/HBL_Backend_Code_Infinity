/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;
import com.temenos.infinity.tradelending.resource.api.PaymentRequestResource;



public class SubmitPaymentRequestOperation implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		PaymentRequestResource requestResource = DBPAPIAbstractFactoryImpl.getResource(PaymentRequestResource.class);
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		PaymentRequestDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), PaymentRequestDTO.class);
		return requestResource.submitPaymentRequest(inputDto, request);
	}

}
