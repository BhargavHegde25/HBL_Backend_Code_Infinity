package com.temenos.infinity.api.arrangements.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface DocumentStorageResource extends Resource {

	public Result downloadDocument(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
	
	
	
	public Result searchDocument(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
	
}
