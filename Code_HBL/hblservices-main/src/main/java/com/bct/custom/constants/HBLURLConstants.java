package com.bct.custom.constants;

public class HBLURLConstants {
public static final String FILTER = "$filter";

public static final String C360_SERVICEID="HBLC360MerchantCRUDService";
public static final String GET_ALL_MERCHANT_CATEGORIES_OPERATION="dbxdb_merchantcategory_get";
public static final String GET_MERCHANT_DETAILS_OPERATION="dbxdb_merchantdetails_get";
public static final String GET_MERCHANT_FIELDS_OPERATION="dbxdb_merchantfields_get";
public static final String GET_PAYMENT_AGGREGATOR_OPERATION="dbxdb_paymentaggregator_get";
public static final String UPDATE_PAYMENT_AGGREGATOR_OPERATION="dbxdb_paymentaggregator_update";  
public static final String CREATE_PAYMENT_AGGREGATOR_OPERATION="dbxdb_paymentaggregator_create";
public static final String UPDATE_MERCHANT_DETAILS_OPERATION="dbxdb_merchantdetails_update";  
public static final String CREATE_MERCHANT_DETAILS_OPERATION="dbxdb_merchantdetails_create";  
public static final String UPDATE_MERCHANT_FIELDS_OPERATION="dbxdb_merchantfields_update";  
public static final String CREATE_MERCHANT_FIELDS_OPERATION="dbxdb_merchantfields_create";  
public static final String UPDATE_MERCHANT_CATEGORIES_OPERATION="dbxdb_merchantcategory_update";  
public static final String CREATE_MERCHANT_CATEGORIES_OPERATION="dbxdb_merchantcategory_create";
public static final String CONNECTIPS_SERVICEID="ConnectIPS";
public static final String CONNECTIPS_OP_GET_MERCHANTCATEGORY="getMerchantCategories";
public static final String UPDATE_MERCHANT_PAYMENT_CHARGES_OPERATION="dbxdb_merchantpaymentcharges_update";
public static final String GET_MERCHANT_PAYMENT_CHARGES="dbxdb_merchantpaymentcharges_get"; 

public static final String HBL_OLB_CRUD_OPERATION_SERVICE = "HBLMerchantCRUDService";
public static final String BILL_PAY_HISTORY_OPERATION = "dbxdb_billpaytransfers_get";
public static final String BRANCH_DETAILS_GET_OPERATION = "dbxdb_branchdetails_get";

public static final String TRANSACTIONPIN_SERVICE="dbpRbLocalServicesdb";
public static final String TRANSACTIONPIN_OPERATION="dbxdb_transactionpin_create";
public static final String DISPUTE_CREATE="dbxdb_disputeTransactions_create";

public static final String NORMAL_FD_CREATE="dbxdb_normalfixeddeposits_create";
public static final String HIMAL_REMIT_FD_CREATE="dbxdb_himalfixeddeposits_create";
public static final String STRUCTURED_FD_CREATE="dbxdb_structuredfixeddeposits_create";

public static final String CROSSBORDER_API_LOG_CREATE="dbxdb_crossborderapilog_create";
public static final String ESEWA_API_LOG_CREATE="dbxdb_esewaApiAuditLog_create";
public static final String ESEWA_TRANSACTION_LOG_CREATE="dbxdb_esewaTransactionLog_create";
public static final String ESEWA_REPROCESS_LOG_CREATE="dbxdb_esewaTransactionReprocessLog_create";

public static final String ESEWA_TRANSACTION_PENDING_LOG_CREATE="dbxdb_esewaTransactionPendingLog_create";
public static final String ESEWA_TRANSACTION_PENDING_LOG_GET="dbxdb_esewaTransactionPendingLog_get";
public static final String ESEWA_TRANSACTION_PENDING_LOG_UPDATE="dbxdb_esewaTransactionPendingLog_update";

public static final String CARD_LIMIT_CREATE="dbxdb_cardConfigLimit_create";
public static final String CARD_LIMIT_UPDATE="dbxdb_cardConfigLimit_update";
public static final String CARD_LIMIT_DELETE="dbxdb_cardConfigLimit_delete";
public static final String CARD_LIMIT_GET="dbxdb_cardConfigLimit_get";

public static final String CANTSIGNIN_OTP_CREATE="dbxdb_OTP_create";
public static final String CANTSIGNIN_OTP_UPDATE="dbxdb_OTP_update";

public static final String REQUEST_NEW_CARD_CREATE="dbxdb_requestnewcard_create";
public static final String REQUEST_NEW_CARD_GET="dbxdb_requestnewcard_get";
public static final String REQUEST_NEW_CARD_DATASET="requestnewcard";
public static final String TAB_MERCHANT_DETAILS ="dbxdb.merchantdetails";
public static final String TAB_MERCHANT_CATEGORIES ="dbxdb.merchantcategory";
public static final String TAB_MERCHANT_PAYMENT_CHARGES="dbxdb.merchantpaymentcharges"; 
public static final String TAB_MERCHANT_FIELDS="dbxdb.merchantfields";
public static final String DELETE_MERCHANT_PAYMENT_CHARGES="dbxdb_merchantpaymentcharges_delete";
public static final String DELETE_MERCHANT_FIELDS="dbxdb_merchantfields_delete";
public static final String GET_MASTER_MERCHANTS="dbxdb_mastermerchants_get";
public static final String GET_CARD_LIMIT="dbxdb_cardLimit_get";

public static final String QR_TRANSACTION_HISTORY_GET = "dbxdb_qrtransaction_history_get";
public static final String QR_TRANSACTION_HISTORY_CREATE = "dbxdb_qrtransaction_history_create";
public static final String QRPAYMENT_CHARGES_GET = "dbxdb_qrpaymentcharges_get";
public static final String CONNECTIPS_OP_VALIDATE_QR = "validateQR";
public static final String CONNECTIPS_QR_SERVICEID = "HBLQRNepalPay";

public static final String HBL_CUSTOM_QRSERVICE = "HBLCustomQRService";
public static final String GET_QR_PAYMENT_CHARGES = "getQRPaymentCharges";
public static final String QR_VALIDATION_RESULTS_CREATE = "dbxdb_qr_validation_results_create";
public static final String GET_QR_VALIDATION_RESULTS = "dbxdb_qr_validation_results_get";
public static final String CONNECTIPS_OP_QR_PAYMENT = "QRPayment";
public static final String CONNECTIPS_OP_QR_PAYMENT_DIRECT_DEBIT = "QRPaymentDirectDebit";

public static final String SMARTQR_SERVICEID = "HBLSmartQR";
public static final String SMARTQR_OP_MERCHANT_VERIFICATION = "merchantVerification";
public static final String CONNECTIPS_OP_VALIDATE_OTHER_BANK_ACCOUNT = "validateOtherBankAccount";
public static final String CIPS_TRANS_HISTORY_OPERATION = "dbxdb_interbankfundtransfers_get";

public static final String CARDLESSCARDTRANSACTION_RESULTS_CREATE = "dbxdb_cardless_transaction_audit_log_create";

}
