package com.kony.dbp.alertrouterservice.messagedata;

public class Email {
	
	String contact;
	String subject;
	String message;
	@Override
	public String toString() {
		return "EmailA [contact=" + contact + ", subject=" + subject + ", message=" + message + "]";
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
