package com.dbp.reminderengine.utils;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class HelperMethods {

	private HelperMethods() {
	}

	public static Result returnResult(boolean flag, String msg) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		} else {
			res.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRMSG, msg, Constants.STRING));
		}
		return res;
	}
	
	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}
	
	public static String replaceSchemaName(String operationid, String schemaname) {
		if (operationid == null)
			return operationid;
		if (operationid.contains("{schema_name}"))
			operationid = operationid.replace("{schema_name}", schemaname);
		return operationid;

	}

}
