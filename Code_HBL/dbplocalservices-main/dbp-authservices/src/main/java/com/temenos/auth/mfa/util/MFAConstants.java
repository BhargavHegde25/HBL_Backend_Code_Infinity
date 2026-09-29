package com.temenos.auth.mfa.util;

public class MFAConstants {

    public static final String SECURE_ACCESS_CODE = "SECURE_ACCESS_CODE";
    public static final String SECURITY_QUESTIONS = "SECURITY_QUESTIONS";
    public static final String MFA_TYPE_ID = "mfaTypeId";
    public static final String DISPLAY_ALL = "DISPLAY_ALL";
    public static final String DISPLAY_PRIMARY = "DISPLAY_PRIMARY";
    public static final String DISPLAY_NO_VALUE = "DISPLAY_NO_VALUE";
    public static final String SAC_CODE_LENGTH = "SAC_CODE_LENGTH";
    public static final String MFA_CONFIGURATION = "mfaConfigurations";
    public static final String SQ_NUMBER_OF_QUESTION_ASKED = "SQ_NUMBER_OF_QUESTION_ASKED";
    public static final String SAC_CODE_EXPIRES_AFTER = "SAC_CODE_EXPIRES_AFTER";
    public static final String LOCK_USER = "LOCK_USER";
    public static final String MAX_FAILED_ATTEMPTS_ALLOWED = "MAX_FAILED_ATTEMPTS_ALLOWED";
    public static final String SAC_MAX_RESEND_REQUESTS_ALLOWED = "SAC_MAX_RESEND_REQUESTS_ALLOWED";
    public static final String LOGOUT_USER = "LOGOUT_USER";
    public static final String SECURITY_KEY = "securityKey";
    public static final String SERVICE_KEY = "serviceKey";
    public static final String SERVICE_NAME = "serviceName";
    public static final String OTP = "OTP";
    public static final String SECURITY_QUESTION = "securityQuestions";
    public static final String MFA_ATTRIBUTES = "MFAAttributes";
    public static final String RETRY_COUNT = "retryCount";
    public static final String FREQUENCY_VALUE = "frequencyValue";
    public static final String IS_MFA_REQUIRED = "isMFARequired";
    public static final String INPUT_CONTENT = "inputContent";
    public static final String EMAIL_SUBJECT = "emailSubject";
    public static final String EMAIL_BODY = "emailBody";
    public static final String SMS_TEXT = "smsText";
    public static final String TRANSACTION_TYPE = "transactionType";

    public static final String OPERATION = "operation";
    public static final String SUB_OPERATION = "subOperation";

    public static final String MFA_KEY = "mfaKey";
    public static final String MFA_Value = "mfaValue";
    public static final String SAC_PREFERENCE_CRITERIA = "SAC_PREFERENCE_CRITERIA";
    public static final String FREQUENCYTYPE_ID = "frequencyTypeId";
    public static final String SECURITY_QUESTIONS_DB = "securityQuestions";
    public static final String IS_VERIFIED = "isVerified";
    public static final String MFA_USER_ID = "User_id";

    public static final String ACH_TYPE_IDENTIFIER = "TransactionType_id";
    public static final String ACH_TYPE_IDENTIFIER_VALUE_COLLECTION = "1";
    public static final String ACH_TYPE_IDENTIFIER_VALUE_PAYMENT = "2";
    public static final String ACH_SERVICE_ID_COLLECTION = "ACH_COLLECTION_CREATE";
    public static final String ACH_SERVICE_ID_PAYMENT = "ACH_PAYMENT_CREATE";
    public static final String ACH_FILE_UPLOAD = "ACH_FILE_UPLOAD";
    public static final String ACH_TEMPLATE_ID = "Template_id";
    public static final String ACH_FILE_CONTENTS = "Contents";
    public static final String ACH_DEBIT_ACCOUNT = "DebitAccount";
    public static final String ACH_TOTAL_AMOUNT = "TotalAmount";
    public static final String ACH_TRANSACTION_AMOUNT = "Amount";
    public static final String ACH_COMPANY_ID = "Company_id";
    public static final String ACH_TRANSACTION_RECORDS = "Records";
    public static final String ACH_TRANSACTION_SUB_RECORDS = "SubRecords";

    public static final String SERVICE_ID_PRELOGIN = "SERVICE_ID_100";
    public static final String SERVICE_ID_LOGIN = "LOGIN";
    public static final String USERNAME = "UserName";
    public static final String SERVICE_ID_POSTLOGIN = "SERVICE_ID_125";
    public static final String SERVICE_ID_USERNAME_UPDATE = "USERNAME_UPDATE";
    public static final String SERVICE_ID_PASSWORD_UPDATE = "PASSWORD_UPDATE";
    public static final String SERVICE_ID_CARD_MANAGEMENT = "CARD_MANAGEMENT";
    public static final String VERIFY = "verify";
    public static final String RESEND = "resend";
    public static final String REQUEST = "request";

    /**
     * Audit Engine Constants
     */
    public static final String LOGIN = "LOGIN";
    public static final String LOGIN_ATTEMPT_MFA = "LOGIN_ATTEMPT_MFA";
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    public static final String SID_EVENT_SUCCESS = "SID_EVENT_SUCCESS";
    public static final String SID_EVENT_FAILURE = "SID_EVENT_FAILURE";
    public static final String MFA_TYPE = "mfatype";
    public static final String MFA_STATE = "mfastate";

    public static final String CUSTOMER_COMMUNICATION = "customerCommunication";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String UNMASKED = "unmasked";
    public static final String OTP_OTP = "otp";

    public static final String OTP_VALIDITY_IN_MINS = "OTP_VALIDITY_IN_MINS";
}
