package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.ProfileManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CustomerGetByUserNameOperation implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		ProfileManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(ProfileManagementResource.class);
		
		return resource.getByUserName(methodID, inputArray, dcRequest, dcResponse);
		
	}

}
