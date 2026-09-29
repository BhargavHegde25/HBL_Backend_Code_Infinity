package com.temenos.msArrangement.utils;

public class ErrorConstants {

    private ErrorConstants() {
    }

    public static final String CUSTOMER_360_API_LOGIN_FAILURE = "Failed to fetch API Auth Token from Customer360";
    public static final String USER_NOT_EXISTS_IN_DBX = "User doesn't exist in DBX. ";
    public static final String USER_EXISTS_IN_DBX = "User exists in DBX. ";
    public static final String USER_NOT_EXISTS_IN_EXTERNALBANK = "User doesn't exist in ExternalBank. ";
    public static final String USER_EXISTS_IN_EXTERNALBANK = "User exists in ExternalBank. ";
    public static final String USER_ALREADY_EXISTS = "User already exists with the given info in DBX. ";
    public static final String USERNAME_ALREADY_EXISTS = "This User Name is not available. Please try another one. ";

    public static final String PROVIDE_USERNAME = "Please provide username. ";
    public static final String PROVIDE_USERNAME_AND_PASSWORD = "Please provide username and Password. ";
    public static final String PROVIDE_VALID_PASSWORD = "Password does not meet the validation criteria";
    public static final String INCORRECT_USERNAME_OR_PASSWORD = "Incorrect UserName or Password. ";
    public static final String PASSWORD_CANNOT_RESET = "Password cannot be reset";
    public static final String INVALID_PASSWORD_RESETLINK =
            "Invalid Password Reset Link. Please contact the bank for more information";

    public static final String BACKEND_IDENTIFIER_NOT_FOUND = "BackendIdentifier Not found. ";

    public static final String INVALID_DETAILS = "Invalid parameters please give valid details.";

    public static final String RECORD_FOUND_IN_DBX = "Record found in DBX. ";
    public static final String RECORD_NOT_FOUND_IN_DBX = "Record not found in DBX. ";
    public static final String BACKGROUNDVERIFICATION_FAILED = "backgroundverification failed. ";

    public static final String RECORDS_EXISTS = "Records exists";
    public static final String RECORDS_NOT_EXISTS = "Records doesn't exists";

    public static final String SECURITY_ERROR = "SECURITY ERROR - NOT AUTHORIZED";
    public static final String SECURITY_EXCEPTION = "Security Exception";
    public static final String UNAUTHORIZED_ACCESS = "SECURITY EXCEPTION - UNAUTHORIZED ACCESS";

    public static final String ERROR_IN_SEARCH = "Error in searching record.";
    public static final String ERROR_IN_CREATE_PROSPECT = "Failed to Create Prospect";
    public static final String ERROR_IN_CREATE = "Creation Failed";
    public static final String ERROR_IN_CREATE_CORPORATE = "Failed to Create Corporate User";
    public static final String ACCOUNTS_NOT_EXISTS_IN_DBX = "Accounts don't exist for Organisation.";

    public static final String INCORRECT_DETAILS = "Incorrect details";
    public static final String CSR_ASSIST_TOKEN = "Failed to verify CSR Assist token";
    public static final String SESSIONTOKEN_EXPIRED = "session token is expired";
    public static final String LOGIN_SUCCESS = "Login success";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String TRIAL_EXPIRED = "Your trial period has expired. Please create another user";
    public static final String PROFILE_LOCKED = "Your profile is locked, it will be unlocked after x mins";
    public static final String PROFILE_SUSPENDED =
            "Sorry, your profile is suspended. Please contact bank for more information";
    public static final String ACTIVATION_BEFORE_LOGIN =
            "You need to complete the online banking activation process before you login. Please contact the bank for more information.";
    public static final String SUBMIT_AGREEMENT =
            "You are required to submit an agreement with the bank. Please contact the bank for more information.";
    public static final String INACTIVE_USER = "User is not Active.";
    public static final String AUTHENTICATE_WITH_CB = "Authenticate With CoreBanking System.";
    public static final String NOT_AUTHORISED_TO_USE =
            "You are not authorized to use this application. Please contact the bank for more information.";
    public static final String REPORTING_PARAMS_MISSING = "Reporting Params are missing from request Headers.";
    public static final String REGISTER_YOUR_DEVICE = "Please register your device to Use Pin.";
    public static final String INVALID_PIN = "Invalid Pin.";
    public static final String INVALID_USERNAME = "Invalid username.";

    public static final String INVALID_ACCOUNT_NUMBER = "Invalid account number. please provide valid details.";
    public static final String INVALID_ACCOUNT_ACCESS = "You don’t have access to this account";
    public static final String INTERNAL_SERVICE_ERROR = "Internal service error";
    public static final String NO_MATCHING_RECORDS_FOUND = "No matching records found";
    public static final String RECORD_DOESNT_BELONG_TO_THE_SAME_COMPANY =
            "Provided record doesn't belong to the same company";

    public static final String MISSING_TEMPLATE_NAME =
            "TemplateName is missing in the input. please provide valid TemplateName.";
    public static final String MISSING_TEMPLATE_ID =
            "Template_id is missing in the input. please provide valid Template_id.";
    public static final String MISSING_TEMPLATE_RECORD_ID =
            "TransactionRecord_id is missing in the input. please provide valid TransactionRecord_id.";
    public static final String MISSING_TRANSACTION_ID =
            "TransactionType_id is missing in the input. please provide valid TransactionType_id.";
    public static final String MISSING_TEMPLATE_REQUEST_ID =
            "TemplateRequestType_id is missing in the input. please provide valid TemplateRequestType_id.";
    public static final String MISSING_RECORDS = "Records is missing in the input. please provide valid Records.";
    public static final String MISSING_MAX_AMOUNT =
            "MaxAmount is missing in the input. please provide valid MaxAmount.";
    public static final String MISSING_AMOUNT = "Amount is missing in the input. please provide valid Amount.";
    public static final String MISSING_PAYEE = "Payee is missing in the input. please provide Payee.";
    public static final String MISSING_DEBIT_ACCOUNT =
            "DebitAccount is missing in the input. please provide valid DebitAccount.";
    public static final String MISSING_DEBIT_CREDET_ACCOUNT =
            "DebitOrCreditAccount is missing in the input. please provide valid DebitOrCreditAccount.";
    public static final String MISSING_EFFECTIVE_DATE =
            "EffectiveDate is missing in the input. please provide valid EffectiveDate(YYYY-MM-DD).";
    public static final String MISSING_TRANSACTION_DATE =
            "TransactionDate is missing in the input. please provide valid TransactionDate(YYYY-MM-DD).";
    public static final String MISSING_BBGENERAL_TRANSACTION_TYPE_ID =
            "BBGeneralTransactionType_id is missing in the input. please provide valid BBGeneralTransactionType_id.";
    public static final String MISSING_REQUEST_ID =
            "Request_id is missing in the input. please provide valid Request_id.";
    public static final String MISSING_ACTION = "Action is missing in the input. please provide valid Action.";
    public static final String MISSING_TRANSACTIONENTRY =
            "TransactionEntry is missing in the input. please provide valid TransactionEntry JSON Object.";

    public static final String USER_UNAUTHORIZED = "Logged in User is not authorized to perform this action";
    public static final String REQUEST_PROCESS_FAILED =
            "Failed to process the request. Please verify your input and try again";

    public static final String ACH_TEMPLATE_CREATION_FAILED = "Failed to create ACH Template";
    public static final String ACH_TEMPLATE_RECORD_CREATION_FAILED = "Failed to create ACH TemplateRecord";
    public static final String ACH_TEMPLATE_SUB_RECORD_CREATION_FAILED = "Failed to create ACH TemplateSubRecord";

    public static final String ACH_TRANSACTION_CREATION_FAILED = "Failed to create ACH Transaction";
    public static final String ACH_TRANSACTION_RECORD_CREATION_FAILED = "Failed to create ACH TransactionRecord";
    public static final String ACH_TRANSACTION_SUB_RECORD_CREATION_FAILED = "Failed to create ACH TransactionSubRecord";
    public static final String APPROVE_FAILED = "Failed to approve the transaction";

    public static final String TOTAL_AMOUNT_GREATER_THAN_MAX_AMOUNT = "Total amount is greater than the Maximum amount";
    public static final String TOTAL_AMOUNT_CANNOT_BE_ZERO = "Total amount cannot be 0";
    public static final String FILE_TOO_LARGE = "File too Large";
    public static final String FILE_FORMATS_NOT_AVAILABLE = "File Formats Not Available";
    public static final String UNSUPPORTED_FILE_TYPE = "Unsupported File Type";

    public static final String FAILED_TO_FETCH_USERNAME = "Failed to fetch UserName Details";
    public static final String FAILED_TO_FETCH_USER_DETAILS = "Failed to fetch User Details";
    public static final String FAILED_TO_FETCH_CUSTOMER_DETAILS = "Failed to fetch Customer Details";
    public static final String FAILED_TO_FETCH_EMPLOYEE_DETAILS = "Failed to fetch Employee Details";
    public static final String FAILED_TO_FETCH_OWNER_DETAILS = "Failed to fetch Owner Details";
    public static final String FAILED_TO_FETCH_ORGANIZATION_DETAILS = "Failed to fetch Organization Details";
    public static final String FAILED_TO_FETCH_ORGANIZATIONACCOUNTS_DETAILS =
            "Failed to fetch OrganizationAccounts Details";
    public static final String FAILED_TO_FETCH_ORGANIZATIONEMPLOYES_DETAILS =
            "Failed to fetch OrganizationEmployes Details";
    public static final String FAILED_TO_FETCH_TIN_DETAILS = "Failed to fetch TIN Details";
    public static final String FAILED_TO_FETCH_ACCOUNTS_DETAILS = "Failed to fetch Accounts Details";
    public static final String GIVEN_ACCOUNT_DOES_NOT_BELONG_TO_CUSTOMER =
            "Given account id does not belong to the customer";
    public static final String ERROR_UPDATING_PASSWORD = "Error in updating Pasword";
    public static final String INVALID_PASSWORD = "Invalid Password";

    public static final String INVALID_REQUEST_PASS_USERNAME = "Please provide username.";
    public static final String USER_CREATION_FAILED_IN_CORE = "User creation failed with given username";
    public static final String USER_DOESNT_BELONG_TO_ORG = "User doesn't belong to Organization.";
    public static final String INVALID_REQUEST_USERNAME_ID_MISSING = "User creation failed with given username";
    public static final String ACCOUNTS_MISSING_IN_REQUEST = "Please provide accounts in the request.";
    public static final String CUSTOMER_ACCOUNT_CREATE_FAILED = "Account creation failed.";
    public static final String CUSTOMER_ACCOUNT_DELETE_FAILED = "Error in deleting the Account.";
    public static final String CUSTOMER_ROLE_NOT_AVAILABLE = "No role available in the request to proceed.";
    public static final String TRANSACTION_LIMITS_NOT_CREATED = "Error while creating transaction limits.";
    public static final String TRANSACTION_LIMITS_NOT_UPDATED = "Error while updating transaction limits.";

    public static final String UNABLE_TO_DETERMINE = "Unable to determine MFA configuration";
    public static final String INVALID_SERVICE_KEY = "Invalid Service Key";
    public static final String INVALID_REQUEST_PAYLOAD = "Invalid Request Payload";
    public static final String FAILED_TO_GENRATE_SERVICE_KEY = "Failed to generate Service Key";
    public static final String SECURE_ACCESS_CODE_EXPIRED = "Secure Access Code has expired. Request a resend";
    public static final String INVALID_SECURITY_QUESTIONS = "Invalid Security Questions";
    public static final String ERR_IN_CREATING_RECORD = "Error in creating a Record";

    public static final String INVALID_REQUEST = "Invalid Request.";
    public static final String EXTERNAL_BANK_ERROR = "IDM_CONFIG is not External Bank";
    public static final String DBX_BANK_ERROR = "IDM_CONFIG is not External Bank";
    public static final String ADMIN_CALL_FAILED = "Admin call has been failed";

    public static final String ACH_PREPROCESSOR_COMPANY_VALIDATION = "You are not authorized to use this template.";
    public static final String ACH_PREPROCESSOR_ACCOUNT_VALIDATION = "You are not authorized to use this account";
    public static final String ACH_PREPROCESSOR_TEMPLATE_FETCH_FAILED = "Failed to fetch Template with given details";
    
    public static final String BILLPAY_UPDATE_CONFIRMATION_NUMBER_VALIDATION = "Parameter confirmationNumber is missing";
    public static final String BILLPAY_UPDATE_STATUS_VALIDATION = "Parameter status is missing";
    
}
