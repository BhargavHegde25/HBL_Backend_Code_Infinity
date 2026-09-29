package com.temenos.infinity.api.arrangements.prop;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.Property;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

/**
 * <p>
 * Class to load and access values of accounttype.properties file
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class AccountsCountProperties {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static Properties PROPS = null;

    @SuppressWarnings("unused")
    private AccountsCountProperties() {
        // Private Constructor
    }

    public AccountsCountProperties(DataControllerRequest request) {
        PROPS = loadProps(request);
    }

    @SuppressWarnings("unchecked")
    private static Properties loadProps(DataControllerRequest request) {
        Properties properties = new Properties();

        String CACHE_KEY_ACCOUNTTYPE_MAPPING = "accountsCountProperties";
        final int CACHE_TIME = 10 * 60; // 10 minutes
        Map<String, String> accountsCountProperties = new HashMap<>();

        try {
            Object object = ArrangementsUtils.getDataFromCache(request, CACHE_KEY_ACCOUNTTYPE_MAPPING);
            if (null != object) {
                if (StringUtils.isNotBlank(object.toString())) {
                    JSONObject txnTypes = new JSONObject(object.toString());
                    if (txnTypes.length() != 0) {
                    	accountsCountProperties = new Gson().fromJson(txnTypes.toString(), Map.class);
                    }
                }
                if (!accountsCountProperties.isEmpty()) {
                    properties.putAll(accountsCountProperties);
                    return properties;
                }
            }

            JSONObject accountsCount = ArrangementsUtils.getBundleConfigurations(
                    TemenosConstants.ACCOUNT_TYPE_BUNDLE_NAME, TemenosConstants.ACCOUNTS_COUNT_COMPACT_DASHBOARD, request);
            JSONObject configData = new JSONObject();
            if (accountsCount != null) {
                JSONArray configurations = accountsCount.optJSONArray(TemenosConstants.CONFIGURATIONS);
                if (configurations != null && configurations.length() > 0) {
                     configData = configurations.optJSONObject(0);
                   }
            }
            if (configData!=null) {
                try {
                    properties = Property.toProperties(configData);
                    accountsCountProperties = new ObjectMapper().readValue(configData.toString(), HashMap.class);
                } catch (Exception e) {
                    alert.prepareError("Cannot convert string to properties" + e).log();
                }
            }

            ArrangementsUtils.insertDataIntoCache(request, accountsCountProperties, CACHE_KEY_ACCOUNTTYPE_MAPPING, CACHE_TIME);

        } catch (Exception e) {
            alert.prepareError("Unable to fetch account types from bundle configurations").log();
        }

        return properties;
    }

    /**
     * Returns query associated with this key
     * 
     * @param key
     * @return
     */
    public static String getValue(String propertyKey) {
        return PROPS.getProperty(propertyKey);
    }

}
