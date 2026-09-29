package com.temenos.dbx.product.constants;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Contains constants for integration operation Name/ object verb name
 */
public final class OperationName {

	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");

	public static final String DB_APPROVALRULE_GET = SCHEMA_NAME+"_approvalrule_get";
	public static final String DB_APPROVALMATRIX_DELETE = SCHEMA_NAME+"_approvalmatrix_delete";
	public static final String DB_APPROVALMATRIX_GET = SCHEMA_NAME+"_approvalmatrix_get";
	public static final String DB_CUSTOMERAPPROVALMATRIX_GET = SCHEMA_NAME+"_customerapprovalmatrix_get";
	public static final String DB_CUSTOMERAPPROVALMATRIXTEMPLATE_GET = SCHEMA_NAME+"_customerapprovalmatrixtemplate_get";
	public static final String DB_APPROVALMATRIX_DEFAULT_DELETE = SCHEMA_NAME+"_approvalmatrix_default_delete_proc";
	public static final String DB_APPROVALMATRIX_DEFAULT_CREATE = SCHEMA_NAME+"_approvalmatrix_default_create_proc";
	public static final String DB_APPROVALMATRIX_CREATE = SCHEMA_NAME+"_approvalmatrix_create_proc";
	public static final String DB_APPROVALMATRIX_SOFTDELETE = SCHEMA_NAME+"_approvalmatrix_update_softdeleteflag_proc";
	public static final String DB_GETACCOUNTACTIONAPPROVERLIST = SCHEMA_NAME+"_account_action_approvers_proc";
	public static final String DB_GETALLTEMPLATERECORDS = SCHEMA_NAME + "_fetch_achtemplaterecords_proc";
	public static final String DB_GETALLTEMPLATESUBRECORDS = SCHEMA_NAME + "_fetch_achtemplatesubrecords_proc";
	public static final String DB_BBREQUEST_GET = SCHEMA_NAME+"_bbrequest_get";

	public static final String DB_WIRETRANSFERS_FETCH = SCHEMA_NAME+"_fetch_wiretransfer_details_proc";
	public static final String DB_APPROVAL_REQUEST_COUNT_FETCH = SCHEMA_NAME+"_approvalrequest_counts_proc";

	public static final String DB_CUSTOMER_GET = SCHEMA_NAME+"_customer_get";
	public static final String DB_ORGANIZATION_ACTIONS_PROC = SCHEMA_NAME+"_organization_actions_proc";
	public static final String DB_GROUP_ACTIONS_PROC = SCHEMA_NAME + "_group_actions_proc";
	public static final String DB_CUSTOMER_ACTIONS_PROC = SCHEMA_NAME + "_customer_actions_proc";
	public static final String DB_CUSTOMER_GROUP_ACTIONS_PROC = SCHEMA_NAME + "_customer_group_actions_proc";
	public static final String GET_CUSTOMER_ACCOUNT_ACTION_LIMITS = "GetCustomerAccountActionLimits";

	public static final String DB_ACCOUNTS_GET = SCHEMA_NAME+"_getaccountsview_get";
	public static final String DB_CUSTOMERACCOUNTS_GET = SCHEMA_NAME+"_customeraccounts_get";
	public static final String DB_CONTRACTACCOUNTS_GET = SCHEMA_NAME+"_contractaccounts_get";

	public static final String DB_BILLPAYTRANSFERS_UPDATE = SCHEMA_NAME+"_billpaytransfers_update";
	public static final String DB_OWNACCOUNTTRANSFERS_UPDATE = SCHEMA_NAME+"_ownaccounttransfers_update";
	public static final String DB_INTRABANKTRANSFERS_UPDATE = SCHEMA_NAME+"_intrabanktransfers_update";
	public static final String DB_INTERNATIONALFUNDTRANSFERS_UPDATE = SCHEMA_NAME+"_internationalfundtransfers_update";
	public static final String DB_INTERBANKFUNDTRANSFERS_UPDATE = SCHEMA_NAME+"_interbankfundtransfers_update";
	public static final String DB_WIRETRANSFERS_UPDATE = SCHEMA_NAME+"_wiretransfers_update";
	public static final String DB_P2PTRANSFERS_UPDATE = SCHEMA_NAME+"_p2ptransfers_update";

	public static final String DB_BILLPAYTRANSFERS_GET = SCHEMA_NAME+"_billpaytransfers_get";
	public static final String DB_OWNACCOUNTTRANSFERS_GET = SCHEMA_NAME+"_ownaccounttransfers_get";
	public static final String DB_INTRABANKTRANSFERS_GET = SCHEMA_NAME+"_intrabanktransfers_get";
	public static final String DB_INTERNATIONALFUNDTRANSFERS_GET = SCHEMA_NAME+"_internationalfundtransfers_get";
	public static final String DB_INTERBANKFUNDTRANSFERS_GET = SCHEMA_NAME+"_interbankfundtransfers_get";
	public static final String DB_WIRETRANSFERS_GET = SCHEMA_NAME+"_wiretransfers_get";
	public static final String DB_P2PTRANSFERS_GET = SCHEMA_NAME+"_p2ptransfers_get";

	public static final String DB_BILLPAYTRANSFERS_DELETE = SCHEMA_NAME+"_billpaytransfers_delete";
	public static final String DB_OWNACCOUNTTRANSFERS_DELETE = SCHEMA_NAME+"_ownaccounttransfers_delete";
	public static final String DB_INTRABANKTRANSFERS_DELETE = SCHEMA_NAME+"_intrabanktransfers_delete";
	public static final String DB_INTERNATIONALFUNDTRANSFERS_DELETE = SCHEMA_NAME+"_internationalfundtransfers_delete";
	public static final String DB_INTERBANKFUNDTRANSFERS_DELETE = SCHEMA_NAME+"_interbankfundtransfers_delete";
	public static final String DB_WIRETRANSFERS_DELETE = SCHEMA_NAME+"_wiretransfers_delete";
	public static final String DB_P2PTRANSFERS_DELETE = SCHEMA_NAME+"_p2ptransfers_delete";

	public static final String DB_BILLPAYTRANSFERS_CREATE = SCHEMA_NAME+"_billpaytransfers_create";
	public static final String DB_OWNACCOUNTTRANSFERS_CREATE = SCHEMA_NAME+"_ownaccounttransfers_create";
	public static final String DB_INTRABANKTRANSFERS_CREATE = SCHEMA_NAME+"_intrabanktransfers_create";
	public static final String DB_INTERNATIONALFUNDTRANSFERS_CREATE = SCHEMA_NAME+"_internationalfundtransfers_create";
	public static final String DB_INTERBANKFUNDTRANSFERS_CREATE = SCHEMA_NAME+"_interbankfundtransfers_create";
	public static final String DB_WIRETRANSFERS_CREATE = SCHEMA_NAME+"_wiretransfers_create";
	public static final String DB_P2PTRANSFERS_CREATE = SCHEMA_NAME+"_p2ptransfers_create";

	public static final String DB_ONETIMEPAYEE_CREATE = SCHEMA_NAME+"_onetimepayee_create";
	public static final String DB_ONETIMEPAYEE_GET = SCHEMA_NAME+"_onetimepayee_get";

	public static final String DB_BULKWIREFILEFORMATTYPE_CREATE = SCHEMA_NAME+"_bulkwirefileformattype_create";
    public static final String DB_BULKWIREFILEFORMATTYPE_DELETE = SCHEMA_NAME+"_bulkwirefileformattype_delete";
    public static final String DB_BULKWIREFILEFORMATTYPE_GET = SCHEMA_NAME+"_bulkwirefileformattype_get";
    public static final String DB_BULKWIREFILEFORMATTYPE_UPDATE = SCHEMA_NAME+"_bulkwirefileformattype_update";

    public static final String DB_BULKWIREFILELINEITEMS_CREATE = "createBWLineItems";
    public static final String DB_BULKWIREFILELINEITEMS_DELETE = SCHEMA_NAME+"_bulkwirefilelineitems_delete";
    public static final String DB_BULKWIREFILELINEITEMS_GET = SCHEMA_NAME+"_bulkwirefilelineitems_get";
    public static final String DB_BULKWIREFILELINEITEMS_UPDATE = SCHEMA_NAME+"_bulkwirefilelineitems_update";

    public static final String DB_BULKWIREFILETRANSACTDETAILS_CREATE = SCHEMA_NAME+"_bulkwirefiletransactdetails_create";
    public static final String DB_BULKWIREFILETRANSACTDETAILS_DELETE = SCHEMA_NAME+"_bulkwirefiletransactdetails_delete";
    public static final String DB_BULKWIREFILETRANSACTDETAILS_GET = SCHEMA_NAME+"_bulkwirefiletransactdetails._get";
    public static final String DB_BULKWIREFILETRANSACTDETAILS_UPDATE = SCHEMA_NAME+"_bulkwirefiletransactdetails_update";
    public static final String DB_BULKWIREFILETRANSACTDETAILS_FETCH = SCHEMA_NAME+"_fetch_bulkwire_files_transct_detail_proc";

    public static final String DB_BULKWIRESAMPLEFILE_CREATE = SCHEMA_NAME+"_bulkwiresamplefile_create";
    public static final String DB_BULKWIRESAMPLEFILE_DELETE = SCHEMA_NAME+"_bulkwiresamplefile_delete";
    public static final String DB_BULKWIRESAMPLEFILE_GET = SCHEMA_NAME+"_bulkwiresamplefile_get";
    public static final String DB_BULKWIRESAMPLEFILE_UPDATE = SCHEMA_NAME+"_bulkwiresamplefile_update";

    public static final String DB_BULKWIREFILELINEITEMS_PROC = SCHEMA_NAME+"_fetch_bulkwire_filelineitems_proc";

    public static final String DB_BULKWIREFILES_CREATE = SCHEMA_NAME+"_bulkwirefiles_create";
    public static final String DB_BULKWIREFILES_DELETE = SCHEMA_NAME+"_bulkwirefiles_delete";
    public static final String DB_BULKWIREFILES_GET = SCHEMA_NAME+"_bulkwirefiles_get";
    public static final String DB_BULKWIREFILES_UPDATE = SCHEMA_NAME+"_bulkwirefiles_update";
    public static final String DB_BULKWIREFILES_FETCH = SCHEMA_NAME+"_fetch_bulkwire_files_proc";

    public static final String DB_BULKWIRETRANSACTIONEXECDETAILS_FETCH = SCHEMA_NAME+"_fetch_bulkWireTransactionsExecution_details_proc";

    public static final String DB_ACH_TEMPLATE_CREATE = SCHEMA_NAME+"_bbtemplate_create";

	public static final String DB_ACH_TEMPLATE_RECORD_CREATE = SCHEMA_NAME+"_bbtemplaterecord_create";

	public static final String DB_ACH_TEMPLATE_SUBRECORD_CREATE = SCHEMA_NAME+"_bbtemplatesubrecord_create";

	public static final String DB_ACH_TEMPLATE_RECORD_SUBRECORD_CREATE = SCHEMA_NAME+"_ach_template_record_subrecord_create_proc";

	public static final String BILLPAYTRANSFER_BACKEND = TransactionBackendURL.getBackendURL("BillPayTransfer");
	public static final String P2PTRANSFER_BACKEND = TransactionBackendURL.getBackendURL("P2PTransfer");
	public static final String DOMESTIC_WIRE_TRANSFER_BACKEND = TransactionBackendURL.getBackendURL("DomesticWireTransfer");
	public static final String INTERNATIONAL_WIRE_TRANSFER_BACKEND = TransactionBackendURL.getBackendURL("InternationalWireTransfer");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_WITHOUT_AAPROVER = TransactionBackendURL.getBackendURL("IntraBankFundTransferWithoutApprover");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_WITH_AAPROVER = TransactionBackendURL.getBackendURL("IntraBankFundTransferWithApprover");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InternationalAccFundTransferWithoutApprover");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_WITH_APPROVER = TransactionBackendURL.getBackendURL("InternationalAccFundTransferWithApprover");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InterBankFundTransferWithoutApprover");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_WITH_APPROVER = TransactionBackendURL.getBackendURL("InterBankFundTransferWithApprover");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsWithoutApprover");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_WITH_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsWithApprover");

	public static final String BILLPAYTRANSFER_BACKEND_EDIT = TransactionBackendURL.getBackendURL("BillPayTransferEdit");
	public static final String P2PTRANSFER_BACKEND_EDIT = TransactionBackendURL.getBackendURL("P2PTransferEdit");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_EDIT_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InterBankFundTransferEditWithoutApprover");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_EDIT_WITH_APPROVER = TransactionBackendURL.getBackendURL("InterBankFundTransferEditWithApprover");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_EDIT_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("IntraBankFundTransferEditWithoutApprover");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_EDIT_WITH_APPROVER = TransactionBackendURL.getBackendURL("IntraBankFundTransferEditWithApprover");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_EDIT_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InternationalAccFundTransferEditWithoutApprover");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_EDIT_WITH_APPROVER = TransactionBackendURL.getBackendURL("InternationalAccFundTransferEditWithApprover");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_EDIT_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsEditWithoutApprover");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_EDIT_WITH_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsEditWithApprover");
	public static final String INTERNATIONAL_WIRE_TRANSFER_BACKEND_EDIT = "InternationalWireTransferEdit";
	public static final String DOMESTIC_WIRE_TRANSFER_BACKEND_EDIT = "DomesticWireTransferEdit";
	
	public static final String BILLPAYTRANSFER_BACKEND_DELETE = TransactionBackendURL.getBackendURL("BillPayTransferDelete");
	public static final String P2PTRANSFER_BACKEND_DELETE = TransactionBackendURL.getBackendURL("P2PTransferDelete");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_DELETE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InternationalFundTransferDeleteWithoutApprover");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_DELETE_WITH_APPROVER = TransactionBackendURL.getBackendURL("InternationalFundTransferDeleteWithApprover");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_DELETE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("IntraBankAccFundTransferDeleteWithoutApprover");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_DELETE_WITH_APPROVER = TransactionBackendURL.getBackendURL("IntraBankAccFundTransferDeleteWithApprover");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_DELETE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InterBankFundTransferDeleteWithoutApprover");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_DELETE_WITH_APPROVER = TransactionBackendURL.getBackendURL("InterBankFundTransferDeleteWithApprover");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_DELETE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsDeleteWithoutApprover");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_DELETE_WITH_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsDeleteWithApprover");
	public static final String INTERNATIONAL_WIRE_TRANSFER_BACKEND_DELETE = "InternationalWireTransferDelete";
	public static final String DOMESTIC_WIRE_TRANSFER_BACKEND_DELETE = "DomesticWireTransferDelete";
	
	public static final String BILLPAYTRANSFER_BACKEND_CANCEL_OCCURRENCE = TransactionBackendURL.getBackendURL("BillPayTransferCancelOccurrence");
	public static final String P2PTRANSFER_BACKEND_CANCEL_OCCURRENCE = TransactionBackendURL.getBackendURL("P2PTransferCancelOccurrence");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InternationalFundTransferCancelWithoutApprover");
	public static final String INTER_BANK_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITH_APPROVER = TransactionBackendURL.getBackendURL("InternationalFundTransferCancelWithApprover");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("InternationalFundTransferCancelWithoutApprover");
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITH_APPROVER = TransactionBackendURL.getBackendURL("InternationalFundTransferCancelWithApprover");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("IntraBankAccFundTransferCancelWithoutApproval");
	public static final String INTRA_BANK_FUND_TRANSFER_BACKEND_CANCEL_OCCURRENCE_WITH_APPROVER = TransactionBackendURL.getBackendURL("IntraBankAccFundTransferCancelWithApproval");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_CANCEL_OCCURRENCE_WITHOUT_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsCancelWithoutApprover");
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_CANCEL_OCCURRENCE_WITH_APPROVER = TransactionBackendURL.getBackendURL("TransferToOwnAccountsCancelWithApprover");

	public static final String RDC_CREATE = TransactionBackendURL.getBackendURL("CreateRDC");
	public static final String DB_SEARCH_EXTRACT_QUERY = TransactionBackendURL.getBackendURL("SearchExtractQuery");
	public static final String GET_ALL_ENTITY_ITEMS = "GetAllEntityItems";

	public static final String CREATE_BULK_BILL_PAY = "CreateBulkBillPay";
	public static final String CREATE_BULK_WIRE_INTERNAIONAL_TRANSFER = "CreateInternationalBulkWireTransfer";
	public static final String CREATE_BULK_WIRE_DOMESTIC_TRANSFER = "CreateDomesticBulkWireTransfer";

	public static final String DB_ACHFILE_CREATE = SCHEMA_NAME+"_achfile_create";
	public static final String DB_ACHFILE_GET = SCHEMA_NAME+"_achfile_get";
	public static final String DB_ACHFILE_UPDATE = SCHEMA_NAME+"_achfile_update";
	public static final String DB_ACHFILE_DELETE = SCHEMA_NAME+"_achfile_delete";
	
	public static final String DB_MANAGEAPPROVALMATRIX_UPDATE_PROC = SCHEMA_NAME+"_manageapprovalmatrix_update_proc";
	public static final String DB_MANAGEAPPROVALMATRIX_GET = SCHEMA_NAME+"_manageapprovalmatrix_get";
	public static final String DB_CONTRACTCORECUSTOMERS_GET = SCHEMA_NAME+"_contractcorecustomers_get";
	public static final String DB_CONTRACTCUSTOMERS_GET = SCHEMA_NAME+"_contractcustomers_get";

	public static final String DB_ACH_FILE_RECORD_SUBRECORD_CREATE = SCHEMA_NAME+"_ach_file_record_subrecord_create_proc";
	public static final String DB_ACHFILERECORD_GET = SCHEMA_NAME+"_achfilerecord_get";
	public static final String DB_ACHFILESUBRECORD_GET = SCHEMA_NAME+"_achfilesubrecord_get";

	public static final String ACH_FILE_FORMAT_TYPES_GET = SCHEMA_NAME+"_achfileformattype_get";
	public static final String ACH_FILE_VENDOR_SERVICE = TransactionBackendURL.getBackendURL("ACHVendorServiceForFileUpload");

	public static final String DB_APPROVALMATRIX_FETCH_RECORDS_PROC = SCHEMA_NAME+"_approvalmatrix_fetch_records_proc";
	public static final String DB_FETCH_APPROVALMATRIXTEMPLATE_PROC = SCHEMA_NAME+"_fetch_approvalmatrixtemplate_proc";
	public static final String DB_BBREQUEST_CREATE = SCHEMA_NAME +"_bbrequest_create";
	public static final String DB_BBREQUEST_UPDATE = SCHEMA_NAME +"_bbrequest_update";
	public static final String DB_BBREQUEST_DELETE = SCHEMA_NAME +"_bbrequest_delete";
	public static final String DB_REQUESTAPPROVALMATRIX_CREATE = SCHEMA_NAME + "_requestapprovalmatrix_create";
	public static final String DB_REQUESTAPPROVALMATRIX_DELETE = SCHEMA_NAME + "_requestapprovalmatrix_delete";
	public static final String DB_AUTO_REJECT_INVALID_PENDING_TRANSCATIONS_PROC = SCHEMA_NAME +"_auto_reject_invalid_pending_requests_proc";

	public static final String DB_CUSTOMERGROUP_GET = SCHEMA_NAME+"_customergroup_get";
	public static final String DB_MEMBERGROUP_GET = SCHEMA_NAME+"_membergroup_get";

	public static final String DB_BBTRANSACTIONTYPES_GET = SCHEMA_NAME + "_bbtransactiontype_get";
	public static final String APPROVAL_MATRIX_FETCH_RECORDS_PROC = SCHEMA_NAME + "_approvalmatrix_fetch_records_proc";
	public static final String DB_TEMPLATE_GET = SCHEMA_NAME+"_bbtemplate_get";
	public static final String DB_TEMPLATE_DELETE = SCHEMA_NAME+"_bbtemplate_delete";
	public static final String DB_TEMPLATE_RECORD_GET = SCHEMA_NAME + "_bbtemplaterecord_get";
	public static final String BB_CREATE_TRANSACTION = "createTransaction";
	public static final String DB_BBTEMPLATE_UPDATE = SCHEMA_NAME + "_bbtemplate_update";
	public static final String DB_TEMPLATE_SUB_RECORD_GET = SCHEMA_NAME + "_bbtemplatesubrecord_get";
	public static final String DB_BBTAXTYPE_GET = SCHEMA_NAME+"_bbtaxtype_get";
	public static final String DB_BBTAXSUBTYPE_GET = SCHEMA_NAME+"_bbtaxsubtype_get";

	public static final String DB_BBTEMPLATETYPE_GET = SCHEMA_NAME+"_bbtemplatetype_get";

	public static final String BB_TEMPLATEREQUESTTYPE_GET = SCHEMA_NAME+"_bbtemplaterequesttype_get";
    public static final String HOLIDAYS_GET =SCHEMA_NAME+"_holidays_get";

	public static final String DB_ACH_TEMPLATES_GET = SCHEMA_NAME+"_fetch_bbtemplate_proc";
	public static final String DB_FETCH_ACHFILES_PROC = SCHEMA_NAME+"_fetch_achfiles_proc";

	public static final String DB_ACH_TRANSACTION_CREATE = SCHEMA_NAME+"_achtransaction_create";
    public static final String DB_ACH_TRANSACTION_DELETE = SCHEMA_NAME+"_achtransaction_delete";
    public static final String DB_ACH_TRANSACTION_GET = SCHEMA_NAME+"_achtransaction_get";
    public static final String DB_ACH_TRANSACTION_UPDATE = SCHEMA_NAME+"_achtransaction_update";

	public static final String DB_ACH_TRANSACTION_RECORD_CREATE = SCHEMA_NAME+"_achtransactionrecord_create";
    public static final String DB_ACH_TRANSACTION_RECORD_DELETE = SCHEMA_NAME+"_achtransactionrecord_delete";
    public static final String DB_ACH_TRANSACTION_RECORD_GET = SCHEMA_NAME+"_achtransactionrecord_get";
    public static final String DB_ACHTRANSACTION_RECORD_UPDATE = SCHEMA_NAME+"_achtransactionrecord_update";

	public static final String DB_ACH_TRANSACTION_SUBRECORD_CREATE = SCHEMA_NAME+"_achtransactionsubrecord_create";
    public static final String DB_ACH_TRANSACTION_SUBRECORD_DELETE = SCHEMA_NAME+"_achtransactionsubrecord_delete";
    public static final String DB_ACH_TRANSACTION_SUBRECORD_GET = SCHEMA_NAME+"_achtransactionsubrecord_get";
    public static final String DB_ACH_TRANSACTION_SUBRECORD_UPDATE = SCHEMA_NAME+"_achtransactionsubrecord_update";
    public static final String DB_ACHTRANSACTIONS_FETCH_RECORDS_SUBRECORDS = SCHEMA_NAME+"_achtransactions_fetch_records_subrecords_proc";

    public static final String DB_ACH_TRANSACTION_RECORD_SUBRECORD_CREATE = SCHEMA_NAME+"_ach_transaction_record_subrecord_create_proc";

    public static final String DB_FETCH_ACH_TRANSACTION_PROC = SCHEMA_NAME+"_fetch_achtransaction_proc";
    public static final String CREATE_ACH_PAYMENT_WITH_VENDOR = TransactionBackendURL.getBackendURL("ACHPaymentVendorService");
    public static final String CREATE_ACH_COLLECTION_WITH_VENDOR = TransactionBackendURL.getBackendURL("ACHCollectionVendorService");
    
    public static final String FETCH_ACH_ORCH_COLLECTION_STATUS = TransactionBackendURL.getBackendURL("FetchACHOrchCollectionStatus");
    public static final String FETCH_ACH_ORCH_PAYMENT_STATUS = TransactionBackendURL.getBackendURL("FetchACHOrchPaymentStatus");
    public static final String FETCH_ACH_ORCH_FILE_STATUS = TransactionBackendURL.getBackendURL("FetchACHOrchFileStatus");
    
	public static final String DB_BBREQUEST_UPDATESTATUS_PROC = SCHEMA_NAME+"_bbrequest_updatestatus_proc";
	public static final String DB_BBREQUEST_UPDATECOUNTER_PROC = SCHEMA_NAME+"_bbrequest_updatecounter_proc";
	public static final String DB_BBACTEDREQUEST_CREATE = SCHEMA_NAME+"_bbactedrequest_create";

	public static final String DB_BBACTEDREQUEST_CREATE_PROC = SCHEMA_NAME+ "_log_bbactedrequest_proc";

	public static final String DB_ACHTRANSACTION_UPDATE = SCHEMA_NAME+"_achtransaction_update";


	public static final String DB_FETCH_REQUEST_HISTORY_PROC = SCHEMA_NAME+"_fetch_request_history_proc";
	public static final String DB_GET_ACH_TRANSACTION_RECORDS_PROC = SCHEMA_NAME+"_fetch_achtransactionrecords_proc";
	public static final String DB_GET_ACH_TRANSACTION_SUBRECORDS_PROC = SCHEMA_NAME + "_fetch_achtransactionsubrecords_proc";

	public static final String DB_FETCH_GENERAL_TRANSACTION_PROC = SCHEMA_NAME+"_fetch_generaltranscation_proc";

	 public static final String DB_COUNTRY_GET = SCHEMA_NAME+"_country_get";
     public static final String DB_CURRENY_GET = SCHEMA_NAME+"_currency_get";
	 public static final String DB_USERACCOUNTS_GET = SCHEMA_NAME+"_accounts_get";
	 public static final String DB_ACCOUNTSTYPE_GET = SCHEMA_NAME+"_accounttype_get";
	 public static final String DB_APPLICATION_GET = SCHEMA_NAME+"_application_get";
	 public static final String DB_FEATUREACTION_GET = SCHEMA_NAME+"_featureaction_get";
	 public static final String DB_FEATURE_GET = SCHEMA_NAME+"_feature_get";
	 public static final String DB_LIMITGROUP_GET = SCHEMA_NAME+"_limitgroup_get";

	 public static final String DB_AUTHORIZATIONCHECKFORREJECTANDWITHDRAWL_PROC = SCHEMA_NAME+"_authorizationCheckForRejectAndWithdrawl_proc";
	 public static final String DB_BBACTEDREQUEST_GET = SCHEMA_NAME+"_bbactedrequest_get";
	 public static final String DB_BBACTEDREQUEST_UPDATE = SCHEMA_NAME+"_update_bbactedrequest_proc";
	 public static final String CARDS_UPDATE =SCHEMA_NAME+"_card_update";
	 public static final String CARDSTRANSACTION_UPDATE =SCHEMA_NAME+"_cardtransaction_update";

	 public static final String DB_BULKWIRES_FETCH = SCHEMA_NAME+"_fetch_bulkwires_proc";
	 public static final String DB_BULKWIRESTEMPLATE_CREATE = SCHEMA_NAME+ "_bulkwiretemplate_create_proc";
	 public static final String DB_BULKWIRESTEMPLATE_UPDATE = SCHEMA_NAME+ "_bulkwiretemplate_update_proc";
	 public static final String DB_BULKWIRETEMPLATELINEITEMS_FETCH = SCHEMA_NAME+"_fetch_bulkwiretemplatelineitems_proc";
	 public static final String DB_BULKWIRETEMPLATE_GET = SCHEMA_NAME+"_bulkwiretemplate_get";
	 public static final String DB_BULKWIRESTEMPLATE_DELETE = SCHEMA_NAME+ "_bulkwiretemplate_delete_proc";

	 public static final String DB_GETREQUESTAPPROVERS_PROC = SCHEMA_NAME+"_getRequestApprovers_proc";
	 public static final String DB_REQUESTACTEDAPPROVERS_GET = SCHEMA_NAME+"_bbactedrequest_get";

	 public static final String DB_BULKWIRETEMPLATETRANSACTDETAILS_CREATE = SCHEMA_NAME+"_bulkwiretemplatetransactdetails_create";
	 public static final String DB_BULKWIRETEMPLATETRANSACTIONEXECDETAILS_FETCH = SCHEMA_NAME+"_fetch_bulkWireTemplateTransactionsExecution_details_proc";
	 public static final String DB_BULKWIRETEMPLATE_UPDATE = SCHEMA_NAME+"_bulkwiretemplate_update";
	 public static final String DB_BULKWIRETEMPLATETRANSACTDETAILS_FETCH = SCHEMA_NAME+"_fetch_bulkwire_template_transct_detail_proc";
	 public static final String DB_FETCH_UNSELECTEDPAYEES_PROC = SCHEMA_NAME+"_fetch_unselectedPayees_proc";
	 public static final String DB_FETCH_DOMESTICINTERNATIONALCOUNT_PROC = SCHEMA_NAME+"_fetch_bwtemplate_domesticInternationalCount_proc";
	 public static final String DB_FETCH_DOMESTICINTERNATIONALCOUNTFILE_PROC = SCHEMA_NAME+"_fetch_bwfile_domesticInternationalCount_proc";
	 public static final String DB_UPDATE_BWTEMPLATE_RECIPIENT_COUNT = SCHEMA_NAME+"_Update_BulkWireTemplateRecipient_Count";
	 
	 public static final String GET_TRANSACTIONS_AMOUNT = "getTransactionsAmount";
     public static final String CARDSTATEMENT_GET =SCHEMA_NAME+"_cardstatements_get";
     public static final String DB_CARDS_UPDATE =SCHEMA_NAME+"_card_update";
	 public static final String DB_CARDS_CREATE =SCHEMA_NAME+"_card_create";
	 public static final String DB_CARDS_GET = SCHEMA_NAME+"_card_get";
	 public static final String DB_CARDPRODDUCTSVIEW_GET = SCHEMA_NAME+"_cardproductsview_get";
	 public static final String CARDS_TRANSACTION_GET =SCHEMA_NAME+"_cardtransaction_get";
	 public static final String DB_FETCH_CARDTRANSACTION_PROC = SCHEMA_NAME+"_fetch_cardtransaction_proc";

	 /* payee RDBMS Service DBX Layer Mappings starts here */
	 public static final String DB_INTRABANKPAYEE_GET = SCHEMA_NAME + "_intrabankpayee_get";
	 public static final String DB_INTRABANKPAYEE_CREATE = SCHEMA_NAME + "_intrabankpayee_create";
	 public static final String DB_P2PPAYEE_GET = SCHEMA_NAME + "_p2ppayee_get";
	 public static final String DB_P2PPAYEE_CREATE = SCHEMA_NAME + "_p2ppayee_create";
	 public static final String DB_BILLPAYPAYEE_GET = SCHEMA_NAME + "_billpaypayee_get";
	 public static final String DB_WIRETRANSFERSPAYEE_GET = SCHEMA_NAME + "_wiretransferspayee_get";
	 public static final String DB_WIRETRANSFERSPAYEE_DELETE = SCHEMA_NAME + "_wiretransferspayee_delete";
	 public static final String GET_WIRE_TRANSFERS_PAYEES = "getWireTransfersPayees";
	 public static final String DB_WIRETRANSFERSPAYEE_CREATE = SCHEMA_NAME + "_wiretransferspayee_create";
	 public static final String DB_INTRABANKPAYEE_DELETE = SCHEMA_NAME + "_intrabankpayee_delete";
	 public static final String DB_BILLPAYPAYEE_CREATE = SCHEMA_NAME + "_billpaypayee_create";
	 public static final String DB_INTERBANKPAYEE_GET = SCHEMA_NAME + "_interbankpayee_get";
	 public static final String DB_INTERBANKPAYEE_CREATE = SCHEMA_NAME + "_interbankpayee_create";
	 public static final String DB_INTERBANKPAYEE_DELETE = SCHEMA_NAME + "_interbankpayee_delete";
	 public static final String DB_INTERBANKPAYEE_EDIT = SCHEMA_NAME + "_interbankpayee_update";
	 public static final String GET_EXTERNAL_PAYEES = "getExternalPayees";
	 public static final String DB_BILLPAYPAYEE_DELETE = SCHEMA_NAME + "_billpaypayee_delete";
	 public static final String DB_INTERNATIONALPAYEE_GET = SCHEMA_NAME + "_internationalpayee_get";
	 public static final String DB_INTERNATIONALPAYEE_CREATE = SCHEMA_NAME + "_internationalpayee_create";
	 public static final String DB_INTERNATIONALPAYEE_DELETE = SCHEMA_NAME + "_internationalpayee_delete";
	 public static final String DB_PAYEE_GET = SCHEMA_NAME + "_payee_get";
	 public static final String DB_EXTERNALACCOUNT_GET = SCHEMA_NAME + "_externalaccount_get";
	 public static final String DB_EXTERNALACCOUNT_UPDATE = SCHEMA_NAME + "_externalaccount_update";
	 public static final String DB_P2PPAYEE_UPDATE = SCHEMA_NAME + "_p2ppayee_update";
	 public static final String DB_P2PPAYEE_DELETE = SCHEMA_NAME + "_p2ppayee_delete";

	/* payee RDBMS Service DBX Layer Mappings ends here */

	 /* payee LOB Services Operations Mappings starts here */
	 public static final String BACKENDBILLPAYPAYEEGETORCH = TransactionBackendURL.getBackendURL("BackendBillPayPayeeGetOrch");
	 public static final String BACKENDINTERBANKPAYEEGETORCH = TransactionBackendURL.getBackendURL("BackendInterBankPayeeGetOrch");
	 public static final String BACKENDINTRABANKPAYEEGETORCH = TransactionBackendURL.getBackendURL("BackendIntraBankPayeeGetOrch");
	 public static final String BACKENDINTERNATIONALPAYEEGETORCH = TransactionBackendURL.getBackendURL("BackendInternationalPayeeGetOrch");
	 public static final String BACKENDP2PPAYEEGETORCH = TransactionBackendURL.getBackendURL("BackendP2pPayeeGetOrch");
	 public static final String BACKENDWIRETRANSFERSPAYEEGETORCH = TransactionBackendURL.getBackendURL("BackendWireTransfersPayeeGetOrch");
		
	 public static final String BILLPAYPAYEE_BACKEND_GET = TransactionBackendURL.getBackendURL("BillPayPayeeGet");
	 public static final String BILLPAYPAYEE_BACKEND_CREATE = TransactionBackendURL.getBackendURL("BillPayPayeeCreate");
	 public static final String BILLPAYPAYEE_BACKEND_DELETE = TransactionBackendURL.getBackendURL("BillPayPayeeDelete");
	 public static final String BILLPAYPAYEE_BACKEND_EDIT = TransactionBackendURL.getBackendURL("BillPayPayeeEdit");
	 public static final String INTER_BANK_PAYEE_BACKEND_GET = TransactionBackendURL.getBackendURL("InterBankPayeeGet");
	 public static final String INTER_BANK_PAYEE_BACKEND_CREATE = TransactionBackendURL.getBackendURL("InterBankPayeeCreate");
	 public static final String INTER_BANK_PAYEE_BACKEND_DELETE= TransactionBackendURL.getBackendURL("InterBankPayeeDelete");
	 public static final String INTER_BANK_PAYEE_BACKEND_EDIT = TransactionBackendURL.getBackendURL("InterBankPayeeEdit");
	 public static final String EXTERNALPAYEEPAYEE_BACKEND_GET = TransactionBackendURL.getBackendURL("ExternalPayeeGet");
	 public static final String WIRETRANSFERSPAYEE_BACKEND_CREATE = TransactionBackendURL.getBackendURL("WireTransfersPayeeCreate");
	 public static final String WIRETRANSFERSPAYEE_BACKEND_DELETE = TransactionBackendURL.getBackendURL("WireTransfersPayeeDelete");
	 public static final String WIRETRANSFERSPAYEE_BACKEND_EDIT = TransactionBackendURL.getBackendURL("WireTransfersPayeeEdit");
	 public static final String WIRETRANSFERS_BACKEND_GET = TransactionBackendURL.getBackendURL("WireTransfersPayeeGet");
	 public static final String INTRA_BANK_PAYEE_BACKEND_GET = TransactionBackendURL.getBackendURL("IntraBankPayeeGet");
	 public static final String INTRA_BANK_PAYEE_BACKEND_CREATE = TransactionBackendURL.getBackendURL("IntraBankPayeeCreate");
	 public static final String INTRA_BANK_PAYEE_BACKEND_EDIT = TransactionBackendURL.getBackendURL("IntraBankPayeeEdit");
	 public static final String INTRA_BANK_PAYEE_BACKEND_DELETE = TransactionBackendURL.getBackendURL("IntraBankPayeeDelete");
	 public static final String P2P_PAYEE_BACKEND_GET = TransactionBackendURL.getBackendURL("P2PPayeeGet");
	 public static final String P2P_PAYEE_BACKEND_CREATE = TransactionBackendURL.getBackendURL("P2PPayeeCreate");
	 public static final String P2P_PAYEE_BACKEND_DELETE = TransactionBackendURL.getBackendURL("P2PPayeeDelete");
	 public static final String P2P_PAYEE_BACKEND_EDIT = TransactionBackendURL.getBackendURL("P2PPayeeEdit");
	 public static final String INTERNATIONAL_PAYEE_BACKEND_GET = TransactionBackendURL.getBackendURL("InternationalPayeeGet");
	 public static final String INTERNATIONAL_PAYEE_BACKEND_CREATE = TransactionBackendURL.getBackendURL("InternationalPayeeCreate");
	 public static final String INTERNATIONAL_PAYEE_BACKEND_EDIT = TransactionBackendURL.getBackendURL("InternationalPayeeEdit");
	 public static final String INTERNATIONAL_PAYEE_BACKEND_DELETE = TransactionBackendURL.getBackendURL("InternationalPayeeDelete");
	/* payee LOB service Operations Mappings ends here */

	 public static final String GET_COMBINED_USER_PERMISSIONS = "GetCombinedUserPermissions";
	 public static final String GETINFINITYUSERCONTRACTCUSTOMERS = "GetInfinityUserContractCustomers";

	public static final String GETUSERNAMEBYCORECUSTID = "getUsernameByCoreCustomerId";
	 
	public static final String DB_CUSTOMVIEW_CREATE = SCHEMA_NAME + "_customview_create";
	public static final String DB_CUSTOMVIEW_GET = SCHEMA_NAME + "_customview_get";
	public static final String DB_CUSTOMVIEW_EDIT = SCHEMA_NAME + "_customview_update";
	public static final String DB_CUSTOMVIEW_DELETE = SCHEMA_NAME + "_customview_delete";
	
	public static final String DB_BULKPAYMENTSUBRECORD_CREATE = SCHEMA_NAME + "_bulkpaymentsubrecord_create";
	public static final String DB_BULKPAYMENTSUBRECORD_GET = SCHEMA_NAME + "_bulkpaymentsubrecord_get";
	public static final String DB_BULKPAYMENTSUBRECORD_DELETE = SCHEMA_NAME + "_bulkpaymentsubrecord_delete";
	public static final String DB_BULKPAYMENTSUBRECORD_UPDATE = SCHEMA_NAME + "_bulkpaymentsubrecord_update";
	
	public static final String DB_BULKPAYMENTRECORD_GET = SCHEMA_NAME + "_bulkpaymentrecord_get";
    public static final String DB_BULKPAYMENTRECORD_CREATE = SCHEMA_NAME + "_bulkpaymentrecord_create";
    public static final String DB_BULKPAYMENTRECORD_UPDATE = SCHEMA_NAME + "_bulkpaymentrecord_update";
    public static final String DB_BULKPAYMENTRECORD_DELETE = SCHEMA_NAME + "_bulkpaymentrecord_delete";
	
	public static final String DB_BULKPAYMENT_FILE_CREATE = SCHEMA_NAME + "_bulkpaymentfiles_create";
	
	public static final String GETSAMPLEFILES = "getSampleFiles";
	public static final String UPLOADFILE = "uploadFile";

	public static final String GETONGOINGPAYMENTS = "fetchOnGoingPayments";
	public static final String FETCHHISTORY = "fetchHistory";
	public static final String FETCHUPLOADEDFILES = "fetchUploadedFiles";
	public static final String FETCH_BULKPAYMENT_RECORDDETAILS_BY_ID = "fetchBulkPaymentRecordDetailsById";
	public static final String INITIATE_BULKPAYMENT = "initiatePayment";
	public static final String FETCHBULKPAYMENTPO = "fetchPaymentOrders";
	public static final String CREATE_TRANSFER = "createTransfer";
	
	public static final String CANCELBULKPAYMENTRECORD = "cancelBulkPaymentRecord";

	public static final String UPDATEBULKPAYMENTRECORD = "updateBulkPaymentRecord";
	public static final String UPDATEBULKPAYMENTRECORDSTATUS = "updateBulkPaymentRecordStatus";
	public static final String REJECTBULKPAYMENTRECORD = "rejectBulkPaymentRecord";
	public static final String APPROVEBULKPAYMENTORDER = "approveBulkPaymentOrder";

	public static final String DB_FETCH_APPROVALQUEUE_PROC = SCHEMA_NAME + "_fetch_approvalqueue_proc" ;
	public static final String DB_FETCH_PENDING_APPROVALSQUEUE_PROC = SCHEMA_NAME + "_fetch_pending_approvalsqueue_proc" ;
	public static final String ADDBULKPAYMENTPO = "addPaymentOrder";
	public static final String EDITBULKPAYMENTPO = "editPaymentOrder";
	public static final String DELETEBULKPAYMENTPO = "deletePaymentOrder";

	public static final String FETCHRECORDS = "fetchRecords";
	public static final String APPROVEPAYMENTORDERS = "approvePaymentOrders";

	public static final String DB_BULKPAYMENTFILE_GET = SCHEMA_NAME + "_bulkpaymentfiles_get";

	public static final String DB_FETCH_APPROVERS_PROC = SCHEMA_NAME + "_fetch_approvers_proc" ;
	public static final String DB_FETCH_PENDING_GROUP_LIST_PROC = SCHEMA_NAME + "_fetch_pending_group_list_proc" ;
	
	//forex operations
	public static final String FOREXOROCGET = SCHEMA_NAME + "_forex_proc_get";
	public static final String FETCH_BASE_CURRENCY = "fetchBaseCurrency";
	public static final String FETCH_CURRENCY_LIST = "fetchCurrencyList";
	public static final String FETCH_FOREX_RATES = "fetchForexRates";
	public static final String FETCH_DASHBOARD_FOREX_RATES = "fetchDashboardForexRates";
	public static final String UPDATE_USER_RECENT_CURRENCY_PROC = "dbxdb_update_user_recent_currency";

	public static final String DB_BULKPAYMENTTEMPLATE_CREATE = SCHEMA_NAME + "_bulkpaymenttemplate_create";
	public static final String BULKPAYMENT_TEMPLATE_PO_CREATE_PROC = SCHEMA_NAME +"_bulkpayment_template_po_create_proc";
	public static final String DB_BULKPAYMENTTEMPLATE_DELETE = SCHEMA_NAME + "_bulkpaymenttemplate_delete";
	public static final String DB_BULKPAYMENTTEMPLATE_GET = SCHEMA_NAME + "_bulkpaymenttemplate_get";

	public static final String CANCELLATION_REASONS_GET_OPERATION =  "fetchCancellationReasons";


	public static final String DB_BULKPAYMENT_REQUEST_GET = SCHEMA_NAME + "_bulkpaymentrequest_get";
	public static final String DB_BULKPAYMENTREQUESTPOS_GET = SCHEMA_NAME + "_bulkpaymentrequestpos_get";

	public static final String DB_BULKPAYMENTTEMPLATEPOS_GET = SCHEMA_NAME + "_bulkpaymenttemplatepos_get";
	public static final String BULKPAYMENT_REQUEST_PO_CREATE_PROC = SCHEMA_NAME +"_bulkpayment_request_po_create_proc";
	public static final String DB_BULKPAYMENTREQUEST_CREATE = SCHEMA_NAME + "_bulkpaymentrequest_create";
	public static final String DB_BULKPAYMENTREQUEST_DELETE = SCHEMA_NAME +"_bulkpaymentrequest_delete";
	public static final String UPLOADBULKPAYMENTFILE = "uploadBulkPaymentFile";

	public static final String APPROVE_ACH_FILE_REQUEST = "ApproveACHFileRequest" ; 
	public static final String APPROVE_ACH_REQUEST = "approveACHRequest";
	public static final String APPROVE_GENERAL_TRANSACTION = "ApproveGeneralTransaction";
	public static final String APPROVE_BULK_PAYMENT_RECORD = "approveBulkPaymentRecord";
	/**
	* End: Added as part of ADP-2810
	*/
	
	public static final String DB_CONTRACT_GET = SCHEMA_NAME + "_contract_get";
	public static final String DB_SERVICEDEFINITION_ACTIONLIMITS_UPDATE = SCHEMA_NAME + "_servicedefinition_action_limits_update_proc";
	public static final String DB_SERVICEDEFINITION_REMOVE_ACTIONS = SCHEMA_NAME + "_servicedefinition_remove_actions_proc";
	public static final String DB_CUSTOMERROLE_ACTIONLIMITS_UPDATE = SCHEMA_NAME + "_group_limits_update_proc";
	public static final String DB_CUSTOMERROLE_REMOVE_ACTIONS = SCHEMA_NAME + "_group_actions_remove_proc";
	
	
	public static final String DB_CONTRACTACTIONLIMIT_GET = SCHEMA_NAME + "_contractactionlimit_get";
	public static final String DB_ACTIONLIMIT_GET = SCHEMA_NAME + "_actionlimit_get";
	
	public static final String DB_CUSTOMERLIMITGROUPLIMITS_GET = SCHEMA_NAME + "_customerlimitgrouplimits_get";
	public static final String DB_LIMITGROUPDISPLAYNAMEDESCRIPTION_GET = SCHEMA_NAME + "_limitgroupdisplaynamedescription_get";
	
	public static final String GETBANKDATE = "getBankDate";
	
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_GET = TransactionBackendURL.getBackendURL("TransferToOwnAccountsGet");//"TransferToOwnAccountsGet";
	public static final String TRANSFER_TO_OWN_ACCOUNTS_BACKEND_UPDATE_STATUS = "TransferToOwnAccountsUpdateStatus";
	
	public static final String BILLPAY_TRANSFER_BACKEND_GET = "BillPayTransferGet";
	public static final String BILLPAY_TRANSFER_BACKEND_UPDATE_STATUS = "BillPayTransferUpdateStatus";
	
	public static final String DOMESTIC_WIRE_TRANSFER_BACKEND_GET = "DomesticWireTransferGet";
	public static final String DOMESTIC_WIRE_TRANSFER_BACKEND_UPDATE_STATUS = "DomesticWireTransferUpdateStatus";
	
	public static final String INTERNATIONAL_WIRE_TRANSFER_BACKEND_GET = "InternationalWireTransferGet";
	public static final String INTERNATIONAL_WIRE_TRANSFER_BACKEND_UPDATE_STATUS = "InternationalWireTransferUpdateStatus";

	public static final String P2PTRANSFER_BACKEND_GET = "P2PTransferGet";
	public static final String P2PTRANSFER_BACKEND_UPDATE_STATUS = "P2PTransferUpdateStatus";
	
	public static final String APPROVE = "Approve";

	public static final String INTRABANK_FUND_TRANSFER_BACKEND_GET = TransactionBackendURL.getBackendURL("IntraBankAccFundTransferGet");//"IntraBankFundTransferGet";
	public static final String INTRABANK_FUND_TRANSFER_BACKEND_UPDATE_STATUS = "IntraBankFundTransferUpdateStatus";
	
	public static final String INTERBANK_FUND_TRANSFER_BACKEND_GET = TransactionBackendURL.getBackendURL("InterBankAccFundTransferGet");//"InterBankFundTransferGet";
	public static final String INTERBANK_FUND_TRANSFER_BACKEND_UPDATE_STATUS = "InterBankFundTransferUpdateStatus";
		
	public static final String VALIDATE_FOR_APPROVALS = "ValidateForApprovals";
	public static final String UPDATE_BACKENDID_IN_APPROVALQUEUE = "UpdateBackendIdInApprovalQueue";
	
	public static final String REJECT_CHEQUEBOOK_RECORD = "rejectChequeBookRequest";

	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_GET = TransactionBackendURL.getBackendURL("InternationalFundTransferGet");//"InternationalAccFundTransferGet";
	public static final String INTERNATIONAL_ACC_FUND_TRANSFER_BACKEND_UPDATE_STATUS = "InternationalAccFundTransferUpdateStatus";
	
	public static final String CREATE_INTERNATIONAL_FUND_TRANSACTION = "CreateInternationalFundTransaction";
	public static final String CREATE_INTER_BANK_FUND_TRANSACTION = "CreateInterBankFundTransaction";
	public static final String CREATE_INTRA_BANK_FUND_TRANSACTION = "CreateIntraBankFundTranscation";
	public static final String APPROVE_CHEQUEBOOK_REQUEST = "ApproveChequeBookRequest";
	public static final String WITHDRAW_CHEQUE = "WithdrawCheque";
	public static final String GET_CHEQUE_DETAILS = "fetchChequeDetails";
	public static final String RETRIEVE_ATTACHMENTS = "retrieveAttachments";

	public static final String APPROVE_LETTEROFCREDIT = "approveLetterOfCredit";
	public static final String WITHDRAW_LETTEROFCREDIT = "withdrawLetterOfCredit";
    public static final String REJECT_LETTEROFCREDIT = "rejectLetterOfCredit";
    public static final String FETCH_LETTEROFCREDIT="fetchLetterOfCreditDetails";
    
	public static final String DB_SIGNATORYGROUP_CREATE = SCHEMA_NAME + "_signatorygroup_create";
	public static final String DB_SIGNATORYGROUP_GET = SCHEMA_NAME + "_signatorygroup_get";
	public static final String DB_SIGNATORYGROUP_DELETE = SCHEMA_NAME + "_signatorygroup_delete";
	public static final String DB_SIGNATORYGROUP_UPDATE = SCHEMA_NAME + "_signatorygroup_update";
	public static final String DB_CUSTOMERSIGNATORYGROUP_CREATE = SCHEMA_NAME + "_customersignatorygroup_create";
	public static final String DB_CUSTOMERSIGNATORYGROUP_GET = SCHEMA_NAME + "_customersignatorygroup_get";
	public static final String DB_CUSTOMERSIGNATORYGROUP_DELETE = SCHEMA_NAME + "_customersignatorygroup_delete";
	public static final String DB_CUSTOMERSIGNATORYGROUP_UPDATE = SCHEMA_NAME + "_customersignatorygroup_update";
	
	public static final String DB_SIGNATORYGROUPMATRIX_GET = SCHEMA_NAME + "_signatorygroupmatrix_get";
	public static final String DB_SIGNATORYGROUPMATRIXTEMPLATE_GET = SCHEMA_NAME + "_signatorygroupmatrixtemplate_get";

	public static final String DB_SIGNATORYGROUPREQUESTMATRIX_CREATE = SCHEMA_NAME + "_signatorygrouprequestmatrix_create";
	public static final String DB_SIGNATORYGROUPREQUESTMATRIX_DELETE = SCHEMA_NAME + "_signatorygrouprequestmatrix_delete";
	public static final String DB_GET_SIGNATORYGROUP_APPROVERS_PROC = SCHEMA_NAME+"_get_signatorygroup_approvers_proc";
	
	public static final String DB_FETCH_CUSTOMERSIGNATORYGROUP_DETAILS_PROC = SCHEMA_NAME+"_fetch_customersignatorygroup_details_proc";

	public static final String DB_FETCH_SIGNATORYGROUP_FOR_CUSTOMER_AND_REQUEST_PROC = SCHEMA_NAME + "_fetch_signatorygroup_for_customer_and_request_proc";
	public static final String DB_SIGNATORYGROUPREQUESTMATRIX_UPDATE = SCHEMA_NAME + "_signatorygrouprequestmatrix_update";
	public static final String DB_SIGNATORYGROUPREQUESTMATRIX_GET = SCHEMA_NAME + "_signatorygrouprequestmatrix_get";
	public static final String DB_INCREMENT_RECEIVEDAPPROVALS_PROC = SCHEMA_NAME + "_increment_receivedapprovals_proc";
	
	public static final String DB_APPROVALMODE_GET = SCHEMA_NAME + "_approvalmode_get";

	public static final String DB_SIGNATORY_APPROVALMATRIX_CREATE = SCHEMA_NAME+"_approvalmatrix_signatorygroupmatrixcreate_proc";
	public static final String DB_SIGNATORYGROUP_CREATE_PROC = SCHEMA_NAME+"_signatorygroup_create_proc";
	public static final String DB_SIGNATORYGROUP_UPDATE_PROC = SCHEMA_NAME+"_signatorygroup_update_proc";
	public static final String DB_FETCH_NEW_SIGNATORY_GROUP_USER_DETAILS = SCHEMA_NAME+"_fetch_new_signatory_group_user_details";
	public static final String DB_APPROVALMATRIX_FETCH_SIGNATORYRECORDS_PROC = SCHEMA_NAME+"_approvalmatrix_fetch_grouprecords_proc";
	public static final String DB_FETCH_SIGNATORYGROUPS_PROC = SCHEMA_NAME+"_fetch_signatorygroups_proc";
	public static final String DB_FETCH_NO_GROUP_USERS_PROC = SCHEMA_NAME+"_fetch_nogroup_user_details_proc";
	public static final String DB_FETCH_SIGNATORYGROUP_DETAILS_PROC = SCHEMA_NAME+"_fetch_signatorygroup_details_proc";
	public static final String DB_FETCH_APPROVAL_GROUPS_FOR_PENDINGTXN_PROC = SCHEMA_NAME+"_fetch_approvalgroups_for_pendingtxn_proc";
	public static final String DB_FETCH_SIGNATORYGROUPS_IN_APPROVALRULE_PROC = SCHEMA_NAME+"_fetch_signatorygroups_in_approvalrule_proc";
	public static final String DB_UPDATE_SIGNATORYGROUP_FORUSER_PROC = SCHEMA_NAME+"_update_signatorygroup_for_user_proc";

	public static final String DB_FETCH_REQUESTAPPROVALMATRIX_DETAILS_PROC = SCHEMA_NAME+"_fetch_requestapprovalmatrix_details_proc";
	public static final String DB_UPDATE_REQUESTAPPROVALMATRIX_PROC = SCHEMA_NAME+"_update_requestapprovalmatrix_proc";

	public static final String DB_UPDATE_TRANSACTION_STATUS_PROC = SCHEMA_NAME+"_update_transaction_status_proc";

	public static final String DB_NICKNAMES_GET = SCHEMA_NAME+"_getAccountNicknamesByAccountIds";

	public static final String DB_FETCH_SIGNATORYGROUP_CUSTOMER_DETAILS_PROC = SCHEMA_NAME+"_fetch_signatorygroup_customer_details_proc";
	public static final String DB_APPROVALMATRIXTEMPLATE_DEFAULT_CREATE_PROC = SCHEMA_NAME+"_approvalmatrixtemplate_default_create_proc";
	public static final String DB_APPROVALMATRIXTEMPLATE_CLEANUP_PROC = SCHEMA_NAME+"_approvalmatrixtemplate_cleanup_proc";
	public static final String DB_CREATE_APPROVALMATRIXTEMPLATE_PROC = SCHEMA_NAME+"_approvalmatrixtemplate_create_proc";
	public static final String DB_GET_APPROVALMATRIXTEMPLATE = SCHEMA_NAME+"_approvalmatrixtemplate_get";
	public static final String DB_BBREQUEST_UPDATEADDITIONALMETA_PROC = SCHEMA_NAME + "_bbrequest_updateadditionalmeta_proc";
	public static final String DB_GET_CUSTOMERACTIONS = SCHEMA_NAME+"_customeraction_get";
	public static final String DB_GET_CONTRACTACCOUNTS = SCHEMA_NAME+"_contractaccounts_get";
	public static final String DB_CREATE_APPROVALMODE = SCHEMA_NAME+"_approvalmode_create";
	public static final String DB_FETCH_ACCOUNTDETAILS_PROC = SCHEMA_NAME+"_fetch_accountdetails_proc";
	
	public static final String APPROVE_TRANSACTION = TransactionBackendURL.getBackendURL("ApproveTransaction");
	public static final String REJECT_TRANSACTION = TransactionBackendURL.getBackendURL("RejectTransaction");
	public static final String WITHDRAW_TRANSACTION = TransactionBackendURL.getBackendURL("WithdrawTransaction");
	
	public static final String VALIDATE_TRANSACTION = TransactionBackendURL.getBackendURL("ValidateTransaction");
	
	public static final String GET_BANK_DETAILS_BY_SWIFTCODE = TransactionBackendURL.getBackendURL("GetBankDetailsBySwiftCode");//"getBankDetailsBySwiftCode";

	// SRMS Backend Integration services
	public static final String SRMS_CREATEPAYMENT_WITHOUTAPPROVER = TransactionBackendURL.getBackendURL("CreatePaymentWithoutApprover");
	public static final String GET_PAYMENTORDER_TRANSACTIONDETAILS = TransactionBackendURL.getBackendURL("GetPaymentOrderTransactionDetails");
	public static final String CREATE_STANDINGORDER_WITHOUTAPPROVER = TransactionBackendURL.getBackendURL("CreateStandingOrderWithoutApprover");
	public static final String GET_STANDINGORDER_TRANSACTIONDETAILS = TransactionBackendURL.getBackendURL("GetStandingOrderTransactionDetails");
	public static final String CANCEL_DIRECT_DEBIT = TransactionBackendURL.getBackendURL("CancelDirectDebit");
	public static final String STOPNEXTPAYMENT_DIRECT_DEBIT = TransactionBackendURL.getBackendURL("StopNextPayment");

	public static final String GET_EXTERNAL_PAYEES_PROC = SCHEMA_NAME + "_GetExternalPayeesProc";

	public static final String DB_CONFIGURATIONS_GET = SCHEMA_NAME + "_configurations_get";
	public static final String DB_FETCH_ACCOUNTLEVELCUSTOMERLIMITS_FOR_FEATUREACTION_PROC = SCHEMA_NAME + "_fetch_accountlevelcustomerlimits_for_featureaction_proc";
	public static final String DB_CREATE_APPROVALMATRIXRULE_FOR_FEATUREACTION_AND_LIMITTYPE_PROC = SCHEMA_NAME + "_create_approvalmatrixrule_for_featureaction_and_limittype_proc";
	public static final String DB_FETCH_APPROVALMATRIX_ACTIVERULES_FOR_CONTRACTCIFMAP = SCHEMA_NAME + "_fetch_approvalmatrix_activerules_for_contractcifmap";
	public static final String DB_CREATE_NEW_COMPOSITE_REQUEST_IN_APPROVALQUEUE_PROC = SCHEMA_NAME + "_create_new_composite_request_in_approvalqueue_proc";
	public static final String DB_APPROVALMATRIX_DEFAULT_INIT_PROC = SCHEMA_NAME + "_approvalmatrix_default_init_proc";
	public static final String DB_FETCH_COMPOSITE_APPROVALMODE_PROC = SCHEMA_NAME + "_fetch_composite_approvalmode_proc";
	public static final String DB_FETCH_COMPOSITE_APPROVALMATRIXSTATUS_PROC = SCHEMA_NAME + "_fetch_composite_approvalmatrixstatus_proc";
	public static final String DB_UPDATE_APPROVALMODE_FOR_CONTRACTANDCIF_PROC = SCHEMA_NAME + "_update_approvalmode_for_contractandcif_proc";
	public static final String DB_UPDATE_COMPOSITE_APPROVALMATRIXSTATUS_PROC = SCHEMA_NAME + "_update_composite_approvalmatrixstatus_proc";
	public static final String DB_FETCH_REQUESTS_WITH_APPROVALMATRIXINFO_PROC = SCHEMA_NAME + "_fetch_requests_with_approvalmatrixinfo_proc";
	public static final String DB_APPROVE_PENDINGREQUESTS_IN_APPROVALQUEUE_PROC = SCHEMA_NAME + "_approve_pendingrequests_in_approvalqueue_proc";
	public static final String DB_REJECT_PENDINGREQUESTS_IN_APPROVALQUEUE_PROC = SCHEMA_NAME + "_reject_pendingrequests_in_approvalqueue_proc";
    public static final String DB_WITHDRAW_PENDINGREQUESTS_IN_APPROVALQUEUE_PROC = SCHEMA_NAME + "_withdraw_pendingrequests_in_approvalqueue_proc";
    public static final String DB_FETCH_PENDINGAPPROVERS_FOR_REQUEST_PROC = SCHEMA_NAME + "_fetch_pendingapprovers_for_request_proc";
	public static final String DB_FETCH_REQUEST_HISTORYINFO_PROC = SCHEMA_NAME + "_fetch_request_historyinfo_proc";
	public static final String DB_FETCH_ALL_PENDINGREQUESTS_PROC = SCHEMA_NAME + "_fetch_all_pendingrequests_proc";
	public static final String DB_FETCH_ALL_PENDINGAPPROVALS_PROC = SCHEMA_NAME + "_fetch_all_pendingapprovals_proc";
	public static final String DB_FETCH_ALL_REQUESTHISTORY_PROC = SCHEMA_NAME + "_fetch_all_requesthistory_proc";
	public static final String DB_FETCH_ALL_APPROVALHISTORY_PROC = SCHEMA_NAME + "_fetch_all_approvalhistory_proc";
	public static final String DB_FETCH_ALL_APPROVALREQUESTS_COUNTS_PROC = SCHEMA_NAME + "_fetch_all_approvalrequests_counts_proc";

}
	 