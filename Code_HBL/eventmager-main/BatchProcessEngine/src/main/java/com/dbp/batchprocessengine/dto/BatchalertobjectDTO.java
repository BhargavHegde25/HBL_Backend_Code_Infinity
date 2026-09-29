package com.dbp.batchprocessengine.dto;

public class BatchalertobjectDTO {
	private String objectid = null;
	private String operation = null;
	private String lastsync = null;
	public String getObjectid() {
		return objectid;
	}
	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getLastsync() {
		return lastsync;
	}
	public void setLastsync(String lastsync) {
		this.lastsync = lastsync;
	}

}
