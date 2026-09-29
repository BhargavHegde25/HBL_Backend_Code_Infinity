package com.kony.adminconsole.service.productmanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface FacilityResource extends Resource {

	public Result createFacility(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result editFacility(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getFacility(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
}
