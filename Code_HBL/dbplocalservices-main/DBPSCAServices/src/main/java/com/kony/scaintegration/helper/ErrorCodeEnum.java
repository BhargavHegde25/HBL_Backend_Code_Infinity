package com.kony.scaintegration.helper;

import com.google.gson.JsonObject;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public enum ErrorCodeEnum {
	ERR_39000(39000, "IS_SCA_ENABLED Runtime param must be set"), ERR_39001(39001, "Unable to generate service key"),
	ERR_39002(39002, "Unable to process request, SCA verification failed"), ERR_39003(39003, "Invalid input"),
	ERR_39004(39004, "Error ocurred while getting service key"),
	ERR_39005(39005, "No Records found, verification failed"),
	ERR_39006(39006, "Error ocurred while getting request payload from db"), 
	ERR_39007(39007, "No records found for given serviceKey and customerId"), 
	ERR_39008(39008, "Unable to update isVerified flag");
	private int errorCode;
	private String message;
	public static final String ERROR_CODE_KEY = "dbpErrCode";
	private static final String ERROR_MESSAGE_KEY = "dbpErrMsg";

	private ErrorCodeEnum(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

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
		result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), "int"));
		result.addParam(new Param(ERROR_MESSAGE_KEY, this.getMessage(), "String"));
		return result;
	}

	public JsonObject setErrorCode(JsonObject result) {
		if (result == null) {
			result = new JsonObject();
		}
		result.addProperty(ERROR_CODE_KEY, this.getErrorCodeAsString());
		result.addProperty(ERROR_MESSAGE_KEY, this.getMessage());
		return result;
	}

}
