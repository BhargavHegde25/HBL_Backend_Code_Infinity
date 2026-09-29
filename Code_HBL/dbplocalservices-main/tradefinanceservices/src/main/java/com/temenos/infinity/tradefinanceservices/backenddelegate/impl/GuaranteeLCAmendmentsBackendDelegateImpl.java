/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GuaranteeLCAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteeLCAmendmentsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class GuaranteeLCAmendmentsBackendDelegateImpl implements GuaranteeLCAmendmentsBackendDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public List<GuaranteeLCAmendmentsDTO> getGuaranteeLCAmendments(DataControllerRequest request) {
        List amendmentsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentsList = invoke().addDTO(GuaranteeLCAmendmentsDTO.class).addDataControllerRequest(request).
                        addTypeAndSubType("GuaranteeLetterOfCreditAmendmentsType", "GuaranteeLetterOfCreditAmendmentsSubType")
                        .getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                amendmentsList = getInstance().addDTO(GuaranteeLCAmendmentsDTO.class).addDataControllerRequest(request)
                        .addModule("GuaranteeLetterOfCreditAmendmentsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }

/* TODO: change in dto
            amendmentsDTO.setAmendmentSRMSRequestId(singleOrder.getString("serviceReqId"));
            amendmentsDTO.setAmendmentReference(singleOrder.getString("serviceReqId"));
            amendmentsDTO.setAmountWithCurrency(getAmountWithCurrency(amendmentsDTO.getCurrency(), amendmentsDTO.getAmount(), false));
            amendmentsDTO.setAmendRequestedDateFormatted(amendmentsDTO.getAmendRequestedDate().substring(0, 10));
 */
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching issued guarantee amendments").log();
        }
        return amendmentsList;
    }

    @Override
    public GuaranteeLCAmendmentsDTO createGuaranteeLCAmendment(GuaranteeLCAmendmentsDTO amendmentDTO, DataControllerRequest request) {
        JSONObject reqBody = constructSRMSReqBody(amendmentDTO, new JSONObject());
        String requestBody = reqBody.toString().replaceAll("\"", "'");
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request).
                    addTypeAndSubType("GuaranteeLetterOfCreditAmendmentsType", "GuaranteeLetterOfCreditAmendmentsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(reqBody)
                    .addModule("GuaranteeLetterOfCreditAmendmentsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            amendmentDTO.setAmendmentSRMSRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
            amendmentDTO.setAmendmentReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            amendmentDTO = new GuaranteeLCAmendmentsDTO();
            amendmentDTO.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            amendmentDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            alert.prepareError("Failed to create amendment request order. Error Code" + responseObject.getString(PARAM_DBP_ERR_MSG)).log();
        }
        return amendmentDTO;
    }

    @Override
    public GuaranteeLCAmendmentsDTO getGuaranteeLCAmendmentById(String amendmentSRMSId, DataControllerRequest request) {
        GuaranteeLCAmendmentsDTO amendmentDTO = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentDTO = (GuaranteeLCAmendmentsDTO) invoke().addDTO(GuaranteeLCAmendmentsDTO.class).addServiceRequestId(amendmentSRMSId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                amendmentDTO = (GuaranteeLCAmendmentsDTO) getInstance().addDTO(GuaranteeLCAmendmentsDTO.class).filterByRecordId(amendmentSRMSId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching guarantees").log();
        }
        return amendmentDTO;
    }

    public GuaranteeLCAmendmentsDTO updateGuaranteeAmendment(GuaranteeLCAmendmentsDTO amendmentDTO, JSONObject inputObj, DataControllerRequest request) {
        JSONObject reqBody = constructSRMSReqBody(amendmentDTO, inputObj);
        String requestBody = reqBody.toString().replaceAll("\"", "'");

        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(amendmentDTO.getAmendmentSRMSRequestId())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(amendmentDTO.getAmendmentSRMSRequestId()).addRequestBody(reqBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            amendmentDTO.setAmendmentSRMSRequestId(responseObject.getString(PARAM_UNIQUE_ID));
        } else {
            amendmentDTO = new GuaranteeLCAmendmentsDTO();
            amendmentDTO.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            amendmentDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return amendmentDTO;
    }

    private JSONObject constructSRMSReqBody(GuaranteeLCAmendmentsDTO inputDTO, JSONObject reqBody) {
        if (StringUtils.isNotEmpty(inputDTO.getBenificiaryName()))
            reqBody.put("benificiaryName", inputDTO.getBenificiaryName());
        if (StringUtils.isNotBlank(inputDTO.getBeneficiaryDetails()))
            reqBody.put("beneficiaryDetails", inputDTO.getBeneficiaryDetails().replaceAll("\'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getGuaranteesReference()))
            reqBody.put("guaranteesReference", inputDTO.getGuaranteesReference());
        if (StringUtils.isNotEmpty(inputDTO.getCurrency()))
            reqBody.put("currency", inputDTO.getCurrency());
        if (StringUtils.isNotEmpty(inputDTO.getAmount()))
            reqBody.put("amount", inputDTO.getAmount());
        if (StringUtils.isNotEmpty(inputDTO.getExpiryDate()))
            reqBody.put("expiryDate", inputDTO.getExpiryDate());
        if (StringUtils.isNotEmpty(inputDTO.getProductType()))
            reqBody.put("productType", inputDTO.getProductType());
        if (StringUtils.isNotEmpty(inputDTO.getIssueDate()))
            reqBody.put("issueDate", inputDTO.getIssueDate());
        if (StringUtils.isNotEmpty(inputDTO.getInstructingParty()))
            reqBody.put("instructingParty", inputDTO.getInstructingParty().replaceAll("\'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getBillType()))
            reqBody.put("billType", inputDTO.getBillType());
        if (StringUtils.isNotEmpty(inputDTO.getExpiryType()))
            reqBody.put("expiryType", inputDTO.getExpiryType());
        if (StringUtils.isNotEmpty(inputDTO.getApplicantParty()))
            reqBody.put("applicantParty", inputDTO.getApplicantParty().replaceAll("\'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentNo()))
            reqBody.put("amendmentNo", inputDTO.getAmendmentNo().replaceAll("\'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentEffectiveDate()))
            reqBody.put("amendmentEffectiveDate", inputDTO.getAmendmentEffectiveDate());
        if (StringUtils.isNotEmpty(inputDTO.getAmendAmount()))
            reqBody.put("amendAmount", inputDTO.getAmendAmount());
        if (StringUtils.isNotEmpty(inputDTO.getAmendCharges()))
            reqBody.put("amendCharges", inputDTO.getAmendCharges());
        if (StringUtils.isNotEmpty(inputDTO.getAmendExpiryType()))
            reqBody.put("amendExpiryType", inputDTO.getAmendExpiryType());
        if (StringUtils.isNotEmpty(inputDTO.getAmendExpiryDate()))
            reqBody.put("amendExpiryDate", inputDTO.getAmendExpiryDate());
        if (StringUtils.isNotEmpty(inputDTO.getAmendExpiryCondition()))
            reqBody.put("amendExpiryCondition", inputDTO.getAmendExpiryCondition());
        if (StringUtils.isNotEmpty(inputDTO.getAmendDetails()))
            reqBody.put("amendDetails", inputDTO.getAmendDetails());
        if (StringUtils.isNotEmpty(inputDTO.getMessageToBank()))
            reqBody.put("messageToBank", inputDTO.getMessageToBank());
        if (StringUtils.isNotEmpty(inputDTO.getAmendStatus()))
            reqBody.put("amendStatus", inputDTO.getAmendStatus());
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentReference()))
            reqBody.put("amendmentReference", inputDTO.getAmendmentReference());
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentSRMSRequestId()))
            reqBody.put("amendmentSRMSRequestId", inputDTO.getAmendmentSRMSRequestId());
        if (StringUtils.isNotEmpty(inputDTO.getCancellationStatus()))
            reqBody.put("cancellationStatus", inputDTO.getCancellationStatus());
        if (StringUtils.isNotEmpty(inputDTO.getApprovedDate()))
            reqBody.put("approvedDate", inputDTO.getApprovedDate());
        if (StringUtils.isNotEmpty(inputDTO.getReasonForReturned()))
            reqBody.put("reasonForReturned", inputDTO.getReasonForReturned());
        if (StringUtils.isNotEmpty(inputDTO.getReturnMessage()))
            reqBody.put("returnMessage", inputDTO.getReturnMessage());
        if (StringUtils.isNotEmpty(inputDTO.getCorporateUserName()))
            reqBody.put("corporateUserName", inputDTO.getCorporateUserName());
        if (StringUtils.isNotEmpty(inputDTO.getSupportingDocument()))
            reqBody.put("supportingDocument", inputDTO.getSupportingDocument().replaceAll("\'", "\""));
        if (StringUtils.isNotEmpty(inputDTO.getRejectedReason()))
            reqBody.put("rejectedReason", inputDTO.getRejectedReason());
        if (StringUtils.isNotEmpty(inputDTO.getRejectedDate()))
            reqBody.put("rejectedDate", inputDTO.getRejectedDate());
        if (StringUtils.isNotEmpty(inputDTO.getGuaranteesSRMSId()))
            reqBody.put("guaranteesSRMSId", inputDTO.getGuaranteesSRMSId());
        if (StringUtils.isNotEmpty(inputDTO.getAmendRequestedDate()))
            reqBody.put("amendRequestedDate", inputDTO.getAmendRequestedDate());
        if (StringUtils.isNotEmpty(inputDTO.getHistoryCount()))
            reqBody.put("historyCount", inputDTO.getHistoryCount());
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentHistory1()))
            reqBody.put("amendmentHistory1", new JSONObject(inputDTO.getAmendmentHistory1()).toString());
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentHistory2()))
            reqBody.put("amendmentHistory2", new JSONObject(inputDTO.getAmendmentHistory2()).toString());
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentHistory3()))
            reqBody.put("amendmentHistory3", new JSONObject(inputDTO.getAmendmentHistory3()).toString());
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentHistory4()))
            reqBody.put("amendmentHistory4", new JSONObject(inputDTO.getAmendmentHistory4()).toString());
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentHistory5()))
            reqBody.put("amendmentHistory5", new JSONObject(inputDTO.getAmendmentHistory5()).toString());
        return reqBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}