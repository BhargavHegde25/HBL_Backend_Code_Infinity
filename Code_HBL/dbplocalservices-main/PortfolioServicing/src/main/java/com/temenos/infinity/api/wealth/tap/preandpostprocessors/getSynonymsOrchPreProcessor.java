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
 * (INFO) Prepares the input for the TAP service in the desired format.
 * 
 * @author himaja.sridhar
 *
 */
public class getSynonymsOrchPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		
		//inputMap.put("id", request.getParameter("idVal").toString());
		inputMap.put("loop_count", request.getParameter("idCnt"));
		
		
		return true;
	}

}
