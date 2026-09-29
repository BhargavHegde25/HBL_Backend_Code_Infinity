package com.kony.dbx.util;
/**
 * @author Gopinath Vaddepally KH2453
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariConfiguration implements Constants{

	private static HikariDataSource datasource;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	/**
	 * @author Gopinath Vaddepally KH2453
	 * @param dcRequest
	 * @return data source for resuable connections
	 */
	public static HikariDataSource getDataSource(DataControllerRequest dcRequest) {
		try {
			if (datasource == null || datasource.isClosed())
				synchronized (HikariConfiguration.class) {
					if (datasource == null || datasource.isClosed()) {
						HashMap<String,String> dbProperties = getDBProperties(dcRequest);
						HikariConfig config = new HikariConfig();
						String dbDriver = "mysql";
//						if (dbProperties != null && dbProperties.size() > 0) {
//							dbDriver = dbProperties.get(PARAM_DBX_DB_DRIVER);
//						}
						switch (dbDriver) {
						case CONST_DRIVER_MYSQL:
							config.setDriverClassName("com.mysql.jdbc.Driver");
							break;
						default:
							config.setDriverClassName("com.mysql.jdbc.Driver");
							break;
						}
						config.setJdbcUrl(dbProperties.get(PARAM_DBX_DB_HOST_URL));
						config.setUsername(dbProperties.get(PARAM_DBX_DB_USERNAME));
						config.setPassword(dbProperties.get(PARAM_DBX_DB_PASSWORD));

						config.setMaxLifetime(
								Integer.parseInt(C_SETMAXLIFETIME));
						config.setMinimumIdle(
								Integer.parseInt(C_SETMINIMUMIDLE));
						config.setIdleTimeout(
								Integer.parseInt(C_SETIDLETIMEOUT));
						config.setMaximumPoolSize(
								Integer.parseInt(C_SETMAXIMUMDBPOOLSIZE));
						config.setAutoCommit(true);

						datasource = new HikariDataSource(config);
					}
				}

		} catch (Exception e) {
			System.out.println("Exception e");
			alert.prepareError("Error occured", e).log();
		}
		return datasource;
	}
	/**
	 * @author Gopinath Vaddepally KH2453
	 * @return connection object from data source
	 */
	public static Connection getconnection() {
		Connection con = null;
		try {
			con = datasource.getConnection();
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching connection", e).log();
		}
		return con;
	}

	/**
	 * closes data source
	 */
	public static synchronized void closeDatasource() {
		try {
			if (datasource != null) {
				datasource.close();
				datasource = null;
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured in closing datasource").log();
		}
	}

	/**
	 * closes connection
	 * @param connections
	 */
	public static void close(Connection... connections) {
		for (Connection connection : connections) {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqlex) {
				}
			}
		}
	}

	// Method to close one or more result sets.
	public static void close(ResultSet... resultSets) {
		for (ResultSet resultSet : resultSets) {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException sqlex) {
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
				}
			}
		}
	}

	/**
	 * @author Gopinath Vaddepally KH2453
	 * @param request
	 * @return map with all db details
	 */
	public static HashMap<String,String> getDBProperties(DataControllerRequest request)
	{
		HashMap<String, String> db_properties = new HashMap<>();
		try {
			ServicesManager servicesManager = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = servicesManager.getConfigurableParametersHelper();
			Map<String, String> serverProperties = paramHelper.getAllServerProperties();

			//fetching  db related server properties
			String schemaName = getPropertyValue(request, Constants.PROPERTIES_FILE, Constants.SECTION_CONFIGURATION, Constants.PROPERTYNAME_SCHEMANAME);
			db_properties.put(PARAM_DBX_DB_NAME,schemaName);
			//db_properties.put(PARAM_DBX_DB_NAME,paramHelper.getServerProperty(PARAM_DBX_SCHEMA_NAME));
			db_properties.put(PARAM_DBX_DB_HOST_URL,CommonUtils.getPropertyValue(serverProperties, PARAM_DBX_DB_HOST_URL));
			db_properties.put(PARAM_DBX_DB_USERNAME,CommonUtils.getPropertyValue(serverProperties, PARAM_DBX_DB_USERNAME));
			db_properties.put(PARAM_DBX_DB_PASSWORD,CommonUtils.getPropertyValue(serverProperties, PARAM_DBX_DB_PASSWORD));
			db_properties.put(PARAM_DBX_DB_DRIVER,"mysql");
			alert.prepareError("db properties hash map "+db_properties).log();
		} catch (Exception e) {
			// TODO: handle exception
			alert.prepareError("error ocuured in process util class while fetching db server configuration "+e).log();
		}
		return db_properties;
	}
	public static String getPropertyValue(DataControllerRequest request, String propFile, String section, String propertyName)
			throws Exception {

		// Check the session first
		String propertyValue = "";
		if (request != null) {
			propertyValue = CommonUtils.retreiveFromSession(propertyName, request) != null
                    ? CommonUtils.retreiveFromSession(propertyName, request).toString()
                    : "";
		}

		// If not found in the session, load it from the properties file & add to
		// session
		if (propertyValue == null || propertyValue.equalsIgnoreCase("")) {

			propertyValue = getProperty(propFile, section, propertyName);
			 if (request != null) {
	                CommonUtils.insertIntoSession(propertyName, propertyValue, request);
	            }
		}
		return propertyValue;
	}

	public static String getProperty(String propFile, String section, String key) throws Exception {

		return CommonUtils.getProperty(propFile, Constants.HIKARI_PREFIX, section, key);
	}

}
