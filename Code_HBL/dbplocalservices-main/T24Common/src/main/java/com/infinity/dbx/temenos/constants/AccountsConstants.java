package com.infinity.dbx.temenos.constants;

public interface AccountsConstants {

    String T24_SEQUENCENUMBER = "1";
    String T24_IDENTIFIER = "customer_id";
    String T24 = "T24";

    String CORE_ID = "CoreId";
    String ACCOUNTID = "accountID";
    String ARRANGEMENT_ID = "arrangementId";
    String INTEREST_PAID = "dividentPaidYtd";
    String START_DATE=  "startDate";
    String NEXT_PAYMENT_AMOUNT = "nextPaymentAmount";
    String INTEREST_RATE = "interestRate";
    String INTEREST_PAID_LASTYEAR = "interestPaidLastYear";
    String INTEREST_PAID_YTD = "interestPaidYTD";
    String LAST_PAYMENT_AMOUNT= "LastPaymentAmount";
    String LAST_PAYMENT_DATE = "LastPaymentDate";
    String DIVIDEND_RATE = "dividendRate";
    String DIVIDEND_LAST_PAID_DATE = "dividendLastPaidDate";
    String PRINCIPAL_BALANCE = "principalBalance";
    String BLOCKED_AMOUNT = "blockedAmount";
    String AVAILABLE_BALANCE = "availableBalance";
    String CURRENT_BALANCE = "currentBalance";
    String OPENING_DATE = "openingDate";
    String PAYMENT_DUE = "paymentDue";
    String CURR_AMOUNT_DUE = "currentAmountDue";
    String ORIGINAL_AMOUNT = "originalAmount";
    String NICKNAME = "nickName";
    String PARAM_STATUS = "status";
    String PARAM_ERROR_MESSAGE = "errmsg";
    String PARAM_ERROR_CODE = "errcode";
    String STATUS_FAILED = "failed";
    String STATUS_SUCCESS = "success";
    String PARAM_EXTERNAL_ID = "externalId";
    String PARAM_VALIDATE= "validate";
    String PARAM_VALIDATE_ONLY="validate_only";
    String PARAM_ESTATEMENT_TOGGLE ="eStatementEnable";
    String PARAM_ESTATEMENT_PRINT_ATTRIBUTE_NAME ="eStatementName";
    String PARAM_ESTATEMENT_PRINT_ATTRIBUTE_VALUE ="eStatementValue";
    String ESTATEMENT ="ESTATEMENT";
    String ESTATEMENT_NAME ="Electronic.Statement";
    String PRINTING_OPTION = "Printing.Option";
    String ESTATEMENT_NULL = "NULL";
    String OUTSTANDING_BALANCE = "outstandingBalance";
    String PRINCIPAL_VALUE = "principalValue";
    String PAYOFF_AMOUNT = "payoffAmount";
    String CURRENCY_CODE = "currencyCode";
    String DBP_ERROR_MSG = "dbpErrMsg";
    String DBP_ERR_CODE = "dbpErrCode";
    String T24_TXN_TYPE_ACTRF = "ACTRF";
    String CURRENCY_ID = "currencyId";
    String TOACCOUNT_CURRENCY = "toAccountCurrency";
    String PRODUCT_TYPE = "productType";
    String T24_FROMACCOUNT = "T24_FROMACCOUNTNUMBER";
    String ACTIVITY_ID = "activityId";
    String PRODUCT_ID = "productId";
    String PRODUCT_LI = "productLi";
    String PRODUCT = "product";
    String PRODUCT_NAME = "productName";
    String FAVOURITE_STATUS = "favouriteStatus";
    String UPDATENICKNAME_CALL = "nickNameUpdateCall";
    String DB_ACCOUNTID = "Account_id";
    String CUSTOMER_CORE_ID = "coreCustomerID";
    String DB_FAVOURITESTATUS = "FavouriteStatus";
    String DB_ACCOUNT_NAME = "AccountName";
    String DB_USER_ID = "User_id";
    String TYPE_ID = "type_id";
    String DISPLAY_NAME = "displayName";
    String CUSTOMER_REFERENCE = "customerReference";
    String STATEMENT = "statement";
    String MEMBERSHIP_ID = "Membership_id";
    String MEMBERSHIP_NAME = "MembershipName";
    String CUSTOMER_ID = "customerId";
    String CUSTOMER_NAME = "customerName";
    String ACCOUNT_ID = "Account_id";
    String CUSTOMER_TYPE_ID = "CustomerType_id";
    String DB_MEMBERSHIP_ID = "Membership_id";
    String DB_MEMBERSHIP_NAME = "MembershipName";
    String IS_BUSINESS_ACCOUNT = "isBusinessAccount";
	String SCHEDULE_PROPERTY="scheduleProperty";
    String SCHEDULE_PAYMENT_FREQUENCY="schedulePaymentFrequency";
    String PRINCIPAL_INTEREST="Principal Interest";
    String REPAYMENT_FREQUENCY="rePaymentFrequency";
    String DIVIDENDPAIDYTD="dividendPaidYTD";
    String DIVIDEND_LAST_PAID_AMOUNT="dividendLastPaidAmount";
    String INTERESTPAIDYTD="interestPaidYtd";
    String LASTPAIDINTERESTAMOUNT="lastPaidInterestAmount";
    String LASTPAYMENTDATE="lastPaymentDate";
    String PROPERTY_CREDIT_INTEREST="Credit Interest";
    String ACCRUALAMOUNT="accrualAmount";

    // Constants for params
    String PARAM_USERNAME = "username";
    String PARAM_FULLNAME = "fullname";

    
    // Constants for Dataset
    String DS_ACCOUNTS = "Accounts";
    String DS_ORGINAZTION_ACCOUNTS = "OgranizationAccounts";
    String DS_PRODUCTS = "products";
    String DS_INTERESTS = "interests";
    String DS_CUSTOMERACCOUNTS = "customeraccounts";
    String DS_DB_ACCOUNTS = "accounts";
    String DS_SCHEDULES="schedules";
    String FUTURE_INSTALLMENT_COUNT="futureInstallmentsCount";
    String OVERDUE_INSTALLMENT_COUNT="overDueInstallmentsCount";
    String PAID_INSTALLMENT_COUNT="paidInstallmentsCount";

    // IRIS Account types
    String TERM_DEPOSITS = "TERM.DEPOSIT";
    String SS_FIXED_TERM = "SS.FIXED.TERM";
    String SS_ROLL_OVER = "SS.ROLLOVER.01M";
    String PERSONAL_LOANS = "PERSONAL.LOAN";
    String MORTGAGES = "MORTGAGE";
    String VEHICLE_LOAN = "VEHICLE.LOAN";
    String SAVINGS_ACCOUNTS = "SAVINGS.ACCOUNT";
    String CONS_SAVING = "CONS.SAVINGS";
    String CONS_MM = "CONS.MM";
    String SS_SAVINGS_CHILD = "SS.SAVINGS.CHILD";
    String SS_SAVINGS_REGULAR = "SS.SAVINGS.REGULAR";
    String CURRENT_ACCOUNTS = "CURRENT.ACCOUNT";
    String CONS_CHECKING = "CONS.CHECKING";
    String SS_PAYG = "SS.PAYG";
    String SS_MONTHLY = "SS.MONTHLY";
    String SS_ANNUAL = "SS.ANNUAL";
    

    // Customer Types
    String RETAIL_TYPE = "TYPE_ID_RETAIL";
    String PARAM_ORGANISATION_ID = "Organization_Id";
    String FILTER = "$filter";
    String ORGANISATION_FILTER = "Organization_id eq ";
    String PARAM_STATUS_DESC = "StatusDesc";
    String STATUS_ACTIVE = "Active";
    String DBX_ACCOUNT_TYPE_6 = "6";
    String PARAM_IS_ORGANIZATION_ACCOUNT = "IsOrganizationAccount";

    String PARAM_ACCOUNT_PERMISSION = "transactionPermission";
    String PARAM_ADMIN = "ADMIN";
    String PARAM_ESTATEMENTENABLE = "eStatementEnable";
    String DB_PARAM_ESTATEMENTENABLE = "EStatementmentEnable";
    String LENDING_UPDATE_STATEMENT = "LENDING-UPDATE-STATEMENT";
    String DEPOSITS_UPDATE_STATEMENT = "DEPOSITS-UPDATE-STATEMENT";
    String ACCOUNTS_UPDATE_STATEMENT = "ACCOUNTS-UPDATE-STATEMENT";
    String PARAM_UPDATEDBY = "UpdatedBy";
    String PARAM_LASTUPDATED = "LastUpdated";
    String PARAM_RESULT = "result";
    String RESPONSE_ACCOUNTID = "accountId";
    

    String PARAM_ACCOUNT_JOINT_HOLDER = "jointHolders";
    String PARAM_ACCOUNT_JOINT_OWNER = "jointOwner";
    
    String ACCOUNT_PROPS = "Account.properties";
    
    
    //New activities for product lines
    String ACCOUNTS_NEW_ACTIVITY = "ACCOUNTS-NEW-ARRANGEMENT";
    String DEPOSITS_NEW_ACTIVITY = "DEPOSITS-NEW-ARRANGEMENT";
    String LENDING_NEW_ACTIVITY = "LENDING-NEW-ARRANGEMENT";
    
    String FEATURE_ID = "FeatureId";
	String FEATURE_VALUE = "SelectedValue";
	String SEQUENCE_NO = "SequenceNo";
	String HEADER = "header";
	String TRANSACTION_STATUS = "transactionStatus";
	String LIVE = "Live";
	String PROPERTIES = "properties";
	String ARRANGEMENT_ACTIVITY = "arrangementActivity";
	String BODY = "body";
	String ACC_ID = "accountId";
	String PARAM_TRANSFER_LIMIT = "transferLimit";
	String PARAM_ACC_NAME = "accountName";
	
	//Account banner constants
	String DEFAULT_CAMPAIGN = "DEFAULT_CAMPAIGN";
	String CAMPAIGN_SERVICE = "dbpRbLocalServicesdb";
	String CAMPAIGN_OPERATION = "dbxdb_custcompletedcampaign_create";
	
}
