/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.resource.api;



import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface PortfolioDetailsResource extends Resource {
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
	
	Result getInstrumentTotal(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO) Gets the Asset Details
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 */

	Result getAssetAllocation(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);
	/**
	 * (INFO) Gets the Cash Balance
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	
	Result getCashBalance(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response);

	Result getPortfolioDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
