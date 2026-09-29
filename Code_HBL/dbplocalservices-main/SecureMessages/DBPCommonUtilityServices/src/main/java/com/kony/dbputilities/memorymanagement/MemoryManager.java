package com.kony.dbputilities.memorymanagement;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.ehcache.ResultCache;

public class MemoryManager {

    private MemoryManager() {

    }

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

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
    
    public static void saveIntoCache(String key, byte[] value, int cacheTime) {

        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }

        if (resultCache != null && StringUtils.isNotBlank(key) && value!=null) {
            resultCache.insertIntoCache(key, value, cacheTime);
        }

    }

    public static void saveIntoCache(String key, String value) {
        int cacheTime = DBPUtilitiesConstants.DEFAULT_CACHE_TIME;
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

    public static void saveIntoCache(String key, byte[] value) {
        int cacheTime = DBPUtilitiesConstants.DEFAULT_CACHE_TIME;
        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }

        if (resultCache != null && StringUtils.isNotBlank(key)) {

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