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
public class InwardCollectionAmendmentsDTO implements Serializable, DBPDTO {
    private String amendAmount;
    private String amendDocuments;
    private String amendMaturityDate;
    private String amendRemittingBank;
    private String amendTenorType;
    private String amendUsanceDetails;
    private String amendmentNo;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID, "amendmentSrmsId", "srmsId"})
    private String amendmentSrmsId;
    private String amount;
    private String cancellationStatus;
    private String createdDate;
    private String collectionSrmsId;
    private String currency;
    private String draweeAcknowledgement;
    private String draweeAcknowledgementDate;
    private String drawer;
    private String maturityDate;
    private String messageFromBank;
    private String messageToBank;
    private String reasonForCancellation;
    private String reasonForRejection;
    private String reasonForReturn;
    private String receivedOn;
    private String remittingBank;
    private String status;
    private String tenorType;
    private String transactionReference;
    @JsonAlias({PARAM_LASTUPDATEDTIMESTAMP})
    private String lastUpdatedDate;
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
    private String maturityDateFormatted;

    public String getMaturityDateFormatted() {
        return _formatDate(receivedOn);
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
        return amendmentSrmsId;
    }

    public String getProduct() {
        return "Inward Collection Amendment";
    }

    public InwardCollectionAmendmentsDTO() {
    }

    public String getAmendAmount() {
        return amendAmount;
    }

    public void setAmendAmount(String amendAmount) {
        this.amendAmount = amendAmount;
    }

    public String getAmendDocuments() {
        return amendDocuments;
    }

    public void setAmendDocuments(String amendDocuments) {
        this.amendDocuments = amendDocuments;
    }

    public String getAmendMaturityDate() {
        return amendMaturityDate;
    }

    public void setAmendMaturityDate(String amendMaturityDate) {
        this.amendMaturityDate = amendMaturityDate;
    }

    public String getAmendRemittingBank() {
        return amendRemittingBank;
    }

    public void setAmendRemittingBank(String amendRemittingBank) {
        this.amendRemittingBank = amendRemittingBank;
    }

    public String getAmendTenorType() {
        return amendTenorType;
    }

    public void setAmendTenorType(String amendTenorType) {
        this.amendTenorType = amendTenorType;
    }

    public String getAmendUsanceDetails() {
        return amendUsanceDetails;
    }

    public void setAmendUsanceDetails(String amendUsanceDetails) {
        this.amendUsanceDetails = amendUsanceDetails;
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public String getAmendmentSrmsId() {
        return amendmentSrmsId;
    }

    public void setAmendmentSrmsId(String amendmentSrmsId) {
        this.amendmentSrmsId = amendmentSrmsId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCollectionSrmsId() {
        return collectionSrmsId;
    }

    public void setCollectionSrmsId(String collectionSrmsId) {
        this.collectionSrmsId = collectionSrmsId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
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

    public String getReasonForCancellation() {
        return reasonForCancellation;
    }

    public void setReasonForCancellation(String reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
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

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
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
