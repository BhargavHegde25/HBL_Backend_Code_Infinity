package com.temenos.infinity.smartbanking.advisory.constants;

import com.dbp.core.constants.DBPConstants;

public class CommonConstants {
	
	// error-handling
	public static final String ERRCODE = DBPConstants.DBP_ERROR_CODE_KEY;
	public static final String ERRMSG = DBPConstants.DBP_ERROR_MESSAGE_KEY;
	public static final boolean validationFlag = true;

	// http success codes
	public static final String HTTP_SUCCESSCODE_200 = "200";

	// http error codes
	public static final String HTTP_ERRCODE_400 = "400";
	public static final String HTTP_ERRCODE_404 = "404";

	// op Status
	public static final String OPSTATUS_FAILURE = "-1";
	public static final String OPSTATUS_SUCCESS = "0";
	
	public static final String Db_Operation = "dbpRbLocalServicesdb";
	public static final String Customer_Get = "_customer_get";
	public static final String Customer_Update = "_customer_update";


}