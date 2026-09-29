/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.impl;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.DocumentsBackendDelegate;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author k.meiyazhagan
 */
public class DocumentsBackendImpl implements DocumentsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result uploadDocumentDBXDB(String referenceId, String customerId, String uploadedFileName, String fileExtension, String fileContents, DataControllerRequest request) {
        Result result = new Result();
        String message = "Upload document operation failed";

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("userId", customerId);
        inputMap.put("paymentFileID", referenceId);
        inputMap.put("transactionId", CommonUtils.generateUniqueID(32));
        inputMap.put("paymentFileName", uploadedFileName);
        inputMap.put("paymentFileType", fileExtension);
        inputMap.put("paymentFileContents", fileContents);

        try {
            result = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
                    URLConstants.PAYMENT_FILES_CREATE);
            if (StringUtils.isBlank(result.getErrMsgParamValue())) {
                message = "Upload document operation success";
                result.addParam("status", "true");
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while uploading attachment from DBXDB. Error: " + e).log();
            result.addErrMsgParam("Error occurred while uploading attachment");
            result.addParam("status", "false");
        }

        result.addParam("message", message);
        return result;
    }

    @Override
    public Result fetchDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request) {
        Result result = new Result();
        Map<String, Object> inputMap = new HashMap<>();
        Dataset dataSet = new Dataset();

        try {
            inputMap.put(DBPUtilitiesConstants.FILTER, "paymentFileID " + DBPUtilitiesConstants.EQUAL + referenceId
                    + DBPUtilitiesConstants.AND + "userId" + DBPUtilitiesConstants.EQUAL + customerId);

            result = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
                    URLConstants.PAYMENT_FILES_GET);

            if (result.getAllDatasets().size() > 0) {
                List<Record> files = result.getAllDatasets().get(0).getAllRecords();
                for (Record file : files) {
                    Record fileName = new Record();
                    fileName.addParam(new Param("fileName", file.getParamValueByName("paymentFileName")));
                    fileName.addParam(new Param("fileID", file.getParamValueByName("paymentFileID")));
                    fileName.addParam(new Param("uploadedTimeStamp", file.getParamValueByName("createdts")));
                    dataSet.addRecord(fileName);
                }
                dataSet.setId("fileNames");
            }
            result.addDataset(dataSet);
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching attachment from DBXDB. Error: " + e).log();
            result.addErrMsgParam("Error occurred while fetching attachment");
        }

        return result;
    }

    @Override
    public Result deleteDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request) {
        Result result = new Result();
        String message = "Deletion Failed";
        Map<String, String> inputMap = new HashMap<>();

        try {
            inputMap.put(DBPUtilitiesConstants.FILTER, "userId" + DBPUtilitiesConstants.EQUAL + customerId
                    + DBPUtilitiesConstants.AND + "paymentFileID" + DBPUtilitiesConstants.EQUAL + referenceId);
            result = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
                    URLConstants.PAYMENT_FILES_DELETE);

            if (StringUtils.isNotBlank(result.getParamValueByName("deletedRecords"))) {
                message = "Deletion Successful";
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while deleting attachment in DBXDB. Error: " + e).log();
            result.addErrMsgParam("Error occurred while deleting attachment");
        }

        result.addParam("message", message);
        return result;
    }
}
