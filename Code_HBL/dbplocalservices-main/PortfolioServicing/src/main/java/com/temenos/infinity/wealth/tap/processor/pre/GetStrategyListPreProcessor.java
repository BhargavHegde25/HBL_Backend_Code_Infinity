/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * @author himaja.sridhar
 *
 */
public class GetStrategyListPreProcessor implements DataPreProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
				diagnostic.prepareDebug("==========> GetStrategyListPreProcessor TAP - Entered ").log();
				String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_STRATEGIES,
						request);
				JSONObject json = new JSONObject(INF_WLTH_STRATEGIES);
				diagnostic.prepareDebug("==========>  GetStrategyListPreProcessor TAP - Environment variables fetched and set").log();
				String strategyName = "";
				String name = request.getParameter("idVal");
				for(int i=0;i<json.length();i++) {
					String nameVal = json.names().get(i).toString().toUpperCase();
					if(name.equals(nameVal)) {
						strategyName = json.names().get(i).toString();
					}	
					}
				inputMap.put("id", name);
				request.setAttribute("name", strategyName);
				inputMap.put("completeOnly", "true");
				diagnostic.prepareDebug("==========>  GetStrategyListPreProcessor TAP - End:Manipulation of input parameters").log();
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  GetStrategyListPreProcessor TAP - Token Generation Succeeded").log();
				return true;
				
			} 
		
	}


