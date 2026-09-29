package com.kony.adminconsole.service.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface UserFeatureResource extends Resource {
	
	public Result getInternalUserFeatures(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result getInternalUserFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result updateInternalUserFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result updateInternalUserActionStatus(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result getAllInternalFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
}
