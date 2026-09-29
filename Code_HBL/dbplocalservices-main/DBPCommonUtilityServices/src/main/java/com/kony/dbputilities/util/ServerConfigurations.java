package com.kony.dbputilities.util;

import java.io.Serializable;

import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public enum ServerConfigurations implements Serializable {
	
	SAVINGSPOT_HOST_URL, SAVINGSPOT_ROLE_ID, SAVINGSPOT_AUTHORIZATION_KEY,
	SAVINGSPOT_DEPLOYMENT_PLATFORM,ARRANGEMENTS_BACKEND,DBP_AC_APP_KEY,DBP_AC_ACCESS_TOKEN,DBP_AC_APP_SECRET,MS_T24_AUTH_TOKEN_VALIDITY,
	ACCAGG_ROLE_ID,AMS_ROLE_ID,HOLDINGS_ROLE_ID,DM_API_KEY;

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
			return getValue();
		} catch (Exception e) {
			return "";
		}
	}

}
