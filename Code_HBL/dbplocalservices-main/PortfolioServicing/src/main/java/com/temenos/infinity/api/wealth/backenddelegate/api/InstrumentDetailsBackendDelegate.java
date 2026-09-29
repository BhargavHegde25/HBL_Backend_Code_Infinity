package com.temenos.infinity.api.wealth.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface InstrumentDetailsBackendDelegate extends BackendDelegate {

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
	Result getInstrumentDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

}
