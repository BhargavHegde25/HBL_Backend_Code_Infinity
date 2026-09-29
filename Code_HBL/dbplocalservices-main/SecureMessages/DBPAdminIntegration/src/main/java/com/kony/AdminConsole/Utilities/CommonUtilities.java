package com.kony.AdminConsole.Utilities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CommonUtilities {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static Map<String, Object> getInputMapFromInputArray(Object[] inputArray) {
        @SuppressWarnings("unchecked")
        Map<String, Object> inputMap = (HashMap<String, Object>) inputArray[1];
        return inputMap;
    }

    public static JSONObject getStringAsJSONObject(String jsonString) {
        JSONObject generatedJSONObject = new JSONObject();
        if (CommonUtilities.isEmptyString(jsonString)) {
            return null;
        }
        try {
            generatedJSONObject = new JSONObject(jsonString);
            return generatedJSONObject;
        } catch (JSONException e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }
    }

    public static boolean isEmptyString(String testString) {
        if (testString == null || testString.trim().length() == 0) {
            return true;
        }
        return false;
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
            String key = keys.next();
            if (JSONObject.get(key) instanceof String) {
                Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof JSONArray) {
                Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
                dataset.setId(key);
                response.addDataset(dataset);
            }
        }

        return response;
    }

    public static Result constructResultFromJSONObject(JSONObject JSONObject) {
        Result response = new Result();
        if (JSONObject == null || JSONObject.length() == 0) {
            return response;
        }
        Iterator<String> keys = JSONObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            if (JSONObject.get(key) instanceof String) {
                Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof JSONArray) {
                Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
                response.addDataset(dataset);
            }
        }

        return response;
    }

    public static String getISOFormattedLocalTimestamp() {
        String localDateTime;
        if (LocalDateTime.now().getSecond() == 0) {
            localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
        } else {
            localDateTime = LocalDateTime.now().withNano(0).toString();
        }
        return localDateTime;
    }

}