package com.kony.dbputilities.dbutil;

public final class DataBaseProperties {
    private DataBaseProperties() {
    }

    public static final String MAX_POOLSIZE = "10";
    public static final String MIN_IDLE = "1";
    public static final String IDLE_TIMEOUT = "30000";
    public static final String DATASOURCE_CACHEPREPSTMTS = "true";
    public static final String DATASOURCE_PREPSTMTCAHCESIZE = "250";
    public static final String DATASOURCE_PREPSTMTCAHCESQLLIMIT = "2048";
    public static final String DATASOURCE_USESERVERPREPSTMTS = "true";
    public static final String DATASOURCE_USELOCALSESSIONSTATE = "true";
    public static final String DATASOURCE_USELOCALTRANSSTATE = "true";
    public static final String DATASOURCE_REWRITEBATCHEDSTMTS = "true";
    public static final String DATASOURCE_CACHERESULTSETMETADATA = "true";
    public static final String DATASOURCE_CACHESERVERCONFIG = "true";
    public static final String DATASOURCE_ELIDESETAUTOCOMMITS = "true";
    public static final String DATASOURCE_MAINTAINTIMESTATS = "false";

}
