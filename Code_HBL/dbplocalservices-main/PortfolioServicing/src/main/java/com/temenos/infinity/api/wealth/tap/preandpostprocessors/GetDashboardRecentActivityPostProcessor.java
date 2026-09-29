package com.temenos.infinity.api.wealth.tap.preandpostprocessors;


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
import com.temenos.infinity.api.wealth.backenddelegate.impl.WealthDashboardBackendDelegateImpl;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetDashboardRecentActivityPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			WealthDashboardBackendDelegateImpl wealthDashboardBackendDelegateImpl =  new WealthDashboardBackendDelegateImpl();
			JSONObject recentActivity = new JSONObject();
			JSONArray recentActivityArray = new JSONArray();
			JSONArray sortedJSON = new JSONArray();
			JSONArray filteredRecentActivity = new JSONArray();
			Dataset bodyRec = result.getDatasetById("body");
			int pageSize = 4;
			if(bodyRec != null) {
			recentActivityArray = ResultToJSON.convertDataset(bodyRec);
			sortedJSON = wealthDashboardBackendDelegateImpl.getSortedJsonArray(recentActivityArray);
			filteredRecentActivity = wealthDashboardBackendDelegateImpl.returnSearch(sortedJSON);
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
			return resultObj;
			
		}
		 catch (Exception e) {
			 alert.prepareError("Error while invoking GetDashboardRecentActivityPostProcessor - "
						+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
			}
		return null;
	}

}
