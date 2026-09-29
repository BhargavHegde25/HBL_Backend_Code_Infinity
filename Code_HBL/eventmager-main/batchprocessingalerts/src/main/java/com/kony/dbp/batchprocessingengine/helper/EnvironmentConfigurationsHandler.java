package com.kony.dbp.batchprocessingengine.helper;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;

public class EnvironmentConfigurationsHandler {
	private EnvironmentConfigurationsHandler() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static String getValue(String key, ServicesManager servicemanager) {
		try {
			ConfigurableParametersHelper configurableParametersHelper = servicemanager
					.getConfigurableParametersHelper();
			return configurableParametersHelper.getServerProperty(key);
		} catch (Exception are) {

			alert.prepareError("Error occured while fetching environment configuration parameter. Attempted Key:" + key, are).log();

		}
		return null;
	}
}