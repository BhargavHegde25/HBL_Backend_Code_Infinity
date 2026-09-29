package com.kony.alertslogservices.dbutils;

import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import org.sql2o.Sql2o;

import com.kony.alertslogservices.util.ArchivalConstants;
import com.kony.alertslogservices.util.Helpermethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Datasource handler that maintains the log databases
 * 
 * @author Sridhar Reddy
 *
 */

public class AlertsLogDataSourceHandler {
	private AlertsLogDataSourceHandler() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	
	private static HikariDataSource logdatasource;
	private static Sql2o logsql2o;
	private static final InheritableThreadLocal<DataControllerRequest> THREAD_LOCAL_DCR = new InheritableThreadLocal<>(); // used
																															// to

	private static synchronized void createLogDataSource() {
		try {
			if (logdatasource == null || logdatasource.isClosed()) {
				HikariConfig config = new HikariConfig(getLogDatasourceProps());
				config.setMaxLifetime(600000);
				config.setMinimumIdle(0);
				config.setIdleTimeout(300000);
				logdatasource = new HikariDataSource(config);
				logsql2o = new Sql2o(logdatasource);
			}
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
	}

	public static Sql2o getLogSql2oInstance() {
		if (logsql2o == null || logdatasource == null || logdatasource.isClosed()) {
			try {
				createLogDataSource();
			} catch (Exception e) {
				alert.prepareError("Failed creating DBP log datasource", e).log();
				closeLogDataSource(); // closing DBP log datasource if created
			}
		}
		return logsql2o;
	}

	public static synchronized void closeLogDataSource() {
		try {
			if (logdatasource != null) {
				alert.prepareError("[Informational] Releasing DBP log database resources").log();
				logdatasource.close();
			}
		} catch (Exception e) {
			alert.prepareError("Failed to release resources of DBP log datasource", e).log();
		}
	}

	private static Properties getLogDatasourceProps() throws Exception {

		DataControllerRequest requestInstance = getRequest();

		Properties properties = new Properties();
		properties.put("driverClassName", Helpermethods.getConfigProperty(ArchivalConstants.ALERTHISTORY_ARCH_DATABASE_DRIVER));
		properties.put("jdbcUrl", Helpermethods.getConfigProperty(ArchivalConstants.ALERTHISTORY_ARCH_DATABASE_URL));
		properties.put("username", Helpermethods.getConfigProperty(ArchivalConstants.ALERTHISTORY_ARCH_DATABASE_USER));
		properties.put("password", Helpermethods.getConfigProperty(ArchivalConstants.ALERTHISTORY_ARCH_DATABASE_PSWD));
		properties.put("maximumPoolSize", "1");
		return properties;
	}

	public static DataControllerRequest getRequest() {
		return THREAD_LOCAL_DCR.get();
	}

	public static void setRequest(DataControllerRequest dataControllerRequest) {
		THREAD_LOCAL_DCR.set(dataControllerRequest);
	}

	public static void removeRequest() {
		THREAD_LOCAL_DCR.remove();
	}

}
