package com.kony.campaign.common;

public final class CampaignConstants {	
	// Server properties
	public static final String CAMPAIGN_FETCHTYPE = "CAMPAIGN_FETCHTYPE";
	public static final String REALTIME = "REALTIME";
	public static final String CACHE = "CACHE";
	public static final String CAMPAIGN_CORETYPE = "CAMPAIGN_CORETYPE";
	public static final String DC_PREFIX="CAMPAIGN_DC_";
	public static final String DC_SUFFIX_USERNAME="_USERNAME";
	public static final String DC_SUFFIX_PASSWORD="_PASSWORD";
	public static final String CAMPAIGN_DC_USERNAME = "CAMPAIGN_DC_DEFAULTUSERNAME";
	public static final String CAMPAIGN_DC_PASSWORD = "CAMPAIGN_DC_DEFAULTPASSWORD";
	public static final String CORE_TYPE_DEFAULT_VALUE = "";
	public static final String CAMPAIGNS_CAROUSEL_NUMBER = "CAMPAIGNS_CAROUSEL_NUMBER";
	public static final String CAMPAIGN_CACHE_EXPIRY = "CAMPAIGN_CACHE_EXPIRY";
	public static final String CAMPAIGN_THREAD_POOL_SIZE = "CAMPAIGN_THREAD_POOL_SIZE";
	public static final String DBX_SCHEMA_NAME = "DBX_SCHEMA_NAME";
	public static final String CRUDLAYER = "CRUDLayer";
	public static final String DBXDB_PLACEHOLDER_GET = "dbxdb_placeholder_get";
	public static final String CAMPAIGNLIST = "campaignList";
	public static final String PLACEHOLDERID = "placeholderId";
	public static final String PLACEHOLDER = "placeholder";
	public static final String ODATA_FILTER = "$filter";
	
	public static final String SCHEMA_NAME_PLACE_HOLDER = "{schema_name}";
	
	public static final String CAMPAIGN_MANAGEMENT_JAVA = "CampaignManagementJava";
	public static final String GET_CAMPAIGNS_OPERATION = "GetCampaignsOperation";
	public static final String GET_ALL_DEFAULT_CAMPAIGNS = "GetAllDefaultCampaigns";
	
	public static final String CAMPAIGN_PROCESSING_SERVICE = "CampaignProcessingService";
	public static final String GET_ALL_CAMPAIGNS = "GetAllCampaignsForAnEvent";
	
	public enum ExternalChannels {	
		SMS("SMS"),PUSH("PUSH NOTIFICATIONS"),EMAIL("EMAIL");
		
		private ExternalChannels(String name) {
			this.externalName = name;
		}

		private String externalName;

		public String getExternalName() {
			return externalName;
		}
	}

	public enum InternalChannels {		
		WEB,MOBILE		
	}

	public enum CampaignFilterTypes{
		FILTER_ON_PLACEHOLDER,NO_FILTER
	}
		
	public static final String SERVICE_GET_CAMPAIGNS_FOR_EVENT = "getCampaignsForEvent";
	public static final String CAMPAIGN_SERVICE = "CampaignService";
	public static final String SERVICE_GET_ALL_CAMPAIGNS = "getAllCampaigns";
	public static final String SERVICE_GET_DEFAULT_CAMPAIGNS = "getDefaultCampaigns";
	public static final String SERVICE_GETCUSTIGNORE_CAMPAIGNS = "getCustIgnoreCampaigns";
	public static final String SERVICE_ORCH_NAME = "Orch_Campaign";
	public static final String ORCH_OPERATION_ONLINE_PLACEHOLDER = "getEventAndDefaultIgnoreForOnlinePH";
	public static final String ORCH_OPERATION_PRELOGIN = "getEventAndDefaultForPreLogin";
	public static final String ORCH_OPERATION_OFFLINE = "getEventAndIgnoreForOffline";
	public static final String ORCH_OPERATION_JOB = "getAllCampaignsIgnoreForJob";
	public static final String CAMPAIGN_DB_SERVICE = "CampaignDBService";
	public static final String CUST_COMPLETED_CAMPAIGN_POST_OPERATION = SCHEMA_NAME_PLACE_HOLDER+"_custcompletedcampaign_create";
	
	public static final String PARAM_CAMPAIGN_ID = "campaign_id";
	public static final String PARAM_CUSTOMER_ID = "customer_id";
	
	
	public static final String PRELOGIN = "PRELOGIN";
	public static final String OPSTATUS = "opstatus";
	public static final String IDENITY_BACKENDIDENTIFIERS = "backendIdentifiers"; 
	public static final String IDENITY_BACKENDID ="BackendId";
	
	//"dbpErrCode"
	public static final String DBP_ERROR_MESSAGE = "dbpErrMsg";
	public static final String DBP_ERROR_CODE = "dbpErrCode";
	public static final String LOOP_SEPERATOR = "loop_seperator";
	public static final String LOOP_COUNT = "loop_count";
	public static final String LOOP_DATASET = "LoopDataset";
	
	public static final String RESPONSE_LOOP_VALUE = "value";
	
	public static final String DESKTOP = "desktop";
	public static final String MOBILE = "mobile";
	public static final String REPORTINGPARAMS_CHNL = "chnl";
	
	public static final String PARAM_EVENT_CODE = "eventCode";
	public static final String PARAM_PLACEHOLDER_CODE = "placeholderCode";
	public static final String PARAM_SCALE = "scale";
	public static final String PARAM_CORE_CUSTOMER_ID = "coreCustomerId";
	public static final String PARAM_CHANNEL_TYPE = "channelType";
	public static final String PARAM_CAMPAIGN_STATUS = "campaignStatus";
	
	// Service invocation params
	public static final String LOOPING_OPERATION_NAME = "determineCampaignEligibility";
	public static final String LOOPING_SERVICE_NAME = "Orch_Campaign";

	//looping orchestration params
	public static final String LOOP_PARAM_FILTER = "filter";
	public static final String LOOP_PARAM_ENDPOINT_URL = "endpointUrl";
	public static final String LOOP_PARAM_CAMPAIGN_ID = "campaignId";
	public static final String LOOP_PARAM_DATACONTEXT_ID = "datacontextId";
	
	//DataContext/Analytic Servive params
	public static final String VALUE = "value";
	public static final String MESSAGE = "message";
	public static final String CODE = "code";
	public static final String CUSTOMERS_DATASET = "Customers";
	public static final String CUSTOMER_NUMBER = "CustomerNumber";
	public static final String CUSTOMER_ID = "CustomerId";
	public static final String ELIGABLE = "eligable";
	public static final String ERROR = "error";
	
	public static final String CAMPAIN_REQ_FILTER_AND = " and ";
	public static final String CAMPAIN_REQ_CUSTOMER_FILTER = "CustomerNumber eq '";
	
	// Response processing params
	public static final String GETIGNORECAMPAIGNS_DATASET = "records";
	public static final String GETIGNORECAMPAIGNS_BACKENDID = "BackendId";
	public static final String GETIGNORECAMPAIGNS_CAMPAIGNID = "campaign_id";
	
	public static final String GETCAMPAIGNS_CAMPAIGNDATASET = "CampaignList";
	public static final String GETCAMPAIGNS_CHANNEL_SUB_TYPE = "channelSubType";
	public static final String GETCAMPAIGNS_CONDITION_EXPRESSION = "attributes";
	public static final String GETCAMPAIGNS_END_POINT_URL = "EndPointURL";
	public static final String GETCAMPAIGNS_DATA_CONTEXT_NAME = "name";
	public static final String GETCAMPAIGNS_DATACONTEXT = "DataContext";
	public static final String GETCAMPAIGNS_CAMPAIGN_PRIORITY = "campaignPriority";
	public static final String GETCAMPAIGNS_CHANNEL_DETAILS = "channelDetails";
	public static final String GETCAMPAIGNS_CAMPAIGN_ID = "campaignId";
	public static final String GETCAMPAIGNS_CONTENT = "content";
	public static final String GETCAMPAIGNS_SUBJECT = "subject";	
	public static final String GETCAMPAIGNS_OFFLINE_TEMPLATE = "offlineTemplate";	
	public static final String GETCAMPAIGNS_TARGET_URL = "targetURL";
	public static final String GETCAMPAIGNS_PLACEHOLDER_ID = "placeholderId";
	public static final String GETCAMPAIGNS_IMAGE_URL = "imageURL";
	public static final String GETCAMPAIGNS_IMAGE_SCALE = "imageScale";
	public static final String GETCAMPAIGNS_PLACEHOLDER_IDENTIFIER = "placeholderIdentifier";
	public static final String GETCAMPAIGNS_ONLINE_CONTENT = "onlineContent";
	public static final String GETCAMPAIGNS_IMAGE_INDEX = "imageIndex";
	public static final String ERRCODE = "errcode";
	public static final String ERRMSG = "errmsg";
	public static final String ONLINE_CONTENT_ID = "onlineContentId";
	public static final String ONLINE_CONTENT_STRING = "onlineContentString";	
	public static final String BANNER_DESCRIPTION = "bannerDescription";
	public static final String BANNER_TITLE = "bannerTitle";
	public static final String BUTTON_LABEL =  "callToActionButtonLabel";
	public static final String ACTION_TARGET_URL = "callToActionTargetURL";
	public static final String SHOW_CLOSEON = "showCloseIcon";
	public static final String SHOW_READ_LATER_BUTTON = "showReadLaterButton";
	
	public static final String DEFAULTCAMPAIGN_DATASET = "DefaultcampaignList";
	public static final String ERRCODE_FOR_DEFAULT = "errcodeForDefault";
	public static final String ERRMSG_FOR_DEFAULT = "errmsgForDefault";
	
	// InternalEvent Response params
	public static final String DEFAULT_CAMPAIGN = "DEFAULT_CAMPAIGN";
	public static final String RESPONSE_IMAGE_INDEX = "imageIndex";
	public static final String RESPONSE_DESTINATION_URL = "destinationURL";
	public static final String RESPONSE_IMAGE_URL = "imageURL";
	public static final String RESPONSE_CAMPAIGN_PLACEHOLDER_ID = "campaignPlaceholderId";
	public static final String RESPONSE_PLACEHOLDER_CODE = "placeholderCode";
	public static final String RESPONSE_SCALE_CODE = "scale";
	public static final String RESPONSE_CAMPAIGN_ID = "campaignId";
	public static final String RESPONSE_CAMPAIGN_SPECIFICATIONS = "CampaignSpecifications";	
	
	// EVENTS
	public static final String PUSH_EVENT_SERVICENAME = "QueueMaster";
	public static final String PUSH_EVENT_OPERATION_NAME = "PushEventQueue";
	
	public static final String PARAM_PRODUCER = "producer";
	public static final String PARAM_EVENTS = "events";
	public static final String PARAM_TOKEN = "token";
	
	public static final String REQ_CUSTOMER_ID = "customerId";
	public static final String REQ_CUSTOMER_DATA = "customerData";
	public static final String PARAM_EVENT_DATA = "eventData";	
	public static final String EXTERNAL_EVENT_CODE = "eventCode";	
	
	public static final String EVENT_OTHER_DATA = "otherData";
	public static final String EVENT_DATA = "eventData";
	public static final String EVENT_SUBTYPE = "eventSubType";
	public static final String EVENT_STATUS = "status";
	public static final String EVENT_STATUS_VALUE = "SID_EVENT_SUCCESS";
	public static final String EVENT_CAMPAIGN_TYPE = "CAMPAIGN";
	public static final String EVENT_CAMPAIGN_SUB_TYPE = "CAMPAIGN";
	public static final String EVENT_TYPE = "eventType";
	public static final String EVENT_CUSTOMER_ID = "customerId";
	public static final String EVENT_CUSTOM_PARAMS = "customParams";
	public static final String EVENT_MESSAGE_CONTENT = "messageContent";
	public static final String EVENT_TEXT = "text";
	public static final String EVENT_SUBJECT = "subject";	

	public static final String BASICHEADER = "Basic ";
	public static final Object PARAM_CUSTOM_PARAMS = "customParams";
	
	public static final String DATACONTEXT_SERVICE_NO_USERNAME_PASSWORDS_ERRMSG = "Runtime properties for DataContext username and passwords should be defined";
	public static final String DATACONTEXT_RETURNED_NON_200_CODE = "DataContext service failed with error code";
	public static final String CUSTOM_PARAMS_SHOULD_BE_A_PROPER_JSON_OBJECT = "custom params should be a proper JsonObject";
	public static final String ERROR_WHILE_EXTRACTING_CORE_USER_ID = "Error while extracting coreUserID";
	public static final String CAMPAIGN_CORETYPE_HAS_TO_BE_SET_PROPERLY = "CAMPAIGN_CORETYPE has to be set properly";
	public static final String CHANNEL_IS_NOT_VALID = "Channel is not valid";	
	public static final String EVENT_CODE_IS_MANDATORY = "eventCode is mandatory";
	public static final String CORE_CUSTOMER_ID_IS_MANDATORY_ERRMSG = "coreCustomerId is mandatory";
	public static final String CAMPAIGNID_CANNOT_BE_EMPTY = "campaignId cannot be empty";
	public static final String ERROR_WHILE_FINDING_USERID = "Not able to map to exact UserId, error while extracting userId";
	public static final String CAMPAIGN_STATUS = "campaignStatus";

	
	public static final String AUTHORIZATION = "Authorization";

	private CampaignConstants() {		 
	}

}
