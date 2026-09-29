/**
 * 
 */
package com.infinity.dbx.temenos.constants;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public interface TemenosConstants extends TemenosErrorConstants {

    String TEMENOS_PROPERTIES_FILE = "Temenos.properties";
    String PARAM_DATASET = "dataset";
    String PARAM_RECORD = "record";
    String PARAM_PARAM = "param";
    String PARAM_EMAIL = "email";
    public static final String BACKEND_ID = "BackendId";
    String PARAM_PHONE = "phone";
    String PARAM_UNMASKED = "unmasked";
    String PARAM_IS_PRIMARY = "isPrimary";
    String USER_ID = "userID";
    String CONSTANT_TEMPLATE_NAME = "T24";
    String PARAM_USERNAME = "UserName";
    String PARAM_COMPANYID = "companyId";
    String PARAM_LEGALENTITYID="legalEntityId";
    String PARAM_USER_ID = "userId";
    String PARAM_ROLE_ID = "roleId";
    String PARAM_DBX_USER_ID = "dbxUserId";
    String PARAM_AUTHORIZATION = "Authorization";
    String PARAM_X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    String PARAM_BENEFICARY_NAME = "beneficaryName";
    String PARAM_IBAN = "IBAN";
    String PARAM_PAYMENT_TYPE = "paymentType";
    String PARAM_BACKEND_NAME = "BackendName";
    String PARAM_CERT_NAME = "CertName";
    String PARAM_CERT_PRIVATE_KEY = "CertPrivateKey";
    String PARAM_CERT_PUBLIC_KEY = "CertPublicKey";
    String PARAM_BACKEND_CERTIFICATE = "backendcertificate";
    String PARAM_LOGINUSERID = "loginUserId";
    String PARAM_ORGANIZATIONID = "Organization_Id";
    String KONY_DBX_ZIP_CODE = "zipcode";
    String KONY_DBX_FIRSTNAME = "FirstName";
    String KONY_DBX_DATEOFBIRTH = "DateOfBirth";
    String KONY_DBX_CITY_NAME = "addressCity";
    String KONY_DBX_LASTNAME = "LastName";
    String KONY_DBX_USER_NAME_CAMELCASE = "UserName";
    String T24_USER_ADDR1 = "addressLine1";
    String T24_USER_ADDR2 = "addressLine2";
    // String MEMBERSHIPID = "MembershipId";
    String FROM_DATE = "dateFrom";

    String EQ = "eq";
    String AND = "and";
    String $FILTER = "$filter";
    String PRIVATE_ENCRYPTION_KEY = "T24_PRIVATE_ENCRYPTION_KEY";
    // Constants for Integration service
    String SERVICE_T24IS_ACCOUNTS = "T24ISAccounts";
    String SERVICE_T24IS_TRANSFERS = "T24ISTransfers";
    String SERVICE_T24IS_ONBOARDING_ACCOUNTS = "T24ISOnboardingAccounts";
    String SERVICE_T24IS_BULK_PAYMENTS = "T24BulkPaymentAPIs";
    String SERVICE_T24IS_TRANSACTIONS = "T24ISTransactions";
    String SERVICE_T24IS_FOREX_DETAILS = "T24ISForexService";
    String SERVICE_T24IS_PAYMENTORDERS = "T24ISPaymentOrders";
    String SERVICE_T24IS_STANDINGORDERS = "T24ISStandingOrders";

    // Constants for Orchestration service
    String SERVICE_T24OS_BULK_PAYMENTS = "T24BulkPaymentsOrch";
    String SERVICE_T24OS_COMMONORCHESTRATIONSERVICE = "CommonOrchestrationService";

    // Constants for Integration operations
    String OP_ACCOUNT_DETAILS = "getAccountDetails";
    String OP_LOAN_DETAILS = "getLoanDetails";
    String OP_DEPOSIT_DETAILS = "getDepositDetails";
    String OP_CREATE_TRANSFER = "oneTimeTransfer";
    String OP_OPEN_NEW_SAVINGS_ACCOUNT = "openNewSavingsAccount";
    String OP_OPEN_NEW_CHECKING_ACCOUNT = "openNewCheckingAccount";
    String OP_OPEN_NEW_DEPOSIT_ACCOUNT = "openNewDepositAccount";
    String OP_OPEN_NEW_LOAN_ACCOUNT = "openNewLoanAccount";
    String OP_USER_GET = "getUserDetails";
    String OP_CONTACT_DETAILS_GET = "getContactDetails";
    String KONY_DBX_SERVICE_JAVA = "dbpProductServices";
    String KONY_DBX_OP_JAVA_GETUSERDETAILS_CONCURRENT = "GetUserDetailsConcurrent";
    String OP_CREATE_ACCOUNT = "createAccount";
    String OP_CREATE_PO = "createPaymentOrder";
    String OP_FETCH_PO = "fetchPaymentOrders";
    String OP_EDIT_PO = "editPaymentOrder";
    String OP_REMOVE_PO = "removePaymentOrder";
    String OP_AUTHORISE_PO = "authorisePaymentOrder";
    String OP_UPLOAD_BULK_FILE = "uploadBulkFile";
    String OP_INITIATE_BULK_PAYMENT = "initiateBulkPayment";
    String OP_CANCEL_BULK_PAYMENT = "cancelBulkPayment";
    String OP_EDIT_BULK_PAYMENT = "editBulkPayment";
    String OP_FETCH_ONGOING_BULK_PAYMENT = "fetchOnGoingPayments";
    String OP_FETCH_BULK_PAYMENT_HISTORY = "fetchBulkPaymentHistory";
    String OP_FETCH_UPLOADED_BULK_FILES = "fetchUploadedFiles";
    String OP_UPDATE_BULK_PAYMENT_STATUS = "updateBulkPaymentStatus";
    String OP_NAME_UPDATE_ESTMT_PREF = "updateEStatementPreferences";
    String OP_APPROVE_PO = "approvePaymentOrder";
    String OP_FETCH_BULKPAYMENT_RECORDDETAILS_BY_ID = "fetchBulkPaymentRecordDetailsById";
    String OP_FETCHRECORDS = "fetchRecords";
    String OP_REJECT_BULKPAYMENT_RECORD = "rejectBulkPaymentRecord";
    String OP_GET_ASSOCIATED_CUSTOMERS = "getAssociatedCustomers";
    String OP_FOREX_DETAILS = "fetchForexDetails";
    String OP_APPROVE_PAYMENT = "approvePayment";
    String OP_CREATE_PAYMENT_WITH_APPROVER = "createPaymentWithApprover";
    String OP_CREATE_PAYMENT_WITHOUT_APPROVER = "createPaymentWithoutApprover";
    String OP_REVERSE_PAYMENT_WITH_APPROVER = "reversePaymentWithApprover";
    String OP_REVERSE_PAYMENT_WITHOUT_APPROVER = "reversePaymentWithoutApprover";
    String OP_CANCEL_AWAITING_PAYMENT = "cancelAwaitingPayment";
    String OP_REJECT_PAYMENT = "rejectPayment";
    String OP_WITHDRAW_PAYMENT = "withdrawPayment";
    String OP_GET_PAYMENT_BY_ID = "fetchPaymentDetailsById";
    String OP_GET_PAYMENT_ORDER_TRANSACTION_DETAILS = "getPaymentOrderTransactionDetails";
    String OP_GET_STANDING_ORDER_TRANSACTION_DETAILS = "getStandingOrderTransactionDetails";
    String OP_CREATE_STANDINGORDER_WITHOUT_APPROVER = "createStandingOrderWithoutApprover";
    String OP_EDIT_STANDINGORDER_WITH_APPROVER = "editStandingOrderWithApprover";
    String OP_EDIT_STANDINGORDER_WITHOUT_APPROVER = "editStandingOrderWithoutApprover";
    String OP_CREATE_STANDINGORDER_WITH_APPROVER = "createStandingOrderWithApprover";
    String OP_REVERSE_STANDINGORDER_WITHOUT_APPROVER = "reverseStandingOrderWithoutApprover";
    String OP_REVERSE_STANDINGORDER_WITH_APPROVER = "reverseStandingOrderWithApprover";
    String OP_APPROVE_STANDINGORDER = "approveStandingOrder";
    String OP_REJECT_STANDINGORDER = "rejectStandingOrder";
    String OP_WITHDRAW_STANDINGORDER = "withdrawStandingOrder";
    String OP_GET_CONVERTEDRATE = "getConvertedValue";

    String OP_FETCH_CANCELLATION_REASONS = "fetchCancellationReasons";

    String OP_GET_BENEFICIARY_NAME = "getBeneficiaryName";
    String OP_GET_BIC_FROM_BANK_DETAILS = "getBICFromBankDetails";
    String OP_VALIDATE_IBAN_AND_GET_BANK_DETAILS = "validateIBANandGetBankDetails";
    String OP_GET_IBAN_DETAILS = "getIBANdetails";
    String OP_VALIDATE_IBAN = "validateIBAN";

    String OP_VALIDATE_BIC_AND_GET_BANK_NAME = "validateBICandFetchBankName";

    // Constants for session attributes
    String SESSION_ATTRIB_EXTERNAL_ACCOUNT = "ExternalAccounts";
    String SESSION_ATTRIB_DOMESTIC_ACCOUNT = "DomesticAccounts";
    String SESSION_ATTRIB_OTHERBANK_ACCOUNT = "OtherBankAccounts";
    String SESSION_ATTRIB_INTERNATIONAL_ACCOUNT = "InternationalAccounts";
    String SERVICE_BACKEND_CERTIFICATE = "dbpRbLocalServicesdb";
    String SERVICE_BACKEND_PRODUCTSERVICE = "dbpProductServices";
    String OPERATION_BACKEND_CERTIFICATE_GET = "dbxdb_backendcertificate_get";
    String OPERATION_CUSTOMER_ACCOUNTS_GET = "dbxdb_customeraccounts_get";
    String OP_ACCOUNTS_GET = "dbxdb_accounts_get";
    String OP_ACCOUNTS_UPDATE = "dbxdb_accounts_update";
    String OP_ACCOUNTS_CREATE = "dbxdb_accounts_create";
    String OP_CUSTOMERACCOUNTS_CREATE = "dbxdb_customeraccounts_create";
    String OP_CUSTOMERACCOUNTS_UPDATE = "dbxdb_customeraccounts_update";
    String OP_CUSTOMER_GET = "dbxdb_customer_get";
    String OP_STATES_GET = "dbxdb_region_get";
    String OP_COUNTRY_GET = "dbxdb_country_get";
    String OP_CITY_GET = "dbxdb_city_get";
    String OP_COMMUNICATION_GET = "dbxdb_customercommunication_get";
    String OP_CUSTOMER_ADDRESS_VIEW__GET = "dbxdb_customeraddress_view_get";
    String OP_CONTRACT_CUSTOMERS_GET = "dbxdb_contractcustomers_get";
    
    String OP_TRANSACTIONTYPEMAPPING_GET = "dbxdb_transactiontypemapping_get";
    String OP_CUSTOMERACCOUNTS_CORECUSTOMERINFO_VIEW = "dbxdb_customeraccounts_corecustomerinfo_view_get";
    String OP_ACCOUNT_DETAILS_FOR_COMBINED_STATEMENT = "getAccountByAccountIdForCombinedStatement";
    String OP_NEW_ACCOUNT_PROCESSING = "NewAccountProcessing";
    // Account Types
    String ACCOUNT_TYPE_SPROUT = "Sprout";

    // DB datasets
    String DS_ACCOUNTS = "accounts";
    String DS_CUSTOMER = "customer";
    String DS_REGION = "region";
    String DS_COUNTRY = "country";
    String DS_CITY = "city";
    String KONY_DBX_CUSTOMER_DATASET_ID = "customer";

    // Login Flows
    String FLOW_TYPE = "flowType";
    String PRE_LOGIN_FLOW = "PreLogin";
    String LOGIN_FLOW = "Login";
    String POST_LOGIN_FLOW = "PostLogin";

    // Properties
    String PROP_PREFIX_TEMENOS = "Temenos";

    String PROP_PREFIX_GENERAL = "General";
    String PROP_PREFIX_TRANSACTIONS = "Transactions";

    String PROP_PREFIX_VERSION = "Version";

    String VERSION_4_2_6 = "4.2.6";
    String VERSION_2020_04 = "2020.07";

    String T24_PAYMENT_CONTAINER = "T24_PAYMENT_CONTAINER";

    // Getting User Attributes
    String X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    String SERVICE_ID_USER = "T24ISUser";

    String T24ISTRANSACTIONS_SERVICE_ID = "T24ISTransactions";
    String GETBANKDATES_OPERATIONS_ID = "getBankDates";
    String ERR_MSG_NO_RECORDS = "No records";

    // Date patterns
    String MMDDYYYY = "MM-dd-yyyy";
    String YYYYMMDD = "yyyy-MM-dd";

    // Status
    String STATUS = "status";
    String RECORD_STATUS = "recordStatus";
    String INAU = "INAU";
    String WAITMASTER ="WAITMASTER";
    String PAYMENT_ORDER_ID = "paymentOrderId";
    String PAYMENTSTATUS = "paymentStatus";
    String WAITMASTER_AUTH = "WAITMASTER(AUTH)";
    String WAITMASTER_INAU = "WAITMASTER(INAU)";    
    String CREATED = "CREATED";
    String ERROR = "ERROR";
    String REJECTED = "REJECTED";
    String CANCELLED = "CANCELLED";
    String COMPLETED = "COMPLETED";
    String PLACED = "PLACED";
    String CHILDCOMPLETED = "CHILDCOMPLETED";
    String PROCESSED = "PROCESSED";
    String WAREHOUSED = "WAREHOUSED";
    String SENDORDER = "SENDORDER";
    String AWAITINGACK = "AWAITINGACK";
    String RESERVEFUNDS = "RESERVEFUNDS";
    String ORDERAPPROVED = "ORDERAPPROVED";
    String READYFOREXECUTION = "READYFOREXECUTION";
    String FRAUDCHECK = "FRAUDCHECK";
    String FRAUDCHECKCOMPLETE = "FRAUDCHECKCOMPLETE";
    String FRAUDCHECKFAILED = "FRAUDCHECKFAILED";
    String READY_FOR_EXECUTION = "Ready for Execution";
    String MODIFIED = "Modified";
    String NEWLY_ADDED = "Newly Added";
    String IN_ERROR = "In Error";
    String REJECT = "Rejected";
    String CANCEL = "Cancelled";
    String PROCESSING_COMPLETED = "Processing Completed";
    String PROCESSING = "Processing";
    
    String ACCP = "ACCP";
    String ACSC = "ACSC";
    String ACSP = "ACSP";
    String ACWC = "ACWC";
    String PNDG = "PNDG";
    String RJCT = "RJCT";

    String PAYMENT_ACCEPTED = "Payment Accepted";
    String SETTLEMENT_ACCEPTED = "Settlement Accepted";
    String PAYMENT_PENDING = "Payment Pending";
    String SETTLEMENT_IN_PROCESS = "Settlement In Process";
    String PAYMENT_REJECTED = "Payment Rejected";
    String ACCEPTED_WITH_CHANGE = "Accepted with change";

    // errors
    String INVALID_ACCOUNT_NUMBER = "Invalid Account number";
    String INVALID_IBAN = "Invalid IBAN";
    String INVALID_BIC = "Invalid Swift/BIC code";
    String COMPANY_ID="companyId";
	
	String API_ACCESS_TOKEN_KEY = "X-Kony-AC-API-Access-Token";
    String ACCESS_TOKEN = "DBP_AC_ACCESS_TOKEN";
    String AC_APP_KEY = "AC-X-Kony-App-Key";
    String APP_KEY = "DBP_AC_APP_KEY"; 
    String AC_APP_SECRET_KEY = "AC-X-Kony-App-Secret";
    String APP_SECRET = "DBP_AC_APP_SECRET";
    String C360_LOGIN_SERVICE = "C360APILogin";
    String C360_LOGIN_OPERATION = "login";
    String CLAIMS_TOKEN_KEY = "claims_token";
    String C360_CONFIGURATION_SERVICE = "dbpRbLocalServicesdb";
    String C360_CONFIGURATION_OPERATION = "dbxdb_configurations_get";
    String BUNDLE_NAME = "bundle_id";
    String CONFIG_KEY = "config_key";
    String ACCOUNT_TYPE_BUNDLE_NAME = "DBP_CONFIG_BUNDLE";
    String ACCOUNT_TYPE_CONFIG_KEY = "ACCOUNT_TYPES";
    String DBP_CONFIG_TABLE_VALUE = "config_value";
    String CONFIGURATIONS = "configurations";
    
    String TRANSACTION_STATUS_SUCCESSFUL = "Successful";
    String TRANSACTION_STATUS_PENDING = "Pending";
    
    String GET_BANK_DETAILS_BY_SWIFTCODE = "getBankDetailsBySwiftCode";
    String T24_IS_PAYMENTS_VIEW = "T24ISPaymentsView";

}
