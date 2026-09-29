package com.dbp.externalevent.utils;

public enum ExternalEventsEnum {
	ERROR_INVALID("16000", "Invalid input parameters."), ERROR_INSECURE("16001", "Input contains insecure data."),
	ERROR_NOT_CONFIGURE("16002", "Topic is not configured for this EventCode, please update configuration.");
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
