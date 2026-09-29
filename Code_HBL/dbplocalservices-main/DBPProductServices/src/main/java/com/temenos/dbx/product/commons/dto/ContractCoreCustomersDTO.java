package com.temenos.dbx.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractCoreCustomersDTO implements DBPDTO{

	private static final long serialVersionUID = -4563604943763455753L;
	
	private String id;
	private String contractId;
	private String taxId;
	private String coreCustomerId;
	private String coreCustomerName;
	private boolean isPrimary;
	private boolean isBusiness;
	private String addressLine1;
    private String addressLine2;
    private String cityName;
    private String country;
    private String zipCode;
    private String state;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getContractId() {
		return contractId;
	}
	
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	public String getTaxId() {
		return taxId;
	}
	
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	
	public String getCoreCustomerId() {
		return coreCustomerId;
	}
	
	public void setCoreCustomerId(String coreCustomerId) {
		this.coreCustomerId = coreCustomerId;
	}
	
	public String getCoreCustomerName() {
		return coreCustomerName;
	}
	
	public void setCoreCustomerName(String coreCustomerName) {
		this.coreCustomerName = coreCustomerName;
	}
	
	public boolean isPrimary() {
		return isPrimary;
	}
	
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	
	public boolean isBusiness() {
		return isBusiness;
	}
	
	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}
	
	public String getaddressLine1() {
        return addressLine1;
    }

    public void setaddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getaddressLine2() {
        return addressLine2;
    }

    public void setaddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getcityName() {
        return cityName;
    }

    public void setcityName(String cityName) {
        this.cityName = cityName;
    }

    public String getcountry() {
        return country;
    }

    public void setcountry(String country) {
        this.country = country;
    }

    public String getzipCode() {
        return zipCode;
    }

    public void setzipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }
	
}
