package com.temenos.infinity.api.holdings.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author KH2281
 * @version 1.0 implements {@link DBPDTO}
 * 
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class AccountTransactionsDTO implements DBPDTO {

	private static final long serialVersionUID = 6597925078705187784L;

	public AccountTransactionsDTO() {
		super();
	}

	

	private String accountId;
	private String checkNumber;
	private String transactionType;
	private String valueDate;
	private String bookingDate;
	private String processingDate;
	private String customerId;
	private String currency;
	private int accountOfficerId;
	private int categorisactionId;
	private double amountInAccountCurrency;
	private double transactionAmount;
	private double amountInEventCurrency;
	private double exchangeRate;
	private String externalReference;
	private String customerReference;
	private String transactionReference;
	private String narrative;
	private String tradeDate;
	private String offset;
	private String limit;
	private String order;
	private String isScheduled;
	private String sortBy;
	private String fromAccountNumber;
	private String scheduledDate;
	private String notes;
	private String description;
	private String transactionDate;
	private double amount;
	private String payeeCurrency;
	private String Id;
	private String statusDesc;
	private String postedDate;
	private double fromAccountBalance;
	private String searchStartDate;
	private String authToken;
	private String headerAuthValue;
	private String headerreportingValue;
	private String userName;
	private String deviceId;
	private String channelDescription;
	private String searchMinAmount="";
	private String searchMaxAmount="";
	private String searchEndDate="";
	private String searchDescription="";
	private String searchType="";
	private String statusDescription;
	private String transactionCurrency;
	private String transactionId;
	private String interest;
	private String principal;
	private String installmentType;
	private String cumulativeInterest;
	private String insurance;
	private String tax;
	private String charges;
	private String outstandingBalance;
	private String date;
	private String installmentAmount;
	private String transaction_Id;

	public AccountTransactionsDTO(String accountId,String checkNumber, String transactionType, String valueDate, String bookingDate,
			String processingDate, String customerId, String currency, int accountOfficerId, int categorisactionId,
			double amountInAccountCurrency, double transactionAmount, double amountInEventCurrency, double exchangeRate,
			String externalReference, String customerReference, String transactionReference, String narrative,
			String tradeDate, String offset, String limit, String order, String isScheduled, String sortBy,
			String fromAccountNumber, String scheduledDate, String notes, String description, String transactionDate,
			double amount, String payeeCurrency, String id, String statusDesc, String postedDate,
			double fromAccountBalance, String searchStartDate, String authToken, String headerAuthValue,
			String headerreportingValue, String userName, String deviceId, String channelDescription,
			String searchMinAmount, String searchMaxAmount, String searchEndDate, String searchDescription,
			String searchType, String statusDescription, String transactionCurrency, String transactionId,
			String interest, String principal, String installmentType, String cumulativeInterest, String insurance,
			String tax, String charges, String outstandingBalance,String date,String installmentAmount,String transaction_Id) {
		super();
		this.accountId = accountId;
		this.checkNumber=checkNumber;
		this.transactionType = transactionType;
		this.valueDate = valueDate;
		this.bookingDate = bookingDate;
		this.processingDate = processingDate;
		this.customerId = customerId;
		this.currency = currency;
		this.accountOfficerId = accountOfficerId;
		this.categorisactionId = categorisactionId;
		this.amountInAccountCurrency = amountInAccountCurrency;
		this.transactionAmount = transactionAmount;
		this.amountInEventCurrency = amountInEventCurrency;
		this.exchangeRate = exchangeRate;
		this.externalReference = externalReference;
		this.customerReference = customerReference;
		this.transactionReference = transactionReference;
		this.narrative = narrative;
		this.tradeDate = tradeDate;
		this.offset = offset;
		this.limit = limit;
		this.order = order;
		this.isScheduled = isScheduled;
		this.sortBy = sortBy;
		this.fromAccountNumber = fromAccountNumber;
		this.scheduledDate = scheduledDate;
		this.notes = notes;
		this.description = description;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.payeeCurrency = payeeCurrency;
		Id = id;
		this.statusDesc = statusDesc;
		this.postedDate = postedDate;
		this.fromAccountBalance = fromAccountBalance;
		this.searchStartDate = searchStartDate;
		this.authToken = authToken;
		this.headerAuthValue = headerAuthValue;
		this.headerreportingValue = headerreportingValue;
		this.userName = userName;
		this.deviceId = deviceId;
		this.channelDescription = channelDescription;
		this.searchMinAmount = searchMinAmount;
		this.searchMaxAmount = searchMaxAmount;
		this.searchEndDate = searchEndDate;
		this.searchDescription = searchDescription;
		this.searchType = searchType;
		this.statusDescription = statusDescription;
		this.transactionCurrency = transactionCurrency;
		this.transactionId = transactionId;
		this.interest = interest;
		this.principal = principal;
		this.installmentType = installmentType;
		this.cumulativeInterest = cumulativeInterest;
		this.insurance = insurance;
		this.tax = tax;
		this.charges = charges;
		this.outstandingBalance = outstandingBalance;
		this.date=date;
		this.installmentAmount=installmentAmount;
		this.transaction_Id = transaction_Id;
	}


	public String getInstallmentAmount() {
		return installmentAmount;
	}


	public void setInstallmentAmount(String installmentAmount) {
		this.installmentAmount = installmentAmount;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getChannelDescription() {
		return channelDescription;
	}

	public void setChannelDescription(String channelDescription) {
		this.channelDescription = channelDescription;
	}

	public String getHeaderAuthValue() {
		return headerAuthValue;
	}

	public void setHeaderAuthValue(String headerAuthValue) {
		this.headerAuthValue = headerAuthValue;
	}

	public String getHeaderreportingValue() {
		return headerreportingValue;
	}

	public void setHeaderreportingValue(String headerreportingValue) {
		this.headerreportingValue = headerreportingValue;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public String getId() {
		return this.Id;
	}

	public void setId(String id) {
		this.Id = id;
	}

	public double getFromAccountBalance() {
		return fromAccountBalance;
	}

	public void setFromAccountBalance(double fromAccountBalance) {
		this.fromAccountBalance = fromAccountBalance;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPayeeCurrency() {
		return payeeCurrency;
	}

	public void setPayeeCurrency(String payeeCurrency) {
		this.payeeCurrency = payeeCurrency;
	}

	public String getIsScheduled() {
		return isScheduled;
	}

	public void setIsScheduled(String isScheduled) {
		this.isScheduled = isScheduled;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(String processingDate) {
		this.processingDate = processingDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getAccountOfficerId() {
		return accountOfficerId;
	}

	public void setAccountOfficerId(int accountOfficerId) {
		this.accountOfficerId = accountOfficerId;
	}

	public int getCategorisactionId() {
		return categorisactionId;
	}

	public void setCategorisactionId(int categorisactionId) {
		this.categorisactionId = categorisactionId;
	}

	public double getAmountInAccountCurrency() {
		return amountInAccountCurrency;
	}

	public void setAmountInAccountCurrency(double amountInAccountCurrency) {
		this.amountInAccountCurrency = amountInAccountCurrency;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public double getAmountInEventCurrency() {
		return amountInEventCurrency;
	}

	public void setAmountInEventCurrency(double amountInEventCurrency) {
		this.amountInEventCurrency = amountInEventCurrency;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTransaction_Id() {
		return transaction_Id;
	}


	public void setTransaction_Id(String transaction_Id) {
		this.transaction_Id = transaction_Id;
	}


	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionID) {
		this.transactionType = transactionID;
	}

	public String getSearchMinAmount() {
		return searchMinAmount;
	}


	public void setSearchMinAmount(String searchMinAmount) {
		this.searchMinAmount = searchMinAmount;
	}


	public String getSearchMaxAmount() {
		return searchMaxAmount;
	}


	public void setSearchMaxAmount(String searchMaxAmount) {
		this.searchMaxAmount = searchMaxAmount;
	}


	public String getSearchEndDate() {
		return searchEndDate;
	}


	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}


	public String getSearchDescription() {
		return searchDescription;
	}


	public void setSearchDescription(String searchDescription) {
		this.searchDescription = searchDescription;
	}


	public String getSearchType() {
		return searchType;
	}


	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	public String getInterest() {
		return interest;
	}


	public void setInterest(String interest) {
		this.interest = interest;
	}


	public String getPrincipal() {
		return principal;
	}


	public void setPrincipal(String principal) {
		this.principal = principal;
	}


	public String getInstallmentType() {
		return installmentType;
	}


	public void setInstallmentType(String installmentType) {
		this.installmentType = installmentType;
	}


	public String getCumulativeInterest() {
		return cumulativeInterest;
	}


	public void setCumulativeInterest(String cumulativeInterest) {
		this.cumulativeInterest = cumulativeInterest;
	}


	public String getInsurance() {
		return insurance;
	}


	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}


	public String getTax() {
		return tax;
	}


	public void setTax(String tax) {
		this.tax = tax;
	}


	public String getCharges() {
		return charges;
	}


	public void setCharges(String charges) {
		this.charges = charges;
	}


	public String getOutstandingBalance() {
		return outstandingBalance;
	}


	public void setOutstandingBalance(String outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + accountOfficerId;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(amountInAccountCurrency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(amountInEventCurrency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((authToken == null) ? 0 : authToken.hashCode());
		result = prime * result + ((bookingDate == null) ? 0 : bookingDate.hashCode());
		result = prime * result + ((checkNumber == null) ? 0 : checkNumber.hashCode());
		result = prime * result + categorisactionId;
		result = prime * result + ((channelDescription == null) ? 0 : channelDescription.hashCode());
		result = prime * result + ((charges == null) ? 0 : charges.hashCode());
		result = prime * result + ((cumulativeInterest == null) ? 0 : cumulativeInterest.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerReference == null) ? 0 : customerReference.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		temp = Double.doubleToLongBits(exchangeRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((externalReference == null) ? 0 : externalReference.hashCode());
		temp = Double.doubleToLongBits(fromAccountBalance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((headerAuthValue == null) ? 0 : headerAuthValue.hashCode());
		result = prime * result + ((headerreportingValue == null) ? 0 : headerreportingValue.hashCode());
		result = prime * result + ((installmentAmount == null) ? 0 : installmentAmount.hashCode());
		result = prime * result + ((installmentType == null) ? 0 : installmentType.hashCode());
		result = prime * result + ((insurance == null) ? 0 : insurance.hashCode());
		result = prime * result + ((interest == null) ? 0 : interest.hashCode());
		result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
		result = prime * result + ((limit == null) ? 0 : limit.hashCode());
		result = prime * result + ((narrative == null) ? 0 : narrative.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((offset == null) ? 0 : offset.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((outstandingBalance == null) ? 0 : outstandingBalance.hashCode());
		result = prime * result + ((payeeCurrency == null) ? 0 : payeeCurrency.hashCode());
		result = prime * result + ((postedDate == null) ? 0 : postedDate.hashCode());
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		result = prime * result + ((processingDate == null) ? 0 : processingDate.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((searchDescription == null) ? 0 : searchDescription.hashCode());
		result = prime * result + ((searchEndDate == null) ? 0 : searchEndDate.hashCode());
		result = prime * result + ((searchMaxAmount == null) ? 0 : searchMaxAmount.hashCode());
		result = prime * result + ((searchMinAmount == null) ? 0 : searchMinAmount.hashCode());
		result = prime * result + ((searchStartDate == null) ? 0 : searchStartDate.hashCode());
		result = prime * result + ((searchType == null) ? 0 : searchType.hashCode());
		result = prime * result + ((sortBy == null) ? 0 : sortBy.hashCode());
		result = prime * result + ((statusDesc == null) ? 0 : statusDesc.hashCode());
		result = prime * result + ((statusDescription == null) ? 0 : statusDescription.hashCode());
		result = prime * result + ((tax == null) ? 0 : tax.hashCode());
		result = prime * result + ((tradeDate == null) ? 0 : tradeDate.hashCode());
		temp = Double.doubleToLongBits(transactionAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((transactionReference == null) ? 0 : transactionReference.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((valueDate == null) ? 0 : valueDate.hashCode());
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
		AccountTransactionsDTO other = (AccountTransactionsDTO) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (accountOfficerId != other.accountOfficerId)
			return false;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (Double.doubleToLongBits(amountInAccountCurrency) != Double.doubleToLongBits(other.amountInAccountCurrency))
			return false;
		if (Double.doubleToLongBits(amountInEventCurrency) != Double.doubleToLongBits(other.amountInEventCurrency))
			return false;
		if (authToken == null) {
			if (other.authToken != null)
				return false;
		} else if (!authToken.equals(other.authToken))
			return false;
		if (bookingDate == null) {
			if (other.bookingDate != null)
				return false;
		} else if (!bookingDate.equals(other.bookingDate))
			return false;
		if (checkNumber == null) {
			if (other.checkNumber != null)
				return false;
		} else if (!checkNumber.equals(other.checkNumber))
			return false;
		if (categorisactionId != other.categorisactionId)
			return false;
		if (channelDescription == null) {
			if (other.channelDescription != null)
				return false;
		} else if (!channelDescription.equals(other.channelDescription))
			return false;
		if (charges == null) {
			if (other.charges != null)
				return false;
		} else if (!charges.equals(other.charges))
			return false;
		if (cumulativeInterest == null) {
			if (other.cumulativeInterest != null)
				return false;
		} else if (!cumulativeInterest.equals(other.cumulativeInterest))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (customerReference == null) {
			if (other.customerReference != null)
				return false;
		} else if (!customerReference.equals(other.customerReference))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (Double.doubleToLongBits(exchangeRate) != Double.doubleToLongBits(other.exchangeRate))
			return false;
		if (externalReference == null) {
			if (other.externalReference != null)
				return false;
		} else if (!externalReference.equals(other.externalReference))
			return false;
		if (Double.doubleToLongBits(fromAccountBalance) != Double.doubleToLongBits(other.fromAccountBalance))
			return false;
		if (fromAccountNumber == null) {
			if (other.fromAccountNumber != null)
				return false;
		} else if (!fromAccountNumber.equals(other.fromAccountNumber))
			return false;
		if (headerAuthValue == null) {
			if (other.headerAuthValue != null)
				return false;
		} else if (!headerAuthValue.equals(other.headerAuthValue))
			return false;
		if (headerreportingValue == null) {
			if (other.headerreportingValue != null)
				return false;
		} else if (!headerreportingValue.equals(other.headerreportingValue))
			return false;
		if (installmentAmount == null) {
			if (other.installmentAmount != null)
				return false;
		} else if (!installmentAmount.equals(other.installmentAmount))
			return false;
		if (installmentType == null) {
			if (other.installmentType != null)
				return false;
		} else if (!installmentType.equals(other.installmentType))
			return false;
		if (insurance == null) {
			if (other.insurance != null)
				return false;
		} else if (!insurance.equals(other.insurance))
			return false;
		if (interest == null) {
			if (other.interest != null)
				return false;
		} else if (!interest.equals(other.interest))
			return false;
		if (isScheduled == null) {
			if (other.isScheduled != null)
				return false;
		} else if (!isScheduled.equals(other.isScheduled))
			return false;
		if (limit == null) {
			if (other.limit != null)
				return false;
		} else if (!limit.equals(other.limit))
			return false;
		if (narrative == null) {
			if (other.narrative != null)
				return false;
		} else if (!narrative.equals(other.narrative))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (offset == null) {
			if (other.offset != null)
				return false;
		} else if (!offset.equals(other.offset))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (outstandingBalance == null) {
			if (other.outstandingBalance != null)
				return false;
		} else if (!outstandingBalance.equals(other.outstandingBalance))
			return false;
		if (payeeCurrency == null) {
			if (other.payeeCurrency != null)
				return false;
		} else if (!payeeCurrency.equals(other.payeeCurrency))
			return false;
		if (postedDate == null) {
			if (other.postedDate != null)
				return false;
		} else if (!postedDate.equals(other.postedDate))
			return false;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		if (processingDate == null) {
			if (other.processingDate != null)
				return false;
		} else if (!processingDate.equals(other.processingDate))
			return false;
		if (scheduledDate == null) {
			if (other.scheduledDate != null)
				return false;
		} else if (!scheduledDate.equals(other.scheduledDate))
			return false;
		if (searchDescription == null) {
			if (other.searchDescription != null)
				return false;
		} else if (!searchDescription.equals(other.searchDescription))
			return false;
		if (searchEndDate == null) {
			if (other.searchEndDate != null)
				return false;
		} else if (!searchEndDate.equals(other.searchEndDate))
			return false;
		if (searchMaxAmount == null) {
			if (other.searchMaxAmount != null)
				return false;
		} else if (!searchMaxAmount.equals(other.searchMaxAmount))
			return false;
		if (searchMinAmount == null) {
			if (other.searchMinAmount != null)
				return false;
		} else if (!searchMinAmount.equals(other.searchMinAmount))
			return false;
		if (searchStartDate == null) {
			if (other.searchStartDate != null)
				return false;
		} else if (!searchStartDate.equals(other.searchStartDate))
			return false;
		if (searchType == null) {
			if (other.searchType != null)
				return false;
		} else if (!searchType.equals(other.searchType))
			return false;
		if (sortBy == null) {
			if (other.sortBy != null)
				return false;
		} else if (!sortBy.equals(other.sortBy))
			return false;
		if (statusDesc == null) {
			if (other.statusDesc != null)
				return false;
		} else if (!statusDesc.equals(other.statusDesc))
			return false;
		if (statusDescription == null) {
			if (other.statusDescription != null)
				return false;
		} else if (!statusDescription.equals(other.statusDescription))
			return false;
		if (tax == null) {
			if (other.tax != null)
				return false;
		} else if (!tax.equals(other.tax))
			return false;
		if (tradeDate == null) {
			if (other.tradeDate != null)
				return false;
		} else if (!tradeDate.equals(other.tradeDate))
			return false;
		if (Double.doubleToLongBits(transactionAmount) != Double.doubleToLongBits(other.transactionAmount))
			return false;
		if (transactionCurrency == null) {
			if (other.transactionCurrency != null)
				return false;
		} else if (!transactionCurrency.equals(other.transactionCurrency))
			return false;
		if (transactionDate == null) {
			if (other.transactionDate != null)
				return false;
		} else if (!transactionDate.equals(other.transactionDate))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		if (transactionReference == null) {
			if (other.transactionReference != null)
				return false;
		} else if (!transactionReference.equals(other.transactionReference))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (valueDate == null) {
			if (other.valueDate != null)
				return false;
		} else if (!valueDate.equals(other.valueDate))
			return false;
		return true;
	}

}
