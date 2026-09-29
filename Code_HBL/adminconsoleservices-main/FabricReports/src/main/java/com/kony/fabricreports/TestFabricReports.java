package com.kony.fabricreports;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

public class TestFabricReports implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		if (methodID.equals("getFabricReportsList"))
			return JSONToResult.convert(ProcessFabricReports.getListOfReportsFromFabric().toString());
		if (methodID.equals("getFiltersForReport")) {
			Result result = new Result();
			String reportId = request.getParameter("reportId");
			if (reportId == null || StringUtils.isBlank(reportId)) {
				result.addParam(new Param("error", "invalid input", "String"));
				return result;
			}
			return JSONToResult.convert(ProcessFabricReports.getFabricReportFilters(reportId).toString());
		}
		if (methodID.equals("viewReport")) {
			Result result = new Result();
			String reportId = request.getParameter("reportId");
			int pagenum = 1;
			try {
				pagenum = Integer.parseInt(request.getParameter("page"));
			} catch (Exception e) {
				// Error in reading page
			}
			if (pagenum < 0)
				pagenum = 1;
			String filters = request.getParameter("filters");
			if (reportId == null || StringUtils.isBlank(reportId) || filters == null) {
				result.addParam(new Param("error", "invalid input", "String"));
				return result;
			}
			JSONArray resarray = ProcessFabricReports.viewFabricReport(reportId, filters, pagenum);
			boolean isfailed = true;
			diagnostic.prepareDebug("resarray" + resarray).log();
			try {
				for (int i = 0; i < resarray.length(); i++) {
					JSONObject res = resarray.getJSONObject(i);
					if (res.has("html")) {
						result.addParam(new Param("html", res.getString("html"), "String"));
						isfailed = false;
					}
					if (res.has("totalPages")) {
						result.addParam(new Param("totalPages", res.get("totalPages").toString(), "String"));
						isfailed = false;
					}

				}
			} catch (Exception e) {
				alert.prepareError("Error ocured", e).log();
			}
			if (isfailed)
				result.addParam(new Param("success", "false", "String"));

			return result;
		}
		if (methodID.equals("downloadReport")) {
			Result result = new Result();
			String reportId = request.getParameter("reportId");
			String filters = request.getParameter("filters");
			String fileType = request.getParameter("fileType");
			if (reportId == null || StringUtils.isBlank(reportId) || filters == null || fileType == null
					|| StringUtils.isBlank(fileType)) {
				result.addParam(new Param("error", "invalid input", "String"));
				return result;
			}
			JSONObject res = ProcessFabricReports.downloadFabricReport(reportId, filters, fileType);
			if (res.has(fileType)) {
				result.addParam(new Param(fileType, res.getString(fileType), "String"));
				return result;
			}
			return JSONToResult.convert(res.toString());
		}
		return new Result();
	}
}
