package com.temenos.dbx.transaction.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BankDTO implements DBPDTO {

	private static final long serialVersionUID = 1712098749461284189L;
	private String address1;
	private String address2;
	private String phone;
	
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String addressLine1) {
		this.address1 = addressLine1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String addressLine2) {
		this.address2 = addressLine2;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
