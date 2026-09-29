/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_SRMSID;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ReceivedGuaranteeClaimsDTO implements DBPDTO {

    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String claimsSRMSId;
    private String claimCurrency;
    private String claimAmount;
    private String claimCreditAccount;
    private String chargesDebitAccount;
    private String demandType;
    private String newExtensionDate;
    private String documentInformation;
    private String physicalDocuments;
    private String forwardDocuments;
    private String otherDemandDetails;
    private String utilizedAmount;
    private String returnedCount;

    //payment details
    private String paymentStatus;
    private String paymentSettledDate;

    //document related fields
    private String documentStatus;
    private String totalDocuments;
    private String returnedDocuments;
    private String messageFromBank;
    private String reasonForReturn;
    private String discrepancies;
    private String returnMessageToBank;

    //Guarantee fields
    private String messageToBank;
    private String productType;
    private String guaranteeAndSBLCType;
    private String guaranteesReferenceNo;
    private String guaranteesSRMSId;
    private String amount;
    private String expiryType;
    private String expiryDate;
    private String issuingBank;
    private String status;
    private String createdOn;
    private String issueDate;
    private String beneficiaryName;
    private String applicant;

    private String serviceRequestTime;
    private String returnedHistory;
    private String returnedTime;
    @JsonIgnore
    private JSONObject history;

    // Message
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
    private String amountFormatted;
	@JsonIgnore
    private String expiredOnFormatted;
	
    public String getExpiredOnFormatted() {
        return _formatDate(expiryDate);
    }
    
    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(claimAmount)) ? formatAmount(String.valueOf(claimAmount)) : String.valueOf(claimAmount);
    }
    
    public String getTradeCurrency() {
        return claimCurrency;
    }
    public String getServiceRequestSrmsId() {
        return claimsSRMSId;
    }

    public String getProduct() {
        return "Claim Initiated";
    }

    public ReceivedGuaranteeClaimsDTO() {
    }

    public String getClaimsSRMSId() {
        return claimsSRMSId;
    }

    public void setClaimsSRMSId(String claimsSRMSId) {
        this.claimsSRMSId = claimsSRMSId;
    }

    public String getClaimCurrency() {
        return claimCurrency;
    }

    public void setClaimCurrency(String claimCurrency) {
        this.claimCurrency = claimCurrency;
    }

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getClaimCreditAccount() {
        return claimCreditAccount;
    }

    public void setClaimCreditAccount(String claimCreditAccount) {
        this.claimCreditAccount = claimCreditAccount;
    }

    public String getChargesDebitAccount() {
        return chargesDebitAccount;
    }

    public void setChargesDebitAccount(String chargesDebitAccount) {
        this.chargesDebitAccount = chargesDebitAccount;
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

    public String getDocumentInformation() {
        return documentInformation;
    }

    public void setDocumentInformation(String documentInformation) {
        this.documentInformation = documentInformation;
    }

    public String getPhysicalDocuments() {
        return physicalDocuments;
    }

    public void setPhysicalDocuments(String physicalDocuments) {
        this.physicalDocuments = physicalDocuments;
    }

    public String getForwardDocuments() {
        return forwardDocuments;
    }

    public void setForwardDocuments(String forwardDocuments) {
        this.forwardDocuments = forwardDocuments;
    }

    public String getOtherDemandDetails() {
        return otherDemandDetails;
    }

    public void setOtherDemandDetails(String otherDemandDetails) {
        this.otherDemandDetails = otherDemandDetails;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentSettledDate() {
        return paymentSettledDate;
    }

    public void setPaymentSettledDate(String paymentSettledDate) {
        this.paymentSettledDate = paymentSettledDate;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(String totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public String getReturnedDocuments() {
        return returnedDocuments;
    }

    public void setReturnedDocuments(String returnedDocuments) {
        this.returnedDocuments = returnedDocuments;
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

    public String getReturnedCount() {
        return returnedCount;
    }

    public JSONObject getHistory() {
        return history;
    }

    public void setHistory(JSONObject history) {
        this.history = history;
    }

    public void setReturnedCount(String returnedCount) {
        this.returnedCount = returnedCount;
    }

    public String getGuaranteesReferenceNo() {
        return guaranteesReferenceNo;
    }

    public void setGuaranteesReferenceNo(String guaranteesReferenceNo) {
        this.guaranteesReferenceNo = guaranteesReferenceNo;
    }

    public String getUtilizedAmount() {
        return utilizedAmount;
    }

    public void setUtilizedAmount(String utilizedAmount) {
        this.utilizedAmount = utilizedAmount;
    }

    public String getReturnedTime() {
        return returnedTime;
    }

    public void setReturnedTime(String returnedTime) {
        this.returnedTime = returnedTime;
    }

    public String getGuaranteesSRMSId() {
        return guaranteesSRMSId;
    }

    public void setGuaranteesSRMSId(String guaranteesSRMSId) {
        this.guaranteesSRMSId = guaranteesSRMSId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getReasonForReturn() {
        return reasonForReturn;
    }

    public void setReasonForReturn(String reasonForReturn) {
        this.reasonForReturn = reasonForReturn;
    }

    public String getDiscrepancies() {
        return discrepancies;
    }

    public void setDiscrepancies(String discrepancies) {
        this.discrepancies = discrepancies;
    }

    public String getReturnMessageToBank() {
        return returnMessageToBank;
    }

    public void setReturnMessageToBank(String returnMessageToBank) {
        this.returnMessageToBank = returnMessageToBank;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getServiceRequestTime() {
        return serviceRequestTime;
    }

    public void setServiceRequestTime(String serviceRequestTime) {
        this.serviceRequestTime = serviceRequestTime;
    }

    public String getReturnedHistory() {
        return returnedHistory;
    }

    public void setReturnedHistory(String returnedHistory) {
        this.returnedHistory = returnedHistory;
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
}
