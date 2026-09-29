package com.kony.dbputilities.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultCacheImpl {

    Map<String, String> map = null;

    static ResultCacheImpl cache = null;

    public static ResultCacheImpl getInstance() {

        if (cache == null) {
            cache = new ResultCacheImpl();
        }

        return cache;
    }

    public void insertIntoCache(String arg0, String arg1) {

        if (map == null) {
            map = new ConcurrentHashMap<>();
        }
        map.put(arg0, arg1);
    }

    public String retrieveFromCache(String arg0) {

        if (map != null) {
            return map.get(arg0);
        }
        return "";
    }

    public Object removeFromCache(String arg0) {

        if (map != null) {
            return map.remove(arg0);
        }
        return "";
    }

}
