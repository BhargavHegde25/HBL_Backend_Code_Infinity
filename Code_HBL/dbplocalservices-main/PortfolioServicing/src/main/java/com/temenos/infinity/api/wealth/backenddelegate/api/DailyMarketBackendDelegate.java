/**
 * 
 */
package com.temenos.infinity.api.wealth.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public interface DailyMarketBackendDelegate extends BackendDelegate {
	/**
	 * (INFO) Fetches Daily Market
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 * @param headersMap
	 */
	Result getDailyMarket(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

}
