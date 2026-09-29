package com.kony.dbputilities.utils;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ConvertJsonToResult {
    private ConvertJsonToResult() {
    }

    public static Result convert(String jsonObject) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jObject = (JsonObject) jsonParser.parse(jsonObject);
        return convert(jObject);
    }

    public static Result convert(JsonObject jsonObject) {
        Result result = new Result();
        if (null != jsonObject) {
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                processJsonElement(result, entry.getValue(), entry.getKey());
            }
        } else {
            result.addParam(new Param("errormsg", "Received empty response", "String"));
        }
        return result;
    }

    private static void processJsonElement(Result result, JsonElement jElement, String key) {
        if (null == jElement) {
            return;
        } else if (jElement.isJsonArray()) {
            addDatasetToResult(result, (JsonArray) jElement, key);
        } else if (jElement.isJsonObject()) {
            addRecordsToResult(result, (JsonObject) jElement, key);
        } else {
            result.addParam(new Param(key, jElement.getAsString(), "String"));
        }
    }

    private static void addRecordsToResult(Result result, JsonObject jsonObject, String key) {
        if (null != jsonObject) {
            Record record = constructRecord(jsonObject);
            record.setId(key);
            result.addRecord(record);
        }
    }

    private static void addDatasetToResult(Result result, JsonArray jsonArray, String key) {
        Dataset ds = constructDataset(jsonArray);
        ds.setId(key);
        result.addDataset(ds);
    }

    private static void addRecordToRecord(Record record, JsonObject jsonArray, String key) {
        Record rd = constructRecord(jsonArray);
        rd.setId(key);
        record.addRecord(rd);
    }

    private static void addDatasetToRecord(Record record, JsonArray jsonArray, String key) {
        Dataset ds = constructDataset(jsonArray);
        ds.setId(key);
        record.addDataset(ds);
    }

    private static Dataset constructDataset(JsonArray jsonArray) {
        Dataset dataset = new Dataset();
        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.size(); i++) {
                Record record = constructRecord((JsonObject) jsonArray.get(i));
                dataset.addRecord(record);
            }
        }
        return dataset;
    }

    public static Record constructRecord(JsonObject jsonObject) {
        Record record = new Record();
        if (null != jsonObject) {
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                processJsonElement(record, entry.getValue(), entry.getKey());
            }
        }
        return record;
    }

    private static void processJsonElement(Record record, JsonElement jElement, String key) {
        if (null == jElement) {
            return;
        } else if (jElement.isJsonArray()) {
            addDatasetToRecord(record, (JsonArray) jElement, key);
        } else if (jElement.isJsonObject()) {
            addRecordToRecord(record, (JsonObject) jElement, key);
        } else {
            record.addParam(new Param(key, jElement.isJsonNull() ? null : jElement.getAsString(), "String"));
        }
    }
}