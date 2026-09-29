package com.dbp.batchprocessengine.dto;

public class BackendIdentifierDTO  {

	private static final long serialVersionUID = 1L;

	private String customerId;
	private String backendId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBackendId() {
		return backendId;
	}

	public void setBackendId(String backendId) {
		this.backendId = backendId;
	}

	
}
