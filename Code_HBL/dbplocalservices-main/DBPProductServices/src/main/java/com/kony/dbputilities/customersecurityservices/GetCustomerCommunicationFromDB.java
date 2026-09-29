package com.kony.dbputilities.customersecurityservices;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerCommunicationResource;

public class GetCustomerCommunicationFromDB implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		CustomerCommunicationResource resource = DBPAPIAbstractFactoryImpl.getResource(CustomerCommunicationResource.class);
		return resource.getPrimaryCommunicationForLogin(methodID, inputArray, dcRequest, dcResponse);
	}

}