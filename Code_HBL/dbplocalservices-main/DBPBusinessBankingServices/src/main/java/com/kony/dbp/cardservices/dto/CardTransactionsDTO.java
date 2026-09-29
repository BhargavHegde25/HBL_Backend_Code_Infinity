package com.kony.dbp.cardservices.dto;


import com.dbp.core.api.DBPDTO;

public class CardTransactionsDTO implements DBPDTO {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cardNumber;
	private String transactionDescription;
	private String transactionBalance;
	private String transactionMerchantAddressName;
	private String transactionMerchantCity;
	private String merchantCategory;
	private String transactionStatus;
	private String transactionType;
	private String transactionCategory;
	private String transactionDetailDescription;
	private String transactionIndicator;
	private String transactionDate;
	private String transactionTime;
	private String transactionAmount;
	private String transactionReferenceNumber;
	private String transactionCurrencyCode;
	private String transactionExchangeRate;
	private String exchangeCurrency;
	private String exchangeAmount;
	private String transactionTaxIndicator;
	private String taxPercentage;
	private String transactionTaxAmount;
	private String transactionTerminalID;
	private String cardType;
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getTransactionDescription() {
		return transactionDescription;
	}
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}
	public String getTransactionBalance() {
		return transactionBalance;
	}
	public void setTransactionBalance(String transactionBalance) {
		this.transactionBalance = transactionBalance;
	}
	public String getTransactionMerchantAddressName() {
		return transactionMerchantAddressName;
	}
	public void setTransactionMerchantAddressName(String transactionMerchantAddressName) {
		this.transactionMerchantAddressName = transactionMerchantAddressName;
	}
	public String getTransactionMerchantCity() {
		return transactionMerchantCity;
	}
	public void setTransactionMerchantCity(String transactionMerchantCity) {
		this.transactionMerchantCity = transactionMerchantCity;
	}
	public String getMerchantCategory() {
		return merchantCategory;
	}
	public void setMerchantCategory(String merchantCategory) {
		this.merchantCategory = merchantCategory;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionCategory() {
		return transactionCategory;
	}
	public void setTransactionCategory(String transactionCategory) {
		this.transactionCategory = transactionCategory;
	}
	public String getTransactionDetailDescription() {
		return transactionDetailDescription;
	}
	public void setTransactionDetailDescription(String transactionDetailDescription) {
		this.transactionDetailDescription = transactionDetailDescription;
	}
	public String getTransactionIndicator() {
		return transactionIndicator;
	}
	public void setTransactionIndicator(String transactionIndicator) {
		this.transactionIndicator = transactionIndicator;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTransactionReferenceNumber() {
		return transactionReferenceNumber;
	}
	public void setTransactionReferenceNumber(String transactionReferenceNumber) {
		this.transactionReferenceNumber = transactionReferenceNumber;
	}
	public String getTransactionCurrencyCode() {
		return transactionCurrencyCode;
	}
	public void setTransactionCurrencyCode(String transactionCurrencyCode) {
		this.transactionCurrencyCode = transactionCurrencyCode;
	}
	public String getTransactionExchangeRate() {
		return transactionExchangeRate;
	}
	public void setTransactionExchangeRate(String transactionExchangeRate) {
		this.transactionExchangeRate = transactionExchangeRate;
	}
	public String getExchangeCurrency() {
		return exchangeCurrency;
	}
	public void setExchangeCurrency(String exchangeCurrency) {
		this.exchangeCurrency = exchangeCurrency;
	}
	public String getExchangeAmount() {
		return exchangeAmount;
	}
	public void setExchangeAmount(String exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
	}
	public String getTransactionTaxIndicator() {
		return transactionTaxIndicator;
	}
	public void setTransactionTaxIndicator(String transactionTaxIndicator) {
		this.transactionTaxIndicator = transactionTaxIndicator;
	}
	public String getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(String taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	public String getTransactionTaxAmount() {
		return transactionTaxAmount;
	}
	public void setTransactionTaxAmount(String transactionTaxAmount) {
		this.transactionTaxAmount = transactionTaxAmount;
	}
	public String getTransactionTerminalID() {
		return transactionTerminalID;
	}
	public void setTransactionTerminalID(String transactionTerminalID) {
		this.transactionTerminalID = transactionTerminalID;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((cardType == null) ? 0 : cardType.hashCode());
		result = prime * result + ((exchangeAmount == null) ? 0 : exchangeAmount.hashCode());
		result = prime * result + ((exchangeCurrency == null) ? 0 : exchangeCurrency.hashCode());
		result = prime * result + ((merchantCategory == null) ? 0 : merchantCategory.hashCode());
		result = prime * result + ((taxPercentage == null) ? 0 : taxPercentage.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((transactionBalance == null) ? 0 : transactionBalance.hashCode());
		result = prime * result + ((transactionCategory == null) ? 0 : transactionCategory.hashCode());
		result = prime * result + ((transactionCurrencyCode == null) ? 0 : transactionCurrencyCode.hashCode());
		result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
		result = prime * result + ((transactionDescription == null) ? 0 : transactionDescription.hashCode());
		result = prime * result
				+ ((transactionDetailDescription == null) ? 0 : transactionDetailDescription.hashCode());
		result = prime * result + ((transactionExchangeRate == null) ? 0 : transactionExchangeRate.hashCode());
		result = prime * result + ((transactionIndicator == null) ? 0 : transactionIndicator.hashCode());
		result = prime * result
				+ ((transactionMerchantAddressName == null) ? 0 : transactionMerchantAddressName.hashCode());
		result = prime * result + ((transactionMerchantCity == null) ? 0 : transactionMerchantCity.hashCode());
		result = prime * result + ((transactionReferenceNumber == null) ? 0 : transactionReferenceNumber.hashCode());
		result = prime * result + ((transactionStatus == null) ? 0 : transactionStatus.hashCode());
		result = prime * result + ((transactionTaxAmount == null) ? 0 : transactionTaxAmount.hashCode());
		result = prime * result + ((transactionTaxIndicator == null) ? 0 : transactionTaxIndicator.hashCode());
		result = prime * result + ((transactionTerminalID == null) ? 0 : transactionTerminalID.hashCode());
		result = prime * result + ((transactionTime == null) ? 0 : transactionTime.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
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
		CardTransactionsDTO other = (CardTransactionsDTO) obj;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		if (exchangeAmount == null) {
			if (other.exchangeAmount != null)
				return false;
		} else if (!exchangeAmount.equals(other.exchangeAmount))
			return false;
		if (exchangeCurrency == null) {
			if (other.exchangeCurrency != null)
				return false;
		} else if (!exchangeCurrency.equals(other.exchangeCurrency))
			return false;
		if (merchantCategory == null) {
			if (other.merchantCategory != null)
				return false;
		} else if (!merchantCategory.equals(other.merchantCategory))
			return false;
		if (taxPercentage == null) {
			if (other.taxPercentage != null)
				return false;
		} else if (!taxPercentage.equals(other.taxPercentage))
			return false;
		if (transactionAmount == null) {
			if (other.transactionAmount != null)
				return false;
		} else if (!transactionAmount.equals(other.transactionAmount))
			return false;
		if (transactionBalance == null) {
			if (other.transactionBalance != null)
				return false;
		} else if (!transactionBalance.equals(other.transactionBalance))
			return false;
		if (transactionCategory == null) {
			if (other.transactionCategory != null)
				return false;
		} else if (!transactionCategory.equals(other.transactionCategory))
			return false;
		if (transactionCurrencyCode == null) {
			if (other.transactionCurrencyCode != null)
				return false;
		} else if (!transactionCurrencyCode.equals(other.transactionCurrencyCode))
			return false;
		if (transactionDate == null) {
			if (other.transactionDate != null)
				return false;
		} else if (!transactionDate.equals(other.transactionDate))
			return false;
		if (transactionDescription == null) {
			if (other.transactionDescription != null)
				return false;
		} else if (!transactionDescription.equals(other.transactionDescription))
			return false;
		if (transactionDetailDescription == null) {
			if (other.transactionDetailDescription != null)
				return false;
		} else if (!transactionDetailDescription.equals(other.transactionDetailDescription))
			return false;
		if (transactionExchangeRate == null) {
			if (other.transactionExchangeRate != null)
				return false;
		} else if (!transactionExchangeRate.equals(other.transactionExchangeRate))
			return false;
		if (transactionIndicator == null) {
			if (other.transactionIndicator != null)
				return false;
		} else if (!transactionIndicator.equals(other.transactionIndicator))
			return false;
		if (transactionMerchantAddressName == null) {
			if (other.transactionMerchantAddressName != null)
				return false;
		} else if (!transactionMerchantAddressName.equals(other.transactionMerchantAddressName))
			return false;
		if (transactionMerchantCity == null) {
			if (other.transactionMerchantCity != null)
				return false;
		} else if (!transactionMerchantCity.equals(other.transactionMerchantCity))
			return false;
		if (transactionReferenceNumber == null) {
			if (other.transactionReferenceNumber != null)
				return false;
		} else if (!transactionReferenceNumber.equals(other.transactionReferenceNumber))
			return false;
		if (transactionStatus == null) {
			if (other.transactionStatus != null)
				return false;
		} else if (!transactionStatus.equals(other.transactionStatus))
			return false;
		if (transactionTaxAmount == null) {
			if (other.transactionTaxAmount != null)
				return false;
		} else if (!transactionTaxAmount.equals(other.transactionTaxAmount))
			return false;
		if (transactionTaxIndicator == null) {
			if (other.transactionTaxIndicator != null)
				return false;
		} else if (!transactionTaxIndicator.equals(other.transactionTaxIndicator))
			return false;
		if (transactionTerminalID == null) {
			if (other.transactionTerminalID != null)
				return false;
		} else if (!transactionTerminalID.equals(other.transactionTerminalID))
			return false;
		if (transactionTime == null) {
			if (other.transactionTime != null)
				return false;
		} else if (!transactionTime.equals(other.transactionTime))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}
	

}
