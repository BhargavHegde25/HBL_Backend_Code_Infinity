package com.hbl.infinity.accounts.perf;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * Reads the getList performance server properties. Every value has a safe default, and a missing, unreadable or
 * invalid value falls back to that default. Values are read on each call, so a property change takes effect on
 * the next request without a restart.
 */
public final class GetListPerfConfig {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private GetListPerfConfig() {
    }

    /**
     * @return true when the per-request timing summary should be logged ({@value GetListPerfConstants#PROP_TIMING_LOG})
     */
    public static boolean isTimingLogEnabled() {
        return getBoolean(GetListPerfConstants.PROP_TIMING_LOG, GetListPerfConstants.DEFAULT_TIMING_LOG);
    }

    /**
     * Reads a boolean server property. Only "true" and "false" (any case) are accepted; anything else returns the
     * default and logs one warning.
     *
     * @param key          server property name
     * @param defaultValue value used when the property is missing or invalid
     * @return the property value, or the default
     */
    static boolean getBoolean(String key, boolean defaultValue) {
        String value = readProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        value = value.trim();
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        alert.prepareWarn("Invalid value for server property " + key + ", using default " + defaultValue).log();
        return defaultValue;
    }

    private static String readProperty(String key) {
        try {
            return EnvironmentConfigurationsHandler.getServerProperty(key);
        } catch (RuntimeException e) {
            // Server properties unavailable (for example outside Fabric): behave as if the property is not set.
            return null;
        }
    }
}
