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
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ExportLOCDTO implements DBPDTO {
    private String drawingAmount;
    private String lcType;
    private String lcReferenceNo;
    private String issuingBankReference;
    private String advisingBankReference;
    private String applicant;
    private String utilizedLCAmount;
    private String issueDate;
    private String amount;
    private String expiryDate;
    private String currency;
    private String issuingBank;
    private String applicantaddress;
    private String issuingbankaddress;
    private String paymentTerms;
    private String documentName;
    private String uploadedFiles;
    private String forwardContract;
    private String beneficiaryName;
    private String beneficiaryAddress;
    private String goodsDescription;
    private String additionalConditions;
    private String confirmInstructions;
    private String latestShipmentDate;
    private String status;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String exportLCId;
    private String errorMsg;
    private String errorCode;
    @JsonIgnore
    @JsonAlias({PARAM_REQUEST_DATE_TIME})
    private String lcCreatedOn;
    private String lcUpdatedOn;
    private String customerId;
    private String amendmentNo;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;
    private String beneficiaryConsent;
    private String reasonForRejection;
    private String messageToBank;
    @JsonIgnore
    private String expiredOnFormatted;
    @JsonIgnore
    private String issueDateFormatted;
    @JsonIgnore
    private String latestShipmentDateFormatted;
    @JsonIgnore
    private String amountFormatted;

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(amount)) ? formatAmount(String.valueOf(amount)) : String.valueOf(amount);
    }

    public String getLatestShipmentDateFormatted() {
        return _formatDate(latestShipmentDate);
    }

    public String getIssueDateFormatted() {
        return _formatDate(issueDate);
    }

    public String getExpiredOnFormatted() {
        return _formatDate(expiryDate);
    }

    public String getTradeCurrency() {
        return currency;
    }

    public String getServiceRequestSrmsId() {
        return exportLCId;
    }

    public String getProduct() {
        return "Export LC";
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLcCreatedOn() {
        try {
            return HelperMethods.changeDateFormat(lcCreatedOn, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setLcCreatedOn(String lcCreatedOn) {
        this.lcCreatedOn = lcCreatedOn;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(String uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
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

    public ExportLOCDTO() {
        super();
    }

    public String getDrawingAmount() {
        return drawingAmount;
    }

    public void setDrawingAmount(String drawingAmount) {
        this.drawingAmount = drawingAmount;
    }

    public String getLcType() {
        return lcType;
    }

    public void setLcType(String lcType) {
        this.lcType = lcType;
    }

    public String getLcReferenceNo() {
        return lcReferenceNo;
    }

    public void setLcReferenceNo(String lcReferenceNo) {
        this.lcReferenceNo = lcReferenceNo;
    }

    public String getIssuingBankReference() {
        return issuingBankReference;
    }

    public void setIssuingBankReference(String issuingBankReference) {
        this.issuingBankReference = issuingBankReference;
    }

    public String getAdvisingBankReference() {
        return advisingBankReference;
    }

    public void setAdvisingBankReference(String advisingBankReference) {
        this.advisingBankReference = advisingBankReference;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getUtilizedLCAmount() {
        return utilizedLCAmount;
    }

    public void setUtilizedLCAmount(String utilizedLCAmount) {
        this.utilizedLCAmount = utilizedLCAmount;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getApplicantaddress() {
        return applicantaddress;
    }

    public void setApplicantaddress(String applicantaddress) {
        this.applicantaddress = applicantaddress;
    }

    public String getIssuingbankaddress() {
        return issuingbankaddress;
    }

    public void setIssuingbankaddress(String issuingbankaddress) {
        this.issuingbankaddress = issuingbankaddress;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getForwardContract() {
        return forwardContract;
    }

    public void setForwardContract(String forwardContract) {
        this.forwardContract = forwardContract;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getAdditionalConditions() {
        return additionalConditions;
    }

    public void setAdditionalConditions(String additionalConditions) {
        this.additionalConditions = additionalConditions;
    }

    public String getConfirmInstructions() {
        return confirmInstructions;
    }

    public void setConfirmInstructions(String confirmInstructions) {
        this.confirmInstructions = confirmInstructions;
    }

    public String getLatestShipmentDate() {
        return latestShipmentDate;
    }

    public void setLatestShipmentDate(String latestShipmentDate) {
        this.latestShipmentDate = latestShipmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExportLCId() {
        return exportLCId;
    }

    public void setExportLCId(String exportLCId) {
        this.exportLCId = exportLCId;
    }

    public String getBeneficiaryConsent() {
        return beneficiaryConsent;
    }

    public void setBeneficiaryConsent(String beneficiaryConsent) {
        this.beneficiaryConsent = beneficiaryConsent;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getLcUpdatedOn() {
        return lcUpdatedOn;
    }

    public void setLcUpdatedOn(String lcUpdatedOn) {
        this.lcUpdatedOn = lcUpdatedOn;
    }

}