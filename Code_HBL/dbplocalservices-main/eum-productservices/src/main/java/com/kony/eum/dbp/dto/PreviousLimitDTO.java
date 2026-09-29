package com.kony.eum.dbp.dto;

public class PreviousLimitDTO {

	private String id;
	private Double value;
	private boolean isDerived = true;
	private boolean isAllowed = false;
	private boolean isProcessed = false;
	
	public boolean isProcessed() {
		return isProcessed;
	}
	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDerived() {
		return isDerived;
	}
	public void setDerived(boolean isDerived) {
		this.isDerived = isDerived;
	}
	public boolean isAllowed() {
		return isAllowed;
	}
	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}
	
	
}
