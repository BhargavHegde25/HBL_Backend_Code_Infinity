package com.kony.adminconsole.commons.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.config.ConfigurationParameterUtilities;

/**
 * <p>
 * Handler Class to fetch the Environment Configuration Values
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class EnvironmentConfigurationsHandler {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    /**
     * Returns the value associated with the provided server property name
     * 
     * @param propertyName
     * @param requestInstance
     * @return associated value or null if either no value exists or on any exception
     */
    public static String getServerAppPropertyValue(String propertyName, DataControllerRequest requestInstance) {
        try {
            ServicesManager servicesManager = requestInstance != null ? requestInstance.getServicesManager()
                    : ServicesManagerHelper.getServicesManager((HttpServletRequest) null);
            return getServerAppPropertyValue(propertyName, servicesManager);
        } catch (Exception e) {
            alert.prepareError("Error occured while accessing Services Manager", e).log();
        }
        return null;
    }

    /**
     * Returns the value associated with the provided server property name
     * 
     * @param propertyName
     * @param requestInstance
     * @return associated value or null if either no value exists or on any exception
     */
    public static String getServerAppPropertyValue(String propertyName, HttpServletRequest requestInstance) {
        try {
            return getServerAppPropertyValue(propertyName, ServicesManagerHelper.getServicesManager(requestInstance));
        } catch (Exception e) {
            alert.prepareError("Error occured while accessing Services Manager", e).log();
        }
        return null;
    }

    /**
     * Returns the value associated with the provided server property name
     * 
     * @param propertyName
     * @param servicesManager
     * @return
     */
    public static String getServerAppPropertyValue(String propertyName, ServicesManager servicesManager) {
        try {
            ConfigurableParametersHelper configurableParametersHelper =
                    servicesManager.getConfigurableParametersHelper();
            Map<String, String> serverProperties = configurableParametersHelper.getAllServerProperties();
            String value = ConfigurationParameterUtilities.getValue(propertyName, serverProperties);
            return value;
        } catch (Exception e) {
            alert.prepareError("Error occured while fetching environment configuration parameter. Attempted server property:"
                    + propertyName, e).log();
        }
        return null;
    }

    /**
     * Returns the value associated with the provided client app property name
     * 
     * @param propertyName
     * @param requestInstance
     * @return associated value or null if either no value exists or on any exception
     */
    public static String getClientAppPropertyValue(String propertyName, DataControllerRequest requestInstance) {
        try {
            return getClientAppPropertyValue(propertyName, requestInstance.getServicesManager());
        } catch (Exception e) {
            alert.prepareError("Error occured while accessing Services Manager", e).log();
        }
        return null;
    }

    /**
     * Returns the value associated with the provided client app property name
     * 
     * @param propertyName
     * @param requestInstance
     * @return associated value or null if either no value exists or on any exception
     */
    public static String getClientAppPropertyValue(String propertyName, HttpServletRequest requestInstance) {
        try {
            return getClientAppPropertyValue(propertyName, ServicesManagerHelper.getServicesManager(requestInstance));
        } catch (Exception e) {
            alert.prepareError("Error occured while accessing Services Manager", e).log();
        }
        return null;
    }

    /**
     * Returns the value associated with the provided client app property name
     * 
     * @param propertyName
     * @param servicesManager
     * @return
     */
    public static String getClientAppPropertyValue(String propertyName, ServicesManager servicesManager) {
        try {
            ConfigurableParametersHelper configurableParametersHelper =
                    servicesManager.getConfigurableParametersHelper();
            Map<String, String> clientProperties = configurableParametersHelper.getAllClientAppProperties();
            String value = ConfigurationParameterUtilities.getValue(propertyName, clientProperties);
            return value;
        } catch (Exception e) {
            alert.prepareError("Error occured while fetching environment configuration parameter. Attempted client app property:"
                    + propertyName, e).log();
        }
        return null;
    }
}
