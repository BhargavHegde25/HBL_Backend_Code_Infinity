package com.kony.campaignsmanagement.utils;

import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public enum ErrorCodesEnum {

	ERR_10001(10001, "Operation name is a manadatory field"),
	ERR_10002(10002, "Exception in java service"),
	ERR_10003(10003, "Error occurred while creating profile"),
	ERR_10004(10004, "Error occurred while creating profile conditions"),
	ERR_10005(10005, "Internal Error."),
	ERR_10006(10006, "Mandatory fields are empty"),
	ERR_22245(22245, "Campaign mandatory feilds cannot be empty"),
	ERR_22246(22246,"Error while creating campaign"),
	ERR_22251(22251, "Error while getting campaigns"),
    ERR_22252(22252, "Error while updating campaigns"),
	ERR_10007(10007, "Profile with similar name already exists"),
	ERR_10008(10008, "No profile present to update."),
	ERR_10009(10009, "Error occurred while updating profile"),
	ERR_10010(10010, "Error occurred while updating profile conditions"),
	ERR_10011(10011, "Cannot update, profile with same name already exits."),
	ERR_10012(10012, "Given condition does not belong to this profile."),
	ERR_10013(10013, "Error while fetching profile conditions"),
	ERR_10014(10014, "Cannot update profile in deleted state."),
	ERR_10015(10015, "Failed to delete removed profile conditions."),
	ERR_10016(10016, "Error while fetching profiles")
	;

	private int errorCode;
	private String message;
	public static final String ERROR_CODE_KEY = "dbpErrCode";
	public static final String ERROR_MESSAGE_KEY = "dbpErrMsg";

	private ErrorCodesEnum(int errorCode, String message) {
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
		result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
		result.addParam(new Param(ERROR_MESSAGE_KEY, this.getMessage(), FabricConstants.STRING));
		return result;
	}
	
	public JSONObject setErrorCode(JSONObject json) {
		if(json == null) {
			json = new JSONObject();
		}
		json.put(ERROR_CODE_KEY, this.getErrorCodeAsString());
		json.put(ERROR_MESSAGE_KEY, this.getMessage());
		return json;
	}

}
