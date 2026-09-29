package com.temenos.dbx.eum.sca;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public enum ErrorCodeEnum {

    ERR_99500(99500, "Please contact the Bank for further details."),
	ERR_99501(99501, "Please contact the Bank for further details."), 
	ERR_99502(99502, "Please contact the Bank for further details."),
	ERR_99503(99503, "Please contact the Bank for further details."),
	ERR_99504(99504, "Please contact the Bank for further details."), 
	ERR_99505(99505, "Please contact the Bank for further details.");
    

    public static final String ERROR_CODE_KEY = DBPConstants.DBP_ERROR_CODE_KEY;
    public static final String ERROR_MESSAGE_KEY = DBPConstants.DBP_ERROR_MESSAGE_KEY;
    public static final String OPSTATUS_CODE = DBPConstants.FABRIC_OPSTATUS_KEY;
    public static final String HTTPSTATUS_CODE = DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY;
    public static final String ERROR_DETAILS = "errorDetails";
    private int errorCode;
    private String message;

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

    public Integer getErrorCodeAsInt() {
        return Integer.valueOf(errorCode);
    }

    public String getMessage(String... params) {
        return String.format(this.message, params);
    }

    public Result setErrorCode(Result result) {
        if (result == null) {
            result = new Result();
        }

        result.removeParamByName(OPSTATUS_CODE);
        result.removeParamByName(HTTPSTATUS_CODE);
        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, this.getMessage(), MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "-1", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));
        result.addErrMsgParam(this.getMessage());

        return result;
    }

    public Record setErrorCode(Record record) {
        if (record == null) {
            record = new Record();
        }

        record.removeParamByName(OPSTATUS_CODE);
        record.removeParamByName(HTTPSTATUS_CODE);
        record.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        record.addParam(new Param(ERROR_MESSAGE_KEY, this.getMessage(), MWConstants.STRING));
        record.addParam(new Param(OPSTATUS_CODE, "-1", MWConstants.INT));
        record.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));
        record.addStringParam("errmsg", this.getMessage());

        return record;
    }

    public Result setErrorCode(Result result, String errorMessage) {
        if (result == null) {
            result = new Result();
        }
        if(StringUtils.isBlank(errorMessage)) {
            errorMessage = this.message;
        }
        result.removeParamByName(OPSTATUS_CODE);
        result.removeParamByName(HTTPSTATUS_CODE);
        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "-1", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));
        result.addErrMsgParam(errorMessage);

        return result;
    }

    public Record setErrorCode(Record record, String errorMessage) {
        if (record == null) {
            record = new Record();
        }

        record.removeParamByName(OPSTATUS_CODE);
        record.removeParamByName(HTTPSTATUS_CODE);
        record.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        record.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        record.addParam(new Param(OPSTATUS_CODE, "-1", MWConstants.INT));
        record.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));
        record.addStringParam("errmsg", errorMessage);
        
        return record;
    }

    public Result setErrorCode(Result result, String errorCode, String errorMessage) {
        if (result == null) {
            result = new Result();
        }

        result.removeParamByName(OPSTATUS_CODE);
        result.removeParamByName(HTTPSTATUS_CODE);
        result.addParam(new Param(ERROR_CODE_KEY, errorCode, MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "-1", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));
        result.addErrMsgParam(errorMessage);

        return result;
    }
    
    public Result setErrorCodewithErrorDetails(Result result, String errorMessage, String errorDetails) {
        if (result == null) {
            result = new Result();
        }

        result.removeParamByName(OPSTATUS_CODE);
        result.removeParamByName(HTTPSTATUS_CODE);
        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        result.addParam(new Param(ERROR_DETAILS, errorDetails, MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "-1", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));
        result.addErrMsgParam(errorMessage);

        return result;
    }

    public JsonObject setErrorCode(JsonObject result) {
        if (result == null) {
            result = new JsonObject();
        }

        result.remove(OPSTATUS_CODE);
        result.remove(HTTPSTATUS_CODE);
        result.addProperty(ERROR_CODE_KEY, this.getErrorCodeAsInt());
        result.addProperty(ERROR_MESSAGE_KEY, this.getMessage());
        result.addProperty(OPSTATUS_CODE, Integer.parseInt("-1"));
        result.addProperty(HTTPSTATUS_CODE, Integer.parseInt("0"));
        result.addProperty("errmsg", this.getMessage());

        return result;
    }

    public JsonObject setErrorCode(JsonObject result, String message) {
        if (result == null) {
            result = new JsonObject();
        }

        result.remove(OPSTATUS_CODE);
        result.remove(HTTPSTATUS_CODE);
        result.addProperty(ERROR_CODE_KEY, this.getErrorCodeAsInt());
        result.addProperty(ERROR_MESSAGE_KEY, message);
        result.addProperty(OPSTATUS_CODE, Integer.parseInt("-1"));
        result.addProperty(HTTPSTATUS_CODE, Integer.parseInt("0"));
        result.addProperty("errmsg", message);

        return result;
    }
    
    public Result updateResultObject(Result result) {
		if (result == null) {
			return constructResultObject();
		} else {
			return addAttributesToResultObject(result);
		}
	}

	public Result constructResultObject() {
		Result result = new Result();
		return addAttributesToResultObject(result);
	}

	private Result addAttributesToResultObject(Result result) {
		result.removeParamByName(OPSTATUS_CODE);
        result.removeParamByName(HTTPSTATUS_CODE);
		result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString()));
		result.addParam(new Param(ERROR_MESSAGE_KEY, this.message));
		result.addParam(new Param(OPSTATUS_CODE, "-1", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));
        result.addErrMsgParam(this.message);
		return result;
	}

}
