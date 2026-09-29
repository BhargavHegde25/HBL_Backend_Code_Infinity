package com.kony.dbp.alertrouterservice.utils;

public enum ErrorCodeEnum {
	ERROR_INVALID("16003", "Invalid input parameters."), ERROR_INSECURE("16004", "Input contains insecure data."),
	ERROR_NO_SHARESECRET("16005", "shared secret has not been configured."),
	ERROR_MISSING("16011", "Missing input parameters.");
	
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
