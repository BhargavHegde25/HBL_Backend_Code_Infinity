package com.kony.auditlogservices.util;

import org.apache.commons.lang3.StringUtils;

public class QueryFormer {
	
	public static final String MYSQL_DB = "MYSQL";
	public static final String MSSQL_DB = "MSSQL";
	public static final String ORACLE_DB = "ORACLE";
	
	public static String getDBType() {
		
		String jdbcUrl = null;
		try {
			jdbcUrl = Helpermethods.
					getConfigProperty(ArchivalConstants.AUDITLOG_ARCH_DATABASE_URL);
		} catch (Exception e) {
			
		}
		
		jdbcUrl = jdbcUrl.toLowerCase();
		String dbType = null;
		if (jdbcUrl.contains("mysql")) {
			dbType = MYSQL_DB;
		} else if (jdbcUrl.contains("sqlserver")) {
			dbType = MSSQL_DB;
		} else if (jdbcUrl.contains("oracle")) {
			dbType = ORACLE_DB;
		}
		return dbType;
	}
	
	public static String getSchemaName() {
		
		String schemaName = "dbxlogs";
		try {
			schemaName = Helpermethods.
					getConfigProperty(ArchivalConstants.LOG_SCHEMA_NAME);
		} catch (Exception e) {
			
		}
		
		return schemaName;
	}
	
	public static String replaceSchameName(String query) {
		if(StringUtils.isNotBlank(query)) {
			return query.replace(":schemaName", getSchemaName());
		}
		return query;
	}
	 
}