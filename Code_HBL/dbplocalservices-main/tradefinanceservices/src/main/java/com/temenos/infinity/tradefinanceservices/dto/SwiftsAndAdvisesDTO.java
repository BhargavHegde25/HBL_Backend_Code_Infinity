/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_SRMSID;

public class SwiftsAndAdvisesDTO implements Serializable, DBPDTO {
    private String beneficiaryName;
    private String swiftMessage;
    private String swiftMessageType;
    private String swiftDate;
    private String swiftCategory;
    private String drawingsSrmsRequestOrderID;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String swiftsAndAdvicesSrmsRequestOrderID;
    private String customerId;
    private String flowType;
    private String status;

    private String sender;
    private String receiver;
    private String guaranteesAmendId;
    private String adviceName;
    private String messageDate;
    private String messageType;
    private String message;
    private String module;

    private String bCode;
    private String bic;
    private String transferDateOrTime;
    private String type;
    private String newSequence;
    private String requestedDateOfIssue;
    private String formOfUndertaking;
    private String applicableRules;
    private String typeOfUndertaking;
    private String expiryType;
    private String dateOfExpiry;
    private String expiryConditionOrEvent;
    private String applicant;
    private String obligorOrInstructingParty;
    private String issuer;
    private String beneficiary;
    private String undertakingAmount;
    private String additionalAmountInformation;
    private String availableWith;
    private String charges;
    private String documentAndPresentationInstructions;
    private String requestedLocalUndertakingTermsAndConditions;
    private String standardWordingRequired;
    private String standardWordingRequestedLanguage;
    private String governingLawAndOrPlaceOfJurisdiction;
    private String automaticExtensionPeriod;
    private String automaticExtensionNonExtensionPeriod;
    private String automaticExtensionNotificationPeriod;
    private String automaticExtensionFinalExpiryDate;
    private String demandIndicator;
    private String transferIndicator;
    private String transferConditions;
    private String underlyingTransactionDetails;
    private String deliveryOfLocalUndertaking;
    private String deliveryToOrCollectionBy;
    private String swiftMessagesReference;
    private String orderId;
    private String createdDate;

    private String errorCode;
    private String errorMessage;

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getSwiftMessage() {
        return swiftMessage;
    }

    public void setSwiftMessage(String swiftMessage) {
        this.swiftMessage = swiftMessage;
    }

    public String getSwiftMessageType() {
        return swiftMessageType;
    }

    public void setSwiftMessageType(String swiftMessageType) {
        this.swiftMessageType = swiftMessageType;
    }

    public String getSwiftDate() {
        return swiftDate;
    }

    public void setSwiftDate(String swiftDate) {
        this.swiftDate = swiftDate;
    }

    public String getSwiftCategory() {
        return swiftCategory;
    }

    public void setSwiftCategory(String swiftCategory) {
        this.swiftCategory = swiftCategory;
    }

    public String getDrawingsSrmsRequestOrderID() {
        return drawingsSrmsRequestOrderID;
    }

    public void setDrawingsSrmsRequestOrderID(String drawingsSrmsRequestOrderID) {
        this.drawingsSrmsRequestOrderID = drawingsSrmsRequestOrderID;
    }

    public String getSwiftsAndAdvicesSrmsRequestOrderID() {
        return swiftsAndAdvicesSrmsRequestOrderID;
    }

    public void setSwiftsAndAdvicesSrmsRequestOrderID(String swiftsAndAdvicesSrmsRequestOrderID) {
        this.swiftsAndAdvicesSrmsRequestOrderID = swiftsAndAdvicesSrmsRequestOrderID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getGuaranteesAmendId() {
        return guaranteesAmendId;
    }

    public void setGuaranteesAmendId(String guaranteesAmendId) {
        this.guaranteesAmendId = guaranteesAmendId;
    }

    public String getAdviceName() {
        return adviceName;
    }

    public void setAdviceName(String adviceName) {
        this.adviceName = adviceName;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getTransferDateOrTime() {
        return transferDateOrTime;
    }

    public void setTransferDateOrTime(String transferDateOrTime) {
        this.transferDateOrTime = transferDateOrTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewSequence() {
        return newSequence;
    }

    public void setNewSequence(String newSequence) {
        this.newSequence = newSequence;
    }

    public String getRequestedDateOfIssue() {
        return requestedDateOfIssue;
    }

    public void setRequestedDateOfIssue(String requestedDateOfIssue) {
        this.requestedDateOfIssue = requestedDateOfIssue;
    }

    public String getFormOfUndertaking() {
        return formOfUndertaking;
    }

    public void setFormOfUndertaking(String formOfUndertaking) {
        this.formOfUndertaking = formOfUndertaking;
    }

    public String getApplicableRules() {
        return applicableRules;
    }

    public void setApplicableRules(String applicableRules) {
        this.applicableRules = applicableRules;
    }

    public String getTypeOfUndertaking() {
        return typeOfUndertaking;
    }

    public void setTypeOfUndertaking(String typeOfUndertaking) {
        this.typeOfUndertaking = typeOfUndertaking;
    }

    public String getExpiryType() {
        return expiryType;
    }

    public void setExpiryType(String expiryType) {
        this.expiryType = expiryType;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getExpiryConditionOrEvent() {
        return expiryConditionOrEvent;
    }

    public void setExpiryConditionOrEvent(String expiryConditionOrEvent) {
        this.expiryConditionOrEvent = expiryConditionOrEvent;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getObligorOrInstructingParty() {
        return obligorOrInstructingParty;
    }

    public void setObligorOrInstructingParty(String obligorOrInstructingParty) {
        this.obligorOrInstructingParty = obligorOrInstructingParty;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getUndertakingAmount() {
        return undertakingAmount;
    }

    public void setUndertakingAmount(String undertakingAmount) {
        this.undertakingAmount = undertakingAmount;
    }

    public String getAdditionalAmountInformation() {
        return additionalAmountInformation;
    }

    public void setAdditionalAmountInformation(String additionalAmountInformation) {
        this.additionalAmountInformation = additionalAmountInformation;
    }

    public String getAvailableWith() {
        return availableWith;
    }

    public void setAvailableWith(String availableWith) {
        this.availableWith = availableWith;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getDocumentAndPresentationInstructions() {
        return documentAndPresentationInstructions;
    }

    public void setDocumentAndPresentationInstructions(String documentAndPresentationInstructions) {
        this.documentAndPresentationInstructions = documentAndPresentationInstructions;
    }

    public String getRequestedLocalUndertakingTermsAndConditions() {
        return requestedLocalUndertakingTermsAndConditions;
    }

    public void setRequestedLocalUndertakingTermsAndConditions(String requestedLocalUndertakingTermsAndConditions) {
        this.requestedLocalUndertakingTermsAndConditions = requestedLocalUndertakingTermsAndConditions;
    }

    public String getStandardWordingRequired() {
        return standardWordingRequired;
    }

    public void setStandardWordingRequired(String standardWordingRequired) {
        this.standardWordingRequired = standardWordingRequired;
    }

    public String getStandardWordingRequestedLanguage() {
        return standardWordingRequestedLanguage;
    }

    public void setStandardWordingRequestedLanguage(String standardWordingRequestedLanguage) {
        this.standardWordingRequestedLanguage = standardWordingRequestedLanguage;
    }

    public String getGoverningLawAndOrPlaceOfJurisdiction() {
        return governingLawAndOrPlaceOfJurisdiction;
    }

    public void setGoverningLawAndOrPlaceOfJurisdiction(String governingLawAndOrPlaceOfJurisdiction) {
        this.governingLawAndOrPlaceOfJurisdiction = governingLawAndOrPlaceOfJurisdiction;
    }

    public String getAutomaticExtensionPeriod() {
        return automaticExtensionPeriod;
    }

    public void setAutomaticExtensionPeriod(String automaticExtensionPeriod) {
        this.automaticExtensionPeriod = automaticExtensionPeriod;
    }

    public String getAutomaticExtensionNonExtensionPeriod() {
        return automaticExtensionNonExtensionPeriod;
    }

    public void setAutomaticExtensionNonExtensionPeriod(String automaticExtensionNonExtensionPeriod) {
        this.automaticExtensionNonExtensionPeriod = automaticExtensionNonExtensionPeriod;
    }

    public String getAutomaticExtensionNotificationPeriod() {
        return automaticExtensionNotificationPeriod;
    }

    public void setAutomaticExtensionNotificationPeriod(String automaticExtensionNotificationPeriod) {
        this.automaticExtensionNotificationPeriod = automaticExtensionNotificationPeriod;
    }

    public String getAutomaticExtensionFinalExpiryDate() {
        return automaticExtensionFinalExpiryDate;
    }

    public void setAutomaticExtensionFinalExpiryDate(String automaticExtensionFinalExpiryDate) {
        this.automaticExtensionFinalExpiryDate = automaticExtensionFinalExpiryDate;
    }

    public String getDemandIndicator() {
        return demandIndicator;
    }

    public void setDemandIndicator(String demandIndicator) {
        this.demandIndicator = demandIndicator;
    }

    public String getTransferIndicator() {
        return transferIndicator;
    }

    public void setTransferIndicator(String transferIndicator) {
        this.transferIndicator = transferIndicator;
    }

    public String getTransferConditions() {
        return transferConditions;
    }

    public void setTransferConditions(String transferConditions) {
        this.transferConditions = transferConditions;
    }

    public String getUnderlyingTransactionDetails() {
        return underlyingTransactionDetails;
    }

    public void setUnderlyingTransactionDetails(String underlyingTransactionDetails) {
        this.underlyingTransactionDetails = underlyingTransactionDetails;
    }

    public String getDeliveryOfLocalUndertaking() {
        return deliveryOfLocalUndertaking;
    }

    public void setDeliveryOfLocalUndertaking(String deliveryOfLocalUndertaking) {
        this.deliveryOfLocalUndertaking = deliveryOfLocalUndertaking;
    }

    public String getDeliveryToOrCollectionBy() {
        return deliveryToOrCollectionBy;
    }

    public void setDeliveryToOrCollectionBy(String deliveryToOrCollectionBy) {
        this.deliveryToOrCollectionBy = deliveryToOrCollectionBy;
    }

    public String getSwiftMessagesReference() {
        return swiftMessagesReference;
    }

    public void setSwiftMessagesReference(String swiftMessagesReference) {
        this.swiftMessagesReference = swiftMessagesReference;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
