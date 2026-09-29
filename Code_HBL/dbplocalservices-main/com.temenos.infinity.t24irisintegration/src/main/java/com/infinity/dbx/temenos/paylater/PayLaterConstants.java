package com.infinity.dbx.temenos.paylater;

public interface PayLaterConstants {

	
	String PARAM_STATUS="status";
	String PARAM_VALUE_SUCCESS="success";
	String PARAM_VALUE_FAILED="failed";
	String PARAM_TRANSACTION_STATUS="transactionStatus";
	String TRANSACTION_STATUS_LIVE="Live";
	String PARAM_OVERRIDE="override";
	String SERVICE_ID_PAYLATER="t24PayLater";
	String OPER_ID_CREATE_INSTALLMENT="createInstallment";
	String PARAM_ERR_MSG="errmsg";
	String PARAM_TRANS_DESC="transactionDescription";
	String PARAM_TRANS_DESC_1="transactionDescription1";
	String PARAM_DESCRIPTION="description";
	String PARAM_TRANS_DATE="transactionDate";
	String DATE_FORMAT="yyyy-MM-dd";
	String PARAM_REM_INF="remittanceInformation";
	String PARAM_REM_INF1="remittanceInformation1";
	String PARAM_ACCOUNT_ID="accountID";
	String PARAM_SPROUT="Sprout";
	String PARAM_DUE_ON="dueOn";
	String DATASET_INSTALLMENTS="installments";
	String PARAM_MATURITY_DATE="maturityDate";
	String DATASET_INST_PROD="instProducts";
	String PARAM_INSTALLMENT_START_DATE="installmentStartDate";
	String DATE_FROM = "dateFrom";
	String T24_DATE_FORMATE = "yyyyMMdd";
}
