package com.temenos.infinity.api.wealthOrder.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MarketOrderBackendDelegate extends BackendDelegate {

	/**
	 * (INFO) Fetches product details for the given ISIN/product code by calling business
	 * delegate method to fetch the same
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 19459
	 * @param headersMap
	 */
	Result createMarketOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

	/**
	 * (INFO) Modifies the order details for the chosen Order.
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 * @param headersMap
	 */
	Result modifyOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

}
