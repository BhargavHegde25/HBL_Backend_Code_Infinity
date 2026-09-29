package com.temenos.infinity.tradefinanceservices.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError, Constants {

    ERR_26014(26014, FETCH_CUSTOMER_ID_FAILED),
    ERR_10227(10227, FETCH_FEATURES_FAILED),
    ERR_10117(10117, USER_UNAUTHORIZED),
    ERR_10118(10118, VALIDATION_FAILED),
    ERR_10308(10308, USER_ACCESS_FOR_THIS_ACTION_FAILED),
    ERRTF_29045(29045, UNABLE_TO_CREATE_LOC_REQUEST),
    ERRTF_29046(29046, UNABLE_TO_GET_LOC_REQUEST),
    ERRTF_29047(29047, UNABLE_TO_UPDATE_LOC),
    ERRTF_29048(29048, FAILED_TO_UPDATE_LOC),
    ERRTF_29049(29049, MANDATORY_FIELDS_MISSING),
    ERRTF_29050(29054, NO_LOC_AVAILABLE_BETWEEN_RANGE),
    ERRTF_29051(29051, FETCH_SRMSID_FAILED),
    ERRTF_29052(29052, SRMSID_NOT_FOUND_IN_RESPONSE),
    ERRTF_29053(29053, FETCH_LOC_FAILED),
    ERRTF_29057(29057, RECORD_NOT_FOUND),
    ERRTF_29058(29058, FILE_ID_MISSING),
    ERRTF_29059(29059, DOWNLOAD_ERROR),
    ERRTF_29060(29060, DATE_VALIDATION_FAILED),
    ERRTF_29054(29054, INITIATE_TRADE_FINANCE_DOWNLOAD_FAILED),
    ERRTF_29055(29055, ERROR_WHILE_GENERATING_REPORT),
    ERRTF_29056(29056, TRADE_FINANCE_FILE_GENERATION_FAILED),
    ERRTF_29061(29061, UNABLE_TO_GET_DRAWING_REQUEST),
    ERRTF_29062(29062, FETCH_DRAWING_FAILED),
    ERRTF_29063(29063, FAILED_TO_CREATE_LOC_DRAWINGS),
    ERRTF_29064(29064, FAILED_TO_CREATE_LOC_SWIFT_AND_ADVICES),
    ERRTF_29065(29065, UNABLE_TO_GETBYID_LOC),
    ERRTF_29066(29066, FAILED_TO_UPDATE_DRAWING),
    ERRTF_29067(29067, REQUESTED_DRAWING_COMPLETED),
    ERRTF_29068(29068, VALID_STATUS),
    ERRTF_29069(29069, FAILED_TO_GET_SWIFT_AND_ADVICES),
    ERRTF_29070(29070, SECURITY_EXCEPTION_UNAUTHORIZED_ACCESS),
    ERRTF_29071(29071, RECORD_WITH_SRMSID_NOT_FOUND),
    ERRTF_29072(29072, FAILED_TO_CREATE_EXPORTDRAWING),
    ERRTF_29073(29073, RECORD_CANNOT_BE_EDITED),
    ERRTF_29074(29074, UNABLE_TO_DELETE_RECORD),
    ERRTF_29075(29075, UNABLE_TO_UPDATE_DRAWING),
    ERRTF_29076(29076, FAILED_TO_DELETE_ATTACHMENT),
    ERRTF_29077(29077, UNKNOWN_STATUS),
    ERRTF_29078(29078, DRAWING_NOT_ELIGIBE_PAYMENT_ADVICE),
    ERRTF_29079(29079, XLSX_DOWNLOAD_ERROR),
    ERRTF_29080(29080, FETCH_AMENDMENT_FAILED),
    ERRTF_29081(29081, AMENDMENT_UPDATE_FAILED),
    ERRTF_29082(29082, FAILED_TO_FETCH_ATTACHMENT),
    ERRTF_29083(29083, FAILED_TO_UPLOAD_ATTACHMENT),
    ERRTF_29091(29091, CREATE_REQUEST_FAILED),
    ERRTF_29092(29092, UPDATE_REQUEST_FAILED),
    ERRTF_29093(29093, GET_REQUEST_FAILED),
    ERR_12000(12000, INTERNAL_SERVICE_ERROR),
    ERR_12001(12001, UNAUTHORIZED_USER),
    ERR_12002(12002, INVALID_INPUT),
    ERR_12003(12003, INVALID_STATUS),
    ERR_12004(12004, BACKEND_FAILED_ERROR),
    ERR_12005(12005, MAXIMUM_RETURN_REACHED),
    ERR_12006(12006, MAXIMUM_RETURN_REACHED),
    ERR_12007(12007, BACKEND_FAILED),
    ERR_12008(12008, BACKEND_FAILED_ERROR);

    private int errorCode;
    private String message;

    public static final String ERROR_CODE_KEY = "dbpErrCode";
    public static final String ERROR_MESSAGE_KEY = "dbpErrMsg";

    ErrorCodeEnum(int errorCode, String message) {
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

    public String getMessage(String params) {
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