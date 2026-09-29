package com.kony.adminconsole.reports.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ReportsManagementResource  extends Resource{
	Result manageReports(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	Result getUserReports(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
