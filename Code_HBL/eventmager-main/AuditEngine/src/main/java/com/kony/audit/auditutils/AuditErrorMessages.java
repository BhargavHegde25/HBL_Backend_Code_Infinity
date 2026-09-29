package com.kony.audit.auditutils;

public class AuditErrorMessages {
	private AuditErrorMessages() {

	}

	public static final String C_EVENT_MISSING = "events parameter is missing";
	public static final String C_TOKEN_MISSING = "token parameter is missing";
	public static final String C_AUTH_FAIL = "Authentication is failed in calling service.";
	public static final String C_JSON_PARSE_ERROR = "An exception occurred while parsing the JSON in parameter events";
	public static final String C_NOT_JSON_ERROR = "Parameter events does not contain a JSON array";
	public static final String C_PROCESSING_ERROR = "Exception occured in processing events data";
	public static final String C_EMPTY_EVENT = "Invalid or empty Events";
	public static final String C_INVALID_INPUT = "Invalid input, input missing mandatory feilds.";

}
