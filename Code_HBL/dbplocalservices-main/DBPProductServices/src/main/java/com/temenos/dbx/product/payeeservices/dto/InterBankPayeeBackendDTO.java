package com.temenos.dbx.product.payeeservices.dto;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.temenos.dbx.product.commonsutils.ValidationUtils;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterBankPayeeBackendDTO  implements DBPDTO {
	private static final long serialVersionUID = 2327170752243448591L;
	
	@JsonAlias({"Id","payeeId"})
    private String id;
    private String payeeId;
    @JsonAlias({"User_id"})
    private String userId;
    private String accountNumber;
    private String accountType;
    private String bankName;
    private String beneficiaryName;
    private String countryName;
    private String createdOn;
    private String firstName;
    private String isInternationalAccount;
    private String isSameBankAccount;
    private String isVerified;
    private String lastName;
    private String nickName;
    private String notes;
    private String routingNumber;
    private String swiftCode;
    @JsonIgnore
    private String isBusinessPayee;
    private Object cif;
    private String softDelete;
    @JsonAlias({"externalaccount"})
    private String externalAccount;
    @JsonAlias({"IBAN"})
    private String iban;
    private String sortCode;
    private String phoneCountryCode;
    private String phoneNumber;
    private String phoneExtension;
    private String addressNickName;
    private String addressLine1;
    private String city;
    private String zipcode;
    @JsonAlias({"countryName"})
    private String country;
    @JsonAlias({"organizationId"})
    private String companyId;
    private String customerId;
    private String noOfCustomersLinked;
    @JsonAlias({"externalaccountcol"})
    private String externalAccountCol;
    private  String dbpErrCode;
    @JsonAlias({"errorMessage", "errmsg"})
    private  String dbpErrMsg;
    private String deletedRecords;
    @JsonAlias({"addressLine02"})
    private String addressLine2;
    private String email;
    private String phone;
    private String legalEntityId;
    private String isApproved;
    private String clearingCode;
    private String clearingIdentifierCode;
    private String payeeVerification;
    private String bankId;
    
    public String getClearingCode() {
        return clearingCode;
    }

    public void setClearingCode(String clearingCode) {
        this.clearingCode = clearingCode;
    }
    
    public String getClearingIdentifierCode() {
        return clearingIdentifierCode;
    }

    public void setClearingIdentifierCode(String clearingIdentifierCode) {
        this.clearingIdentifierCode = clearingIdentifierCode;
    }
    
    public String getPayeeVerification() {
        return payeeVerification;
    }

    public void setPayeeVerification(String payeeVerification) {
        this.payeeVerification = payeeVerification;
    }
    
    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }
    public String getCustomerId() { return customerId;}

    public void setCustomerId(String customerId) { this.customerId = customerId;}

    public String getCompanyId() { return companyId;}

    public void setCompanyId(String companyId) {this.companyId = companyId;}

    public String getExternalAccount() {
        return externalAccount;
    }

    public void setExternalAccount(String externalAccount) {
        this.externalAccount = externalAccount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getExternalAccountCol() {
        return externalAccountCol;
    }

    public void setExternalAccountCol(String externalAccountCol) {
        this.externalAccountCol = externalAccountCol;
    }

    public  String getSortCode() {
        return sortCode;
    }

    public void setSortCode( String sortCode) {
        this.sortCode = sortCode;
    }

    public  String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode( String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public  String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.phone = phoneNumber;
    }

    public  String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension( String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public  String getAddressNickName() {
        return addressNickName;
    }

    public void setAddressNickName( String addressNickName) {
        this.addressNickName = addressNickName;
    }

    public  String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1( String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public  String getCity() {
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

    public  String getId() {
        return id;
    }

    public void setId( String id) {
        this.id = id;
        this.payeeId = id;
    }

    public  String getUserId() {
        return userId;
    }

    public void setUserId( String userId) {
        this.userId = userId;
        this.setCustomerId(userId);
    }

    public  String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber( String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public  String getAccountType() {
        return accountType;
    }

    public void setAccountType( String accountType) {
        this.accountType = accountType;
    }

    public  String getBankName() {
        return bankName;
    }

    public void setBankName( String bankName) {
        this.bankName = bankName;
    }

    public  String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName( String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public  String getCountryName() {
        return countryName;
    }

    public void setCountryName( String countryName) {
        this.countryName = countryName;
    }

    public  String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn( String createdOn) {
        this.createdOn = createdOn;
    }

    public  String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName) {
        this.firstName = firstName;
    }

    public  String getIsInternationalAccount() {
        return isInternationalAccount;
    }

    public void setIsInternationalAccount( String isInternationalAccount) {
        this.isInternationalAccount = isInternationalAccount;
    }

    public  String getIsSameBankAccount() {
        return isSameBankAccount;
    }

    public void setIsSameBankAccount( String isSameBankAccount) {
        this.isSameBankAccount = isSameBankAccount;
    }

    public  String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified( String isVerified) {
        this.isVerified = isVerified;
    }

    public  String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName) {
        this.lastName = lastName;
    }

    public  String getNickName() {
        return nickName;
    }

    public void setNickName( String nickName) {
        this.nickName = nickName;
    }

    public  String getNotes() {
        return notes;
    }

    public void setNotes( String notes) {
        this.notes = notes;
    }

    public  String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber( String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public  String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode( String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public  String getIsBusinessPayee() {
        return isBusinessPayee;
    }

    public void setIsBusinessPayee( String isBusinessPayee) {
        this.isBusinessPayee = isBusinessPayee;
    }

    public  String getCif() {
        return cif.toString();
    }

    public void setCif( String cif) {
        this.cif = cif;
    }

    public  String getSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete( String softDelete) {
        this.softDelete = softDelete;
    }

    public  String getDbpErrCode() {
        return dbpErrCode;
    }

    public void setDbpErrCode( String dbpErrCode) {
        this.dbpErrCode = dbpErrCode;
    }

    public  String getDbpErrMsg() {
        return dbpErrMsg;
    }

    public void setDbpErrMsg( String dbpErrMsg) {
        this.dbpErrMsg = dbpErrMsg;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
        this.id = payeeId;
    }

    public InterBankPayeeBackendDTO(){
        /* default constructor*/
        super();
    }

    public String getDeletedRecords() {
        return deletedRecords;
    }

    public void setDeletedRecords(String deletedRecords) {
        this.deletedRecords = deletedRecords;
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
	
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public InterBankPayeeBackendDTO(String id, String userId, String accountNumber, String accountType, String bankName, String beneficiaryName, String countryName, String createdOn, String firstName, String isInternationalAccount, String isSameBankAccount, String isVerified, String lastName, String nickName, String notes, String routingNumber, String swiftCode, String isBusinessPayee, String cif, String softDelete, String externalAccount, String iban, String sortCode, String phoneCountryCode, String phoneNumber, String phoneExtension, String addressNickName, String addressLine1, String city, String zipcode, String country, String customerId, String companyId, String externalAccountCol, String dbpErrCode, String dbpErrMsg, String deletedRecords, String payeeId, String noOfCustomersLinked, String addressLine2, String email, String phone, String clearingCode, String clearingIdentifierCode, String payeeVerification, String bankId) {
        this.id = id;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.bankName = bankName;
        this.beneficiaryName = beneficiaryName;
        this.countryName = countryName;
        this.createdOn = createdOn;
        this.firstName = firstName;
        this.isInternationalAccount = isInternationalAccount;
        this.isSameBankAccount = isSameBankAccount;
        this.isVerified = isVerified;
        this.lastName = lastName;
        this.nickName = nickName;
        this.notes = notes;
        this.routingNumber = routingNumber;
        this.swiftCode = swiftCode;
        this.isBusinessPayee = isBusinessPayee;
        this.cif = cif;
        this.softDelete = softDelete;
        this.externalAccount = externalAccount;
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
        this.companyId = companyId;
        this.customerId = customerId;
        this.externalAccountCol = externalAccountCol;
        this.dbpErrCode = dbpErrCode;
        this.dbpErrMsg = dbpErrMsg;
        this.deletedRecords = deletedRecords;
        this.payeeId = payeeId;
        this.noOfCustomersLinked = noOfCustomersLinked;
        this.addressLine2 = addressLine2;
        this.email = email;
        this.phone = phone;
        this.clearingCode = clearingCode;
        this.clearingIdentifierCode = clearingIdentifierCode;
        this.payeeVerification = payeeVerification;
        this.bankId=bankId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        InterBankPayeeBackendDTO other = (InterBankPayeeBackendDTO) o;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
        result = prime * result + ((cif == null) ? 0 : cif.hashCode());
        result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
        result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isBusinessPayee == null) ? 0 : isBusinessPayee.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
        result = prime * result + ((softDelete == null) ? 0 : softDelete.hashCode());
        result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((noOfCustomersLinked == null) ? 0 : noOfCustomersLinked.hashCode());
        result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((legalEntityId == null) ? 0 : legalEntityId.hashCode());
        return result;
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
    	if(StringUtils.isNotBlank(this.swiftCode) && !"N/A".equalsIgnoreCase(this.swiftCode) && !ValidationUtils.isValidSwiftCode(this.swiftCode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.routingNumber) && !"N/A".equalsIgnoreCase(this.routingNumber) && !ValidationUtils.isValidRoutingNumber(this.routingNumber) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.clearingCode) && !ValidationUtils.isValidText(this.clearingCode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.clearingIdentifierCode) && !ValidationUtils.isValidText(this.clearingIdentifierCode) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.payeeVerification) && !ValidationUtils.isValidText(this.payeeVerification) ) {
    		return false;
    	}
    	return true;
    }

    public void setLegalEntityId( String legalEntityId ) {
        this.legalEntityId = legalEntityId;
    }

    public String getLegalEntityId( ) {
        return legalEntityId;
    }
}

