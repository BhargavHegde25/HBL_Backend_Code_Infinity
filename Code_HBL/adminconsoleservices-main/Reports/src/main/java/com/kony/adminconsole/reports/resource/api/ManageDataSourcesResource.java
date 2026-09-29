package com.kony.adminconsole.reports.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ManageDataSourcesResource extends Resource {
	Result getDataSourcesList(String methodID,
			Object[] inputArray,
			DataControllerRequest request,
			DataControllerResponse response);
}
