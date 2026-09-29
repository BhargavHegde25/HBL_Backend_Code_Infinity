/**
 * 
 */
package com.temenos.infinity.api.wealthservices.util;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosErrorConstants;

/**
 * This Utils class is used to handle T24 Errors and Overrides
 * 
 */
public class T24ErrorAndOverrideHandling implements TemenosErrorConstants {
	/*
	 * static holder design pattern to create singleton object
	 */

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private static class Holder {
		static final T24ErrorAndOverrideHandling INSTANCE = new T24ErrorAndOverrideHandling();
	}

	public static T24ErrorAndOverrideHandling getInstance() {
		return Holder.INSTANCE;
	}

	public Result ProcessT24Error(Result result, DataControllerRequest request, DataControllerResponse response) {

		Result errResult = new Result();
		String serviceResponse = response.getResponse();
		diagnostic.prepareDebug("Iris Service Response : " + serviceResponse).log();

		// ----Get status code----
		int statusCode = response.getStatusCode();

		// Check for Authentication Error
		if (statusCode == AUTHENTICATION_FAILUIRE) {
			// Add Generic DBP Error Message
			errResult.addParam(DBP_ERR_CODE, T24_AUTH_ERR_CODE);
			errResult.addParam(DBP_ERR_MESSAGE, T24_AUTH_ERR_MESSAGE);
			errResult.addParam(DBP_ERR_RESULT, TRUE);
			return errResult;
		}

		// ----Process T24 EB Error Messages and Error type overrides----
		JSONObject resultObject = new JSONObject(serviceResponse);
		if (resultObject.has(IRIS_ERROR_GROUP)) {
			JSONArray dbpErrors = new JSONArray();
			JSONObject errorObject = resultObject.optJSONObject(IRIS_ERROR_GROUP);
			if (errorObject != null) {
				JSONArray errorDetails = errorObject.optJSONArray(IRIS_ERROR_DETAILS_GROUP);
				if (errorDetails != null) {
					for (Object err : errorDetails) {
						JSONObject T24Err = new JSONObject(err.toString());
						JSONObject dbpErr = new JSONObject();
						if (T24Err.has(IRIS_MESSAGE) && StringUtils.isNotBlank(T24Err.getString(IRIS_MESSAGE)))
							dbpErr.put(IRIS_MESSAGE, T24Err.getString(IRIS_MESSAGE));
						if (T24Err.has(IRIS_CODE) && StringUtils.isNotBlank(T24Err.getString(IRIS_CODE)))
							dbpErr.put(IRIS_ID, T24Err.getString(IRIS_CODE));
						// Process DBP Error Messages - Convert T24 to DBP error
						// To Do
						dbpErrors.put(dbpErr);
					}
				}

				errResult.addParam(ERROR_DETAILS, dbpErrors.toString());
				// Add Generic DBP Error Message
				errResult.addParam(DBP_ERR_CODE, T24_GENERIC_ERR_CODE);
				errResult.addParam(DBP_ERR_MESSAGE, T24_GENERIC_ERR_MESSAGE);
				errResult.addParam(DBP_ERR_RESULT, TRUE);

			}
			return errResult;

		}

		// Process Warning type overrides -- Phase 2 Development
		/*
		 * if (resultObject.has(IRIS_OVERRIDE) && statusCode == BAD_REQUEST) {
		 * JSONObject overrideObject = resultObject.optJSONObject(IRIS_OVERRIDE); if
		 * (overrideObject != null) { JSONArray overrideDetails =
		 * overrideObject.optJSONArray(IRIS_OVERRIDE_DETAILS_GROUP); if (overrideDetails
		 * != null && overrideDetails.length() > 0) { result.addParam(OVERRIDES,
		 * overrideDetails.toString()); } } // Check for err msg if
		 * (result.getParamValueByName("errmsg") != null) { alert.prepareError(
		 * "Removing errmsg to proceed with override response" +
		 * result.getParamValueByName("errmsg")).log(); result.removeParamByName("errmsg"); }
		 * }
		 */

		// Handle Empty, Warning Overrides as errors (400 response)
		if (resultObject.has(IRIS_OVERRIDE) && statusCode == BAD_REQUEST) {
			JSONArray dbpErrors = new JSONArray();
			JSONObject overrideObject = resultObject.optJSONObject(IRIS_OVERRIDE);
			if (overrideObject != null) {
				JSONArray overrideDetails = overrideObject.optJSONArray(IRIS_OVERRIDE_DETAILS_GROUP);
				for (Object overr : overrideDetails) {
					JSONObject T24Ovr = new JSONObject(overr.toString());
					JSONObject dbpErr = new JSONObject();
					if (T24Ovr.has(IRIS_OVERRIDE_TYPE) && StringUtils.isNotBlank(T24Ovr.getString(IRIS_OVERRIDE_TYPE))
							&& T24Ovr.getString(IRIS_OVERRIDE_TYPE).equalsIgnoreCase(IRIS_OVERRIDE_MESSAGE))
						continue;
					if (T24Ovr.has(IRIS_OVERRIDE_DESCRIPTION)
							&& StringUtils.isNotBlank(T24Ovr.getString(IRIS_OVERRIDE_DESCRIPTION)))
						dbpErr.put(IRIS_MESSAGE, T24Ovr.getString(IRIS_OVERRIDE_DESCRIPTION));
					if (T24Ovr.has(IRIS_ID) && StringUtils.isNotBlank(T24Ovr.getString(IRIS_ID)))
						dbpErr.put(IRIS_ID, T24Ovr.getString(IRIS_ID));
					// Process DBP Error Messages - Convert T24 to DBP error
					// To Do
					dbpErrors.put(dbpErr);
				}

				errResult.addParam(ERROR_DETAILS, dbpErrors.toString());
				// Add Generic DBP Error Message
				errResult.addParam(DBP_ERR_CODE, T24_GENERIC_ERR_CODE);
				errResult.addParam(DBP_ERR_MESSAGE, T24_GENERIC_ERR_MESSAGE);
				errResult.addParam(DBP_ERR_RESULT, TRUE);
			}
			return errResult;
		}

		// Process Auto and message type Overrides Acknowledgement Overrides - Setting
		// as Error
		if (statusCode == SUCCESS) {
			JSONObject body = resultObject.optJSONObject(IRIS_BODY);
			if (body != null) {
				// Handle AA response overrides
				if (body.has(AA_ARRANGEMENT_ACTIVITY)) {
					body = body.optJSONObject(AA_ARRANGEMENT_ACTIVITY);
				}
				JSONArray ackOverrides = body.optJSONArray(OVERRIDES);
				if (ackOverrides != null) {
					JSONArray dbpOverrides = new JSONArray();
					if (ackOverrides != null) {
						for (Object override : ackOverrides) {
							JSONObject T24Override = new JSONObject(override.toString());
							JSONObject dbpOverride = new JSONObject();
							if (T24Override.has(IRIS_OVERRIDE)
									&& StringUtils.isNotBlank(T24Override.getString(IRIS_OVERRIDE))) {
								String overrideString = T24Override.getString(IRIS_OVERRIDE);
								int index = overrideString.indexOf("}");
								dbpOverride.put(IRIS_ID, overrideString.substring(0, index));
								dbpOverride.put(IRIS_MESSAGE,
										overrideString.substring(index + 1, overrideString.length()));
							}
							dbpOverrides.put(dbpOverride);
						}
					}

					result.addParam(MESSAGE_DETAILS, dbpOverrides.toString());
				}
			}

		}

		return result;
	}

	public boolean isErrorResult(Result result) {
		String errResult = getParamValue(result, DBP_ERR_RESULT);
		if (errResult.equalsIgnoreCase(TRUE))
			return true;
		else
			return false;
	}

	public static String getParamValue(Result result, String name) {

		// Get the parameter value
		String value = "";
		if (result != null && name != null) {
			if (result.getParamByName(name) != null) {
				if (result.getParamByName(name).getValue() != null) {
					value = result.getParamByName(name).getValue();
				}
			}
		}
		return value;
	}

}
