package com.temenos.dbx.product.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.resource.api.CustomerResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CreateProspectOperation implements JavaService2 {
	private Alert alert; 
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        
        Result result = new Result();
           try {
               CustomerResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                       .getFactoryInstance(ResourceFactory.class).getResource(CustomerResource.class);
              result = customerResource.saveFromDBX(methodID, inputArray, dcRequest, dcResponse);
           } catch (Exception e) {
               alert.prepareError("Caught exception while creating Customer: ",  e).log();
           }

           return result;
	}
}