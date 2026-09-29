package com.kony.dbputilities.util;

import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class JSONUtil {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static boolean hasKey(JsonObject jsonObject, String key) {
        return (isJsonNotNull(jsonObject) && jsonObject.has(key) && isJsonNotNull(jsonObject.get(key)));
    }
    
    public static String getString(JsonObject jsonObject, String key) {
       return hasKey(jsonObject, key)? jsonObject.get(key).getAsString() : "";
    }
    
    public static JsonArray getJsonArrary(JsonObject jsonObject, String key) {
        if(hasKey(jsonObject, key) &&
        		jsonObject.get(key).isJsonArray()) {
        	return jsonObject.getAsJsonArray(key);
        }
        return null;
     }

    public static boolean isJsonNull(JsonElement jsonElement) {
        return (jsonElement == null || jsonElement.isJsonNull());
    }

    public static boolean isJsonNotNull(JsonElement jsonElement) {
        return !(isJsonNull(jsonElement));
    }

    public static boolean isJsonValueCorrect(JsonObject jsonObject, String key, String value) {
        boolean haskey = (isJsonNotNull(jsonObject) && jsonObject.has(key) && isJsonNotNull(jsonObject.get(key)));
        return haskey && jsonObject.get(key).getAsString().equals(value);
    }
    
    public static JsonObject parseAsJsonObject(String input) {
		try {
			return new JsonParser().parse(input).getAsJsonObject();
		} catch (Exception e) {
			alert.prepareError("unable to convert string to object", e).log();
		}
		return null;
	}
    
    public static JsonArray parseAsJsonArray(String input) {
		try {
			return new JsonParser().parse(input).getAsJsonArray();
		} catch (Exception e) {
			alert.prepareError("unable to convert string to object", e).log();
		}
		return null;
	}
    
    public static String getJSONString(Set<String> set) {
        return (new JSONArray(set.toString())).toString();
    }
}
