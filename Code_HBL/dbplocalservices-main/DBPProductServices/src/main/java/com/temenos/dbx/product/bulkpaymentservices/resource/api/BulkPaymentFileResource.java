package com.temenos.dbx.product.bulkpaymentservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BulkPaymentFileResource extends Resource{
	/**
	 *  method to upload Bulk payment File
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Bulk Payment File Details
	 *  
	 */
	public Result uploadBulkPaymentFile(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 * method to fetch Bulk payment Sample Files
	 * @param methodID operationName
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with Bulk Payment Sample Files Details
	 *  
	 */
	public Result fetchBulkPaymentSampleFiles(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 * method to fetch Bulk payment uploaded Files
	 * @param methodID operationName
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with Bulk Payment Uploaded Files Details
	 *  
	 */
	public Result fetchBulkPaymentUploadedFiles(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	/**
	 * method to download bulk payment file
	 * @param methodID operationName
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with bulk payment details PDF
	 *  
	 */
	public Result initiateBulkPaymentAckFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * method to initiate download for bulk payment file acknowledgment
	 * @param methodID operationName
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with fileId details
	 *  
	 */
	public Result downloadBulkPaymentAckFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
