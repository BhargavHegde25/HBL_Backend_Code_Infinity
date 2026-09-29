package com.kony.dbputilities.cardsservices;


import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrderServiceRequest implements JavaService2{

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		try {
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

			String serviceName = "ServiceRequestJavaService";
			String operationName = "createOrder";

			String resultStr = DBPServiceExecutorBuilder.builder()
	                    .withServiceId(serviceName)
	                    .withOperationId(operationName)
	                    .withRequestParameters(params).withRequestHeaders(serviceHeaders)
	                    .withDataControllerRequest(request).build().getResponse();
			result = JSONToResult.convert(new JSONObject(resultStr).toString());			
		} catch (Exception e) {
			Result errorResult = new Result();
			return errorResult;
		}
		return result;
	}
}
