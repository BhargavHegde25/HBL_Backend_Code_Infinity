package com.temenos.infinity.api.wealth.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public interface WealthDashboardBusinessDelegate extends BusinessDelegate {
	
	/**
	 * (INFO) Gets the Portfolio list for Dashboard
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
	 * (INFO) Gets the Asset list for Dashboard
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
	 * (INFO) Gets the Recent Activity list for Dashboard
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
				 * (INFO) Gets the graph data for dashboard
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
