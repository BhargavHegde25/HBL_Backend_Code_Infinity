package com.temenos.dbx.product.transactionservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2638
 * @version 1.0
 * Interface for GeneralTransactionsResource extends {@link Resource}
 *
 */
public interface GeneralTransactionsResource extends Resource{
	
	/**
	 *  method to fetch all general transactions
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with All General Transaction Details
	 *  
	 */
	public Result fetchAllGeneralTransactions(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);
	
	/**
	 *  method to fetch general transaction with given transaction_id and feature action id
	 *  @param methodID operationName
	 *  @param inputArray input parameters with transaction_id and featureActionId 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with General Transaction Details of given transaction_id
	 *  
	 */
	public Result fetchGeneralTransactionById(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);

	public Result getPurposeCodesById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;

	public Result getExternalCodes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
	
	
}