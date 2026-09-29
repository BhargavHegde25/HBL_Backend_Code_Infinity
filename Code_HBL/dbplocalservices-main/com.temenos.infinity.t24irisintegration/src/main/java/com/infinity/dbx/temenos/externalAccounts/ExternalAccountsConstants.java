package com.infinity.dbx.temenos.externalAccounts;

public interface ExternalAccountsConstants {
	
	// Constants for Params
	String SWIFTCODE = "swiftCode";
	String PAYMENT_PRODUCT = "preferredPaymentProduct";
	String TRANSACTION_TYPE = "transactionType";
	String IS_INTERNATIONAL_ACCOUNT = "isInternationalAccount";
	String IS_SAMEBANK_ACCOUNT = "isSameBankAccount";
	String IS_VERIFIED = "isVerified";
	String NICKNAME = "nickName";
	String ROUTING_NUMBER = "routingNumber";
	String CREATED_ON = "createdOn";
	String BENEFICIARY_NAME = "beneficiaryName";
	String ACCOUNT_NUMBER = "accountNumber";
	String IBAN = "IBAN";
	String ACCOUNT_TYPE = "accountType";
	String VERSION_NUMBER = "versionNumber";
	String ID = "Id";
	String BENEFICIARY_SHORT_NAME = "beneficiaryShortName";
	
	// Constants for external account types
	String ACCOUNT_TYPE_DOMESTIC = "DOMESTIC";
	String ACCOUNT_TYPE_INTERNATIONAL = "INATIONAL";
	String ACCOUNT_TYPE_SEPA = "SEPA";
	String ACCOUNT_TYPE_INSTPAY = "INSTPAY";
	
	// Constants for transaction type codes
	String TRANSACTION_TYPE_BCIB = "BCIB";
	String TRANSACTION_TYPE_OTIB = "OTIB";
	String TRANSACTION_TYPE_ACIB = "ACIB";
	
	// Constants for dataset
	String DS_EXTERNAL_ACCOUNTS = "externalAccounts";
	String DS_BENEFICIARIES = "beneficiaries";
	
	// Constants for date format
	String T24_FORMAT = "yyMMddhhmm";
	String OFFSET = "offset";
	String LIMIT = "limit";
	String OFFSET_VALUE = "0";
	String LIMIT_VALUE = "1000";

}
