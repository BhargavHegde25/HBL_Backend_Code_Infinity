package com.temenos.dbx.party.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.party.resource.api.DueDiligenceResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class UpdateAssetLiabilitiesOperation implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		DueDiligenceResource dueDiligenceResource = DBPAPIAbstractFactoryImpl.getResource(DueDiligenceResource.class);
		return dueDiligenceResource.updateAssetLiablities(methodId, inputArray, request, response);
	}

}
