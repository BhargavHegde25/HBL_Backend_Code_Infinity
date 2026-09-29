package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;


import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * 
 * (INFO) Prepares the input for the TAP service in the desired format. 
 * 
 * @author himaja.sridhar
 *
 **/

public class GetSearchInstrumentListPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String search = inputMap.get(TemenosConstants.INSTRUMENTNAME).toString();
		search = ("*").concat(search.concat("*"));
		inputMap.put(TemenosConstants.INSTRUMENTNAME, search);
		return true;
	}

}
