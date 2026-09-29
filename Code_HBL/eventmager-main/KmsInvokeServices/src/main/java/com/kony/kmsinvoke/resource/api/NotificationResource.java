package com.kony.kmsinvoke.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface NotificationResource extends Resource {
	
	Result sendNotification(Object[] inputArray, DataControllerRequest request);

}
