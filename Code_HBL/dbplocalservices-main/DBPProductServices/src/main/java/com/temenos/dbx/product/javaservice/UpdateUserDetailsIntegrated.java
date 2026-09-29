 package com.temenos.dbx.product.javaservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class UpdateUserDetailsIntegrated implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
        	
        	return updateUserDetailsFromTransact(inputArray, request);

        }
		
		
		
	
		Map<String, Object> inputParams = getRequestPayloadInMap(request);
        inputParams.putAll(HelperMethods.getInputParamObjectMap(inputArray));
        String serviceId = ServiceId.DBP_PRODUCT_SERVICE;
		String operationName = OperationName.UPDATE_CUSTOMER_DETAILS_OPERATION;
		Map<String, Object> headerMap = new HashMap<>();
		JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
		        inputParams, headerMap);
		return JSONToResult.convert(jsonobject.getAsString());

	}



	private Object updateUserDetailsFromTransact(Object[] inputArray, DataControllerRequest request) {
		Map<String, Object> inputParams = getRequestPayloadInMap(request);
		inputParams.putAll(HelperMethods.getInputParamObjectMap(inputArray));
		String serviceId = ServiceId.COMMON_ORCHESTRATION_SERVICE;
		String operationName = OperationName.UPDATE_USER_DETAILS;
		Map<String, Object> headerMap = new HashMap<>();
		JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
		        inputParams, headerMap, HelperMethods.getAuthToken(request));
		return JSONToResult.convert(jsonobject.getAsString());
	}
	

	
	private Map<String, Object> getRequestPayloadInMap(DataControllerRequest request) {

        Map<String, Object> inputmap = new HashMap<>();
        Iterator<String> params = request.getParameterNames();
        while(params.hasNext()) {
        String key = params.next();
        inputmap.put(key, request.getParameter(key));
        }
        return inputmap;
        }

	

}
