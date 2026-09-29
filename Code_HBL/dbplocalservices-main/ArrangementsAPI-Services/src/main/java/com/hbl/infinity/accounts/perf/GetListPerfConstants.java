package com.hbl.infinity.accounts.perf;

/**
 * Constants for the Holdings/DigitalArrangements/getList performance work: server property names and their
 * defaults, and the component names used in timing logs.
 */
public final class GetListPerfConstants {

    /** Server property: when "true", each getList component logs one timing summary line per request. */
    public static final String PROP_TIMING_LOG = "HBL_GETLIST_TIMING_LOG";
    public static final boolean DEFAULT_TIMING_LOG = false;

    /** Server property: seconds a loaded bundle configuration is reused; 0 turns the cache off. */
    public static final String PROP_BUNDLE_CONFIG_TTL_SECONDS = "HBL_BUNDLE_CONFIG_TTL_SECONDS";
    public static final int DEFAULT_BUNDLE_CONFIG_TTL_SECONDS = 600;

    /** Server property: master switch for the getList snapshot cache; anything but "true" runs today's code. */
    public static final String PROP_CACHE_ENABLED = "HBL_GETLIST_CACHE_ENABLED";
    public static final boolean DEFAULT_CACHE_ENABLED = false;

    /** Server property: seconds a getList snapshot is kept (safety net behind the version keys). */
    public static final String PROP_ENT_TTL_SECONDS = "HBL_GETLIST_ENT_TTL_SECONDS";
    public static final int DEFAULT_ENT_TTL_SECONDS = 1800;

    /** Server property: seconds a permission-version key is kept. */
    public static final String PROP_PERMVER_TTL_SECONDS = "HBL_GETLIST_PERMVER_TTL_SECONDS";
    public static final int DEFAULT_PERMVER_TTL_SECONDS = 86400;

    /** Server property: maximum milliseconds to wait for one cache read before treating it as unavailable. */
    public static final String PROP_CACHE_TIMEOUT_MS = "HBL_GETLIST_CACHE_TIMEOUT_MS";
    public static final int DEFAULT_CACHE_TIMEOUT_MS = 50;

    /** Server property: threads in the bounded pool used for cache calls; read once, when the pool is created. */
    public static final String PROP_POOL_SIZE = "HBL_GETLIST_PARALLEL_POOL_SIZE";
    public static final int DEFAULT_POOL_SIZE = 16;

    /** Prefix of every key this package writes to the Fabric cache; "v1" is the snapshot schema version. */
    public static final String CACHE_KEY_PREFIX = "HBLGL:v1:";
    public static final String KEY_GLOBAL_VERSION = CACHE_KEY_PREFIX + "GLOBALVER";
    public static final String KEY_CUSTOMER_VERSION = CACHE_KEY_PREFIX + "PERMVER:";

    /** Snapshot stages, one per getList class that reads the database. */
    public static final String STAGE_T24_PRE = "PRE";
    public static final String STAGE_T24_POST = "T24POST";
    public static final String STAGE_OBJECT_POST = "OBJPOST";

    /** Component names used in the timing log line. */
    public static final String COMPONENT_JAVA_SERVICE = "GetAccountsOperation";
    public static final String COMPONENT_T24_PRE = "getAccountsFromT24PreProcessor";
    public static final String COMPONENT_T24_POST = "getAccountsFromT24PostProcessor";
    public static final String COMPONENT_OBJECT_POST = "GetAccountsPostLoginObjectServicePostProcessor";

    private GetListPerfConstants() {
    }
}
