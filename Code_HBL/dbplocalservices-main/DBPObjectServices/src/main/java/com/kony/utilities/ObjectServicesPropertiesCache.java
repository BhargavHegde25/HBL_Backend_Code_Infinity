package com.kony.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class ObjectServicesPropertiesCache {
    private final Properties configprop = new Properties();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private ObjectServicesPropertiesCache() {
        // Private constructor to restrict new instances
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("objectservice.properties");

        try {
            configprop.load(in);
        } catch (IOException e) {
            diagnostic.prepareDebug("Exception occured:" + e).log();
        }
		finally {
	        	if (in!=null) {
	        		try {
	        			in.close();			
	        		}
	        		catch(Exception e)
	        		{
	        			alert.prepareError(e.toString()).log();
	        		}
	        	}
		}
    }

    // Bill Pugh Solution for singleton pattern
    private static class LazyHolder {
        private static final ObjectServicesPropertiesCache instance = new ObjectServicesPropertiesCache();
    }

    public static ObjectServicesPropertiesCache getInstance() {
        return LazyHolder.instance;
    }

    public String getProperty(String key) {
        return configprop.getProperty(key);
    }

    public Set<String> getAllPropertyNames() {
        return configprop.stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return configprop.containsKey(key);
    }

}