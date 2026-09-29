/**
 * 
 */
package com.infinity.dbx.dbp.jwt.auth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.ehcache.ResultCacheImpl;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class TemenosUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    public Map<String, String> transactionTypesMap = new HashMap<String, String>();
    public Map<String, String> accountTypesMap = new HashMap<String, String>();

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
    
    
    /**
     * Get a property value for the specified key from the Kony Digital Banking properties file
     * 
     * @param String
     *            Property file to lookup
     * @param String
     *            Group to lookup
     * @param String
     *            Section to lookup
     * @param String
     *            Key to lookup
     * @return String Property value
     **/
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
    
    /**
     * Get properties from the specified properties file
     * 
     * @param String
     *            Property file to lookup
     * @return String Property value
     **/
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

    /**
     * @author Gopinath Vaddepally KH2453
     * @param key
     * @param request
     * @return environment property
     */
    public static String getServerEnvironmentProperty(String key, DataControllerRequest request) {
        String serverProperty = null;
        try {
            serverProperty = request.getServicesManager().getConfigurableParametersHelper().getServerProperty(key);
        } catch (Exception e) {
            
            alert.prepareError("error occured ",e).log();
        }
        return serverProperty;
    }
    
    /**
     * @author Gopinath Vaddepally KH2453
     * @param key
     * @param request
     * @return environment property
     */
    public static String getServerEnvironmentProperty(String key) {
        String serverProperty = null;
        try {
            serverProperty = ServicesManagerHelper.getServicesManager().getConfigurableParametersHelper().getServerProperty(key);
        } catch (Exception e) {
            
            alert.prepareError("error occured ",e).log();
        }
        return serverProperty;
    }
    
    /**
     * with DCR
     * 
     * @param key
     * @param value
     * @param request
     * @throws Exception
     */
    public <T> void insertIntoSession(String key, T value, DataControllerRequest request) {
        try {
            ServicesManager servicesManager = request.getServicesManager();
            /*
             * reading reporting params
             */
            JSONObject reportingParams = getReportingParams(request);
            insertDataIntoCache(servicesManager, reportingParams, key, value);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
        }
    }
    
    /**
     * with DCR
     * 
     * @param key
     * @param value
     * @param request
     * @throws Exception
     */
    public <T> void insertIntoSession(String key, T value, Map<String, Object> headerMap) {
        try {
            ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
            /*
             * reading reporting params
             */
            JSONObject reportingParams = getReportingParams(headerMap);
            insertDataIntoCache(servicesManager, reportingParams, key, value);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
        }
    }

    /**
     * overloaded with FabricRequestManager
     * 
     * @param key
     * @param value
     * @param requestManager
     * @throws Exception
     */
    public <T> void insertIntoSession(String key, T value, FabricRequestManager requestManager) {
        try {
            ServicesManager servicesManager = requestManager.getServicesManager();
            JSONObject reportingParams = getReportingParams(requestManager);
            insertDataIntoCache(servicesManager, reportingParams, key, value);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
        }
    }

    /**
     * inserts data into cache
     * 
     * @param servicesManager
     * @param reportingParams
     * @param key
     * @param value
     */
    private <T> void insertDataIntoCache(ServicesManager servicesManager, JSONObject reportingParams, String key,
            T value) {
        HashMap<String, T> sessionCacheMap;
        String cacheKey = "";
        /*
         * check whether already the cache exists
         */
        String userId = servicesManager.getIdentityHandler().getUserId();
        /*
         * checking the flow - if userid exists post login else pre login
         */
        if (StringUtils.isNotBlank(userId)) {
            cacheKey = userId;
        }
        cacheKey = cacheKey+"_"+key;
        ResultCache resultCache = null;
        resultCache = servicesManager.getResultCache();

        /*Object cachedData = resultCache.retrieveFromCache(cacheKey);
        if (cachedData == null) {
            sessionCacheMap = new HashMap<>();
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            sessionCacheMap = gson.fromJson((String) cachedData, type);
        }
        sessionCacheMap.put(key, value);
        Gson gson = new Gson();
        String jsonString = gson.toJson(sessionCacheMap);*/
        resultCache.insertIntoCache(cacheKey, value, 3600);
    }

    /**
     * with DCR
     * 
     * @param key
     * @param dcRequest
     * @return
     * @throws Exception
     */
    public Object retreiveFromSession(String key, DataControllerRequest dcRequest) {
        try {
            ServicesManager servicesManager = dcRequest.getServicesManager();
            JSONObject reportingParams = getReportingParams(dcRequest);
            return retriveDataFromCache(servicesManager, reportingParams, key);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
            return null;
        }

    }
    
    /**
     * with DCR
     * 
     * @param key
     * @param dcRequest
     * @return
     * @throws Exception
     */
    public Object retreiveFromSession(String key, Map<String, Object> headerMap) {
        try {
            ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
            JSONObject reportingParams = getReportingParams(headerMap);
            return retriveDataFromCache(servicesManager, reportingParams, key);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
            return null;
        }

    }

    /**
     * overloaded method with FabricRequestManager
     * 
     * @param key
     * @param fabricRequestManager
     * @return
     * @throws Exception
     */
    public Object retreiveFromSession(String key, FabricRequestManager fabricRequestManager) {
        try {
            ServicesManager servicesManager = fabricRequestManager.getServicesManager();
            JSONObject reportingParams = getReportingParams(fabricRequestManager);
            Object result = null;
            return retriveDataFromCache(servicesManager, reportingParams, key);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
            return null;
        }

    }

    /**
     * retrieves data from cache
     * 
     * @param servicesManager
     * @param reportingParams
     * @param result
     * @param key
     */
    private Object retriveDataFromCache(ServicesManager servicesManager, JSONObject reportingParams, String key) {
        Object result = null;
        String cacheKey = "";
        String userId = servicesManager.getIdentityHandler().getUserId();
        if (StringUtils.isNotBlank(userId)) {
            cacheKey = userId;
        }
        //cacheKey = cacheKey + reportingParams.getString("did");
        cacheKey = cacheKey+"_"+key;
        ResultCache resultCache = servicesManager.getResultCache();
        String valueInCache = "";
        try {
            valueInCache = resultCache.retrieveFromCache(cacheKey) != null
                    ? (String) resultCache.retrieveFromCache(cacheKey)
                    : null;
        } catch (Exception e) {
            try {
                valueInCache = (String) ServicesManagerHelper.getServicesManager().getResultCache()
                        .retrieveFromCache(cacheKey);
            } catch (MiddlewareException e1) {
                alert.prepareError(e1.toString()).log();
                valueInCache = (String) ResultCacheImpl.getInstance().retrieveFromCache(cacheKey);
            }
        }
        /*if (StringUtils.isNotBlank(valueInCache)) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            Map<String, Object> resultMap = gson.fromJson(valueInCache, type);
            if (resultMap != null) {
                if (resultMap.get(key) != null) {
                    result = resultMap.get(key);
                }
            }
        }*/
        result = valueInCache;
        return result;
    }

    /**
     * with DCR
     * 
     * @param dcRequest
     * @returnJSONObject of reporting params
     */
    public JSONObject getReportingParams(DataControllerRequest dcRequest) {
        String encodedReportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
        JSONObject reportingParamsJson = null;
        if (StringUtils.isNotBlank(encodedReportingParams)) {
            try {
                reportingParamsJson = new JSONObject(URLDecoder.decode(encodedReportingParams, "utf-8"));
            } catch (Exception e) {
                alert.prepareError("error reading reporting params " + e.getLocalizedMessage(), e).log();
            }
        }
        return reportingParamsJson;
    }
    
    /**
     * with DCR
     * 
     * @param 
     * @returnJSONObject of reporting params
     */
    public JSONObject getReportingParams(Map<String, Object> headerMap) {
        String encodedReportingParams = (String) headerMap.get("X-Kony-ReportingParams");
        JSONObject reportingParamsJson = null;
        if (StringUtils.isNotBlank(encodedReportingParams)) {
            try {
                reportingParamsJson = new JSONObject(URLDecoder.decode(encodedReportingParams, "utf-8"));
            } catch (Exception e) {
                alert.prepareError("error reading reporting params " + e.getLocalizedMessage(), e).log();
            }
        }
        return reportingParamsJson;
    }

    /**
     * overloaded method take FabricRequestManager
     * 
     * @param fabrequestManager
     * @return JSONObject of reporting params
     */
    public JSONObject getReportingParams(FabricRequestManager fabrequestManager) {
        String encodedReportingParams = fabrequestManager.getHeadersHandler().getHeader("X-Kony-ReportingParams");
        JSONObject reportingParamsJson = null;
        if (StringUtils.isNotBlank(encodedReportingParams)) {
            try {
                reportingParamsJson = new JSONObject(URLDecoder.decode(encodedReportingParams, "utf-8"));
            } catch (Exception e) {
                alert.prepareError("error reading reporting params " + e.getLocalizedMessage(), e).log();
            }
        }
        return reportingParamsJson;
    }

    public void invalidateSession(DataControllerRequest request) {
        try {
            ServicesManager servicesManager = request.getServicesManager();
            ResultCache resultCache = servicesManager.getResultCache();
            JSONObject reportingParams = getReportingParams(request);
            String cacheKey = getCacheKey(servicesManager, reportingParams);
            resultCache.removeFromCache(cacheKey);
        } catch (Exception e) {
            alert.prepareError("error invalidating session -" + e.getLocalizedMessage(), e).log();
        }
    }

    public String getCacheKey(ServicesManager servicesManager, JSONObject reportingParams) {
        String cacheKey = "";
        String userId = servicesManager.getIdentityHandler().getUserId();
        if (StringUtils.isNotBlank(userId)) {
            cacheKey = userId;
        }
        cacheKey = cacheKey + reportingParams.getString("did");
        return cacheKey;
    }


    public Map<String, Object> convertJsonToMap(JsonObject input) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
        Map<String, Object> inputMap = gson.fromJson(input, type);
        return inputMap;
    }


    public <T> void insertDataIntoCache(DataControllerRequest request, T value, String key, int time) {
        try {
            ServicesManager servicesManager = request.getServicesManager();
            ResultCache resultCache = servicesManager.getResultCache();
            Gson gson = new Gson();
            String jsonString = gson.toJson(value);
            resultCache.insertIntoCache(key, jsonString, time);
        } catch (Exception e) {
            alert.prepareError(e.getLocalizedMessage(), e).log();
        }
    }
    
    public <T> void insertDataIntoCache(T value, String key, int time) {
        try {
            ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
            ResultCache resultCache = servicesManager.getResultCache();
            Gson gson = new Gson();
            String jsonString = gson.toJson(value);
            resultCache.insertIntoCache(key, jsonString, time);
        } catch (Exception e) {
            alert.prepareError(e.getLocalizedMessage(), e).log();
        }
    }

    public Object getDataFromCache(DataControllerRequest request, String key) {
        Object cachedData = null;
        try {
            ServicesManager servicesManager = request.getServicesManager();
            ResultCache resultCache = servicesManager.getResultCache();
            cachedData = resultCache.retrieveFromCache(key);
        } catch (Exception e) {
            alert.prepareError(e.getLocalizedMessage(), e).log();
        }
        return cachedData;
    }
    
    public Object getDataFromCache(String key) {
        Object cachedData = null;
        try {
            ServicesManager servicesManager = ServicesManagerHelper.getServicesManager();
            ResultCache resultCache = servicesManager.getResultCache();
            cachedData = resultCache.retrieveFromCache(key);
        } catch (Exception e) {
            alert.prepareError(e.getLocalizedMessage(), e).log();
        }
        return cachedData;
    }

    public Object buildObjectFromJSONString(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<Object>() {
        }.getType();
        Object object = gson.fromJson(jsonString, type);
        return object;
    }

    public String getDateFormat(String Date, String Pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Pattern);
        String date = simpleDateFormat.format(new Date(Date));
        return date;
    }

    public static boolean isTokenExpired(String token) {
		boolean isTokenExpired = false;
		try {
			JWT parse = JWTParser.parse(token);
			Date expirationTime = parse.getJWTClaimsSet().getExpirationTime();
			if (new Date().after(expirationTime)) {
				isTokenExpired = true;
			}
		} catch (ParseException e) {
			
		}
		return isTokenExpired;
	}
	
	public static String getJWTUniqueId(String token) {
		String uniqueId = "";
		if (token != null) {
			try {
				JWT jwt = JWTParser.parse(token);
				uniqueId = jwt.getJWTClaimsSet().getJWTID();
			} catch (Exception e) {
				uniqueId = UUID.randomUUID().toString();
			}
		}
		return uniqueId;
	}
    
}
