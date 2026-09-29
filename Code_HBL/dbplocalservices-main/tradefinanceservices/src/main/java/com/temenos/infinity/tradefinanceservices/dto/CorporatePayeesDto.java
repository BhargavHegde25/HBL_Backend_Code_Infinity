/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class CorporatePayeesDto implements DBPDTO {
	
	private static final long serialVersionUID = 8399691982667293054L;
	
	private String name;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String country;
	private String zipcode;
	private String id;
	private String payeeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		setPayeeId(id);
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public CorporatePayeesDto() {
	}

	public CorporatePayeesDto(String name, String address1, String address2, String city, String state, String country,
							  String zipcode) {
		super();
		this.name = name;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address1, address2, city, country, name, state, zipcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorporatePayeesDto other = (CorporatePayeesDto) obj;
		return Objects.equals(address1, other.address1) && Objects.equals(address2, other.address2)
				&& Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(name, other.name) && Objects.equals(state, other.state)
				&& Objects.equals(zipcode, other.zipcode);
	}
	
	

}
