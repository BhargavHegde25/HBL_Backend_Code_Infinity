package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerPreferenceResource;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerSecurityQuestionsResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetIsSecurityQuestionsConfiguredOperation implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		
		CustomerSecurityQuestionsResource impl = DBPAPIAbstractFactoryImpl.getResource(CustomerSecurityQuestionsResource.class);
		
		return impl.getAreSecurityQuestionsConfigured(methodID, inputArray, dcRequest, dcResponse);
		
	}

}
