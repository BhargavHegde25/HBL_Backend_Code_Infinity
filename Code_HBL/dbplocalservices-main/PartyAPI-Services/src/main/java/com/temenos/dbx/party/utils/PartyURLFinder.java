package com.temenos.dbx.party.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;


public class PartyURLFinder {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Properties serviceURLProps = new Properties();

    static {
        try (InputStream inputStream = PartyURLFinder.class.getClassLoader()
                .getResourceAsStream("PartyServiceURLs.properties");) {
            serviceURLProps.load(inputStream);
        } catch (FileNotFoundException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        } catch (IOException e) {
            diagnostic.prepareInfo("error while reading properties file", e).log();
        }
    }
    
    public PartyURLFinder() {
        // TODO Auto-generated constructor stub
    }

    public static String getServiceUrl(String pathKey) {
        if (serviceURLProps.containsKey(pathKey)) {
            return serviceURLProps.getProperty(pathKey).trim();
        }
        return "";
    }
    
    
    public static String getServiceUrl(String pathKey, String party_id) {
        if (serviceURLProps.containsKey(pathKey)) {
            return serviceURLProps.getProperty(pathKey).trim().replace("{party_id}", party_id);
        }
        
        return "";
    }

}