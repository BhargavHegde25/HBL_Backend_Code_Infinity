package com.temenos.dbx.product.approvalservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


/**
 * 
 * @author KH1755
 * @version 1.0
 * Interface for ApprovalRequestResource extends {@link Resource}
 *
 */
public interface ApprovalRequestResource extends Resource{	

	/**
	 *  method to get the counts for approvals and requests
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of JSON containing all the approval rules
	 *  
	 */
	public Result getCounts(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 *  method to fetch ACH files pending for my approval
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of ACH file details pending for my approval
	 *  
	 */
	public Result fetchACHFilesPendingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 *  method to fetch my ACH file requests
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of ACH file requests 
	 *  
	 */
	public Result fetchACHFileMyRequests(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 *  method to fetch rejected ACH Files
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of rejected ACH Files 
	 *  
	 */
	public Result fetchRejectedACHFiles(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 *  method to fetch general transactions pending for my approval
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of general transaction details pending for my approval
	 *  
	 */
	public Result fetchGeneralTransactionsPendingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 *  method to fetch my general transaction requests
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of general transaction requests 
	 *  
	 */
	public Result fetchGeneralTransactionMyRequests(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

	/**
	 * @author KH2626
	 * @version 1.0
	 * @param methodID contains id related to the invoked service
	 * @param inputArray contains request params
	 * @param request sdk request object
	 * @param response sdk response object
	 * @return list of ach transactions pending for approval
	 * @throws Exception
	 */
	public Result fetchACHTransactionsPendingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 * @author KH2626
	 * @version 1.0
	 * @param methodID contains id related to the invoked service
	 * @param inputArray contains request params
	 * @param request sdk request object
	 * @param response sdk response object
	 * @return list of ach transactions that are rejected
	 * @throws Exception
	 */
	public Result fetchRejectedACHTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 * @author KH2626
	 * @version 1.0
	 * @param methodID contains id related to the invoked service
	 * @param inputArray contains request params
	 * @param request sdk request object
	 * @param response sdk response object
	 * @return list of ach transactions went for request queue
	 * @throws Exception
	 */
	public Result fetchACHTransactionMyRequests(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	/**
	 *  method to get the pending counts for approvals and requests
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of JSON containing all the approval rules
	 *  
	 */
	public Result getPendingCounts(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}