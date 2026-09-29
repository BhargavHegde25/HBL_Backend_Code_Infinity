package com.bct.custom.exceptions;

public class InvalidSignatureException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2222539697108657127L;
	private String errorCode;
    private String message ;
    
	public InvalidSignatureException() {
		super();
	}

	public InvalidSignatureException(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public InvalidSignatureException(String message) {
		super();
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    

}
