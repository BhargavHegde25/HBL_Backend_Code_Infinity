package com.kony.dbputilities.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kony.dbputilities.exceptions.HttpCallException;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AdminUtil {

    private static String ACCESS_TOKEN = null;
    private static String TOKEN_UPDATED_TIME = null;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static Result invokeAPI(Map<String, String> inputParams, String relativeURL, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken(dcRequest));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return ServiceCallHelper.invokePassThroughServiceAndGetResult(dcRequest,
                HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
    }

    public static Result invokeAPI(Map<String, String> inputParams, String relativeURL,
            FabricRequestManager requestManager) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken(requestManager));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        try {
            return ServiceCallHelper.invokePassThroughServiceAndGetResult(requestManager,
                    HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
        } catch (Exception e) {
            alert.prepareError("Exception while calling relativeURL" + relativeURL, e).log();
        }
        return new Result();
    }

    public static Result invokeAPI(Map<String, String> inputParams, String relativeURL) {
        Map<String, String> headerMap = new HashMap<>();
        String backendToken = getAdminToken();
        headerMap.put("backendToken", backendToken);
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        try {
            return ServiceCallHelper.invokeServiceAndGetResult(HelperMethods.convertToObjectMap(inputParams), headerMap,
                    relativeURL, backendToken);
        } catch (Exception e) {
            alert.prepareError("Exception while calling relativeURL" + relativeURL, e).log();
        }
        return new Result();
    }

    public static JsonObject invokeAPIAndGetJson(Map<String, String> inputParams, Map<String, String> headerParams,
            String relativeURL, DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, Object> headerMap = new HashMap<>();
        if (headerParams != null) {
            headerMap = HelperMethods.convertToObjectMap(headerParams);
        }
        headerMap.put("backendToken", getAdminToken(dcRequest));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return ServiceCallHelper.invokePassThroughServiceAndGetJson(dcRequest,
                HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
    }

    public static JsonObject invokeAPIAndGetJson(Map<String, String> inputParams, Map<String, String> headerParams,
            String relativeURL, FabricRequestManager requestManager) throws HttpCallException {
        Map<String, Object> headerMap = new HashMap<>();
        if (headerParams != null) {
            headerMap = HelperMethods.convertToObjectMap(headerParams);
        }
        headerMap.put("backendToken", getAdminToken(requestManager));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return ServiceCallHelper.invokePassThroughServiceAndGetJson(requestManager,
                HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
    }

    public static JsonObject invokeAPIAndGetJson(Map<String, String> inputParams, String relativeURL,
            DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken(dcRequest));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return ServiceCallHelper.invokePassThroughServiceAndGetJson(dcRequest,
                HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
    }

    public static JsonObject invokeAPIAndGetJson(Map<String, String> inputParams, String relativeURL,
            FabricRequestManager fabricRequestManager) throws HttpCallException {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken(fabricRequestManager));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return ServiceCallHelper.invokePassThroughServiceAndGetJson(fabricRequestManager,
                HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
    }

    public static JsonObject invokeAPIAndGetJson(Map<String, String> inputParams, String relativeURL,
            String konyAuthToken) throws HttpCallException {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken());
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return ServiceCallHelper.invokePassThroughServiceAndGetJson(HelperMethods.convertToObjectMap(inputParams),
                headerMap, relativeURL, konyAuthToken);
    }

    public static JsonObject invokeAPIAndGetJson(Map<String, String> inputParams, String relativeURL)
            throws HttpCallException {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken());
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return ServiceCallHelper.invokeServiceAndGetJson(HelperMethods.convertToObjectMap(inputParams), headerMap,
                relativeURL);
    }

    public static JsonObject invokeAPIAndGetResult(Map<String, String> inputParams, String relativeURL,
            FabricRequestManager requestManager) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken(requestManager));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        try {
            return ServiceCallHelper.invokePassThroughServiceAndGetJson(requestManager,
                    HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
        } catch (Exception e) {
            alert.prepareError("Exception while calling relativeURL" + relativeURL, e).log();
        }
        return new JsonObject();
    }

    public static JsonObject invokeAPIAndGetResult(Map<String, String> inputParams, String relativeURL,
            DataControllerRequest dcRequest) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken(dcRequest));
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        try {
            return ServiceCallHelper.invokePassThroughServiceAndGetJson(dcRequest,
                    HelperMethods.convertToObjectMap(inputParams), headerMap, relativeURL);
        } catch (Exception e) {
            alert.prepareError("Exception while calling relativeURL" + relativeURL, e).log();
        }
        return new JsonObject();
    }

    public static JsonObject invokeAPIAndGetResult(Map<String, String> inputParams, String relativeURL) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", getAdminToken());
        headerMap.put("X-Kony-AC-API-Access-By", "OLB");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        try {
            return ServiceCallHelper.invokeServiceAndGetJson(HelperMethods.convertToObjectMap(inputParams), headerMap,
                    relativeURL);
        } catch (Exception e) {
            alert.prepareError("Exception while calling relativeURL" + relativeURL, e).log();
        }
        return new JsonObject();
    }

    public static String getForceLoginAccessToken(DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("X-Kony-AC-API-Access-Token", URLFinder.getPathUrl(URLConstants.ADMIN_ACCESS_TOKEN, dcRequest));
        headers.put("AC-X-Kony-App-Secret", URLFinder.getPathUrl(URLConstants.ADMIN_SECREAT, dcRequest));
        headers.put("AC-X-Kony-App-Key", URLFinder.getPathUrl(URLConstants.ADMIN_KEY, dcRequest));
        headers.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        JsonObject response = null;
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(dcRequest, null, headers,
                    URLConstants.ADMIN_LOGIN_URL);
        } catch (Exception e) {
            alert.prepareError("Exception while calling c360 login", e).log();
        }
        if (null != response && null != response.get("claims_token")) {
            return response.get("claims_token").getAsString();
        }
        return "";
    }

    private static String getForceLoginAccessToken(FabricRequestManager requestManager) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Kony-AC-API-Access-Token",
                URLFinder.getPathUrl(URLConstants.ADMIN_ACCESS_TOKEN, requestManager));
        headers.put("AC-X-Kony-App-Secret", URLFinder.getPathUrl(URLConstants.ADMIN_SECREAT, requestManager));
        headers.put("AC-X-Kony-App-Key", URLFinder.getPathUrl(URLConstants.ADMIN_KEY, requestManager));
        headers.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        JsonObject response = null;
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(requestManager, null, headers,
                    URLConstants.ADMIN_LOGIN_URL);
        } catch (Exception e) {
            alert.prepareError("Exception while calling c360 login", e).log();
        }
        if (null != response && null != response.get("claims_token")) {
            return response.get("claims_token").getAsString();
        }
        return "";
    }

    public static String getForceLoginAccessToken() throws HttpCallException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("X-Kony-AC-API-Access-Token", URLFinder.getServerRuntimeProperty(URLConstants.ADMIN_ACCESS_TOKEN));
        headers.put("AC-X-Kony-App-Secret", URLFinder.getServerRuntimeProperty(URLConstants.ADMIN_SECREAT));
        headers.put("AC-X-Kony-App-Key", URLFinder.getServerRuntimeProperty(URLConstants.ADMIN_KEY));
        headers.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        JsonObject response = null;
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(null, headers,
                    URLConstants.ADMIN_LOGIN_URL);
        } catch (Exception e) {
            alert.prepareError("Exception while calling c360 login", e).log();
        }
        if (null != response && null != response.get("claims_token")) {
            return response.get("claims_token").getAsString();
        }
        return "";
    }

    public static String getAdminToken(DataControllerRequest requestInstance) {
        Calendar currentTimeStamp = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        try {
            if (StringUtils.isBlank(ACCESS_TOKEN) || TOKEN_UPDATED_TIME == null) {
                TOKEN_UPDATED_TIME = String.valueOf(simpleDateFormat.format(currentTimeStamp.getTime()));
                ACCESS_TOKEN = getForceLoginAccessToken(requestInstance);
            } else {

                Date parsedTime = simpleDateFormat.parse(TOKEN_UPDATED_TIME);
                Calendar TokenUpdatedTime = Calendar.getInstance();
                TokenUpdatedTime.setTime(parsedTime);

                currentTimeStamp.add(Calendar.MINUTE,
                        (-1) * Integer.parseInt(URLFinder.getPathUrl(URLConstants.TIME_OUT_IN_MINS, requestInstance)));
                if (TokenUpdatedTime.before(currentTimeStamp)) {
                    ACCESS_TOKEN = getForceLoginAccessToken(requestInstance);
                    TOKEN_UPDATED_TIME = String.valueOf(simpleDateFormat.format(Calendar.getInstance().getTime()));
                }

            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return ACCESS_TOKEN;
    }

    public static String getAdminToken(FabricRequestManager requestInstance) {
        Calendar currentTimeStamp = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        try {
            if (StringUtils.isBlank(ACCESS_TOKEN) || TOKEN_UPDATED_TIME == null) {
                TOKEN_UPDATED_TIME = String.valueOf(simpleDateFormat.format(currentTimeStamp.getTime()));
                ACCESS_TOKEN = getForceLoginAccessToken(requestInstance);
            } else {

                Date parsedTime = simpleDateFormat.parse(TOKEN_UPDATED_TIME);
                Calendar TokenUpdatedTime = Calendar.getInstance();
                TokenUpdatedTime.setTime(parsedTime);

                currentTimeStamp.add(Calendar.MINUTE,
                        (-1) * Integer.parseInt(URLFinder.getPathUrl(URLConstants.TIME_OUT_IN_MINS, requestInstance)));
                if (TokenUpdatedTime.before(currentTimeStamp)) {
                    ACCESS_TOKEN = getForceLoginAccessToken(requestInstance);
                    TOKEN_UPDATED_TIME = String.valueOf(simpleDateFormat.format(Calendar.getInstance().getTime()));
                }

            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return ACCESS_TOKEN;
    }

    public static String getAdminToken() {
        Calendar currentTimeStamp = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        try {
            if (StringUtils.isBlank(ACCESS_TOKEN) || TOKEN_UPDATED_TIME == null) {
                TOKEN_UPDATED_TIME = String.valueOf(simpleDateFormat.format(currentTimeStamp.getTime()));
                ACCESS_TOKEN = getForceLoginAccessToken();
            } else {

                Date parsedTime = simpleDateFormat.parse(TOKEN_UPDATED_TIME);
                Calendar TokenUpdatedTime = Calendar.getInstance();
                TokenUpdatedTime.setTime(parsedTime);

                currentTimeStamp.add(Calendar.MINUTE,
                        (-1) * Integer.parseInt(URLFinder.getServerRuntimeProperty(URLConstants.TIME_OUT_IN_MINS)));
                if (TokenUpdatedTime.before(currentTimeStamp)) {
                    ACCESS_TOKEN = getForceLoginAccessToken();
                    TOKEN_UPDATED_TIME = String.valueOf(simpleDateFormat.format(Calendar.getInstance().getTime()));
                }

            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return ACCESS_TOKEN;
    }

    public static JsonObject invokeC360ServiceAndGetJson(DataControllerRequest dcRequest,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url, String backendToken) {
        if (null == headerParams) {
            headerParams = new HashMap<>();
        }
        headerParams.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerParams.put("X-Kony-AC-API-Access-By", "OLB");
        if (StringUtils.isNotBlank(backendToken)) {
            headerParams.put("backendToken", backendToken);
        }
        return ServiceCallHelper.invokePassThroughServiceAndGetJson(dcRequest, inputParams, headerParams, url);
    }

    public static JsonObject invokeC360ServiceAndGetJson(Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url, String backendToken) {
        if (null == headerParams) {
            headerParams = new HashMap<>();
        }
        headerParams.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                headerParams.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerParams.put("X-Kony-AC-API-Access-By", "OLB");
        if (StringUtils.isNotBlank(backendToken)) {
            headerParams.put("backendToken", backendToken);
        }
        return ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerParams, url);
    }

    public static Boolean verifyCSRAssistToken(DataControllerRequest dcRequest, String session_token) {

        Result csrAssistResponse = new Result();
        try {
            Map<String, String> input = new HashMap<>();
            input.put("ReportingParams", dcRequest.getHeader("X-Kony-ReportingParams"));
            input.put("csrAssistGrantToken", session_token);
            csrAssistResponse = invokeAPI(input, URLConstants.ADMIN_VERIFY_CSR_ASSIST_TOKEN,
                    dcRequest);

            if (csrAssistResponse == null || csrAssistResponse.getParamByName("isTokenValid") == null) {
                dcRequest.setAttribute("CSR_Response", "Failed while verifying CSR assist token");
                return false;
            }
            if (csrAssistResponse.getParamByName(DBPConstants.DBP_ERROR_CODE_KEY) != null) {
                dcRequest.setAttribute("CSR_Response",
                        csrAssistResponse.getParamByName(DBPConstants.DBP_ERROR_MESSAGE_KEY).getValue());
                dcRequest.setAttribute("CSR_ResponseCode",
                        csrAssistResponse.getParamByName(DBPConstants.DBP_ERROR_CODE_KEY).getValue());
                return false;
            }
            if (csrAssistResponse.getParamByName("CustomerUsername") != null) {
                dcRequest.setAttribute("CSRAssist_Customer_username",
                        csrAssistResponse.getParamByName("CustomerUsername").getValue());
            }
            if (csrAssistResponse.getParamByName("CustomerId") != null) {
                dcRequest.setAttribute("CSRAssist_Customer_id",
                        csrAssistResponse.getParamByName("CustomerId").getValue());
            }
            if (csrAssistResponse.getParamByName("accountId") != null) {
                dcRequest.setAttribute("accountId",
                        csrAssistResponse.getParamByName("accountId").getValue());
            }
            if (csrAssistResponse.getRecordById("C360UserDetails").getParamByName("id") != null) {
                dcRequest.setAttribute("CSR_User_Id",
                        csrAssistResponse.getRecordById("C360UserDetails").getParamByName("id").getValue());
            }
            if (csrAssistResponse.getRecordById("C360UserDetails").getParamByName("role") != null) {
                dcRequest.setAttribute("CSR_Role",
                        csrAssistResponse.getRecordById("C360UserDetails").getParamByName("role").getValue());
            }
            if (csrAssistResponse.getRecordById("C360UserDetails").getParamByName("name") != null) {
                dcRequest.setAttribute("CSR_Name",
                        csrAssistResponse.getRecordById("C360UserDetails").getParamByName("name").getValue());
            }
            if (csrAssistResponse.getRecordById("C360UserDetails").getParamByName("username") != null) {
                dcRequest.setAttribute("CSR_Username",
                        csrAssistResponse.getRecordById("C360UserDetails").getParamByName("username").getValue());
            }
            if (csrAssistResponse.getParamByName("CustomerType") != null) {
                dcRequest.setAttribute("CSRAssist_User_Type",
                        csrAssistResponse.getParamByName("CustomerType").getValue());
            }
            if (csrAssistResponse.getDatasetById("CSRAssistCompositeActions") != null) {
                JSONArray permissionsJSONArray = getCustomerPermissions(csrAssistResponse);
                dcRequest.setAttribute("permissions", permissionsJSONArray.toString());
            }
            if (csrAssistResponse.getParamByName("features") != null) {
                dcRequest.setAttribute("features",
                        csrAssistResponse.getParamByName("features").getValue());
            }

            return Boolean.parseBoolean(csrAssistResponse.getParamByName("isTokenValid").getValue());
        } catch (Exception e) {
            dcRequest.setAttribute("CSR_Response", "Unexpected error occurred while verifying CSR assist token");
            return false;
        }
    }

    private static JSONArray getCustomerPermissions(Result result) {
        JSONArray permissionsJSONArray = new JSONArray();
        Dataset services = result.getDatasetById("CSRAssistCompositeActions");
        List<Record> records = new ArrayList<>();
        if ((services != null) && (services.getAllRecords().size() != 0)) {
            records = services.getAllRecords();
            for (int i = 0; i < records.size(); i++) {
                JSONObject service = CommonUtils.convertRecordToJSONObject(services.getRecord(i));
                String serviceId = service.getString("Action_id");
                permissionsJSONArray.put(serviceId);
            }
        }
        return permissionsJSONArray;
    }
    
    /*
     * This Method checks for Admin details if available.
     * And add the details in customParams to track the Audit Logs.
     */
    public static void addAdminUserNameRoleIfAvailable(JsonObject customParams, DataControllerRequest dcRequest ){
    	
    	if(null == customParams || null == dcRequest) {
    		return;
    	}
    	try {
			
    		Map<String, Object> userAttributes = dcRequest.getServicesManager().getIdentityHandler().getUserAttributes();
    		if(null != userAttributes) {
    			
    			String csrUsername = userAttributes.get("CSR_Username") != null? (String) userAttributes.get("CSR_Username") : null  ;
    			String csrRole = userAttributes.get("CSR_Role") != null? (String) userAttributes.get("CSR_Role") : null  ;
    			if(StringUtils.isNotBlank(csrUsername) && StringUtils.isNotBlank(csrRole) ) {
    				
    				customParams.addProperty("AdminUserName",csrUsername);
    				customParams.addProperty("AdminUserRole",csrRole);
    				customParams.addProperty("isCSRAssist","1");
    			}
    		}
    	} catch (Exception exp) {
			
			alert.prepareError("Encountered exception while fetching userAttributes from identity(DataControllerRequest): "+exp).log();
		}
    }
    
    /*
     * This Method checks for Admin details if available.
     * And add the details in customParams to track the Audit Logs.
     */
    public static void addAdminUserNameRoleIfAvailable(JsonObject customParams, FabricRequestManager requestManager ){
    	
    	if(null == customParams || null == requestManager) {
    		return;
    	}
    	try {
			
    		Map<String, Object> userAttributes = requestManager.getServicesManager().getIdentityHandler().getUserAttributes();
    		if(null != userAttributes) {
    			
    			String csrUsername = userAttributes.get("CSR_Username") != null? (String) userAttributes.get("CSR_Username") : null  ;
    			String csrRole = userAttributes.get("CSR_Role") != null? (String) userAttributes.get("CSR_Role") : null  ;
    			if(StringUtils.isNotBlank(csrUsername) && StringUtils.isNotBlank(csrRole) ) {
    				
    				customParams.addProperty("AdminUserName",csrUsername);
    				customParams.addProperty("AdminUserRole",csrRole);
    				customParams.addProperty("isCSRAssist","1");
    			}
    		}
    	} catch (Exception exp) {
			
			alert.prepareError("Encountered exception while fetching userAttributes from identity(FabricRequestManager): "+exp).log();
		}
    }
}
