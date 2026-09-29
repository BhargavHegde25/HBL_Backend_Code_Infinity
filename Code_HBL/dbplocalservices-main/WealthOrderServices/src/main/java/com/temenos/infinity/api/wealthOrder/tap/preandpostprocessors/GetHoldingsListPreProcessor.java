/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
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

			inputMap.put("minStatusE", "Accounted");
			inputMap.put("maxStatusE", "Accounted");
			return true;
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}
}
