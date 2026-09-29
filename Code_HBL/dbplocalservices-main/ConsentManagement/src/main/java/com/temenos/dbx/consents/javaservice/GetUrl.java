package com.temenos.dbx.consents.javaservice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.URLFinder;

public class GetUrl {

	private static Properties urlProps = new Properties();
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public void loadProperties() {
		String fileName = new String();
		try {
			String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");
			if ("STUB".equalsIgnoreCase(PAYMENT_BACKEND)) {
				fileName = "MockProperties.properties";
			} else {
				fileName = "T24Properties.properties";
			}
			try (InputStream inputStream = URLFinder.class.getClassLoader().getResourceAsStream(fileName);) {
				urlProps.load(inputStream);
			} catch (FileNotFoundException e) {
				diagnostic.prepareInfo("error while loading properties file", e).log();
			}
		} catch (IOException e) {
			diagnostic.prepareInfo("error while reading properties file", e).log();

		}
	}

	public static final String getURL(String type) {
		String command = urlProps.getProperty(type);
		return command;
	}
}
