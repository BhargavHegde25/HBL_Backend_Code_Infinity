package com.kony.auditlogservices.dtoclasses;

import java.util.Date;

import com.kony.auditlogservices.core.BaseActivity;

public class AuditActivityDTO extends BaseActivity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String eventid;
	private String eventtype;
	private String eventsubtype;
	private String statusid;
	private String appid;
	private String username;
	private String customerid;
	private String adminusername;
	private String adminuserrole;
	private String producer;
	private String moneymovementrefid;
	private String createdby;

	private String eventdata;
	private boolean softdeleteflag = false;
	private String partyid;
	private String corecustomerid;
	private java.util.Date createdts;
	private String sessionid;
	private String nonsearchable;
	private Date eventts;
	private String ipaddress;
	private boolean iscsrassist;
	private String platform;
	private String appversion;
	private String channel;
	private String deviceid;
	private String browser;
	private String operatingsystem;
	private String devicemodel;

	private String phonenumber;
	private String email;

	private String mfastate;
	private String mfaserviceKey;
	private String mfatype;
	private String creditcardnumber;
	private String appsessionid;
	private String payeenickname;
	private String relationshipnumber;

	public AuditActivityDTO() {
		this.createdts = new java.util.Date();

	}

	public void setappSessionId(String appSessionId) {
		this.appsessionid = appSessionId;
	}

	public String getappSessionId() {
		return appsessionid;
	}

	public void setpayeeNickName(String payeeNickName) {
		this.payeenickname = payeeNickName;
	}

	public String getpayeeNickName() {
		return payeenickname;
	}

	public void setrelationshipNumber(String relationshipNumber) {
		this.relationshipnumber = relationshipNumber;
	}

	public String getrelationshipNumber() {
		return relationshipnumber;
	}

	public boolean getisCSRAssist() {
		return iscsrassist;
	}

	public void setisCSRAssist(String isCSRAssist) {
		if (isCSRAssist.equals("true") || isCSRAssist.equals("1"))
			this.iscsrassist = true;
		else
			this.iscsrassist = false;
	}

	public Date geteventts() {
		return eventts;
	}

	public void seteventts(Date eventts) {
		this.eventts = eventts;
	}

	public void setmfa_State(String mfastate) {
		this.mfastate = mfastate;
	}

	public String getmfa_State() {
		return mfastate;
	}

	public void setmfa_Type(String mfatype) {
		this.mfatype = mfatype;
	}

	public String getmfa_Type() {
		return mfatype;
	}

	public void setmfa_ServiceKey(String mfaserviceKey) {
		this.mfaserviceKey = mfaserviceKey;
	}

	public String getmfa_ServiceKey() {
		return mfaserviceKey;
	}

	public void setphoneNumber(String phoneNumber) {
		this.phonenumber = phoneNumber;
	}

	public String getphoneNumber() {
		return phonenumber;
	}

	public void setcreditcardnumber(String creditcardnumber) {
		this.creditcardnumber = creditcardnumber;
	}

	public String getcreditcardnumber() {
		return creditcardnumber;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public String getemail() {
		return email;
	}

	public Date getcreatedts() {
		return createdts;
	}

	public String getsessionId() {
		return sessionid;
	}

	public void setnonSearchable(String nonSearchable) {
		this.nonsearchable = nonSearchable;
	}

	public String getnonSearchable() {
		return nonsearchable;
	}

	public void setsessionId(String sessionId) {
		this.sessionid = sessionId;
	}

	public void setcreatedts(Date createdts) {
		this.createdts = createdts;
	}

	public boolean getsoftdeleteflag() {
		return softdeleteflag;
	}

	public void setsoftdeleteflag(boolean softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public String getEventdata() {
		return eventdata;
	}

	public void setEventdata(String eventdatajson) {
		this.eventdata = eventdatajson;
	}

	public String getEventtype() {
		return eventtype;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventtype(String eventType) {
		this.eventtype = eventType;
	}

	public void setEventid(String eventId) {
		this.eventid = eventId;
	}

	public String getEventsubtype() {
		return eventsubtype;
	}

	public void setEventsubtype(String eventSubType) {
		this.eventsubtype = eventSubType;
	}

	public String getStatus_id() {
		return statusid;
	}

	public void setStatus_id(String statusId) {
		this.statusid = statusId;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appId) {
		this.appid = appId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getCustomer_id() {
		return customerid;
	}

	public void setCustomer_id(String customerId) {
		this.customerid = customerId;
	}

	public String getAdminusername() {
		return adminusername;
	}

	public void setAdminusername(String adminUserName) {
		this.adminusername = adminUserName;
	}

	public String getAdminuserrole() {
		return adminuserrole;
	}

	public void setAdminuserrole(String adminUserRole) {
		this.adminuserrole = adminUserRole;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getMoneymovementrefid() {
		return moneymovementrefid;
	}

	public void setMoneymovementrefid(String moneyMovementRefId) {
		this.moneymovementrefid = moneyMovementRefId;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getdeviceModel() {
		return devicemodel;
	}

	public void setdeviceModel(String deviceModel) {
		this.devicemodel = deviceModel;
	}

	public void setoperatingSystem(String operatingSystem) {
		this.operatingsystem = operatingSystem;
	}

	public String getoperatingSystem() {
		return operatingsystem;
	}

	public void setbrowser(String browser) {
		this.browser = browser;
	}

	public String getbrowser() {
		return browser;
	}

	public void setdeviceId(String deviceId) {
		this.deviceid = deviceId;
	}

	public String getdeviceId() {
		return deviceid;
	}

	public void setchannel(String channel) {
		this.channel = channel;
	}

	public String getchannel() {
		return channel;
	}

	public void setappVersion(String appVersion) {
		this.appversion = appVersion;
	}

	public String getappVersion() {
		return appversion;
	}

	public void setplatform(String platform) {
		this.platform = platform;
	}

	public String getplatform() {
		return platform;
	}

	public void setipAddress(String ipAddress) {
		this.ipaddress = ipAddress;
	}

	public String getipAddress() {
		return ipaddress;
	}

	public static void main(String[] args) {

	}

	public String getPartyid() {
		return partyid;
	}

	public void setPartyid(String partyid) {
		this.partyid = partyid;
	}

	public String getCorecustomerid() {
		return corecustomerid;
	}

	public void setCorecustomerid(String corecustomerid) {
		this.corecustomerid = corecustomerid;
	}

}
