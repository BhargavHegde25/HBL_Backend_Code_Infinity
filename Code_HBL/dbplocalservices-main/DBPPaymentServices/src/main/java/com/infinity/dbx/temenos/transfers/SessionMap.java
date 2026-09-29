package com.infinity.dbx.temenos.transfers;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SessionMap implements Serializable {
    private static final long serialVersionUID = -9051925743215391378L;
    private Map<String, Map<String, String>> data = new HashMap<>();

    public void addAttributeForKey(String key, String attribute, String attributeValue) {
        data.putIfAbsent(key, new HashMap<String, String>());
        data.get(key).put(attribute, attributeValue);
    }

    public void addKey(String key) {
        data.put(key, new HashMap<String, String>());
    }

    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    public Map<String, String> getValue(String key) {
        return data.get(key);
    }

    public boolean isEmpty() {
        return data == null || data.size() <= 0;
    }

    public String getAttributeValueForKey(String key, String attribute) {
        return (data.get(key) != null) ? data.get(key).get(attribute) : null;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public void setData(String data) {
        if (null == data) {
            return;
        }
        Gson gson = new Gson();
        Type sessionMapType = new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
        Map<String, Map<String, String>> retirved = gson.fromJson(data, sessionMapType);
        this.data = retirved;
    }
}

