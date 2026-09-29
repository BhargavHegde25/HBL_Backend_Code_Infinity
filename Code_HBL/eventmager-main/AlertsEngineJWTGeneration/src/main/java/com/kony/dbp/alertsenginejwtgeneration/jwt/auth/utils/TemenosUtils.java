/**
 * 
 */
package com.kony.dbp.alertsenginejwtgeneration.jwt.auth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kony.dbp.alertsenginejwtgeneration.jwt.auth.AuthConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class TemenosUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

    /*
     * static holder design pattern to create singleton object
     */
    private static class Holder {
        static final TemenosUtils INSTANCE = new TemenosUtils();
    }

    public static TemenosUtils getInstance() {
        return Holder.INSTANCE;
    }

    private TemenosUtils() {

    }
    public static String getProperty(String propFile, String group, String section, String key) throws Exception {

        // Sanity checks
        if (propFile == null || propFile.equalsIgnoreCase(""))
            throw new Exception("Properties file must be provided");
        if (group == null || group.equalsIgnoreCase(""))
            throw new Exception("Group must be provided");
        if (section == null || section.equalsIgnoreCase(""))
            throw new Exception("Section must be provided");
        if (key == null || key.equalsIgnoreCase(""))
            throw new Exception("Key must be provided");

        // First we try to read in the properties file
        Properties properties = getProperties(propFile);

        // Now return the value for the specified group.section.key
        StringBuilder sb = new StringBuilder();
        sb.append(group);
        sb.append(".");
        sb.append(section);
        sb.append(".");
        sb.append(key);
        return properties.getProperty(sb.toString());
    }
    
    private static Properties getProperties(String propFile) throws Exception {

        // Validations
        if (propFile == null || propFile.equalsIgnoreCase(""))
            throw new Exception("Properties file must be provided");

        Properties properties = new Properties();
        InputStream propertiesStream = null;

        // First we try to read in the properties file
        try {
            propertiesStream = CommonUtils.class.getClassLoader().getResourceAsStream(propFile);
            if (propertiesStream != null)
                properties.load(propertiesStream);
        } catch (IOException ex) {
            alert.prepareError("Unable to read the properties file: " + AuthConstants.PROPERTIES_FILE).log();
        } finally {
            if (propertiesStream != null) {
                try {
                    propertiesStream.close();
                } catch (IOException e) {
                    alert.prepareError("Unable to close the properties file inputstream").log();
                }
            }
        }
        return properties;
    }

    public static String getServerEnvironmentProperty(String key, DataControllerRequest request) {
        String serverProperty = null;
        try {
            serverProperty =  EnvironmentConfigurationsHandler.getServerAppProperty(key);            		
        } catch (Exception e) {
        	alert.prepareError("Error while fetching server property " , e ).log();
        	
        }
        return serverProperty;
    }
    public Map<String, Object> convertJsonToMap(JsonObject input) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
        Map<String, Object> inputMap = gson.fromJson(input, type);
        return inputMap;
    }


    public Object buildObjectFromJSONString(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<Object>() {
        }.getType();
        Object object = gson.fromJson(jsonString, type);
        return object;
    }

    
}
