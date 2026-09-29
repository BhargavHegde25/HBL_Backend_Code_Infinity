package com.temenos.dbx.product.transactionservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

import com.konylabs.middleware.dataobject.Result;

public interface IntraBankFundTransferResource extends Resource {

    /**
     * creates internal transaction after validating the limits and puts into approval queue if required
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);
    /**
     * updates the status of a given transactionId along with confirmation number from backend
	 * @param MethodID operation name 
	 * @param inputArray {transaction_id, status, confirmationNumber}
	 * @param request DataControllerRequest object
	 * @param response DataControllerResponse object
	 * @return Result that contains the processed response of an operation. 
	**/
	public Result updateStatus(String MethodID,Object [] inputArray,
			DataControllerRequest request, DataControllerResponse response);
	

	/** InternalTranscation post processor this is hooked on the line of business service to 
     *  frame the response from core and to update the transaction status in DBX
     * @param inputArray input parameters
     * @param request DataControllerRequest
     * @param response DataControllerResponse
     * @return {@link Result}
     */
    Result processResponseFromLineOfBusiness(Result result, DataControllerRequest request,
                    DataControllerResponse response);
    
    /**
     * Edits an intrabank fund transaction after validating the limits and puts into approval queue if required
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    Result editTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);
    
    /**
     * Cancel Scheduled Transaction Occurrence
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    Result cancelScheduledTransactionOccurrence(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);
    
    /**
     * Delete Transaction
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    Result deleteTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);
    
}
