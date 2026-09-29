package com.kony.externalalerts.util;

public enum ExternalAlertsEnum {

	ERROR_INVALID("91000", "Invalid input parameters."), ERROR_EMPTYINPUT("91001", "No records found to push to SF"),
	ERROR_EXCEPTION("91002", "Exception ocurred"), ERROR_CUSTOMERID("91003", "CustomerId is null"),
	ERROR_SFID("91004", "Salesforce id is null");

	String errcode;
	String errmsg;

	private ExternalAlertsEnum(String errcode, String errmsg) {
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
