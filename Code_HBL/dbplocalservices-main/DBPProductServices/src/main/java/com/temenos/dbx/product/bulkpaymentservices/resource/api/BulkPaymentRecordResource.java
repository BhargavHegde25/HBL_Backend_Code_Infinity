package com.temenos.dbx.product.bulkpaymentservices.resource.api;


import java.util.List;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;

public interface BulkPaymentRecordResource extends Resource{
	

	/**
	 *  method to fetch ongoing Bulk payments
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Bulk Payment File Details
	 * @throws ApplicationException 
	 *  
	 */
	public Result fetchOnGoingBulkPayments(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) throws ApplicationException;
	
	/**
	 * method to fetch Bulk payment history
	 * @param methodID operationName
	 * @param inputArray input parameters 
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with Bulk Payment History Details
	 *  
	 */
	public Result fetchBulkPaymentHistory(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 * method to cancel Bulk payment record
	 * @param methodID operationName
	 * @param inputArray input parameters containing recordID
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result Object with Bulk Payment recordId, confirmation Number and status 
	 *  
	 */
	public Result cancelBulkPaymentRecord(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);


	/**
	 * method to review bulk payment record
	 * @param methodID operationName
	 * @param inputArray input parameters
	 * @param request DataControllerRequest
	 * @param response DataControllerResponse
	 * @return Result object with review status
	 * @throws MiddlewareException 
	 */
	public Result review(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) throws MiddlewareException;
	
	
	/**
	 *  method to approve bulk payment record
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	public Result approveBulkPaymentRecord(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to update bulk payment record
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result object with confirmation number
	 *  
	 */
	public Result updateBulkPaymentRecord(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to reject BulkPaymentRecord
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	public Result rejectBulkPaymentRecord(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	

	/**
	 *  method to fetch BulkPayment Records Waiting For Approval
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	public Result fetchRecordsWaitingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 *  method to fetch Records Reviewed By Me And In ApprovalQueue
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	public Result fetchRecordsReviewedByMeAndInApprovalQueue(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response);

	/**
	 *  method to fetch Record History Reviewed By Me
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	public Result fetchRecordHistoryReviewedByMe(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to fetch Record History Acted By Me
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	public Result FetchRecordsHistoryActedByMe(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to fetch Backend Response And Merge Approval Info
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return {@link Result}
	 *  
	 */
	public List<BulkPaymentRecordDTO> fetchBackendResponseAndMergeApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);
	
	/**
	 * Logs BulkPayment request rejections and approvals auditactivity
	 * @param request
	 * @param response
	 * @param res
	 * @param eventSubType
	 * @param result
	 */
	public void logApproveOrRejectTransaction (DataControllerRequest request, DataControllerResponse response, Object res, String eventSubType, Result result);
	
	/**
	 * Fetch record Details by Id
	 * @param request
	 * @param response
	 * @param res
	 * @param eventSubType
	 * @param result
	 */
	public Result fetchBulkPaymentRecordById(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);
	
	/**
	 *  method to Execution Dates
	 *  @param timeValue Time Frame Required
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Execution Date Details
	 * @throws ApplicationException 
	 *  
	 */
	public Result fetchExecutionDates(String timeValue, DataControllerRequest request, 
			DataControllerResponse response) throws ApplicationException;
}
