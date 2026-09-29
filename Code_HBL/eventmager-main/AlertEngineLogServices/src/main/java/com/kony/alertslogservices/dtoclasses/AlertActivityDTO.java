package com.kony.alertslogservices.dtoclasses;

import com.kony.alertslogservices.core.BaseActivity;

public class AlertActivityDTO extends BaseActivity {

	private static final long serialVersionUID = 1009876783167367247L;

	private String id;
	private String corecustomerid;
	private String eventid;
	private String alertsubtypeid;
	private String alerttypeid;
	private String alertcategoryid;
	private String alertstatusid;
	private String customerId;
	private String languagecode;
	private String channelid;
	private String status;
	private String subject;
	private String sendername;
	private String senderemail;
	private String referencenumber;
	private java.util.Date dispatchdate;
	private String createdby;
	private String modifiedby;
	private java.util.Date createdts;
	private java.util.Date lastmodifiedts;
	private java.util.Date synctimestamp;
	private String message;
	private String errormessage;
	private boolean softdeleteflag = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getcoreCustomerId() {
		return corecustomerid;
	}

	public void setcoreCustomerId(String corecustomerid) {
		this.corecustomerid = corecustomerid;
	}

	public boolean getsoftdeleteflag() {
		return softdeleteflag;
	}

	public void setsoftdeleteflag(boolean softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errorMessage) {
		this.errormessage = errorMessage;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventId) {
		this.eventid = eventId;
	}

	public String getAlertsubtypeid() {
		return alertsubtypeid;
	}

	public void setAlertsubtypeid(String alertSubTypeId) {
		this.alertsubtypeid = alertSubTypeId;
	}

	public String getAlerttypeid() {
		return alerttypeid;
	}

	public void setAlerttypeid(String alertTypeId) {
		this.alerttypeid = alertTypeId;
	}

	public String getAlertcategoryid() {
		return alertcategoryid;
	}

	public void setAlertcategoryid(String alertCategoryId) {
		this.alertcategoryid = alertCategoryId;
	}

	public String getAlertstatusid() {
		return alertstatusid;
	}

	public void setAlertstatusid(String alertStatusId) {
		this.alertstatusid = alertStatusId;
	}

	public String getCustomer_id() {

		return customerId;
	}

	public void setCustomer_id(String customerId) {
		this.customerId = customerId;

	}
	public String getLanguagecode() {
		return languagecode;
	}

	public void setLanguagecode(String languageCode) {
		this.languagecode = languageCode;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelId) {
		this.channelid = channelId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSendername() {
		return sendername;
	}

	public void setSendername(String senderName) {
		this.sendername = senderName;
	}

	public String getSenderemail() {
		return senderemail;
	}

	public void setSenderemail(String senderEmail) {
		this.senderemail = senderEmail;
	}

	public String getReferencenumber() {
		return referencenumber;
	}

	public void setReferencenumber(String referenceNumber) {
		this.referencenumber = referenceNumber;
	}

	public java.util.Date getDispatchdate() {
		return dispatchdate;
	}

	public void setDispatchdate(java.util.Date dispatchDate) {
		this.dispatchdate = dispatchDate;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public java.util.Date getCreatedts() {
		return createdts;
	}

	public void setCreatedts(java.util.Date createdts) {
		this.createdts = createdts;
	}

	public java.util.Date getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(java.util.Date lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public java.util.Date getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(java.util.Date synctimestamp) {
		this.synctimestamp = synctimestamp;
	}
	
	@Override
	public String toString() {
		return "AlertActivityDTO [id=" + id + ", corecustomerid=" + corecustomerid + ", eventid=" + eventid
				+ ", alertsubtypeid=" + alertsubtypeid + ", alerttypeid=" + alerttypeid + ", alertcategoryid="
				+ alertcategoryid + ", alertstatusid=" + alertstatusid + ", customerId=" + customerId
				+ ", languagecode=" + languagecode + ", channelid=" + channelid + ", status=" + status + ", subject="
				+ subject + ", sendername=" + sendername + ", senderemail=" + senderemail + ", referencenumber="
				+ referencenumber + ", dispatchdate=" + dispatchdate + ", createdby=" + createdby + ", modifiedby="
				+ modifiedby + ", createdts=" + createdts + ", lastmodifiedts=" + lastmodifiedts + ", synctimestamp="
				+ synctimestamp + ", message=" + message + ", errormessage=" + errormessage + ", softdeleteflag="
				+ softdeleteflag + "]";
	}
}
