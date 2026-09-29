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

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_SRMSID;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils._formatDate;

import java.text.ParseException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ExportLCAmendmentsDTO implements DBPDTO {
    private String applicantName;
    private String exportlcReferenceNo;
    private String exportlcSRMSRequestId;
    private String amendmentStatus;
    private String amendmentNo;
    private String lcType;
    private String lcIssueDate;
    private String lcExpiryDate;
    private String lcCurrency;
    private String lcAmountStatus;
    private String oldLcAmount;
    private String newLcAmount;
    private String latestShipmentDate;
    private String periodOfPresentation;
    private String otherAmendments;
    private String amendmentChargesPayer;
    private String chargesDebitAccount;
    private String amendmentReferenceNo;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String amendmentSRMSRequestId;
    private String amendmentReceivedDate;
    private String selfAcceptance;
    private String selfAcceptanceDate;
    private String selfRejectedDate;
    private String reasonForSelfRejection;
    private String errorCode;
    private String errorMessage;
    private String product;
    private String serviceRequestSrmsId;
    private String tradeCurrency;
    @JsonIgnore
    private String amendReceivedDateFormatted;
    @JsonIgnore
    private String issuedDateFormatted;
    @JsonIgnore
    private String expiredDateFormatted;

    public String getExpiredDateFormatted() {
        return _formatDate(lcExpiryDate);
    }

    public String getIssuedDateFormatted() {
        return _formatDate(lcIssueDate);
    }

    public String getAmendReceivedDateFormatted() {
        return _formatDate(amendmentReceivedDate);
    }

    public String getTradeCurrency() {
        return lcCurrency;
    }

    public String getServiceRequestSrmsId() {
        return amendmentSRMSRequestId;
    }

    public String getProduct() {
        return "Export Amendment";
    }

    public String getExportlcReferenceNo() {
        return exportlcReferenceNo;
    }

    public void setExportlcReferenceNo(String exportlcReferenceNo) {
        this.exportlcReferenceNo = exportlcReferenceNo;
    }

    public String getExportlcSRMSRequestId() {
        return exportlcSRMSRequestId;
    }

    public void setExportlcSRMSRequestId(String exportlcSRMSRequestId) {
        this.exportlcSRMSRequestId = exportlcSRMSRequestId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getAmendmentStatus() {
        return amendmentStatus;
    }

    public void setAmendmentStatus(String amendmentStatus) {
        this.amendmentStatus = amendmentStatus;
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public String getLcType() {
        return lcType;
    }

    public void setLcType(String lcType) {
        this.lcType = lcType;
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

    public String getLcCurrency() {
        return lcCurrency;
    }

    public void setLcCurrency(String lcCurrency) {
        this.lcCurrency = lcCurrency;
    }

    public String getLcAmountStatus() {
        return lcAmountStatus;
    }

    public void setLcAmountStatus(String lcAmountStatus) {
        this.lcAmountStatus = lcAmountStatus;
    }

    public String getOldLcAmount() {
        return oldLcAmount;
    }

    public void setOldLcAmount(String oldLcAmount) {
        this.oldLcAmount = oldLcAmount;
    }

    public String getNewLcAmount() {
        return newLcAmount;
    }

    public void setNewLcAmount(String newLcAmount) {
        this.newLcAmount = newLcAmount;
    }

    public String getLatestShipmentDate() {
        try {
            return HelperMethods.changeDateFormat(latestShipmentDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setLatestShipmentDate(String latestShipmentDate) {
        this.latestShipmentDate = latestShipmentDate;
    }

    public String getPeriodOfPresentation() {
        return periodOfPresentation;
    }

    public void setPeriodOfPresentation(String periodOfPresentation) {
        this.periodOfPresentation = periodOfPresentation;
    }

    public String getOtherAmendments() {
        return otherAmendments;
    }

    public void setOtherAmendments(String otherAmendments) {
        this.otherAmendments = otherAmendments;
    }

    public String getAmendmentChargesPayer() {
        return amendmentChargesPayer;
    }

    public void setAmendmentChargesPayer(String amendmentChargesPayer) {
        this.amendmentChargesPayer = amendmentChargesPayer;
    }

    public String getChargesDebitAccount() {
        return chargesDebitAccount;
    }

    public void setChargesDebitAccount(String chargesDebitAccount) {
        this.chargesDebitAccount = chargesDebitAccount;
    }

    public String getAmendmentReferenceNo() {
        return amendmentSRMSRequestId;
    }

    public String getAmendmentSRMSRequestId() {
        return amendmentSRMSRequestId;
    }

    public void setAmendmentSRMSRequestId(String amendmentSRMSRequestId) {
        this.amendmentSRMSRequestId = amendmentSRMSRequestId;
    }

    public String getAmendmentReceivedDate() {
        try {
            return HelperMethods.changeDateFormat(amendmentReceivedDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setAmendmentReceivedDate(String amendmentReceivedDate) {
        this.amendmentReceivedDate = amendmentReceivedDate;
    }

    public String getSelfAcceptance() {
        return selfAcceptance;
    }

    public void setSelfAcceptance(String selfAcceptance) {
        this.selfAcceptance = selfAcceptance;
    }

    public String getSelfAcceptanceDate() {
        try {
            return HelperMethods.changeDateFormat(selfAcceptanceDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setSelfAcceptanceDate(String selfAcceptanceDate) {
        this.selfAcceptanceDate = selfAcceptanceDate;
    }

    public String getSelfRejectedDate() {
        try {
            return HelperMethods.changeDateFormat(selfRejectedDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setSelfRejectedDate(String selfRejectedDate) {
        this.selfRejectedDate = selfRejectedDate;
    }

    public String getReasonForSelfRejection() {
        return reasonForSelfRejection;
    }

    public void setReasonForSelfRejection(String reasonForSelfRejection) {
        this.reasonForSelfRejection = reasonForSelfRejection;
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

    public ExportLCAmendmentsDTO() {
        super();
    }

    public ExportLCAmendmentsDTO(String applicantName, String exportlcReferenceNo, String exportlcSRMSRequestId,
                                 String amendmentStatus, String amendmentNo, String lcType, String lcIssueDate, String lcExpiryDate,
                                 String lcCurrency, String lcAmountStatus, String oldLcAmount, String newLcAmount, String latestShipmentDate,
                                 String periodOfPresentation, String otherAmendments, String amendmentChargesPayer,
                                 String chargesDebitAccount, String amendmentReferenceNo, String amendmentSRMSRequestId,
                                 String amendmentReceivedDate, String selfAcceptance, String selfAcceptanceDate, String selfRejectedDate,
                                 String reasonForSelfRejection, String errorCode, String errorMessage) {
        super();
        this.applicantName = applicantName;
        this.exportlcReferenceNo = exportlcReferenceNo;
        this.exportlcSRMSRequestId = exportlcSRMSRequestId;
        this.amendmentStatus = amendmentStatus;
        this.amendmentNo = amendmentNo;
        this.lcType = lcType;
        this.lcIssueDate = lcIssueDate;
        this.lcExpiryDate = lcExpiryDate;
        this.lcCurrency = lcCurrency;
        this.lcAmountStatus = lcAmountStatus;
        this.oldLcAmount = oldLcAmount;
        this.newLcAmount = newLcAmount;
        this.latestShipmentDate = latestShipmentDate;
        this.periodOfPresentation = periodOfPresentation;
        this.otherAmendments = otherAmendments;
        this.amendmentChargesPayer = amendmentChargesPayer;
        this.chargesDebitAccount = chargesDebitAccount;
        this.amendmentReferenceNo = amendmentReferenceNo;
        this.amendmentSRMSRequestId = amendmentSRMSRequestId;
        this.amendmentReceivedDate = amendmentReceivedDate;
        this.selfAcceptance = selfAcceptance;
        this.selfAcceptanceDate = selfAcceptanceDate;
        this.selfRejectedDate = selfRejectedDate;
        this.reasonForSelfRejection = reasonForSelfRejection;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amendmentChargesPayer == null) ? 0 : amendmentChargesPayer.hashCode());
        result = prime * result + ((amendmentNo == null) ? 0 : amendmentNo.hashCode());
        result = prime * result + ((amendmentReceivedDate == null) ? 0 : amendmentReceivedDate.hashCode());
        result = prime * result + ((amendmentReferenceNo == null) ? 0 : amendmentReferenceNo.hashCode());
        result = prime * result + ((amendmentSRMSRequestId == null) ? 0 : amendmentSRMSRequestId.hashCode());
        result = prime * result + ((amendmentStatus == null) ? 0 : amendmentStatus.hashCode());
        result = prime * result + ((applicantName == null) ? 0 : applicantName.hashCode());
        result = prime * result + ((chargesDebitAccount == null) ? 0 : chargesDebitAccount.hashCode());
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
        result = prime * result + ((exportlcReferenceNo == null) ? 0 : exportlcReferenceNo.hashCode());
        result = prime * result + ((exportlcSRMSRequestId == null) ? 0 : exportlcSRMSRequestId.hashCode());
        result = prime * result + ((latestShipmentDate == null) ? 0 : latestShipmentDate.hashCode());
        result = prime * result + ((lcAmountStatus == null) ? 0 : lcAmountStatus.hashCode());
        result = prime * result + ((lcCurrency == null) ? 0 : lcCurrency.hashCode());
        result = prime * result + ((lcExpiryDate == null) ? 0 : lcExpiryDate.hashCode());
        result = prime * result + ((lcIssueDate == null) ? 0 : lcIssueDate.hashCode());
        result = prime * result + ((lcType == null) ? 0 : lcType.hashCode());
        result = prime * result + ((newLcAmount == null) ? 0 : newLcAmount.hashCode());
        result = prime * result + ((oldLcAmount == null) ? 0 : oldLcAmount.hashCode());
        result = prime * result + ((otherAmendments == null) ? 0 : otherAmendments.hashCode());
        result = prime * result + ((periodOfPresentation == null) ? 0 : periodOfPresentation.hashCode());
        result = prime * result + ((reasonForSelfRejection == null) ? 0 : reasonForSelfRejection.hashCode());
        result = prime * result + ((selfAcceptance == null) ? 0 : selfAcceptance.hashCode());
        result = prime * result + ((selfAcceptanceDate == null) ? 0 : selfAcceptanceDate.hashCode());
        result = prime * result + ((selfRejectedDate == null) ? 0 : selfRejectedDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExportLCAmendmentsDTO other = (ExportLCAmendmentsDTO) obj;
        if (amendmentChargesPayer == null) {
            if (other.amendmentChargesPayer != null)
                return false;
        } else if (!amendmentChargesPayer.equals(other.amendmentChargesPayer))
            return false;
        if (amendmentNo == null) {
            if (other.amendmentNo != null)
                return false;
        } else if (!amendmentNo.equals(other.amendmentNo))
            return false;
        if (amendmentReceivedDate == null) {
            if (other.amendmentReceivedDate != null)
                return false;
        } else if (!amendmentReceivedDate.equals(other.amendmentReceivedDate))
            return false;
        if (amendmentReferenceNo == null) {
            if (other.amendmentReferenceNo != null)
                return false;
        } else if (!amendmentReferenceNo.equals(other.amendmentReferenceNo))
            return false;
        if (amendmentSRMSRequestId == null) {
            if (other.amendmentSRMSRequestId != null)
                return false;
        } else if (!amendmentSRMSRequestId.equals(other.amendmentSRMSRequestId))
            return false;
        if (amendmentStatus == null) {
            if (other.amendmentStatus != null)
                return false;
        } else if (!amendmentStatus.equals(other.amendmentStatus))
            return false;
        if (applicantName == null) {
            if (other.applicantName != null)
                return false;
        } else if (!applicantName.equals(other.applicantName))
            return false;
        if (chargesDebitAccount == null) {
            if (other.chargesDebitAccount != null)
                return false;
        } else if (!chargesDebitAccount.equals(other.chargesDebitAccount))
            return false;
        if (errorCode == null) {
            if (other.errorCode != null)
                return false;
        } else if (!errorCode.equals(other.errorCode))
            return false;
        if (errorMessage == null) {
            if (other.errorMessage != null)
                return false;
        } else if (!errorMessage.equals(other.errorMessage))
            return false;
        if (exportlcReferenceNo == null) {
            if (other.exportlcReferenceNo != null)
                return false;
        } else if (!exportlcReferenceNo.equals(other.exportlcReferenceNo))
            return false;
        if (exportlcSRMSRequestId == null) {
            if (other.exportlcSRMSRequestId != null)
                return false;
        } else if (!exportlcSRMSRequestId.equals(other.exportlcSRMSRequestId))
            return false;
        if (latestShipmentDate == null) {
            if (other.latestShipmentDate != null)
                return false;
        } else if (!latestShipmentDate.equals(other.latestShipmentDate))
            return false;
        if (lcAmountStatus == null) {
            if (other.lcAmountStatus != null)
                return false;
        } else if (!lcAmountStatus.equals(other.lcAmountStatus))
            return false;
        if (lcCurrency == null) {
            if (other.lcCurrency != null)
                return false;
        } else if (!lcCurrency.equals(other.lcCurrency))
            return false;
        if (lcExpiryDate == null) {
            if (other.lcExpiryDate != null)
                return false;
        } else if (!lcExpiryDate.equals(other.lcExpiryDate))
            return false;
        if (lcIssueDate == null) {
            if (other.lcIssueDate != null)
                return false;
        } else if (!lcIssueDate.equals(other.lcIssueDate))
            return false;
        if (lcType == null) {
            if (other.lcType != null)
                return false;
        } else if (!lcType.equals(other.lcType))
            return false;
        if (newLcAmount == null) {
            if (other.newLcAmount != null)
                return false;
        } else if (!newLcAmount.equals(other.newLcAmount))
            return false;
        if (oldLcAmount == null) {
            if (other.oldLcAmount != null)
                return false;
        } else if (!oldLcAmount.equals(other.oldLcAmount))
            return false;
        if (otherAmendments == null) {
            if (other.otherAmendments != null)
                return false;
        } else if (!otherAmendments.equals(other.otherAmendments))
            return false;
        if (periodOfPresentation == null) {
            if (other.periodOfPresentation != null)
                return false;
        } else if (!periodOfPresentation.equals(other.periodOfPresentation))
            return false;
        if (reasonForSelfRejection == null) {
            if (other.reasonForSelfRejection != null)
                return false;
        } else if (!reasonForSelfRejection.equals(other.reasonForSelfRejection))
            return false;
        if (selfAcceptance == null) {
            if (other.selfAcceptance != null)
                return false;
        } else if (!selfAcceptance.equals(other.selfAcceptance))
            return false;
        if (selfAcceptanceDate == null) {
            if (other.selfAcceptanceDate != null)
                return false;
        } else if (!selfAcceptanceDate.equals(other.selfAcceptanceDate))
            return false;
        if (selfRejectedDate == null) {
            if (other.selfRejectedDate != null)
                return false;
        } else if (!selfRejectedDate.equals(other.selfRejectedDate))
            return false;
        return true;
    }
}
