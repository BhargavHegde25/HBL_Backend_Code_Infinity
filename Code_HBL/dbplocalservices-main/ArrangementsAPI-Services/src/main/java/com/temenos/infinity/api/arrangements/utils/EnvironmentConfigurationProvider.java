package com.temenos.infinity.api.arrangements.utils;

import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class EnvironmentConfigurationProvider {
	public static String getConfiguredServerProperty(String key) throws MiddlewareException {
		return getConfiguredServerProperty(ServicesManagerHelper.getServicesManager(), key);
	}
	
	public static String getConfiguredServerProperty(ServicesManager servicesManager, String key) {
		return servicesManager.getConfigurableParametersHelper().getServerProperty(key);
	}
}