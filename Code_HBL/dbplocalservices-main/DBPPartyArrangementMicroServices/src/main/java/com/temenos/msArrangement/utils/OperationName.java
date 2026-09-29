package com.temenos.msArrangement.utils;

/**
 * 
 * @author KH2281
 * @version 1.0
 * Contains constants for integration operation Name/ object verb name
 */
public final class OperationName {
	
	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("MS_HOLDINGS_URL");
	public static final String AMS_SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("MS_ARRANGEMENTS_URL");
	public static final String COMPANY_ID = EnvironmentConfigurationsHandler.getValue("COMPANY_ID");

	public static final String GET_ACCOUNTTRANSACTIONS = SCHEMA_NAME+"/holdings/accounts/param/transactions";
	public static final String GET_ACCOUNT_BALANCES = SCHEMA_NAME+"/holdings/accounts/param/balances";
	public static final String GET_ACCOUNTS = AMS_SCHEMA_NAME+"/holdings/parties/param/bulkarrangements";
	

}