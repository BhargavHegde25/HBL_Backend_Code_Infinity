package com.dbp.combinedaccessalerts.util;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class HelperMethods {
	private HelperMethods() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static boolean isValidInput(String input) {
		if (input == null || input.equals("")) {
			return false;
		}
		return true;
	}

	public static Result returnResult(boolean flag, String err) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		} else {
			res.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRMSG, err, Constants.STRING));
		}
		return res;
	}

	public static String replaceSchemaName(String operationid, String schemaname) {
		if (operationid == null)
			return operationid;
		if (operationid.contains("{schema_name}"))
			operationid = operationid.replace("{schema_name}", schemaname);
		return operationid;

	}

	public static String getConfigProperty(String key) {
		if (key == null || key.equals(""))
			return null;
		String value = null;
		try {
			value = EnvironmentConfigurationsHandler.getServerAppProperty(key);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in fetching environment config variable " + key + ":", e).log();
		}
		return value;
	}
}
