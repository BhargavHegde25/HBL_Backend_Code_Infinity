package com.temenos.infinity.api.srmstransactions.utils;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.ehcache.ResultCache;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;

public class MemoryManagerUtils {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private MemoryManagerUtils() {
    }
    
    
    public static Object retrieve(String key) {
        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
            if (resultCache != null && StringUtils.isNotBlank(key)) {
                String value = (String) resultCache.retrieveFromCache(key);
                diagnostic.prepareDebug("retrieving cacheData {}", value).log();
                SessionMap sessionData = new SessionMap();
                sessionData.setData(value);
                return sessionData; 
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }
        return null;

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

}