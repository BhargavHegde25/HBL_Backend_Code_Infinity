package com.temenos.dbx.product.constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.URLFinder;

/**
 * Contains method to get transactionBackend service URL values
 * 
 * @author 
 *
 */
public class PayeeVerificationBackendURLFinder {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static Properties urlCountryProps = new Properties();
	private static Properties urlPaymentTypeProps = new Properties();

	/*
	 * This method loads the properties based on the backend
	 */
	public void loadTransactionBackendURLs() {
		// Load country properties
		String fileName = "config/PayeeVerificationCountryBackendURLs.properties";
		try (InputStream inputStream = URLFinder.class.getClassLoader().getResourceAsStream(fileName);) {
			urlCountryProps.load(inputStream);
		} catch (FileNotFoundException e) {
			diagnostic.prepareInfo("error while reading properties file", e).log();
		} catch (IOException e) {
			diagnostic.prepareInfo("error while reading properties file", e).log();
		}
		// Load payment type properties
		fileName = "config/PayeeVerificationPaymentTypeBackendURLs.properties";
		try (InputStream inputStream = URLFinder.class.getClassLoader().getResourceAsStream(fileName);) {
			urlPaymentTypeProps.load(inputStream);
		} catch (FileNotFoundException e) {
			diagnostic.prepareInfo("error while reading properties file", e).log();
		} catch (IOException e) {
			diagnostic.prepareInfo("error while reading properties file", e).log();
		}
	}

	public static final String getCountryBackendURL(String countryId) {
		if (urlCountryProps.containsKey(countryId)) {
			return urlCountryProps.getProperty(countryId).trim();
		} else
			return urlCountryProps.getProperty("default").trim();
	}

	public static final String getPaymentTypeBackendURL(String paymentType) {
		if (urlPaymentTypeProps.containsKey(paymentType)) {
			return urlPaymentTypeProps.getProperty(paymentType).trim();
		} else
			return urlPaymentTypeProps.getProperty("default").trim();
	}
}