package com.kony.audit.auditutils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonParsingEngine {
	private JsonParsingEngine() {
	}

	public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		if (object == null || object.isJsonNull())
			return null;
		JsonElement element = object.get(key);
		if ((element == null || element.isJsonNull()) && (required)) {
			throw new IllegalArgumentException("Required attribute '" + key + "' not present in event");
		}
		return element;
	}

	public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {

		try {
			JsonElement element = getElementFromJsonObject(object, key, required);
			return (element == null || element.isJsonNull()) ? null : element.getAsString();
		} catch (Exception e) {
			return null;
		}

	}

}
