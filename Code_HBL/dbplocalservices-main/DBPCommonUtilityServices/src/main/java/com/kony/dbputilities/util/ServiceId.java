package com.kony.dbputilities.util;

public final class ServiceId {

    public static final String COMMON_ORCHESTRATION_SERVICE =
            IntegrationTemplateURLFinder.getBackendURL("CommonOrchestrationService");

    public static final String T24ISACCOUNTS_INTEGRATION_SERVICE =
            IntegrationTemplateURLFinder.getBackendURL("AccountsInfoService");

    public static final String T24ISUSER_INTEGRATION_SERVICE =
            IntegrationTemplateURLFinder.getBackendURL("UserInfoService");

    public static final String BACKEND_TYPE =
            IntegrationTemplateURLFinder.getBackendURL(DBPUtilitiesConstants.BACKEND_TYPE);

    public static final String BACKEND_CUSTOMER_IDENTIFIER_NAME =
            IntegrationTemplateURLFinder.getBackendURL(DBPUtilitiesConstants.BACKEND_CUSTOMER_IDENTIFIER_NAME);

    public static final String T24ISEXTRA_INTEGRATION_SERVICE =
            IntegrationTemplateURLFinder.getBackendURL("UserIntegrationService");
   
    public static final String DBP_PRODUCT_SERVICE =
            IntegrationTemplateURLFinder.getBackendURL("eumProductServices");
}
