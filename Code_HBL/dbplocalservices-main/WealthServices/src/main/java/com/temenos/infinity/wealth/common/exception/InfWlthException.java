package com.temenos.infinity.wealth.common.exception;

/**
 * InfWlthException is an exception for handling custom error/exception scenarios
 * 
 * @author Rajesh Kappera
 */
public class InfWlthException extends Exception {
	private static final long serialVersionUID = 3080324457725543829L;
	
	private String outputStatus;
	private String errorMessage;

	public String getOutputStatus() {
		return outputStatus;
	}
	
	public void setOutputStatus(String outputStatus) {
		this.outputStatus = outputStatus;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public InfWlthException() {
		super();
	}
	
	public InfWlthException(String message){
		super(message);
	}
	
	public InfWlthException(String outputStatus, String errorMessage){
		this.outputStatus = outputStatus;
		this.errorMessage = errorMessage;
	}
	
	public InfWlthException(Throwable cause){
		super(cause);
	}
	
	public InfWlthException(String message, Throwable cause){
		super(message, cause);
	}
}
