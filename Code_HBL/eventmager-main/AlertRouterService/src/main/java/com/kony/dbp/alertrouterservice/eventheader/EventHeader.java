package com.kony.dbp.alertrouterservice.eventheader;

public class EventHeader {
	
	String eventType;
	String eventSubtype;
	String eventStatus;
	String messageType;
	String alertRequestID;
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventSubtype() {
		return eventSubtype;
	}
	public void setEventSubtype(String eventSubtype) {
		this.eventSubtype = eventSubtype;
	}
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getAlertRequestID() {
		return alertRequestID;
	}
	public void setAlertRequestID(String alertRequestID) {
		this.alertRequestID = alertRequestID;
	}
	@Override
	public String toString() {
		return "EventHeader [eventType=" + eventType + ", eventSubtype=" + eventSubtype + ", eventStatus=" + eventStatus
				+ ", messageType=" + messageType + ", alertRequestID=" + alertRequestID + "]";
	}
	
	

}
