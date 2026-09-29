package com.dbp.batchprocessengine.javaservice;

import com.dbp.batchprocessengine.resource.api.DataUpdateJobResource;
import com.dbp.batchprocessengine.resource.impl.DataUpdateJobResourceImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class PullDataFromCoreGenerateEvents implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		DataUpdateJobResource dataupdateresource = new DataUpdateJobResourceImpl();
		return dataupdateresource.generateEventsFromCoreData(request);
	}
}
