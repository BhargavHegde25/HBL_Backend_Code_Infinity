package com.temenos.infinity.api.holdings.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum HoldingsAPIServices implements InfinityServices {

    HOLDINGSMICROSERVICESJSON_GETACCOUNTTRANSACTIONS("HoldingsMicroServicesJSON", "getAccountPostedTransactions"),
    HOLDINGSMICROSERVICESJSON_GETACCOUNTBALANCES("HoldingsMicroServicesJSON", "getAccountBalances"),
    T24_GETPENDINGTRANSACTIONS("ArrangementsT24Services", "getPendingTransactions"),
    HOLDINGSMICROSERVICESJSON_GETACCOUNTPENDINGANDPOSTEDTRANSACTIONS("TransactionsOrchServices",
            "getAccountPendingAndPostedTransactions"),
    HOLDINGSMICROSERVICESJSON_GETLOANSCHEDULETRANSACTIONS("ArrangementMicroService",
            "getLoanSchedule"),
    DBXUSER_GET_ACCOUNTSOVERVIEW("dbpRbLocalServicesdb", "dbxdb_customer_device_information_view_get"),
	HOLDINGSMICROSERVICESJSON_SEARCHTRANSACTIONS("HoldingsMicroServicesJSON","searchTransactions"),
	HOLDINGSMICROSERVICESJSON_SEARCHTRANSACTIONSTIMED("HoldingsMicroServicesJSON","searchTransactionsTimed"),
	DOC_GENERATEADHOC("DownloadTransactionJavaService","generateAdhocStatementFile"),
	DOCUMENT_STORAGE_ATTACHMENTS("DocumentIntegrationServices","searchDocument"),
	ATTACHMENTS_DBXDB_SERVICE("dbpRbLocalServicesdb","dbxdb_paymentfiles_get"),
    DBXUSER_GET_USERDETAILS("dbpRbLocalServicesdb", "dbxdb_customerverify_get"),
    DBXUSER_GET_BACKENDIDENTIFIERDETAILS("dbpRbLocalServicesdb", "dbxdb_backendidentifier_get"),
    DBXUSER_GET_DBXCUSTOMERDETAILS("dbpRbLocalServicesdb", "dbxdb_customer_get"),
    DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS("dbpRbLocalServicesdb", "dbxdb_accounts_get"),
    DBXDB_GET_TRANSACTIONTYPEMAPPING("dbpRbLocalServicesdb", "dbxdb_transactiontypemapping_get"),
    SERVICE_BACKEND_CERTIFICATE("dbpRbLocalServicesdb", "dbxdb_backendcertificate_get"),
	MOCKMORTGAGEMS_GETPOSTEDANDPENDINGTARNSACTIONS("mockMortagageMS", "getMortgagePendingAndPostedTransactions"),
	MOCKMORTGAGEMS_GETLOANSCHEDULETRANSACTIONS("mockMortagageMS", "getMortgageLoanScheduledTrasaction"),
	ACCOUNT_PENDING_AND_POSTED_TRANSACTIONS_MOCK("mockServices", "GetAccountPendingAndPostedTransactionsMock"),
	DOWNLOAD_STATEMENTS_MOCK("mockServices", "DownloadStatementMock"),
	LOAN_SCHEDULED_TRANSACTIONS_MOCK("mockServices", "getLoanScheduleTransactionsMock");
    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private HoldingsAPIServices(String serviceName, String operationName) {
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
