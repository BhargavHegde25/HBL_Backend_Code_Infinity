package com.kony.dbp.alertrouterservice.sms;

public class Messages {
	
	private Message message ;

	@Override
	public String toString() {
		return "Messages [message=" + message + "]";
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	

}
