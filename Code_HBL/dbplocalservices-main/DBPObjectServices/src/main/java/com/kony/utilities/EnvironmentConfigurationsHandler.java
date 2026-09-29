package com.kony.utilities;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class EnvironmentConfigurationsHandler {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static String getValue(String key, FabricRequestManager requestInstance) {
        try {
            ServicesManager serviceManager = requestInstance.getServicesManager();
            ConfigurableParametersHelper configurableParametersHelper = serviceManager
                    .getConfigurableParametersHelper();
            return configurableParametersHelper.getServerProperty(key);
        } catch (Exception e) {
            alert.prepareError("Error occured", e).log();
        }

        return null;
    }
}