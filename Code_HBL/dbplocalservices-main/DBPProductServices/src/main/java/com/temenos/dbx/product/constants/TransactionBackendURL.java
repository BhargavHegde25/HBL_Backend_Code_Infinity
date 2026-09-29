package com.temenos.dbx.product.constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.URLFinder;
/**
 * Contains method to get transactionBackend service URL values
 * @author kh2624
 *
 */
public class TransactionBackendURL{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Properties urlProps = new Properties();
    private static String PAYMENT_BACKEND_MOCK = "MOCK";
    
    /*
     * This method loads the properties based on the backend
     */
    public void loadTransactionBackendURLs() {
        String Payment_Backend = EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");
        if (StringUtils.isBlank(Payment_Backend)|| Payment_Backend.equalsIgnoreCase("STUB"))
            Payment_Backend = PAYMENT_BACKEND_MOCK;
        String fileName = "config/" + Payment_Backend.toUpperCase() + "_TransactionBackendURLs.properties";
        try (InputStream inputStream = URLFinder.class.getClassLoader().getResourceAsStream(fileName);) {
            urlProps.load(inputStream);
        } catch (FileNotFoundException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        } catch (IOException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        }
    }
/**
 * method to get serviceId for given service key
 * @author kh2624
 * @param serviceKey
 * @return serviceId
 *
 */
public static final String getBackendURL (String serviceId) {
	  if (urlProps.containsKey(serviceId)) {
          return urlProps.getProperty(serviceId).trim();
      }
		return serviceId;
    }
}