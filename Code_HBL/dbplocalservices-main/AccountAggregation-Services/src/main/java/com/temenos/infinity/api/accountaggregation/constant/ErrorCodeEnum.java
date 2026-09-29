package com.temenos.infinity.api.accountaggregation.constant;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

/**
 * Enumeration that holds error codes
 * 
 * @author Aditya Mankal
 *
 */
public enum ErrorCodeEnum implements DBPError {

    ERR_20000(20000, "Unauthorized access"), ERR_20001(20001, "Invalid Payload"), ERR_20002(20002, "No records found"),
    ERR_20003(20003, "Failed to create SaltEdge Customer"), ERR_20004(20004, "Failed to retrieve Banks"),
    ERR_20005(20005, "Failed to delete Connections"), ERR_20006(20006, "Failed to create SaltEdge Consent "),
    ERR_20007(20007, "Failed to refresh SaltEdge Consent "), ERR_20008(20008, "Failed to update status "),
    ERR_20009(20009, "Failed to load  connection "), ERR_20010(20010, "Failed to load accounts "),
    ERR_20011(20011, "Cannot be refreshed now"), ERR_20400(20040, "Internal Error"), ERR_20041(20041, "Backend Failed"), ERR_20042(20042, "Couldn't generate Auth Token Successfully, empty response");

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