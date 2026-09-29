package com.temenos.infinity.api.arrangements.constants;

public interface UserAccountSettingConstants {
    String PARAM_CUSTOMER_ID = "customer_id";
	String PARAM_PROPERTY = "OMSUserManagement";
    String PARAM_ORDER_ID = "orderId";
    String PARAM_ORDER_STATUS = "status";
    String PARAM_ERROR_MESSAGE="errorMessage";
    
    String PARAM_SOURCE = "UserManagement";
       
    String ACCOUNT_ID = "accountId";
    String ACCOUNTID = "accountID";
    
    String BLOCKED_FUNDS_LOCK_REASON = "lockReason";
	String BLOCKED_FUNDS_TRANSACTION_REFERENCE = "transactionReference";
	String BLOCKED_FUNDS_LOCKED_EVENT_ID= "lockedEventId";
	String BLOCKED_FUNDS_EVENT_ID= "eventId";
	String BLOCKED_FUNDS_DATE_RANGE = "fromDate";
    String SEARCH_START_DATE_PARAM = "searchStartDate";
    String SEARCH_END_DATE_PARAM = "searchEndDate";
	String PARAM_FILTER = "filter";
	
	String TRANSACTION = "Transactions";
	
	String ERR_EMPTY_RESPONSE = "No records found in the response";
	String PARAM_PAGESIZE = "pageSize";
	String PARAM_TOTALSIZE = "totalSize";
	String PARAM_PAGESTART = "pageStart";
	String PARAM_FROM_DATE = "fromDate";
	String PARAM_TO_DATE = "toDate";
	String PARAM_TOTAL_SIZE_PENDING = "totalSizeOfPending";
	String PARAM_PAGE_SIZE_PENDING = "pageSizeOfPending";
}
