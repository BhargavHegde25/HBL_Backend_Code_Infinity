package com.kony.campaign.dto;

import com.dbp.core.api.DBPDTO;
import com.google.gson.JsonObject;

public class EventDTO implements DBPDTO {

	private static final long serialVersionUID = 1805122041950251217L;

	private String eventId;
	
	private String placeholderCode;
	
	private String scale;
	
	private String userID;
	
	private String coreCustId;
	
	private String channel;
	
	private transient JsonObject eventData;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}	

	public String getPlaceholderCode() {
		return placeholderCode;
	}

	public void setPlaceholderCode(String placeholderCode) {
		this.placeholderCode = placeholderCode;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public JsonObject getEventData() {
		return eventData;
	}

	public void setEventData(JsonObject eventData) {
		this.eventData = eventData;
	}

	public String getCoreCustId() {
		return coreCustId;
	}

	public void setCoreCustId(String coreCustId) {
		this.coreCustId = coreCustId;
	}

}
