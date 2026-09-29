package com.kony.dbp.cardservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author KH2638
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardsDTO implements DBPDTO{
	
	private static final long serialVersionUID = 886403891118328614L;
	
	private int Id;
	private String card_Status;
	private String User_id;
	private String expirationDate;
	private String pinNumber;
	private String reason;
	private long cardNumber;
	private String cardType;
	private String action;
	private String account_id;
	private String creditLimit;
	private String availableCredit;
	private String serviceProvider;
	private String billingAddress;
	private String cardProductName;
	private String secondaryCardHolder;
	private String withdrawlLimit;
	private String isInternational;
	private String bankName;
	private String cardHolderName;
	private int cvv;
	private int cardDisplayName;
	public CardsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CardsDTO(int id, String card_Status, String user_id, String expirationDate, String pinNumber, String reason,
			long cardNumber, String cardType, String action, String account_id, String creditLimit,
			String availableCredit, String serviceProvider, String billingAddress, String cardProductName,
			String secondaryCardHolder, String withdrawlLimit, String isInternational, String bankName,
			String cardHolderName, int cvv, int cardDisplayName) {
		super();
		Id = id;
		this.card_Status = card_Status;
		User_id = user_id;
		this.expirationDate = expirationDate;
		this.pinNumber = pinNumber;
		this.reason = reason;
		this.cardNumber = cardNumber;
		this.cardType = cardType;
		this.action = action;
		this.account_id = account_id;
		this.creditLimit = creditLimit;
		this.availableCredit = availableCredit;
		this.serviceProvider = serviceProvider;
		this.billingAddress = billingAddress;
		this.cardProductName = cardProductName;
		this.secondaryCardHolder = secondaryCardHolder;
		this.withdrawlLimit = withdrawlLimit;
		this.isInternational = isInternational;
		this.bankName = bankName;
		this.cardHolderName = cardHolderName;
		this.cvv = cvv;
		this.cardDisplayName = cardDisplayName;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getCard_Status() {
		return card_Status;
	}
	public void setCard_Status(String card_Status) {
		this.card_Status = card_Status;
	}
	public String getUser_id() {
		return User_id;
	}
	public void setUser_id(String user_id) {
		User_id = user_id;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getPinNumber() {
		return pinNumber;
	}
	public void setPinNumber(String pinNumber) {
		this.pinNumber = pinNumber;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getAvailableCredit() {
		return availableCredit;
	}
	public void setAvailableCredit(String availableCredit) {
		this.availableCredit = availableCredit;
	}
	public String getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getCardProductName() {
		return cardProductName;
	}
	public void setCardProductName(String cardProductName) {
		this.cardProductName = cardProductName;
	}
	public String getSecondaryCardHolder() {
		return secondaryCardHolder;
	}
	public void setSecondaryCardHolder(String secondaryCardHolder) {
		this.secondaryCardHolder = secondaryCardHolder;
	}
	public String getWithdrawlLimit() {
		return withdrawlLimit;
	}
	public void setWithdrawlLimit(String withdrawlLimit) {
		this.withdrawlLimit = withdrawlLimit;
	}
	public String getIsInternational() {
		return isInternational;
	}
	public void setIsInternational(String isInternational) {
		this.isInternational = isInternational;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public int getCardDisplayName() {
		return cardDisplayName;
	}
	public void setCardDisplayName(int cardDisplayName) {
		this.cardDisplayName = cardDisplayName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Id;
		result = prime * result + ((User_id == null) ? 0 : User_id.hashCode());
		result = prime * result + ((account_id == null) ? 0 : account_id.hashCode());
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((availableCredit == null) ? 0 : availableCredit.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
		result = prime * result + cardDisplayName;
		result = prime * result + ((cardHolderName == null) ? 0 : cardHolderName.hashCode());
		result = prime * result + (int) (cardNumber ^ (cardNumber >>> 32));
		result = prime * result + ((cardProductName == null) ? 0 : cardProductName.hashCode());
		result = prime * result + ((cardType == null) ? 0 : cardType.hashCode());
		result = prime * result + ((card_Status == null) ? 0 : card_Status.hashCode());
		result = prime * result + ((creditLimit == null) ? 0 : creditLimit.hashCode());
		result = prime * result + cvv;
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((isInternational == null) ? 0 : isInternational.hashCode());
		result = prime * result + ((pinNumber == null) ? 0 : pinNumber.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((secondaryCardHolder == null) ? 0 : secondaryCardHolder.hashCode());
		result = prime * result + ((serviceProvider == null) ? 0 : serviceProvider.hashCode());
		result = prime * result + ((withdrawlLimit == null) ? 0 : withdrawlLimit.hashCode());
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
		CardsDTO other = (CardsDTO) obj;
		if (Id != other.Id)
			return false;
		if (User_id == null) {
			if (other.User_id != null)
				return false;
		} else if (!User_id.equals(other.User_id))
			return false;
		if (account_id == null) {
			if (other.account_id != null)
				return false;
		} else if (!account_id.equals(other.account_id))
			return false;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (availableCredit == null) {
			if (other.availableCredit != null)
				return false;
		} else if (!availableCredit.equals(other.availableCredit))
			return false;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		if (billingAddress == null) {
			if (other.billingAddress != null)
				return false;
		} else if (!billingAddress.equals(other.billingAddress))
			return false;
		if (cardDisplayName != other.cardDisplayName)
			return false;
		if (cardHolderName == null) {
			if (other.cardHolderName != null)
				return false;
		} else if (!cardHolderName.equals(other.cardHolderName))
			return false;
		if (cardNumber != other.cardNumber)
			return false;
		if (cardProductName == null) {
			if (other.cardProductName != null)
				return false;
		} else if (!cardProductName.equals(other.cardProductName))
			return false;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		if (card_Status == null) {
			if (other.card_Status != null)
				return false;
		} else if (!card_Status.equals(other.card_Status))
			return false;
		if (creditLimit == null) {
			if (other.creditLimit != null)
				return false;
		} else if (!creditLimit.equals(other.creditLimit))
			return false;
		if (cvv != other.cvv)
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (isInternational == null) {
			if (other.isInternational != null)
				return false;
		} else if (!isInternational.equals(other.isInternational))
			return false;
		if (pinNumber == null) {
			if (other.pinNumber != null)
				return false;
		} else if (!pinNumber.equals(other.pinNumber))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (secondaryCardHolder == null) {
			if (other.secondaryCardHolder != null)
				return false;
		} else if (!secondaryCardHolder.equals(other.secondaryCardHolder))
			return false;
		if (serviceProvider == null) {
			if (other.serviceProvider != null)
				return false;
		} else if (!serviceProvider.equals(other.serviceProvider))
			return false;
		if (withdrawlLimit == null) {
			if (other.withdrawlLimit != null)
				return false;
		} else if (!withdrawlLimit.equals(other.withdrawlLimit))
			return false;
		return true;
	}
	
}