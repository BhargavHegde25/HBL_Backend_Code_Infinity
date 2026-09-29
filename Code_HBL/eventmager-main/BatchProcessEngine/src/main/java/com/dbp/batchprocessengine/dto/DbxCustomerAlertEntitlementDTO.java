package com.dbp.batchprocessengine.dto;



public class DbxCustomerAlertEntitlementDTO  {

	private static final long serialVersionUID = 1L;
	private String customerId;
	private String alertTypeId;
	private String accountId;
	private String value1;
	private String value2;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAlertTypeId() {
		return alertTypeId;
	}

	public void setAlertTypeId(String alertTypeId) {
		this.alertTypeId = alertTypeId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
