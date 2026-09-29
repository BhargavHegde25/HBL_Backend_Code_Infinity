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
     * @return seconds a bundle configuration stays cached ({@value GetListPerfConstants#PROP_BUNDLE_CONFIG_TTL_SECONDS});
     *         0 means the cache is off
     */
    public static int getBundleConfigTtlSeconds() {
        return getNonNegativeInt(GetListPerfConstants.PROP_BUNDLE_CONFIG_TTL_SECONDS,
                GetListPerfConstants.DEFAULT_BUNDLE_CONFIG_TTL_SECONDS);
    }

    /**
     * @return true when the getList snapshot cache is switched on ({@value GetListPerfConstants#PROP_CACHE_ENABLED})
     */
    public static boolean isCacheEnabled() {
        return getBoolean(GetListPerfConstants.PROP_CACHE_ENABLED, GetListPerfConstants.DEFAULT_CACHE_ENABLED);
    }

    /**
     * @return seconds a getList snapshot is kept ({@value GetListPerfConstants#PROP_ENT_TTL_SECONDS})
     */
    public static int getEntTtlSeconds() {
        return getPositiveInt(GetListPerfConstants.PROP_ENT_TTL_SECONDS, GetListPerfConstants.DEFAULT_ENT_TTL_SECONDS);
    }

    /**
     * @return seconds a permission-version key is kept ({@value GetListPerfConstants#PROP_PERMVER_TTL_SECONDS})
     */
    public static int getPermVerTtlSeconds() {
        return getPositiveInt(GetListPerfConstants.PROP_PERMVER_TTL_SECONDS,
                GetListPerfConstants.DEFAULT_PERMVER_TTL_SECONDS);
    }

    /**
     * @return milliseconds to wait for one cache read ({@value GetListPerfConstants#PROP_CACHE_TIMEOUT_MS})
     */
    public static int getCacheTimeoutMs() {
        return getPositiveInt(GetListPerfConstants.PROP_CACHE_TIMEOUT_MS,
                GetListPerfConstants.DEFAULT_CACHE_TIMEOUT_MS);
    }

    /**
     * @return threads in the cache-call pool ({@value GetListPerfConstants#PROP_POOL_SIZE})
     */
    public static int getPoolSize() {
        return getPositiveInt(GetListPerfConstants.PROP_POOL_SIZE, GetListPerfConstants.DEFAULT_POOL_SIZE);
    }

    /**
     * Reads a strictly positive integer server property. Zero, a negative or a non-numeric value returns the
     * default and logs one warning.
     *
     * @param key          server property name
     * @param defaultValue value used when the property is missing or invalid
     * @return the property value, or the default
     */
    static int getPositiveInt(String key, int defaultValue) {
        int value = getNonNegativeInt(key, defaultValue);
        if (value > 0) {
            return value;
        }
        alert.prepareWarn("Invalid value for server property " + key + ", using default " + defaultValue).log();
        return defaultValue;
    }

    /**
     * Reads a non-negative integer server property. A negative or non-numeric value returns the default and logs
     * one warning.
     *
     * @param key          server property name
     * @param defaultValue value used when the property is missing or invalid
     * @return the property value, or the default
     */
    static int getNonNegativeInt(String key, int defaultValue) {
        String value = readProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed >= 0) {
                return parsed;
            }
        } catch (NumberFormatException e) {
            // Falls through to the warning below.
        }
        alert.prepareWarn("Invalid value for server property " + key + ", using default " + defaultValue).log();
        return defaultValue;
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
