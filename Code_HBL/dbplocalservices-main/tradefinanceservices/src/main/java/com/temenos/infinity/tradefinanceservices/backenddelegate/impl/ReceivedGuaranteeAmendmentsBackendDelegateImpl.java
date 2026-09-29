/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ReceivedGuaranteeAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedAmendmentsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERRTF_29071;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class ReceivedGuaranteeAmendmentsBackendDelegateImpl implements ReceivedGuaranteeAmendmentsBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public ReceivedAmendmentsDTO createReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(inputDto).toString().replaceAll("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("ReceivedGuaranteeAmendmentsType", "ReceivedGuaranteeAmendmentsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("ReceivedGuaranteeAmendmentsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setAmendmentSrmsId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            // ERRTF_29091
            alert.prepareError("Error occurred while creating received amendment", responseObject).log();
            inputDto = new ReceivedAmendmentsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public ReceivedAmendmentsDTO updateReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request) {
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
            alert.prepareError("Error occurred while updating received amendment", responseObject).log();
            inputDto = new ReceivedAmendmentsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<ReceivedAmendmentsDTO> getReceivedAmendments(DataControllerRequest request) {
        List amendmentsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentsList = invoke().addDTO(ReceivedAmendmentsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("ReceivedGuaranteeAmendmentsType", "ReceivedGuaranteeAmendmentsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                amendmentsList = getInstance().addDTO(ReceivedAmendmentsDTO.class).addDataControllerRequest(request)
                        .addModule("ReceivedGuaranteeAmendmentsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching received guarantee amendments", e).log();
        }
        return amendmentsList;
    }

    @Override
    public ReceivedAmendmentsDTO getReceivedAmendmentById(String amendmentSrmsId, DataControllerRequest request) {
        ReceivedAmendmentsDTO amendmentDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentDto = (ReceivedAmendmentsDTO) invoke().addDTO(ReceivedAmendmentsDTO.class).addServiceRequestId(amendmentSrmsId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                amendmentDto = (ReceivedAmendmentsDTO) getInstance().addDTO(ReceivedAmendmentsDTO.class).filterByRecordId(amendmentSrmsId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            amendmentDto.setAmendmentSrmsId(amendmentSrmsId);
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching received amendment", e).log();
            amendmentDto.setDbpErrCode(ERRTF_29071.getErrorCodeAsString());
            amendmentDto.setDbpErrMsg(ERRTF_29071.getErrorMessage());
        }
        return amendmentDto;
    }

    private JSONObject constructSRMSParams(ReceivedAmendmentsDTO inputDTO) {
        JSONObject reqBody = new JSONObject();
        if (StringUtils.isNotBlank(inputDTO.getAmendmentSrmsId()))
            reqBody.put("amendmentSrmsId", inputDTO.getAmendmentSrmsId());
        if (StringUtils.isNotBlank(inputDTO.getGuaranteeSrmsId()))
            reqBody.put("guaranteeSrmsId", inputDTO.getGuaranteeSrmsId());
        if (StringUtils.isNotBlank(inputDTO.getStatus()))
            reqBody.put("status", inputDTO.getStatus());
        if (StringUtils.isNotBlank(inputDTO.getAmendmentNo()))
            reqBody.put("amendmentNo", inputDTO.getAmendmentNo());
        if (StringUtils.isNotBlank(inputDTO.getReceivedOn()))
            reqBody.put("receivedOn", inputDTO.getReceivedOn());
        if (StringUtils.isNotBlank(inputDTO.getApplicant()))
            reqBody.put("applicant", inputDTO.getApplicant());
        if (StringUtils.isNotBlank(inputDTO.getProductType()))
            reqBody.put("productType", inputDTO.getProductType());
        if (StringUtils.isNotBlank(inputDTO.getLcType()))
            reqBody.put("lcType", inputDTO.getLcType());
        if (StringUtils.isNotBlank(inputDTO.getAmount()))
            reqBody.put("amount", inputDTO.getAmount());
        if (StringUtils.isNotBlank(inputDTO.getCurrency()))
            reqBody.put("currency", inputDTO.getCurrency());
        if (StringUtils.isNotBlank(inputDTO.getExpiryType()))
            reqBody.put("expiryType", inputDTO.getExpiryType());
        if (StringUtils.isNotBlank(inputDTO.getAmendmentCharges()))
            reqBody.put("amendmentCharges", inputDTO.getAmendmentCharges());
        if (StringUtils.isNotBlank(inputDTO.getDateOfAmountChange()))
            reqBody.put("dateOfAmountChange", inputDTO.getDateOfAmountChange());
        if (StringUtils.isNotBlank(inputDTO.getAmendAmount()))
            reqBody.put("amendAmount", inputDTO.getAmendAmount());
        if (StringUtils.isNotBlank(inputDTO.getAmendExpiryType()))
            reqBody.put("amendExpiryType", inputDTO.getAmendExpiryType());
        if (StringUtils.isNotBlank(inputDTO.getAmendExpiryDate()))
            reqBody.put("amendExpiryDate", inputDTO.getAmendExpiryDate());
        if (StringUtils.isNotBlank(inputDTO.getAmendExpiryConditions()))
            reqBody.put("amendExpiryConditions", inputDTO.getAmendExpiryConditions());
        if (StringUtils.isNotBlank(inputDTO.getBeneficiaryDetails()))
            reqBody.put("beneficiaryDetails", inputDTO.getBeneficiaryDetails().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getOtherAmendments()))
            reqBody.put("otherAmendments", inputDTO.getOtherAmendments().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getOtherInstructions()))
            reqBody.put("otherInstructions", inputDTO.getOtherInstructions());
        if (StringUtils.isNotBlank(inputDTO.getMessageFromBank()))
            reqBody.put("messageFromBank", inputDTO.getMessageFromBank());
        if (StringUtils.isNotBlank(inputDTO.getSupportingDocuments()))
            reqBody.put("supportingDocuments", inputDTO.getSupportingDocuments().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getSelfAcceptance()))
            reqBody.put("selfAcceptance", inputDTO.getSelfAcceptance());
        if (StringUtils.isNotBlank(inputDTO.getSelfAcceptanceDate()))
            reqBody.put("selfAcceptanceDate", inputDTO.getSelfAcceptanceDate());
        if (StringUtils.isNotBlank(inputDTO.getReasonForSelfRejection()))
            reqBody.put("reasonForSelfRejection", inputDTO.getReasonForSelfRejection());
        if (StringUtils.isNotBlank(inputDTO.getMessageToBank()))
            reqBody.put("messageToBank", inputDTO.getMessageToBank());
        if (StringUtils.isNotBlank(inputDTO.getLastUpdatedTimeStamp()))
            reqBody.put("lastUpdatedTimeStamp", inputDTO.getLastUpdatedTimeStamp());
        reqBody.put("reasonForReturn", inputDTO.getReasonForReturn());

        return reqBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}
