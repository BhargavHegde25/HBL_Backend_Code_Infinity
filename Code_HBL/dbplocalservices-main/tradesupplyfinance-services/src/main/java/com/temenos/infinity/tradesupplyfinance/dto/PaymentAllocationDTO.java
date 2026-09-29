/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;

/**
 * @author k.meiyazhagan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PaymentAllocationDTO implements DBPDTO {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private String beneficiaryId;
    private String beneficiaryName;
    @JsonAlias(PARAM_CREATED_DATE)
    private String createdDate;
    private String currency;
    private String fundingDocuments;
    private String originalAmount;
    @JsonAlias({PARAM_RECORD_ID})
    private String paymentAllocationId;
    private String receiptAmount;
    private String senderId;
    private String senderName;
    private String status;
    private String transactionId;
    @JsonAlias({PARAM_UPDATED_DATE})
    private String updatedDate;
    private String uploadedFrom;
    private String uploadedBy;
    private String valueDate;
    private String dbpErrCode;
    private String dbpErrMsg;

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
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

    public String getFundingDocuments() {
        return fundingDocuments;
    }

    public void setFundingDocuments(String fundingDocuments) {
        try {
            JSONArray docs = new JSONArray(fundingDocuments);
            this.fundingDocuments = docs.isEmpty() ? new PaymentAllocationDTO().getFundingDocuments() : docs.toString();
        } catch (Exception e) {
            this.fundingDocuments = new PaymentAllocationDTO().getFundingDocuments();
            alert.prepareError("SCF :: Parsing Documents Failed", e).log();
        }
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getPaymentAllocationId() {
        return paymentAllocationId;
    }

    public void setPaymentAllocationId(String paymentAllocationId) {
        this.paymentAllocationId = paymentAllocationId;
    }

    public String getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUploadedFrom() {
        return uploadedFrom;
    }

    public void setUploadedFrom(String uploadedFrom) {
        this.uploadedFrom = uploadedFrom;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
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