package com.temenos.infinity.api.arrangements.constants;

import com.dbp.core.error.DBPError;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public enum ErrorCodeEnum implements DBPError {

	ERR_11024(11024, "Invalid parameters please give valid details."), 
	ERR_11026(11026, "Record not found in DBX. "),
	ERR_20040(20040, "Failed to fetch backenId"),
	ERR_20041(20041, "Backend failed to fetch account details"),
    ERR_20042(20042, "Backend failed to fetch account balance from Holdings Micro Services"),
    ERR_20043(20043, "Backend failed to fetch account logo URL from AAG services"),
    ERR_20044(20044, "Backend failed to fetch accounts from DBX "),
    ERR_20045(20045, "Business User : Backend failed to fetch account details from T24 DB"),
    ERR_20046(20046, "Business User : Backend failed to fetch account Overview details from T24 DB"),
    ERR_20047(20047, "Invalid Customer Type"), ERR_20048(20048, "Please Provide Account ID"),
    ERR_20049(20049, "Backend failed to fetch arrangement details from Arrangement Micro Services"),
    ERR_20050(20050, "Backend failed to fetch companyId from runtime"),
    ERR_20051(20051, "Backend failed to fetch connection details from AAG services"),
    ERR_20052(20052, "Misssing input param username/CustomerId while fetching the accounts for admin"),
    ERR_20053(20053, "Customer Doesn't exist for the provided username"),
    ERR_20054(20054, "Unable to fetch the backend Id for the provided user name"),
    ERR_20055(20055, "Couldn't generate Auth Token Successfully, empty response"),
    ERR_20056(20056, "Unable to get micro service account id"),
    ERR_20057(20057, "Backend failed to fetch account details"),
    ERR_20058(20058, "Backend Failed"),
    ERR_20059(20059, "Communication ID Missing"),
	ERR_20060(20060, "DeleteAddress ID Missing"),    
	ERR_20061(20061, "Update Account Settings for %s returned %s due to %s"),
	ERR_20062(20062, "Update User Settings for %s returned %s due to %s"),
	ERR_200511(200511, "AccountId Missing"),
	ERR_200512(200512, "EStmtEnabled Missing"), 
	ERR_200513(200513, "Email Missing"),
	ERR_200514(200514, "Phone Number Missing"), 
	ERR_200515(200515, "Phone Number Identity Missing"),
	ERR_200516(200516, "Email Identity Missing"),
	ERR_200517(200517, "Invalid Email Id sent in the request"),
	ERR_200518(200518, "Unable to fetch data from T24"),
	ERR_200519(200519, "Error while invoking Javaservice"),
	ERR_200520(200520, "Create Payoff Loan simulation failed"),
	ERR_200521(200521, "Get Payoff Loan simulation results failed");
	
    private int errorCode;
    private String message;

    public static final String ERROR_CODE_KEY = "dbpErrCode";
    public static final String ERROR_MESSAGE_KEY = "dbpErrMsg";

    private ErrorCodeEnum(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }

    @Override
    public String getErrorCodeAsString() {
        return String.valueOf(errorCode);
    }
   
	public String getMessage(String... params) {
		return String.format(this.message, params);
	}
	
	public Result setErrorCode(Result result, String message) {
		if (result == null) {
			result = new Result();
		}
		result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), FabricConstants.INT));
		result.addParam(new Param(ERROR_MESSAGE_KEY, message, FabricConstants.STRING));
		return result;
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
        result.addParam(new Param(ERROR_MESSAGE_KEY, this.getErrorMessage(), FabricConstants.STRING));
        return result;
    }

}
