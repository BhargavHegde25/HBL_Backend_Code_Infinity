package com.kony.dbputilities.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class IntegrationTemplateURLFinder {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Properties urlProps = new Properties();
    public static boolean isIntegrated = false;

    static {
        try (InputStream inputStream = URLFinder.class.getClassLoader()
                .getResourceAsStream("DBXIntegrationURLs.properties");) {
            urlProps.load(inputStream);
            if (urlProps.size() > 0
                    && "true".equalsIgnoreCase(urlProps.getProperty(DBPUtilitiesConstants.IS_INTEGRATED))) {
                isIntegrated = true;
            } else {
                isIntegrated = false;
            }

        } catch (FileNotFoundException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        } catch (IOException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        } catch (Exception e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        }

    }

    public static final String getBackendURL(String serviceId) {
        if (urlProps.containsKey(serviceId)) {
            return urlProps.getProperty(serviceId).trim();
        }
        return "";
    }

    public static String getAccountTypeName(String productId) {

        String accountTypePropertyFilename =
                IntegrationTemplateURLFinder.getBackendURL(DBPUtilitiesConstants.ACCOUNTTYPE_PROPERTIES_FILE_NAME);
        return URLFinder.getPropertyValue(productId,
                accountTypePropertyFilename);
    }
}