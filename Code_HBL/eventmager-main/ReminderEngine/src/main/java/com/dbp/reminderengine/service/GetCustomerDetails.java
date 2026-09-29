package com.dbp.reminderengine.service;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.reminderengine.resource.api.CustomerDetailsResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class GetCustomerDetails implements JavaService2 {

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		CustomerDetailsResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(CustomerDetailsResource.class);

		return customerResource.getCustomers(request);
	}

}
