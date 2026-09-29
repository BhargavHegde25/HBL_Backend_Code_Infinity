package com.kony.dbp.batchprocessingalerts;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.batchprocessingengine.helper.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class AlertsSubscribers implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static Map<String, JsonArray> getAlertTypesFromDB() {
		Map<String, JsonArray> alertdefinitiontablemap = new HashMap<>();

		try {
			String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(Constants.BPDBSERVICE, null,
					Constants.BATCHALERTDEFGET, new HashMap<String, Object>(), new HashMap<String, Object>(), "");
			JsonParser parser = new JsonParser();
			JsonArray recordsArray = parser.parse(responseString).getAsJsonObject()
					.getAsJsonArray("batchalertdefinition");
			for (JsonElement record : recordsArray) {
				JsonObject res = record.getAsJsonObject();
				try {
					if (res.get(Constants.OBJECTTYPE) == null)
						continue;
					if (!alertdefinitiontablemap.containsKey(res.get(Constants.OBJECTTYPE).getAsString()))
						alertdefinitiontablemap.put(res.get(Constants.OBJECTTYPE).getAsString(), new JsonArray());
					JsonArray current = alertdefinitiontablemap.get(res.get(Constants.OBJECTTYPE).getAsString());
					JsonObject curobj = getAlertDefinitionColumnValues(res);
					current.add(curobj);
					alertdefinitiontablemap.put(res.get(Constants.OBJECTTYPE).getAsString(), current);
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
			}

		} catch (Exception e) {
			alert.prepareError("Exception occurred", e).log();
		}
		return alertdefinitiontablemap;
	}

	private static JsonObject getAlertDefinitionColumnValues(JsonObject res) {
		JsonObject curobj = new JsonObject();
		Stream<String> stream = Stream.of(Constants.ALERTTYPECOLUMN, Constants.OBJECTTYPE, Constants.COLUMNNAMEDB,
				Constants.CONDITIONCOL, Constants.VALUECOL, Constants.CHECKTYPE, Constants.PARAMETERCOLUMNNAME);
		stream.forEach(column -> {
			if (res.has(column))
				curobj.addProperty(column, res.get(column).getAsString());
		});
		return curobj;
	}

	public boolean isTokenValid(String events, String token, DataControllerRequest dcrequest) throws Exception {
		String generatedtoken = HelperMethods.deriveToken(dcrequest.getServicesManager(), events);
		if (generatedtoken != null)
			return generatedtoken.equals(token);
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodid, Object[] inputarray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		try {
			String events = null;
			String token = null;
			JsonElement eventselement;
			JsonArray eventsjsonarray = null;
			JsonArray eventsarray = null;
			Map<String, String> inputparams = null;
			if (inputarray != null && inputarray.length > 1) {
				inputparams = (Map<String, String>) inputarray[1];
			}
			if (inputparams != null) {
				token = inputparams.get(Constants.TOKEN);
				events = inputparams.get(Constants.EVENTS);
			}
			if (events == null) {
				return result(result, Constants.TRUE, "events payload is null");
			}

			if (token == null)
				return result(result, Constants.TRUE, "invalid token");
			if (!isTokenValid(events, token, request)) {
				return result(result, Constants.TRUE, "invalid token");
			}

			eventselement = new JsonParser().parse(events);
			if (!eventselement.isJsonArray()) {
				return result(result, Constants.TRUE, "err2");
			}

			eventsjsonarray = eventselement.getAsJsonArray();
			eventsarray = ProcessEvents.processAllEvents(eventsjsonarray, request);

			if (eventsarray == null)
				return lognull(result, Constants.EVENT);

			JsonArray finaleventsarray = new JsonArray();
			finaleventsarray = getfinaleventsarray(eventsarray);
			diagnostic.prepareDebug("finaleventsarray " + finaleventsarray).log();

			result = ProcessEvents.processBatchAndCallQueueMaster(finaleventsarray, request.getServicesManager());
			result.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
			result.addParam(new Param(Constants.DBPERRMSG, "", Constants.STRING));
		} catch (Exception e) {
			alert.prepareError("Exception occured:", e).log();
			return result(result, Constants.TRUE, e.toString());
		}
		return result;
	}

	private JsonArray getfinaleventsarray(JsonArray eventsarray) {
		JsonArray finaleventsarray = new JsonArray();
		try {
			Map<String, Object> inputmap = new HashMap<>();
			inputmap.put(Constants.EVENTS, eventsarray);
			String responseString = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(Constants.PROCESSEVENTSSERVICE,
					null, Constants.PROCESSEVENTSSOPER, inputmap, new HashMap<String, Object>(), "");
			JsonParser parser = new JsonParser();
			finaleventsarray = parser.parse(responseString).getAsJsonObject().getAsJsonArray("events");
		} catch (Exception e) {
			finaleventsarray = eventsarray;
		}
		if (finaleventsarray == null || !finaleventsarray.isJsonArray()) {
			finaleventsarray = eventsarray;
		}
		return finaleventsarray;
	}

	private Result result(Result result, String successmsg, String dbperrmsg) {
		result.addParam(new Param(Constants.SUCCESS, successmsg, Constants.STRING));
		result.addParam(new Param(Constants.DBPERRMSG, dbperrmsg, Constants.STRING));
		return result;
	}

	public Object lognull(Result res, String text) {
		res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		if (text.equals(Constants.EVENT)) {
			res.addParam(new Param(Constants.DBPERRMSG, "Error in processing events", Constants.STRING));
			return res;
		}
		res.addParam(new Param(Constants.DBPERRMSG, "communication template are null", Constants.STRING));
		return res;
	}

}
