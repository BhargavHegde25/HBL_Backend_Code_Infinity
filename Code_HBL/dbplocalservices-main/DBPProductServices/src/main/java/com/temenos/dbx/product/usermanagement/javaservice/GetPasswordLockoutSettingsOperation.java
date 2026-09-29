package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.UserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetPasswordLockoutSettingsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		
		 Result result = new Result();
	        try {
	            UserManagementResource managementResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(UserManagementResource.class);
	            result = managementResource.getPasswordLockoutSettings(methodID, inputArray, request, response);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception while getting password lockout settings: ",  e).log();
	        }

	        return result;
	}

}