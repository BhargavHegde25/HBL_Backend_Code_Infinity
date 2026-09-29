package com.kony.adminconsole.service.reports;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.ReportManagementHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ReportsManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final int REPORT_NAME_MIN_CHARS = 5;
	private static final int REPORT_NAME_MAX_CHARS = 30;
	private static final int REPORT_DESC_MIN_CHARS = 5;
	private static final int REPORT_DESC_MAX_CHARS = 250;

	private static final String CREATE_REPORT_OPERATION_NAME = "createReport";
	private static final String SHARE_REPORT_OPERATION_NAME = "shareReport";
	private static final String DELETE_REPORT_OPERATION_NAME = "deleteReport";


	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);

			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			String userID = userDetailsBeanInstance.getId();
			if (methodID.equalsIgnoreCase(CREATE_REPORT_OPERATION_NAME)) {
				String reportName = requestInstance.getParameter("reportName");
				String reportDescription = requestInstance.getParameter("reportDescription");
				String isFilterAvailable = requestInstance.getParameter("isFilterAvailable");
				String reportDataSourceId = "FABRIC";
				return createReport(userID, reportName, reportDescription, isFilterAvailable, reportDataSourceId,
						requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(SHARE_REPORT_OPERATION_NAME)) {
				String reportId = requestInstance.getParameter("reportId");
				return shareReport(userID, reportId, requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(DELETE_REPORT_OPERATION_NAME)) {
				String reportId = requestInstance.getParameter("reportId");
				return deleteReport(userID, reportId, requestInstance, authToken);
			} 			
			return new Result();
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
	}

	private Result deleteReport(String userID, String reportId, DataControllerRequest requestInstance,
			String authToken) throws JSONException {
		diagnostic.prepareDebug("In deleteReport").log();
		Result processedResult = new Result();
		Map<String, String> postParametersMap = new HashMap<String, String>();

		if (StringUtils.isBlank(reportId)) {
			String errorMessage = "Report ID is a mandatory input for a Delete Request.It cannot be a NULL/Empty input.";
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
					ActivityStatusEnum.FAILED, "Report delete failed");
			ErrorCodeEnum.ERR_20905.setErrorCode(processedResult);
			processedResult.addParam(new Param("validationError", errorMessage, FabricConstants.STRING));
			return processedResult;
		} else {
			// Delete shared user/role mappings
			postParametersMap.clear();
			postParametersMap.put(ODataQueryConstants.FILTER, "reportId eq '" + reportId + "'");
			String readSharedReportResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_READ,
					postParametersMap, null, requestInstance);

			JSONObject readSharedReportResponseJSON = CommonUtilities.getStringAsJSONObject(readSharedReportResponse);
			if (readSharedReportResponseJSON != null && readSharedReportResponseJSON.has(FabricConstants.OPSTATUS)
					&& readSharedReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readSharedReportResponseJSON.has("sharedreport")) {
				Record deleteSharedReportsRecord = new Record();
				deleteSharedReportsRecord.setId("deleteSharedReports");
				Record deleteReportsRecord = new Record();
				deleteReportsRecord.setId("deleteReports");
				processedResult.addRecord(deleteReportsRecord);
				JSONArray sharedReportRecordsJSONArray = readSharedReportResponseJSON.getJSONArray("sharedreport");
				for (int indexVar = 0; indexVar < sharedReportRecordsJSONArray.length(); indexVar++) {
					JSONObject currReportJSONObject = sharedReportRecordsJSONArray.getJSONObject(indexVar);
					String userId = null, roleId = null, currReportId;
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
					String deleteSharedReportResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_DELETE,
							postParametersMap, null, requestInstance);
					JSONObject deleteSharedReportResponseJSON = CommonUtilities
							.getStringAsJSONObject(deleteSharedReportResponse);
					if (deleteSharedReportResponseJSON != null
							&& deleteSharedReportResponseJSON.has(FabricConstants.OPSTATUS)
							&& deleteSharedReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
								ActivityStatusEnum.SUCCESSFUL, "Shared Report delete successful");
					} else {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
								ActivityStatusEnum.FAILED, "Shared Report delete failed");
					}
					postParametersMap.remove("id");
					deleteReportsRecord.addParam(
							new Param("DeleteSharedReport_" + reportId + "_userId" + userId + "_roleId" + roleId,
									deleteSharedReportResponse, FabricConstants.STRING));
				}
			}
		}
		// Delete from reportdatasource
		postParametersMap.clear();
		postParametersMap.put(ODataQueryConstants.SELECT, "id");
		postParametersMap.put(ODataQueryConstants.FILTER, "reportId eq '" + reportId + "'");
		String readReportDataSourceResponse = Executor.invokeService(ServiceURLEnum.REPORTDATASOURCE_READ,
				postParametersMap, null, requestInstance);

		JSONObject readReportDataSourceResponseJSON = CommonUtilities
				.getStringAsJSONObject(readReportDataSourceResponse);
		if (readReportDataSourceResponseJSON != null && readReportDataSourceResponseJSON.has(FabricConstants.OPSTATUS)
				&& readReportDataSourceResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readReportDataSourceResponseJSON.has("reportdatasource")) {
			Record deleteReportDataSourceRecord = new Record();
			deleteReportDataSourceRecord.setId("deleteReportDataSource");

			processedResult.addRecord(deleteReportDataSourceRecord);
			JSONArray reportDataSourceRecordsJSONArray = readReportDataSourceResponseJSON
					.getJSONArray("reportdatasource");
			for (int indexVar = 0; indexVar < reportDataSourceRecordsJSONArray.length(); indexVar++) {
				JSONObject currReportDataSourceJSONObject = reportDataSourceRecordsJSONArray.getJSONObject(indexVar);
				if (currReportDataSourceJSONObject.has("id")) {
					String currReportDataSourceId = currReportDataSourceJSONObject.optString("id");
					postParametersMap.put("id", currReportDataSourceId);
					String deleteReportDataSourceResponse = Executor.invokeService(
							ServiceURLEnum.REPORTDATASOURCE_DELETE, postParametersMap, null, requestInstance);
					JSONObject deleteReportDataSourceResponseJSON = CommonUtilities
							.getStringAsJSONObject(deleteReportDataSourceResponse);
					if (deleteReportDataSourceResponseJSON != null
							&& deleteReportDataSourceResponseJSON.has(FabricConstants.OPSTATUS)
							&& deleteReportDataSourceResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
								ActivityStatusEnum.SUCCESSFUL, "Report Data source delete successful");
					} else {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.DELETE,
								ActivityStatusEnum.FAILED, "Report data source delete failed");
					}
					postParametersMap.remove("id");
					deleteReportDataSourceRecord.addParam(new Param("DeleteReportDataSource_" + currReportDataSourceId,
							deleteReportDataSourceResponse, FabricConstants.STRING));
				}
			}
		}

		postParametersMap.clear();
		postParametersMap.put("id", reportId);
		String deleteReportResponse = Executor.invokeService(ServiceURLEnum.REPORT_DELETE, postParametersMap, null,
				requestInstance);
		JSONObject deleteReportResponseJSON = CommonUtilities.getStringAsJSONObject(deleteReportResponse);
		if (deleteReportResponseJSON != null && deleteReportResponseJSON.has(FabricConstants.OPSTATUS)
				&& deleteReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			diagnostic.prepareDebug("Report Delete Status:Successful. Report Id:" + reportId).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
					ActivityStatusEnum.SUCCESSFUL, "Report delete successful");
			return processedResult;
		} else {
			diagnostic.prepareDebug("Report Delete Status:Failed. Report Id:" + reportId).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
					ActivityStatusEnum.FAILED, "Report delete failed");
			ErrorCodeEnum.ERR_20908.setErrorCode(processedResult);
			return processedResult;
		}

	}

	private Result shareReport(String userID, String reportId, DataControllerRequest requestInstance,
			String authToken) throws JSONException {
		Result processedResult = new Result();

		JSONArray removedUsers = new JSONArray(requestInstance.getParameter("removedUsers"));
		if (removedUsers.length() > 0) {
			ReportManagementHandler.removeUsers(removedUsers, reportId, authToken, requestInstance, processedResult);
		}
		JSONArray addedUsers = new JSONArray(requestInstance.getParameter("addedUsers"));
		if (addedUsers.length() > 0) {
			ReportManagementHandler.createUsers(addedUsers, reportId, authToken, requestInstance, processedResult);
		}
		JSONArray removedRoles = new JSONArray(requestInstance.getParameter("removedRoles"));
		if (removedRoles.length() > 0) {
			ReportManagementHandler.removeRoles(removedRoles, reportId, authToken, requestInstance, processedResult);
		}
		JSONArray addedRoles = new JSONArray(requestInstance.getParameter("addedRoles"));
		if (addedRoles.length() > 0) {
			ReportManagementHandler.addRoles(addedRoles, reportId, authToken, requestInstance, processedResult);
		}
		Map<String, String> postParametersMap = new HashMap<String, String>();
		StringBuffer errorMessageBuffer = new StringBuffer();
		errorMessageBuffer.append("ERROR:\n");

		if (StringUtils.isBlank(reportId)) {
			errorMessageBuffer
					.append("\nReport ID is a mandatory input for an Update Request.It cannot be a NULL/Empty input.");
		} else {
			postParametersMap.put("id", reportId);
		}

		return processedResult;

	}

	private Result createReport(String userID, String reportName, String reportDescription, String isFilterAvailable,
			String reportDataSourceId, DataControllerRequest requestInstance, String authToken) throws JSONException {
		diagnostic.prepareDebug("createReport").log();

		Result processedResult = new Result();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		boolean isValidData = true;
		StringBuffer errorMessageBuffer = new StringBuffer();
		errorMessageBuffer.append("ERROR:\n");

		String reportID = CommonUtilities.getNewId().toString();
		postParametersMap.put("id", reportID);

		if (reportName == null || reportName.length() < REPORT_NAME_MIN_CHARS
				|| reportName.length() > REPORT_NAME_MAX_CHARS) {
			isValidData = false;
			errorMessageBuffer.append("\nReport Name should have atleast " + REPORT_NAME_MIN_CHARS
					+ " characters and a maximum of " + REPORT_NAME_MAX_CHARS + " characters");
		} else {
			postParametersMap.put("name", reportName);
		}

//        if (reportDescription == null || reportDescription.length() < REPORT_DESC_MIN_CHARS
//                || reportDescription.length() > REPORT_DESC_MAX_CHARS) {
//            isValidData = false;
//            errorMessageBuffer.append("\nReport Description should have atleast " + REPORT_DESC_MAX_CHARS
//                    + " characters and a maximum of " + REPORT_DESC_MAX_CHARS + " characters");
//        } else {
//            postParametersMap.put("Description", reportDescription);
//        }

//        if (isFilterAvailable.equalsIgnoreCase("TRUE") || isFilterAvailable.equalsIgnoreCase("1")
//                || isFilterAvailable.equalsIgnoreCase("YES"))
//            postParametersMap.put("isFilterAvailable", "1");
//        else
//            postParametersMap.put("isFilterAvailable", "0");
		postParametersMap.put("reportDataSourceId", reportDataSourceId);
		if (isValidData) {
			String createReportResponse = Executor.invokeService(ServiceURLEnum.REPORT_CREATE, postParametersMap, null,
					requestInstance);
			JSONObject createReportResponseJSON = CommonUtilities.getStringAsJSONObject(createReportResponse);
			if (createReportResponseJSON != null && createReportResponseJSON.has(FabricConstants.OPSTATUS)
					&& createReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				diagnostic.prepareDebug("Create Report Call Status:Successful. Report Id:" + reportID).log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.CREATE,
						ActivityStatusEnum.SUCCESSFUL, "Report create successful");
				return processedResult;
			} else {
				diagnostic.prepareDebug("Create Report Call Status:Failed. Report Id:" + reportID).log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.CREATE,
						ActivityStatusEnum.FAILED, "Report create failed");
				ErrorCodeEnum.ERR_20906.setErrorCode(processedResult);
				processedResult
						.addParam(new Param("createReportResponse", createReportResponse, FabricConstants.STRING));
				return processedResult;
			}
		} else {
			diagnostic.prepareDebug("Report Delete Status:Failed. Report Id:" + reportID).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "Report create failed, invalid data");
			processedResult.addParam(new Param("createAlert", errorMessageBuffer.toString(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_20905.setErrorCode(processedResult);
			return processedResult;
		}
	}

}