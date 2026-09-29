package com.kony.dbp.alertrouterservice.userdata;

public class UserData {
	
	
	String corecustomerid;
	String customerId;
	String accountnumber;
	String companyLegalUnit;
	
	public String getCorecustomerid() {
		return corecustomerid;
	}
	public void setCorecustomerid(String corecustomerid) {
		this.corecustomerid = corecustomerid;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAccountnumber() {
		return accountnumber;
	}
	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}
	public String getCompanyId() {
		return companyLegalUnit;
	}
	public void setCompanyId(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	@Override
	public String toString() {
		return "UserData [corecustomerid=" + corecustomerid + ", customerId=" + customerId + ", accountnumber="
				+ accountnumber + ",companyLegalUnit=" + companyLegalUnit +"]";
	}



}
