package com.temenos.infinity.tradefinanceservices.utils;

import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * <p>
 * Class to load and access values of transactiontype.properties file
 * </p>
 * 
 *
 */
public class TradeFinanceProperties {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public TradeFinanceProperties() {
        // Private Constructor
    }

    public static Properties loadProps(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = TradeFinanceProperties.class.getClassLoader()
                .getResourceAsStream(fileName+".properties")) {
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            alert.prepareError("Error while loading properties", e).log();
        }
        return properties; 
    }

}
