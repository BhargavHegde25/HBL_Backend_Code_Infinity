package com.kony.memorymgmt;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.ehcache.ResultCache;

public class MemoryManager {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private MemoryManager() {
    }

    static void save(FabricRequestManager fabricReqManager, String key, Object value) {

        try {
            ResultCache resultCache = null;
            SessionMap sessionData = (SessionMap) value;
            diagnostic.prepareDebug("saving cacheData {}", sessionData).log();
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
            if (resultCache != null && StringUtils.isNotBlank(key) && null != sessionData
                    && StringUtils.isNotBlank(sessionData.toString())) {

                resultCache.insertIntoCache(key, sessionData.toString(), DBPUtilitiesConstants.DEFAULT_CACHE_TIME);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }

    }

    static Object retrieve(FabricRequestManager fabricReqManager, String key) {
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

}