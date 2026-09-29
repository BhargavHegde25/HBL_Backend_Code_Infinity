package com.kony.adminconsole.service.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface EmployeeEntitlementResource extends Resource {
	
	public Result createEntitlementByUserId(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result getEntitlementByUserId(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
            
    public Result updateEntitlementByUserId(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result getEntitlement(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	
	public Result updateEntitlement(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    public Result deleteEntitlement(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
}
