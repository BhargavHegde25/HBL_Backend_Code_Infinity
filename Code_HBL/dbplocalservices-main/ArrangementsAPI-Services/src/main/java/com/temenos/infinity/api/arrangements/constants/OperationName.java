package com.temenos.infinity.api.arrangements.constants;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Contains constants for integration operation Name/ object verb name
 */
public final class OperationName {

	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");

	public static final String DB_CUSTOMVIEW_CREATE = SCHEMA_NAME + "_customview_create";
	public static final String DB_CUSTOMVIEW_GET = SCHEMA_NAME + "_customview_get";
	public static final String DB_CUSTOMVIEW_EDIT = SCHEMA_NAME + "_customview_update";
	public static final String DB_CUSTOMVIEW_DELETE = SCHEMA_NAME + "_customview_delete";
	
}
	 