package com.temenos.dbx.product.payeeservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalPayeeListDTO implements DBPDTO {

    private static final long serialVersionUID = 886403890034328614L;
    private String id;

    private String Id;
    private String typeId;
    private String payeeId;
    private String createdBy;
    private String contractId;
    private String cif;
    @JsonIgnore
    private String isBusinessPayee;
    private String noOfCustomersLinked;
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
    @JsonAlias({"addressLine02"})
    private String addressLine2;
    private String email;
    private String phone;
    @JsonAlias({"payeeRequestStatus"})
    private String payeeStatus = "Active";
    private String clearingCode;
	private String clearingIdentifierCode;
	private String streetName;
	private String bankCountryName;
	private String townName;
	private String intermediaryBIC;
	private String payeeVerification;
	
	public ExternalPayeeListDTO(){

    }


    public ExternalPayeeListDTO(String id, String typeId, String payeeId, String createdBy, String contractId, String cif, String isBusinessPayee, String noOfCustomersLinked, String accountNumber, String accountType, String bankName, String beneficiaryName, String countryName, String createdOn, String firstName, String isInternationalAccount, String isSameBankAccount, String isVerified, String lastName, String nickName, String notes, String routingNumber, String swiftCode, String softDelete, String externalAccount, String iban, String sortCode, String phoneCountryCode, String phoneNumber, String phoneExtension, String addressNickName, String addressLine1, String city, String zipcode, String country, String companyId, String addressLine2, String email, String phone, String payeeStatus,String clearingCode, String clearingIdentifierCode, String streetName, String bankCountryName, String townName, String intermediaryBIC, String payeeVerification) {
        this.id = id;
        this.typeId = typeId;
        this.payeeId = payeeId;
        this.createdBy = createdBy;
        this.contractId = contractId;
        this.cif = cif;
        this.isBusinessPayee = isBusinessPayee;
        this.noOfCustomersLinked = noOfCustomersLinked;
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
        this.addressLine2 = addressLine2;
        this.email = email;
        this.phone = phone;
        this.payeeStatus = payeeStatus;
        this.clearingCode = clearingCode;
        this.clearingIdentifierCode = clearingIdentifierCode;
        this.streetName = streetName;
    	this.bankCountryName = bankCountryName;
    	this.townName = townName;
    	this.intermediaryBIC = intermediaryBIC;
    	this.payeeVerification = payeeVerification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public String getNoOfCustomersLinked() {
        return noOfCustomersLinked;
    }

    public void setNoOfCustomersLinked(String noOfCustomersLinked) {
        this.noOfCustomersLinked = noOfCustomersLinked;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIsInternationalAccount() {
        return isInternationalAccount;
    }

    public void setIsInternationalAccount(String isInternationalAccount) {
        this.isInternationalAccount = isInternationalAccount;
    }

    public String getIsSameBankAccount() {
        return isSameBankAccount;
    }

    public void setIsSameBankAccount(String isSameBankAccount) {
        this.isSameBankAccount = isSameBankAccount;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(String softDelete) {
        this.softDelete = softDelete;
    }

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
    }

    public String getPayeeStatus() {
        return payeeStatus;
    }

    public void setPayeeStatus( String payeeStatus ) {
        this.payeeStatus = payeeStatus;
    }
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
	
	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	
	public String getBankCountryName() {
		return bankCountryName;
	}

	public void setBankCountryName(String bankCountryName) {
		this.bankCountryName = bankCountryName;
	}
	
	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}
	
    public String getIntermediaryBIC() {
		return intermediaryBIC;
	}

	public void setIntermediaryBIC(String intermediaryBIC) {
		this.intermediaryBIC = intermediaryBIC;
	}
	
	public String getPayeeVerification() {
        return payeeVerification;
    }

    public void setPayeeVerification(String payeeVerification) {
        this.payeeVerification = payeeVerification;
    }
}
