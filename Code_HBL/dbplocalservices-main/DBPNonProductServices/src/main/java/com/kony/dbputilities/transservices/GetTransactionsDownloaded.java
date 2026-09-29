package com.kony.dbputilities.transservices;

import java.util.HashMap;
import java.util.Map;

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

public class GetTransactionsDownloaded implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String fileId = inputParams.get("fileId");
        byte[] bytes = (byte[]) MemoryManager.getFromCache(fileId);
        if(bytes == null)
        {
			return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
		}
        FileGenerator generator = FileGeneratorFactory.getFileGenerator(inputParams.get("fileType"));
        String fileName = getFileName(inputParams);
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
        String fileName = StringUtils.isNotBlank(accountNumber) ? accountNumber : "Transaction Report";
        return fileName + "." + inputParams.get("fileType");
    }

    private Map<String, String> getCustomHeaders(String filename, String contentType) {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, contentType);
        customHeaders.put("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        return customHeaders;
    }
}