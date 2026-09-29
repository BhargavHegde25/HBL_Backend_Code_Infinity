package com.temenos.infinity.api.accountsweeps.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountsweeps.dto.AccountSweepsDTO;
import com.temenos.infinity.api.commons.constants.FabricConstants;

/**
 * @author naveen.yerra
 */
public enum ErrorCodeEnum implements DBPError, Constants {
    ERR_2000(2000, INTERNAL_SERVICE_ERROR),
    ERR_3001(3001, ERROR_FETCHING_SWEEP_FROM_BACKEND),
    ERR_3003(3003, ACCOUNT_ID_NULL),
    ERR_3004(3004, SECURITY_EXCEPTION),
    ERR_3005(3005, FAILED_TO_FETCH_CUSTOMER_ID),
    ERR_3006(3006, FAILED_TO_FETCH_ACCOUNTS),
    ERR_3007(3007, ERROR_CREATE_SWEEP_AT_BACKEND),
    ERR_3008(3008, ACCOUNT_UNAUTHORIZED),
    ERR_3009(3009, MANDATORY_FIELD_MISSING),
    ERR_3010(3010, INVALID_DATE),

    ERR_3011(3011, INVALID_INPUT),
    ERR_3012(3012, ACCOUNT_UNAUTHORIZED_FOR_FEATUREACTION),
    ERR_3013(3013, UNAUTHORIZED_CURRENCYCODE_ACCOUNTSTATUS),

    ERR_27017(27017, INVALID_AMOUNT_VALUE)

    ;

    private final int errorCode;
    private final String errorMessage;
    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorCodeAsString() {
        return DBPError.super.getErrorCodeAsString();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public AccountSweepsDTO setErrorMessageToDto(AccountSweepsDTO inputDto) {
        if(inputDto == null)
            inputDto = new AccountSweepsDTO();
        inputDto.setErrorCode(String.valueOf(this.errorCode));
        inputDto.setErrorMessage(this.errorMessage);
        return inputDto;
    }

    public Result setErrorCode(Result result) {
        if (result == null) {
            result = new Result();
        }
        result.addParam(new Param(DBP_ERR_CODE, this.getErrorCodeAsString(), FabricConstants.INT));
        result.addParam(new Param(DBP_ERR_MSG, this.getErrorMessage(), FabricConstants.STRING));
        return result;
    }



}
