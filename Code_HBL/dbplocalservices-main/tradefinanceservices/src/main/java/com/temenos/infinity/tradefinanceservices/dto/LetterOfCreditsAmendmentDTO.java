/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.ParseException;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.formatAmount;

/**
 * @author k.meiyazhagan
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LetterOfCreditsAmendmentDTO implements Serializable, DBPDTO {

    private String lcReferenceNo;
    private double lcAmount;
    private String lcCurrency;
    private String tolerancePercentage;
    private double maximumCreditAmount;
    private String additionalAmountPayable;
    private String paymentTerms;
    private String availableWith1;
    private String availableWith2;
    private String availableWith3;
    private String availableWith4;
    private String issueDate;
    private String expiryDate;
    private String expiryPlace;
    private String chargesAccount;
    private String commisionAccount;
    private String marginAccount;
    private String messageToBank;
    private String beneficiaryName;
    private String beneficiaryAddressLine1;
    private String beneficiaryAddressLine2;
    private String beneficiaryPostCode;
    private String beneficiaryCountry;
    private String beneficiaryCity;
    private String beneficiaryState;
    private String beneficiaryBank;
    private String beneficiaryBankAdressLine1;
    private String beneficiaryBankAdressLine2;
    private String beneficiaryBankPostCode;
    private String beneficiaryBankCountry;
    private String beneficiaryBankCity;
    private String beneficiaryBankState;
    private String placeOfTakingIncharge;
    private String portOfLoading;
    private String portOfDischarge;
    private String placeOfFinalDelivery;
    private String latestShippingDate;
    private String presentationPeriod;
    private String transshipment;
    private String partialShipments;
    private String incoTerms;
    private String modeOfShipment;
    private String descriptionOfGoods;
    private String documentsRequired;
    private String additionalConditionsCode;
    private String otherAdditionalConditions;
    private String documentCharges;
    private String supportDocuments;
    private String fileToUpload;
    private String confirmationInstruction;
    private String transferable;
    private String standByLC;
    private String isDraft;
    private String additionalPayableCurrency;
    private String code;
    private String msg;
    private String flowType;
    private String id;
    private String status;
    private String ErrorCode;
    private String ErrorMessage;
    private String srmsReqOrderID;
    private String lcCreatedOn;
    private boolean referenceNomatch;
    private String ErrorCodeSRMSmatch;
    private String ErrorMsgSRMSmatch;
    private String CustomerId;
    @JsonIgnore
    private String errorDetails;
    @JsonIgnore
    private String createdOnFormatted;
    @JsonIgnore
    private String issuedOnFormatted;
    @JsonIgnore
    private String expiredOnFormatted;
    @JsonIgnore
    private String amountFormatted;
    @JsonIgnore
    private String amendDateFormatted;
    @JsonIgnore
    private String amendApprovedDateFormatted;
    private String screenNumber;
    private String draftCount;
    private String draftAmount;
    private String deletedCount;
    private String deletedAmount;
    private String selfApprovedCount;
    private String selfApprovedAmount;
    private String totalCount;
    private String totalAmount;
    private String signatoryApprovalRequired;

    // for amendmentspurpose
    private String amountType;
    private String otherAmendments;
    private String amendCharges;
    private String chargesPaid;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String amendmentReference;
    @JsonAlias({PARAM_LASTUPDATEDTIMESTAMP})
    private String amendmentDate;
    private String amendmentApprovedDate;
    private String creditAmount;
    private String amendmentExpiryDate;
    private String importLCId;
    private String amendStatus;
    private String lcSRMSId;
    private String utilizedAmount;
    private String transactReference;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;

    public String getAmendApprovedDateFormatted() {
        return _formatDate(amendmentApprovedDate);
    }

    public String getAmendDateFormatted() {
        return _formatDate(amendmentDate);
    }

    public String getTradeCurrency() {
        return lcCurrency;
    }

    public String getServiceRequestSrmsId() {
        return amendmentReference;
    }

    public String getProduct() {
        return "Import Amendment";
    }

    public String getLcReferenceNo() {
        return lcReferenceNo;
    }

    public String getUtilizedAmount() {
        return utilizedAmount;
    }

    public void setUtilizedAmount(String utilizedAmount) {
        this.utilizedAmount = utilizedAmount;
    }

    public void setLcReferenceNo(String lcReferenceNo) {
        this.lcReferenceNo = lcReferenceNo;
    }

    public double getLcAmount() {
        return lcAmount;
    }

    public void setLcAmount(double lcAmount) {
        this.lcAmount = lcAmount;
    }

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(lcAmount)) ? formatAmount(String.valueOf(lcAmount)) : String.valueOf(lcAmount);
    }

    public String getLcCurrency() {
        return lcCurrency;
    }

    public void setLcCurrency(String lcCurrency) {
        this.lcCurrency = lcCurrency;
    }

    public String getTolerancePercentage() {
        return tolerancePercentage;
    }

    public void setTolerancePercentage(String tolerancePercentage) {
        this.tolerancePercentage = tolerancePercentage;
    }

    public double getMaximumCreditAmount() {
        return maximumCreditAmount;
    }

    public void setMaximumCreditAmount(double maximumCreditAmount) {
        this.maximumCreditAmount = maximumCreditAmount;
    }

    public String getAdditionalAmountPayable() {
        return additionalAmountPayable;
    }

    public void setAdditionalAmountPayable(String additionalAmountPayable) {
        this.additionalAmountPayable = additionalAmountPayable;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getAvailableWith1() {
        return availableWith1;
    }

    public void setAvailableWith1(String availableWith1) {
        this.availableWith1 = availableWith1;
    }

    public String getAvailableWith2() {
        return availableWith2;
    }

    public void setAvailableWith2(String availableWith2) {
        this.availableWith2 = availableWith2;
    }

    public String getAvailableWith3() {
        return availableWith3;
    }

    public void setAvailableWith3(String availableWith3) {
        this.availableWith3 = availableWith3;
    }

    public String getAvailableWith4() {
        return availableWith4;
    }

    public void setAvailableWith4(String availableWith4) {
        this.availableWith4 = availableWith4;
    }

    public String getIssueDate() {
        try {
            return HelperMethods.changeDateFormat(issueDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuedOnFormatted() {
        return _formatDate(issueDate);
    }

    public String getExpiryDate() {
        try {
            return HelperMethods.changeDateFormat(expiryDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiredOnFormatted() {
        return _formatDate(expiryDate);
    }

    public String getExpiryPlace() {
        return expiryPlace;
    }

    public void setExpiryPlace(String expiryPlace) {
        this.expiryPlace = expiryPlace;
    }

    public String getChargesAccount() {
        return chargesAccount;
    }

    public void setChargesAccount(String chargesAccount) {
        this.chargesAccount = chargesAccount;
    }

    public String getCommisionAccount() {
        return commisionAccount;
    }

    public void setCommisionAccount(String commisionAccount) {
        this.commisionAccount = commisionAccount;
    }

    public String getMarginAccount() {
        return marginAccount;
    }

    public void setMarginAccount(String marginAccount) {
        this.marginAccount = marginAccount;
    }

    public String getMessageToBank() {
        return messageToBank;
    }

    public void setMessageToBank(String messageToBank) {
        this.messageToBank = messageToBank;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryAddressLine1() {
        return beneficiaryAddressLine1;
    }

    public void setBeneficiaryAddressLine1(String beneficiaryAddressLine1) {
        this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
    }

    public String getBeneficiaryAddressLine2() {
        return beneficiaryAddressLine2;
    }

    public void setBeneficiaryAddressLine2(String beneficiaryAddressLine2) {
        this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
    }

    public String getBeneficiaryPostCode() {
        return beneficiaryPostCode;
    }

    public void setBeneficiaryPostCode(String beneficiaryPostCode) {
        this.beneficiaryPostCode = beneficiaryPostCode;
    }

    public String getBeneficiaryCountry() {
        return beneficiaryCountry;
    }

    public void setBeneficiaryCountry(String beneficiaryCountry) {
        this.beneficiaryCountry = beneficiaryCountry;
    }

    public String getBeneficiaryCity() {
        return beneficiaryCity;
    }

    public void setBeneficiaryCity(String beneficiaryCity) {
        this.beneficiaryCity = beneficiaryCity;
    }

    public String getBeneficiaryState() {
        return beneficiaryState;
    }

    public void setBeneficiaryState(String beneficiaryState) {
        this.beneficiaryState = beneficiaryState;
    }

    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }

    public void setBeneficiaryBank(String beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }

    public String getBeneficiaryBankAdressLine1() {
        return beneficiaryBankAdressLine1;
    }

    public void setBeneficiaryBankAdressLine1(String beneficiaryBankAdressLine1) {
        this.beneficiaryBankAdressLine1 = beneficiaryBankAdressLine1;
    }

    public String getBeneficiaryBankAdressLine2() {
        return beneficiaryBankAdressLine2;
    }

    public void setBeneficiaryBankAdressLine2(String beneficiaryBankAdressLine2) {
        this.beneficiaryBankAdressLine2 = beneficiaryBankAdressLine2;
    }

    public String getBeneficiaryBankPostCode() {
        return beneficiaryBankPostCode;
    }

    public void setBeneficiaryBankPostCode(String beneficiaryBankPostCode) {
        this.beneficiaryBankPostCode = beneficiaryBankPostCode;
    }

    public String getBeneficiaryBankCountry() {
        return beneficiaryBankCountry;
    }

    public void setBeneficiaryBankCountry(String beneficiaryBankCountry) {
        this.beneficiaryBankCountry = beneficiaryBankCountry;
    }

    public String getBeneficiaryBankCity() {
        return beneficiaryBankCity;
    }

    public void setBeneficiaryBankCity(String beneficiaryBankCity) {
        this.beneficiaryBankCity = beneficiaryBankCity;
    }

    public String getBeneficiaryBankState() {
        return beneficiaryBankState;
    }

    public void setBeneficiaryBankState(String beneficiaryBankState) {
        this.beneficiaryBankState = beneficiaryBankState;
    }

    public String getPlaceOfTakingIncharge() {
        return placeOfTakingIncharge;
    }

    public void setPlaceOfTakingIncharge(String placeOfTakingIncharge) {
        this.placeOfTakingIncharge = placeOfTakingIncharge;
    }

    public String getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public String getPlaceOfFinalDelivery() {
        return placeOfFinalDelivery;
    }

    public void setPlaceOfFinalDelivery(String placeOfFinalDelivery) {
        this.placeOfFinalDelivery = placeOfFinalDelivery;
    }

    public String getLatestShippingDate() {
        try {
            return HelperMethods.changeDateFormat(latestShippingDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setLatestShippingDate(String latestShippingDate) {
        this.latestShippingDate = latestShippingDate;
    }

    public String getPresentationPeriod() {
        return presentationPeriod;
    }

    public void setPresentationPeriod(String presentationPeriod) {
        this.presentationPeriod = presentationPeriod;
    }

    public String getTransshipment() {
        return transshipment;
    }

    public void setTransshipment(String transshipment) {
        this.transshipment = transshipment;
    }

    public String getPartialShipments() {
        return partialShipments;
    }

    public void setPartialShipments(String partialShipments) {
        this.partialShipments = partialShipments;
    }

    public String getIncoTerms() {
        return incoTerms;
    }

    public void setIncoTerms(String incoTerms) {
        this.incoTerms = incoTerms;
    }

    public String getModeOfShipment() {
        return modeOfShipment;
    }

    public void setModeOfShipment(String modeOfShipment) {
        this.modeOfShipment = modeOfShipment;
    }

    public String getDescriptionOfGoods() {
        return descriptionOfGoods;
    }

    public void setDescriptionOfGoods(String descriptionOfGoods) {
        this.descriptionOfGoods = descriptionOfGoods;
    }

    public String getDocumentsRequired() {
        return documentsRequired;
    }

    public void setDocumentsRequired(String documentsRequired) {
        this.documentsRequired = documentsRequired;
    }

    public String getAdditionalConditionsCode() {
        return additionalConditionsCode;
    }

    public void setAdditionalConditionsCode(String additionalConditionsCode) {
        this.additionalConditionsCode = additionalConditionsCode;
    }

    public String getOtherAdditionalConditions() {
        return otherAdditionalConditions;
    }

    public void setOtherAdditionalConditions(String otherAdditionalConditions) {
        this.otherAdditionalConditions = otherAdditionalConditions;
    }

    public String getDocumentCharges() {
        return documentCharges;
    }

    public void setDocumentCharges(String documentCharges) {
        this.documentCharges = documentCharges;
    }

    public String getSupportDocuments() {
        return supportDocuments;
    }

    public void setSupportDocuments(String supportDocuments) {
        this.supportDocuments = supportDocuments;
    }

    public String getFileToUpload() {
        return fileToUpload;
    }

    public void setFileToUpload(String fileToUpload) {
        this.fileToUpload = fileToUpload;
    }

    public String getConfirmationInstruction() {
        return confirmationInstruction;
    }

    public void setConfirmationInstruction(String confirmationInstruction) {
        this.confirmationInstruction = confirmationInstruction;
    }

    public String getTransferable() {
        return transferable;
    }

    public void setTransferable(String transferable) {
        this.transferable = transferable;
    }

    public String getStandByLC() {
        return standByLC;
    }

    public void setStandByLC(String standByLC) {
        this.standByLC = standByLC;
    }

    public String getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(String isDraft) {
        this.isDraft = isDraft;
    }

    public String getAdditionalPayableCurrency() {
        return additionalPayableCurrency;
    }

    public void setAdditionalPayableCurrency(String additionalPayableCurrency) {
        this.additionalPayableCurrency = additionalPayableCurrency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getSrmsReqOrderID() {
        return srmsReqOrderID;
    }

    public void setSrmsReqOrderID(String srmsReqOrderID) {
        this.srmsReqOrderID = srmsReqOrderID;
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

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getCreatedOnFormatted() {
        return _formatDate(lcCreatedOn);
    }

    public boolean isReferenceNomatch() {
        return referenceNomatch;
    }

    public void setReferenceNomatch(boolean referenceNomatch) {
        this.referenceNomatch = referenceNomatch;
    }

    public String getErrorCodeSRMSmatch() {
        return ErrorCodeSRMSmatch;
    }

    public void setErrorCodeSRMSmatch(String errorCodeSRMSmatch) {
        ErrorCodeSRMSmatch = errorCodeSRMSmatch;
    }

    public String getErrorMsgSRMSmatch() {
        return ErrorMsgSRMSmatch;
    }

    public void setErrorMsgSRMSmatch(String errorMsgSRMSmatch) {
        ErrorMsgSRMSmatch = errorMsgSRMSmatch;
    }

    public String getDraftCount() {
        return draftCount;
    }

    public void setDraftCount(String draftCount) {
        this.draftCount = draftCount;
    }

    public String getDraftAmount() {
        return draftAmount;
    }

    public void setDraftAmount(String draftAmount) {
        this.draftAmount = draftAmount;
    }

    public String getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(String deletedCount) {
        this.deletedCount = deletedCount;
    }

    public String getDeletedAmount() {
        return deletedAmount;
    }

    public void setDeletedAmount(String deletedAmount) {
        this.deletedAmount = deletedAmount;
    }

    public String getSelfApprovedCount() {
        return selfApprovedCount;
    }

    public void setSelfApprovedCount(String selfApprovedCount) {
        this.selfApprovedCount = selfApprovedCount;
    }

    public String getSelfApprovedAmount() {
        return selfApprovedAmount;
    }

    public void setSelfApprovedAmount(String selfApprovedAmount) {
        this.selfApprovedAmount = selfApprovedAmount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSignatoryApprovalRequired() {
        return signatoryApprovalRequired;
    }

    public void setSignatoryApprovalRequired(String signatoryApprovalRequired) {
        this.signatoryApprovalRequired = signatoryApprovalRequired;
    }

    // new getter setter

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getOtherAmendments() {
        return otherAmendments;
    }

    public void setOtherAmendments(String otherAmendments) {
        this.otherAmendments = otherAmendments;
    }

    public String getAmendCharges() {
        return amendCharges;
    }

    public void setAmendCharges(String amendCharges) {
        this.amendCharges = amendCharges;
    }

    public String getChargesPaid() {
        return chargesPaid;
    }

    public void setChargesPaid(String chargesPaid) {
        this.chargesPaid = chargesPaid;
    }

    public String getAmendmentReference() {
        return amendmentReference;
    }

    public void setAmendmentReference(String amendmentReference) {
        this.amendmentReference = amendmentReference;
        this.serviceRequestSrmsId = amendmentReference;
    }

    public String getAmendmentDate() {
        try {
            return HelperMethods.changeDateFormat(amendmentDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setAmendmentDate(String amendmentDate) {
        this.amendmentDate = amendmentDate;
    }

    public String getAmendmentApprovedDate() {
        try {
            return HelperMethods.changeDateFormat(amendmentApprovedDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setAmendmentApprovedDate(String amendmentApprovedDate) {
        this.amendmentApprovedDate = amendmentApprovedDate;
    }

    public String getAmendmentExpiryDate() {
        try {
            return HelperMethods.changeDateFormat(amendmentExpiryDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setAmendmentExpiryDate(String amendmentExpiryDate) {
        this.amendmentExpiryDate = amendmentExpiryDate;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getImportLCId() {
        return importLCId;
    }

    public void setImportLCId(String importLCId) {
        this.importLCId = importLCId;
    }

    public String getAmendStatus() {
        return amendStatus;
    }

    public void setAmendStatus(String amendStatus) {
        this.amendStatus = amendStatus;
    }

    public String getLcSRMSId() {
        return lcSRMSId;
    }

    public void setLcSRMSId(String lcSRMSId) {
        this.lcSRMSId = lcSRMSId;
    }

    public String getTransactReference() {
        return transactReference;
    }

    public void setTransactReference(String transactReference) {
        this.transactReference = transactReference;
    }

    public String getScreenNumber() {
        return screenNumber;
    }

    public void setScreenNumber(String screenNumber) {
        this.screenNumber = screenNumber;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

}