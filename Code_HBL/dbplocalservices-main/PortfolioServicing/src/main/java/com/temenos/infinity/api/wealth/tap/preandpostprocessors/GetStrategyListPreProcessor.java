/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.HashMap;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
	
				String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_STRATEGIES,
						request);
				JSONObject json = new JSONObject(INF_WLTH_STRATEGIES);
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
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				return true;
				
			} 
		
	}


