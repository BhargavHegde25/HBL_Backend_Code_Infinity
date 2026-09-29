package com.kony.dbp.queuemaster.utils;

public class QueryFormer {
	
	public static String getDBType(String key) {
		
		String jdbcUrl = EnvironmentConfigurationsHandler.getValue(key);
		
		jdbcUrl = jdbcUrl.toLowerCase();
		String dbType = null;
		if (jdbcUrl.contains("mysql")) {
			dbType = "MYSQL";
		} else if (jdbcUrl.contains("sqlserver")) {
			dbType = "MSSQL";
		}else if (jdbcUrl.contains("oracle")) {
			dbType = "ORACLE";
		}
		return dbType;
	}
	 
	  public static String getQueryForDBType(String enumName, String key){
          String query = getDBType(key)+"_"+enumName;
       return query;
           
       }
	 
}