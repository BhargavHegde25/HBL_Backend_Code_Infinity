package com.kony.fabricreports;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.fabricreports.util.ReportsConstants;

public class CustomReports {
	private CustomReports() {

	}

	public static JSONArray getListOfReports() throws Exception {
		JSONArray listOfReports = FabricReportsManageService.processReportsAction(0,
				ReportsConstants.GET_LIST_OF_REPORTS, "");
		if (listOfReports != null && !listOfReports.isNull(0))
			return listOfReports;
		return null;
	}

	public static JSONArray getFiltersForReport(String reportId) throws Exception {
		if (reportId != null) {
			reportId = reportId.replace(" ", "_").replace("-", "_");
			JSONArray filters = FabricReportsManageService.processReportsAction(0,
					ReportsConstants.GET_FILTERS_FOR_REPORT, reportId);
			if (filters != null)
				return filters;
		}
		return null;
	}

	public static JSONArray viewReport(String reportId, String filters, int pageno) throws Exception {
		if (reportId != null) {
			reportId = reportId.replace(" ", "_").replace("-", "_");
			JSONArray htmlResponseArray = new JSONArray();
			if (pageno == 1) {
				return processViewReport(reportId, filters);
			}
			htmlResponseArray = FabricReportsManageService.processReportsAction(pageno, ReportsConstants.VIEW_REPORT,
					reportId, filters);
			if (htmlResponseArray != null)
				return htmlResponseArray;
		}
		return null;
	}

	private static JSONArray processViewReport(String reportId, String filters) throws DBPApplicationException {
		JsonArray noofpages = new JsonArray();
		JsonArray htmlResponseArray1 = new JsonArray();
		JSONArray htmlResponseArray = new JSONArray();
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put("loop_count", ReportsConstants.LOOPCOUNTVAL);
		inputmap.put("loop_seperator", ReportsConstants.LOOPSEPARATORVAL);
		inputmap.put("pageno", ReportsConstants.PAGENUMVAL);
		inputmap.put("methodId", ReportsConstants.METHODIDVAL);
		inputmap.put("reportId", reportId + ReportsConstants.LOOPSEPARATORVAL + reportId);
		inputmap.put("filters", filters + ReportsConstants.LOOPSEPARATORVAL + filters);
		String response = DBPServiceExecutorBuilder.builder().withOperationId(ReportsConstants.VIEWREPORTORCHOPERATION)
				.withRequestParameters(inputmap).withServiceId(ReportsConstants.ORCHSERVICE).build().getResponse();
		JsonArray arr = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("LoopDataset");
		for (JsonElement element : arr) {
			if (element.isJsonObject()) {
				JsonObject obj = element.getAsJsonObject();
				if (obj.has(ReportsConstants.EXECUTIONS)) {
					String executions = obj.get(ReportsConstants.EXECUTIONS).getAsString();
					noofpages = new JsonParser().parse(executions).getAsJsonArray();
				} else if (obj.has(ReportsConstants.VIEW_REPORT)) {
					String viewReport = obj.get(ReportsConstants.VIEW_REPORT).getAsString();
					htmlResponseArray1 = new JsonParser().parse(viewReport).getAsJsonArray();
				}
			}
		}
		if (noofpages.size() > 0)
			htmlResponseArray1.add(noofpages.get(0));
		htmlResponseArray = new JSONArray(htmlResponseArray1.toString());
		return htmlResponseArray;
	}

	public static JSONArray downloadReport(String reportId, String filters, String fileType) throws Exception {
		if (reportId != null) {
			reportId = reportId.replace(" ", "_").replace("-", "_");
			JSONArray downloadResponseArray = FabricReportsManageService.processReportsAction(0,
					ReportsConstants.DOWNLOAD_REPORT, reportId, filters, fileType);
			if (downloadResponseArray != null && !downloadResponseArray.isNull(0))
				return downloadResponseArray;
		}
		return null;
	}

}
