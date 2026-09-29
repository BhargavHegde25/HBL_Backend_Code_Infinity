package com.kony.adminconsole.reports.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.reports.businessdelegate.api.ManageReportsBusinessDelegate;
import com.kony.adminconsole.reports.exception.ApplicationException;
import com.kony.adminconsole.reports.resource.api.ReportsManagementResource;
import com.kony.adminconsole.reports.utilities.ErrorCodeEnum;
import com.kony.adminconsole.reports.utilities.ReportsConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ReportsManagementResourceImpl implements ReportsManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final String CREATE_REPORT_OPERATION_NAME = "createReport";
	private static final String SHARE_REPORT_OPERATION_NAME = "shareReport";
	private static final String DELETE_REPORT_OPERATION_NAME = "deleteReport";

	@Override
	public Result manageReports(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		try {
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);

			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			String userID = userDetailsBeanInstance.getId();
			if (methodID.equalsIgnoreCase(CREATE_REPORT_OPERATION_NAME)) {
				return createReport(userID, requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(SHARE_REPORT_OPERATION_NAME)) {
				return shareReport(userID, requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(DELETE_REPORT_OPERATION_NAME)) {
				return deleteReport(requestInstance, userID, authToken);
			}
			return new Result();
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_29005.setErrorCode(errorResult);
			return errorResult;
		}
	}

	private Result shareReport(String userID, DataControllerRequest requestInstance, String authToken)
			throws Exception {
		diagnostic.prepareDebug("shareReport").log();
		Result processedResult = new Result();
		try {
			String reportId = requestInstance.getParameter("reportId");
			StringBuffer errorMessageBuffer = new StringBuffer();
			errorMessageBuffer.append("ERROR:\n");
			if (StringUtils.isBlank(reportId)) {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.UPDATE,
						ActivityStatusEnum.SUCCESSFUL, "Report share failed");
				String report_message = "Report ID is a mandatory input for an Update Request.It cannot be a NULL/Empty input.";
				errorMessageBuffer.append(report_message);
				processedResult
						.addParam(new Param(ReportsConstants.VALIDATION_ERROR, report_message, FabricConstants.STRING));
			} else {
				JSONArray removedUsers = new JSONArray(requestInstance.getParameter("removedUsers"));
				JSONArray addedUsers = new JSONArray(requestInstance.getParameter("addedUsers"));
				JSONArray removedRoles = new JSONArray(requestInstance.getParameter("removedRoles"));
				JSONArray addedRoles = new JSONArray(requestInstance.getParameter("addedRoles"));
				Map<String, Object> postParametersMap = new HashMap<String, Object>();
				postParametersMap.put("reportId", reportId);
				postParametersMap.put("removedUsers", removedUsers);
				postParametersMap.put("addedUsers", addedUsers);
				postParametersMap.put("removedRoles", removedRoles);
				postParametersMap.put("addedRoles", addedRoles);
				postParametersMap.put("userID", userID);
				ManageReportsBusinessDelegate reportsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ManageReportsBusinessDelegate.class);
				String response = reportsBusinessDelegate.shareReport(postParametersMap, authToken);
				if (response.equals(ReportsConstants.SUCCESS)) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.UPDATE,
							ActivityStatusEnum.SUCCESSFUL, "Report share successful");
					processedResult.addParam(
							new Param(ReportsConstants.STATUS, ReportsConstants.SUCCESS, FabricConstants.STRING));
				}
			}
			return processedResult;
		} catch (ApplicationException e) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Report share unsuccessful");
			processedResult = new Result();
			alert.prepareError("ApplicationException found:" + e).log();
			e.getErrorCodeEnum().setErrorCode(processedResult);
			return processedResult;
		} catch (Exception e) {
			processedResult = new Result();
			alert.prepareError("Exception:" + e).log();
			ErrorCodeEnum.ERR_29005.setErrorCode(processedResult);
			return processedResult;
		}
	}

	private Result createReport(String userID, DataControllerRequest requestInstance, String authToken) throws Exception{
		diagnostic.prepareDebug("createReport").log();
		Map<String, String> postParametersMap = new HashMap<String, String>();

		String reportID = CommonUtilities.getNewId().toString();
		Result processedResult = new Result();
		String reportName = requestInstance.getParameter("reportName");
		String reportDescription = requestInstance.getParameter("reportDescription");
		String reportDataSourceId = requestInstance.getParameter("reportDataSourceId"); // "FABRIC"
		String externalId = requestInstance.getParameter("externalId");

		if (StringUtils.isBlank(reportDataSourceId) || reportDataSourceId == null) {
			reportDataSourceId = "FABRIC";
		}
		postParametersMap.put("id", reportID);
		postParametersMap.put("reportDataSourceId", reportDataSourceId);
		postParametersMap.put("description", reportDescription);
		postParametersMap.put("createdby", userID);
		postParametersMap.put("modifiedby", userID);
		postParametersMap.put("externalId", externalId);
		if (StringUtils.isBlank(reportName)) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "Report create failed");
			diagnostic.prepareDebug("\nReport Name cannot be empty").log();
			ErrorCodeEnum.ERR_29004.setErrorCode(processedResult);
			return processedResult;
		}
		if (StringUtils.isBlank(externalId)) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "Report create failed");
			diagnostic.prepareDebug("\nExternal Id cannot be empty").log();
			ErrorCodeEnum.ERR_29019.setErrorCode(processedResult);
			return processedResult;
		}
		postParametersMap.put("name", reportName);
		ManageReportsBusinessDelegate reportsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ManageReportsBusinessDelegate.class);
		String createReportResponse = reportsBusinessDelegate.createReport(postParametersMap, authToken);
		JSONObject createReportResponseJSON = CommonUtilities.getStringAsJSONObject(createReportResponse);
		if (createReportResponseJSON != null && createReportResponseJSON.has(FabricConstants.OPSTATUS)
				&& createReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			diagnostic.prepareDebug("Create Report Call Status:Successful. Report Id:" + reportID).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.CREATE,
					ActivityStatusEnum.SUCCESSFUL, "Report create successful");
			processedResult
			.addParam(new Param(ReportsConstants.STATUS, ReportsConstants.SUCCESS, FabricConstants.STRING));
		} else {
			diagnostic.prepareDebug("Create Report Call Status:Failed. Report Id:" + reportID).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "Report create failed");
			ErrorCodeEnum.ERR_29003.setErrorCode(processedResult);
			processedResult
					.addParam(new Param("createReportResponse", createReportResponse, FabricConstants.STRING));
		}
		return processedResult;
	}

	private Result deleteReport(DataControllerRequest requestInstance, String userId, String authToken)
			throws Exception {
		Result result = new Result();
		try {
			diagnostic.prepareDebug("In deleteReport").log();
			String reportId = requestInstance.getParameter("reportId");
			if (StringUtils.isBlank(reportId)) {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "Report delete failed");
				String errorMessage = "Report ID is a mandatory input for a Delete Request.It cannot be a NULL/Empty input.";
				ErrorCodeEnum.ERR_29000.setErrorCode(result);
				result.addParam(new Param(ReportsConstants.VALIDATION_ERROR, errorMessage, FabricConstants.STRING));
			} else {
				ManageReportsBusinessDelegate reportsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ManageReportsBusinessDelegate.class);
				String responseString = reportsBusinessDelegate.deleteReport(reportId, userId, authToken);
				JSONObject response = CommonUtilities.getStringAsJSONObject(responseString);
				if (response != null && response.has(FabricConstants.OPSTATUS)
						&& response.getInt(FabricConstants.OPSTATUS) == 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
							ActivityStatusEnum.SUCCESSFUL, "Report delete successful");
					diagnostic.prepareDebug("Report Delete Status:Successful. Report Id:" + reportId).log();
					result.addParam(
							new Param(ReportsConstants.STATUS, ReportsConstants.SUCCESS, FabricConstants.STRING));
				} else {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.DELETE,
							ActivityStatusEnum.SUCCESSFUL, "Report delete failed");
					diagnostic.prepareDebug("Report Delete Status:Failed. Report Id:" + reportId).log();
					throw new ApplicationException(ErrorCodeEnum.ERR_29000);
				}
			}
			return result;
		} catch (ApplicationException e) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.REPORTS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Report share unsuccessful");
			result = new Result();
			alert.prepareError("ApplicationException found:" + e).log();
			e.getErrorCodeEnum().setErrorCode(result);
			return result;
		} catch (Exception e) {
			result = new Result();
			alert.prepareError("Exception:" + e).log();
			ErrorCodeEnum.ERR_29005.setErrorCode(result);
			return result;
		}
	}

	@Override
	public Result getUserReports(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String authToken = request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);

			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
			String userID = userDetailsBeanInstance.getId();
			String roleID = userDetailsBeanInstance.getRoleId();
			ManageReportsBusinessDelegate reportsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ManageReportsBusinessDelegate.class);
			JSONObject userReportsResponse = reportsBusinessDelegate.getUserReports(userID, roleID, authToken);

			if (userReportsResponse != null && userReportsResponse.has(FabricConstants.OPSTATUS)
					&& userReportsResponse.getInt(FabricConstants.OPSTATUS) == 0) {
				diagnostic.prepareDebug("User Reports Call Status:Successful. userID:" + userID).log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL, "User Reports Call Status:Successfull");
				JSONArray userReportsResponseArr = userReportsResponse.getJSONArray("records");
				JSONObject currJSONObject;
				Dataset dataSet = new Dataset();
				dataSet.setId("userReports");
				for (int indexVar = 0; indexVar < userReportsResponseArr.length(); indexVar++) {
					currJSONObject = userReportsResponseArr.getJSONObject(indexVar);
					Record currRecord = new Record();
					for (String currKey : currJSONObject.keySet()) {
						currRecord.addParam(
								new Param(currKey, currJSONObject.optString(currKey), FabricConstants.STRING));
					}

					Result usersRoles = reportsBusinessDelegate.getReportUsersAndRoles(currJSONObject.optString("id"),
							authToken);
					if (usersRoles.getDatasetById("roles") != null)
						currRecord.addDataset(usersRoles.getDatasetById("roles"));
					if (usersRoles.getDatasetById("users") != null)
						currRecord.addDataset(usersRoles.getDatasetById("users"));
					dataSet.addRecord(currRecord);
				}
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL, "Get Report Call Status:Successfull");
				result.addDataset(dataSet);
			} else {
				diagnostic.prepareDebug("User Reports Call Status:Failed. userID:" + userID).log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.CREATE,
						ActivityStatusEnum.FAILED, "Failed to get user reports");
				ErrorCodeEnum.ERR_29012.setErrorCode(result);
				result.addParam(new Param(ReportsConstants.STATUS, ReportsConstants.FAILURE, FabricConstants.STRING));
			}
			return result;
		} catch (ApplicationException e) {
			result = new Result();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "Failed to get report");
			alert.prepareError("Application Exception.Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(result);
			return result;
		} catch (Exception e) {
			result = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_29005.setErrorCode(result);
			return result;
		}
	}


	

}
