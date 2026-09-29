/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.MWConstants;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.HTTP_HEADER_CONTENT_DISPOSITION;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getAttachmentName;

public class downloadGeneratedListOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();

        @SuppressWarnings("unchecked")
        Map<String, Object> inputParams = (HashMap<String, Object>) objects[1];
        String fileId = (String) inputParams.get("fileId");
        if (StringUtils.isBlank(fileId)) {
            alert.prepareError("FileId is missing in the payload which is mandatory to fetch the file details").log();
            return ErrorCodeEnum.ERRTF_29058.setErrorCode(result);
        }

        fileId += new CorporateManager(dataControllerRequest).getSessionId();
        String fileDetails = (String) MemoryManager.getFromCache(fileId);
        if (fileDetails == null) {
            alert.prepareError("SECURITY EXCEPTION - UNAUTHORIZED ACCESS").log();
            response.setAttribute("dbpErrMsg", "SECURITY EXCEPTION - UNAUTHORIZED ACCESS");
            response.setAttribute("dbpErrCode", "12403");
            return new Result();
        }
        MemoryManager.removeFromCache(fileId);
        byte[] bytes = Base64.decodeBase64(fileDetails);
        String fileName = getAttachmentName(fileId.substring(0, 4)) + "s";

        // custom headers
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        customHeaders.put(HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, HTTP_HEADER_CONTENT_DISPOSITION);
        customHeaders.put(HTTP_HEADER_CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"");

        try {
            response.getHeaders().putAll(customHeaders);
            response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(new ByteArrayEntity(bytes)));
            response.setStatusCode(HttpStatus.SC_OK);
            return result;
        } catch (Exception e) {
            alert.prepareError("Error while downloading the " + fileName + " List", e).log();
        }
        return ErrorCodeEnum.ERRTF_29079.setErrorCode(new Result());
    }

}
