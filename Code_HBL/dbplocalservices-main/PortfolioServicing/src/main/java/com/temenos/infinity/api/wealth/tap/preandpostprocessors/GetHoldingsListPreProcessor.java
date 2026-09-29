/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.HashMap;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;
/**
 * @author balaji.krishnan
 *
 */
public class GetHoldingsListPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {

			TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
			obj.execute(inputMap, request, response, result);
			String isIncludeOrders = request.getParameter(TemenosConstants.ISINCLUEORDERS);
			//String isIncludeOrders = "false";// disabling includeOrders implementation for tap
			if(isIncludeOrders.equalsIgnoreCase("true")) {
				inputMap.put("minStatusE", "Cancelled");
				request.addRequestParam_(TemenosConstants.ISINCLUEORDERS,"true");
			}else {
				inputMap.put("minStatusE", "Accounted");
				request.addRequestParam_(TemenosConstants.ISINCLUEORDERS,"false");
			}
			inputMap.put("maxStatusE", "Accounted");
			return true;
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}
}
