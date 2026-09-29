package com.temenos.dbx.product.signatorygroupservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


/**
 * 
 * @author KH1769
 * @version 1.0
 * Interface for ApprovalRuleResource extends {@link Resource}
 *
 */
public interface ApprovalModeResource extends Resource{
	
	/**
	 * (INFO)calls the business delegate for fetching all the assigned users of coreCustomerId(given in input params) 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH1769
	 */
	public Result fetchApprovalMode(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO)calls the business delegate for fetching all the assigned users of coreCustomerId(given in input params) 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH1769
	 */
	public Result updateApprovalMode(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO)calls the business delegate for fetching all the assigned users of coreCustomerId(given in input params) 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH1769
	 */
	public Result deleteApprovalMode(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	
	
}
