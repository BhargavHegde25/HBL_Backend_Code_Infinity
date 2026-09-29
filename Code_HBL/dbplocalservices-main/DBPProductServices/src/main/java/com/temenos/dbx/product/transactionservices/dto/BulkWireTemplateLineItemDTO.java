package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;

public class BulkWireTemplateLineItemDTO implements DBPDTO {

	private static final long serialVersionUID = 6056089083613956720L;
	
	private String bulkWireTemplateLineItemID;
	private String swiftCode;
	private String recipientCountryName;
	private String recipientName;
	private String bulkWireTransferType;
	private String transactionType;
	private String internationalRoutingNumber;
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
	private String recipientAccountNumber;
	private String routingNumber;
	private String templateRecipientCategory;
	private String recipientId;
	private String accountNickname;
	private String payeeId;
	

	public BulkWireTemplateLineItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BulkWireTemplateLineItemDTO(String bulkWireTemplateLineItemID, String swiftCode, String recipientCountryName,
			String recipientName, String bulkWireTransferType, String transactionType,
			String internationalRoutingNumber, String recipientAddressLine1, String recipientAddressLine2,
			String recipientCity, String recipientState, String recipientZipCode, String recipientBankName,
			String recipientBankAddress1, String recipientBankAddress2, String recipientBankZipCode,
			String recipientBankcity, String recipientBankstate, String recipientAccountNumber, String routingNumber,
			String templateRecipientCategory, String recipientId, String accountNickname, String payeeId) {
		super();
		this.bulkWireTemplateLineItemID = bulkWireTemplateLineItemID;
		this.swiftCode = swiftCode;
		this.recipientCountryName = recipientCountryName;
		this.recipientName = recipientName;
		this.bulkWireTransferType = bulkWireTransferType;
		this.transactionType = transactionType;
		this.internationalRoutingNumber = internationalRoutingNumber;
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
		this.recipientAccountNumber = recipientAccountNumber;
		this.routingNumber = routingNumber;
		this.templateRecipientCategory = templateRecipientCategory;
		this.recipientId = recipientId;
		this.accountNickname = accountNickname;
		this.payeeId = payeeId;
	}
	
	

	public String getBulkWireTemplateLineItemID() {
		return bulkWireTemplateLineItemID;
	}

	public void setBulkWireTemplateLineItemID(String bulkWireTemplateLineItemID) {
		this.bulkWireTemplateLineItemID = bulkWireTemplateLineItemID;
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

	public String getTemplateRecipientCategory() {
		return templateRecipientCategory;
	}

	public void setTemplateRecipientCategory(String templateRecipientCategory) {
		this.templateRecipientCategory = templateRecipientCategory;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getAccountNickname() {
		return accountNickname;
	}

	public void setAccountNickname(String accountNickname) {
		this.accountNickname = accountNickname;
	}
	
	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNickname == null) ? 0 : accountNickname.hashCode());
		result = prime * result + ((bulkWireTemplateLineItemID == null) ? 0 : bulkWireTemplateLineItemID.hashCode());
		result = prime * result + ((bulkWireTransferType == null) ? 0 : bulkWireTransferType.hashCode());
		result = prime * result + ((internationalRoutingNumber == null) ? 0 : internationalRoutingNumber.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
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
		result = prime * result + ((recipientId == null) ? 0 : recipientId.hashCode());
		result = prime * result + ((recipientName == null) ? 0 : recipientName.hashCode());
		result = prime * result + ((recipientState == null) ? 0 : recipientState.hashCode());
		result = prime * result + ((recipientZipCode == null) ? 0 : recipientZipCode.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((templateRecipientCategory == null) ? 0 : templateRecipientCategory.hashCode());
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
		BulkWireTemplateLineItemDTO other = (BulkWireTemplateLineItemDTO) obj;
		if (accountNickname == null) {
			if (other.accountNickname != null)
				return false;
		} else if (!accountNickname.equals(other.accountNickname))
			return false;
		if (bulkWireTemplateLineItemID == null) {
			if (other.bulkWireTemplateLineItemID != null)
				return false;
		} else if (!bulkWireTemplateLineItemID.equals(other.bulkWireTemplateLineItemID))
			return false;
		if (bulkWireTransferType == null) {
			if (other.bulkWireTransferType != null)
				return false;
		} else if (!bulkWireTransferType.equals(other.bulkWireTransferType))
			return false;
		if (internationalRoutingNumber == null) {
			if (other.internationalRoutingNumber != null)
				return false;
		} else if (!internationalRoutingNumber.equals(other.internationalRoutingNumber))
			return false;
		if (payeeId == null) {
			if (other.payeeId != null)
				return false;
		} else if (!payeeId.equals(other.payeeId))
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
		if (recipientId == null) {
			if (other.recipientId != null)
				return false;
		} else if (!recipientId.equals(other.recipientId))
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
		if (templateRecipientCategory == null) {
			if (other.templateRecipientCategory != null)
				return false;
		} else if (!templateRecipientCategory.equals(other.templateRecipientCategory))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}
}
