/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetAllStrategiesLoopPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {

				String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler
						.getValue(TemenosConstants.INF_WLTH_STRATEGIES, request);
				JSONObject json = new JSONObject(INF_WLTH_STRATEGIES);
				inputMap.put("loop_count", json.length());
				String strategyName = "",strategyValue="";
				int score = 0;
				boolean isScore = false;
				if(request.containsKeyInRequest("score"))
				{
				 score = Integer.parseInt(request.getParameter("score"));
				 isScore = true;
				}
				else {
					strategyValue = request.getParameter("strategyName");
				}
				String[] idVal = new String[json.length()];
				int j = 0;
				if(isScore) {
				for (int i = 0; i < json.length(); i++) {
					String name = json.names().get(i).toString();
					int lowLimit = Integer.parseInt(json.getString(name).split("~")[0]);
					int highLimit = Integer.parseInt(json.getString(name).split("~")[1]);
					
					if (score >= lowLimit && score <= highLimit) {
						strategyName = name.toUpperCase();
						name = "";
					} else {
						name = name.toUpperCase();
					}
					if (name != "") {
						idVal[++j] = name;
					}
				}
				idVal[0] = strategyName;
				}
				else {
					idVal[0] = strategyValue.toUpperCase();
					for(int i=0;i<json.length();i++) {
						String name = json.names().get(i).toString();
						if(name.equalsIgnoreCase(strategyValue)) {
						}
						else {
							idVal[++j]=name.toUpperCase();
						}
					}
				}
				inputMap.put("idVal", idVal);
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("Error in GetStrategyListPreProcessor-TAP" + e).log();
			e.getMessage();
		}
		return false;

	}
}
