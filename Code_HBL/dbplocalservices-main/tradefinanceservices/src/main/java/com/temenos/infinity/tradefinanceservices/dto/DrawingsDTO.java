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

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawingsDTO implements Serializable, DBPDTO {
    private String lcReferenceNo;
    private String lcType;
    private String drawingReferenceNo;
    private String beneficiaryName;
    private String documentStatus;
    @JsonAlias({PARAM_REQUEST_DATE_TIME})
    private String drawingCreationDate;
    private String drawingCurrency;
    private String drawingAmount;
    private String drawingStatus;
    private String lcAmount;
    private String lcCurrency;
    private String lcIssueDate;
    private String lcExpiryDate;
    private String paymentTerms;
    private String presentorReference;
    private String presentorName;
    private String documentsReceived;
    private String forwardContact;
    private String shippingGuaranteeReference;
    private String approvalDate;
    private String totalDocuments;
    private String documentName;
    private String discrepancyDescription;
    private String paymentStatus;
    private String rejectedDate;
    private String totalAmountToBePaid;
    private String accountToBeDebited;
    private String messageFromBank;
    private String messageToBank;
    private String totalPaidAmount;
    private String paymentDate;
    private String reasonForRejection;
    private String discrepancies;
    private String acceptance;
    private String messageType;
    private String deliveryDestination;
    private String messageDate;
    private String messageCategory;
    private String CustomerId;
    private String flowType;
    private String status;
    private String errorCode;
    private String errorMessage;
    private String lcSrmsReqOrderID;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String drawingsSrmsReqOrderID;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;
    @JsonIgnore
    private String createdOnFormatted;
    @JsonIgnore
    private String amountFormatted;

    public String getCreatedOnFormatted() {
        return _formatDate(drawingCreationDate);
    }

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(lcAmount)) ? formatAmount(String.valueOf(lcAmount)) : String.valueOf(lcAmount);
    }

    public String getTradeCurrency() {
        return drawingCurrency;
    }

    public String getServiceRequestSrmsId() {
        return drawingsSrmsReqOrderID;
    }

    public String getProduct() {
        return "Import Drawing";
    }

    public String getLcReferenceNo() {
        return lcReferenceNo;
    }

    public void setLcReferenceNo(String lcReferenceNo) {
        this.lcReferenceNo = lcReferenceNo;
    }

    public String getLcType() {
        return lcType;
    }

    public void setLcType(String lcType) {
        this.lcType = lcType;
    }

    public String getDrawingReferenceNo() {
        return drawingReferenceNo;
    }

    public void setDrawingReferenceNo(String drawingReferenceNo) {
        this.drawingReferenceNo = drawingReferenceNo;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDrawingCreationDate() {
        try {
            return HelperMethods.changeDateFormat(drawingCreationDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setDrawingCreationDate(String drawingCreationDate) {
        this.drawingCreationDate = drawingCreationDate;
    }

    public String getDrawingCurrency() {
        return drawingCurrency;
    }

    public void setDrawingCurrency(String drawingCurrency) {
        this.drawingCurrency = drawingCurrency;
    }

    public String getDrawingAmount() {
        return drawingAmount;
    }

    public void setDrawingAmount(String drawingAmount) {
        this.drawingAmount = drawingAmount;
    }

    public String getDrawingStatus() {
        return drawingStatus;
    }

    public void setDrawingStatus(String drawingStatus) {
        this.drawingStatus = drawingStatus;
    }

    public String getLcAmount() {
        return lcAmount;
    }

    public void setLcAmount(String lcAmount) {
        this.lcAmount = lcAmount;
    }

    public String getLcCurrency() {
        return lcCurrency;
    }

    public void setLcCurrency(String lcCurrency) {
        this.lcCurrency = lcCurrency;
    }

    public String getLcIssueDate() {
        try {
            return HelperMethods.changeDateFormat(lcIssueDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setLcIssueDate(String lcIssueDate) {
        this.lcIssueDate = lcIssueDate;
    }

    public String getLcExpiryDate() {
        try {
            return HelperMethods.changeDateFormat(lcExpiryDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setLcExpiryDate(String lcExpiryDate) {
        this.lcExpiryDate = lcExpiryDate;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getPresentorReference() {
        return presentorReference;
    }

    public void setPresentorReference(String presentorReference) {
        this.presentorReference = presentorReference;
    }

    public String getPresentorName() {
        return presentorName;
    }

    public void setPresentorName(String presentorName) {
        this.presentorName = presentorName;
    }

    public String getDocumentsReceived() {
        return documentsReceived;
    }

    public void setDocumentsReceived(String documentsReceived) {
        this.documentsReceived = documentsReceived;
    }

    public String getForwardContact() {
        return forwardContact;
    }

    public void setForwardContact(String forwardContact) {
        this.forwardContact = forwardContact;
    }

    public String getShippingGuaranteeReference() {
        return shippingGuaranteeReference;
    }

    public void setShippingGuaranteeReference(String shippingGuaranteeReference) {
        this.shippingGuaranteeReference = shippingGuaranteeReference;
    }

    public String getApprovalDate() {
        try {
            return HelperMethods.changeDateFormat(approvalDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(String totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDiscrepancyDescription() {
        return discrepancyDescription;
    }

    public void setDiscrepancyDescription(String discrepancyDescription) {
        this.discrepancyDescription = discrepancyDescription;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRejectedDate() {
        try {
            return HelperMethods.changeDateFormat(rejectedDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setRejectedDate(String rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getTotalAmountToBePaid() {
        return totalAmountToBePaid;
    }

    public void setTotalAmountToBePaid(String totalAmountToBePaid) {
        this.totalAmountToBePaid = totalAmountToBePaid;
    }

    public String getAccountToBeDebited() {
        return accountToBeDebited;
    }

    public void setAccountToBeDebited(String accountToBeDebited) {
        this.accountToBeDebited = accountToBeDebited;
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

    public String getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(String totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public String getPaymentDate() {
        try {
            return HelperMethods.changeDateFormat(paymentDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }

    public String getDiscrepancies() {
        return discrepancies;
    }

    public void setDiscrepancies(String discrepancies) {
        this.discrepancies = discrepancies;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getDeliveryDestination() {
        return deliveryDestination;
    }

    public void setDeliveryDestination(String deliveryDestination) {
        this.deliveryDestination = deliveryDestination;
    }

    public String getMessageDate() {
        try {
            return HelperMethods.changeDateFormat(messageDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(String messageCategory) {
        this.messageCategory = messageCategory;
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

    public String getLcSrmsReqOrderID() {
        return lcSrmsReqOrderID;
    }

    public void setLcSrmsReqOrderID(String lcSrmsReqOrderID) {
        this.lcSrmsReqOrderID = lcSrmsReqOrderID;
    }

    public String getDrawingsSrmsReqOrderID() {
        return drawingsSrmsReqOrderID;
    }

    public void setDrawingsSrmsReqOrderID(String drawingsSrmsReqOrderID) {
        this.drawingsSrmsReqOrderID = drawingsSrmsReqOrderID;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }
}
