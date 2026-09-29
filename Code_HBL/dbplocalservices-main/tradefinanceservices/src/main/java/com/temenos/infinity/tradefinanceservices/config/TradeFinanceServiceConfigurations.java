package com.temenos.infinity.tradefinanceservices.config;

import java.io.Serializable;

import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

/**
 * Class used to fetch the Server Run-time Configuration Parameter values.
 * 
 * @author smugesh
 *
 */
public enum TradeFinanceServiceConfigurations implements Serializable {

    T24_PRIVATE_ENCRYPTION_KEY, DBP_HOST_URL;

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
