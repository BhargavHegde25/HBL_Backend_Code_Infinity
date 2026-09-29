package com.temenos.infinity.api.QRPayments.constants;

public interface Constants {
	String DBP_ERR_MSG = "dbpErrMsg";
	String DBP_ERR_CODE = "dbpErrCode";
	String X_KONY_AUTHORIZATION = "X-Kony-Authorization";
	String X_KONY_REPORTING_PARAMS = "X-Kony-ReportingParams";
	String PARAM_TYPE = "type";
	String PARAM_SUB_TYPE = "subtype";
	String PARAM_REQUEST_BODY = "requestBody";
	String FAILED_TO_FETCH_CUSTOMER_ID = "Failed to fetch customer Id";
	String ACCOUNT_UNAUTHORIZED = "One of the Offset accounts does not belongs to the logged in user";
    String FROMACCOUNTCURRENCY_DIFFER_TRANSACTIONCURRENCY = "FromAccountCurrency and TransactionCurrency is different";
    String INTERNAL_BANK_ACCOUNTS = "INTERNAL_BANK_ACCOUNTS";
}
