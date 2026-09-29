package com.temenos.dbx.product.achservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2638
 * @version 1.0
 * Interface for ACHFileServicesResource extends {@link Resource}
 *
 */
public interface ACHFileResource extends Resource{

	/**
	 *  method to upload ACH File
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with ACH File Details
	 *  
	 */
	public Result uploadACHFile(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 *  method to get file formats
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with ACH File Formats
	 *  
	 */
	public Result getACHFileFormats(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 *  method to fetch all ACH files
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with All ACH Files Details
	 *  
	 */
	public Result fetchAllACHFiles(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);
	
	/**
	 *  method to fetch ACH file of given Id
	 *  @param methodID operationName
	 *  @param inputArray input parameters with achfile_id 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with ACH Files Details of given Id
	 *  
	 */
	public Result fetchACHFileById(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);
	
}