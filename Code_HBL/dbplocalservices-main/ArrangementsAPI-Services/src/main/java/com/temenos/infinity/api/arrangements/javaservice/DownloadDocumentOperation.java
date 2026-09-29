package com.temenos.infinity.api.arrangements.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.resource.api.DocumentStorageResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class DownloadDocumentOperation implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		DocumentStorageResource documentStorageResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(DocumentStorageResource.class);
		Result result = documentStorageResource.downloadDocument(methodID, inputArray, request, response);
		
		return result;
	}
	

}
