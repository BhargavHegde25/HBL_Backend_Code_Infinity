/**
 * 
 */
package com.infinity.dbx.temenos.constants;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public interface TemenosErrorConstants {
	
	String TRANSACTION_DELETE_FAILED_AT_BACKEND = "transaction delete is failed at backend";
	String TRANSACTION_CANCEL_OCCURRENCE_FAILED_AT_BACKEND = "transaction occurrence cancel is failed at backend";
	
	String TRUE = "true";
	String FALSE = "false";
	
	//Status Codes
	int SUCCESS = 200;
	int BAD_REQUEST = 400;
	int INTERNAL_SERVER_ERROR = 500;
	int AUTHENTICATION_FAILUIRE = 401;
	
	//EB Error Constants
	String IRIS_ERROR_GROUP = "error";
	String IRIS_ERROR_DETAILS_GROUP = "errorDetails";
	String STATUS_CODE = "statusCode";
	String IRIS_CODE = "code";
	String IRIS_MESSAGE = "message";
	String ERROR_DETAILS = "errorDetails";
	String DBP_ERR_CODE = "dbpErrCode";
	String DBP_ERR_MESSAGE = "dbpErrMsg";
	String T24_GENERIC_ERR_CODE = "15001";
	String T24_GENERIC_ERR_MESSAGE = "Backend Integration Failed";
	String T24_AUTH_ERR_CODE = "15002";
    String T24_AUTH_ERR_MESSAGE = "Unable to authenticate against Transact";
    String DBP_ERR_RESULT = "errorResult";
	
	//Override Constants
	String IRIS_OVERRIDE = "override";
	String IRIS_OVERRIDE_DETAILS_GROUP = "overrideDetails";
	String IRIS_OVERRIDE_DESCRIPTION = "description";
	String IRIS_OVERRIDE_MESSAGE = "Message";
	String IRIS_OVERRIDE_TYPE = "type";
	String IRIS_OVERRIDE_OPTIONS = "options";
	String DBP_RESPONSE_CODE = "responseCode";
	String OVERRIDES = "overrides";
	String IRIS_BODY = "body";
	String IRIS_ID = "id";
	String AA_ARRANGEMENT_ACTIVITY = "arrangementActivity";
	String MESSAGE_DETAILS = "messageDetails";
	
}
