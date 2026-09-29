package com.kony.makerchecker.utils;

import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public enum ErrorCodesEnum {

	ERR_10001(10001, "Operation name is a manadatory field"),
	ERR_10002(10002, "Failed to fetch data from makercheckerconfig table"),
	ERR_10003(10003, "No data present for the given input"),
	ERR_10004(10004, "legal entity cannot be null"),
	ERR_10005(10005, "Internal Error."),
	ERR_10006(10006, "Error occured while storing the request."),
	ERR_10007(10007, "Exception occured while trying to get the dashboard counts for makerchecker."),
	ERR_10008(10008, "Exception occured while trying to get the user legal entities."),
	ERR_10009(10009, "Exception occured while processing approval requests data."),
	ERR_10010(10010, "Exception occured while trying to get data from get_mc_approvalrequests_view."),
	ERR_10011(10011, "User do not have the required permissions."),
	ERR_10012(10012, "Error occured while parsing the filter data"),
	ERR_10013(10013, "Failed to fetch pending Requests data from  table"),
	ERR_10014(10014, "Exception occured while trying to get data from dbxdb_checkforpendingrequests_proc."),
	ERR_10015(10015, "module, action, recordId, companyLegalUnit Params are manadatory fields"),
	ERR_10016(10016, "Exception while fetching Approval Request details"),
	ERR_10017(10017, "module, action, requestId Params are manadatory fields"),
	ERR_10018(10018, "Action is a manadatory field"),
	ERR_10019(10019, "Request id is a manadatory field"),
	ERR_10020(10020, "Exception while approving/rejecting the approval request"),
	ERR_10021(10021, "Action can be either APPROVED or REJECTED"),
	ERR_10022(10022, "Error occurred while rejecting the request"),
	ERR_10023(10023, "Error occurred while approving the request"),
	ERR_10024(10024, "Exception occured while trying to get data from dbxdb_makercheckerconfig_view_get."),
	ERR_10025(10025, "Exception in Maker Checker Config Manage Service."),
	ERR_10026(10026, "Request is not in pending status and cannot complete your action."),
	ERR_10027(10027, "Invalid RequestId."),
	ERR_10028(10028, "Exception while parsing approvalrequest details"),
	ERR_10029(10029, "Exception occured while trying to get data from dbxdb_fetch_maker_pending_requests_proc."),
	ERR_10030(10030, "legalEntityId is manadatory !"),
	ERR_10031(10031, "User don't have permission to Approval with request type for selected Legal entity"),
	ERR_10032(10032, "Exception occurred in invoking getCheckerApprovalRequests"),
	ERR_10033(10033, "Exception while fetching Approvar Pending Request details!"),
	ERR_10034(10034, "Caught exception while fetching Pending Checker Approval request in get_checkerpending_requests_proc!"),
	ERR_10035(10035, "companyLegalUnit and userType are manadatory"),
	ERR_10036(10036, "Failed to fetch Request history data from  table"),
	ERR_10037(10037, "languagecode is manadatory !"),
	ERR_10038(10038, "Exception while fetching Module Request Type Names!"),
	ERR_10039(10039, "Exception occured while trying to get data from dbxdb_get_mc_moduleactionname_view."),
	ERR_10040(10040, "Exception occured while trying to get data from dbxdb_update_makercheckerconfig_proc."),
	ERR_10041(10041, "companyLegalUnit and id are manadatory fields !"),
	ERR_10042(10042, "Error occured while getting response from isApprovalRequired"),
	ERR_10043(10043, "Multi Entity Module can't have SHARED as Company Legal Unit"),
	ERR_10044(10044, "For Multi Entity Module LegalEntityId is mandatory"),
	ERR_10045(10045, "Error occurred while withdrawing the request"),
	ERR_10046(10046, "Action not allowed!!"),
	ERR_10047(10047, "isDetailedViewReq is a manadatory field"),
	ERR_10048(10048, "Error occurred while viewing the customer details"),
	ERR_10049(10049, "Error occurred while fetching data for enroll customer view details"),
	ERR_10050(10050, "Exception occured while parsing enroll customer view details data"),
	ERR_10051(10051, "Exception occured while trying to update data in dbxdb_approvalrequests_update"),
	ERR_10052(10052, "Error occurred while fetching data for edit customer view details"),
	ERR_10053(10053, "Error occurred while viewing the contract details"),
	ERR_10054(10054, "Exception occured while trying to fetch contract details."),
	ERR_10055(10055, "Exception occured while trying to fetch contract features actions and limits."),
	ERR_10056(10056, "Error occurred while fetching data for create contract view details"),
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
	
	public JsonObject setErrorCode(JsonObject json) {
		if(json == null) {
			json = new JsonObject();
		}
		json.addProperty(ERROR_CODE_KEY, this.getErrorCodeAsString());		
		json.addProperty(ERROR_MESSAGE_KEY, this.getMessage());
		return json;
	}
	
	

}
