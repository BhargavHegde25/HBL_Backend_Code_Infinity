package com.temenos.infinity.api.chequemanagement.utils;

import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * <p>
 * Class to load and access values of transactiontype.properties file
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class ChequeManagementProperties {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    public ChequeManagementProperties() {
        // Private Constructor
    }

    public static Properties loadProps(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = ChequeManagementProperties.class.getClassLoader()
                .getResourceAsStream(fileName+".properties")) {
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            alert.prepareError("Error while loading properties", e).log();
        }
        return properties; 
    }

}
