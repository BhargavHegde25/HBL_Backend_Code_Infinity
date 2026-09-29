package com.hbl.adminconsole.getlistcache;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

import com.kony.adminconsole.commons.utils.MemoryManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * Spotlight side of the online-banking getList cache invalidation.
 * <p>
 * Online banking caches what Holdings/DigitalArrangements/getList reads from the database, under keys that contain a
 * global version token. After Spotlight changes customer actions, groups, features or permissions, it replaces that
 * token here, so the next getList of every customer reads the database again.
 * <p>
 * This only works because Spotlight and online banking run on the same Fabric server and so share its result cache.
 * The key and property names must stay identical to
 * {@code com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator} in DBPCommonUtilityServices (online
 * banking); Spotlight does not depend on that module, hence this copy. When the stores are not shared, the bump has
 * no effect and cached results expire after HBL_GETLIST_ENT_TTL_SECONDS.
 * <p>
 * Never throws, and runs whether or not the cache is switched on.
 */
public final class GetListCacheInvalidator {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    /** Same value as GetListCacheInvalidator.KEY_GLOBAL_VERSION in DBPCommonUtilityServices. */
    public static final String KEY_GLOBAL_VERSION = "HBLGL:v1:GLOBALVER";

    /** Same property and default as in DBPCommonUtilityServices. */
    public static final String PROP_PERMVER_TTL_SECONDS = "HBL_GETLIST_PERMVER_TTL_SECONDS";
    public static final int DEFAULT_PERMVER_TTL_SECONDS = 86400;

    private GetListCacheInvalidator() {
    }

    /**
     * Makes every customer's cached getList results obsolete. Call after any Spotlight write that can change what
     * getList returns (customer actions, groups and their entitlements, features, business types).
     */
    public static void permissionsChanged() {
        try {
            MemoryManager.saveIntoCache(KEY_GLOBAL_VERSION, newToken(), ttlSeconds());
        } catch (RuntimeException | LinkageError e) {
            alert.prepareError("getList cache: version bump could not be written; cached results expire within "
                    + "HBL_GETLIST_ENT_TTL_SECONDS", e).log();
        }
    }

    static String newToken() {
        return Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + "-"
                + Long.toString(ThreadLocalRandom.current().nextLong() & Long.MAX_VALUE, Character.MAX_RADIX);
    }

    private static int ttlSeconds() {
        try {
            String value = ServicesManagerHelper.getServicesManager().getConfigurableParametersHelper()
                    .getServerProperty(PROP_PERMVER_TTL_SECONDS);
            if (StringUtils.isNotBlank(value)) {
                int parsed = Integer.parseInt(value.trim());
                if (parsed > 0) {
                    return parsed;
                }
            }
        } catch (Exception e) {
            // Missing, unreadable or invalid: use the default.
        }
        return DEFAULT_PERMVER_TTL_SECONDS;
    }
}
