package com.infinity.dbx.temenos.transactions;

public interface TransactionConstants {

    String TRANSACTION = "Transactions";
    String PARAM_STATUS_DESCRIPTION = "statusDescription";
    String PARAM_DESCRIPTION = "description";
    String TRANSFER_TO = "Transfer To ";
    String PARAM_VALUE_SUCCESSFUL = "Successful";
    String PARAM_VALUE_PENDING = "Pending";
    String PARAM_TRANSACTION_TYPE = "transactionType";
    String PARAM_PAY_TYPE = "payType";
    String PARAM_IS_SCHEDULED = "isScheduled";
    String PARAM_VALUE_ALL = "ALL";
    String PARAM_VALUE_TRANSFERS = "Transfers";
    String PARAM_VALUE_DEPOSITS = "Deposits";
    String PARAM_VALUE_WITHDRAWALS = "Withdrawals";
    String PARAM_DEBIT = "debit";
    String PARAM_CREDIT = "credit";
    String PARAM_FILE_NAMES = "fileNames";
    String PARAM_FILE_NAME = "fileName";
    String PARAM_FILE_ID = "fileID";
    String TRUE = "true";
    String FALSE_SMALL = "false";
    String FALSE = "FALSE";
    String PARAM_VALUE_BOTH = "Both";
    String SERVICE_ID_TRANSACTIONS = "T24ISTransactions";
    String SERVICE_ID_ORCH = "CommonOrchestrationService";
    String SERVICE_ID_RBLOCALSERVICESDB = "dbpRbLocalServicesdb";
    String SERVICE_ID_DOCUMENTINTEGRATIONSERVICES = "DocumentIntegrationServices";
    String OPER_ID_PAYMENTFILESGET = "dbxdb_paymentfiles_get";
    String OPER_ID_DMSSEARCH = "searchDocument";
    String OPER_ID_DEPOSITS = "getDeposits";
    String OPER_ID_WITHDRAWALS = "getWithdrawls";
    String OPER_ID_SCHEDULED = "getAccountScheduledTransactions";
    String OPER_ID_TRANSACTIONS = "getPendingandPostedTransactions";
    String OPER_ID_LOANSCHEDULE= "loanScheduleTransaction";
    String PARAM_ARRANGEMENTID = "arrangementID";
    String TRANS_AC = "AC";
    String TRANS_OT = "OT";
    String TRANS_IT = "IT";
    String TRANS_BC = "BC";
    String EXTERNAL_TRANSFER = "ExternalTransfer";
    String INTERNAL_TRANSFER = "InternalTransfer";
    String PARAM_VALUE_TO_ACCOUNT_NAME = "toAccountName";
    String TRANS_TYPE_OTHERS = "Others";
    String PARAM_OFFSET = "offset";
    String PARAM_LIMIT = "limit";
    String PARAM_FIRST_RECORD_NUMBER = "firstRecordNumber";
    String PARAM_LAST_RECORD_NUMBER = "lastRecordNumber";
    String PARAM_PAGE_START = "page_start";
    String PARAM_PAGE_START_PENDING = "pageStartOfPending";
	String PARAM_TOTAL_SIZE_PENDING = "totalSizeOfPending";
	String PARAM_PAGE_SIZE_PENDING = "pageSizeOfPending";
	String PARAM_PAGE_START_COMPLETED = "pageStartOfCompleted";
	String PARAM_TOTAL_SIZE_COMPLETED = "totalSizeOfCompleted";
	String PARAM_PAGE_SIZE_COMPLETED = "pageSizeOfCompleted";
	String PARAM_PAGESIZE = "pageSize";
	String PARAM_TOTALSIZE = "totalSize";
	String PARAM_PAGESTART = "pageStart";
    String PARAM_TRANSACTION_COUNT = "transactionCount";
    String PARAM_PAGE_START_DEF_VALUE = "0";
    String PARAM_TRANSACTION_COUNT_DEF_VALUE = "100";
    String FUTURE_LIMIT="2000";
    String FUTURE_START="1";
    String PARAM_TRANSACTION = "transaction";
    String PARAM_COMPLETED = "Completed";
    // Error Messages
    String ERR_EMPTY_RESPONSE = "No records found in the response";
    String ERR_EMPTY_REQUEST = "Empty Request Found";
    String PARAM_TO_EXTERNAL_ACCOUNT_NUMBER = "toExternalAccountNumber";
    String ACCOUNT_NUMBER = "accountNumber";
    String ACCOUNTID = "accountID";
    String TRN_TYPE_PAIDIN = "PAIDIN";
    String TRN_TYPE_PAIDOUT = "PAIDOUT";
    String PARAM_SEARCH_TYPE = "searchType";
    String PARAM_SEARCH_TYPE_VALUE = "SEARCH";

    // Transaction Properties
    String PROP_SECTION_TRANSACTIONS = "Transactions";
    String PROP_NUMBER_OF_DAYS = "NumOfDays";
    // Remove if accountId is coming
    String ACCOUNT_ID = "accountId";
    // Transaction types
    String P2P_DEBITS = "P2PDebits";
    String P2P_CREDITS = "P2PCredits";

    String TRANSACTIONS_DATE_BACKEND_FORMAT = "yyyyMMdd";
    int SCHEDULED_TRANSACTIONS_FETCH_WINDOW = 7;
    String SEARCH_START_DATE_PARAM = "searchStartDate";
    String SEARCH_END_DATE_PARAM = "searchEndDate";

    // Debits
    String PAYMENT_ORDER_DEBITS_ARRAY_KEY = "debits";

    String PAYMENT_ORDER_DEBITS_ARRAY_ACCOUNT_IBAN_KEY = "debitAccountIBAN";
    String PAYMENT_ORDER_DEBITS_ARRAY_CURRENCY_KEY = "debitCurrency";
    String PAYMENT_ORDER_DEBITS_ARRAY_ACCOUNT_ID_KEY = "debitAccountId";
    String PAYMENT_ORDER_DEBITS_ARRAY_ACCOUNT_NAME_KEY = "accountName";

    String FROM_ACCOUNT_IBAN_KEY = "fromAccountIBAN";
    String FROM_ACCOUNT_CURRENCY_KEY = "fromAccountCurrency";
    String FROM_ACCOUNT_NUMBER_KEY = "fromAccountNumber";
    String FROM_ACCOUNT_NAME_KEY = "fromAccountName";

    // Credits
    String PAYMENT_ORDER_CREDITS_ARRAY_KEY = "credits";

    String PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_IBAN_KEY = "creditAccountIBAN";
    String PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_ID_KEY = "creditAccountId";
    String PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_NAME_KEY = "creditAccountName";

    String ACCOUNT_SWIFTCODE_KEY = "swiftCode";

    String TO_ACCOUNT_IBAN_KEY = "toAccountIBAN";
    String TO_ACCOUNT_NAME_KEY = "toAccountName";
    String TO_ACCOUNT_NUMBER_KEY = "toAccountNumber";
    String EXTERNAL_ACCOUNT_NUMBER_KEY = "ExternalAccountNumber";

    // Beneficiaries
    String PAYMENT_ORDER_BENEFICIARIES_ARRAY_KEY = "beneficiaries";

    String BENEFICIARY_BIC_KEY = "beneficiaryBIC";
    String BENEFICIARY_NAME_KEY = "beneficiaryName";
    String BENEFICIARY_ACCOUNT_ID_KEY = "beneficiaryAccountId";
    String BENEFICIARY_IBAN_KEY = "beneficiaryIBAN";

    // Narratives
    String PAYMENT_ORDER_NARRATIVES_ARRAY_KEY = "narratives";

    String NARRATIVE_KEY = "narrative";
    String DESCRIPTION_KEY = "description";

    String FREQUENCY_TYPE_KEY = "frequencyType";

    String EXECUTION_DATE_KEY = "executionDate";
    String SCHEDULED_DATE_KEY = "scheduledDate";
    String FREQUENCY_START_DATE_KEY = "frequencyStartDate";

    String PAYMENT_ORDER_ID_KEY = "paymentOrderId";
    String TRANSACTION_ID_KEY = "transactionId";
    String COMPANY_ID="companyId";

    String ACCOUNT_WITH_BANK_BIC = "accountWithBankBIC";
    String BIC_ID_KEY = "BICId";
    
    String PAYMENT_ORDER_PRODUCT_ID_KEY = "paymentOrderProductId";
    
    String STANDING_ORDER_PRODUCT_NAME_KEY = "paymentOrderProductName";
    String TRANSACTION_TYPE_KEY = "transactionType";

    // Fixed Values
    String FREQUENCY_TYPE_DEFAULT_VALUE = "Once";

    String ACTRF_PRODUCT_ID = "ACTRF";  
    String DOMESTIC_PRODUCT_ID = "DOMESTIC";
    String DOMESTIC_PRODUCT_ID_ACOTHER = "ACOTHER";
    String INATIONAL_PRODUCT_ID = "INATIONAL";
    String INSTA_PAY_PRODUCT_ID = "INSTPAY";
    String SEPA_PRODUCT_ID = "SEPA";
    String PAYMENT_TYPE = "paymentType";
    String SHA_PAID_BY = "SHA";
    String BEN_PAID_BY = "BEN";
    String OUR_PAID_BY = "OUR";
    String PAID_BY_BOTH = "Both";
    String PAID_BY_SELF = "Self";
    String PAID_BY_BENEFICIARY = "Beneficiary";
    String CHARGE_BEARER = "chargeBearer";
	String PAID_BY = "paidBy";

    String EXTERNAL_TRANSFER_PRODUCT_ID = "ExternalTransfer";
    String INTERNAL_TRANSFER_PRODUCT_ID = "InternalTransfer";

    String TRANSACTION_DATE_FORMAT = "yyyy-MM-dd";
	String PARAM_COUNTER_PARTY_ID = "counterPartyAccountId";
	String TRANSACTION_CURRECNY = "transactionCurrency";
	String CURRENCY_ID = "paymentCurrencyId";
	String CURRENCY_ID_KEY = "currencyId";
    String PARAM_LIST_TYPE_INDIVIDUAL = "INDIVIDUAL";
    String PARAM_TRANSACTION_PERMISSION = "transactionPermission";
    String PARAM_ADMIN = "ADMIN";
	  
	String BLOCKED_FUNDS_LOCK_REASON = "lockReason";
	String BLOCKED_FUNDS_TRANSACTION_REFERENCE = "transactionReference";
	String BLOCKED_FUNDS_LOCKED_EVENT_ID= "lockedEventId";
	String BLOCKED_FUNDS_EVENT_ID= "eventId";
	String BLOCKED_FUNDS_DATE_RANGE = "fromDate";
	String PARAM_FILTER = "filter";
	String PARAM_ERROR_MESSAGE = "errmsg";
	String SERVICE_NAME = "serviceName";
	String INATIONAL_PRODUCT_SERVICE_NAME = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE";
	String INSTA_PAY_PRODUCT_SERVICE_NAME = "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE";
	String DOMESTIC_PRODUCT_SERVICE_NAME = "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE"; 
	String ACOTHER_PRODUCT_SERVICE_NAME = "INTRA_BANK_FUND_TRANSFER_CREATE";
	String ACTRF_PRODUCT_SERVICE_NAME = "TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE";
	String OVERRIDES  = "overrides";
	String OVERRIDE_LIST  = "overrideList";
	String CHARGES  = "charges";
	String CHARGE_TYPE  = "chargeType";
	String CHARGE_AMOUNT  = "chargeAmount";
	String CHARGES_NAME  = "chargeName";
	String CHARGES_CURRENCY  = "chargeCurrency";
	String CHARGE_ACCOUNT_CURRENCY_AMOUNT  = "chargeAccountCurrencyAmount";
	String ID = "id";
	String MESSAGE = "message";
	String DEBITACCID = "debitAccountId";
	String ACCID = "accountId";
	String USERID = "USER_ID";
	String CHARGES_ARRAY  = "chargesArray";
	String PARAM_FROM_DATE = "fromDate";
	String PARAM_TO_DATE = "toDate";
	String PARAM_EXECUTION_DATE = "paymentExecutionDateLE";
	String PARAM_DEBIT_ACCOUNT_ID = "debitAccountId";
	String PARAM_CURRENT_PAYMENT_STATE = "currentPaymentState";
	String PARAM_CURRENT_PAYMENT_STATE_VALUE = "Complete";
	String CONSTANT_ACCOUNTS = "Accounts";
	String PARAM_USER_ID = "user_id";
	String SERVICE_ID_ATTACHMENTS = "DocumentIntegrationServices";
	String DOCUMENT_STORAGE_SEARCH = "searchDocument";
	
	String PAYMENT_STATUS = "paymentStatus";
	String CURRENT_STATUS = "currentStatus";
	String DEFAULT = "default";
	String ACTIVE = "Active";
	String EXPIRED = "Expired";
	String STATUS_DESCRIPTION = "statusDescription";
	String TRANSACTION_PERIOD ="transactionPeriod";
	
	String PAYMENT_ORDER_CHARGES_ARRAY_KEY = "charges";
	String PAYMENT_ORDER_CHARGE_AMOUNT_KEY = "chargeAmount";
	String PAYMENT_ORDER_TOTAL_DEBIT_KEY = "totalDebitAmount";
	
	String DBP_BUNDLE = "DBP_CONFIG_BUNDLE";
	String DBP_CONFIG_KEY = "PAYMENT_STATUS";
	String DBP_CONFIG_TABLE_VALUE = "config_value";

	String DBP_OVERRIDE_CONFIG_KEY = "OVERRIDE_VALUES";
	
	String RECORD_STATUS = "recordStatus";
	String PENDING_APPROVAL = "pendingApproval";
	
	String INAU = "INAU";
	String RNAU = "RNAU";
	String IHLD = "IHLD";
	
	String PAYMENT_ORDER_REMITTANCE_ARRAY_KEY = "remittanceInformations";
    String REMITTANCE_INFO_KEY = "remittanceInformation";
    
    String PARAM_DISPLAY_NAMES = "displayNames";
    String PARAM_DISPLAY_NAME = "displayName";
    String PARAM_NOTES = "notes";
	String PAYMENT_ID = "Id";
	
	String CREDIT_VALUE_DATE = "creditValueDate";
	String MESSAGE_DETAILS = "messageDetails";
	String EXTERNAL_ID_KEY = "externalId"; 
	String PARAM_FREQ_END_DATE = "frequencyEndDate";
	String UNTIL_I_CANCEL = "Until I Cancel";

}