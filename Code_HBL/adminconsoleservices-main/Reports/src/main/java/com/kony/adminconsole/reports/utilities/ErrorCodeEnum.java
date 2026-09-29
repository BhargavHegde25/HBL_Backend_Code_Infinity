package com.kony.adminconsole.reports.utilities;


import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
/**
 * Enum that holds error codes related to admin console services and processors
 * 
 * @author Sri Kavya Pitchika
 *
 */

public enum ErrorCodeEnum {
	ERR_29000(29000, "Failed to delete report"),
	ERR_29001(29001, "Failed to share report to the users"),
	ERR_29002(29002, "Failed to share report to the roles"),
    ERR_29003(29003, "Failed to create report"),
	ERR_29004(29004, "Report Name cannot be empty"),
	ERR_29005(29005, "Internal Error"),
	ERR_29006(29006, "Failed to get data sources"), 
	ERR_29007(29007, "Invalid input for fabric reports"),
	ERR_29008(29008, "Failed to fetch list of fabric reports"), 
	ERR_29009(29009, "Failed to fetch filters"),
	ERR_29010(29010, "Failed to view report"), 
	ERR_29011(29011, "Invalid Customer Role Id"),
	ERR_29012(29012, "Failed to get role(s)"), 
	ERR_29013(29009, "Invalid userId"),
	ERR_29014(29014, "Failed to get user(s)"), 
	ERR_29015(29015, "User cannot share report"),
	ERR_29016(29016, "Failed to get report(s)"), 
	ERR_29017(29017, "Failed to read Shared report(s)"),
	ERR_29018(29018, "User cannot delete report"),
	ERR_29019(29019, "External Id cannot be empty"),
	ERR_29020(29020,"No changes identified");
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
     * Sets the {@link Result} instance with opstatus, error message from this {@link ErrorCodeEnum} enum constant
     * 
     * @param result
     */
    public Result setErrorCode(Result result) {
        if (result == null) {
            result = new Result();
        }
        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, this.getMessage(), FabricConstants.STRING));
        return result;
    }

	public JSONObject setErrorCode(JSONObject result) {
		if (result == null) {
			result = new JSONObject();
		}
		result.put(ERROR_CODE_KEY, this.getErrorCodeAsString());
		result.put(ERROR_MESSAGE_KEY, this.getMessage());
		return result;
	}
	public JSONArray setErrorCode(JSONArray result) {
		if (result == null) {
			result = new JSONArray();
		}
		JSONObject js = new JSONObject();
		js.put(ERROR_CODE_KEY, this.getErrorCodeAsString());
		js.put(ERROR_MESSAGE_KEY, this.getMessage());
		result.put(js);
		return result;
	}

}
