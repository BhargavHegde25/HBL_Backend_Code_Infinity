package com.temenos.dbx.product.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@JsonInclude(Include.NON_NULL)
public class ExtensionData implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -6810811736374057213L;

    Map<String, String> map = new HashMap<String, String>();

    /**
     * @return the field
     */
    public String getField(String key) {
        return map.get(key);
    }
    /**
     * @param field1 the field to set
     */
    public void setField(String field, String value) {
        map.put(field, value);
    }


    public JsonObject toStringJson() {
        JsonObject jsonObject = new JsonObject();
        
        for(Entry<String, String> entry : map.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }

        return jsonObject;
    }
    public void loadFromJson(JsonObject jsonObject) {
        
        for(Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAsString());
        }
    }
}
