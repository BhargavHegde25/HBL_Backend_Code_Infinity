package com.dbp.externalevent.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.externalevent.resource.api.UpdateEventConfigResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class UpdateEventData implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		UpdateEventConfigResource dataupdateresource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(UpdateEventConfigResource.class);
		return dataupdateresource.updateEventConfigData();
	}

}
