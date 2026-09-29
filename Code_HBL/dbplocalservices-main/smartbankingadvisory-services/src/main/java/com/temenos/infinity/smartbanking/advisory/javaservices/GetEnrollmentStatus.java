package com.temenos.infinity.smartbanking.advisory.javaservices;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.smartbanking.advisory.resource.api.SmartBankingAdvisoryResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetEnrollmentStatus implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		SmartBankingAdvisoryResource smartBankingAdvisoryResource = DBPAPIAbstractFactoryImpl
				.getResource(SmartBankingAdvisoryResource.class);
		Result result = smartBankingAdvisoryResource.getEnrollmentStatus(methodId, inputArray, request, response);
		return result;
	}

}