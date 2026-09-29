package com.dbp.campaigndeliveryengine.dtoclasses;

import com.google.gson.JsonElement;

public class EventDTO {
	private String corecustomerid;
	private String customerid;
	private JsonElement eventjson;
	private boolean sms = false;
	private boolean push = false;
	private boolean email = false;
	private String smssubject = null;
	private String smstext = null;
	private String emailsubject = null;
	private String emailtext = null;
	private String pushsubject = null;
	private String pushtext = null;

	public EventDTO(JsonElement eventjson, String corecustomerid, String customerid) {
		this.corecustomerid = corecustomerid;
		this.customerid = customerid;
		this.eventjson = eventjson;
	}

	public JsonElement getEventjson() {
		return eventjson;
	}

	public void setEventjson(JsonElement eventjson) {
		this.eventjson = eventjson;
	}

	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}

	public boolean isPush() {
		return push;
	}

	public void setPush(boolean push) {
		this.push = push;
	}

	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	public String getSmssubject() {
		return smssubject;
	}

	public void setSmssubject(String smssubject) {
		this.smssubject = smssubject;
	}

	public String getSmstext() {
		return smstext;
	}

	public void setSmstext(String smstext) {
		this.smstext = smstext;
	}

	public String getEmailsubject() {
		return emailsubject;
	}

	public void setEmailsubject(String emailsubject) {
		this.emailsubject = emailsubject;
	}

	public String getEmailtext() {
		return emailtext;
	}

	public void setEmailtext(String emailtext) {
		this.emailtext = emailtext;
	}

	public String getPushsubject() {
		return pushsubject;
	}

	public void setPushsubject(String pushsubject) {
		this.pushsubject = pushsubject;
	}

	public String getPushtext() {
		return pushtext;
	}

	public void setPushtext(String pushtext) {
		this.pushtext = pushtext;
	}

	public String getCorecustomerid() {
		return corecustomerid;
	}

	public void setCorecustomerid(String corecustomerid) {
		this.corecustomerid = corecustomerid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

}
