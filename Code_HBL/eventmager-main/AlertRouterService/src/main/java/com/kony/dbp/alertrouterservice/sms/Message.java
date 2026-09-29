package com.kony.dbp.alertrouterservice.sms;

import java.util.Date;

public class Message {
	
	
	private String priorityService;
	private Recipients recipients;
	private String content;
	
	public String getPriorityService() {
		return priorityService;
	}
	public void setPriorityService(String priorityService) {
		this.priorityService = priorityService;
	}
	public Recipients getRecipients() {
		return recipients;
	}
	public void setRecipients(Recipients recipients) {
		this.recipients = recipients;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Message [priorityService=" + priorityService + ", recipients=" + recipients + ", content=" + content
				+ "]";
	}
	
	
	

}
