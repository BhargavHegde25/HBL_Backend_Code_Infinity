package com.kony.audit.auditutils;

import com.google.gson.JsonElement;

public class Event {

	private JsonElement eventjson;
	private String eventtype;
	private String eventsubtype;
	private String status;
	private String eventId;
	private String customerId;
	private String userName;
	private String accountId;
	private String appId;
	private String corecustomerid;

	public Event(Event e) {
		this.eventjson = e.eventjson;
		this.accountId = e.accountId;
		this.corecustomerid = e.corecustomerid;

	}

	public Event(String eventId, String eventtype, String eventsubtype, String status) {
		this.eventId = eventId;
		this.eventtype = eventtype;
		this.eventsubtype = eventsubtype;
		this.status = status;

	}

	public Event(JsonElement eventjoson) {
		this.eventjson = eventjoson;
	}

	public void setJsonElement(JsonElement event) {
		this.eventjson = event;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setaccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setappId(String appId) {
		this.appId = appId;
	}

	public JsonElement getJsonElement() {
		return eventjson;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getUserName() {
		return userName;
	}

	public String getaccountId() {
		return accountId;
	}

	public String getappId() {
		return appId;
	}

	public String geteventId() {
		return eventId;
	}

	public String geteventtype() {
		return eventtype;
	}

	public String geteventsubtype() {
		return eventsubtype;
	}

	public String getstatus() {
		return status;
	}

	public String getCorecustomerid() {
		return corecustomerid;
	}

	public void setCorecustomerid(String corecustomerid) {
		this.corecustomerid = corecustomerid;
	}

}
