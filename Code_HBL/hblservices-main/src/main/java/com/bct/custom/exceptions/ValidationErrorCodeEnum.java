package com.bct.custom.exceptions;

import com.kony.dbputilities.util.ErrorConstants;

public enum ValidationErrorCodeEnum {
	ERR_2000(2000,"Invalid Input"),
	ERR_2001(2001,"null Input"),
	ERR_2002(2002,"Record Exist");
	
	private int errorCode;
    private String message ;
    
	private ValidationErrorCodeEnum() {
	}

	private ValidationErrorCodeEnum(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
    

}
