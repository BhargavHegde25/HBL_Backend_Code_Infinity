package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class OrganisationEmployeesViewDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3180796842525374070L;

	private String orgemp_id;
	private String orgemp_orgid;
	private String orgemp_cusid;
	private String isAuthSignatory;
	private String isOwner;
	private String customer_id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String userName;
	private String drivingLicenseNumber;
	private String dateOfBirth;
	private String ssn;
	private String custcomm_id;
	private String custcomm_typeid;
	private String custcomm_custid;
	private String custcomm_value;
	private String statusId;
	private String createdts;
	private String lastLoginTime;
	private String groupId;
	public String getOrgemp_id() {
		return orgemp_id;
	}
	public void setOrgemp_id(String orgemp_id) {
		this.orgemp_id = orgemp_id;
	}
	public String getOrgemp_orgid() {
		return orgemp_orgid;
	}
	public void setOrgemp_orgid(String orgemp_orgid) {
		this.orgemp_orgid = orgemp_orgid;
	}
	public String getOrgemp_cusid() {
		return orgemp_cusid;
	}
	public void setOrgemp_cusid(String orgemp_cusid) {
		this.orgemp_cusid = orgemp_cusid;
	}
	public String getIsAuthSignatory() {
		return isAuthSignatory;
	}
	public void setIsAuthSignatory(String isAuthSignatory) {
		this.isAuthSignatory = isAuthSignatory;
	}
	public String getIsOwner() {
		return isOwner;
	}
	public void setIsOwner(String isOwner) {
		this.isOwner = isOwner;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDrivingLicenseNumber() {
		return drivingLicenseNumber;
	}
	public void setDrivingLicenseNumber(String drivingLicenseNumber) {
		this.drivingLicenseNumber = drivingLicenseNumber;
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
	public String getCustcomm_id() {
		return custcomm_id;
	}
	public void setCustcomm_id(String custcomm_id) {
		this.custcomm_id = custcomm_id;
	}
	public String getCustcomm_typeid() {
		return custcomm_typeid;
	}
	public void setCustcomm_typeid(String custcomm_typeid) {
		this.custcomm_typeid = custcomm_typeid;
	}
	public String getCustcomm_custid() {
		return custcomm_custid;
	}
	public void setCustcomm_custid(String custcomm_custid) {
		this.custcomm_custid = custcomm_custid;
	}
	public String getCustcomm_value() {
		return custcomm_value;
	}
	public void setCustcomm_value(String custcomm_value) {
		this.custcomm_value = custcomm_value;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getCreatedts() {
		return createdts;
	}
	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}