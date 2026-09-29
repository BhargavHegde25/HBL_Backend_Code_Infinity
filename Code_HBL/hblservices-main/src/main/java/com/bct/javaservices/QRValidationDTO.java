package com.bct.javaservices;

public class QRValidationDTO {
	private String transactionId;
	private String aggregatorType;
	private String aggregatorName;
	private String fromAccountNumber;
	private double amount;
	private double debitAmount;
	private double transactionFee;
	private String validationRequest;
	private String validationResult;
	private String status;
	private String created_at;
	private String updated_at;
	private String actualPayload;
	private String requestType;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAggregatorType() {
		return aggregatorType;
	}

	public void setAggregatorType(String aggregatorType) {
		this.aggregatorType = aggregatorType;
	}

	public String getAggregatorName() {
		return aggregatorName;
	}

	public void setAggregatorName(String aggregatorName) {
		this.aggregatorName = aggregatorName;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(double debitAmount) {
		this.debitAmount = debitAmount;
	}

	public double getTransactionFee() {
		return transactionFee;
	}

	public void setTransactionFee(double transactionFee) {
		this.transactionFee = transactionFee;
	}

	public String getValidationRequest() {
		return validationRequest;
	}

	public void setValidationRequest(String validationRequest) {
		this.validationRequest = validationRequest;
	}

	public String getValidationResult() {
		return validationResult;
	}

	public void setValidationResult(String validationResult) {
		this.validationResult = validationResult;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getActualPayload() {
		return actualPayload;
	}

	public void setActualPayload(String actualPayload) {
		this.actualPayload = actualPayload;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

}
