package com.temenos.infinity.api.wealth.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public interface WealthDashboardResource extends Resource {
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 */
	Result getPortfolioList(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);	
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 */
	Result getAssetList(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);
			
				/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @author 22952
	 * @param response
	 * @return
	 */
	Result getDashboardRecentActivity(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);	
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 */
	Result getDashboardGraphData(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);
}
