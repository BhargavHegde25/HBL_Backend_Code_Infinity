package com.hbl.productservicesExtn.exception;

public class CustomException extends Exception{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 6748498613577852456L;
	private String errorMessage;
	 private String errorCode;
	 
	public CustomException() {
		super();
	}
	


	public CustomException(String errorCode, String errorMessage) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}



	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	@Override
	public String toString() {
		return "CustomException [errorMessage=" + errorMessage + ", errorCode=" + errorCode + "]";
	}
	 


}
