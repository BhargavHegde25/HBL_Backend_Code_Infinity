package com.kony.dbx.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Customer implements Serializable {
	
	/**
	 * Unique ID for Serialization
	 */
	private static final long serialVersionUID = 5402532880850712158L;

	/*
	 * Customer address line 1
	 */
	private String address1;
	/*
	 * Is the account eligible for check deposits
	 */
	private String supportDeposit;

	/*
	 * Customer address line 1
	 */
	private String address2;

	/*
	 * Customer city
	 */
	private String city;

	/*
	 * Customer data of birth
	 */
	private String dateOfBirth;
	/*
	 * Customer Locator
	 */
	private String locator;

	/*
	 * Default source account customer set for Bill Pay. The value of this is unique
	 * account id
	 */
	private String defaultAccountBillPay;

	/*
	 * Default source account customer set for Cardless cash staging. The value of
	 * this is unique account id
	 */
	private String defaultAccountCardless;

	/*
	 * Default account where Remote Deposit Capture are deposited. The value of this
	 * is unique account id
	 */
	private String defaultAccountDeposit;

	/*
	 * Default source account customer set for P2P. The value of this is unique
	 * account id
	 */
	private String defaultAccountP2pFrom;

	/*
	 * Default destination account customer set for P2P. The value of this is unique
	 * account id
	 */
	private String defaultAccountP2pTo;

	/*
	 * Default source account customer set for Transfers. The value of this is
	 * unique account id
	 */
	private String defaultAccountTransfers;

	/*
	 * Customer email address
	 */
	private String email;
	private String secondaryEmail;

	/*
	 * Customer first name
	 */
	private String firstName;

	/*
	 * Customer last name
	 */
	private String lastName;

	/*
	 * Customer last login date and time
	 */
	private String lastLoginTime;

	/*
	 * Customer middle name
	 */
	private String middleName;

	/*
	 * Customer phone number
	 */
	private String phone;

	/*
	 * Customer role
	 */
	private String role;

	/*
	 * Customer street address
	 */
	private String street;

	/*
	 * Customer state
	 */
	private String state;

	/*
	 * Customer username which is used to login
	 */
	private String userName;

	/*
	 * Customer unique ID
	 */
	private String userId;

	/*
	 * Customer zipcode
	 */
	private String zipcode;

	/*
	 * /* Customer zipcode
	 */
	private String isForeignAddress;

	/*
	 * Flexible key value pair for example unique id from vaious backend systems
	 * like one from Bill Pay, another from P2P etc...
	 */
	private HashMap<String, String> userAttributes;

	private String organizationId;

	private String ssn;

	/*
	 * Constructor
	 */
	public Customer() {

		this.address1 = "";
		this.address2 = "";
		this.city = "";
		this.dateOfBirth = "";
		this.defaultAccountBillPay = "";
		this.defaultAccountCardless = "";
		this.defaultAccountDeposit = "";
		this.defaultAccountP2pFrom = "";
		this.defaultAccountP2pTo = "";
		this.defaultAccountTransfers = "";
		this.email = "";
		this.firstName = "";
		this.lastName = "";
		this.lastLoginTime = "";
		this.middleName = "";
		this.phone = "";
		this.role = "";
		this.street = "";
		this.state = "";
		this.userName = "";
		this.userId = "";
		this.zipcode = "";
		this.supportDeposit = "";
		this.organizationId = "";
		this.ssn = "";
		this.secondaryEmail="";
		this.userAttributes = new HashMap<String, String>();
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return this.address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return this.address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return this.dateOfBirth;
	}

	/**
	 * @return the defaultAccountBillPay
	 */
	public String getDefaultAccountBillPay() {
		return this.defaultAccountBillPay;
	}

	/**
	 * @return the defaultAccountCardless
	 */
	public String getDefaultAccountCardless() {
		return this.defaultAccountCardless;
	}

	/**
	 * @return the defaultAccountDeposit
	 */
	public String getDefaultAccountDeposit() {
		return this.defaultAccountDeposit;
	}

	/**
	 * @return the defaultAccountP2pFrom
	 */
	public String getDefaultAccountP2pFrom() {
		return this.defaultAccountP2pFrom;
	}

	/**
	 * @return the defaultAccountP2pTo
	 */
	public String getDefaultAccountP2pTo() {
		return this.defaultAccountP2pTo;
	}

	/**
	 * @return the defaultAccountTransfers
	 */
	public String getDefaultAccountTransfers() {
		return this.defaultAccountTransfers;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @return the lastLoginTime
	 */
	public String getLastLoginTime() {
		return this.lastLoginTime;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return this.phone;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return this.role;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * Get a specific user attribute
	 * 
	 * @param String
	 *            The name of the attribute to retrieve
	 * @return String The user attribute
	 */
	public String getUserAttribute(String attributeName) {
		return this.userAttributes.get(attributeName);
	}

	/**
	 * Get a list of the user attribute names
	 * 
	 * @return Set<String> The list of user attribute names
	 */
	public Set<String> getUserAttributeNames() {
		return this.userAttributes.keySet();
	}

	/**
	 * @return the userAttributes
	 */
	public HashMap<String, String> getUserAttributes() {
		return this.userAttributes;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return this.zipcode;
	}

	/**
	 * @param address1
	 *            the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @param address2
	 *            the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @param defaultAccountBillPay
	 *            the defaultAccountBillPay to set
	 */
	public void setDefaultAccountBillPay(String defaultAccountBillPay) {
		this.defaultAccountBillPay = defaultAccountBillPay;
	}

	/**
	 * @param defaultAccountCardless
	 *            the defaultAccountCardless to set
	 */
	public void setDefaultAccountCardless(String defaultAccountCardless) {
		this.defaultAccountCardless = defaultAccountCardless;
	}

	/**
	 * @param defaultAccountDeposit
	 *            the defaultAccountDeposit to set
	 */
	public void setDefaultAccountDeposit(String defaultAccountDeposit) {
		this.defaultAccountDeposit = defaultAccountDeposit;
	}

	/**
	 * @param defaultAccountP2pFrom
	 *            the defaultAccountP2pFrom to set
	 */
	public void setDefaultAccountP2pFrom(String defaultAccountP2pFrom) {
		this.defaultAccountP2pFrom = defaultAccountP2pFrom;
	}

	/**
	 * @param defaultAccountP2pTo
	 *            the defaultAccountP2pTo to set
	 */
	public void setDefaultAccountP2pTo(String defaultAccountP2pTo) {
		this.defaultAccountP2pTo = defaultAccountP2pTo;
	}

	/**
	 * @param defaultAccountTransfers
	 *            the defaultAccountTransfers to set
	 */
	public void setDefaultAccountTransfers(String defaultAccountTransfers) {
		this.defaultAccountTransfers = defaultAccountTransfers;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param lastLoginTime
	 *            the lastLoginTime to set
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Add an attribute
	 * 
	 * @param String
	 *            The attribute key
	 * @param String
	 *            The attribute value
	 */
	public void setUserAttribute(String key, String value) {
		this.userAttributes.put(key, value);
	}

	/**
	 * @param userAttributes
	 *            the userAttributes to set
	 */
	public void setUserAttributes(HashMap<String, String> userAttributes) {
		this.userAttributes = userAttributes;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param zipcode
	 *            the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public String getSupportDeposit() {
		return supportDeposit;
	}

	public void setSupportDeposit(String supportDeposit) {
		this.supportDeposit = supportDeposit;
	}

	public String getIsForeignAddress() {
		return isForeignAddress;
	}

	public void setIsForeignAddress(String isForeignAddress) {
		this.isForeignAddress = isForeignAddress;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getorganizationId() {
		return organizationId;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getSsn() {
		return ssn;
	}
	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	public String getSecondaryEmail() {
		return secondaryEmail;
	}
}
