package com.kony.dbpalerts.alertsutils;

import java.util.HashSet;
import java.util.Set;

public class UserAlertDTO {

	public UserAlertDTO() {
		this.setChannels(new HashSet<>());
	}

	private String alertTypeId;
	private String customerid;
	private String accountId;
	private String accounttype;
	private String value1;
	private String value2;
	private Set<String> channels;

	public Set<String> getChannels() {
		return channels;
	}

	public void setChannels(Set<String> channels) {
		this.channels = channels;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getAlertTypeId() {
		return alertTypeId;
	}

	public void setAlertTypeId(String alertTypeId) {
		this.alertTypeId = alertTypeId;
	}

}
