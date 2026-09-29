/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_SRMSID;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;

import org.json.JSONArray;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GuaranteesDTO implements DBPDTO {
    private String beneficiaryName;
    private String productType;
    private String guaranteeAndSBLCType;
    private String guaranteesReferenceNo;
    private String createdOn;
    private String status;
    private String amount;
    private String issueDate;
    private String expiryDate;
    private String expiryType;
    private String totalBeneficiaries;
    private String modeOfTransaction;
    private String advisingBank;
    private String customerId;
    private String instructingParty;
    private String instructingPartyName;
    private String instructingPartyId;
    private String applicantParty;
    private String applicantPartyName;
    private String applicantPartyId;
    private String currency;
    private String expectedIssueDate;
    private String claimExpiryDate;
    private String expiryCondition;
    private String extendExpiryDate;
    private String extensionCapPeriod;
    private String notificationPeriod;
    private String extensionDetails;
    private String governingLaw;
    private String otherInstructions;
    private String beneficiaryType;
    private String deliveryInstructions;
    private String beneficiaryAddress1;
    private String beneficiaryAddress2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String saveBeneficiary;
    private String swiftCode;
    private String bankName;
    private String iban;
    private String localCode;
    private String bankAddress1;
    private String bankAddress2;
    private String bankCity;

    private String bankState;
    private String bankCountry;
    private String bankZipCode;
    private String instructionCurrencies;
    private String limitInstructions;
    private String otherBankInstructions;
    private String messageToBank;
    private String documentReferences;
    private String documentName;
    private String clauseConditions;
    private String errorMsg;
    private String errorCode;
    private String dbpErrMsg;
    private String dbpErrCode;
    @JsonAlias({"guaranteesSRMSId", PARAM_SRMSID, PARAM_RECORD_ID})
    private String guaranteesSRMSId;
    private String message;
    private String beneficiaryDetails;
    private String totalAmount;
    private String serviceRequestTime;
    private String isSingleSettlement;
    private String extensionPeriod;
    private String linkedPayees;
    private String applicableRules;
    private String demandAcceptance;
    private String partialDemandPercentage;
    private String returnHistory;
    private String reasonForReturn;
    private String corporateUserName;
    private String amendmentNo;
    private String lastUpdatedTimeStamp;
    private String amountWithCurrency;
    private String transferable;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;
    private String relatedTransactionReference;
    @JsonIgnore
    private String createdOnFormatted;
    @JsonIgnore
    private String beneficiaryCount;
    private String errorDetails;

    public String getBeneficiaryCount() {
        JSONArray beneficiaries = new JSONArray(beneficiaryDetails);
        return Integer.toString(beneficiaries.length());
    }

    public String getCreatedOnFormatted() {
        return _formatDate(createdOn);
    }

    public String getTradeCurrency() {
        return currency;
    }

    public String getServiceRequestSrmsId() {
        return guaranteesSRMSId;
    }

    public String getProduct() {
        return "Issued GT & SBLC";
    }

    public String getInstructingPartyName() {
        return instructingPartyName;
    }


    public void setInstructingPartyName(String instructingPartyName) {
        this.instructingPartyName = instructingPartyName;
    }

    public String getInstructingPartyId() {
        return instructingPartyId;
    }

    public void setInstructingPartyId(String instructingPartyId) {
        this.instructingPartyId = instructingPartyId;
    }

    public String getApplicantPartyName() {
        return applicantPartyName;
    }

    public void setApplicantPartyName(String applicantPartyName) {
        this.applicantPartyName = applicantPartyName;
    }

    public String getApplicantPartyId() {
        return applicantPartyId;
    }

    public void setApplicantPartyId(String applicantPartyId) {
        this.applicantPartyId = applicantPartyId;
    }

    public String getTransferable() {
        return transferable;
    }

    public void setTransferable(String transferable) {
        this.transferable = transferable;
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public String getGuaranteeAndSBLCType() {
        return guaranteeAndSBLCType;
    }

    public void setGuaranteeAndSBLCType(String guaranteeAndSBLCType) {
        this.guaranteeAndSBLCType = guaranteeAndSBLCType;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getApplicableRules() {
        return applicableRules;
    }

    public void setApplicableRules(String applicableRules) {
        this.applicableRules = applicableRules;
    }

    public String getDemandAcceptance() {
        return demandAcceptance;
    }

    public void setDemandAcceptance(String demandAcceptance) {
        this.demandAcceptance = demandAcceptance;
    }

    public String getPartialDemandPercentage() {
        return partialDemandPercentage;
    }

    public void setPartialDemandPercentage(String partialDemandPercentage) {
        this.partialDemandPercentage = partialDemandPercentage;
    }

    public String getLinkedPayees() {
        return linkedPayees;
    }

    public void setLinkedPayees(String linkedPayees) {
        this.linkedPayees = linkedPayees;
    }

    public String getExtensionPeriod() {
        return extensionPeriod;
    }

    public void setExtensionPeriod(String extensionPeriod) {
        this.extensionPeriod = extensionPeriod;
    }

    public String getIsSingleSettlement() {
        return isSingleSettlement;
    }

    public void setIsSingleSettlement(String isSingleSettlement) {
        this.isSingleSettlement = isSingleSettlement;
    }

    public String getServiceRequestTime() {
        return serviceRequestTime;
    }

    public void setServiceRequestTime(String serviceRequestTime) {
        this.serviceRequestTime = serviceRequestTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBeneficiaryDetails() {
        return beneficiaryDetails;
    }

    public void setBeneficiaryDetails(String beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGuaranteesSRMSId() {
        return guaranteesSRMSId;
    }

    public void setGuaranteesSRMSId(String guaranteesSRMSId) {
        this.guaranteesSRMSId = guaranteesSRMSId;
        this.guaranteesReferenceNo = guaranteesSRMSId;
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errormsg) {
        this.errorMsg = errormsg;
    }

    // Getter And Setter
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getGuaranteesReferenceNo() {
        return guaranteesSRMSId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryType() {
        return expiryType;
    }

    public void setExpiryType(String expiryType) {
        this.expiryType = expiryType;
    }

    public String getTotalBeneficiaries() {
        return totalBeneficiaries;
    }

    public void setTotalBeneficiaries(String totalBeneficiaries) {
        this.totalBeneficiaries = totalBeneficiaries;
    }

    public String getModeOfTransaction() {
        return modeOfTransaction;
    }

    public void setModeOfTransaction(String modeOfTransaction) {
        this.modeOfTransaction = modeOfTransaction;
    }

    public String getAdvisingBank() {
        return advisingBank;
    }

    public void setAdvisingBank(String advisingBank) {
        this.advisingBank = advisingBank;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInstructingParty() {
        return instructingParty;
    }

    public void setInstructingParty(String instructingParty) {
        this.instructingParty = instructingParty;
    }

    public String getApplicantParty() {
        return applicantParty;
    }

    public void setApplicantParty(String applicantParty) {
        this.applicantParty = applicantParty;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpectedIssueDate() {
        return expectedIssueDate;
    }

    public void setExpectedIssueDate(String expectedIssueDate) {
        this.expectedIssueDate = expectedIssueDate;
    }

    public String getClaimExpiryDate() {
        return claimExpiryDate;
    }

    public void setClaimExpiryDate(String claimExpiryDate) {
        this.claimExpiryDate = claimExpiryDate;
    }

    public String getExpiryCondition() {
        return expiryCondition;
    }

    public void setExpiryCondition(String expiryCondition) {
        this.expiryCondition = expiryCondition;
    }

    public String getExtendExpiryDate() {
        return extendExpiryDate;
    }

    public void setExtendExpiryDate(String extendExpiryDate) {
        this.extendExpiryDate = extendExpiryDate;
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

    public String getGoverningLaw() {
        return governingLaw;
    }

    public void setGoverningLaw(String governingLaw) {
        this.governingLaw = governingLaw;
    }

    public String getOtherInstructions() {
        return otherInstructions;
    }

    public void setOtherInstructions(String otherInstructions) {
        this.otherInstructions = otherInstructions;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getBeneficiaryAddress1() {
        return beneficiaryAddress1;
    }

    public void setBeneficiaryAddress1(String beneficiaryAddress1) {
        this.beneficiaryAddress1 = beneficiaryAddress1;
    }

    public String getBeneficiaryAddress2() {
        return beneficiaryAddress2;
    }

    public void setBeneficiaryAddress2(String beneficiaryAddress2) {
        this.beneficiaryAddress2 = beneficiaryAddress2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSaveBeneficiary() {
        return saveBeneficiary;
    }

    public void setSaveBeneficiary(String saveBeneficiary) {
        this.saveBeneficiary = saveBeneficiary;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public String getBankAddress1() {
        return bankAddress1;
    }

    public void setBankAddress1(String bankAddress1) {
        this.bankAddress1 = bankAddress1;
    }

    public String getBankAddress2() {
        return bankAddress2;
    }

    public void setBankAddress2(String bankAddress2) {
        this.bankAddress2 = bankAddress2;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getBankState() {
        return bankState;
    }

    public void setBankState(String bankState) {
        this.bankState = bankState;
    }

    public String getBankCountry() {
        return bankCountry;
    }

    public void setBankCountry(String bankCountry) {
        this.bankCountry = bankCountry;
    }

    public String getBankZipCode() {
        return bankZipCode;
    }

    public void setBankZipCode(String bankZipCode) {
        this.bankZipCode = bankZipCode;
    }

    public String getInstructionCurrencies() {
        return instructionCurrencies;
    }

    public void setInstructionCurrencies(String instructionCurrencies) {
        this.instructionCurrencies = instructionCurrencies;
    }

    public String getLimitInstructions() {
        return limitInstructions;
    }

    public void setLimitInstructions(String limitInstructions) {
        this.limitInstructions = limitInstructions;
    }

    public String getOtherBankInstructions() {
        return otherBankInstructions;
    }

    public void setOtherBankInstructions(String otherBankInstructions) {
        this.otherBankInstructions = otherBankInstructions;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getDocumentReferences() {
        return documentReferences;
    }

    public void setDocumentReferences(String documentReferences) {
        this.documentReferences = documentReferences;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getClauseConditions() {
        return clauseConditions;
    }

    public void setClauseConditions(String clauseConditions) {
        this.clauseConditions = clauseConditions;
    }

    public String getReturnHistory() {
        return returnHistory;
    }

    public void setReturnHistory(String returnHistory) {
        this.returnHistory = returnHistory;
    }

    public String getReasonForReturn() {
        return reasonForReturn;
    }

    public void setReasonForReturn(String reasonForReturn) {
        this.reasonForReturn = reasonForReturn;
    }

    public String getCorporateUserName() {
        return corporateUserName;
    }

    public void setCorporateUserName(String corporateUserName) {
        this.corporateUserName = corporateUserName;
    }

    public String getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    public void setLastUpdatedTimeStamp(String lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }

    public String getAmountWithCurrency() {
        return TradeFinanceCommonUtils.getAmountWithCurrency(currency, amount, false);
    }

    public void setAmountWithCurrency(String amountWithCurrency) {
        this.amountWithCurrency = amountWithCurrency;
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

}
