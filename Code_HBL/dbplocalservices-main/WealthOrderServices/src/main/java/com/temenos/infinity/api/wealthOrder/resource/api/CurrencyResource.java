package com.temenos.infinity.api.wealthOrder.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CurrencyResource extends Resource {

	/**
	 * (INFO) Fetches market rates for the given currency pairs by calling business
	 * delegate method to fetch the same
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 19459
	 */
	Result getMarketRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * (INFO) Creates forex order by calling business
	 * delegate method to fetch the same
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 19459
	 */
	Result createForexOrders(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
