package com.kony.eventdispatcher.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.kony.eventdispatcher.dto.EventTriggerConfig;
import com.kony.utils.ErrorCodeEnum;
import com.kony.utils.HelperMethods;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.registry.AppRegistryException;

public class EventsDispatcherWrapper implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static void process(DataControllerRequest request, DataControllerResponse response, Result result) {
		if (EventsConfigurationHolder.getEventsConfiguration() == null) {
			EventsConfigurationHolder.updateEventsConfiguration();
		}
		Result res = new Result();
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		String[] feilds = className.split("\\.");
		className = null;
		if (feilds.length > 0)
			className = feilds[feilds.length - 1];
		if (StringUtils.isNotBlank(className) && className.contains("$")) {
			className = className.substring(0, className.indexOf("$"));
		}
		try {
			JsonObject customparams = fetchCustomParamsFromResult(result);
			String filter = EventTriggerConfigDispatcher.getFilterForEventConfiguration(request);
			JsonArray eventsConfiguration = EventTriggerConfigDispatcher.getEventConfigurationFromHolder(filter,
					className, res);
			if (res.hasParamByName(URLConstants.DBPERRMSG)) {
				alert.prepareError("Result " + ResultToJSON.convert(res)).log();
				return;
			}
			if (eventsConfiguration.isJsonNull() || eventsConfiguration.size() == 0) {
				alert.prepareError("No Configurations found for given serviceId").log();
				return;
			}
			String reportingParamsString = null;
			reportingParamsString = request.getHeader(URLConstants.XKONYREPORTINGPARAMS);
			if (StringUtils.isBlank(reportingParamsString)) {
				try {
					reportingParamsString = request.getServicesManager().getDeviceRequestData().getReportingParams();
				} catch (AppRegistryException e) {
					// do nothing
				}
			}
			diagnostic.prepareDebug("Reporting params in wrapper " + reportingParamsString).log();
			List<EventTriggerConfig> events = EventTriggerConfigDispatcher.initialiseObjectsAndGetEventRecords(
					eventsConfiguration, request, response, customparams, reportingParamsString, res);
			if (res.hasParamByName(URLConstants.DBPERRMSG)) {
				alert.prepareError("Result " + ResultToJSON.convert(res)).log();
				return;
			}
			callOrchService(events);
			HelperMethods.returnSuccess(res);
			return;
		} catch (Exception e) {
			alert.prepareError(URLConstants.EXCEPTION, e).log();
		}
		HelperMethods.result(res, ErrorCodeEnum.ERROR_EXCEPTION);
		alert.prepareError("Result " + ResultToJSON.convert(res)).log();

	}

	private static JsonObject fetchCustomParamsFromResult(Result result) {
		try {
			return new JsonParser().parse(ResultToJSON.convert(result)).getAsJsonObject();
		} catch (Exception e) {
			alert.prepareError("Error Occured while parsing result object").log();
		}
		return new JsonObject();

	}

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if (EventsConfigurationHolder.getEventsConfiguration() == null) {
			EventsConfigurationHolder.updateEventsConfiguration();
		}
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		String[] feilds = className.split("\\.");
		className = null;
		if (feilds.length > 0) {
			className = feilds[feilds.length - 1];
		}
		if (StringUtils.isNotBlank(className) && className.contains("$")) {
			className = className.substring(0, className.indexOf("$"));
		}
		Result res = new Result();
		try {
			String filter = EventTriggerConfigDispatcher.getFilterForEventConfiguration(fabricRequestManager);
			JsonArray eventsConfiguration = EventTriggerConfigDispatcher.getEventConfigurationFromHolder(filter,
					className, res);
			if (res.hasParamByName(URLConstants.DBPERRMSG)) {
				alert.prepareError("Result " + ResultToJSON.convert(res)).log();
				return true;
			}
			if (eventsConfiguration.isJsonNull() || eventsConfiguration.size() == 0) {
				alert.prepareError("No Configurations found for given serviceId").log();
				return true;
			}
			List<EventTriggerConfig> events = EventTriggerConfigDispatcher.initialiseObjectsAndGetEventRecords(
					eventsConfiguration, fabricRequestManager, fabricResponseManager, res);
			if (res.hasParamByName(URLConstants.DBPERRMSG)) {
				alert.prepareError("Result " + ResultToJSON.convert(res)).log();
				return true;
			}
			callOrchService(events);
			HelperMethods.returnSuccess(res);
			diagnostic.prepareDebug("Result " + ResultToJSON.convert(res)).log();
			return true;
		} catch (Exception e) {
			alert.prepareError(URLConstants.EXCEPTION, e).log();
		}
		HelperMethods.result(res, ErrorCodeEnum.ERROR_EXCEPTION);
		alert.prepareError("Result " + ResultToJSON.convert(res)).log();
		return true;
	}

	private static void callOrchService(List<EventTriggerConfig> events) {
		try {
			Map<String, Object> requestParameters = generateOrchPayload(events);
			HelperMethods.callInternalService(requestParameters, "EventDispatcher_Orch", "dispatchEvent", null);
		} catch (Exception e) {
			alert.prepareError("Error occured in calling service", e).log();
		}
	}

	private static Map<String, Object> generateOrchPayload(List<EventTriggerConfig> events) {
		Map<String, Object> requestParameters = new HashMap<>();
		StringBuilder finalevents = new StringBuilder();
		for (EventTriggerConfig event : events) {
			String eventstr = null;
			try {
				eventstr = JSONUtils.stringify(event);
			} catch (Exception e) {
				alert.prepareError("Error in parsing event object", e).log();
			}
			if (eventstr == null)
				continue;
			finalevents.append(eventstr + URLConstants.LOOPSEPARATORVAL);

		}
		requestParameters.put(URLConstants.INPUTEVENTS, finalevents.toString());
		requestParameters.put("loop_separator", URLConstants.LOOPSEPARATORVAL);
		requestParameters.put("loop_count", events.size());
		diagnostic.prepareDebug("inputparams for orch service " + finalevents).log();
		return requestParameters;
	}

}
