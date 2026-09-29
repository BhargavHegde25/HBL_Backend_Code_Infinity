package com.kony.adminconsole.service.alertmanagement.staging.util;

import java.util.List;

public class AlertSubscription {
	private String customerId;
	private String accountType;
	private String accountID;
	private String preferenceLevel;
	private String catId;
	private List<Channel> chanls;
	private Frequency freq;
	private List<AlertGroup> groups;	
	private String backendId;
	
	public String getPreferenceLevel() {
		return preferenceLevel;
	}
	public void setPreferenceLevel(String preferenceLevel) {
		this.preferenceLevel = preferenceLevel;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public List<Channel> getChanls() {
		return chanls;
	}
	public void setChanls(List<Channel> chanls) {
		this.chanls = chanls;
	}
	public Frequency getFreq() {
		return freq;
	}
	public void setFreq(Frequency freq) {
		this.freq = freq;
	}
	public List<AlertGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<AlertGroup> groups) {
		this.groups = groups;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public String getBackendId() {
		return backendId;
	}
	public void setBackendId(String backendId) {
		this.backendId = backendId;
	}	
	
}
