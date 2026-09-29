/**
 * 
 */
package com.infinity.dbx.temenos.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.externalAccounts.ExternalAccount;
import com.kony.dbx.objects.Account;
import com.kony.dbx.objects.Customer;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.ehcache.ResultCacheImpl;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.nimbusds.jose.util.IOUtils;

public class TemenosUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    public Map<String, String> transactionTypesMap = new HashMap<>();
    public Map<String, String> accountTypesMap = new HashMap<>();
    public Map<String, String> bannerAccountTypesMap = new HashMap<>();
    public Map<String, String> userMap = new HashMap<>();

    /*
     * static holder design pattern to create singleton object
     */
    private static class Holder {
        static final TemenosUtils INSTANCE = new TemenosUtils();
    }

    public static TemenosUtils getInstance() {
        return Holder.INSTANCE;
    }

    private TemenosUtils() {

    }

    /*
     * validates the result object
     */
    public boolean validateResult(Result result, String searchType, String key) {
        boolean isValid = false;
        if (result != null) {
            if (StringUtils.isNotBlank(searchType)) {
                switch (searchType) {
                case TemenosConstants.PARAM_DATASET:
                    if (result.getDatasetById(key) != null) {
                        isValid = true;
                    }
                    break;
                case TemenosConstants.PARAM_RECORD:
                    if (result.getRecordById(key) != null) {
                        isValid = true;
                    }
                    break;
                case TemenosConstants.PARAM_PARAM:
                    if (result.getParamByName(key) != null) {
                        isValid = true;
                    }
                    break;
                default:
                    break;
                }
            }
        }
        return isValid;
    }

    /**
     * with DCR
     * 
     * @param key
     * @param value
     * @param request
     * @throws Exception
     */
    public <T> void insertIntoSession(String key, T value, DataControllerRequest request) {
        try {
            ServicesManager servicesManager = request.getServicesManager();
            String sessionId = request.getHeader(Constants.DEVICE_ID);
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
            String sessionId = request.getHeadersHandler().getHeader(Constants.DEVICE_ID);
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
    private <T> void insertDataIntoCache(ServicesManager servicesManager, String key, T value, String sessionId) {
        try {
            HashMap<String, T> sessionCacheMap;
            String cacheKey = "";
            /*
             * check whether already the cache exists
             */
            String userId = servicesManager.getIdentityHandler() != null

                    ? servicesManager.getIdentityHandler().getUserId() : "";
            /*
             * checking the flow - if userid exists post login else pre login
             */
            cacheKey = StringUtils.isNotBlank(userId)
                    && !Constants.USER_ID_ANONYMOUS.equalsIgnoreCase(servicesManager.getIdentityHandler().getUserId())
                            ? userId : sessionId;
            cacheKey = cacheKey+"_"+key;
            ResultCache resultCache = null;
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();

            /*Object cachedData = resultCache.retrieveFromCache(cacheKey);
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
            String jsonString = gson.toJson(sessionCacheMap);*/
            resultCache.insertIntoCache(cacheKey, value, 3600);
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
    public Object retreiveFromSession(String key, DataControllerRequest dcRequest) {
        try {
            ServicesManager servicesManager = dcRequest.getServicesManager();
            String sessionId = dcRequest.getHeader(Constants.DEVICE_ID);
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
    public Object retreiveFromSession(String key, FabricRequestManager dcRequest) {
        try {
            ServicesManager servicesManager = dcRequest.getServicesManager();
            String header = dcRequest.getHeadersHandler().getHeader(Constants.DEVICE_ID);
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
    private Object retriveDataFromCache(ServicesManager servicesManager, String key, String sessionId) {
        Object result = null;
        try {
            String cacheKey = "";
            String userId = servicesManager.getIdentityHandler() != null
                    ? servicesManager.getIdentityHandler().getUserId() : "";
            cacheKey = StringUtils.isNotBlank(userId) && !Constants.USER_ID_ANONYMOUS.equalsIgnoreCase(userId) ? userId
                    : sessionId;
            cacheKey = cacheKey + "_" + key;
            ResultCache resultCache = servicesManager.getResultCache();
            String valueInCache = "";
            try {
                valueInCache = resultCache.retrieveFromCache(cacheKey) != null
                        ? (String) resultCache.retrieveFromCache(cacheKey) : null;
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

    /**
     * invoke object service with DataControllerRequest
     * 
     * @param serviceId
     * @param objectId
     * @param operationId
     * @param request
     * @return Result
     */
    public Result callObjectService(String serviceId, String objectId, String operationId,
            DataControllerRequest request) {
        Result result = new Result();
        try {
            ServicesManager servicesManager = request.getServicesManager();
            OperationData operationData = servicesManager.getOperationDataBuilder().withServiceId(serviceId)
                    .withVersion("v1").withObjectId(objectId).withOperationId(operationId).build();
            ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withDCRRequest(request)
                    .build();
            result = serviceRequest.invokeServiceAndGetResult();
        } catch (Exception e) {
            // TODO: handle exception
           alert.prepareError(e.toString()).log();
        }
        return result;
    }

    /**
     * overloaded method take FabricRequestManager
     * 
     * @param serviceId
     * @param objectId
     * @param operationId
     * @param request
     * @return
     */
    public Result callObjectService(String serviceId, String objectId, String operationId, FabricRequestManager request,
            JsonObject input, Map<String, Object> headerMap) {
        Result result = new Result();
        try {
            Map<String, Object> inputMap = convertJsonToMap(input);
            ServicesManager servicesManager = request.getServicesManager();
            OperationData operationData = servicesManager.getOperationDataBuilder().withServiceId(objectId)
                    .withObjectId(serviceId).withOperationId(operationId).withVersion("1.0").build();
            ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withInputs(inputMap)
                    .withHeaders(headerMap)
                    .withAuthorizationToken(request.getHeadersHandler().getHeader("X-Kony-Authorization")).build();
            result = serviceRequest.invokeServiceAndGetResult();
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return result;
    }

    public Map<String, Object> convertJsonToMap(JsonObject input) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
        Map<String, Object> inputMap = gson.fromJson(input, type);
        return inputMap;
    }

    /**
     * Copy a record to POJO Object
     * 
     * @param object
     *            Object where to copy
     * @param record
     *            Record from backend
     * 
     * @return Account
     **/
    public static Account copyToAccount(Class<Account> destinationClass, Record record) {
        Account account = new Account();
        List<Param> params = record.getAllParams();
        for (Param param : params) {
            String name = param.getName();
            String value = param.getValue();
            String methodName = "set" + toCamelCase(name);
            try {
                Method method = destinationClass.getDeclaredMethod(methodName, String.class);
                method.invoke(account, value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                // Simply ignore coping the data if the method doesn't exist
               
            }
        }
        return account;
    }

    /**
     * Copy a record to POJO Object
     * 
     * @param object
     *            Object where to copy
     * @param record
     *            Record from backend
     * 
     * @return ExternalAccount
     **/
    public static ExternalAccount copyToExtrenalAccount(Class<ExternalAccount> destinationClass, Record record) {
        ExternalAccount externalAccount = new ExternalAccount();
        List<Param> params = record.getAllParams();
        for (Param param : params) {
            String name = param.getName();
            String value = param.getValue();
            String methodName = "set" + toCamelCase(name);
            try {
                Method method = destinationClass.getDeclaredMethod(methodName, String.class);
                method.invoke(externalAccount, value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                // Simply ignore coping the data if the method doesn't exist
               
            }
        }
        return externalAccount;
    }

    /**
     * To change the given string to camel case, which is useful in cases where
     * we don't need to define multiple constants.
     * 
     * @param String
     *            input
     * 
     * @return String
     **/
    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            /*
             * char previousChar = inputString.charAt(i - 1); if (previousChar
             * == ' ') { char currentCharToUpperCase =
             * Character.toUpperCase(currentChar); result = result +
             * currentCharToUpperCase; } else { char currentCharToLowerCase =
             * Character.toLowerCase(currentChar); result = result +
             * currentCharToLowerCase; }
             */
            result = result + currentChar;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void loadTransactionTypeProperties(DataControllerRequest request) {
        Result result = new Result();
        final String CACHE_KEY_TRANSATIONTYPE_MAPPING = "transactionTypeMapping";
        final int CACHE_TIME = 10 * 60; // 10 minutes
        HashMap<String, Object> inputParams = new HashMap<>();
        HashMap<String, Object> headerParams = new HashMap<>();
        TemenosUtils temenosUtils = TemenosUtils.getInstance();

        try {
            Object object = temenosUtils.getDataFromCache(request, CACHE_KEY_TRANSATIONTYPE_MAPPING);
            if (null != object) {
                if (StringUtils.isNotBlank(object.toString())) {
                    JSONObject txnTypes = new JSONObject(object.toString());
                    if (txnTypes.length() != 0) {
                        transactionTypesMap = new Gson().fromJson(txnTypes.toString(), Map.class);
                    }
                }
                if (!transactionTypesMap.isEmpty()) {
                    return;
                }
            }
            result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
                    TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_TRANSACTIONTYPEMAPPING_GET,
                    false);
            if (null != result && !result.getAllDatasets().isEmpty()) {
                Dataset dataset = result.getAllDatasets().get(0);
                for (int i = 0; i < dataset.getAllRecords().size(); i++) {
                    Record record = dataset.getAllRecords().get(i);
                    String key = StringUtils.isNotBlank(record.getParamValueByName("backendTransactionTypeId"))
                            ? record.getParamValueByName("backendTransactionTypeId") : "";
                    String value = StringUtils.isNotBlank(record.getParamValueByName("dbxTransactionType"))
                            ? record.getParamValueByName("dbxTransactionType") : "";
                    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                        transactionTypesMap.put(key, value);
                    }

                }

            }

            temenosUtils.insertDataIntoCache(request, transactionTypesMap, CACHE_KEY_TRANSATIONTYPE_MAPPING,
                    CACHE_TIME);
        } catch (Exception e) {
        	alert.prepareError(e.toString()).log();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadAccountTypeProperties(DataControllerRequest request) {

        String CACHE_KEY_ACCOUNTTYPE_MAPPING = "accountTypeMapping";
        final int CACHE_TIME = 10 * 60; // 10 minutes

        TemenosUtils temenosUtils = TemenosUtils.getInstance();

        try {
            Object object = temenosUtils.getDataFromCache(request, CACHE_KEY_ACCOUNTTYPE_MAPPING);
            if (null != object) {
                if (StringUtils.isNotBlank(object.toString())) {
                    JSONObject txnTypes = new JSONObject(object.toString());
                    if (txnTypes.length() != 0) {
                        accountTypesMap = new Gson().fromJson(txnTypes.toString(), Map.class);
                    }
                }
                if (!accountTypesMap.isEmpty()) {
                    return;
                }
            }

            // Load account type properties from bundle configurations
            JSONObject accountTypes = getBundleConfigurations(TemenosConstants.ACCOUNT_TYPE_BUNDLE_NAME,
                    TemenosConstants.ACCOUNT_TYPE_CONFIG_KEY, request);
            String accountType = "";
            if (accountTypes != null) {
                JSONArray configurations = accountTypes.optJSONArray(TemenosConstants.CONFIGURATIONS);
                if (configurations != null && configurations.length() > 0) {
                    JSONObject configData = configurations.optJSONObject(0);
                    if (configData.has(TemenosConstants.DBP_CONFIG_TABLE_VALUE)) {
                        accountType = configData.getString(TemenosConstants.DBP_CONFIG_TABLE_VALUE);
                    }
                }
            }
            try {
                accountTypesMap = new ObjectMapper().readValue(accountType, HashMap.class);
            } catch (Exception e) {
                alert.prepareError("Unable to transform account types properties" + e).log();
            }

            temenosUtils.insertDataIntoCache(request, accountTypesMap, CACHE_KEY_ACCOUNTTYPE_MAPPING, CACHE_TIME);

        } catch (Exception e) {
            alert.prepareError("Unable to load types properties" + e).log();
        }
    }

    /**
     * To Load User Properties from resources file
     * 
     * @return void
     **/
    public void loadUserProperties() {
        Properties properties = new Properties();
        InputStream is = TemenosUtils.class.getClassLoader().getResourceAsStream("user.properties");
        try {
            properties.load(is);
            properties.forEach((key, value) -> userMap.put((String) key, (String) value));
        } catch (IOException e) {
        	alert.prepareError(e.toString()).log();
        } finally {
	    	if (is != null) {
	    		try {
	    			is.close();
	    		}
	    		catch(Exception e) {
	    			alert.prepareError(e.toString()).log();
	    		}
	    	}
	    }
    }

    public void loadBannerAccountTypeProperties() {
        Properties properties = new Properties();
        InputStream is = TemenosUtils.class.getClassLoader().getResourceAsStream("bannerAccountTypes.properties");
        try {
            properties.load(is);
            properties.forEach((key, value) -> bannerAccountTypesMap.put((String) key, (String) value));
        } catch (IOException e) {
        	alert.prepareError(e.toString()).log();
        } finally {
	    	if (is != null) {
	    		try {
	    			is.close();
	    		}
	    		catch(Exception e) {
	    			alert.prepareError(e.toString()).log();
	    		}
	    	}
	    }
    }

    public <T> void insertDataIntoCache(DataControllerRequest request, T value, String key, int time) {
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

    public Object getDataFromCache(DataControllerRequest request, String key) {
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

    public Object buildObjectFromJSONString(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<Object>() {
        }.getType();
        Object object = gson.fromJson(jsonString, type);
        return object;
    }

    public String getDateFormat(String Date, String Pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Pattern);
        String date = simpleDateFormat.format(new Date(Date));
        return date;
    }

    public static Result getEmptyResult(String transaction) {
        Result result = new Result();
        try {
            result.addDataset(new Dataset(transaction));
            CommonUtils.setOpStatusOk(result);
            result.addHttpStatusCodeParam(Constants.PARAM_HTTP_STATUS_OK);
        } catch (Exception e) {
        	alert.prepareError(e.toString()).log();
        }

        return result;
    }

    public HashMap<String, Account> getAccountsMapFromCache(DataControllerRequest request) {
        String accountsInSessionGson = (String) retreiveFromSession(Constants.SESSION_ATTRIB_ACCOUNT, request);
        if (StringUtils.isNotBlank(accountsInSessionGson)) {
            Gson gson = new Gson();
            Type AccountMapType = new TypeToken<HashMap<String, Account>>() {
            }.getType();
            HashMap<String, Account> accounts = gson.fromJson(accountsInSessionGson, AccountMapType);
            return accounts;
        }
        alert.prepareError("Unable to find accounts in session").log();
        return null;
    }

    public HashMap<String, ExternalAccount> getExternalAccountsMapFromCache(String sessionAttribute,
            DataControllerRequest request) {
        String externalAccountsInSessionGson = (String) retreiveFromSession(sessionAttribute, request);
        if (StringUtils.isNotBlank(externalAccountsInSessionGson)) {
            Gson gson = new Gson();
            Type ExternalAccountMapType = new TypeToken<HashMap<String, ExternalAccount>>() {
            }.getType();
            HashMap<String, ExternalAccount> externalAccounts = gson.fromJson(externalAccountsInSessionGson,
                    ExternalAccountMapType);
            return externalAccounts;
        }

        alert.prepareError("Unable to find " + sessionAttribute + " in session").log();
        return null;
    }

    /**
     * Create a customer object Record User Record object
     * 
     * @return Customer object
     **/
    public static Customer createCustomer(Record userRecord) {

        Customer cust = new Customer();
        userRecord.removeParamByName("test");
        // Process each of the parameters
        List<Param> params = userRecord.getAllParams();
        for (int i = 0; i < params.size(); i++) {
            Param param = params.get(i);
            switch (param.getName()) {
            case Constants.KONY_DBX_USER_LOWERCASE_ID:
                cust.setUserId(param.getValue());
                break;
            case Constants.KONY_DBX_USER_ID:
                cust.setUserId(param.getValue());
                break;
            case Constants.KONY_DBX_USER_NAME:
            case TemenosConstants.KONY_DBX_USER_NAME_CAMELCASE:
                cust.setUserName(param.getValue());
                break;
            case Constants.KONY_DBX_USER_FIRSTNAME:
            case TemenosConstants.KONY_DBX_FIRSTNAME:
                cust.setFirstName(param.getValue());
                break;
            case Constants.KONY_DBX_USER_LASTNAME:
            case TemenosConstants.KONY_DBX_LASTNAME:
                cust.setLastName(param.getValue());
                break;
            case Constants.KONY_DBX_USER_DOB:
            case TemenosConstants.KONY_DBX_DATEOFBIRTH:
                cust.setDateOfBirth(param.getValue());
                break;
            // case Constants.KONY_DBX_USER_ADDR1:
            // cust.setAddress1(param.getValue());
            // break;
            // case Constants.KONY_DBX_USER_ADDR2:
            // cust.setAddress2(param.getValue());
            // break;
            // case Constants.KONY_DBX_USER_CITY:
            // case TemenosConstants.KONY_DBX_CITY_NAME:
            // cust.setCity(param.getValue());
            // break;
            // case Constants.KONY_DBX_USER_STATE:
            // cust.setState(param.getValue());
            // break;
            // case Constants.KONY_DBX_USER_ZIP:
            // case TemenosConstants.KONY_DBX_ZIP_CODE:
            // cust.setZipcode(param.getValue());
            // break;
            case Constants.KONY_DBX_USER_DEFAULT_P2P_FROMACCT:
                cust.setDefaultAccountP2pFrom(param.getValue());
                break;
            case Constants.TEST:
                break;
            }
        }
        Dataset phonesDS = userRecord.getDatasetById("ContactNumbers");
        if (phonesDS != null) {
            for (Record rec : phonesDS.getAllRecords()) {
                if ("true".equalsIgnoreCase(CommonUtils.getParamValue(rec, TemenosConstants.PARAM_IS_PRIMARY))) {
                    cust.setPhone(CommonUtils.getParamValue(rec, "phoneCountryCode")
                            + CommonUtils.getParamValue(rec, "phoneNumber"));
                    break;
                }
            }
        }

        Dataset emailsDS = userRecord.getDatasetById("EmailIds");
        if (emailsDS != null) {
            for (Record rec : emailsDS.getAllRecords()) {
                if ("true".equalsIgnoreCase(CommonUtils.getParamValue(rec, TemenosConstants.PARAM_IS_PRIMARY))) {
                    cust.setEmail(CommonUtils.getParamValue(rec, "Value"));
                    break;
                }
            }
        }
        Dataset addressDS = userRecord.getDatasetById("Addresses");
        if (addressDS != null) {
            for (Record rec : addressDS.getAllRecords()) {
                if ("true".equalsIgnoreCase(CommonUtils.getParamValue(rec, TemenosConstants.PARAM_IS_PRIMARY))) {
                    cust.setAddress1(CommonUtils.getParamValue(rec, TemenosConstants.T24_USER_ADDR1));
                    cust.setAddress2(CommonUtils.getParamValue(rec, TemenosConstants.T24_USER_ADDR2));
                    cust.setCity(CommonUtils.getParamValue(rec, TemenosConstants.KONY_DBX_CITY_NAME));
                    cust.setState(CommonUtils.getParamValue(rec, Constants.KONY_DBX_USER_STATE));
                    cust.setZipcode(CommonUtils.getParamValue(rec, TemenosConstants.KONY_DBX_ZIP_CODE));
                    break;
                }
            }
        }
        return cust;
    }

    public static String convertDateFormat(String dateToConvert, String fromPattern, String toPattern)
            throws ParseException {
        Date date = new SimpleDateFormat(fromPattern).parse(dateToConvert);
        return new SimpleDateFormat(toPattern).format(date);
    }

    public static String readContentFromFile(String FileName) throws IOException {
        InputStream input = TemenosUtils.class.getClassLoader().getResourceAsStream(FileName);
        String value = "";
        try {
            value = IOUtils.readInputStreamToString(input, Charset.defaultCharset());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        } finally {
	    	if (input != null) {
	    		try {
	    			input.close();
	    		}
	    		catch(Exception e)
	    		{
	    			alert.prepareError(e.toString()).log();
	    		}
	    	}
	    }
        return value;
    }

    public static String getFormattedTimeStamp(Date dt, String format) {
        String dtFormat = "yyyy-MM-dd'T'HH:mm:ss";
        if (StringUtils.isNotBlank(format)) {
            dtFormat = format;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
        return formatter.format(dt);
    }

    // Check input account is logged in customer's account
    public static boolean checkCustomerAccount(DataControllerRequest request, String accountId) {
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
        if (accounts != null) {
            String[] accountIds = null;
            if (accountId.contains("-")) {
                accountIds = accountId.split("-");
                if (accountIds != null && accountIds.length > 0) {
                    accountId = accountIds[1];
                }
            }
            Account account = accounts.get(accountId);
            String InputAccountId = account.getAccountId();
            if (StringUtils.isNotBlank(InputAccountId)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static JSONArray getFirstOccuringArray(JSONObject obj) {

        if (StringUtils.isNotBlank(obj.optString("dbpErrMsg"))) {
            JSONArray array = new JSONArray();
            array.put(obj);
            return array;
        }

        Iterator<String> keys = obj.keySet().iterator();
        while (keys.hasNext()) {
            try {
                return obj.getJSONArray(keys.next());
            } catch (JSONException e) {
                // do nothing;
            }
        }
        return null;
    }

    /*
     * Input - DataControllerRequest Instance Output - C360 Auth Token String
     */

    public static String SpotlightLogin(DataControllerRequest request) {
        Map<String, Object> headersMap = new HashMap<>();
        String api_Access_Token = CommonUtils.getServerEnvironmentProperty(TemenosConstants.ACCESS_TOKEN, request);
        String ac_app_key = CommonUtils.getServerEnvironmentProperty(TemenosConstants.APP_KEY, request);
        String ac_app_secret_key = CommonUtils.getServerEnvironmentProperty(TemenosConstants.APP_SECRET, request);
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
        /*String AuthToken = SpotlightLogin(request);

        if (StringUtils.isBlank(AuthToken)) {
            alert.prepareError("C360 authentication failed. Aborting get configurations").log();
            return new JSONObject();
        }
        headerMap.put("backendToken", AuthToken);
        headerMap.put(TemenosConstants.PARAM_X_KONY_AUTHORIZATION,
                request.getHeader(TemenosConstants.PARAM_X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");*/
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
    
    /*
     * Store Data in Byte format
     */
    public void saveIntoCache(String key, byte[] value, int cacheTime) {

        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }

        if (resultCache != null && StringUtils.isNotBlank(key) && value!=null) {
            resultCache.insertIntoCache(key, value, cacheTime);
        }

    }
    
    /*
     * Removes the data from cache
     */
    public void removeFromCache(String key) {

        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }

        if (resultCache != null && StringUtils.isNotBlank(key)) {
            resultCache.removeFromCache(key);
        }

    } 
}
