package com.kony.dbputilities.util;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class EnvironmentConfigurationsHandler {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static String getValue(String key, DataControllerRequest requestInstance) {
        try {
            ServicesManager serviceManager = null;
            if (null != requestInstance) {
                serviceManager = requestInstance.getServicesManager();
            } else {
                serviceManager = ServicesManagerHelper.getServicesManager();
            }

            return com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler
                    .getServerAppProperty(key, serviceManager);

            // ConfigurableParametersHelper configurableParametersHelper =
            // serviceManager.getConfigurableParametersHelper();
            // String requiredURL = configurableParametersHelper.getServerProperty(key);

            // String requiredURL = ConfigurationParameterUtilities.getValue(key,
            // configurableParametersHelper.getAllServerProperties());
            // return requiredURL;
        } catch (Exception are) {
            alert.prepareError(are.getMessage()).log();
        }
        return null;
    }

    public static String getValue(String key, FabricRequestManager requestManager) {
        ServicesManager serviceManager = requestManager.getServicesManager();
        String URL = null;
        try {
            URL = com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler
                    .getServerAppProperty(key, serviceManager);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the run time values :" + e.getMessage()).log();
        }
        return URL;

        // ConfigurableParametersHelper configurableParametersHelper = serviceManager.getConfigurableParametersHelper();
        // String requiredURL = configurableParametersHelper.getServerProperty(key);
        // return requiredURL;
    }

    public static String getValue(String key) {
        ServicesManager serviceManager;
        try {
            serviceManager = ServicesManagerHelper.getServicesManager();
            return com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler
                    .getServerAppProperty(key, serviceManager);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return "";
    }

    public static String getServerProperty(String key) {
        ServicesManager serviceManager;
        String requiredURL = "";
        try {
            serviceManager = ServicesManagerHelper.getServicesManager();
            ConfigurableParametersHelper configurableParametersHelper =
                    serviceManager.getConfigurableParametersHelper();
            requiredURL = configurableParametersHelper.getServerProperty(key);
        } catch (MiddlewareException e) {
        	alert.prepareError(e).log();
        }
        return requiredURL;
    }
}