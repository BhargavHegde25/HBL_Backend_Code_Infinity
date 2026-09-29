package com.temenos.infinity.api.accountsweeps.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.awt.*;

/**
 * @author naveen.yerra
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AccountSweepsDTO implements DBPDTO {

    private static final long serialVersionUID = -3912113200301575348L;
    private String primaryAccountName, primaryAccountNumber, secondaryAccountName,
    secondaryAccountNumber, belowSweepAmount, aboveSweepAmount, frequency, startDate, endDate,currencyCode,serviceRequestId;
    @JsonIgnore
    private String dbpErrMsg, dbpErrCode, message, errorCode, errorMessage,sweepType,previousSecondaryAccountNumber;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getPreviousSecondaryAccountNumber() {
        return previousSecondaryAccountNumber;
    }

    public void setPreviousSecondaryAccountNumber(String previousSecondaryAccountNumber) {
        this.previousSecondaryAccountNumber = previousSecondaryAccountNumber;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPrimaryAccountName() {
        return primaryAccountName;
    }

    public void setPrimaryAccountName(String primaryAccountName) {
        this.primaryAccountName = primaryAccountName;
    }

    public String getPrimaryAccountNumber() {
        return primaryAccountNumber;
    }

    public void setPrimaryAccountNumber(String primaryAccountNumber) {
        this.primaryAccountNumber = primaryAccountNumber;
    }

    public String getSecondaryAccountName() {
        return secondaryAccountName;
    }

    public void setSecondaryAccountName(String secondaryAccountName) {
        this.secondaryAccountName = secondaryAccountName;
    }

    public String getSecondaryAccountNumber() {
        return secondaryAccountNumber;
    }

    public void setSecondaryAccountNumber(String secondaryAccountNumber) {
        this.secondaryAccountNumber = secondaryAccountNumber;
    }

    public String getBelowSweepAmount() {
        return belowSweepAmount;
    }

    public void setBelowSweepAmount(String belowSweepAmount) {
        if("0.00".equals(belowSweepAmount)) {
    		this.belowSweepAmount = "";
    	}else {
    		this.belowSweepAmount = belowSweepAmount;
    	}
    }

    public String getAboveSweepAmount() {
        return aboveSweepAmount;
    }

    public void setAboveSweepAmount(String aboveSweepAmount) {
    	if("0.00".equals(aboveSweepAmount)) {
    		this.aboveSweepAmount = "";
    	}else {
    		this.aboveSweepAmount = aboveSweepAmount;
    	}
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
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

    public String getDbpErrMsg() {
        return dbpErrMsg;
    }

    public void setDbpErrMsg(String dbpErrMsg) {
        this.dbpErrMsg = dbpErrMsg;
    }

    public String getDbpErrCode() {
        return dbpErrCode;
    }

    public void setDbpErrCode(String dbpErrCode) {
        this.dbpErrCode = dbpErrCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSweepType() {
        if (!belowSweepAmount.equals("") && !belowSweepAmount.equals("0.00")  && !aboveSweepAmount.equals("") && !aboveSweepAmount.equals("0.00"))
            return sweepType = "Both";

        if (aboveSweepAmount.equals("") || aboveSweepAmount.equals("0.00"))
            return sweepType = "Below";

        if (belowSweepAmount.equals("") || belowSweepAmount.equals("0.00"))
            return sweepType = "Above";

        return sweepType;
    }

    public void setSweepType(String sweepType) {
        this.sweepType=sweepType;
    }


}
