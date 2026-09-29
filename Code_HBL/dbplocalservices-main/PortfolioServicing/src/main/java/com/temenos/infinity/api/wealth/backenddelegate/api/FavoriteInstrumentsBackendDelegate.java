package com.temenos.infinity.api.wealth.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface FavoriteInstrumentsBackendDelegate extends BackendDelegate {

	/**
	 * (INFO) Fetches Favorite Instruments  for the given list of codes by calling business
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
	Result getFavoriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

}
