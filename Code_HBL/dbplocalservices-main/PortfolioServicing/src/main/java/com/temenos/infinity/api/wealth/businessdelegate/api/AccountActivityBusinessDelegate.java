package com.temenos.infinity.api.wealth.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface AccountActivityBusinessDelegate extends BusinessDelegate {

	/**
	 * (INFO) Gets the Accountactivity
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @param headersMap
	 * @return
	 */
	Result getAccountActivity(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

	/**
	 * (INFO) Gets the Account Activity by Search and Sort
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @param headersMap
	 * @return
	 */
	Result getAccountActivityOperations(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
}
