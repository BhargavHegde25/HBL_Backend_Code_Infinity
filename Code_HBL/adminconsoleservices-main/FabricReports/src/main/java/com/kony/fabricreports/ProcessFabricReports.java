package com.kony.fabricreports;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProcessFabricReports {
	public static JSONObject getListOfReportsFromFabric() {
		JSONObject result = new JSONObject();
		try {
			JSONArray listOfReports = CustomReports.getListOfReports();
			if (listOfReports == null || listOfReports.isNull(0)) {
				result.put("error", "operation failed");
				return result;
			}
			result.put("reports", listOfReports);
		} catch (Exception e) {
			result.put("error", "operation failed");
			return result;
		}
		return result;
	}

	public static JSONObject getFabricReportFilters(String reportId) {
		JSONObject result = new JSONObject();
		try {
			JSONArray filters = CustomReports.getFiltersForReport(reportId);
			System.out.println("filters3 " + filters);
			if (filters == null) {
				result.put("error", "operation failed");
				return result;
			}
			result.put("filters", filters);
		} catch (Exception e) {
			result.put("error", "operation failed");
			return result;
		}
		return result;
	}

	public static JSONArray viewFabricReport(String reportId, String filters, int pageno) {
		JSONObject result = new JSONObject();
		JSONArray htmlResponseArray = new JSONArray();
		try {
			htmlResponseArray = CustomReports.viewReport(reportId, filters, pageno);
			if (htmlResponseArray == null || htmlResponseArray.isNull(0)) {
				result.put("error", "operation failed");
				return htmlResponseArray;
			}
			return htmlResponseArray;
		} catch (Exception e) {
			result.put("error", "operation failed");
			return htmlResponseArray;
		}

	}

	public static JSONObject downloadFabricReport(String reportId, String filters, String fileType) {
		JSONObject result = new JSONObject();
		try {
			JSONArray downloadResponseArray = CustomReports.downloadReport(reportId, filters, fileType);
			if (downloadResponseArray == null || downloadResponseArray.isNull(0)) {
				result.put("error", "operation failed");
				return result;
			}
			result = downloadResponseArray.getJSONObject(0);
		} catch (Exception e) {
			result.put("error", "operation failed");
			return result;
		}
		return result;
	}

}
