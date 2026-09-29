/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.*;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.TradeFinanceDocumentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceUtils;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeFinanceDocumentsBackendImpl implements TradeFinanceDocumentsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result uploadDocumentDMS(String referenceId, String customerId, String uploadedFileName, String fileContents, DataControllerRequest request) {
        Result result = new Result();
        String message = "Upload document operation failed";
        TradeFinanceUtils tfUtils = TradeFinanceUtils.getInstance();

        Map<String, Object> headerMap = new HashMap<>();
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("userId", customerId);
        inputMap.put("referenceId", referenceId);
        inputMap.put("documentName", uploadedFileName);
        inputMap.put("category", "payment");
        inputMap.put("content", fileContents);
        inputMap.put("version", "1.0");
        inputMap.put("documentGroup", TradeFinanceConstants.DMS_DOCUMENT_GROUP);
        inputMap.put("ownerSystemId", TradeFinanceConstants.DMS_OWNER_SYSTEM_ID);

        try {
            inputMap.put("authorization", tfUtils.generateDMSToken(request));
            String docResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(TradeFinanceAPIServices.DOCUMENTINTEGRATIONSERVICES_UPLOADDOCUMENT
                            .getServiceName())
                    .withOperationId(TradeFinanceAPIServices.DOCUMENTINTEGRATIONSERVICES_UPLOADDOCUMENT
                            .getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap)
                    .withDataControllerRequest(request).build().getResponse();

            result = JSONToResult.convert(docResponse);
            if (StringUtils.isNotBlank(result.getParamValueByName("documentId")) && result.getParamValueByName("opstatus").equals("0")) {
                message = "Upload document operation success";
                result.addParam("status", "success");
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while uploading attachment from DMS. Error: " + e).log();
            result.addErrMsgParam("Error occurred while uploading attachment");
        }

        result.addParam("message", message);
        return result;
    }

    @Override
    public Result fetchDocumentDMS(String referenceId, DataControllerRequest request) {

        Result result = new Result();
        Dataset dataSet = new Dataset();
        String message = "Failed to fetch attachments";
        TradeFinanceUtils tfUtils = TradeFinanceUtils.getInstance();
        Map<String, Object> headerMap = new HashMap<>();

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("documentId", referenceId);
        inputMap.put("documentGroup", TradeFinanceConstants.DMS_DOCUMENT_GROUP);
        inputMap.put("ownerSystemId", TradeFinanceConstants.DMS_OWNER_SYSTEM_ID);

        String docResponse = null;
        try {
            inputMap.put("authorization", tfUtils.generateDMSToken(request));

            docResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(TradeFinanceAPIServices.DOCUMENTINTEGRATIONSERVICES_DOWNLOADDOCUMENT.getServiceName())
                    .withOperationId(TradeFinanceAPIServices.DOCUMENTINTEGRATIONSERVICES_DOWNLOADDOCUMENT.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

            result = JSONToResult.convert(docResponse);
            Record fileName = new Record();
            fileName.addParam(new Param("fileName", result.getParamValueByName("documentName")));
            fileName.addParam(new Param("fileID", referenceId));
            fileName.addParam(new Param("fileContent", result.getParamValueByName("content")));
            dataSet.addRecord(fileName);
            dataSet.setId("fileNames");
            result.addDataset(dataSet);
            message = "Success in fetching attachments";
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching attachments from DMS " + e).log();
            result.addErrMsgParam("Error occurred while fetching attachments");
        }

        result.addParam("message", message);
        return result;
    }

    @Override
    public Result deleteDocumentDMS(String referenceId, DataControllerRequest request) {
        Result result = new Result();
        String message = "Deletion Failed";
        TradeFinanceUtils tfUtils = TradeFinanceUtils.getInstance();
        Map<String, Object> headerMap = new HashMap<>();

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("documentId", referenceId);
        inputMap.put("documentGroup", TradeFinanceConstants.DMS_DOCUMENT_GROUP);
        inputMap.put("ownerSystemId", TradeFinanceConstants.DMS_OWNER_SYSTEM_ID);

        String docResponse = null;
        try {
            inputMap.put("authorization", tfUtils.generateDMSToken(request));
            docResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(
                            TradeFinanceAPIServices.DOCUMENTINTEGRATIONSERVICES_DELETEDOCUMENT.getServiceName())
                    .withOperationId(TradeFinanceAPIServices.DOCUMENTINTEGRATIONSERVICES_DELETEDOCUMENT
                            .getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap)
                    .withDataControllerRequest(request).build().getResponse();

            result = JSONToResult.convert(docResponse);
            JSONObject dmsResponse = new JSONObject(docResponse);
            if (dmsResponse.has("opstatus") && dmsResponse.get("opstatus").toString().equals("0")) {
                message = "Deletion Successful";
                result.addParam("message", message);
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while deleting attachment in DMS. Error: " + e).log();
            result.addErrMsgParam("Error occurred while deleting attachment");
        }
        result.addParam("message", message);
        return result;
    }

    @Override
    public Result uploadDocumentDBXDB(String referenceId, String customerId, String uploadedFileName, String fileExtension, String fileContents, DataControllerRequest request) {
        Result result = new Result();
        String message = "Upload document operation failed";

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("userId", customerId);
        inputMap.put("paymentFileID", CommonUtils.generateUniqueID(32));
        inputMap.put("transactionId", referenceId);
        inputMap.put("paymentFileName", uploadedFileName);
        inputMap.put("paymentFileType", fileExtension);
        inputMap.put("paymentFileContents", fileContents);

        try {
            result = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
                    URLConstants.PAYMENT_FILES_CREATE);
            if (result != null && StringUtils.isBlank(result.getErrMsgParamValue())) {
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
            inputMap.put(DBPUtilitiesConstants.FILTER, "transactionId " + DBPUtilitiesConstants.EQUAL + referenceId
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
                    + DBPUtilitiesConstants.AND + "transactionId" + DBPUtilitiesConstants.EQUAL + referenceId);
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
