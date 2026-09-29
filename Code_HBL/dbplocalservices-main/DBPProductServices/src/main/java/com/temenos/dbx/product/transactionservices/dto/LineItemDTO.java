package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author KH2144
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class LineItemDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3444161975606377887L;
	
	private String bulkWireTransferType;
	private String recipientCountryName;
	private String recipientName;
	private String transactionType;
	private String recipientAddressLine1;
	private String recipientAddressLine2;
	private String recipientCity;
	private String recipientState;
	private String recipientZipCode;
	private String swiftCode;
	private String recipientAccountNumber;
	private String routingNumber;
	private String internationalRoutingNumber;
	private String accountNickname;
	private String recipientBankName;
	private String recipientBankAddress1;
	private String recipientBankAddress2;
	private String recipientBankcity;
	private String recipientBankstate;
	private String recipientBankZipCode;
	private String fromAccountNumber;
	private String currency;
	private String amount;
	private String note;
	private String bulkWireFileID;
	private String loop_count;
	public LineItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LineItemDTO(String bulkWireTransferType, String recipientCountryName, String recipientName,
			String transactionType, String recipientAddressLine1, String recipientAddressLine2, String recipientCity,
			String recipientState, String recipientZipCode, String swiftCode, String recipientAccountNumber,
			String routingNumber, String internationalRoutingNumber, String accountNickname, String recipientBankName,
			String recipientBankAddress1, String recipientBankAddress2, String recipientBankcity,
			String recipientBankstate, String recipientBankZipCode, String fromAccountNumber, String currency,
			String amount, String note,String bulkWireFileID,String loop_count) {
		super();
		this.bulkWireTransferType = bulkWireTransferType;
		this.recipientCountryName = recipientCountryName;
		this.recipientName = recipientName;
		this.transactionType = transactionType;
		this.recipientAddressLine1 = recipientAddressLine1;
		this.recipientAddressLine2 = recipientAddressLine2;
		this.recipientCity = recipientCity;
		this.recipientState = recipientState;
		this.recipientZipCode = recipientZipCode;
		this.swiftCode = swiftCode;
		this.recipientAccountNumber = recipientAccountNumber;
		this.routingNumber = routingNumber;
		this.internationalRoutingNumber = internationalRoutingNumber;
		this.accountNickname = accountNickname;
		this.recipientBankName = recipientBankName;
		this.recipientBankAddress1 = recipientBankAddress1;
		this.recipientBankAddress2 = recipientBankAddress2;
		this.recipientBankcity = recipientBankcity;
		this.recipientBankstate = recipientBankstate;
		this.recipientBankZipCode = recipientBankZipCode;
		this.fromAccountNumber = fromAccountNumber;
		this.currency = currency;
		this.amount = amount;
		this.note = note;
		this.bulkWireFileID = bulkWireFileID;
		this.loop_count=loop_count;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bulkWireFileID == null) ? 0 : bulkWireFileID.hashCode());
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
		LineItemDTO other = (LineItemDTO) obj;
		if (bulkWireFileID == null) {
			if (other.bulkWireFileID != null)
				return false;
		} else if (!bulkWireFileID.equals(other.bulkWireFileID))
			return false;
		return true;
	}
	public String getBulkWireFileID() {
		return bulkWireFileID;
	}
	public void setBulkWireFileID(String bulkWireFileID) {
		this.bulkWireFileID = bulkWireFileID;
	}
	public String getLoop_count() {
		return loop_count;
	}
	public void setLoop_count(String loop_count) {
		this.loop_count = loop_count;
	}
	public String getBulkWireTransferType() {
		return bulkWireTransferType;
	}
	public void setBulkWireTransferType(String bulkWireTransferType) {
		this.bulkWireTransferType = bulkWireTransferType;
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
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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
	public String getSwiftCode() {
		return swiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
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
	public String getInternationalRoutingNumber() {
		return internationalRoutingNumber;
	}
	public void setInternationalRoutingNumber(String internationalRoutingNumber) {
		this.internationalRoutingNumber = internationalRoutingNumber;
	}
	public String getAccountNickname() {
		return accountNickname;
	}
	public void setAccountNickname(String accountNickname) {
		this.accountNickname = accountNickname;
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
	public String getRecipientBankZipCode() {
		return recipientBankZipCode;
	}
	public void setRecipientBankZipCode(String recipientBankZipCode) {
		this.recipientBankZipCode = recipientBankZipCode;
	}
	public String getFromAccountNumber() {
		return fromAccountNumber;
	}
	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}