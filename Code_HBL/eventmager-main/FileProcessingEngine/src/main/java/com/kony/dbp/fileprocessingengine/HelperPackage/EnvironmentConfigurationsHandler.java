package com.kony.dbp.fileprocessingengine.HelperPackage;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;

public class EnvironmentConfigurationsHandler {
	private EnvironmentConfigurationsHandler() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

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