package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors.CancelDMAOrdersPreProcessor;

/**
 * (INFO) Builds the Result in the desired format for the Refinitiv service.
 *
 */

public class getDXMasterAssetTypesPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {

			if (((request.getParameter(TemenosConstants.APPLICATION) != null
					&& request.getParameter(TemenosConstants.APPLICATION).equalsIgnoreCase("DXMaster"))
					|| (request.getParameter("DXMaster") != null
							&& request.getParameter("DXMaster").equalsIgnoreCase("true"))
					|| request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null)
					|| request.getParameter("T24Favourite") != null) {
				if (request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null
						|| request.getParameter(TemenosConstants.OPSTATUS).equalsIgnoreCase("0")
						|| request.getParameter("T24Favourite") != null) {
					/*TransactTokenGenPreProcessor obj = new TransactTokenGenPreProcessor();
					obj.execute(inputMap, request, response, result);*/
					if (request.getParameter("T24Favourite") == null
							&& request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null) {
						inputMap.put("param", "instrumentName");
						String paramValue = request.getParameter("paramValue");
						inputMap.put("instrumentId", paramValue);
					} else if (request.getParameter("T24Favourite") != null) {
						inputMap.put("param", "contractId");
						String paramValue = request.getParameter("T24Instrumentids");
						paramValue = paramValue.replaceAll(" ", "%20").trim();
						paramValue = paramValue.replaceAll("#", "%23").trim();
						inputMap.put("instrumentId", paramValue);
						request.addRequestParam_("T24DXMasterflag", "true");
					} else {
						inputMap.put("param", "contractId");
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
