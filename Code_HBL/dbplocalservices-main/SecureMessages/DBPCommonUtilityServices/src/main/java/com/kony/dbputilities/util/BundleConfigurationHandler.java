package com.kony.dbputilities.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.konylabs.middleware.controller.DataControllerRequest;

public class BundleConfigurationHandler {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /**
     * Bundle name constants
     */

    public static final String BUDLENAME_C360 = "C360";
    public static final String BUDLENAME_LOANS = "Loans";
    public static final String BUDLENAME_DBP = "DBP";
    public static final String BUDLENAME_NUO = "NUO";

    /**
     * Bundle Id constants
     */
    public static final String BUNDLEID_DBP = "DBP_CONFIG_BUNDLE";
    public static final String BUNDLEID_C360 = "C360_CONFIG_BUNDLE";
    /**
     * C360 Bundle parameter constants
     */

    public static final String ACTIVATIONCODE_EXPIRYTIME = "ACTIVATIONCODE_EXPIRYTIME";
    public static final String ACTIVATIONCODE_LENGTH = "ACTIVATIONCODE_LENGTH";
    public static final String ACTIVATIONCODE_VALIDATIONATTEMPTS = "ACTIVATIONCODE_VALIDATIONATTEMPTS";
    public static final String CAPTCHA_LENGTH = "CAPTCHA_LENGTH";
    public static final String ORGANIZATION_ID_LENGTH = "ORGANIZATION_ID_LENGTH";
    public static final String CUSTOMER_ID_LENGTH = "CUSTOMER_ID_LENGTH";
    public static final String USERNAME_LENGTH = "USERNAME_LENGTH";
    
    public static final String AUTO_SYNC_ACCOUNTS = "AUTO_SYNC_ACCOUNTS";
    public static final String BUSINESS_SECTORID_LIST = "BUSINESS_SECTORID_LIST";

    /**
     * NUO Bundle parameter constants
     */
    public static final String PROSPECT_EXPIRY_DATE = "PROSPECT_EXPIRY_DATE";

    public static final String DEFAULT_RETAIL_SERVICE_ID = "DEFAULT_RETAIL_SERVICE_ID";
    public static final String DEFAULT_BUSINESS_SERVICE_ID = "DEFAULT_BUSINESS_SERVICE_ID";
    public static final String AUTO_SYNC_RETAIL_ACCOUNTS = "AUTO_SYNC_RETAIL_ACCOUNTS";
    public static final String AUTO_SYNC_BUSINESS_ACCOUNTS = "AUTO_SYNC_BUSINESS_ACCOUNTS";

	public static final String DEFAULT_PROSPECT_GROUP = "DEFAULT_PROSPECT_GROUP";
    
    

    /**
     * 
     * @param bundleId
     * @param key
     * @param headersMap
     * @return
     */
    public static String fetchConfigurationValueOnKey(String bundleId, String key, Map<String, Object> headersMap) {
        String value = "";
        StringBuilder sb = new StringBuilder();
        sb.append("bundle_id").append(DBPUtilitiesConstants.EQUAL).append(bundleId);
        sb.append(DBPUtilitiesConstants.AND);
        sb.append("config_key").append(DBPUtilitiesConstants.EQUAL).append(key);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, URLConstants.CONFIGURATIONS_GET);
        if (JSONUtil.hasKey(response, "configurations") && response.get("configurations").isJsonArray() &&
                response.get("configurations").getAsJsonArray().size() > 0) {
            value = JSONUtil.getString(response.get("configurations").getAsJsonArray().get(0).getAsJsonObject(),
                    "config_value");
        }
        return value;
    }

    /**
     * Fetches the specified bundle name configurations
     * 
     * @param bundleName
     * @param dcRequest
     * @return
     */
    private static Map<String, String> fetchConfigurations(String bundleName, DataControllerRequest dcRequest) {
        Map<String, String> configurations = new HashMap<>();
        try {
            HashMap<String, String> input = new HashMap<>();
            input.put("bundle_name", bundleName);
            JsonObject json = AdminUtil.invokeAPIAndGetJson(input, URLConstants.ADMIN_CONFIGURATIONS, dcRequest);
            if (JSONUtil.hasKey(json, "Configurations")) {
                for (JsonElement jsonElement : json.get("Configurations").getAsJsonArray()) {
                    configurations.put(JSONUtil.getString(jsonElement.getAsJsonObject(), "key"),
                            JSONUtil.getString(jsonElement.getAsJsonObject(), "value"));
                }
            }
        } catch (HttpCallException e) {
            alert.prepareError("Exception occured while fetching Admin configurations").log();
           
        }

        return configurations;
    }

    /**
     * Fetches the specified bundle name configurations
     * 
     * @param bundleName
     * @param dcRequest
     * @return
     */
    private static String fetchConfigurations(String bundleName, String ConfigKey,
            Map<String, Object> headersMap) {
        Map<String, String> configurations = new HashMap<>();
        Map<String, String> input = new HashMap<>();
        input.put("bundle_name", bundleName);
        headersMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        JsonObject json = new JsonObject();
        try {
            json = AdminUtil.invokeAPIAndGetJson(input, URLConstants.ADMIN_CONFIGURATIONS,
                    "");
        } catch (HttpCallException e) {
        	alert.prepareError(e).log();
        }
        if (JSONUtil.hasKey(json, "Configurations")) {
            for (JsonElement jsonElement : json.get("Configurations").getAsJsonArray()) {
                configurations.put(JSONUtil.getString(jsonElement.getAsJsonObject(), "key"),
                        JSONUtil.getString(jsonElement.getAsJsonObject(), "value"));
            }
        }
        return configurations.get(ConfigKey);
    }

    public static String fetchBundleConfigurations(String bundleName, String ConfigKey,
            Map<String, Object> headersMap) {
        if (BUDLENAME_C360.equalsIgnoreCase(bundleName))
            return fetchConfigurations(BUDLENAME_C360, ConfigKey, headersMap);
        if (BUDLENAME_NUO.equalsIgnoreCase(bundleName))
            return fetchConfigurations(BUDLENAME_NUO, ConfigKey, headersMap);
        if (BUDLENAME_DBP.equalsIgnoreCase(bundleName))
            return fetchConfigurations(BUDLENAME_DBP, ConfigKey, headersMap);
        if (BUDLENAME_LOANS.equalsIgnoreCase(bundleName))
            return fetchConfigurations(BUDLENAME_LOANS, ConfigKey, headersMap);
        return null;
    }

    public static Map<String, String> fetchBundleConfigurations(String bundleName, DataControllerRequest dcRequest) {
        if (BUDLENAME_C360.equalsIgnoreCase(bundleName))
            return fetchC360Configurations(dcRequest);
        if (BUDLENAME_NUO.equalsIgnoreCase(bundleName))
            return fetchNUOConfigurations(dcRequest);
        if (BUDLENAME_DBP.equalsIgnoreCase(bundleName))
            return fetchDBPConfigurations(dcRequest);
        if (BUDLENAME_LOANS.equalsIgnoreCase(bundleName))
            return fetchLoansConfigurations(dcRequest);
        return null;
    }

    private static Map<String, String> fetchC360Configurations(DataControllerRequest dcRequest) {
        return fetchConfigurations(BUDLENAME_C360, dcRequest);
    }

    private static Map<String, String> fetchNUOConfigurations(DataControllerRequest dcRequest) {
        return fetchConfigurations(BUDLENAME_NUO, dcRequest);
    }

    private static Map<String, String> fetchDBPConfigurations(DataControllerRequest dcRequest) {
        return fetchConfigurations(BUDLENAME_DBP, dcRequest);
    }

    private static Map<String, String> fetchLoansConfigurations(DataControllerRequest dcRequest) {
        return fetchConfigurations(BUDLENAME_LOANS, dcRequest);
    }

    private static boolean isConfigurationValid(Map<String, String> map) {
        return (map != null && !map.isEmpty()) ? true : false;
    }
}
