package com.kony.dbpalerts.alertsutils;

import java.util.Set;

public class Event {
	private String eventjson = null;
	private String eventid = null;
	private String alertcategory = null;
	private String alerttype = null;
	private String alertsubtype = null;
	private boolean isaccountLevel = false;
	private boolean isglobal = false;
	private boolean chsms = false;
	private boolean chemail = false;
	private boolean chpush = false;
	private boolean chnotification = false;
	private boolean userregisterwithkms = false;
	private boolean isaccounttypelevel = false;
	private Set<String> phone = null;
	private Set<String> email = null;
	private String customerid = null;
	private String customertype = null;
	private String recipientType = null;
	private String recipientErrMsg = null;
	private String firstname = null;
	private String lastname = null;
	private String middlename = null;
	private String username = null;
	private String attributeid = null;
	private String alertconditionid = null;
	private String value1 = null;
	private String value2 = null;
	private String accountid = null;
	private String countrycode = null;
	private String commstatusid = null;
	private String alerttypestatus = null;
	private String alertcategorystatus = null;
	private String alertsubtypestatus = null;
	private String languagecode = null;
	private String appid = null;
	// Logging related parameters
	private String smsrefid = null;
	private String smsmessage = null;
	private String mailrefid = null;
	private String mailmessage = null;
	private String pushrefid = null;
	private String pushmessage = null;
	private String notificationrefid = null;
	private String notificationmessage = null;
	private String accounttypeid = null;
	private String corecustomerid = null;
	private NotificationQueryClass notificationobj2 = null;
	private boolean isExternalSystem = false;
	private String otherErrorMessage = null;
	private String alertCategoryName = null;
	private String alertGroupName = null;
	private String alertName = null;
	private String legalEntityId = null;

	public void setIsaccounttypelevel(Boolean acctypelevel) {
		this.isaccounttypelevel = acctypelevel;
	}

	public boolean getIsaccounttypelevel() {
		return this.isaccounttypelevel;
	}

	public void setEventJson(String eventjson) {
		this.eventjson = eventjson;
	}

	public String getEventJson() {
		return this.eventjson;
	}

	public void setAlertcategory(String alertcategory) {
		this.alertcategory = alertcategory;
	}

	public String getAlertcategory() {
		return this.alertcategory;
	}

	public void setPhone(Set<String> phone) {
		this.phone = phone;
	}

	public Set<String> getPhone() {
		return this.phone;
	}

	public void setEmail(Set<String> email) {
		this.email = email;
	}

	public Set<String> getEmail() {
		return this.email;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setAccountTypeId(String accounttype) {
		this.accounttypeid = accounttype;
	}

	public String getAccountTypeId() {
		return this.accounttypeid;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}

	public String getCustomertype() {
		return this.customertype;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getMiddlename() {
		return this.middlename;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setAttributeid(String attributeid) {
		this.attributeid = attributeid;
	}

	public String getAttributeid() {
		return this.attributeid;
	}

	public void setAlertconditionid(String alertconditionid) {
		this.alertconditionid = alertconditionid;
	}

	public String getAlertconditionid() {
		return this.alertconditionid;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue1() {
		return this.value1;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue2() {
		return this.value2;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public String getAccountid() {
		return this.accountid;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getCountrycode() {
		return this.countrycode;
	}

	public void setAlerttypestatus(String alerttypestatus) {
		this.alerttypestatus = alerttypestatus;
	}

	public String getAlerttypestatus() {
		return this.alerttypestatus;
	}

	public void setAlertcategorystatus(String alertcategorystatus) {
		this.alertcategorystatus = alertcategorystatus;
	}

	public String getAlertcategorystatus() {
		return this.alertcategorystatus;
	}

	public void setAlertsubtypestatus(String alertsubtypestatus) {
		this.alertsubtypestatus = alertsubtypestatus;
	}

	public String getAlertsubtypestatus() {
		return this.alertsubtypestatus;
	}

	public void setLanguagecode(String languagecode) {
		this.languagecode = languagecode;
	}

	public String getLanguagecode() {
		return this.languagecode;
	}

	public void setSmsrefid(String smsrefid) {
		this.smsrefid = smsrefid;
	}

	public String getSmsrefid() {
		return this.smsrefid;
	}

	public void setSmsmessage(String smsmessage) {
		this.smsmessage = smsmessage;
	}

	public String getSmsmessage() {
		return this.smsmessage;
	}

	public void setMailrefid(String mailrefid) {
		this.mailrefid = mailrefid;
	}

	public String getMailrefid() {
		return this.mailrefid;
	}

	public void setMailmessage(String mailmessage) {
		this.mailmessage = mailmessage;
	}

	public String getMailmessage() {
		return this.mailmessage;
	}

	public void setPushrefid(String pushrefid) {
		this.pushrefid = pushrefid;
	}

	public String getPushrefid() {
		return this.pushrefid;
	}

	public void setPushmessage(String pushmessage) {
		this.pushmessage = pushmessage;
	}

	public String getPushmessage() {
		return this.pushmessage;
	}

	public void setNotificationrefid(String notificationrefid) {
		this.notificationrefid = notificationrefid;
	}

	public String getNotificationrefid() {
		return this.notificationrefid;
	}

	public void setNotificationmessage(String notificationmessage) {
		this.notificationmessage = notificationmessage;
	}

	public String getNotificationmessage() {
		return this.notificationmessage;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppid() {
		return this.appid;
	}

	public void setIsaccountLevel(boolean isaccountLevel) {
		this.isaccountLevel = isaccountLevel;
	}

	public boolean getIsaccountLevel() {
		return this.isaccountLevel;
	}

	public void setIsglobal(boolean isglobal) {
		this.isglobal = isglobal;
	}

	public boolean getIsglobal() {
		return this.isglobal;
	}

	public void setChsms(boolean chsms) {
		this.chsms = chsms;
	}

	public boolean getChsms() {
		return this.chsms;
	}

	public void setChemail(boolean chemail) {
		this.chemail = chemail;
	}

	public boolean getChemail() {
		return this.chemail;
	}

	public void setChpush(boolean chpush) {
		this.chpush = chpush;
	}

	public boolean getChpush() {
		return this.chpush;
	}

	public void setChnotification(boolean chnotification) {
		this.chnotification = chnotification;
	}

	public boolean getChnotification() {
		return this.chnotification;
	}

	public void setUserregisterwithkms(boolean userregisterwithkms) {
		this.userregisterwithkms = userregisterwithkms;
	}

	public boolean getUserregisterwithkms() {
		return this.userregisterwithkms;
	}

	public String getCorecustomerid() {
		return corecustomerid;
	}

	public void setCorecustomerid(String corecustomerid) {
		this.corecustomerid = corecustomerid;
	}

	public NotificationQueryClass getNotificationobj2() {
		return notificationobj2;
	}

	public void setNotificationobj2(NotificationQueryClass notificationobj2) {
		this.notificationobj2 = notificationobj2;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
	}

	public String getAlerttype() {
		return alerttype;
	}

	public void setAlerttype(String alerttype) {
		this.alerttype = alerttype;
	}

	public String getAlertsubtype() {
		return alertsubtype;
	}

	public void setAlertsubtype(String alertsubtype) {
		this.alertsubtype = alertsubtype;
	}

	public String getCommstatusid() {
		return commstatusid;
	}

	public void setCommstatusid(String commstatusid) {
		this.commstatusid = commstatusid;
	}

	public String getRecipientType() {
		return recipientType;
	}

	public void setRecipientType(String recipientType) {
		this.recipientType = recipientType;
	}

	public String getRecipientErrMsg() {
		return recipientErrMsg;
	}

	public void setRecipientErrMsg(String recipientErrMsg) {
		this.recipientErrMsg = recipientErrMsg;
	}

	public void setIsExternalSystem(boolean isExternalSystem) {
		this.isExternalSystem = isExternalSystem;
	}

	public boolean getIsExternalSystem() {
		return isExternalSystem;
	}

	public void setOtherErrMsg(String otherErrorMessage) {
		this.otherErrorMessage = otherErrorMessage;
	}

	public String getOtherErrorMessage() {
		return otherErrorMessage;
	}

	public void setAlertCategoryName(String alertCategoryName) {
		this.alertCategoryName = alertCategoryName;
	}

	public String getAlertCategoryName() {
		return alertCategoryName;
	}

	public void setAlertGroupName(String alertGroupName) {
		this.alertGroupName = alertGroupName;
	}

	public String getAlertGroupName() {
		return alertGroupName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public String getAlertName() {
		return alertName;
	}
	
	public void setCompanyLegalUnit(String legalEntityId ) {
		this.legalEntityId = legalEntityId;
	}
	public String getCompanyLegalUnit() {
		return legalEntityId;
	}

}
