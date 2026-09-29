package com.temenos.dbx.product.achservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2395
 * @version 1.0
 * Interface for ACHCommonsResource extends {@link Resource}
 *
 */

public interface ACHCommonsResource extends Resource {
	/**
	 *  method to fetch template request type
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with template request type
	 *  
	 */

	public Result fetchTemplateRequestType(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to fetch transaction type
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with transaction types
	 *  
	 */
	
	public Result fetchBBTransactionTypes(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 *  method to fetch tax type
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with tax types
	 *  
	 */
	
	public Result fetchACHTaxType(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 *  method to fetch tax subtype
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with tax sub types
	 *  
	 */
	public Result fetchACHTaxSubType(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
}
