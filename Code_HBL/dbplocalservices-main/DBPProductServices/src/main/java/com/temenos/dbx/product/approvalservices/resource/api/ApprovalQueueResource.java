package com.temenos.dbx.product.approvalservices.resource.api;

import java.io.IOException;
import java.util.List;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;

public interface ApprovalQueueResource extends Resource{

	/**
	 * @author KH2626
	 * @version 1.0
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	public Result fetchRequestHistory(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);

	/**
	 *  method to reject ACH transaction
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result rejectACHTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to approve ach transaction
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result approveACHTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to approve ACH file
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result approveACHFileRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to reject ACH file
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result rejectACHFileRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to approve General Transaction
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result approveGeneralTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to reject General Transaction
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result rejectGeneralTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to withdraw ACH transaction
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result withdrawACHTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to withdraw ACH File
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result withdrawACHFileRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to withdraw General Transaction
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	Result withdrawGeneralTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * Auto rejects the pending transactions in the approval queue for which the required no of approvers are not available
	 * @param requestManager
	 * @param responseManager
	 * @param requestChain
	 */
	public void autoRejectPendingTransactionsInApprovalQueue(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager);
	
	/**
	 *  method to fetch Approvers
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 */
	public Result fetchApprovers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to fetch Records Pending for my approvals
	 *  @param methodID 
	 *  @param inputArray
	 *  @param request
	 *  @param response
	 *  @return {@link Result}
	 */
	public Result fetchRecordsPendingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException;

	/**
	 *  method to fetch History 
	 *  @param methodID 
	 *  @param inputArray
	 *  @param request
	 *  @param response
	 *  @return {@link Result}
	 */
	public Result fetchMyApprovalHistory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException;
	
	/**
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	public Result approve(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	public Result reject(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	public Result withdraw(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 *  @param methodID 
	 *  @param inputArray
	 *  @param request
	 *  @param response
	 *  @return {@link Result}
	 */
	public Result fetchAllMyPendingRequests(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException;
	
	/**
	 *  @param methodID 
	 *  @param inputArray
	 *  @param request
	 *  @param response
	 *  @return {@link Result}
	 */
	public Result fetchMyRequestHistory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException;
	
	/**
	 * @param requests
	 * @param DataControllerRequest
	 * @return {@link List<ApprovalRequestDTO>}
	 */
	public List<ApprovalRequestDTO> fetchAllRequests(List<BBRequestDTO> requests, DataControllerRequest dcr);

	/**
	 *  Method to get status of transaction by validating limits
	 *  @param methodID 
	 *  @param inputArray
	 *  @param request
	 *  @param response
	 *  @return {@link Result}
	 */
	public Result validateForApprovals(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException;

	/**
	 *  Method to update backend transactionId in approval queue
	 *  @param methodID 
	 *  @param inputArray
	 *  @param request
	 *  @param response
	 *  @return {@link Result}
	 */
	public Result updateBackendIdInApprovalQueue(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException;
	
	/**
	 *  Method to re-notify approver for pending request
	 *  @param methodID 
	 *  @param inputArray
	 *  @param request
	 *  @param response
	 *  @return {@link Result}
	 */
	public Result renotifyPendingApprovalRequest(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
}
