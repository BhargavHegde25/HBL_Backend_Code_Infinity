package com.temenos.dbx.party.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class PartyConfiguration {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static Properties configProps = new Properties();

	static {
		try (InputStream inputStream = PartyURLFinder.class.getClassLoader()
				.getResourceAsStream("PartyConfiguration.properties");) {
			configProps.load(inputStream);
		} catch (FileNotFoundException e) {
			diagnostic.prepareInfo("error while reading properties file", e).log();
		} catch (IOException e) {
			diagnostic.prepareInfo("error while reading properties file", e).log();
		}
	}

	public static String getConfiguration(String key) {
		if (configProps.containsKey(key)) {
			return configProps.getProperty(key).trim();
		}
		return "";
	}

}
