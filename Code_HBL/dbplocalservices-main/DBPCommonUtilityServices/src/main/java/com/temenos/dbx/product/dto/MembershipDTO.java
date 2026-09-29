package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class MembershipDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -7769457189218596410L;
    private String id;
    private boolean isCustomerCentric;
    private String name;
    private String taxId;
    private String phone;
    private String email;
    private boolean isBusinessType;
    private String addressId;
    private String createdby;
    private String modifiedby;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String ssn;
    private String status;
    private String addressLine1;
    private String addressLine2;
    private String cityName;
    private String country;
    private String zipCode;
    private String state;
    private String industry;
    private String isBusiness;
    private String sectorId;
    private String IDType_id;
    private String IDValue;
    private String IDIssueDate;
    private String IDExpiryDate;
    private String companyLegalUnit;

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    public String getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(String isBusiness) {
        this.isBusiness = isBusiness;
    }
    
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    AddressDTO address;
    MembershipRelationDTO membershipRelation;

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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public AddressDTO getAddressDTO() {
        return address;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.address = addressDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCustomerCentric() {
        return isCustomerCentric;
    }

    public void setCustomerCentric(boolean isCustomerCentric) {
        this.isCustomerCentric = isCustomerCentric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
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

    public boolean isBusinessType() {
        return isBusinessType;
    }

    public void setBusinessType(boolean isBusinessType) {
        this.isBusinessType = isBusinessType;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MembershipRelationDTO getMembershipRelation() {
        return membershipRelation;
    }

    public void setMembershipRelation(MembershipRelationDTO membershipRelation) {
        this.membershipRelation = membershipRelation;
    }

	public String getIDType_id() {
		return IDType_id;
	}

	public void setIDType_id(String iDType_id) {
		IDType_id = iDType_id;
	}

	public String getIDValue() {
		return IDValue;
	}

	public void setIDValue(String iDValue) {
		IDValue = iDValue;
	}

	public String getIDIssueDate() {
		return IDIssueDate;
	}

	public void setIDIssueDate(String iDIssueDate) {
		IDIssueDate = iDIssueDate;
	}

	public String getIDExpiryDate() {
		return IDExpiryDate;
	}

	public void setIDExpiryDate(String iDExpiryDate) {
		IDExpiryDate = iDExpiryDate;
	}
    

}
