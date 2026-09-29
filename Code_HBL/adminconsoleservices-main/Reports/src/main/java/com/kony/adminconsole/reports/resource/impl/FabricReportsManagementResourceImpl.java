package com.kony.adminconsole.reports.resource.impl;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.reports.businessdelegate.api.FabricReportsManagementBusinessDelegate;
import com.kony.adminconsole.reports.businessdelegate.api.ManageReportsBusinessDelegate;
import com.kony.adminconsole.reports.exception.ApplicationException;
import com.kony.adminconsole.reports.resource.api.FabricReportsManagementResource;
import com.kony.adminconsole.reports.utilities.ErrorCodeEnum;
import com.kony.adminconsole.reports.utilities.ReportsConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class FabricReportsManagementResourceImpl implements FabricReportsManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Result manageFabricReports(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		FabricReportsManagementBusinessDelegate fabricReportsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(FabricReportsManagementBusinessDelegate.class);
		if (methodID.equals(ReportsConstants.GET_LIST_OF_FABRIC_REPORTS))
			return removeAlreadySavedFabricReports(request,fabricReportsBusinessDelegate.getListOfReportsFromFabric());
		if (methodID.equals(ReportsConstants.GET_LIST_OF_FILTERS)) {
			String reportId = request.getParameter(ReportsConstants.REPORTID);
			if (reportId == null || StringUtils.isBlank(reportId)) {
				return ErrorCodeEnum.ERR_29004.setErrorCode(result);
			}
			return JSONToResult.convert(fabricReportsBusinessDelegate.getFabricReportFilters(reportId).toString());
		}
		if (methodID.equals(ReportsConstants.VIEW_REPORT)) {
			String reportId = request.getParameter(ReportsConstants.REPORTID);
			int pagenum = 1;
			try {
				pagenum = Integer.parseInt(request.getParameter(ReportsConstants.PAGENO));
			} catch (Exception e) {
				// Error in reading page
			}
			if (pagenum < 0)
				pagenum = 1;
			String filters = request.getParameter(ReportsConstants.FILTERS);
			if (reportId == null || StringUtils.isBlank(reportId) || filters == null) {
				return ErrorCodeEnum.ERR_29007.setErrorCode(result);
			}
			JSONArray resarray = fabricReportsBusinessDelegate.viewFabricReport(reportId, filters, pagenum);

			boolean isfailed = true;
			try {
				for (int i = 0; i < resarray.length(); i++) {
					JSONObject res = resarray.getJSONObject(i);
					if (res.has(ReportsConstants.VIEW_TYPE)) {
						result.addParam(new Param(ReportsConstants.VIEW_TYPE, res.getString(ReportsConstants.VIEW_TYPE),
								ReportsConstants.STRING));
						isfailed = false;
					}
					if (res.has(ReportsConstants.TOTALPAGES)) {
						result.addParam(new Param(ReportsConstants.TOTALPAGES,
								res.get(ReportsConstants.TOTALPAGES).toString(), ReportsConstants.STRING));
						isfailed = false;
					}
				}
			} catch (Exception e) {
				alert.prepareError("Error Occured", e).log();
			}
			if (isfailed)
				return JSONToResult.convert(ErrorCodeEnum.ERR_29010.setErrorCode(new JSONObject()).toString());
			return result;
		}
		if (methodID.equals(ReportsConstants.DOWNLOAD_REPORT)) {
			String reportId = request.getParameter(ReportsConstants.REPORTID);
			String filters = request.getParameter(ReportsConstants.FILTERS);
			String fileType = request.getParameter(ReportsConstants.FILETYPE);
			if (reportId == null || StringUtils.isBlank(reportId) || filters == null || fileType == null
					|| StringUtils.isBlank(fileType)) {
				return ErrorCodeEnum.ERR_29007.setErrorCode(result);
			}
			JSONObject res = fabricReportsBusinessDelegate.downloadFabricReport(reportId, filters, fileType);
			if (res.has(fileType)) {
				result.addParam(new Param(fileType, res.getString(fileType), ReportsConstants.STRING));
				return result;
			}
			return JSONToResult.convert(res.toString());
		}
		return new Result();
	}
	public Result removeAlreadySavedFabricReports(DataControllerRequest request,JSONObject result) {
		Result processedResult=new Result();
		try {
			String authToken = request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			if(result.has("reports")) {
				org.json.JSONArray arr= result.getJSONArray("reports");
				JSONObject currJSONObject;
				Dataset dataSet = new Dataset();
				dataSet.setId("reports");
				for(int i=0;i<arr.length();i++) {
					ManageReportsBusinessDelegate reportsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
							.getFactoryInstance(BusinessDelegateFactory.class)
							.getBusinessDelegate(ManageReportsBusinessDelegate.class);
					currJSONObject = arr.getJSONObject(i);
					String reportId=currJSONObject.optString("id");
					String dataSourceId="FABRIC";
					UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
					String userID = userDetailsBeanInstance.getId();
					Boolean reportResponse = reportsBusinessDelegate.isExternalReportAlreadySaved(reportId, dataSourceId, userID, authToken);
					if(!reportResponse) {
						Record currRecord = new Record();
						for (String currKey : currJSONObject.keySet()) {
							currRecord.addParam(
									new Param(currKey, currJSONObject.optString(currKey), FabricConstants.STRING));
						}
						dataSet.addRecord(currRecord);
					}
					}
				processedResult.addDataset(dataSet);
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL, "Report get successful");
				}
			else return JSONToResult.convert(result.toString());
			return processedResult;	
		} catch (ApplicationException e) {
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Failed to get reports");
			processedResult = new Result();
			alert.prepareError("ApplicationException found:" + e).log();
			e.getErrorCodeEnum().setErrorCode(result);
			return processedResult;
		} catch (Exception e) {
			processedResult = new Result();
			alert.prepareError("Exception:" + e).log();
			ErrorCodeEnum.ERR_29005.setErrorCode(result);
			return processedResult;
		}
		
	}
}
