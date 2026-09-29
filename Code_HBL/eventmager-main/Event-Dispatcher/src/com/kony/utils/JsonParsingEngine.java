package com.kony.utils;

import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonParsingEngine {
	private JsonParsingEngine() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static void getAllPairsFromJson(JsonObject jsonObj, Map<String, String> resMap) {
		if (jsonObj == null || !jsonObj.isJsonObject() || jsonObj.isJsonNull())
			return;
		JsonObject resobj = new JsonObject();
		auxGetAllPairsFromJson(jsonObj, resobj);
		insertIntoMap(resMap, resobj);

	}

	private static void insertIntoMap(Map<String, String> resmap, JsonObject resobj) {
		Set<Map.Entry<String, JsonElement>> entries = resobj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			try {
				resmap.put(entry.getKey().toLowerCase(), resobj.get(entry.getKey()).getAsString());
			} catch (Exception e) {
				alert.prepareError("Error occured", e).log();
			}
		}

	}

	private static void auxGetAllPairsFromJson(JsonElement element, JsonObject resobj) {
		if (element == null || element.isJsonNull())
			return;
		if (element.isJsonObject()) {
			buildingFromJsonObject(element, resobj);
		} else {
			buildingFromNonJsonObject(element, resobj);
		}

	}

	private static void buildingFromNonJsonObject(JsonElement element, JsonObject resobj) {
		try {

			JsonElement eventsElement = new JsonParser().parse(element.toString());
			if (eventsElement.isJsonArray()) {
				JsonArray eventsjsonarray = eventsElement.getAsJsonArray();
				callAuxService(eventsjsonarray, resobj);
			} else if (eventsElement.isJsonObject()) {
				auxGetAllPairsFromJson(eventsElement, resobj);
			}
		} catch (Exception e) {
			diagnostic.prepareDebug(URLConstants.EXCEPTION, e).log();
		}

	}

	private static void callAuxService(JsonArray eventsjsonarray, JsonObject resobj) {
		for (int i = 0; i < eventsjsonarray.size(); i++) {
			try {
				auxGetAllPairsFromJson(eventsjsonarray.get(i), resobj);
			} catch (Exception e) {
				diagnostic.prepareDebug(e.toString()).log();
			}
		}

	}

	private static void buildingFromJsonObject(JsonElement element, JsonObject resobj) {
		JsonObject obj = element.getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			String key = entry.getKey();
			JsonElement val = entry.getValue();
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
					JsonElement eventsElement = new JsonParser().parse(val.toString());
					auxGetAllPairsFromJson(eventsElement, resobj);
				} catch (Exception e) {
					diagnostic.prepareDebug(e.toString()).log();
				}
			}
		}

	}

}
