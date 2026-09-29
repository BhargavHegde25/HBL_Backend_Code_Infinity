package com.kony.adminconsole.campaign.resource;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface DataContextResource  extends Resource {
	
	Result getActiveCountForSegment(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
