package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.OrganizationUserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CheckIfOrgMemberExistsOperation implements JavaService2{

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		OrganizationUserManagementResource managementResource = DBPAPIAbstractFactoryImpl.getResource(OrganizationUserManagementResource.class);
		return managementResource.checkIfMemberExists(methodID, inputArray, request, response);
		
	}

	
}
