package com.temenos.infinity.api.docmanagement.acctstatement.javaservices;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.constants.Constants;

public class DownloadAdhocStatementFile implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();

		HashMap<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		diagnostic.prepareDebug("************** DownloadAdhocStatementFile inputParams :"+inputParams).log();
		//String fileId = inputParams.get("fileId");
		String fileIdSec = inputParams.get("fileIdSec");
		
		String shakey = (String) MemoryManager.getFromCache(fileIdSec + inputParams.get("fileType"));
		//diagnostic.prepareDebug("************** DownloadAdhocStatementFile shakey :"+shakey).log();
		String key = inputParams.get("codeVerifier");
		if (key == null || "".equals(key)) {
			return ErrorCodeEnum.ERR_12401.setErrorCode(new Result());
		}
		String shaedkey = toHexString(getSHA(key));
		//diagnostic.prepareDebug("************** DownloadAdhocStatementFile shaedkey :"+shaedkey).log();
		if (shakey == null || !shakey.equals(shaedkey)) {
			return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
		}
		String fileId = (String) MemoryManager.getFromCache(shakey + inputParams.get("fileType"));
		String fileType = null;
		String fileName = null;

		HashMap<String, String> data = new HashMap<String, String>();

		data.put("id", fileId);
		data.put("fieldName", "fileContent");
		diagnostic.prepareDebug("************ DownloadAdhocStatementFile data :"+data).log();
//		Result getStatementFileContent = HelperMethods.callApi(dcRequest, data, HelperMethods.getHeaders(dcRequest),
//				URLConstants.ACCOUNTS_STATEMENT_FILES_BINARY_GET);
//		diagnostic.prepareDebug("************ DownloadAdhocStatementFile getStatementFileContent :"+ResultToJSON.convert(getStatementFileContent)).log();

		HashMap<String, String> detailsInputdata = new HashMap<String, String>();
		detailsInputdata.put(Constants.$FILTER, "id  eq '" + fileId + "'");
		diagnostic.prepareDebug("************ DownloadAdhocStatementFile detailsInputdata :"+detailsInputdata).log();
		Result getStatementFileDetails = HelperMethods.callApi(dcRequest, detailsInputdata,	HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_STATEMENT_FILES_GET);

		diagnostic.prepareDebug("************ DownloadAdhocStatementFile getStatementFileDetails :"+ResultToJSON.convert(getStatementFileDetails)).log();
		Dataset accountStatementset = getStatementFileDetails.getDatasetById("accountsstatementfiles");
		String base64 = null;
		if (accountStatementset.getAllRecords().size() > 0) {
			diagnostic.prepareInfo("Record is available for accountsstatementfiles").log();
			fileName = accountStatementset.getRecord(0).getParamValueByName("fileName");
			fileType = accountStatementset.getRecord(0).getParamValueByName("fileType");
			base64 = new String(accountStatementset.getRecord(0).getParamValueByName("fileContent"));
		} else {
			alert.prepareError("Error while fetching file name and file type accountStatementDetails").log();
			result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_28026.getErrorCode())));
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28026.getMessage()));
			return result;
		}

		

		if (StringUtils.isBlank(base64)) {
			alert.prepareError("Error while fetching base64 file from accountsstatementfiles table").log();
			result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_28026.getErrorCode())));
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28026.getMessage()));
			return result;
		}

		byte[] bytes = Base64.getMimeDecoder().decode(base64);

		if (fileType != null && (fileType.equals("pdf") || fileType.equals("application/pdf")))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/pdf"));
		else if (fileType != null && (fileType.equals("csv") || fileType.equals("text/csv")))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "text/csv"));
		else if (fileType != null && (fileType.equals("xls") || fileType.equals("xlsx") || fileType.equals("application/vnd.ms-excel")))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

		try {
			dcResponse.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(new ByteArrayEntity(bytes)));
		} catch (IOException exception) {
			alert.prepareError("Error while downloading file", exception).log();
			result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_28027.getErrorCode())));
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28027.getMessage()));
			return result;
		}
		dcResponse.setStatusCode(HttpStatus.SC_OK);
		MemoryManager.removeFromCache(fileIdSec + inputParams.get("fileType"));
		MemoryManager.removeFromCache(shakey + inputParams.get("fileType"));
		return result;
	}

	private Map<String, String> getCustomHeaders(String filename, String contentType) {
		Map<String, String> customHeaders = new HashMap<>();
		customHeaders.put(HttpHeaders.CONTENT_TYPE, contentType);
		customHeaders.put("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		return customHeaders;
	}
	
    
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }

}