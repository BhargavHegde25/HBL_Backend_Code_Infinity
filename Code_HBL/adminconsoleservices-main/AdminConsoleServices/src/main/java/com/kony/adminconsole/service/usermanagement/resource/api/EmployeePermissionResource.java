package com.kony.adminconsole.service.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface EmployeePermissionResource extends Resource {
	
	public Result getEmployeePermissions(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result getEmployeePermissionDetails(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
            
    public Result createEmployeePermission(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result updateEmployeePermissionDetails(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result updateEmployeePermissionStatus(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    public Result fetchLegalEntityList(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

	public Result getPermissionsByLegalEntities(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
}