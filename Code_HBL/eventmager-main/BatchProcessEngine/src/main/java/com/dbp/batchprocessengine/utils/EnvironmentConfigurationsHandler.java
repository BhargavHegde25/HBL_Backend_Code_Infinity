package com.dbp.batchprocessengine.utils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;

public class EnvironmentConfigurationsHandler {
	private EnvironmentConfigurationsHandler() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static String getValue(String key, FabricRequestManager requestInstance) {
		try {
			ServicesManager serviceManager = requestInstance.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			return configurableParametersHelper.getServerProperty(key);

		} catch (Exception are) {

			alert.prepareError("Error occured while fetching environment configuration parameter. Attempted Key:" + key, are).log();

		}
		return null;
	}
	
	public static String getValue(String key, DataControllerRequest requestInstance) {
		try {
			ServicesManager serviceManager = requestInstance.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			return configurableParametersHelper.getServerProperty(key);

		} catch (Exception are) {

			alert.prepareError("Error occured while fetching environment configuration parameter. Attempted Key:" + key, are).log();

		}
		return null;
	}
}