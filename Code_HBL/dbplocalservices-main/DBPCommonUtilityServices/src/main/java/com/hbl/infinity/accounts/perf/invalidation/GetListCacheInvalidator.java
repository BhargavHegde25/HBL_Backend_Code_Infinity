package com.hbl.infinity.accounts.perf.invalidation;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * Makes cached getList results obsolete after a write to data getList depends on (accounts, permissions,
 * contracts, roles, default account, nickname, consent...).
 * <p>
 * The getList snapshot cache (package com.hbl.infinity.accounts.perf in ArrangementsAPI-Services) builds every key
 * from a global version token and a per-customer version token. Replacing a token here means the next getList
 * builds new keys, misses, and reads the database again. This class lives in DBPCommonUtilityServices so that
 * every module that writes such data can call it without depending on ArrangementsAPI-Services.
 * <p>
 * Every method is safe to call on any path: it never throws, and it runs whether or not
 * HBL_GETLIST_CACHE_ENABLED is on, so switching the cache on later never finds stale entries. A bump that cannot be
 * written is logged; the cached entries then expire after HBL_GETLIST_ENT_TTL_SECONDS at the latest. Nothing here
 * logs customer ids or cache values.
 */
public final class GetListCacheInvalidator {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    /** Prefix of every getList cache key; "v1" is the snapshot schema version. */
    public static final String KEY_PREFIX = "HBLGL:v1:";
    /** Version token shared by every customer. */
    public static final String KEY_GLOBAL_VERSION = KEY_PREFIX + "GLOBALVER";
    /** Prefix of the per-customer version token; the Infinity customer id follows. */
    public static final String KEY_CUSTOMER_VERSION = KEY_PREFIX + "PERMVER:";

    /** Server property: seconds a version token is kept. */
    public static final String PROP_PERMVER_TTL_SECONDS = "HBL_GETLIST_PERMVER_TTL_SECONDS";
    public static final int DEFAULT_PERMVER_TTL_SECONDS = 86400;

    private GetListCacheInvalidator() {
    }

    /**
     * Makes one customer's cached getList results obsolete. Use after a write that only concerns this customer
     * (their accounts' nickname, default account, consent, e-statement settings...). Does nothing for a blank id.
     *
     * @param customerId Infinity customer id
     */
    public static void customerChanged(String customerId) {
        if (StringUtils.isNotBlank(customerId)) {
            replace(KEY_CUSTOMER_VERSION + customerId);
        }
    }

    /**
     * Makes the logged-in customer's cached getList results obsolete. When the customer cannot be read from the
     * session, every customer's results are made obsolete instead, so a change is never missed.
     *
     * @param request current request
     */
    public static void sessionCustomerChanged(DataControllerRequest request) {
        String customerId = null;
        try {
            customerId = HelperMethods.getCustomerIdFromSession(request);
        } catch (RuntimeException e) {
            // Falls back to the global bump below.
        }
        if (StringUtils.isNotBlank(customerId)) {
            customerChanged(customerId);
        } else {
            permissionsChanged();
        }
    }

    /**
     * Makes every customer's cached getList results obsolete. Use after a write that can affect many customers
     * (contracts, roles, service definitions, feature permissions, user management done by an administrator).
     */
    public static void permissionsChanged() {
        replace(KEY_GLOBAL_VERSION);
    }

    /**
     * @return a new random version token; two calls never return the same token in practice
     */
    public static String newToken() {
        return Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + "-"
                + Long.toString(ThreadLocalRandom.current().nextLong() & Long.MAX_VALUE, Character.MAX_RADIX);
    }

    private static void replace(String key) {
        try {
            MemoryManager.saveIntoCache(key, newToken(), ttlSeconds());
        } catch (RuntimeException | LinkageError e) {
            alert.prepareError("getList cache: version bump could not be written; cached results expire within "
                    + "HBL_GETLIST_ENT_TTL_SECONDS", e).log();
        }
    }

    private static int ttlSeconds() {
        try {
            String value = EnvironmentConfigurationsHandler.getServerProperty(PROP_PERMVER_TTL_SECONDS);
            if (StringUtils.isNotBlank(value)) {
                int parsed = Integer.parseInt(value.trim());
                if (parsed > 0) {
                    return parsed;
                }
            }
        } catch (RuntimeException e) {
            // Missing, unreadable or invalid: use the default.
        }
        return DEFAULT_PERMVER_TTL_SECONDS;
    }
}
