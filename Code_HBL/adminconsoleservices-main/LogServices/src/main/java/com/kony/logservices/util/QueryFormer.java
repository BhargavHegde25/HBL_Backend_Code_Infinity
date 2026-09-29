package com.kony.logservices.util;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;

public class QueryFormer {
	
	private QueryFormer() {}
	
	public static final String  dbName = getValue("LOG_SCHEMA_NAME");

	public static String getDBType() {
		String jdbcUrl = QueryFormer.getValue("LOG_DATASOURCE_JDBC_URL");
		jdbcUrl=jdbcUrl.toLowerCase();
		String dbType = null;
		if (jdbcUrl.contains("mysql")) {
			dbType = "MYSQL";
		} else if (jdbcUrl.contains("sqlserver")) {
			dbType = "MSSQL";
		}
		else if (jdbcUrl.contains("oracle")) {
			dbType = "ORACLE";
		}
		return dbType;
	}
	
	public static String getValue(String key) {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			return configurableParametersHelper.getServerProperty(key);
		} catch (Exception e) {
			return (e.getMessage());
		}

	}
	
	public static String replaceDbType(String enumName){
		String query=QueryFormer.getDBType()+"_"+enumName;
		return query;

	}
}

