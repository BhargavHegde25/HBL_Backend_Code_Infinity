package com.kony.audit.auditprocess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kony.audit.auditutils.AuditConstants;
import com.kony.audit.auditutils.AuditDBConstants;
import com.kony.audit.auditutils.AuditErrorMessages;
import com.kony.audit.auditutils.AuditUtils;
import com.kony.audit.auditutils.Event;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class AuditEvent implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {

			if (AuditUtils.getMainSchemaname() == null)
				AuditUtils.setMainSchemaname(AuditUtils.getConfigProperty("DBX_SCHEMA_NAME"));

			if (AuditUtils.getLogSchemaname() == null)
				AuditUtils.setLogSchemaname(AuditUtils.getConfigProperty("LOG_SCHEMA_NAME"));
			PreProcessStaticData.preProcessStaticData();
			Map<String, String> inputParams = null;
			String events = null;
			String token = null;
			JsonElement eventsElement;
			JsonArray eventsjsonarray = null;
			List<Event> eventsobjlist = null;

			if (inputArray != null && inputArray.length > 1) {
				inputParams = (Map<String, String>) inputArray[1];
			}

			if (inputParams != null) {
				token = inputParams.get(AuditConstants.TOKEN);
				events = inputParams.get(AuditConstants.EVENTS);
			}
			if (events == null) {
				return AuditUtils.returnResult(true, AuditErrorMessages.C_EVENT_MISSING);
			}
			if (token == null) {
				return AuditUtils.returnResult(true, AuditErrorMessages.C_TOKEN_MISSING);
			}
			if (!AuditUtils.isTokenValid(events, token)) {
				return AuditUtils.returnResult(true, AuditErrorMessages.C_AUTH_FAIL);
			}
			if ((eventsElement = AuditUtils.parseString(events)) == null) {
				return AuditUtils.returnResult(true, AuditErrorMessages.C_JSON_PARSE_ERROR);
			}
			if (!eventsElement.isJsonArray()) {
				return AuditUtils.returnResult(true, AuditErrorMessages.C_NOT_JSON_ERROR);
			}
			eventsjsonarray = eventsElement.getAsJsonArray();
			eventsobjlist = PreProcessEvent.preProcess(eventsjsonarray);
			if (eventsobjlist.isEmpty()) {
				return AuditUtils.returnResult(true, AuditErrorMessages.C_EMPTY_EVENT);
			}
			callOrchService(eventsobjlist);
			return AuditUtils.returnResult(true, "");

		} catch (Exception e) {
			alert.prepareError("Exception occured:", e).log();
			return AuditUtils.returnResult(true, e.toString());
		}
	}

	private static Map<String, Object> generatePayload(List<Event> events) {
		Map<String, Object> requestParameters = new HashMap<>();

		StringBuilder jsonelem = new StringBuilder();
		StringBuilder customerids = new StringBuilder();
		StringBuilder usernames = new StringBuilder();
		StringBuilder accountids = new StringBuilder();
		StringBuilder corecustids = new StringBuilder();
		int eventcount = 0;
		for (Event event : events) {
			if (event.getJsonElement() != null) {
				jsonelem.append(event.getJsonElement()).append(AuditConstants.AUDIT_LOOP_SEPARATOR);
				customerids.append(event.getCustomerId()).append(AuditConstants.AUDIT_LOOP_SEPARATOR);
				usernames.append(event.getUserName()).append(AuditConstants.AUDIT_LOOP_SEPARATOR);
				if (event.getaccountId() == null)
					event.setaccountId(AuditConstants.NULL_STRING);
				accountids.append(event.getaccountId()).append(AuditConstants.AUDIT_LOOP_SEPARATOR);
				if (event.getCorecustomerid() == null)
					event.setCorecustomerid(AuditConstants.NULL_STRING);
				corecustids.append(event.getCorecustomerid()).append(AuditConstants.AUDIT_LOOP_SEPARATOR);
				eventcount++;
			}
		}
		requestParameters.put(AuditConstants.SERVICE_EVENTJSON, jsonelem);
		requestParameters.put(AuditConstants.SERVICE_CUSTOMERID, customerids);
		requestParameters.put(AuditConstants.SERVICE_USERNAME, usernames);
		requestParameters.put(AuditConstants.SERVICE_ACCOUNTID, accountids);
		requestParameters.put(AuditConstants.SERVICE_CORECUSTOMERID, corecustids);
		requestParameters.put(AuditConstants.LOOP_COUNT, eventcount);
		requestParameters.put(AuditConstants.LOOP_SEPARATOR, AuditConstants.AUDIT_LOOP_SEPARATOR);

		return requestParameters;

	}

	public static void callOrchService(List<Event> events) {

		Map<String, Object> requestParameters = generatePayload(events);
		try {
			AuditUtils.callInternalService(requestParameters, AuditDBConstants.AUDIT_EVENT_ORCH,
					AuditDBConstants.AUDIT_EVENT_ORCH_OPERATION, null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
	}

}
