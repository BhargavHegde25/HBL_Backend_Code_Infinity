package com.kony.dbputilities.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;

public class URLFinder {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Properties urlProps = new Properties();

    static {
        try (InputStream inputStream = URLFinder.class.getClassLoader()
                .getResourceAsStream("DBPServiceURLs.properties");) {
            urlProps.load(inputStream);
        } catch (FileNotFoundException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        } catch (IOException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        }
    }

    private URLFinder() {
    }

    public static String getPathUrl(String pathKey) {
        if (urlProps.containsKey(pathKey)) {
            return urlProps.getProperty(pathKey).trim();
        }
        return "";
    }

    public static String getPathUrl(String pathKey, DataControllerRequest dcRequest) {
        return EnvironmentConfigurationsHandler.getValue(pathKey, dcRequest);
    }

    public static String getCompleteUrl(DataControllerRequest dcRequest, String baseUrl, String url) {
        return getPathUrl(baseUrl, dcRequest) + getPathUrl(url);
    }

    public static String getUserSessionUrl(DataControllerRequest dcRequest) {
        return EnvironmentConfigurationsHandler.getValue(URLConstants.GET_USERSESSION_USR_ATTR, dcRequest);
    }

    public static String getCustomerSessionUrl(DataControllerRequest dcRequest) {
        return EnvironmentConfigurationsHandler.getValue(URLConstants.CUSTOMER_SESSION_URL, dcRequest);
    }

    public static String getNUOSessionUrl(DataControllerRequest dcRequest) {
        return EnvironmentConfigurationsHandler.getValue(URLConstants.NUO_SESSION_URL, dcRequest);
    }

    public static String getOTPByPassState(DataControllerRequest dcRequest) {
        return EnvironmentConfigurationsHandler.getValue(URLConstants.OTP_BYPASS, dcRequest);
    }

    public static String getCustomerSessionUrl(FabricRequestManager requestManager) {

        return EnvironmentConfigurationsHandler.getValue(URLConstants.CUSTOMER_SESSION_URL, requestManager);
    }

    public static String getCompleteUrl(FabricRequestManager requestManager, String baseUrl, String url) {

        return getPathUrl(baseUrl, requestManager) + getPathUrl(url);
    }

    public static String getPathUrl(String pathKey, FabricRequestManager requestManager) {

        return EnvironmentConfigurationsHandler.getValue(pathKey, requestManager);
    }

    public static String getCompletePathUrl(String dbpHostUrl, String pathKey) {

        return null;
    }

    public static String getServerRuntimeProperty(String pathKey) {
        return EnvironmentConfigurationsHandler.getValue(pathKey);
    }
    
    public static String getPropertyValue(String propertyKey, String prpertiesFileName) {
        try (InputStream inputStream = URLFinder.class.getClassLoader()
                .getResourceAsStream(prpertiesFileName);) {
            urlProps.load(inputStream);
        } catch (FileNotFoundException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        } catch (IOException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        }
        if (urlProps.containsKey(propertyKey)) {
            return urlProps.getProperty(propertyKey).trim();
        }
        return "";
    }

}