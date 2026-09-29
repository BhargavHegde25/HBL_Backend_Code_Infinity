/**
 * 
 */
package com.infinity.dbx.temenos.constants;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public interface EnrollmentConstants {
	/*
	 * General Constants
	 */
	String PARAM_LAST_NAME = "LastName";
	String PARAM_SSN = "Ssn";
	String PARAM_DOB = "DateOfBirth";
	String PARAM_BODY_DS = "body";
	String PARAM_COMMUNICATION_DEVICES_DS = "communicationDevices";
	String PARAM_SMS_NUMBER = "smsNumber";
	String PARAM_EMAIL = "email";
	String PARAM_CONTACT_INFORMATION = "contactInformation";
	String PARAM_EMAIL_ADDRESS = "emailAddress";
	String PARAM_PHONE_NUMBER = "phoneNumber";
	String PARAM_PERSONAL_INFORMATION = "personalInformation";
	String PARAM_USERNAME = "userName";
	String PARAM_FIRST_NAME = "firstName";
	String PARAM_MIDDLENAME = "middleName";
	String PARAM_LAST_NAME_CAMEL_CASE = "lastName";
	String PARAM_DATE_OF_BIRTH = "dateOfBirth";
	String PARAM_DATE_OF_BIRTH_SMALL_O = "dateofBirth";
	String PARAM_SSN_UPPER_CASE = "SSN";
	String PARAM_ADDRESS_INFORMATION = "addressInformation";
	String PARAM_ADDRESS_LINE_1 = "addressLine1";
	String PARAM_ADDRESS_LINE2 = "addressLine2";
	String PARAM_CITY = "city";
	String PARAM_STATE = "state";
	String PARAM_ZIP_CODE = "zipcode";
	String PARAM_COUNTRY = "country";
	String PARAM_BACKEND_IDENTIFIERS_INFO = "backendIdentifierInfo";
	String PARAM_SEQUENCE_NUMBER = "sequenceNumber";
	String PARAM_BACKEND_ID = "BackendId";
	String PARAM_BACKEND_TYPE = "BackendType";
	String PARAM_IDENTIFIER_NAME = "identifier_name";
	String PARAM_CUSTOMER_NAME = "customerName";
	String PARAM_SOCIAL_SECURITY_ID = "socialSecurityId";
	String PARAM_STREETS = "streets";
	String PARAM_STREET = "street";
	String PARAM_COUNTRIES = "countries";
	String PARAM_ADDRESS_CITIES = "addressCities";
	String PARAM_ADDRESS_CITY = "addressCity";
	String PARAM_POST_CODES = "postCodes";
	String PARAM_POST_CODE = "postCode";
	String PARAM_CUSTOMER_ID = "customerId";
	String PARAM_USER_NAME_PASCAL_CASE = "UserName";
	String PARAM_ID = "";
	String PARAM_CUSTOMER_ID_PASCAL_CASE = "Customer_id";
	String PARAM_CUSTID = "CustId";
	String CONSTANT_T24 = "T24";
	String CONSTANT_CUSTOMER = "customer";
	String PARAM_CORE_COMMUNICATION = "coreCommunication";
	String PARAM_IS_USER_EXISTS = "isUserExists";
	String CONSTANT_T24_DATE_FORMATTER = "yyyy-MM-dd";
	
	/*
	 * error constants
	 */
	String ERR_ENROLLMENT_VALID_INPUTS = "Please provide valid inputs. LastName/ssn/dateofbirth must not be empty";
	
	/**
	 * service constants
	 */
	String SERVICE_ID_USER = "T24ISUser";
	String OPERATION_GET_CUSTOMER_DETAILS = "getCustomer";
	String SERVICE_DBP_RB_LOCAL_SERVICE_DB = "dbpRbLocalServicesdb";
	String OPERATION_DBXDB_BACKENDIDENTIFIER_CREATE = "dbxdb_backendidentifier_create";
	
	//Properties
	String PROP_SECTION_ENROLLMENT = "Enrollment";
	String PROP_PRE_LOGIN_USERNAME = "PreLoginUserName";
	String PROP_PRE_LOGIN_USER_ID = "PreLoginUserId";
	String DEF_ERR_CODE = "5000";
}

