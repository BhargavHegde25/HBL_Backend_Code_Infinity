package com.kony.dbp.alertrouterservice.email;

public class Recipient {
	
	String type;
	String emailId;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	@Override
	public String toString() {
		return "Recipient [id=" + emailId + ", type=" + type + "]";
	}
	
	

}
