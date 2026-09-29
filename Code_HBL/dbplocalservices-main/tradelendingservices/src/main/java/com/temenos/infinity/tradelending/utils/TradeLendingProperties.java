/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.utils;

import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_PROPERTY;

import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author mrunalini.adepu
 *
 */
public class TradeLendingProperties {

	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Properties properties = new Properties();

    public void loadTypeAndSubType() {
        String fileName = PARAM_PROPERTY + ".properties";
        try (InputStream inputStream = TradeLendingProperties.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (Exception e) {
            alert.prepareError("Error while loading properties", e).log();
        }
    }

    public static String getProperty(String value) {
        return properties.getProperty(value);
    }
}
