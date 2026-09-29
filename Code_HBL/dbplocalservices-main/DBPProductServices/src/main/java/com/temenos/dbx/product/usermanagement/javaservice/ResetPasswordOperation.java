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

public class ResetPasswordOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

		
		 Result result = new Result();
	        try {
	            UserManagementResource userManagementResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(UserManagementResource.class);
	           result = userManagementResource.resetPassword(methodID, inputArray, dcRequest, dcResponse);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception while creating Customer: ",  e).log();
	        }

	        return result;
	}

}
