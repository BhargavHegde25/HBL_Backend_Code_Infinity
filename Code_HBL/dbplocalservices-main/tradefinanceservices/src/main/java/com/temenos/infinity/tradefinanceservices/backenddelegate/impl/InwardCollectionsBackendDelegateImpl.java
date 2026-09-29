/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.InwardCollectionsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
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

public class InwardCollectionsBackendDelegateImpl implements InwardCollectionsBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public InwardCollectionsDTO createInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(inputDTO).toString().replace("\"", "'");

        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("InwardCollectionsType", "InwardCollectionsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("InwardCollectionsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDTO.setCollectionSrmsId(responseObject.get(PARAM_UNIQUE_ID).toString());
            inputDTO.setTransactionReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDTO = new InwardCollectionsDTO();
            alert.prepareWarn("Unable to create inward collection request order").log();
            // ERRTF_29091 - err message
            inputDTO.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDTO;
    }

    @Override
    public List<InwardCollectionsDTO> getInwardCollections(DataControllerRequest request) {
        List collectionList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                collectionList = invoke().addDTO(InwardCollectionsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("InwardCollectionsType", "InwardCollectionsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                collectionList = getInstance().addDTO(InwardCollectionsDTO.class).addDataControllerRequest(request)
                        .addModule("InwardCollectionsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
            // setCollectionSrmsId
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching inward collections", e).log();
        }
        return collectionList;
    }

    @Override
    public InwardCollectionsDTO getInwardCollectionById(String collectionSrmsId, DataControllerRequest request) {
        InwardCollectionsDTO collectionDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                collectionDto = (InwardCollectionsDTO) invoke().addDTO(InwardCollectionsDTO.class).addServiceRequestId(collectionSrmsId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                collectionDto = (InwardCollectionsDTO) getInstance().addDTO(InwardCollectionsDTO.class).filterByRecordId(collectionSrmsId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            collectionDto.setCollectionSrmsId(collectionSrmsId);
        } catch (Exception e) {
            // ERRTF_29070 / ERRTF_29071
            alert.prepareError("Error occurred while fetching inward collection", e).log();
        }
        return collectionDto;
    }

    @Override
    public InwardCollectionsDTO updateInwardCollection(InwardCollectionsDTO collectionDTO, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(collectionDTO).toString().replace("\"", "'");

        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(collectionDTO.getCollectionSrmsId())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(collectionDTO.getCollectionSrmsId()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            collectionDTO.setCollectionSrmsId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            collectionDTO = new InwardCollectionsDTO();
            // error msg: ERRTF_29092
            collectionDTO.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            collectionDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return collectionDTO;
    }

    private JSONObject constructSRMSParams(InwardCollectionsDTO inputDTO) {
        JSONObject reqBody = new JSONObject();
        if (StringUtils.isNotBlank(inputDTO.getAmendmentDetails()))
            reqBody.put("amendmentDetails", inputDTO.getAmendmentDetails().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAmount()))
            reqBody.put("amount", inputDTO.getAmount());
        if (StringUtils.isNotBlank(inputDTO.getBillExchangeStatus()))
            reqBody.put("billExchangeStatus", inputDTO.getBillExchangeStatus());
        if (StringUtils.isNotBlank(inputDTO.getCharges()))
            reqBody.put("charges", inputDTO.getCharges());
        if (StringUtils.isNotBlank(inputDTO.getChargesDebitFrom()))
            reqBody.put("chargesDebitFrom", inputDTO.getChargesDebitFrom());
        if (StringUtils.isNotBlank(inputDTO.getCreatedDate()))
            reqBody.put("createdDate", inputDTO.getCreatedDate());
        if (StringUtils.isNotBlank(inputDTO.getCurrency()))
            reqBody.put("currency", inputDTO.getCurrency());
        if (StringUtils.isNotBlank(inputDTO.getDebitAmountFrom()))
            reqBody.put("debitAmountFrom", inputDTO.getDebitAmountFrom());
        if (StringUtils.isNotBlank(inputDTO.getDocumentNo()))
            reqBody.put("documentNo", inputDTO.getDocumentNo());
        if (StringUtils.isNotBlank(inputDTO.getDocumentsUploaded()))
            reqBody.put("documentsUploaded", inputDTO.getDocumentsUploaded().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getDraweeAcknowledgement()))
            reqBody.put("draweeAcknowledgement", inputDTO.getDraweeAcknowledgement());
        if (StringUtils.isNotBlank(inputDTO.getDraweeAcknowledgementDate()))
            reqBody.put("draweeAcknowledgementDate", inputDTO.getDraweeAcknowledgementDate());
        if (StringUtils.isNotBlank(inputDTO.getDrawerName()))
            reqBody.put("drawerName", inputDTO.getDrawerName());
        if (StringUtils.isNotBlank(inputDTO.getIncoTerms()))
            reqBody.put("incoTerms", inputDTO.getIncoTerms());
        if (StringUtils.isNotBlank(inputDTO.getLastUpdatedDate()))
            reqBody.put("lastUpdatedDate", inputDTO.getLastUpdatedDate());
        if (StringUtils.isNotBlank(inputDTO.getMaturityDate()))
            reqBody.put("maturityDate", inputDTO.getMaturityDate());
        if (StringUtils.isNotBlank(inputDTO.getMessageFromBank()))
            reqBody.put("messageFromBank", inputDTO.getMessageFromBank());
        if (StringUtils.isNotBlank(inputDTO.getMessageToBank()))
            reqBody.put("messageToBank", inputDTO.getMessageToBank());
        if (StringUtils.isNotBlank(inputDTO.getPaymentStatus()))
            reqBody.put("paymentStatus", inputDTO.getPaymentStatus());
        if (StringUtils.isNotBlank(inputDTO.getReasonForRejection()))
            reqBody.put("reasonForRejection", inputDTO.getReasonForRejection());
        if (StringUtils.isNotBlank(inputDTO.getReasonForReturn()))
            reqBody.put("reasonForReturn", inputDTO.getReasonForReturn());
        if (StringUtils.isNotBlank(inputDTO.getReceivedOn()))
            reqBody.put("receivedOn", inputDTO.getReceivedOn());
        if (StringUtils.isNotBlank(inputDTO.getRemittingBank()))
            reqBody.put("remittingBank", inputDTO.getRemittingBank());
        if (StringUtils.isNotBlank(inputDTO.getSettledDate()))
            reqBody.put("settledDate", inputDTO.getSettledDate());
        if (StringUtils.isNotBlank(inputDTO.getStatus()))
            reqBody.put("status", inputDTO.getStatus());
        if (StringUtils.isNotBlank(inputDTO.getTenorType()))
            reqBody.put("tenorType", inputDTO.getTenorType());
        if (StringUtils.isNotBlank(inputDTO.getTransactionReference()))
            reqBody.put("transactionReference", inputDTO.getTransactionReference());
        if (StringUtils.isNotBlank(inputDTO.getUsanceAcceptance()))
            reqBody.put("usanceAcceptance", inputDTO.getUsanceAcceptance());
        if (StringUtils.isNotBlank(inputDTO.getUsanceAcceptanceDate()))
            reqBody.put("usanceAcceptanceDate", inputDTO.getUsanceAcceptanceDate());
        if (StringUtils.isNotBlank(inputDTO.getUsanceAcceptanceEligibility()))
            reqBody.put("usanceAcceptanceEligibility", inputDTO.getUsanceAcceptanceEligibility());
        if (StringUtils.isNotBlank(inputDTO.getUsanceDetails()))
            reqBody.put("usanceDetails", inputDTO.getUsanceDetails());
        if (StringUtils.isNotBlank(inputDTO.getRejectedDate()))
            reqBody.put("rejectedDate", inputDTO.getRejectedDate());
        if (StringUtils.isNotBlank(inputDTO.getDocumentsAgainstAcceptance()))
            reqBody.put("documentsAgainstAcceptance", inputDTO.getDocumentsAgainstAcceptance());

        return reqBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}
