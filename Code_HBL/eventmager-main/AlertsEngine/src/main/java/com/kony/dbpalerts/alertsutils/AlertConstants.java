package com.kony.dbpalerts.alertsutils;

public class AlertConstants {

	private AlertConstants() {

	}

	public static final String AID_LOWER = "aid";
	public static final String APPID_LOWER = "appid";
	public static final String SUCCESS_STATUS = "SID_ACTIVE";
	public static final String SID_SUBSCRIBED = "SID_SUBSCRIBED";

	public static final String SID_DELIVERYFAILED = "SID_DELIVERYFAILED";
	public static final String SID_DELIVERY_NOTSUBMITTED = "SID_DELIVERY_NOTSUBMITTED";
	public static final String SID_DELIVERY_SUBMITTED = "SID_DELIVERY_SUBMITTED";

	public static final String DEFAULT_LANGUAGE = "en-US";
	public static final String USA_CODE = "United States";

	public static final String FILTER = "$filter";
	
	public static final String OPSTATUS = "opstatus";
	public static final String ALERTRECIPIENTTYPE = "alertrecipienttype";

	public enum ALERTCONDITIONS {
		GREATER_THAN, GREATER_EQUAL_TO, LESS_THAN, IN_BETWEEN, EQUALS_TO, LESS_EQUAL_TO, NOT_EQUAL_TO, CONTAINS;
		public String getName() {
			return this.name();
		}
	}

	public static final String SMS_LOWER = "sms";
	public static final String EMAIL_LOWER = "email";
	public static final String NOTIFICATION_LOWER = "notification";
	public static final String PUSH_LOWER = "push";

	public static final String MESSAGE_CONTENT = "messagecontent";

	public static final String EXTERNALMOBILE = "externalphone";
	public static final String EXTERNALEMAIL = "externalemail";
	public static final String BACKENDID = "BackendId";

	public static final String DBX_KMS_EMAIL = "DBX_KMS_EMAIL";
	public static final String DBX_KMS_SMS = "DBX_KMS_SMS";
	public static final String DBX_KMS_PUSH = "DBX_KMS_PUSH";
	public static final String DBX_KMS_APPKEY = "DBX_KMS_APPKEY";
	public static final String QUEUEMASTER_SHARED_SECRET = "QUEUEMASTER_SHARED_SECRET";

	// DB rows Constants
	public static final String ALERTCATEGORYID = "AlertCategoryId";
	public static final String ALERTTYPE_STATUS_ID = "alerttype_status_id";
	public static final String ALERTSUBTYPETYPE_STATUS_ID = "alertsubtypetype_status_id";
	public static final String ALERTCATEGORY_STATUS_ID = "alertcategory_status_id";
	public static final String ALERTCONDITIONID = "AlertConditionId";
	public static final String ATTRIBUTEID = "AttributeId";
	public static final String RECIPIENTTYPE = "recipienttype";
	public static final String ISGLOBAL = "IsGlobal";
	public static final String ACCOUNTLEVEL = "accountLevel";
	public static final String VALUE1 = "Value1";
	public static final String VALUE2 = "Value2";
	public static final String CHANNELID = "ChannelId";
	public static final String ACCOUNTLEVELSUBSCRIBED = "accountlevelsubscribed";
	public static final String ACCOUNTID = "AccountId";
	public static final String CUSTOMER_ID = "Customer_id";
	public static final String VAL = "Value";
	public static final String FIRSTNAME = "FirstName";
	public static final String CUSTOMERID = "CustomerId";
	public static final String MIDDLENAME = "MiddleName";
	public static final String LASTNAME = "LastName";
	public static final String CUSTOMERTYPE_ID = "CustomerType_id";
	public static final String COUNTRYCODE = "CountryCode";
	public static final String USERNAME = "UserName";
	public static final String APPID = "appId";
	public static final String APPID_1 = "Appid";
	public static final String APPID_NEW = "AppId";
	public static final String AID = "aid";
	public static final String OTHERDATA = "otherData";
	public static final String EVENTID = "eventId";
	public static final String ACCOUNT_ID = "Account_id";
	public static final String USER_ID = "User_id";
	public static final String CUSTOMERTYPEID = "customerTypeId";
	public static final String C_SUCCESS_STATUS = "C_SUCCESS_STATUS";
	public static final String LEGALENTITYID= "companyLegalUnit";

	public static final String SUBSCRIBED = "SID_SUBSCRIBED";
	// Channel specific
	public static final String CH_SMS = "CH_SMS";
	public static final String CH_PUSH_NOTIFICATION = "CH_PUSH_NOTIFICATION";
	public static final String CH_NOTIFICATION_CENTER = "CH_NOTIFICATION_CENTER";
	public static final String CH_EMAIL = "CH_EMAIL";
	public static final String ID = "id";

	public static final String MESSAGE = "message";

	public static final String TEXT = "Text";
	public static final String SUBJECT = "Subject";

	public static final String SUCCESS = "success";
	public static final String STRING = "String";
	public static final String ERRMSG = "errMsg";
	public static final String ERRCODE = "errcode";
	public static final String DBPERRMSG = "dbpErrMsg";
	public static final String DBPERRCODE = "dbpErrCode";
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	public static final String ID_QUERY = "SELECT LAST_INSERT_ID() as lastid;";

	public static final String ACCOUNTSUBSCRIPTIONSTATUS = "AccountSubscriptionStatus";

	public static final String NAME = "Name";

	public static final String SENDERNAME = "SenderName";
	public static final String SENDERMAIL = "SenderEmail";
	public static final String CHANNELID_UPPER = "ChannelID";

	public static final String STATUS_ID = "Status_id";

	public static final String ALERTSUBTYPEID = "AlertSubTypeId";
	public static final String LANGUAGECODE = "LanguageCode";

	public static final String ALERTSUBTYPEID_CAMEL = "alertSubTypeId";
	public static final String EVENTDATA = "eventData";
	public static final String SESSION = "session";
	public static final String REQINPUT = "requestInput";
	public static final String RESOUTPUT = "responseOutput";
	public static final String CUSTOMPARAMS = "customParams";
	public static final String ACCOUNTID_UPPER = "AccountID";
	public static final String EVENTTYPE = "eventType";
	public static final String EVENTSUBTYPE = "eventSubType";
	public static final String STATUS = "status";
	public static final String ACCOUNTNUMBER = "accountnumber";
	public static final String EVENT = "event";
	public static final String TOKEN = "token";
	public static final String EVENTS = "events";
	public static final String OTHER = "other";

	public static final String EMAIL = "email";

	public static final String NOTIFY_SUB = "notificationSubject";
	public static final String NOTIFY_TEXT = "notificationText";
	public static final String ISREAD = "isRead";
	public static final String USER = "user";
	public static final String CORECUSTOMERID = "corecustomerid";
	public static final String ZERO = "0";

	// KMS EMAIL Properties
	public static final String RECIPIENT = "recipient";
	public static final String TYPE = "type";
	public static final String TO = "TO";
	public static final String EMAILID = "emailId";
	public static final String RECIPIENTS = "recipients";
	public static final String SENDEREMAIL_LOWER = "senderEmail";
	public static final String NULL = "null";
	public static final String SENDERNAME_LOWER = "senderName";
	public static final String SUBJECT_LOWER = "subject";
	public static final String CONTENT = "content";
	public static final String PRIORITY = "priority";
	public static final String EMAILS = "emails";
	public static final String EMAILSERVICEREQUEST = "emailServiceRequest";
	public static final String EMPTYSTRING = "";

	public static final String PRIORITYSERVICE = "priorityService";
	public static final String DATA = "data";
	public static final String TITLE = "title";
	public static final String UFID = "ufid";
	public static final String SUBSCRIBER = "subscriber";
	public static final String PUSH = "PUSH";
	public static final String SUBSCRIBERS = "subscribers";
	public static final String PLATFORMSPECIFICPROPS = "platformSpecificProps";
	public static final String MESSAGES = "messages";
	public static final String MESSAGEREQUEST = "messageRequest";
	public static final String MIMETYPE = "mimeType";
	public static final String TEXT_PLAIN = "text/plain";

	public static final String MOBILE = "mobile";
	public static final String STARTTIMESTAMP = "startTimestamp";
	public static final String EXPIRYTIMESTAMP = "expiryTimestamp";
	public static final String SMSSERVICEREQUEST = "smsServiceRequest";

	public static final String CUSTOMERID_LOWER = "customerId";
	public static final String ACC_TYPE_ID = "accounttype_id";
	public static final String ISALERTACCOUNTLEVEL = "isAlertAccountIDLevel";

	public static final String TRANSACTIONS = "transactions";
	public static final String ACCOUNTTYPE = "AccountType";
	public static final String ASTERISK = "*";

	public static final int THREAD_SIZE = 5;
	public static final int DBTHREADSIZE = 1;

	public static final String ALERT_LOOP_SEPARATOR = ";";

	// SERVICE CONSTANTS
	public static final String INPUT_EVENTS = "events";
	public static final String INPUT_RECIPIENTINFO="recipientInfo";

	public static final String LOOP_SEPARATOR = "loop_separator";
	public static final String LOOP_COUNT = "loop_count";
	public static final String LOOP_DATASET = "LoopDataset";

	public static final String ALERTS_ORCH_SERVICE = "Alerts_Orch";
	public static final String ALERTS_ORCH_OPERATION = "serveAlert";

	public static final String ALERTS_RECIPIENT_ORCH_OPERATION = "getBulkAlertRecipients";
	public static final String ALERTS_RECIPIENT_JAVA_OPERATION = "getAlertRecipients";
	
	
	public static final String ALERTS_DEFAULT_LANGUAGE = "ALERTS_DEFAULT_LANGUAGE";

	public static final String ISPRIMARY = "isPrimary";
	public static final String ISALERTSREQUIRED = "isAlertsRequired";
	public static final String GETRECIPIENTSERVICE_INPUT_EMPTY = "Recipientservice is not called with proper input";
	public static final String SENDEMAILOPERATION = "sendEmail";
	public static final String KMSINVOKESERVICE = "notificationInvokeService";
	public static final String SENDSMSOPERATION = "sendSms";
	public static final String SENDPUSHNOTIFICATIONOPERATION = "sendPushNotification";
	public static final String NOTIFYQUERY = "notifyQuery";
	public static final String USERNOTIFYQUERY = "userNotifyquery";
	public static final String ALERTHISTORYQUERY = "alertHistoryQuery";
	public static final String SENDNOTIFICATIONOPERATION = "sendNotification";
	public static final String INPUTPARAMS = "inputparams";
	public static final String LOGPARAMS = "logparams";
	public static final String INCLUDEPREFERREDCONTACT = "includepreferredcontact";
	public static final String REFERENCENUMBER = "referenceId";
	public static final String EXTERNALSYSTEM = "externalSystem";
	public static final String PHONENUMBER = "phoneNumber";
	public static final String PHONECOUNTRYCODE = "phoneCountryCode";
	public static final String ALERTCATEGORYNAME = "alertCategoryName";
	public static final String ALERTGROUPNAME = "alertGroupName";
	public static final String ALERTNAME = "alertName";
	public static final String EXTERNALALERTSSERVICEID = "ExternalAlerts";
	public static final String EXTERNALALERTSOPERATIONID = "pushAlertsToExternal";

	public enum ALERTLEVEL {
		CATEGORY, GROUP, ALERT
	}
}
