package com.infinity.dbx.temenos.stoppayments;

public interface StopPaymentConstants {
    String PARAM_TRANSACTIONTYPE_ONLINE = "ONLINE";
    String PARAM_TRANSACTIONTYPE_PHONE = "PHONE";
    String PARAM_TRANSACTIONTYPE_BANK = "BANK";
    String PARAM_TRANSACTIONTYPE_MAIL = "MAIL";
    String PARAM_TRANSACTIONTYPE_BATCH_FEED = "BATCH FEED";
    String PARAM_TRANSACTIONTYPE_LETTER = "LETTER";
    String PARAM_TRANSACTIONTYPE_BRANCH = "BRANCH";
    String PARAM_CHECKREASON_DUPLICATE = "Duplicate";
    String PARAM_CHECKREASON_INSUFFICIENT_FUNDS = "Insufficient Account Balance";
    String PARAM_CHECKREASON_LOST_OR_STOLEN = "Lost or stolen Check";
    String PARAM_CHECKREASON_DEFECTIVE_GOODS = "Defective goods";
    String PARAM_CHECKREASON_OTHERS = "Others";
    String PARAM_T24CHECKREASON_DUPLICATE = "CHKSTO";
    String PARAM_T24CHECKREASON_INSUFFICIENT_FUNDS = "CUSPROT";
    String PARAM_T24CHECKREASON_LOST_OR_STOLEN = "CHKLOST";
    String PARAM_T24CHECKREASON_DEFECTIVE_GOODS = "CHKDES";
    String PARAM_T24CHECKREASON_OTHERS = "TERM";
    String PARAM_T24CHECKREASON_ACCOUNT_ID = "accountID";
    String PARAM_CHEQUE_STATUS = "chequeStatus";
    String PARAM_CHEQUE_ISSUE_ID = "chequeIssueId";
    String EXTERNAL_ID = "externalId";
    String PARAM_CHEQUE_ISSUE_VALIDATE = "validate";
    String PARAM_VALIDATE_ONLY = "validate_only";
    String PARAM_PAGE_SIZE = "page_size";
    String PARAM_LIMIT = "limit";

    String PARAM_CHEQUE_NUMBER = "checkNumber1";
    String PARAM_STOP_CONDITION_ID = "transactionStopConditionId";

    String PARAM_STOP_AMOUNT = "StopbyAmount";
    String PARAM_STOP_CHEQUE_NUMBER = "StopbyCheckNumber";
    String PARAM_STOP_CHEQUE_AMOUNT = "StopbyCheckNumberandAmount";

    // Stop Payment Module Definition
    String STOP_PAYMENT_MODULE = "STOP_PAYMENT_MODULE";
    String TRANSACTION_STOP_MODULE = "TZ.TRANSACTION.STOP.INSTRUCTION";
    String PAYMENT_STOP_MODULE = "PAYMENT.STOP";
    String T24_SERVICE_NAME_STOP_PAYMENTS = "T24ISStopPayments";
    String TRANSACTION_STOP = "createStopPayments";
    String PAYMENT_STOP = "createPaymentStops";
    String GET_TRANSACTION_STOP = "getT24AllStopPayments";
    String GET_PAYMENT_STOP = "getT24AllPaymentStops";
    String PARAM_PAYMENT_STOP_ACCOUNT = "fromAccountNumber";
    String PARAM_FILE_VERSION = "fileVersions";
    String PARAM_FILE_VERSION_VALUE = "HIS LIV";
    String PARAM_AMOUNT = "amount";
    String PARAM_TRANSACTION_TYPE = "transactionType";
    String REVOKE_PAYMENT_STOP = "revokePaymentStops";

    String PARAM_T24STOPREASON_DUPLICATE = "23";
    String PARAM_T24STOPREASON_INSUFFICIENT_FUNDS = "21";
    String PARAM_T24STOPREASON_LOST = "1";
    String PARAM_T24STOPREASON_STOLEN = "2";
    String PARAM_T24STOPREASON_DEFECTIVE_GOODS = "3";
    String PARAM_T24STOPREASON_OTHERS = "23";

    // Param for Cheque Supplements
    String PARAM_CHEQUE_SUPPLEMENT_ACCOUNT_ID = "accountId";
    String PARAM_CHEQUE_SUPPLEMENT_CHEQUE_NUMBER = "chequeNumber";
    String PARAM_CHEQUE_SUPPLEMENT_CHEQUE_RANGE = "chequeRange";
    String PARAM_CHEQUE_SUPPLEMENT_PAYEE_NAME = "payeeName";
    String PARAM_CHEQUE_SUPPLEMENT_STATUS = "status";
    String PARAM_CHEQUE_SUPPLEMENT_AMOUNT = "amount";
    String PARAM_CHEQUE_SUPPLEMENT_AMOUNT_RANGE = "amountRange";
    String PARAM_CHEQUE_SUPPLEMENT_TYPE = "chequeTypeId";
    String PARAM_CHEQUE_SUPPLEMENT_FILTER = "filter";
    // Constants for request params
    String STOPCHECKPAYMENT_REQ = "StopCheckPaymentRequest";
    String CHECK_REASON = "checkReason";
    String CHEQUE_STATUS = "70";
    String STATUS_FAILED = "failed";
    
    //Stop Cheque Payments Constants
    String PARAM_BODY = "body";
    String PARAM_ERROR_MESSAGE = "errmsg";
    String PARAM_STOP_STATUS = "transactionStopStatus";
    String PARAM_CREATE_DATE = "createDate";
    String PARAM_STOP_INSRUCTION = "stopInstructionId";
    String PARAM_STOP_REASON = "stopReason";
    String PARAM_EXPIRY_DATE = "expiryDate";
    String PARAM_NICK_NAME = "fromAccountNickName";
    String PARAM_MULTI_GROUP = "multigroup";
    String PARAM_ATTRIBUTE_NAME = "attributeName"; 
    String PARAM_ATTRIBUTE_VALUES = "attributeValues";
    String PARAM_ATTRIBUTE_VALUE = "attributeValue";
    String PARAM_STOP_PAYMENT_AMOUNT = "AMOUNT";
    String PARAM_PAYMENT_STOP_CHEQUE_NUMBER ="CHECKNUMBER";
    
    String PARAM_REQUEST_TYPE = "requestType";
    String PARAM_REQUEST_TYPE_SERIES = "Series";
    String PARAM_REQUEST_TYPE_SINGLE = "Single";
    String PARAM_ID = "Id";
    String PARAM_REQUEST_VALIDITY = "requestValidity";
    String PARAM_CHEQUE_TRANSACTION_DATE = "transactionDate";
    String PARAM_CHEQUE_NUMBER_TWO = "checkNumber2";
    String PARAM_STATUS_DESCRIPTION = "statusDesc";
    String PARAM_CHEQUE_FEE = "fee";
    String PARAM_CHECK_DATE_OF_ISSUE = "checkDateOfIssue";
    String PARAM_TRANSACTION_NOTES = "transactionsNotes";
    String PARAM_REMARKS = "remarks";
    String PARAM_REMARK = "remark";
    String PARAM_PAYMENT_STOP_REASON = "paymentStopResason";
    String PARAM_CUSTOMER_ID = "customerId";
    String PARAM_CHEQUE_DATE = "chequeDate";
    String PARAM_CHARGE_AMOUNTS = "chargeAmounts";
    String PARAM_CHARGE_AMOUNT = "chargeAmount";
    String PARAM_AMOUNT_FROM = "amountFrom";
    String PARAM_STOPS = "stops";
    String PARAM_FIRST_CHEQUE = "firstChequeId";
    String PARAM_LAST_CHEQUE = "lastChequeId";
    String PARAM_STOP_DATE = "stopDate";
    String PARAM_BENEFICIARY_ID = "beneficiaryId";
    String PARAM_SORY_BY = "sortBy";
    String PARAM_ORDER = "order";
    String PARAM_TRANSACTIONS_RESULT = "accountransactionview";
    
    String PARAM_ERROR = "error";
    String PARAM_TYPE = "type";
    String PARAM_ERROR_DETAILS = "errorDetails";
    String PARAM_CHARGE_DETAILS = "chargeDetails";
    String PARAM_CHARGES = "charges";
    String PARAM_ERROR_CODE = "code";
    String PARAM_ERR_CODE = "errcode";
    String PARAM_ERR_MESSAGE = "message";
    String PARAM_FEES = "fees";
    String PARAM_FEE = "fee"; 
    String PARAM_CHEQUE_BOOK_RESULT = "ChequeBookRequests";
    String PARAM_CHEQUE_SUPPLEMENTS_RESULT = "ChequeSupplements";
    String PARAM_CHEQUE_AMOUNT= "chequeAmount";
    String PARAM_CHEQUE_AMOUNT_FROM = "chequeAmountFrom";
    String PARAM_REFERENCE_NUMBER = "referenceNumber";
    String PARAM_PAYEE_NAMES = "payeeNames";
    String PARAM_PAYEE_NAME = "payeeName";
    String PARAM_DATE = "date";
    String PARAM_BENEFICIARIES = "beneficiaries";
    String PARAM_NOTES = "notes";
    String PARAM_NOTE = "note";
    
    String CHEQUE_STATUS_60 ="AUTOMATIC RE-ORDER";
    String CHEQUE_STATUS_61 ="Cheque Book Requested";
    String CHEQUE_STATUS_65 ="Cheque Book Cancelled";
    String CHEQUE_STATUS_69 ="Cheque Book Sent to Print";
    String CHEQUE_STATUS_70 ="REQUEST RECIEVED";
    String CHEQUE_STATUS_71 ="Cheque Book Extracted";
    String CHEQUE_STATUS_75 ="Cheque Book Printed";
    String CHEQUE_STATUS_80 ="SENT FOR PRINTING";
    String CHEQUE_STATUS_85 ="Cheque Book Received";
    String CHEQUE_STATUS_90 ="ISSUED";
    String CHEQUE_STATUS_FAILED ="FAILED";
    
    //Revoke Stop Cheque Payments Constants
    String PARAM_T24REVOKE_ACCOUNT_ID = "fromAccountNumber";
    String PARAM_T24REVOKE_CHEQUE_ID = "checkNumber1";
    String PARAM_T24REVOKE_REVOKE_DATE = "revokeDate";
    String PARAM_T24REVOKE_REVOKE_TYPE = "revokeChequeTypeId";
    String PARAM_T24REVOKE_IS_REVOKE = "isRevoke";
    String PARAM_T24REVOKE_AMOUNT = "amount";

}