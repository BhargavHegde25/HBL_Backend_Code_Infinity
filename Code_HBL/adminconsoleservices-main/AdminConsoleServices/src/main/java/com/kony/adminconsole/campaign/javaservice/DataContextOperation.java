package com.kony.adminconsole.campaign.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.campaign.resource.DataContextResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

public class DataContextOperation implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
	
		DataContextResource dcResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(DataContextResource.class);

		return dcResource.
				getActiveCountForSegment(methodID, inputArray, request, response);
				
	}
	

}
