package com.kony.adminconsole.service.alertmanagement.staging.util;

public class Frequency {
	private String id;
	private String value;
	private String time;	
	
	public Frequency(String id, String value, String time) {
		super();
		this.id = id;
		this.value = value;
		this.time = time;		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
