package com.kony.dbputilities.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceCallHelper;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.AdminConsole.Utilities.URLConstants;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public final class HelperMethods {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

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
            if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, JSONObject.get(key).toString(), MWConstants.STRING);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, JSONObject.get(key).toString(), MWConstants.STRING);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof JSONArray) {
                Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
                dataset.setId(key);
                response.addDataset(dataset);
            } else if (JSONObject.get(key) instanceof JSONObject) {
                Record record = constructRecordFromJSONObject(JSONObject.getJSONObject(key));
                record.setId(key);
                response.addRecord(record);
            } else {
                Param param = new Param(key, JSONObject.optString(key), MWConstants.STRING);
                response.addParam(param);
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
                Dataset dataset = CommonUtilities.constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
                dataset.setId(key);
                response.addDataset(dataset);
            } else if (JSONObject.get(key) instanceof JSONObject) {
                Record record = CommonUtilities.constructRecordFromJSONObject(JSONObject.getJSONObject(key));
                record.setId(key);
                response.addRecord(record);
            }
        }

        return response;
    }

    public static String convertTimetoISO8601Format(Date dateInstance) {
        if (dateInstance == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        return dateFormat.format(dateInstance);
    }

    public static Date parseDateStringToDate(String dateString, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            return sdf.parse(dateString, new ParsePosition(0));
        } catch (Exception exe) {
            alert.prepareError(exe.getMessage()).log();
            return null;
        }
    }

    public static void setValidationMsgwithCode(String message, String code, Result result) {
        Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR_MESSAGE, message,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(validionMsg);
        Param status = new Param(DBPUtilitiesConstants.ERROR_CODE, code, DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(status);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getInputParamMap(Object[] inputArray) {
        return (HashMap<String, String>) inputArray[1];
    }

    public static Map<String, Object> convertToObjectMap(Map<String, String> inputmap) {
        Map<String, Object> retMap = new HashMap<>();
        for (String key : inputmap.keySet()) {
            retMap.put(key, inputmap.get(key));
        }
        return retMap;
    }

    public static String getFieldValue(Result result, String fieldName) {
        String id = "";
        if (HelperMethods.hasRecords(result)) {
            try {
                Dataset ds = result.getAllDatasets().get(0);
                id = getParamValue(ds.getRecord(0).getParam(fieldName));
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
                return id;
            }
        }
        return id;
    }

    public static String getFieldValue(Record record, String fieldName) {
        String id = "";
        if (null != record) {
            id = getParamValue(record.getParam(fieldName));
        }
        return id;
    }

    public static String getParamValue(Param p) {
        String value = "";
        if (null != p) {
            value = p.getValue();
        }
        return (null == value) ? "" : value;
    }

    public static boolean hasRecords(Result result) {
        if (hasError(result) || null == result.getAllDatasets() || result.getAllDatasets().isEmpty()) {
            return false;
        }
        Dataset ds = result.getAllDatasets().get(0);
        return null != ds && null != ds.getAllRecords() && ds.getAllRecords().size() > 0;
    }

    public static String getUserIdFromSession(DataControllerRequest dcRequest) {
        Map<String, String> user = getUserFromIdentityService(dcRequest);
        return user.get("customer_id");
    }

    public static String getUsernameFromSession(DataControllerRequest dcRequest) {
        Map<String, String> user = getUserFromIdentityService(dcRequest);
        return user.get("UserName");
    }

    public static Map<String, Object> getHeaders(DataControllerRequest dcRequest) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        return headerMap;
    }

    public static Map<String, String> getUserFromIdentityService(DataControllerRequest dcRequest) {
        Map<String, String> user = new HashMap<>();
        Result userAttributesResponse = new Result();

        try {
            userAttributesResponse = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, null,
                    HelperMethods.getHeaders(dcRequest), URLConstants.USER_ATTRIBUTES_GET_IDENTITY);
        } catch (Exception e) {
            alert.prepareError("Exception while fetching user attributes", e).log();
        }

        if (userAttributesResponse != null) {
            for (Param param : userAttributesResponse.getAllParams()) {
                user.put(param.getName(), param.getValue());
            }
        }

        return user;
    }

    public static boolean hasError(Result result) {
        boolean status = false;
        if (null == result || null != result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR)) {
            status = true;
        }
        return status;
    }

    public static String getCurrentTimeStamp() {
        return getFormattedTimeStamp(new Date(), null);
    }

    public static String getFormattedTimeStamp(Date dt, String format) {
        String dtFormat = "yyyy-MM-dd'T'hh:mm:ss";
        if (StringUtils.isNotBlank(format)) {
            dtFormat = format;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
        return formatter.format(dt);
    }

    public static Date getFormattedTimeStamp(String dt) {
        SimpleDateFormat[] expectedFormats = new SimpleDateFormat[] { new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S"),
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"), new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"),
                new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("MM/dd/yyyy") };
        for (int i = 0; i < expectedFormats.length; i++) {
            try {
                return expectedFormats[i].parse(dt);
            } catch (ParseException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }
        return new Date();
    }

    public static String convertDateFormat(String dob, String to) throws ParseException {
        Date date = getFormattedTimeStamp(dob);
        return HelperMethods.getFormattedTimeStamp(date, to);
    }

    public static String getNewId() {
        UUID uuidValue = UUID.randomUUID();
        return uuidValue.toString();
    }

    public static Map<String, String> getHeaders(FabricRequestManager requestManager) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                requestManager.getHeadersHandler().getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return headerMap;

    }

    public static Map<String, Object> updateHeadersForC360Integration(DataControllerRequest dcRequest,
            Map<String, Object> headerMap) {
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        headerMap.put("backendToken", ServiceConfig.getValue("Auth_Token"));
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        return headerMap;

    }

    public static JsonObject getJsonObject(String jsonString) {
        try {
            JsonParser parser = new JsonParser();
            return parser.parse(jsonString).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String invokeC360ServiceAndGetString(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        updateHeadersForC360Integration(dcRequest, headerParams);
        return ServiceCallHelper.invokePassThroughServiceAndGetString(dcRequest, inputParams, headerParams, url);
    }

    public static String invokeServiceAndGetString(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        updateHeadersForC360Integration(dcRequest, headerParams);
        return ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputParams, headerParams, url);
    }

    public static Result invokeServiceAndGetResult(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        updateHeadersForC360Integration(dcRequest, headerParams);
        return ServiceCallHelper.invokeServiceAndGetResult(dcRequest, inputParams, headerParams, url);
    }

    public static Result invokeC360ServiceAndGetResult(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        updateHeadersForC360Integration(dcRequest, headerParams);
        return ServiceCallHelper.invokePassThroughServiceAndGetResult(dcRequest, inputParams, headerParams, url);
    }

}