package com.kony.audit.auditutils;

public class AuditDBConstants {
	private AuditDBConstants() {

	}

	public static final String GETCUSTOMERDATA = "{schema_name}_dbpevents_getCustomerData";
	public static final String GETCUSTIDFROMACCOUNT = "{schema_name}_dbpevents_getCustidFromAccount";
	public static final String GETCUSTIDFROMCORE = "{schema_name}_dbpevents_getCustIdFromCore";
	public static final String MONEYMOVEMENT_INSERT = "{schema_name}_moneymovementlog_create";
	public static final String AUDIT_INSERT = "{schema_name}_auditactivity_create";
	public static final String APPLICATION_GET = "{schema_name}_application_get";
	public static final String APPMAPPINGAID_GET = "{schema_name}_appmappingaid_get";
	public static final String TRANSACTION_TYPE_GET = "{schema_name}_transactiontype_get";

	
	public static final String EVENTDBDBSERVICE = "EventManagerDBService";
	
	
	public static final String AUDIT_EVENT_ORCH = "AuditService_Orch";
	public static final String AUDIT_EVENT_ORCH_OPERATION = "serveevent_orch";

}
