package com.temenos.infinity.api.docmanagement.acctstatement.javaservices;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetCombinedStatementsDownloaded implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		
		Result result = new Result();
		HashMap<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String fileType = inputParams.get("fileType");
		String fileName = inputParams.get("fileName");
		String fileId = inputParams.get("id");
        String shakey = (String) MemoryManager.getFromCache(fileId+inputParams.get("fileType"));
        String key = inputParams.get("codeVerifier");
        String shaedkey = toHexString(getSHA(key));
        byte[] bytes= {};
		if(shakey.equals(shaedkey))
	 bytes = (byte[])MemoryManager.getFromCache(fileId);
        if(bytes == null)
        {
			return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
		}
		
		if (fileType.equals("pdf") || fileType.equals("application/pdf"))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/pdf"));
		else if (fileType.equals("csv") || fileType.equals("text/csv"))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "text/csv"));
		else if (fileType.equals("xls") || fileType.equals("xlsx") || fileType.equals("application/vnd.ms-excel"))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		else if (fileType.equals("qfx"))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/vnd.intu.qfx"));
		else if (fileType.equals("qbo"))
			dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/vnd.intu.qbo"));
		
		try {
			dcResponse.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
					new BufferedHttpEntity(new ByteArrayEntity(bytes)));
		} catch (IOException exception) {
			alert.prepareError("Error while downloading file", exception).log();
			result.addParam(
					new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_28027.getErrorCode())));
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28027.getMessage()));
			return result;
		}
		bytes= null;
		dcResponse.setStatusCode(HttpStatus.SC_OK);
		MemoryManager.removeFromCache(fileId);
		MemoryManager.removeFromCache(fileId+inputParams.get("fileType"));
		return result;
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

	private Map<String, String> getCustomHeaders(String filename, String contentType) {
		Map<String, String> customHeaders = new HashMap<>();
		customHeaders.put(HttpHeaders.CONTENT_TYPE, contentType);
		customHeaders.put("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		return customHeaders;
	}

}
