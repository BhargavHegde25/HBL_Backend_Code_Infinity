package com.temenos.infinity.api.docmanagement.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import com.kony.dbputilities.fileutil.FileGenerator;
import com.kony.dbputilities.fileutil.FileGeneratorFactory;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class GetTransactionsDownloaded implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String fileId = inputParams.get("fileId");
        byte[] bytes = (byte[]) MemoryManager.getFromCache(fileId);
        if(bytes == null)
        {
			return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
		}
        MemoryManager.removeFromCache(fileId);
        FileGenerator generator = FileGeneratorFactory.getFileGenerator(inputParams.get("fileType"));
        String fileName = getFileName(inputParams);
        diagnostic.prepareDebug("**************fileName fileName" + fileName).log();
        dcResponse.getHeaders().putAll(getCustomHeaders(fileName, generator.getContentType()));
        dcResponse.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
                new BufferedHttpEntity(new ByteArrayEntity(bytes)));
        dcResponse.setStatusCode(HttpStatus.SC_OK);       
        return new Result();
    }

	private String getFileName(Map<String, String> inputParams) {
		if ("xls".equalsIgnoreCase(inputParams.get("fileType"))) {
			inputParams.put("fileType", "xlsx");
		}
		if (StringUtils.isNotBlank(inputParams.get("transactionId"))) {
			return inputParams.get("transactionId") + "." + inputParams.get("fileType");
		}

		String accountNumber = inputParams.get("accountNumber");
		String fileName = "Transactions";
		String cardNumber = inputParams.get("cardNumber");
		String statementDate = inputParams.get("statementDate");
		diagnostic.prepareDebug("**************getFileName accountNumber" + accountNumber).log();
		diagnostic.prepareDebug("**************getFileName fileName" + fileName).log();
		diagnostic.prepareDebug("**************getFileName cardNumber" + cardNumber).log();
		diagnostic.prepareDebug("**************getFileName statementDate" + statementDate).log();
		
		if (StringUtils.isNotBlank(accountNumber)) {
			fileName = accountNumber;
			diagnostic.prepareDebug("**************IF filename" + fileName).log();
		}else if (StringUtils.isNotBlank(cardNumber) && StringUtils.isNotBlank(statementDate)) {
			fileName = cardNumber + "_" + statementDate;
			diagnostic.prepareDebug("**************Else IF filename" + fileName).log();
		}else {
			fileName = "Transactions";
			diagnostic.prepareDebug("**************Else filename" + fileName).log();
		}
		return fileName + "." + inputParams.get("fileType");
	}
    
   

    private Map<String, String> getCustomHeaders(String filename, String contentType) {
    	
    	diagnostic.prepareDebug("**************getCustomHeaders filename" + filename).log();
    	diagnostic.prepareDebug("**************getCustomHeaders contentType" + contentType).log();
        Map<String, String> customHeaders = new HashMap<>();
         customHeaders.put(HttpHeaders.CONTENT_TYPE, contentType);
        customHeaders.put("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        diagnostic.prepareDebug("**************header val" + customHeaders.get("Content-Disposition")).log();
        return customHeaders;
    	
    	
    }
}