package com.kony.adminconsole.utilities;

public final class ACConstants {
	
		public static final String ERROR_WHILE_PROCESSING_THE_RESPONSE = "Error while processing the response";
		
		public static final String VALUE = "value";
		public static final String MESSAGE = "message";
		public static final String CODE = "code";
		public static final String CUSTOMERS_DATASET = "Customers";
		public static final String CUSTOMER_NUMBER = "CustomerNumber";
		public static final String CUSTOMER_ID = "CustomerId";
		public static final String ELIGABLE = "eligable";
		public static final String ERROR = "error";
		public static final String BASICHEADER = "Basic ";
		public static final String DCCUSTOMERRMSG = "dccustomerrmsg";
		public static final String DATACONTEXT_RETURNED_NON_200_CODE = "DataContext service failed with error code ";
		public static final String DATACONTEXT_SERVICE_NO_USERNAME_PASSWORDS_ERRMSG = "Runtime properties for DataContext username and passwords should be defined";;
		public static final String CAMPAIGN_DC_USERNAME = "CAMPAIGN_DC_DEFAULTUSERNAME";
		public static final String CAMPAIGN_DC_PASSWORD = "CAMPAIGN_DC_DEFAULTPASSWORD";
		public static final String DBP_ERROR_MESSAGE = "dbpErrMsg";
		public static final String DBP_ERROR_CODE = "dbpErrCode";
		public static final String HAS_PERMISSION = "hasPermission";
		public static final String HTTP_STATUS_CODE = "httpStatusCode";
		
		public static final String LOOP_SEPERATOR = "loop_separator";
		public static final String LOOP_COUNT = "loop_count";
		public static final String LOOP_DATASET = "LoopDataset";
		public static final String DC_SUFFIX_USERNAME="_USERNAME";
		public static final String DC_SUFFIX_PASSWORD="_PASSWORD";
		public static final String DC_PREFIX="CAMPAIGN_DC_";
		public static final String ERRCODE = "errcode";
		public static final String ERRMSG = "errmsg";
		
	//	public static final String GET_USERS_FOR_SEGMENT = "getUsersForSegment";
		public static final String GET_ALL_SEGEMENT_DATASET = "allProfiles";
		
		//getprofiles service constants
		public static final String PROFILE_ID = "profileId";
		public static final String PROFILE_NAME = "profileName";
		public static final String PROFILE_DESCRIPTION = "profileDescription";
		public static final String PROFILE_STATUS = "profileStatus";
		public static final String NUMBER_OF_USERS = "numberOfUsers";
		public static final String SOFT_DELETE_INDICATOR = "softDeleteIndicator";
		public static final String PROFILE_CREATION_DATE = "profileCreationDate";
		public static final String PROFILE_DEACTIVATED_DATE = "profileDeactivatedDate";
		public static final String PROFILE_CONDITIONS = "profileConditions";
		public static final String PROFILE_CONDITION_ID ="profileConditionId";
		public static final String CONDITION_EXPRESSION = "conditionExpression";
		public static final String DATA_CONTEXT_ID = "dataContextId";	
		public static final String DATA_CONTEXT_ENDPOINTS = "dataContextEndPoints";
		
		public static final String CAMPAIGN_MS_SERVICE_NAME = "CampaignManagementMS";
		public static final String GET_ALL_SEGMENTS = "getAllSegments";
		public static final String UPDATE_USERS_FOR_SEGMENT = "updateUsersForSegments";
		
		// Service invocation params
		public static final String LOOPING_OPERATION_NAME = "getActiveUsersForEachDC";
		public static final String LOOPING_SERVICE_NAME = "Campaign_Segment";

		//looping orchestration params
		public static final String LOOP_PARAM_FILTER = "filter";
		public static final String LOOP_PARAM_ENDPOINT_URL = "endpointUrl";
		public static final String LOOP_PARAM_SEGMENT_ID = "segmentId";
		public static final String LOOP_PARAM_DATACONTEXT_ID = "datacontextId";
		
		//updateUsers to segment ms params
		public static final String OPSTATUS = "opstatus";
		public static final String PROFILES = "profiles";
		public static final String SUCCESS = "Success";
		
		public static final String SEGMENT = "segment";
		public static final String PROFILE_CONDITIONSS = "profileConditions";
		public static final String DATA_CONTEXT_END_POINTS = "dataContextEndPoints";
		public static final String USERCOUNT = "usercount";
		
		public static final String ALERT_SUB_TYPE_ID = "alertSubTypeId";
		public static final String CHANNEL_ID = "channelId";
		public static final String CHANNEL_ID_CAPITAL = "ChannelID";
		public static final String CUSTOMER_TYPE_ID = "customerTypeId";
		public static final String APP_ID = "appId";
		public static final String ALERT_TYPE_ID = "alertTypeId";
		public static final String ALERTSUBTYPEAPP_TN = "alertsubtypeapp";
		public static final String ALERTSUBTYPEACCOUNTTYPE_TN = "alertsubtypeaccounttype";
		public static final String ALERTSUBTYPECUSTOMERTYPE_TN = "alertsubtypecustomertype";
		public static final String ALERTSUBTYPECHANNEL_TN = "alertsubtypechannel";
		public static final String ALERTSUBTYPETEXT_TN = "alertsubtypetext";
		public static final String CUSTOMERVIEWALERTCONFIGURATION_TN ="customerviewalertconfiguration";
		public static final String ALERTTYPEAPP_TN = "alerttypeapp";
		public static final String ALERTTYPEACCOUNTTYPE_TN = "alerttypeaccounttype";
		public static final String ALERTTYPECUSTOMERTYPE_TN = "alerttypecustomertype";
		public static final String ALERTTYPECHANNEL_TN = "alerttypechannel";
		public static final String ALERTTYPETEXT_TN = "alerttypetext";
		public static final String ALERTCATEGORYCHANNEL_TN = "alertcategorychannel";
		public static final String ALERT_CATEGORY_ID = "alertCategoryId";
		public static final String ID_LOWERCASE = "id";
		public static final String LANGUAGE_CODE = "languageCode";
		public static final String ACCOUNT_TYPE_ID = "accountTypeId";
		public static final String CUSTOMERALERTCHANNEL_TN = "customeralertchannel";
		public static final String CUSTOMERALERTFREQUENCY_TN = "customeralertfrequency";
		public static final String ALERT_FREQUENCY_ID = "alertFrequencyId";
		public static final String CHANL_SELECT_STR = CHANNEL_ID + "," + ALERT_TYPE_ID + "," + ALERT_SUB_TYPE_ID;
		public static final String FRQY_SELECT_STR = ALERT_FREQUENCY_ID + "," + ALERT_TYPE_ID + "," + ALERT_SUB_TYPE_ID;
		public static final String STAR_VALUE = "*";
		
		//Object Service names for logging
		//Service Definition
		public static final String CREATE_SERVICE_DEFINITION = "createServiceDefinition";
		public static final String DELETE_SERVICE_DEFINITION = "deleteServiceDefinition";
		public static final String UPDATE_DEFAULT_ROLE = "updateDefaultRole";
		public static final String EDIT_SERVICE_DEFINITION = "editServiceDefinition";
		public static final String MANAGE_SERVICE_DEFINITION = "manageServiceDefinitionStatus";
		//Contract
		public static final String CREATE_CONTRACT = "createContract";
		public static final String EDIT_CONTRACT = "editContract";
		//Customer roles
		public static final String CREATE_CUSTOMER_ROLE = "createGroup";
		public static final String EDIT_CUSTOMER_ROLE = "editGroup";
		public static final String CUSTOMER_ROLE_STATUS="manageStatus";
		public static final String CUSTOMER_ROLE_ASSIGN="CustomerAssignRole";
		public enum ALERTPREFERNCES{CATEGORY,GROUP,ALERT};
		//Customer profile
		public static final String EDIT_CUSTOMER_BASIC_INFO="EditCustomerBasicInfo";
		public static final String EDIT_CUSTOMER_CONTACT="EditCustomerContact";
		public static final String EDIT_INFINITY_USER="editInfinityUser";
		public static final String CREATE_INFINITY_USER="createInfinityUser";
		public static final String UPDATE_DBP_USER_STATUS="updateDBPUserStatus";

		public static final String SUCCESSFULLY="successfully";
		public static final String FAILED="failed";
		public static final String CREATED="created";
		public static final String UPDATED="updated";
		public static final String DELETED="deleted";
		
		public static final String ALERT_EXTL_REFERENCE_MAP = "alertExtlReferenceMap";
		public static final String EXTERNAL_ALERT_GROUP_MAP = "externalAlertGroupMap";
		public static final String ALERT_SUBSCRIPTION = "alertSubscription";
		public static final String EXTERNAL_SUBSCRIPTION_MAP = "externalSubscriptionMap";
		public static final String EXTERNAL_SUBSCRIPTION_INITIATOR = "externalSubscriptionInitiator";
		public static final String ALERT_MANAGEMENT = "AlertManagement";
		public static final String SUBSCRIPTION_TABLENAME = "dbxcustomeralertentitlement";
		
		//Deployment platforms
		public static final String AZURE = "azure";
		public static final String AWS = "aws";
		
		public static final String DATASET_RECORDS = "records";
		public static final String DATASET_RECORDS1 = "records1";
		public static final String ACTIONS = "actions";
		public static final String FEATURES = "features";
		public static final String COMMA_SEPERATOR = ",";
		
		//
		public static final String SPOTLIGHT_PROSPECT_GDPR = "spotlight-prospect-gdpr";
		public static final String FILTER = "$filter";
		// Query Param Constants
		public static final String DBP_KONY_AUTHORIZATION_TOKEN_QUERY_PARAM = "authToken";
		// Header Constants
		public static final String X_KONY_AUTHORIZATION_HEADER = "X-Kony-Authorization";
		//
		public static final String NON_VERIFIED_PROSPECT_PREFIX = "NNVF";
		public static final String NON_VERIFIED_EXISTING_CUSTOMER_PREFIX = "ENVF";
		public static final String NO_RECORD_FOR_APPLICATION = "No data found for application id - ";
		public static final String NO_RECORD_FOR_REQUEST = "No data found for request id - ";
		
		public static final String CUSTOMER = "customer";
		public static final String COMPANY_LEGAL_UNIT = "companyLegalUnit";
		public static final String TYPE_ID = "Type_Id";
		public static final String TYPE_ID_BUSINESS = "TYPE_ID_BUSINESS";
		public static final String TYPE_ID_WEALTH = "TYPE_ID_WEALTH";
		public static final String TYPE_ID_RETAIL = "TYPE_ID_RETAIL";
		public static final String DBXDB_BACKEND = "DBXDB";
		
		//Shared LegalEntityID
		public static final String SHARED_LEGAL_ENTITY_ID = "SHARED";
		
		public static final String INT = "int";
		public static final String STRING = "String";

		public static final String DBP_ERR_CODE = "dbpErrCode";
		public static final String DBP_ERR_MSG = "dbpErrMsg";
		
		private ACConstants() {}
}
