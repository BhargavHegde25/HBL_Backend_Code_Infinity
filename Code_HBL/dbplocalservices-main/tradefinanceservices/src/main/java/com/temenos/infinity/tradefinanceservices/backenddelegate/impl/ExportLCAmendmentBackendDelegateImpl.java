/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ExportLCAmendmentBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class ExportLCAmendmentBackendDelegateImpl implements ExportLCAmendmentBackendDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public List<ExportLCAmendmentsDTO> getExportAmendments(DataControllerRequest request) {
        List amendmentsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentsList = invoke().addDTO(ExportLCAmendmentsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("ExportLetterOfCreditsAmendmentType", "ExportLetterOfCreditsAmendmentSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                amendmentsList = getInstance().addDTO(ExportLCAmendmentsDTO.class).addDataControllerRequest(request)
                        .addModule("ExportLetterOfCreditsAmendmentModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
            // setAmendmentSRMSRequestId / setAmendmentReferenceNo
        } catch (Exception e) {
            request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Error occurred while fetching export amendments", e).log();
        }
        return amendmentsList;
    }

    public ExportLCAmendmentsDTO amendExportLCcreate(ExportLCAmendmentsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        JSONObject requestBody = constructRequestPayload(inputDto);
        requestBody.put("serviceRequestTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
        String payloadRequestBody = requestBody.toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(payloadRequestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("ExportLetterOfCreditsAmendmentType", "ExportLetterOfCreditsAmendmentSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(payloadRequestBody)
                    .addModule("ExportLetterOfCreditsAmendmentModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setAmendmentSRMSRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
            inputDto.setSelfAcceptance("Pending");
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            inputDto = new ExportLCAmendmentsDTO();
            inputDto.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Error occurred while creating amendment " + responseObject).log();
        }
        return inputDto;
    }

    public JSONObject constructRequestPayload(ExportLCAmendmentsDTO letterOfCredit) {
        JSONObject requestBody = new JSONObject();
        if (StringUtils.isNotEmpty(letterOfCredit.getApplicantName())) {
            requestBody.put("applicantName", letterOfCredit.getApplicantName());
        }
        if (letterOfCredit.getOldLcAmount() != null) {
            requestBody.put("oldLcAmount",
                    String.valueOf(letterOfCredit.getOldLcAmount()));
        }

        if (letterOfCredit.getNewLcAmount() != null) {
            requestBody.put("newLcAmount",
                    String.valueOf(letterOfCredit.getNewLcAmount()));
        }

        if (StringUtils.isNotEmpty(letterOfCredit.getExportlcReferenceNo())) {
            requestBody.put("exportlcReferenceNo", letterOfCredit.getExportlcReferenceNo());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getExportlcSRMSRequestId())) {
            requestBody.put("exportlcSRMSRequestId", letterOfCredit.getExportlcSRMSRequestId());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentStatus())) {
            requestBody.put("amendmentStatus", letterOfCredit.getAmendmentStatus());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentNo())) {
            requestBody.put("amendmentNo", letterOfCredit.getAmendmentNo());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLcType())) {
            requestBody.put("lcType", letterOfCredit.getLcType());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLcIssueDate())) {
            requestBody.put("lcIssueDate", letterOfCredit.getLcIssueDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLcExpiryDate())) {
            requestBody.put("lcExpiryDate", letterOfCredit.getLcExpiryDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLcCurrency())) {
            requestBody.put("lcCurrency", letterOfCredit.getLcCurrency());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLcAmountStatus())) {
            requestBody.put("lcAmountStatus", letterOfCredit.getLcAmountStatus());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLatestShipmentDate())) {
            requestBody.put("latestShipmentDate", letterOfCredit.getLatestShipmentDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getOtherAmendments())) {
            requestBody.put("otherAmendments", letterOfCredit.getOtherAmendments());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getPeriodOfPresentation())) {
            requestBody.put("periodOfPresentation", letterOfCredit.getPeriodOfPresentation());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentChargesPayer())) {
            requestBody.put("amendmentChargesPayer", letterOfCredit.getAmendmentChargesPayer());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getChargesDebitAccount())) {
            requestBody.put("chargesDebitAccount", letterOfCredit.getChargesDebitAccount());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentReferenceNo())) {
            requestBody.put("amendmentReferenceNo", letterOfCredit.getAmendmentReferenceNo());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentSRMSRequestId())) {
            requestBody.put("amendmentSRMSRequestId", letterOfCredit.getAmendmentSRMSRequestId());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentReceivedDate())) {
            requestBody.put("amendmentReceivedDate", letterOfCredit.getAmendmentReceivedDate());
        }
		/*if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentReceivedDate())) {
			requestBody.put("amendmentReceivedDate", letterOfCredit.getAmendmentReceivedDate());
		}*/
        if (StringUtils.isNotEmpty(letterOfCredit.getReasonForSelfRejection())) {
            requestBody.put("reasonForSelfRejection", letterOfCredit.getReasonForSelfRejection());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getSelfRejectedDate())) {
            requestBody.put("selfRejectedDate", letterOfCredit.getSelfRejectedDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getSelfAcceptance())) {
            requestBody.put("selfAcceptance", letterOfCredit.getSelfAcceptance());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getSelfAcceptanceDate())) {
            requestBody.put("selfAcceptanceDate", letterOfCredit.getSelfAcceptanceDate());
        }


        // requestBody.put("lcAmount", String.valueOf(letterOfCredit.getLcAmount()));
        return requestBody;
    }

    public ExportLCAmendmentsDTO updateExportLCAmendment(ExportLCAmendmentsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructRequestPayload(inputDto).toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getAmendmentSRMSRequestId())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getAmendmentSRMSRequestId()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setAmendmentSRMSRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new ExportLCAmendmentsDTO();
            alert.prepareError("Unable to update export lc amendment " + responseObject).log();
            inputDto.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));

        }
        return inputDto;
    }

    public ExportLCAmendmentsDTO getExportLCAmendmentById(String amendmentSRMSRequestId, DataControllerRequest request) {
        ExportLCAmendmentsDTO amendmentDTO = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentDTO = (ExportLCAmendmentsDTO) invoke().addDTO(ExportLCAmendmentsDTO.class).addServiceRequestId(amendmentSRMSRequestId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                amendmentDTO = (ExportLCAmendmentsDTO) getInstance().addDTO(ExportLCAmendmentsDTO.class).filterByRecordId(amendmentSRMSRequestId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            amendmentDTO.setAmendmentSRMSRequestId(amendmentSRMSRequestId);
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching export lc amendment", e).log();
        }
        return amendmentDTO;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}