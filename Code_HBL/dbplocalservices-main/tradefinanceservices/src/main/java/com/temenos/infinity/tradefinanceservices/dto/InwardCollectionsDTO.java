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
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class InwardCollectionsDTO implements Serializable, DBPDTO {
    private String amendmentDetails;
    private String amount;
    private String billExchangeStatus;
    private String charges;
    private String chargesDebitFrom;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String collectionSrmsId;
    private String createdDate;
    private String currency;
    private String debitAmountFrom;
    private String documentNo;
    private String documentsUploaded;
    private String documentsAgainstAcceptance;
    private String draweeAcknowledgement;
    private String draweeAcknowledgementDate;
    private String drawerName;
    private String incoTerms;
    @JsonAlias({PARAM_LASTUPDATEDTIMESTAMP})
    private String lastUpdatedDate;
    private String maturityDate;
    private String messageFromBank;
    private String messageToBank;
    private String paymentStatus;
    private String reasonForRejection;
    private String rejectedDate;
    private String reasonForReturn;
    private String receivedOn;
    private String remittingBank;
    private String settledDate;
    private String status;
    private String tenorType;
    private String transactionReference;
    private String usanceAcceptance;
    private String usanceAcceptanceDate;
    private String usanceAcceptanceEligibility;
    private String usanceDetails;
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
    private String maturityDateFormatted;
	@JsonIgnore
    private String amountFormatted;
	
	public String getMaturityDateFormatted() {
        return _formatDate(maturityDate);
    }
	
	public String getReceivedOnFormatted() {
        return _formatDate(receivedOn);
    }
	
	public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(amount)) ? formatAmount(String.valueOf(amount)) : String.valueOf(amount);
    }
    

    public String getTradeCurrency() {
        return currency;
    }
    public String getServiceRequestSrmsId() {
        return collectionSrmsId;
    }
    public String getProduct() {
        return "Inward Collection";
    }

    public InwardCollectionsDTO() {
    }

    public String getAmendmentDetails() {
        return amendmentDetails;
    }

    public void setAmendmentDetails(String amendmentDetails) {
        this.amendmentDetails = amendmentDetails;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBillExchangeStatus() {
        return billExchangeStatus;
    }

    public void setBillExchangeStatus(String billExchangeStatus) {
        this.billExchangeStatus = billExchangeStatus;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getChargesDebitFrom() {
        return chargesDebitFrom;
    }

    public void setChargesDebitFrom(String chargesDebitFrom) {
        this.chargesDebitFrom = chargesDebitFrom;
    }

    public String getCollectionSrmsId() {
        return collectionSrmsId;
    }

    public void setCollectionSrmsId(String collectionSrmsId) {
        this.collectionSrmsId = collectionSrmsId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDebitAmountFrom() {
        return debitAmountFrom;
    }

    public void setDebitAmountFrom(String debitAmountFrom) {
        this.debitAmountFrom = debitAmountFrom;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocumentsUploaded() {
        return documentsUploaded;
    }

    public void setDocumentsUploaded(String documentsUploaded) {
        this.documentsUploaded = documentsUploaded;
    }
    public String getDocumentsAgainstAcceptance() {
        return documentsAgainstAcceptance;
    }

    public void setDocumentsAgainstAcceptance(String documentsAgainstAcceptance) {
        this.documentsAgainstAcceptance = documentsAgainstAcceptance;
    }

    public String getDraweeAcknowledgement() {
        return draweeAcknowledgement;
    }

    public void setDraweeAcknowledgement(String draweeAcknowledgement) {
        this.draweeAcknowledgement = draweeAcknowledgement;
    }

    public String getDraweeAcknowledgementDate() {
        return draweeAcknowledgementDate;
    }

    public void setDraweeAcknowledgementDate(String draweeAcknowledgementDate) {
        this.draweeAcknowledgementDate = draweeAcknowledgementDate;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName;
    }

    public String getIncoTerms() {
        return incoTerms;
    }

    public void setIncoTerms(String incoTerms) {
        this.incoTerms = incoTerms;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getMaturityDate() {
        return maturityDate;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public String getReasonForReturn() {
        return reasonForReturn;
    }

    public void setReasonForReturn(String reasonForReturn) {
        this.reasonForReturn = reasonForReturn;
    }

    public String getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = receivedOn;
    }

    public String getRemittingBank() {
        return remittingBank;
    }

    public void setRemittingBank(String remittingBank) {
        this.remittingBank = remittingBank;
    }

    public String getSettledDate() {
        return settledDate;
    }

    public void setSettledDate(String settledDate) {
        this.settledDate = settledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenorType() {
        return tenorType;
    }

    public void setTenorType(String tenorType) {
        this.tenorType = tenorType;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getUsanceAcceptance() {
        return usanceAcceptance;
    }

    public void setUsanceAcceptance(String usanceAcceptance) {
        this.usanceAcceptance = usanceAcceptance;
    }

    public String getUsanceAcceptanceDate() {
        return usanceAcceptanceDate;
    }

    public void setUsanceAcceptanceDate(String usanceAcceptanceDate) {
        this.usanceAcceptanceDate = usanceAcceptanceDate;
    }

    public String getUsanceAcceptanceEligibility() {
        return usanceAcceptanceEligibility;
    }

    public void setUsanceAcceptanceEligibility(String usanceAcceptanceEligibility) {
        this.usanceAcceptanceEligibility = usanceAcceptanceEligibility;
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
}
