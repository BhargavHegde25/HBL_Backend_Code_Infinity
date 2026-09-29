package com.kony.dbp.batchprocessingalerts;

public class Constants {
	private Constants() {
		
	}
	
	
	public static final String CONDITIONCOLUMN = "Condition";
	public static final String VALUECOLUMN = "Value";
	public static final String COLUMNNAME = "ColumnName";
	public static final String ALERTTYPE = "AlertType";
	public static final String ALERTTYPES = "AlertTypes";
	public static final String EVENTSUBTYPE = "eventSubType";
	public static final String EVENTDATA = "eventData";
	public static final String EVENTTYPE = "eventType";
	public static final String CUSTOMPARAMS = "customParams";
	public static final String ADDITIONALPARAMS = "customParams";
	public static final String STATUS = "status";
	
	public static final String OTHERDATA = "otherData";

	public static final String REQUESTINPUT = "requestInput";

	public static final String SUBSCRIBERS = "subscribers";
	public static final String ALERTCONDITIONID = "alertConditionId";
	public static final String VALUE1 = "value1";
	public static final String VALUE2 = "value2";
	public static final String ACCOUNTID = "accountId";
	public static final String ATTRIBUTEID = "attributeId";
	public static final String SUBTYPES = "subTypes";
	public static final String CUSTOMERID = "customerId";
	public static final String APPID = "appId";
	public static final String RETAIL_BANKING = "RETAIL_BANKING";
	public static final String RETAIL_AND_BUSINESS_BANKING = "RETAIL_AND_BUSINESS_BANKING";
	public static final String BATCH_ALERT_APP_ID = "BATCH_ALERT_APP_ID";
	
	public static final String SUCCESS = "success";
	public static final String EVENTS = "events";
	public static final String TOKEN = "token";
	public static final String PRODUCER = "producer";
	public static final String ALERTTYPESKEY = "alertTypes";
	public static final String CORECUSTOMERIDS = "coreCustomerIds";
	public static final String ACCOUNTNUMBER = "accountnumber";
	
	// compare conditions
	public static final String EQUALSTO = "EQUALS_TO";
	public static final String GREATERTHAN = "GREATER_THAN";
	public static final String GREATEREQUALTO = "GREATER_EQUAL_TO";
	public static final String LESSTHAN = "LESS_THAN";
	public static final String LESSEQUALTO = "LESS_EQUAL_TO";
	public static final String NOTEQUALTO = "NOT_EQUAL_TO";
	public static final String INBETWEEN = "IN_BETWEEN";
	public static final String CONTAINS = "CONTAINS";
	

	
	// db columns 
	public static final String OBJECTTYPE = "objectType";
	public static final String ALERTTYPECOLUMN = "alertType";
	public static final String COLUMNNAMEDB = "columnName";
	public static final String CONDITIONCOL = "condition";
	public static final String VALUECOL = "value";
	public static final String LATESTTIMESTAMP = "latestSyncedTimestamp";
	public static final String CHECKTYPE = "dueDateChecktype";
	public static final String PARAMETERCOLUMNNAME = "dueDateParamName";
	
	public static final String BACKENDID = "BackendId";
	public static final String CUSTOMERIDCOL = "Customer_id";
	
	//service call
	public static final String BATCHPROCESSINGOBJECTS = "BatchProcessingObjects";
	public static final String BPOVERSION = "1.0";
	public static final String BPOOBJECTID = "AlertSubscribers";
	public static final String BPOOPERATIONID = "getAlertSubscribers";
	public static final String ALERTSUBTYPESPARAM = "AlertSubTypes";
	public static final String BPDBSERVICE = "EventManagerDBService";
	public static final String BATCHALERTDEFGET = "dbxdb_batchalertdefinition_get";
	public static final String BACKENDIDGET = "dbxdb_backendidentifier_get";
	public static final String CALLQUEUEMASTERSERVICE_ORCH = "BatchProcessing_Orch";
	public static final String QUEUEMASTEROPERATION = "callQueueMaster_orch";
	public static final Object LOOP_SEPARATOR_VAL = "$$$";
	public static final String PROCESSEVENTSSERVICE = "ProcessBatchEvents";
	public static final String PROCESSEVENTSSOPER = "processEvents";
	public static final String LOOPCOUNT = "loop_count";
	public static final String LOOP_SEPARATOR = "loop_separator";
	
	public static final String BATCH_PROCESSING_ENGINE_BATCHLIMIT = "BATCH_PROCESSING_ENGINE_BATCHLIMIT";
	
	public static final String QUEUEMASTER_SHARED_SECRET = "QUEUEMASTER_SHARED_SECRET";

	
	public static final String STRING = "String";
	public static final String DBPERRMSG = "dbpErrMsg";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String EVENT = "event";
	public static final String CORECUSTOMERS = "coreCustomers";
	public static final String CORECUSTOMERID = "coreCustomerId";
	public static final String ATTRIBUTES = "attributes";
	public static final String ALERTSUBTYPE = "AlertSubType";
	
	
}
