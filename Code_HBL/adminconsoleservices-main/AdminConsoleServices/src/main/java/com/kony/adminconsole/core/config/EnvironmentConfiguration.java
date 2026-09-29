package com.kony.adminconsole.core.config;

import java.io.Serializable;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Class used to fetch the Run Time Configuration Parameter values.
 * 
 * @author Aditya Mankal
 *
 */
public enum EnvironmentConfiguration implements Serializable {

    AC_HOST_URL,
    AC_DBP_SERVICES_URL,
    AC_LOG_SERVICES_URL,
    AC_DBP_AUTH_URL,
    AC_FABRIC_LOGIN_USERNAME,
    AC_FABRIC_LOGIN_PASSWORD,
    AC_DBP_APP_KEY,
    AC_DBP_APP_SECRET,
    AC_DBP_SHARED_SECRET,
    AC_EMAIL_TEMPLATE_LOGO_URL,
    AC_EMAIL_TEMPLATE_TEMENOS_LOGO_URL,
    AC_CSR_ASSIST_OLB_HOST_URL,
    AC_CSR_ASSIST_CL_HOST_URL,
    AC_CSR_ASSIST_CO_HOST_URL,
    AC_APP_ID_ONBOARDING,
    AC_CREATE_CUSTOMER_HOST_URL,
    AC_KMS_URL, AC_ENCRYPTION_KEY,
    AC_INTERNAL_API_ACCESS_TOKEN,
    AC_LOG_SERVICES_API_ACCESS_TOKEN,
    DBX_SCHEMA_NAME,
    AC_OKTA_AUTHORIZATION_KEY,
    AC_SERVICE_INVOKE_METHOD,
    AC_APPID_TO_APP_MAPPING,
    AC_NUMVERIFY_API_KEY,
    AC_CL_APP_KEY,
    AC_CL_APP_SECRET,
    AC_CL_SERVICES_URL,
    BRANCH_ID_REFERENCE,
    AC_CL_SHARED_SECRET,
    KEYCLOAK_SERVICE_ACCOUNT_CLIENT_ID,
    KEYCLOAK_SERVICE_ACCOUNT_CLIENT_SECRET,
    KEYCLOAK_SERVICE_REDIRECT_URI,
    ONBOARDING_ENTITY_DEFINTION,
    SME_ONBOARDING_ENTITY_DEFINTION,
    AC_MSG_SUGGESTION_LIMIT,
    AC_MSG_CREATE_THREAD_COUNT,
    AC_CSR_ASSIST_OLB_ACC_URL,
    AC_APP_ID_OLB,
    ODMS_DEPLOYMENT_PLATFORM,
    ODMS_AUTHORIZATION_KEY,
    DUE_DILIGENCE_DEPLOYMENT_PLATFORM,
    DUE_DILIGENCE_AUTHORIZATION_KEY,
    SFS_KEYCLOAK_SERVICE_CLIENT_ID,
    SFS_KEYCLOAK_SERVICE_CLIENT_SECRET,
    AC_TPP_TOKEN_PUBLIC_KEY_URL,
    AC_TPP_TOKEN_ISSUER,
    AC_TPP_TOKEN_AUDIANCE,
    AC_TPP_SYSTEM_USER_USERNAME,
    AC_TPP_PUBLIC_KEY,
    AC_DBP_REPORTING_PARAMS,
    USERNAME,
    MARKETING_CATALOGUE_DEPLOYMENT_PLATFORM,
    MARKETING_CATALOGUE_AUTHORIZATION_KEY,
    PASSWORD,
	IS_SCA_ENABLED,
	LEAD_ENTITY_DEFINITION,
    AC_CUST_READ_BATCH_SIZE;

    public String getValue(DataControllerRequest requestInstance) {
        return EnvironmentConfigurationsHandler.getServerAppPropertyValue(this.name(), requestInstance);
    }
    public String getValue(ServicesManager servicesManager) {
        return EnvironmentConfigurationsHandler.getServerAppPropertyValue(this.name(), servicesManager);
    }

}
