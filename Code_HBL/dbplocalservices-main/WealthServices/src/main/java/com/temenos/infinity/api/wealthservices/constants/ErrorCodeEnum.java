package com.temenos.infinity.api.wealthservices.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError {

	ERR_20040(20040, "Internal Service Error"),
	ERR_20041(20041, "Error from Microservice"),
    ERR_20042(20042, "No records found");

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
    
    public Result setErrorCode(Result result, String errorMessage) {
        if (result == null) {
            result = new Result();
        }
        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString()));
        result.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage));
        return result;
    }

}