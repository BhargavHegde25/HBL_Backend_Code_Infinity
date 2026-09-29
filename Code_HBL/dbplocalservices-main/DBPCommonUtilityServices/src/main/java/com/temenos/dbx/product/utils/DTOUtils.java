package com.temenos.dbx.product.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.api.DBPDTOEXT;

public class DTOUtils {
    private static Alert alert;
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static void initLOG() {
        if (alert == null) {
            alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        }
    }

    public static Map<String, Object> getParameterMap(Object object, boolean withMappings) {
        Map<String, Object> map = new HashMap<String, Object>();
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

            try {
                if (field.get(object) != null) {

                    String value = "";

                    if (field.getType().equals(int.class)) {
                        value = field.getInt(object) + "";
                    } else if (field.getType().equals(String.class)) {
                        value = String.valueOf(field.get(object));
                    } else if (field.getType().equals(boolean.class)) {
                        value = String.valueOf(field.getBoolean(object));
                    } else if (field.getType().equals(Boolean.class)) {
                        value = String.valueOf(field.get(object));
                    }

                    if (fieldName != null) {
                        if(HelperMethods.isValidDateFormat(value)){
                            map.put(fieldName, HelperMethods.getFormattedLocaleDate(value, "yyyy-MM-dd"));
                        }
                        else {
                            map.put(fieldName, value);
                        }
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                alert.prepareError("Caught exception while converting Object to map: ", e).log();
            }

            field.setAccessible(false);
        }

        return map;
    }

    public static void loadInputIntoDTO(Object object, Map<String, String> inputParams, boolean withMappings) {
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
            String value = inputParams.get(fieldName);
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
        initLOG();
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

    public static JsonObject getJsonObjectFromObject(Object object, boolean withMappings) {
        initLOG();
        JsonObject jsonObject = new JsonObject();
        Field[] fields = object.getClass().getDeclaredFields();
        Map<String, String> map = DTOMappings.getDTOObjectPropertyMappings(object.getClass());


        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            if(withMappings && map != null && map.containsKey(fieldName)) {
                fieldName = map.get(fieldName);
            }

            try {

                if (field.get(object) != null) {
                    if (field.getType().equals(int.class)) {
                        jsonObject.addProperty(fieldName, field.getInt(object));
                    } else if (field.getType().equals(String.class)) {
                        jsonObject.addProperty(fieldName, String.valueOf(field.get(object)));
                    } else if (field.getType().equals(boolean.class)) {
                        jsonObject.addProperty(fieldName, String.valueOf(field.getBoolean(object)));
                    } else if (field.getType().equals(Boolean.class)) {
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

    public static JsonObject getJsonObjectFromObjectPrint(Object object) {
        initLOG();
        JsonObject jsonObject = new JsonObject();
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String fieldName = field.getName();
            try {

                if (field.get(object) != null) {
                    if (field.getType().equals(int.class)) {
                        jsonObject.addProperty(fieldName, field.getInt(object));
                    } else if (field.getType().equals(String.class)) {
                        jsonObject.addProperty(fieldName, String.valueOf(field.get(object)));
                    } else if (field.getType().equals(boolean.class)) {
                        jsonObject.addProperty(fieldName, String.valueOf(field.getBoolean(object)));
                    } else if (field.getType().equals(Boolean.class)) {
                        jsonObject.addProperty(fieldName, String.valueOf(field.get(object)));
                    } else if (field.getType().equals(List.class)) {

                        List<?> list = (List<?>) field.get(object);

                        if (list != null && list.size() > 0) {
                            JsonArray array = new JsonArray();
                            for (Object object2 : list) {
                                array.add(getJsonObjectFromObject(object2));
                            }

                            jsonObject.add(fieldName, array);
                        }
                    } else if (!Modifier.isFinal(field.getModifiers())) {
                        Object object2 = field.get(object);
                        jsonObject.add(fieldName, getJsonObjectFromObject(object2));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                alert.prepareError("Caught exception while converting Object to Json: ", e).log();
            }
            field.setAccessible(false);
        }
        return jsonObject;
    }

    public static Object getDTOFromRecord(Record record, Class<?> className, boolean withMappings, boolean withDateFormat) {
        initLOG();
        Object object = null;
        try {
            object = className.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {

            alert.prepareError("Caught exception while getting instance from class : ", e).log();
        }

        if (object != null) {
            Map<String, String> map = new HashMap<String, String>();
            if (withMappings) {
                map = DTOMappings.getDTOObjectPropertyMappings(className);
            }
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                if (withMappings && map != null && map.containsKey(fieldName)) {
                    fieldName = map.get(fieldName);
                }
                if (record.getNameOfAllParams().contains(fieldName)
                        && StringUtils.isNotBlank(record.getParamValueByName(fieldName))) {
                    field.setAccessible(true);
                    try {
                        if (field.getType().equals(int.class)) {
                            field.set(object, Integer.parseInt(record.getParamValueByName(fieldName)));
                        } else if (field.getType().equals(String.class)) {
                            field.set(object, record.getParamValueByName(fieldName));
                        } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                            field.set(object, Boolean.parseBoolean(record.getParamValueByName(fieldName)));
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        alert.prepareError("Caught exception while setting value to field: ", e).log();
                    }
                    field.setAccessible(false);
                }

            }
        }

        return object;
    }


    public static Record getRecordFromDTO(Object object, boolean withMappings) {
        initLOG();

        if (object != null) {
            Map<String, String> map = new HashMap<String, String>();
            Record record = new Record();

            if (withMappings) {
                map = DTOMappings.getDTOObjectPropertyMappings(object.getClass());
            }
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                if (withMappings && map != null && map.containsKey(fieldName)) {
                    fieldName = map.get(fieldName);
                }

                field.setAccessible(true);

                try {
                    if(field.get(object) != null) {
                        if (field.getType().equals(int.class)) {
                            record.addParam(fieldName, ""+((int) field.get(object)), Param.INT_CONST);
                        } else if (field.getType().equals(String.class)) {
                            record.addParam(fieldName, ""+((String) field.get(object)), Param.STRING_CONST);
                        } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                            record.addParam(fieldName, ""+((boolean) field.get(object)), Param.BOOLEAN_CONST);
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    alert.prepareError("Caught exception while setting value to field: ", e).log();
                }
                field.setAccessible(false);

            }

            return record;
        }

        return null;
    }


    public static List<DBPDTO> getDTOListfromDB(DataControllerRequest dcRequest, String filterQuery, String url,
            boolean withMappings, boolean withDateFormat) {

        initLOG();
        List<DBPDTO> dbpdtos = new ArrayList<DBPDTO>();
        Map<String, Object> inputParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(filterQuery)) {
            inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
        }
        Result result = new Result();
        try {
            result = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, inputParams,
                    HelperMethods.getHeaders(dcRequest), url);
        } catch (HttpCallException e) {
            alert.prepareError("Caught exception while creating Customer: ", e).log();
        }

        if (HelperMethods.hasRecords(result)) {
            Class<?> className = DTOMappings.getDTOCalssFromTableName(url);
            Dataset dataset = HelperMethods.getDataSet(result);
            Object object = null;
            for (Record record : dataset.getAllRecords()) {
                object = getDTOFromRecord(record, className, withMappings, withDateFormat);
                if (object != null) {
                    dbpdtos.add((DBPDTO) object);
                }
            }
        }
        return dbpdtos;
    }

    public static List<DBPDTO> getDTOListfromDB(Map<String, Object> headerMap, String filterQuery, String url,
            boolean withMappings, boolean withDateFormat) {

        initLOG();
        List<DBPDTO> dbpdtos = new ArrayList<DBPDTO>();
        Map<String, Object> inputParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(filterQuery)) {
            inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
        }
        Result result = new Result();
        try {
            result = ServiceCallHelper.invokeServiceAndGetResult(inputParams, new HashMap<String, String>(), url,
                    (String) headerMap.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE));
        } catch (HttpCallException e) {
            alert.prepareError("Caught exception while creating Customer: ", e).log();
        }

        if (HelperMethods.hasRecords(result)) {
            Class<?> className = DTOMappings.getDTOCalssFromTableName(url);
            Dataset dataset = HelperMethods.getDataSet(result);
            Object object = null;
            for (Record record : dataset.getAllRecords()) {
                object = getDTOFromRecord(record, className, withMappings, withDateFormat);
                if (object != null) {
                    dbpdtos.add((DBPDTO) object);
                }
            }
        }
        return dbpdtos;
    }

    public static void loadDTOListfromDB(List<DBPDTOEXT> objects, String filterQuery, String url,
            boolean withMappings, boolean withDateFormat) {

        initLOG();
        Map<String, Object> inputParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(filterQuery)) {
            inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
        }
        diagnostic.prepareDebug("HBL inputParams loadDTOListfromDB"+ inputParams.toString()).log();
        Result result = JSONToResult
                .convert(ServiceCallHelper.invokeServiceAndGetJson(inputParams, new HashMap(), url).toString());
        diagnostic.prepareDebug("HBL Result in loadDTOListfromDB:"+ ResultToJSON.convert(result)).log();
        if (HelperMethods.hasRecords(result)) {
            Class<?> className = DTOMappings.getDTOCalssFromTableName(url);
            Dataset dataset = HelperMethods.getDataSet(result);
            Object object = null;
            for (Record record : dataset.getAllRecords()) {
                object = getDTOFromRecord(record, className, withMappings, withDateFormat);
                objects.add((DBPDTOEXT) object);
            }
        }
    }

    public static void loadDTOListfromDB(List<DBPDTOEXT> objects, Map<String, Object> inputParams, String url,
            boolean withMappings, boolean withDateFormat) {

        initLOG();
        Result result = new Result();
        result = JSONToResult
                .convert(ServiceCallHelper.invokeServiceAndGetJson(inputParams, new HashMap(), url).toString());
        if (HelperMethods.hasRecords(result)) {
            Class<?> className = DTOMappings.getDTOCalssFromTableName(url);
            Dataset dataset = HelperMethods.getDataSet(result);
            Object object = null;
            for (Record record : dataset.getAllRecords()) {
                object = getDTOFromRecord(record, className, withMappings, withDateFormat);
                objects.add((DBPDTOEXT) object);
            }
        }
    }

    public static Object loadJsonObjectIntoObject(JsonObject jsonObject, Class<?> className, boolean withMappings) {
        initLOG();
        Object object = null;
        try {
            object = className.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            alert.prepareError("Caught exception while getting instance from class : ", e).log();
            return object;
        }

        Field[] fields = className.getDeclaredFields();
        Field field;
        String fieldName;
        Map<String, String> dtoMappings = null;
        if (withMappings) {
            dtoMappings = DTOMappings.getDTOObjectPropertyMappings(className);
        }
        for (int j = 0; j < fields.length; j++) {
            field = fields[j];
            if (withMappings && dtoMappings != null && dtoMappings.containsKey(field.getName())) {
                fieldName = dtoMappings.get(field.getName());
            } else {
                fieldName = field.getName();
            }
            field.setAccessible(true);
            if ((jsonObject.has(fieldName) && !jsonObject.get(fieldName).isJsonNull())
                    || (jsonObject.has(fieldName + 's') && !jsonObject.get(fieldName + 's').isJsonNull())) {
                try {
                    if (field.getType().equals(int.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsInt());
                    } else if (field.getType().equals(String.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsString());
                    } else if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                        if (jsonObject.get(fieldName).getAsString().equals("1") || jsonObject.get(fieldName).getAsString().equals("true")) {
                            field.set(object, true);
                        } else {
                            field.set(object, false);
                        }
                    } else if (field.getType().equals(List.class)) {
                        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                        Class<?> listClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                        List<Object> list = null;
                        if (jsonObject.has(field.getName())) {
                            list = loadArrayObjectIntoList(jsonObject.get(fieldName).getAsJsonArray(), listClass,
                                    DTOMappings.getDTOObjectPropertyMappings(listClass) != null);
                        } else if (jsonObject.has(field.getName() + 's')) {
                            list = loadArrayObjectIntoList(jsonObject.get(fieldName + 's').getAsJsonArray(), listClass,
                                    DTOMappings.getDTOObjectPropertyMappings(listClass) != null);
                        }
                        field.set(object, list);
                    } else {
                        className = DTOMappings.getDTOCalssFromTableName(fieldName);
                        if (className != null) {
                            Object object1 = loadJsonObjectIntoObject(jsonObject.get(field.getName()).getAsJsonObject(),
                                    className, DTOMappings.getDTOObjectPropertyMappings(className) != null);
                            field.set(object, object1);
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    alert.prepareError("Caught exception while loading json object to object: ", e).log();
                }
            }
            field.setAccessible(false);
        }

        return object;
    }

    private static List<Object> loadArrayObjectIntoList(JsonArray jsonArray, Class<?> className, boolean withMappings) {
        initLOG();
        List<Object> objects = new ArrayList<Object>();
        Object object;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            object = loadJsonObjectIntoObject(jsonObject, className, withMappings);
            objects.add(object);
        }
        return objects;
    }

    public static List<DBPDTO> getDTOListfromDB(FabricRequestManager requestManager, String filterQuery,
            String url, boolean withMappings, boolean withDateFormat) {
        initLOG();
        List<DBPDTO> dbpdtos = new ArrayList<DBPDTO>();
        Map<String, Object> inputParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(filterQuery)) {
            inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
        }

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                requestManager.getHeadersHandler().getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        Result result = new Result();
        result = ServiceCallHelper.invokeServiceAndGetResult(requestManager, inputParams, headers, url);
        if (HelperMethods.hasRecords(result)) {
            Class<?> className = DTOMappings.getDTOCalssFromTableName(url);

            Dataset dataset = HelperMethods.getDataSet(result);

            Object object = null;
            for (Record record : dataset.getAllRecords()) {
                object = getDTOFromRecord(record, className, withMappings, withDateFormat);
                if (object != null) {
                    dbpdtos.add((DBPDTO) object);
                }
            }
        }
        return dbpdtos;
    }

    public static boolean persistObject(DBPDTOEXT object, Map<String, Object> headers) {
        List<List<DBPDTOEXT>> queue = new LinkedList<List<DBPDTOEXT>>();
        List<DBPDTOEXT> objectList = new LinkedList<DBPDTOEXT>();

        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = object.getClass().getDeclaredFields();

        Map<String, String> mappings = null;
        boolean withMappings = (DTOMappings.getDTOObjectPropertyMappings(object.getClass()) != null);

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

            try {
                if (field.get(object) != null) {

                    String value = "";

                    if (field.getType().equals(int.class)) {
                        value = field.getInt(object) + "";
                    } else if (field.getType().equals(String.class)) {
                        value = String.valueOf(field.get(object));
                    } else if (field.getType().equals(boolean.class)) {
                        value = String.valueOf(field.getBoolean(object));
                    } else if (field.getType().equals(Boolean.class)) {
                        value = String.valueOf(field.get(object));
                    } else if (field.getType().equals(List.class)) {
                        List<?> list = (List<?>) field.get(object);
                        if (list != null && list.size() > 0) {
                            queue.add((List<DBPDTOEXT>) list);
                        }
                    } else if (!Modifier.isFinal(field.getModifiers())) {
                        Object object2 = field.get(object);
                        objectList.add((DBPDTOEXT) object2);
                    }

                    if (fieldName != null) {
                        if(HelperMethods.isValidDateFormat(value)){
                            map.put(fieldName, HelperMethods.getFormattedLocaleDate(value, "yyyy-MM-dd"));
                        }
                        else {
                            map.put(fieldName, value);
                        }
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                alert.prepareError("Caught exception while converting Object to map: ", e).log();
            }
            field.setAccessible(false);
        }

        for (DBPDTOEXT dbpdtoext : objectList) {
            try {
                if(!persistObject(dbpdtoext, headers)) {
                    return false;
                }
            } catch (Exception e) {
                alert.prepareError("Caught exception while saving in DB : " + dbpdtoext.getClass().getName() + " :", e).log();
            }
        }

        // diagnostic.prepareDebug("\n Params: "+map+" \n Headers: "+headers+" \n object : "+object.getClass()).log();
        if (object.persist(map, headers)) {
            for (int i = 0; i < queue.size(); i++) {
                List<DBPDTOEXT> list = queue.get(i);
                for (DBPDTOEXT dbpdtoext : list) {
                    try {
                        if(!persistObject(dbpdtoext, headers)) {
                            return false;
                        }
                    } catch (Exception e) {
                        alert.prepareError("Caught exception while saving in DB : " + dbpdtoext.getClass().getName() + " :",
                                e).log();
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }

    public static Object buildDTOFromDatabase(DBPDTOEXT object, String customerID, boolean withMappings) {

        Object returningObject = object.loadDTO(customerID);

        if (returningObject == null) {
            return null;
        }

        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                if (field.getType().equals(List.class)) {
                    ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                    Class<?> listClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                    if (!listClass.equals(String.class)) {
                        Object list = listClass.newInstance();
                        field.set(returningObject, ((DBPDTOEXT) list).loadDTO(customerID));
                    }
                }
                field.setAccessible(false);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            alert.prepareError("Caught exception while converting Object to map: ", e).log();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            alert.prepareError("Caught exception while converting Object to map: ", e).log();
        }

        return returningObject;
    }

    public static void copy(Object object1, Object object2) {

        if (object1.getClass().equals(object2.getClass())) {
            Field[] fields = object2.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (!field.getType().equals(long.class) && !field.getType().equals(Long.class) && field.get(object2) != null) {
                        field.set(object1, field.get(object2));
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    alert.prepareError("Error while copying fields ", e).log();
                }
                field.setAccessible(false);
            }
        }
    }

    public static Map<String, String> getResponseMap(Object object, Set<String> set, boolean withMappings) {

        Map<String, String> responseMap = new HashMap<String, String>();
        if (object != null) {
            Map<String, String> map = new HashMap<String, String>();
            if (withMappings) {
                map = DTOMappings.getDTOObjectPropertyMappings(object.getClass());
            }
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                if (withMappings && map != null && map.containsKey(fieldName)) {
                    fieldName = map.get(fieldName);
                }
                if(set.contains(fieldName)) {
                    field.setAccessible(true);
                    try {
                        if(field.get(object) != null) {
                            if (field.getType().equals(int.class)) {
                                responseMap.put(fieldName, ""+(int)field.get(object));
                            } else if (field.getType().equals(String.class)) {
                                responseMap.put(fieldName, (String)field.get(object));
                            } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                                responseMap.put(fieldName, ""+((boolean) field.get(object)));
                            }
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        alert.prepareError("Caught exception while setting value to field: ", e).log();
                    }
                    field.setAccessible(false);
                }
            }
        }

        return responseMap;
    }

    public static void loadDTOFromJson(Object object, JsonObject jsonObject) {
        LoggerUtil logger = new LoggerUtil(DTOUtils.class);
        Field[] fields= object.getClass().getDeclaredFields();

        for(int i=0; i<fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            if(jsonObject.has(fieldName) && !jsonObject.get(fieldName).isJsonNull()) {
                try {
                    if(field.getType().equals(String.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsString());
                    }
                    else if(field.getType().equals(int.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsInt());
                    }
                    else if(field.getType().equals(Boolean.class) || field.getType().equals(Boolean.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsBoolean());
                    }

                } catch (IllegalArgumentException | IllegalAccessException e) {
                    alert.prepareError("Caught exception while converting json to Object: " , e).log();
                }
            }
            field.setAccessible(false);
        }

    }

    public static void loadDTOFromJson(Object object, JsonObject jsonObject, boolean withMappings) {
        LoggerUtil logger = new LoggerUtil(DTOUtils.class);
        Field[] fields= object.getClass().getDeclaredFields();

        Map<String, String> map = DTOMappings.getDTOObjectPropertyMappings(object.getClass());

        for(int i=0; i<fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();

            if(withMappings) {
                fieldName = map.get(fieldName);
            }

            if(jsonObject.has(fieldName) && !jsonObject.get(fieldName).isJsonNull()) {
                try {
                    if(field.getType().equals(String.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsString());
                    }
                    else if(field.getType().equals(int.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsInt());
                    }
                    else if(field.getType().equals(Boolean.class) || field.getType().equals(Boolean.class)) {
                        field.set(object, jsonObject.get(fieldName).getAsBoolean());
                    }

                } catch (IllegalArgumentException | IllegalAccessException e) {
                    alert.prepareError("Caught exception while converting json to Object: " , e).log();
                }
            }
            field.setAccessible(false);
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getDTOList(String jsonArrayString, Class<T> classType) {
        List<T> dtoList = new ArrayList<T>();
        JsonParser parser = new JsonParser();
        if(StringUtils.isNotBlank(jsonArrayString)) {
            try {
            JsonArray array = parser.parse(jsonArrayString).getAsJsonArray();
            for (JsonElement element : array) {
                if (element.isJsonObject()) {
                    T dto = null;
                    if (null != DTOMappings.getDTOObjectPropertyMappings(classType)) {
                        dto = (T) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), classType, true);
                    } else {
                        dto = (T) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), classType, false);
                    }
                    if (null != dto) {
                        dtoList.add(dto);
                    }
    
                }
    
            }
            }catch (Exception e) {
            	alert.prepareError("exception", e).log();
            }
        }
        return dtoList;
    }

}
