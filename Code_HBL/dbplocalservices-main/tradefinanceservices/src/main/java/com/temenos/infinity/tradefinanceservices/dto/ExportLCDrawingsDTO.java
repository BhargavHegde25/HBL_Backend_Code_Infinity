/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.constants.Constants;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ExportLCDrawingsDTO implements Serializable, DBPDTO {
    private String drawingAmount;
    private String lcReferenceNo;
    private String financeBill;
    private String currency;
    private String applicant;
    private String creditAccount;
    private String externalAccount;
    private String chargesDebitAccount;
    private String messageToBank;
    private String status;
    private String physicalDocuments;
    private String uploadedDocuments;
    private String forwardDocuments;
    private String returnedDocuments;
    private String exportLCId;
    private String advisingBankReference;
    private String lcType;
    private String lcAmount;
    private String expiryDate;
    private String totalDocuments;
    private String documentStatus;
    private String documentReference;
    private String discrepencies;
    private String discrepenciesAcceptance;
    private String paymentStatus;
    private String totalAmount;
    private String reasonForReturn;
    private String drawingReferenceNo;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String drawingSRMSRequestId;
    @JsonAlias({PARAM_REQUEST_DATE_TIME})
    private String drawingCreatedDate;
    private String discrepanciesHistory;
    private String discrepanciesHistory1;
    private String discrepanciesHistory2;
    private String discrepanciesHistory3;
    private String discrepanciesHistory4;
    private String discrepanciesHistory5;
    private String lcCurrency;
    private String lcIssueDate;
    private String issuingBank;
    private String paymentDate;
    private String messageFromBank;
    private String errorMessage;
    private String errorCode;
    private String customerId;
    private String returnedDate;
    private String approvedDate;
    private String returnMessageToBank;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;
    @JsonIgnore
    private String amountFormatted;
    @JsonIgnore
    private String createdOnFormatted;

    public String getCreatedOnFormatted() {
        return _formatDate(drawingCreatedDate);
    }

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(drawingAmount)) ? formatAmount(String.valueOf(drawingAmount)) : String.valueOf(drawingAmount);
    }

    public String getTradeCurrency() {
        return currency;
    }

    public String getServiceRequestSrmsId() {
        return drawingSRMSRequestId;
    }

    public String getProduct() {
        return "Export Drawing";
    }

    public String getDrawingAmount() {
        return drawingAmount;
    }

    public void setDrawingAmount(String drawingAmount) {
        this.drawingAmount = drawingAmount;
    }

    public String getLcReferenceNo() {
        return lcReferenceNo;
    }

    public void setLcReferenceNo(String lcReferenceNo) {
        this.lcReferenceNo = lcReferenceNo;
    }

    public String getFinanceBill() {
        return financeBill;
    }

    public void setFinanceBill(String financeBill) {
        this.financeBill = financeBill;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getExternalAccount() {
        return externalAccount;
    }

    public void setExternalAccount(String externalAccount) {
        this.externalAccount = externalAccount;
    }

    public String getChargesDebitAccount() {
        return chargesDebitAccount;
    }

    public void setChargesDebitAccount(String chargesDebitAccount) {
        this.chargesDebitAccount = chargesDebitAccount;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhysicalDocuments() {
        return physicalDocuments;
    }

    public void setPhysicalDocuments(String physicalDocuments) {
        this.physicalDocuments = physicalDocuments;
    }

    public String getUploadedDocuments() {
        return uploadedDocuments;
    }

    public void setUploadedDocuments(String uploadedDocuments) {
        this.uploadedDocuments = uploadedDocuments;
    }

    public String getForwardDocuments() {
        return forwardDocuments;
    }

    public void setForwardDocuments(String forwardDocuments) {
        this.forwardDocuments = forwardDocuments;
    }

    public String getReturnedDocuments() {
        return returnedDocuments;
    }

    public void setReturnedDocuments(String returnedDocuments) {
        this.returnedDocuments = returnedDocuments;
    }

    public String getExportLCId() {
        return exportLCId;
    }

    public void setExportLCId(String exportLCId) {
        this.exportLCId = exportLCId;
    }

    public String getAdvisingBankReference() {
        return advisingBankReference;
    }

    public void setAdvisingBankReference(String advisingBankReference) {
        this.advisingBankReference = advisingBankReference;
    }

    public String getLcType() {
        return lcType;
    }

    public void setLcType(String lcType) {
        this.lcType = lcType;
    }

    public String getLcAmount() {
        return lcAmount;
    }

    public void setLcAmount(String lcAmount) {
        this.lcAmount = lcAmount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(String totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    public String getDiscrepencies() {
        return discrepencies;
    }

    public void setDiscrepencies(String discrepencies) {
        this.discrepencies = discrepencies;
    }

    public String getDiscrepenciesAcceptance() {
        return discrepenciesAcceptance;
    }

    public void setDiscrepenciesAcceptance(String discrepenciesAcceptance) {
        this.discrepenciesAcceptance = discrepenciesAcceptance;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReasonForReturn() {
        return reasonForReturn;
    }

    public void setReasonForReturn(String reasonForReturn) {
        this.reasonForReturn = reasonForReturn;
    }

    public String getDrawingReferenceNo() {
        return drawingReferenceNo;
    }

    public String getDrawingSRMSRequestId() {
        return drawingSRMSRequestId;
    }

    public void setDrawingSRMSRequestId(String drawingSRMSRequestId) {
        this.drawingReferenceNo = drawingSRMSRequestId;
        this.drawingSRMSRequestId = drawingSRMSRequestId;
    }

    public String getDrawingCreatedDate() {
        try {
            return HelperMethods.changeDateFormat(drawingCreatedDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setDrawingCreatedDate(String drawingCreatedDate) {
        this.drawingCreatedDate = drawingCreatedDate;
    }

    public String getDiscrepanciesHistory() {
        return discrepanciesHistory;
    }

    public void setDiscrepanciesHistory(String discrepanciesHistory) {
        this.discrepanciesHistory = discrepanciesHistory;
    }

    public String getDiscrepanciesHistory1() {
        return discrepanciesHistory1;
    }

    public void setDiscrepanciesHistory1(String discrepanciesHistory1) {
        this.discrepanciesHistory1 = discrepanciesHistory1;
    }

    public String getDiscrepanciesHistory2() {
        return discrepanciesHistory2;
    }

    public void setDiscrepanciesHistory2(String discrepanciesHistory2) {
        this.discrepanciesHistory2 = discrepanciesHistory2;
    }

    public String getDiscrepanciesHistory3() {
        return discrepanciesHistory3;
    }

    public void setDiscrepanciesHistory3(String discrepanciesHistory3) {
        this.discrepanciesHistory3 = discrepanciesHistory3;
    }

    public String getDiscrepanciesHistory4() {
        return discrepanciesHistory4;
    }

    public void setDiscrepanciesHistory4(String discrepanciesHistory4) {
        this.discrepanciesHistory4 = discrepanciesHistory4;
    }

    public String getDiscrepanciesHistory5() {
        return discrepanciesHistory5;
    }

    public void setDiscrepanciesHistory5(String discrepanciesHistory5) {
        this.discrepanciesHistory5 = discrepanciesHistory5;
    }

    public String getLcCurrency() {
        return lcCurrency;
    }

    public void setLcCurrency(String lcCurrency) {
        this.lcCurrency = lcCurrency;
    }

    public String getLcIssueDate() {
        return lcIssueDate;
    }

    public void setLcIssueDate(String lcIssueDate) {
        this.lcIssueDate = lcIssueDate;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getMessageFromBank() {
        return messageFromBank;
    }

    public void setMessageFromBank(String messageFromBank) {
        this.messageFromBank = messageFromBank;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(String returnedDate) {
        this.returnedDate = returnedDate;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getReturnMessageToBank() {
        return returnMessageToBank;
    }

    public void setReturnMessageToBank(String returnMessageToBank) {
        this.returnMessageToBank = returnMessageToBank;
    }

}