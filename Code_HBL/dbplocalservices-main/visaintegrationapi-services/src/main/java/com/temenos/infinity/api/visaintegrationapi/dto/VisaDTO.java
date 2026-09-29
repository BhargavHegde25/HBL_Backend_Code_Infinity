package com.temenos.infinity.api.visaintegrationapi.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class VisaDTO implements DBPDTO {

	private static final long serialVersionUID = 6597925078705187784L;

	public VisaDTO() {
		super();
	}

	private String firstName;
	private String lastName;

	private String phoneNumber;
	private String phoneCountryCode;
	private String extension;

	private String email;

	private String locale;
	private String status;

	private String name;
	private String addrLine1;
	private String addrLine2;
	private String addrLine3;
	private String city;
	private String state;
	private String countryCode;
	private String postalCode;

	private String encMobileNumber;
	private String encAddress;
	private String encEmailAddress;

	private String vParentCustomerID;
	private String clientCustomerId;
	private String vCustomerID;
	private String createdDateTime;
	private String updatedDateTime;
	private String CardID;
	private String last4;
	private String paymentAccountReference;
	private String expiryMonth;
	private String expiryYear;
	private String deviceID;
	private String deviceCert;
	private String nonceSignature;
	private String nonce;
	private String accountNumber;
	private String nameOnCard;
	private String cvv2;
	private String billingAddress;
	private String encCard;
	private String vCustomerIDForPartner;
	private String vClientIDForPartner;

	private String paymentService;
	private String opaquePaymentCard;
	private String activationData;
	private String encryptedPassData;
	private String ephemeralPublicKey;
	private String cardType;
	private String operation;

	private String code;
	private String message;
	private String errorMessage;

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public String getAddrLine3() {
		return addrLine3;
	}

	public void setAddrLine3(String addrLine3) {
		this.addrLine3 = addrLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEncMobileNumber() {
		return encMobileNumber;
	}

	public void setEncMobileNumber(String encMobileNumber) {
		this.encMobileNumber = encMobileNumber;
	}

	public String getEncAddress() {
		return encAddress;
	}

	public void setEncAddress(String encAddress) {
		this.encAddress = encAddress;
	}

	public String getEncEmailAddress() {
		return encEmailAddress;
	}

	public void setEncEmailAddress(String encEmailAddress) {
		this.encEmailAddress = encEmailAddress;
	}

	public String getvParentCustomerID() {
		return vParentCustomerID;
	}

	public void setvParentCustomerID(String vParentCustomerID) {
		this.vParentCustomerID = vParentCustomerID;
	}

	public String getClientCustomerId() {
		return clientCustomerId;
	}

	public void setClientCustomerId(String clientCustomerId) {
		this.clientCustomerId = clientCustomerId;
	}

	public String getvCustomerID() {
		return vCustomerID;
	}

	public void setvCustomerID(String vCustomerID) {
		this.vCustomerID = vCustomerID;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getPaymentAccountReference() {
		return paymentAccountReference;
	}

	public void setPaymentAccountReference(String paymentAccountReference) {
		this.paymentAccountReference = paymentAccountReference;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceCert() {
		return deviceCert;
	}

	public void setDeviceCert(String deviceCert) {
		this.deviceCert = deviceCert;
	}

	public String getNonceSignature() {
		return nonceSignature;
	}

	public void setNonceSignature(String nonceSignature) {
		this.nonceSignature = nonceSignature;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getEncCard() {
		return encCard;
	}

	public void setEncCard(String encCard) {
		this.encCard = encCard;
	}

	public String getvCustomerIDForPartner() {
		return vCustomerIDForPartner;
	}

	public void setvCustomerIDForPartner(String vCustomerIDForPartner) {
		this.vCustomerIDForPartner = vCustomerIDForPartner;
	}

	public String getvClientIDForPartner() {
		return vClientIDForPartner;
	}

	public void setvClientIDForPartner(String vClientIDForPartner) {
		this.vClientIDForPartner = vClientIDForPartner;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(String paymentService) {
		this.paymentService = paymentService;
	}

	public String getOpaquePaymentCard() {
		return opaquePaymentCard;
	}

	public void setOpaquePaymentCard(String opaquePaymentCard) {
		this.opaquePaymentCard = opaquePaymentCard;
	}

	public String getActivationData() {
		return activationData;
	}

	public void setActivationData(String activationData) {
		this.activationData = activationData;
	}

	public String getEncryptedPassData() {
		return encryptedPassData;
	}

	public void setEncryptedPassData(String encryptedPassData) {
		this.encryptedPassData = encryptedPassData;
	}

	public String getEphemeralPublicKey() {
		return ephemeralPublicKey;
	}

	public void setEphemeralPublicKey(String ephemeralPublicKey) {
		this.ephemeralPublicKey = ephemeralPublicKey;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCardID() {
		return CardID;
	}

	public void setCardID(String cardID) {
		CardID = cardID;
	}

}
