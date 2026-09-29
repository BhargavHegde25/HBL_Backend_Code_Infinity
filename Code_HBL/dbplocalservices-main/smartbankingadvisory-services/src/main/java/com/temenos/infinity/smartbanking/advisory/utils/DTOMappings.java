package com.temenos.infinity.smartbanking.advisory.utils;

import java.util.HashMap;
import java.util.Map;

public class DTOMappings {

    private static Map<Class<?>, Map<String, String>> dtoObjectPropertyMappings =
            new HashMap<Class<?>, Map<String, String>>();

    public static Map<String, String> getDTOObjectPropertyMappings(Class<?> className) {
        return dtoObjectPropertyMappings.get(className);
    }
}
