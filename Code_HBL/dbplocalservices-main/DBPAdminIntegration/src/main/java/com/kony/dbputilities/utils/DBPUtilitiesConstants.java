package com.kony.dbputilities.utils;

import java.math.BigDecimal;

import org.apache.http.HttpStatus;

public final class DBPUtilitiesConstants {
    private DBPUtilitiesConstants() {
    }

    public static final String STATUS_CODE = "StatusCode";
    public static final String HTTP_SUCCESS_CODE = Integer.toString(HttpStatus.SC_OK);
    public static final String X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    public static final String X_KONY_AUTHORIZATION_VALUE = "x-kony-authorization";
    public static final String HTTP_ERROR_CODE = "403";
    public static final String RECORD_COUNT = "recordCount";
    public static final String ZERO = "0";
    public static final String FILTER = "$filter";
    public static final String ORDERBY = "$orderby";
    public static final String TOP = "$top";
    public static final String SKIP = "$skip";
    public static final String SELECT = "$select";
    public static final String SORTBY = "sortBy";
    public static final String ORDER = "order";
    public static final String VALIDATION_ERROR = "errmsg";
    public static final String VALIDATION_ERROR_MESSAGE = "errorMessage";
    public static final String ERROR_CODE = "errorCode";
    public static final String SUCCESS = "success";
    public static final String IDM_IDENTIFIER = "IDMidentifier";
    public static final String IDENTIFIER_TYPE = "identifierType";
    public static final String COREBANKING_USER_VERIFICATION = "COREBANKING_USER_VERIFICATION";
    public static final String COREBANKING_USERNAME_VERIFICATION = "COREBANKING_USERNAME_VERIFICATION";
    public static final String COREBANKING_CORPORATE_CREATE = "COREBANKING_CORPORATE_CREATE";
    public static final String COREBANKING_CUSTOMER_CREATE = "COREBANKING_CUSTOMER_CREATE";
    public static final String COREBANKING_LOGIN = "COREBANKING_LOGIN";
    public static final String COREBANKING_USER_UPDATE = "COREBANKING_USER_UPDATE";
    public static final String USER_NOT_EXISTS_IN_DBX = "User doesn't exist in DBX. ";
    public static final String USER_EXISTS_IN_DBX = "User exists in DBX. ";
    public static final String USER_NOT_EXISTS_IN_EXTERNALBANK = "User doesn't exist in ExternalBank. ";
    public static final String USER_EXISTS_IN_EXTERNALBANK = "User exists in ExternalBank. ";
    public static final String PROVIDE_USERNAME = "Please provide username. ";
    public static final String PROVIDE_USERNAME_AND_PASSWORD = "Please provide username and Password. ";
    public static final String PROVIDE_VALID_PASSWORD = "Password does not meet the validation criteria";
    public static final String BACKEND_IDENTIFIER_NOT_FOUND = "BackendIdentifier Not found. ";
    public static final String INCORRECT_USERNAME_OR_PASSWORD = "Incorrect UserName or Password. ";
    public static final String INVALID_DETAILS = "Invalid parameters please give valid details.";
    public static final String USER_ALREADY_EXISTS = "User already exists with the given info in DBX. ";
    public static final String USERNAME_ALREADY_EXISTS = "This User Name is not available. Please try another one. ";
    public static final String ACCOUNTS_MISSING_IN_REQUEST = "Please provide accounts in the request.";
    public static final String ACCOUNTS_MISSING_FOR_ORG = "No Accounts are mapped for organization.";
    public static final String USER_ID_MISSED_IN_REQUEST = "User creation failed with given username";
    public static final String USER_ID_MEM_ID_MISSED_IN_REQUEST = "Owner Creation failed.";

    public static final String ACCOUNTS_EXISTS_IN_DBX = "Accounts exist for Organisation. ";
    public static final String ACCOUNTS_NOT_EXISTS_IN_DBX = "Accounts don't exist for Organisation. ";
    public static final String RECORD_FOUND_IN_DBX = "Record found in DBX. ";
    public static final String RECORD_NOT_FOUND_IN_DBX = "Record not found in DBX. ";

    public static final String DBX = "DBX";
    public static final String EXTERNALBANK = "ExternalBank";
    public static final String RESULT_MSG = "result";
    public static final String SUCCESS_MSG = "Successful";
    public static final String STRING_TYPE = "String";
    public static final String INT = "int";
    public static final String OTP = "otp";
    public static final String RESULT_ATTRIBUTE_KEY = "ResultOnException";
    public static final String EQUAL = " eq ";
    public static final String NOT_EQ = " ne ";
    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String GREATER_EQUAL = " ge ";
    public static final String LESS_EQUAL = " le ";
    public static final String LESS_THAN = " lt ";
    public static final String GREATER_THAN = " gt ";
    public static final String SOFT_DELETE_FLAG = "softdeleteflag";
    public static final String CUSTOMER_LAST_NAME = "LastName";
    /**
     * Application constants
     */
    public static final String APPLICAION_ID = "Id";
    public static final String OSTYPE = "OSType";
    public static final String OSVERSION = "OSversion";
    public static final String BANNERURL = "BannerURL";
    public static final String CURRENCYCODE = "currencyCode";
    public static final String BUSINESSDAYS = "BusinessDays";
    public static final String DISTANCEUNIT = "DistanceUnit";
    public static final String BANKNAME = "BankName";
    /**
     * LockObject constants
     */
    public static final String EXTERNAL_ID = "ExternalId";
    public static final String OBJECT_ID = "ObjectId";
    public static final String USER = "User";
    public static final String OBJECT_NAME = "ObjectName";
    public static final String MODE = "Mode";
    /**
     * Numberrange constants
     */
    public static final String DEFAULT_START_VALUE = "1";
    public static final int NR_MAX_LENGTH = 9;
    public static final String CURRENT_VALUE = "CurrentValue";
    public static final String LENGTH = "Length";
    public static final String START_VALUE = "StartValue";
    public static final String END_VALUE = "EndValue";
    /**
     * User constants
     */
    public static final String EMAIL = "email";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String PHONE_NUMBER = "phone";
    public static final String USER_NAME = "userName";
    public static final String PWD_FIELD = "password";
    public static final String PWD_NEW_FIELD = "Password";
    public static final String OLD_PWD_FIELD = "oldpassword";
    public static final String FIRST_NAME = "userFirstName";
    public static final String LAST_NAME = "userLastName";
    public static final String DS_USER = "user";
    public static final String DS_CUSTOMER = "customer";
    public static final String DS_CUSTOMERPREFERENCE = "customerpreference";
    public static final String DS_CUSTOMERCOMMUNICATION = "customercommunication";
    public static final String SESSION_ID = "Session_id";
    public static final String ROLE = "role";
    public static final String DOB = "dateOfBirth";
    public static final String C_DOB = "DateOfBirth";
    public static final String SSN = "ssn";
    public static final String C_SSN = "Ssn";
    public static final String U_ID = "Id";
    public static final String C_ID = "id";
    public static final String IS_BILL_PAY_ACTIVE = "isBillPayActivated";
    public static final String IS_P2P_ACTIVE = "isP2PActivated";
    public static final String DEF_ACCNT_BILLPAY = "default_account_billPay";
    public static final String IS_ENROLLED = "isEnrolled";
    public static final String USR_ATTR = "user_attributes";
    public static final String PIN = "pin";
    public static final String IS_PIN_SET = "isPinSet";
    public static final String ARE_USR_ALERTS_ON = "areUserAlertsTurnedOn";
    public static final String USR_ALERTS_ON = "alertsTurnedOn";
    public static final String CUSTOMER_ACCOUNTS_ATTR = "cust_acc_attr";
    public static final String CUSTOMER_ACCOUNTS_DATASET = "customeraccounts";
    /**
     * usersecurity constants
     */
    public static final String USER_SECURITY_TABLE = "usersecurity";
    public static final String PROVIDER_TOKEN = "provider_token";
    public static final String SECURITY_ATTRIBUTES = "security_attributes";
    public static final String SESSION_TOKEN = "session_token";
    /**
     * useralerts constants
     */
    public static final String UA_USR_ID = "User_id";
    /**
     * usernotification constants
     */
    public static final String UNREAD_COUNT = "UnreadNotificationCount";
    public static final String UN_USR_ID = "user_id";
    public static final String IS_READ = "isRead";
    public static final String RECEIVED_DATE = "receivedDate";
    public static final String TBL_USERNOTIFICATION = "usernotification";
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String UN_ID = "id";
    /**
     * Accounts constants
     */
    public static final String ACCOUNT_ID = "Account_id";
    public static final String TYPE_ID = "Type_id";
    public static final String USER_NAME_A = "UserName";
    public static final String ACCOUNT_HOLDER = "AccountHolder";
    public static final String NICK_NAME = "NickName";
    public static final String AVAILABLE_BAL = "AvailableBalance";
    public static final String AVAILABLE_BAL_S = "availableBalance";
    public static final String CURRENT_AMNT_DUE = "CurrentAmountDue";
    public static final String CURRENT_AMNT_DUE_S = "currentAmountDue";
    public static final String LATE_FEES_DUE = "LateFeesDue";
    public static final String PAY_OFF_CHARGE = "PayOffCharge";
    public static final String PRINCIPAL_BAL_S = "principalBalance";
    public static final String PRINCIPAL_BAL = "PrincipalBalance";
    /**
     * AccountType constants
     */
    public static final String ID = "Id";
    public static final String TERM_N_CONDITIONS = "TermsAndConditions";
    public static final String RATES = "Rates";
    public static final String DS_ACCOUNTTYPE = "accounttype";
    /**
     * AccountStatement constants
     */
    public static final String FROM_DATE = "Date_From";
    public static final String TO_DATE = "Date_To";
    public static final String TYPE = "Type";
    public static final String MONTH = "Month";
    public static final String DESCRIPTION = "Description";
    public static final String STATEMENTLINK = "Statementlink";
    /**
     * AccountTransaction constants
     */
    public static final BigDecimal MAX_DEPOSIT_AMOUNT = new BigDecimal("10000");
    public static final String PAYEE_ID = "payeeID";
    public static final String TRANSACTION_TYPE = "transactionType";
    public static final String SEARCH_FETCH_TYPE = "searchFetchType";
    public static final String SEARCH_DESCRIPTION = "searchDescription";
    public static final String FIRST_RECORD_NUMBER = "firstRecordNumber";
    public static final String LAST_RECORD_NUMBER = "lastRecordNumber";
    public static final String SEARCH_MIN_AMOUNT = "searchMinAmount";
    public static final String SEARCH_MAX_AMOUNT = "searchMaxAmount";
    public static final String SEARCH_AMOUNT = "searchAmount";
    public static final String SEARCH_START_DATE = "searchStartDate";
    public static final String SEARCH_END_DATE = "searchEndDate";
    public static final String SEARCH_DATE = "searchDateRange";
    public static final String CHK_NUMBER = "checkNumber";
    public static final String FRM_CHK_NUMBER = "fromCheckNumber";
    public static final String TO_CHK_NUMBER = "toCheckNumber";
    public static final String ACCT_NUMBER = "accountNumber";
    public static final String SEARCH_TRANS_TYPE = "searchTransactionType";
    public static final String TRANS_ID = "transactionID";
    public static final String CUSTOMTER_ID = "Customer_id";
    public static final String CORE_ATTR = "core_attributes";
    public static final String REFERENCE_ID = "Reference_id";
    public static final String AMOUNT = "Amount";
    public static final String T_PAYEE_ID = "Payee_id";
    public static final String T_TRANS_ID = "Id";
    public static final String T_USR_ID = "User_id";
    public static final String T_TYPE_ID = "Type_id";
    public static final String USR_ID = "userId";
    public static final String CREATED_DATE = "CreatedDate";
    public static final String TO_ACCOUNT_BALANCE = "toAccountBalance";
    public static final String NO_OF_RECURRENCES = "numberOfRecurrences";
    public static final String RECUR_DESC = "recurrenceDesc";

    public static final String TRANSACTION_TYPE_DEPOSIT = "Deposit";
    public static final String TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST = "StopCheckPaymentRequest";
    public static final String TRANSACTION_TYPE_WIRE = "Wire";
    public static final String TRANSACTION_TYPE_CARDLESS = "Cardless";
    public static final String TRANSACTION_TYPE_LOAN = "Loan";
    public static final String TRANSACTION_TYPE_INTERNAL_TRANSFER = "InternalTransfer";
    public static final String TRANSACTION_TYPE_EXTERNAL_TRANSFER = "ExternalTransfer";
    public static final String TRANSACTION_TYPE_PAY_BILL = "BillPay";
    public static final String TRANSACTION_TYPE_P2P = "P2P";
    public static final String TRANSACTION_TYPE_REQUEST = "Request";
    public static final String TRANSACTION_TYPE_CHECK_WITHDRWAL = "CheckWithdrawal";
    public static final String TRANSACTION_TYPE_WITHDRWAL = "Withdrawal";
    public static final String TRANSACTION_TYPE_INTEREST = "Interest";
    public static final String TRANSACTION_FETCH_TYPE_SEARCH = "Search";
    public static final String TRANSACTION_TYPE_SEARCH_BOTH = "Both";
    public static final String TRANSACTION_TYPE_SEARCH_WITHDRAWAL = "Withdrawal";
    public static final String TRANSACTION_TYPE_SEARCH_P2PCREDITS = "P2PCredits";
    public static final String TRANSACTION_TYPE_SEARCH_TRANSFERS = "Transfers";
    public static final String TRANSACTION_TYPE_SEARCH_CHECKS = "Checks";
    public static final String TRANSACTION_TYPE_SEARCH_P2PDEBITS = "P2PDebits";
    public static final String TRANSACTION_TYPE_SEARCH_BILLPAY = "BillPay";
    public static final String TRANSACTION_TYPE_SEARCH_DEPOSIT = "Deposit";
    public static final String TRANSACTION_STATUS_SUCCESSFUL = "Successful";
    public static final String TRANSACTION_STATUS_FAILED = "Failed";
    public static final String TRANSACTION_STATUS_CANCELLED = "Cancelled";
    public static final String TRANSACTION_STATUS_PENDING = "Pending";
    public static final String MOBILE_DEPOSIT_MESSAGE = "Mobile Deposit To ";
    public static final String TRANSFER_MESSAGE = "Transfer To ";
    public static final String TRANSACTION_TYPE_POS = "POS";
    public static final String TRANSACTION_TYPE_INTERNET = "InternetTransaction";
    public static final String TRANSACTION_TYPE_TAX = "Tax";
    public static final String TRANSACTION_TYPE_FEE = "Fee";
    public static final String TRANSACTION_TYPE_CARDPAYMENT = "CardPayment";

    public static final String FRM_ACCONT = "fromAccountNumber";
    public static final String TO_ACCONT = "toAccountNumber";
    public static final String TRANS_DATE = "transactionDate";
    public static final String CHK_NUM = "checkNumber";
    public static final String STATUS_DESC = "statusDesc";
    public static final String IS_SCHEDULED = "IsScheduled";
    public static final String SCHEDULED_DATE = "scheduledDate";
    public static final String RECENT_TRANS_VIEW = "recenttransactionviews";
    public static final String TO_EXT_ACCT_NUM = "toExternalAccountNumber";
    public static final String FREQUENCY_TYPE = "frequencyType";
    public static final String TRANS_NOTES = "notes";
    public static final String FRM_ACCNT_BAL = "fromAccountBalance";
    public static final String TO_ACCNT_BAL = "toAccountBalance";
    public static final String PERSON_ID = "Person_Id";
    /**
     * ExtrernalAccount
     */
    public static final String ACCOUNT_TYPE = "AccountType";
    public static final String BANK_NAME = "BankName";
    public static final String BENEFICIARY_NAME = "BeneficiaryName";
    public static final String USER_ID = "User_id";
    public static final String BANK_ID = "Bank_id";
    public static final String USR_ACCNT = "User_Account";
    public static final String ACCNT_NUM = "accountNumber";
    public static final String EXT_ID = "Id";
    public static final String N_NAME = "nickName";
    public static final String F_NAME = "firstName";
    public static final String L_NAME = "lastName";
    public static final String SOFT_DEL = "softDelete";
    public static final String TRANS_TYPE = "transType";
    public static final String IS_INT_ACCNT = "isInternationalAccount";
    public static final String IS_SAME_BANK_ACCNT = "isSameBankAccount";
    public static final String CREATED_ON = "createdOn";
    public static final String IS_VERIFIED = "isVerified";
    /**
     * payees constants
     */
    public static final String P_ID = "id";
    public static final String TRANS_DESC = "Description";
    public static final String P_PAYEE_ID = "Payee_id";
    public static final String P_ACCT_NUM = "AccountNumber";
    public static final String P_NICK_NAME = "NickName";
    public static final String P_SOFT_DEL = "SoftDelete";
    public static final String P_USER_ID = "User_Id";
    public static final String P_F_NAME = "FirstName";
    public static final String P_L_NAME = "LastName";
    public static final String P_NAME = "Name";
    public static final String P_SEARCH_STRING = "SearchString";
    public static final String P_OFFSET = "offset";
    public static final String P_LIMIT = "limit";
    /**
     * userpersonalinfo constants
     */
    public static final String PI_USR_ID = "User_id";
    public static final String PERSONL_INFO = "userPersonalInfo";
    public static final String EMPLOYMENT_INFO = "userEmploymentInfo";
    public static final String FINANCIAL_INFO = "userFinancialInfo";
    public static final String SECURITY_QUEST = "userSecurityQuestions";
    public static final String EMP_DOC = "employementDoc";
    public static final String ADDRESS_DOC = "addressDoc";
    public static final String INCOME_DOC = "incomeDoc";
    public static final String SIG_IMG = "signatureImage";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String ZIP_CODE = "zipcode";
    public static final String JOB_PROF = "jobProfile";
    public static final String EXPERIENCE = "experience";
    public static final String ANNUAL_INCOME = "annualIncome";
    public static final String SPOUSE_NAME = "spousename";
    public static final String CITY = "city";
    public static final String DEPENDENT_NOS = "noOfDependents";
    public static final String ASSETS = "assests";
    public static final String PI_DOB = "dateOfBirth";
    public static final String GENDER = "gender";
    public static final String PI_FIRST_NAME = "userfirstname";
    public static final String PI_LAST_NAME = "userlastname";
    public static final String MARITAL_STATUS = "maritalstatus";
    public static final String SPOUSE_F_NAME = "spouseFirstName";
    public static final String SPOUSE_L_NAME = "spouseLastName";
    public static final String ADDRESS1 = "addressLine1";
    public static final String ADDRESS2 = "addressLine2";
    public static final String COMAPNY = "company";
    public static final String MONTHLY_EXP = "montlyExpenditure";
    public static final String EMP_INFO = "employmentInfo";

    /**
     * usercheckcredit
     */
    public static final String IS_CREDIT_CHK = "isCreditCheck";
    public static final String IS_SIG_UPLOAD = "isSingatureUpload";
    public static final String CREDIT_CHECK = "creditCheck";
    public static final String SIGNATURE_IMG = "singatureImage";
    /**
     * usersecurityquestions
     */
    public static final String USR_SERCURITY_QUES = "userSecurityQuestions";
    public static final String USR_SECURITY_LIST = "usersecurityli";
    public static final String JSON_QUES_ID = "question_id";
    public static final String JSON_ANS = "answer";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    /**
     * userproducts
     */
    public static final String USR_PRODUCTS = "userProducts";
    public static final String PRODUCT_ID = "Product_id";
    public static final String PRODUCT_LIST = "productLi";
    public static final String JSON_PRD_ID = "productId";
    /**
     * product constants
     */
    public static final String STATE_ID = "StateId";
    public static final String ACNT_TYPE = "accountType";
    /**
     * branch n branchtype
     */
    public static final String MB_ADDRESS1 = "MainBranchaddressLine1";
    public static final String MB_ADDRESS2 = "MainBranchaddressLine2";
    public static final String MB_CITY = "MainBranchCity";
    public static final String MB_LATITUDE = "MainBranchLatitude";
    public static final String MB_LONGTUDE = "MainBranchLongitude";
    public static final String MB_PHONE = "MainBranchPhone";
    public static final String MB_SERVICES = "MainBranchServices";
    public static final String MB_STATE = "MainBranchState";
    public static final String MB_WORKING_HOURS = "MainBranchWorkingHours";
    public static final String MB_ZIPCODE = "MainBranchzipCode";
    public static final String MB_TYPE = "branchType";
    public static final String IS_UPDATE = "isUpdate";
    public static final String IS_UPDATE_MANDT = "isUpdateMandatory";
    public static final String BRANCH_TYPE = "Type";
    /**
     * Advertisement constants
     */
    public static final String DM_ADD_ID = "dm_add_id";
    /**
     * messages
     */
    public static final String CATEGORY_ID = "Category_id";
    public static final String SUB_CATEGORY_ID = "Subcategory_id";
    public static final String M_RECEIVED_DATE = "receivedDate";
    public static final String M_SOFT_DEL_DATE = "softdeletedDate";
    public static final String M_CREATED_DATE = "createdDate";
    public static final String M_SENT_DATE = "sentDate";
    /**
     * pfmpiechart
     */
    public static final String PFMPIRCHART_ID = "id";
    public static final String USERID = "userid";
    public static final String MONTH_ID = "monthId";
    public static final String CATEGORYID = "categoryId";
    public static final String CASH_SPENT = "cashSpent";
    public static final String YEAR = "year";
    /**
     * Transactiontype
     */
    public static final String TRANS_TYPE_DESC = "description";
    public static final String TRANS_TYPE_TABLE_NAME = "transactiontype";
    /**
     * Message
     */
    public static final String MSG_STATUS = "status";
    public static final String MSG_READ_STATUS = "isRead";
    public static final String COUNT = "count";
    public static final String IS_SOFT_DELETED = "isSoftDeleted";
    /**
     * Payperson
     */
    public static final String PP_FIRSTNAME = "firstName";
    public static final String PP_LASTNAME = "lastName";
    public static final String PP_PHONE = "phone";
    public static final String PP_EMAIL = "email";
    public static final String PP_USR_ID = "User_id";
    public static final String PP_NICKNAME = "nickName";
    public static final String PP_ID = "id";
    public static final String PP_SECONDARYEMAIL = "secondaryEmail";
    public static final String PP_SEC_PHONENUM = "secondoryPhoneNumber";
    public static final String PP_PRI_CONT_SEND = "primaryContactForSending";
    /**
     * Scheduled Transaction
     */
    public static final String SCHEDULED_TRANS = "scheduledtransaction";
    public static final String TOKEN = "token";
    public static final String TRANSACTION_ID = "transactionId";
    public static final String TRANS_STATUS_DESC_COMPLETED = "Completed";
    public static final String TRANS_STATUS_DESC_PENDING = "pending";
    public static final String TRANS_IS_SCHEDULED = "isScheduled";

    /**
     * Phone
     */
    public static final String PHONE_ID = "id";
    public static final String COUNTRY_TYPE = "countryType";
    public static final String EXTENSION = "extension";
    public static final String ISPRIMARY = "isPrimary";
    public static final String RECEIVE_PROMO = "receivePromotions";
    public static final String PHONE_NUM = "phoneNumber";
    public static final String PHONE_TYPE = "type";

    /**
     * checkorder
     */
    public static final String CHECK_ID = "id";
    public static final String CHK_ACCOUNT_ID = "account_id";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String STATUS = "status";
    public static final String LEAF_COUNT = "leafCount";
    public static final String NAME = "name";
    public static final String POSTBOX_NUM = "postBoxNumber";
    public static final String ORGMEM_ATTR = "orgmem_attributes";

    public static final int SALT_NUMBER_OF_ROUNDS = 11;
    public static final String LOGGEDIN_USER_IS_NOT_ADMIN = "Logged in user is not Admin or Owner";
    public static final String INVALID_STATUS = "Invalid Status";
    
    public static final String PARAM_CUSTOMER_TYPE_STR = "customerTypeStr";
    public static final String CLIENT_IP = "CLIENT_IP";

}