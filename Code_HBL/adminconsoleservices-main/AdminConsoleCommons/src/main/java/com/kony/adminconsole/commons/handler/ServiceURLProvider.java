package com.kony.adminconsole.commons.handler;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.URLProvider;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Common URL provider class to resolve the relevant URL as per the requested service. Resolves the URL by fetching
 * values from the Run Time Configurations
 * 
 * @author Aditya Mankal
 *
 */
public class ServiceURLProvider implements URLProvider {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String KEY_ENDS_MARKER = "_$_";

    @Override
    public String execute(String operationURL, DataControllerRequest requestInstance) {

        try {
            String targetURL = StringUtils.EMPTY;
            String propertyKey = operationURL.substring(
                    operationURL.indexOf(KEY_ENDS_MARKER) + KEY_ENDS_MARKER.length(),
                    operationURL.lastIndexOf(KEY_ENDS_MARKER));
            String baseURL = operationURL.substring(0,
                    operationURL.lastIndexOf(KEY_ENDS_MARKER) + KEY_ENDS_MARKER.length());
            targetURL = operationURL.replace(baseURL,
                    EnvironmentConfigurationsHandler.getServerAppPropertyValue(propertyKey, requestInstance));
            diagnostic.prepareDebug("URL resolved from server configurations :" + targetURL).log();
            return targetURL;
        } catch (Exception e) {
            alert.prepareError("Error occured while resolving operation URL. Attempted Operation URL:" + operationURL).log();
            alert.error(e);
        }
        return null;
    }

}
