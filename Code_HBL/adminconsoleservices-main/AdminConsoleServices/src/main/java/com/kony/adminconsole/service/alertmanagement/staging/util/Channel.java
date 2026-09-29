package com.kony.adminconsole.service.alertmanagement.staging.util;

public class Channel {
	
	private String id;
	private boolean isSub;	
	
	public Channel(String id, boolean isSub) {
		super();
		this.id = id;
		this.isSub = isSub;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSub() {
		return isSub;
	}
	public void setSub(boolean isSub) {
		this.isSub = isSub;
	}
	
	

}
