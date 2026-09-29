package com.temenos.infinity.api.wealthOrder.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface FavoriteInstrumentsResource extends Resource {

	/**
	 * (INFO) Fetches Favorite Instruments  for the given list of codes by calling business
	 * delegate method to fetch the same
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22950
	 */
	Result getFavoriteInstrumentsResource(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	
}
