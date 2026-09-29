package com.hbl.infinity.accounts.perf;

/**
 * Constants for the Holdings/DigitalArrangements/getList performance work: server property names and their
 * defaults, and the component names used in timing logs.
 */
public final class GetListPerfConstants {

    /** Server property: when "true", each getList component logs one timing summary line per request. */
    public static final String PROP_TIMING_LOG = "HBL_GETLIST_TIMING_LOG";
    public static final boolean DEFAULT_TIMING_LOG = false;

    /** Component names used in the timing log line. */
    public static final String COMPONENT_JAVA_SERVICE = "GetAccountsOperation";
    public static final String COMPONENT_T24_PRE = "getAccountsFromT24PreProcessor";
    public static final String COMPONENT_T24_POST = "getAccountsFromT24PostProcessor";
    public static final String COMPONENT_OBJECT_POST = "GetAccountsPostLoginObjectServicePostProcessor";

    private GetListPerfConstants() {
    }
}
