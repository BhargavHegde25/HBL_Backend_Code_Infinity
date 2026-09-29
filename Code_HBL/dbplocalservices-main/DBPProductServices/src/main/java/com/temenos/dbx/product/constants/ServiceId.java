package com.temenos.dbx.product.constants;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Contains constants for Integration service name/ Object name
 */
public final class ServiceId {
	public static final String DBPRBLOCALSERVICEDB = "dbpRbLocalServicesdb";
	public static final String DBP_PRODUCT_SERVICES = "dbpProductServices";
	public static final String BILL_PAY_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("BillpayLOB");
	public static final String P2P_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("P2PLOB");
	public static final String DOMESTIC_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("DomesticWireTransferLOB");
	public static final String INTERNATIONAL_WIRE_TRANSFER_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("InternationalWireTransferLOB");
	public static final String INTRA_BANK_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("IntrabankFundTransferLOB");
    public static final String INTER_BANK_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("InterbankFundTransferLOB");
    public static final String OWN_ACCOUNT_TRANSFER_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("OwnAccountTransferLOB");
    public static final String INTERNATIONAL_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("InternationalFundTransferLOB");
    public static final String BULK_BILL_PAY_SERVICE = "CreateBulkBillPayOrchestration";
    public static final String ACH_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("ACHLineOfBusinessServices");
    public static final String ACH_ORCH_LOB = TransactionBackendURL.getBackendURL("ACHOrchLOB");
    public static final String DBP_ACH_SERVICES = "dbpACHServices";
    public static final String BULK_WIRE_FILE_DOWNLOAD_SERVICE = "downloadFileBulkWire";
    public static final String BULK_WIRE_SAMPLE_FILE_DOWNLOAD_SERVICE = "downloadSampleFileBulkWire";
    public static final String BULK_WIRE_TRANSFER_SERVICE = "CreateBulkWireTransferOrch";
    public static final String CHEQUE_MANAGEMENT_SERVICE = "dbpChequeManagementServices";
    public static final String DBPTRANSACTIONORCH = "dbpBulkWireOrch";
    public static final String RDC_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("RDCLOB");
    public static final String TRANSACTIONSLIMIT = "TransactionsLimit";

    /* payee LOB Services Mappings */
    public static final String BILL_PAY_PAYEE_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("BillPayPayeeLOB");
    public static final String INTER_BANK_PAYEE_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("InterBankPayeeLOB");
    public static final String EXTERNAL_PAYEE_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("ExternalPayeeLOB");
	public static final String DBP_EXTERNAL_PAYEES_ORCH = "dbpExternalPayeesOrch";
    public static final String WIRE_TRANSFERS_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("WireTransfersPayeeLOB");
	public static final String INTRA_BANK_PAYEE_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("IntraBankPayeeLOB");
	public static final String INTERNATIONAL_PAYEE_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("InternationalPayeeLOB");
	public static final String P2P_PAYEE_LINE_OF_BUSINESS_SERVICE = TransactionBackendURL.getBackendURL("P2PPayeeLOB");
	
	public static final String BACKENDPAYEESERVICESORCH = TransactionBackendURL.getBackendURL("BackendPayeeServicesOrch");
	
	public static final String FETCHUSERNAMESERVICESORCH = TransactionBackendURL.getBackendURL("CustomerAndCustomerGroup");
	
	public static final String DBPNPBULKPAYMENTSERVICES = "dbpNPBulkPaymentServices";
	public static final String DBPNONPRODUCTSERVICES = "dbpNonProductServices";
	public static final String DBPNPBULKPAYMENTSORCH = "dbpNPBulkPaymentsOrch";
	public static final String DBPBULKPAYMENTSERVICES = "dbpBulkPaymentServices";
	
	//forex services mappings
	public static final String DBPNPFOREXSERVICES = "dbpNpForexServices";
	public static final String DBPFOREXORCH = "dbpForexOrch";
	
    public static final String DBP_APPROVAL_REQUEST_SERVICES = "dbpApprovalRequestServices";

    public static final String RBOBJECTS = "RBObjects";
    public static final String DBP_TRANSACTION_SERVICES = "dbpTransactionServices";
    public static final String DBP_CHEQUEMANAGEMENT_SERVICES = "dbpChequeManagementServices";
    
    public static final String SERVICE_REQUEST_JAVA_SERVICE = "ServiceRequestJavaService"; 
    public static final String DBP_DATA_STORAGE_APIS = "DBPDataStorageAPIs";
    public static final String EUM_PRODUCT_SERVICES = "eumProductServices";
    
    public static final String DBP_TRADEFINANCE_SERVICES = "TradeFinanceServices";
    
    public static final String APPROVAL_TRANSACTION_SERVICE = TransactionBackendURL.getBackendURL("ApprovalTransactionService");
    
    public static final String VALIDATE_TRANSACTION_SERVICE = TransactionBackendURL.getBackendURL("ValidateTransactionService");
    
    public static final String T24_IS_PAYMENTS_VIEW = TransactionBackendURL.getBackendURL("T24ISPaymentsView");

    // SRMS Backend Integration services
    public static final String SRMSBACKEND_PAYMENTORDER_INTEGRATIONSERVICE = TransactionBackendURL.getBackendURL("SRMSBackendPaymentOrderIntegrationService");
    public static final String SRMSBACKEND_STANDINGORDER_INTEGRATIONSERVICE = TransactionBackendURL.getBackendURL("SRMSBackendStandingOrderIntegrationService");
    public static final String SRMSBACKEND_PAYMENTSVIEW_INTEGRATIONSERVICE = TransactionBackendURL.getBackendURL("SRMSBackendPaymentsViewIntegrationService");

}