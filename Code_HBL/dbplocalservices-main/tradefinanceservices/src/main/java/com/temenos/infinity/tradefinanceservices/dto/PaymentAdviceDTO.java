/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;

public class PaymentAdviceDTO implements Serializable, DBPDTO {
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String paymentAdviceReference;
    private String adviceName;
    private String currency;
    private String drawingAmount;
    private String beneficiary;
    private String paymentDate;
    private String creditedAmount;
    private String creditedAccount;
    private String charges;
    private String advisingBank;
    private String drawingReferenceNo;
    private String orderId;
    @JsonAlias({PARAM_REQUEST_DATE_TIME})
    private String createdDate;
    private String message;
    private String status;
    private String errorCode;
    private String errorMessage;
    private String debitedAmount;
    private String debitedAccount;
    private String issuingBank;

    public String getPaymentAdviceReference() {
        return paymentAdviceReference;
    }

    public void setPaymentAdviceReference(String paymentAdviceReference) {
        this.paymentAdviceReference = paymentAdviceReference;
    }

    public String getAdviceName() {
        return adviceName;
    }

    public void setAdviceName(String adviceName) {
        this.adviceName = adviceName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDrawingAmount() {
        return drawingAmount;
    }

    public void setDrawingAmount(String drawingAmount) {
        this.drawingAmount = drawingAmount;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCreditedAmount() {
        return creditedAmount;
    }

    public void setCreditedAmount(String creditedAmount) {
        this.creditedAmount = creditedAmount;
    }

    public String getCreditedAccount() {
        return creditedAccount;
    }

    public void setCreditedAccount(String creditedAccount) {
        this.creditedAccount = creditedAccount;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getAdvisingBank() {
        return advisingBank;
    }

    public void setAdvisingBank(String advisingBank) {
        this.advisingBank = advisingBank;
    }

    public String getDrawingReferenceNo() {
        return drawingReferenceNo;
    }

    public void setDrawingReferenceNo(String drawingReferenceNo) {
        this.drawingReferenceNo = drawingReferenceNo;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getDebitedAmount() {
        return debitedAmount;
    }

    public void setDebitedAmount(String debitedAmount) {
        this.debitedAmount = debitedAmount;
    }

    public String getDebitedAccount() {
        return debitedAccount;
    }

    public void setDebitedAccount(String debitedAccount) {
        this.debitedAccount = debitedAccount;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }
}
