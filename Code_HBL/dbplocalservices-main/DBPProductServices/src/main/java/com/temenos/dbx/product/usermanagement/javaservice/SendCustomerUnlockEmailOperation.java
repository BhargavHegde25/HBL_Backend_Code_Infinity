package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.OrganizationUserManagementResource;
import com.temenos.dbx.product.usermanagement.resource.api.ProfileManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class SendCustomerUnlockEmailOperation implements JavaService2{

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		ProfileManagementResource managementResource = DBPAPIAbstractFactoryImpl.getResource(ProfileManagementResource.class);
		return managementResource.sendCustomerUnlockEmail(methodID, inputArray, request, response);
		
	}

	
}
