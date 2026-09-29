package com.kony.objectserviceutils;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class EventsDispatcher1 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static Result callQueueMaster(ServicesManager servicesManager, Map<String, Object> inputMap,
            Map<String, Object> headerMap) {
        Result result = new Result();

        try {
            OperationData operationData = servicesManager.getOperationDataBuilder().withServiceId("QueueMaster")
                    .withOperationId("PushEventQueue").build();

            ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withInputs(inputMap)
                    .withHeaders(headerMap).build();
            result = serviceRequest.invokeServiceAndGetResult();
        } catch (AppRegistryException arex) {
            result = getErrorResult(1012, "Could not access QueueManager service via app registry");
        } catch (Exception ex) {
            result = getErrorResult(ex);
        }

        return result;
    }

    public static Result dispatch(FabricRequestManager requestManager, FabricResponseManager responseManager,
            String eventType, String eventSubType, String producer, String StatusId, String Account, String customerId,
            JsonObject customParams, String appid) {
        Result result = new Result();

        JsonObject requestData = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        JsonObject responseData = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        // created Objects to store eventdata , eventDetails and otherData
        JsonObject eventData = new JsonObject();
        JsonObject eventDetails = new JsonObject();
        JsonObject otherData = new JsonObject();
        String reportingParamsString = "";

        try {
            reportingParamsString = requestManager.getHeadersHandler().getHeader("X-Kony-ReportingParams");
            eventDetails.addProperty("session", reportingParamsString);

        } catch (Exception e) {
            alert.prepareError("ReportingParamException", e).log();
        }

        eventData.add("requestInput", requestData);
        eventData.add("responseOutput", responseData);
        eventData.add("customParams", customParams);

        otherData.addProperty("customerId", customerId);
        if (Account != null) {
            otherData.addProperty("accountnumber", Account);
        }
        otherData.addProperty("appid", appid);

        eventDetails.addProperty("eventType", eventType);
        eventDetails.addProperty("eventSubType", eventSubType);
        eventDetails.addProperty("status", StatusId);
        eventDetails.add("otherData", otherData);
        eventDetails.add("eventData", eventData);

        JsonArray events = new JsonArray();

        events.add(eventDetails);

        Map<String, Object> inputMap = new HashMap();
        Map<String, Object> headerMap = new HashMap();

        inputMap.put("events", events);
        inputMap.put("producer", producer);

        ServicesManager servicesManager = requestManager.getServicesManager();
        String eventsString = events.toString();
        String derivedToken = deriveToken(servicesManager, eventsString);
        inputMap.put("token", derivedToken);
        result = callQueueMaster(servicesManager, inputMap, headerMap);
        alert.prepareError("dispatched the code").log();
        return result;
    }

    private static Result getErrorResult(Throwable ex) {
        String errorMsg = ex.getMessage();
        if (errorMsg == null) {
            StackTraceElement ste = ex.getStackTrace()[0];
            errorMsg = ex.getClass().getName() + "' thrown in " + ste.getClassName() + "." + ste.getMethodName();
        }
        return getErrorResult(7777, errorMsg);
    }

    private static Result getErrorResult(int errorNumber, String errorMsg) {
        Result result = new Result();
        result.addParam(new Param("errornumber", Integer.toString(errorNumber)));
        result.addParam(new Param("errormsg", errorMsg));
        result.addParam(new Param("success", "false"));
        return result;
    }

    public static JsonObject getJsonObjectFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = getElementFromJsonObject(object, key, required);
        if (element == null) {
            return null;
        }
        if (!element.isJsonObject()) {
            throw new IllegalArgumentException("Value for attribute '" + key + "' is not a JSON object");
        }
        return element.getAsJsonObject();
    }

    public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = getElementFromJsonObject(object, key, required);
        return element == null ? null : element.getAsString();
    }

    public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = object.get(key);
        if ((element == null) && (required)) {
            throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
        }
        return element;
    }

    public static String deriveToken(ServicesManager servicesManager, String events) {
        ConfigurableParametersHelper configHelper = servicesManager.getConfigurableParametersHelper();
        String secret = configHelper.getServerProperty("QUEUEMASTER_SHARED_SECRET");
        if (secret == null || secret.length() == 0) {
            throw new RuntimeException("QueueMaster shared secret has not been configured!");
        }
        String eventsHash = Hashing.sha512().hashString(events, Charsets.UTF_8).toString(); // Hashing using Guava lib
        String saltedSecret = eventsHash + secret;
        return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
    }

}
