package com.dbp.reminderengine.utils;

public class Constants {

	private Constants() {

	}

	public static final String STARTTIME = "startTime";
	public static final String ENDTIME = "endTime";
	public static final String SCHEDULEDAY = "scheduleDay";
	public static final String SUCCESS = "success";
	public static final String TRUE = "true";
	public static final String STRING = "String";
	public static final String FALSE = "false";
	public static final String DBPERRMSG = "errmsg";
	public static final String ORCH_SERVICE = "ReminderEngine_Orch";
	public static final String ORCH_OPERATION = "processAlerts_Orch";
	public static final String ERROR = "error occurred or no records found";
	public static final String DATETIMEFORMATTER = "yyyy-MM-dd HH:mm:ss";
	public static final Object ENDTIMEVAL = "23:59:59";
	public static final Object STARTTIMEVAL = "00:00:00";
	public static final String LASTSYNCTIME = "lastExecTime";
	public static final String LASTSYNCTIMEUPDATE_OPERATION = "{schema_name}_alertfrequencyjobexectime_update";
	public static final String RECORDS = "records";
	public static final String TIMEFORMATTER = "HH:mm:ss";
	public static final String GETLASTSYNCTIME_OPERATION = "{schema_name}_alertfrequencyjobexectime_get";
	public static final String DATASETID = "alertfrequencyjobexectime";
	public static final String STARTTIMEPREV = "startTimePrev";
	public static final String STARTTIMECURR = "startTimeCurr";
	public static final String ENDTIMEPREV = "endTimePrev";
	public static final String ENDTIMECURR = "endTimeCurr";
	public static final String SCHEDULEDAYPREV = "scheduleDayPrev";
	public static final String SCHEDULEDAYCURR = "scheduleDayCurr";
	public static final String GETZONEOFFSET_OPER = "{schema_name}_application_get";
	public static final String REMINDERENGINEDBSERVICE = "EventManagerDBService";
	public static final String APPLICATIONDATASETID = "application";
	public static final String TIMEZONEOFFSET = "timeZoneOffset";
	public static final String SCHEDULEDATE = "scheduleDate";
	public static final String GETCUTOMERS_SP_OPERATION = "{schema_name}_getCustomersAlertFrequency_Sp";
	public static final String GETALLCUTOMERS_SP_OPERATION = "{schema_name}_getAllCustomersAlertFrequency_Sp";
	public static final String SCHEDULEDATECURR = "scheduleDateCurr";
	public static final String SCHEDULEDATEPREV = "scheduleDatePrev";
	public static final String ISLASTDATE = "isLastDate";
	public static final String ISPREVDATELASTDATE = "isPrevDateLastDate";
	public static final String REMINDERENGINEISORCHCALL = "REMINDER_ENGINE_IS_ORCH_CALL";
	public static final Object LOOPSEPARATORVAL = "$$$";
	public static final String CUSTOMERIDS = "customerIds";
	public static final String CUSTOMERIDCOL = "Customer_id";
	public static final String BACKENDID = "BackendId";
	public static final String GETBACKENDIDS_OP = "{schema_name}_getCoreIdForCustomerIds_Sp";
	public static final String CUSTOMERID = "customerId";
	public static final String REMINDERENGINESTUBSERVICE = "ReminderEngineStub";
	public static final String STUB_OPERATION = "reminderServiceStubData";
	public static final String APPID = "RETAIL_AND_BUSINESS_BANKING";
	public static final String BACKENDTYPE = "CORE";
	public static final String BACKENDTYPECOL = "backendType";
	
	public enum ALERTLEVEL {
		CATEGORY, GROUP, ALERT
	}
	
	public static final String CUSTOMERVIEWALERTCONFIGURATION_GET = "{schema_name}_customerviewalertconfiguration_get";

	public static final String  GETALLCUTOMERS_ALERTLEVEL="{schema_name}_getAllCustomersAlertFrequency_Sp_alertlevel";
	public static final String GETALLCUTOMERS_CATEGORYLEVEL="{schema_name}_getAllCustomersAlertFrequency_Sp_categorylevel";
	public static final String GETALLCUTOMERS_GROUPLEVEL ="{schema_name}_getAllCustomersAlertFrequency_Sp_grouplevel";
	
	
	public static final String  GETCUTOMERS_CATEGORYLEVEL ="{schema_name}_getCustomersAlertFrequency_Sp_categorylevel";
	public static final String GETCUTOMERS_GROUPLEVEL ="{schema_name}_getCustomersAlertFrequency_Sp_grouplevel";
	public static final String GETCUTOMERS_ALERTLEVEL="{schema_name}_getCustomersAlertFrequency_Sp_alertlevel";
}
