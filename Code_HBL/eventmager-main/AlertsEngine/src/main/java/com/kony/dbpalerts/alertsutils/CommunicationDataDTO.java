package com.kony.dbpalerts.alertsutils;

public class CommunicationDataDTO {

	private String firstName;
	private String middleName;
	private String lastName;
	private String customertypeid;
	private String phone = null;
	private String email = null;

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

	public String getCustomertypeid() {
		return customertypeid;
	}

	public void setCustomertypeid(String customertypeid) {
		this.customertypeid = customertypeid;
	}

}
