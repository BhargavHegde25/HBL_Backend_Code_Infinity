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
public class ReceivedGuaranteesDTO implements Serializable, DBPDTO {
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String guaranteeSrmsId;
    private String transactionReference;
    private String receivedOn;
    private String status;
    private String productType;
    private String lcType;
    private String relatedTransactionReference;
    private String modeOfTransaction;
    private String applicantParty;
    private String beneficiaryDetails;
    private String amount;
    private String currency;
    private String expiryType;
    private String expiryDate;
    private String expiryConditions;
    private String expectedIssueDate;
    private String autoExtensionExpiry;
    private String extensionPeriod;
    private String extensionCapPeriod;
    private String notificationPeriod;
    private String extensionDetails;
    private String applicableRules;
    private String governingLaw;
    private String deliveryInstructions;
    private String otherInstructions;
    private String applicantName;
    private String applicantAddress;
    private String issuingBankName;
    private String issuingBankSwiftBicCode;
    private String issuingBankIban;
    private String issuingBankLocalCode;
    private String issuingBankAddress;
    private String messageFromBank;
    private String messageToBank;
    private String uploadedDocuments;
    private String selfAcceptance;
    private String selfAcceptanceDate;
    private String reasonForSelfRejection;
    private String selfRejectionHistory;
    private String clausesAndConditions;
    private String amountWithCurrency;
    private String lastAmendmentDetails;
    private String claimInformation;
    private String utilizedAmount;
    private String liabilityDetails;
    private String releasedAmount;
    private String demandAcceptance;
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
    @JsonIgnore
    private String expectedIssuedOnFormatted;

    public String getExpectedIssuedOnFormatted() {
        return _formatDate(expectedIssueDate);
    }

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
        return guaranteeSrmsId;
    }

    public String getProduct() {
        return "Received GT & SBLC";
    }

    public String getGuaranteeSrmsId() {
        return guaranteeSrmsId;
    }

    public void setGuaranteeSrmsId(String guaranteeSrmsId) {
        this.guaranteeSrmsId = guaranteeSrmsId;
        this.transactionReference = guaranteeSrmsId;
    }

    public String getTransactionReference() {
        return guaranteeSrmsId;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = receivedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRelatedTransactionReference() {
        return relatedTransactionReference;
    }

    public void setRelatedTransactionReference(String relatedTransactionReference) {
        this.relatedTransactionReference = relatedTransactionReference;
    }

    public String getModeOfTransaction() {
        return modeOfTransaction;
    }

    public void setModeOfTransaction(String modeOfTransaction) {
        this.modeOfTransaction = modeOfTransaction;
    }

    public String getApplicantParty() {
        return applicantParty;
    }

    public void setApplicantParty(String applicantParty) {
        this.applicantParty = applicantParty;
    }

    public String getBeneficiaryDetails() {
        return beneficiaryDetails;
    }

    public void setBeneficiaryDetails(String beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryConditions() {
        return expiryConditions;
    }

    public void setExpiryConditions(String expiryConditions) {
        this.expiryConditions = expiryConditions;
    }

    public String getExpectedIssueDate() {
        return expectedIssueDate;
    }

    public void setExpectedIssueDate(String expectedIssueDate) {
        this.expectedIssueDate = expectedIssueDate;
    }

    public String getAutoExtensionExpiry() {
        return autoExtensionExpiry;
    }

    public void setAutoExtensionExpiry(String autoExtensionExpiry) {
        this.autoExtensionExpiry = autoExtensionExpiry;
    }

    public String getExtensionPeriod() {
        return extensionPeriod;
    }

    public void setExtensionPeriod(String extensionPeriod) {
        this.extensionPeriod = extensionPeriod;
    }

    public String getExtensionCapPeriod() {
        return extensionCapPeriod;
    }

    public void setExtensionCapPeriod(String extensionCapPeriod) {
        this.extensionCapPeriod = extensionCapPeriod;
    }

    public String getNotificationPeriod() {
        return notificationPeriod;
    }

    public void setNotificationPeriod(String notificationPeriod) {
        this.notificationPeriod = notificationPeriod;
    }

    public String getExtensionDetails() {
        return extensionDetails;
    }

    public void setExtensionDetails(String extensionDetails) {
        this.extensionDetails = extensionDetails;
    }

    public String getApplicableRules() {
        return applicableRules;
    }

    public void setApplicableRules(String applicableRules) {
        this.applicableRules = applicableRules;
    }

    public String getGoverningLaw() {
        return governingLaw;
    }

    public void setGoverningLaw(String governingLaw) {
        this.governingLaw = governingLaw;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getOtherInstructions() {
        return otherInstructions;
    }

    public void setOtherInstructions(String otherInstructions) {
        this.otherInstructions = otherInstructions;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public String getIssuingBankName() {
        return issuingBankName;
    }

    public void setIssuingBankName(String issuingBankName) {
        this.issuingBankName = issuingBankName;
    }

    public String getIssuingBankSwiftBicCode() {
        return issuingBankSwiftBicCode;
    }

    public void setIssuingBankSwiftBicCode(String issuingBankSwiftBicCode) {
        this.issuingBankSwiftBicCode = issuingBankSwiftBicCode;
    }

    public String getIssuingBankIban() {
        return issuingBankIban;
    }

    public void setIssuingBankIban(String issuingBankIban) {
        this.issuingBankIban = issuingBankIban;
    }

    public String getIssuingBankLocalCode() {
        return issuingBankLocalCode;
    }

    public void setIssuingBankLocalCode(String issuingBankLocalCode) {
        this.issuingBankLocalCode = issuingBankLocalCode;
    }

    public String getIssuingBankAddress() {
        return issuingBankAddress;
    }

    public void setIssuingBankAddress(String issuingBankAddress) {
        this.issuingBankAddress = issuingBankAddress;
    }

    public String getMessageFromBank() {
        return messageFromBank;
    }

    public void setMessageFromBank(String messageFromBank) {
        this.messageFromBank = messageFromBank;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getUploadedDocuments() {
        return uploadedDocuments;
    }

    public void setUploadedDocuments(String uploadedDocuments) {
        this.uploadedDocuments = uploadedDocuments;
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

    public String getReasonForSelfRejection() {
        return reasonForSelfRejection;
    }

    public void setReasonForSelfRejection(String reasonForSelfRejection) {
        this.reasonForSelfRejection = reasonForSelfRejection;
    }

    public String getSelfRejectionHistory() {
        return selfRejectionHistory;
    }

    public void setSelfRejectionHistory(String selfRejectionHistory) {
        this.selfRejectionHistory = selfRejectionHistory;
    }

    public String getClausesAndConditions() {
        return clausesAndConditions;
    }

    public void setClausesAndConditions(String clausesAndConditions) {
        this.clausesAndConditions = clausesAndConditions;
    }

    public String getAmountWithCurrency() {
        return TradeFinanceCommonUtils.getAmountWithCurrency(this.getCurrency(), this.getAmount(), false);
    }

    public String getLastAmendmentDetails() {
        return lastAmendmentDetails;
    }

    public void setLastAmendmentDetails(String lastAmendmentDetails) {
        this.lastAmendmentDetails = lastAmendmentDetails;
    }

    public String getClaimInformation() {
        return claimInformation;
    }

    public void setClaimInformation(String claimInformation) {
        this.claimInformation = claimInformation;
    }

    public String getUtilizedAmount() {
        return utilizedAmount;
    }

    public void setUtilizedAmount(String utilizedAmount) {
        this.utilizedAmount = utilizedAmount;
    }

    public String getLiabilityDetails() {
        return liabilityDetails;
    }

    public void setLiabilityDetails(String liabilityDetails) {
        this.liabilityDetails = liabilityDetails;
    }

    public String getReleasedAmount() {
        return releasedAmount;
    }

    public void setReleasedAmount(String releasedAmount) {
        this.releasedAmount = releasedAmount;
    }

    public String getDemandAcceptance() {
        return demandAcceptance;
    }

    public void setDemandAcceptance(String demandAcceptance) {
        this.demandAcceptance = demandAcceptance;
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
