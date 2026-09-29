package com.kony.dbputilities.util;

import com.dbp.core.constants.DBPConstants;

public final class ErrorCodes {

    private ErrorCodes() {
    }

    public static final String RECORD_FOUND = "3400";
    public static final String RECORD_NOT_FOUND = "3400";
    public static final String ERROR_SEARCHING_RECORD = "3401";
    public static final String ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS = "3403";

    public static final String RECORD_CREATED = "3400";
    public static final String RECORD_NOT_CREATED = "3102";
    public static final String ERROR_CREATING_RECORD = "3101";
    public static final String ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS = "3103";
    public static final String ERROR_UPDATING_RECORD = "3104";
    public static final String ERROR_DELETING_RECORD = "3105";
    public static final String SECURITY_ERROR = "3199";

    public static final String LOGIN_SUCCESS = "2000";
    public static final String LOGIN_FAILED = "2002";
    public static final String ERROR_LOGGING_IN = "2001";
    public static final String ERROR_LOGGING_IN_MANDATORY_INFORMATION_MISS = "2003";
    public static final String ERRCODE = DBPConstants.DBP_ERROR_CODE_KEY;
   	public static final String ERRMSG = DBPConstants.DBP_ERROR_MESSAGE_KEY;
}