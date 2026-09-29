package com.kony.adminconsole.reports.businessdelegate.api;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.reports.exception.ApplicationException;
import com.konylabs.middleware.dataobject.Result;

public interface ManageReportsBusinessDelegate extends BusinessDelegate {
	String createReport(Map<String, String> map, String authToken);

	String shareReport(Map<String, Object> map, String authToken) throws ApplicationException, JSONException;

	String deleteReport(String reportId, String userId, String authToken) throws ApplicationException;

	JSONObject getUserReports(String userId, String roleId, String authToken);

	Result getReportUsersAndRoles(String reportId, String authToken)
			throws ApplicationException;
	Boolean isExternalReportAlreadySaved(String reportId, String dataSourceId, String createdBy, String authToken) throws ApplicationException;

}
