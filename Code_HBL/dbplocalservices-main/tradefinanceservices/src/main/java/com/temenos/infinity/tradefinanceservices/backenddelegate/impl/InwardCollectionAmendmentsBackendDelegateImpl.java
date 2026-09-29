/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.InwardCollectionAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionAmendmentsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class InwardCollectionAmendmentsBackendDelegateImpl implements InwardCollectionAmendmentsBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public InwardCollectionAmendmentsDTO createInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(inputDto).toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("InwardCollectionAmendmentsType", "InwardCollectionAmendmentsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("InwardCollectionAmendmentsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setAmendmentSrmsId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            // ERRTF_29091
            inputDto = new InwardCollectionAmendmentsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<InwardCollectionAmendmentsDTO> getInwardCollectionAmendments(DataControllerRequest request) {
        List amendmentsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentsList = invoke().addDTO(InwardCollectionAmendmentsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("InwardCollectionAmendmentsType", "InwardCollectionAmendmentsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                amendmentsList = getInstance().addDTO(InwardCollectionAmendmentsDTO.class).addDataControllerRequest(request)
                        .addModule("InwardCollectionAmendmentsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
            // set amendmentSrmsId
            // set lastUpdatedDate
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching inward amendments", e).log();
        }
        return amendmentsList;
    }

    @Override
    public InwardCollectionAmendmentsDTO getInwardCollectionAmendmentById(String amendmentSrmsId, DataControllerRequest request) {
        InwardCollectionAmendmentsDTO amendmentDTO = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentDTO = (InwardCollectionAmendmentsDTO) invoke().addDTO(InwardCollectionAmendmentsDTO.class).addServiceRequestId(amendmentSrmsId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                amendmentDTO = (InwardCollectionAmendmentsDTO) getInstance().addDTO(InwardCollectionAmendmentsDTO.class).filterByRecordId(amendmentSrmsId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            amendmentDTO.setAmendmentSrmsId(amendmentSrmsId);
            // amendmentDTO.setLastUpdatedDate(singleOrder.getString(PARAM_ORDER_PROCESSED_TIME));
        } catch (Exception e) {
            // ERRTF_29070 / ERRTF_29071
            alert.prepareError("Error occurred while fetching inward amendments", e).log();
        }
        return amendmentDTO;
    }

    @Override
    public InwardCollectionAmendmentsDTO updateInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(inputDto).toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getAmendmentSrmsId())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getAmendmentSrmsId()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setAmendmentSrmsId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            // ERRTF_29092
            alert.prepareError("Unable to update inward amendment " + responseObject).log();
            inputDto = new InwardCollectionAmendmentsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    private JSONObject constructSRMSParams(InwardCollectionAmendmentsDTO inputDTO) {
        JSONObject reqBody = new JSONObject();

        if (StringUtils.isNotBlank(inputDTO.getAmendAmount()))
            reqBody.put("amendAmount", inputDTO.getAmendAmount());
        if (StringUtils.isNotBlank(inputDTO.getAmendDocuments()))
            reqBody.put("amendDocuments", inputDTO.getAmendDocuments().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAmendMaturityDate()))
            reqBody.put("amendMaturityDate", inputDTO.getAmendMaturityDate());
        if (StringUtils.isNotBlank(inputDTO.getAmendRemittingBank()))
            reqBody.put("amendRemittingBank", inputDTO.getAmendRemittingBank());
        if (StringUtils.isNotBlank(inputDTO.getAmendTenorType()))
            reqBody.put("amendTenorType", inputDTO.getAmendTenorType());
        if (StringUtils.isNotBlank(inputDTO.getAmendUsanceDetails()))
            reqBody.put("amendUsanceDetails", inputDTO.getAmendUsanceDetails());
        if (StringUtils.isNotBlank(inputDTO.getAmendmentNo()))
            reqBody.put("amendmentNo", inputDTO.getAmendmentNo());
        if (StringUtils.isNotBlank(inputDTO.getAmendmentSrmsId()))
            reqBody.put("amendmentSrmsId", inputDTO.getAmendmentSrmsId());
        if (StringUtils.isNotBlank(inputDTO.getAmount()))
            reqBody.put("amount", inputDTO.getAmount());
        if (StringUtils.isNotBlank(inputDTO.getCancellationStatus()))
            reqBody.put("cancellationStatus", inputDTO.getCancellationStatus());
        if (StringUtils.isNotBlank(inputDTO.getCollectionSrmsId()))
            reqBody.put("collectionSrmsId", inputDTO.getCollectionSrmsId());
        if (StringUtils.isNotBlank(inputDTO.getCreatedDate()))
            reqBody.put("createdDate", inputDTO.getCreatedDate());
        if (StringUtils.isNotBlank(inputDTO.getCurrency()))
            reqBody.put("currency", inputDTO.getCurrency());
        if (StringUtils.isNotBlank(inputDTO.getDraweeAcknowledgement()))
            reqBody.put("draweeAcknowledgement", inputDTO.getDraweeAcknowledgement());
        if (StringUtils.isNotBlank(inputDTO.getDraweeAcknowledgementDate()))
            reqBody.put("draweeAcknowledgementDate", inputDTO.getDraweeAcknowledgementDate());
        if (StringUtils.isNotBlank(inputDTO.getDrawer()))
            reqBody.put("drawer", inputDTO.getDrawer());
        if (StringUtils.isNotBlank(inputDTO.getMaturityDate()))
            reqBody.put("maturityDate", inputDTO.getMaturityDate());
        if (StringUtils.isNotBlank(inputDTO.getMessageFromBank()))
            reqBody.put("messageFromBank", inputDTO.getMessageFromBank());
        if (StringUtils.isNotBlank(inputDTO.getMessageToBank()))
            reqBody.put("messageToBank", inputDTO.getMessageToBank());
        if (StringUtils.isNotBlank(inputDTO.getReasonForCancellation()))
            reqBody.put("reasonForCancellation", inputDTO.getReasonForCancellation());
        if (StringUtils.isNotBlank(inputDTO.getReasonForRejection()))
            reqBody.put("reasonForRejection", inputDTO.getReasonForRejection());
        if (StringUtils.isNotBlank(inputDTO.getReceivedOn()))
            reqBody.put("receivedOn", inputDTO.getReceivedOn());
        if (StringUtils.isNotBlank(inputDTO.getRemittingBank()))
            reqBody.put("remittingBank", inputDTO.getRemittingBank());
        if (StringUtils.isNotBlank(inputDTO.getStatus()))
            reqBody.put("status", inputDTO.getStatus());
        if (StringUtils.isNotBlank(inputDTO.getTenorType()))
            reqBody.put("tenorType", inputDTO.getTenorType());
        if (StringUtils.isNotBlank(inputDTO.getTransactionReference()))
            reqBody.put("transactionReference", inputDTO.getTransactionReference());
        reqBody.put("reasonForReturn", inputDTO.getReasonForReturn());

        return reqBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }

}