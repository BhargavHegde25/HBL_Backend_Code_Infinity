package com.kony.dbp.alertrouterservice.email;


public class Email {
	
	private Recipients recipients;
	private String senderName;
	private String subject;
	private String content;
	private String priority;
	public Recipients getRecipients() {
		return recipients;
	}
	public void setRecipients(Recipients recipients) {
		this.recipients = recipients;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "Email [recipients=" + recipients + ", senderName=" + senderName + ", subject=" + subject + ", content="
				+ content + ", priority=" + priority + "]";
	}
	
	
	
	

}
