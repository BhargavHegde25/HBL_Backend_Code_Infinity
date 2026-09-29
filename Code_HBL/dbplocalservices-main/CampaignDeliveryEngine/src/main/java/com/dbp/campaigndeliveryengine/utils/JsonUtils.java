package com.dbp.campaigndeliveryengine.utils;

import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {
	private JsonUtils() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static void getAllPairsFromJson(JsonElement eventjsonelem, Map<String, String> resmap) {
		try {
			if (eventjsonelem == null || eventjsonelem.isJsonNull() || !eventjsonelem.isJsonObject())
				return;
			JsonObject eventobj = eventjsonelem.getAsJsonObject();
			JsonElement tempelem = null;

			tempelem = eventobj.get(CampaignDeliveryEngineConstants.EVENTDATA);

			JsonObject resobj = new JsonObject();
			if (tempelem == null)
				return;
			auxGetAllPairsFromJson(tempelem, resobj);
			insertIntoMap(resmap, resobj);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
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

	private static void callAuxService(JsonArray eventsjsonarray, JsonObject resobj) {
		for (int i = 0; i < eventsjsonarray.size(); i++) {
			try {
				auxGetAllPairsFromJson(eventsjsonarray.get(i), resobj);
			} catch (Exception e) {
				diagnostic.prepareInfo("").log();
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

}
