package com.kony.dbputilities.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class JSONUtil {

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
}
