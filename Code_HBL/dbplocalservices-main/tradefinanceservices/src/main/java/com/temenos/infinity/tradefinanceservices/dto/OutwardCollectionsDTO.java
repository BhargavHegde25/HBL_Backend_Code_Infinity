/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

/**
 * @author k.meiyazhagan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OutwardCollectionsDTO implements DBPDTO {

    @JsonIgnore
    private String product;
    @JsonIgnore
    private String serviceRequestSrmsId;
    @JsonIgnore
    private String tradeCurrency;
    private String allowUsanceAcceptance;
    private String amount;
    @JsonIgnore
    private String amountFormatted;
    private String billOfExchangeStatus;
    private String collectingBank;
    private String collectingBankAddress;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String collectionReference;
    private String courierTrackingDetails;
    private String createdOn;
    @JsonIgnore
    private String createdOnFormatted;
    private String creditAccount;
    private String currency;
    @JsonIgnore
    private String customerId;
    private String debitAccount;
    private String deliveryInstructions;
    private String documentNo;
    private String draweeAcceptance;
    private String draweeAcknowledgement;
    private String draweeAddress;
    private String draweeName;
    private String incoTerms;
    private String instructionsForBills;
    private String isBillExchangeSigned;
    private String lastAmendmentDetails;
    private String maturityDate;
    @JsonIgnore
    private String maturityDateFormatted;
    private String messageFromBank;
    private String messageToBank;
    private String otherCollectionDetails;
    private String paymentStatus;
    private String physicalDocuments;
    private String reasonForRejection;
    private String reasonForCancellation;
    private String reasonForReturn;
    private String returnedHistory;
    private String status;
    private String swiftOrBicCode;
    private String tenorType;
    private String settledAmount;
    @JsonIgnore
    private String requestSelection;
    @JsonIgnore
    private String transactReference;
    @JsonAlias({PARAM_LASTUPDATEDTIMESTAMP})
    private String updatedOn;
    @JsonIgnore
    private String updatedOnFormatted;
    private String uploadDocuments;
    private String usanceDays;
    private String usanceDetails;
    private String errorMsg;
    private String errorCode;
    private String errorDetails;
    private String dbpErrCode;
    private String dbpErrMsg;


    public String getTradeCurrency() {
        return currency;
    }

    public String getServiceRequestSrmsId() {
        return collectionReference;
    }

    public String getProduct() {
        return "Outward Collection";
    }

    public String getAllowUsanceAcceptance() {
        return allowUsanceAcceptance;
    }

    public void setAllowUsanceAcceptance(String allowUsanceAcceptance) {
        this.allowUsanceAcceptance = allowUsanceAcceptance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(amount) ? formatAmount(amount) : amount;
    }

    public String getBillOfExchangeStatus() {
        return billOfExchangeStatus;
    }

    public void setBillOfExchangeStatus(String billOfExchangeStatus) {
        this.billOfExchangeStatus = billOfExchangeStatus;
    }

    public String getCollectingBank() {
        return collectingBank;
    }

    public void setCollectingBank(String collectingBank) {
        this.collectingBank = collectingBank;
    }

    public String getCollectingBankAddress() {
        return collectingBankAddress;
    }

    public void setCollectingBankAddress(String collectingBankAddress) {
        this.collectingBankAddress = new JSONObject(collectingBankAddress).toString();
    }

    public String getCollectionReference() {
        return collectionReference;
    }

    public void setCollectionReference(String collectionReference) {
        this.collectionReference = collectionReference;
    }

    public String getCourierTrackingDetails() {
        return courierTrackingDetails;
    }

    public void setCourierTrackingDetails(String courierTrackingDetails) {
        this.courierTrackingDetails = courierTrackingDetails;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreatedOnFormatted() {
        return _formatDate(createdOn);
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDraweeAcceptance() {
        return draweeAcceptance;
    }

    public void setDraweeAcceptance(String draweeAcceptance) {
        this.draweeAcceptance = draweeAcceptance;
    }

    public String getDraweeAcknowledgement() {
        return draweeAcknowledgement;
    }

    public void setDraweeAcknowledgement(String draweeAcknowledgement) {
        this.draweeAcknowledgement = draweeAcknowledgement;
    }

    public String getDraweeAddress() {
        return draweeAddress;
    }

    public void setDraweeAddress(String draweeAddress) {
        this.draweeAddress = new JSONObject(draweeAddress).toString();
    }

    public String getDraweeName() {
        return draweeName;
    }

    public void setDraweeName(String draweeName) {
        this.draweeName = draweeName;
    }

    public String getIncoTerms() {
        return incoTerms;
    }

    public void setIncoTerms(String incoTerms) {
        this.incoTerms = incoTerms;
    }

    public String getInstructionsForBills() {
        return instructionsForBills;
    }

    public void setInstructionsForBills(String instructionsForBills) {
        this.instructionsForBills = new JSONArray(instructionsForBills).toString();
    }

    public String getIsBillExchangeSigned() {
        return isBillExchangeSigned;
    }

    public void setIsBillExchangeSigned(String isBillExchangeSigned) {
        this.isBillExchangeSigned = isBillExchangeSigned;
    }

    public String getLastAmendmentDetails() {
        return lastAmendmentDetails;
    }

    public void setLastAmendmentDetails(String lastAmendmentDetails) {
        this.lastAmendmentDetails = new JSONObject(lastAmendmentDetails).toString();
        ;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public String getMaturityDateFormatted() {
        return _formatDate(maturityDate);
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
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

    public String getOtherCollectionDetails() {
        return otherCollectionDetails;
    }

    public void setOtherCollectionDetails(String otherCollectionDetails) {
        this.otherCollectionDetails = otherCollectionDetails;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPhysicalDocuments() {
        return physicalDocuments;
    }

    public void setPhysicalDocuments(String physicalDocuments) {
        this.physicalDocuments = new JSONArray(physicalDocuments).toString();
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }

    public String getReasonForReturn() {
        return reasonForReturn;
    }

    public void setReasonForReturn(String reasonForReturn) {
        this.reasonForReturn = reasonForReturn;
    }

    public String getReturnedHistory() {
        return returnedHistory;
    }

    public void setReturnedHistory(String returnedHistory) {
        this.returnedHistory = new JSONArray(returnedHistory).toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSwiftOrBicCode() {
        return swiftOrBicCode;
    }

    public void setSwiftOrBicCode(String swiftOrBicCode) {
        this.swiftOrBicCode = swiftOrBicCode;
    }

    public String getTenorType() {
        return tenorType;
    }

    public void setTenorType(String tenorType) {
        this.tenorType = tenorType;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedOnFormatted() {
        return _formatDate(updatedOn);
    }

    public String getUploadDocuments() {
        return uploadDocuments;
    }

    public void setUploadDocuments(String uploadDocuments) {
        this.uploadDocuments = new JSONArray(uploadDocuments).toString();
    }

    public String getUsanceDays() {
        return usanceDays;
    }

    public void setUsanceDays(String usanceDays) {
        this.usanceDays = usanceDays;
    }

    public String getUsanceDetails() {
        return usanceDetails;
    }

    public void setUsanceDetails(String usanceDetails) {
        this.usanceDetails = usanceDetails;
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

    public String getReasonForCancellation() {
        return reasonForCancellation;
    }

    public void setReasonForCancellation(String reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
    }

    public String getRequestSelection() {
        return requestSelection;
    }

    public void setRequestSelection(String requestSelection) {
        this.requestSelection = requestSelection;
    }

    public String getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(String settledAmount) {
        this.settledAmount = settledAmount;
    }

    public String getTransactReference() {
        return transactReference;
    }

    public void setTransactReference(String transactReference) {
        this.transactReference = transactReference;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
