package com.temenos.infinity.wealth.refinitiv.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetTopMarketNewsRefinitivPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {

			diagnostic.prepareDebug("==========> GetTopMarketNewsRefinitivPreProcessor Refinitiv - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			result.addParam(TemenosConstants.WEALTH_CORE, wealthCore);
			if (wealthCore != null
					&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("T24,Refinitiv"))) {

				// Map<String, Object> inputMap = new HashMap<>();
				// Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
				Object topicObj = request.getParameter(TemenosConstants.TOPIC);
				Object limitObj = request.getParameter(TemenosConstants.PAGESIZE);
				Object offsetObj = request.getParameter(TemenosConstants.PAGEOFFSET);
				if (topicObj == null || topicObj.equals("")) {
					return unauthAccess(result, TemenosConstants.TOPIC);
				} else {
					String topic = null, limitVal = null, offsetVal = null;
					String maxCount = "10";

					if (topicObj != null) {
						topic = request.getParameter(TemenosConstants.TOPIC).toString();
						inputMap.put(TemenosConstants.TOPIC, topic);
					}

					if (limitObj != null) {
						limitVal = request.getParameter(TemenosConstants.PAGESIZE).toString();
						inputMap.put(TemenosConstants.PAGESIZE, limitVal);
					}
					if (offsetObj != null) {
						offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET).toString();
						inputMap.put(TemenosConstants.PAGEOFFSET, offsetVal);
					}
					if (limitObj != null && offsetObj != null) {
						maxCount = "30";
					}
					

					inputMap.put(TemenosConstants.MAXCOUNT, maxCount);
					inputMap.put("ReturnPrivateNetworkURL", "false");
					request.addRequestParam_(TemenosConstants.MAXCOUNT, maxCount);
					request.addRequestParam_("ReturnPrivateNetworkURL", "false");
				}
				diagnostic.prepareDebug("==========> GetTopMarketNewsRefinitivPreProcessor Refinitiv - Exited ").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetTopMarketNewsRefinitivPreProcessor Refinitiv - Exiting without Token Generation ").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetTopMarketNewsRefinitivPreProcessor Refinitiv - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

	private boolean unauthAccess(Result result, String param) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + param + " is mandatory.");
		return false;

	}
}
