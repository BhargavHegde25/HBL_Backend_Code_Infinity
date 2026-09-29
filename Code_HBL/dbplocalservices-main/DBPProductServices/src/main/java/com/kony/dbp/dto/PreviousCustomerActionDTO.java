package com.kony.dbp.dto;

import java.util.HashMap;
import java.util.Map;

public class PreviousCustomerActionDTO {
	
	private String id;
	private boolean isAllowed = false;
	private boolean isDerived = true;
	private String actionId;
	private String accountId;
	private Map<String, PreviousLimitDTO> limits;
	private boolean isProcessed = false;

	public boolean isProcessed() {
		return isProcessed;
	}
	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	public boolean isDerived() {
		return isDerived;
	}
	public void setDerived(boolean isDerived) {
		this.isDerived = isDerived;
	}
	public PreviousCustomerActionDTO(){
		limits = new HashMap<>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isAllowed() {
		return isAllowed;
	}
	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Map<String, PreviousLimitDTO> getLimits() {
		return limits;
	}
	public void setLimits(Map<String, PreviousLimitDTO> limits) {
		this.limits = limits;
	}
}
