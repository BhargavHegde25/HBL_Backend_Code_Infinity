package com.kony.kmsinvoke.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonParsingEngine {
	private JsonParsingEngine() {
	}


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
			//Do Nothing
		}
		return null;
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

}
