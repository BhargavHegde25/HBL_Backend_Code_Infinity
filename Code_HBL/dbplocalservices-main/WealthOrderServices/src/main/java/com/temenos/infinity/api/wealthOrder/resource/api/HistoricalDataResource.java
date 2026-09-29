package com.temenos.infinity.api.wealthOrder.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface HistoricalDataResource extends Resource {

	/**
	 * (INFO) Fetches historical data for the given currency pairs by calling business
	 * delegate method to fetch the same
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 19459
	 */
	Result getHistoricalData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);


}
