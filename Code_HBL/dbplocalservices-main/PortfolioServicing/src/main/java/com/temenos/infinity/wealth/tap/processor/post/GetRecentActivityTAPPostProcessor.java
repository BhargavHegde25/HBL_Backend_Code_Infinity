package com.temenos.infinity.wealth.tap.processor.post;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetRecentActivityTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetRecentActivityTAPPostProcessor TAP - Entered ").log();
			JSONObject recentActivity = new JSONObject();
			JSONArray recentActivityArray = new JSONArray();
			JSONArray sortedJSON = new JSONArray();
			JSONArray filteredRecentActivity = new JSONArray();
			Dataset bodyRec = result.getDatasetById("body");
			int pageSize = 4;
			if(bodyRec != null) {
			recentActivityArray = ResultToJSON.convertDataset(bodyRec);
			sortedJSON = getSortedJsonArray(recentActivityArray);
			filteredRecentActivity = returnSearch(sortedJSON);
			int arrSize = filteredRecentActivity.length();	
			if (arrSize > pageSize) {
				for (int i = arrSize - 1; i >= pageSize; i-- ) {
					filteredRecentActivity.remove(i);
				}
			}
			}else {
			filteredRecentActivity = new JSONArray();
			}
			recentActivity.put("recentActivity", filteredRecentActivity);
			Result resultObj = Utilities.constructResultFromJSONObject(recentActivity);
			resultObj.addOpstatusParam("0");
			resultObj.addHttpStatusCodeParam("200");
			resultObj.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetRecentActivityTAPPostProcessor TAP - Exited ").log();
			return resultObj;
			
		}
		 catch (Exception e) {
			 alert.prepareError("==========> GetRecentActivityTAPPostProcessor TAP - Error: " + e.getMessage()).log();
			}
		return null;
	}

	public JSONArray getSortedJsonArray(JSONArray jsonArr) {

		JSONArray sortedJsonArr = new JSONArray();

		String sortValue = TemenosConstants.TRADEDATE;

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArr.length(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {

			private final String KEY_NAME = sortValue;

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String str1 = new String();
				String str2 = new String();
				str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
				str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
				return str1.compareToIgnoreCase(str2);
			}

		});

		for (int i = jsonArr.length() - 1; i >= 0; i--) {
			sortedJsonArr.put(jsonValues.get(i));
		}

		return sortedJsonArr;
	}
	public JSONArray returnSearch(JSONArray array) {
		JSONArray filtedArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String type = obj.getString("orderType");
				if (type.equalsIgnoreCase("Buy") || type.equalsIgnoreCase("Sell")) {
					filtedArray.put(obj);
				}
			} catch (Exception e) {

				alert.prepareError("Error while invoking TAP - "
						+ PortfolioWealthAPIServices.WEALTH_GETDASHBOARDRECENTACTIVITY.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filtedArray;
	}
}

