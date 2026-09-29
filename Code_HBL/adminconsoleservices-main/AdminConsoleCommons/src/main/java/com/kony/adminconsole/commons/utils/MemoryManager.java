package com.kony.adminconsole.commons.utils;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.ehcache.ResultCache;

public class MemoryManager {
	
	
	public static final int DEFAULT_CACHE_TIME = 20 * 60;

    private MemoryManager() {

    }

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    /*
	 * cacheTime: The duration for which to cache in seconds
	 */
    
    public static void saveIntoCache(String key, String value, int cacheTime) {

        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }

        if (resultCache != null && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {

            resultCache.insertIntoCache(key, value, cacheTime);
        }

    }

    public static void saveIntoCache(String key, String value) {
    	
        int cacheTime = DEFAULT_CACHE_TIME;
        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }

        if (resultCache != null && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {

            resultCache.insertIntoCache(key, value, cacheTime);
        }

    }

    public static Object getFromCache(String key) {
        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }
        if (resultCache != null && StringUtils.isNotBlank(key)) {

            return resultCache.retrieveFromCache(key);
        }

        return new Object();

    }

    public static void removeFromCache(String key) {
        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }
        if (resultCache != null && StringUtils.isNotBlank(key)) {
            resultCache.removeFromCache(key);
        }

    }

}
