package com.temenos.infinity.api.wealth.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public interface WealthDashboardBackendDelegate extends BackendDelegate {

/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @param headersMap
	 * @return
	 */
	Result getPortfolioList(String methodID, Object[] inputArray,DataControllerRequest request,
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
	Result getAssetList(String methodID, Object[] inputArray,DataControllerRequest request,
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
				Result getDashboardRecentActivity(String methodID, Object[] inputArray,DataControllerRequest request,
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
	Result getDashboardGraphData(String methodID, Object[] inputArray,DataControllerRequest request,
								DataControllerResponse response, Map<String, Object> headersMap);
}
