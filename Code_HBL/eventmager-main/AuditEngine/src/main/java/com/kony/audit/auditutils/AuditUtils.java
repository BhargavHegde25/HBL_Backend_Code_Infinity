package com.kony.audit.auditutils;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class AuditUtils {
	private AuditUtils() {
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String mainschemaname = null;
	private static String logschemaname = null;

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static Map<String, Object> convertJsonToMap(JsonObject json) {
		Map<String, Object> map = new HashMap<>();
		if (json == null || json.isJsonNull())
			return map;
		for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>) json.entrySet()) {
			try {
				String key = entry.getKey();
				JsonElement val = entry.getValue();
				if (val != null && !val.isJsonNull()) {
					if (key.equals("eventts") || key.contains("Date") || key.contains("date")) {
						if (isDateValid(val.getAsString()))
							map.put(key, parseDate(val.getAsString()));
						continue;
					}
					map.put(key, val.getAsString());
				}
			} catch (Exception e) {
				diagnostic.prepareDebug("Error occured ", e).log();
			}
		}
		return map;
	}

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

	public static JsonObject getJsonObjects(String key, JsonObject event, boolean required) {
		JsonObject eventobj = event.getAsJsonObject();
		JsonElement tempele = null;
		JsonObject eventdata = null;
		tempele = JsonParsingEngine.getElementFromJsonObject(eventobj, key, required);
		eventdata = tempele.getAsJsonObject();
		return eventdata;
	}

	public static String getJsonObjects(JsonObject event, String key, boolean required) {
		JsonObject eventobj = event.getAsJsonObject();
		String tempele = null;
		try {
			tempele = JsonParsingEngine.getStringFromJsonObject(eventobj, key, required);
			if (tempele == null)
				return null;
			if (tempele.equals("") && (key.equalsIgnoreCase("eventId") || key.equalsIgnoreCase("eventType")
					|| key.equalsIgnoreCase("eventSubType") || key.equalsIgnoreCase("Status_id")
					|| key.equalsIgnoreCase("user") || key.equalsIgnoreCase("customerId")
					|| key.equalsIgnoreCase("accountnumber")))
				return null;
		} catch (Exception e) {
			return null;
		}
		return tempele;
	}

	public static Result returnResult(boolean success, String dbperrmsg) {
		Result result = new Result();
		if (success) {
			result.addParam(new Param(AuditConstants.SUCCESS, AuditConstants.TRUE, AuditConstants.STRING));
			if (!dbperrmsg.equals(""))
				result.addParam(new Param(AuditConstants.DBPERRMSG, dbperrmsg, AuditConstants.STRING));
			return result;
		}
		result.addParam(new Param(AuditConstants.SUCCESS, AuditConstants.FALSE, AuditConstants.STRING));
		result.addParam(new Param(AuditConstants.DBPERRMSG, dbperrmsg, AuditConstants.STRING));
		return result;
	}

	public static boolean isTokenValid(String events, String token) {
		String generatedtoken = deriveToken(events);
		if (generatedtoken == null)
			return false;
		return generatedtoken.equals(token);
	}

	public static boolean isDateValid(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			simpleDateFormat.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static Date parseDate(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	private static String deriveToken(String events) {
		try {
			String secret = getConfigProperty("QUEUEMASTER_SHARED_SECRET");
			if (secret == null || secret.length() == 0)
				return null;
			String eventsHash = Hashing.sha512().hashString(events, StandardCharsets.UTF_8).toString();
			String saltedSecret = eventsHash + secret;
			return Hashing.sha512().hashString(saltedSecret, StandardCharsets.UTF_8).toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}

	public static String replaceSchemaName(String operationid, String schemaname) {
		if (operationid == null || schemaname == null)
			return operationid;
		if (operationid.contains("{schema_name}"))
			operationid = operationid.replace("{schema_name}", schemaname);
		return operationid;

	}

	public static JsonElement parseString(String events) {
		JsonElement eventsElement = null;
		try {
			eventsElement = (new JsonParser()).parse(events);
		} catch (Exception e) {
			alert.prepareError("Error occured in parsing:", e).log();
		}
		return eventsElement;
	}

	public static String getMainSchemaname() {
		return mainschemaname;
	}

	public static void setMainSchemaname(String mainschemaname) {
		AuditUtils.mainschemaname = mainschemaname;
	}

	public static String getLogSchemaname() {
		return logschemaname;
	}

	public static void setLogSchemaname(String logschemaname) {
		AuditUtils.logschemaname = logschemaname;
		if (logschemaname == null)
			AuditUtils.logschemaname = "dbxlogs";

	}
}
