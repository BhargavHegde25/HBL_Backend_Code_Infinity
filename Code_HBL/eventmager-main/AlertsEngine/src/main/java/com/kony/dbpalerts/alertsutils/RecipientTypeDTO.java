package com.kony.dbpalerts.alertsutils;

import java.util.Map;

public class RecipientTypeDTO {
	
	private String eventID ;
	private String recipientType;
	private String serviceName ;
	private String operationName;
	private Map<String,Object> inputMap;	
	
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public String getRecipientType() {
		return recipientType;
	}
	public void setRecipientType(String recipientType) {
		this.recipientType = recipientType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public Map<String, Object> getInputMap() {
		return inputMap;
	}
	public void setInputMap(Map<String, Object> inputMap) {
		this.inputMap = inputMap;
	}

}
