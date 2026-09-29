/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.HTTP_HEADER_CONTENT_DISPOSITION;

/**
 * @author k.meiyazhagan
 */
public class DownloadTradeFinanceAttachmentOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result;
        try {
            String fileId = request.getParameter("fileId");
            String fileName = null;
            String fileType = null;
            String fileContents = null;
            HashMap<String, String> dataMapFilter = new HashMap<>();

            if (CommonUtils.isDMSIntegrationEnabled()) {
                dataMapFilter.put("documentId", fileId);
                result = HelperMethods.callApi(request, dataMapFilter, HelperMethods.getHeaders(request), URLConstants.DOCUMENT_STORAGE_DOWNLOAD);
                fileContents = result.getParamValueByName("documentContent");
            } else {
                dataMapFilter.put(DBPUtilitiesConstants.FILTER, "paymentFileID" + DBPUtilitiesConstants.EQUAL + fileId);
                result = HelperMethods.callApi(request, dataMapFilter, HelperMethods.getHeaders(request), URLConstants.PAYMENT_FILES_GET);
                if (!HelperMethods.hasRecords(result)) {
                    return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
                }
                if (!result.getAllDatasets().isEmpty() && result.getAllDatasets().get(0).getRecord(0) != null) {
                    Record recordData = result.getAllDatasets().get(0).getRecord(0);
                    fileName = recordData.getParamValueByName("paymentFileName");
                    fileType = recordData.getParamValueByName("paymentFileType");
                    fileContents = recordData.getParamValueByName("paymentFileContents");
                }
            }

            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put(HttpHeaders.CONTENT_TYPE, fileType);
            customHeaders.put(HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, HTTP_HEADER_CONTENT_DISPOSITION);
            customHeaders.put("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.getHeaders().putAll(customHeaders);

            byte[] bytes = Base64.getMimeDecoder().decode(fileContents);
            response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(new ByteArrayEntity(bytes)));
            response.setStatusCode(HttpStatus.SC_OK);

            return result;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERRTF_29054.setErrorCode(new Result());
        }
    }
}
