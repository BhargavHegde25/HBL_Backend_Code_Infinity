package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetRecommendedStrategyPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {

			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_STRATEGIES,
					request);

			if (portfolioId.equalsIgnoreCase("100777-4")) {

				JSONObject returnObj = new JSONObject();
				returnObj.put("recStrategyName", "Dynamic");
				JSONArray jsArray = new JSONArray(strategyList(INF_WLTH_STRATEGIES));
				returnObj.put("strategyList", sortingArray(jsArray,"minVal"));
				returnObj.put(TemenosConstants.PORTFOLIOID, request.getParameterValues(TemenosConstants.PORTFOLIOID));
				returnObj.put("strategyId", "632");
				returnObj.put("assets", assets(portfolioId));
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);

			} else if (portfolioId.equalsIgnoreCase("100777-5")) {

				JSONObject returnObj = new JSONObject();
				returnObj.put("recStrategyName", "Balanced");
				JSONArray jsArray = new JSONArray(strategyList(INF_WLTH_STRATEGIES));
				returnObj.put("strategyList", sortingArray(jsArray,"minVal"));
				returnObj.put(TemenosConstants.PORTFOLIOID, request.getParameterValues(TemenosConstants.PORTFOLIOID));
				returnObj.put("strategyId", "415");
				returnObj.put("assets", assets(portfolioId));
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);
			}

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking GetRecommendedStrategyPostProcessor MOCK - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return result;
	}

	private JSONArray assets(String portfolioId) {
		String assetName[] = new String[] { "Stocks", "Bonds", "Cash", "Funds" };
		String weight[] = new String[] { "25.00", "25.00", "25.00", "25.00" };
		if (portfolioId.equalsIgnoreCase("100777-4")) {
		assetName = new String[] { "Stocks", "Bonds", "Cash", "Funds" };
		weight = new String[] { "45.00", "35.00", "10.00", "10.00" };
		}
		else if (portfolioId.equalsIgnoreCase("100777-5")) {	
		}
		JSONArray al1 = new JSONArray();
		for (int i = 0; i < assetName.length; i++) {
			HashMap<String, String> assets_hm = new HashMap<String, String>();
			assets_hm.put("assetName", assetName[i]);
			assets_hm.put("weight", weight[i]);
			al1.put(assets_hm);
		}

		return al1;
	}

	private ArrayList<HashMap<String, String>> strategyList(String INF_WLTH_STRATEGIES) {
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		if (INF_WLTH_STRATEGIES != null && INF_WLTH_STRATEGIES.length() > 0) {
			JSONObject json = new JSONObject(INF_WLTH_STRATEGIES);
			Iterator<String> keys = json.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				String values = json.get(key).toString();
				String value[] = values.split("~");

				HashMap<String, String> strategyList_hm = new HashMap<String, String>();
				strategyList_hm.put("strategyName", key);
				strategyList_hm.put("minVal", value[0]);
				strategyList_hm.put("maxVal", value[1]);
				al.add(strategyList_hm);

			}
		}
		return al;

	}
	public JSONArray sortingArray(JSONArray jsonArr, String sortBy) {
	    JSONArray sortedJsonArray = new JSONArray();

	    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	    for (int i = 0; i < jsonArr.length(); i++) {
	        jsonValues.add(jsonArr.getJSONObject(i));
	    }
	    Collections.sort( jsonValues, new Comparator<JSONObject>() {
	        private final String KEY_NAME = sortBy;

	        @Override
	        public int compare(JSONObject a, JSONObject b) {
	            Integer valA = null;
	            Integer valB = null;

	            try {
	                valA = Integer.parseInt(a.get(KEY_NAME).toString());
	                valB = Integer.parseInt(b.get(KEY_NAME).toString());
	            } 
	            catch (JSONException e) {
	            	e.getMessage();
	            }

	            return valA.compareTo(valB);
	        }
	    });

	    for (int i = 0; i < jsonArr.length(); i++) {
	        sortedJsonArray.put(jsonValues.get(i));
	    }

		return sortedJsonArray;
		}

}
