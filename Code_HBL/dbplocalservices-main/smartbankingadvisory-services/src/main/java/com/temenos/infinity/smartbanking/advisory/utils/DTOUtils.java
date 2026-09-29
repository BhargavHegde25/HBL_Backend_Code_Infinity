package com.temenos.infinity.smartbanking.advisory.utils;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.temenos.infinity.smartbanking.advisory.utils.DTOMappings;

public class DTOUtils {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	public static void loadInputIntoDTO(Object object, Map<String, Object> inputParams, boolean withMappings) {
        Field[] fields = object.getClass().getDeclaredFields();

        Map<String, String> mappings = null;
        if (withMappings) {
            mappings = DTOMappings.getDTOObjectPropertyMappings(object.getClass());
        }

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            if (withMappings && mappings != null && mappings.containsKey(fieldName)) {
                fieldName = mappings.get(fieldName);
            }
            String value = inputParams.containsKey(fieldName) ? inputParams.get(fieldName).toString(): "";
            try {
                if (StringUtils.isNotBlank(value)) {
                    if (field.getType().equals(int.class)) {
                        field.setInt(object, Integer.parseInt(value));
                    } else if (field.getType().equals(String.class)) {
                        field.set(object, value);
                    } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                        field.set(object, Boolean.parseBoolean(value));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                alert.prepareError("Caught exception while converting Object to map: ", e).log();
            }

            field.setAccessible(false);
        }
    }
	
	public static JsonObject getJsonObjectFromObject(Object object) {
        JsonObject jsonObject = new JsonObject();
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String fieldName = field.getName();
            try {
                String value = "";
                if (field.get(object) != null) {
                    if (field.getType().equals(int.class)) {
                        jsonObject.addProperty(fieldName, field.getInt(object));
                    } else if (field.getType().equals(String.class) &&  StringUtils.isNotBlank(String.valueOf(field.get(object)))) {
                        jsonObject.addProperty(fieldName, String.valueOf(field.get(object)));
                    } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                        jsonObject.addProperty(fieldName, String.valueOf(field.get(object)));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                alert.prepareError("Caught exception while converting Object to Json: ", e).log();
            }
            field.setAccessible(false);
        }
        return jsonObject;
    }
	
}
