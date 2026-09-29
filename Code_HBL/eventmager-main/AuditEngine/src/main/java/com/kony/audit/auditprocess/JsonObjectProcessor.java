package com.kony.audit.auditprocess;

import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.audit.auditutils.AuditConstants;
import com.kony.audit.auditutils.AuditUtils;

public class JsonObjectProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static void fetchAndInsertTransactionTypeIdintoJson(JsonObject tempjson,
			Map<String, String> transactiontypedata) {
		String transactionType = null;

		if (tempjson == null || tempjson.isJsonNull() || !tempjson.isJsonObject())
			return;
		if (tempjson.getAsJsonObject(AuditConstants.REQINPUT) == null
				|| tempjson.getAsJsonObject(AuditConstants.REQINPUT).get(AuditConstants.TRANSACTIONTYPE) == null)
			return;
		JsonObject requestInput = tempjson.getAsJsonObject(AuditConstants.REQINPUT);
		transactionType = tempjson.getAsJsonObject(AuditConstants.REQINPUT).get(AuditConstants.TRANSACTIONTYPE)
				.getAsString();
		if (!transactiontypedata.containsKey(transactionType)) {
			requestInput.addProperty(AuditConstants.TRANSACTIONTYPE, "-1");
			return;
		}
		requestInput.addProperty(AuditConstants.TRANSACTIONTYPE, transactiontypedata.get(transactionType));
	}

	public static void getAllPairsFromJson(JsonElement eventjsonelem, Map<String, String> resmap, String typeofdata,
			Map<String, String> transactiontypedata) {
		try {
			if (eventjsonelem == null || eventjsonelem.isJsonNull() || !eventjsonelem.isJsonObject())
				return;
			JsonObject eventobj = eventjsonelem.getAsJsonObject();
			JsonElement tempelem = null;
			if (typeofdata.equals(AuditConstants.EVENTDATA)) {
				tempelem = eventobj.get(AuditConstants.EVENTDATA);
				if (tempelem != null && !tempelem.isJsonNull())
					fetchAndInsertTransactionTypeIdintoJson(tempelem.getAsJsonObject(), transactiontypedata);
			} else if (typeofdata.equals(AuditConstants.OTHERDATA)) {
				tempelem = eventobj.get(AuditConstants.OTHERDATA);
			} else if (typeofdata.equals(AuditConstants.SESSION)) {
				tempelem = eventobj.get(AuditConstants.SESSION);
			}
			JsonObject resobj = new JsonObject();
			if (tempelem == null)
				return;
			auxGetAllPairsFromJson(tempelem, resobj);
			Set<Map.Entry<String, JsonElement>> entries = resobj.entrySet();
			for (Map.Entry<String, JsonElement> entry : entries) {
				resmap.put(entry.getKey().toLowerCase(), resobj.get(entry.getKey()).getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
	}

	private static void procecssJsonObject(JsonElement element, JsonObject resobj) {
		JsonObject obj = element.getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			String key = entry.getKey();
			JsonElement val = entry.getValue();
			try {
				if (resobj.has(key) && StringUtils.isNotBlank(resobj.get(key).getAsString()))
					continue;
			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
			}

			if (val == null || val.isJsonNull())
				continue;
			try {
				resobj.addProperty(key, val.getAsString());
			} catch (Exception e) {
				resobj.addProperty(key, val.toString());
			}
			if (val.isJsonObject()) {
				auxGetAllPairsFromJson(entry.getValue(), resobj);
			} else {
				try {
					JsonElement eventsElement = new JsonParser().parse(val.getAsString());
					auxGetAllPairsFromJson(eventsElement, resobj);
				} catch (Exception e) {
					diagnostic.prepareInfo("ParsingError").log();
				}
			}
		}
	}

	private static void processNonJsonObject(JsonElement element, JsonObject resobj) {
		try {
			JsonElement eventsElement = new JsonParser().parse(element.getAsString());
			if (eventsElement.isJsonArray()) {
				JsonArray eventsjsonarray = eventsElement.getAsJsonArray();
				for (int i = 0; i < eventsjsonarray.size(); i++)
					auxGetAllPairsFromJson(eventsjsonarray.get(i), resobj);
			} else if (eventsElement.isJsonObject()) {
				auxGetAllPairsFromJson(eventsElement, resobj);
			}
		} catch (Exception e) {
			diagnostic.prepareInfo("parsing error").log();
		}
	}

	private static void auxGetAllPairsFromJson(JsonElement element, JsonObject resobj) {

		try {
			if (element == null || element.isJsonNull())
				return;
			if (element.isJsonObject()) {
				procecssJsonObject(element, resobj);
			} else {
				processNonJsonObject(element, resobj);
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("").log();
		}
	}

	public void fetchParamsFromPayload(JsonElement event, String typeofdata, Map<String, String> transactiontypedata,
			Map<String, String> returnmap) {
		JsonObject tempjson = null;
		if (typeofdata.equals(AuditConstants.EVENTDATA)) {
			try {
				tempjson = AuditUtils.getJsonObjects(AuditConstants.EVENTDATA, event.getAsJsonObject(), false);
				fetchAndInsertTransactionTypeIdintoJson(tempjson, transactiontypedata);
				if (tempjson != null && !tempjson.isJsonNull())
					fetchAlertContentFieldsFromEventData(tempjson, returnmap);
			} catch (Exception e) {
				diagnostic.prepareDebug("Parameter Fetch error", e).log();
			}

		} else if (typeofdata.equals(AuditConstants.OTHERDATA)) {
			try {
				tempjson = AuditUtils.getJsonObjects(AuditConstants.OTHERDATA, event.getAsJsonObject(), false);
				if (tempjson != null && !tempjson.isJsonNull())
					fetchAlertContentFieldsFromAJson(tempjson, returnmap);
			} catch (Exception e) {
				diagnostic.prepareDebug("parameter fetch error", e).log();
			}

		} else if (typeofdata.equals(AuditConstants.SESSION)) {
			try {
				String sessionstr = AuditUtils.getJsonObjects(event.getAsJsonObject(), AuditConstants.SESSION, false);
				JsonElement eventsElement = new JsonParser().parse(sessionstr);
				tempjson = eventsElement.getAsJsonObject();
			} catch (Exception e) {
				try {
					tempjson = AuditUtils.getJsonObjects(AuditConstants.SESSION, event.getAsJsonObject(), false);
				} catch (Exception e2) {
					diagnostic.prepareDebug("parameter fetch error", e).log();
				}

			}
			if (tempjson != null && !tempjson.isJsonNull())
				fetchAlertContentFieldsFromAJson(tempjson, returnmap);

		}

	}

	public void fetchAlertContentFieldsFromAJson(JsonObject otherdata, Map<String, String> alertcontentfields) {
		Set<Map.Entry<String, JsonElement>> entries = otherdata.entrySet();// will return members of your object

		for (Map.Entry<String, JsonElement> entry : entries) {
			try {
				alertcontentfields.put(entry.getKey().toLowerCase(),
						AuditUtils.getJsonObjects(otherdata, entry.getKey(), false));
			} catch (Exception e) {
				alert.prepareError("Error occured", e).log();
			}
		}
	}

	private void fetchAlertContentFieldsFromEventData(JsonObject eventdata, Map<String, String> alertcontentfields) {

		try {
			JsonObject requestinput = AuditUtils.getJsonObjects(AuditConstants.REQINPUT, eventdata, false);
			fetchAlertContentFieldsFromAJson(requestinput, alertcontentfields);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in processing request input parameters", e).log();
		}
		try {
			JsonObject requestoutput = AuditUtils.getJsonObjects(AuditConstants.RESOUTPUT, eventdata, false);
			fetchAlertContentFieldsFromAJson(requestoutput, alertcontentfields);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in processing response output parameters", e).log();
		}
		try {
			JsonObject customparams = AuditUtils.getJsonObjects(AuditConstants.CUSTOMPARAMS, eventdata, false);
			fetchAlertContentFieldsFromAJson(customparams, alertcontentfields);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in processing custom parameters", e).log();
		}
	}
}
