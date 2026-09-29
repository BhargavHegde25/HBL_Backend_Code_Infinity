package com.bct.custom.dto;

import java.util.HashMap;

public class QRTransactionDTO {

	private String customerId;
	private String fromAccountNumber;
	private String toAccountNumber;
	private String transactionType;
	private String transactionId;
	private double transactionAmount;
	private String transactRefId;
	private double amount;
	private String referenceId;
	private String fromAccountName;
	private String toAccountName;
	private String fromAccountCurrency;
	private String toAccountCurrency;
	private String transactionCurrency;
	private String description;
	private String merchantCode;
	private String status;
	private String notes;
	private double fee;
	private String createdBy;
	private String errmsg;
	private String date;
	private int softDeleteFlag = 0;
	private String companyId;
	private String externalServiceRequest;
	private String externalServiceResponse;
	private HashMap<String, Object> externalApiPayload;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getFromAccountName() {
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

	public String getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(String fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}

	public String getToAccountCurrency() {
		return toAccountCurrency;
	}

	public void setToAccountCurrency(String toAccountCurrency) {
		this.toAccountCurrency = toAccountCurrency;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public int getSoftDeleteFlag() {
		return softDeleteFlag;
	}

	public void setSoftDeleteFlag(int softDeleteFlag) {
		this.softDeleteFlag = softDeleteFlag;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public HashMap<String, Object> getExternalApiPayload() {
		return externalApiPayload;
	}

	public void setExternalApiPayload(HashMap<String, Object> externalApiPayload) {
		this.externalApiPayload = externalApiPayload;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTransactRefId() {
		return transactRefId;
	}

	public void setTransactRefId(String transactRefId) {
		this.transactRefId = transactRefId;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getExternalServiceRequest() {
		return externalServiceRequest;
	}

	public void setExternalServiceRequest(String externalServiceRequest) {
		this.externalServiceRequest = externalServiceRequest;
	}

	public String getExternalServiceResponse() {
		return externalServiceResponse;
	}

	public void setExternalServiceResponse(String externalServiceResponse) {
		this.externalServiceResponse = externalServiceResponse;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}