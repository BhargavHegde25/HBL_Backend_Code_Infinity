package com.temenos.infinity.api.wealthOrder.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MarketOrderResource extends Resource {

	/**
	 * (INFO) Fetches Product Details for the given ISIN/ product Code by calling business
	 * delegate method to fetch the same
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22950
	 */
	Result createMarketOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	/**
	 * (INFO) Modifies the order details for the chosen Order. 
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 */

	Result modifyOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
}
