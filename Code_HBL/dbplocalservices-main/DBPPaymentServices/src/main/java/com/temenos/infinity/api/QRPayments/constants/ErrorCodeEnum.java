package com.temenos.infinity.api.QRPayments.constants;

import com.dbp.core.error.DBPError;
import com.kony.dbputilities.util.ErrorConstants;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError, Constants {
	ERR_20041(20041, "Backend Failed"), ERR_12031(12031, ErrorConstants.MISSING_AMOUNT),
	ERR_12307(12307, ErrorConstants.FROMACCOUNTNUMBER_SAME_AS_TOACCOUNTNUMBER),
	ERR_12001(12001, ErrorConstants.USER_UNAUTHORIZED), ERR_27017(27017, ErrorConstants.INVALID_AMOUNT_VALUE),
	ERR_28021(28021, ErrorConstants.ERROR_IN_PARSING_INPUT_RECORDS), ERR_00000(15001, "Error occured at backend"),
	ERR_12601(12601, ErrorConstants.TRANSACTION_CREATION_FAILED_AT_BACKEND),
	ERR_12602(12602, FROMACCOUNTCURRENCY_DIFFER_TRANSACTIONCURRENCY), ERR_12603(12603, FAILED_TO_FETCH_CUSTOMER_ID),
	ERR_12604(12604, ACCOUNT_UNAUTHORIZED),;

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

	public Result setErrorCode(Result result) {
		if (result == null) {
			result = new Result();
		}
		result.addParam(new Param(DBP_ERR_CODE, this.getErrorCodeAsString(), FabricConstants.INT));
		result.addParam(new Param(DBP_ERR_MSG, this.getErrorMessage(), FabricConstants.STRING));
		return result;
	}
}
