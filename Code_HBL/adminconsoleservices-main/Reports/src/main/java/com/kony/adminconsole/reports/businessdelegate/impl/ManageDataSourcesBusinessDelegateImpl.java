package com.kony.adminconsole.reports.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.reports.businessdelegate.api.ManageDataSourcesBusinessDelegate;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;

public class ManageDataSourcesBusinessDelegateImpl implements ManageDataSourcesBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public JSONObject getDataSourcesList(String createdBy, String authToken) throws Exception {
		String getDataSourcesListResponse = Executor.invokeService(ServiceURLEnum.REPORTDATASOURCE_READ, null, null,
				authToken);
		JSONObject getDataSourcesListResponseJSON = CommonUtilities.getStringAsJSONObject(getDataSourcesListResponse);
		if (getDataSourcesListResponseJSON != null && getDataSourcesListResponseJSON.has(FabricConstants.OPSTATUS)
				&& getDataSourcesListResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& getDataSourcesListResponseJSON.has("reportdatasource")) {
			JSONArray getDataSourcesListResponseJSONArr = getDataSourcesListResponseJSON
					.getJSONArray("reportdatasource");
			JSONArray responseArr = new JSONArray();
			JSONObject responseObj = new JSONObject();
			Map<String, String> postParametersMap = new HashMap<String, String>();
			for (int indexVar = 0; indexVar < getDataSourcesListResponseJSONArr.length(); indexVar++) {
				JSONObject currJSONObject = getDataSourcesListResponseJSONArr.getJSONObject(indexVar);
				JSONObject dataObj = new JSONObject();
				dataObj.put("id", currJSONObject.optString("id"));
				dataObj.put("dataSourceId", currJSONObject.optString("dataSourceId"));
				dataObj.put("name", currJSONObject.optString("name"));
				dataObj.put("createdts", currJSONObject.optString("createdts"));
				// retreive reports count for a given datasource
				postParametersMap.clear();
				StringBuffer filterQueryBuffer = new StringBuffer();
				filterQueryBuffer.append("reportDataSourceId eq '" + currJSONObject.optString("id") + "' and createdby eq '"+createdBy+"'");
				filterQueryBuffer.trimToSize();
				String filterQuery = filterQueryBuffer.toString();
				filterQuery = filterQuery.trim();
				postParametersMap.put(ODataQueryConstants.FILTER, filterQuery);
				String readReportsResponse = Executor.invokeService(ServiceURLEnum.REPORT_READ, postParametersMap, null,
						authToken);
				JSONObject readReportsResponseJSON = CommonUtilities.getStringAsJSONObject(readReportsResponse);
				if (readReportsResponseJSON != null && readReportsResponseJSON.has(FabricConstants.OPSTATUS)
						&& readReportsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readReportsResponseJSON.has("report")) {
					JSONArray readReportsResponseJSONArr = readReportsResponseJSON.getJSONArray("report");
					dataObj.put("reportsCount", readReportsResponseJSONArr.length() + "");
					responseArr.put(dataObj);
				}
			}
			responseObj.put("reportdatasource", responseArr);
			return responseObj;
		}
		return getDataSourcesListResponseJSON;
	}

}