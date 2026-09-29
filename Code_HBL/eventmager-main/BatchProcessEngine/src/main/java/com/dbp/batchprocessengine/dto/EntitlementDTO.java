package com.dbp.batchprocessengine.dto;

public class EntitlementDTO {

	private String customerid;
	private String alerttypeid;
	private String alertsubtypeid;
	private String accountid;
	private String accounttype;
	private String value1;
	private String value2;
	private String companyLegalUnit;

	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getAlerttypeid() {
		return alerttypeid;
	}

	public void setAlerttypeid(String alerttypeid) {
		this.alerttypeid = alerttypeid;
	}

	public String getAlertsubtypeid() {
		return alertsubtypeid;
	}

	public void setAlertsubtypeid(String alertsubtypeid) {
		this.alertsubtypeid = alertsubtypeid;
	}

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
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

}
