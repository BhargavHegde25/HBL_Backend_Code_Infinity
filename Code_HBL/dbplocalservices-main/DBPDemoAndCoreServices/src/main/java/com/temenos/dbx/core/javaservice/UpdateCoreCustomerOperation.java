package com.temenos.dbx.core.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.CoreCustomerResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class UpdateCoreCustomerOperation implements JavaService2 {
	private static Alert alert;

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

		alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		
		 Result result = new Result();
	        try {
	            CoreCustomerResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(CoreCustomerResource.class);
	           result = customerResource.updateFromParty(methodID, inputArray, dcRequest, dcResponse);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception while creating Customer: ",  e).log();
	        }

	        return result;
	}
	
}