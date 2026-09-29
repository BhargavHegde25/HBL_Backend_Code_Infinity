package com.kony.adminconsole.service.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface EmployeeRoleResource extends Resource {
	
	public Result getEmployeeRoles(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result getEmployeeRoleDetails(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
            
    public Result createEmployeeRole(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result updateEmployeeRoleDetails(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result updateEmployeeRoleStatus(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
}
