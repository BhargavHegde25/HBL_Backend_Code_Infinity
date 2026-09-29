package com.dbp.batchprocessengine.javaservice;

import com.dbp.batchprocessengine.resource.api.AlertSubscribersResource;
import com.dbp.batchprocessengine.resource.impl.AlertSubscribersResourceImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class GetSubscribers implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		AlertSubscribersResource customerResource = new AlertSubscribersResourceImpl();

		return customerResource.getSubscribers(methodID, inputArray, request, response);
	}

}
