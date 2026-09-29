package com.kony.adminconsole.service.servicedefinition.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.servicedefinition.resource.api.ServiceDefinitionResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class SearchServiceDefinitionOperation implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			ServiceDefinitionResource serviceDefinitionResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(ServiceDefinitionResource.class);
			result = serviceDefinitionResource.searchServiceDefinition(methodId, inputArray, request, response);
		}
		catch(Exception exp) {
			alert.prepareError("Caught exception at invoke of SearchServiceDefinitionOperation: ", exp).log();
			Dataset serviceDataset = new Dataset();
			serviceDataset.setId("servicedefinition");
			result.addDataset(serviceDataset);
		}
		return result;
	}

}