package com.hbl.productservicesExtn.constants;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public enum HBLEnums {
	DENIED_MONTHLY("Denied", "Denied due to monthly limit"),
	ERR_12506(12506, "transaction is denied due to monthly limit"),
	ERR_12503(12503, "transaction is denied due to autodenial monthly limit"),
	ERR_20000(20000, "We're sorry, your payment couldn't be processed at this time. Please check your payment details or try again later. If the issue persists, contact customer support for assistance."),
	ERR_20001(20001, "Sorry! Your payment could not be processed at this time.We apologize for the inconvenience."),
	ERR_12616(12616, "transaction is denied due to daily global limit"),
	ERR_12617(12617, "transaction is denied due to monthly global limit");
	 public static final String ERROR_CODE_KEY = DBPConstants.DBP_ERROR_CODE_KEY;
	    public static final String ERROR_MESSAGE_KEY = DBPConstants.DBP_ERROR_MESSAGE_KEY;
	    public static final String OPSTATUS_CODE = DBPConstants.FABRIC_OPSTATUS_KEY;
	    public static final String HTTPSTATUS_CODE = DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY;
	    public static final String ERROR_DETAILS = "errorDetails";
	private String status;
    private String message;
    private int error;
    private String errorMsg;
    
    public String getErrorCodeAsString() {
        return String.valueOf(error);
    }
	HBLEnums(String status, String message) {
		// TODO Auto-generated constructor stub
		 this.status = status;
	     this.message = message;
	}

	HBLEnums(int error, String errMsg) {
		// TODO Auto-generated constructor stub
		 this.error = error;
	     this.errorMsg = errMsg;
	}
	public Result setErrorCode(Result result) {
        if (result == null) {
            result = new Result();
        }

        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, this.getErrorMsg(), MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "0", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));

        return result;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	

}
