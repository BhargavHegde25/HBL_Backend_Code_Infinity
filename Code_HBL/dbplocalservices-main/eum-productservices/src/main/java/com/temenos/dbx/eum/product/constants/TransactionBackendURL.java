package com.temenos.dbx.eum.product.constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.URLFinder;
import com.temenos.dbx.eum.product.constants.TransactionBackendURL;
/**
 * Contains method to get transactionBackend service URL values
 * @author kh2624
 *
 */
public class TransactionBackendURL{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Properties urlProps = new Properties();

    static {
        try (InputStream inputStream = URLFinder.class.getClassLoader()
                .getResourceAsStream("TransactionBackendURLs.properties");) {
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