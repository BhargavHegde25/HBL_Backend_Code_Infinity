/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentsOrchPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
				
				String[] strArr = request.getParameter("instrumentId").split(" ");
				inputMap.put("loop_count", strArr.length);
				inputMap.put("instrumentId", strArr);
				
		return true;
	}

}
