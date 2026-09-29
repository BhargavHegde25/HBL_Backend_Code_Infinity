package com.infinity.dbx.temenos.utils;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.constants.TemenosErrorConstants;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

/**
 * This Utils class is used to handle T24 Errors and Overrides
 *
 * @author smugesh
 *
 */
public class T24ErrorAndOverrideHandling implements TemenosErrorConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /*
     * static holder design pattern to create singleton object
     */
    
    private static class Holder {
        static final T24ErrorAndOverrideHandling INSTANCE = new T24ErrorAndOverrideHandling();
    }

    public static T24ErrorAndOverrideHandling getInstance() {
        return Holder.INSTANCE;
    }

    private T24ErrorAndOverrideHandling() {

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
        /*if (resultObject.has(IRIS_OVERRIDE) && statusCode == BAD_REQUEST) {
            JSONObject overrideObject = resultObject.optJSONObject(IRIS_OVERRIDE);
            if (overrideObject != null) {
                JSONArray overrideDetails = overrideObject.optJSONArray(IRIS_OVERRIDE_DETAILS_GROUP);
                if (overrideDetails != null && overrideDetails.length() > 0) {
                    result.addParam(OVERRIDES, overrideDetails.toString());
                }
            } // Check for err msg
            if (result.getParamValueByName("errmsg") != null) {
                alert.prepareError(
                        "Removing errmsg to proceed with override response" + result.getParamValueByName("errmsg")).log();
                result.removeParamByName("errmsg");
            }
        } */
         

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

        // Process Auto and message type Overrides Acknowledgement Overrides - Setting as Error
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
        String errResult = CommonUtils.getParamValue(result, DBP_ERR_RESULT);
        if (errResult.equalsIgnoreCase(TRUE))
            return true;
        else
            return false;
    }

    /*
     * This method is used to add custom error messages for the specific flow
     * Input : (result, "1002", "Account Creation Failed") Output : Updated
     * Result
     */
    public Result AddCustomErrorDetails(Result result, String dbpErrCode, String dbpErrMsg) {
        if (StringUtils.isNotBlank(dbpErrCode)) {
            result.removeParamByName(DBP_ERR_CODE);
            result.addStringParam(DBP_ERR_CODE, dbpErrCode);
        }
        if (StringUtils.isNotBlank(dbpErrMsg)) {
            result.removeParamByName(DBP_ERR_MESSAGE);
            result.addStringParam(DBP_ERR_MESSAGE, dbpErrMsg);
        }
        return result;
    }
    
    //Identify the method type
    public boolean isGetRequest(DataControllerRequest request, String response) throws AppRegistryException{
        String method = request.getServicesManager().getOperationData().getHttpMethod();
        if (StringUtils.isNotBlank(method) && method.equalsIgnoreCase("GET")){
            alert.prepareError("Iris Method is : "+method+" ,Returning result without processing").log();
            return true;
        }
        if(StringUtils.isBlank(method)){
            if(StringUtils.isNotBlank(response)){
                JSONObject IrisResponse = new JSONObject(response);
                if(IrisResponse.has(IRIS_BODY) && IrisResponse.optJSONArray(IRIS_BODY) != null){
                    alert.prepareError("Iris Method is : "+method+" ,Returning result without processing").log();
                    return true;
                }   
            }
        }
        return false;
    }

}
