package com.temenos.infinity.api.chequemanagement.constants;
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
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementProperties;


public class GetCommandUrl {

	private static Properties urlProps = new Properties();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public void loadURL() {
		String fileName=new String();
		try {
        String CHEQUE_BACKEND = EnvironmentConfigurationsHandler.getValue("CHEQUE_BACKEND");
        if ("SRMS_MOCK".equalsIgnoreCase(CHEQUE_BACKEND)) {
         fileName = "MockChequeManagement.properties";
        }
        else {
         fileName = "T24ChequeManagement.properties";
        }
        try (InputStream inputStream = URLFinder.class.getClassLoader().getResourceAsStream(fileName);) {
            urlProps.load(inputStream);
        } catch (FileNotFoundException e) {
            diagnostic.prepareInfo("error while loading properties file", e).log();
        } 
		}
		catch (IOException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        
        }
    }
	public static final String getURL (String type) {
		try {
		diagnostic.prepareDebug("entering the getURL class ").log();
		    String command=urlProps.getProperty(type);
		   diagnostic.prepareDebug("command name fetched from properties file"+command).log();
	       return command;
		}
		catch(Exception e) {
			alert.prepareError("error in fetching the command name in getURL"+ e).log();
			return null;
		}
	    }
}
