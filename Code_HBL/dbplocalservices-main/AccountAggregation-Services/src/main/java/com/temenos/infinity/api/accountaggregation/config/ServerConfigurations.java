package com.temenos.infinity.api.accountaggregation.config;

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

    CONFIG_KEY, AMS_COMPANYID, DBP_HOST_URL, ACCAGG_PRIVATE_ENCRYPTION_KEY, ACCAGG_DEPLOYMENT_PLATFORM, ACCAGG_AUTHORIZATION_KEY,
	ACCAGG_ROLE_ID, CONSENT_DEPLOYMENT_PLATFORM, CONSENT_AUTHORIZATION_KEY; 

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
