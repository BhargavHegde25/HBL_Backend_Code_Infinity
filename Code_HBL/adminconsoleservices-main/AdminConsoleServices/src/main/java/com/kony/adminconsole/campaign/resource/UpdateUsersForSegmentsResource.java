package com.kony.adminconsole.campaign.resource;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface UpdateUsersForSegmentsResource extends Resource {
	
	Result updateActiveCountForAllSegments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getAndupdateActiveUsersForSegment(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	
}
