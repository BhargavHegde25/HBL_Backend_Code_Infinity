package com.temenos.dbx.product.approvalmatrixservices.resource.api;
import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH9450
 * @version 1.0
 * Interface for ApprovalMatrixResource extends {@link Resource}
 *
 */
public interface ApprovalMatrixResource extends Resource{

	/**
	 *  method to fetch Approval matrix records 
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
	Result fetchApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to update Approval Matrix 
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return result object with success or failure message
	 *  
	 */
	Result updateApprovalMatrixEntry(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException;
	
	/**
	 *  method to disable Approval Matrix  
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
	Result disableApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to disable Approval Matrix  
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
	Result updateApprovalMatrixStatus(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to enable Approval Matrix 
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
	Result enableApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to check if Approval Matrix is disabled 
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
	Result isApprovalMatrixDisabled(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 *  method to fetch Approval matrix records by only contract id 
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
	Result fetchApprovalMatrixByContractId(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
