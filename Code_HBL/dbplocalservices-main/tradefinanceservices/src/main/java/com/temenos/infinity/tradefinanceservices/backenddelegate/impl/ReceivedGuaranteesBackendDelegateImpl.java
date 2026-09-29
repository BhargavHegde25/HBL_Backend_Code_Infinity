/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ReceivedGuaranteesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERRTF_29071;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class ReceivedGuaranteesBackendDelegateImpl implements ReceivedGuaranteesBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public ReceivedGuaranteesDTO createReceivedGuarantee(ReceivedGuaranteesDTO inputDto, DataControllerRequest request) {
        String requestBody = constructSRMSParams(inputDto).toString().replaceAll("\"", "'");

        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("ReceivedGuaranteesType", "ReceivedGuaranteesSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("ReceivedGuaranteesModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setGuaranteeSrmsId(responseObject.get(PARAM_UNIQUE_ID).toString());
            inputDto.setTransactionReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            // ERRTF_29091
            alert.prepareError("Unable to create guarantee request order ", responseObject).log();
            inputDto = new ReceivedGuaranteesDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<ReceivedGuaranteesDTO> getReceivedGuarantees(DataControllerRequest request) {
        List guaranteesList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                guaranteesList = invoke().addDTO(ReceivedGuaranteesDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("ReceivedGuaranteesType", "ReceivedGuaranteesSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                guaranteesList = getInstance().addDTO(ReceivedGuaranteesDTO.class).addDataControllerRequest(request)
                        .addModule("ReceivedGuaranteesModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching received guarantees", e).log();
        }
        return guaranteesList;
    }

    @Override
    public ReceivedGuaranteesDTO getReceivedGuaranteeById(String guaranteeSrmsId, DataControllerRequest request) {
        ReceivedGuaranteesDTO guaranteeDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                guaranteeDto = (ReceivedGuaranteesDTO) invoke().addDTO(ReceivedGuaranteesDTO.class).addServiceRequestId(guaranteeSrmsId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                guaranteeDto = (ReceivedGuaranteesDTO) getInstance().addDTO(ReceivedGuaranteesDTO.class).filterByRecordId(guaranteeSrmsId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            guaranteeDto.setGuaranteeSrmsId(guaranteeSrmsId);
            guaranteeDto.setTransactionReference(guaranteeSrmsId);
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching received collection", e).log();
            guaranteeDto.setDbpErrCode(ERRTF_29071.getErrorCodeAsString());
            guaranteeDto.setDbpErrMsg(ERRTF_29071.getErrorMessage());
            guaranteeDto.setErrorMsg(e.getMessage());
        }
        return guaranteeDto;
    }

    @Override
    public ReceivedGuaranteesDTO updateReceivedGuarantee(ReceivedGuaranteesDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructSRMSParams(inputDto).toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getGuaranteeSrmsId())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getGuaranteeSrmsId()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setGuaranteeSrmsId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            // ERRTF_29092
            alert.prepareError("Error occurred while updating received guarantee", responseObject).log();
            inputDto = new ReceivedGuaranteesDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    private JSONObject constructSRMSParams(ReceivedGuaranteesDTO inputDTO) {
        JSONObject reqBody = new JSONObject();
        if (StringUtils.isNotBlank(inputDTO.getGuaranteeSrmsId()))
            reqBody.put("guaranteeSrmsId", inputDTO.getGuaranteeSrmsId());
        if (StringUtils.isNotBlank(inputDTO.getTransactionReference()))
            reqBody.put("transactionReference", inputDTO.getTransactionReference());
        if (StringUtils.isNotBlank(inputDTO.getReceivedOn()))
            reqBody.put("receivedOn", inputDTO.getReceivedOn());
        if (StringUtils.isNotBlank(inputDTO.getStatus()))
            reqBody.put("status", inputDTO.getStatus());
        if (StringUtils.isNotBlank(inputDTO.getProductType()))
            reqBody.put("productType", inputDTO.getProductType());
        if (StringUtils.isNotBlank(inputDTO.getLcType()))
            reqBody.put("lcType", inputDTO.getLcType());
        if (StringUtils.isNotBlank(inputDTO.getRelatedTransactionReference()))
            reqBody.put("relatedTransactionReference", inputDTO.getRelatedTransactionReference());
        if (StringUtils.isNotBlank(inputDTO.getModeOfTransaction()))
            reqBody.put("modeOfTransaction", inputDTO.getModeOfTransaction());
        if (StringUtils.isNotBlank(inputDTO.getApplicantParty()))
            reqBody.put("applicantParty", inputDTO.getApplicantParty());
        if (StringUtils.isNotBlank(inputDTO.getBeneficiaryDetails()))
            reqBody.put("beneficiaryDetails", inputDTO.getBeneficiaryDetails().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAmount()))
            reqBody.put("amount", inputDTO.getAmount());
        if (StringUtils.isNotBlank(inputDTO.getCurrency()))
            reqBody.put("currency", inputDTO.getCurrency());
        if (StringUtils.isNotBlank(inputDTO.getExpiryType()))
            reqBody.put("expiryType", inputDTO.getExpiryType());
        if (StringUtils.isNotBlank(inputDTO.getExpiryDate()))
            reqBody.put("expiryDate", inputDTO.getExpiryDate());
        if (StringUtils.isNotBlank(inputDTO.getExpiryConditions()))
            reqBody.put("expiryConditions", inputDTO.getExpiryConditions());
        if (StringUtils.isNotBlank(inputDTO.getExpectedIssueDate()))
            reqBody.put("expectedIssueDate", inputDTO.getExpectedIssueDate());
        if (StringUtils.isNotBlank(inputDTO.getAutoExtensionExpiry()))
            reqBody.put("autoExtensionExpiry", inputDTO.getAutoExtensionExpiry());
        if (StringUtils.isNotBlank(inputDTO.getExtensionPeriod()))
            reqBody.put("extensionPeriod", inputDTO.getExtensionPeriod());
        if (StringUtils.isNotBlank(inputDTO.getExtensionCapPeriod()))
            reqBody.put("extensionCapPeriod", inputDTO.getExtensionCapPeriod());
        if (StringUtils.isNotBlank(inputDTO.getNotificationPeriod()))
            reqBody.put("notificationPeriod", inputDTO.getNotificationPeriod());
        if (StringUtils.isNotBlank(inputDTO.getExtensionDetails()))
            reqBody.put("extensionDetails", inputDTO.getExtensionDetails());
        if (StringUtils.isNotBlank(inputDTO.getApplicableRules()))
            reqBody.put("applicableRules", inputDTO.getApplicableRules());
        if (StringUtils.isNotBlank(inputDTO.getGoverningLaw()))
            reqBody.put("governingLaw", inputDTO.getGoverningLaw());
        if (StringUtils.isNotBlank(inputDTO.getApplicableRules()))
            reqBody.put("applicableRules", inputDTO.getApplicableRules());
        if (StringUtils.isNotBlank(inputDTO.getDeliveryInstructions()))
            reqBody.put("deliveryInstructions", inputDTO.getDeliveryInstructions());
        if (StringUtils.isNotBlank(inputDTO.getOtherInstructions()))
            reqBody.put("otherInstructions", inputDTO.getOtherInstructions());
        if (StringUtils.isNotBlank(inputDTO.getApplicantName()))
            reqBody.put("applicantName", inputDTO.getApplicantName());
        if (StringUtils.isNotBlank(inputDTO.getApplicantAddress()))
            reqBody.put("applicantAddress", inputDTO.getApplicantAddress().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getIssuingBankName()))
            reqBody.put("issuingBankName", inputDTO.getIssuingBankName());
        if (StringUtils.isNotBlank(inputDTO.getIssuingBankSwiftBicCode()))
            reqBody.put("issuingBankSwiftBicCode", inputDTO.getIssuingBankSwiftBicCode());
        if (StringUtils.isNotBlank(inputDTO.getIssuingBankIban()))
            reqBody.put("issuingBankIban", inputDTO.getIssuingBankIban());
        if (StringUtils.isNotBlank(inputDTO.getIssuingBankLocalCode()))
            reqBody.put("issuingBankLocalCode", inputDTO.getIssuingBankLocalCode());
        if (StringUtils.isNotBlank(inputDTO.getIssuingBankAddress()))
            reqBody.put("issuingBankAddress", inputDTO.getIssuingBankAddress().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getMessageFromBank()))
            reqBody.put("messageFromBank", inputDTO.getMessageFromBank());
        if (StringUtils.isNotBlank(inputDTO.getUploadedDocuments()))
            reqBody.put("uploadedDocuments", inputDTO.getUploadedDocuments().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getSelfAcceptance()))
            reqBody.put("selfAcceptance", inputDTO.getSelfAcceptance());
        if (StringUtils.isNotBlank(inputDTO.getSelfAcceptanceDate()))
            reqBody.put("selfAcceptanceDate", inputDTO.getSelfAcceptanceDate());
        if (StringUtils.isNotBlank(inputDTO.getSelfRejectionHistory()))
            reqBody.put("selfRejectionHistory", inputDTO.getSelfRejectionHistory().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getClausesAndConditions()))
            reqBody.put("clausesAndConditions", inputDTO.getClausesAndConditions().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getLastAmendmentDetails()))
            reqBody.put("lastAmendmentDetails", inputDTO.getLastAmendmentDetails().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getClaimInformation()))
            reqBody.put("claimInformation", inputDTO.getClaimInformation().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getUtilizedAmount()))
            reqBody.put("utilizedAmount", inputDTO.getUtilizedAmount());
        if (StringUtils.isNotBlank(inputDTO.getReleasedAmount()))
            reqBody.put("releasedAmount", inputDTO.getReleasedAmount());
        if (StringUtils.isNotBlank(inputDTO.getLiabilityDetails()))
            reqBody.put("liabilityDetails", inputDTO.getLiabilityDetails().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getDemandAcceptance()))
            reqBody.put("demandAcceptance", inputDTO.getDemandAcceptance());
        reqBody.put("reasonForSelfRejection", inputDTO.getReasonForSelfRejection());
        reqBody.put("messageToBank", inputDTO.getMessageToBank());
        return reqBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}