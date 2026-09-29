package com.dbp.batchprocessengine.utils;

public enum BatchProcessingEnum {
	ERROR_INVALID("16050", "Invalid service type."), ERROR_OTHER("16052", "");
	String errcode;
	String errmsg;

	private BatchProcessingEnum(String errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public String getErrCode() {
		return this.errcode;
	}

	public String setErrMsg(String msg) {
		return this.errmsg = msg;
	}

	public String getErrMsg() {
		return this.errmsg;
	}

}
