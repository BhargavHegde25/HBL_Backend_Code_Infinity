package com.temenos.infinity.api.wealthOrder.resource.api;



import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface HoldingsListResource extends Resource {
	/**
	 * (INFO) Gets the Instrument's total value
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 */

	Result getHoldingsList(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO) Gets the List returned by Search
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 */

	Result getSearchInstrumentList(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);

}
