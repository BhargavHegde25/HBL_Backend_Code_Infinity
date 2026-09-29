package com.temenos.infinity.api.usermanagement.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError {

	ERR_20041(20041, "Backend Failed"), ERR_200511(200511, "AccountId Missing"),
	ERR_200512(200512, "EStmtEnabled Missing"), ERR_200513(200513, "Email Missing"),
	ERR_200514(200514, "Phone Number Missing"), ERR_200515(200515, "Phone Number Identity Missing"),
	ERR_200516(200516, "Email Identity Missing"), ERR_20052(20052, "Error occured while update Account Settings"),
	ERR_20053(20053, "Unable to create user account settings order"),
	ERR_20054(20054, "Update Account Settings Failed, %s"), ERR_20055(20055, "Update Account Settings Failed"),
	ERR_20056(20056, "Update Customer Details Failed, %s"), ERR_20057(20057, "Update Customer Details Failed"),
	ERR_20058(20058, "Error occured while update Customer Details"), ERR_20059(20059, "Communication ID Missing"),
	ERR_20060(20060, "DeleteAddress ID Missing"),ERR_20061(20061, "Update Account Settings for %s returned %s due to %s"),
	ERR_20062(20062, "Update User Settings for %s returned %s due to %s"),
	ERR_200517(200517, "Invalid Email Id sent in the request"),
	ERR_200530(200530, "User can have maximum %s email Ids"),
	ERR_200531(200531, "Email Id already exists for the user"),
	ERR_200532(200532,"Upload Documents Failed"),
	ERR_200533(200533,"SECURITY EXCEPTION - UNAUTHORIZED ACCESS");

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