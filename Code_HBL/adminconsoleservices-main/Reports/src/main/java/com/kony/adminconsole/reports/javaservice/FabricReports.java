package com.kony.adminconsole.reports.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.reports.resource.api.FabricReportsManagementResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

public class FabricReports implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		FabricReportsManagementResource dcResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(FabricReportsManagementResource.class);

		return dcResource.manageFabricReports(methodID, inputArray, request, response);
	}
}
