package com.temenos.dbx.party.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.resource.api.CustomerResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

public class UpdatePartyClassificationOperation implements JavaService2{
private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private LoggerUtil logger;
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		logger = new LoggerUtil(CreateProspectOperation.class);
        
        Result result = new Result();
           try {
               CustomerResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                       .getFactoryInstance(ResourceFactory.class).getResource(CustomerResource.class);
              result = customerResource.createPartyClassification(methodID, inputArray, dcRequest, dcResponse, true);
           } catch (Exception e) {
               alert.prepareError("Caught exception while creating Customer: ",  e).log();
           }

           return result;
	}

}
