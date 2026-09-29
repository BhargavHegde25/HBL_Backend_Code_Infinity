package com.kony.adminconsole.reports.businessdelegate.api;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;

public interface FabricReportsManagementBusinessDelegate extends BusinessDelegate {
	JSONObject getListOfReportsFromFabric();
	JSONObject getFabricReportFilters(String reportId);
	JSONArray viewFabricReport(String reportId, String filters, int pagenum);
	JSONObject downloadFabricReport(String reportId, String filters, String fileType);
}
