package com.kony.adminconsole.utilities;

/**
 * Enum that holds error codes related to admin console services and processors
 * 
 * @author Venkateswara Rao Alla, Aditya Mankal
 *
 */

public enum ErrorCodeEnum {

    ERR_90000(20000, "Unauthorized access"), 
    ERR_90001(20001, "Internal Error")
;
	
    
	private int errorCode;
    private String message;
    public static final String ERROR_CODE_KEY = "dbpErrCode";
    public static final String ERROR_MESSAGE_KEY = "dbpErrMsg";

    private ErrorCodeEnum(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCodeAsString() {
        return String.valueOf(errorCode);
    }

  
}
