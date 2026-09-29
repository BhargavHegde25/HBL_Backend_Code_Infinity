package com.temenos.infinity.api.visaintegrationapi.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError {

	ERR_20041(20041, "Backend Failed"), ERR_20001(20001, "Invalid Payload"),
	ERR_20002(20002, "Failed to create Simulation"),
	ERR_20003(20003, "No records were found that matched the selection criteria"),
	ERR_20004(20004, "Autoform Login Failed"),
	ERR_20005(20005, "Missing AutoForm Creds, Kindly add it to environment variables"),
	ERR_20042(20042, "Please Provide Valid Account Number"), ERR_20006(20006, "Authorization Failed"),
	ERR_20400(20040, "Internal Error"),
	ERR_20043(20043, "Unable to read value for number of days from fabric configuration"),
	ERR_20059(20059, "vCardID is missing"), ERR_20060(20060, "deviceID is missing"),
	ERR_20061(20061, "clientCustomerID is missing"), ERR_20062(20062, "deviceCert is missing"),
	ERR_20063(20063, "nonceSignature is missing"), ERR_20064(20064, "nonce is missing"),
	ERR_20053(20053, "unabel to link card"),
	ERR_20065(20065, "failed to link card as service returned %s with code %s"), ERR_20066(20066, "encCard missing"),
	ERR_20067(20067, "failed to enroll card as service returned %s with code %s"),
	ERR_20068(20068, "accountNumber is missing"), ERR_20069(20069, "expiryMonth or expiryDate is missing"),
	ERR_20070(20070, "Feature permission missing"),ERR_20071(20071, "userID and cardID are not linked");

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

	public Result setErrorCode(Result result, String message) {
		if (result == null) {
			result = new Result();
		}
		result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
		result.addParam(new Param(ERROR_MESSAGE_KEY, message, FabricConstants.STRING));
		return result;
	}

}