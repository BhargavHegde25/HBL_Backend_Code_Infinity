package com.temenos.infinity.transactionadvice.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError {

	ERR_25001(25001, "Backend Failed"), 
	ERR_25002(25002, "Invalid Payload"), 
	ERR_25003(25003, "DMS-No records were found that matched the selection criteria"), 
	ERR_25004(25004, "DMS- Login Failed"),
	ERR_25005(25005, "DMS- Login Username and Password are empty"), 
	ERR_25006(25006, "Please Provide Valid Account Number");

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
	 * Sets the {@link Result} instance with opstatus, error message from this
	 * {@link ErrorCodeEnum} enum constant
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