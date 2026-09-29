package com.kony.dbputilities.dbutil;

import java.sql.Connection;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBManager {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private DBManager() {
    }

    private static HikariDataSource DATASOURCE;

    private static synchronized void createDataSource(DataControllerRequest dcRequest) throws Exception {
        if (DATASOURCE == null || DATASOURCE.isClosed()) {

            Properties properties = new Properties();
            String jdbcUrl = URLFinder.getPathUrl(URLConstants.DB_JDBCURL, dcRequest);
            String userName = URLFinder.getPathUrl(URLConstants.DB_USERNAME, dcRequest);
            String password = URLFinder.getPathUrl(URLConstants.DB_PASSWORD, dcRequest);

            properties.setProperty("jdbcUrl", jdbcUrl);
            properties.setProperty("username", userName);
            properties.setProperty("password", password);
            properties.setProperty("maximumPoolSize", DataBaseProperties.MAX_POOLSIZE);
            properties.setProperty("minimumIdle", DataBaseProperties.MIN_IDLE);
            properties.setProperty("idleTimeout", DataBaseProperties.IDLE_TIMEOUT);
            properties.setProperty("dataSource.cachePrepStmts", DataBaseProperties.DATASOURCE_CACHEPREPSTMTS);
            properties.setProperty("dataSource.prepStmtCacheSize", DataBaseProperties.DATASOURCE_PREPSTMTCAHCESIZE);
            properties.setProperty("dataSource.prepStmtCacheSqlLimit",
                    DataBaseProperties.DATASOURCE_PREPSTMTCAHCESQLLIMIT);
            properties.setProperty("dataSource.useServerPrepStmts", DataBaseProperties.DATASOURCE_USESERVERPREPSTMTS);
            properties.setProperty("dataSource.useLocalSessionState",
                    DataBaseProperties.DATASOURCE_USELOCALSESSIONSTATE);
            properties.setProperty("dataSource.useLocalTransactionState",
                    DataBaseProperties.DATASOURCE_USELOCALTRANSSTATE);
            properties.setProperty("dataSource.rewriteBatchedStatements",
                    DataBaseProperties.DATASOURCE_REWRITEBATCHEDSTMTS);
            properties.setProperty("dataSource.cacheResultSetMetadata",
                    DataBaseProperties.DATASOURCE_CACHERESULTSETMETADATA);
            properties.setProperty("dataSource.cacheServerConfiguration",
                    DataBaseProperties.DATASOURCE_CACHESERVERCONFIG);
            properties.setProperty("dataSource.elideSetAutoCommits", DataBaseProperties.DATASOURCE_ELIDESETAUTOCOMMITS);
            properties.setProperty("dataSource.maintainTimeStats", DataBaseProperties.DATASOURCE_MAINTAINTIMESTATS);

            HikariConfig config = new HikariConfig(properties);
            DATASOURCE = new HikariDataSource(config);
        }
    }

    public static synchronized void closeDataSource() {
        if (DATASOURCE != null) {
            try {
                DATASOURCE.close();
                DATASOURCE = null;
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        }
    }

    public static Connection getConnection(DataControllerRequest dcRequest) {

        try {
            if (DATASOURCE == null || DATASOURCE.isClosed()) {
                createDataSource(dcRequest);
            }
            return DATASOURCE.getConnection();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

}
