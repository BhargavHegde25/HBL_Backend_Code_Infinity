package com.temenos.infinity.api.accountaggregation.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum AccountAggregationServices implements InfinityServices {

    ACCOUNTAGGREGATIONJSON_GETBANKSFORCOUNTRY("AccountAggregationJSON", "getBanksForCountry"),
    ACCOUNTAGGREGATIONJSON_GETCONNECTIONDETAILS("AccountAggregationJSON", "getConnectionDetails"),
    ACCOUNTAGGREGATIONJSON_CREATECUSTOMER("AccountAggregationJSON", "createCustomer"),
    ACCOUNTAGGREGATIONJSON_GETCONSENTURL("AccountAggregationJSON", "getConsentURL"),
    ACCOUNTAGGREGATIONJSON_REAUTHCONNECTIONURL("AccountAggregationJSON", "reauthConnectionURL"),
    ACCOUNTAGGREGATIONJSON_LOADCONNECTIONS("AccountAggregationJSON", "loadConnections"),
    ACCOUNTAGGREGATIONJSON_LOADACCOUNTSWITHTRANSACTIONS("AccountAggregationJSON", "loadAccountsWithTransactions"),
    ACCOUNTAGGREGATIONJSON_REFRESHCONNECTION("AccountAggregationJSON", "refreshConnection"),
    ACCOUNTAGGREGATIONJSON_DELETECONNECTION("AccountAggregationJSON", "deleteConnection"),
    ACCOUNTAGGREGATIONJSON_GETLOGOURL("AccountAggregationJSON", "getLogoURL"),
    CONSENTJSON_CREATECONSENT("ConsentJSON", "createConsent"),
    DBPPRODUCTSERVICES_GETCUSTOMERTERMSANDCONDITIONS("dbpProductServices", "getCustomerTermsAndConditions"),
    SERVICE_BACKEND_CERTIFICATE("dbpRbLocalServicesdb", "dbxdb_backendcertificate_get");

    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private AccountAggregationServices(String serviceName, String operationName) {
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
