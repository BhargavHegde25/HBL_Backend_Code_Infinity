package com.kony.dbpalerts.alertsprocess;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsErrorMessages;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.kony.dbpalerts.dbconnectionutils.AccountLevelAlertInfo;
import com.kony.dbpalerts.dbconnectionutils.InitialDbProcess;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class PushAlert implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static String deriveToken(String events) throws Exception {
		String secret = AlertsUtils.getConfigProperty(AlertConstants.QUEUEMASTER_SHARED_SECRET);
		if (secret == null || secret.length() == 0) {
			return null;
		}
		String eventsHash = Hashing.sha512().hashString(events, StandardCharsets.UTF_8).toString(); // Hashing using
																									// Guava lib
		String saltedsecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedsecret, StandardCharsets.UTF_8).toString();
	}


	public boolean isTokenValid(String events, String token) throws Exception {
		String generatedtoken = deriveToken(events);
		if (generatedtoken != null)
			return generatedtoken.equals(token);
		return false;
	}

	public Object lognull(Result res, String text) {
		res.addParam(new Param(AlertConstants.SUCCESS, AlertConstants.TRUE, AlertConstants.STRING));
		if (text.equals(AlertConstants.EVENT)) {
			res.addParam(new Param(AlertConstants.DBPERRMSG, "Error in processing events", AlertConstants.STRING));
			return res;
		}
		res.addParam(new Param(AlertConstants.DBPERRMSG, "communication template are null", AlertConstants.STRING));
		return res;
	}

	private Result result(Result result, String successmsg, String dbperrmsg) {
		result.addParam(new Param(AlertConstants.SUCCESS, successmsg, AlertConstants.STRING));
		result.addParam(new Param(AlertConstants.DBPERRMSG, dbperrmsg, AlertConstants.STRING));
		return result;
	}

	private static synchronized void setAccountlevelFlag() {
		StaticDataHolder.setIsalertaccountLevel(AccountLevelAlertInfo.isAlertAccountLevel());

	}

	public Object invoke(String methodID, Object[] inputarray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		if (StaticDataHolder.getSchemaname() == null)
			StaticDataHolder.setSchemaname(AlertsUtils.getConfigProperty("DBX_SCHEMA_NAME"));
		if(StaticDataHolder.getExternalAlerts()==null)
			StaticDataHolder.setExternalAlerts(AlertsUtils.getConfigProperty("SEND_ALERTHISTORY_TO_EXTERNAL"));
		String alertConfigLevel = InitialDbProcess.getAlertLevel();
		if (alertConfigLevel == null) {
			diagnostic.prepareDebug("Alert level is not configured in customerviewalertconfiguration").log();
			return result(result, "true", "Alert level is not configured");
		}
		StaticDataHolder.verifyAndInitializePreprocessData();
		try {
			String events = null;
			String token = null;
			JsonElement eventselement;
			JsonArray eventsjsonarray = null;
			List<Event> eventsarray = null;
			ConcurrentMap<String, Map<String, JsonObject>> communicationdata = null;
			ConcurrentMap<String, String> custswitchdata = null;
			Map<String, String> inputparams = null;
			if (inputarray != null && inputarray.length > 1) {
				inputparams = (Map<String, String>) inputarray[1];
			}
			if (inputparams != null) {
				token = inputparams.get(AlertConstants.TOKEN);
				events = inputparams.get(AlertConstants.EVENTS);
			}
			if (events == null) {
				return result(result, AlertConstants.TRUE, AlertsErrorMessages.ERRMSG1);
			}
			if (token == null) {
				return result(result, AlertConstants.TRUE, AlertsErrorMessages.ERRMSG2);

			}
			if (!isTokenValid(events, token)) {
				return result(result, AlertConstants.TRUE, AlertsErrorMessages.ERRMSG3);
			}
			eventselement = new JsonParser().parse(events);
			if (!eventselement.isJsonArray()) {
				return result(result, AlertConstants.TRUE, AlertsErrorMessages.ERRMSG5);
			}
			eventsjsonarray = eventselement.getAsJsonArray();
			eventsarray = ProcessEvents.processAllEvents(eventsjsonarray);
			if (eventsarray == null || eventsarray.isEmpty())
				return lognull(result, AlertConstants.EVENT);
			
			if (StaticDataHolder.getIsalertaccountLevel() == null)
				setAccountlevelFlag();

			ProcessEvents.assignAccountLevelConf(eventsarray, StaticDataHolder.getIsalertaccountLevel());
			ProcessEvents.fillCustomerDetails(eventsarray);
			eventsarray = FetchChannelData.fetchAlertMasterData(eventsarray,alertConfigLevel);
			ProcessEvents.processprimaryData(eventsjsonarray,eventsarray);
			FetchChannelData.fetchEventSpecificDataConfig(eventsarray, StaticDataHolder.getIsalertaccountLevel(), alertConfigLevel);
			communicationdata = FetchCommunicationData.fetchCommunicationTemplateData(eventsarray);
			if (communicationdata == null)
				return lognull(result, AlertConstants.OTHER);
			StaticDataHolder.setGlobalcommunicationdata(communicationdata);
			custswitchdata = ProcessAlert.customerSwitchData(eventsarray);
			StaticDataHolder.setGlobalcustswitchdata(custswitchdata);
			callOrchService(eventsarray);
			clearResources();
			result.addParam(new Param(AlertConstants.SUCCESS, AlertConstants.TRUE, AlertConstants.STRING));
			result.addParam(new Param(AlertConstants.DBPERRMSG, "", AlertConstants.STRING));

		} catch (Exception e) {
			alert.prepareError("Exception occured:", e).log();
			return result(result, AlertConstants.TRUE, e.toString());
		}
		return result;
	}

	private static void clearResources() {
		if (StaticDataHolder.getGlobalcommunicationdata() != null)
			StaticDataHolder.getGlobalcommunicationdata().clear();
		if (StaticDataHolder.getGlobalcustswitchdata() != null)
			StaticDataHolder.getGlobalcustswitchdata().clear();
	}

	private void callOrchService(List<Event> events) {
		try {
			Map<String, Object> requestParameters = generateOrchPayload(events);
			AlertsUtils.callInternalService(requestParameters, AlertConstants.ALERTS_ORCH_SERVICE,
					AlertConstants.ALERTS_ORCH_OPERATION, null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in calling service", e).log();
		}
	}

	private static Map<String, Object> generateOrchPayload(List<Event> events) {
		Map<String, Object> requestParameters = new HashMap<>();
		StringBuilder finalevents = new StringBuilder();
		for (Event event : events) {
			String eventstr = null;
			try {
				eventstr = JSONUtils.stringify(event);
			} catch (Exception e) {
				diagnostic.prepareDebug("Error in parsing event object", e).log();
			}
			if (eventstr == null)
				continue;
			finalevents.append(eventstr + AlertConstants.ALERT_LOOP_SEPARATOR);
		}
		requestParameters.put(AlertConstants.INPUT_EVENTS, finalevents.toString());
		requestParameters.put(AlertConstants.LOOP_SEPARATOR, AlertConstants.ALERT_LOOP_SEPARATOR);
		requestParameters.put(AlertConstants.LOOP_COUNT, events.size());
		return requestParameters;
	}
}