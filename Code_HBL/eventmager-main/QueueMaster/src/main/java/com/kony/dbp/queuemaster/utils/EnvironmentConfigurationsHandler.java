package com.kony.dbp.queuemaster.utils;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;

public class EnvironmentConfigurationsHandler {
	private EnvironmentConfigurationsHandler() {

	}
	
	public static String getValue(String key) {
        
		return Config.getValue(key);

    }
}