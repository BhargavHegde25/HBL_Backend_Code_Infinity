package com.infinity.dbx.temenos.transfers;

public interface TransferConstants {

    String T24_SERVICE_NAME_TRANSFERS = "T24ISTransfers";
    String T24_SERVICE_NAME_TRANSACTION = "T24ISPaymentsView";
    String STANDING_ORDER_OPERATION_NAME = "standingOrders";
    String ONE_TIME_TRANSFER_OPERATION_NAME = "oneTimeTransfer";
    String PAYMENTORDER_DETAILS_OPERATION_NAME = "getPaymentOrderTransactionDetails";
    String STANDINGORDER_DETAILS_OPERATION_NAME = "getStandingOrderTransactionDetails";
    
    
    String OP_GET_TRANSFER_STATUS = "GetTransactionStatus";

    String DELETE_PAYMENTORDER_OPERATION_NAME = "deletePaymentOrder";
    String DELETE_STATNDINGORDER_OPERATION_NAME = "deleteStandingOrder";

    String T24_SERVICE_NAME_ONBOARDING_TRANSFERS = "OnboardingTransfer";
    String ONBOARDING_TRANSFER_OPERATION = "createOneTimeTransfer";

    // current frequency values
   /* String DAILY_FREQUENCY_VALUE = "BSNSS";
    String LAST_DAY_OF_MONTH_FREQUENCY_VALUE = "LMNTH";
    String WEEKLY_FREQUENCY_VALUE = "WEEK1";
    String BIWEEKLY_FREQUENCY_VALUE = "WEEK2";
    String MONTHLY_FREQUENCY_VALUE = "M01";
    String QUARTERLY_FREQUENCY_VALUE = "M03";
    String SEMI_ANNUAL_FREQUENCY_VALUE = "M06";
    String ANNUAL_FREQUENCY_VALUE = "M12";
    String FREQUENCY_BI_WEEKLY = "BiWeekly";
    */
    String DAILY_FREQUENCY_VALUE = " e0Y e0M e0W e1D e0F";
    String LAST_DAY_OF_MONTH_FREQUENCY_VALUE = "LMNTH";
    String WEEKLY_FREQUENCY_VALUE = " e0Y e0M e1W e0D e0F";
    String BIWEEKLY_FREQUENCY_VALUE = " e0Y e0M e2W e0D e0F";
    String MONTHLY_FREQUENCY_VALUE = " e0Y e1M e0W e0D e0F";
    String QUARTERLY_FREQUENCY_VALUE = " e0Y e4M e0W e0D e0F";
    String SEMI_ANNUAL_FREQUENCY_VALUE = " e0Y e6M e0W e0D e0F";
    String ANNUAL_FREQUENCY_VALUE = " e1Y e0M e0W e0D e0F";
    String FREQUENCY_BI_WEEKLY = "BiWeekly";

    // T24 frequencies
    String BI_WEEKLY_FREQUENCY = "Fortnightly";
    String HALF_YEARLY_FREQUENCY = "Semiannual";
    String YEARLY_FREQUENCY = "Annually";

    String SESSION_ATRIB_STANDING_ORDERS = "StandingOrders";
    String PARAM_CURRENT_FREQUENCY = "currentFrequency";
    String TRANSCTION_TYPE_INTERNAL_TRANSFER = "InternalTransfer";
    String TRANSCTION_TYPE_EXTERNAL_TRANSFER = "ExternalTransfer";
    String SERVICE_NAME = "serviceName";
    String PAYMENTORDERPRODUCTNAME = "paymentOrderProductName";
    String SUPRESSFT = "suppressFT";
    String SUPRESSFT_VALUE = "PAYMENT";
    String PAYMENTMETHOD = "paymentMethod";
    String PARAM_CURRENCY_ID = "currencyId";
    String PARAM_VERSION_NUMBER = "versionNumber";
    String PARAM_EXECUTION_DATE = "executionDate";
    String PARAM_REFERENCE_ID = "referenceId";
    String PARAM_BENEFICIARY_NAME = "beneficiaryName";
    String PARAM_SWIFT_CODE = "swiftCode";
    String PARAM_DESCRIPTION = "description";
    String PARAM_STATUS = "status";
    String STATUS_FAILED = "failed";
    String STATUS_SUCCESS = "success";
    String BODY = "body";
    String PARAM_TRANSACTION_ID = "transactionId";
    String PARAM_CURRENT_STATUS = "currentStatus";
    String STATUS_COMPLETE = "Complete";
    String IS_COMPLETED = "isCompleted";
    String PARAM_PAYMENT_CONTAINER = "paymentContainer";

    // paymentOrder product details
    String INTERNAL_TRANSFER_PRODUCT_ID = "ACTRF";
    String DOMESTIC_TRANSFER_PRODUCT_ID = "DOMESTIC";
    String INTERNATIONAL_TRANSFER_PRODUCT_ID = "INATIONAL";
    String INTRA_TRANSFER_PRODUCT_ID = "INSTPAY";
    String SEPA_TRANSFER_PRODUCT_ID = "SEPA";
    String FEDWIRE_TRANSFER_PRODUCT_ID = "FEDWIRE";
    String ACH_TRANSFER_PRODUCT_ID = "ACH";
    String CHAPS_TRANSFER_PRODUCT_ID = "CHAPS";
    String FASTER_TRANSFER_PRODUCT_ID = "FASTER PAYMENT";
    String RINGS_TRANSFER_PRODUCT_ID = "RINGS";
    String BISERA_TRANSFER_PRODUCT_ID = "BISERA";
    String ACOTHER_TRANSFER_PRODUCT_ID = "ACOTHER";

    // PAYMENT METHOD VALUES
    String INTERNAL_TRANSFER_PAYMENT_METHOD = "ACIB";
    String DOMESTIC_TRANSFER_PAYMENT_METHOD = "IT";
    String PARAM_OTHER_BANK_TRANSFER = "otherBankTransfer";
    String INTERNATIONAL_TRANSFER_PAYMENT_METHOD = "OTIB";

    
    // service names 2020.07
    String INTERNAL_TRANSFER_SERVICE_ID = "TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE";
    String DOMESTIC_TRANSFER_SERVICE_ID = "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE";
    String INTERNATIONAL_TRANSFER_SERVICE_ID = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE";
    String INTRA_BANK_TRANSFER_SERVICE_ID = "INTRA_BANK_FUND_TRANSFER_CREATE";
    String DOCUMENT_INTEGRATION_OBJECT_ID = "DocumentStorageObjects" ;
    String DOCUMENT_INTEGRATION_SERVICE_ID = "DocumentStorage";
    String DOCUMENT_INTEGRATION_UPLOAD_OPER_ID = "uploadDocument";
    String RBLOCALSERVICESDB_SERVICE_ID = "dbpRbLocalServicesdb";
    String PAYMENTFILESCREATE_OPER_ID = "dbxdb_paymentfiles_create";
    String PARAM_UPLOADED_ATTACHMENTS = "uploadedattachments";

    int UNIQUE_ID_LENGTH = 32;
	int FILENAME_INDEX = 0;
	int FILETYPE_INDEX = 1;
	int FILECONTENTS_INDEX = 2;
	  
    //service names 4.2.6
    String INTERNAL_TRANSFER_SERVICE_ID_PREV = "SERVICE_ID_2";
	String DOMESTIC_TRANSFER_SERVICE_ID_PREV = "SERVICE_ID_3";
	String INTERNATIONAL_TRANSFER_SERVICE_ID_PREV = "SERVICE_ID_4";
    
    String PARAM_NUMBER_OF_RECURRENCES = "numberOfRecurrences";
    String X_KONY_REPORTING_PARAMS = "X-Kony-ReportingParams";
    String PARAM_ERR_ID = "errid";

    // ERR msgs and code
    String ERR_NON_WORKING_DAY_CODE = "STO.CURR.FREQ.NON.WORK.DAY";
    String ERR_NON_WORKING_DAY_MSG = "Transaction Cannot be done on non working day ";
    String ERR_NON_WORKING_DAY_CODE_DBX = "9014";

    String ERR_OVER_DRAFT_CODE = "AC-OVERDRAFT.ON.ACCOUNT";
    String ERR_OVER_DRAFT_CODE_DBX = "9013";

    String ERR_DEFAULT_MSG = "Error while creating Transaction";
    String ERR_DEFAULT_CODE_DBX = "9000";
	String EXTERNAL_ACCOUNT_NUMBER = "ExternalAccountNumber";
	public static final String PAYMENT_PRODUCT_ID = "paymentOrderProductId";
	
	String PARAM_TRANSACTION_NOTES = "transactionsNotes";
	String PARAM_STO_DISPLAYNAME = "displayName";
	int PARAM_STO_DISPLAYNAME_LENGTH = 35;
	
	String PARAM_PO_SWIFT_ADDRESS = "swiftAddress";
	String PARAM_STREET_NAME = "streetName";
	int PARAM_PO_SWIFT_ADDRESS_LENGTH = 35;

	String PARAM_BENEFICIARY_ADDR_LINE1 = "beneficiaryAddressLine1";
	String PARAM_BENEFICIARY_ADDR_LINE2 = "beneficiaryAddressLine2";
	String PARAM_BENEFICIARY_CITY = "beneficiaryCity";
	String PARAM_BENEFICIARY_ZIP_CODE = "beneficiaryZipcode";
	String PARAM_BENEFICIARY_COUNTRY = "beneficiarycountry";
	
    public static final String PARAM_CHARGES = "charges";
    public static final String PARAM_CHARGE_ACC_CCY_AMOUNT = "chargeAccountCurrencyAmount";
    public static final String PARAM_CHARGE_ACC_CCY_ID = "chargeAccountCurrencyId";
    String SRMS_SERVICE_NAME = "SRMSTransactionsJavaService";
    String SRMS_OPERATION_NAME = "getOneTimeTransactionById";
    String PARAM_SRMS_TRANSACTIONS = "SRMS_TRANSACTIONS";
    String PARAM_PAYMENT_BACKEND = "PAYMENT_BACKEND";
}
