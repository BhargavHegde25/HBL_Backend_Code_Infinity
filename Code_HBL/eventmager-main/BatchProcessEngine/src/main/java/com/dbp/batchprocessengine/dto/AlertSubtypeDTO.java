package com.dbp.batchprocessengine.dto;

public class AlertSubtypeDTO {

	private String alertsubtypeid;
	private String alerttypeid;
	private String attributeid;
	private String alertconditionid;
	private String value1;
	private String value2;
	private boolean isglobal;

	public String getAlertsubtypeid() {
		return alertsubtypeid;
	}

	public void setAlertsubtypeid(String alertsubtypeid) {
		this.alertsubtypeid = alertsubtypeid;
	}

	public String getAlerttypeid() {
		return alerttypeid;
	}

	public void setAlerttypeid(String alerttypeid) {
		this.alerttypeid = alerttypeid;
	}

	public String getAttributeid() {
		return attributeid;
	}

	public void setAttributeid(String attributeid) {
		this.attributeid = attributeid;
	}

	public String getAlertconditionid() {
		return alertconditionid;
	}

	public void setAlertconditionid(String alertconditionid) {
		this.alertconditionid = alertconditionid;
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

	public boolean isIsglobal() {
		return isglobal;
	}

	public void setIsglobal(String isglobal) {
		if (isglobal.equals("1") || isglobal.equals("true"))
			this.isglobal = true;
	}

}
