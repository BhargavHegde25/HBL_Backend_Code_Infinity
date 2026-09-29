package com.dbp.campaigndeliveryengine.dtoclasses;

public class KMSConfigDTO {

	private String kmsapikey;
	private String emailurl;
	private String pushurl;
	private String smsurl;
	private String appkey;

	public String getEmailurl() {
		return emailurl;
	}

	public void setEmailurl(String emailurl) {
		this.emailurl = emailurl;
	}

	public String getPushurl() {
		return pushurl;
	}

	public void setPushurl(String pushurl) {
		this.pushurl = pushurl;
	}

	public String getSmsurl() {
		return smsurl;
	}

	public void setSmsurl(String smsurl) {
		this.smsurl = smsurl;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getKmsapikey() {
		return kmsapikey;
	}

	public void setKmsapikey(String kmsapikey) {
		this.kmsapikey = kmsapikey;
	}

}
