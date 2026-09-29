package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.UserSearchResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetUserNameOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		
		 Result result = new Result();
	        try {
	            UserSearchResource managementResource = DBPAPIAbstractFactoryImpl.getResource(UserSearchResource.class);
	            result = managementResource.verifyDbxUserName(methodID, inputArray, request, response);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception while updating Customer Preferences: ",  e).log();
	        }

	        return result;
	}

}