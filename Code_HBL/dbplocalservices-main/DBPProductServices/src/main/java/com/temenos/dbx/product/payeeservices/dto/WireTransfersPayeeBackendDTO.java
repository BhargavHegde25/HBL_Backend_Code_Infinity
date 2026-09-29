package com.temenos.dbx.product.payeeservices.dto;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.commonsutils.ValidationUtils;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WireTransfersPayeeBackendDTO implements DBPDTO {
	
    private static final long serialVersionUID = 6272269331835905801L;
	
    @JsonAlias({"Id","payeeId"})
	private String id;
    private String payeeId;
	@JsonAlias({"Type_id","type"})
	private String typeId;
	@JsonAlias({"payeeName"})
	private String name;
	@JsonAlias({"payeeAccountNumber"})
	private String accountNumber;
	private String companyName;
	private String phone;
	private String email;
	private String firstName;
	private String lastName;
	@JsonAlias({"eBillEnable"})
	private String ebillEnable;
	@JsonAlias({"Region_id"})
	private String regionId;
	@JsonAlias({"City_id"})
	private String cityId;
	private String cityName;
	private String state;
	private String addressLine1;
	private String addressLine2;
	private String zipCode;
	@JsonAlias({"User_Id"})
	private String userId;
	@JsonAlias({"payeeNickName"})
	private String nickName;
	private String softDelete;
	private String billermaster_id;
	private String isAutoPayEnabled;
	private String nameOnBill;
	private String notes;
	private String billerId;
	private String country;
	private String swiftCode;
	private String routingCode;
	private String bankName;
	@JsonAlias({"bankAddressLine1"})
	private String bankAddressLine1;
	@JsonAlias({"bankAddressLine2"})
	private String bankAddressLine2;
	@JsonAlias({"bankCity"})
	private String bankCity;
	@JsonAlias({"bankState"})
	private String bankState;
	@JsonAlias({"bankZip"})
	private String bankZip;
	private String isWiredRecepient;
	private String internationalAccountNumber;
	private String wireAccountType;
	private String internationalRoutingCode;
	private String isManuallyAdded;
	private String phoneExtension;
	private String phoneCountryCode;
	@JsonAlias({"IBAN"})
	private String iban;
	private String transitDays;
	
	private String billId;
	private String lastPaidAmount;
	private String lastPaidDate;
	private String billDueDate;
	private String billDescription;
	private String paidAmount;
	private String dueAmount;
	private String billGeneratedDate;
	private String ebillURL;
	private String billerCategory;
	private String ebillSupport;
	private String street;
	private String payeeAccountNumber;
	private String payeeName;
	private String payeeNickName;
	private String cif;
	@JsonIgnore
	private String isBusinessPayee;
	@JsonAlias({"organizationId"})
	private String companyId;
	private String noOfCustomersLinked;
	
	private String dbpErrCode;
	@JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	
	
	public WireTransfersPayeeBackendDTO() {
		super();
	}

	public WireTransfersPayeeBackendDTO(String id, String typeId, String name, String accountNumber, String companyName,
			String phone, String email, String firstName, String lastName, String ebillEnable, String regionId,
			String cityId, String cityName, String state, String addressLine1, String addressLine2, String zipCode,
			String userId, String nickName, String softDelete, String billermaster_id, String isAutoPayEnabled,
			String nameOnBill, String notes, String billerId, String country, String swiftCode, String routingCode,
			String bankName, String bankAddressLine1, String bankAddressLine2, String bankCity, String bankState,
			String bankZip, String isWiredRecepient, String internationalAccountNumber, String wireAccountType,
			String internationalRoutingCode, String isManuallyAdded, String phoneExtension, String phoneCountryCode,
			String iban, String transitDays, String billId, String lastPaidAmount, String lastPaidDate,
			String billDueDate, String billDescription, String paidAmount, String dueAmount, String billGeneratedDate,
			String ebillURL, String billerCategory, String ebillSupport, String cif, String isBusinessPayee, 
			String companyId, String street, String dbpErrCode, String dbpErrMsg, String payeeId, String payeeName, String payeeNickName, String payeeAccountNumber, String noOfCustomersLinked) {
		super();
		this.id = id;
		this.typeId = typeId;
		this.name = name;
		this.accountNumber = accountNumber;
		this.companyName = companyName;
		this.phone = phone;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.ebillEnable = ebillEnable;
		this.regionId = regionId;
		this.cityId = cityId;
		this.cityName = cityName;
		this.state = state;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.zipCode = zipCode;
		this.userId = userId;
		this.nickName = nickName;
		this.softDelete = softDelete;
		this.billermaster_id = billermaster_id;
		this.isAutoPayEnabled = isAutoPayEnabled;
		this.nameOnBill = nameOnBill;
		this.notes = notes;
		this.billerId = billerId;
		this.country = country;
		this.swiftCode = swiftCode;
		this.routingCode = routingCode;
		this.bankName = bankName;
		this.bankAddressLine1 = bankAddressLine1;
		this.bankAddressLine2 = bankAddressLine2;
		this.bankCity = bankCity;
		this.bankState = bankState;
		this.bankZip = bankZip;
		this.isWiredRecepient = isWiredRecepient;
		this.internationalAccountNumber = internationalAccountNumber;
		this.wireAccountType = wireAccountType;
		this.internationalRoutingCode = internationalRoutingCode;
		this.isManuallyAdded = isManuallyAdded;
		this.phoneExtension = phoneExtension;
		this.phoneCountryCode = phoneCountryCode;
		this.iban = iban;
		this.transitDays = transitDays;
		this.billId = billId;
		this.lastPaidAmount = lastPaidAmount;
		this.lastPaidDate = lastPaidDate;
		this.billDueDate = billDueDate;
		this.billDescription = billDescription;
		this.paidAmount = paidAmount;
		this.dueAmount = dueAmount;
		this.billGeneratedDate = billGeneratedDate;
		this.ebillURL = ebillURL;
		this.billerCategory = billerCategory;
		this.ebillSupport = ebillSupport;
		this.cif = cif;
		this.isBusinessPayee = isBusinessPayee;
		this.companyId = companyId;
		this.street = street;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.payeeId = payeeId;
		this.payeeName = payeeName;
		this.payeeNickName = payeeNickName;
		this.payeeAccountNumber = payeeAccountNumber;
		this.noOfCustomersLinked = noOfCustomersLinked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.payeeId = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.payeeName = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		this.payeeAccountNumber = accountNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEbillEnable() {
		return ebillEnable;
	}

	public void setEbillEnable(String ebillEnable) {
		this.ebillEnable = ebillEnable;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
		this.payeeNickName = nickName;
	}

	public String getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(String softDelete) {
		this.softDelete = softDelete;
	}

	public String getBillermaster_id() {
		return billermaster_id;
	}

	public void setBillermaster_id(String billermaster_id) {
		this.billermaster_id = billermaster_id;
	}

	public String getIsAutoPayEnabled() {
		return isAutoPayEnabled;
	}

	public void setIsAutoPayEnabled(String isAutoPayEnabled) {
		this.isAutoPayEnabled = isAutoPayEnabled;
	}

	public String getNameOnBill() {
		return nameOnBill;
	}

	public void setNameOnBill(String nameOnBill) {
		this.nameOnBill = nameOnBill;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddressLine1() {
		return bankAddressLine1;
	}

	public void setBankAddressLine1(String bankAddressLine1) {
		this.bankAddressLine1 = bankAddressLine1;
	}

	public String getBankAddressLine2() {
		return bankAddressLine2;
	}

	public void setBankAddressLine2(String bankAddressLine2) {
		this.bankAddressLine2 = bankAddressLine2;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankState() {
		return bankState;
	}

	public void setBankState(String bankState) {
		this.bankState = bankState;
	}

	public String getBankZip() {
		return bankZip;
	}

	public void setBankZip(String bankZip) {
		this.bankZip = bankZip;
	}

	public String getIsWiredRecepient() {
		return isWiredRecepient;
	}

	public void setIsWiredRecepient(String isWiredRecepient) {
		this.isWiredRecepient = isWiredRecepient;
	}

	public String getInternationalAccountNumber() {
		return internationalAccountNumber;
	}

	public void setInternationalAccountNumber(String internationalAccountNumber) {
		this.internationalAccountNumber = internationalAccountNumber;
	}

	public String getWireAccountType() {
		return wireAccountType;
	}

	public void setWireAccountType(String wireAccountType) {
		this.wireAccountType = wireAccountType;
	}

	public String getInternationalRoutingCode() {
		return internationalRoutingCode;
	}

	public void setInternationalRoutingCode(String internationalRoutingCode) {
		this.internationalRoutingCode = internationalRoutingCode;
	}

	public String getIsManuallyAdded() {
		return isManuallyAdded;
	}

	public void setIsManuallyAdded(String isManuallyAdded) {
		this.isManuallyAdded = isManuallyAdded;
	}

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getTransitDays() {
		return transitDays;
	}

	public void setTransitDays(String transitDays) {
		this.transitDays = transitDays;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getLastPaidAmount() {
		return lastPaidAmount;
	}

	public void setLastPaidAmount(String lastPaidAmount) {
		this.lastPaidAmount = lastPaidAmount;
	}

	public String getLastPaidDate() {
		return lastPaidDate;
	}

	public void setLastPaidDate(String lastPaidDate) {
		this.lastPaidDate = lastPaidDate;
	}

	public String getBillDueDate() {
		return billDueDate;
	}

	public void setBillDueDate(String billDueDate) {
		this.billDueDate = billDueDate;
	}

	public String getBillDescription() {
		return billDescription;
	}

	public void setBillDescription(String billDescription) {
		this.billDescription = billDescription;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(String dueAmount) {
		this.dueAmount = dueAmount;
	}

	public String getBillGeneratedDate() {
		return billGeneratedDate;
	}

	public void setBillGeneratedDate(String billGeneratedDate) {
		this.billGeneratedDate = billGeneratedDate;
	}

	public String getEbillURL() {
		return ebillURL;
	}

	public void setEbillURL(String ebillURL) {
		this.ebillURL = ebillURL;
	}

	public String getBillerCategory() {
		return billerCategory;
	}

	public void setBillerCategory(String billerCategory) {
		this.billerCategory = billerCategory;
	}

	public String getEbillSupport() {
		return ebillSupport;
	}

	public void setEbillSupport(String ebillSupport) {
		this.ebillSupport = ebillSupport;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getIsBusinessPayee() {
		return isBusinessPayee;
	}

	public void setIsBusinessPayee(String isBusinessPayee) {
		this.isBusinessPayee = isBusinessPayee;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	
	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
		this.id = payeeId;
	}
	
	public String getPayeeAccountNumber() {
		return payeeAccountNumber;
	}

	public void setPayeeAccountNumber(String payeeAccountNumber) {
		this.payeeAccountNumber = payeeAccountNumber;
		this.accountNumber = payeeAccountNumber;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
		this.name = payeeName;
	}

	public String getPayeeNickName() {
		return payeeNickName;
	}

	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
		this.nickName = payeeNickName;
	}
	
	public String getNoOfCustomersLinked() {
		return noOfCustomersLinked;
	}

	public void setNoOfCustomersLinked(String noOfCustomersLinked) {
		this.noOfCustomersLinked = noOfCustomersLinked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((addressLine1 == null) ? 0 : addressLine1.hashCode());
		result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
		result = prime * result + ((bankAddressLine1 == null) ? 0 : bankAddressLine1.hashCode());
		result = prime * result + ((bankAddressLine2 == null) ? 0 : bankAddressLine2.hashCode());
		result = prime * result + ((bankCity == null) ? 0 : bankCity.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankState == null) ? 0 : bankState.hashCode());
		result = prime * result + ((bankZip == null) ? 0 : bankZip.hashCode());
		result = prime * result + ((billDescription == null) ? 0 : billDescription.hashCode());
		result = prime * result + ((billDueDate == null) ? 0 : billDueDate.hashCode());
		result = prime * result + ((billGeneratedDate == null) ? 0 : billGeneratedDate.hashCode());
		result = prime * result + ((billId == null) ? 0 : billId.hashCode());
		result = prime * result + ((billerCategory == null) ? 0 : billerCategory.hashCode());
		result = prime * result + ((billerId == null) ? 0 : billerId.hashCode());
		result = prime * result + ((billermaster_id == null) ? 0 : billermaster_id.hashCode());
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((dueAmount == null) ? 0 : dueAmount.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((ebillEnable == null) ? 0 : ebillEnable.hashCode());
		result = prime * result + ((ebillSupport == null) ? 0 : ebillSupport.hashCode());
		result = prime * result + ((ebillURL == null) ? 0 : ebillURL.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((internationalAccountNumber == null) ? 0 : internationalAccountNumber.hashCode());
		result = prime * result + ((internationalRoutingCode == null) ? 0 : internationalRoutingCode.hashCode());
		result = prime * result + ((isAutoPayEnabled == null) ? 0 : isAutoPayEnabled.hashCode());
		result = prime * result + ((isBusinessPayee == null) ? 0 : isBusinessPayee.hashCode());
		result = prime * result + ((isManuallyAdded == null) ? 0 : isManuallyAdded.hashCode());
		result = prime * result + ((isWiredRecepient == null) ? 0 : isWiredRecepient.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((lastPaidAmount == null) ? 0 : lastPaidAmount.hashCode());
		result = prime * result + ((lastPaidDate == null) ? 0 : lastPaidDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameOnBill == null) ? 0 : nameOnBill.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((paidAmount == null) ? 0 : paidAmount.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((phoneCountryCode == null) ? 0 : phoneCountryCode.hashCode());
		result = prime * result + ((phoneExtension == null) ? 0 : phoneExtension.hashCode());
		result = prime * result + ((regionId == null) ? 0 : regionId.hashCode());
		result = prime * result + ((routingCode == null) ? 0 : routingCode.hashCode());
		result = prime * result + ((softDelete == null) ? 0 : softDelete.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((transitDays == null) ? 0 : transitDays.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((wireAccountType == null) ? 0 : wireAccountType.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((payeeNickName == null) ? 0 : payeeNickName.hashCode());
		result = prime * result + ((payeeAccountNumber == null) ? 0 : payeeAccountNumber.hashCode());
		result = prime * result + ((noOfCustomersLinked == null) ? 0 : noOfCustomersLinked.hashCode());
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
		WireTransfersPayeeBackendDTO other = (WireTransfersPayeeBackendDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean isValidInput() {
		
		if(StringUtils.isNotBlank(this.nickName) && !ValidationUtils.isValidName(this.nickName) ) {
    		return false;
    	}
		if(StringUtils.isNotBlank(this.accountNumber) && !ValidationUtils.isValidAccountNumber(this.accountNumber) ) {
    		return false;
    	}
		if(StringUtils.isNotBlank(this.name) && !ValidationUtils.isValidName(this.name) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.zipCode) && !ValidationUtils.isValidText(this.zipCode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.cityName) && !ValidationUtils.isValidText(this.cityName) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.state) && !ValidationUtils.isValidText(this.state) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.addressLine1) && !ValidationUtils.isValidText(this.addressLine1) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.addressLine2) && !ValidationUtils.isValidText(this.addressLine2) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.typeId) && !(this.typeId.equalsIgnoreCase("Individual") || this.typeId.equalsIgnoreCase("Business")) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.country) && !ValidationUtils.isValidText(this.country) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.bankName) && !ValidationUtils.isValidText(this.bankName) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.bankAddressLine1) && !ValidationUtils.isValidText(this.bankAddressLine1) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.bankAddressLine2) && !ValidationUtils.isValidText(this.bankAddressLine2) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.bankCity) && !ValidationUtils.isValidText(this.bankCity) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.bankState) && !ValidationUtils.isValidText(this.bankState) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.bankZip) && !ValidationUtils.isValidText(this.bankZip) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.internationalRoutingCode) && !ValidationUtils.isValidRoutingNumber(this.internationalRoutingCode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.swiftCode) && !ValidationUtils.isValidSwiftCode(this.swiftCode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.routingCode) && !ValidationUtils.isValidRoutingNumber(this.routingCode) ) {
    		return false;
    	}
    	return true;
    }

}
