package com.dbp.batchprocessengine.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface AlertSubscribersResource extends Resource {

	Result getSubscribers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
