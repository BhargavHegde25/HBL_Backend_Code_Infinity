package com.kony.adminconsole.alertmanage.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.alertmanage.resource.CustExternalAlertSubscriptionResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CustExternalAlertSubscriptionOperation implements JavaService2  {

	
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		CustExternalAlertSubscriptionResource alertResource = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(ResourceFactory.class).getResource(CustExternalAlertSubscriptionResource.class);

		return alertResource.registerExtnlSubcription(methodId, inputArray, request, response);
		
	}

		
}
