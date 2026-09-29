package com.kony.adminconsole.service.signatorygroup.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface SignatoryGroupResource extends Resource {

	public Result createSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result deleteSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getNoGroupUsers(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getApprovalPermissionsForUser(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getAllSignatoryGroupsbyCoreCustomerIds(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getAllSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getSignatoryGroupDetails(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result fetchApprovalMode(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateApprovalMode(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result deleteApprovalMode(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result isSignatoryGroupEligibleForDelete(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
}
