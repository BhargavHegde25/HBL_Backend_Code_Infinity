package com.dbp.campaigndeliveryengine.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ServeEventResource extends Resource{
	
	Result serveEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
