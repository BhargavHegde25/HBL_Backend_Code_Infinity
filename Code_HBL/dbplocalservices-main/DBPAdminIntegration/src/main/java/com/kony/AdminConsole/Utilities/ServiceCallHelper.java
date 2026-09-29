package com.kony.AdminConsole.Utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.utils.ConvertJsonToResult;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class ServiceCallHelper {
    private static final String SEPARATOR = ":";

    private ServiceCallHelper() {
    }

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    public static String getObjectName(String serviceURL) {
        String objectName = StringUtils.substringBetween(serviceURL, SEPARATOR, SEPARATOR);
        return StringUtils.isBlank(objectName) ? null : objectName;
    }

    public static String getServiceName(String serviceURL) {
        return StringUtils.substringBefore(serviceURL, SEPARATOR);
    }

    public static String getOperationName(String serviceURL, DataControllerRequest dcRequest) {
        String operationName = StringUtils.substringAfterLast(serviceURL, SEPARATOR);
        if (operationName.contains("{schema_name}_")) {
            operationName = operationName.replace("{schema_name}", getDatabaseSchemaName(dcRequest));
        }
        return StringUtils.isBlank(operationName) ? null : operationName;
    }

    public static String getOperationName(String serviceURL, FabricRequestManager requestManager) {
        String operationName = StringUtils.substringAfterLast(serviceURL, SEPARATOR);
        if (operationName.contains("{schema_name}_")) {
            operationName = operationName.replace("{schema_name}", getDatabaseSchemaName(requestManager));
        }
        return StringUtils.isBlank(operationName) ? null : operationName;
    }

    private static CharSequence getDatabaseSchemaName(DataControllerRequest dcRequest) {
        return ServiceConfig.getValueFromRunTime(URLConstants.SCHEMA_NAME, dcRequest);
    }

    private static CharSequence getDatabaseSchemaName(FabricRequestManager requestManager) {
        return ServiceConfig.getValueFromRunTime(URLConstants.SCHEMA_NAME, requestManager);
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

    public static JsonObject invokeServiceAndGetJson(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        String serviceURL = ServiceConfig.getValue(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL, dcRequest);
        String objectName = getObjectName(serviceURL);

        try {
            String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName,
                    operationName, inputParams, headerParams, dcRequest);
            JsonParser parser = new JsonParser();
            return parser.parse(responseString).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(serviceURL);
    }

    public static String invokeServiceAndGetString(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        String serviceURL = ServiceConfig.getValue(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL, dcRequest);
        String objectName = getObjectName(serviceURL);

        try {
            return DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, objectName, operationName,
                    inputParams, headerParams, dcRequest);
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return "";
    }

    public static Result invokeServiceAndGetResult(DataControllerRequest dcRequest, Map<String, Object> inputParams,
            Map<String, Object> headerParams, String url) {
        String serviceURL = ServiceConfig.getValue(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL, dcRequest);
        String objectName = getObjectName(serviceURL);

        try {
            return DBPServiceInvocationWrapper.invokeServiceAndGetResult(serviceName, objectName, operationName,
                    inputParams, headerParams, dcRequest);
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return getExceptionMsgAsResult(url);
    }

    private static Map<String, Object> convertMap(Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Entry<String, String> entry : map.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    public static Result invokeServiceAndGetResult(FabricRequestManager requestManager, Map<String, Object> inputParams,
            Map<String, String> headerParams, String url) {
        String serviceURL = ServiceConfig.getValue(url);
        String serviceName = getServiceName(serviceURL);
        String operationName = getOperationName(serviceURL, requestManager);
        String objectName = getObjectName(serviceURL);

        try {
            return DBPServiceInvocationWrapper.invokeServiceAndGetResult(serviceName, objectName, operationName,
                    inputParams, convertMap(headerParams), requestManager);
        } catch (Exception e) {
            alert.prepareError("Exception while calling service " + serviceURL, e).log();
        }
        return getExceptionMsgAsResult(url);
    }

    public static String invokePassThroughServiceAndGetString(DataControllerRequest dcRequest,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url) {
        String serviceURL = ServiceConfig.getValue(url);
        try {
            String serviceName = getServiceName(serviceURL);
            String operationName = getOperationName(serviceURL, dcRequest);
            String objectName = getObjectName(serviceURL);
            return DBPServiceInvocationWrapper.invokePassThroughServiceAndGetString(serviceName, objectName,
                    operationName, inputParams, headerParams, dcRequest);
        } catch (Exception e) {
            alert.prepareError("Exception while calling :" + serviceURL, e).log();
        }
        return getExceptionMsgAsJson(url).toString();
    }

    public static Result invokePassThroughServiceAndGetResult(DataControllerRequest dcRequest,
            Map<String, Object> inputParams, Map<String, Object> headerParams, String url) {
        Result response = null;
        String serviceURL = ServiceConfig.getValue(url);
        try {
            String responseJson = invokePassThroughServiceAndGetString(dcRequest, inputParams, headerParams, url);
            response = ConvertJsonToResult.convert(responseJson);
        } catch (Exception e) {
            alert.prepareError("Exception while calling :" + serviceURL, e).log();
            response = getExceptionMsgAsResult(url);
        }
        return response;
    }

}