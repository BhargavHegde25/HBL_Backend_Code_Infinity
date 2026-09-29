/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_SRMSID;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getDaysInBetween;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class IssuedGuaranteeClaimsDTO implements DBPDTO {
    private String beneficiaryName;
    private String guaranteesSRMSId;
    private String productType;
    private String guaranteeAndSBLCType;
    private String amount;
    private String unUtilizedAmount;
    private String issueDate;
    private String expiryType;
    private String expiryDate;
    private String expiryCondition;
    private String advisingBank;
    private String status;
    private String claimStatus;
    @JsonAlias({"claimsSRMSId", PARAM_SRMSID, PARAM_RECORD_ID})
    private String claimsSRMSId;
    private String claimType;
    private String claimAmount;
    private String claimCurrency;
    private String receivedOn;
    private String expectedSettlementDate;
    private String dueDays;
    private String presentationDetails;
    private String demandType;
    private String newExtensionDate;
    private String documents;
    private String documentStatus;
    private String otherDemandDetails;
    private String messageFromBank;
    private String claimAcceptance;
    private String debitAccount;
    private String requestedOverdraft;
    private String reasonForRejection;
    private String rejectedDate;
    private String acceptedDate;
    private String messageToBank;
    private String discrepancyDetails;
    private String discrepancyAcceptance;
    private String discrepancyHistory;
    private String returnCount;
    private String returnedTime;

    private String serviceRequestTime;

    private String paymentStatus;
    private String settledDate;
    private String totalAmountToBePaid;
    private String reasonForReturn;
    private String returnMessageToBank;
    private String corporateUserName;
    private String message;
    private String errMsg;
    private String errCode;
    private String errorMessage;
    private String errorCode;
    private String errorMsg;
    private String dbpErrMsg;
    private String dbpErrCode;
    @JsonIgnore
    private String product;
    @JsonIgnore
    private String serviceRequestSrmsId;
    @JsonIgnore
    private String tradeCurrency;
    @JsonIgnore
    private String receivedOnFormatted;
    @JsonIgnore
    private String amountFormatted;
    @JsonIgnore
    private String countDown;

    public String getCountDown() {
        return getDaysInBetween(expectedSettlementDate, _formatDate(receivedOn));
    }

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(claimAmount)) ? formatAmount(String.valueOf(claimAmount)) : String.valueOf(claimAmount);
    }

    public String getReceivedOnFormatted() {
        return _formatDate(receivedOn);
    }

    public String getTradeCurrency() {
        return claimCurrency;
    }

    public String getServiceRequestSrmsId() {
        return claimsSRMSId;
    }

    public String getProduct() {
        return "Claim Received";
    }

    public IssuedGuaranteeClaimsDTO() {
    }

    public IssuedGuaranteeClaimsDTO(String beneficiaryName, String guaranteesSRMSId, String productType,
                                    String guaranteeAndSBLCType, String amount, String unUtilizedAmount,
                                    String issueDate,
                                    String expiryType, String expiryDate, String expiryCondition, String advisingBank,
                                    String status, String claimStatus, String claimsSRMSId, String claimType,
                                    String claimAmount, String receivedOn, String expectedSettlementDate, String dueDays,
                                    String presentationDetails, String demandType, String newExtensionDate,
                                    String documents, String otherDemandDetails, String messageFromBank,
                                    String claimAcceptance, String debitAccount, String requestedOverdraft,
                                    String reasonForRejection, String rejectedDate, String acceptedDate,
                                    String messageToBank, String discrepancyDetails, String discrepancyAcceptance,
                                    String paymentStatus, String settledDate, String totalAmountToBePaid,
                                    String reasonForReturn, String returnMessageToBank, String corporateUserName) {
        this.beneficiaryName = beneficiaryName;
        this.guaranteesSRMSId = guaranteesSRMSId;
        this.productType = productType;
        this.guaranteeAndSBLCType = guaranteeAndSBLCType;
        this.amount = amount;
        this.unUtilizedAmount = unUtilizedAmount;
        this.issueDate = issueDate;
        this.expiryType = expiryType;
        this.expiryDate = expiryDate;
        this.expiryCondition = expiryCondition;
        this.advisingBank = advisingBank;
        this.status = status;
        this.claimStatus = claimStatus;
        this.claimsSRMSId = claimsSRMSId;
        this.claimType = claimType;
        this.claimAmount = claimAmount;
        this.receivedOn = receivedOn;
        this.expectedSettlementDate = expectedSettlementDate;
        this.dueDays = dueDays;
        this.presentationDetails = presentationDetails;
        this.demandType = demandType;
        this.newExtensionDate = newExtensionDate;
        this.documents = documents;
        this.otherDemandDetails = otherDemandDetails;
        this.messageFromBank = messageFromBank;
        this.claimAcceptance = claimAcceptance;
        this.debitAccount = debitAccount;
        this.requestedOverdraft = requestedOverdraft;
        this.reasonForRejection = reasonForRejection;
        this.rejectedDate = rejectedDate;
        this.acceptedDate = acceptedDate;
        this.messageToBank = messageToBank;
        this.discrepancyDetails = discrepancyDetails;
        this.discrepancyAcceptance = discrepancyAcceptance;
        this.paymentStatus = paymentStatus;
        this.settledDate = settledDate;
        this.totalAmountToBePaid = totalAmountToBePaid;
        this.reasonForReturn = reasonForReturn;
        this.returnMessageToBank = returnMessageToBank;
        this.corporateUserName = corporateUserName;
    }

    public String getReturnedTime() {
        return returnedTime;
    }

    public void setReturnedTime(String returnedTime) {
        this.returnedTime = returnedTime;
    }

    public String getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(String returnCount) {
        this.returnCount = returnCount;
    }

    public String getDiscrepancyHistory() {
        return discrepancyHistory;
    }

    public void setDiscrepancyHistory(String discrepancyHistory) {
        this.discrepancyHistory = discrepancyHistory;
    }

    public String getServiceRequestTime() {
        return serviceRequestTime;
    }

    public void setServiceRequestTime(String serviceRequestTime) {
        this.serviceRequestTime = serviceRequestTime;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getGuaranteesSRMSId() {
        return guaranteesSRMSId;
    }

    public void setGuaranteesSRMSId(String guaranteesSRMSId) {
        this.guaranteesSRMSId = guaranteesSRMSId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getGuaranteeAndSBLCType() {
        return guaranteeAndSBLCType;
    }

    public void setGuaranteeAndSBLCType(String guaranteeAndSBLCType) {
        this.guaranteeAndSBLCType = guaranteeAndSBLCType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnUtilizedAmount() {
        return unUtilizedAmount;
    }

    public void setUnUtilizedAmount(String unUtilizedAmount) {
        this.unUtilizedAmount = unUtilizedAmount;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
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

    public String getExpiryCondition() {
        return expiryCondition;
    }

    public void setExpiryCondition(String expiryCondition) {
        this.expiryCondition = expiryCondition;
    }

    public String getAdvisingBank() {
        return advisingBank;
    }

    public void setAdvisingBank(String advisingBank) {
        this.advisingBank = advisingBank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getClaimsSRMSId() {
        return claimsSRMSId;
    }

    public void setClaimsSRMSId(String claimsSRMSId) {
        this.claimsSRMSId = claimsSRMSId;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getExpectedSettlementDate() {
        return expectedSettlementDate;
    }

    public void setExpectedSettlementDate(String expectedSettlementDate) {
        this.expectedSettlementDate = expectedSettlementDate;
    }

    public String getDueDays() {
        return dueDays;
    }

    public void setDueDays(String dueDays) {
        this.dueDays = dueDays;
    }

    public String getPresentationDetails() {
        return presentationDetails;
    }

    public void setPresentationDetails(String presentationDetails) {
        this.presentationDetails = presentationDetails;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public String getNewExtensionDate() {
        return newExtensionDate;
    }

    public void setNewExtensionDate(String newExtensionDate) {
        this.newExtensionDate = newExtensionDate;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getOtherDemandDetails() {
        return otherDemandDetails;
    }

    public void setOtherDemandDetails(String otherDemandDetails) {
        this.otherDemandDetails = otherDemandDetails;
    }

    public String getMessageFromBank() {
        return messageFromBank;
    }

    public void setMessageFromBank(String messageFromBank) {
        this.messageFromBank = messageFromBank;
    }

    public String getClaimAcceptance() {
        return claimAcceptance;
    }

    public void setClaimAcceptance(String claimAcceptance) {
        this.claimAcceptance = claimAcceptance;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getRequestedOverdraft() {
        return requestedOverdraft;
    }

    public void setRequestedOverdraft(String requestedOverdraft) {
        this.requestedOverdraft = requestedOverdraft;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }

    public String getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(String rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(String acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getDiscrepancyDetails() {
        return discrepancyDetails;
    }

    public void setDiscrepancyDetails(String discrepancyDetails) {
        this.discrepancyDetails = discrepancyDetails;
    }

    public String getDiscrepancyAcceptance() {
        return discrepancyAcceptance;
    }

    public void setDiscrepancyAcceptance(String discrepancyAcceptance) {
        this.discrepancyAcceptance = discrepancyAcceptance;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getSettledDate() {
        return settledDate;
    }

    public void setSettledDate(String settledDate) {
        this.settledDate = settledDate;
    }

    public String getTotalAmountToBePaid() {
        return totalAmountToBePaid;
    }

    public void setTotalAmountToBePaid(String totalAmountToBePaid) {
        this.totalAmountToBePaid = totalAmountToBePaid;
    }

    public String getReasonForReturn() {
        return reasonForReturn;
    }

    public void setReasonForReturn(String reasonForReturn) {
        this.reasonForReturn = reasonForReturn;
    }

    public String getReturnMessageToBank() {
        return returnMessageToBank;
    }

    public void setReturnMessageToBank(String returnMessageToBank) {
        this.returnMessageToBank = returnMessageToBank;
    }

    public String getCorporateUserName() {
        return corporateUserName;
    }

    public void setCorporateUserName(String corporateUserName) {
        this.corporateUserName = corporateUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    public String getClaimCurrency() {
        return claimCurrency;
    }

    public void setClaimCurrency(String claimCurrency) {
        this.claimCurrency = claimCurrency;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = receivedOn;
    }
}
