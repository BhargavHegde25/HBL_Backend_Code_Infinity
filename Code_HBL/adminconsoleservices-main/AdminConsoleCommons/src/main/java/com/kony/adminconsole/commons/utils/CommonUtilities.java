package com.kony.adminconsole.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
/**
 * @author
 *
 */
public class CommonUtilities {

    private static final Random RANDOM = new Random();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * @param base64FileContent
     * @return
     * @throws Exception
     */
    public static File constructFileFromBase64String(String base64FileContent) throws Exception {
        byte[] decodedFileContent = Base64.decodeBase64(base64FileContent.getBytes("UTF-8"));
        File file = File.createTempFile("customerRequest", "messageattachment");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(decodedFileContent);
        } catch (IOException e) {
            alert.prepareError("Exception in Constructing  File Output Stream", e).log();
        }

        finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                alert.prepareError("Exception in Closing  File Output Stream", e).log();
            }

        }
        return file;
    }

    /**
     * @param base64String
     * @return
     */
    public static int getFileSize(String base64String) {
        if (StringUtils.isBlank(base64String)) {
            return 0;
        }
        return (base64String.length() * 3 / 4) - StringUtils.countMatches(base64String, "=");
    }

    /**
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        if (file == null) {
            return StringUtils.EMPTY;
        }
        String fileName = file.getName();
        if (StringUtils.isBlank(fileName) || !fileName.contains(".") || fileName.endsWith(".")) {
            return StringUtils.EMPTY;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * @param file
     * @return
     */
    public static String getFileExtension(String filename) {
        if (StringUtils.isBlank(filename) || !filename.contains(".") || filename.endsWith(".")) {
            return StringUtils.EMPTY;
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * @param emailAddress
     * @return
     */
    public static boolean isValidEmailID(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailAddress);
        return matcher.find();
    }

    /**
     * Fetch the auth token either from request headers or from query parameters. Return null if not found in both.
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getAuthToken(DataControllerRequest request) {
        Object authToken = null;
        // locate Auth token from headers. If not found, locate it from query parameters
        // otherwise return null
        authToken = request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
        if (authToken == null) {
            Map<String, Object> queryParams = (Map<String, Object>) request.getAttribute("queryparams");
            if (queryParams != null) {
                authToken = queryParams.get("authToken");
            }
        }
        return String.class.cast(authToken);

    }

    /**
     * @param dateInstance
     * @return
     */
    public static String convertTimetoISO8601Format(Date dateInstance) {
        if (dateInstance == null) {
            return StringUtils.EMPTY;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        return dateFormat.format(dateInstance);
    }

    /**
     * @param timestamp
     * @param dateformat
     * @return
     */
    public static Date parseTimestampStringtoDateObject(String timestamp, String dateformat) {
        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(dateformat))
            return null;
        SimpleDateFormat dateFormatInstance = new SimpleDateFormat(dateformat);
        Date dateObject = null;
        try {
            dateObject = dateFormatInstance.parse(timestamp);
        } catch (ParseException e) {
            alert.prepareError("Unexpected error has occurred", e).log();

        }
        return dateObject;
    }

    /**
     * @param fileObject
     * @return
     * @throws Exception
     */
    public static String encodeFile(File fileObject) throws Exception {
        // DELETING this pattern match here as we will already checking the file pattern in the calling method
        // CustomerRequestAndRequestMessageManageService.validateRequestInstanceAndInitialiseBean
        // if (!Pattern.matches("^[A-Za-z0-9\\s]*+(.txt|.doc|.docx|.pdf|.png|.jpeg|.jpg)$",
        // fileObject.getName().toLowerCase())) {
        // alert.prepareError("Invalid filename or unsupported file extension" + fileObject.getName()).log();
        // throw new Exception("Invalid filename or unsupported file extension");
        // }
        try (FileInputStream fileInputStream = new FileInputStream(fileObject)) {
            String encode = "";
            byte bt[] = new byte[(int) fileObject.length()];
            int count = fileInputStream.read(bt);
            diagnostic.prepareDebug("Read " + count + " bytes.").log();
            encode = new String(Base64.encodeBase64(bt));
            return encode;
        } catch (Exception e) {
            alert.prepareError("Unexpected error has occurred", e).log();
        }
        return null;
    }

    /**
     * @param sourceString
     * @param targetSubString
     * @param replacementString
     * @return
     */
    public static String replaceLastOccuranceOfString(String sourceString, String targetSubString,
            String replacementString) {
        if (targetSubString == null)
            return sourceString;
        if (replacementString == null)
            replacementString = "";
        int index = sourceString.lastIndexOf(targetSubString);
        if (index == -1)
            return sourceString;
        return sourceString.substring(0, index) + replacementString
                + sourceString.substring(index + targetSubString.length());
    }

    /**
     * @param targetJSONArray
     * @return
     */
    public static ArrayList<String> getStringElementsOfJSONArrayToArrayList(JSONArray targetJSONArray) {
        ArrayList<String> resultArrayList = new ArrayList<String>();
        if (targetJSONArray == null)
            return resultArrayList;
        for (int indexVar = 0; indexVar < targetJSONArray.length(); indexVar++) {
            try {
                resultArrayList.add(targetJSONArray.optString(indexVar));
            } catch (Exception e) {
                continue;
            }
        }
        return resultArrayList;
    }

    /**
     * @return
     */
    public static UUID getNewId() {
        return UUID.randomUUID();
    }

    /**
     * @param jsonString
     * @return
     */
    public static JSONObject getStringAsJSONObject(String jsonString) {
        JSONObject generatedJSONObject = new JSONObject();
        if (StringUtils.isBlank(jsonString))
            return null;
        try {
            generatedJSONObject = new JSONObject(jsonString);
            return generatedJSONObject;
        } catch (JSONException e) {
            alert.prepareError("Unexpected error has occurred", e).log();
            return null;
        }
    }

    /**
     * @param jsonString
     * @return
     */
    public static JSONArray getStringAsJSONArray(String jsonString) {
        JSONArray generatedJSONArray = new JSONArray();
        if (StringUtils.isBlank(jsonString))
            return null;
        try {
            generatedJSONArray = new JSONArray(jsonString);
            return generatedJSONArray;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * @param timeStamp
     * @param timeStampFormat
     * @return
     */
    public static long getTimeElapsedFromTimestampToNowInMinutes(String timeStamp, String timeStampFormat) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(timeStampFormat);
        Date userCreationDateTimeInstance;
        try {
            userCreationDateTimeInstance = dateTimeFormat.parse(timeStamp);
            Date currentDateTimeInstance = new Date();
            long diffInMilliSeconds = currentDateTimeInstance.getTime() - userCreationDateTimeInstance.getTime();
            return TimeUnit.MILLISECONDS.toMinutes(diffInMilliSeconds);
        } catch (ParseException e) {
            alert.prepareError("Unexpected error has occurred", e).log();
        }
        return 0l;
    }

    /**
     * @param sourceObjectArray
     * @return
     */
    public static HashMap<String, String> getObjectArrayAsHashMap(Object sourceObjectArray) {
        String objectArrayAsString = sourceObjectArray.toString();
        objectArrayAsString = objectArrayAsString.substring(1, objectArrayAsString.length() - 1);
        String[] keyValuePairArray = objectArrayAsString.split(",");
        HashMap<String, String> keyValuePairMap = new HashMap<String, String>();
        for (String currKeyValuePair : keyValuePairArray) {
            String[] currKeyValuePairArray = currKeyValuePair.split("=");
            if (currKeyValuePairArray.length > 1)
                keyValuePairMap.put(currKeyValuePairArray[0].trim(), currKeyValuePairArray[1].trim());
            else
                keyValuePairMap.put(currKeyValuePairArray[0].trim(), "");
        }
        return keyValuePairMap;
    }

    /**
     * @param newValue
     * @param existingStringArray
     * @return
     */
    public static String[] prependStringToStringArray(String newValue, String[] existingStringArray) {
        String[] newStringArray = new String[existingStringArray.length + 1];
        newStringArray[0] = newValue;
        System.arraycopy(existingStringArray, 0, newStringArray, 1, existingStringArray.length);
        return newStringArray;
    }

    /**
     * @return
     */
//    public static String genPhoneNumber() {
//        return (RANDOM.nextInt(9999) + 10000) + "" + (RANDOM.nextInt(9999) + 10000);
//    }

    /**
     * @param JSONArray
     * @return
     */
    public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
        Dataset dataset = new Dataset();
        for (int count = 0; count < JSONArray.length(); count++) {
            Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
            dataset.addRecord(record);
        }
        return dataset;
    }

    /**
     * @param JSONObject
     * @return
     */
    public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
        Record response = new Record();
        if (JSONObject == null || JSONObject.length() == 0) {
            return response;
        }
        Iterator<String> keys = JSONObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.INT);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.BOOLEAN);
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
                Param param = new Param(key, JSONObject.optString(key), FabricConstants.STRING);
                response.addParam(param);
            }
        }

        return response;
    }

    /**
     * @param JSONObject
     * @return
     */
    public static Result constructResultFromJSONObject(JSONObject JSONObject) {
        Result response = new Result();
        if (JSONObject == null || JSONObject.length() == 0) {
            return response;
        }
        Iterator<String> keys = JSONObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.INT);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.BOOLEAN);
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
                Param param = new Param(key, JSONObject.optString(key), FabricConstants.STRING);
                response.addParam(param);
            }
        }
        return response;
    }

    /**
     * @return
     */
    public static String getISOFormattedLocalTimestamp() {
        String localDateTime;
        if (LocalDateTime.now().getSecond() == 0) {
            localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
        } else {
            localDateTime = LocalDateTime.now().withNano(0).toString();
        }
        return localDateTime;
    }

    /**
     * @return
     */
    public static String getCustomerIdForGivenUserName() {
        String localDateTime;
        if (LocalDateTime.now().getSecond() == 0) {
            localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
        } else {
            localDateTime = LocalDateTime.now().withNano(0).toString();
        }
        return localDateTime;
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static int generateRandomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (getRandomId() * range) + min;

    }
    
    public static long getRandomId() {
		Random random = new SecureRandom();
		return 1000000000L + random.nextInt(900000000);
	}

    /**
     * @param sourceString
     * @return
     */
    public static String encodeToBase64(String sourceString) {
        if (sourceString == null) {
            return null;
        }
        return new String(java.util.Base64.getEncoder().encode(sourceString.getBytes()));
    }

    /**
     * @param sourceString
     * @return
     */
    public static String decodeFromBase64(String sourceString) {
        if (sourceString == null) {
            return null;
        }
        return new String(Base64.decodeBase64(sourceString));
    }

    /**
     * @param sourceString
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeURI(String sourceString) throws UnsupportedEncodingException {
        if (sourceString == null) {
            return null;
        }
        return new String(java.net.URLEncoder.encode(sourceString, "UTF-8"));
    }

    /**
     * @param sourceString
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decodeURI(String sourceString) throws UnsupportedEncodingException {
        if (sourceString == null) {
            return null;
        }
        return new String(java.net.URLDecoder.decode(sourceString, "UTF-8"));
    }

    /**
     * @param inputJSON
     * @return
     */
    public static Result getResultObjectFromJSONObject(JSONObject inputJSON) {
        Result response = new Result();
        if (inputJSON == null || inputJSON.length() == 0) {
            return response;
        }
        Iterator<String> keys = inputJSON.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (inputJSON.get(key) instanceof String) {
                Param param = new Param(key, inputJSON.getString(key), FabricConstants.STRING);
                response.addParam(param);

            } else if (inputJSON.get(key) instanceof Integer) {
                Param param = new Param(key, inputJSON.optString(key), FabricConstants.INT);
                response.addParam(param);

            } else if (inputJSON.get(key) instanceof Boolean) {
                Param param = new Param(key, inputJSON.optString(key), "boolean");
                response.addParam(param);

            } else if (inputJSON.get(key) instanceof JSONArray) {
                Dataset dataset = CommonUtilities.constructDatasetFromJSONArray(inputJSON.getJSONArray(key));
                dataset.setId(key);
                response.addDataset(dataset);
            } else if (inputJSON.get(key) instanceof JSONObject) {
                Record record = CommonUtilities.constructRecordFromJSONObject(inputJSON.getJSONObject(key));
                record.setId(key);
                response.addRecord(record);
            }
        }

        return response;
    }

    /**
     * @return
     */
    public static long getNumericId() {
        long generatedValue;
        generatedValue = getRandomId();
        return generatedValue;
    }

    /**
     * @param sourceJSONArray
     * @return
     */
    public static List<String> getJSONArrayAsList(JSONArray sourceJSONArray) {
        List<String> resultList = new ArrayList<String>();
        if (sourceJSONArray == null || sourceJSONArray.length() == 0) {
            return resultList;
        }
        for (int indexVar = 0; indexVar < sourceJSONArray.length(); indexVar++) {
            resultList.add(sourceJSONArray.optString(indexVar));
        }
        return resultList;
    }

    /**
     * @param stringifiedList
     * @return
     */
    public static List<String> getStringifiedArrayAsList(String stringifiedList) {
        List<String> processedList = new ArrayList<>();
        if (StringUtils.isNotBlank(stringifiedList)) {
            JSONArray jsonArray = getStringAsJSONArray(stringifiedList);
            if (jsonArray != null && jsonArray.length() > 0) {
                processedList = getJSONArrayAsList(jsonArray);
            }
        }
        return processedList;
    }

    /**
     * @param sourceList
     * @return
     */
    public static List<String> removeDuplicatesInList(List<String> sourceList) {
        if (sourceList != null && !sourceList.isEmpty()) {
            Set<String> set = new HashSet<String>();
            set.addAll(sourceList);
            sourceList.clear();
            sourceList.addAll(set);
        }
        return sourceList;
    }

    /**
     * @param responseInstance
     * @param errorMessage
     * @throws Exception
     */
    public static void fileDownloadFailure(DataControllerResponse responseInstance, String errorMessage)
            throws Exception {
        Map<String, String> customHeaders = new HashMap<String, String>();
        customHeaders.put("Content-Type", "text/plain; charset=utf-8");

        responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON,
                new BufferedHttpEntity(new StringEntity(errorMessage, StandardCharsets.UTF_8)));
        responseInstance.getHeaders().putAll(customHeaders);
        responseInstance.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * @param dateString
     * @param dateFormat
     * @return
     */
    public static Date parseDateStringToDate(String dateString, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            return sdf.parse(dateString, new ParsePosition(0));
        } catch (Exception exe) {
            return null;
        }
    }

    /**
     * @param inputDate
     * @return
     */
    public static int calculateAge(Date inputDate) {
        try {
            Calendar calender = Calendar.getInstance();
            calender.setTime(inputDate);
            int dayOfMonth = calender.get(Calendar.DAY_OF_MONTH);
            int month = calender.get(Calendar.MONTH);
            int year = calender.get(Calendar.YEAR);
            LocalDate today = LocalDate.now(); // Today's date
            LocalDate birthday = LocalDate.of(year, month, dayOfMonth);
            Period period = Period.between(birthday, today);
            return period.getYears();
        } catch (Exception e) {
            alert.prepareError("Exception in calculating Age.", e).log();
            throw e;
        }
    }

    /**
     * @param languageIdentifier
     * @return
     */
    public static String formatLanguageIdentifier(String languageIdentifier) {
        diagnostic.prepareDebug("Formatting Language Identifier. Recieved Value:" + languageIdentifier).log();
        if (StringUtils.isBlank(languageIdentifier)) {
            return StringUtils.EMPTY;
        }
        languageIdentifier = languageIdentifier.replace("_", "-");
        List<LanguageRange> languageRange = Locale.LanguageRange.parse(languageIdentifier);
        if (languageRange != null && !languageRange.isEmpty()) {
            String formattedIdentifier = languageRange.get(0).getRange();
            formattedIdentifier = formattedIdentifier.replace("_", "-");
            diagnostic.prepareDebug("Formatted Language Identifier. Result Value:" + formattedIdentifier).log();
            return formattedIdentifier;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Method to remove filter records from CRUD response based on the Locale
     * 
     * @param records
     * @param localeIdentifierKey
     * @param uniqueIdentifierKey
     * @param defaultLocale
     * @return filtered records based on the Locale
     */
    public static JSONArray filterRecordsByLocale(JSONArray records, String localeIdentifierKey,
            String uniqueIdentifierKey, String defaultLocale) {

        try {
            diagnostic.prepareDebug("Recieved Default Locale:" + defaultLocale).log();
            diagnostic.prepareDebug("Recieved Locale Identifier Key:" + localeIdentifierKey).log();
            diagnostic.prepareDebug("Recieved Unique Identifier Key:" + uniqueIdentifierKey).log();

            if (records != null && records.length() > 0 && StringUtils.isNotBlank(uniqueIdentifierKey)
                    && StringUtils.isNotBlank(localeIdentifierKey)) {
                diagnostic.prepareDebug("Count of Records" + records.length()).log();

                // Filtered Records
                JSONArray filteredRecords = new JSONArray();

                // Method local variables
                JSONObject currJSON;
                String currLocale;
                String currUniqueIdentifer;

                // Maps used for filtering of Records
                Map<String, JSONObject> defaultLocaleRecords = new HashMap<>();
                Map<String, JSONObject> userPreferredLocaleRecords = new HashMap<>();

                diagnostic.prepareDebug("Traversing Input Records").log();

                for (Object currObject : records) {

                    if (currObject instanceof JSONObject) {
                        currJSON = (JSONObject) currObject;

                        if (currJSON.has(uniqueIdentifierKey)) {

                            // Unique Identifier of current record
                            currUniqueIdentifer = currJSON.optString(uniqueIdentifierKey);

                            // Locale of current record
                            currLocale = currJSON.optString(localeIdentifierKey);

                            if (StringUtils.equals(currLocale, defaultLocale)) {
                                // Locale of current record and Default Locale are same. Add to Default Locale
                                // Map
                                defaultLocaleRecords.put(currUniqueIdentifer, currJSON);
                            } else {
                                // Locale of current record and Default Locale are different. Add to User
                                // Preferred Locale Map
                                userPreferredLocaleRecords.put(currUniqueIdentifer, currJSON);
                            }
                        }

                    }
                }

                diagnostic.prepareDebug("Count of Default Locale Records:" + defaultLocaleRecords.size()).log();
                diagnostic.prepareDebug("Count of User Preferred Locale Records:" + userPreferredLocaleRecords.size()).log();

                diagnostic.prepareDebug("Consolidating segregated records").log();
                // User Preferred Locale Records take precedence over Default Locale Records
                for (Entry<String, JSONObject> currEntry : userPreferredLocaleRecords.entrySet()) {
                    filteredRecords.put(currEntry.getValue());
                    if (defaultLocaleRecords.containsKey(currEntry.getKey())) {
                        // Remove Records of Default Locale for which the records of user preferred
                        // Locale are present
                        defaultLocaleRecords.remove(currEntry.getKey());
                    }
                }
                diagnostic.prepareDebug("Count of Default Locale Records after traversing User Preferrred Locale Records:"
                        + defaultLocaleRecords.size()).log();

                // Add Default Locale Records for which records of User Preferred Locale are not
                // available
                for (Entry<String, JSONObject> currEntry : defaultLocaleRecords.entrySet()) {
                    filteredRecords.put(currEntry.getValue());
                }

                diagnostic.prepareDebug("Returning consolidated Records").log();
                // Return filtered set of Records
                return filteredRecords;
            }

            // Default return value
            return records;
        } catch (Exception e) {
            alert.prepareError("Exception in Filtering Records by Locale. Exception:", e).log();
            throw e;
        }
    }

    /**
     * Method to sort JSON Array of JSON objects on a particular attribute
     * 
     * @param jsonArray
     * @param key
     * @param isAscending
     * @param TRUE
     *            if 'key' is of numeric type
     * @return sorted JSON Array
     */
    public static JSONArray sortJSONArrayOfJSONObjects(JSONArray jsonArray, String key, boolean isAscending,
            boolean isNumericValue) {

        try {

            if (jsonArray == null || jsonArray.length() == 0 || StringUtils.isBlank(key)) {
                return jsonArray;
            }

            // Add JSON Objects into List
            List<JSONObject> jsonValuesList = new ArrayList<>();
            for (int index = 0; index < jsonArray.length(); index++) {
                jsonValuesList.add(jsonArray.getJSONObject(index));
            }

            // Sort JSON Array
            Collections.sort(jsonValuesList, new Comparator<JSONObject>() {

                @Override
                public int compare(JSONObject object1, JSONObject object2) {

                    try {
                        if (isNumericValue == true) {

                            Integer val1 = Integer.valueOf(object1.optInt(key));
                            Integer val2 = Integer.valueOf(object2.optInt(key));

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        } else {
                            String val1 = object1.optString(key);
                            String val2 = object2.optString(key);

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        }
                    } catch (Exception e) {
                        alert.prepareError("Exception in sorting JSON Array. Exception:", e).log();
                        throw e;
                    }
                }
            });

            // Construct result JSON Array
            JSONArray sortedJSONArray = new JSONArray();
            for (int i = 0; i < jsonValuesList.size(); i++) {
                sortedJSONArray.put(jsonValuesList.get(i));
            }

            return sortedJSONArray;
        } catch (Exception e) {
            alert.prepareError("Exception in sorting JSON Array. Exception:", e).log();
            throw e;
        }
    }
    
    
    public static List<Record> sortListOfRecords(List<Record> recordsArray , String key, boolean isAscending,
            boolean isNumericValue) {

        try {

            if (recordsArray == null || recordsArray.size() == 0 || StringUtils.isBlank(key)) {
                return recordsArray;
            }

                  // Sort JSON Array
            Collections.sort(recordsArray, new Comparator<Record>() {

                @Override
                public int compare(Record rec1, Record rec2) {

                    try {
                        if (isNumericValue == true) {

                            Integer val1 = Integer.valueOf(rec1.getParamValueByName(key));
                            Integer val2 = Integer.valueOf(rec2.getParamValueByName(key));

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        } else {
                            String val1 = rec1.getParamValueByName(key);
                            String val2 = rec2.getParamValueByName(key);

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        }
                    } catch (Exception e) {
                        alert.prepareError("Exception in sorting Records Array. Exception:", e).log();
                        throw e;
                    }
                }
            });


            return recordsArray;
        } catch (Exception e) {
            alert.prepareError("Exception in sorting JSON Array. Exception:", e).log();
            throw e;
        }
    }


    /**
     * Method to sort Dataset of Records on a particular attribute
     * 
     * @param dataset
     * @param key
     * @param isAscending
     * @param TRUE
     *            if 'key' is of numeric type
     * @return sorted Dataset
     */
    public static Dataset sortDatasetofRecords(Dataset dataset, String key, boolean isAscending,
            boolean isNumericValue) {

        try {

            if (dataset == null || dataset.getAllRecords().size() == 0 || StringUtils.isBlank(key)) {
                return dataset;
            }

            // Add Records into List
            List<Record> recordsList = new ArrayList<>(dataset.getAllRecords());

            // Sort JSON Array
            Collections.sort(recordsList, new Comparator<Record>() {

                @Override
                public int compare(Record record1, Record record2) {

                    try {
                        if (isNumericValue == true) {

                            Integer val1 = Integer.valueOf(record1.getParamByName(key).getValue());
                            Integer val2 = Integer.valueOf(record2.getParamByName(key).getValue());

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        } else {
                            String val1 = record1.getParamByName(key).getValue();
                            String val2 = record2.getParamByName(key).getValue();

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        }
                    } catch (Exception e) {
                        alert.prepareError("Exception in sorting Dataset. Exception:", e).log();
                        throw e;
                    }
                }
            });

            // Construct result Dataset
            Dataset sortedDataset = new Dataset();
            sortedDataset.addAllRecords(recordsList);

            return sortedDataset;
        } catch (Exception e) {
            alert.prepareError("Exception in sorting Dataset. Exception:", e).log();
            throw e;
        }
    }

    /**
     * Method to sort List of Records on a particular attribute
     * 
     * @param records
     * @param key
     * @param isAscending
     * @param TRUE
     *            if 'key' is of numeric type
     * @return sorted Records
     */
    public static List<Record> sortRecordsByParamAttribute(List<Record> records, String key, boolean isAscending,
            boolean isNumericValue) {

        try {

            if (records == null || records.size() == 0 || StringUtils.isBlank(key)) {
                return records;
            }

            // Clone Records List
            List<Record> recordsList = new ArrayList<>(records);

            Collections.sort(recordsList, new Comparator<Record>() {

                @Override
                public int compare(Record record1, Record record2) {

                    try {
                        if (isNumericValue == true) {

                            Integer val1 = Integer.valueOf(record1.getParamByName(key).getValue());
                            Integer val2 = Integer.valueOf(record2.getParamByName(key).getValue());

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        } else {
                            String val1 = record1.getParamByName(key).getValue();
                            String val2 = record2.getParamByName(key).getValue();

                            if (isAscending == true) {
                                return val1.compareTo(val2);
                            } else {
                                return val2.compareTo(val1);
                            }
                        }
                    } catch (Exception e) {
                        alert.prepareError("Exception in sorting Dataset. Exception:", e).log();
                        throw e;
                    }
                }
            });

            return recordsList;

        } catch (Exception e) {
            alert.prepareError("Exception in sorting Records. Exception:", e).log();
            throw e;
        }
    }

    /**
     * Method to sort Map of JSON Objects based on an attribute
     * 
     * @param objectMap
     * @param key
     * @param isAscending
     * @param isNumeric
     * @return Sorted Map
     */
    public static Map<String, JSONObject> sortJSONObjectMapByValue(Map<String, JSONObject> objectMap, String key,
            boolean isAscending, boolean isNumeric) {

        try {
            // Create a list from elements of HashMap
            List<Map.Entry<String, JSONObject>> objectList =
                    new LinkedList<Map.Entry<String, JSONObject>>(objectMap.entrySet());

            // Sort the List
            Collections.sort(objectList, new Comparator<Map.Entry<String, JSONObject>>() {
                public int compare(Map.Entry<String, JSONObject> object1, Map.Entry<String, JSONObject> object2) {
                    int sortValue = 0;
                    if (isNumeric) {
                        Integer value1 = Integer.valueOf(object1.getValue().optInt(key));
                        Integer value2 = Integer.valueOf(object2.getValue().optInt(key));
                        sortValue = value1.compareTo(value2);
                    } else {
                        String value1 = object1.getValue().optString(key);
                        String value2 = object2.getValue().optString(key);
                        sortValue = value1.compareTo(value2);
                    }
                    if (isAscending) {
                        return sortValue;
                    } else {
                        return -sortValue;
                    }
                }
            });

            // Prepare Result Map
            Map<String, JSONObject> sortedMap = new LinkedHashMap<String, JSONObject>();
            for (Map.Entry<String, JSONObject> entry : objectList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            return sortedMap;
        } catch (Exception e) {
            alert.prepareError("Exception in sorting Map. Exception:", e).log();
            throw e;
        }
    }

    /**
     * @param recordsPerPage
     * @param pageNumber
     * @return
     */
    public static int calculateOffset(int recordsPerPage, int pageNumber) {
        int offset = recordsPerPage * (pageNumber - 1);
        if (offset < 0) {
            return 0;
        }
        return offset;
    }

    /**
     * @param sourceInputStream
     * @return
     * @throws IOException
     */
    public static File getInputStreamAsFile(InputStream sourceInputStream) throws IOException {
        File file = null;
        file = File.createTempFile("prefix", "suffix");
        FileOutputStream result = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = sourceInputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        result.close();
        return file;
    }
    
    public static boolean containSpecialChars(String inputString)
    {
//    	if(inputString.contains("+")||inputString.contains("-")||inputString.contains("="))
    	if(inputString.contains("+")||inputString.contains("="))
    	{
    		return true;
    	}
    	return false;
    	
    }
    
    public static boolean containAnySpecialCharacters(String inputString)
    { 			
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(inputString);
		return m.find();
    }
    public static boolean containAnySpecialCharactersForDescription(String inputString)
    { 			
		Pattern p = Pattern.compile("[^'\\\"a-z0-9 ,._-]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(inputString);
		return m.find();
    }
    
    public static boolean containSpecialCharacters(String inputString)
    { 			
		Pattern p = Pattern.compile("[^a-z0-9 ,.-]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(inputString);
		return m.find();
    }
    public static boolean containSpecialCharactersCsr(String inputString)
    {             
        Pattern p = Pattern.compile("[~!@#$%^&*,./-]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inputString);
        return m.find();
    }

    public static String doubleToStringWithoutScientificNotation(Double dd) {
		
    	if(null == dd) {
    		return StringUtils.EMPTY;
    	}
		DecimalFormat df = new DecimalFormat("#.0");
		df.setMaximumFractionDigits(8);
		return df.format(dd);
	}
    
    public static String prependSingleQuoteIfFirstCharIsTriggerChar(String str) {
		
    	if(StringUtils.isBlank(str)) {
    		return str;
    	}
    	
    	char firstChar = str.charAt(0);
    	switch(firstChar) {
    	
    		case '=': str = "'="+str.substring(1);
    				break;
    		
    		case '+': str = "'+"+str.substring(1);
    				break;
    		
    		case '-': str = "'-"+str.substring(1);
    				break;
    		
    		case '@': str = "'@"+str.substring(1);
			break;
    		
    	}
    	
    	return str;
	}

    public static Set<String> getLoggedInUserPermissions(DataControllerRequest request){
        try {
        	
            return _getUserPermissions(request);
        } catch (Exception E) {
            alert.prepareError("Exception at CommonUtilities() - getLoggedInUserPermissions: " + E).log();
            return null;
        }
    }
    // Commenting this method, as it is not being used anywhere.
    /*public static Set<String> getLoggedInUserPermissions(FabricRequestManager request){
        try{
            IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
            return _getUserPermissions(identityHandler);
        } catch (Exception E) {
            alert.prepareError("Exception at CommonUtilities() - getLoggedInUserPermissions: " + E).log();
            return null;
        }
    }*/

    public static Map<String, String> getLoggedInUserAttributes(DataControllerRequest dcRequest){
        try{
            IdentityHandler identityHandler = dcRequest.getServicesManager().getIdentityHandler();
            return _getUserAttributes(identityHandler);
        }catch(Exception E){
            alert.prepareError("Exception at CommonUtilities() - getLoggedInUserAttributes: " + E).log();
            return null;
        }
    }

    public static Map<String, String> getLoggedInUserAttributes(FabricRequestManager requestManager){
        try{
            IdentityHandler identityHandler = requestManager.getServicesManager().getIdentityHandler();
            return _getUserAttributes(identityHandler);
        }catch(Exception E){
            alert.prepareError("Exception at CommonUtilities() - getLoggedInUserAttributes: " + E).log();
            return null;
        }
    }

    /**
     * @description returns a map of all the user attributes set in the session
     * @param identityHandler
     * @return Map<String, String> userAttributes
     */
    private static Map<String, String> _getUserAttributes(IdentityHandler identityHandler){
        Map<String, String> userAttributes = new HashMap<>();
        try {
            Map<String, Object> userAttributesMap = identityHandler.getUserAttributes();
            for(String attrKey : userAttributesMap.keySet()){
                userAttributes.put(attrKey, (String) userAttributesMap.get(attrKey));
            }
        } catch(Exception E){
            alert.prepareError("Exception at CommonUtilities() - _getUserAttributes: " + E).log();
            return null;
        }
        return userAttributes;
    }

    /**
     * @description returns a set of active permissions for the logged in user
     * @param identityHandler
     * @return Map<String, String> userAttributes
     */
    private static Set<String> _getUserPermissions(DataControllerRequest request){
    	Set<String > userPermissions = new HashSet<String>();
        try {
        	String identityPermissions = SessionMemoryManager.getFromSession(request, "permissions").toString();
        	if (StringUtils.isNotBlank(identityPermissions)) {
        		JSONArray jsonArray = new JSONArray(identityPermissions);
        		if (jsonArray.length() > 0) {
        			for(int i=0;i<jsonArray.length();i++)
        			{
        				userPermissions.add(jsonArray.getString(i));
        		}
        	}
        }
    } catch (Exception E) {
    	alert.prepareError("Exception at CommonUtilities() - _getUserPermissions: " + E).log();
    	return null;
    }
        return userPermissions;
    }

    public static String getSQLConcatenatedString(Set<String> stringSet){
        return "'" + String.join("','", stringSet) + "'";
    }

    /**
     * @description function to splice/paginate the JSONArray received from the db response
     * @param {JSONArray} dataArray
     * @param {Integer} pageSize
     * @param {Integer} pageOffset (0-based)
     * @return {JSONArray} paginatedArray
     *
     * TODO: try to move pagination logic to sql proc
     *
     * sample queries ->
     * select * from approvalrequests limit 22, 4; - mysql
     * select * from approvalrequests OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY; - oracle sql & mssql
     */
    public static JSONArray getPaginatedJSONArray(JSONArray dataArray, Integer pageSize, Integer pageOffset){
        //TODO: try to move paginatino to proc
        // select * from approvalrequests limit 22, 4; - mysql
        // select * from approvalrequests OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY; - oracle sql & mssql
        //
        if(pageOffset == null || pageSize == null || pageOffset < 0 || pageSize < 1){
            return dataArray;       // if any of the parameters are null or inconsistent, return the un-paginated array
        }
        JSONArray paginatedArray = new JSONArray();
        int dataArrLen = dataArray.length();
        int fromIndex = (pageOffset) * pageSize;
        if(fromIndex >= dataArrLen){
            return paginatedArray;  // return the empty array, if fromIndex is exceeding the number of records
        }
        int toIndex = fromIndex + pageSize;
        if(toIndex > dataArrLen){
            toIndex = dataArrLen;   // clamp the toIndex to the size of the dataArray
        }
        for(int i=fromIndex; i<toIndex; i++){
            paginatedArray.put(dataArray.get(i));
        }
        return paginatedArray;
    }
    //
	public static boolean isBackendResponseSuccess(Result result, String paramName) {
		boolean isSuccess = false;
		String paramValue = (result != null) ? result.getParamValueByName(paramName) : null;
		if (paramValue != null) {
			switch (paramName) {
			case "status":
				isSuccess = paramValue.equals("Success");
				break;
			case "httpStatusCode":
				isSuccess = paramValue.equals("200") || paramValue.equals("201");
				break;
			case "opstatus":
				isSuccess = paramValue.equals("0");
				break;
			}
		}
		return isSuccess;
	}

	//
	public static JSONObject getJSONFromRequest(DataControllerRequest request) {
		JSONObject json = new JSONObject();
		request.getParameterNames().forEachRemaining(name -> {
			json.put(name, request.getParameter(name));
		});
		return json;
	}

	//
	public static JSONObject getEntityItemEntry(String entityItemDefinitionName, JSONArray entityItems, String name) {
		JSONObject entry = new JSONObject();

		Optional<Object> s = StreamSupport.stream(entityItems.spliterator(), true)
				.filter(item -> (StringUtils.equals(((JSONObject) item).optString("name"), name) && StringUtils
						.equals(((JSONObject) item).optString("entityItemDefinitionName"), entityItemDefinitionName)))
				.findFirst();

		if (!s.isPresent())
			return null;
		else {
			JSONObject obj = (JSONObject) s.get();
			entry.put("entry", obj.optString("entry"));
			entry.put("id", obj.optString("id"));
			entry.put("type", obj.optString("type"));
			entry.put("version", obj.optString("version"));
			entry.put("name", obj.optString("name"));
			entry.put("entryHash", obj.optString("entryHash"));
			return entry;
		}
	}
	//
	public static boolean isValidJsonArray(String input) {
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(input);
			if(jsonElement.isJsonArray()) {
				StreamSupport.stream(jsonElement.getAsJsonArray().spliterator(), true).map(n -> (JsonElement) n).forEach((n)->{
					// Validating each JSON Object inside JSON Array
					if(!n.isJsonObject()) {
						throw new JsonParseException("Not a JSON Object");
					}
				});
			}else {
				return false;
			}
		}catch(JsonParseException e) {
			return false;
		}
		return true;
	}
	//
	public static Result withSuccessParams(Result result) {
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return result;
	}
	public static boolean validRequestInputForHTMLTags(DataControllerRequest request) {
		Iterator<String> parameters = request.getParameterNames();
		while(parameters.hasNext()) {
			String paramValue = request.getParameter(parameters.next());
			if(StringUtils.isNotBlank(paramValue)) {
				String expression = "(</*[a-z|A-Z|0-9]*>)+";
				Pattern pattern = Pattern.compile(expression);
		        Matcher matcher = pattern.matcher(paramValue);
		        boolean matchFound = matcher.find();
		        if(matchFound) {
		        	return false;
		        }
			}
		}
		return true;
	}
	/**
	 * Returns true only if the input pay load is valid
	 * @param request
	 * @return boolean
	 */
	public static boolean validateCompleteRequestInputJson(DataControllerRequest request) {
		Iterator<String> parameters = request.getParameterNames();
		while (parameters.hasNext()) {
			String paramJson = request.getParameter(parameters.next());
			if (StringUtils.isNotBlank(paramJson)) {
				if (!JSONInputValidatorUtil.isValidNestedJsonInput(paramJson)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static String messageDescriptionValidation(String messageDescription) {
		
		String p = "(<)\\/?(?!a|i|u|b|br|div|span|ul|li|table|tbody|tr|th|td|font|blockquote|h1|h2|h3|h4|p|img)[^(>)]*(>)";

		messageDescription = messageDescription.replaceAll("alert\\(", "(").replaceAll("iframe", "")
				.replaceAll("prompt\\(", "(").replaceAll("onload=", "").replaceAll("onerror=", "").replaceAll("confirm\\(", "(").replaceAll(p, "<");

		return messageDescription;
	}
	
	public static String getUniqueNumericString(int length) {
		SimpleDateFormat idFormatter = new SimpleDateFormat("ssSSS");
		String dateString = idFormatter.format(new Date());
		String randomString;
		SecureRandom secureRand = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		int randStrLen;
		if (length > 10) {
			randStrLen = length - dateString.length();
		} else {
			randStrLen = 10 - dateString.length();
		}
		for (int i = 0; i < randStrLen; i++) {
			sb.append(Integer.toString(secureRand.nextInt(10)));
		}
		randomString = sb.toString();
		return randomString + dateString;

	}

	public static String getFormattedTimeStamp(Date dt, String format) {
		String dtFormat = "yyyy-MM-dd'T'HH:mm:ss";
		if (StringUtils.isNotBlank(format)) {
			dtFormat = format;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
		return formatter.format(dt);
	}

	private static SimpleDateFormat[] expectedFormats = new SimpleDateFormat[] {
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("MM/dd/yyyy"), new SimpleDateFormat("dd MMM yy HH:mm") };

	public static Date getFormattedTimeStamp(String dt) {

		for (int i = 0; i < expectedFormats.length; i++) {
			try {
				return expectedFormats[i].parse(dt);
			} catch (Exception e) {
			}
		}
		return new Date();
	}
}
