package com.temenos.infinity.api.wealthOrder.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface OrdersListResource extends Resource {

	/**
	 * (INFO) Gets the  Transaction's List
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 */
	
	Result getOrdersDetails(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);	
}
