
package com.kony.dbp.queuemaster.utils;

import com.temenos.logger.alert.Alert;
import com.dbp.core.constants.DBPConstants;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public abstract class MiscUtils {

	private MiscUtils() {
		
	}
	// Method to derive a valid token for a given set of events. A shared secret is hashed using the
	// events as salt to produce the token. This ensures that a different token is required for any given
	// set of events.
	public static String deriveToken(String events) {
		String salt = Hashing.sha512().hashString(events, Charsets.UTF_8).toString();
		String secret = Config.getValue("QUEUEMASTER_SHARED_SECRET");
		String saltedSecret = salt + secret;
		return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
	}

	// Method to extract an element from a JSON object.
	public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = object.get(key);
		if (element == null && required) {
			throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
		}
		return element;
	}

	// Method to extract a floating point number from a JSON object.
	public static Float getFloatFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		return ((element == null) ? null : element.getAsFloat());
	}

	// Method to extract a JSON object from another JSON object.
	public static JsonObject getJsonObjectFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		if (element == null) {
			return null;
		} else {
			if (!element.isJsonObject()) {
				throw new IllegalArgumentException("Value for attribute '" + key + "' is not a JSON object");
			}
			return element.getAsJsonObject();
		}
	}

	// Method to extract a string from a JSON object.
	public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
		try {
		JsonElement element = getElementFromJsonObject(object, key, required);
		//return ((element.isJsonNull()) ? null : element.getAsString());
		return ((element == null) ? null : element.getAsString());
		}catch (Exception e) {
			return null;
		}
	}

	// Method to generically handle thrown errors or exceptions.
	public static void handleFailure(Alert logger, Result result, Throwable ex) {
		String errorMsg = ex.getMessage();
		if (errorMsg == null) {
			StackTraceElement ste = ex.getStackTrace()[0];
			errorMsg = ex.getClass().getName() + " thrown in " + ste.getClassName() + "." + ste.getMethodName();
		}
		if (logger != null) {
			logger.prepareError(errorMsg, ex).log();
		}
		if (result != null) {
			handleFailure(null, result, 57777, errorMsg);
		}
	}

	// Method to generically handle error conditions.
	public static void handleFailure(Alert logger, Result result, int errorNumber, String errorMsg) {
		if (logger != null) {
			logger.prepareError(errorMsg).log();
		}
		if (result != null) {
			result.addParam(new Param("dbpErrCode", Integer.toString(errorNumber), DBPConstants.FABRIC_STRING_CONSTANT_KEY));
			result.addParam(new Param("dbpErrMsg", errorMsg, DBPConstants.FABRIC_STRING_CONSTANT_KEY));
			result.addParam(new Param("success", Boolean.toString(false), DBPConstants.FABRIC_STRING_CONSTANT_KEY));
		}
	}

	// Method to generically handle success.
	public static void handleSuccess(Result result) {
		// Not logging success results for now.
		if (result != null) {
			result.addParam(new Param("success", Boolean.toString(true), DBPConstants.FABRIC_STRING_CONSTANT_KEY));
		}
	}
}
