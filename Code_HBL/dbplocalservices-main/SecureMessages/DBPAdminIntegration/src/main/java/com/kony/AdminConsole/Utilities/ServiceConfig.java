package com.kony.AdminConsole.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;

public class ServiceConfig {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static Properties props = loadProps();

    private static Properties loadProps() {
        InputStream serviceConfigInputStream = ServiceConfig.class.getClassLoader()
                .getResourceAsStream("ServiceConfig.properties");
        props = new Properties();
        try {
            props.load(serviceConfigInputStream);
        } catch (IOException e) {
            alert.prepareError(e.getMessage()).log();
        }
		finally {
        	if (serviceConfigInputStream!=null) {
        		try {
        			serviceConfigInputStream.close();
        		}
        		catch(Exception e)
        		{
        			alert.prepareError(e).log();
        		}
        	}
        }
        return props;
    }

    private ServiceConfig() {
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public static void setValue(String key, String value) {
        props.setProperty(key, value);
    }

    public static String getValueFromRunTime(String pathKey, DataControllerRequest dcRequest) {
        return EnvironmentConfigurationsHandler.getValue(pathKey, dcRequest);
    }

    public static String getValueFromRunTime(String pathKey, FabricRequestManager requestManager) {
        return EnvironmentConfigurationsHandler.getValue(pathKey, requestManager);
    }
}
