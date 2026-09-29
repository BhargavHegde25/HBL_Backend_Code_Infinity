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

public class getDXAssetTypesPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {

			if (request.getParameter("T24Favourite") != null
					|| request.getParameter(TemenosConstants.APPLICATION) != null
					|| request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null) {
				if (request.getParameter("T24Favourite") != null
						|| request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null
						|| request.getParameter(TemenosConstants.APPLICATION).equalsIgnoreCase("DX")) {
					/*TransactTokenGenPreProcessor obj = new TransactTokenGenPreProcessor();
					obj.execute(inputMap, request, response, result);*/
					if (request.getParameter("T24Favourite") == null
							&& request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null) {
						inputMap.put("param", "instrumentName");
						String paramValue = request.getParameter("paramValue");
						inputMap.put("instrumentId", paramValue);
					} else if (request.getParameter("T24Favourite") != null) {
						inputMap.put("param", "instrumentId");
						String paramValue = request.getParameter("T24Instrumentids");
						paramValue = paramValue.replaceAll(" ", "%20").trim();
						paramValue = paramValue.replaceAll("#", "%23").trim();
						inputMap.put("instrumentId", paramValue);
					} else {
						inputMap.put("param", "instrumentId");
					}

					return true;
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					return false;
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("Error in CancelNDMAOrdersPreProcessor" + e).log();
			e.getMessage();
			return false;
		}
	}

}
