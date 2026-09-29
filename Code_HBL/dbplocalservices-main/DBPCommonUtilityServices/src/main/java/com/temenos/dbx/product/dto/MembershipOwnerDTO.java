package com.temenos.dbx.product.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;

public class MembershipOwnerDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -8244243556229744371L;
    private String id;
    private String membershipId;
    private String userName;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String ssn;
    private String taxId;
    private String phone;
    private String email;
    private String createdby;
    private String modifiedby;
    private String lastmodifiedts;
    private String createdts;
    MembershipDTO membership;
    private String salutation;
    private String memberType;
    private String memberTypeId;
    private String memberTypeName;
    private String maritalStatus;
    private String employmentStatus;
    private String drivingLicenseNumber;
    private String middleName;
    private String isRetailUserPresent;
    private List<SignatoryTypeDTO> SignatoryTypes;

    public MembershipDTO getMembershipDTO() {
        return membership;
    }

    public void setMembershipDTO(MembershipDTO membershipDTO) {
        this.membership = membershipDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

    public String getCreatedts() {
        return createdts;
    }

    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    public MembershipDTO getMembership() {
        return membership;
    }

    public void setMembership(MembershipDTO membership) {
        this.membership = membership;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<SignatoryTypeDTO> getSignatoryTypes() {
        return SignatoryTypes;
    }

    public void setSignatoryTypes(List<SignatoryTypeDTO> SignatoryTypes) {
        this.SignatoryTypes = SignatoryTypes;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getIsRetailUserPresent() {
        return isRetailUserPresent;
    }

    public void setIsRetailUserPresent(String isRetailUserPresent) {
        this.isRetailUserPresent = isRetailUserPresent;
    }

    public String getMemberTypeId() {
        return memberTypeId;
    }

    public void setMemberTypeId(String memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

    public String getMemberTypeName() {
        return memberTypeName;
    }

    public void setMemberTypeName(String memberTypeName) {
        this.memberTypeName = memberTypeName;
    }

}
