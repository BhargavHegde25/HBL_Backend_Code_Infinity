package com.temenos.infinity.api.chequemanagement.utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.ehcache.ResultCacheImpl;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.infinity.api.chequemanagement.config.ChequeManagementAPIServices;
import com.temenos.infinity.api.chequemanagement.constants.Constants;
import com.temenos.infinity.api.chequemanagement.dto.Account;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;

public class ChequeManagementUtilities {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /**
     * <p>
     * Fetch the backendT24 customerId
     * </p>
     * 
     * @param request
     * @return backendId
     */
    public static String getT24BackendId(DataControllerRequest request) {
        ServicesManager servicesManager;
        String backendIdentifiers = null;
        try {
            servicesManager = request.getServicesManager();
            backendIdentifiers = (String) servicesManager.getIdentityHandler().getUserAttributes()
                    .get("backendIdentifiers");
        } catch (Exception e) {
            return new String();
        }

        String backendUserId = null;
        if (StringUtils.isNotBlank(backendIdentifiers)) {
            backendUserId = getBackendId(backendIdentifiers, "T24");
            return backendUserId;
        } else
            return new String();

    }

    public static String getUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
        // TODO Auto-generated method stub
        try {
            if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
                if (userMap.get(attribute) != null) {
                    String attributeValue = userMap.get(attribute) + "";
                    alert.prepareError("value of " + attribute + "from identity is " + attributeValue).log();
                    return attributeValue;
                }
                alert.prepareError("value of " + attribute + "from identity is null").log();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            alert.prepareError(e.toString()).log();
        }

        return "";
    }

    private static String getBackendId(String backendIdentifiers, String templateName) {
        String BackendId = null;
        BackendId = getCoreId(backendIdentifiers, templateName, "customerId", "1");
        return BackendId;
    }

    private static String getCoreId(String backendIdentifiers, String BackendType, String IdentifierName,
            String SequenceNumber) {
        String BackendId = null;
        if (StringUtils.isNotBlank(backendIdentifiers)) {
            JSONObject backendIdentifiersJSON = new JSONObject(backendIdentifiers);
            if (backendIdentifiersJSON.has(BackendType)) {
                JSONArray templateIdentifiers = backendIdentifiersJSON.getJSONArray(BackendType);

                for (int i = 0; i < templateIdentifiers.length(); i++) {
                    String identifier_name = templateIdentifiers.getJSONObject(i).getString("identifier_name");
                    String sequenceNumber = templateIdentifiers.getJSONObject(i).getString("sequence_number");

                    if (BackendType.equals("T24")) {
                        if (IdentifierName.equals(identifier_name) && SequenceNumber.equals(sequenceNumber)) {
                            BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
                        }
                    } else {
                        if (IdentifierName.equals(identifier_name)) {
                            BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
                        }
                    }
                }
            }
        }
        return BackendId;
    }

    /*
     * Remove the company id prefix id from account Input - GB0010001-78786
     * OutPut - 78786
     */
    public static String RemoveCompanyId(String accountId) {
        String[] accountIds = null;
        if (accountId.contains("-")) {
            accountIds = accountId.split("-");
            if (accountIds != null && accountIds.length > 0) {
                accountId = accountIds[1];
            }
        }
        return accountId;
    }

    /**
     * <p>
     * Fetch the backendT24 customerId from GetUserAttributesDetails
     * </p>
     * 
     * @param authToken
     * @return backendId
     */
    public static String getT24BackendIdUsingAuth(String authToken) {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();

            if (!StringUtils.isBlank(authToken)) {
                diagnostic.prepareDebug("Using Auth Token from Param-Login").log();
                headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, authToken);
            }
            String serviceResponse = Executor.invokeService(
                    ChequeManagementAPIServices.DBPUSERATTRIBUTES_GETUSERATTRIBUTEDETAILS, inputMap, headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null)
                diagnostic.prepareDebug("failed").log();
            else if (serviceResponseJSON.has("BackendId")
                    && StringUtils.isNotEmpty(serviceResponseJSON.getString("BackendId"))) {
                String backendUserId = null;
                if (StringUtils.isNotBlank(serviceResponseJSON.getString("BackendId"))) {
                    backendUserId = getBackendId(serviceResponseJSON.getString("BackendId"), "T24");
                    return backendUserId;
                } else
                    return new String();
            }
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return new String();
        }
        return new String();

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
            cacheKey = cacheKey+"_"+key;
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

    // Check input account is logged in customer's account
    public static boolean checkCustomerAccount(DataControllerRequest request, String accountId) {
        ChequeManagementUtilities chkUtils = new ChequeManagementUtilities();
        HashMap<String, Account> accounts = chkUtils.getAccountsMapFromCache(request);
        if (accounts != null) {
            String[] accountIds = null;
            if (accountId.contains("-")) {
                accountIds = accountId.split("-");
                if (accountIds != null && accountIds.length > 0) {
                    accountId = accountIds[1];
                }
            }
            Account account = accounts.get(accountId);
            if (account != null) {
                if (StringUtils.isNotBlank(account.getAccountId())) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    public static JSONArray getFirstOccuringArray(JSONObject obj) {
		
		if(StringUtils.isNotBlank(obj.optString("dbpErrMsg"))) {
			JSONArray array = new JSONArray();
			array.put(obj);
			return array;
		}

		Iterator<String> keys = obj.keySet().iterator();
		while(keys.hasNext()) {
			try {
				return obj.getJSONArray(keys.next());
			}
			catch(JSONException e) {
				//do nothing;
			}
		}
		return null;
	}
}
