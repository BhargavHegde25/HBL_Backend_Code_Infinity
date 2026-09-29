package com.temenos.infinity.api.wealthOrder.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CurrencyBackendDelegate extends BackendDelegate {

	/**
	 * (INFO) Fetches market rates for the given currency pairs by calling backend
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
	Result getMarketRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

	/**
	 * (INFO) Creates forex order by calling backend delegate method to create the
	 * same
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 19459
	 */
	Result createForexOrders(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

}
