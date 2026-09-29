package com.kony.dbp.alertrouterservice.sms;

public class SmsServiceRequest {
	
	private Messages messages ;

	public Messages getMessages() {
		return messages;
	}

	public void setMessages(Messages messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "SmsServiceRequest [messages=" + messages + "]";
	}
	
	

}
