package com.dbp.batchprocessengine.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class SubscriberDTO {

	private static final long serialVersionUID = 5035451677588972439L;

	private String customerid;
	private String corecustomerid;
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getCorecustomerid() {
		return corecustomerid;
	}
	public void setCorecustomerid(String corecustomerid) {
		this.corecustomerid = corecustomerid;
	}



	

}
