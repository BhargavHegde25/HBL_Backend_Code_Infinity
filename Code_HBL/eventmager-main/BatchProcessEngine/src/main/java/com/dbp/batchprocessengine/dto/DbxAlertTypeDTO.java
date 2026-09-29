package com.dbp.batchprocessengine.dto;


public class DbxAlertTypeDTO  {

	private static final long serialVersionUID = 1L;
	private String id;
	private String alertConditionId;
	private String value1;
	private String value2;
	private String isGlobal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlertConditionId() {
		return alertConditionId;
	}

	public void setAlertConditionId(String alertConditionId) {
		this.alertConditionId = alertConditionId;
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

	public String getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(String isGlobal) {
		this.isGlobal = isGlobal;
	}

}
