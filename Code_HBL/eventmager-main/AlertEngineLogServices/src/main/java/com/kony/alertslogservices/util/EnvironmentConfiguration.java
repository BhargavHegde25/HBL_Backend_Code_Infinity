package com.kony.alertslogservices.util;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;

public class EnvironmentConfiguration {
	private EnvironmentConfiguration() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static String getValue(String key, DataControllerRequest requestInstance) {
		String val = null;
		try {
			ServicesManager serviceManager = requestInstance.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			val = configurableParametersHelper.getServerProperty(key);

		} catch (Exception are) {
			diagnostic.prepareDebug("error", are).log();
		}
		return val;
	}

}
