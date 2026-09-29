package com.kony.adminconsole.exception;

import com.kony.adminconsole.utilities.ErrorCodeEnum;

/**
 * 
 * Exception class used for Consumer Lending Authentication Exceptions
 * 
 * @author Aditya Mankal
 * 
 */

public class CLAuthenticationException extends ApplicationException {

    private static final long serialVersionUID = 8378581456282862036L;

    public CLAuthenticationException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum);
    }

    public CLAuthenticationException(ErrorCodeEnum errorCodeEnum, Throwable cause) {
        super(errorCodeEnum, cause);
    }

}
