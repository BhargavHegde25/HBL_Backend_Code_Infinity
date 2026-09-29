package com.temenos.infinity.api.srmstransactions.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError {

    ERR_32041(32041, "Backend Failed"), 
    ERR_33201(32001, "Invalid Payload"),
    ERR_32002(32002, "Failed to create Simulation"),
    ERR_32003(32003, "No records were found that matched the selection criteria"),
    ERR_32004(32004, "Autoform Login Failed"),
    ERR_32005(32005, "Missing AutoForm Creds, Kindly add it to environment variables"),
    ERR_32042(32042, "Please Provide Valid Account Number"),
    ERR_32006(32006, "Authorization Failed"),
    ERR_20400(32040, "Internal Error"),
    ERR_32043(32043, "Unable to read value for number of days from fabric configuration"),
    ERR_32044(32044, "Unable to fetch transactions from SRMS"),
    ERR_32045(32045, "Unable to fetch user accounts from cache");

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

}