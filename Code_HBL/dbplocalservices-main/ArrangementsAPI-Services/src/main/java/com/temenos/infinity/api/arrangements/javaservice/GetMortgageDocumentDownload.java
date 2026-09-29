package com.temenos.infinity.api.arrangements.javaservice;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
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



public class GetMortgageDocumentDownload implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
                         DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String fileId = inputParams.get("fileId");
        byte[] bytes = (byte[]) MemoryManager.getFromCache(fileId);
        if (bytes == null) {
            return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
        }
        String attachmentName = "MortgageDocument.pdf";
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
        customHeaders.put("Content-Disposition", "attachment; filename=\"" + attachmentName + "\"");
        dcResponse.getHeaders().putAll(customHeaders);
        dcResponse.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(new ByteArrayEntity(bytes)));
        dcResponse.setStatusCode(HttpStatus.SC_OK);
        MemoryManager.removeFromCache(fileId);
        return result;

    }
}