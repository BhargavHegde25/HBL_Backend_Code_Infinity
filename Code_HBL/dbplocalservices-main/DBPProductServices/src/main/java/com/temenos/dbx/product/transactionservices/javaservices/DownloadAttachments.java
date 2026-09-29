package com.temenos.dbx.product.transactionservices.javaservices;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DownloadAttachments implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			String fileName = null;
			String fileID = null;
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			fileName = inputParams.get("fileName");
			fileID = inputParams.get("fileID");
			HashMap<String, String> dataMapFilter = new HashMap<>();
			String fileType = null;
			String fileContents = null;
			if (fileName != null && fileID != null) {

				if (CommonUtils.isDMSIntegrationEnabled()) {
					dataMapFilter.put("documentId", fileID);
					result = HelperMethods.callApi(request, dataMapFilter, HelperMethods.getHeaders(request),
							URLConstants.DOCUMENT_STORAGE_DOWNLOAD);
					fileType = fileName.split("[.]")[1];
					fileContents = new String(result.getParamValueByName("documentContent").toString());

				} else {
					dataMapFilter.put(DBPUtilitiesConstants.FILTER,  "paymentFileName" + DBPUtilitiesConstants.EQUAL + fileName
							+ DBPUtilitiesConstants.AND + "paymentFileID" + DBPUtilitiesConstants.EQUAL + fileID);
					result = HelperMethods.callApi(request, dataMapFilter, HelperMethods.getHeaders(request),
							URLConstants.PAYMENT_FILES_GET);
					if (!HelperMethods.hasRecords(result)) {
						return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
					}
					if (result.getAllDatasets().size() > 0) {
						if(result.getAllDatasets().get(0).getRecord(0) != null) {
							Record records = result.getAllDatasets().get(0).getRecord(0);
							fileType = records.getParamValueByName("paymentFileType");
							fileContents = new String(records.getParamValueByName("paymentFileContents"));
						}
					}
				}
				if (fileType.equals("pdf") || fileType.equals("application/pdf"))
					dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/pdf"));
				if (fileType.equals("jpeg") || fileType.equals("image/jpeg"))
					dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "image/jpeg"));
				byte[] bytes = Base64.getMimeDecoder().decode(fileContents);
				dcResponse.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
						new BufferedHttpEntity(new ByteArrayEntity(bytes)));
				dcResponse.setStatusCode(HttpStatus.SC_OK);
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return result;
	}

	private Map<String, String> getCustomHeaders(String filename, String contentType) {
		Map<String, String> customHeaders = new HashMap<>();
		customHeaders.put(HttpHeaders.CONTENT_TYPE, contentType);
		customHeaders.put("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		return customHeaders;
	}
}
