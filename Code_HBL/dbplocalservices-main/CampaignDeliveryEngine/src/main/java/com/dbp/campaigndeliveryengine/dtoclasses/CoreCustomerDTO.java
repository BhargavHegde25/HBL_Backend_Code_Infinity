package com.dbp.campaigndeliveryengine.dtoclasses;

public class CoreCustomerDTO {
	private String corecustid;
	private String custid;
	private String companyLegalUnit = null;
	
	public String getCorecustid() {
		return corecustid;
	}
	
	public void setCompanyLegalUnit(String companyLegalUnit) 
	{    
		this.companyLegalUnit = companyLegalUnit;
	}
	public String getCompanyLegalUnit() 
	{    
		return this.companyLegalUnit;
	}
	
	public void setCorecustid(String corecustid) {
		this.corecustid = corecustid;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

}
