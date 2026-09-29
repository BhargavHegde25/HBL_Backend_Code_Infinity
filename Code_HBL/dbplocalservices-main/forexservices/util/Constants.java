package com.kony.dbx.util;

public interface Constants {

	// DBX Properties File
	public static final String PROPERTIES_FILE = "KonyDBX.properties";

	// Configurable params constants
	public static final String ENCRYPTED_KEY = "TEMPLATE_ENCRYPTION_KEY";

	// Boolean constants
	public static final String FALSE = "false";
	public static final String TRUE = "true";

	// Date format constants
	public static final String DATE_FULL = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String DATE_YYYYMMDD = "yyyy-MM-dd";
	public static final String DBX_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT = "HHmm";
	public static final String TIME_FULL = "hh:mm aa";
	public static final String TIME_OSI = "T00:00:00Z";

	// KonyFabric Status Codes
	public static final String PARAM_HTTP_STATUS_OK = "200";
	public static final String PARAM_OP_STATUS_ERROR = "5000";
	public static final String PARAM_OP_STATUS_OK = "0";

	public static final String HTTP_HDR_X_KONY_AUTH = "X-Kony-Authorization";

	public static final String PARAM_LOOP_COUNT = "loop_count";
	public static final String PARAM_CAMEL_CASE_LOOP_COUNT = "loopCount";
	public static final String PARAM_LOOP_DATASET = "LoopDataset";

	// Generic parameters
	public static final String PARAM_ERR_MSG = "errmsg";
	public static final String PARAM_ERR_CODE = "errcode";
	public static final String PARAM_FAULT_CODE = "faultcode";
	public static final String PARAM_HTTP_STATUS_CODE = "httpStatusCode";
	public static final String PARAM_OP_STATUS = "opstatus";
	public static final String PARAM_SUCCESS = "success";
	public static final String DBP_HOST_URL = "DBP_HOST_URL";

	// Parameter Data Types
	public static final String PARAM_DATATYPE_INT = "int";
	public static final String PARAM_DATATYPE_STRING = "string";

	// Constants for days of the week
	public static final String DOW_MONDAY = "Monday";
	public static final String DOW_TUESDAY = "Tuesday";
	public static final String DOW_WEDNESDAY = "Wednesday";
	public static final String DOW_THURSDAY = "Thursday";
	public static final String DOW_FRIDAY = "Friday";
	public static final String DOW_SATURDAY = "Saturday";
	public static final String DOW_SUNDAY = "Sunday";
	public static final String LAST_DAYOFMONTH = "28";
	public static final String TOTAL_MONTHS_IN_A_YEAR = "12";
	public static final String JANUARY = "01";

	// DBX Account Types
	public static final String DBX_ACCOUNT_TYPE_1 = "1";
	public static final String DBX_ACCOUNT_TYPE_1_DESC = "Checking";
	public static final String DBX_ACCOUNT_TYPE_2 = "2";
	public static final String DBX_ACCOUNT_TYPE_2_DESC = "Savings";
	public static final String DBX_ACCOUNT_TYPE_3 = "3";
	public static final String DBX_ACCOUNT_TYPE_3_DESC = "CreditCard";
	public static final String DBX_ACCOUNT_TYPE_4 = "4";
	public static final String DBX_ACCOUNT_TYPE_4_DESC = "Deposit";
	public static final String DBX_ACCOUNT_TYPE_5 = "5";
	public static final String DBX_ACCOUNT_TYPE_5_DESC = "Mortgage";
	public static final String ACCOUNT_TYPE_SPROUT = "Sprout";

	// DBX General Parameters
	public static final String PARAM_ACCOUNT_HOLDER = "accountHolder";
	public static final String PARAM_ACCOUNT_NAME = "accountName";
	public static final String PARAM_ACCOUNT_TYPE = "accountType";
	public static final String PARAM_ACCOUNT_ID ="accountId";
	public static final String PARAM_AMOUNT = "amount";
	public static final String PARAM_BANK_NAME = "bankName";
	public static final String PARAM_DOLLAR_FILTER = "$filter";
	public static final String PARAM_DOLLAR_ORDERBY = "$orderby";
	public static final String PARAM_DOLLAR_SELECT = "$select";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_FREQUENCY_TYPE = "frequencyType";
	public static final String PARAM_FREQUENCY_END_DATE = "frequencyEndDate";
	public static final String PARAM_DAY1 = "day1";
	public static final String PARAM_DAY2 = "day2";
	public static final String PARAM_STATUS_DESCRIPTION = "statusDescription";
	public static final String PARAM_FROM_ACCOUNT_NAME = "fromAccountName";
	public static final String PARAM_FROM_ACCOUNT_NUMBER = "fromAccountNumber";
	public static final String PARAM_FROM_ACCOUNT_TYPE = "fromAccountType";
	public static final String PARAM_FROM_CHECK_NUMBER = "fromCheckNumber";
	public static final String PARAM_EXPIRATION_CHECK = "ExpirationCheck";
	public static final String PARAM_FREQUENCY_ENDDATE = "frequencyEndDate";
	public static final String PARAM_ID = "id";
	public static final String PARAM_INTERNATIONAL_BANK_ACCOUNT = "isInternationalAccount";
	public static final String PARAM_PAYEE_ID = "payeeId";
	public static final String PARAM_PAYEE_NAME = "payeeName";
	public static final String PARAM_PAYEE_NICK_NAME = "payeeNickName";
	public static final String PARAM_PHONE = "phone";
	public static final String PARAM_SAME_BANK_ACCOUNT = "isSameBankAccount";
	public static final String PARAM_SCHEDULED = "isScheduled";
	public static final String PARAM_SCHEDULED_DATE = "scheduledDate";
	public static final String PARAM_SEARCH_END_DATE = "searchEndDate";
	public static final String PARAM_SEARCH_MINIMUM_AMOUNT = "searchMinAmount";
	public static final String PARAM_SEARCH_MIXIMUM_AMOUNT = "searchMaxAmount";
	public static final String PARAM_SEARCH_START_DATE = "searchStartDate";
	public static final String PARAM_SEARCH_TRANSCTION_TYPE = "searchTransactionType";
	public static final String PARAM_TO_ACCOUNT_NAME = "toAccountName";
	public static final String PARAM_EXTERNAL_ACCOUNT_NUMBER = "ExternalAccountNumber";
	public static final String PARAM_TO_ACCOUNT_NUMBER = "toAccountNumber";
	public static final String PARAM_TO_ACCOUNT_TYPE = "toAccountType";
	public static final String PARAM_TO_CHECK_NUMBER = "toCheckNumber";
	public static final String PARAM_TRANSACTION_ID = "transactionId";
	public static final String PARAM_TRANSACTION_NOTES = "transactionsNotes";
	public static final String PARAM_TRANSACTION_TYPE = "transactionType";
	public static final String PARAM_TRANSFER_DESTINATION_ACCOUNT = "supportTransferTo";
	public static final String PARAM_TRANSFER_SOURCE_ACCOUNT = "supportTransferFrom";
	public static final String PARAM_DEPOSIT_DESTINATION_ACCOUNT = "supportDeposit";
	public static final String PARAM_USER_ID = "user_id";
	public static final String PARAM_SUPPORT_CHECKS = "supportChecks";
	public static final String BACKEND_ID = "BackendId";
	public static final String SEQUENCE_NUMBER = "sequence_number";
	public static final String IDENTIFIER_NAME = "identifier_name";
	public static final String PARAM_CUSTOMER_ID = "customerId";
	public static final String PARAM_SUPPORT_BILLPAY = "supportBillPay"; 
	public static final String COMPANY_ID = "CompanyId";

	// DBX User Parameters
	public static final String KONY_DBX_USER_ADDR1 = "AddressLine1";
	public static final String KONY_DBX_USER_ADDR2 = "AddressLine2";
	public static final String KONY_DBX_USER_CITY = "CityName";
	public static final String KONY_DBX_USER_DEFAULT_BILLPAY_ACCT = "default_account_billPay";
	public static final String KONY_DBX_USER_DEFAULT_CARDLESS_ACCT = "default_account_cardless";
	public static final String KONY_DBX_USER_DEFAULT_DEPOSIT_ACCT = "default_account_deposit";
	public static final String KONY_DBX_USER_DEFAULT_P2P_FROMACCT = "default_from_account_p2p";
	public static final String KONY_DBX_USER_DEFAULT_P2P_TOACCT = "default_to_account_p2p";
	public static final String KONY_DBX_USER_DEFAULT_PAYMENTS_ACCT = "default_account_payments";
	public static final String KONY_DBX_USER_DEFAULT_XFER_ACCT = "default_account_transfers";
	public static final String KONY_DBX_USER_DOB = "dateOfBirth";
	public static final String KONY_DBX_USER_EMAIL = "email";
	public static final String KONY_DBX_USER_FIRSTNAME = "userFirstName";
	public static final String KONY_DBX_USER_ID = "Id";
	public static final String KONY_DBX_USER_LOWERCASE_ID = "id";
	public static final String KONY_DBX_USER_id = "id";
	public static final String KONY_DBX_USER_ISBILLPAY_ACTIVE = "isBillPayActivated";
	public static final String KONY_DBX_USER_ISBILLPAY_SUPPORTED = "isBillPaySupported";
	public static final String KONY_DBX_USER_ISP2P_ACTIVE = "isP2PActivated";
	public static final String KONY_DBX_USER_ISP2P_SUPPORTED = "isP2PSupported";
	public static final String KONY_DBX_USER_LASTNAME = "userLastName";
	public static final String KONY_DBX_USER_LASTLOGINTIME = "lastlogintime";
	public static final String KONY_DBX_USER_NAME = "userName";
	public static final String KONY_DBX_USER_PHONE = "phone";
	public static final String KONY_DBX_USER_ROLE = "role";
	public static final String KONY_DBX_USER_SSN = "ssn";
	public static final String KONY_DBX_USER_STATE = "RegionName";
	public static final String KONY_DBX_USER_ZIP = "zipcode";

	// DBX Account Parameters
	public static final String KONY_DBX_ACCOUNT_ID = "Account_id";
	public static final String KONY_DBX_ACCOUNT_NAME = "AccountName";
	public static final String KONY_DBX_ACCOUNT_NICK_NAME = "NickName";
	public static final String KONY_DBX_ACCOUNT_STATUS_CODE = "Status_id";
	public static final String KONY_DBX_ACCOUNT_STATUS_DESC = "StatusDesc";
	public static final String KONY_DBX_ACCOUNT_TYPE = "Type_id";
	public static final String KONY_DBX_ACCOUNT_CURRENCY_CODE = "CurrencyCode";
	public static final String KONY_DBX_ACCOUNT_AVAIL_BAL = "AvailableBalance";
	public static final String KONY_DBX_ACCOUNT_CURRENT_BAL = "CurrentBalance";
	public static final String KONY_DBX_ACCOUNT_OUTSTANDING_BAL = "OutstandingBalance";

	// DBX Payment Frequencies
	public static final String PAYMENT_ONCE = "Once";
	public static final String PAYMENT_DAILY = "Daily";
	public static final String PAYMENT_WEEKLY = "Weekly";
	public static final String PAYMENT_EVERY_TWO_WEEKS = "Every Two Weeks";
	public static final String PAYMENT_BI_WEEKLY = "BiWeekly";
	public static final String PAYMENT_EVERY_FOUR_WEEKS = "Every Four Weeks";
	public static final String PAYMENT_MONTHLY = "Monthly";
	public static final String PAYMENT_TWICE_MONTHLY = "Twice Monthly";
	public static final String PAYMENT_EVERY_OTHER_MONTHLY = "Every Other Month";
	public static final String PAYMENT_QUARTERLY = "Quarterly";
	public static final String PAYMENT_HALF_YEARLY = "Half Yearly";
	public static final String PAYMENT_YEARLY = "Yearly";

	public static final String FREQUENCY_ONCE = "Once";
	public static final String FREQUENCY_RECURRING = "Recurring";
	public static final String FREQUENCY_DAILY = "Daily";
	public static final String FREQUENCY_WEEKLY = "Weekly";
	public static final String FREQUENCY_EVERY_TWO_WEEKS = "Every Two Weeks";
	public static final String FREQUENCY_MONTHLY = "Monthly";
	public static final String FREQUENCY_QUARTERLY = "Quarterly";
	public static final String FREQUENCY_HALF_YEARLY = "Half Yearly";
	public static final String FREQUENCY_YEARLY = "Yearly";
	public static final String FREQUENCY_SEMIMONTHLY = "Semimonthy";
	public static final String FREQUENCY_BIMONTHLY = "Bimonthy";

	// DBX Request Query String Parameters
	public static final String QUERY_PARAM_LIMIT = "limit";
	public static final String QUERY_PARAM_ORDER = "order";
	public static final String QUERY_PARAM_OFFSET = "offset";
	public static final String QUERY_PARAM_SORTBY = "sortBy";

	// ODATA Condition constants
	public static final String EQUAL = " eq ";
	public static final String NOT_EQUAL = " ne ";
	public static final String AND = " and ";
	public static final String OR = " or ";
	public static final String GREATER_THAN_OR_EQUAL = " ge ";
	public static final String LESS_THAN_OR_EQUAL = " le ";
	public static final String LESS_THAN = " lt ";
	public static final String GREATER_THAN = " gt ";

	// Regex constants
	public static final String VALID_EMAIL_ADDRESS_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

	// DBX Account Categories
	public static final String ACCOUNT_TYPE_CHECKING = "Checking";
	public static final String ACCOUNT_TYPE_SAVINGS = "Savings";
	public static final String ACCOUNT_TYPE_CREDIT_CARD = "CreditCard";
	public static final String SESSION_ATTRIB_ACCOUNT = "Accounts";
	public static final String SESSION_ATTRIB_MICR_ACCOUNT = "MICRAccounts";

	public static final String ACCOUNT_TYPE_DEPOSIT = "Deposit";
	public static final String ACCOUNT_TYPE_MORTGAGE = "Mortgage";
	public static final String ACCOUNT_TYPE_LOAN = "Loan";

	// DBX Transaction Type
	public static final String TRANSCTION_TYPE_DEPOSIT = "Deposit";
	public static final String TRANSCTION_TYPE_DEPOSITS = "Deposits";
	public static final String TRANSCTION_TYPE_INTERNAL_TRANSFER = "InternalTransfer";
	public static final String TRANSCTION_TYPE_EXTERNAL_TRANSFER = "ExternalTransfer";
	public static final String TRANSCTION_TYPE_WITHDRAWAL = "Withdrawal";
	public static final String TRANSCTION_TYPE_wITHDRAWAL = "withdrawal";
	public static final String TRANSCTION_TYPE_WITHDRAWALS = "Withdrawals";
	public static final String TRANSCTION_TYPE_wITHDRAWALS = "withdrawals";
	public static final String TRANSCTION_TYPE_CHECKS = "Checks";
	public static final String TRANSCTION_TYPE_TRANSFERS = "Transfers";
	public static final String TRANSCTION_TYPE_BILL_PAY = "BillPay";
	public static final String TRANSCTION_TYPE_P2P = "p2p";

	// Constants for Session Attribute Keys
	public static final String SESSION_ATTRIB_CUSTOMER = "customer";

	public static final String BANK_NAME = "Kony Bank";
	public static final String YES = "1";
	public static final String NO = "0";

	public static final String TEST = "test";
	String PARAM_PERSON_CENTRIC = "PERSON";
	String PARAM_ACCOUNT_CENTRIC = "ACCOUNT";

	String PARAM_DBX_DB_NAME = "DBX_DB_NAME";
	String PARAM_DBX_DB_DRIVER = "DBX_DB_DRIVER";
	String PARAM_DBX_DB_HOST_URL = "DBX_DB_HOST_URL";
	String PARAM_DBX_DB_USERNAME = "DBX_DB_USERNAME";
	String PARAM_DBX_DB_PASSWORD = "DBX_DB_PASSWORD";

	String CONST_DRIVER_MYSQL = "mysql";
	String CONST_DRIVER_ORACLE = "oracle";

	String C_SETMAXLIFETIME = "600000";
	String C_SETMINIMUMIDLE = "1";
	String C_SETIDLETIMEOUT = "300000";
	String C_SETMAXIMUMDBPOOLSIZE = "10";

	public static final String SECTION_CONFIGURATION = "Configuration";

	public static final String PROPERTYNAME_SCHEMANAME = "SchemaName";

	public static final String HIKARI_PREFIX = "Hikari";

	public static final String DEVICE_ID = "X-Kony-DeviceId";

	public static final String USER_ID_ANONYMOUS = "anonymous";

	public static final String CUSTOMER_ID = "Customer_id";

	public static final String PARAM_SEQUENCENUMBER = "sequenceNumber";

	public static final String BACKEND_TYPE = "BackendType";
	// public static final String USER_ID_ANONYMOUS = "anonymous";

	public static final String DBX_DB_SERVICE_NAME = "dbpRbLocalServicesdb";
	public static final String DBX_DB_BACKEND_IDENTIFIER_GET = "dbxdb_backendidentifier_get";

	public static final String PARAM_BACKEND_IDENTIFIERS = "backendIdentifiers";
	public static final String DS_BACKEND_IDENTIFIER = "backendidentifier";
	public static final String PARAM_IDENTIFIER_NAME = "identifier_name";
	public static final String CONSTANT_UNIQUE_ID = "CustomerId";
	
	public static final String PARAM_CODE = "code";
	public static final String PARAM_TYPE = "type";
	public static final String CONSTANT_DELIMETER = "-";
	
	public static final String PARAM_INST_ID_REFERENCE = "instructionIdReference";
	public static final int PARAM_INST_ID_REFERENCE_LEN = 35;
	
	public static final String AWAITING_FUNDS = "AwaitingFunds";
	public static final String CREDIT_VALUE_DATE = "creditValueDate";

	public static final String CONTENT_TYPE ="contentType";

}