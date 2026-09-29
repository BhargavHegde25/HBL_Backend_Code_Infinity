package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the Result in the desired format for the Refinitiv service.
 *
 */

public class getSearchInstrumentListPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			
			//TransactTokenGenPreProcessor obj = new TransactTokenGenPreProcessor();
			//obj.execute(inputMap, request, response, result);
			String instrumentName = "";
			instrumentName = ("%27" + request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString().trim()
					+ "%27").replace(" ", "%20");
			inputMap.put("instrumentName", instrumentName);
			inputMap.put("paramValue", instrumentName);

			return true;

		} catch (Exception e) {
			alert.prepareError("Error in CancelNDMAOrdersPreProcessor" + e).log();
			e.getMessage();
			return false;
		}
	}

}
