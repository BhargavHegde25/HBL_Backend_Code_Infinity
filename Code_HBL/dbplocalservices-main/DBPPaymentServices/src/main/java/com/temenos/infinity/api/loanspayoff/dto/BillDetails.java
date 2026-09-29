package com.temenos.infinity.api.loanspayoff.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BillDetails implements Serializable {

	private static final long serialVersionUID = 6597925078705187770L;

	private double totalAmount;
	private List<Properties> properties;
	private String backendOverride;
	private String backendError;

	public BillDetails() {
		super();
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<Properties> getProperties() {
		return properties;
	}

	public void setProperties(List<Properties> properties) {
		this.properties = properties;
	}

	public String getBackendOverride() {
		return backendOverride;
	}

	public void setBackendOverride(String backendOverride) {
		this.backendOverride = backendOverride;
	}

	public String getBackendError() {
		return backendError;
	}

	public void setBackendError(String backendError) {
		this.backendError = backendError;
	}

}
