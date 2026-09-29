package com.kony.adminconsole.service.customerrole.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author kruthi.manojna
 *
 */
public interface CustomerRoleResource extends Resource{
	
    /**
     *  This method gets all the group action limits
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing group action limits
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the group details
     */
    public Result fetchAllGroupActionLimits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

    /**
     *  This method gets all the customer groups (or FI defined Roles)
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing groups
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the  group details
     */
	public Result fetchAllGroups(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

    /**
     *  This method creates a group
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to create a group
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the group details
     */
	public Result createGroup(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;

    /**
     *  This method edits a group / Customer role
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a group
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the group details
     */
	public Result editGroup(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

    /**
     *  This method activates/deactivates a Customer role
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a group
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the group details
     */
	public Result manageStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     *  This method fetches the list of all Customer roles
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a group
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the group details
     */
	public Result downloadGroupsList(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
	
	/**
     *  This method fetches the list of all Common features,actions and limits between given Customer role id and service definition id
     *  @param methodId contains the operation id
     *  @param inputArray contains the group id and service definition id
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the feature action details
     */

	public Result getCommonFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
