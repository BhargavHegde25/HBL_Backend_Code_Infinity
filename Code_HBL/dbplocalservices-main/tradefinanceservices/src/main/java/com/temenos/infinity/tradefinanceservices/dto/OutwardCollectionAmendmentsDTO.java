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

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

/**
 * @author k.meiyazhagan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OutwardCollectionAmendmentsDTO implements DBPDTO {

    @JsonIgnore
    private String product;
    @JsonIgnore
    private String serviceRequestSrmsId;
    @JsonIgnore
    private String tradeCurrency;
    private String allowUsanceAcceptance;
    private String amendmentNo;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String amendmentReference;
    private String amendTenorType;
    private String amount;
    @JsonIgnore
    private String amountFormatted;
    private String cancellationStatus;
    private String chargesDebitAccount;
    private String collectingBank;
    private String collectionReference;
    private String corporateUserName;
    private String courierTrackingDetails;
    private String creditAccount;
    private String currency;
    private String customerId;
    private String documentNo;
    private String maturityDate;
    @JsonIgnore
    private String maturityDateFormatted;
    private String messageToBank;
    private String otherCollectionDetails;
    private String physicalDocuments;
    private String reasonForRejection;
    private String reasonForReturn;
    private String requestedOn;
    @JsonIgnore
    private String requestedOnFormatted;
    private String returnedHistory;
    private String status;
    private String tenorType;
    @JsonAlias({PARAM_LASTUPDATEDTIMESTAMP})
    private String updatedOn;
    @JsonIgnore
    private String updatedOnFormatted;
    private String uploadDocuments;
    private String usanceDays;
    private String usanceDetails;
    private String errorMsg;
    private String errorCode;
    private String dbpErrCode;
    private String dbpErrMsg;


    public String getTradeCurrency() {
        return currency;
    }

    public String getServiceRequestSrmsId() {
        return amendmentReference;
    }

    public String getProduct() {
        return "Outward Collection Amendment";
    }

    public String getAllowUsanceAcceptance() {
        return allowUsanceAcceptance;
    }

    public void setAllowUsanceAcceptance(String allowUsanceAcceptance) {
        this.allowUsanceAcceptance = allowUsanceAcceptance;
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public String getAmendmentReference() {
        return amendmentReference;
    }

    public void setAmendmentReference(String amendmentReference) {
        this.amendmentReference = amendmentReference;
    }

    public String getAmendTenorType() {
        return amendTenorType;
    }

    public void setAmendTenorType(String amendTenorType) {
        this.amendTenorType = amendTenorType;
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

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }

    public String getChargesDebitAccount() {
        return chargesDebitAccount;
    }

    public void setChargesDebitAccount(String chargesDebitAccount) {
        this.chargesDebitAccount = chargesDebitAccount;
    }

    public String getCollectingBank() {
        return collectingBank;
    }

    public void setCollectingBank(String collectingBank) {
        this.collectingBank = collectingBank;
    }

    public String getCollectionReference() {
        return collectionReference;
    }

    public void setCollectionReference(String collectionReference) {
        this.collectionReference = collectionReference;
    }

    public String getCorporateUserName() {
        return corporateUserName;
    }

    public void setCorporateUserName(String corporateUserName) {
        this.corporateUserName = corporateUserName;
    }

    public String getCourierTrackingDetails() {
        return courierTrackingDetails;
    }

    public void setCourierTrackingDetails(String courierTrackingDetails) {
        this.courierTrackingDetails = courierTrackingDetails;
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

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getMaturityDateFormatted() {
        return _formatDate(maturityDate);
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

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getRequestedOnFormatted() {
        return _formatDate(requestedOn);
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
}
