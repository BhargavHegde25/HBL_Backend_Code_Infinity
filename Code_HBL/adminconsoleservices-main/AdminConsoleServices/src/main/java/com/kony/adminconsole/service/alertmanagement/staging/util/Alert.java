package com.kony.adminconsole.service.alertmanagement.staging.util;

import java.util.List;

public class Alert {
	private String id;
	private List<Channel> chanls;
	private Frequency freq;
	private String value1;
	private String value2;
	private boolean isSub;
	
	public Alert(String id, boolean isSub) {
		super();
		this.id = id;
		this.isSub = isSub;
	}
	public Alert(String id, String value1, String value2, boolean isSub) {
		super();
		this.id = id;
		this.value1 = value1;
		this.value2 = value2;
		this.isSub = isSub;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public boolean isSub() {
		return isSub;
	}
	public void setSub(boolean isSub) {
		this.isSub = isSub;
	}	
	
	
		
}
