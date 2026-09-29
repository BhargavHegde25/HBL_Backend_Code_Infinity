package com.kony.adminconsole.reports.businessdelegate.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.reports.businessdelegate.api.FabricReportsManagementBusinessDelegate;
import com.kony.adminconsole.reports.utilities.ErrorCodeEnum;
import com.kony.fabricreports.*;

public class FabricReportsManagementBusinessDelegateImpl implements FabricReportsManagementBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public JSONObject getListOfReportsFromFabric() {
		JSONObject result = new JSONObject();
		try {
			JSONArray listOfReports = CustomReports.getListOfReports();
			if (listOfReports == null || listOfReports.isNull(0)) {
				return ErrorCodeEnum.ERR_29008.setErrorCode(result);
			}
			result.put("reports", listOfReports);
		} catch (Exception e) {
			alert.prepareError("Exception occurred while getting list of reports ", e).log();
			return ErrorCodeEnum.ERR_29008.setErrorCode(result);
		}
		return result;
	}

	@Override
	public JSONObject getFabricReportFilters(String reportId) {

		JSONObject result = new JSONObject();
		try {
			JSONArray filters = CustomReports.getFiltersForReport(reportId);
			if (filters == null) {
				return ErrorCodeEnum.ERR_29009.setErrorCode(result);
			}
			result.put("filters", filters);
		} catch (Exception e) {
			alert.prepareError("Exception occurred while fetching filters ", e).log();
			return ErrorCodeEnum.ERR_29009.setErrorCode(result);
		}
		return result;
	}

	@Override
	public JSONArray viewFabricReport(String reportId, String filters, int pageno) {
		JSONArray result = new JSONArray();
		try {
			JSONArray htmlResponseArray = CustomReports.viewReport(reportId, filters, pageno);
			if (htmlResponseArray == null || htmlResponseArray.isNull(0)) {
				return ErrorCodeEnum.ERR_29010.setErrorCode(result);
			}
			diagnostic.prepareDebug("htmlResponseArray" + htmlResponseArray).log();
			return htmlResponseArray;
		} catch (Exception e) {
			alert.prepareError("Exception occurred while viewing report ", e).log();
			return ErrorCodeEnum.ERR_29010.setErrorCode(result);
		}
	}

	@Override
	public JSONObject downloadFabricReport(String reportId, String filters, String fileType) {
		JSONObject result = new JSONObject();
		try {
			JSONArray downloadResponseArray = CustomReports.downloadReport(reportId, filters, fileType);
			if (downloadResponseArray == null || downloadResponseArray.isNull(0)) {
				return ErrorCodeEnum.ERR_29010.setErrorCode(result);
			}
			result = downloadResponseArray.getJSONObject(0);
		} catch (Exception e) {
			alert.prepareError("Exception occurred while downloading report ", e).log();
			return ErrorCodeEnum.ERR_29010.setErrorCode(result);
		}
		return result;
	}

}
