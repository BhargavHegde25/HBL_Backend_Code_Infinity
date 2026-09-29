/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ExportLetterOfCreditsDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class ExportLetterOfCreditsDrawingsBackendDelegateImpl
        implements ExportLetterOfCreditsDrawingsBackendDelegate, TradeFinanceConstants {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    public ExportLCDrawingsDTO createExportDrawing(ExportLCDrawingsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(inputDto).toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("ExportLetterOfCreditsDrawingType", "ExportLetterOfCreditsDrawingsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("ExportLetterOfCreditsDrawingsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setDrawingCreatedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            inputDto.setDrawingSRMSRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new ExportLCDrawingsDTO();
            inputDto.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Unable to create Letter Of Credit request order " + responseObject).log();
        }
        return inputDto;
    }

    public ExportLCDrawingsDTO updateExportLetterOfCreditDrawing(ExportLCDrawingsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(inputDto).toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getDrawingSRMSRequestId())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getDrawingSRMSRequestId()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setDrawingSRMSRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            alert.prepareError("Unable to update Drawings requests " + responseObject).log();
            inputDto = new ExportLCDrawingsDTO();
            inputDto.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    private JSONObject constructSRMSParams(ExportLCDrawingsDTO inputDTO) {
        JSONObject reqBody = new JSONObject();
        if (StringUtils.isNotEmpty(inputDTO.getDrawingAmount()))
            reqBody.put("drawingAmount", inputDTO.getDrawingAmount());
        if (StringUtils.isNotEmpty(inputDTO.getFinanceBill()))
            reqBody.put("financeBill", inputDTO.getFinanceBill());
        if (StringUtils.isNotEmpty(inputDTO.getCurrency()))
            reqBody.put("currency", inputDTO.getCurrency());
        if (StringUtils.isNotEmpty(inputDTO.getApplicant()))
            reqBody.put("applicant", inputDTO.getApplicant());
        if (StringUtils.isNotEmpty(inputDTO.getCreditAccount()))
            reqBody.put("creditAccount", inputDTO.getCreditAccount());
        if (StringUtils.isNotEmpty(inputDTO.getExternalAccount()))
            reqBody.put("externalAccount", inputDTO.getExternalAccount());
        if (StringUtils.isNotEmpty(inputDTO.getChargesDebitAccount()))
            reqBody.put("chargesDebitAccount", inputDTO.getChargesDebitAccount());
        if (StringUtils.isNotEmpty(inputDTO.getMessageToBank()))
            reqBody.put("messageToBank", inputDTO.getMessageToBank());
        if (StringUtils.isNotEmpty(inputDTO.getStatus()))
            reqBody.put("status", inputDTO.getStatus());
        if (StringUtils.isNotEmpty(inputDTO.getLcReferenceNo()))
            reqBody.put("lcReferenceNo", inputDTO.getLcReferenceNo());
        if (StringUtils.isNotEmpty(inputDTO.getPhysicalDocuments()))
            reqBody.put("physicalDocuments", inputDTO.getPhysicalDocuments());
        if (StringUtils.isNotEmpty(inputDTO.getLcType()))
            reqBody.put("lcType", inputDTO.getLcType());
        if (StringUtils.isNotEmpty(inputDTO.getUploadedDocuments()))
            reqBody.put("uploadedDocuments", inputDTO.getUploadedDocuments());
        if (StringUtils.isNotEmpty(inputDTO.getForwardDocuments()))
            reqBody.put("forwardDocuments", inputDTO.getForwardDocuments());
        if (StringUtils.isNotEmpty(inputDTO.getAdvisingBankReference()))
            reqBody.put("advisingBankReference", inputDTO.getAdvisingBankReference());
        if (StringUtils.isNotEmpty(inputDTO.getExportLCId()))
            reqBody.put("exportLCId", inputDTO.getExportLCId());
        if (StringUtils.isNotEmpty(inputDTO.getLcAmount()))
            reqBody.put("lcAmount", inputDTO.getLcAmount());
        if (StringUtils.isNotEmpty(inputDTO.getExpiryDate()))
            reqBody.put("expiryDate", inputDTO.getExpiryDate());
        if (StringUtils.isNotEmpty(inputDTO.getTotalDocuments()))
            reqBody.put("totalDocuments", inputDTO.getTotalDocuments());
        if (StringUtils.isNotEmpty(inputDTO.getDiscrepencies()))
            reqBody.put("discrepencies", inputDTO.getDiscrepencies());
        if (StringUtils.isNotEmpty(inputDTO.getDiscrepenciesAcceptance()))
            reqBody.put("discrepenciesAcceptance", inputDTO.getDiscrepenciesAcceptance());
        if (StringUtils.isNotEmpty(inputDTO.getPaymentStatus()))
            reqBody.put("paymentStatus", inputDTO.getPaymentStatus());
        if (StringUtils.isNotEmpty(inputDTO.getTotalAmount()))
            reqBody.put("totalAmount", inputDTO.getTotalAmount());
        if (StringUtils.isNotEmpty(inputDTO.getDocumentStatus()))
            reqBody.put("documentStatus", inputDTO.getDocumentStatus());
        if (StringUtils.isNotEmpty(inputDTO.getDiscrepanciesHistory1()))
            reqBody.put("discrepanciesHistory1",
                    inputDTO.getDiscrepanciesHistory1().replace("'", "\'").replace("'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getDiscrepanciesHistory2()))
            reqBody.put("discrepanciesHistory2",
                    inputDTO.getDiscrepanciesHistory2().replace("'", "\'").replace("'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getDiscrepanciesHistory3()))
            reqBody.put("discrepanciesHistory3",
                    inputDTO.getDiscrepanciesHistory3().replace("'", "\'").replace("'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getDiscrepanciesHistory4()))
            reqBody.put("discrepanciesHistory4",
                    inputDTO.getDiscrepanciesHistory4().replace("'", "\'").replace("'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getDiscrepanciesHistory5()))
            reqBody.put("discrepanciesHistory5",
                    inputDTO.getDiscrepanciesHistory5().replace("'", "\'").replace("'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getLcCurrency()))
            reqBody.put("lcCurrency", inputDTO.getLcCurrency());
        if (StringUtils.isNotEmpty(inputDTO.getLcIssueDate()))
            reqBody.put("lcIssueDate", inputDTO.getLcIssueDate());
        if (StringUtils.isNotEmpty(inputDTO.getIssuingBank()))
            reqBody.put("issuingBank", inputDTO.getIssuingBank());
        if (StringUtils.isNotEmpty(inputDTO.getPaymentDate()))
            reqBody.put("paymentDate", inputDTO.getPaymentDate());
        if (StringUtils.isNotEmpty(inputDTO.getMessageFromBank()))
            reqBody.put("messageFromBank", inputDTO.getMessageFromBank());
        if (StringUtils.isNotEmpty(inputDTO.getReasonForReturn()))
            reqBody.put("reasonForReturn", inputDTO.getReasonForReturn());
        if (StringUtils.isNotEmpty(inputDTO.getReturnedDate()))
            reqBody.put("returnedDate", inputDTO.getReturnedDate());
        if (StringUtils.isNotEmpty(inputDTO.getReturnMessageToBank()))
            reqBody.put("returnMessageToBank", inputDTO.getReturnMessageToBank());
        if (StringUtils.isNotEmpty(inputDTO.getReturnedDocuments()))
            reqBody.put("returnedDocuments", inputDTO.getReturnedDocuments());
        if (StringUtils.isNotEmpty(inputDTO.getApprovedDate()))
            reqBody.put("approvedDate", inputDTO.getApprovedDate());
        if (StringUtils.isNotEmpty(inputDTO.getDocumentReference()))
            reqBody.put("documentReference", inputDTO.getDocumentReference());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        reqBody.put("drawingCreatedDate", sdf.format(date));

        return reqBody;
    }

    @Override
    public List<ExportLCDrawingsDTO> getExportLetterOfCreditDrawings(DataControllerRequest request) {
        List drawingsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                drawingsList = invoke().addDTO(ExportLCDrawingsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("ExportLetterOfCreditsDrawingType", "ExportLetterOfCreditsDrawingsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                drawingsList = getInstance().addDTO(ExportLCDrawingsDTO.class).addDataControllerRequest(request)
                        .addModule("ExportLetterOfCreditsDrawingsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
            // setDrawingSRMSRequestId / setDrawingReferenceNo

//            if (StringUtils.isNotBlank(exportDTO.getUploadedDocuments())) {
//                JSONArray uploadedDocuments = new JSONArray(exportDTO.getUploadedDocuments());
//                exportDTO.setUploadedDocuments(uploadedDocuments.toString());
//            }
//            if (StringUtils.isNotBlank(exportDTO.getReturnedDocuments())) {
//                JSONArray returnedDocuments = new JSONArray(exportDTO.getReturnedDocuments());
//                exportDTO.setReturnedDocuments(returnedDocuments.toString());
//            }
//            if (StringUtils.isNotBlank(exportDTO.getPhysicalDocuments())) {
//                JSONArray physicalDocuments = new JSONArray(exportDTO.getPhysicalDocuments());
//                exportDTO.setPhysicalDocuments(physicalDocuments.toString());
//            }
//            if (StringUtils.isNotBlank(exportDTO.getDiscrepencies())) {
//                JSONArray discrepencies = new JSONArray(exportDTO.getDiscrepencies());
//                exportDTO.setDiscrepencies(discrepencies.toString());
//            }
//
//            JSONArray discrepanciesHistory = new JSONArray();
//            if (StringUtils.isNotBlank(exportDTO.getDiscrepanciesHistory1()))
//                discrepanciesHistory.put(exportDTO.getDiscrepanciesHistory1());
//            if (StringUtils.isNotBlank(exportDTO.getDiscrepanciesHistory2()))
//                discrepanciesHistory.put(exportDTO.getDiscrepanciesHistory2());
//            if (StringUtils.isNotBlank(exportDTO.getDiscrepanciesHistory3()))
//                discrepanciesHistory.put(exportDTO.getDiscrepanciesHistory3());
//            if (StringUtils.isNotBlank(exportDTO.getDiscrepanciesHistory4()))
//                discrepanciesHistory.put(exportDTO.getDiscrepanciesHistory4());
//            if (StringUtils.isNotBlank(exportDTO.getDiscrepanciesHistory5()))
//                discrepanciesHistory.put(exportDTO.getDiscrepanciesHistory5());
//            if (!discrepanciesHistory.isEmpty())
//                exportDTO.setDiscrepanciesHistory(discrepanciesHistory.toString());
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching export drawings", e).log();
        }
        return drawingsList;
    }

    @Override
    public ExportLCDrawingsDTO getExportLetterOfCreditDrawingById(DataControllerRequest request, String drawingSRMSRequestId) {
        ExportLCDrawingsDTO drawingDTO = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                drawingDTO = (ExportLCDrawingsDTO) invoke().addDTO(ExportLCDrawingsDTO.class).addServiceRequestId(drawingSRMSRequestId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                drawingDTO = (ExportLCDrawingsDTO) getInstance().addDTO(ExportLCDrawingsDTO.class).filterByRecordId(drawingSRMSRequestId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            drawingDTO.setDrawingSRMSRequestId(drawingSRMSRequestId);
            // drawingDTO.setCustomerId((String) drawingDetailsResponse.get(PARAM_PARTY_ID));

            if (StringUtils.isNotBlank(drawingDTO.getUploadedDocuments())) {
                JSONArray uploadedDocuments = new JSONArray(drawingDTO.getUploadedDocuments());
                drawingDTO.setUploadedDocuments(uploadedDocuments.toString());
            }
            if (StringUtils.isNotBlank(drawingDTO.getReturnedDocuments())) {
                JSONArray returnedDocuments = new JSONArray(drawingDTO.getReturnedDocuments());
                drawingDTO.setReturnedDocuments(returnedDocuments.toString());
            }
            if (StringUtils.isNotBlank(drawingDTO.getPhysicalDocuments())) {
                JSONArray physicalDocuments = new JSONArray(drawingDTO.getPhysicalDocuments());
                drawingDTO.setPhysicalDocuments(physicalDocuments.toString());
            }
            if (StringUtils.isNotBlank(drawingDTO.getDiscrepencies())) {
                JSONArray discrepencies = new JSONArray(drawingDTO.getDiscrepencies());
                drawingDTO.setDiscrepencies(discrepencies.toString());
            }

            JSONArray discrepanciesHistory = new JSONArray();
            if (StringUtils.isNotBlank(drawingDTO.getDiscrepanciesHistory1()))
                discrepanciesHistory.put(drawingDTO.getDiscrepanciesHistory1());
            if (StringUtils.isNotBlank(drawingDTO.getDiscrepanciesHistory2()))
                discrepanciesHistory.put(drawingDTO.getDiscrepanciesHistory2());
            if (StringUtils.isNotBlank(drawingDTO.getDiscrepanciesHistory3()))
                discrepanciesHistory.put(drawingDTO.getDiscrepanciesHistory3());
            if (StringUtils.isNotBlank(drawingDTO.getDiscrepanciesHistory4()))
                discrepanciesHistory.put(drawingDTO.getDiscrepanciesHistory4());
            if (StringUtils.isNotBlank(drawingDTO.getDiscrepanciesHistory5()))
                discrepanciesHistory.put(drawingDTO.getDiscrepanciesHistory5());
            if (!discrepanciesHistory.isEmpty())
                drawingDTO.setDiscrepanciesHistory(discrepanciesHistory.toString());
        } catch (Exception e) {
            request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Error occurred while fetching outward collection", e).log();
        }
        return drawingDTO;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}