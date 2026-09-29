package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author KH2301
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BulkWireFileLineItemsDTO implements DBPDTO {


	private static final long serialVersionUID = 4280121954164668804L;

	private String bulkWireFileLineItemID;
	private String swiftCode;
	private String recipientCountryName;
	private String recipientName;
	private String bulkWireTransferType;
	private String transactionType;
	private String internationalRoutingNumber;
	private String fromAccountNumber;
	private String note;
	private String recipientAddressLine1;
	private String recipientAddressLine2;
	private String recipientCity;
	private String recipientState;
	private String recipientZipCode;
	private String recipientBankName;
	private String recipientBankAddress1;
	private String recipientBankAddress2;
	private String recipientBankZipCode;
	private String recipientBankcity;
	private String recipientBankstate;
	private String currency;
	private String accountNickname;
	private Double amount;
	private String recipientAccountNumber;
	private String routingNumber;



	public String getBulkWireFileLineItemID() {
		return bulkWireFileLineItemID;
	}


	public void setBulkWireFileLineItemID(String bulkWireFileLineItemID) {
		this.bulkWireFileLineItemID = bulkWireFileLineItemID;
	}

	public String getSwiftCode() {
		return swiftCode;
	}


	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}


	public String getRecipientCountryName() {
		return recipientCountryName;
	}


	public void setRecipientCountryName(String recipientCountryName) {
		this.recipientCountryName = recipientCountryName;
	}


	public String getRecipientName() {
		return recipientName;
	}


	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}


	public String getBulkWireTransferType() {
		return bulkWireTransferType;
	}


	public void setBulkWireTransferType(String bulkWireTransferType) {
		this.bulkWireTransferType = bulkWireTransferType;
	}


	public String getTransactionType() {
		return transactionType;
	}


	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}


	public String getInternationalRoutingNumber() {
		return internationalRoutingNumber;
	}


	public void setInternationalRoutingNumber(String internationalRoutingNumber) {
		this.internationalRoutingNumber = internationalRoutingNumber;
	}


	public String getFromAccountNumber() {
		return fromAccountNumber;
	}


	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getRecipientAddressLine1() {
		return recipientAddressLine1;
	}


	public void setRecipientAddressLine1(String recipientAddressLine1) {
		this.recipientAddressLine1 = recipientAddressLine1;
	}


	public String getRecipientAddressLine2() {
		return recipientAddressLine2;
	}


	public void setRecipientAddressLine2(String recipientAddressLine2) {
		this.recipientAddressLine2 = recipientAddressLine2;
	}


	public String getRecipientCity() {
		return recipientCity;
	}


	public void setRecipientCity(String recipientCity) {
		this.recipientCity = recipientCity;
	}


	public String getRecipientState() {
		return recipientState;
	}


	public void setRecipientState(String recipientState) {
		this.recipientState = recipientState;
	}


	public String getRecipientZipCode() {
		return recipientZipCode;
	}


	public void setRecipientZipCode(String recipientZipCode) {
		this.recipientZipCode = recipientZipCode;
	}


	public String getRecipientBankName() {
		return recipientBankName;
	}


	public void setRecipientBankName(String recipientBankName) {
		this.recipientBankName = recipientBankName;
	}


	public String getRecipientBankAddress1() {
		return recipientBankAddress1;
	}


	public void setRecipientBankAddress1(String recipientBankAddress1) {
		this.recipientBankAddress1 = recipientBankAddress1;
	}


	public String getRecipientBankAddress2() {
		return recipientBankAddress2;
	}


	public void setRecipientBankAddress2(String recipientBankAddress2) {
		this.recipientBankAddress2 = recipientBankAddress2;
	}


	public String getRecipientBankZipCode() {
		return recipientBankZipCode;
	}


	public void setRecipientBankZipCode(String recipientBankZipCode) {
		this.recipientBankZipCode = recipientBankZipCode;
	}


	public String getRecipientBankcity() {
		return recipientBankcity;
	}


	public void setRecipientBankcity(String recipientBankcity) {
		this.recipientBankcity = recipientBankcity;
	}


	public String getRecipientBankstate() {
		return recipientBankstate;
	}


	public void setRecipientBankstate(String recipientBankstate) {
		this.recipientBankstate = recipientBankstate;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getAccountNickname() {
		return accountNickname;
	}


	public void setAccountNickname(String accountNickname) {
		this.accountNickname = accountNickname;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getRecipientAccountNumber() {
		return recipientAccountNumber;
	}


	public void setRecipientAccountNumber(String recipientAccountNumber) {
		this.recipientAccountNumber = recipientAccountNumber;
	}


	public String getRoutingNumber() {
		return routingNumber;
	}


	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}


	public BulkWireFileLineItemsDTO(String bulkWireFileLineItemID, String swiftCode,
			String recipientCountryName, String recipientName, String bulkWireTransferType, String transactionType,
			String internationalRoutingNumber, String fromAccountNumber, String note, String recipientAddressLine1,
			String recipientAddressLine2, String recipientCity, String recipientState, String recipientZipCode,
			String recipientBankName, String recipientBankAddress1, String recipientBankAddress2,
			String recipientBankZipCode, String recipientBankcity, String recipientBankstate, String currency,
			String accountNickname, Double amount, String recipientAccountNumber, String routingNumber) {
		super();
		this.bulkWireFileLineItemID = bulkWireFileLineItemID;
		this.swiftCode = swiftCode;
		this.recipientCountryName = recipientCountryName;
		this.recipientName = recipientName;
		this.bulkWireTransferType = bulkWireTransferType;
		this.transactionType = transactionType;
		this.internationalRoutingNumber = internationalRoutingNumber;
		this.fromAccountNumber = fromAccountNumber;
		this.note = note;
		this.recipientAddressLine1 = recipientAddressLine1;
		this.recipientAddressLine2 = recipientAddressLine2;
		this.recipientCity = recipientCity;
		this.recipientState = recipientState;
		this.recipientZipCode = recipientZipCode;
		this.recipientBankName = recipientBankName;
		this.recipientBankAddress1 = recipientBankAddress1;
		this.recipientBankAddress2 = recipientBankAddress2;
		this.recipientBankZipCode = recipientBankZipCode;
		this.recipientBankcity = recipientBankcity;
		this.recipientBankstate = recipientBankstate;
		this.currency = currency;
		this.accountNickname = accountNickname;
		this.amount = amount;
		this.recipientAccountNumber = recipientAccountNumber;
		this.routingNumber = routingNumber;
	}

	public BulkWireFileLineItemsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNickname == null) ? 0 : accountNickname.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((bulkWireFileLineItemID == null) ? 0 : bulkWireFileLineItemID.hashCode());
		result = prime * result + ((bulkWireTransferType == null) ? 0 : bulkWireTransferType.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((internationalRoutingNumber == null) ? 0 : internationalRoutingNumber.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((recipientAccountNumber == null) ? 0 : recipientAccountNumber.hashCode());
		result = prime * result + ((recipientAddressLine1 == null) ? 0 : recipientAddressLine1.hashCode());
		result = prime * result + ((recipientAddressLine2 == null) ? 0 : recipientAddressLine2.hashCode());
		result = prime * result + ((recipientBankAddress1 == null) ? 0 : recipientBankAddress1.hashCode());
		result = prime * result + ((recipientBankAddress2 == null) ? 0 : recipientBankAddress2.hashCode());
		result = prime * result + ((recipientBankName == null) ? 0 : recipientBankName.hashCode());
		result = prime * result + ((recipientBankZipCode == null) ? 0 : recipientBankZipCode.hashCode());
		result = prime * result + ((recipientBankcity == null) ? 0 : recipientBankcity.hashCode());
		result = prime * result + ((recipientBankstate == null) ? 0 : recipientBankstate.hashCode());
		result = prime * result + ((recipientCity == null) ? 0 : recipientCity.hashCode());
		result = prime * result + ((recipientCountryName == null) ? 0 : recipientCountryName.hashCode());
		result = prime * result + ((recipientName == null) ? 0 : recipientName.hashCode());
		result = prime * result + ((recipientState == null) ? 0 : recipientState.hashCode());
		result = prime * result + ((recipientZipCode == null) ? 0 : recipientZipCode.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
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
		BulkWireFileLineItemsDTO other = (BulkWireFileLineItemsDTO) obj;
		if (accountNickname == null) {
			if (other.accountNickname != null)
				return false;
		} else if (!accountNickname.equals(other.accountNickname))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (bulkWireFileLineItemID == null) {
			if (other.bulkWireFileLineItemID != null)
				return false;
		} else if (!bulkWireFileLineItemID.equals(other.bulkWireFileLineItemID))
			return false;
		if (bulkWireTransferType == null) {
			if (other.bulkWireTransferType != null)
				return false;
		} else if (!bulkWireTransferType.equals(other.bulkWireTransferType))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (fromAccountNumber == null) {
			if (other.fromAccountNumber != null)
				return false;
		} else if (!fromAccountNumber.equals(other.fromAccountNumber))
			return false;
		if (internationalRoutingNumber == null) {
			if (other.internationalRoutingNumber != null)
				return false;
		} else if (!internationalRoutingNumber.equals(other.internationalRoutingNumber))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (recipientAccountNumber == null) {
			if (other.recipientAccountNumber != null)
				return false;
		} else if (!recipientAccountNumber.equals(other.recipientAccountNumber))
			return false;
		if (recipientAddressLine1 == null) {
			if (other.recipientAddressLine1 != null)
				return false;
		} else if (!recipientAddressLine1.equals(other.recipientAddressLine1))
			return false;
		if (recipientAddressLine2 == null) {
			if (other.recipientAddressLine2 != null)
				return false;
		} else if (!recipientAddressLine2.equals(other.recipientAddressLine2))
			return false;
		if (recipientBankAddress1 == null) {
			if (other.recipientBankAddress1 != null)
				return false;
		} else if (!recipientBankAddress1.equals(other.recipientBankAddress1))
			return false;
		if (recipientBankAddress2 == null) {
			if (other.recipientBankAddress2 != null)
				return false;
		} else if (!recipientBankAddress2.equals(other.recipientBankAddress2))
			return false;
		if (recipientBankName == null) {
			if (other.recipientBankName != null)
				return false;
		} else if (!recipientBankName.equals(other.recipientBankName))
			return false;
		if (recipientBankZipCode == null) {
			if (other.recipientBankZipCode != null)
				return false;
		} else if (!recipientBankZipCode.equals(other.recipientBankZipCode))
			return false;
		if (recipientBankcity == null) {
			if (other.recipientBankcity != null)
				return false;
		} else if (!recipientBankcity.equals(other.recipientBankcity))
			return false;
		if (recipientBankstate == null) {
			if (other.recipientBankstate != null)
				return false;
		} else if (!recipientBankstate.equals(other.recipientBankstate))
			return false;
		if (recipientCity == null) {
			if (other.recipientCity != null)
				return false;
		} else if (!recipientCity.equals(other.recipientCity))
			return false;
		if (recipientCountryName == null) {
			if (other.recipientCountryName != null)
				return false;
		} else if (!recipientCountryName.equals(other.recipientCountryName))
			return false;
		if (recipientName == null) {
			if (other.recipientName != null)
				return false;
		} else if (!recipientName.equals(other.recipientName))
			return false;
		if (recipientState == null) {
			if (other.recipientState != null)
				return false;
		} else if (!recipientState.equals(other.recipientState))
			return false;
		if (recipientZipCode == null) {
			if (other.recipientZipCode != null)
				return false;
		} else if (!recipientZipCode.equals(other.recipientZipCode))
			return false;
		if (routingNumber == null) {
			if (other.routingNumber != null)
				return false;
		} else if (!routingNumber.equals(other.routingNumber))
			return false;
		if (swiftCode == null) {
			if (other.swiftCode != null)
				return false;
		} else if (!swiftCode.equals(other.swiftCode))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}
}