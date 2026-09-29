package com.kony.dbp.exception;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Result;

/**
 * Exception class used to hold the error code and error message. This exception can be thrown from the calling classes
 * or methods used by java services, pre/post processors and can further used for catching it and sending the
 * appropriate error code and message to requested client.
 * 
 * @author Venkateswara Rao Alla, Aditya Mankal
 *
 */

public class ApplicationException extends Exception {

    private static final long serialVersionUID = 7065601577555260335L;

    private ErrorCodeEnum errorCodeEnum;
    private String customMessage;

    public ApplicationException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }

    public ApplicationException(ErrorCodeEnum errorCodeEnum, Throwable cause) {
        super(cause);
        this.errorCodeEnum = errorCodeEnum;
    }

    public ErrorCodeEnum getErrorCodeEnum() {
        return errorCodeEnum;
    }

    public ApplicationException(ErrorCodeEnum errorCodeEnum, String customMessage) {
        this.errorCodeEnum = errorCodeEnum;
        this.customMessage = customMessage;
    }

    
    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        String errMsg;
        
        String message = super.getMessage();
        if (StringUtils.isNotBlank(message)) {
            builder.append(message);
        }
        
        if(StringUtils.isNotBlank(customMessage) ) {
        	errMsg = customMessage;
        }
        else {
        	errMsg = errorCodeEnum.getMessage();
        }

        builder.append(" [ErrorCode=").append(errorCodeEnum.getErrorCode()).append(", ").append(" Message=")
                .append(String.valueOf(errMsg)).append("]");

        return builder.toString();
    }

    public void setError(Result result) {
        errorCodeEnum.setErrorCode(result, customMessage);
    }

}
