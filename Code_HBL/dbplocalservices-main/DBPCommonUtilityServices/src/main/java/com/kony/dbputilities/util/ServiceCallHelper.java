package com.kony.dbputilities.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class ServiceCallHelper {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final String SEPARATOR = "/";

    private ServiceCallHelper() {
    }

    public static Result invokeServiceAndGetResult(FabricRequestManager requestManager, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(requestManager, serviceURL);
        String objectName = getObjectName(serviceURL);

        try {
            Result result =
                    DBPServiceInvocationWrapper.invokeServiceAndGetResult(serviceName, objectName, operationName,
                            inputParams, headerParams, requestManager);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, result, null);
            return result;
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + URLFinder.getPathUrl(url), e).log();
        }
        return getExceptionMsgAsResult(url);
    }

    public static JsonObject invokeServiceAndGetJson(FabricRequestManager requestManager,
            @SuppressWarnings("rawtypes") Map inputParams, Map<String, String> headerParams, String url) {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(requestManager, serviceURL);
        String objectName = getObjectName(serviceURL);

        try {
            @SuppressWarnings("unchecked")
            String json = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, convertMap(headerParams), requestManager);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url);
    }

    public static JsonObject invokeServiceAndGetJson(Map<String, Object> inputParams, Map<String, Object> headerParams,
            String url) {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL);
        String objectName = getObjectName(serviceURL);

        try {
            String json = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, headerParams, StringUtils.EMPTY);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url);
    }

    public static JsonObject invokeServiceAndGetJson(String serviceName, String objectName, String operationName,
            Map<String, Object> inputParams, Map<String, Object> headerParams) {
        try {
            String json = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, headerParams, StringUtils.EMPTY);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceName + "/" + objectName + "/" + operationName, e).log();
        }
        return getExceptionMsgAsJson(serviceName + "/" + objectName + "/" + operationName);
    }
    
    public static JsonObject invokeServiceAndGetJson(String serviceName, String objectName, String operationName,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String konyAuthToken) {
        try {
            String json = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, headerParams, konyAuthToken);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceName + "/" + objectName + "/" + operationName, e).log();
        }
        return getExceptionMsgAsJson(serviceName + "/" + objectName + "/" + operationName);
    }

    public static JsonObject invokeServiceAndGetJson(Map<String, Object> inputParams, Map<String, Object> headerParams,
            String url, String konyAuthToken) {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL);
        String objectName = getObjectName(serviceURL);

        try {
            String json = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, headerParams, konyAuthToken);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url);
    }

    public static String invokeServiceAndGetString(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL, dcRequest);
        String objectName = getObjectName(serviceURL);
        String str = null;
        try {
            str = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, convertMap(headerParams), dcRequest);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, str);
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + URLFinder.getPathUrl(url), e).log();
        }
        return str;
    }
    
    public static String invokeServiceAndGetString(Map<String, Object> inputParams,
            Map<String, String> headerParams, String url, String token) throws HttpCallException {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL);
        String objectName = getObjectName(serviceURL);
        String str = null;
        try {
        	
        	str = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withOperationId(operationName)
					.withFabricAuthToken(token).build().getResponse();
        	
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, str);
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + URLFinder.getPathUrl(url), e).log();
        }
        return str;
    }
    

    public static Result invokeServiceAndGetResult(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {
        String serviceURL = URLFinder.getPathUrl(url);
        if(StringUtils.isBlank(serviceURL)) {
            serviceURL = url;
        }
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL, dcRequest);
        String objectName = getObjectName(serviceURL);
        try {
            Result result =
                    DBPServiceInvocationWrapper.invokeServiceAndGetResult(serviceName, objectName, operationName,
                            inputParams, convertMap(headerParams), dcRequest);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, result, null);
            return result;
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + URLFinder.getPathUrl(url), e).log();
        }
        return getExceptionMsgAsResult(url);
    }

    public static Result invokeServiceAndGetResult(Map<String, Object> inputParams,
            Map<String, String> headerParams, String url, String token) throws HttpCallException {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL);
        String objectName = getObjectName(serviceURL);
        try {
            Result result =
                    DBPServiceInvocationWrapper.invokeServiceAndGetResult(serviceName, objectName, operationName,
                            inputParams, convertMap(headerParams), token);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, result, null);
            return result;
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + URLFinder.getPathUrl(url), e).log();
        }
        return getExceptionMsgAsResult(url);
    }

    public static Map<String, Object> convertMap(Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        if (map == null || map.isEmpty()) {
            return resultMap;
        }
        for (Entry<String, String> entry : map.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    public static JsonObject invokeServiceAndGetJson(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) throws HttpCallException {
        String serviceURL = URLFinder.getPathUrl(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL, dcRequest);
        String objectName = getObjectName(serviceURL);

        try {
            String json = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, headerParams, dcRequest);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url);
    }

    public static JsonObject invokePassThroughServiceAndGetJson(DataControllerRequest dcRequest,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url) {
        String serviceURL = URLFinder.getPathUrl(url);
        try {
            String serviceName = getServiceName(serviceURL);
            String operationName = getOperationName(serviceURL, dcRequest);
            String objectName = getObjectName(serviceURL);
            String json = DBPServiceInvocationWrapper.invokePassThroughServiceAndGetString(serviceName, objectName,
                    operationName, inputParams, headerParams, dcRequest);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling :" + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url);
    }
    public static JsonObject invokePassThroughServiceAndGetJson(
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url ,String konyauthtoken) {
        String serviceURL = URLFinder.getPathUrl(url);
        try {
            String serviceName = getServiceName(serviceURL);
            String operationName = getOperationName(serviceURL);
            String objectName = getObjectName(serviceURL);
            String json = DBPServiceInvocationWrapper.invokePassThroughServiceAndGetString(serviceName, objectName,
                    operationName, inputParams, headerParams, konyauthtoken);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling :" + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url);
    }
    
    public static JsonObject invokePassThroughServiceAndGetJson(FabricRequestManager requestManager,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url) {
        String serviceURL = URLFinder.getPathUrl(url);
        try {
            String serviceName = getServiceName(serviceURL);
            String operationName = getOperationName(requestManager, serviceURL);
            String objectName = getObjectName(serviceURL);
            String json = DBPServiceInvocationWrapper.invokePassThroughServiceAndGetString(serviceName, objectName,
                    operationName, inputParams, headerParams, requestManager);
            printLog(objectName + "_" + serviceName + "_" + operationName, inputParams, headerParams, null, json);
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling :" + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url);
    }

    public static Result invokePassThroughServiceAndGetResult(DataControllerRequest dcRequest,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url) {
        Result response = null;
        String serviceURL = URLFinder.getPathUrl(url);
        try {
            JsonObject responseJson = invokePassThroughServiceAndGetJson(dcRequest, inputParams, headerParams, url);
            response = ConvertJsonToResult.convert(responseJson);
        } catch (Exception e) {
            alert.prepareError("Exception while calling :" + serviceURL, e).log();
            response = getExceptionMsgAsResult(url);
        }
        return response;
    }

    public static Result invokePassThroughServiceAndGetResult(FabricRequestManager requestManager,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url) {
        Result response = null;
        String serviceURL = URLFinder.getPathUrl(url);
        try {
            JsonObject responseJson = invokePassThroughServiceAndGetJson(requestManager, inputParams, headerParams,
                    url);
            response = ConvertJsonToResult.convert(responseJson);
        } catch (Exception e) {
            alert.prepareError("Exception while calling :" + serviceURL, e).log();
            response = getExceptionMsgAsResult(url);
        }
        return response;
    }

    public static String getObjectName(String serviceURL) {
        String basePart = "/services/data/v1/";
        if (serviceURL.contains(basePart)) {
            int start = 0;
            if (serviceURL.contains("/operations/")) {
                start = serviceURL.indexOf("/operations/") + "/operations/".length();
            } else {
                start = serviceURL.indexOf("/objects/") + "/objects/".length();
            }
            return serviceURL.substring(start, serviceURL.lastIndexOf("/"));
        }
        return null;
    }

    public static String getServiceName(String serviceURL) {
        String basePart = "/services/data/v1/";
        if (serviceURL.contains(basePart)) {
            return serviceURL.substring(basePart.length(), serviceURL.indexOf("/", basePart.length()));
        }
        return serviceURL.substring(serviceURL.indexOf("/", 1) + 1, serviceURL.lastIndexOf("/"));
    }

    public static String getOperationName(String serviceURL) {
        String operationName = StringUtils.substringAfterLast(serviceURL, SEPARATOR);
        if (operationName.contains("{schema_name}_")) {
            operationName = operationName.replace("{schema_name}", getDatabaseSchemaName(URLConstants.SCHEMA_NAME));
        }
        if (operationName.contains("{log_schema_name}_")) {
            operationName =
                    operationName.replace("{log_schema_name}", getDatabaseSchemaName(URLConstants.LOG_SCHEMA_NAME));
        }
        return StringUtils.isBlank(operationName) ? null : operationName;
    }

    public static String getOperationName(String serviceURL, DataControllerRequest dcRequest) {
        String operationName = StringUtils.substringAfterLast(serviceURL, SEPARATOR);
        if (operationName.contains("{schema_name}_")) {
            operationName = operationName.replace("{schema_name}", getDatabaseSchemaName(dcRequest));
        }
        return StringUtils.isBlank(operationName) ? null : operationName;
    }

    private static String getOperationName(FabricRequestManager requestManager, String serviceURL) {
        String operationName = StringUtils.substringAfterLast(serviceURL, SEPARATOR);
        if (operationName.contains("{schema_name}_")) {
            operationName = operationName.replace("{schema_name}", getDatabaseSchemaName(requestManager));
        }
        return StringUtils.isBlank(operationName) ? null : operationName;
    }

    private static CharSequence getDatabaseSchemaName(FabricRequestManager requestManager) {
        return URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, requestManager);
    }

    private static CharSequence getDatabaseSchemaName(DataControllerRequest dcRequest) {
        return URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest);
    }

    private static CharSequence getDatabaseSchemaName(String operationName) {
        if (operationName != null)
            return EnvironmentConfigurationsHandler.getValue(operationName);
        return EnvironmentConfigurationsHandler.getValue(URLConstants.SCHEMA_NAME);
    }

    private static JsonObject getExceptionMsgAsJson(String serviceURL) {
        JsonObject errResponse = new JsonObject();
        StringBuilder message = new StringBuilder();
        message.append("Exception occured while invoking service with [ServiceId_ObjectId_OperationId] [")
                .append(serviceURL).append("]");
        errResponse.addProperty("errmsg", message.toString());
        return errResponse;
    }

    private static Result getExceptionMsgAsResult(String serviceURL) {
        Result result = new Result();
        StringBuilder message = new StringBuilder();
        message.append("Exception occured while invoking service with [ServiceId_ObjectId_OperationId] [")
                .append(serviceURL).append("]");
        result.addParam("errmsg", message.toString());
        return result;
    }

    private static void printLog(String URL, Map inputParams, Map headerParams, Result result, String response) {

        if (diagnostic == null) {
            diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        }
        if (inputParams != null) {
            diagnostic.prepareDebug("InputParams for call " + URL + " : " + inputParams).log();
        }
        if (headerParams != null) {
            diagnostic.prepareDebug("HeaderParams for call " + URL + " : " + headerParams).log();
        }
        if (result != null) {
            diagnostic.prepareDebug("Response from call " + URL + " : " + ResultToJSON.convert(result)).log();
        } else {
            diagnostic.prepareDebug("Response from call " + URL + " : " + response).log();
        }
    }
}