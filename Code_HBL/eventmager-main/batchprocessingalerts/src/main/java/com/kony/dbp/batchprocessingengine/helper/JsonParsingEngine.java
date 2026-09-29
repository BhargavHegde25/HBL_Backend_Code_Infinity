package com.kony.dbp.batchprocessingengine.helper;

import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonParsingEngine {
	private JsonParsingEngine() {
	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		if (object == null)
			return null;
		JsonElement element = object.get(key);
		if ((element == null) && (required)) {
			throw new IllegalArgumentException("Required attribute '" + key + "' not present in event");
		}
		return element;
	}

	public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
		try {
			JsonElement element = getElementFromJsonObject(object, key, required);
			return element == null ? null : element.getAsString();
		} catch (Exception e) {
			diagnostic.prepareInfo("Error occured", e).log();
		}
		return null;
	}

	public static void auxGetAllPairsFromJson(JsonElement element, JsonObject resobj) {
		if (element == null || element.isJsonNull())
			return;
		if (element.isJsonObject()) {
			buildingFromJsonObject(element, resobj);
		} else {
			buildingFromNonJsonObject(element, resobj);
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
					JsonElement eventsElement = new JsonParser().parse(val.getAsString());
					auxGetAllPairsFromJson(eventsElement, resobj);
				} catch (Exception e) {
					diagnostic.prepareInfo("").log();
				}
			}
		}
	}

	private static void buildingFromNonJsonObject(JsonElement element, JsonObject resobj) {
		try {

			JsonElement eventsElement = new JsonParser().parse(element.getAsString());
			if (eventsElement.isJsonArray()) {
				JsonArray eventsjsonarray = eventsElement.getAsJsonArray();
				callAuxService(eventsjsonarray, resobj);
			} else if (eventsElement.isJsonObject()) {
				auxGetAllPairsFromJson(eventsElement, resobj);
			}
		} catch (Exception e) {
			diagnostic.prepareInfo("").log();
		}

	}

	private static void callAuxService(JsonArray eventsjsonarray, JsonObject resobj) {
		for (int i = 0; i < eventsjsonarray.size(); i++) {
			try {
				auxGetAllPairsFromJson(eventsjsonarray.get(i), resobj);
			} catch (Exception e) {
				diagnostic.prepareInfo("").log();
			}
		}
	}

}
