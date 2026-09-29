package com.temenos.dbx.eum.product.constants;

public class ACHConstants {

	public static final String TEMPLATE_TYPE_ACH = "1";
	public static final String TEMPLATE_TYPE_ACH_COLLECTION = "1";
	public static final String TEMPLATE_TYPE_ACH_PAYMENT = "2";
	public static final String TEMPLATE_REQUEST_TYPE_FEDERAL_TAX = "8";

	public static final String ACH_TRANSACTION_TYPE_PAYMENT = "Payment";
	public static final String ACH_TRANSACTION_TYPE_COLLECTION = "Collection";
	public static final String ACH_TRANSACTION_TYPE_PAYMENT_AND_COLLECTION = "Payment and Collection";
	public static final String TEMPLATE_ID_NOT_AVAILABLE = "Null";
	public static final int TEMPLATE_ID_DEFAULT = -1;
	public static final String ACH_TRANSACTION = "achtransaction";

	public static final String TEMPLATE_REQUEST_TYPE = "TemplateRequestType";
	public static final String Transaction_Type_ID = "transactionType_id";
	public static final String BB_Template_Request_Type = "bbtemplaterequesttype";
	
	public static final String TRANSACTION_TYPES = "TransactionTypes";
	public static final String TAX_TYPE = "TaxType";
	public static final String TAX_SUB_TYPE = "bbTaxSubType";
	public static final String Tax_Type = "taxType";
	public static final String TRANSACTION_ID = "_transactionId";
	public static final String TRANSACTION_RECORD_ID = "_transactionRecordId";
	public static final String TRANSACTION_RECORDS = "TransactionRecords";
	public static final String TRANSACTION_SUBRECORDS = "TransactionSubRecord";
	
	public static final String ORGANIZATION_ID = "Organization_id";
	public static final String CUSTOMER_ACCOUNTS = "customeraccounts";
	
	public static final String _ACH_FILE_ID = "_achFile_id";
	public static final String ACHFILE_RECORD = "achfilerecord";
	public static final String ACHFILE_SUBRECORD = "achfilesubrecord";
	public static final String ACHFILERECORDS = "AchFileRecords";
	public static final String ACHFILESUBRECORDS = "AchFileSubrecords";
	public static final String ACH_FILE_ID = "achFileId";
	public static final String ACH_FILE_RECORD_ID = "achFileRecordId";
	public static final String ACH_FILE_TIMESTAMP_FORMAT = "yyyy-MM-dd";
	public static final String ACH_FILE_RECORD_TIMESTAMP_FORMAT = "yyMMdd";
	public static final String FILE_EXTENSION = "fileextension";
	
	public static final String SID_EVENT_SUCCESS = "SID_EVENT_SUCCESS";
    public static final String SID_EVENT_FAILURE = "SID_EVENT_FAILURE";
    public static final String NO_TEMPLATE_USED ="No Template Used";
    public static final String ACH_PAYMENT_TEMPLATE_TRANSFER = "ACH_PAYMENT_TEMPLATE_TRANSFER";
    public static final String ACH_COLLECTION_TEMPLATE_TRANSFER = "ACH_COLLECTION_TEMPLATE_TRANSFER";
    public static final String ACH_PAYMENT_ONE_TIME_TRANSFER = "ACH_PAYMENT_ONE_TIME_TRANSFER";
    public static final String ACH_COLLECTION_ONE_TIME_TRANSFER = "ACH_COLLECTION_ONE_TIME_TRANSFER";
    
}
