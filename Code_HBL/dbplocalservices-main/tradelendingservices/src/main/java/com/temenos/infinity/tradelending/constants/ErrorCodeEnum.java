/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.constants;

import static com.kony.dbputilities.util.ErrorCodeEnum.HTTPSTATUS_CODE;
import static com.kony.dbputilities.util.ErrorCodeEnum.OPSTATUS_CODE;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.error.DBPError;
import com.google.gson.JsonObject;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

/**
 * @author mrunalini.adepu
 *
 */
public enum ErrorCodeEnum implements DBPError {

	ERR_30001(30001, "Input Validation Failed"),
    ERR_30002(30002, "Requested record not found"),
    ERR_30003(30003, "Error occurred while sending request"),
    ERR_30004(30004, "Mandatory fields are missing in request payload"),
    ERR_30008(30008, "Logged in user is not authorized to perform this action"),
    ERR_30009(30009, "Error occurred while generating the document"),
    ERR_30011(30011, "Failed to upload document"),
    ERR_30012(30012, "Failed to fetch document"),
    ERR_30013(30013, "Failed to delete document"),
    ERR_30016(30016, "Error occurred while processing input"),
    ERR_30018(30018, "Error occurred while processing input"),
    ERR_30026(30026, "Request exceeds more than maximum(10) records allowed"),
    ERR_30027(30027, "Backend Failed to create invoice");

    private final int errorCode;
    private final String errorMessage;

    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public JsonObject setErrorCode(JsonObject result) {
        if (result == null) {
            result = new JsonObject();
        }
        result.addProperty(DBPConstants.DBP_ERROR_CODE_KEY, this.getErrorCode());
        result.addProperty(DBPConstants.DBP_ERROR_MESSAGE_KEY, this.getErrorMessage());
        result.addProperty(OPSTATUS_CODE, Integer.parseInt("0"));
        result.addProperty(HTTPSTATUS_CODE, Integer.parseInt("0"));
        return result;
    }

    public Result setErrorCode(Result result) {
        if (result == null) {
            result = new Result();
        }
        result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
        result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, this.getErrorMessage(), FabricConstants.STRING));
        return result;
    }

    public Result setErrorCode(Result result, String errorMessage) {
        if (result == null) {
            result = new Result();
        }
        result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
        result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, errorMessage, FabricConstants.STRING));
        return result;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public String getErrorCodeAsString() {
        return DBPError.super.getErrorCodeAsString();
    }
}
