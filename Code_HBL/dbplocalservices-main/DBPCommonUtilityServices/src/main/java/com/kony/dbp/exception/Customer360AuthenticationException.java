package com.kony.dbp.exception;

import com.kony.dbputilities.util.ErrorCodeEnum;

/**
 * 
 * Exception class used for DBP Authentication Exceptions
 * 
 * @author Aditya Mankal
 * 
 */

public class Customer360AuthenticationException extends ApplicationException {

    private static final long serialVersionUID = 8378581456282862036L;

    public Customer360AuthenticationException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum);
    }

    public Customer360AuthenticationException(ErrorCodeEnum errorCodeEnum, Throwable cause) {
        super(errorCodeEnum, cause);
    }

}
