package com.kony.dbp.queuemaster.utils;

public enum ExternalEventsEnum {
	ERROR_INVALID("16003", "Invalid input parameters."), ERROR_INSECURE("16004", "Input contains insecure data."),
	ERROR_NO_SHARESECRET("16005", "shared secret has not been configured.");
	String errcode;
	String errmsg;

	private ExternalEventsEnum(String errcode, String errmsg) {
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
