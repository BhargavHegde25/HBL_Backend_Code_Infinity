package com.kony.adminconsole.reports.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.reports.resource.api.ManageDataSourcesResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetDataSourcesService implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		ManageDataSourcesResource dcResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(ManageDataSourcesResource.class);

		return dcResource.
				getDataSourcesList(methodID, inputArray, request, response);
	
	}

}
