package com.kony.dbputilities.util;

public final class OperationName {

    public static final String GET_ALL_ACCOUNTS = IntegrationTemplateURLFinder.getBackendURL("getAllAccounts");

    public static final String GET_ACCOUNT_INFORMATION =
            IntegrationTemplateURLFinder.getBackendURL("GetAccountHolderInformation");

    public static final String GET_AUTHORIZED_SIGNATORIES =
            IntegrationTemplateURLFinder.getBackendURL("getCompanyAuthorizedSignatories");

    public static final String GET_COMPANY_DETAILS =
            IntegrationTemplateURLFinder.getBackendURL("getCompanyDetails");

    public static final String GET_COMPANY_ADDRESS =
            IntegrationTemplateURLFinder.getBackendURL("getCompanyAddress");

    public static final String GET_COMPANYID_FROM_TAXID =
            IntegrationTemplateURLFinder.getBackendURL("getCompanyIdFromTaxId");

    public static final String CORE_CUSTOMER_SEARCH =
            IntegrationTemplateURLFinder.getBackendURL("coreCustomerSearch");

    public static final String CORE_CUSTOMER_GET =
            IntegrationTemplateURLFinder.getBackendURL("getUserDetails");

    public static final String SEARCH_CORE_CUSTOMER = IntegrationTemplateURLFinder.getBackendURL("searchCoreCustomers");

    public static final String GET_PARTY_RELATIONS = IntegrationTemplateURLFinder.getBackendURL("getPartyRelations");

    public static final String GET_CORE_CUSTOMER_ACCOUNTS =
            IntegrationTemplateURLFinder.getBackendURL("getCoreCustomerAccountDetails");

    public static final String GET_USER_DETAILS =
            IntegrationTemplateURLFinder.getBackendURL("getUserDetails");

    public static final String UPDATE_CUSTOMER_DETAILS_OPERATION =
            IntegrationTemplateURLFinder.getBackendURL("UpdateCustomerDetailsOperation");

    public static final String UPDATE_USER_DETAILS =
            IntegrationTemplateURLFinder.getBackendURL("updateUserDetails");

    public static final String UPDATE_FULL_USER_DETAILS =
            IntegrationTemplateURLFinder.getBackendURL("updateFullUserDetails");

    public static final String GET_CORE_USER_EXISTS =
            IntegrationTemplateURLFinder.getBackendURL("checkCoreUserExists");

}
