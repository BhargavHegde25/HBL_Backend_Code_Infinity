package com.kony.dbputilities.dbutil;

import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;

public class QueryFormer {
	
	public static String getDBType(DataControllerRequest dcRequest) {
	String jdbcUrl = URLFinder.getPathUrl(URLConstants.DB_JDBCURL, dcRequest);
	jdbcUrl=jdbcUrl.toLowerCase();
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
	/*
	 * enum DBTypeEnum{ MSSQL("MSSQL"), MYSQL("MYSQL"); }
	 */
}