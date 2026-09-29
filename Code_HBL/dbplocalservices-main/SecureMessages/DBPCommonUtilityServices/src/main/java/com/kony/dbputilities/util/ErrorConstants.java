package com.kony.dbputilities.util;

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
    public static final String INCORRECT_USERNAME_OR_PASSWORD = "Incorrect username or password.";
    public static final String PASSWORD_CANNOT_RESET = "Password cannot be reset";
    public static final String INVALID_PASSWORD_RESETLINK =
            "Invalid Password Reset Link. Please contact the bank for more information";

    public static final String BACKEND_IDENTIFIER_NOT_FOUND = "BackendIdentifier Not found. ";

    public static final String INVALID_DETAILS = "Invalid parameters please give valid details.";
    public static final String MANDATORY_INPUTFIELDS_ARE_EMPTY = "Mandatory input fields are empty.";

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
    public static final String SALESFORCE_ERROR = "SalesForce is not linked with user";
    public static final String TRIAL_EXPIRED = "Your trial period has expired. Please create another user";
    public static final String PROFILE_LOCKED = "Your profile is locked, it will be unlocked after x mins";
    public static final String PROFILE_SUSPENDED =
            "Sorry, your e-Banking profile has been suspended. Please contact us for more information and to enable your profile.";
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

    public static final String MISSING_TRANSACTION_ID =
            "Transaction_id is missing in the input. please provide valid Transaction_id.";
    public static final String MISSING_TRANSACTION_RECORD_ID =
            "Transaction_record_id is missing in the input. please provide valid Transaction_record_id.";
    public static final String ACH_TRANSACTION_RECORD_FETCH_FAILED =
            "Failed to fetch Transaction records with given details";
    public static final String ACH_TRANSACTION_SUBRECORD_FETCH_FAILED =
            "Failed to fetch Transaction sub records with given details";
    public static final String MISSING_TEMPLATE_NAME =
            "TemplateName is missing in the input. please provide valid TemplateName.";
    public static final String MISSING_TEMPLATE_ID =
            "Template_id is missing in the input. please provide valid Template_id.";
    public static final String MISSING_TEMPLATE_RECORD_ID =
            "TransactionRecord_id is missing in the input. please provide valid TransactionRecord_id.";
    public static final String MISSING_TRANSACTION_TYPE_ID =
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
    public static final String MISSING_CUSTOMER_ID =
            "customerId is missing in the input, which is mandatory";
    public static final String OFFSET_ACCOUNT_UNAUTHORIZED =
            "One of the Offset accounts does not belongs to the logged in user";
    public static final String USER_UNAUTHORIZED = "Logged in User is not authorized to perform this action";
    public static final String USER_UNAUTHORIZED_TO_VIEW_PAYEE = "The logged in user is not authorized to view payees";
    public static final String REQUEST_PROCESS_FAILED =
            "Failed to process the request. Please verify your input and try again";

    public static final String FAILED_TO_FETCH_CONTRACT_CUSTOMER_LIMITS = "Failed to fetch contract coreCustomer limits";
    public static final String ERROR_WHILE_FETCHING_USER_ROLE = "Error while fetching user Role";
    public static final String ERROR_WHILE_FETCHING_ROLE_LIMITS = "Error while fetching role limits";
    public static final String ERROR_WHILE_FETCHING_USER_LIMITS = "Error while fetching user limits";

    public static final String ACH_TEMPLATE_CREATION_FAILED = "Failed to create ACH Template";
    public static final String ACH_TEMPLATE_DELETION_FAILED = "Failed to delete ACH Template";
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

    public static final String BACKEND_INVOCATION_FAILED = "Backend invocation failed to create Transcation";
    public static final String BACKEND_PENDING_TRANSACTION_INVOCATION_FAILED = "Backend invocation failed to create Pending Transcation";
    public static final String TRANSACTION_CREATION_FAILED_AT_BACKEND = "transaction creation is failed at backend";
    public static final String TRANSACTION_EDIT_FAILED_AT_BACKEND = "transaction edit is failed at backend";
    public static final String TRANSACTION_DELETE_FAILED_AT_BACKEND = "transaction delete is failed at backend";
    public static final String TRANSACTION_CANCEL_OCCURRENCE_FAILED_AT_BACKEND =
            "transaction occurrence cancel is failed at backend";

    public static final String TRANSACTION_DENIED_DUE_TO_MAX_LIMIT =
            "transaction is denied due to max transaction limit";
    public static final String TRANSACTION_DENIED_DUE_TO_MIN_LIMIT =
            "transaction is denied due to min transaction limit";
    public static final String TRANSACTION_DENIED_DUE_TO_DAILY_LIMIT = "transaction is denied due to daily limit";
    public static final String TRANSACTION_DENIED_DUE_TO_WEEKLY_LIMIT = "transaction is denied due to weekly limit";
    public static final String TRANSACTION_DENIED_DUE_TO_AUTODENIAL_MAX_LIMIT =
            "transaction is denied due to autodenial max limit";
    public static final String BULK_PAYMENT_LIMIT_EXHAUSTED = "The utilization limit for Bulk Payments has exceeded for the day.";
    public static final String TRANSACTION_DENIED_DUE_TO_AUTO_DENIAL_DAILY_LIMIT =
            "transaction is denied due to autodenial daily limit";
    public static final String TRANSACTION_DENIED_DUE_TO_AUTO_DENIAL_WEEKLY_LIMIT =
            "transaction is denied due to autodenial weekly limit";
    public static final String TRANSACTION_DENIED_DUE_TO_INVALID_APPROVAL_MATRIX_LIMIT =
            "Transaction cannot be executed. Please update the approval matrix of your organization and re-submit the transaction";

    public static final String INVALID_ATTACHMENT_TYPE =
            "File cannot be uploaded. Only files with the following extensions are allowed: .jpeg, .pdf";
    public static final String INVALID_ATTACHMENT_NAME =
            "Incorrect file name. Please upload file with a valid name(only alphanumeric characters are allowed).";
    public static final String INVALID_ATTACHMENT_SIZE = "File exceeds the maximum allowed file size of 2mb.";
    public static final String INVALID_REQUEST = "Invalid Request.";
    public static final String EXTERNAL_BANK_ERROR = "IDM_CONFIG is not External Bank";
    public static final String DBX_BANK_ERROR = "IDM_CONFIG is not External Bank";
    public static final String ADMIN_CALL_FAILED = "Admin call has been failed";

    public static final String ACH_PREPROCESSOR_COMPANY_VALIDATION = "You are not authorized to use this template.";
    public static final String ACH_PREPROCESSOR_ACCOUNT_VALIDATION = "You are not authorized to use this account";
    public static final String ACH_PREPROCESSOR_TEMPLATE_FETCH_FAILED = "Failed to fetch Template with given details";

    public static final String BILLPAY_UPDATE_CONFIRMATION_NUMBER_VALIDATION =
            "Parameter confirmationNumber is missing";
    public static final String BILLPAY_UPDATE_STATUS_VALIDATION = "Parameter status is missing";
    public static final String P2P_MISSING_PERSON_ID = "Parameter personId/toAccountNumber/p2pContact is missing";

    public static final String TRANSACTION_TYPE_NOT_FOUND = "Transaction type not found";
    public static final String FETCH_APPROVERS_FAILED =
            "Unable to fetch approvers for specified account and featureaction";
    public static final String APPROVALMATRIX_UPDATE_FAILED = "Unable to update approval matrix";
    public static final String APPROVALMATRIXTEMPLATE_UPDATE_FAILED = "Unable to update approval matrix template";
    public static final String ERROR_FETCHING_ACTIONIDS = "Error while fetching actionIDs";
    public static final String APPROVALMATRIXTEMPLATE_ASSIGN_FAILED = "Error while assigning approval matrix template records to each account";
    public static final String APPROVALMATRIX_FETCHUSINGCONTRACTID_FAILED = "Error while fetching approval matrix records for each cif";
    
    public static final String INVALID_LIMITS = "Given limits are not mutually exclusive and exhaustive";
    public static final String INVALID_APPROVERS = "Given approvers have no authority";

    public static final String MISSING_FEATURE_ACTION_ID = "Feature Action Id is missing";
    public static final String MISSING_ACH_FILE_ID = "ACH File Id is missing";
    public static final String UNABLE_TO_DETERMINE_C360_CONFIGURATION = "Unable to get C360 Configuration.";

    
    public static final String MISSING_BULKWIRE_FILE_ID = "BulkWireFile_id is missing in the input. please provide valid BulkWireFile_id";
    public static final String MISSING_FILE_ID = "FileId is missing in the input. please provide valid FileId";
    public static final String ERROR_GENERATING_FILE_ID = "Error while generating fileId";
    public static final String INSUFFICIENT_PERMISSIONS_BULKWIRE_FILE_ID = "User does not have access to the requested File ID";
    public static final String MISSING_BULKWIREFILE_EXECUTION_ID = "WireFileExecution_id is missing in the input. please provide valid WireFileExecution_id";
    

    public static final String USER_ALREADY_APPROVED = "User has already approved the request";
    public static final String CANNOT_WITHDRAW = "Cannot Withdraw the Transaction as it is not in Pending state";
    public static final String CANNOT_REJECT = "Cannot Reject the Transaction as it is not in Pending state";

    public static final String INCORRECT_FILE_FORMAT = "Incorrect file format, Please upload file of a valid  format";
    public static final String INCORRECT_FILE_DATA = "Incorrect file contents. Verify the file and please try again.";
    public static final String EMPTY_BULKWIRE_FILE = "Empty file. Please verify the file and try again.";
    public static final String FILE_TRANSACTION_LIMIT_EXCEED =
            "Number of transactions in the file exceeds the allowed limit. Please restrict the number of transactions to X.";
    public static final String MISSING_BULKWIRE_FILE_EXECUTION_ID =
            "BulkWireFile_Execution_id is missing in the input. please provide valid BulkWireFile_Execution_id";

    public static final String INVALID_FILE = "Invalid File";
    public static final String FROMACCOUNTNUMBER_SAME_AS_TOACCOUNTNUMBER =
            "From Account Number is same as To Account Number";
    public static final String PROVIDE_MANDATORY_FIELDS = "Please provide mandatory fields";

    public static final String ACH_FILE_PARSE_ERROR = "ACH file could not be parsed.";
    public static final String UNBALANCED_ACH_FILE =
            "Unbalanced ACH File is not Supported. Please Provide a Balanced ACH File.";
    public static final String INVALID_EFFECTIVE_DATE =
            "Effective Date mentioned in ACH File is not a valid date. Please provide a valid future Effective Date";
    public static final String ACH_FILE_VALIDATION_ERROR =
            "Insufficient permission for file upload on the listed debit / credit accounts. Please re-check your file or contact your business administrator for more information";
    public static final String FETCH_ACHFILERECORD_FAILED =
            "Unable to fetch achfilerecords for specified achfileId and feature action";
    public static final String FETCH_ACHFILESUBRECORD_FAILED =
            "Unable to fetch achfilesubrecords for specified achfileId and feature action";
    public static final String OFFSET_AMOUNT_IS_NOT_BALANCED =
            "Offset Amount present in ACH File is not balancing the file. Please re-check your file.";
    public static final String INVALID_REQUEST_TYPE = "Request type provided in ACH File is not valid.";
    public static final String ACH_TEMPLATE_EDIT_FAILED = "Failed to update existing template";
    public static final String ACH_INVALID_FILENAME = "Invalid File Name";
    public static final String ACH_USER_UNAUTHORIZED = "You are not authorized to perform this action";
    public static final String ACH_NO_DEBIT_CREDIT_ERROR =
            "Both debit and credit amount cannot be 0. Please re-check your file.";

    public static final String INVALID_RECIPIENT_CATEGORY =
            "TemplateRecipientCategory must be any of EXISTINGRECIPIENT, MANUALLYADDED, EXTRACTEDFROMFILE";
    public static final String TEMPLATE_EMPTY_FIELDS =
            "BulkWireTemplateName,defaultFromAccount and defaultCurrency are mandatory fields";
    public static final String DUPLICATE_TEMPLATENAME =
            "A Template with this Name already exists. Please use a different name";
    public static final String PAYEEID_MANDATORY = "payeeId is mandatory for EXISTINGRECIPIENT category";
    public static final String DB_FETCH_ERROR = "Error occured while fetching data from Database";

    public static final String MISSING_BULKWIRE_TEMPLATE_ID =
            "BulkWireTemplateId is missing in the input. please provide valid BulkWireTemplateId";
    public static final String INSUFFICIENT_PERMISSIONS_BULKWIRE_TEMPLATE_ID =
            "User does not have access to the requested Template ID";
    public static final String INAPPROPRIATE_REQUEST_PAYLOAD_BULKWIRETYPE =
            "Value for bulkWireType is not appropriate, it can be either File or Template only";
    public static final String INAPPROPRIATE_REQUEST_PAYLOAD = "Inappropriate request payload";
    public static final String INVALID_ACCOUNTNUMBER_BULKWIRETRANSFER = "Invalid account number for the user";

	
	public static final String APPROVERS_MANDATORY = "Each Limit must have atleast one approver";
	
	public static final String USER_ALREADY_LINKED = "Linking profiles failed. User already has combined access";
	public static final String USER_NOT_LINKED = "Delinking profiles failed. User doesn't have combined access";
	public static final String USERLINKING_BACKEND_FAILED = "Backend failed while linking the user profiles";
	public static final String USERDELINKING_BACKEND_FAILED = "Backend failed while delinking the user profiles";
	public static final String PARTY_FETCH_FAILED = "Failed to fetch user details from party";
	public static final String CARD_FETCH_FAILED = "Failed to fetch card details";
	public static final String COMMUNICATION_DETAILS_FETCH_FAILED = "Failed to fetch communication details";
	public static final String COMMUNICATION_DETAILS_EDIT_FAILED = "Failed to update communication details";
	public static final String CARD_EDIT_FAILED = "Failed to update card details";
	public static final String NO_PERMISSION_TO_ADD_DOMESTIC_RECIPIENTS = "You do not have the permission to add domestic recipients";
	public static final String NO_PERMISSION_TO_ADD_INTERNATIONAL_RECIPIENTS = "You do not have the permission to add international recipients"; 
	public static final String USER_DETAILS_MISMATCH = "Basic details of the users are not matching";
	public static final String USER_NOT_SIGNATORY = "Business user is not a signatory";
	public static final String COMBINED_ALERT_SERVICES_FAILED = "Backend failed while combined alert services";
	public static final String SENDING_ALERT_FAILED = "Failed to send alert to the user";
	public static final String FAILED_TO_FETCH_ORGANISATIONEMPLOYEE = "Failed to fetch organisation employee details";
	public static final String FAILED_TO_UPDATE_ORGANISATIONEMPLOYEE = "Failed to update organisation employee details";
	public static final String FAILED_TO_FETCH_BACKENDIDENTIFIER = "Failed to fetch backend identifier details";
	public static final String FAILED_TO_UPDATE_BACKENDIDENTIFIER = "Failed to update backend identifier details";
	public static final String SENDING_ACTIVATION_LINK_FAILED = "Failed to send activation link to the user";
	
	public static final String FAILED_TO_CREATE_CUSTOM_VIEW = "Failed to create custom view.";
	public static final String FAILED_TO_DELETE_CUSTOM_VIEW = "Failed to delete custom view.";
	public static final String FAILED_TO_EDIT_CUSTOM_VIEW = "Failed to edit custom view.";
	public static final String FAILED_TO_GET_CUSTOM_VIEW = "Failed to get custom view.";
	
	public static final String BULK_PAYMENTS_FILE_UPDATE_ERROR = "Unable to update the status of the uploaded file";
	public static final String BULK_PAYMENTS_FILE_DELETE_ERROR = "Unable to delete the uploaded file";
	public static final String BULK_PAYMENTS_FILE_PARSE_ERROR = "Failed to parse the uploaded file";
	public static final String BULK_PAYMENTS_FILE_FETCH_INPUT_ERROR = "Error occured while fetching the input params";
	public static final String BULK_PAYMENTS_FILE_CREATE_ERROR = "Unable to Create Bulk Payment File";
	public static final String EMPTY_BULK_PAYMENTS_FILE = "Empty file";
	public static final String EMPTY_BULK_PAYMENTS_FILE_DESCRIPTION = "Empty Description";
	public static final String EMPTY_BULK_PAYMENTS_BATCHMODE = "Processing Mode is not selected during file upload";
	public static final String EMPTY_BULK_PAYMENTS_FILE_BATCHMODE = "File does not have Batch/Processing mode";
	public static final String FAILED_TO_STORE_BULK_PAYMENTS_FILE = "Unable to store the file info at Infinity";
	public static final String FAILED_TO_STORE_BULK_PAYMENTS_FILE_BACKEND = "Unable to store the file info at Backend";
	public static final String INVALID_BULK_PAYMENTS_FILE_BATCHMODE = "The Processing mode specified in the file upload is not matching with the batch process selected. Kindly correct the file and reupload or select the right batch processing mode";
	public static final String ERROR_IN_PO_STATUS = "Unable to Proceed as the underlying Payments are in Intermediate or Unauthorised Status.";
	
	public static final String ERROR_WHILE_FETCHING_LIMITGROUP_LIMITS = "Error while fetching limits for Limit Group";
	public static final String ERROR_WHILE_FETCHING_LIMITGROUPID = "Error while fetching the limit group id";
	
	public static final String BULK_PAYMENTS_PO_CREATE_ERROR = "Failed to create payment order";
	public static final String BULK_PAYMENTS_PO_FETCH_ERROR = "Failed to fetch payment orders";
	public static final String BULK_PAYMENTS_PO_DELETE_ERROR = "Failed to delete the payment order";
	public static final String BULK_PAYMENTS_PO_EDIT_ERROR = "Failed to edit payment order";
	
	public static final String BULK_PAYMENTS_RECORD_CREATE_ERROR = "Failed to create Bulk Payment Record";
	public static final String BULK_PAYMENTS_RECORD_FETCH_ERROR = "Failed to fetch Bulk Payment Record";
	public static final String BULK_PAYMENTS_RECORD_DELETE_ERROR = "Failed to delete the Bulk Payment Record";
	public static final String BULK_PAYMENTS_RECORD_EDIT_ERROR = "Failed to edit Bulk Payment Record";
	
	public static final String FILTER_RESPONSE_FAILED_ERROR = "Failed to filter response";
	public static final String INVALID_BULKPAYMENTS_FILE_NAME = "You have added an Invalid file. Please upload file with correct filename and extension .csv or .xml";
	public static final String INVALID_BULKPAYMENTS_FILE_SIZE = "The file size exceeds the maximum limit of 20 mb. Please retry with a lesser file size";

	public static final String FETCH_SAMPLE_FILES_FAILED_ERROR = "Failed to filter response";
	public static final String FETCH_ONGOING_PAYMENTS_ERROR = "Failed to fetch ongoing payments";
	public static final String FETCH_UPLOADED_FILES_FAILED_ERROR = "Failed to fetch bulk payment uploaded files";
	public static final String UPLOAD_FAILED_ERROR = "Error occured while invoking uploadBulkPaymentFile";
	public static final String UPLOAD_FAILED_ERROR_BACKEND = "Error occured while invoking uploadBulkPaymentFile at backend";
	
	public static final String INVALID_BULKPAYMENTS_RECORDID ="Invalid Payload. Please provide valid details.";
	public static final String UNAUTHORISED_USER_BULK_PAYMENT_UPLOAD = "Logged in user is not authorised to upload a bulk payment file with the fromAccount mentioned in the file";
	public static final String UNAUTHORISED_USER_BULK_PAYMENT_REVIEW = "Logged in user is not authorised to review a bulk payment with the fromAccount";
	public static final String BULKPAYMENTS_FAILED_ERROR_BACKEND = "Error occured while initiating payments for BulkPayment file at backend";
	public static final String CANCEL_BULKPAYMENTS_RECORD_ERROR = "Error occured while cancelling Bulk payment record";
	public static final String UNAUTHORISED_USER_CANCEL_BULKPAYMENT_RECORD = "Logged in user is not authorised to cancel a bulk payment record with the record ID mentioned";
	
	public static final String BULK_PAYMENTS_REVIEW_ERROR = "Failed to review Bulk Payment Record";
	public static final String FAILED_TO_FETCH_BULK_PAYMENT_RECORD_DETAILS_BACKEND = "Failed to fetch record details from backend";
	public static final String INVALID_TOTAL_AMOUNT = "Total amount of the record is invalid";

	public static final String UPDATE_BULKPAYMENTS_RECORD_ERROR = "Failed to update bulk payments record";

	public static final String FUNDING_ACCOUNT_ACCESS_DENIED = "User doesn't have the access to the mentioned Account ID";
	public static final String SAVINGSPOT_ACCESS_DENIED = "User doesn't have the access to the mentioned Savings Pot ID";
	public static final String SAVINGSPOT_CLOSED = "Savings Pot that the user is trying to access is already closed";
	public static final String INSUFFICIENT_BALANCE_IN_FUNDING_ACCOUNT = "Funding Account does not have sufficient balance to fund the Savings Pot with ";
	public static final String INSUFFICIENT_BALANCE_IN_SAVINGSPOT = "Savings Pot does not have sufficient balance to withdraw ";
	public static final String MANDATORY_BULKPAYMENTS_FROMACCOUNT_DESCRIPTION = "fromAccount or description is mandatory field";
	public static final String INVALID_BULKPAYMENTS_DESCRIPTION = "Invalid bulkPayment Description";
	public static final String INVALID_BULKPAYMENTS_RECIPIENTNAME = "Invalid Recipient Name. Please provide valid details.";
	public static final String INVALID_BULKPAYMENTS_CURRENCY = "Invalid Currency. Please provide valid details.";
	public static final String INVALID_BULKPAYMENTS_AMOUNT = "Invalid Amount. Please provide valid details.";
	public static final String INVALID_BULKPAYMENTS_PAYMENTORDERID = "Invalid PaymentOrderId . Please provide valid details.";
	public static final String FAILED_TO_FETCH_BULKPAYMENTS_RECORDS = "Error occurred while fetching bulk payment requests from Approval Queue";
	public static final String FAILED_TO_FETCH_RECORDS_REVIEWD_BY_ME_AND_IN_APPROVALQUEUE = "Error occured while invoking fetchRecordsReviewedByMeAndInApprovalQueue";
	public static final String FAILED_TO_FETCH_RECORDS_WAITING_FOR_MY_APPROVAL = "Error occured while invoking fetchRecordsWaitingForMyApproval";
	public static final String FAILED_TO_FETCH_RECORD_HISTORY_REVIEWED_BY_ME = "Error occured while invoking fetchRecordHistoryReviewedByMe";
	public static final String FAILED_TO_FETCH_RECORD_HISTORY_ACTED_BY_ME = "Error occured while invoking fetchRecordHistoryActedByMe";
	public static final String FUNDING_ACCOUNT_IS_BUSINESS_ACCOUNT = "Funding account is a business account, savings pot cannot be created for a business account";
	public static final String FAILED_TO_FETCH_APPROVERS = "Error occured while invoking FetchApproversOperation";
	public static final String MULTIPLE_PAYMENT_BLOCKS_ERROR = " The file contains multiple payment information blocks. Only one payment information block per file is allowed";
	public static final String BULK_PAYMENTS_DOWNLOAD_FAILED = "Error occured while downloading the Bulk payment file";
	public static final String INITIATE_BULK_PAYMENTS_DOWNLOAD_FAILED = "Error occured while initiating the bulk payment download";
	public static final String FILE_ID_GENERATION_FAILED = "File Id generation failed";
	public static final String BULK_PAYMENTS_FILE_GENERATION_FAILED = "Error occured while generating the Bulk payment file";
	public static final String FAILED_TO_FETCH_BENEFICIARY_NAME = "Error occurred while fetching  beneficiary details from the account";
	public static final String INVALID_ACCOUNT_ID = "Invalid Account number";
	public static final String INVALID_IBAN = "Invalid IBAN";
	public static final String INVALID_SWIFT_BIC = "Invalid Swift/BIC code";
	public static final String FAILED_TO_FETCH_BANK_DETAILS = "Failed to fetch Beneficiary bank details";
	
	public static final String TRANSACTIONS_RDC_CREATE_ERROR = "Failed to create RDC order";

	public static final String ERROR_IN_INVOKING_CREATEBULKPAYEMENTTEMPLATE = "Error occured while invoking CreateBulkPaymentTemplate";
	public static final String INVALID_TEMPLATENAME = "Invalid template Name";
	public static final String INVALID_CURRENCY = "Invalid Currency";
	public static final String INVALID_PROCESSINGMODE = "Invalid Processing Mode";
	public static final String UNAUTHORISED_USER_BULKPAYMENT_TEMPLATE_CREATE = "User unauthorized to create Bulk Payment Template";
	public static final String UNAUTHORISED_USER_BULKPAYMENT_TEMPLATE_DELETE = "User unauthorized to delete Bulk Payment Template";
	public static final String UNABLE_TO_STORE_TEMPLATE_AT_BACKEND = "Unable to store the bulkpayment template info at Backend";
	public static final String BULKPAYMENT_TEMPLATE_DELETION_FAILED = "BulkPayment Template deletion failed";
	public static final String BULKPAYMENT_TEMPLATE_EDIT_FAILED = "BulkPayment Template edit failed";
	public static final String INVALID_FROMACCOUNT = "Invalid fromAccount.";
	public static final String ERROR_IN_INVOKING_FETCHBULKPAYEMENTTEMPLATE = "Error occured while invoking fetchBulkPaymentTemplates";
	public static final String UNAUTHORISED_USER_BULKPAYMENT_TEMPLATE_FETCH = "User unauthorized to fetch Bulk Payment Template";
	public static final String BULK_PAYMENTS_TEMPLATES_FETCH_ERROR = "Failed to fetch bulkpayments templates";
	public static final String DOWNLOAD_ERROR = "Error while downloading the file";
	public static final String INVALID_TEMPLATEID = "Invalid templateId.";
	public static final String ERROR_IN_CREATING_RECORDS = "Error occured while creating records in request tables";
	public static final String ERROR_IN_INVOKING_FETCHBULKPAYEMENTTEMPLATEBYID = "Error occured while invoking fetchTemplateDetailsByTemplateId";
	public static final String ERROR_IN_INVOKING_FETCHTEMPLATEPOSBYID = "Error occured while invoking fetchPOsByTemplateId";
	public static final String UNAUTHORISED_USER_BULKREQUEST_CREATE = "User unauthorized to create Bulk request";
	public static final String ERROR_IN_PARSING_INPUT_RECORDS = "Error occurred while parsing the input records";

	public static final String ERROR_IN_UPDATE_RECORDS = "Error occured while updating data";
	public static final String MISSING_CONTRACTID = "Contract Id is a mandatory parameter";
	public static final String INVALID_DISABLEFLAG = "Invalid disable flag";
	public static final String MISSING_DISABLEFLAG = "disable flag is a mandatory parameter";
	public static final String EMPTY_CIFLIST = "No CIF found for given contract id";
	public static final String ERROR_IN_APPROVALMATRIX_STATUS_UPDATE = "Error occurred while changing the status of Approval Matrix";
	public static final String NO_CONTRACT_CIF_ASSOCIATION = "Provided Cif is not associated to this Contract";
	public static final String ERROR_FETCHING_STATUS ="Error occured while fetching status of Approval Matrix";
	
	public static final String INVALID_DATE = "The execution date defined in the file is in Past, please correct the date in the file and re-upload";

    public static final String MISSING_BULKWIRE_TEMPLATE_EXECUTION_ID =
            "BulkWireTemplate_Execution_id is missing in the input. please provide valid BulkWireTemplate_Execution_id";
    public static final String USER_ACCOUNT_UNAUTHORIZED =
            "Logged in User is not authorized to perform transfer with this Account";
    public static final String MISSING_CUSTOMROLE_NAME = "Unable to find customRoleName";
    public static final String MISSING_PARENTROLE_ID = "Unable to find parentRole_id";
    public static final String MISSING_CUSTOMROLE_ID = "Unable to find customRoleId";
    public static final String INVALID_CUSTOM_ROLE_NAME_OR_DESCRIPTION = "Invalid custom role name or description";
    public static final String INVALID_TRANSACTION_LIMITS = "Invalid transaction limits";
    public static final String UNIQUE_CUSTOM_ROLE_NAME = "Custom role name cannot be duplicated";
    public static final String INVALID_ORG_ACCOUNTS = "Invalid organisation accounts";
    public static final String EMPTY_ACCOUNT_LIST = "Account list is empty";
    public static final String PAYEE_CREATION_FAILED = "Failed to create payee";
    public static final String MISSING_PAYEE_ID = "Payee Id is missing";
    public static final String PAYEE_EDIT_FAILED = "Failed to edit payee";
    public static final String PAYEE_DELETE_FAILED = "Failed to delete payee";
    public static final String PAYEE_FETCH_FAILED = "Failed to fetch payees";
    public static final String PAYEE_BACKEND_FETCH_FAILED = "Failed to fetch payee record with given details";
    public static final String EMPTY_CIF = "Please provide cif value to which payee needs to be associated";
    public static final String ALL_THE_ACCOUNTS_DOESNOT_BELONG_TO_SAME_CIF = "All the given from accounts doesn't belong to same CIF";
    public static final String DUPLICATE_PAYEE = "Payee with same details is already associated with one of the input cif.";
    
 // Cheque Management Codes
    public static final String BACKEND_FAILED = "Backend Failed";
    public static final String FAILED_IN_POSTPROCESSOR = "Failed in post processor";
    public static final String FAILED_TO_DELETE_REQUESTID = "Failed to delete request id";
    public static final String FAILED_TO_FETCH_CHEQUE_BOOKS = "Unable to get cheque book requests from OMS";
    public static final String INVALID_PAYLOAD = "Invalid Payload";
    public static final String NO_RECORDS_FROM_TRANSACT = "No records were found that matched the selection criteria";
    public static final String ACCOUNT_NOT_FOUND = "FA-000001 - Account Not Found";
    public static final String ACCOUNT_NUMBER_NOT_RELATED_TO_CUSTOMER = "Please Provide Valid Account Number,Account is not related to customer";
    public static final String AUTHORIZATION_FAILED = "Authorization Failed";
    public static final String FAILED_TO_CREATE_SERVICE_REQUEST = "Unable to create order. Failed to find service request id %s";
    public static final String FAILED_TO_CREATE_CHEQUE_BOOK_REQUEST = "Unable to create order for cheque book request";
    public static final String FAILED_TO_CREATE_STOP_PAYMENT_REQUEST = "Unable to create order for Stop payment request";
    public static final String FAILED_TO_FETCH_STOP_PAYMENTS = "Unable to get stop payment requests";
    public static final String FAILED_TO_GET_CUSTOMER = "Unable to get customer id and accounts";
    public static final String FAILED_TO_VALIDATE_CHEQUE_BOOK_REQUEST = "Error occured while validating cheque book request";
    public static final String CHEQUE_VALIDATION_FAILED = "Cheque Book Validation Failed";
    public static final String FAILED_TO_VALIDATE_STOP_PAYMENT_REQUEST = "Error occured while validating stop payment request";
    public static final String STOP_PAYMENT_VALIDATION_FAILED = "Stop Payment Validation Failed";
    public static final String BULKPAYMENT_EXPIRED_ERROR = "The execution date has expired and this cannot be processed further";
    public static final String PAYMENT_DATE_ERROR = "Record do not have payment date.";
	public static final String SELF_APPROVAL_NOT_ALLOWED= "Failed to invoke the transaction approval request services for self approval"; //Added as part of ADP-2810
	public static final String FAILED_TO_FETCH_APPLICATION_RECORD = "Failed to fetch applicaton record details"; //Added as part of ADP-2810
	public static final String FAILED_TO_FETCH_DBP_HOST_URL="Failed to fetch DBP_HOST_URL";
	public static final String FAILED_BASE64_FETCH_MOCK="Failed to fetch base64 content from mock database";
	public static final String FAILED_DELETE_FROM_MOCK="Failed to delete attachment from mock database";
	public static final String TRANSACTION_EXECUTION_FAILED = "Transaction execution has failed"; //Added as part of ADP-2810
	public static final String NULL_FEATURE_ACTION_ERROR = "Feature Action ID cannot be null. Please provide featuer action id.";
	public static final String FAILED_TO_FETCH_ACCOUNT_STATEMENTS="Error occured while fetching data from accountsstatementfiles ";
	public static final String FAILED_TO_GENERATE_FILE="Unknown exception while Generating  file";
	public static final String FAILED_TO_UPDATE_ACCOUNT_STATEMENTS="Failed to update accountstatements file table";
	public static final String FAILED_TO_CREATE_ACCOUNT_STATEMENTS="Failed to create accountstatements file table";
	public static final String UNKNOWN_ERROR="Unknown error occured while generating statement";
	public static final String CIF_VALIDATION = "Changed Account Number doesn't belong to the previous CIF";
	public static final String FAILED_TO_UPDATE_SERVICEDEFINTIION_LIMITSANDPERMISSIONS = "Failed to update service definition limits and permissions";
	public static final String FAILED_TO_UPDATE_CUSTOMERROLE_LIMITSANDPERMISSIONS = "Failed to update customer role limits and permissions";
	public static final String LANGUAGECODE_IS_EMPTY = "LanguageCode is empty.";
	public static final String CHEQUE_MANAGEMENT_APPROVE_FAILED = "Cheque management Approve failed";
	public static final String CHEQUE_MANAGEMENT_WITHDRAW_FAILED = "Cheque management withdraw failed";
	public static final String NOT_ASSOCIATED_TO_SIGNATORY_GROUP = "User is not associated to signatory group";
	
	public static final String ACC_PERMISSION_CHECK_EXCEP_MSG = "Unable to fetch the details";
	public static final String INVALID_ACCNUM ="Invalid or Missing input accountNumber";
	public static final String INVALID_PERMISSION ="Invalid or Missing input permissions";
	public static final String FAILED_VALIDATION_PERMISSION = "Failed to validate account permission";
	public static final String FAILED_LIMITS_VALIDATION = "Failed to validate limits";
	public static final String FAILED_UPDATING_BACKENDID = "Failed to update Backend Id for the request";
	public static final String FAILED_FETCHING_TRANSACTION = "Failed to fetch transaction details";
	public static final String FAILED_CREATING_DBXENTRY = "Error occured while creating entry into the DBX table";
	public static final String FAILED_VALIDATION_FOR_APPROVALS = "Error occured while validating for approvals";
	public static final String ERROR_UPDATING_BACKENDID = "Error while updating Backend Id in Approval Queue";
	public static final String ERROR_APPROVING_TRANSACTION_AT_BACKEND = "Error while approving in the backend";
	public static final String VALID_SERVICE_NAME = "Please Pass Valid Service Name";
	public static final String FAILED_TO_FETCH_CONVERTED_AMOUNT = "Failed to fetch converted amount";
	public static final String INVALID_AMOUNT_VALUE = "failed to parse amount value";
	
	public static final String INVALID_ORDERTYPE_ACCESS = "Logged in User is not authorized to view %s orders";
	public static final String ERROR_IN_COUNT_SERVICE = "Error occurred while fetching requests for counts";
	
	public static final String NO_APPROVALMATRIX_ENTRY_FOUND = "No approvalMatrix entry found for this featureactionId";
	public static final String NO_LIMITRANGE_FOR_FEATUREACTIONID = "No limitranges found for featureactionId";
	public static final String APPROVALMATRIX_NOT_SET = "Approval matrix is not set for this contract-CoreCustomerId, account and actionId";
	public static final String INVALID_APPROVALMATRIX_FOR_MULTIPLE_UPTO_RANGES = "Invalid approval matrix entries for amount ranges, multiple Upto ranges cannot exist";
	public static final String INVALID_APPROVALMATRIX_FOR_MULTIPLE_ABOVE_RANGES = "Invalid approval matrix entries for amount ranges, multiple Above ranges cannot exist";
	public static final String FAILED_TO_FETCH_APPROVER_IDS = "Failed to fetch approvers for the request";
	public static final String FETCHED_APPROVER_IDS_NOT_VALID = "Invalid approvers present in the approval matrix rules";
	public static final String NO_APPROVALMATRIX_ID_FOUND_FOR_REQUEST = "No matching Approval matrix rule found for this request";
	public static final String INVALID_BENEFICIARY_ACCOUNT = "The account number/name entered does not match an account on our records. Check the account number and account name and try again.";
	public static final String INVALID_IBAN_OR_SWIFT = "IBAN or Swift Code is not valid";
	
	public static final String SIGNATORY_GROUP_NAME_MANDATORY = "Signatory group name is mandatory";
	public static final String SIGNATORY_GROUP_NAME_SPECIAL_CHARS = "Signatory group name or description should not contain special characters";
	public static final String CONTRACT_ID_MANDATORY = "Contract id is mandatory";
	public static final String CORECUSTOMER_ID_MANDATORY = "Core customer id is mandatory";
	public static final String SIGNATORIES_MANDATORY = "Signatories are mandatory";
	public static final String SIGNATORY_ID_MISSING = "Signatory group id is mandatory";
	public static final String FAILED_TO_FETCH_SIGNATORY_GROUPS = "Failed to fetch signatory groups";
	public static final String SIGNATORY_GROUP_NOT_ELIGIBLE = "Signatory group can't be deleted as there are pending transactions for its approval or it's configured in Approval Matrix";
	public static final String FAILED_TO_FETCH_CUSTOMERSIGNATORY = "Failed to fetch customer signatory details";
	
	public static final String TRANSACTION_ID_MISSING = "Transaction ID is missing";
	public static final String FEATURE_ACTION_ID_MISSING = "Feature Action Id is missing";
	public static final String APPROVER_USERID_MISSING = "Approver UserId is missing";
	public static final String FEATUREACTION_IS_INCORRECT = "Feature Action is incorrect";
	public static final String DENIED_ACCESS_TO_CIF_AND_CONTRACT = "User doesn't have permission to access data related to the given coreCustomerId and contractId";
	public static final String NO_SIGNATORYGROUPS_FOUND = "Signatory Group doesnot exist with the given Id";
	public static final String DENIED_ACCESS_TO_SIGNATORYGROUP = "User doesn't have permission to access data related to the given Signatory Group";
	public static final String FAILED_TO_FETCH_CORECUSTOMERS = "Failed to fetch CoreCustomers for the user";
	public static final String BLOCK_APPROVED_APPLICANT = "We are not authorized to sign in with the credentials. Please look at your email for instructions on how to set your digital credentials or use our Enroll option at the sign-in screen.";
	public static final String UPDATE_SIGNATORYGROUP_FAILED = "Failed to update Signatory group";
	public static final String APPROVALMATRIX_NOT_CONFIGURED = "Approval Matrix is not configured, Please configure and try again";
	public static final String ERROR_WHILE_GENERATING_JWT_TOKEN = "Error while generating JWT token";
	
	public static final String MIXED_APPROVALMATRIX_FOUND = "Transaction contains user based and signatory group based approval matrix. Please Configure correctly and try again.";
}
