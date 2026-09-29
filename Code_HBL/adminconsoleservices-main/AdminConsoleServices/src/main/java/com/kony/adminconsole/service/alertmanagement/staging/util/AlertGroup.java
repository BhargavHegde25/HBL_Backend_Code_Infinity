package com.kony.adminconsole.service.alertmanagement.staging.util;

import java.util.List;

public class AlertGroup {
	private String typeID;
	private List<Channel> chanls;
	private Frequency freq;
	private List<Alert> alerts;
	private boolean isSub;	
	
	public AlertGroup(String typeID, boolean isSub) {
		super();
		this.typeID = typeID;
		this.isSub = isSub;
	}
	public String getTypeID() {
		return typeID;
	}
	public void setTypeID(String typeID) {
		this.typeID = typeID;
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
	public List<Alert> getAlerts() {
		return alerts;
	}
	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}
	public boolean isSub() {
		return isSub;
	}
	public void setSub(boolean isSub) {
		this.isSub = isSub;
	}
	
	
	
	
}
