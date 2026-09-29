package com.temenos.dbx.product.transactionservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

import com.konylabs.middleware.dataobject.Result;

public interface BulkWireTransactionsResource extends Resource {
	
    /** creates bulk Wire transactions
     * @param methodID operationName
     * @param inputArray input parameters
     * @param request DataControllerRequest
     * @param response DataControllerResponse
     * @return {@link Result}
     * @author KH1769
     */
    Result createBulkWireTransfer(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    /**
     * (INFO)Fetches the details of all the transactions on a bulk wire file,
     * After checking whether the user has the required permission.
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     * @author KH2264
     */
	Result getBulkWireFileTransactionsDetail(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
     * (INFO)Fetches the details of all the transactions executed on a particular bulkwirefile with the given execution Id,
     * After checking whether the user has the required permission.
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     * @author KH2265
     */
	Result getTransactionsByBulkWireFileExecutionId(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
     * (INFO)Fetches the details of all the transactions executed on a particular bulkwiretemplate with the given execution Id,
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     * @author KH2347
     */
	public Result getTransactionsByBulkWireTemplateExecutionId(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	/**
     * (INFO)Fetches the details of all the transactions on a bulk wire template,
     * After checking whether the user has the required permission.
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     * @author KH2347
     */
	public Result getBulkWireTemplateTransactionDetail(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
