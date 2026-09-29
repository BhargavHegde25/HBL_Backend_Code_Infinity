package com.temenos.infinity.api.arrangements.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum ArrangementsAPIServices implements InfinityServices {

    ACCOUNTAGGREGATIONJSON_GETCONNECTIONDETAILS("AccountAggregationJSON", "getConnectionDetails"),
    ACCOUNTAGGREGATIONJSON_GETLOGOURL("AccountAggregationJSON", "getLogoURL"),
    HOLDINGSMICROSERVICESJSON_GETACCOUNTBALANCES("HoldingsMicroServicesJSON", "getAccountBalances"),
    ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTS("ArrangementMicroService", "getArrangements"),
    ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTOVERVIEW("ArrangementMicroService", "getAccountDetails"),
    ARRANGEMENTSMICROSERVICESJSON_GETLOANINSTALLMENTOVERVIEW("ArrangementMicroService", "getLoanInstallmentsDetails"),
    ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTSBYCUSTOMERID("ArrangementMicroService", "getArrangementsByCustomerId"),
    ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTSBYACCOUNTID("ArrangementMicroService", "getArrangementsByAccountId"),
    ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTSBYSOCIALSECURITYID("ArrangementMicroService","getArrangementsBySocialSecurityId"),
    ARRANGEMENTSORCHSERVICESJSON_GETUSERPORTFOLIODETAILS("ArrangementsOrchServices","getUserPortfolioAccounts"),
    ARRANGEMENTSORCHSERVICESJSON_UPDATEACCOUNTDETAILS("ArrangementsOrchServices","updateAccountSettings"),
    MOCKMORTGAGEMS_FETCHMORTGAGEACCOUNT("mockMortagageMS", "fetchMortgageAccount"),
    MOCKMORTGAGEMS_GETMORTGAGEDETAILS("mockMortagageMS", "GetMortgageDetails"),
    T24ISACCOUNTS_GETBUSINESSACCOUNTS("T24IrisArrangementServices", "getAccountsByAccountId"),
    T24ISACCOUNTS_GETBUSINESSACCOUNTOVERVIEW("T24ISExtra", "getAccountDetails"),
    DBXUSER_GET_ACCOUNTSOVERVIEW("dbpRbLocalServicesdb", "dbxdb_customer_device_information_view_get"),
    DBXUSER_GET_USERDETAILS("dbpRbLocalServicesdb", "dbxdb_customerverify_get"),
    DBXUSER_GET_BACKENDIDENTIFIERDETAILS("dbpRbLocalServicesdb", "dbxdb_backendidentifier_get"),
    DBXUSER_GET_DBXCUSTOMERDETAILS("dbpRbLocalServicesdb", "dbxdb_customer_get"),
    DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS("dbpRbLocalServicesdb", "dbxdb_accounts_get"),
    SERVICE_BACKEND_CERTIFICATE("dbpRbLocalServicesdb", "dbxdb_backendcertificate_get"),
    T24ISACCOUNTS_GETACCOUNTDETAILSFORCOMBINEDSTMTS("T24ISExtra", "getAccountDetailsForCombinedStatement"),
    DBXDB_CUSTOMER_ACCOUNTS("dbpRbLocalServicesdb",
            "dbxdb_customeraccounts_get"),
    DBXDB_CONTRACT_CUSTOMERS("dbpRbLocalServicesdb", "dbxdb_contractcustomers_get"),
    GETCORECUSTOMERIDSANDACCOUNTS("dbpRbLocalServicesdb", "dbxdb_getCoreCustomerIdsAndAccounts"),
    UPDATE_CORECUSTOMERID_FAV_STATUS("dbpRbLocalServicesdb", "dbxdb_update_customer_favorite_status"),
    DBPPRODUCTSERVICES_NEWACCOUNTPROCESSING("dbpProductServices", "NewAccountProcessing"),
    DBXDB_FETCH_DEFAULT_ACTIONS("dbpRbLocalServicesdb", "dbxdb_fetch_default_account_actions_proc"),
    GET_USER_SCHEDULED_TRANSACTIONS_MOCK("mockServices", "GetUserScheduledTransactionsMock"),
	GET_BLOCKED_FUNDS_MOCK("mockServices", "GetBlockedFundsMock"),
	UPDATE_ESTATEMENT_PREFERENCES("ArrangementMicroService", "updateEStatementPreferences"),
	MORTGAGE_GETMORTGAGESIMULATEDRESULTS("mockMortagageMS","getMortgageSimulatedResults"),
	SERVICEREQUESTJSON_UPDATEORDER("ServiceRequestJSON", "updateOrder"),
	T24MORTGAGESERVICES_GETSIMULATEDRESULTS("T24MortgageServices","getSimulatedResults"),
	T24MORTGAGESERVICES_CREATEPAYOFFSIMULATION("T24MortgageServices","CreatePayoffSimulation"),
	T24MORTGAGESERVICES_GETPAYOFFSIMULATION("T24MortgageServices","getPayOffSimulationResults"),
	T24IRISARRANGEMENTSERVICES_GETLATESTBALANCES("T24IrisArrangementServices","getLatestBalances"),
	ARRANGEMENTS_SAVINGSACCOUNT_CLOSURE("ArrangementsJavaServices","savingAccountClose"),
	ARRANGEMENTS_CHECKINGACCOUNT_CLOSURE("ArrangementsJavaServices","currentAccountClose")
;

    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private ArrangementsAPIServices(String serviceName, String operationName) {
        this.serviceName = serviceName;
        this.operationName = operationName;
    }

    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public String getOperationName() {
        return this.operationName;
    }

}
