package com.kony.utils;

public enum ErrorCodeEnum {
	
	ERROR_EXCEPTION("19000", "Exception occurred while dispatching event. Refer logs for more details"),
	ERROR_INVALIDFILTER("19001", "Invalid Service data for fetching event configuration from db"),
	ERROR_EXCEPTIONDBSERVICE("19002", "Exception Occured while invoking Db service"),
	ERROR_EVENTLISTEMPTY("19003", "Event configuration is empty for service data");
	String errcode;
	String errmsg;

	private ErrorCodeEnum(String errcode, String errmsg) {
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
