package com.infinity.dbx.temenos.user;

public interface UserConstants {

    String PROP_GENERAL = "General";
    String PROP_BANKNAME = "BankName";

    String DATASET_USER = "user";
    String DATASET_ADDRESS = "Addresses";
    String DATASET_CONTACTS = "ContactNumbers";
    String DATASET_EMAILS = "EmailIds";

    String ID = "id";
    String CUSTOMER_ID = "customerId";
    String CUSTOMER_NAME = "customerName";
    String USERNAME = "userName";
    String FIRSTNAME = "FirstName";
    String LASTNAME = "LastName";
    String CONTACT_DETAILS = "contactDetails";
    String SECONDARY_EMAIL = "secondaryemail";
    String SECONDARY_PHONE = "secondaryphone";
    String SECONDARY_EMAIL2 = "secondaryemail2";
    String SECONDARY_PHONE2 = "secondaryphone2";
    String PHONE_NUMBER = "phoneNumber";
    String OFFICE_PHN_NUMBER = "officePhoneNumber";
    String OFFICE_PHONE = "officePhone";
    String ADDRESSES = "addresses";
    String REGION_ID = "Region_id";
    String COUNTRY_ID = "Country_id";
    String NAME = "Name";
    String COUNTRY = "country";
    String CITY_ID = "City_id";
    String ZIPCODE = "zipCode";
    String DB_ZIPCODE = "ZipCode";
    String ADDRESS_TYPE = "Addr_type";
    String ADDRESS = "address";
    String EMAILIDS = "emailIds";
    String PHONE_NUMBERS = "phoneNumbers";
    String VALUE = "value";
    String ADDRLINE1 = "addrLine1";
    String ADDR_TYPE = "addressType";
    String TYPE_ID = "Type_id";
    String PHONE_CODE = "phoneCountryCode";
    String DELETE_COMM_ID = "deleteCommunicationID";
    String PHONE = "phone";
    String PHONE_EXT = "phoneExtension";
    String CITY = "city";
    String ADDRESS_LINE1 = "addressLine1";
    String BANK_NAME = "bankName";
    String CORE_CUSTOMER_ID = "coreCustomerID";
    String PARTY_ID = "partyID";
    String CORE_ID = "coreID";

    String SERVICE_ID_USER = "T24ISUser";
    String SERVICE_ID_ONBOARDING_USER = "T24ISOnboardingUser";
    String SERVICE_ID_ACCOUNT = "T24ISAccounts";

    String ADDRESS_TYPE_WORK = "ADR_TYPE_WORK";
    String ADDRESS_TYPE_HOME = "ADR_TYPE_HOME";
    String COMM_TYPE_PHONE = "COMM_TYPE_PHONE";
    String EXT_TYPE_MOBILE = "Mobile";
    String EXT_TYPE_WORK = "Work";

    String OP_GET_USER_DETAILS = "getUserDetails";
    String OP_UPDATE_FULL_USER_DETAILS = "updateFullUserDetails";
    String OP_GET_FULL_USER_DETAILS = "getFullUserDetails";
    String OP_GET_T24_ORIGINATION_DATA = "GetUserData";
    String OP_CREATE_T24 = "createT24Operation";
    String OP_UPDATE_T24 = "updateT24CustomerOperation";
    String OP_GET_USER_DETAILS_TO_ADMIN = "getUserDetailsToAdmin";

    String IDENTITY_MAPPING_JSON = "IdentityTypeMapping.json";
    String EMPLOYMENT_STATUS_MAPPING_JSON = "EmploymentStatusMapping.json";
    String USER_PROPERTIES = "user.properties";
    String OP_GET_USER_FOR_ADMIN = "getUserDetailsForAdmin";
    String DB_SERVICE = "dbpRbLocalServicesdb";
    String OP_GET_USER = "_customer_get";
    String OP_GET_BACKENDIDENTIFIER = "_backendidentifier_get";
    String OP_CREATE_BACKENDIDENTIFIER = "_backendidentifier_create";
    
    String T24 = "T24";
    String customerId = "customerId";
    
    //Industry types properties file
    String INDUSTRY_TYPES = "IndustryTypes.properties";
    
    //User types
	String RETAIL_USER = "Retail";
	String SME_USER = "SME";
	String COMPANY = "COMPANY";
	String RELATED_COMPANY= "RELATEDCOMPANY";
	String COMPANY_REG_NO = "COMPANY.REG.NO";
	
	String PARTIES = "parties";
	String DUE_DILIGENCE_MS_BASE_URL = "DUE_DILIGENCE_MS_BASE_URL";
	String SOURCE_OF_FUNDS_SERVICE_URL = "/api/v2.0.0/party/parties/{party_id}/sourceOfFunds" ;
	String ASSET_LIABILITIES_SERVICE_URL = "/api/v1.0.0/party/parties/{party_id}/assetLiabDetails";
	
}
