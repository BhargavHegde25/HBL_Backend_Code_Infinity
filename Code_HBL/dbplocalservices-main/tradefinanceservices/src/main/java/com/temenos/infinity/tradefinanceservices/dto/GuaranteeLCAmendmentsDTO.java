/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;

import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GuaranteeLCAmendmentsDTO implements DBPDTO {
    private String benificiaryName;
    private String beneficiaryDetails;
    private String guaranteesReference;
    private String currency;
    private String amount;
    private String expiryDate;
    private String productType;
    private String issueDate;
    private String instructingParty;
    private String billType;
    private String expiryType;
    private String applicantParty;
    private String amendmentNo;
    private String amendmentEffectiveDate;
    private String amendAmount;
    private String amendCharges;
    private String amendExpiryType;
    private String amendExpiryDate;
    private String amendExpiryCondition;
    private String amendDetails;
    private String messageToBank;
    private String amendStatus;
    @JsonAlias({PARAM_REQUEST_DATE_TIME})
    private String amendRequestedDate;
    private String amendmentReference;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String amendmentSRMSRequestId;
    private String cancellationStatus;
    private String approvedDate;
    private String reasonForReturned;
    private String returnMessage;
    private String corporateUserName;
    private String supportingDocument;
    private String rejectedReason;
    private String rejectedDate;
    private String guaranteesSRMSId;
    private String dbpErrMsg;
    private String dbpErrCode;
    private String historyCount;
    private String amendmentHistory1;
    private String amendmentHistory2;
    private String amendmentHistory3;
    private String amendmentHistory4;
    private String amendmentHistory5;
    private String amountWithCurrency;
    private String amendRequestedDateFormatted;
    private String relatedTransactionReference;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;
    @JsonIgnore
    private String createdOnFormatted;
    @JsonIgnore
    private String amendEffectiveDateFormatted;
    private String errorDetails;

    public String getAmendEffectiveDateFormatted() {
        return _formatDate(amendmentEffectiveDate);
    }

    public String getCreatedOnFormatted() {
        return _formatDate(amendRequestedDate);
    }

    public String getTradeCurrency() {
        return currency;
    }

    public String getServiceRequestSrmsId() {
        return amendmentSRMSRequestId;
    }

    public String getProduct() {
        return "Issued GT & SBLC Amendment";
    }

    public String getBenificiaryName() {
        return benificiaryName;
    }

    public void setBenificiaryName(String benificiaryName) {
        this.benificiaryName = benificiaryName;
    }

    public String getBeneficiaryDetails() {
        return beneficiaryDetails;
    }

    public void setBeneficiaryDetails(String beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
    }

    public String getGuaranteesReference() {
        return guaranteesReference;
    }

    public void setGuaranteesReference(String guaranteesReference) {
        this.guaranteesReference = guaranteesReference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getInstructingParty() {
        return instructingParty;
    }

    public void setInstructingParty(String instructingParty) {
        this.instructingParty = instructingParty;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getExpiryType() {
        return expiryType;
    }

    public void setExpiryType(String expiryType) {
        this.expiryType = expiryType;
    }

    public String getApplicantParty() {
        return applicantParty;
    }

    public void setApplicantParty(String applicantParty) {
        this.applicantParty = applicantParty;
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public String getAmendmentEffectiveDate() {
        return amendmentEffectiveDate;
    }

    public void setAmendmentEffectiveDate(String amendmentEffectiveDate) {
        this.amendmentEffectiveDate = amendmentEffectiveDate;
    }

    public String getAmendAmount() {
        return amendAmount;
    }

    public void setAmendAmount(String amendAmount) {
        this.amendAmount = amendAmount;
    }

    public String getAmendCharges() {
        return amendCharges;
    }

    public void setAmendCharges(String amendCharges) {
        this.amendCharges = amendCharges;
    }

    public String getAmendExpiryType() {
        return amendExpiryType;
    }

    public void setAmendExpiryType(String amendExpiryType) {
        this.amendExpiryType = amendExpiryType;
    }

    public String getAmendExpiryDate() {
        return amendExpiryDate;
    }

    public void setAmendExpiryDate(String amendExpiryDate) {
        this.amendExpiryDate = amendExpiryDate;
    }

    public String getAmendExpiryCondition() {
        return amendExpiryCondition;
    }

    public void setAmendExpiryCondition(String amendExpiryCondition) {
        this.amendExpiryCondition = amendExpiryCondition;
    }

    public String getAmendDetails() {
        return amendDetails;
    }

    public void setAmendDetails(String amendDetails) {
        this.amendDetails = amendDetails;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getAmendStatus() {
        return amendStatus;
    }

    public void setAmendStatus(String amendStatus) {
        this.amendStatus = amendStatus;
    }

    public String getAmendRequestedDate() {
        return amendRequestedDate;
    }

    public void setAmendRequestedDate(String amendRequestedDate) {
        this.amendRequestedDate = amendRequestedDate;
    }

    public String getAmendmentReference() {
        return amendmentReference;
    }

    public void setAmendmentReference(String amendmentReference) {
        this.amendmentReference = amendmentReference;
        this.amendmentSRMSRequestId = amendmentReference;
    }

    public String getAmendmentSRMSRequestId() {
        return amendmentSRMSRequestId;
    }

    public void setAmendmentSRMSRequestId(String amendmentSRMSRequestId) {
        this.amendmentSRMSRequestId = amendmentSRMSRequestId;
    }

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getReasonForReturned() {
        return reasonForReturned;
    }

    public void setReasonForReturned(String reasonForReturned) {
        this.reasonForReturned = reasonForReturned;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getCorporateUserName() {
        return corporateUserName;
    }

    public void setCorporateUserName(String corporateUserName) {
        this.corporateUserName = corporateUserName;
    }

    public String getSupportingDocument() {
        return supportingDocument;
    }

    public void setSupportingDocument(String supportingDocument) {
        this.supportingDocument = supportingDocument;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(String rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getGuaranteesSRMSId() {
        return guaranteesSRMSId;
    }

    public void setGuaranteesSRMSId(String guaranteesSRMSId) {
        this.guaranteesSRMSId = guaranteesSRMSId;
    }

    public String getDbpErrMsg() {
        return dbpErrMsg;
    }

    public void setDbpErrMsg(String dbpErrMsg) {
        this.dbpErrMsg = dbpErrMsg;
    }

    public String getDbpErrCode() {
        return dbpErrCode;
    }

    public void setDbpErrCode(String dbpErrCode) {
        this.dbpErrCode = dbpErrCode;
    }

    public String getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(String historyCount) {
        this.historyCount = historyCount;
    }

    public String getAmendmentHistory1() {
        return amendmentHistory1;
    }

    public void setAmendmentHistory1(String amendmentHistory1) {
        this.amendmentHistory1 = amendmentHistory1;
    }

    public String getAmendmentHistory2() {
        return amendmentHistory2;
    }

    public void setAmendmentHistory2(String amendmentHistory2) {
        this.amendmentHistory2 = amendmentHistory2;
    }

    public String getAmendmentHistory3() {
        return amendmentHistory3;
    }

    public void setAmendmentHistory3(String amendmentHistory3) {
        this.amendmentHistory3 = amendmentHistory3;
    }

    public String getAmendmentHistory4() {
        return amendmentHistory4;
    }

    public void setAmendmentHistory4(String amendmentHistory4) {
        this.amendmentHistory4 = amendmentHistory4;
    }

    public String getAmendmentHistory5() {
        return amendmentHistory5;
    }

    public void setAmendmentHistory5(String amendmentHistory5) {
        this.amendmentHistory5 = amendmentHistory5;
    }

    public String getAmountWithCurrency() {
        return amountWithCurrency;
    }

    public void setAmountWithCurrency(String amountWithCurrency) {
        this.amountWithCurrency = amountWithCurrency;
    }

    public String getAmendRequestedDateFormatted() {
        return amendRequestedDateFormatted;
    }

    public void setAmendRequestedDateFormatted(String amendRequestedDateFormatted) {
        this.amendRequestedDateFormatted = amendRequestedDateFormatted;
    }

    public String getRelatedTransactionReference() {
        return relatedTransactionReference;
    }

    public void setRelatedTransactionReference(String relatedTransactionReference) {
        this.relatedTransactionReference = relatedTransactionReference;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public GuaranteeLCAmendmentsDTO swap(GuaranteeLCAmendmentsDTO inputDTO) {
        if (StringUtils.isNotEmpty(inputDTO.getBenificiaryName()))
            this.benificiaryName = inputDTO.getBenificiaryName();
        if (StringUtils.isNotEmpty(inputDTO.getBeneficiaryDetails()))
            this.beneficiaryDetails = inputDTO.getBeneficiaryDetails();
        if (StringUtils.isNotEmpty(inputDTO.getGuaranteesReference()))
            this.guaranteesReference = inputDTO.getGuaranteesReference();
        if (StringUtils.isNotEmpty(inputDTO.getCurrency()))
            this.currency = inputDTO.getCurrency();
        if (StringUtils.isNotEmpty(inputDTO.getAmount()))
            this.amount = inputDTO.getAmount();
        if (StringUtils.isNotEmpty(inputDTO.getExpiryDate()))
            this.expiryDate = inputDTO.getExpiryDate();
        if (StringUtils.isNotEmpty(inputDTO.getProductType()))
            this.productType = inputDTO.getProductType();
        if (StringUtils.isNotEmpty(inputDTO.getIssueDate()))
            this.issueDate = inputDTO.getIssueDate();
        if (StringUtils.isNotEmpty(inputDTO.getInstructingParty()))
            this.instructingParty = inputDTO.getInstructingParty();
        if (StringUtils.isNotEmpty(inputDTO.getBillType()))
            this.billType = inputDTO.getBillType();
        if (StringUtils.isNotEmpty(inputDTO.getExpiryType()))
            this.expiryType = inputDTO.getExpiryType();
        if (StringUtils.isNotEmpty(inputDTO.getApplicantParty()))
            this.applicantParty = inputDTO.getApplicantParty();
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentNo()))
            this.amendmentNo = inputDTO.getAmendmentNo();
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentEffectiveDate()))
            this.amendmentEffectiveDate = inputDTO.getAmendmentEffectiveDate();
        if (StringUtils.isNotEmpty(inputDTO.getAmendAmount()))
            this.amendAmount = inputDTO.getAmendAmount();
        if (StringUtils.isNotEmpty(inputDTO.getAmendCharges()))
            this.amendCharges = inputDTO.getAmendCharges();
        if (StringUtils.isNotEmpty(inputDTO.getAmendExpiryType()))
            this.amendExpiryType = inputDTO.getAmendExpiryType();
        if (StringUtils.isNotEmpty(inputDTO.getAmendExpiryDate()))
            this.amendExpiryDate = inputDTO.getAmendExpiryDate();
        if (StringUtils.isNotEmpty(inputDTO.getAmendExpiryCondition()))
            this.amendExpiryCondition = inputDTO.getAmendExpiryCondition();
        if (StringUtils.isNotEmpty(inputDTO.getAmendDetails()))
            this.amendDetails = inputDTO.getAmendDetails();
        if (StringUtils.isNotEmpty(inputDTO.getMessageToBank()))
            this.messageToBank = inputDTO.getMessageToBank();
        if (StringUtils.isNotEmpty(inputDTO.getAmendStatus()))
            this.amendStatus = inputDTO.getAmendStatus();
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentReference()))
            this.amendmentReference = inputDTO.getAmendmentReference();
        if (StringUtils.isNotEmpty(inputDTO.getAmendmentSRMSRequestId()))
            this.amendmentSRMSRequestId = inputDTO.getAmendmentSRMSRequestId();
        if (StringUtils.isNotEmpty(inputDTO.getCancellationStatus()))
            this.cancellationStatus = inputDTO.getCancellationStatus();
        if (StringUtils.isNotEmpty(inputDTO.getApprovedDate()))
            this.approvedDate = inputDTO.getApprovedDate();
        if (StringUtils.isNotEmpty(inputDTO.getReasonForReturned()))
            this.reasonForReturned = inputDTO.getReasonForReturned();
        if (StringUtils.isNotEmpty(inputDTO.getReturnMessage()))
            this.returnMessage = inputDTO.getReturnMessage();
        if (StringUtils.isNotEmpty(inputDTO.getCorporateUserName()))
            this.corporateUserName = inputDTO.getCorporateUserName();
        if (StringUtils.isNotEmpty(inputDTO.getSupportingDocument()))
            this.supportingDocument = inputDTO.getSupportingDocument();
        if (StringUtils.isNotEmpty(inputDTO.getRejectedReason()))
            this.rejectedReason = inputDTO.getRejectedReason();
        if (StringUtils.isNotEmpty(inputDTO.getRejectedDate()))
            this.rejectedDate = inputDTO.getRejectedDate();
        if (StringUtils.isNotEmpty(inputDTO.getGuaranteesSRMSId()))
            this.guaranteesSRMSId = inputDTO.getGuaranteesSRMSId();
        if (StringUtils.isNotEmpty(inputDTO.getAmendRequestedDate()))
            this.amendRequestedDate = inputDTO.getAmendRequestedDate();
        if (StringUtils.isNotEmpty(inputDTO.getHistoryCount()))
            this.historyCount = inputDTO.getHistoryCount();
        return this;
    }

}