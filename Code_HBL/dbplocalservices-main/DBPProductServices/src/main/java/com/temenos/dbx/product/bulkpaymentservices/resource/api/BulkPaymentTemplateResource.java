package com.temenos.dbx.product.bulkpaymentservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BulkPaymentTemplateResource extends Resource {


	/**
	 * method to create Bulk payment Template
	 * @param methodID operationName
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with Bulk Payment Template Details
	 *  
	 */
	Result createBulkPaymentTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * method to fetch Bulk payment uploaded FilesTemplates
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with Bulk Payment Templates Details
	 *  
	 */
	public Result fetchBulkPaymentTemplates(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);

	/**
    * @param request DataControllerRequest
    * @param response DataControllerResponse
    * @return Result Object with deleted Bulk Payment Template status Details
    *  
    */
	Result deleteBulkPaymentTemplate(String methodId, Object[] inputArray, DataControllerRequest request,
           DataControllerResponse response);

    /**
    * @param request DataControllerRequest
    * @param response DataControllerResponse
    * @return Result Object with deleted Bulk Payment Template status Details
    *  
    */
    Result editBulkPaymentTemplate(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    /**
     * @param request DataControllerRequest
     * @param response DataControllerResponse
     * @return Result Object with bulk request details
     *  
     */
    public Result createBulkRequest(String methodID, Object[] inputArray, DataControllerRequest dcRequest, 
			DataControllerResponse response);
    
    /**
	 * method to fetch Bulk payment template POs by templateId
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with Bulk Payment Templates Details
	 *  
	 */
    public Result fetchPOsByTemplateId(String methodID, Object[] inputArray, DataControllerRequest request,
    		DataControllerResponse response);
    
    /**
   	 * method to fetch Bulk payment template details by templateId
   	 * @param inputArray input parameters 
   	 * @param request DataControllerRequest
   	 * @param response DataControllerResponse
   	 * @return Result Object with Bulk Payment Templates Details
   	 *  
   	 */
       public Result fetchTemplateByTemplateId(String methodID, Object[] inputArray, DataControllerRequest request,
       		DataControllerResponse response);
}
