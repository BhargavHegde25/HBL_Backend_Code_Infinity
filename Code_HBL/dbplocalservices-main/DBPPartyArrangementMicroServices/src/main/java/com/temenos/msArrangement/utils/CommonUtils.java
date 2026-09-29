package com.temenos.msArrangement.utils;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CommonUtils {
	
    public static final String STRING = "string";
    public static final String INT = "int";

    public static Result getResultObjectFromJSONObject(JSONObject JSONObject) {
        Result response = new Result();
        if (JSONObject == null || JSONObject.length() == 0) {
            return response;
        }
        Iterator<String> keys = JSONObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (JSONObject.get(key) instanceof String) {
                Param param = new Param(key, JSONObject.getString(key), STRING);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, String.valueOf(JSONObject.get(key)), INT);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, String.valueOf(JSONObject.get(key)), "boolean");
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof JSONArray) {
                Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
                dataset.setId(key);
                response.addDataset(dataset);
            } else if (JSONObject.get(key) instanceof JSONObject) {
                Record record = constructRecordFromJSONObject(JSONObject.getJSONObject(key));
                record.setId(key);
                response.addRecord(record);
            }
        }

        return response;
    }
    
    public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
        Dataset dataset = new Dataset();
        for (int count = 0; count < JSONArray.length(); count++) {
            Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
            dataset.addRecord(record);
        }
        return dataset;
    }
    
    public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
        Record response = new Record();
        if (JSONObject == null || JSONObject.length() == 0) {
            return response;
        }
        Iterator<String> keys = JSONObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, JSONObject.get(key).toString(), STRING);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, JSONObject.get(key).toString(), STRING);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof JSONArray) {
                Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
                dataset.setId(key);
                response.addDataset(dataset);
            }  else if (JSONObject.get(key) instanceof JSONObject) {                                
                Record record =  constructRecordFromJSONObject(JSONObject.getJSONObject(key));
                record.setId(key);
                response.addRecord(record);
            } else {
                Param param = new Param(key, JSONObject.optString(key), STRING);
                response.addParam(param);
            }
        }

        return response;
    }
  //Process the result If any error occurs
    public static Result ProcessResult(JSONObject obj){
        Result result = new Result();
        if(obj.toString().equals("{}")){
            result.addOpstatusParam(10001);
            result.addErrMsgParam("Invalid Query Details. Bad Request");
            result.addHttpStatusCodeParam(400);
        }else {
            result =  getResultObjectFromJSONObject(obj);
            result.addOpstatusParam(100);
            result.addHttpStatusCodeParam(200);
        }
        
        return result; 
    }
    
}
