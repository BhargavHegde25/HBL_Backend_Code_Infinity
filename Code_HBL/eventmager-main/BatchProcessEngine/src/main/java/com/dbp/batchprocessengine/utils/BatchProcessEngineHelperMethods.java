package com.dbp.batchprocessengine.utils;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class BatchProcessEngineHelperMethods {
	private static String schemaname = null;

	private BatchProcessEngineHelperMethods() {
	}

	public static String replaceSchemaName(String operationid, String schemaname) {
		if (operationid == null || schemaname == null)
			return operationid;
		if (operationid.contains("{schema_name}"))
			operationid = operationid.replace("{schema_name}", schemaname);
		return operationid;

	}

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}

	public static Result returnResult(boolean flag, BatchProcessingEnum err) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(BatchProcessEngineConstants.SUCCESS, BatchProcessEngineConstants.TRUE,
					BatchProcessEngineConstants.STRING));
		} else {
			res.addParam(new Param(BatchProcessEngineConstants.SUCCESS, BatchProcessEngineConstants.FALSE,
					BatchProcessEngineConstants.STRING));
			res.addParam(new Param(BatchProcessEngineConstants.DBPERRMSG, err.getErrMsg(),
					BatchProcessEngineConstants.STRING));
			res.addParam(new Param(BatchProcessEngineConstants.DBPERRCODE, err.getErrCode(),
					BatchProcessEngineConstants.STRING));
		}
		return res;
	}

	public static Result returnResult(boolean flag, String msg) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(BatchProcessEngineConstants.SUCCESS, BatchProcessEngineConstants.TRUE,
					BatchProcessEngineConstants.STRING));
		} else {
			res.addParam(new Param(BatchProcessEngineConstants.SUCCESS, BatchProcessEngineConstants.FALSE,
					BatchProcessEngineConstants.STRING));
			res.addParam(new Param(BatchProcessEngineConstants.DBPERRMSG, msg, BatchProcessEngineConstants.STRING));
		}
		return res;
	}

	public static String getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime()).toString();

	}

	public static String getSchemaname() {
		return schemaname;
	}

	public static void setSchemaname(String schemaname) {
		BatchProcessEngineHelperMethods.schemaname = schemaname;
	}

}
