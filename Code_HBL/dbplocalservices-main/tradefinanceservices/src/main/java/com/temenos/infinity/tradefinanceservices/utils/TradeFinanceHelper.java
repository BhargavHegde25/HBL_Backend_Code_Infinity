/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.utils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.InputStream;
import java.util.Properties;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_PROPERTY;

public class TradeFinanceHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Properties properties = new Properties();

    public void loadTypeAndSubType() {
        String fileName = PARAM_PROPERTY + ".properties";
        try (InputStream inputStream = TradeFinanceHelper.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (Exception e) {
            alert.prepareError("Error while loading properties", e).log();
        }
    }

    public static String getProperty(String value) {
        return properties.getProperty(value);
    }
}
