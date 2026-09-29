package com.temenos.infinity.api.arrangements.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.ehcache.ResultCacheImpl;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.nimbusds.jose.JWSAlgorithm;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.MSCertificateConstants;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.dto.AccountsDTO;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate; 

/**
 * <p>
 * THIS CLASS WAS ADDED AS A TEMPORARY SOLUTION TO CONTAIN NON-STANDARDISED UTILITIES THAT WERE BEING USED BY THIS
 * EXPERIENCE API
 * </p>
 *
 */
public class ArrangementsUtils {

    private ArrangementsUtils() {

    }

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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

    public static int calculateOffset(int recordsPerPage, int pageNumber) {
        int offset = recordsPerPage * (pageNumber - 1);
        if (offset < 0) {
            return 0;
        }
        return offset;
    }

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

    public static String convertDateFormat(String dob, String to) throws ParseException {
        if (dob == null || "".equals(dob)) {
            return null;
        }
        Date date = getFormattedTimeStamp(dob);
        return getFormattedTimeStamp(date, to);
    }

    public static String getFormattedTimeStamp(Date dt, String format) {
        String dtFormat = "yyyy-MM-dd'T'HH:mm:ss";
        if (StringUtils.isNotBlank(format)) {
            dtFormat = format;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
        return formatter.format(dt);
    }

    public static Date getFormattedTimeStamp(String dt) {
        SimpleDateFormat[] expectedFormats = new SimpleDateFormat[] { new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
                new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("MM/dd/yyyy") };
        for (int i = 0; i < expectedFormats.length; i++) {
            try {
                return expectedFormats[i].parse(dt);
            } catch (ParseException e) {
            }
        }
        return new Date();
    }

    public static String convertDateToYYYYMMDD(String date) {
        if (date.length() > 10) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate;
            try {
                parsedDate = simpleDateFormat.parse(date);
                return simpleDateFormat.format(parsedDate);
            } catch (ParseException e) {
               alert.prepareError(e.toString()).log();
                return "";
            }
        }
        return date;
    }

    /**
     * with DCR
     * 
     * @param key
     * @param value
     * @param request
     * @throws Exception
     */
    public static <T> void insertIntoSession(String key, T value, DataControllerRequest request) {
        try {
            ServicesManager servicesManager = request.getServicesManager();
            String sessionId = request.getHeader(TemenosConstants.COOKIE);
            insertDataIntoCache(servicesManager, key, value, sessionId);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
        }
    }

    /**
     * overloaded with FabricRequestManager
     * 
     * @param key
     * @param value
     * @param requestManager
     * @throws Exception
     */
    public <T> void insertIntoSession(String key, T value, FabricRequestManager request) {
        try {
            ServicesManager servicesManager = request.getServicesManager();
            String sessionId = request.getHeadersHandler().getHeader(TemenosConstants.COOKIE);
            insertDataIntoCache(servicesManager, key, value, sessionId);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
        }
    }

    /**
     * inserts data into cache
     * 
     * @param servicesManager
     * @param key
     * @param value
     */
    private static <T> void insertDataIntoCache(ServicesManager servicesManager, String key, T value,
            String sessionId) {
        try {
            HashMap<String, T> sessionCacheMap;
            String cacheKey = "";
            /*
             * check whether already the cache exists
             */
            String userId = servicesManager.getIdentityHandler() != null

                    ? servicesManager.getIdentityHandler().getUserId()
                    : "";
            /*
             * checking the flow - if userid exists post login else pre login
             */
            cacheKey = StringUtils.isNotBlank(userId) && !TemenosConstants.USER_ID_ANONYMOUS
                    .equalsIgnoreCase(servicesManager.getIdentityHandler().getUserId()) ? userId : sessionId;
            ResultCache resultCache = null;
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();

            Object cachedData = resultCache.retrieveFromCache(cacheKey);
            if (cachedData == null) {
                sessionCacheMap = new HashMap<>();
            } else {
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, Object>>() {
                }.getType();
                sessionCacheMap = gson.fromJson((String) cachedData, type);
            }
            sessionCacheMap.put(key, value);
            Gson gson = new Gson();
            String jsonString = gson.toJson(sessionCacheMap);
            resultCache.insertIntoCache(cacheKey, jsonString, 3600);
        } catch (Exception e) {
            alert.prepareError("Exception occured while storing data into ResultCache" + e).log();
        }
    }

    /**
     * with DCR
     * 
     * @param key
     * @param dcRequest
     * @return
     * @throws Exception
     */
    public static Object retreiveFromSession(String key, DataControllerRequest dcRequest) {
        try {
            ServicesManager servicesManager = dcRequest.getServicesManager();
            String sessionId = dcRequest.getHeader(TemenosConstants.COOKIE);
            return retriveDataFromCache(servicesManager, key, sessionId);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
            return null;
        }

    }

    /**
     * overloaded method with FabricRequestManager
     * 
     * @param key
     * @param fabricRequestManager
     * @return
     * @throws Exception
     */
    public static Object retreiveFromSession(String key, FabricRequestManager dcRequest) {
        try {
            ServicesManager servicesManager = dcRequest.getServicesManager();
            String header = dcRequest.getHeadersHandler().getHeader(TemenosConstants.COOKIE);
            return retriveDataFromCache(servicesManager, key, header);
        } catch (Exception e) {
            alert.prepareError("Exception occured:" + e).log();
            return null;
        }

    }

    /**
     * retrieves data from cache
     * 
     * @param servicesManager
     * @param result
     * @param key
     */
    @SuppressWarnings("deprecation")
    private static Object retriveDataFromCache(ServicesManager servicesManager, String key, String sessionId) {
        Object result = null;
        try {
            String cacheKey = "";
            String userId =
                    servicesManager.getIdentityHandler() != null ? servicesManager.getIdentityHandler().getUserId()
                            : "";
            cacheKey = StringUtils.isNotBlank(userId) && !TemenosConstants.USER_ID_ANONYMOUS.equalsIgnoreCase(userId)
                    ? userId
                    : sessionId;
            cacheKey = cacheKey + "_" + key;
            ResultCache resultCache = servicesManager.getResultCache();
            String valueInCache = "";
            try {
                valueInCache = resultCache.retrieveFromCache(cacheKey) != null
                        ? (String) resultCache.retrieveFromCache(cacheKey)
                        : null;
            } catch (Exception e) {
                try {
                    valueInCache = (String) ServicesManagerHelper.getServicesManager().getResultCache()
                            .retrieveFromCache(cacheKey);
                } catch (MiddlewareException e1) {
                    alert.prepareError(e1.toString()).log();
                    valueInCache = (String) ResultCacheImpl.getInstance().retrieveFromCache(cacheKey);
                }
            }
            /*if (StringUtils.isNotBlank(valueInCache)) {
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, Object>>() {
                }.getType();
                Map<String, Object> resultMap = gson.fromJson(valueInCache, type);
                if (resultMap != null) {
                    if (resultMap.get(key) != null) {
                        result = resultMap.get(key);
                    }
                }
            }*/
            result = valueInCache;
        } catch (Exception e) {
            alert.prepareError("Exception occured while retrieving from cache" + e).log();
            return null;
        }
        return result;
    }

    public static String buildOdataCondition(String field, String operator, String value) {

        StringBuilder sb = new StringBuilder();
        sb.append(field);
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        sb.append(value);
        return sb.toString();
    }

    public static String buildSearchGroupQuery(String left, String operator, String right, Boolean group) {

        StringBuilder sb = new StringBuilder();
        if (group) {
            sb.append("(");
        }
        sb.append(left);
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        sb.append(right);
        if (group) {
            sb.append(")");
        }
        return sb.toString();
    }

    public static Map<String, String> getBackendId(DataControllerRequest request, String customerId, String backendType,
            String identifier_name, String sequenceNumber) {

        String backendId = "";
        String companyId = "";
        try {

            HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
            HashMap<String, Object> svcParams = new HashMap<String, Object>();
            String filter = buildOdataCondition(TemenosConstants.CUSTOMER_ID, TemenosConstants.EQUAL, customerId);
            filter = StringUtils.isNotBlank(sequenceNumber)
                    ? buildSearchGroupQuery(filter, TemenosConstants.AND,
                            buildOdataCondition(TemenosConstants.PARAM_SEQUENCENUMBER, TemenosConstants.EQUAL,
                                    sequenceNumber),
                            false)
                    : filter;
            filter = StringUtils.isNotBlank(backendType) ? buildSearchGroupQuery(filter, TemenosConstants.AND,
                    buildOdataCondition(TemenosConstants.BACKEND_TYPE, TemenosConstants.EQUAL, backendType), false)
                    : filter;
            svcParams.put(TemenosConstants.PARAM_DOLLAR_FILTER, filter);

            String DBXBackendIdDetails = "";

            try {
                DBXBackendIdDetails = DBPServiceExecutorBuilder.builder()
                        .withServiceId(ArrangementsAPIServices.DBXUSER_GET_BACKENDIDENTIFIERDETAILS.getServiceName())

                        .withOperationId(ArrangementsAPIServices.DBXUSER_GET_BACKENDIDENTIFIERDETAILS.getOperationName())

                        .withRequestParameters(svcParams).withRequestHeaders(svcHeaders)
                        .withDataControllerRequest(request).build().getResponse();
            } catch (Exception e) {
                alert.prepareError("Business User : Backend failed to fetch account Overview details from T24 DB").log();
            }
            Result dbxBcResult = new Result();
            if (StringUtils.isNotBlank(DBXBackendIdDetails)) {
                dbxBcResult = JSONToResult.convert(new JSONObject(DBXBackendIdDetails).toString());
            }
            Dataset backendIdentifiersDataset = dbxBcResult.getDatasetById(TemenosConstants.DS_BACKEND_IDENTIFIER);
            if (null != backendIdentifiersDataset) {
                for (Record rec : backendIdentifiersDataset.getAllRecords()) {
                    if (identifier_name.equals(rec.getParamValueByName(TemenosConstants.PARAM_IDENTIFIER_NAME))) {
                        backendId = rec.getParamValueByName(TemenosConstants.BACKEND_ID);
                        companyId = rec.getParamValueByName(TemenosConstants.COMPANY_ID);
                    }

                }
            }

        } catch (Exception e) {
            alert.prepareError("Error while retrieving backendId for  " + customerId).log();
        }
        Map<String, String> backendDetails = new HashMap<String, String>();
        backendDetails.put("backendId", backendId);
        backendDetails.put("companyId", companyId);
        return backendDetails;
    }
    
    /**
     * Get Backend Certificate from DB
     * 
     * @param backendName
     */    
    public static BackendCertificate getCertFromDB(String backendName) {
        BackendCertificate backendCertificate = new BackendCertificate();
        Map<String, Object> inputMap = new HashMap<>();
        StringBuffer queryString = new StringBuffer();
        queryString.append("BackendName" + " ");
        queryString.append("eq" + " ");
        queryString.append(backendName);

        inputMap.put("$filter", queryString.toString());
        String backendCertResponse = null;
        try {
            backendCertResponse =
                    Executor.invokeService(ArrangementsAPIServices.SERVICE_BACKEND_CERTIFICATE, inputMap, null);
            diagnostic.prepareDebug("Backend Certificate" + backendCertResponse).log();
        } catch (Exception e1) {
            alert.prepareError("Service call to dbxdb failed "+e1.toString()).log();
        }
        JSONObject serviceResponseJSON = Utilities.convertStringToJSON(backendCertResponse);
        if (serviceResponseJSON == null) {
            alert.prepareError("EmptyResponse no backend certificate for MS").log();
        }
        if (serviceResponseJSON.has("backendcertificate")
                && serviceResponseJSON.getJSONArray("backendcertificate").length() > 0) {
            JSONObject certificateObj = serviceResponseJSON.getJSONArray("backendcertificate").getJSONObject(0);
            backendCertificate.setBackendName(certificateObj.getString("BackendName"));
            String certificateEncryptionKey = new String();
            try {
                certificateEncryptionKey = ServerConfigurations.AMS_PRIVATE_ENCRYPTION_KEY.getValue();
            } catch (Exception e) {
                alert.prepareError("Couldnt parse MS_PRIVATE_ENCRYPTION_KEY from environment "+ e.toString()).log();

            }

            if (StringUtils.isNotBlank(certificateEncryptionKey))
                backendCertificate.setCertificateEncryptionKey(certificateEncryptionKey);
            backendCertificate.setCertificatePrivateKey(certificateObj.getString("CertPrivateKey"));
            backendCertificate.setCertificatePublicKey(certificateObj.getString("CertPublicKey"));
            backendCertificate.setGetPublicKeyServiceURL("/sample/service");
            backendCertificate.setId(certificateObj.getString("id"));
            backendCertificate.setJwsAlgorithm(JWSAlgorithm.RS256);

        }

        return backendCertificate;

    }
    
    /**
     * Get User Attributes from Identity Session
     * 
     * @param request
     * @param attributeName
     */ 
    public static String getUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
        // TODO Auto-generated method stub
        try {
            if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
                if (userMap.get(attribute) != null) {
                    String attributeValue = userMap.get(attribute) + "";

                    return attributeValue;
                }

            }
        } catch (Exception e) {
           
            alert.prepareError(e.toString()).log();
        }

        return "";
    }
    
    /**
     * Method to add Authorization Headers for MS
     * 
     * param String authToken
     * param Map<String, Object> headerMap
     * @return Map<String, Object> headerMap
     */
	public static Map<String, Object> generateSecurityHeaders(String authToken, Map<String, Object> headerMap) {
		headerMap.put("Authorization", authToken);
		if (StringUtils.isNotEmpty(ServerConfigurations.AMS_DEPLOYMENT_PLATFORM.getValueIfExists())) {
			if (StringUtils.equalsIgnoreCase(ServerConfigurations.AMS_DEPLOYMENT_PLATFORM.getValueIfExists(),
					MSCertificateConstants.AWS))
				headerMap.put("x-api-key", ServerConfigurations.AMS_AUTHORIZATION_KEY.getValueIfExists());
			else if (StringUtils.equalsIgnoreCase(ServerConfigurations.AMS_DEPLOYMENT_PLATFORM.getValueIfExists(),
					MSCertificateConstants.AZURE))
				headerMap.put("x-functions-key", ServerConfigurations.AMS_AUTHORIZATION_KEY.getValueIfExists());
		}
		headerMap.put("roleId", ServerConfigurations.AMS_ROLE_ID.getValueIfExists());
		return headerMap;
	}
	
	public static Map<String, Object> generateSecurityHeadersForHMS(String authToken, Map<String, Object> headerMap) {
		headerMap.put("Authorization", authToken);
		if (StringUtils.isNotEmpty(ServerConfigurations.HOLDINGS_DEPLOYMENT_PLATFORM.getValueIfExists())) {
			if (StringUtils.equalsIgnoreCase(ServerConfigurations.HOLDINGS_DEPLOYMENT_PLATFORM.getValueIfExists(),
					MSCertificateConstants.AWS))
				headerMap.put("x-api-key", ServerConfigurations.HOLDINGS_AUTHORIZATION_KEY.getValueIfExists());
			else if (StringUtils.equalsIgnoreCase(ServerConfigurations.HOLDINGS_DEPLOYMENT_PLATFORM.getValueIfExists(),
					MSCertificateConstants.AZURE))
				headerMap.put("x-functions-key", ServerConfigurations.HOLDINGS_AUTHORIZATION_KEY.getValueIfExists());
		}
		headerMap.put("roleId", ServerConfigurations.HOLDINGS_ROLE_ID.getValueIfExists());
		return headerMap;
	}
	
	public static Map<String, Object> generateSecurityHeadersForSRMS(String authToken, Map<String, Object> headerMap) {
		headerMap.put("Authorization", authToken);
		if (StringUtils.isNotEmpty(ServerConfigurations.SERVICE_REQUEST_DEPLOYMENT_PLATFORM.getValueIfExists())) {
			if (StringUtils.equalsIgnoreCase(ServerConfigurations.SERVICE_REQUEST_DEPLOYMENT_PLATFORM.getValueIfExists(),
					MSCertificateConstants.AWS))
				headerMap.put("x-api-key", ServerConfigurations.SERVICE_REQUEST_AUTHORIZATION_KEY.getValueIfExists());
			else if (StringUtils.equalsIgnoreCase(ServerConfigurations.SERVICE_REQUEST_DEPLOYMENT_PLATFORM.getValueIfExists(),
					MSCertificateConstants.AZURE))
				headerMap.put("x-functions-key", ServerConfigurations.SERVICE_REQUEST_AUTHORIZATION_KEY.getValueIfExists());
		}
		headerMap.put("roleId", ServerConfigurations.SERVICE_REQUEST_ROLE_ID.getValueIfExists());
		return headerMap;
	}
	 
	public static HashMap<String, AccountsDTO> getAccountsMapFromCache(DataControllerRequest request) {
        String accountsInSessionGson = (String) retreiveFromSession("Accounts", request);
        if (StringUtils.isNotBlank(accountsInSessionGson)) {
            Gson gson = new Gson();
            Type AccountMapType = new TypeToken<HashMap<String, AccountsDTO>>() {
            }.getType();
            HashMap<String, AccountsDTO> accounts = gson.fromJson(accountsInSessionGson, AccountMapType);
            return accounts;
        }
        alert.prepareError("Unable to find accounts in session").log();
        return null;
    }
	
	public static String getAccountIdWithCompanyFromCache(String accountId, DataControllerRequest request){
        HashMap<String, AccountsDTO> accounts = ArrangementsUtils.getAccountsMapFromCache(request);
        AccountsDTO account = accounts.get(accountId); 
        return account.getAccountIdWithCompanyId();  
    }
	
    /*
     * Input - DataControllerRequest Instance Output - C360 Auth Token String
     */

    public static String SpotlightLogin(DataControllerRequest request) {
        Map<String, Object> headersMap = new HashMap<>();
        String api_Access_Token = "";
        try {
            api_Access_Token = ServerConfigurations.DBP_AC_ACCESS_TOKEN.getValue();
        } catch (Exception e) {
            alert.prepareError("Couldnt parse DBP_AC_ACCESS_TOKEN from environment "+ e.toString()).log();
        }
        String ac_app_key = "";
        try {
            ac_app_key = ServerConfigurations.DBP_AC_APP_KEY.getValue();
        } catch (Exception e) {
            alert.prepareError("Couldnt parse DBP_AC_APP_KEY from environment "+ e.toString()).log();
        }
        String ac_app_secret_key = "";
        try {
            ac_app_secret_key = ServerConfigurations.DBP_AC_APP_SECRET.getValue();
        } catch (Exception e) {
            alert.prepareError("Couldnt parse DBP_AC_APP_SECRET from environment "+ e.toString()).log();
        }
        headersMap.put(TemenosConstants.API_ACCESS_TOKEN_KEY, api_Access_Token);
        headersMap.put(TemenosConstants.AC_APP_KEY, ac_app_key);
        headersMap.put(TemenosConstants.AC_APP_SECRET_KEY, ac_app_secret_key);
        JSONObject result = new JSONObject();

        try {
            result = invokeServiceAndGetJson(request, null, headersMap, TemenosConstants.C360_LOGIN_SERVICE,
                    TemenosConstants.C360_LOGIN_OPERATION, null);
        } catch (Exception e) {
            alert.prepareError("Failed to fetch API Auth Token from Customer360. Service Response:" + e).log();
        }
        String claimsToken = "";
        if (result != null && result.has(TemenosConstants.CLAIMS_TOKEN_KEY))
            claimsToken = result.get(TemenosConstants.CLAIMS_TOKEN_KEY).toString();
        if (StringUtils.isBlank(claimsToken)) {
            alert.prepareError("C360 Auth Token Null" + claimsToken).log();
            return StringUtils.EMPTY;
        }
        return claimsToken;
    }

    /*
     * I - bundleName - String I - configKey - String O - Configurations -
     * JSONObject
     */

    public static JSONObject getBundleConfigurations(String bundleName, String configKey,
            DataControllerRequest request) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        String serviceName = TemenosConstants.C360_CONFIGURATION_SERVICE;
        String operationName = TemenosConstants.C360_CONFIGURATION_OPERATION;
        StringBuilder filter = new StringBuilder();

        if (StringUtils.isNotBlank(bundleName)) {
            filter.append(TemenosConstants.BUNDLE_NAME + " eq '" + bundleName + "'");
        }
        if (StringUtils.isNotBlank(configKey)) {
            filter.append(" and " + TemenosConstants.CONFIG_KEY + " eq '" + configKey + "'");
        }
        params.put("$filter", filter.toString());
        // Authenticate C360
        String AuthToken = SpotlightLogin(request);

        if (StringUtils.isBlank(AuthToken)) {
            alert.prepareError("C360 authentication failed. Aborting get configurations").log();
            return new JSONObject();
        }
        headerMap.put("backendToken", AuthToken);
        headerMap.put(TemenosConstants.PARAM_X_KONY_AUTHORIZATION,
                request.getHeader(TemenosConstants.PARAM_X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()); 
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        JSONObject configurations = new JSONObject();
        try {
            configurations = invokeServiceAndGetJson(request, params, headerMap, serviceName, operationName, null);
        } catch (Exception e) {
            alert.prepareError("Failed to fetch Bundle Configurations:" + e).log();
            return new JSONObject();
        }
        return configurations;
    }

    @SuppressWarnings("deprecation")
    public static JSONObject invokeServiceAndGetJson(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String serviceName, String operationName, String objectName) {

        try {
            String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName,
                    operationName, inputParams, headerParams, dcRequest);
            if (StringUtils.isNotBlank(responseString)) {
                return new JSONObject(responseString);
            }
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + operationName, e).log();
        }
        return getExceptionMsgAsJson(serviceName, operationName);
    }

    private static JSONObject getExceptionMsgAsJson(String serviceName, String Opertaion) {
        JSONObject errResponse = new JSONObject();
        StringBuilder message = new StringBuilder();
        message.append("Exception occured while invoking service with [ServiceId_ObjectId_OperationId] [")
                .append(serviceName + "_" + Opertaion).append("]");
        errResponse.put("errmsg", message.toString());
        return errResponse;
    }
    
    public static <T> void insertDataIntoCache(DataControllerRequest request, T value, String key, int time) {
        try {
            ServicesManager servicesManager = request.getServicesManager();
            ResultCache resultCache = servicesManager.getResultCache();
            Gson gson = new Gson();
            String jsonString = gson.toJson(value);
            resultCache.insertIntoCache(key, jsonString, time);
        } catch (Exception e) {
            alert.prepareError(e.getLocalizedMessage(), e).log();
        }
    }
    
    public static Object getDataFromCache(DataControllerRequest request, String key) {
        Object cachedData = null;
        try {
            ServicesManager servicesManager = request.getServicesManager();
            ResultCache resultCache = servicesManager.getResultCache();
            cachedData = resultCache.retrieveFromCache(key);
        } catch (Exception e) {
            alert.prepareError(e.getLocalizedMessage(), e).log();
        }
        return cachedData;
    }
    
    public static Properties loadProps(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = ArrangementsUtils.class.getClassLoader()
                .getResourceAsStream(fileName+".properties")) {
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            alert.prepareError("Error while loading properties", e).log();
        }
        return properties; 
    }


}
