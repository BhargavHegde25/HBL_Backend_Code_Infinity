package com.kony.adminconsole.service.locationsandlocationservices;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LocationsCSVDownloadService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		try {
			Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
			String fileId = queryParamsMap.get("fileId");
			String locationfileclob = (String) MemoryManager.getFromCache(fileId);
			String locationfileId = (String) MemoryManager.getFromCache("locationfileId");
			if (locationfileId != null && locationfileId.equals("locationTemplate")) {
				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"locationTemplate.csv\"");

				responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON,
						new BufferedHttpEntity(new InputStreamEntity(
								this.getClass().getClassLoader().getResourceAsStream("locationTemplate.csv"))));
				responseInstance.getHeaders().putAll(customHeaders);
			} else {
				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"branchAtmCSV.csv\"");

				responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON,
						new BufferedHttpEntity(new StringEntity(locationfileclob, StandardCharsets.UTF_8)));
				responseInstance.getHeaders().putAll(customHeaders);
				responseInstance.setStatusCode(HttpStatus.SC_OK);
			}
			MemoryManager.removeFromCache(fileId);
		}catch (Exception e) {
			alert.prepareError("Exception while downloading Locations CSV file", e).log();
			ErrorCodeEnum.ERR_20687.setErrorCode(result);

			String errorMessage = "Failed to download Locations CSV file. Please contact administrator.";
			CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
		}
		return result;
	}
}
