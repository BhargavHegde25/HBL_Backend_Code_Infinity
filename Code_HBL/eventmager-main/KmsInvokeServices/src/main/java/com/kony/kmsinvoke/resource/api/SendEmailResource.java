package com.kony.kmsinvoke.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface SendEmailResource extends Resource {
	
	Result sendEmail(Object[] inputArray, DataControllerRequest request);

}
