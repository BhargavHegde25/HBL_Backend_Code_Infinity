package com.kony.adminconsole.reports.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.reports.businessdelegate.api.ManageReportsBusinessDelegate;
import com.kony.adminconsole.reports.exception.ApplicationException;
import com.kony.adminconsole.reports.utilities.ErrorCodeEnum;
import com.kony.adminconsole.reports.utilities.ReportsConstants;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ManageReportsBusinessDelegateImpl implements ManageReportsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public String createReport(Map<String, String> map,String authToken){
		try {
			String createReportResponse = Executor.invokeService(ServiceURLEnum.REPORT_CREATE, map, null,
					authToken);
			return createReportResponse;
		} catch (Exception e) {
			alert.prepareError("Runtime Exception.Exception Trace:", e).log();
			return ErrorCodeEnum.ERR_29003.getMessage();
		}
	}

	@Override
	public String shareReport(Map<String, Object> map, String authToken) throws ApplicationException, JSONException {

		String reportId = (String) map.get("reportId");
		String userId = (String) map.get("userID");
		// Validate if current user has create the shared report
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put(ODataQueryConstants.FILTER, "id eq '" + reportId + "'");
		String serviceResponse = Executor.invokeService(ServiceURLEnum.REPORT_READ, queryMap, null, authToken);
		JSONObject reportReadResponse = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if (reportReadResponse == null || !reportReadResponse.has(FabricConstants.OPSTATUS)
				|| reportReadResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| reportReadResponse.optJSONArray("report") == null) {
			alert.prepareError("Failed to read reports").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29016);
		}
		JSONArray reportArray = reportReadResponse.optJSONArray("report");
		JSONObject currJson = reportArray.optJSONObject(0);
		String createdBy = currJson.optString("createdby");
		if (!createdBy.equals(userId)) {
			alert.prepareError("User cannot share report").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29015);
		}
		JSONArray removedUsers = (JSONArray) map.get("removedUsers");
		JSONArray addedUsers = (JSONArray) map.get("addedUsers");
		JSONArray removedRoles = (JSONArray) map.get("removedRoles");
		JSONArray addedRoles = (JSONArray) map.get("addedRoles");
		
		
		if(addedUsers.isEmpty() && addedRoles.isEmpty() && removedRoles.isEmpty() && addedRoles.isEmpty() ) {
			alert.prepareError("User cannot share report").log();	
			throw new ApplicationException(ErrorCodeEnum.ERR_29020);
			
		}

		if (addedRoles.length() > 0)
			manageSharedRoles(addedRoles, true, reportId, authToken);
		if (removedRoles.length() > 0)
			manageSharedRoles(removedRoles, false, reportId, authToken);
		if (addedUsers.length() > 0)
			manageSharedUsers(addedUsers, true, reportId, authToken);
		if (removedUsers.length() > 0)
			manageSharedUsers(removedUsers, false, reportId, authToken);
		return ReportsConstants.SUCCESS;
	}

	public void manageSharedUsers(JSONArray customers, boolean isAdded, String reportId,
			String authToken)
			throws JSONException, ApplicationException {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		for (int i = 0; i < customers.length(); i++) {
            
			String userId = ((JSONObject) customers.get(i)).getString("id");
			postParametersMap.put("reportId", reportId);
			postParametersMap.put("userId", userId);
			postParametersMap.put("roleId", "*");
			String manageUsersResponse = Executor.invokeService(isAdded==true?ServiceURLEnum.SHAREDREPORT_CREATE:ServiceURLEnum.SHAREDREPORT_DELETE,
					postParametersMap,null,authToken);
			JSONObject manageUsersResponseJSON = CommonUtilities
					.getStringAsJSONObject(manageUsersResponse);
			if (manageUsersResponseJSON != null
					&& manageUsersResponseJSON.has(FabricConstants.OPSTATUS)
					&& manageUsersResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				alert.prepareError("Share report to user successful").log();
			}
			else
				throw new ApplicationException(ErrorCodeEnum.ERR_29001);
		}

	}

	public void manageSharedRoles(JSONArray roles, boolean isAdded, String reportId,
			String authToken)
			throws JSONException, ApplicationException {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		for (int i = 0; i < roles.length(); i++) {
			String roleId = ((JSONObject) roles.get(i)).getString("id");
			// Validate Role Id
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put(ODataQueryConstants.FILTER, "role_id eq '" + roleId + "'");
			String serviceResponse = Executor.invokeService(ServiceURLEnum.ROLES_VIEW_READ, queryMap, null, authToken);
			JSONObject roleReadResponse = CommonUtilities.getStringAsJSONObject(serviceResponse);
			if (roleReadResponse == null || !roleReadResponse.has(FabricConstants.OPSTATUS)
					|| roleReadResponse.getInt(FabricConstants.OPSTATUS) != 0
					|| roleReadResponse.optJSONArray("roles_view") == null) {
				alert.prepareError("Failed to read roles_view").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_29012);
			}
			if (roleReadResponse.optJSONArray("roles_view").length() == 0) {
				alert.prepareError("Invalid Role Id").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_29011);
			}
			postParametersMap.clear();
			postParametersMap.put("userId", "*");
			postParametersMap.put("reportId", reportId);
			postParametersMap.put("roleId", roleId);
			String manageRolesResponse = Executor.invokeService(isAdded==true?ServiceURLEnum.SHAREDREPORT_CREATE:ServiceURLEnum.SHAREDREPORT_DELETE,
					postParametersMap, null, authToken);
			JSONObject manageRolesResponseJSON = CommonUtilities
					.getStringAsJSONObject(manageRolesResponse);
			if (manageRolesResponseJSON != null && manageRolesResponseJSON.has(FabricConstants.OPSTATUS)
					&& manageRolesResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				alert.prepareError("Share report to role successful").log();
			}
			else
				throw new ApplicationException(ErrorCodeEnum.ERR_29002);
		}

	}
	
	@ Override
	public String deleteReport(String reportId, String userID, String authToken) throws ApplicationException {
		// Delete shared user/role mappings\

		Map<String, String> postParametersMap = new HashMap<String, String>();
		// Validate if current user has create the report to be deleted
		postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + reportId + "'");
		String serviceResponse = Executor.invokeService(ServiceURLEnum.REPORT_READ, postParametersMap, null, authToken);
		JSONObject reportReadResponse = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if (reportReadResponse == null || !reportReadResponse.has(FabricConstants.OPSTATUS)
				|| reportReadResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| reportReadResponse.optJSONArray("report") == null) {
			alert.prepareError("Failed to read reports").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29016);
		}
		JSONArray reportArray = reportReadResponse.optJSONArray("report");
		JSONObject currJson = reportArray.optJSONObject(0);
		String createdBy = currJson.optString("createdby");
		if (!createdBy.equals(userID)) {
			alert.prepareError("User cannot delete report").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29018);
		}
		postParametersMap.clear();
		postParametersMap.put(ODataQueryConstants.FILTER, "reportId eq '" + reportId + "'");
		String readSharedReportResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_READ, postParametersMap,
				null, authToken);
		JSONObject readSharedReportResponseJSON = CommonUtilities.getStringAsJSONObject(readSharedReportResponse);
		if (readSharedReportResponseJSON != null && readSharedReportResponseJSON.has(FabricConstants.OPSTATUS)
				&& readSharedReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readSharedReportResponseJSON.has("sharedreport")) {
			Record deleteSharedReportsRecord = new Record();
			deleteSharedReportsRecord.setId("deleteSharedReports");
			Record deleteReportsRecord = new Record();
			deleteReportsRecord.setId("deleteReports");
			JSONArray sharedReportRecordsJSONArray = readSharedReportResponseJSON.getJSONArray("sharedreport");
			for (int indexVar = 0; indexVar < sharedReportRecordsJSONArray.length(); indexVar++) {
				JSONObject currReportJSONObject = sharedReportRecordsJSONArray.getJSONObject(indexVar);
				String userId = null, roleId = null, currReportId;
				postParametersMap.clear();
				if (currReportJSONObject.has("reportId")) {
					currReportId = currReportJSONObject.optString("reportId");
					postParametersMap.put("reportId", currReportId);
				}
				if (currReportJSONObject.has("userId")) {
					userId = currReportJSONObject.optString("userId");
					postParametersMap.put("userId", userId);
				}
				if (currReportJSONObject.has("roleId")) {
					roleId = currReportJSONObject.optString("roleId");
					postParametersMap.put("roleId", roleId);
				}
				Executor.invokeService(ServiceURLEnum.SHAREDREPORT_DELETE, postParametersMap, null, authToken);
			}
		}
		postParametersMap.clear();
		postParametersMap.put("id", reportId);
		String deleteReportResponse = Executor.invokeService(ServiceURLEnum.REPORT_DELETE, postParametersMap, null,
				authToken);
		return deleteReportResponse;
	}

	public JSONObject getUserReports(String userId, String roleId, String authToken) {

		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("p_userId", userId);
		inputMap.put("p_roleId", roleId);
		JSONObject getReportsResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.GET_REPORTS_PROC, inputMap, null, authToken));

		return getReportsResponse;
	}

	@Override
	public Result getReportUsersAndRoles(String reportId,
			String authToken)
			throws ApplicationException {
		Result result = new Result();
		Dataset usersDataset = new Dataset();
		Dataset rolesDataset = new Dataset();
		rolesDataset.setId("roles");
		usersDataset.setId("users");
		diagnostic.prepareDebug("Fetching Shared Report users. Report Id:" + reportId).log();
		// Fetch Reports - Users and roles association
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, "reportId eq '" + reportId + "'");
		String readSharedReportResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_READ, inputMap, null,
				authToken);
		JSONObject readSharedReportResponseJSON = CommonUtilities.getStringAsJSONObject(readSharedReportResponse);
		if (readSharedReportResponseJSON == null || !readSharedReportResponseJSON.has(FabricConstants.OPSTATUS)
				|| readSharedReportResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readSharedReportResponseJSON.has("sharedreport")) {
			alert.prepareError("Failed to Read sharedreport").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29017);
		}
		JSONArray usersAndRolesArray = readSharedReportResponseJSON.optJSONArray("sharedreport");
		// Fetch the Users the Report is shared with
		diagnostic.prepareDebug("Fetched Shared Report users. Report Id:" + reportId).log();
		ArrayList<String> userList = new ArrayList<String>();
		ArrayList<String> roleList = new ArrayList<String>();
		if (usersAndRolesArray != null && usersAndRolesArray.length() > 0) {
			JSONObject currJSON;
			for (Object currObject : usersAndRolesArray) {
				if (currObject instanceof JSONObject) {
					Record currUserOrRoleRecord = new Record();
					currJSON = (JSONObject) currObject;
					if (!currJSON.optString("userId").isEmpty() && !currJSON.optString("userId").equals("*")) {
						currUserOrRoleRecord
								.addParam(new Param("id", currJSON.optString("userId"), FabricConstants.STRING));
						userList.add(currJSON.optString("userId"));
						usersDataset.addRecord(currUserOrRoleRecord);
					} else {
						currUserOrRoleRecord
								.addParam(new Param("id", currJSON.optString("roleId"), FabricConstants.STRING));
						roleList.add(currJSON.optString("roleId"));
						rolesDataset.addRecord(currUserOrRoleRecord);
					}
				}
			}
			result.addDataset(usersDataset);
			result.addDataset(rolesDataset);
		}
		return result;
	}

	@Override
	public Boolean isExternalReportAlreadySaved(String reportId, String dataSourceId, String createdBy, String authToken) throws ApplicationException{
		Map<String, String> postParametersMap = new HashMap<String, String>();
		// Validate if given report is already exists
		StringBuffer filterQueryBuffer = new StringBuffer();
		filterQueryBuffer.append("externalId eq '" + reportId + "' and reportDataSourceId eq '"+dataSourceId+"' and createdby eq '"+createdBy+"'");
		filterQueryBuffer.trimToSize();
		String filterQuery = filterQueryBuffer.toString();
		filterQuery = filterQuery.trim();
		postParametersMap.put(ODataQueryConstants.FILTER, filterQuery);
		String serviceResponse = Executor.invokeService(ServiceURLEnum.REPORT_READ, postParametersMap, null, authToken);
		JSONObject reportReadResponse = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if (reportReadResponse == null || !reportReadResponse.has(FabricConstants.OPSTATUS)
				|| reportReadResponse.getInt(FabricConstants.OPSTATUS) != 0
				|| reportReadResponse.optJSONArray("report") == null) {
			alert.prepareError("Failed to read reports").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29016);
		}
		else {
			JSONArray reportJSONArray = reportReadResponse.getJSONArray("report");
			if(reportJSONArray.length()>0) {
				return true;
			}
		}
		return false;
	}
}
