package com.kony.dbputilities.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.Authentication;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.http.HttpConnector;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServiceRequestBuilder;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;
import com.konylabs.middleware.api.processor.HeadersHandler;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.kony.dbputilities.util.TokenUtils;

public final class HelperMethods {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static void setValidationMsg(String message, DataControllerRequest dcRequest, Result result) {
        setValidationErrorMsg(message, result);
    }

    public static void setValidationMsg(String message, Record result) {
        Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR, message,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(validionMsg);
        addStatusCodes(result);
    }

    public static void setValidationMsg(String message, Result result) {
        Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR, message,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(validionMsg);
    }

    public static void addStatusCodes(Result result) {
        Param status = new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, DBPUtilitiesConstants.HTTP_ERROR_CODE,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(status);
        Param opstatus = new Param(DBPConstants.FABRIC_OPSTATUS_KEY, DBPUtilitiesConstants.HTTP_ERROR_CODE,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(opstatus);
    }

    public static void addStatusCodes(Record result) {
        Param status = new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, DBPUtilitiesConstants.HTTP_ERROR_CODE,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(status);
        Param opstatus = new Param(DBPConstants.FABRIC_OPSTATUS_KEY, DBPUtilitiesConstants.HTTP_ERROR_CODE,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(opstatus);
    }

    public static void setValidationErrorMsg(String message, Result result) {
        Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR_MESSAGE, message,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(validionMsg);
        addStatusCodes(result);
    }

    public static void setValidationMsgwithCode(String message, String code, Result result) {
        Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR_MESSAGE, message,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(validionMsg);
        Param status = new Param(DBPUtilitiesConstants.ERROR_CODE, code, DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(status);
    }

    public static void setValidationMsgwithCode(String message, String code, Record record) {
        Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR_MESSAGE, message,
                DBPUtilitiesConstants.STRING_TYPE);
        record.addParam(validionMsg);
        Param status = new Param(DBPUtilitiesConstants.ERROR_CODE, code, DBPUtilitiesConstants.STRING_TYPE);
        record.addParam(status);
    }

    public static void setSuccessMsgwithCode(String message, String code, Record record) {
        Param validionMsg = new Param(DBPUtilitiesConstants.SUCCESS, message, DBPUtilitiesConstants.STRING_TYPE);
        record.addParam(validionMsg);
        Param status = new Param(DBPUtilitiesConstants.ERROR_CODE, code, DBPUtilitiesConstants.STRING_TYPE);
        record.addParam(status);
    }

    public static void setSuccessMsgwithCode(String message, String code, Result result) {
        Param validionMsg = new Param(DBPUtilitiesConstants.SUCCESS, message, DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(validionMsg);
        Param status = new Param(DBPUtilitiesConstants.ERROR_CODE, code, DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(status);
    }

    public static void setSuccessMsg(String message, Record record) {
        Param validionMsg = new Param(DBPUtilitiesConstants.SUCCESS, message, DBPUtilitiesConstants.STRING_TYPE);
        record.addParam(validionMsg);
    }

    public static void setSuccessMsg(String message, Result result) {
        Param validionMsg = new Param(DBPUtilitiesConstants.SUCCESS, message, DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(validionMsg);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getInputParamObjectMap(Object[] inputArray) {
        return (HashMap<String, Object>) inputArray[1];
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getInputParamMap(Object[] inputArray) {
        return (HashMap<String, String>) inputArray[1];
    }

    public static Map<String, Object> convertToObjectMap(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        Map<String, Object> retMap = new HashMap<>();
        for (String key : map.keySet()) {
            retMap.put(key, map.get(key));
        }
        return retMap;
    }

    public static Record getRecord(Result result) {
        Record record = new Record();
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            record = ds.getRecord(0);
        }
        return record;
    }

    public static String getFieldValue(Result result, String fieldName) {
        String id = "";
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            id = getParamValue(ds.getRecord(0).getParam(fieldName));
        }
        return id;
    }

    /**
     * Gets field from Result with provided Dataset Name
     */

    public static String getFieldValue(Result result, String datasetName, String fieldName) {
        String id = "";
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getDatasetById(datasetName);
            id = getParamValue(ds.getRecord(0).getParam(fieldName));
        }
        return id;
    }

    public static boolean hasField(Result result, String fieldName) {
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            return (ds.getRecord(0).getParam(fieldName) != null) ? true : false;
        }
        return false;
    }

    public static boolean hasParam(Result result, String fieldName) {
        if (HelperMethods.hasParams(result)) {
            Param param = result.getParamByName(fieldName);
            return (param != null) ? true : false;
        }
        return false;
    }

    public static boolean hasField(Record record, String fieldName) {
        return (record.getParam(fieldName) != null) ? true : false;
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

    public static boolean hasParams(Result result) {
        if (hasError(result) || null == result.getAllParams() || result.getAllParams().isEmpty()) {
            return false;
        }
        return result.getAllParams().size() > 0;
    }

    public static boolean hasRecords(Dataset result) {
        if (null == result || null == result.getAllRecords() || result.getAllRecords().size() <= 0) {
            return false;
        }
        return true;
    }

    static String[] europeCountry = new String[] { "Albania", "Andorra", "Armenia", "Austria", "Azerbaijan", "Belarus",
            "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland", "France",
            "Georgia", "Germany", "Greece", "Hungary", "Iceland", "Ireland", "Italy", "Kazakhstan", "Latvia",
            "Liechtenstein", "Lithuania", "Luxembourg", "Malta", "Moldova", "Monaco", "Netherlands", "Romania",
            "Russia", "San Marino", "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey", "Ukraine",
            "United Kingdom" };

    public static String getCurrencyCode(String country) {
        String currencyCode = "";

        if (country == null || "".equals(country) || "United States".equalsIgnoreCase(country)
                || "United States of America".equalsIgnoreCase(country)) {
            currencyCode = "USD";
        } else if ("India".equalsIgnoreCase(country)) {
            currencyCode = "INR";
        } else if ("United Kingdom".equalsIgnoreCase(country)) {
            currencyCode = "GBP";
        } else if ("Brazil".equalsIgnoreCase(country)) {
            currencyCode = "REL";
        } else if (Arrays.asList(europeCountry).contains(country)) {
            currencyCode = "EUR";
        } else if ("Australia".equalsIgnoreCase(country)) {
            currencyCode = "AUD";
        } else {
            currencyCode = "USD";
        }

        return currencyCode;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addInputParam(Map inputParams, String keyName, String fieldName) {
        if (StringUtils.isNotBlank((String) inputParams.get(keyName))) {
            inputParams.put(fieldName, inputParams.get(keyName));
        }
    }

    public static String getUserIdFromSession(DataControllerRequest dcRequest) {
        return getCustomerIdFromIdentityService(dcRequest);
    }

    public static String getAPIUserIdFromSession(DataControllerRequest dcRequest) {
        return getCustomerIdFromAPIIdentityService(dcRequest);
    }

    public static String getAPIUserIdFromSession(FabricRequestManager requestManager) {
        return getCustomerIdFromAPIIdentityService(requestManager);
    }

    public static String getCustomerIdFromSession(DataControllerRequest dcRequest) {
        return getCustomerIdFromIdentityService(dcRequest);
    }

    public static String getUserIdFromNUOSession(DataControllerRequest dcRequest) {
        Result userAttributesResponse = new Result();
        String customerId = "";
        try {
            if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
                customerId = dcRequest.getServicesManager().getIdentityHandler().getUserId();
            }
        } catch (AppRegistryException e1) {

        }

        try {
            userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest),
                    URLConstants.NUO_USER_ID_GET_IDENTITY);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (userAttributesResponse.getNameOfAllParams().contains("customer_id")) {
            customerId = userAttributesResponse.getParamValueByName("customer_id");
        }
        return customerId;
    }

    public static Map<String, String> getHeaders(DataControllerRequest dcRequest) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return headerMap;
    }

    private static String getCustomerIdFromAPIIdentityService(DataControllerRequest dcRequest) {
        Result userAttributesResponse = new Result();

        String customerId = "";

        try {
            if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
                return dcRequest.getServicesManager().getIdentityHandler().getUserId();
            }
        } catch (AppRegistryException e) {
            alert.prepareError(e.getMessage()).log();
        }

        try {
            userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest),
                    URLConstants.USER_ID_GET_API_IDNETITY);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (userAttributesResponse.getNameOfAllParams().contains("customer_id")) {
            customerId = userAttributesResponse.getParamValueByName("customer_id");
        }
        return customerId;
    }

    private static Map<String, String> getCustomerFromAPIIdentityService(DataControllerRequest dcRequest) {
        Result userAttributesResponse = new Result();

        Map<String, String> user = new HashMap<>();

        try {
            if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> hashMap = dcRequest.getServicesManager().getIdentityHandler().getUserAttributes();

                for (String key : hashMap.keySet()) {
                    user.put(key, hashMap.get(key) + "");
                }

                user.put("user_id", user.get("customer_id"));
                user.put("customer_id", user.get("customer_id"));
                user.put("Customer_id", user.get("customer_id"));
                user.put("Customer_Id", user.get("customer_id"));
                user.put("customerId", user.get("customer_id"));
                user.put("userName", user.get("UserName"));
                user.put("UserName", user.get("UserName"));
                user.put("username", user.get("UserName"));
                user.put("countryCode", user.get("countryCode"));
                user.put("customerType", user.get("CustomerType_id"));
                user.put("CustomerType_id", user.get("CustomerType_id"));
                user.put("isC360Admin", user.get("isSuperAdmin"));
            }
        } catch (MiddlewareException e1) {
            e1.getMessage();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (user.size() == 0) {
            try {
                userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest),
                        URLConstants.USER_ID_GET_API_IDNETITY);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (userAttributesResponse != null) {
                for (Param param : userAttributesResponse.getAllParams()) {
                    user.put(param.getName(), param.getValue());
                }
            }
        }
        return user;
    }

    private static String getCustomerIdFromAPIIdentityService(FabricRequestManager requestManager) {
        Result userAttributesResponse = new Result();

        String customerId = "";

        try {
            if (requestManager.getServicesManager() != null
                    && requestManager.getServicesManager().getIdentityHandler() != null) {
                return requestManager.getServicesManager().getIdentityHandler().getUserId();
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        try {
            userAttributesResponse = callApi(requestManager, null, getHeaders(requestManager),
                    URLConstants.USER_ID_GET_API_IDNETITY);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (userAttributesResponse.getNameOfAllParams().contains("customer_id")) {
            customerId = userAttributesResponse.getParamValueByName("customer_id");
        }
        return customerId;
    }
    
    public static String getSessionTokenFromIdentityService(DataControllerRequest dcRequest) {

        String session_token = "";

        try {
            if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
                return dcRequest.getServicesManager().getIdentityHandler()
                		.getSecurityAttributes().get("session_token").toString();
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        try {
        	Result userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest),
            		URLConstants.USER_ATTRIBUTES_GET_IDENTITY);
            if (userAttributesResponse.getNameOfAllParams().contains("session_token")) {
            	session_token = userAttributesResponse.getParamValueByName("session_token");
            }
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        
        return session_token;
    }
    
    
    public static Map<String,Object> getIdentityServiceInfo(DataControllerRequest dcRequest) {

        String session_token = "";
        IdentityHandler identityHandler = null;
        try {
			if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
				identityHandler = dcRequest.getServicesManager().getIdentityHandler();
				if (identityHandler != null) {
					session_token = identityHandler.getSecurityAttributes().get("session_token").toString();
				}
			}
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

		try {
			if (StringUtils.isBlank(session_token)) {
				Result userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest),
						URLConstants.USER_ATTRIBUTES_GET_IDENTITY);
				if (userAttributesResponse.getNameOfAllParams().contains("session_token")) {
					session_token = userAttributesResponse.getParamValueByName("session_token");
				}
			}
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}
        
        Map<String, Object > identityInfo = new HashMap<>();
		try {
			identityInfo.put("session_token", session_token);
			if (identityHandler != null) {
				identityInfo.putAll(identityHandler.getUserAttributes());
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error in fetchning Identity user attributes",e).log();
		}
		return identityInfo;
    }

    private static String getCustomerIdFromIdentityService(DataControllerRequest dcRequest) {
        Result userAttributesResponse = new Result();

        String customerId = "";

        try {
            if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
                return dcRequest.getServicesManager().getIdentityHandler().getUserId();
            }
        } catch (AppRegistryException e) {
            alert.prepareError(e.getMessage()).log();
        }

        try {
            userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest), URLConstants.USER_ID_GET_IDNETITY);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (userAttributesResponse.getNameOfAllParams().contains("customer_id")) {
            customerId = userAttributesResponse.getParamValueByName("customer_id");
        }
        return customerId;
    }

    private static String getCustomerIdFromIdentityService(FabricRequestManager requestManager) {
        Result userAttributesResponse = new Result();

        String customerId = "";

        if (requestManager.getServicesManager() != null
                && requestManager.getServicesManager().getIdentityHandler() != null) {
            return requestManager.getServicesManager().getIdentityHandler().getUserId();
        }

        try {
            userAttributesResponse = ServiceCallHelper.invokeServiceAndGetResult(null, getHeaders(requestManager),
                    URLConstants.USER_ID_GET_IDNETITY,
                    requestManager.getQueryParamsHandler().getParameter("Auth_Token"));
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        // userAttributesResponse = callApi(requestManager, null,
        // getHeaders(requestManager),
        // URLConstants.USER_ID_GET_IDNETITY);

        if (userAttributesResponse.getNameOfAllParams().contains("customer_id")) {
            customerId = userAttributesResponse.getParamValueByName("customer_id");
        }
        return customerId;
    }

    public static String getCustomerIdFromKnownUserToken(DataControllerRequest dcRequest) {
        Result userAttributesResponse = new Result();
        String customerId = "";

        try {
            userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest),
                    URLConstants.GET_CUSTOMERID_FROM_KNOWN_USER_TOKEN);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (userAttributesResponse.getNameOfAllParams().contains("customer_id")) {
            customerId = userAttributesResponse.getParamValueByName("customer_id");
        }
        alert.prepareError("Csutomer Id from knownusertoken from servicemanager " + customerId).log();

        return customerId;
    }

    public static String getCustomerIdFromKnownUserToken(FabricRequestManager requestManager) {
        Result userAttributesResponse = new Result();
        String customerId = "";

        userAttributesResponse = callApi(requestManager, null, getHeaders(requestManager),
                URLConstants.GET_CUSTOMERID_FROM_KNOWN_USER_TOKEN);

        if (userAttributesResponse.getNameOfAllParams().contains("customer_id")) {
            customerId = userAttributesResponse.getParamValueByName("customer_id");
        }

        return customerId;
    }

    public static Map<String, String> getUserFromIdentityService(DataControllerRequest dcRequest) {
        Map<String, String> user = new HashMap<>();
        try {
            if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> hashMap = CustomerSessionsUtil.getLoggedInUserAttributesMap(dcRequest);

                for (String key : hashMap.keySet()) {
                    user.put(key, hashMap.get(key) + "");
                }

                user.put("user_id", user.get("customer_id"));
                user.put("customer_id", user.get("customer_id"));
                user.put("Customer_id", user.get("customer_id"));
                user.put("Customer_Id", user.get("customer_id"));
                user.put("customerId", user.get("customer_id"));
                user.put("userName", user.get("UserName"));
                user.put("UserName", user.get("UserName"));
                user.put("username", user.get("UserName"));
                user.put("countryCode", user.get("countryCode"));
                user.put("customerType", user.get("CustomerType_id"));
                user.put("CustomerType_id", user.get("CustomerType_id"));
                user.put("isC360Admin", user.get("isSuperAdmin"));
                user.put("isSchedulingEngine", user.get("isSchedulingEngine"));
                user.put("Organization_Id", user.get("Organization_Id"));
                user.put("isCombinedUser", user.get("isCombinedUser"));
            }
        } catch (MiddlewareException e1) {
            e1.getMessage();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (user.size() == 0) {

            Result userAttributesResponse = new Result();
            diagnostic.prepareDebug("getUserFromIdentityService: UserAttributes by Identity").log();
            try {
                userAttributesResponse = callApi(dcRequest, null, getHeaders(dcRequest),
                        URLConstants.USER_ATTRIBUTES_GET_IDENTITY);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (userAttributesResponse != null) {
                for (Param param : userAttributesResponse.getAllParams()) {
                    user.put(param.getName(), param.getValue());
                }
            }
        }
        diagnostic.prepareDebug("getUserFromIdentityService: UserAttributes by Identity" + user).log();
        return user;
    }

    public static Map<String, String> getCustomerFromIdentityService(DataControllerRequest dcRequest) {
        return getUserFromIdentityService(dcRequest);
    }

    public static Map<String, String> getCustomerFromAPIDBPIdentityService(DataControllerRequest dcRequest) {
        return getCustomerFromAPIIdentityService(dcRequest);
    }

    public static Map<String, String> getCustomerFromIdentityService(FabricRequestManager requestManager) {
        Map<String, String> user = new HashMap<>();
        try {
            if (requestManager.getServicesManager() != null
                    && requestManager.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> hashMap = CustomerSessionsUtil.getLoggedInUserAttributesMap(requestManager);

                for (String key : hashMap.keySet()) {
                    user.put(key, hashMap.get(key) + "");
                }

                user.put("user_id", user.get("customer_id"));
                user.put("customer_id", user.get("customer_id"));
                user.put("Customer_id", user.get("customer_id"));
                user.put("Customer_Id", user.get("customer_id"));
                user.put("customerId", user.get("customer_id"));
                user.put("userName", user.get("UserName"));
                user.put("UserName", user.get("UserName"));
                user.put("username", user.get("UserName"));
                user.put("countryCode", user.get("countryCode"));
                user.put("customerType", user.get("CustomerType_id"));
                user.put("CustomerType_id", user.get("CustomerType_id"));
                user.put("isC360Admin", user.get("isSuperAdmin"));
                user.put("isSchedulingEngine", user.get("isSchedulingEngine"));
                user.put("Organization_Id", user.get("Organization_Id"));
                user.put("organizationType", user.get("organizationType"));
                user.put("isCombinedUser", user.get("isCombinedUser"));
            }
        } catch (MiddlewareException e1) {
            e1.getMessage();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (user.size() == 0) {
            Result userAttributesResponse = new Result();

            try {
                userAttributesResponse = callApi(requestManager, null, getHeaders(requestManager),
                        URLConstants.USER_ATTRIBUTES_GET_IDENTITY);
            } catch (Exception e1) {

                e1.getMessage();
            }

            if (userAttributesResponse != null) {
                for (Param param : userAttributesResponse.getAllParams()) {
                    user.put(param.getName(), param.getValue());
                }
            }
        }

        return user;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Result callGetApi(DataControllerRequest dcRequest, String filterQuery, Map<String, String> header,
            String url) throws HttpCallException {
        Map inputParams = new HashMap();

        inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
        return callApi(dcRequest, inputParams, header, url);
    }

    @SuppressWarnings("unchecked")
    public static JsonObject callApiJson(DataControllerRequest dcRequest, @SuppressWarnings("rawtypes") Map inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {
        return ServiceCallHelper.invokeServiceAndGetJson(dcRequest, inputParams, convertMap(headerParams), url);
    }

    public static JsonObject callApiJson(FabricRequestManager requestManager,
            @SuppressWarnings("rawtypes") Map inputParams, Map<String, String> headerParams, String url)
            throws HttpCallException {
        return ServiceCallHelper.invokeServiceAndGetJson(requestManager, inputParams, headerParams, url);
    }

    public static Result callExternalApi(@SuppressWarnings("rawtypes") Map inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {
        HttpConnector httpConn = new HttpConnector();

        JsonObject response = httpConn.invokeHttpPost(url, inputParams, headerParams);
        Result result = ConvertJsonToResult.convert(response);
        return (null == result) ? new Result() : result;
    }

    @SuppressWarnings("unchecked")
    public static Result callApi(DataControllerRequest dcRequest, @SuppressWarnings("rawtypes") Map inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {
        return ServiceCallHelper.invokeServiceAndGetResult(dcRequest, inputParams, headerParams, url);
    }

    @SuppressWarnings("unchecked")
    public static String callApiAndGetString(DataControllerRequest dcRequest,
            @SuppressWarnings("rawtypes") Map inputParams, Map<String, String> headerParams, String url)
            throws HttpCallException {
        return ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputParams, headerParams, url);
    }

    private static Map<String, Object> convertMap(Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Entry<String, String> entry : map.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    @SuppressWarnings("rawtypes")
    public static void removeNullValues(Map map) {
        Iterator itr = map.keySet().iterator();
        while (itr.hasNext()) {
            Object key = itr.next();
            if (StringUtils.isBlank((String) map.get(key))) {
                itr.remove();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static void removeOnlyNullValues(Map map) {
        Iterator itr = map.keySet().iterator();
        while (itr.hasNext()) {
            Object key = itr.next();
            if (null == (String) map.get(key)) {
                itr.remove();
            }
        }
    }

    public static String getDateType(String messageType) {
        String dateType = DBPUtilitiesConstants.M_RECEIVED_DATE;
        if ("Inbox".equals(messageType)) {
            dateType = DBPUtilitiesConstants.M_RECEIVED_DATE;
        } else if (("Drafts").equals(messageType)) {
            dateType = DBPUtilitiesConstants.M_CREATED_DATE;
        } else if (("Sent").equals(messageType)) {
            dateType = DBPUtilitiesConstants.M_SENT_DATE;
        } else if (("Deleted").equals(messageType)) {
            dateType = DBPUtilitiesConstants.M_SOFT_DEL_DATE;
        }
        return dateType;
    }

    public static boolean hasError(Result result) {
        boolean status = false;
        if (null == result || null != result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR)
                || null != result.getParamByName(DBPConstants.DBP_ERROR_CODE_KEY)) {
            status = true;
        }
        return status;
    }

    public static boolean hasError(JsonObject result) {
        boolean status = false;
        if (null == result || result.has(DBPUtilitiesConstants.VALIDATION_ERROR)) {
            status = true;
        }

        return status;
    }

    public static boolean hasDBPErrorMSG(Result result) {
        boolean status = false;
        if (null == result || result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
            status = true;
        }
        return status;
    }

    public static String getError(Result result) {
        if (null != result && null != result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR)) {
            return result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR).getValue();
        }
        return null;
    }

    public static Long diff(String lRowNum, String fRowNum) {
        long first = Long.parseLong(fRowNum);
        long last = Long.parseLong(lRowNum);
        return last - first;
    }

    public static String getCurrentTimeStamp() {
        return getFormattedTimeStamp(new Date(), null);
    }

    public static String getCurrentDate() {
        return getFormattedTimeStamp(new Date(), "yyyy-MM-dd");
    }

    public static String getCurrentTime() {
        return getFormattedTimeStamp(new Date(), "HH:mm:ss");
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
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("MM/dd/yyyy"),
            new SimpleDateFormat("dd MMM yy HH:mm") };

    public static Date getFormattedTimeStamp(String dt) {

        for (int i = 0; i < expectedFormats.length; i++) {
            try {
                return expectedFormats[i].parse(dt);
            } catch (Exception e) {
            }
        }
        return new Date();
    }

    private static DateTimeFormatter[] formats = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy")
    };

    public static String getFormattedLocaleDate(String dt, String format) {
        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd";
        }

        for (int i = 0; i < formats.length; i++) {
            try {
                LocalDate date = LocalDate.parse(dt, formats[i]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return formatter.format(date);
            } catch (Exception e) {
            }
        }

        return DateTimeFormatter.ofPattern(format).format(LocalDate.now());
    }

    public static boolean isValidDateFormat(String dt) {
        for (int i = 0; i < formats.length; i++) {
            try {
                LocalDate.parse(dt, formats[i]);
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static boolean isStringADate(String dt) {
        for (int i = 0; i < expectedFormats.length; i++) {
            expectedFormats[i].setLenient(false);
            try {
                expectedFormats[i].parse(dt);
                return true;
            } catch (ParseException e) {
                diagnostic.prepareDebug(e.getMessage()).log();
            }

        }
        return false;
    }

    public static boolean isStringADouble(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            diagnostic.prepareDebug(e.getMessage()).log();
        }

        return false;
    }

    public static Date getFormattedTimeStamp(String dt, String format) {
        SimpleDateFormat expectedFormats = new SimpleDateFormat(format);
        try {
            return expectedFormats.parse(dt);
        } catch (ParseException e) {
        }
        return new Date();
    }

    public static void updateDateFormat(Record record, String fieldName) throws ParseException {
        String date = getFieldValue(record, fieldName);
        if (StringUtils.isNotBlank(date)) {
            record.addParam(
                    new Param(fieldName, convertDateFormat(date, "yyyy-MM-dd'T'HH:mm:ss'Z'"), MWConstants.STRING));
        }
    }

    /**
     * Increment date in minutes.
     *
     * @param minutes
     *            the minutes
     * @return the date
     */
    public static Date incrementDateInMinutes(long minutes) {
        long ONE_MINUTE_IN_MILLIS = 60000;
        long t = new Date().getTime();
        Date afterAddingMins = new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static String convertDateFormat(String dob, String to) throws ParseException {
        if (dob == null || "".equals(dob)) {
            return null;
        }
        Date date = getFormattedTimeStamp(dob);
        return HelperMethods.getFormattedTimeStamp(date, to);
    }

    public static String changeDateFormat(String dob, String to) throws ParseException {
        if (dob == null || "".equals(dob)) {
            return null;
        }
        String formattedDate = "";
        if (dob.matches("([0-9]{2}) ([A-Z]{3}) ([0-9]{4})")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            Date date = formatter.parse(dob);
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            formattedDate = formatter.format(date);
        } else
            formattedDate = convertDateFormat(dob, to);
        return formattedDate;

    }

    public static Param convertedDateParam(String paramName, String datetime) throws ParseException {
        return new Param(paramName, HelperMethods.convertDateFormat(datetime, "yyyy-MM-dd'T'HH:mm:ss'Z'"),
                DBPConstants.FABRIC_STRING_CONSTANT_KEY);
    }

    public static String getNewId() {
        UUID uuidValue = UUID.randomUUID();
        return uuidValue.toString();
    }

    public static Dataset getDataSet(Result result) {
        Dataset ds = new Dataset();
        if (HelperMethods.hasRecords(result)) {
            ds = result.getAllDatasets().get(0);
        }
        return ds;
    }

    public static String toQFXDateTime(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String reportDate = df.format(date);

        return reportDate;
    }

    public static Date getUTCdate() throws ParseException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateFormatGmt.format(new Date()));
    }

    public static String toOFXDateTime(String dbxDate) {
        return dbxDate.replaceAll("[^0-9]+", "");
    }

    static Map<String, String> CommunicationTypesMap = null;

    public static Map<String, String> getCommunicationTypes() {
        if (CommunicationTypesMap == null) {
            CommunicationTypesMap = new HashMap<>();
            CommunicationTypesMap.put("Email", "COMM_TYPE_EMAIL");
            CommunicationTypesMap.put("Phone", "COMM_TYPE_PHONE");
        }

        return CommunicationTypesMap;
    }

    static Map<String, String> communicationMap = null;

    public static Map<String, String> getCommunicationMap() {
        if (communicationMap == null) {
            communicationMap = new HashMap<>();
            communicationMap.put("COMM_TYPE_EMAIL", "Email");
            communicationMap.put("COMM_TYPE_PHONE", "Phone");
        }

        return communicationMap;
    }

    static Map<String, String> customerStatusMap = null;

    public static Map<String, String> getCustomerStatus() {

        if (customerStatusMap == null) {
            customerStatusMap = new HashMap<>();

            customerStatusMap.put("ACTIVE", "SID_CUS_ACTIVE");
            customerStatusMap.put("LOCKED", "SID_CUS_LOCKED");
            customerStatusMap.put("PENDING VERIFICATION", "SID_CUS_PENDING_VERIFICATION");
            customerStatusMap.put("NEW", "SID_CUS_NEW");
            customerStatusMap.put("SUSPENDED", "SID_CUS_SUSPENDED");
            customerStatusMap.put("INACTIVE", "SID_CUS_INACTIVE");
        }
        return customerStatusMap;
    }

    static Map<String, String> statusMap = null;

    public static Map<String, String> getStatusMap() {
        if (statusMap == null) {
            statusMap = new HashMap<>();

            statusMap.put("SID_CUS_ACTIVE", "ACTIVE");
            statusMap.put("SID_CUS_LOCKED", "LOCKED");
            statusMap.put("SID_CUS_PENDING_VERIFICATION", "PENDING VERIFICATION");
            statusMap.put("SID_CUS_NEW", "NEW");
            statusMap.put("SID_CUS_SUSPENDED", "SUSPENDED");
            statusMap.put("SID_CUS_INACTIVE", "INACTIVE");

        }

        return statusMap;
    }

    static Map<String, String> customerTypes = null;

    public static Map<String, String> getCustomerTypes() {
        if (customerTypes == null) {
            customerTypes = new HashMap<>();

            customerTypes.put("Micro Business", "TYPE_ID_MICRO_BUSINESS");
            customerTypes.put("Micro Business Banking", "TYPE_ID_MICRO_BUSINESS");
            customerTypes.put("Prospect", "TYPE_ID_PROSPECT");
            customerTypes.put("Customer", "TYPE_ID_RETAIL");
            customerTypes.put("Retail Banking", "TYPE_ID_RETAIL");
            customerTypes.put("Small Business", "TYPE_ID_SMALL_BUSINESS");
            customerTypes.put("Small Business Banking", "TYPE_ID_SMALL_BUSINESS");
            customerTypes.put("Retail Customer", "TYPE_ID_RETAIL");
            customerTypes.put("Retail", "TYPE_ID_RETAIL");
            customerTypes.put("Business", "TYPE_ID_BUSINESS");
        }

        return customerTypes;
    }

    static Set<String> dbxdb_Types = null;

    public static Set<String> getBusinessUserTypes() {
        if (dbxdb_Types == null) {
            dbxdb_Types = new HashSet<>();

            dbxdb_Types.add("TYPE_ID_MICRO_BUSINESS");
            dbxdb_Types.add("TYPE_ID_SMALL_BUSINESS");
            dbxdb_Types.add("TYPE_ID_BUSINESS");
        }

        return dbxdb_Types;
    }

    static Map<String, String> customerTypesMap = null;

    public static Map<String, String> getCustomerTypesMap() {

        if (customerTypesMap == null) {
            customerTypesMap = new HashMap<>();

            customerTypesMap.put("TYPE_ID_MICRO_BUSINESS", "Micro Business");
            customerTypesMap.put("TYPE_ID_PROSPECT", "Prospect");
            customerTypesMap.put("TYPE_ID_RETAIL", "Retail Customer");
            customerTypesMap.put("TYPE_ID_SMALL_BUSINESS", "Small Business");
            customerTypesMap.put("TYPE_ID_BUSINESS", "Business");
        }
        return customerTypesMap;
    }

    public static boolean isBusinessUserType(String CustomerType) {
        if (CustomerType != null && (CustomerType.equals("TYPE_ID_BUSINESS"))) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean isProspectUserType(String CustomerType) {
        if (CustomerType != null && (CustomerType.equals("TYPE_ID_PROSPECT"))) {
            return true;
        } else {
            return false;
        }
    }

    public enum CREDENTIAL_TYPE {
        ACTIVATION, RESETPASSWORD, UNLOCK
    }

    public enum FEATUREACTION_TYPE {
        NON_MONETARY, MONETARY
    }

    static Map<String, String> businessTypes = null;

    public static Map<String, String> getBusinessTypes() {
        if (businessTypes == null) {
            businessTypes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            businessTypes.put("Sole Proprietor", "BUSINESS_TYPE_1");
            businessTypes.put("Partnership", "BUSINESS_TYPE_2");
            businessTypes.put("Unincorporated Association", "BUSINESS_TYPE_3");
            businessTypes.put("Trust", "BUSINESS_TYPE_4");
            businessTypes.put("Corporation", "BUSINESS_TYPE_5");
            businessTypes.put("Limited Liability Corporation (LLC)", "BUSINESS_TYPE_6");
        }

        return businessTypes;
    }

    public static long getNumericId() {
        SecureRandom rnd = new SecureRandom();
        long generatedValue;
        generatedValue = 1000000000L + rnd.nextInt(900000000);
        return generatedValue;
    }

    public static long getTaxid() {
        SecureRandom rnd = new SecureRandom();
        long generatedValue;
        generatedValue = 100000000L + rnd.nextInt(90000000);
        return generatedValue;
    }

    public static List<HashMap<String, String>> getAllRecordsMap(String string) {

        List<HashMap<String, String>> list = new ArrayList<>();
        if (StringUtils.isNotBlank(string)) {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonTree = jsonParser.parse(string);
            JsonArray jsonArray = null;
            if (jsonTree.isJsonArray()) {
                jsonArray = jsonTree.getAsJsonArray();
            }

            if (jsonArray != null) {

                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                    map = new HashMap<>();
                    for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        if (!entry.getValue().isJsonNull() && entry.getValue().isJsonPrimitive()) {
                            map.put(entry.getKey(), entry.getValue().getAsString());
                        } else if (!entry.getValue().isJsonNull() && entry.getValue().isJsonArray()) {
                            map.put(entry.getKey(), entry.getValue().getAsJsonArray().toString());
                        } else if (!entry.getValue().isJsonNull() && entry.getValue().isJsonObject()) {
                            map.put(entry.getKey(), entry.getValue().getAsJsonObject().toString());
                        }
                    }

                    list.add(map);
                }
            }
        }
        return list;
    }

    public static HashMap<String, String> getRecordMap(String string) {

        HashMap<String, String> map = new HashMap<>();
        map = new HashMap<>();
        if (StringUtils.isNotBlank(string)) {
            try {
                JsonObject jsonObject = null;
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonTree = jsonParser.parse(string);
                jsonObject = jsonTree.getAsJsonObject();

                for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    map.put(entry.getKey(), entry.getValue().getAsString());
                }
            } catch (Exception e) {
                alert.prepareError("Exception while converting Json to Map", e).log();
            }
        }
        return map;
    }

    public static Result getUserRecordById(String id, DataControllerRequest dcRequest) throws HttpCallException {
        Result result = new Result();

        if (StringUtils.isNotBlank(id)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_GET);
        }
        return result;
    }

    public static String getOrganizationIDfromLoggedInUser(DataControllerRequest dcRequest) throws HttpCallException {

        Map<String, String> userDetails = getUserFromIdentityService(dcRequest);

        if (userDetails.containsKey("Organization_Id"))
            return userDetails.get("Organization_Id");
        else {
            return getOrganizationIDForUser(userDetails.get("customer_id"), dcRequest);
        }
    }

    public static String getOrganizationIDForUser(String user_id, DataControllerRequest dcRequest)
            throws HttpCallException {
        if (StringUtils.isNotBlank(user_id)) {
            Result result = getUserRecordById(user_id, dcRequest);
            return HelperMethods.getFieldValue(result, "Organization_Id");
        }
        return "";
    }

    public static Map<String, String> getCommunicationInfo(String customer_id, DataControllerRequest dcRequest)
            throws HttpCallException {
        HashMap<String, String> Map = new HashMap<>();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customer_id;

        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_COMMUNICATION_GET);
        Map<String, String> map = getCommunicationMap();
        if (HelperMethods.hasRecords(result)) {
            for (Record record : result.getAllDatasets().get(0).getAllRecords()) {
                String communication_type = map.get(HelperMethods.getFieldValue(record, "Type_id"));
                Map.put(communication_type, HelperMethods.getFieldValue(record, "Value"));
            }
        }
        return Map;
    }

    public static Result getUserEmails(String customer_id, DataControllerRequest dcRequest) throws HttpCallException {
        new HashMap<>();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customer_id + DBPUtilitiesConstants.AND
                + "Type_id" + DBPUtilitiesConstants.EQUAL + "COMM_TYPE_EMAIL";

        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        return result;
    }

    public static Result getUserRecordByName(String UserName, DataControllerRequest dcRequest) throws Exception {

        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + "'" + UserName + "'";

        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_GET);
    }

    public static Result getActivationRecord(String UserName, DataControllerRequest dcRequest)
            throws HttpCallException {

        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + UserName;

        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CREDENTIAL_CHECKER_GET);
    }

    public static Result getActivationRecordByActivationId(String id, DataControllerRequest dcRequest)
            throws HttpCallException {

        String filter = "id" + DBPUtilitiesConstants.EQUAL + id;

        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CREDENTIAL_CHECKER_GET);
    }

    /*
     * Use this to validate the date provided is in within range when compared to now. Returns true if Time Now is
     * within range. Input date Format: yyyy-MM-dd hh:mm:ss // Ideal for timestamps read from DB.
     * 
     * Provide range in Hours.
     */
    public static boolean isDateInRange(String firstDate, int rangeInMinutes) {
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date creationDate;
        try {
            creationDate = readDate.parse(firstDate);
        } catch (ParseException e) {
            return false;
        }

        Date now = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(creationDate);
        c.add(Calendar.MINUTE, rangeInMinutes);

        Date ExpiryDate = c.getTime();

        return now.before(ExpiryDate);
    }

    static HashMap<String, String> accountsNames = null;

    public static HashMap<String, String> getAccountsNames() {

        if (accountsNames == null) {
            accountsNames = new HashMap<>();
            accountsNames.put("1", "Checking");
            accountsNames.put("2", "Savings");
            accountsNames.put("3", "CreditCard");
            accountsNames.put("4", "Deposit");
            accountsNames.put("5", "Mortgage");
            accountsNames.put("6", "Loan");
            accountsNames.put("7", "Current");
            accountsNames.put("8", "Investment");
			accountsNames.put("9", "Mortgage Facility");
        }

        return accountsNames;
    }

    static HashMap<String, String> accountsTypes = null;

    public static HashMap<String, String> getAccountsTypes() {

        if (accountsTypes == null) {
            accountsTypes = new HashMap<>();
            accountsTypes.put("Checking", "1");
            accountsTypes.put("Savings", "2");
            accountsTypes.put("CreditCard", "3");
            accountsTypes.put("Deposit", "4");
            accountsTypes.put("Mortgage", "5");
            accountsTypes.put("Loan", "6");
            accountsTypes.put("Current", "7");
            accountsTypes.put("Investment", "8");
            accountsTypes.put("mortgageFacility", "9");
            accountsTypes.put("Registered", "10");  
        }

        return accountsTypes;
    }

    static Set<String> limits = null;

    public static Set<String> getCustomerLimitsTypes() {

        if (limits == null) {
            limits = new HashSet<String>();
            limits.add(DBPUtilitiesConstants.PRE_APPROVED_DAILY_LIMIT);
            limits.add(DBPUtilitiesConstants.PRE_APPROVED_TRANSACTION_LIMIT);
            limits.add(DBPUtilitiesConstants.PRE_APPROVED_WEEKLY_LIMIT);
            limits.add(DBPUtilitiesConstants.AUTO_DENIED_DAILY_LIMIT);
            limits.add(DBPUtilitiesConstants.AUTO_DENIED_TRANSACTION_LIMIT);
            limits.add(DBPUtilitiesConstants.AUTO_DENIED_WEEKLY_LIMIT);
        }

        return limits;

    }

    static HashMap<String, String> addressTypesMap = null;

    public static HashMap<String, String> getAddressTypesMap() {
        if (addressTypesMap == null) {
            addressTypesMap = new HashMap<>();
            addressTypesMap.put("ADR_TYPE_CURRENT", "Current");
            addressTypesMap.put("ADR_TYPE_HOME", "Home");
            addressTypesMap.put("ADR_TYPE_MAILING", "Mailing");
            addressTypesMap.put("ADR_TYPE_OTHER", "Other");
            addressTypesMap.put("ADR_TYPE_PREVIOUS", "Previous");
            addressTypesMap.put("ADR_TYPE_WORK", "Office");
            addressTypesMap.put("Current", "7");
        }

        return addressTypesMap;
    }

    static HashMap<String, String> addressTypes = null;

    public static HashMap<String, String> getAddressTypes() {
        if (addressTypes == null) {
            addressTypes = new HashMap<>();
            addressTypes.put("Current", "ADR_TYPE_CURRENT");
            addressTypes.put("Home", "ADR_TYPE_HOME");
            addressTypes.put("Mailing", "ADR_TYPE_MAILING");
            addressTypes.put("Other", "ADR_TYPE_OTHER");
            addressTypes.put("Previous", "ADR_TYPE_PREVIOUS");
            addressTypes.put("Office", "ADR_TYPE_WORK");
        }

        return addressTypes;
    }

    public static String invokeService(String serviceId, String operationId, Map<String, Object> requestParameters,
            Map<String, Object> headerParams, DataControllerRequest dcRequest) throws Exception {
        try {
            OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder()
                    .withServiceId(serviceId).withOperationId(operationId).build();

            ServiceRequestBuilder requestBuilder = dcRequest.getServicesManager().getRequestBuilder(operationData);
            requestBuilder.withDCRRequest(dcRequest);
            requestBuilder.withAttributes(getAttributes(dcRequest));
            String authToken = HelperMethods.getHeaders(dcRequest).get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION);

            if (headerParams == null) {
                headerParams = new HashMap<>();
            }

            if (StringUtils.isNotBlank(authToken)) {
                headerParams.put("X-Kony-Authorization", authToken); 
            }
            if ((headerParams != null) && (!headerParams.isEmpty())) {
                requestBuilder.withHeaders(headerParams);
            }
            if ((requestParameters != null) && (!requestParameters.isEmpty())) {
                requestBuilder.withInputs(requestParameters);
            }
            ServiceRequest serviceRequest = requestBuilder.build();
            return serviceRequest.invokeServiceAndGetJson();
        } catch (Exception e) {
            alert.prepareError("Failed to execute service as inline method call for service/operation:" + serviceId + "/"
                    + operationId, e).log();
            throw e;
        }
    }

    private static Map<String, Object> getAttributes(DataControllerRequest request) {
        Map<String, Object> attributesMap = new HashMap<>();
        Iterator<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasNext()) {
            String currAttributeName = attributeNames.next();
            attributesMap.put(currAttributeName, request.getAttribute(currAttributeName));
        }
        return attributesMap;
    }

    public static boolean isOwner(DataControllerRequest dcRequest, String orgId) {
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        if (StringUtils.isNotBlank(customerId) && StringUtils.isNotBlank(orgId)) {
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;

            filter += DBPUtilitiesConstants.AND + "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
            Result result = new Result();
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ORGANISATION_OWNER_GET);

            } catch (HttpCallException e) {
                return false;
            }

            if (HelperMethods.hasRecords(result)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAuthenticationCheckRequiredForService(Map<String, String> loggedInUserInfo) {
        String loggedInUserId = loggedInUserInfo.get("customer_id");
        String isC360Admin = loggedInUserInfo.get("isC360Admin");
        String loggedInUserType = loggedInUserInfo.get("customerType");
        alert.prepareError("isAuthenticationCheckRequiredForService: " + loggedInUserId + "/" + loggedInUserType + "/"
                + isC360Admin).log();

        return isAuthenticationCheckRequiredForService(loggedInUserId, isC360Admin, loggedInUserType);
    }

    public static boolean isAuthenticationCheckRequiredForService(String loggedInUserId, String isC360Admin,
            String loggedInUserType) {

        if ("true".equals(isC360Admin)) {
            return false;
        } else {
            return true;
        }
    }

    public static String getCustomerIdFromSession(FabricRequestManager requestManager) {

        return getCustomerIdFromIdentityService(requestManager);
    }

    public static Map<String, String> getHeaders(FabricRequestManager requestManager) {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                requestManager.getHeadersHandler().getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return headerMap;

    }

    public static Map<String, String> getHeadersWithReportingParams(FabricRequestManager requestManager) {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                requestManager.getHeadersHandler().getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS,
                requestManager.getHeadersHandler().getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return headerMap;

    }

    public static Result callGetApi(FabricRequestManager requestManager, String filterQuery,
            Map<String, String> headers, String url) {

        Map<String, String> inputParams = new HashMap<>();

        inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);

        return callApi(requestManager, inputParams, headers, url);
    }

    public static Result callApi(FabricRequestManager requestManager, Map<String, String> inputParams,
            Map<String, String> headerParams, String url) {
        return ServiceCallHelper.invokeServiceAndGetResult(requestManager, convertToObjectMap(inputParams),
                convertToObjectMap(headerParams), url);
    }

    public static boolean isJsonNotNull(JsonElement ele) {
        return null != ele && !ele.isJsonNull();
    }

    public static Map<String, String> getHeadersWithReportingParams(DataControllerRequest dcRequest) {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS,
                dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return headerMap;
    }

    public static Set<String> getRiskAcceptedValues() {
        Set<String> riskValues = new HashSet<>();
        riskValues.add("SID_DEFAULTER");
        riskValues.add("SID_FRAUDDETECTED");
        riskValues.add("SID_HIGHRISK");
        return riskValues;

    }

    public static void callApiAsync(DataControllerRequest dcRequest, @SuppressWarnings("rawtypes") Map inputParams,
            Map<String, String> headerParams, String url) {

        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() {
                try {
                    Thread.currentThread().setName("FooName");
                    return callApi(dcRequest, inputParams, headerParams, url);
                } catch (HttpCallException e) {
                    alert.prepareError(e.getMessage()).log();
                }
                return new Result();
            }
        };

        try {

            ThreadExecutor.getExecutor(dcRequest).execute(callable);
        } catch (InterruptedException e) {
            alert.prepareError(e.getMessage()).log();
            Thread.currentThread().interrupt();
        }
    }

    public static void callApiAsync(Map<String, Object> input, Map<String, Object> headerMap, String dbxSendEmailOrch) {
        Callable<JsonObject> callable = new Callable<JsonObject>() {
            @Override
            public JsonObject call() {
                Thread.currentThread().setName("FooName");
                return ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, dbxSendEmailOrch);
            }
        };

        try {

            ThreadExecutor.getExecutor().execute(callable);
        } catch (InterruptedException e) {
            alert.prepareError(e.getMessage()).log();
            Thread.currentThread().interrupt();
        }
    }

    public static boolean isAdmin(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + userId;
        Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_GET);
        return "admin".equalsIgnoreCase(HelperMethods.getFieldValue(user, "UserName"));
    }

    public static boolean isAdmin(FabricRequestManager requestManager, String userId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + userId;
        Result user = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                URLConstants.USER_GET);
        return "admin".equalsIgnoreCase(HelperMethods.getFieldValue(user, "UserName"));
    }

    public static void callApiAsync(FabricRequestManager requestManager, Map<String, String> inputParams,
            Map<String, String> headerParams, String url) {
        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() {
                return callApi(requestManager, inputParams, headerParams, url);
            }
        };

        try {
            ThreadExecutor.getExecutor(requestManager).execute(callable);
        } catch (InterruptedException e) {
            alert.prepareError(e.getMessage()).log();
            Thread.currentThread().interrupt();

        }
    }

    public static void callExternalApiAsync(@SuppressWarnings("rawtypes") Map inputParams,
            Map<String, String> headerParams, String url, DataControllerRequest dcRequest) {
        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() {
                try {
                    return callExternalApi(inputParams, headerParams, url);
                } catch (HttpCallException e) {
                    alert.prepareError(e.getMessage()).log();
                }
                return new Result();
            }
        };

        try {
            ThreadExecutor.getExecutor(dcRequest).execute(callable);
        } catch (InterruptedException e) {
            alert.prepareError(e.getMessage()).log();
            Thread.currentThread().interrupt();

        }
    }

    public static void callExternalApiAsync(@SuppressWarnings("rawtypes") Map inputParams,
            Map<String, String> headerParams, String url, FabricRequestManager requestManager) {
        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() {
                try {
                    return callExternalApi(inputParams, headerParams, url);
                } catch (HttpCallException e) {
                    alert.prepareError(e.getMessage()).log();
                }
                return new Result();
            }
        };

        try {
            ThreadExecutor.getExecutor(requestManager).execute(callable);
        } catch (InterruptedException e) {
            alert.prepareError(e.getMessage()).log();
            Thread.currentThread().interrupt();

        }
    }

    public static boolean hasErrorOpstatus(JsonObject jsonObject) {
        return !JSONUtil.isJsonValueCorrect(jsonObject, DBPConstants.FABRIC_OPSTATUS_KEY, DBPUtilitiesConstants.ZERO);
    }

    public static boolean hasErrorOpstatus(JSONObject jsonObject) {
        return (jsonObject == null || !jsonObject.has(DBPConstants.FABRIC_OPSTATUS_KEY)
                || jsonObject.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0);
    }

    public static Set<String> splitString(String inputString, String delimiter) {
        Set<String> hashSet = new HashSet<>();
        try {
            hashSet = new HashSet<>(Arrays.asList(StringUtils.split(inputString, delimiter)));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return hashSet;

    }

    public static int generateRandomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (getRandomId() * range) + min;
    }

    public static long getRandomId() {
		SecureRandom random = new SecureRandom();
		return 1000000000L + random.nextInt(900000000);
	}
    
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

    @SuppressWarnings("unchecked")
    public static String getAuthToken(DataControllerRequest request) {
        Object authToken = null;

        authToken = request.getHeader(MWConstants.X_KONY_AUTHORIZATION_HEADER);

        if (authToken == null) {
            Map<String, Object> queryParams = (Map<String, Object>) request.getAttribute("queryparams");
            if (queryParams != null) {
                authToken = queryParams.get("authToken");
            }
        }
        return String.class.cast(authToken);

    }
    
    public static Map<String, Object> addMSJWTAuthHeader(DataControllerRequest request, Map<String, Object> headers,
            String flow_type) {
        request.addRequestParam_("flowType", flow_type);
        String authToken = TokenUtils.getMSAuthToken(request);

        if (headers == null) {
            headers = new HashMap<String, Object>();
        }

        headers.put("Authorization", authToken);
        LegalEntityUtil.addCompanyIDToHeaders(request);
        return headers;
    }
    
    public static Map<String, Object> addMSJWTAuthHeader(Map<String, Object> headers,
            String flow_type) {
    	 Map<String, String> input = new HashMap<String, String>();
         input.put("flowType", flow_type);
         String authToken = TokenUtils.getMSAuthToken(input, headers);

        if (headers == null) {
            headers = new HashMap<String, Object>();
        }

        headers.put("Authorization", authToken);
        LegalEntityUtil.addCompanyIDToHeaders(null);
        return headers;
    }
    
    public static Map<String, Object> addJWTAuthHeader(DataControllerRequest request, Map<String, Object> headers,
            String flow_type) {
        request.addRequestParam_("flowType", flow_type);
        String authToken = TokenUtils.getT24AuthToken(request);

        if (headers == null) {
            headers = new HashMap<String, Object>();
        }

        headers.put("Authorization", authToken);
        LegalEntityUtil.addCompanyIDToHeaders(request);
        return headers;
    }

    public static Map<String, Object> addJWTAuthHeader(Map<String, Object> headers,
            String flow_type) {

        Map<String, String> input = new HashMap<String, String>();
        input.put("flowType", flow_type);
        String authToken = TokenUtils.getT24AuthToken(input, headers);

        if (headers == null) {
            headers = new HashMap<String, Object>();
        }

        headers.put("Authorization", authToken);
        LegalEntityUtil.addCompanyIDToHeaders(null);
        return headers;
    }

    public static boolean isEuropieanGeography() {
        String deploymentGeography = (String) MemoryManager
                .getFromCache(DBPUtilitiesConstants.CACHE_KEY_FOR_DEPLOYMENT_GEOGRAPHY);
        if (StringUtils.isBlank(deploymentGeography)) {
            try {
                Result appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
                        StringUtils.EMPTY);
                deploymentGeography = getFieldValue(appInfo, "deploymentGeography");
                MemoryManager.saveIntoCache(DBPUtilitiesConstants.CACHE_KEY_FOR_DEPLOYMENT_GEOGRAPHY,
                        deploymentGeography);
            } catch (Exception e) {
                alert.prepareError("exception while fetching application", e).log();
            }
        }
        return "EUROPE".equalsIgnoreCase(deploymentGeography);
    }

    public static void getInputParamMapFromRequest(Map<String, String> inputParams, DataControllerRequest dcRequest) {

        String key;
        Iterator<String> iterator = dcRequest.getParameterNames();
        while (iterator.hasNext()) {
            key = iterator.next();
            inputParams.put(key, dcRequest.getParameter(key));
        }
    }

    public static void addError(Result result, DBXResult dbxResult) {
        result.addParam(ErrorCodeEnum.ERROR_CODE_KEY, dbxResult.getDbpErrCode());
        result.addParam(ErrorCodeEnum.ERROR_MESSAGE_KEY, dbxResult.getDbpErrMsg());
    }

    public static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
        JSONObject mergedJSON = new JSONObject();
        if (null == json1 && null == json2) {
            return mergedJSON;
        }
        if (null == json1 || json1.length() == 0) {
            return json2;
        }
        if (null == json2 || json2.length() == 0) {
            return json1;
        }
        try {
            mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
            for (String crunchifyKey : JSONObject.getNames(json2)) {
                mergedJSON.put(crunchifyKey, json2.get(crunchifyKey));
            }

        } catch (JSONException e) {
            throw new RuntimeException("JSON Exception" + e);
        }
        return mergedJSON;
    }

    public static long getNumericId(Integer n) {
        SecureRandom rnd = new SecureRandom();
        long generatedValue;
        generatedValue = (long) (Math.pow(10, n - 1) + rnd.nextInt((int) (9 * Math.pow(10, n - 2))));
        return generatedValue;
    }

    public static String getIdUsingCurrentTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmsssSSS");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    private static Map<String, String> customeridentifierMap = null;

    public static Map<String, String> getCustomerIdentifierMapping() {

        if (customeridentifierMap == null) {
            customeridentifierMap = new HashMap<String, String>();
            customeridentifierMap.put("Driver License", "ID_DRIVING_LICENSE");
            customeridentifierMap.put("Passport", "ID_PASSPORT");
        }

        return customeridentifierMap;
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

    public static String getRandomNumericString(int length) {
        SimpleDateFormat idFormatter = new SimpleDateFormat("ssSSS");
        String dateString = idFormatter.format(new Date());
        String randomString;
        SecureRandom secureRand = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int randStrLen;
        if (length > 5) {
            randStrLen = length - dateString.length();
        } else {
            randStrLen = 8 - dateString.length();
        }
        sb.append(Integer.toString(secureRand.nextInt(9) + 1));
        for (int i = 1; i < randStrLen; i++) {
            sb.append(Integer.toString(secureRand.nextInt(10)));
        }
        randomString = sb.toString();
        return randomString + dateString;
    }

    public static String generateUniqueOrganisationId(DataControllerRequest dcRequest) {
        int orgIdLength = DBPUtilitiesConstants.ORGANISATION_ID_LENGTH;
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        String orgIDLengthString =
                configurations.get(BundleConfigurationHandler.ORGANIZATION_ID_LENGTH);
        if (StringUtils.isNotBlank(orgIDLengthString)) {
            try {
                orgIdLength = Integer.parseInt(orgIDLengthString);
            } catch (NumberFormatException e) {
                orgIdLength = DBPUtilitiesConstants.ORGANISATION_ID_LENGTH;
            }
        }
        return HelperMethods.getUniqueNumericString(orgIdLength);

    }

    public static String generateUniqueContractId(DataControllerRequest dcRequest) {
        int orgIdLength = DBPUtilitiesConstants.ORGANISATION_ID_LENGTH;
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        String orgIDLengthString =
                configurations.get(BundleConfigurationHandler.ORGANIZATION_ID_LENGTH);
        if (StringUtils.isNotBlank(orgIDLengthString)) {
            try {
                orgIdLength = Integer.parseInt(orgIDLengthString);
            } catch (NumberFormatException e) {
                orgIdLength = DBPUtilitiesConstants.ORGANISATION_ID_LENGTH;
            }
        }
        return HelperMethods.getUniqueNumericString(orgIdLength);

    }

    public static String generateUniqueCustomerId(DataControllerRequest dcRequest) {
        int customerIdLength = DBPUtilitiesConstants.CUSTOMER_ID_LENGTH;
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        String customerIDLengthString =
                configurations.get(BundleConfigurationHandler.CUSTOMER_ID_LENGTH);
        if (StringUtils.isNotBlank(customerIDLengthString)) {
            try {
                customerIdLength = Integer.parseInt(customerIDLengthString);
            } catch (NumberFormatException e) {
                customerIdLength = DBPUtilitiesConstants.CUSTOMER_ID_LENGTH;
            }
        }
        return HelperMethods.getUniqueNumericString(customerIdLength);

    }

    public static String generateUniqueUserName(DataControllerRequest dcRequest) {
        int usernameLength = DBPUtilitiesConstants.DEFAULT_USERNAME_LENGTH;
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        String usernameLengthString =
                configurations.get(BundleConfigurationHandler.USERNAME_LENGTH);
        if (StringUtils.isNotBlank(usernameLengthString)) {
            try {
                usernameLength = Integer.parseInt(usernameLengthString);
            } catch (NumberFormatException e) {
                usernameLength = DBPUtilitiesConstants.DEFAULT_USERNAME_LENGTH;
            }
        }
        return HelperMethods.getUniqueNumericString(usernameLength);

    }

    public static int generateProspectExpiryTime(DataControllerRequest dcRequest) {
        int prospectExpiryLimit = DBPUtilitiesConstants.PROSPECT_EXPIRATION_DATE;
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_NUO, dcRequest);
        String orgIDLengthString =
                configurations.get(BundleConfigurationHandler.PROSPECT_EXPIRY_DATE);
        if (StringUtils.isNotBlank(orgIDLengthString)) {
            try {
                prospectExpiryLimit = Integer.parseInt(orgIDLengthString);
            } catch (NumberFormatException e) {
                prospectExpiryLimit = DBPUtilitiesConstants.PROSPECT_EXPIRATION_DATE;
            }
        }
        return prospectExpiryLimit;

    }

    public static boolean isBlank(String... args) {
        List<String> strings = Arrays.asList(args);
        List<String> filtered =
                strings.stream().filter(string -> StringUtils.isBlank(string)).collect(Collectors.toList());
        if (filtered.size() != 0)
            return true;
        return false;
    }

    public static boolean allEmpty(Map<String, Object> searchPostParameters) {

        for (String key : searchPostParameters.keySet()) {
            if (StringUtils.isNotBlank((String) searchPostParameters.get(key))) {
                return false;
            }
        }

        return true;
    }

    public static Map<String, String> getCustomerFromAPIDBPIdentityService(FabricRequestManager fabricRequestManager) {
        return getCustomerFromAPIIdentityService(fabricRequestManager);
    }

    private static Map<String, String> getCustomerFromAPIIdentityService(FabricRequestManager fabricRequestManager) {
        Result userAttributesResponse = new Result();

        Map<String, String> user = new HashMap<>();

        try {
            if (fabricRequestManager.getServicesManager() != null
                    && fabricRequestManager.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> hashMap = fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes();

                for (String key : hashMap.keySet()) {
                    user.put(key, hashMap.get(key) + "");
                }

                user.put("user_id", user.get("customer_id"));
                user.put("customer_id", user.get("customer_id"));
                user.put("Customer_id", user.get("customer_id"));
                user.put("Customer_Id", user.get("customer_id"));
                user.put("customerId", user.get("customer_id"));
                user.put("userName", user.get("UserName"));
                user.put("UserName", user.get("UserName"));
                user.put("username", user.get("UserName"));
                user.put("countryCode", user.get("countryCode"));
                user.put("customerType", user.get("CustomerType_id"));
                user.put("CustomerType_id", user.get("CustomerType_id"));
                user.put("isC360Admin", user.get("isSuperAdmin"));
            }
        } catch (MiddlewareException e1) {
            e1.getMessage();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (user.size() == 0) {
            userAttributesResponse = callApi(fabricRequestManager, null, getHeaders(fabricRequestManager),
                    URLConstants.USER_ID_GET_API_IDNETITY);

            if (userAttributesResponse != null) {
                for (Param param : userAttributesResponse.getAllParams()) {
                    user.put(param.getName(), param.getValue());
                }
            }
        }
        return user;
    }

    public static boolean isJsonEleNull(JsonElement ele) {
        return null == ele || ele.isJsonNull();
    }

    public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = getElementFromJsonObject(object, key, required);
        return isJsonEleNull(element) ? null : element.getAsString();
    }

    public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = object.get(key);
        if (isJsonEleNull(element) && required) {
            throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
        }
        return element;
    }

    public static String getAppId(DataControllerRequest dcRequest) {
        String reportingParams = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        try {
            reportingParams = URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            alert.prepareError("Caught exception while Decoding Reporting Params : ", e).log();
        }
        try {
            JSONObject jsonObject = new JSONObject(reportingParams);
            String aid = "";
            if (reportingParams.contains("aid")) {
                aid = jsonObject.getString("aid");
            }
            String originationMapping = EnvironmentConfigurationsHandler.getServerProperty("ORIGINATION_TNC_MAPPING");
            String appIdtoAppMapping = EnvironmentConfigurationsHandler.getServerProperty("AC_APPID_TO_APP_MAPPING");
            JSONObject appIdtoAppMappingJson = new JSONObject(appIdtoAppMapping);
            JSONObject originationAppidMapping = new JSONObject(originationMapping);
            String currentApp = "";
            if (originationAppidMapping.has(aid))
                currentApp = originationAppidMapping.getString(aid);
            else if (appIdtoAppMappingJson.has(aid))
                currentApp = appIdtoAppMappingJson.getString(aid);

            return currentApp;
        } catch (JSONException e) {
            alert.prepareError("Caught exception while Getting mfaname from reporting Params: ", e).log();
        }
        return "";
    }
	
// method for adding SavingsPot MS headers param - STARTS
	public static Map<String, Object> addSavingsPotMSHeaders(DataControllerRequest request, Map<String, Object> headers,
			String flow_type) {

		headers = addMSJWTAuthHeader(request, headers, flow_type);
		
		headers.put(DBPUtilitiesConstants.ROLE_ID, ServerConfigurations.SAVINGSPOT_ROLE_ID.getValueIfExists());
		
		String deploymentPlatform = ServerConfigurations.SAVINGSPOT_DEPLOYMENT_PLATFORM.getValueIfExists();

		if (StringUtils.isNotEmpty(deploymentPlatform)) {
			if (StringUtils.equalsIgnoreCase(deploymentPlatform,
					DBPUtilitiesConstants.AWS))
				headers.put(DBPUtilitiesConstants.X_API_KEY,
						ServerConfigurations.SAVINGSPOT_AUTHORIZATION_KEY.getValueIfExists());
			else if (StringUtils.equalsIgnoreCase(
					deploymentPlatform,
					DBPUtilitiesConstants.AZURE))
				headers.put(DBPUtilitiesConstants.X_FUNCTIONS_KEY,
						ServerConfigurations.SAVINGSPOT_AUTHORIZATION_KEY.getValueIfExists());
		}
		return headers;
	}
	// method for adding SavingsPot MS headers param - ENDS
	
    private static Map<String, String> convertMapType(Map<String, Object> map) {
        Map<String, String> resultMap = new HashMap<>();
        for (Entry<String, Object> entry : map.entrySet()) {
        	try {
            resultMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        	}catch(Exception e) {
        		
        	}
        }
        return resultMap;
    }
    
    public static Map<String, Object> getHeaders(HeadersHandler handler) {

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
        		handler.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return headerMap;

    }
    
	// PSD2 Changes
	public static boolean isPSD2Agent(DataControllerRequest request) {
		try {
			String userAgentType = CustomerSessionsUtil.getLoggedInUserAttributesMap(request)
					.get(DBPUtilitiesConstants.USERAGENTLOGINTYPE).toString();
			if ("PSD2".equalsIgnoreCase(userAgentType)) {
				return true;
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return false;
	}
	
	/*please use LegalEntityUtil to fetch LegalEntityId*/
	@Deprecated
	public static String getLegalEntityId() {
		return "GB0010001";
	}
	public static String replaceCompanyLegalUnitWithLegalEntityId(String str) {
		return str.replace("companyLegalUnit","legalEntityId");
	}
	
	public static Map<String, Object> addJWTAuthHeaderParty(Map<String, Object> headers,
            String flow_type){
		try {
        String authToken = TokenUtils.getCommonMSAuthToken();
        headers.put("Authorization", authToken);
        
        String deploymentPlatform =
                URLFinder.getServerRuntimeProperty("PARTYMS_DEPLOYMENT_PLATFORM");
        if (StringUtils.isNotBlank(deploymentPlatform)) {
            if (StringUtils.equals(deploymentPlatform, "aws"))
                headers.put("x-api-key",
                        URLFinder.getServerRuntimeProperty("PARTYMS_AUTHORIZATION_KEY"));
            if (StringUtils.equals(deploymentPlatform, "azure"))
                headers.put("x-functions-key",
                        URLFinder.getServerRuntimeProperty("PARTYMS_AUTHORIZATION_KEY"));
        }
		} catch (Exception e) {
			alert.prepareError("Exception in generating JWT authToken for auth header", e).log();
			return headers;
		}
        return headers;
    }
	public static boolean validateRegex(String string) {
		String regex = "[A-Za-z0-9\\s]{1,51}$";
		string.trim();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}
	
	public static int getExpiryTime(String key) {
		try {
			String expiryTime = EnvironmentConfigurationsHandler.getServerProperty(key);
			return Integer.parseInt(expiryTime);
		} catch (Exception e) {
			diagnostic.prepareDebug("The server prperty"+key+" not found or the value is not integer.").log();
		}
		return (20*60);
	}

	public static String getDeviceId(DataControllerRequest dcRequest) {

		String reportingParams = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
		try {
			reportingParams = URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			alert.prepareError("Caught exception while Decoding Reporting Params : ", e).log();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(reportingParams);

			if (reportingParams.contains("did")) {

				return jsonObject.getString("did");
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while Getting DeviceId from reporting Params: ", e).log();
		}

		return "";
	}

	public static String decryptClientIp(String encryptedIp) {
		StringBuilder decryptedId = new StringBuilder();
		String key = "";
		try {
			key = com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler.getClientAppProperty("CLIENT_IP_KEY");	
		}  catch (Exception e) {}
		if(StringUtils.isBlank(key)) {
			key = "255.0.255.0";
		}
		String[] encryptedOctets = encryptedIp.split("\\.");
		String encryptedBinary = "";
		for(String octet : encryptedOctets) {
			encryptedBinary += Integer.toBinaryString(Integer.parseInt(octet)) + " ";
		}
		
		String[] keyOctets = key.split("\\.");
		String keyBinary = "";
		for(String octet : keyOctets) {
			keyBinary += Integer.toBinaryString(Integer.parseInt(octet)) + " ";
		}
		String[] encryptedBits = encryptedBinary.split(" ");
		String[] keyBits = keyBinary.split(" ");
		
		for(int i = 0; i <  encryptedBits.length; i++) {
			decryptedId.append(Integer.parseInt(encryptedBits[i], 2) ^ Integer.parseInt(keyBits[i], 2));
			decryptedId.append(".");
		}
		
		return decryptedId.substring(0, decryptedId.length() - 1);
	}
	
	public static String getDecyptedClientIpAddressFromCache(DataControllerRequest request) {
		String sessionToken = "";
		try {
			sessionToken = request.getServicesManager().getIdentityHandler().getSecurityAttributes()
					.get(DBPUtilitiesConstants.SESSION_TOKEN).toString();
		} catch (Exception e) {
			return "";
		}
		String key = sessionToken + "_" + DBPUtilitiesConstants.CLIENT_IP;
		String ipAddress = MemoryManager.getFromCache(key).toString();
		return ipAddress;
	}
}