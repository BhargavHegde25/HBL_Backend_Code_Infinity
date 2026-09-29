package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomerSearchDTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4506552200963136551L;

	
	private String emailId; 
	private String lastName;
	private String contactNumber;
	private String dateOfBirth;
	private String identifierNumber;
	private String identifierType;
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the contactNumber
	 */
	public String getContactNumber() {
		return contactNumber;
	}
	/**
	 * @param contactNumber the contactNumber to set
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the identifierNumber
	 */
	public String getIdentifierNumber() {
		return identifierNumber;
	}
	/**
	 * @param identifierNumber the identifierNumber to set
	 */
	public void setIdentifierNumber(String identifierNumber) {
		this.identifierNumber = identifierNumber;
	}
	/**
	 * @return the identifierType
	 */
	public String getIdentifierType() {
		return identifierType;
	}
	/**
	 * @param identifierType the identifierType to set
	 */
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}
	
}
