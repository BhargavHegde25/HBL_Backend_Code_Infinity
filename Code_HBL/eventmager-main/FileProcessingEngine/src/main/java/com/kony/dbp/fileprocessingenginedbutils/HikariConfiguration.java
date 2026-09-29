package com.kony.dbp.fileprocessingenginedbutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbp.fileprocessingengine.FileProcessingEngineConstants;
import com.kony.dbp.fileprocessingengine.HelperPackage.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariConfiguration {

	private HikariConfiguration() {

	}

	private static HikariDataSource datasource;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static HikariDataSource getDataSource(DataControllerRequest dcRequest) {
		try {
			ServicesManager servicesmanager = dcRequest.getServicesManager();
			if (datasource == null || datasource.isClosed())
				synchronized (HikariConfiguration.class) {
					if (datasource == null || datasource.isClosed()) {
						HikariConfig config = new HikariConfig();
						config.setDriverClassName(EnvironmentConfigurationsHandler.getValue(
								FileProcessingEngineConstants.FILEPROCESSINGENGINE_DATABASE_DRIVER, servicesmanager));
						config.setJdbcUrl(EnvironmentConfigurationsHandler.getValue(
								FileProcessingEngineConstants.FILEPROCESSINGENGINE_DATABASE_URL, servicesmanager));
						config.setUsername(EnvironmentConfigurationsHandler.getValue(
								FileProcessingEngineConstants.FILEPROCESSINGENGINE_DATABASE_USER, servicesmanager));
						config.setPassword(EnvironmentConfigurationsHandler.getValue(
								FileProcessingEngineConstants.FILEPROCESSINGENGINE_DATABASE_PSWD, servicesmanager));

						config.setMaxLifetime(600000);
						config.setMinimumIdle(1);
						config.setIdleTimeout(300000);
						config.setMaximumPoolSize(Integer.parseInt(EnvironmentConfigurationsHandler.getValue(
								FileProcessingEngineConstants.FILEPROCESSINGENGINE_DB_MAXPOOL_SIZE, servicesmanager)));
						config.setAutoCommit(true);

						datasource = new HikariDataSource(config);
					}
				}

		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
		return datasource;
	}

	public static Connection getconnection() {
		Connection con = null;
		try {
			con = datasource.getConnection();
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching connection", e).log();
		}
		return con;
	}

	public static synchronized void closeDatasource() {
		try {
			if (datasource != null) {
				datasource.close();
				datasource = null;
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured in closing datasource", e).log();
		}
	}

	public static void close(Connection... connections) {
		for (Connection connection : connections) {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqlex) {
					alert.prepareError("Error occured SQL Exception", sqlex).log();
				}
			}
		}
	}

	// Method to close one or more result sets.
	public static void close(ResultSet... resultsets) {
		for (ResultSet resultset : resultsets) {
			if (resultset != null) {
				try {
					resultset.close();
				} catch (SQLException sqlex) {
					alert.prepareError("Error occured SQLException", sqlex).log();
				}
			}
		}
	}

	// Method to close one or more statements.
	public static void close(Statement... statements) {
		for (Statement statement : statements) {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqlex) {
					alert.prepareError("Error occured SQLException", sqlex).log();
				}
			}
		}
	}

}
