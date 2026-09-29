/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.CreateExportLCBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class CreateExportLCBackendDelegateImpl implements CreateExportLCBackendDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public ExportLOCDTO createExportLetterOfCredit(ExportLOCDTO payloadDTO, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(payloadDTO).toString().replace("\"", "'");

        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("ExportLetterOfCreditsType", "ExportLetterOfCreditsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("ExportLetterOfCreditsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            payloadDTO.setExportLCId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            payloadDTO = new ExportLOCDTO();
            alert.prepareError("Unable to create Letter Of Credit request order " + responseObject).log();
            payloadDTO.setErrorMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            // payloadDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }

        return payloadDTO;
    }

    public ExportLOCDTO updateExportLetterOfCredit(ExportLOCDTO payloadDTO, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(payloadDTO).toString().replaceAll("\"", "'");

        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(payloadDTO.getExportLCId())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(payloadDTO.getExportLCId()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            payloadDTO.setExportLCId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            payloadDTO = new ExportLOCDTO();
            alert.prepareError("Unable to update export LC request order " + responseObject).log();
            payloadDTO.setErrorMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            // payloadDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }

        return payloadDTO;
    }

    private JSONObject constructSRMSParams(ExportLOCDTO createPayloadDTO) {
        JSONObject requestBody = new JSONObject();
        if (StringUtils.isNotEmpty(createPayloadDTO.getDrawingAmount()))
            requestBody.put("drawingAmount", createPayloadDTO.getDrawingAmount());
        if (StringUtils.isNotEmpty(createPayloadDTO.getLcType()))
            requestBody.put("lcType", createPayloadDTO.getLcType());
        if (StringUtils.isNotEmpty(createPayloadDTO.getLcReferenceNo()))
            requestBody.put("lcReferenceNo", createPayloadDTO.getLcReferenceNo());
        if (StringUtils.isNotEmpty(createPayloadDTO.getIssuingBankReference()))
            requestBody.put("issuingBankReference", createPayloadDTO.getIssuingBankReference());
        if (StringUtils.isNotEmpty(createPayloadDTO.getAdvisingBankReference()))
            requestBody.put("advisingBankReference", createPayloadDTO.getAdvisingBankReference());
        if (StringUtils.isNotEmpty(createPayloadDTO.getApplicant()))
            requestBody.put("applicant", createPayloadDTO.getApplicant());
        if (StringUtils.isNotEmpty(createPayloadDTO.getUtilizedLCAmount()))
            requestBody.put("utilizedLCAmount", createPayloadDTO.getUtilizedLCAmount());
        if (StringUtils.isNotEmpty(createPayloadDTO.getIssueDate()))
            requestBody.put("issueDate", createPayloadDTO.getIssueDate());
        if (StringUtils.isNotEmpty(createPayloadDTO.getAmount()))
            requestBody.put("amount", createPayloadDTO.getAmount());
        if (StringUtils.isNotEmpty(createPayloadDTO.getExpiryDate()))
            requestBody.put("expiryDate", createPayloadDTO.getExpiryDate());
        if (StringUtils.isNotEmpty(createPayloadDTO.getCurrency()))
            requestBody.put("currency", createPayloadDTO.getCurrency());
        if (StringUtils.isNotEmpty(createPayloadDTO.getIssuingBank()))
            requestBody.put("issuingBank", createPayloadDTO.getIssuingBank());
        if (StringUtils.isNotEmpty(createPayloadDTO.getApplicantaddress()))
            requestBody.put("applicantaddress", createPayloadDTO.getApplicantaddress());
        if (StringUtils.isNotEmpty(createPayloadDTO.getIssuingbankaddress()))
            requestBody.put("issuingbankaddress", createPayloadDTO.getIssuingbankaddress());
        if (StringUtils.isNotEmpty(createPayloadDTO.getPaymentTerms()))
            requestBody.put("paymentTerms", createPayloadDTO.getPaymentTerms());
        if (StringUtils.isNotEmpty(createPayloadDTO.getDocumentName()))
            requestBody.put("documentName", createPayloadDTO.getDocumentName());
        if (StringUtils.isNotEmpty(createPayloadDTO.getUploadedFiles()))
            requestBody.put("uploadedFiles", createPayloadDTO.getUploadedFiles());
        if (StringUtils.isNotEmpty(createPayloadDTO.getForwardContract()))
            requestBody.put("forwardContract", createPayloadDTO.getForwardContract());
        if (StringUtils.isNotEmpty(createPayloadDTO.getBeneficiaryName()))
            requestBody.put("beneficiaryName", createPayloadDTO.getBeneficiaryName());
        if (StringUtils.isNotEmpty(createPayloadDTO.getBeneficiaryAddress()))
            requestBody.put("beneficiaryAddress", createPayloadDTO.getBeneficiaryAddress());
        if (StringUtils.isNotEmpty(createPayloadDTO.getGoodsDescription()))
            requestBody.put("goodsDescription", createPayloadDTO.getGoodsDescription());
        if (StringUtils.isNotEmpty(createPayloadDTO.getAdditionalConditions()))
            requestBody.put("additionalConditions", createPayloadDTO.getAdditionalConditions());
        if (StringUtils.isNotEmpty(createPayloadDTO.getConfirmInstructions()))
            requestBody.put("confirmInstructions", createPayloadDTO.getConfirmInstructions());
        if (StringUtils.isNotEmpty(createPayloadDTO.getLatestShipmentDate()))
            requestBody.put("latestShipmentDate", createPayloadDTO.getLatestShipmentDate());
        if (StringUtils.isNotEmpty(createPayloadDTO.getStatus()))
            requestBody.put("status", createPayloadDTO.getStatus());
        if (StringUtils.isNotEmpty(createPayloadDTO.getAmendmentNo()))
            requestBody.put("amendmentNo", createPayloadDTO.getAmendmentNo());
        if (StringUtils.isNotEmpty(createPayloadDTO.getBeneficiaryConsent()))
            requestBody.put("beneficiaryConsent", createPayloadDTO.getBeneficiaryConsent());
        if (StringUtils.isNotEmpty(createPayloadDTO.getMessageToBank()))
            requestBody.put("messageToBank", createPayloadDTO.getMessageToBank());
        if (StringUtils.isNotEmpty(createPayloadDTO.getReasonForRejection()))
            requestBody.put("reasonForRejection", createPayloadDTO.getReasonForRejection());
        if (StringUtils.isNotEmpty(createPayloadDTO.getLcUpdatedOn()))
            requestBody.put("lcUpdatedOn", createPayloadDTO.getLcUpdatedOn());
//        requestBody.put("lcCreatedOn", getCurrentDateTimeUTF());
        return requestBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}
