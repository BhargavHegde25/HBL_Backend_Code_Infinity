/**
 * 
 */
package com.temenos.infinity.api.wealth.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public interface TopMarketNewsResource extends Resource {

	/**
	 * (INFO)  Fetches Daily Market
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 */

	Result getTopMarketNews(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);
}
