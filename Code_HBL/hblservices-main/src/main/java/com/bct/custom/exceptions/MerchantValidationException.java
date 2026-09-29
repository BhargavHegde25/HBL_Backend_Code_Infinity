package com.bct.custom.exceptions;

import com.kony.dbputilities.util.ErrorCodeEnum;

public class MerchantValidationException extends Exception{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 7200615980517751315L;

	
	
	private ValidationErrorCodeEnum errorCodeEnum;
	 private String customMessage;
	 private int errorCode;
	 
	public MerchantValidationException() {
		super();
	}
	
	 public MerchantValidationException(int errorCode, String customMessage) {
		super();
		this.customMessage = customMessage;
		this.errorCode = errorCode;
	}

	public MerchantValidationException(ValidationErrorCodeEnum errorCodeEnum) {
	        this.errorCodeEnum = errorCodeEnum;
	    }

	public MerchantValidationException(ValidationErrorCodeEnum errorCodeEnum, String customMessage) {
		super();
		this.errorCodeEnum = errorCodeEnum;
		this.customMessage = customMessage;
	}
	
	/*public int getErrorCode() {
		return errorCode;
	}
	*/
	public String getErrorCode() {
		return String. valueOf(errorCode);
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public ValidationErrorCodeEnum getErrorCodeEnum() {
		return errorCodeEnum;
	}
	public void setErrorCodeEnum(ValidationErrorCodeEnum errorCodeEnum) {
		this.errorCodeEnum = errorCodeEnum;
	}
	public String getCustomMessage() {
		return customMessage;
	}
	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}
	 
	 
	 

}
