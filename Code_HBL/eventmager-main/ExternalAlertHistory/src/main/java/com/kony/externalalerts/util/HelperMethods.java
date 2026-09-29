package com.kony.externalalerts.util;

import java.util.Map;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class HelperMethods {
	private HelperMethods() {
		
	}
	
	public static Result returnResult(boolean flag, ExternalAlertsEnum err) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(ExternalAlertsConstants.SUCCESS, ExternalAlertsConstants.TRUE, ExternalAlertsConstants.STRING));
		} else {
			res.addParam(new Param(ExternalAlertsConstants.SUCCESS, ExternalAlertsConstants.FALSE, ExternalAlertsConstants.STRING));
			res.addParam(new Param(ExternalAlertsConstants.DBPERRMSG, err.getErrMsg(), ExternalAlertsConstants.STRING));
			res.addParam(new Param(ExternalAlertsConstants.DBPERRCODE, err.getErrCode(), ExternalAlertsConstants.STRING));
		}
		return res;
	}
	
	public static String callInternalService(Map<String, Object> requestParameters, String serviceid,
			String operationid, String objectid) {
		if (serviceid == null || operationid == null)
			return null;
		try {
			DBPServiceExecutorBuilder db = DBPServiceExecutorBuilder.builder().withServiceId(serviceid)
					.withOperationId(operationid);
			if (objectid != null)
				db = db.withObjectId(objectid);
			return db.withRequestParameters(requestParameters).build().getResponse();
		} catch (Exception e) {

		}
		return null;
	}
	
	public static String getConfigProperty(String key) {
		try {
			return EnvironmentConfigurationsHandler.getServerAppProperty(key);
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String replaceSchemaName(String operationid, String schemaname) {
		if (operationid == null)
			return operationid;
		if (operationid.contains("{schema_name}"))
			operationid = operationid.replace("{schema_name}", schemaname);
		return operationid;

	}

}
