package com.kony.eventdispatcher.wrapper;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.eventdispatcher.dto.EventTriggerConfig;
import com.kony.eventdispatcher.operations.AddConfigToEvent;
import com.kony.eventdispatcher.operations.CsrOperations;
import com.kony.eventdispatcher.operations.IdentityOperations;
import com.kony.eventdispatcher.operations.IntegrationServiceEventsDispatcherOperations;
import com.kony.eventdispatcher.operations.ObjectServiceEventsDispatcherOperations;
import com.kony.eventdispatcher.operations.ReportingParamsOperations;
import com.kony.utils.ErrorCodeEnum;
import com.kony.utils.HelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class EventTriggerConfigDispatcher {

	private EventTriggerConfigDispatcher() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	static List<EventTriggerConfig> initialiseObjectsAndGetEventRecords(JsonArray eventsConfiguration,
			FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager, Result res) {

		JsonObject requestObject = null;
		try {
			requestObject = fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}
		JsonObject responseObject = null;
		try {
			responseObject = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}
		JsonObject otherData = new JsonObject();
		JsonObject customParams = new JsonObject();
		initialiseJsonObjects(otherData, customParams, fabricRequestManager);
		JsonObject reportingParams = ReportingParamsOperations.getReportingParams(fabricRequestManager);
		String customerId = IdentityOperations.getCustomerIdFromSession(fabricRequestManager);
		return AddConfigToEvent.addConfigurationToEvent(eventsConfiguration, otherData, customParams, reportingParams,
				requestObject, responseObject, customerId, res);
	}

	static String getFilterForEventConfiguration(FabricRequestManager fabricRequestManager) {
		OperationData operationData;
		try {
			operationData = fabricRequestManager.getServicesManager().getOperationData();
			String objectId = operationData.getObjectId();
			String serviceId = operationData.getServiceId();
			String operationId = operationData.getOperationId();
			if (StringUtils.isBlank(serviceId) || StringUtils.isBlank(operationId))
				return null;
			return getFilter(objectId, serviceId, operationId);
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}

	public static JsonArray getEventConfigurationFromHolder(String filter, String className, Result res) {
		diagnostic.prepareDebug("filter " + filter).log();
		diagnostic.prepareDebug("classname " + className).log();
		if (StringUtils.isBlank(filter) || StringUtils.isBlank(className)) {
			HelperMethods.result(res, ErrorCodeEnum.ERROR_INVALIDFILTER);
			return new JsonArray();
		}
		
		JsonArray resultantArray = new JsonArray();
		JsonArray eventsConfigArray = EventsConfigurationHolder.getEventsConfiguration();
		diagnostic.prepareDebug("eventsConfigArray " + eventsConfigArray).log();
		try {
			for (JsonElement e : eventsConfigArray) {
				JsonObject obj = e.getAsJsonObject();
				if (obj.get("service").getAsString().equals(filter)
						&& obj.get("classname").getAsString().equals(className)) {
					resultantArray.add(obj);
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}
		return resultantArray;

	}

	private static String getFilter(String objectId, String serviceId, String operationId) {
		String filter = "";
		if (StringUtils.isNotBlank(objectId))
			filter = filter + objectId + "-";
		if (StringUtils.isNotBlank(serviceId) && StringUtils.isNotBlank(operationId))
			filter = filter + serviceId + "-" + operationId;
		return filter;
	}

	static String getFilterForEventConfiguration(DataControllerRequest request) {

		try {
			OperationData operationData = request.getServicesManager().getOperationData();
			String objectId = operationData.getObjectId();
			String serviceId = operationData.getServiceId();
			String operationId = operationData.getOperationId();
			if (serviceId == null || operationId == null)
				return null;
			return getFilter(objectId, serviceId, operationId);
		} catch (AppRegistryException e) {
			diagnostic.prepareDebug(e.toString()).log();

		}
		return null;
	}

	public static void initialiseJsonObjects(JsonObject otherData, JsonObject customParams,
			FabricRequestManager fabricRequestManager) {
		CsrOperations.getCSRrelatedParams(fabricRequestManager, otherData);
		ObjectServiceEventsDispatcherOperations.setSessionId(fabricRequestManager, otherData);
		ObjectServiceEventsDispatcherOperations.setAppSessionId(fabricRequestManager, customParams);

	}

	public static List<EventTriggerConfig> initialiseObjectsAndGetEventRecords(JsonArray eventsConfiguration,
			DataControllerRequest request, DataControllerResponse response, JsonObject customparams,
			String reportingParamsString, Result res) {
		JsonObject requestObj = new JsonObject();
		JsonObject responseObj = new JsonObject();
		JsonObject otherData = new JsonObject();
		EventTriggerConfigDispatcher.initialiseJsonObjects(otherData, customparams, requestObj, responseObj, request,
				response);
		JsonObject reportingParams = ReportingParamsOperations.getReportingParams(request, reportingParamsString);
		String customerId = IdentityOperations.getCustomerIdFromSession(request);

		return AddConfigToEvent.addConfigurationToEvent(eventsConfiguration, otherData, customparams, reportingParams,
				requestObj, responseObj, customerId, res);
	}

	private static void initialiseJsonObjects(JsonObject otherData, JsonObject customParams, JsonObject requestObj,
			JsonObject responseObj, DataControllerRequest request, DataControllerResponse response) {
		IntegrationServiceEventsDispatcherOperations.convertDcRequestToJson(request, requestObj);
		IntegrationServiceEventsDispatcherOperations.convertDcResponseToJson(response, responseObj);
		CsrOperations.getCSRrelatedParams(customParams, otherData);
		IntegrationServiceEventsDispatcherOperations.setAppSessionId(request, customParams);

	}

}
