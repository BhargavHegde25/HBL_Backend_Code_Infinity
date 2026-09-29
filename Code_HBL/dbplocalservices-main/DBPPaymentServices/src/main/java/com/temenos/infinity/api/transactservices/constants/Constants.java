package com.temenos.infinity.api.transactservices.constants;

import java.util.HashMap;

public class Constants {
	
    public static final String COMPANY_ID = "company_id";
    public static final String CREATEDBY = "createdBy";
    public static final String CREATEDON = "createdOn";
    public static final String SOFTDELETEFLAG = "softdeleteflag";
    public static final String BULKWIREFILES_TABLE = "bulkwirefiles";
    public static final String BULKWIREFILE_TABLE = "BulkWireFiles";
	 public static final String WIREFILE_ID = "bulkWireFileID";
	 public static final String DOMESTIC_RECORDS = "Domestic";
    public static final String INTERNATIONAL_RECORDS = "International";
    public static final String FILEFORMAT_TYPES = "fileFormatTypes";
    public static final String BULKWIRETRANSFERTYPE = "bulkWireTransferType";
	 public static final String RECIPIENTNAME = "recipientName";
    public static final String CREATEDTS = "createdts";
    public static final String ASCENDING  = "asc";
    public static final String SORTBYPARAM  = "sortByParam";
    public static final String SEARCHSTRING  = "searchString";
    public static final String SORTORDER  = "sortOrder";
    public static final String PAGEOFFSET  = "pageOffset";
    public static final String PAGESIZE  = "pageSize";
    public static final String AUTO_DENIED_WEEKLY_LIMIT = "AUTO_DENIED_WEEKLY_LIMIT";
    public static final String PRE_APPROVED_WEEKLY_LIMIT = "PRE_APPROVED_WEEKLY_LIMIT";
    public static final String AUTO_DENIED_DAILY_LIMIT = "AUTO_DENIED_DAILY_LIMIT";
    public static final String PRE_APPROVED_DAILY_LIMIT = "PRE_APPROVED_DAILY_LIMIT";
    public static final String AUTO_DENIED_TRANSACTION_LIMIT = "AUTO_DENIED_TRANSACTION_LIMIT";
    public static final String PRE_APPROVED_TRANSACTION_LIMIT = "PRE_APPROVED_TRANSACTION_LIMIT";
	 public static final Object MONETARY_ACTIONTYPE = "MONETARY";
	 public static final Object NON_MONETARY = "NON_MONETARY";
	 public static final String MAX_TRANSACTION_LIMIT = "MAX_TRANSACTION_LIMIT";
	 public static final String DAILY_LIMIT = "DAILY_LIMIT";  
	 public static final String WEEKLY_LIMIT = "WEEKLY_LIMIT";  
	 public static final String NON_MONETARY_LIMIT = "NON_MONETARY_LIMIT";
	 public static final String RECORDS = "records";
    public static final String FI_FEATURESTATUS = "fiFeatureStatus";
    public static final String ORG_FEATURESTATUS = "orgFeatureStatus";
    public static final String SID_FEATURE_ACTIVE = "SID_FEATURE_ACTIVE";
    public static final String ACTIONID = "actionId";
    public static final String FEATURE_STATUS_ID = "Feature_Status_id";
    public static final String ACH_FILE = "achfile";
    public static final String ACH_FILE_FORMAT_TYPE = "achfileformattype";
    public static final String BULKWIRESAMPLEFILE_TABLE = "bulkwiresamplefile";	 
    public static final String APPROVAL_MATRIX_VIEW = "APPROVAL_MATRIX_VIEW"; 
    public static final String LIMITS = "limits";
    public static final String ACCOUNTID = "accountId";
    public static final String CIF = "cif";
    public static final String CONTRACTID = "contractId";
    public static final String LIMITTYPEID = "limitTypeId";
    public static final String LOWERLIMIT = "lowerlimit";
    public static final String UPPERLIMIT = "upperlimit";
    public static final String APPROVALRULEID = "approvalruleId";
    public static final String APPROVERS = "approvers";
    public static final String APPROVERID = "approverId";
    public static final String ACTIONLIST = "actionList";
    public static final String ACCOUNTLIST = "accountList";
    public static final String OPSTATUS = "opstatus";
    public static final String UPDATEDRECORDS = "updatedRecords";
	public static final String TRANSACTION = "transaction";
    public static final String BB_TEMPLATE = "bbtemplate";
    public static final String BB_TEMPLATE_RECORD = "bbtemplaterecord";
    public static final String BB_TEMPLATE_SUBRECORD = "bbtemplatesubrecord";
    public static final String BB_TEMPLATE_TYPE = "bbtemplatetype";
    public static final String SEARCH_STRING = "searchString";
    public static final String MY_REQUESTS = "myRequests";
    public static final String PENDING_FOR_MY_APPROVAL = "pendingForMyApprovals";
    public static final String _QUERY_TYPE = "_queryType";
    public static final String _FEATURE_ACTION_LIST = "_featureactionlist";
    public static final String _CUSTOMER_ID = "_customerId";
    public static final String _TRANSACTION_ID = "_transactionId";
    public static final String _FEATURE_ACTION_ID = "_featureActionId";
    public static final String REJECTED = "rejected";
    public static final String REQUESTID = "requestId";
    public static final String _REQUESTID = "_"+REQUESTID;
    public static final String TYPEIDSMALLBUSINESS = "TYPE_ID_SMALL_BUSINESS";
    public static final String TYPEIDRETAIL = "TYPE_ID_RETAIL";
    public static final String CUSTOMERID = "customerId";
    public static final String BULKWIREFILEEXECUTION_ID = "BulkWireFileExecution_id";
    public static final String BULKWIREFILEEXECUTIONDETAILS = "BulkWireFileExecutionDetails";
    public static final String WIREACCOUNTTYPE = "wireAccountType";
	 public static final String BULKWIREFILELINEITEMS_TABLE = "BulkWireFileLineItems";
	 public static final String BULKWIREFILELINEITEMS = "bulkwirefilelineitems";
    public static final String BULKWIREFILEFORMATTYPE_TABLE = "BulkWireFileFormatTypes";
    public static final String DB_BULKWIREFILEFORMATTYPE = "bulkwirefileformattype";
    public static final String BULKWIREFILE_ID = "bulkWireFileID";
    public static final String BULKWIREFILETRANSACTDETAILS_TABLE = "BulkWireFileTransactDetails";
    public static final String COUNTRY = "country";
    public static final String CURRENCY = "currency";
    public static final String ACCOUNTS = "accounts";
    public static final String ACCOUNT_TYPE = "accounttype";
    public static final String LOOP_DATA_SET = "LoopDataset";
    public static final String BBACTEDREQUEST = "bbactedrequest";
    public static final String BBREQUEST = "bbrequest";
    public static final String APPLICATION = "application";
    public static final String STATUS = "status";
    public static final String CONFIRMATION_NUMBER = "confirmationNumber";
    public static final String STATUSFILTER = "statusFilter";
    public static final String SUCCESS = "Executed";
    public static final String FAILED = "Failed";
    public static final String PENDING = "Pending";
    public static final String DENIED = "Denied";
    public static final String TOTALCOUNT = "totalCount";
    public static final String SUCCESSCOUNT = "successCount";
    public static final String FAILEDCOUNT = "failedCount";
	 public static final String PENDINGCOUNT = "pendingCount";
	 public static final String ORGANIZATION_ID = "Organization_Id";
	 public static final String CUSTOMERTYPE_ID = "CustomerType_id";
	 public static final String CUSTOMER_ID = "customer_id";
	 public static final String CUSTOMER = "customer";
	 public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	 public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	 public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
	 public static final String BULKWIRECATEGORYFILTER = "bulkWireCategoryFilter";
	 public static final String BULKWIRES = "BulkWires";
	 public static final String FILECATEGORY = "fileCategory";
	 public static final String BULKWIRE_FILE = "BULKWIRE_FILE";
	 public static final String TYPE_ID_BUSINESS = "TYPE_ID_BUSINESS";	 
	 public static final String TYPE_ID_RETAIL = "TYPE_ID_RETAIL";
	 public static final String EXISTING_RECIPIENT = "EXISTINGRECIPIENT";
	 public static final String MANUALLY_ADDED = "MANUALLYADDED";
	 public static final String EXTRACTED_FROM_FILE = "EXTRACTEDFROMFILE";
	 public static final String BULKWIRETEMPLATEID = "bulkWireTemplateID";
	 public static final String GROUPBY = "groupBy";
	 public static final String BULKWIRETEMPLATE_TABLE = "bulkwiretemplate";
	 public static final String TEMPLATERECIPIENTCATEGORY = "templateRecipientCategory";
	 public static final String BULKWIRETEMPLATELINEITEMS = "BulkWireTemplateLineItems";
	 public static final String EXISTINGRECIPIENT_RECORD = "ExistingRecipient";
	 public static final String MANUALLYADDED_RECORD = "ManuallyAdded";
	 public static final String EXTRACTEDFROMFILE_RECORD = "ExtractedFromFile";
	 public static final String VIEWREQUESTAPPROVERS = "view_requestapprovers";
	 public static final String CREATED_BY = "createdby";
	 public static final String ERROR_MESSAGE = "ErrorMessage";
	 public static final String TEMPLATE_USER_CONSTRAINT = "TemplateName_ForUser";
	 public static final String TEMPLATE_COMPANY_CONSTRAINT = "TemplateName_ForCompany";
	 public static final String FILE = "File";
	 public static final String TEMPLATE = "Template";
	 public static final String BULKWIRETEMPLATEEXECUTION_ID = "BulkWireTemplateExecution_id";
	 public static final String BULKWIREEXECUTIONDETAILS = "BulkWireExecutionDetails";
	 public static final String BULKWIRETEMPLATETRANSACTDETAILS_TABLE = "BulkWireTemplateTransactDetails";
	 public static final String BULKWIRETEMPLATEEXECUTIONDETAILS = "BulkWireTemplateExecutionDetails";
	 public static final String MAKE_TRANSFER = "MAKE_TRANSFER";
	 public static final String ACH_FILE_INITIATE = "ACH_FILE_INITIATE";
	 public static final String SID_EVENT_FAILURE = "SID_EVENT_FAILURE";
	 public static final String SID_EVENT_SUCCESS = "SID_EVENT_SUCCESS";
	 public static final String REFERENCEID = "referenceId";
	 public static final String AMOUNT = "amount";
	 public static final String FirstName = "FirstName";
	 public static final String LastName = "LastName";
	 public static final String UnSelectedPayees = "UnSelectedPayees";
	 public static final String CARDNUMBER = "_cardNumber";
	 public static final String OFFSET = "_pageOffset";
	 public static final String SIZE = "_pageSize";
	 public static final String ORDER = "_sortOrder";
	 public static final String SORTBY = "_sortByParam";
	 public static final String EXTERNALACCOUNT = "externalaccount";
	 public static final String PAYPERSON_RECORDS = "payperson";
	 public static final String BILLPAYPAYEE = "billpaypayee";
	 public static final String WIRETRANSFERSPAYEE = "wiretransferspayee";
	 public static final String WIRETRANSFERS = "wiretransfers";
	 public static final String PAYEE = "payee";
	 public static final String LOOP_COUNT = "loop_count";
	 public static final String ID = "id";
    public static final String INTERBANKPAYEE = "interbankpayee";
	 public static final String FILES = "Files";
	 public static final String TEMPLATES = "Templates";
	 public static final String FILEDOMESTICVIEW = "fileDomesticView";
	 public static final String FILEINTERNATIONALVIEW = "fileInternationalView";
	 public static final String TEMPLATEDOMESTICVIEW = "templateDomesticView";
	 public static final String TEMPLATEINTERNATIONALVIEW = "templateInternationalView";
	 public static final String USER_ID = "User_Id";
	 public static final String INTRABANKPAYEE = "intrabankpayee";
	 public static final String DELETEDRECORDS = "deletedRecords";
	 public static final String ISSCHEDULED = "isScheduled";
	 public static final String FREQUENCYTYPE = "frequencyType";
	 public static final String ONCE = "once";
	 public static final String SCHEDULED_OWN_ACCOUNT_TRANSFER = "SCHEDULED_OWN_ACCOUNT_TRANSFER";
	 public static final String RECURRING_OWN_ACCOUNT_TRANSFER = "RECURRING_OWN_ACCOUNT_TRANSFER";
	 public static final String ONETIME_OWN_ACCOUNT_TRANSFER = "ONETIME_OWN_ACCOUNT_TRANSFER";

	 public static final String REGISTERED_BILL_PAYMENT = "REGISTERED_BILL_PAYMENT";
	 public static final String NON_REGISTERED_BILL_PAYMENT = "NON_REGISTERED_BILL_PAYMENT";
	 
	 public static final String REG_DOM_WIRE_TRANSFER = "REG_DOM_WIRE_TRANSFER";
	 public static final String REG_INTERNATIONAL_WIRE_TRANSFER = "REG_INTERNATIONAL_WIRE_TRANSFER";
	 public static final String NON_REG_DOM_WIRE_TRANSFER = "NON_REG_DOM_WIRE_TRANSFER";
	 public static final String NON_REG_INTERNATIONAL_WIRE_TRANSFER = "NON_REG_INTERNATIONAL_WIRE_TRANSFER";
	 
	 public static final String SCHEDULED_ = "SCHEDULED_";	 
	 public static final String RECURRING_ = "RECURRING_";
	 public static final String ONETIME_ = "ONETIME_";
	 public static final String REGISTERED_P2P_TRANSFER = "REGISTERED_P2P_TRANSFER";
	 public static final String NON_REGISTERED_P2P_TRANSFER = "NON_REGISTERED_P2P_TRANSFER";
	 
	 public static final String SCHEDULED_OTHER_BANK_TRANSFER = "SCHEDULED_OTHER_BANK_TRANSFER";	 
	 public static final String RECURRING_OTHER_BANK_TRANSFER = "RECURRING_OTHER_BANK_TRANSFER";
	 public static final String ONETIME_OTHER_BANK_TRANSFER = "ONETIME_OTHER_BANK_TRANSFER";
	 public static final String SCHEDULED_SAMEBANK_OTHER_ACCOUNT_TRANSFER = "SCHEDULED_SAMEBANK_OTHER_ACCOUNT_TRANSFER";
	 public static final String RECURRING_SAMEBANK_OTHER_ACCOUNT_TRANSFER = "RECURRING_SAMEBANK_OTHER_ACCOUNT_TRANSFER";
	 public static final String ONETIME_SAMEBANK_OTHER_ACCOUNT_TRANSFER = "ONETIME_SAMEBANK_OTHER_ACCOUNT_TRANSFER";	 
	 public static final String SCHEDULED_INTERNATIONAL_BANK_TRANSFER = "SCHEDULED_INTERNATIONAL_BANK_TRANSFER";
	 public static final String RECURRING_INTERNATIONAL_BANK_TRANSFER = "RECURRING_INTERNATIONAL_BANK_TRANSFER";
	 public static final String ONETIME_INTERNATIONAL_BANK_TRANSFER = "ONETIME_INTERNATIONAL_BANK_TRANSFER";
	 public static final String INTRA_BANK_FUND_TRANSFER = "INTRA_BANK_FUND_TRANSFER";
	 
	 public static final String NUMDOMESTICTRANSACTIONS = "noOfDomesticTransactions";
	 public static final String NUMINTERNATIONALTRANSACTIONS = "noOfInternationalTransactions";
	 
	 public static final String FALSE = "false";
	 public static final String TRUE = "true";
	 public static final String ENABLE_EVENTS = "ENABLE_EVENTS";
	 public static final String INTERNATIONALPAYEE = "internationalpayee";
	 public static final String REFERENCE_KEY = "REF-";
	 
	 public static final String PENDING_APPROVAL_ = "PENDING_APPROVAL_";
	 public static final String REJECTED_ = "REJECTED_";
	 
	 public static final String RETAIL_PERMISSIONS = "retailPermissions";
	 public static final String BUSINESS_PERMISSIONS = "businessPermissions";
	 public static final String TYPE_ID_DOMESTIC = "TYPE_ID_DOMESTIC";
	 public static final String TYPE_ID_INTERNATIONAL = "TYPE_ID_INTERNATIONAL";
	 public static final String DOMESTIC = "Domestic";
	 public static final String INTERNATIONAL = "International";
	 
	 public static final String CUSTOMVIEW = "customview";
	 
	 public static final String ONGOINGPAYMENTS = "onGoingPayments";
	 public static final String UPLOADEDFILES = "uploadedFiles";
	 
	 public static final String SAMPLEFILES = "sampleFiles";
	 public static final String BULKPAYMENTRECORD = "bulkPaymentRecord";

	 public static final String PAYMENTORDERS = "paymentOrders";
	 public static final String HISTORY = "history";
	 public static final String SUCCESSFUL = "Successful";
	 public static final String CURRENCIES = "Currencies";
	 public static final String HTTP_STATUS = "httpStatusCode";
	 public static final String SINGLE = "SINGLE";
	 public static final String MULTI = "MULTI";
	 public static final String FILECONENT = "fileContent";
	 public static final String BULKPAYMENTTEMPLATES = "bulkPaymentTemplates";
	 public static final String TEMPLATE_ID = "templateId";
	 public static final String TEMPLATE_NAME = "templateName";
	 public static final String BANK_DETAILS = "bankDetails";
	 public static final String CANCELLATIONREASONS = "cancellationreasons";
	 public static final String BULKPAYMENTFILE_DATE_FORMAT = "yyyyMMdd";
	 public static final String $FILTER="$filter";
	 public static final String STATUS_INPROGRESS="InProgress";
	 public static final String STATUS_SUCCESS="Success";
	 public static final String STATUS_FAIL="Failed";
	 public static final String STATUS_ERROR="Error";
    public static final String STATUS_INERROR="In Error";
	 public static final String COMBINED_STATTEMENTS_DATE_FORMAT= "ddMMyyyy";
	 
	 public static final String OTHER = "OTHER";
	 public static final String OTHER_NAME = "Other";
	 public static final String FEATUREACTIONID = "featureActionId";
	 public static final String FEATUREACTIONNAME = "featureActionName";
	 public static final String FEATURENAME = "featureName";
	 public static final String MYAPPROVALSPENDING = "myApprovalsPending";
	 public static final String MYAPPROVALSHISTORY = "myApprovalsHistory";
	 public static final String MYREQUESTSPENDING = "myRequestsPending";
	 public static final String MYREQUESTHISTORY = "myRequestHistory";
	 public static final String OTHER_PAYMENTS = "Other Payments";
	 public static final String LIMITGROUPID = "limitgroupId";
	 public static final String LIMITGROUPNAME = "limitgroupName";
	 public static final String FEATUREACTIONS = "featureActions";
	 public static final String COUNTS = "Counts";
	 public static final String NO_APPROVAL = "NO_APPROVAL";
	 public static final String BULK_DATEFORMAT = "MM/dd/yyyy";
	 
	 public static final String DAY = "DAY";
	 public static final String WEEK = "WEEK";
	 public static final String MONTH = "MONTH";
	 public static final String YEAR = "YEAR";
	 
	 public static final String LANGUAGECODE = "languageCode";
	 public static final String CURRENT_WORKING_DATE = "currentWorkingDate";
	 public static final String APPROVAL_PENDING = "Approval Pending";
	 public static final String ACTION_TYPE = "actionType";
	 
	 public static final String PARAM_EVENT_APPROVAL_ACH_FILE = "APPROVAL_ACH_FILE_REQUEST";
	 public static final String PARAM_EVENT_APPROVAL_ACH_TRANSACTION = "APPROVAL_ACH_TRANSACTION_REQUEST";
	 public static final String PARAM_EVENT_APPROVAL_BILLPAY = "APPROVAL_BILLPAY_REQUEST";
	 public static final String PARAM_EVENT_APPROVAL_WIRETRANSFER = "APPROVAL_WIRE_TRANSFER_REQUEST";
	 public static final String PARAM_EVENT_APPROVAL_GENERALTRANSFER = "APPROVAL_GENERAL_TRANSACTION_REQUEST";
	 public static final String APPROVAL_GENERAL_TRANSACTION_CANCELLATION_REQUEST = "APPROVAL_GENERAL_TRANSACTION_CANCELLATION_REQUEST";
	 public static final String PARAM_EVENT_APPROVAL_CHEQUE_BOOK = "APPROVAL_CHEQUE_BOOK_REQUEST";
	 public static final String PARAM_EVENT_NOAPPROVAL_CHEQUEBOOK = "CHECKBOOK_REQUEST";
	 public static final String RENOTIFY_PENDING_APPROVAL_REQUEST = "RENOTIFY_PENDING_APPROVAL_REQUEST";
	 public static final String REQUEST_AND_APPROVAL_ALERTS = "REQUEST_AND_APPROVAL_ALERTS";
	 
	 public static final HashMap<String, String[]> approvalAlertSubEvents = new HashMap<String, String[]>(){
			private static final long serialVersionUID = -940508769431633185L;
			{
				 put("ACH_FILE_APPROVE_0", new String[] {"APPROVE_REQUEST_ACH_FILE"});
				 put("ACH_FILE_APPROVE_1", new String[] {"APPROVE_REQUEST_ACH_FILE_EXECUTION_INITIATOR","APPROVE_REQUEST_ACH_FILE_EXECUTION_ALL_APPROVERS"});
				 put("ACH_FILE_REJECT_0", new String[] {"REJECT_REQUEST_ACH_FILE_INITIATOR","REJECT_REQUEST_ACH_FILE_ALL_APPROVERS"});
				 put("ACH_FILE_WITHDRAW_0", new String[] {"WITHDRAW_REQUEST_ACH_FILE_INITIATOR","WITHDRAW_REQUEST_ACH_FILE_ALL_APPROVERS"});
				 
				 put("ACH_TRANSACTION_APPROVE_0", new String[] {"APPROVE_ACH_TRANSACTION"});
				 put("ACH_TRANSACTION_APPROVE_1", new String[] {"APPROVE_ACH_TRANSACTION_EXECUTION_INITIATOR","APPROVE_ACH_TRANSACTION_EXECUTION_ALL_APPROVERS"});
				 put("ACH_TRANSACTION_REJECT_0", new String[] {"REJECT_ACH_TRANSACTION_INITIATOR","REJECT_ACH_TRANSACTION_ALL_APPROVERS"});
				 put("ACH_TRANSACTION_WITHDRAW_0", new String[] {"WITHDRAW_ACH_TRANSACTION_INITIATOR","WITHDRAW_ACH_TRANSACTION_ALL_APPROVERS"});
				 
				 put("BILLPAY_APPROVE_0", new String[] {"APPROVE_REQUEST_BILLPAY"});
				 put("BILLPAY_APPROVE_1", new String[] {"APPROVE_REQUEST_BILLPAY_EXECUTION_INITIATOR","APPROVE_REQUEST_BILLPAY_EXECUTION_ALL_APPROVERS"});
				 put("BILLPAY_REJECT_0", new String[] {"REJECT_REQUEST_BILLPAY_INITIATOR","REJECT_REQUEST_BILLPAY_ALL_APPROVERS"});
				 put("BILLPAY_WITHDRAW_0", new String[] {"WITHDRAW_REQUEST_BILLPAY_INITIATOR","WITHDRAW_REQUEST_BILLPAY_ALL_APPROVERS"});
				 
				 put("DOMESTIC_WIRETRANSFER_APPROVE_0", new String[] {"APPROVE_DOMESTIC_WIRE_TRANSFER"});
				 put("DOMESTIC_WIRETRANSFER_APPROVE_1", new String[] {"APPROVE_DOMESTIC_WIRE_TRANSFER_INITIATOR","APPROVE_DOMESTIC_WIRE_TRANSFER_APPROVERS"});
				 put("DOMESTIC_WIRETRANSFER_REJECT_0", new String[] {"REJECT_DOMESTIC_WIRE_TRANSFER_INITIATOR","REJECT_DOMESTIC_WIRE_TRANSFER_APPROVERS"});
				 put("DOMESTIC_WIRETRANSFER_WITHDRAW_0", new String[] {"WITHDRAW_DOMESTIC_WIRE_TRANSFER_INITIATOR","WITHDRAW_DOMESTIC_WIRE_TRANSFER_APPROVERS"});
				 
				 put("INTERNATIONAL_WIRETRANSFER_APPROVE_0", new String[] {"APPROVE_INTERNATIONAL_WIRE_TRANSFER"});
				 put("INTERNATIONAL_WIRETRANSFER_APPROVE_1", new String[] {"APPROVE_INTERNATIONAL_WIRE_TRANSFER_INITIATOR","APPROVE_INTERNATIONAL_WIRE_TRANSFER_APPROVERS"});
				 put("INTERNATIONAL_WIRETRANSFER_REJECT_0", new String[] {"REJECT_INTERNATIONAL_WIRE_TRANSFER_INITIATOR","REJECT_INTERNATIONAL_WIRE_TRANSFER_APPROVERS"});
				 put("INTERNATIONAL_WIRETRANSFER_WITHDRAW_0", new String[] {"WITHDRAW_INTERNATIONAL_WIRE_TRANSFER_INITIATOR","WITHDRAW_INTERNATIONAL_WIRE_TRANSFER_APPROVERS"});
				 
				 put("INTER_RECURRING_APPROVE_0", new String[] {"APPROVE_RECURRING_INTER_BANK_TRANSFER"});
				 put("INTER_RECURRING_APPROVE_1", new String[] {"APPROVE_RECURRING_INTER_BANK_TRANSFER_INITIATOR","APPROVE_RECURRING_INTER_BANK_TRANSFER_APPROVERS"});
				 put("INTER_RECURRING_REJECT_0", new String[] {"REJECT_RECURRING_INTER_BANK_TRANSFER_INITIATOR","REJECT_RECURRING_INTER_BANK_TRANSFER_APPROVERS"});
				 put("INTER_RECURRING_WITHDRAW_0", new String[] {"WITHDRAW_RECURRING_INTER_BANK_TRANSFER_INITIATOR","WITHDRAW_RECURRING_INTER_BANK_TRANSFER_APPROVERS"});
				 
				 put("INTER_APPROVE_0", new String[] {"APPROVE_SINGLE_INTER_BANK_TRANSFER"});
				 put("INTER_APPROVE_1", new String[] {"APPROVE_SINGLE_INTER_BANK_TRANSFER_INITIATOR","APPROVE_SINGLE_INTER_BANK_TRANSFER_APPROVERS"});
				 put("INTER_REJECT_0", new String[] {"REJECT_SINGLE_INTER_BANK_TRANSFER_INITIATOR","REJECT_SINGLE_INTER_BANK_TRANSFER_APPROVERS"});
				 put("INTER_WITHDRAW_0", new String[] {"WITHDRAW_SINGLE_INTER_BANK_TRANSFER_INITIATOR","WITHDRAW_SINGLE_INTER_BANK_TRANSFER_APPROVERS"});
				 
				 put("INTRA_RECURRING_APPROVE_0", new String[] {"APPROVE_RECURRING_INTRA_BANK_TRANSFER"});
				 put("INTRA_RECURRING_APPROVE_1", new String[] {"APPROVE_RECURRING_INTRA_BANK_TRANSFER_INITIATOR","APPROVE_RECURRING_INTRA_BANK_TRANSFER_APPROVERS"});
				 put("INTRA_RECURRING_REJECT_0", new String[] {"REJECT_RECURRING_INTRA_BANK_TRANSFER_INITIATOR","REJECT_RECURRING_INTRA_BANK_TRANSFER_APPROVERS"});
				 put("INTRA_RECURRING_WITHDRAW_0", new String[] {"WITHDRAW_RECURRING_INTRA_BANK_TRANSFER_INITIATOR","WITHDRAW_RECURRING_INTRA_BANK_TRANSFER_APPROVERS"});
				 
				 put("INTRA_APPROVE_0", new String[] {"APPROVE_SINGLE_INTRA_BANK_TRANSFER"});
				 put("INTRA_APPROVE_1", new String[] {"APPROVE_SINGLE_INTRA_BANK_TRANSFER_INITIATOR","APPROVE_SINGLE_INTRA_BANK_TRANSFER_APPROVERS"});
				 put("INTRA_REJECT_0", new String[] {"REJECT_SINGLE_INTRA_BANK_TRANSFER_INITIATOR","REJECT_SINGLE_INTRA_BANK_TRANSFER_APPROVERS"});
				 put("INTRA_WITHDRAW_0", new String[] {"WITHDRAW_SINGLE_INTRA_BANK_TRANSFER_INITIATOR","WITHDRAW_SINGLE_INTRA_BANK_TRANSFER_APPROVERS"});
				 
				 put("INTERNATIONAL_RECURRING_APPROVE_0", new String[] {"APPROVE_RECUR_INTERNATIONAL_TRANSFER"});
				 put("INTERNATIONAL_RECURRING_APPROVE_1", new String[] {"APPROVE_RECUR_INTERNATIONAL_TRANSFER_INITIATOR","APPROVE_RECUR_INTERNATIONAL_TRANSFER_APPROVERS"});
				 put("INTERNATIONAL_RECURRING_REJECT_0", new String[] {"REJECT_RECUR_INTERNATIONAL_TRANSFER_INITIATOR","REJECT_RECUR_INTERNATIONAL_TRANSFER_APPROVERS"});
				 put("INTERNATIONAL_RECURRING_WITHDRAW_0", new String[] {"WITHDRAW_RECUR_INTERNATIONAL_TRANSFER_INITIATOR","WITHDRAW_RECUR_INTERNATIONAL_TRANSFER_APPROVERS"});
				 
				 put("INTERNATIONAL_APPROVE_0", new String[] {"APPROVE_SINGLE_INTERNATIONAL_TRANSFER"});
				 put("INTERNATIONAL_APPROVE_1", new String[] {"APPROVE_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR","APPROVE_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS"});
				 put("INTERNATIONAL_REJECT_0", new String[] {"REJECT_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR","REJECT_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS"});
				 put("INTERNATIONAL_WITHDRAW_0", new String[] {"WITHDRAW_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR","WITHDRAW_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS"});
				 
				 put("OWN_ACCOUNT_RECURRING_APPROVE_0", new String[] {"APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER"});
				 put("OWN_ACCOUNT_RECURRING_APPROVE_1", new String[] {"APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR","APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS"});
				 put("OWN_ACCOUNT_RECURRING_REJECT_0", new String[] {"REJECT_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR","REJECT_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS"});
				 put("OWN_ACCOUNT_RECURRING_WITHDRAW_0", new String[] {"WITHDRAW_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR","WITHDRAW_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS"});
				 
				 put("OWN_ACCOUNT_APPROVE_0", new String[] {"APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER"});
				 put("OWN_ACCOUNT_APPROVE_1", new String[] {"APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR","APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS"});
				 put("OWN_ACCOUNT_REJECT_0", new String[] {"REJECT_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR","REJECT_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS"});
				 put("OWN_ACCOUNT_WITHDRAW_0", new String[] {"WITHDRAW_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR","WITHDRAW_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS"});
				 
				 put("APPROVE_CANCEL_INTER_BANK_0", new String[] {"APPROVE_CANCEL_INTER_BANK_INITIATOR"});
				 put("APPROVE_CANCEL_INTER_BANK_1", new String[] {"SUBMIT_CANCEL_INTER_BANK_INITIATOR","SUBMIT_CANCEL_INTER_BANK_APPROVER"});
				 put("REJECT_CANCEL_INTER_BANK_0", new String[] {"REJECT_CANCEL_INTER_BANK_INITIATOR","REJECT_CANCEL_INTER_BANK_APPROVER"});
				 put("WITHDRAW_CANCEL_INTER_BANK_0", new String[] {"WITHDRAW_CANCEL_INTER_BANK_INITIATOR","WITHDRAW_CANCEL_INTER_BANK_APPROVER"});
				 
				 put("APPROVE_CANCEL_RECUR_INTER_BANK_0", new String[] {"APPROVE_CANCEL_RECUR_INTER_BANK_INITIATOR"});
				 put("APPROVE_CANCEL_RECUR_INTER_BANK_1", new String[] {"SUBMIT_CANCEL_RECUR_INTER_BANK_INITIATOR","SUBMIT_CANCEL_RECUR_INTER_BANK_APPROVER"});
				 put("REJECT_CANCEL_RECUR_INTER_BANK_0", new String[] {"REJECT_CANCEL_RECUR_INTER_BANK_INITIATOR","REJECT_CANCEL_RECUR_INTER_BANK_APPROVER"});
				 put("WITHDRAW_CANCEL_RECUR_INTER_BANK_0", new String[] {"WITHDRAW_CANCEL_RECUR_INTER_BANK_INITIATOR","WITHDRAW_CANCEL_RECUR_INTER_BANK_APPROVER"});
				 
				 put("APPROVE_CANCEL_INTRA_BANK_0", new String[] {"APPROVE_CANCEL_INTRA_BANK_INITIATOR"});
				 put("APPROVE_CANCEL_INTRA_BANK_1", new String[] {"SUBMIT_CANCEL_INTRA_BANK_INITIATOR","SUBMIT_CANCEL_INTRA_BANK_APPROVER"});
				 put("REJECT_CANCEL_INTRA_BANK_0", new String[] {"REJECT_CANCEL_INTRA_BANK_INITIATOR","REJECT_CANCEL_INTRA_BANK_APPROVER"});
				 put("WITHDRAW_CANCEL_INTRA_BANK_0", new String[] {"WITHDRAW_CANCEL_INTRA_BANK_INITIATOR","WITHDRAW_CANCEL_INTRA_BANK_APPROVER"});
				 
				 put("APPROVE_CANCEL_RECUR_INTRA_BANK_0", new String[] {"APPROVE_CANCEL_RECUR_INTRA_BANK_INITIATOR"});
				 put("APPROVE_CANCEL_RECUR_INTRA_BANK_1", new String[] {"SUBMIT_CANCEL_RECUR_INTRA_BANK_INITIATOR","SUBMIT_CANCEL_RECUR_INTRA_BANK_APPROVER"});
				 put("REJECT_CANCEL_RECUR_INTRA_BANK_0", new String[] {"REJECT_CANCEL_RECUR_INTRA_BANK_INITIATOR","REJECT_CANCEL_RECUR_INTRA_BANK_APPROVER"});
				 put("WITHDRAW_CANCEL_RECUR_INTRA_BANK_0", new String[] {"WITHDRAW_CANCEL_RECUR_INTRA_BANK_INITIATOR","WITHDRAW_CANCEL_RECUR_INTRA_BANK_APPROVER"});
				 
				 put("APPROVE_CANCEL_INTERNATIONAL_BANK_0", new String[] {"APPROVE_CANCEL_INTERNATIONAL_INITIATOR"});
				 put("APPROVE_CANCEL_INTERNATIONAL_BANK_1", new String[] {"SUBMIT_CANCEL_INTERNATIONAL_INITIATOR","SUBMIT_CANCEL_INTERNATIONAL_APPROVER"});
				 put("REJECT_CANCEL_INTERNATIONAL_BANK_0", new String[] {"REJECT_CANCEL_INTERNATIONAL_INITIATOR","REJECT_CANCEL_INTERNATIONAL_APPROVER"});
				 put("WITHDRAW_CANCEL_INTERNATIONAL_BANK_0", new String[] {"WITHDRAW_CANCEL_INTERNATIONAL_INITIATOR","WITHDRAW_CANCEL_INTERNATIONAL_APPROVER"});
				 
				 put("APPROVE_CANCEL_RECUR_INTERNATIONAL_BANK_0", new String[] {"APPROVE_CANCEL_RECUR_INTERNATIONAL_INITIATOR"});
				 put("APPROVE_CANCEL_RECUR_INTERNATIONAL_BANK_1", new String[] {"SUBMIT_CANCEL_RECUR_INTERNATIONAL_INITIATOR","SUBMIT_CANCEL_RECUR_INTERNATIONAL_APPROVER"});
				 put("REJECT_CANCEL_RECUR_INTERNATIONAL_BANK_0", new String[] {"REJECT_CANCEL_RECUR_INTERNATIONAL_INITIATOR","REJECT_CANCEL_RECUR_INTERNATIONAL_APPROVER"});
				 put("WITHDRAW_CANCEL_RECUR_INTERNATIONAL_BANK_0", new String[] {"WITHDRAW_CANCEL_RECUR_INTERNATIONAL_INITIATOR","WITHDRAW_CANCEL_RECUR_INTERNATIONAL_APPROVER"});
				 
				 put("APPROVE_CANCEL_OWNACCOUNT_0", new String[] {"APPROVE_CANCEL_OWNACCOUNT_INITIATOR"});
				 put("APPROVE_CANCEL_OWNACCOUNT_1", new String[] {"SUBMIT_CANCEL_OWNACCOUNT_INITIATOR","SUBMIT_CANCEL_OWNACCOUNT_APPROVER"});
				 put("REJECT_CANCEL_OWNACCOUNT_0", new String[] {"REJECT_CANCEL_OWNACCOUNT_INITIATOR","REJECT_CANCEL_OWNACCOUNT_APPROVER"});
				 put("WITHDRAW_CANCEL_OWNACCOUNT_0", new String[] {"WITHDRAW_CANCEL_OWNACCOUNT_INITIATOR","WITHDRAW_CANCEL_OWNACCOUNT_APPROVER"});
				 
				 put("APPROVE_CANCEL_RECUR_OWNACCOUNT_0", new String[] {"APPROVE_CANCEL_RECUR_OWNACCOUNT_INITIATOR"});
				 put("APPROVE_CANCEL_RECUR_OWNACCOUNT_1", new String[] {"SUBMIT_CANCEL_RECUR_OWNACCOUNT_INITIATOR","SUBMIT_CANCEL_RECUR_OWNACCOUNT_APPROVER"});
				 put("REJECT_CANCEL_RECUR_OWNACCOUNT_0", new String[] {"REJECT_CANCEL_RECUR_OWNACCOUNT_INITIATOR","REJECT_CANCEL_RECUR_OWNACCOUNT_APPROVER"});
				 put("WITHDRAW_CANCEL_RECUR_OWNACCOUNT_0", new String[] {"WITHDRAW_CANCEL_RECUR_OWNACCOUNT_INITIATOR","WITHDRAW_CANCEL_RECUR_OWNACCOUNT_APPROVER"});
				 
				 put("CHEQUEBOOK_APPROVE_0", new String[] {"APPROVE_CHEQUE_BOOK_REQUEST"});
				 put("CHEQUEBOOK_APPROVE_1", new String[] {"APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR","APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS"});
				 put("CHEQUEBOOK_REJECT_0", new String[] {"REJECT_CHEQUE_BOOK_REQUEST_INITIATOR","REJECT_CHEQUE_BOOK_REQUEST_APPROVERS"});
				 put("CHEQUEBOOK_WITHDRAW_0", new String[] {"WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR","WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS"});
			 }
		 };
		 
		 public static final String PARAM_EVENT_ACH_FILE_APPROVAL_REQUEST = "ACH_FILE_APPROVAL_REQUEST";
		 public static final String PARAM_EVENT_ACH_TRANSACTION_APPROVAL_REQUEST = "ACH_TRANSACTION_APPROVAL_REQUEST";
		 public static final String PARAM_EVENT_BILLPAY_APPROVAL_REQUEST = "BILLPAY_APPROVAL_REQUEST";
		 public static final String PARAM_EVENT_WIRETRANSFER_APPROVAL_REQUEST = "WIRETRANSFER_APPROVAL_REQUEST";
		 public static final String PARAM_EVENT_GENTRANSACTION_APPROVAL_REQUEST = "GENTRANSACTION_APPROVAL_REQUEST";
		 
		 public static final String INTERNATIONAL_WIRE_TRANSFER_CREATE = "Transaction/InternationalWireTransfer";
		 public static final String DOMESTIC_WIRE_TRANSFER_CREATE = "Transaction/DomesticWireTransfer";
		 public static final String CHEQUE_BOOK_REQUEST_CREATE = "Transactions/createChequeBookRequests";
		 public static final String BILL_PAY_CREATE = "Transaction/BillPayTransfer";
		 public static final String ACH_FILE_UPLOAD = "ACHFile/UploadACHFile";
		 public static final String ACH_TRANSACTION_CREATE = "ACHTransactions/createACHTransaction";
		 public static final String TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE = "Transaction/TransferToOwnAccounts";
		 public static final String INTRA_BANK_FUND_TRANSFER_CREATE ="Transaction/IntraBankAccFundTransfer";
		 public static final String INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE = "Transaction/InterBankAccFundTransfer";
		 public static final String INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE = "Transaction/InternationalAccFundTransfer";
		 

		 public static final HashMap<String, String[]> approvalRequestsSubEvents = new HashMap<String, String[]>(){
				private static final long serialVersionUID = -940508769431633185L;
				{
					 put("ACH_FILE_APPROVAL_REQUEST", new String[] {"ACHFILE_REQUEST_TO_INITIATOR","ACHFILE_REQUEST_FOR_ALL_APPROVERS"});
					 put("ACH_TRANSACTION_APPROVAL_REQUEST", new String[] {"ACHTRANSACTION_REQUEST_TO_INITIATOR","ACHTRANSACTION_REQUEST_FOR_ALL_APPROVERS"});
					 put("BILLPAY_APPROVAL_REQUEST", new String[] {"BILLPAY_REQUEST_TO_INITIATOR","BILLPAY_REQUEST_FOR_ALL_APPROVERS"});
					 put("WIRETRANSFER_APPROVAL_REQUEST", new String[] {"WIRETRANSFER_REQUEST_TO_INITIATOR","WIRETRANSFER_REQUEST_FOR_ALL_APPROVERS"});
					 put("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE", new String[] {"OWNACCOUNT_REQUEST_TO_INITIATOR","OWNACCOUNT_REQUEST_FOR_ALL_APPROVERS"});
					 put("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE_REC", new String[] {"REC_OWNACCOUNT_REQUEST_TO_INITIATOR","REC_OWNACCOUNT_REQUEST_FOR_ALL_APPROVERS"});
					 put("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE", new String[] {"INTERTRANSFER_REQUEST_TO_INITIATOR","INTERTRANSFER_REQUEST_FOR_ALL_APPROVERS"});
					 put("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_REC", new String[] {"REC_INTERTRANSFER_REQUEST_TO_INITIATOR","REC_INTERTRANSFER_REQUEST_FOR_ALL_APPROVERS"});
					 put("INTRA_BANK_FUND_TRANSFER_CREATE", new String[] {"INTRATRANSFER_REQUEST_TO_INITIATOR","INTRATRANSFER_REQUEST_FOR_ALL_APPROVERS"});
					 put("INTRA_BANK_FUND_TRANSFER_CREATE_REC", new String[] {"REC_INTRATRANSFER_REQUEST_TO_INITIATOR","REC_INTRATRANSFER_REQUEST_FOR_ALL_APPROVERS"});
					 put("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE", new String[] {"INTERNATIONAL_REQUEST_TO_INITIATOR","INTERNATIONAL_REQUEST_FOR_ALL_APPROVERS"});
					 put("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_REC", new String[] {"REC_INTERNATIONAL_REQUEST_TO_INITIATOR","REC_INTERNATIONAL_REQUEST_FOR_ALL_APPROVERS"});
		 
					 put("INTRA_BANK_FUND_TRANSFER_CANCEL_0", new String[] {"CREATE_CANCEL_INTRA_BANK"});
					 put("INTRA_BANK_FUND_TRANSFER_CANCEL_REC_0", new String[] {"CREATE_CANCEL_RECUR_INTRA_BANK"});
					 put("INTRA_BANK_FUND_TRANSFER_CANCEL_1", new String[] {"CREATE_CANCEL_INTRA_BANK_INITIATOR","CREATE_CANCEL_INTRA_BANK_APPROVER"});
					 put("INTRA_BANK_FUND_TRANSFER_CANCEL_REC_1", new String[] {"CREATE_CANCEL_RECUR_INTRA_BANK_INITIATOR","CREATE_CANCEL_RECUR_INTRA_BANK_APPROVER"});
					 
					 put("INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_0", new String[] {"CREATE_CANCEL_INTER_BANK"});
					 put("INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_REC_0", new String[] {"CREATE_CANCEL_RECUR_INTER_BANK"});
					 put("INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_1", new String[] {"CREATE_CANCEL_INTER_BANK_INITIATOR","CREATE_CANCEL_INTER_BANK_APPROVER"});
					 put("INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_REC_1", new String[] {"CREATE_CANCEL_RECUR_INTER_BANK_INITIATOR","CREATE_CANCEL_RECUR_INTER_BANK_APPROVER"});
					 
					 put("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_0", new String[] {"CREATE_CANCEL_INTERNATIONAL"});
					 put("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_REC_0", new String[] {"CREATE_CANCEL_RECUR_INTERNATIONAL"});
					 put("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_1", new String[] {"CREATE_CANCEL_INTERNATIONAL_INITIATOR","CREATE_CANCEL_INTERNATIONAL_APPROVER"});
					 put("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_REC_1", new String[] {"CREATE_CANCEL_RECUR_INTERNATIONAL_INITIATOR","CREATE_CANCEL_RECUR_INTERNATIONAL_APPROVER"});
					 
					 put("TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_0", new String[] {"CREATE_CANCEL_OWNACCOUNT"});
					 put("TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_REC_0", new String[] {"CREATE_CANCEL_RECUR_OWNACCOUNT"});
					 put("TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_1", new String[] {"CREATE_CANCEL_OWNACCOUNT_INITIATOR","CREATE_CANCEL_OWNACCOUNT_APPROVER"});
					 put("TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_REC_1", new String[] {"CREATE_CANCEL_RECUR_OWNACCOUNT_INITIATOR","CREATE_CANCEL_RECUR_OWNACCOUNT_APPROVER"});
					 
					 put("APPROVAL_CHEQUE_BOOK_REQUEST", new String[] {"CHECKBOOK_REQUEST_TO_INITIATOR","CHECKBOOK_REQUEST_FOR_ALL_APPROVERS"});
					 put("CHECKBOOK_REQUEST", new String[] {"CHECKBOOK_REQUEST_EXECUTED"});
				 }
			 };
			 public static final String CORECUSTOMERID = "coreCustomerId";
			 public static final String CORECUSTOMERIDS = "coreCustomerIds";
			 public static final String SIGNATORYGROUPID = "signatoryGroupId";
			 public static final String SIGNATORIES = "signatories";
			 public static final String SIGNATORYGROUP = "signatorygroup";
			 public static final String SIGNATORYGROUPS = "signatoryGroups";
			public static final String CUSTOMERSIGNATORYGROUP = "customersignatorygroup";
			public static final String USERNAME = "UserName";
			public static final String CUSTOMERNAME = "customerName";
			public static final String ISGROUPMATRIX = "isGroupMatrix";
			public static final String GROUPLIST = "groupList";
			public static final String GROUPRULE = "groupRule";
			public static final String SIGNATORYGROUPNAME = "signatoryGroupName";
			public static final String SIGNATORYGROUPDESCRIPTION = "signatoryGroupDescription";
			public static final String SIGNATORYGROUPVALUES = "signatoryGroupValues";
			public static final String CUSTOMERSIGNATORYGROUPVALUES = "customerSignatoryGroupValues";
			public static final String USERFIRSTNAME = "FirstName";
			public static final String USERLASTNAME = "LastName";
			public static final String ERRMSG = "errmsg";
			
			public static final String FEATURE_ACH_COLLECTION = "ACH_COLLECTION";
			public static final String FEATURE_ACH_FILES = "ACH_FILES";
			public static final String FEATURE_ACH_PAYMENT = "ACH_PAYMENT";
			public static final String FEATURE_BILL_PAY = "BILL_PAY";
			public static final String FEATURE_DOMESTIC_WIRE_TRANSFER = "DOMESTIC_WIRE_TRANSFER";
			public static final String FEATURE_INTERNATIONAL_ACCOUNT_FUND_TRANSFER = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER";
			public static final String FEATURE_INTERNATIONAL_WIRE_TRANSFER = "INTERNATIONAL_WIRE_TRANSFER";
			public static final String FEATURE_INTER_BANK_ACCOUNT_FUND_TRANSFER = "INTER_BANK_ACCOUNT_FUND_TRANSFER";
			public static final String FEATURE_INTRA_BANK_FUND_TRANSFER = "INTRA_BANK_FUND_TRANSFER";
			public static final String FEATURE_P2P = "P2P";
			public static final String FEATURE_TRANSFER_BETWEEN_OWN_ACCOUNT = "TRANSFER_BETWEEN_OWN_ACCOUNT";
			
			public static final String AM_REQUIRE_APPROVALS_FOR_ENTITY_TYPE = "AM_REQUIRE_APPROVALS_FOR_ENTITY_TYPE";
			public static final String AM_MAX_LIMIT_NO_RULES_ALLOW_STP = "AM_MAX_LIMIT_NO_RULES_ALLOW_STP";
			public static final String AM_DAILY_LIMIT_NO_RULES_ALLOW_STP = "AM_DAILY_LIMIT_NO_RULES_ALLOW_STP";
			public static final String AM_WEEKLY_LIMIT_NO_RULES_ALLOW_STP = "AM_WEEKLY_LIMIT_NO_RULES_ALLOW_STP";
			public static final String AM_NON_MONETORY_NO_RULES_ALLOW_STP = "AM_NON_MONETORY_NO_RULES_ALLOW_STP";
			public static final String AM_MODE_DEFAULT_SIGN_GROUP = "AM_MODE_DEFAULT_SIGN_GROUP";
			public static final String PAYMENT_BACKEND = "PAYMENT_BACKEND";

			// DBX Properties File
			public static final String PROPERTIES_FILE = "KonyDBX.properties";

			// Configurable params constants
			public static final String ENCRYPTED_KEY = "TEMPLATE_ENCRYPTION_KEY";


			// Date format constants
			public static final String DATE_FULL = "yyyy-MM-dd'T'HH:mm:ss";
			public static final String DATE_YYYYMMDD = "yyyy-MM-dd";
			public static final String DBX_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
			public static final String TIME_FORMAT = "HHmm";
			public static final String TIME_FULL = "hh:mm aa";
			public static final String TIME_OSI = "T00:00:00Z";

			// KonyFabric Status Codes
			public static final String PARAM_HTTP_STATUS_OK = "200";
			public static final String PARAM_OP_STATUS_ERROR = "5000";
			public static final String PARAM_OP_STATUS_OK = "0";

			public static final String HTTP_HDR_X_KONY_AUTH = "X-Kony-Authorization";

			public static final String PARAM_LOOP_COUNT = "loop_count";
			public static final String PARAM_CAMEL_CASE_LOOP_COUNT = "loopCount";
			public static final String PARAM_LOOP_DATASET = "LoopDataset";

			// Generic parameters
			public static final String PARAM_ERR_MSG = "errmsg";
			public static final String PARAM_ERR_CODE = "errcode";
			public static final String PARAM_FAULT_CODE = "faultcode";
			public static final String PARAM_HTTP_STATUS_CODE = "httpStatusCode";
			public static final String PARAM_OP_STATUS = "opstatus";
			public static final String PARAM_SUCCESS = "success";
			public static final String DBP_HOST_URL = "DBP_HOST_URL";

			// Parameter Data Types
			public static final String PARAM_DATATYPE_INT = "int";
			public static final String PARAM_DATATYPE_STRING = "string";

			// Constants for days of the week
			public static final String DOW_MONDAY = "Monday";
			public static final String DOW_TUESDAY = "Tuesday";
			public static final String DOW_WEDNESDAY = "Wednesday";
			public static final String DOW_THURSDAY = "Thursday";
			public static final String DOW_FRIDAY = "Friday";
			public static final String DOW_SATURDAY = "Saturday";
			public static final String DOW_SUNDAY = "Sunday";
			public static final String LAST_DAYOFMONTH = "28";
			public static final String TOTAL_MONTHS_IN_A_YEAR = "12";
			public static final String JANUARY = "01";

			// DBX Account Types
			public static final String DBX_ACCOUNT_TYPE_1 = "1";
			public static final String DBX_ACCOUNT_TYPE_1_DESC = "Checking";
			public static final String DBX_ACCOUNT_TYPE_2 = "2";
			public static final String DBX_ACCOUNT_TYPE_2_DESC = "Savings";
			public static final String DBX_ACCOUNT_TYPE_3 = "3";
			public static final String DBX_ACCOUNT_TYPE_3_DESC = "CreditCard";
			public static final String DBX_ACCOUNT_TYPE_4 = "4";
			public static final String DBX_ACCOUNT_TYPE_4_DESC = "Deposit";
			public static final String DBX_ACCOUNT_TYPE_5 = "5";
			public static final String DBX_ACCOUNT_TYPE_5_DESC = "Mortgage";
			public static final String ACCOUNT_TYPE_SPROUT = "Sprout";

			// DBX General Parameters
			public static final String PARAM_ACCOUNT_HOLDER = "accountHolder";
			public static final String PARAM_ACCOUNT_NAME = "accountName";
			public static final String PARAM_ACCOUNT_TYPE = "accountType";
			public static final String PARAM_ACCOUNT_ID ="accountId";
			public static final String PARAM_AMOUNT = "amount";
			public static final String PARAM_BANK_NAME = "bankName";
			public static final String PARAM_DOLLAR_FILTER = "$filter";
			public static final String PARAM_DOLLAR_ORDERBY = "$orderby";
			public static final String PARAM_DOLLAR_SELECT = "$select";
			public static final String PARAM_EMAIL = "email";
			public static final String PARAM_FREQUENCY_TYPE = "frequencyType";
			public static final String PARAM_FREQUENCY_END_DATE = "frequencyEndDate";
			public static final String PARAM_DAY1 = "day1";
			public static final String PARAM_DAY2 = "day2";
			public static final String PARAM_STATUS_DESCRIPTION = "statusDescription";
			public static final String PARAM_FROM_ACCOUNT_NAME = "fromAccountName";
			public static final String PARAM_FROM_ACCOUNT_NUMBER = "fromAccountNumber";
			public static final String PARAM_FROM_ACCOUNT_TYPE = "fromAccountType";
			public static final String PARAM_FROM_CHECK_NUMBER = "fromCheckNumber";
			public static final String PARAM_EXPIRATION_CHECK = "ExpirationCheck";
			public static final String PARAM_FREQUENCY_ENDDATE = "frequencyEndDate";
			public static final String PARAM_ID = "id";
			public static final String PARAM_INTERNATIONAL_BANK_ACCOUNT = "isInternationalAccount";
			public static final String PARAM_PAYEE_ID = "payeeId";
			public static final String PARAM_PAYEE_NAME = "payeeName";
			public static final String PARAM_PAYEE_NICK_NAME = "payeeNickName";
			public static final String PARAM_PHONE = "phone";
			public static final String PARAM_SAME_BANK_ACCOUNT = "isSameBankAccount";
			public static final String PARAM_SCHEDULED = "isScheduled";
			public static final String PARAM_SCHEDULED_DATE = "scheduledDate";
			public static final String PARAM_SEARCH_END_DATE = "searchEndDate";
			public static final String PARAM_SEARCH_MINIMUM_AMOUNT = "searchMinAmount";
			public static final String PARAM_SEARCH_MIXIMUM_AMOUNT = "searchMaxAmount";
			public static final String PARAM_SEARCH_START_DATE = "searchStartDate";
			public static final String PARAM_SEARCH_TRANSCTION_TYPE = "searchTransactionType";
			public static final String PARAM_TO_ACCOUNT_NAME = "toAccountName";
			public static final String PARAM_EXTERNAL_ACCOUNT_NUMBER = "ExternalAccountNumber";
			public static final String PARAM_TO_ACCOUNT_NUMBER = "toAccountNumber";
			public static final String PARAM_TO_ACCOUNT_TYPE = "toAccountType";
			public static final String PARAM_TO_CHECK_NUMBER = "toCheckNumber";
			public static final String PARAM_TRANSACTION_ID = "transactionId";
			public static final String PARAM_TRANSACTION_NOTES = "transactionsNotes";
			public static final String PARAM_TRANSACTION_TYPE = "transactionType";
			public static final String PARAM_TRANSFER_DESTINATION_ACCOUNT = "supportTransferTo";
			public static final String PARAM_TRANSFER_SOURCE_ACCOUNT = "supportTransferFrom";
			public static final String PARAM_DEPOSIT_DESTINATION_ACCOUNT = "supportDeposit";
			public static final String PARAM_USER_ID = "user_id";
			public static final String PARAM_SUPPORT_CHECKS = "supportChecks";
			public static final String BACKEND_ID = "BackendId";
			public static final String SEQUENCE_NUMBER = "sequence_number";
			public static final String IDENTIFIER_NAME = "identifier_name";
			public static final String PARAM_CUSTOMER_ID = "customerId";
			public static final String PARAM_SUPPORT_BILLPAY = "supportBillPay";

			// DBX User Parameters
			public static final String KONY_DBX_USER_ADDR1 = "AddressLine1";
			public static final String KONY_DBX_USER_ADDR2 = "AddressLine2";
			public static final String KONY_DBX_USER_CITY = "CityName";
			public static final String KONY_DBX_USER_DEFAULT_BILLPAY_ACCT = "default_account_billPay";
			public static final String KONY_DBX_USER_DEFAULT_CARDLESS_ACCT = "default_account_cardless";
			public static final String KONY_DBX_USER_DEFAULT_DEPOSIT_ACCT = "default_account_deposit";
			public static final String KONY_DBX_USER_DEFAULT_P2P_FROMACCT = "default_from_account_p2p";
			public static final String KONY_DBX_USER_DEFAULT_P2P_TOACCT = "default_to_account_p2p";
			public static final String KONY_DBX_USER_DEFAULT_PAYMENTS_ACCT = "default_account_payments";
			public static final String KONY_DBX_USER_DEFAULT_XFER_ACCT = "default_account_transfers";
			public static final String KONY_DBX_USER_DOB = "dateOfBirth";
			public static final String KONY_DBX_USER_EMAIL = "email";
			public static final String KONY_DBX_USER_FIRSTNAME = "userFirstName";
			public static final String KONY_DBX_USER_ID = "Id";
			public static final String KONY_DBX_USER_LOWERCASE_ID = "id";
			public static final String KONY_DBX_USER_id = "id";
			public static final String KONY_DBX_USER_ISBILLPAY_ACTIVE = "isBillPayActivated";
			public static final String KONY_DBX_USER_ISBILLPAY_SUPPORTED = "isBillPaySupported";
			public static final String KONY_DBX_USER_ISP2P_ACTIVE = "isP2PActivated";
			public static final String KONY_DBX_USER_ISP2P_SUPPORTED = "isP2PSupported";
			public static final String KONY_DBX_USER_LASTNAME = "userLastName";
			public static final String KONY_DBX_USER_LASTLOGINTIME = "lastlogintime";
			public static final String KONY_DBX_USER_NAME = "userName";
			public static final String KONY_DBX_USER_PHONE = "phone";
			public static final String KONY_DBX_USER_ROLE = "role";
			public static final String KONY_DBX_USER_SSN = "ssn";
			public static final String KONY_DBX_USER_STATE = "RegionName";
			public static final String KONY_DBX_USER_ZIP = "zipcode";

			// DBX Account Parameters
			public static final String KONY_DBX_ACCOUNT_ID = "Account_id";
			public static final String KONY_DBX_ACCOUNT_NAME = "AccountName";
			public static final String KONY_DBX_ACCOUNT_NICK_NAME = "NickName";
			public static final String KONY_DBX_ACCOUNT_STATUS_CODE = "Status_id";
			public static final String KONY_DBX_ACCOUNT_STATUS_DESC = "StatusDesc";
			public static final String KONY_DBX_ACCOUNT_TYPE = "Type_id";
			public static final String KONY_DBX_ACCOUNT_CURRENCY_CODE = "CurrencyCode";
			public static final String KONY_DBX_ACCOUNT_AVAIL_BAL = "AvailableBalance";
			public static final String KONY_DBX_ACCOUNT_CURRENT_BAL = "CurrentBalance";
			public static final String KONY_DBX_ACCOUNT_OUTSTANDING_BAL = "OutstandingBalance";

			// DBX Payment Frequencies
			public static final String PAYMENT_ONCE = "Once";
			public static final String PAYMENT_DAILY = "Daily";
			public static final String PAYMENT_WEEKLY = "Weekly";
			public static final String PAYMENT_EVERY_TWO_WEEKS = "Every Two Weeks";
			public static final String PAYMENT_BI_WEEKLY = "BiWeekly";
			public static final String PAYMENT_EVERY_FOUR_WEEKS = "Every Four Weeks";
			public static final String PAYMENT_MONTHLY = "Monthly";
			public static final String PAYMENT_TWICE_MONTHLY = "Twice Monthly";
			public static final String PAYMENT_EVERY_OTHER_MONTHLY = "Every Other Month";
			public static final String PAYMENT_QUARTERLY = "Quarterly";
			public static final String PAYMENT_HALF_YEARLY = "Half Yearly";
			public static final String PAYMENT_YEARLY = "Yearly";

			public static final String FREQUENCY_ONCE = "Once";
			public static final String FREQUENCY_RECURRING = "Recurring";
			public static final String FREQUENCY_DAILY = "Daily";
			public static final String FREQUENCY_WEEKLY = "Weekly";
			public static final String FREQUENCY_MONTHLY = "Monthly";
			public static final String FREQUENCY_QUARTERLY = "Quarterly";
			public static final String FREQUENCY_HALF_YEARLY = "Half Yearly";
			public static final String FREQUENCY_YEARLY = "Yearly";
			public static final String FREQUENCY_SEMIMONTHLY = "Semimonthy";
			public static final String FREQUENCY_BIMONTHLY = "Bimonthy";

			// DBX Request Query String Parameters
			public static final String QUERY_PARAM_LIMIT = "limit";
			public static final String QUERY_PARAM_ORDER = "order";
			public static final String QUERY_PARAM_OFFSET = "offset";
			public static final String QUERY_PARAM_SORTBY = "sortBy";

			// ODATA Condition constants
			public static final String EQUAL = " eq ";
			public static final String NOT_EQUAL = " ne ";
			public static final String AND = " and ";
			public static final String OR = " or ";
			public static final String GREATER_THAN_OR_EQUAL = " ge ";
			public static final String LESS_THAN_OR_EQUAL = " le ";
			public static final String LESS_THAN = " lt ";
			public static final String GREATER_THAN = " gt ";

			// Regex constants
			public static final String VALID_EMAIL_ADDRESS_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

			// DBX Account Categories
			public static final String ACCOUNT_TYPE_CHECKING = "Checking";
			public static final String ACCOUNT_TYPE_SAVINGS = "Savings";
			public static final String ACCOUNT_TYPE_CREDIT_CARD = "CreditCard";
			public static final String SESSION_ATTRIB_ACCOUNT = "Accounts";
			public static final String SESSION_ATTRIB_MICR_ACCOUNT = "MICRAccounts";

			public static final String ACCOUNT_TYPE_DEPOSIT = "Deposit";
			public static final String ACCOUNT_TYPE_MORTGAGE = "Mortgage";
			public static final String ACCOUNT_TYPE_LOAN = "Loan";

			// DBX Transaction Type
			public static final String TRANSCTION_TYPE_DEPOSIT = "Deposit";
			public static final String TRANSCTION_TYPE_DEPOSITS = "Deposits";
			public static final String TRANSCTION_TYPE_INTERNAL_TRANSFER = "InternalTransfer";
			public static final String TRANSCTION_TYPE_EXTERNAL_TRANSFER = "ExternalTransfer";
			public static final String TRANSCTION_TYPE_WITHDRAWAL = "Withdrawal";
			public static final String TRANSCTION_TYPE_wITHDRAWAL = "withdrawal";
			public static final String TRANSCTION_TYPE_WITHDRAWALS = "Withdrawals";
			public static final String TRANSCTION_TYPE_wITHDRAWALS = "withdrawals";
			public static final String TRANSCTION_TYPE_CHECKS = "Checks";
			public static final String TRANSCTION_TYPE_TRANSFERS = "Transfers";
			public static final String TRANSCTION_TYPE_BILL_PAY = "BillPay";
			public static final String TRANSCTION_TYPE_P2P = "p2p";

			// Constants for Session Attribute Keys
			public static final String SESSION_ATTRIB_CUSTOMER = "customer";

			public static final String BANK_NAME = "Kony Bank";
			public static final String YES = "1";
			public static final String NO = "0";

			public static final String TEST = "test";
			String PARAM_PERSON_CENTRIC = "PERSON";
			String PARAM_ACCOUNT_CENTRIC = "ACCOUNT";

			String PARAM_DBX_DB_NAME = "DBX_DB_NAME";
			String PARAM_DBX_DB_DRIVER = "DBX_DB_DRIVER";
			String PARAM_DBX_DB_HOST_URL = "DBX_DB_HOST_URL";
			String PARAM_DBX_DB_USERNAME = "DBX_DB_USERNAME";
			String PARAM_DBX_DB_PASSWORD = "DBX_DB_PASSWORD";

			String CONST_DRIVER_MYSQL = "mysql";
			String CONST_DRIVER_ORACLE = "oracle";

			String C_SETMAXLIFETIME = "600000";
			String C_SETMINIMUMIDLE = "1";
			String C_SETIDLETIMEOUT = "300000";
			String C_SETMAXIMUMDBPOOLSIZE = "10";

			public static final String SECTION_CONFIGURATION = "Configuration";

			public static final String PROPERTYNAME_SCHEMANAME = "SchemaName";

			public static final String HIKARI_PREFIX = "Hikari";

			public static final String DEVICE_ID = "X-Kony-DeviceId";

			public static final String USER_ID_ANONYMOUS = "anonymous";


			public static final String PARAM_SEQUENCENUMBER = "sequenceNumber";

			public static final String BACKEND_TYPE = "BackendType";
			// public static final String USER_ID_ANONYMOUS = "anonymous";

			public static final String DBX_DB_SERVICE_NAME = "dbpRbLocalServicesdb";
			public static final String DBX_DB_BACKEND_IDENTIFIER_GET = "dbxdb_backendidentifier_get";

			public static final String PARAM_BACKEND_IDENTIFIERS = "backendIdentifiers";
			public static final String DS_BACKEND_IDENTIFIER = "backendidentifier";
			public static final String PARAM_IDENTIFIER_NAME = "identifier_name";
			public static final String CONSTANT_UNIQUE_ID = "CustomerId";
			
			public static final String PARAM_CODE = "code";
			public static final String PARAM_TYPE = "type";
			public static final String CONSTANT_DELIMETER = "-";
			
			public static final String PARAM_INST_ID_REFERENCE = "instructionIdReference";
			public static final int PARAM_INST_ID_REFERENCE_LEN = 35;
			
			public static final String AWAITING_FUNDS = "AwaitingFunds";
			public static final String CREDIT_VALUE_DATE = "creditValueDate";

			public static final String CONTENT_TYPE ="contentType";

}
