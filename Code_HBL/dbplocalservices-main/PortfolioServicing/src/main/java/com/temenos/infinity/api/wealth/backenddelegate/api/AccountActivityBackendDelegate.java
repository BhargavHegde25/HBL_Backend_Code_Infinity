package com.temenos.infinity.api.wealth.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface AccountActivityBackendDelegate extends BackendDelegate {

	
	/**
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
