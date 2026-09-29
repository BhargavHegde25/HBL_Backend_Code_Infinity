package com.temenos.infinity.api.accountsweeps.constants;

/**
 * @author naveen.yerra
 */
public interface Constants {

    String INTERNAL_SERVICE_ERROR = "Internal Service Error";
    String ERROR_FETCHING_SWEEP_FROM_BACKEND = "Error occurred while fetching sweep from backend";
    String ACCOUNT_ID_NULL = "Account Id cannot be empty";
    String SECURITY_EXCEPTION = "Security Exception, Unauthorized Access";
    String FAILED_TO_FETCH_CUSTOMER_ID = "Failed to fetch customer Id";
    String FAILED_TO_FETCH_ACCOUNTS = "Failed to fetch accounts";
    String SERVICE_REQUESTS = "serviceReqs";
    String SERVICE_REQUEST_IN = "serviceReqRequestIn";
    String SERVICE_REQ_ID = "serviceReqId";
    String ACCOUNT_ID = "accountId";
    String REQUEST_BODY = "requestBody";
    String ORDER_ID = "orderId";
    String SERVICE_REQ_STATUS = "serviceReqStatus";
    String X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    String X_KONY_REPORTING_PARAMS = "X-Kony-ReportingParams";
    String TYPE = "type";
    String SUBTYPE = "subtype";
    String ACCOUNT_SWEEP = "AccountSweep";
    String INTERNAL_BANK_ACCOUNTS = "INTERNAL_BANK_ACCOUNTS";
    String DBP_ERR_MSG = "dbpErrMsg";
    String DBP_ERR_CODE = "dbpErrCode";
    String ERROR_CREATE_SWEEP_AT_BACKEND = "Error while creating sweep at backend";
    String ACCOUNT_UNAUTHORIZED = "SECURITY EXCEPTION-UNAUTHORISED ACCESS";
    String INVALID_INPUT = "Input validation failed";
    String INVALID_AMOUNT_VALUE="Amount value is either invalid or negative";
    String MANDATORY_FIELD_MISSING = "Mandatory fields are missing from payload";
    String INVALID_DATE="StartDate or Endate is Invalid";
    String ACCOUNT_SWEEP_VIEW="ACCOUNT_SWEEP_VIEW";
    String ACCOUNT_SWEEP_CREATE="ACCOUNT_SWEEP_CREATE";
    String ACCOUNT_SWEEP_EDIT="ACCOUNT_SWEEP_EDIT";
    String ACCOUNT_SWEEP_DELETE="ACCOUNT_SWEEP_DELETE";

    String ACCOUNT_UNAUTHORIZED_FOR_FEATUREACTION="AccountId is unauthorized for particular feature action";

    String UNAUTHORIZED_CURRENCYCODE_ACCOUNTSTATUS="Either Currency code of both accounts not matching or Account Status of both account is not Active or Closure Pending";
}
