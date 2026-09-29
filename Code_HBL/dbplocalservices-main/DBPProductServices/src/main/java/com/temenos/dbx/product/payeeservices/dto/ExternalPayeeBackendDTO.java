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
public class ExternalPayeeBackendDTO implements DBPDTO {

	private static final long serialVersionUID = -9181482912063558845L;
	
	@JsonAlias({"Id","payeeId"})
	private String id;
	private String payeeId;
	@JsonAlias({"User_id"})
	private String userId;
	@JsonAlias({"Bank_id"})
	private String bankId;
	@JsonAlias({"payeeNickName"})
	private String nickName;
	private String firstName;
	private String lastName;
	private String routingNumber;
	private String accountNumber;
	private String accountType;
	private String notes;
	private String countryName;
	private String swiftCode;
	private String user_Account;
	private String beneficiaryName;
	private String isInternationalAccount;
	private String bankName;
	private String isSameBankAccount;
	private String softDelete;
	private String isVerified;
	private String createdOn;
	private String externalaccount;
	@JsonAlias({"IBAN"})
	private String iban;
	private String sortCode;
	private String phoneCountryCode;
	private String phoneNumber;
	private String phone;
	private String phoneExtension;
	private String addressNickName;
	private String addressLine1;
	private String city;
	private String zipcode;
	@JsonAlias({"countryName"})
	private String country;
	private String externalaccountcol;
    @JsonAlias({"addressLine02"})
    private String addressLine2;
    private String email;
	
	private String cif;
	@JsonIgnore
	private String isBusinessPayee;
	@JsonAlias({"organizationId"})
	private String companyId;
	private String noOfCustomersLinked;
	
	private String dbpErrCode;
	@JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;



	private String isApproved;
	
	public ExternalPayeeBackendDTO() {
		super();
	}

	public ExternalPayeeBackendDTO(String id, String userId, String bankId, String nickName, String firstName,
			String lastName, String routingNumber, String accountNumber, String accountType, String notes,
			String countryName, String swiftCode, String user_Account, String beneficiaryName,
			String isInternationalAccount, String bankName, String isSameBankAccount, String softDelete,
			String isVerified, String createdOn, String externalaccount, String iban, String sortCode,
			String phoneCountryCode, String phoneNumber, String phoneExtension, String addressNickName,
			String addressLine1, String city, String zipcode, String country, String externalaccountcol, String cif,
			String isBusinessPayee, String companyId, String dbpErrCode, String dbpErrMsg, String payeeId, String noOfCustomersLinked,
			String addressLine2, String email, String phone) {
		super();
		this.id = id;
		this.userId = userId;
		this.bankId = bankId;
		this.nickName = nickName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.routingNumber = routingNumber;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.notes = notes;
		this.countryName = countryName;
		this.swiftCode = swiftCode;
		this.user_Account = user_Account;
		this.beneficiaryName = beneficiaryName;
		this.isInternationalAccount = isInternationalAccount;
		this.bankName = bankName;
		this.isSameBankAccount = isSameBankAccount;
		this.softDelete = softDelete;
		this.isVerified = isVerified;
		this.createdOn = createdOn;
		this.externalaccount = externalaccount;
		this.iban = iban;
		this.sortCode = sortCode;
		this.phoneCountryCode = phoneCountryCode;
		this.phoneNumber = phoneNumber;
		this.phoneExtension = phoneExtension;
		this.addressNickName = addressNickName;
		this.addressLine1 = addressLine1;
		this.city = city;
		this.zipcode = zipcode;
		this.country = country;
		this.externalaccountcol = externalaccountcol;
		this.cif = cif;
		this.isBusinessPayee = isBusinessPayee;
		this.companyId = companyId;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.payeeId = payeeId;
		this.noOfCustomersLinked = noOfCustomersLinked;
        this.addressLine2 = addressLine2;
        this.email = email;
        this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.payeeId = id;
	}
	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
		this.id = payeeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getUser_Account() {
		return user_Account;
	}

	public void setUser_Account(String user_Account) {
		this.user_Account = user_Account;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getIsInternationalAccount() {
		return isInternationalAccount;
	}

	public void setIsInternationalAccount(String isInternationalAccount) {
		this.isInternationalAccount = isInternationalAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIsSameBankAccount() {
		return isSameBankAccount;
	}

	public void setIsSameBankAccount(String isSameBankAccount) {
		this.isSameBankAccount = isSameBankAccount;
	}

	public String getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(String softDelete) {
		this.softDelete = softDelete;
	}

	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getExternalaccount() {
		return externalaccount;
	}

	public void setExternalaccount(String externalaccount) {
		this.externalaccount = externalaccount;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.phone = phoneNumber;
	}

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public String getAddressNickName() {
		return addressNickName;
	}

	public void setAddressNickName(String addressNickName) {
		this.addressNickName = addressNickName;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getExternalaccountcol() {
		return externalaccountcol;
	}

	public void setExternalaccountcol(String externalaccountcol) {
		this.externalaccountcol = externalaccountcol;
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

	public String getNoOfCustomersLinked() {
		return noOfCustomersLinked;
	}

	public void setNoOfCustomersLinked(String noOfCustomersLinked) {
		this.noOfCustomersLinked = noOfCustomersLinked;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
		
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
		this.phoneNumber = phone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((addressLine1 == null) ? 0 : addressLine1.hashCode());
		result = prime * result + ((addressNickName == null) ? 0 : addressNickName.hashCode());
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
		result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((externalaccount == null) ? 0 : externalaccount.hashCode());
		result = prime * result + ((externalaccountcol == null) ? 0 : externalaccountcol.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isBusinessPayee == null) ? 0 : isBusinessPayee.hashCode());
		result = prime * result + ((isInternationalAccount == null) ? 0 : isInternationalAccount.hashCode());
		result = prime * result + ((isSameBankAccount == null) ? 0 : isSameBankAccount.hashCode());
		result = prime * result + ((isVerified == null) ? 0 : isVerified.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((phoneCountryCode == null) ? 0 : phoneCountryCode.hashCode());
		result = prime * result + ((phoneExtension == null) ? 0 : phoneExtension.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + ((softDelete == null) ? 0 : softDelete.hashCode());
		result = prime * result + ((sortCode == null) ? 0 : sortCode.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((user_Account == null) ? 0 : user_Account.hashCode());
		result = prime * result + ((zipcode == null) ? 0 : zipcode.hashCode());
		result = prime * result + ((noOfCustomersLinked == null) ? 0 : noOfCustomersLinked.hashCode());
        result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		ExternalPayeeBackendDTO other = (ExternalPayeeBackendDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean isValidInput() {
    	if(StringUtils.isNotBlank(this.accountNumber) && !ValidationUtils.isValidAccountNumber(this.accountNumber) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.iban) && !ValidationUtils.isValidAccountNumber(this.iban) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.beneficiaryName) && !ValidationUtils.isValidName(this.beneficiaryName) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.nickName) && !ValidationUtils.isValidName(this.nickName) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.addressLine1) && !ValidationUtils.isValidText(this.addressLine1) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.addressLine2) && !ValidationUtils.isValidText(this.addressLine2) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.city) && !ValidationUtils.isValidText(this.city) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.country) && !ValidationUtils.isValidText(this.country) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.zipcode) && !ValidationUtils.isValidText(this.zipcode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.email) && !ValidationUtils.isValidEmail(this.email) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.phone) && !ValidationUtils.isValidPhoneNumber(this.phone) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.swiftCode) && !ValidationUtils.isValidSwiftCode(this.swiftCode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.routingNumber) && !ValidationUtils.isValidRoutingNumber(this.routingNumber) ) {
    		return false;
    	}
    	return true;
    }
}