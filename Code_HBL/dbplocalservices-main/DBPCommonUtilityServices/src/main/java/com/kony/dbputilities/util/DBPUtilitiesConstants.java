package com.kony.dbputilities.util;

import java.math.BigDecimal;

import org.apache.http.HttpStatus;

import com.dbp.core.constants.DBPConstants;

public final class DBPUtilitiesConstants {

    private DBPUtilitiesConstants() {
    }

    public static final String HTTP_STATUS_CODE = DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY;
    public static final String STATUS_CODE = "StatusCode";
    public static final String HTTP_SUCCESS_CODE = Integer.toString(HttpStatus.SC_OK);
    public static final String X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    public static final String X_KONY_DEVICEID = "X-Kony-DeviceId";
    public static final String X_KONY_AUTHORIZATION_VALUE = "x-kony-authorization";
    public static final String USERAGENTLOGINTYPE = "userLoginAgentType";
    public static final String HTTP_ERROR_CODE = "403";
    public static final String RECORD_COUNT = "recordCount";
    public static final String ZERO = "0";
    public static final String FILTER = "$filter";
    public static final String ORDERBY = "$orderby";
    public static final String TOP = "$top";
    public static final String SKIP = "$skip";
    public static final String SELECT = "$select";
	public static final String CORECUSTOMERID = "coreCustomerId";
    public static final String SORTBY = "sortBy";
    public static final String ORDER = "order";
    public static final String VALIDATION_ERROR = "errmsg";
    public static final String VALIDATION_ERROR_CODE = "errcode";
    public static final String VALIDATION_ERROR_MESSAGE = "errorMessage";
    public static final String ERROR_CODE = "errorCode";
    public static final String SUCCESS = "success";
    public static final String ERROR = "Failed";
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
    public static final String SECURITY_ERROR = "SECURITY ERROR - NOT AUTHORIZED";

    public static final String IS_USER_EXISTS = "isUserExists";
    public static final String IS_USERNAME_EXISTS = "isUserNameExists";
    public static final String IS_OTP_VERIFIED = "isOtpVerified";

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
    public static final String OPEN_BRACE = " ( ";
    public static final String CLOSE_BRACE = " ) ";
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
    public static final String IS_QR_PAYMENT_ACTIVE = "isQRPaymentActivated";
    public static final String DEF_ACCNT_BILLPAY = "default_account_billPay";
    public static final String IS_ENROLLED = "isEnrolled";
    public static final String IS_ENROLLED_FROM_SPOTLIGHT = "isEnrolledFromSpotlight";
    public static final String USR_ATTR = "user_attributes";
    public static final String PIN = "pin";
    public static final String IS_PIN_SET = "IsPinSet";
    public static final String ARE_USR_ALERTS_ON = "areUserAlertsTurnedOn";
    public static final String USR_ALERTS_ON = "alertsTurnedOn";
    public static final String CUSTOMER_ACCOUNTS_ATTR = "cust_acc_attr";
    public static final String CUSTOMER_ID = "Customer_id";
    public static final String C_FIRSTNAME = "firstName";
    public static final String C_LASTNAME = "lastName";
    public static final String C_MIDDLENAME = "middleName";
    public static final String CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION = "Mobile";
    public static final int CUSTOMER_ID_LENGTH = 10;
    public static final int DEFAULT_USERNAME_LENGTH = 10;

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
    public static final String INSTALLMENT_TYPE = "installmentType";
    public static final String DATE_FORMAT = "dateFormat";
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
    public static final String CORE_ATTR = "core_user_attributes";
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
    public static final String TRANSACTION_TYPE_LOAN_SCHEDULE = "LoanSchedule";
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
    public static final String IS_SCHEDULED = "isScheduled";
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
    public static final String IBAN = "IBAN";
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
    public static final String IS_BUSINESS_PAYEE = "isBusinessPayee";
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
    public static final String CUSTOMERID = "customerId";
    public static final String COMPANYID = "companyId";
    public static final String P_ORGANIZATION_ID = "organizationId";
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

    /**
     * Session Scope User Constants
     */
    public static final String USER_COMPANY_ID = "UserCompanyId";
    public static final String USER_ADDRESSES = "Addresses";
    public static final String USER_EMAILIDS = "EmailIds";
    public static final String USER_CONTACT_NUMBERS = "ContactNumbers";
    public static final String USER_ALL_SERVICES = "services";
    public static final String USER_CREATE_SERVICES = "CreateServices";
    public static final String USER_VIEW_SERVICES = "ViewServices";
    public static final String USER_APPROVE_SERVICES = "ApproveServices";
    public static final String USER_SELF_APPROVE_SERVICES = "SelfApproveServices";
    public static final String USER_MANAGE_PAYEE_SERVICES = "ManagePayeeServices";
    public static final String USER_MANAGE_TEMPLATE_SERVICES = "ManageTemplateServices";
    public static final String USER_IS_SECURITY_QUESTIONS_CONFIGURED = "isSecurityQuestionConfigured";
    public static final String BB_STATUS_IDS = "BBStatusIds";

    public static final String COMM_TYPE_PHONE = "COMM_TYPE_PHONE";
    public static final String COMM_TYPE_EMAIL = "COMM_TYPE_EMAIL";
    public static final String VALUE = "Value";
    public static final String X_KONY_REPORTING_PARAMS = "X-Kony-ReportingParams";
    public static final String AUTHORIZATION = "Authorization";
    public static final String IS_OTP_EXPIRED = "isOTPExpired";
    public static final String IS_DEVICE_REGISTERED = "isDeviceRegistered";
    public static final String CORE_SECURITY_ATTRIBUTES = "core_security_attributes";

    public static final String COMM_EMAIL = "Email";
    public static final String COMM_PHONE = "Phone";

    public static final String TNC_LOGIN = "Login_TnC";
    public static final String TNC_ENROLL = "Enroll_TnC";
    public static final String TNC_COMMON = "Common_TnC";
    public static final String TNC_HAMBURGER = "RetailBanking_TnC";
    public static final String TNC_ONBOARDING = "Onboarding_TnC";
    public static final String TNC_BUSINESSENROLL = "BusinessEnrollment_TnC";
    public static final String TNC_ONBOARDING_OFAC = "Onboarding_OFAC_TnC";
    public static final String TNC_ONBOARDING_ESIGN_AGREEMENT = "Onboarding_eSign_Agreement";
    public static final String TNC_ONBOARDING_GOOGLEMAPS_DISCLOSURE = "Onboarding_GoogleMaps_Disclosure";
    public static final String TNC_ONBOARDING_FUNDING_OTP_DISCLAIMER = "Onboarding_Funding_OTP_Disclaimer";
    public static final String TNC_ONBOARDING_FUNDING_DISCLAIMER = "Onboarding_Funding_Disclaimer";
    public static final String TNC_ONBOARDING_FUNDING_ACKNOWLEDGEMENT_DISCLAIMER =
            "Onboarding_Funding_Acknowledgement_Disclaimer";
    public static final String TNC_ONBOARDING_IDSCAN_DISCLAIMER = "Onboarding_IDScan_Disclaimer";
    public static final String TNC_ONBOARDING_PRODUCTDASHBOARD_DISCLAIMER = "Onboarding_ProductDashboard_Disclaimer";
    public static final String TNC_ONBOARDING_UPDATE_PASSWORD_RULES = "Onboarding_update_password_rules";
    public static final String TNC_ONBOARDING_COAPPLICANT_SECTION = "Onboarding_Coapplicant_section";
    public static final String TNC_ORIGINATIONSUBMIT = "TermsAndConditions";
    
    public static final String TNC_THIRDPARTY_AUTH = "Thirdparty_Auth";

    public static final String TYPE_ID_MICRO_BUSINESS = "TYPE_ID_BUSINESS";
    public static final String TYPE_ID_SMALL_BUSINESS = "TYPE_ID_BUSINESS";
    public static final String TYPE_ID_RETAIL = "TYPE_ID_RETAIL";
    public static final String TYPE_ID_BUSINESS = "TYPE_ID_BUSINESS";

    /*
     * campaign constants
     */
    public static final String DEFAULT_CAMPAIGN = "DEFAULT_CAMPAIGN";

    public static final String TNC_DEFAULT_LANGUAGE = "en-US";
    public static final String USER_IMAGE = "UserImage";

    public static final String ACTIONS_SEPERATOR = ",";
    public static final String CUSTOMERS_SEPERATOR = ",";
    public static final String COMMA_SEPERATOR = ",";

    public static final String ORG_FEATURES = "features";
    public static final String ORG_ACTIONLIMITS = "actionlimits";
    public static final String ORG_REMOVED_ACTION_LIMITS = "removedActionlimits";

    public static final String NON_MONETARY = "NON_MONETARY";
    public static final String MONETARY = "MONETARY";
    public static final String ACTION_TYPE = "actionType";
    public static final String ACTION_ID = "actionId";
    public static final String IS_ENABLED = "isEnabled";
    public static final String ACCOUNTS = "Accounts";
    public static final String LIMITS = "limits";

    public static final String AUTO_DENIED_DAILY_LIMIT = "AUTO_DENIED_DAILY_LIMIT";
    public static final String AUTO_DENIED_TRANSACTION_LIMIT = "AUTO_DENIED_TRANSACTION_LIMIT";
    public static final String AUTO_DENIED_WEEKLY_LIMIT = "AUTO_DENIED_WEEKLY_LIMIT";
    public static final String PRE_APPROVED_DAILY_LIMIT = "PRE_APPROVED_DAILY_LIMIT";
    public static final String PRE_APPROVED_TRANSACTION_LIMIT = "PRE_APPROVED_TRANSACTION_LIMIT";
    public static final String PRE_APPROVED_WEEKLY_LIMIT = "PRE_APPROVED_WEEKLY_LIMIT";

    public static final String ACCOUNTS_POSTLOGIN_CACHE_KEY = "ACCOUNTS";
    public static final String ADMIN_CONSOLE_APPIDAPP_MAPPING_KEY = "AC_APPID_TO_APP_MAPPING";

    public static final String CACHE_KEY_FOR_ACCOUNTS_SEARCH = "SEARCHACCOUNT";

    public static final String ARRANGEMENT_HOST_URL_RUNTIME_KEY = "ARRANGEMENTS_HOST_URL";
    public static final String CACHE_KEY_FOR_DEPLOYMENT_GEOGRAPHY = "DEPLOYMENT_GEOGRAPHY";

    public static final int DEFAULT_CACHE_TIME = 20 * 60;

    public static final String ORGANISATION_STATUS_PENDING = "SID_ORG_PENDING";
    public static final String ORGANISATION_STATUS_ACTIVE = "SID_ORG_ACTIVE";

    public static final int ORGANISATION_ID_LENGTH = 10;
    public static final int PROSPECT_EXPIRATION_DATE = 30;

    public static final String SID_ACTIVE = "SID_ACTIVE";

    /**
     * Event status constants
     */

    public static final String SID_EVENT_SUCCESS = "SID_EVENT_SUCCESS";
    public static final String SID_EVENT_FAILURE = "SID_EVENT_FAILURE";

    /**
     * Email Templates
     */
    public static final String CIF_EMAILTEMPLATE = "CIF_AUTH_SIGNATORY_EMAIL";
    public static final String NONCIF_EMAILTEMPLATE = "NONCIF_AUTH_SIGNATORY_EMAIL";
    public static final String CANTSIGNINFLOW_EMAILTEMPLATE = "CANT_SIGNIN_FLOW_EMAIL";
    /**
     * Organization status constants
     */
    public static final String ORG_STATUS_ACTIVE = "SID_ORG_ACTIVE";
    public static final String ORG_STATUS_REJECTED = "SID_ORG_REJECTED";
    public static final String ORG_STATUS_PENDING = "SID_ORG_PENDING";

    /**
     * Contract status constants
     */
    public static final String CONTRACT_STATUS_ACTIVE = "SID_CONTRACT_ACTIVE";
    public static final String CONTRACT_STATUS_REJECTED = "SID_CONTRACT_REJECTED";
    public static final String CONTRACT_STATUS_PENDING = "SID_CONTRACT_PENDING";

    /**
     * Customer status
     */
    public static final String CUSTOMER_STATUS_ACTIVE = "SID_CUS_ACTIVE";
    public static final String CUSTOMER_STATUS_LOCKED = "SID_CUS_LOCKED";
    public static final String CUSTOMER_STATUS_NEW = "SID_CUS_NEW";
    public static final String CUSTOMER_STATUS_PENDINGVERIFICATION = "SID_CUS_PENDING_VERIFICATION";
    public static final String CUSTOMER_STATUS_SUSPENDED = "SID_CUS_SUSPENDED";

    /**
     * Email Template Constants
     */
    public static final String ORGANIZATION_STATUS_PENDING_EMAIL_TEMPLATE =
            "BUSINESS_ENROLLMENT_PENDING_TEMPLATE";
    public static final String ORGANIZATION_STATUS_ACTIVE_EMAIL_TEMPLATE =
            "BUSINESS_ENROLLMENT_ACTIVATION_TEMPLATE";
    public static final String ORAGANIZATION_STATUS_REJECTED_EMAIL_TEMPLATE = "BUSINESS_ENROLLMENT_REJECTED_TEMPLATE";
    public static final String BUSINESS_ENROLLMENT_ACCOUNT_ACTIVATION = "BUSINESS_ENROLLMENT_ACCOUNT_ACTIVATION";
    public static final String ENROLLMENT_USERNAME_TEMPLATE = "ENROLLMENT_USERNAME_TEMPLATE";
    public static final String ENROLLMENT_ACTIVATIONCODE_TEMPLATE = "ENROLLMENT_ACTIVATIONCODE_TEMPLATE";
    public static final String DEVICE_REGISTRATION_ACTIVATIONCODE_TEMPLATE =
            "DEVICE_REGISTRATION_ACTIVATIONCODE_TEMPLATE";
    /**
     * * Boolean String Constants
     */
    public static final String BOOLEAN_STRING_TRUE = "true";
    public static final String BOOLEAN_STRING_FALSE = "false";
    /**
     * Backend type constants
     */
    public static final String CORE_BACKENDTYPE = "CORE";
    public static final String BACKENDID = "backendId";
    /**
     * Account holder types
     */
    public static final String MEMBERTYPE = "memberType";
    public static final String MEMBERTYPE_PRIMARY = "Primary";
    public static final String MEMBERTYPE_JOINT = "Joint";

    /**
     * T24 Constants
     */

    public static final String ACCOUNTTYPE_PROPERTIES_FILE_NAME = "ACCOUNTTYPE_PROPERTIES_FILE_NAME";
    public static final String ARRANGEMENT_ID = "arrangementId";
    public static final String PRIMARY_ACCOUNT_HOLDER = "PrimaryAccountholder";
    public static final String SECONDARY_ACCOUNT_HOLDER = "SecondaryAccountHolder";
    /**
     * 
     */
    public static final String IS_UPDATE_SUCEES = "isUpdateSuccess";
    public static final String ACCOUNT_HOLDER_DATASET = "AccountHolders";

    /**
     * Enroll flow
     */
    public static final String IS_CUSTOMER_ENROLLED = "isCustomerEnrolled";
    public static final String IS_ACTIVATION_LINK_SENT = "isActivationLinkSent";
    public static final String CUSTOMER_STATUS = "customerStatus";

    /**
     * Integration Constants
     */
    public static final String IS_INTEGRATED = "IsIntegrated";
    public static final String BACKEND_TYPE = "BackendType";
    public static final String BACKEND_CUSTOMER_IDENTIFIER_NAME = "BackendCustomerIdentifierName";

    public static final int DEFAULT_CAPTCHA_LENGTH = 5;

    public static final String FUTURE = "future";
    public static final String DUE = "due";
    public static final String PAID = "paid";

    /**
     * CompanyId
     */
    public static final String BRANCH_ID_REFERENCE = "BRANCH_ID_REFERENCE";
    public static final String COMPANY_ID = "companyid";

    public static final String IS_ASSOCIATED = "isAssociated";
    public static final String IS_SELECTED = "isSelected";
    public static final String LEGALENTITYID = "legalEntityId";
    public static final String IS_NEW = "isNew";
    public static final String ONBOARDING_USERNAME_TEMPLATE = "ONBOARDING_USERNAME_TEMPLATE";
    public static final String ONBOARDING_ACTIVATIONCODE_TEMPLATE = "ONBOARDING_ACTIVATIONCODE_TEMPLATE";

    public static final String ONBOARDING_PROSPECT_USERNAME_TEMPLATE = "ONBOARDING_PROSPECT_USERNAME_TEMPLATE";
    public static final String ONBOARDING_PROSPECT_USERNAME_APPICATIONID_TEMPLATE =
            "ONBOARDING_PROSPECT_USERNAME_APPICATIONID_TEMPLATE";
    public static final String ONBOARDING_PROSPECT_ACTIVATIONCODE_TEMPLATE =
            "ONBOARDING_PROSPECT_ACTIVATIONCODE_TEMPLATE";

    public static final String DEFAULT_ACCOUNT_STATUS = "Active";

    public static final String APPLICATION_CACHE_KEY = "APPLICATION";
    public static final String IS_STATE_MANAGEMENT_AVAILABLE = "isStateManagementAvailable";

    public static final String IS_RETAILUSER_ENROLLEMENT = "isRetailUserEnrollment";
    public static final String TYPE_ID_PROSPECT = "TYPE_ID_PROSPECT";
    public static final String ONBOARDING_ENTITY_DEFINTION = "ONBOARDING_ENTITY_DEFINTION";

    public static final String DBX_HOST_URL = "DBX_HOST_URL";

    public static final String PROSPECT_USERNAME_TEMPLATE = "PROSPECT_USERNAME_TEMPLATE";

    public static final String CONSENT_MASTER_ID = "GENERIC.CONSENT";
	
	// added as a part of adding MS headers params
	public static final String AWS = "AWS";
	public static final String AZURE = "AZURE";
	public static final String ROLE_ID = "roleId";
	public static final String X_API_KEY = "x-api-key";
	public static final String X_FUNCTIONS_KEY = "x-functions-key";
	public static final String AccountClosure_TnC_TnC = "AccountClosure_TnC";
	public static final String EarlyPayOffSimulation_TnC = "EarlyPayOffSimulation_TnC";
    public static final String IS_SELFAPPROVAL_ENABLED = "IS_SELFAPPROVAL_ENABLED";
    
    public static final String FLOW_TYPE = "flowType";
	public static final String PRE_LOGIN_FLOW = "PreLogin";
	public static final String LOGIN_FLOW = "Login";
	public static final String POST_LOGIN_FLOW = "PostLogin";
	
	public static final String TRANSACT_PRELOGIN_USERNAME = "T24";
	public static final String TRANSACT_PRELOGIN_USERID = "0001";
	public static final String TRANSACT_GENERIC_ROLEID = "INFINITY.RETAIL";
	public static final String MS_PRELOGIN_USERNAME = "Admin";
	public static final String MS_PRELOGIN_USERID = "10";
	public static final String MS_GENERIC_ROLEID = "ADMIN";
	public static final String PARTY_MS_PRELOGIN_USERNAME = "T24";
	public static final String PARTY_MS_PRELOGIN_USERID = "0001";
	public static final String PARAM_USERNAME = "UserName";
	public static final String PARAM_DBX_USER_ID = "dbxUserId";
	public static final String PARAM_CUSTOMER_ID = "customer_id";
	public static final String DBP_HOST_URL = "DBP_HOST_URL";
	public static final String BACKENDCERTNAME = "T24";
	public static final String AMS_BACKENDCERTNAME = "AMS";
	public static final String DMS_BACKEND = "DMS";
	public static final String DMS_ROLEID = "DocumentOwner";
	public static final String HOLDINGSMS_BACKENDCERTNAME = "HOLDINGSMS";
	public static final String PFWMS_BACKENDCERTNAME = "TRANSACT";
	public static final String ACCAGGMS_BACKENDCERTNAME = "ACCOUNTAGGREGATIONMS";
	public static final String AMS_USERNAME = "FABRICUSER";
	public static final String PRODUCT_TYPE_MS = "ms";
	public static final String PRODUCT_TYPE_DOCMS = "docms";
	public static final String PRODUCT_TYPE_T24 = "t24";
	public static final String FABRIC_ISSUER = "fabric";
	public static final String DOC_AUDTYPE = "documents";
	
	public static final String ADHOC_DOWNLOAD_PAGE_SIZE="500";
	public static final String COMBINED_DOWNLOAD_PAGE_SIZE="9999";
	public static final String CLIENT_IP = "CLIENT_IP";
}