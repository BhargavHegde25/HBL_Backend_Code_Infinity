package com.kony.dbputilities.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
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
    public static final String BUNDLEID_SME = "NUO_CONFIG_SME_BUNDLE";
    public static final String BUNDLEID_RETAIL = "NUO_CONFIG_BUNDLE";
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
    public static final String DEFAULT_RETAIL_SERVICE_ID = "DEFAULT_RETAIL_SERVICE_ID";
    
    public static final String AUTO_SYNC_ACCOUNTS = "AUTO_SYNC_ACCOUNTS";
    public static final String BUSINESS_SECTORID_LIST = "BUSINESS_SECTORID_LIST";
    public static final String SME_CUSTOMER_CONFIG = "SME_T24_CUSTOMER_CONFIG";
    public static final String RETAIL_CUSTOMER_CONFIG = "RETAIL_T24_CUSTOMER_CONFIG";

    /**
     * NUO Bundle parameter constants
     */
    public static final String PROSPECT_EXPIRY_DATE = "PROSPECT_EXPIRY_DATE";

    public static final String DEFAULT_BUSINESS_SERVICE_ID = "DEFAULT_BUSINESS_SERVICE_ID";
    public static final String AUTO_SYNC_RETAIL_ACCOUNTS = "AUTO_SYNC_RETAIL_ACCOUNTS";
    public static final String AUTO_SYNC_BUSINESS_ACCOUNTS = "AUTO_SYNC_BUSINESS_ACCOUNTS";

	public static final String DEFAULT_PROSPECT_GROUP = "DEFAULT_PROSPECT_GROUP";
    
	public static final String CONTRACT_JOB_SCHEDULING_CONFIG = "CONTRACT_JOB_SCHEDULING_CONFIG";
	public static final String T24_CUSTOMER_ROLE_MAPPING = "T24_CUSTOMER_ROLE_MAPPING";
	public static final String CUSTOMER_ROLE_MAPPING = "_CUSTOMER_ROLE_MAPPING";
    

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
            //JsonObject json = AdminUtil.invokeAPIAndGetJson(input, URLConstants.ADMIN_CONFIGURATIONS, dcRequest);
            
            JsonObject json = ServiceCallHelper.invokeServiceAndGetJson(dcRequest,
                    HelperMethods.convertToObjectMap(input), dcRequest.getHeaderMap(), URLConstants.ADMIN_CONFIGURATIONS);
            
            if (JSONUtil.hasKey(json, "Configurations")) {
                for (JsonElement jsonElement : json.get("Configurations").getAsJsonArray()) {
                    configurations.put(JSONUtil.getString(jsonElement.getAsJsonObject(), "key"),
                            JSONUtil.getString(jsonElement.getAsJsonObject(), "value"));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching Admin configurations",e).log();
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
           /* json = AdminUtil.invokeAPIAndGetJson(input, URLConstants.ADMIN_CONFIGURATIONS,
                    "");*/
        	Map<String, Object> headerMap = new HashMap<>();
        	json = ServiceCallHelper.invokeServiceAndGetJson(HelperMethods.convertToObjectMap(input),
                    headerMap, URLConstants.ADMIN_CONFIGURATIONS, "");
        } catch (Exception e) {
        	 alert.prepareError(e.toString()).log();
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
    
    
    public static JSONObject getBundleConfigurations(String bundleName, String pendingLimit, String postedLimit, String prePage,
            DataControllerRequest request) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        String serviceName = "dbpRbLocalServicesdb";
        String operationName ="dbxdb_configurations_get";
        StringBuilder filter = new StringBuilder();

        if (StringUtils.isNotBlank(bundleName)) {
            filter.append("bundle_id" + " eq '" + bundleName + "'");
        }
        if(StringUtils.isNoneBlank(pendingLimit)) {
        	filter.append(" and " + "config_key" + " eq '" + pendingLimit + "'");
        }
        if (StringUtils.isNotBlank(postedLimit)) {
            filter.append(" and " + "config_key" + " eq '" + postedLimit + "'");
        }
        if (StringUtils.isNotBlank(prePage)) {
            filter.append(" and " + "config_key" + " eq '" + prePage + "'");
        }
        String select = "config_value,isPreLoginConfiguration,description,softdeleteflag,target,config_key,bundle_id,config_type,configuration_id";
        params.put("$filter", filter.toString());
        params.put("$select", select);
        
        // Authenticate C360
        /*String AuthToken = SpotlightLogin(request);

        if (StringUtils.isBlank(AuthToken)) {
            alert.prepareError("C360 authentication failed. Aborting get configurations").log();
            return new JSONObject();
        }
        headerMap.put("backendToken", AuthToken);
        headerMap.put("X-Kony-Authorization",
                request.getHeader("X-Kony-Authorization"));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()); 
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");*/
        JSONObject configurations = new JSONObject();
        try {
            configurations = invokeServiceAndGetJson(request, params, headerMap, serviceName, operationName, null);
        } catch (Exception e) {
            alert.prepareError("Failed to fetch Bundle Configurations:" + e).log();
            return new JSONObject();
        }
        return configurations;
    }
    
    public static String SpotlightLogin(DataControllerRequest request) {
        Map<String, Object> headersMap = new HashMap<>();
        String api_Access_Token = "";
        try {
            api_Access_Token = ServerConfigurations.DBP_AC_ACCESS_TOKEN.getValue();
        } catch (Exception e) {
            alert.prepareError("Couldnt parse DBP_AC_ACCESS_TOKEN from environment "+ e.toString()).log();
        }
        String ac_app_key = "";
        try {
            ac_app_key = ServerConfigurations.DBP_AC_APP_KEY.getValue();
        } catch (Exception e) {
            alert.prepareError("Couldnt parse DBP_AC_APP_KEY from environment "+ e.toString()).log();
        }
        String ac_app_secret_key = "";
        try {
            ac_app_secret_key = ServerConfigurations.DBP_AC_APP_SECRET.getValue();
        } catch (Exception e) {
            alert.prepareError("Couldnt parse DBP_AC_APP_SECRET from environment "+ e.toString()).log();
        }
        headersMap.put("X-Kony-AC-API-Access-Token", api_Access_Token);
        headersMap.put("AC-X-Kony-App-Key", ac_app_key);
        headersMap.put("AC-X-Kony-App-Secret", ac_app_secret_key);
        JSONObject result = new JSONObject();

        try {
            result = invokeServiceAndGetJson(request, null, headersMap, "C360APILogin",
            		"login", null);
        } catch (Exception e) {
            alert.prepareError("Failed to fetch API Auth Token from Customer360. Service Response:" + e).log();
        }
        String claimsToken = "";
        if (result != null && result.has("claims_token"))
            claimsToken = result.get("claims_token").toString();
        if (StringUtils.isBlank(claimsToken)) {
            alert.prepareError("C360 Auth Token Null" + claimsToken).log();
            return StringUtils.EMPTY;
        }
        return claimsToken;
    }
    
    
    @SuppressWarnings("deprecation")
    public static JSONObject invokeServiceAndGetJson(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String serviceName, String operationName, String objectName) {

        try {
            String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName,
                    operationName, inputParams, headerParams, dcRequest);
            if (StringUtils.isNotBlank(responseString)) {
                return new JSONObject(responseString);
            }
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + operationName, e).log();
        }
        return getExceptionMsgAsJson(serviceName, operationName);
    }
    
    private static JSONObject getExceptionMsgAsJson(String serviceName, String Opertaion) {
        JSONObject errResponse = new JSONObject();
        StringBuilder message = new StringBuilder();
        message.append("Exception occured while invoking service with [ServiceId_ObjectId_OperationId] [")
                .append(serviceName + "_" + Opertaion).append("]");
        errResponse.put("errmsg", message.toString());
        return errResponse;
    }
}
