package com.temenos.infinity.wealth.mock.processor.pre;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetTopMarketNewsMockPreProcessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetTopMarketNewsMockPreProcessor Mock - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			
			if (wealthCore != null && (wealthCore.equalsIgnoreCase("Mock"))) {
				diagnostic.prepareDebug("==========> GetTopMarketNewsMockPreProcessor Mock - Core check done").log();
				//Map<String, Object> inputMap = new HashMap<>();
				//Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
				Object topicObj = request.getParameter(TemenosConstants.TOPIC);
				Object limitObj = request.getParameter(TemenosConstants.PAGESIZE);
				Object offsetObj = request.getParameter(TemenosConstants.PAGEOFFSET);
				if (topicObj == null || topicObj.equals("")) {
					return unauthAccess(result,TemenosConstants.TOPIC);
				} else {
					String topic = null, limitVal = null, offsetVal = null;
					String maxCount = "4";

					if (topicObj != null) {
						topic = request.getParameter(TemenosConstants.TOPIC).toString();
						inputMap.put(TemenosConstants.TOPIC, topic);
					}

					if (limitObj != null) {
						limitVal = request.getParameter(TemenosConstants.PAGESIZE).toString();
						maxCount = limitVal;
						inputMap.put(TemenosConstants.PAGESIZE, limitVal);
					}
					if (offsetObj != null) {
						offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET).toString();
						inputMap.put(TemenosConstants.PAGEOFFSET, offsetVal);
					}
					
					
					inputMap.put(TemenosConstants.MAXCOUNT, maxCount);
					inputMap.put("ReturnPrivateNetworkURL", "false");
					request.addRequestParam_(TemenosConstants.MAXCOUNT, maxCount);
					request.addRequestParam_("ReturnPrivateNetworkURL", "false");
					return true;
				}

				}
			 else {
					                result.addOpstatusParam("0");
					                result.addHttpStatusCodeParam("200");
					                result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					                diagnostic.prepareDebug("==========> GetTopMarketNewsMockPreProcessor Mock - Exiting for token generation").log();
					                return false;
					            }

					 

					        } catch (Exception e) {
					        	alert.prepareError("==========> GetTopMarketNewsMockPreProcessor Mock - Error: " + e.getMessage()).log();
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
