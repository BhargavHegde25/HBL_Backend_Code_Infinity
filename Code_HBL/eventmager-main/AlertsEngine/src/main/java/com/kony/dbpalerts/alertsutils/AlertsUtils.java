package com.kony.dbpalerts.alertsutils;

import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbpalerts.httputils.HttpConnector;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public final class AlertsUtils {

	private AlertsUtils() {
	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static Result callInternalService(Map<String, Object> requestParameters, String serviceid,
			String operationid, String objectid) {
		if (serviceid == null || operationid == null)
			return new Result();
		try {
			DBPServiceExecutorBuilder db = DBPServiceExecutorBuilder.builder().withServiceId(serviceid)
					.withOperationId(operationid);
			if (objectid != null)
				db = db.withObjectId(objectid);
			return db.withRequestParameters(requestParameters).build().getResult();
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in calling service", e).log();
		}
		return new Result();
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

	public static JsonObject callhttpApi(Map inputparams, Map headerparams, String url)
			throws com.kony.dbpalerts.httputils.HttpCallException {

		JsonObject response = HttpConnector.invokeHttpPost(url, inputparams, headerparams);
		return (null == response) ? new JsonObject() : response;
	}

	public static String getJsonObjects(JsonObject event, String key, boolean required) {
		JsonObject eventobj = event.getAsJsonObject();
		String tempele = null;
		tempele = JsonParsingEngine.getStringFromJsonObject(eventobj, key, required);
		if (tempele == null)
			return null;
		if (tempele.equals("") && (key.equalsIgnoreCase("eventId") || key.equalsIgnoreCase("eventType")
				|| key.equalsIgnoreCase("eventSubType") || key.equalsIgnoreCase("Status_id")
				|| key.equalsIgnoreCase("user") || key.equalsIgnoreCase("customerId")
				|| key.equalsIgnoreCase("accountnumber"))) {
			return null;
		}

		return tempele;
	}

	public static JsonObject getJsonObjects(String key, JsonObject event, boolean required) {
		JsonObject eventdata = null;
		try {
			JsonObject eventobj = event.getAsJsonObject();
			JsonElement tempele = null;

			tempele = JsonParsingEngine.getElementFromJsonObject(eventobj, key, required);
			if (tempele == null)
				return null;
			eventdata = tempele.getAsJsonObject();
			return eventdata;
		} catch (Exception e) {
			return eventdata;
		}

	}

	public static String callInternalServiceAndGetJson(Map<String, Object> inputparams, String serviceid,
			String operationid, String objectid) {
		if (serviceid == null || operationid == null)
			return null;
		try {
			DBPServiceExecutorBuilder db = DBPServiceExecutorBuilder.builder().withServiceId(serviceid)
					.withOperationId(operationid);
			if (objectid != null)
				db = db.withObjectId(objectid);
			return db.withRequestParameters(inputparams).build().getResponse();
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in calling service", e).log();
		}
		return "";
	}

}