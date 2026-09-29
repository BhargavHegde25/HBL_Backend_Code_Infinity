package com.temenos.infinity.api.transactionadviceapi.config;

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

    CAMS_COMPANYID, AMS_COMPANYID, T24_PRIVATE_ENCRYPTION_KEY, DBP_HOST_URL, AUTOFORM_USERNAME,	AUTOFORM_PASSWORD;

    public String getValue() throws Exception {
        ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
        return EnvironmentConfigurationsHandler.getServerAppProperty(this.name(), servicesManager);
    }

    public String getValue(DataControllerRequest requestInstance) throws Exception {
        return EnvironmentConfigurationsHandler.getServerAppProperty(this.name(), requestInstance);
    }

}
