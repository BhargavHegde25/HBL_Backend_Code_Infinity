/**
 * 
 */
package com.temenos.infinity.api.wealth.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface PortfolioDetailsBusinessDelegate extends BusinessDelegate {
	/**
	 * (INFO) Gets the Instrument's total value
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @param headersMap
	 * @author 22952
	 */
	
	Result getInstrumentTotal(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
	/**
	 * (INFO) Gets the Asset Details
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @param headersMap
	 * @author 22952
	 */

	Result getAssetAllocation(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
	/**
	 * (INFO) Gets the Cash Balance
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @param headersMap
	 */
	
	Result getCashBalance(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
	
	Result getPortfolioDetails(String methodID, Object[] inputArray,DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
	/**
	 * (INFO) Gets the Portfolio's Health
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22952
	 */
	Result getPortfolioHealth(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
	Result getAllocation(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
	
}
