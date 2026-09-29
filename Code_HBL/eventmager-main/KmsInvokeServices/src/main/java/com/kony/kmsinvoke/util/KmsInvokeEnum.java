package com.kony.kmsinvoke.util;

public enum KmsInvokeEnum {

	ERROR_INVALID("19000", "Invalid input parameters."),
	ERROR_APIKEY("19001", "KMS API Key is not configured in environment variables."),
	ERROR_EMAILAPIURL("19002", "Unable to fetch KMS email API URL"),
	ERROR_EMAILEXCEPTION("19003", "Exception occured in submitting mail"),
	ERROR_SMSEXCEPTION("19004", "Exception occured in submitting sms"),
	ERROR_SMSAPIURL("19005", "Unable to fetch KMS SMS API URL"),
	ERROR_PUSHEXCEPTION("19006", "Exception occured in submitting push notification"), ERROR_PUSHAPIURL("19007", "Unable to fetch KMS Push Notification API URL");
	String errcode;
	String errmsg;

	private KmsInvokeEnum(String errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public String getErrCode() {
		return this.errcode;
	}

	public String getErrMsg() {
		return this.errmsg;
	}

}
