package com.bct.custom.dto;

import java.util.HashMap;
import java.util.Map;

public class NepalPayTransactionDTO {
	private String instructionId;
	private String validationTraceId;
	private String acquirerId;
	private String merchantPan;
	private String qrType;
	private double amount;
	private double interchangeFee;
	private double transactionFee;
	private String currencyCode;
	private String merchantBillNo;
	private String payerName;
	private String payerPanId;
	private String payerMobileNumber;
	private String payerEmailAddress;
	private String issuerId;
	private String debtorAccount;
	private String debtorAgent;
	private String debtorAgentBranch;
	private String narration;
	private String network;
	private String acquirerCountryCode;
	private String merchantName;
	private String merchantCity;
	private String merchantCountryCode;
	private String localTransactionDateTime;
	private String merchantCategoryCode;
	private String merchantTxnRef;
	private String instrument;
	private String terminal;
	private String encKeySerial;
	private String token;
	private String merchantPostalCode;

	public String getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(String instructionId) {
		this.instructionId = instructionId;
	}

	public String getValidationTraceId() {
		return validationTraceId;
	}

	public void setValidationTraceId(String validationTraceId) {
		this.validationTraceId = validationTraceId;
	}

	public String getAcquirerId() {
		return acquirerId;
	}

	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public String getMerchantPan() {
		return merchantPan;
	}

	public void setMerchantPan(String merchantPan) {
		this.merchantPan = merchantPan;
	}

	public String getQrType() {
		return qrType;
	}

	public void setQrType(String qrType) {
		this.qrType = qrType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getInterchangeFee() {
		return interchangeFee;
	}

	public void setInterchangeFee(double interchangeFee) {
		this.interchangeFee = interchangeFee;
	}

	public double getTransactionFee() {
		return transactionFee;
	}

	public void setTransactionFee(double transactionFee) {
		this.transactionFee = transactionFee;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getMerchantBillNo() {
		return merchantBillNo;
	}

	public void setMerchantBillNo(String merchantBillNo) {
		this.merchantBillNo = merchantBillNo;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayerPanId() {
		return payerPanId;
	}

	public void setPayerPanId(String payerPanId) {
		this.payerPanId = payerPanId;
	}

	public String getPayerMobileNumber() {
		return payerMobileNumber;
	}

	public void setPayerMobileNumber(String payerMobileNumber) {
		this.payerMobileNumber = payerMobileNumber;
	}

	public String getPayerEmailAddress() {
		return payerEmailAddress;
	}

	public void setPayerEmailAddress(String payerEmailAddress) {
		this.payerEmailAddress = payerEmailAddress;
	}

	public String getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}

	public String getDebtorAccount() {
		return debtorAccount;
	}

	public void setDebtorAccount(String debtorAccount) {
		this.debtorAccount = debtorAccount;
	}

	public String getDebtorAgent() {
		return debtorAgent;
	}

	public void setDebtorAgent(String debtorAgent) {
		this.debtorAgent = debtorAgent;
	}

	public String getDebtorAgentBranch() {
		return debtorAgentBranch;
	}

	public void setDebtorAgentBranch(String debtorAgentBranch) {
		this.debtorAgentBranch = debtorAgentBranch;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getAcquirerCountryCode() {
		return acquirerCountryCode;
	}

	public void setAcquirerCountryCode(String acquirerCountryCode) {
		this.acquirerCountryCode = acquirerCountryCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantCity() {
		return merchantCity;
	}

	public void setMerchantCity(String merchantCity) {
		this.merchantCity = merchantCity;
	}

	public String getMerchantCountryCode() {
		return merchantCountryCode;
	}

	public void setMerchantCountryCode(String merchantCountryCode) {
		this.merchantCountryCode = merchantCountryCode;
	}

	public String getLocalTransactionDateTime() {
		return localTransactionDateTime;
	}

	public void setLocalTransactionDateTime(String localTransactionDateTime) {
		this.localTransactionDateTime = localTransactionDateTime;
	}

	public String getMerchantCategoryCode() {
		return merchantCategoryCode;
	}

	public void setMerchantCategoryCode(String merchantCategoryCode2) {
		this.merchantCategoryCode = merchantCategoryCode2;
	}

	public String getMerchantTxnRef() {
		return merchantTxnRef;
	}

	public void setMerchantTxnRef(String merchantTxnRef) {
		this.merchantTxnRef = merchantTxnRef;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getEncKeySerial() {
		return encKeySerial;
	}

	public void setEncKeySerial(String encKeySerial) {
		this.encKeySerial = encKeySerial;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMerchantPostalCode() {
		return merchantPostalCode;
	}

	public void setMerchantPostalCode(String merchantPostalCode) {
		this.merchantPostalCode = merchantPostalCode;
	}

	private Map<String, String> addenda = new HashMap<>();

	public Map<String, String> getAddenda() {
		return addenda;
	}

	public void setAddenda(Map<String, String> addenda) {
		this.addenda = addenda;
	}
}
