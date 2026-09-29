package com.temenos.infinity.product.bulkpaymentservices.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BulkPaymentPOResource extends Resource{
	
	/**
	 *  method to add Payment Order
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Payment Order Details
	 *  
	 */
	public Result addPaymentOrder(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	
	/**
	 *  method to fetch Payment Orders
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Payment Order Details
	 * @throws ApplicationException 
	 *  
	 */
	public Result fetchPaymentOrders(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	
	/**
	 *  method to Edit Payment Orders
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Payment Order Details
	 * @throws ApplicationException 
	 *  
	 */
	public Result editPaymentOrder(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	
	/**
	 *  method to Edit Payment Orders
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Payment Order Details
	 * @throws ApplicationException 
	 *  
	 */
	public Result deletePaymentOrder(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);

}
