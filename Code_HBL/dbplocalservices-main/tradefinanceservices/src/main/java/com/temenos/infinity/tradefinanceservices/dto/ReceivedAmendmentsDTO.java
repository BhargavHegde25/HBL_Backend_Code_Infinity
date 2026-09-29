/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_SRMSID;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceivedAmendmentsDTO implements Serializable, DBPDTO {
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String amendmentSrmsId;
    private String guaranteeSrmsId;
    private String status;
    private String amendmentNo;
    private String receivedOn;
    private String applicant;
    private String productType;
    private String lcType;
    private String amount;
    private String currency;
    private String expiryType;
    private String amendmentCharges;
    private String dateOfAmountChange;
    private String amendAmount;
    private String amendExpiryType;
    private String amendExpiryDate;
    private String amendExpiryConditions;
    private String beneficiaryDetails;
    private String otherInstructions;
    private String lastUpdatedTimeStamp;
    private String messageFromBank;
    private String supportingDocuments;
    private String selfAcceptance;
    private String selfAcceptanceDate;
    private String reasonForReturn;
    private String reasonForSelfRejection;
    private String messageToBank;
    private String amountWithCurrency;
    private String otherAmendments;
    private String errorMsg;
    private String errorCode;
    private String dbpErrCode;
    private String dbpErrMsg;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;
    @JsonIgnore
    private String receivedOnFormatted;
    @JsonIgnore
    private String amountFormatted;

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(amount)) ? formatAmount(String.valueOf(amount)) : String.valueOf(amount);
    }

    public String getReceivedOnFormatted() {
        return _formatDate(receivedOn);
    }

    public String getTradeCurrency() {
        return currency;
    }

    public String getServiceRequestSrmsId() {
        return amendmentSrmsId;
    }

    public String getProduct() {
        return "Received GT & SBLC Amendment";
    }

    public String getAmendmentSrmsId() {
        return amendmentSrmsId;
    }

    public void setAmendmentSrmsId(String amendmentSrmsId) {
        this.amendmentSrmsId = amendmentSrmsId;
    }

    public String getGuaranteeSrmsId() {
        return guaranteeSrmsId;
    }

    public void setGuaranteeSrmsId(String guaranteeSrmsId) {
        this.guaranteeSrmsId = guaranteeSrmsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public String getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = receivedOn;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getLcType() {
        return lcType;
    }

    public void setLcType(String lcType) {
        this.lcType = lcType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpiryType() {
        return expiryType;
    }

    public void setExpiryType(String expiryType) {
        this.expiryType = expiryType;
    }

    public String getAmendmentCharges() {
        return amendmentCharges;
    }

    public void setAmendmentCharges(String amendmentCharges) {
        this.amendmentCharges = amendmentCharges;
    }

    public String getDateOfAmountChange() {
        return dateOfAmountChange;
    }

    public void setDateOfAmountChange(String dateOfAmountChange) {
        this.dateOfAmountChange = dateOfAmountChange;
    }

    public String getAmendAmount() {
        return amendAmount;
    }

    public void setAmendAmount(String amendAmount) {
        this.amendAmount = amendAmount;
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

    public String getAmendExpiryConditions() {
        return amendExpiryConditions;
    }

    public void setAmendExpiryConditions(String amendExpiryConditions) {
        this.amendExpiryConditions = amendExpiryConditions;
    }

    public String getBeneficiaryDetails() {
        return beneficiaryDetails;
    }

    public void setBeneficiaryDetails(String beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
    }

    public String getOtherInstructions() {
        return otherInstructions;
    }

    public void setOtherInstructions(String otherInstructions) {
        this.otherInstructions = otherInstructions;
    }

    public String getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    public void setLastUpdatedTimeStamp(String lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }

    public String getMessageFromBank() {
        return messageFromBank;
    }

    public void setMessageFromBank(String messageFromBank) {
        this.messageFromBank = messageFromBank;
    }

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public String getSelfAcceptance() {
        return selfAcceptance;
    }

    public void setSelfAcceptance(String selfAcceptance) {
        this.selfAcceptance = selfAcceptance;
    }

    public String getSelfAcceptanceDate() {
        return selfAcceptanceDate;
    }

    public void setSelfAcceptanceDate(String selfAcceptanceDate) {
        this.selfAcceptanceDate = selfAcceptanceDate;
    }

    public String getReasonForReturn() {
        return reasonForReturn;
    }

    public void setReasonForReturn(String reasonForReturn) {
        this.reasonForReturn = reasonForReturn;
    }

    public String getReasonForSelfRejection() {
        return reasonForSelfRejection;
    }

    public void setReasonForSelfRejection(String reasonForSelfRejection) {
        this.reasonForSelfRejection = reasonForSelfRejection;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getAmountWithCurrency() {
        return TradeFinanceCommonUtils.getAmountWithCurrency(this.getCurrency(), this.getAmount(), false);
    }

    public String getOtherAmendments() {
        return otherAmendments;
    }

    public void setOtherAmendments(String otherAmendments) {
        this.otherAmendments = otherAmendments;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDbpErrCode() {
        return dbpErrCode;
    }

    public void setDbpErrCode(String dbpErrCode) {
        this.dbpErrCode = dbpErrCode;
    }

    public String getDbpErrMsg() {
        return dbpErrMsg;
    }

    public void setDbpErrMsg(String dbpErrMsg) {
        this.dbpErrMsg = dbpErrMsg;
    }
}