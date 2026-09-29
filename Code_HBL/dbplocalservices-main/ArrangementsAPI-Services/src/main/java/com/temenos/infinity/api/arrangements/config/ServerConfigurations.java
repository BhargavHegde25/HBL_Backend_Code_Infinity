package com.temenos.infinity.api.arrangements.config;

import java.io.Serializable;

import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

/**
 * Class used to fetch the Server Run-time Configuration Parameter values.
 * 
 * @author Aditya Mankal
 *
 */
public enum ServerConfigurations implements Serializable {



	ACCAGG_DEPLOYMENT_PLATFORM, ACCAGG_AUTHORIZATION_KEY, ACCAGG_ROLE_ID,AMS_COMPANYID, CONFIG_KEY, AMS_PRIVATE_ENCRYPTION_KEY, DBP_HOST_URL, AMS_DEPLOYMENT_PLATFORM, AMS_AUTHORIZATION_KEY, AMS_ROLE_ID, DBP_AC_ACCESS_TOKEN, DBP_AC_APP_KEY, DBP_AC_APP_SECRET, ARRANGEMENTS_BACKEND,HOLDINGS_ROLE_ID,HOLDINGS_AUTHORIZATION_KEY,HOLDINGS_DEPLOYMENT_PLATFORM,MOCK_MORTGAGE_RESPONSE,BRANCH_ID_REFERENCE,VIEWDOC_AUTH_KEY,OWNERSYSTEMID,DOCUMENTGROUP,MOCK_ACCOUNT_CLOSURE_LIST,ACCOUNT_CLOSURE_OVERRIDE,SERVICE_REQUEST_DEPLOYMENT_PLATFORM,SERVICE_REQUEST_AUTHORIZATION_KEY,SERVICE_REQUEST_ROLE_ID,REDESIGN_CUSTID;


    public String getValue() throws Exception {
        ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
        return EnvironmentConfigurationsHandler.getServerAppProperty(this.name(), servicesManager);
    }

    public String getValue(DataControllerRequest requestInstance) throws Exception {
        return EnvironmentConfigurationsHandler.getServerAppProperty(this.name(), requestInstance);
    }

    // Method that provides EmptyString if any exception 
    public String getValueIfExists() {
        try {
            ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
            return EnvironmentConfigurationsHandler.getServerAppProperty(this.name(), servicesManager);
        } catch (Exception e) {
            return "";
        }
    }

}