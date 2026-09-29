package com.bct.javaservices;

public class CardlessCashValidationDTO {
	private String validationRequest;
	private String validationResult;
	private String status;
	private String response_message;
	private String switch_request_payload;
	private String response_code;
	private String isAuthValidationEnabled;
	private String requestHeaders;
	private String authResult;

	public String getValidationRequest() {
		return validationRequest;
	}

	public void setValidationRequest(String validationRequest) {
		this.validationRequest = validationRequest;
	}

	public String getValidationResult() {
		return validationResult;
	}

	public void setValidationResult(String validationResult) {
		this.validationResult = validationResult;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponse_message() {
		return response_message;
	}

	public void setResponse_message(String response_message) {
		this.response_message = response_message;
	}

	public String getSwitch_request_payload() {
		return switch_request_payload;
	}

	public void setSwitch_request_payload(String switch_request_payload) {
		this.switch_request_payload = switch_request_payload;
	}

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}

	public String getIsAuthValidationEnabled() {
		return isAuthValidationEnabled;
	}

	public void setIsAuthValidationEnabled(String isAuthValidationEnabled) {
		this.isAuthValidationEnabled = isAuthValidationEnabled;
	}

	public String getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(String string) {
		this.requestHeaders = string;
	}

	public String getAuthResult() {
		return authResult;
	}

	public void setAuthResult(String authResult) {
		this.authResult = authResult;
	}

}
