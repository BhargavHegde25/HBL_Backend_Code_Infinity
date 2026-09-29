/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * (INFO) Gets the Transaction's List
 * 
 * @param methodID
 * @param inputArray
 * @param request
 * @param response
 * @return {@link Result}
 * @param headersMap
 * @author 22952
 */
public interface CancelOrderBackendDelegate extends BackendDelegate {
	
	Result cancelOrder(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

}
