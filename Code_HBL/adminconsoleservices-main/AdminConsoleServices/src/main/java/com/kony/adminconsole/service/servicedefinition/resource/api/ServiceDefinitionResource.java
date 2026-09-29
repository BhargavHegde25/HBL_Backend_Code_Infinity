package com.kony.adminconsole.service.servicedefinition.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ServiceDefinitionResource extends Resource {
	
	/**
     *  This method deletes a service definition
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to delete an existing service definition
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains acknowledgement for deleted service definition
     */

    public Result deleteServiceDefinition(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);
    
    /**
     *  This method gets all the service definition
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing service definition
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the  service definition details
     */
    public Result fetchAllServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
    
    /**
     *  This method creates a service definition
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to create a service definition
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the service definition details
     */
    public Result createServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
    
    /**
     *  This method gets all the service definition action limits
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing service definition action limits
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the  service definition details
     */
    public Result fetchAllServiceDefinitionActionLimits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
    
    /**
     *  This method edits a service definition
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a service definition
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the service definition details
     */
    public Result editServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
    
    /**
     *  This method deactivates a service definition
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to deactivate a service definition
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the service definition details
     */
    public Result deactivateServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;

	/**
	 * This method fetches all roles associated to a service definition
	 * @param methodId
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return Result object contains the Roles details
	 */
	public Result fetchAllRolesForServiceDefinition(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * This method updates default role associated to a service definition
	 * @param methodId
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return Result
	 */
	public Result updateDefaultRole(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
     *  This method fetches all active service definitions which are associated to one or more active roles
     *  @param methodId contains the operation id
     *  @param inputArray 
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the service definition details
     */
	public Result fetchAllServiceDefinitionsForContract(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response);

	/**
     *  This method fetches all feature action limits for service definitions
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter of service definition id
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the service definition details
     */
	public Result fetchAllServiceDefinitionFeatureLimits(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response);
	
	/**
     *  This method fetches all  limits and actions for service definitions
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter of service definition id
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the service definition details
     */
	public Result getServiceDefinitionMonetaryActions(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response);
	
	/**
     *  This method searches for the service definition
     *  @author KH2691
     *  @version 1.0
     *  @param methodId contains the search text
     *  @param inputArray contains the input parameter to fetch existing service definition
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the  service definition details matched with search text
     */
    public Result searchServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
    

    public Result getServiceDefinitionProductIdPermissions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) ;
}
