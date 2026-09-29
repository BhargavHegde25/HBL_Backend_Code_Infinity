package com.temenos.infinity.api.chequemanagement.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError { 

    ERRCHQ_26011(26011, "Unable to create order for cheque book request"),
    ERRCHQ_26012(26012, "Unable to create order for Stop payment request"),
    ERRCHQ_26015(26015, "Error occured while validating cheque book request"),
    ERRCHQ_26016(26016, "Error occured while withdrawing cheque book request"),
    ERRCHQ_26017(26017, "Error occured while validating stop payment request"),
    ERRCHQ_26018(26018, "Unable to create order for Revoke Stop payment request"),
    ERRCHQ_26019(26019, "Error occured while rejecting chequebook request"),
    ERRCHQ_26020(26020, "Error occured while fetching cheque book details"),
    ERRCHQ_26004(26004, "Unable to get cheque book requests from OMS"),
    ERRCHK_26013(26013, "Unable to get stop payment requests"); 

    private int errorCode;
    private String message;

    public static final String ERROR_CODE_KEY = "dbpErrCode";
    public static final String ERROR_MESSAGE_KEY = "dbpErrMsg";

    private ErrorCodeEnum(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }

    @Override
    public String getErrorCodeAsString() {
        return String.valueOf(errorCode);
    }
    
    public String getMessage(String... params) {
        return String.format(this.message, params);
    } 

    /**
     * Sets the {@link Result} instance with opstatus, error message from this {@link ErrorCodeEnum} enum constant
     * 
     * @param result
     */
    public Result setErrorCode(Result result) {
        if (result == null) {
            result = new Result();
        }
        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, this.getErrorMessage(), FabricConstants.STRING));
        return result;
    }
    
    public Result setErrorCode(Result result, String message) {
        if (result == null) {
            result = new Result();
        }
        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, message, FabricConstants.STRING));
        return result;
    }

}