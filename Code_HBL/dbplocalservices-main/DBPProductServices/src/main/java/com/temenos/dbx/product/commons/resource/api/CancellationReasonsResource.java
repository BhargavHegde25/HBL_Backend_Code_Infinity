package com.temenos.dbx.product.commons.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CancellationReasonsResource extends Resource {

	public Result fetchCancellationReasons(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);	
}
