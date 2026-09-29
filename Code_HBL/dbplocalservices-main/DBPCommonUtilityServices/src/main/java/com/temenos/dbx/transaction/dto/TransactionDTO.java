package com.temenos.dbx.transaction.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class TransactionDTO implements DBPDTO {

	private static final long serialVersionUID = -8490541551017811735L;
	
	private String transactionId;
	private String transactionType;
	private String fromAccountNumber;
	private String fromAccountType;
	private String fromNickName;
	private String toAccountNumber;
	private String toAccountType;
	private String toNickName;
	private String toAccountName;
	private String transactionDate;
	private String scheduledDate;
	private String amount;
	private String transactionCurrency;
	private String frequencyType;
	private String recurrenceDesc;
	private String transactionsNotes;
	private String description;
	private String frequencyStartDate;
	private String frequencyEndDate;
	private String payeeName;
	private String payeeNickName;
	private String payPersonEmail;
	private String payPersonName;
	private String payPersonPhone;
	private String cashlessPersonName;
	private String cashlessPhone;
	private String cashlessEmail;
	private String isDisputed;
	private String serviceName;
	private String transactionAmount;
	private String serviceCharge;
	private String convertedAmount;
	private String exchangeRate;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getFromAccountNumber() {
		return fromAccountNumber;
	}
	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}
	public String getFromAccountType() {
		return fromAccountType;
	}
	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}
	public String getFromNickName() {
		return fromNickName;
	}
	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
	}
	public String getToAccountNumber() {
		return toAccountNumber;
	}
	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	public String getToAccountType() {
		return toAccountType;
	}
	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}
	public String getToNickName() {
		return toNickName;
	}
	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTransactionCurrency() {
		return transactionCurrency;
	}
	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getRecurrenceDesc() {
		return recurrenceDesc;
	}
	public void setRecurrenceDesc(String recurrenceDesc) {
		this.recurrenceDesc = recurrenceDesc;
	}
	public String getTransactionsNotes() {
		return transactionsNotes;
	}
	public void setTransactionsNotes(String transactionsNotes) {
		this.transactionsNotes = transactionsNotes;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFrequencyStartDate() {
		return frequencyStartDate;
	}
	public void setFrequencyStartDate(String frequencyStartDate) {
		this.frequencyStartDate = frequencyStartDate;
	}
	public String getFrequencyEndDate() {
		return frequencyEndDate;
	}
	public void setFrequencyEndDate(String frequencyEndDate) {
		this.frequencyEndDate = frequencyEndDate;
	}
	public String getToAccountName() {
		return toAccountName;
	}
	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeNickName() {
		return payeeNickName;
	}
	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
	}
	public String getPayPersonEmail() {
		return payPersonEmail;
	}
	public void setPayPersonEmail(String payPersonEmail) {
		this.payPersonEmail = payPersonEmail;
	}
	public String getPayPersonName() {
		return payPersonName;
	}
	public void setPayPersonName(String payPersonName) {
		this.payPersonName = payPersonName;
	}
	public String getPayPersonPhone() {
		return payPersonPhone;
	}
	public void setPayPersonPhone(String payPersonPhone) {
		this.payPersonPhone = payPersonPhone;
	}
	public String getCashlessPersonName() {
		return cashlessPersonName;
	}
	public void setCashlessPersonName(String cashlessPersonName) {
		this.cashlessPersonName = cashlessPersonName;
	}
	public String getCashlessPhone() {
		return cashlessPhone;
	}
	public void setCashlessPhone(String cashlessPhone) {
		this.cashlessPhone = cashlessPhone;
	}
	public String getCashlessEmail() {
		return cashlessEmail;
	}
	public void setCashlessEmail(String cashlessEmail) {
		this.cashlessEmail = cashlessEmail;
	}
	public String getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	public String getIsDisputed() {
		return isDisputed;
	}
	public void setIsDisputed(String isDisputed) {
		this.isDisputed = isDisputed;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public String getConvertedAmount() {
		return convertedAmount;
	}
	public void setConvertedAmount(String convertedAmount) {
		this.convertedAmount = convertedAmount;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	
}
