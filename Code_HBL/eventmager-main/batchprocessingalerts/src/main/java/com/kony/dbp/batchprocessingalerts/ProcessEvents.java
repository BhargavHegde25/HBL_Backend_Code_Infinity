package com.kony.dbp.batchprocessingalerts;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.batchprocessingengine.helper.*;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class ProcessEvents {
	private ProcessEvents() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static JsonArray processAllEvents(JsonArray eventsjsonarray, DataControllerRequest request) {
		JsonArray events = new JsonArray();
		try {
			Map<String, JsonArray> alertdefinitiontablemap = AlertsSubscribers.getAlertTypesFromDB();
			Map<String, JsonObject> alerttypetoattributemap = getAlertTypeToAttributeAndConditionMapFromDB(
					alertdefinitiontablemap);
			Map<String, JsonArray> subtypeeventpayload = separatesubtypes(eventsjsonarray);
			for (Entry<String, JsonArray> eventsubtypeentry : subtypeeventpayload.entrySet()) {
				Map<String, JsonArray> customerdatamap = processAllEventsAndFormat(
						eventsubtypeentry.getValue().getAsJsonArray());

				Map<String, Set<String>> customeralertmap = fetchAlertTypeFromDB(customerdatamap,
						alertdefinitiontablemap);
				JsonArray subcriberresponse = getSubscriberServiceResponse(customeralertmap, request);

				JsonArray tempevents = parseSubcribersResponse(subcriberresponse, customeralertmap, customerdatamap,
						eventsubtypeentry.getKey(), alertdefinitiontablemap, alerttypetoattributemap);
				events = HelperMethods.mergeJsonArray(events, tempevents);
			}
		} catch (Exception e) {
			alert.prepareError("Error while processing the events:", e).log();
		}
		return events;
	}

	private static Map<String, JsonObject> getAlertTypeToAttributeAndConditionMapFromDB(
			Map<String, JsonArray> alertdefinitiontablemap) {
		Map<String, JsonObject> alerttypetoattributemap = new HashMap<>();
		for (Entry<String, JsonArray> tableobj : alertdefinitiontablemap.entrySet()) {
			for (JsonElement attributes : tableobj.getValue()) {
				alerttypetoattributemap.put(attributes.getAsJsonObject().get(Constants.ALERTTYPECOLUMN).getAsString(),
						attributes.getAsJsonObject());
			}
		}
		return alerttypetoattributemap;
	}

	public static Map<String, JsonArray> separatesubtypes(JsonArray eventpayload) {

		Map<String, JsonArray> result = new HashMap<>();
		for (JsonElement event : eventpayload) {
			String eventsubtype = event.getAsJsonObject().get(Constants.EVENTSUBTYPE).getAsString();
			if (!result.containsKey(eventsubtype)) {
				result.put(eventsubtype, new JsonArray());
			}
			result.get(eventsubtype).add(event.getAsJsonObject());
		}
		return result;
	}

	public static Map<String, JsonArray> processAllEventsAndFormat(JsonArray eventsarray) {
		Map<String, JsonArray> customerdatamap = new HashMap<>();
		try {
			for (JsonElement element : eventsarray) {
				JsonObject customparams = element.getAsJsonObject().get(Constants.EVENTDATA).getAsJsonObject()
						.get(Constants.CUSTOMPARAMS).getAsJsonObject();
				JsonObject requestinput = element.getAsJsonObject().get(Constants.EVENTDATA).getAsJsonObject()
						.get(Constants.REQUESTINPUT).getAsJsonObject();
				String customerid = getAttributeFromCustomerElement(requestinput, Constants.CUSTOMERID);
				String accountid = getAttributeFromCustomerElement(requestinput, Constants.ACCOUNTID);
				for (Entry<String, JsonElement> typeelement : customparams.entrySet()) {
					if (typeelement.getValue().isJsonArray()) {
						for (JsonElement eventdataelement : typeelement.getValue().getAsJsonArray()) {
							JsonArray eventdataarray = new JsonArray();
							if (customerdatamap.containsKey(customerid))
								eventdataarray = customerdatamap.get(customerid);

							JsonObject event = new JsonObject();

							eventdataelement.getAsJsonObject().addProperty(Constants.ACCOUNTID, accountid);
							eventdataelement.getAsJsonObject().addProperty(Constants.CUSTOMERID, customerid);

							event.addProperty(Constants.EVENTSUBTYPE,
									element.getAsJsonObject().get(Constants.EVENTSUBTYPE).getAsString());
							event.addProperty(Constants.CUSTOMERID,
									eventdataelement.getAsJsonObject().get(Constants.CUSTOMERID).getAsString());
							event.add(Constants.EVENTDATA, eventdataelement.getAsJsonObject());
							eventdataarray.add(event);
							customerdatamap.put(customerid, eventdataarray);
						}
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Payload not given in valid format", e).log();
		}
		return customerdatamap;
	}

	public static Map<String, Set<String>> fetchAlertTypeFromDB(Map<String, JsonArray> customerdatamap,
			Map<String, JsonArray> subtypetoalertmap) {
		Map<String, Set<String>> customeralertmap = new HashMap<>();
		for (Entry<String, JsonArray> eventsubtypetoalert : subtypetoalertmap.entrySet()) {
			for (Entry<String, JsonArray> customeriddata : customerdatamap.entrySet()) {
				JsonArray customerdata = customeriddata.getValue();
				for (JsonElement eachdata : customerdata) {
					if (eventsubtypetoalert.getKey()
							.equals(eachdata.getAsJsonObject().get(Constants.EVENTSUBTYPE).getAsString())) {
						JsonArray alerttypetabledata = eventsubtypetoalert.getValue();
						for (JsonElement alerttypedata : alerttypetabledata) {
							fetchAlertTypeFromDBWrapper(customeralertmap, customeriddata, eachdata, alerttypedata);
						}
					}
				}
			}
		}
		return customeralertmap;
	}

	public static JsonArray getSubscriberServiceResponse(Map<String, Set<String>> customeralertmap,
			DataControllerRequest request) {
		String customerids = customeralertmap.keySet().stream().map(Object::toString).collect(Collectors.joining(","));
		String alerttypes = String.join(",",
				customeralertmap.values().stream().flatMap(Set::stream).collect(Collectors.toSet()));
		if (request == null)
			return new JsonParser().parse(getSubscriberTestPayload()).getAsJsonArray();
		else
			return addSubcriberServicePayloadAndCall(alerttypes, customerids, request);
	}

	public static void parseSubcribersResponseWrapper(JsonArray events, JsonObject customerobj, String eventtype,
			JsonObject eventtypedata, Map<String, JsonArray> customerdatamap, Map<String, Set<String>> customeralertmap,
			Map<String, List<String>> duedatealertvaluemap, Map<String, JsonObject> alerttypetoattributemap) {
		String customerid = customerobj.get(Constants.CORECUSTOMERID).getAsString();
		if (customeralertmap.containsKey(customerid) && customeralertmap.get(customerid).contains(eventtype)) {

			for (JsonElement eachcustomerdata : customerdatamap.get(customerid).getAsJsonArray()) {
				Map<String, JsonObject> idaccountiddatamap = new HashMap<>();

				for (JsonElement customerelement : customerobj.get(Constants.ATTRIBUTES).getAsJsonArray()) {
					String condition = getAttributeFromCustomerElement(customerelement, Constants.ALERTCONDITIONID);
					String value1 = getAttributeFromCustomerElement(customerelement, Constants.VALUE1);
					String value2 = getAttributeFromCustomerElement(customerelement, Constants.VALUE2);
					String accountid = getAttributeFromCustomerElement(customerelement, Constants.ACCOUNTID);
					String attributeid = getAttributeFromCustomerElement(customerelement, Constants.ATTRIBUTEID);
					String dbxcustomerid = getAttributeFromCustomerElement(customerelement, Constants.CUSTOMERID);

					checkConditionAndCreatePayload(events, dbxcustomerid, accountid, eachcustomerdata, condition,
							value1, value2, attributeid, eventtypedata, idaccountiddatamap, eventtype,
							duedatealertvaluemap, alerttypetoattributemap);
				}
			}
		}
	}

	public static String getAlertTypeFromDB(String eventsubtype, Map<String, JsonArray> alertdefinitiontablemap) {
		Map<String, JsonArray> mp = alertdefinitiontablemap;
		StringBuilder build = new StringBuilder();

		if (mp.containsKey(eventsubtype)) {
			for (JsonElement event : mp.get(eventsubtype)) {
				build.append(event.getAsJsonObject().get(Constants.ALERTTYPE).getAsString() + ',');
			}
		} else {
			alert.prepareError("ObjectType not present in DB:" + eventsubtype).log();
		}
		return build.toString();
	}

	public static void fetchAlertTypeFromDBWrapper(Map<String, Set<String>> customeralertmap,
			Entry<String, JsonArray> customeriddata, JsonElement eachdata, JsonElement alerttypedata) {

		String condition = getAttributeFromAlertType(alerttypedata, Constants.CONDITIONCOL);
		String value = getAttributeFromAlertType(alerttypedata, Constants.VALUECOL);
		String columnname = getAttributeFromAlertType(alerttypedata, Constants.COLUMNNAMEDB);

		JsonObject tempeventdata = new JsonObject();
		JsonParsingEngine.auxGetAllPairsFromJson(eachdata.getAsJsonObject().get(Constants.EVENTDATA), tempeventdata);
		String paramvalue = getAttributeFromAlertType(tempeventdata, columnname);

		if (condition.equals("") || value.equals("") || columnname.equals("")
				|| (checkCondition(condition, paramvalue, value, ""))) {
			if (!customeralertmap.containsKey(customeriddata.getKey())) {
				Set<String> customeralerttypeset = new HashSet<>();
				customeralertmap.put(customeriddata.getKey(), customeralerttypeset);
			}
			Set<String> customeralerttypeset = customeralertmap.get(customeriddata.getKey());
			customeralerttypeset.add(alerttypedata.getAsJsonObject().get(Constants.ALERTTYPECOLUMN).getAsString());
			customeralertmap.put(customeriddata.getKey(), customeralerttypeset);
		}
	}

	public static boolean checkCondition(String condition, String paramvalue, String value1, String value2) {

		if (condition == null || value1 == null || condition.equals("") || value1.equals(""))
			return true;
		if (paramvalue == null || paramvalue.equals(""))
			return false;
		boolean result = false;
		try {
			switch (condition) {
			case Constants.EQUALSTO:
				try {
					if (Float.parseFloat(paramvalue) == Float.parseFloat(value1))
						result = true;
				} catch (Exception e) {
					if (paramvalue.equalsIgnoreCase(value1))
						result = true;
				}
				break;
			case Constants.GREATERTHAN:
				if (Float.parseFloat(paramvalue) > Float.parseFloat(value1))
					result = true;
				break;
			case Constants.GREATEREQUALTO:
				if (Float.parseFloat(paramvalue) >= Float.parseFloat(value1))
					result = true;
				break;
			case Constants.LESSTHAN:
				if (Float.parseFloat(paramvalue) < Float.parseFloat(value1))
					result = true;
				break;
			case Constants.LESSEQUALTO:
				if (Float.parseFloat(paramvalue) <= Float.parseFloat(value1))
					result = true;
				break;
			case Constants.NOTEQUALTO:
				if (!paramvalue.equals(value1))
					result = true;
				break;
			case Constants.INBETWEEN:
				if (Float.parseFloat(paramvalue) >= Float.parseFloat(value1)
						&& Float.parseFloat(paramvalue) <= Float.parseFloat(value2))
					result = true;
				break;
			case Constants.CONTAINS:
				if (paramvalue.contains(value1))
					result = true;
				break;
			default:
				break;
			}

		} catch (Exception e) {
			alert.prepareError("Exception occurred while checking condition.", e).log();
		}
		return result;
	}

	public static String getObjectTypeForGivenAlert(String alerttype, Map<String, Set<String>> objectalerttypemap) {
		for (Entry<String, Set<String>> ele : objectalerttypemap.entrySet()) {
			if (ele.getValue().contains(alerttype))
				return ele.getKey();
		}
		return "";
	}

	private static String getAttributeFromCustomerElement(JsonElement customerelement, String key) {
		try {
			if (customerelement.getAsJsonObject().has(key)
					&& !customerelement.getAsJsonObject().get(key).isJsonNull()) {
				return customerelement.getAsJsonObject().get(key).getAsString();
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	private static String getAttributeFromAlertType(JsonElement alerttypedata, String key) {
		try {
			if (alerttypedata.getAsJsonObject().has(key) && !alerttypedata.getAsJsonObject().get(key).isJsonNull()) {
				return alerttypedata.getAsJsonObject().get(key).getAsString();
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	private static Map<String, List<String>> getAlertTypeCheckTypeMapForDueDate(String objecttype,
			Map<String, JsonArray> alertdefinitiontablemap) {
		Map<String, List<String>> alerttypeduedateparam = new HashMap<>();

		for (JsonElement ele : alertdefinitiontablemap.get(objecttype)) {
			if (ele.getAsJsonObject().get(Constants.CHECKTYPE) != null
					&& !ele.getAsJsonObject().get(Constants.CHECKTYPE).isJsonNull()
					&& (ele.getAsJsonObject().get(Constants.CHECKTYPE).getAsString().equals("1")
							|| ele.getAsJsonObject().get(Constants.CHECKTYPE).getAsString().equals("-1"))) {
				String key = ele.getAsJsonObject().get(Constants.ALERTTYPECOLUMN).getAsString();
				List<String> typevalue = new ArrayList<>();
				typevalue.add(ele.getAsJsonObject().get(Constants.CHECKTYPE).getAsString());
				typevalue.add(ele.getAsJsonObject().get(Constants.PARAMETERCOLUMNNAME).getAsString());
				alerttypeduedateparam.put(key, typevalue);
			}
		}
		return alerttypeduedateparam;
	}

	private static void checkConditionAndCreatePayload(JsonArray events, String dbxcustomerid, String accountid,
			JsonElement eachcustomerdata, String condition, String value1, String value2, String attributeid,
			JsonObject eventtypedata, Map<String, JsonObject> idaccountiddatamap, String eventtype,
			Map<String, List<String>> duedatealertvaluemap, Map<String, JsonObject> alerttypetoattributemap) {

		if (idaccountiddatamap.containsKey(dbxcustomerid + accountid))
			return;
		JsonObject otherdata = new JsonObject();
		String appid = Constants.RETAIL_AND_BUSINESS_BANKING;
		try {
			appid = HelperMethods.getConfigProperty(Constants.BATCH_ALERT_APP_ID);
		} catch (Exception e1) {
			alert.prepareError(e1.toString()).log();
		}
		otherdata.addProperty(Constants.APPID, appid);
		otherdata.addProperty(Constants.CUSTOMERID, dbxcustomerid);
		boolean isaccounttype = true;
		if (!accountid.equals(""))
			otherdata.addProperty(Constants.ACCOUNTNUMBER, accountid);
		else
			isaccounttype = false;

		boolean isValidEvent = true;
		if (duedatealertvaluemap.containsKey(eventtype)) {
			String date2 = eachcustomerdata.getAsJsonObject().get(Constants.EVENTDATA).getAsJsonObject()
					.get(duedatealertvaluemap.get(eventtype).get(1)).getAsString();
			int numberofdays = HelperMethods.getDifferenceBetweenDates(LocalDate.now().toString(), date2);
			if ((numberofdays < 0 && duedatealertvaluemap.get(eventtype).get(0).equals("-1")
					|| numberofdays >= 0 && duedatealertvaluemap.get(eventtype).get(0).equals("1"))) {
				if (!attributeid.equals(""))
					eachcustomerdata.getAsJsonObject().get(Constants.EVENTDATA).getAsJsonObject()
							.addProperty(attributeid, String.valueOf(Math.abs(numberofdays)));
			} else
				isValidEvent = false;
		}

		JsonObject tempeventdata = new JsonObject();
		JsonParsingEngine.auxGetAllPairsFromJson(eachcustomerdata.getAsJsonObject().get(Constants.EVENTDATA),
				tempeventdata);
		String paramvalue = getAttributeFromCustomerElement(tempeventdata, attributeid);
		String paramaccountid = getAttributeFromCustomerElement(tempeventdata, Constants.ACCOUNTID);
		String companyLegalUnit = getAttributeFromCustomerElement(tempeventdata, "companyLegalUnit");
		otherdata.addProperty("companyLegalUnit", companyLegalUnit);
		if (checkBatchAlertDefinitionCondition(eventtype, tempeventdata, alerttypetoattributemap)
				&& (value1.equals("") || attributeid.equals("")
						|| checkCondition(condition, paramvalue, value1, value2))
				&& (!isaccounttype || paramaccountid.equals(accountid)) && isValidEvent) {
			try {
				for (String eventsubtype : eventtypedata.get(Constants.ALERTSUBTYPE).getAsString().split(",")) {
					JsonObject eventdetails = putParamsInEventObject(eachcustomerdata, eventtype, otherdata,
							eventsubtype);
					idaccountiddatamap.put(dbxcustomerid + accountid, eventdetails);
					events.add(eventdetails);
				}
			} catch (Exception e) {
				alert.prepareError("Error while fetching subtypes or adding events", e).log();
			}

		}

	}

	private static boolean checkBatchAlertDefinitionCondition(String eventtype, JsonObject tempeventdata,
			Map<String, JsonObject> alerttypetoattributemap) {
		JsonObject dbdata = alerttypetoattributemap.get(eventtype);

		String condition = getAttributeFromAlertType(dbdata, Constants.CONDITIONCOL);
		String value = getAttributeFromAlertType(dbdata, Constants.VALUECOL);
		String columnname = getAttributeFromAlertType(dbdata, Constants.COLUMNNAMEDB);

		String paramvalue = getAttributeFromAlertType(tempeventdata, columnname);

		if (condition.equals("") || value.equals("") || columnname.equals("")
				|| (checkCondition(condition, paramvalue, value, "")))
			return true;

		return false;
	}

	public static JsonArray parseSubcribersResponse(JsonArray response, Map<String, Set<String>> customeralertmap,
			Map<String, JsonArray> customerdatamap, String objecttype, Map<String, JsonArray> alertdefinitiontablemap,
			Map<String, JsonObject> alerttypetoattributemap) {

		Map<String, List<String>> alerttypechecktypeparamvaluemap = getAlertTypeCheckTypeMapForDueDate(objecttype,
				alertdefinitiontablemap);
		JsonArray events = new JsonArray();

		if (!response.isJsonArray()) {
			alert.prepareError("response from subcriber is not json array or invalid format.").log();
			return events;
		}
		for (JsonElement alerttype : response) {
			JsonObject eventtypedata = alerttype.getAsJsonObject();
			String eventtype = eventtypedata.get(Constants.ALERTTYPE).getAsString();
			for (JsonElement customerobject : eventtypedata.get(Constants.CORECUSTOMERS).getAsJsonArray()) {
				JsonObject customerobj = customerobject.getAsJsonObject();
				parseSubcribersResponseWrapper(events, customerobj, eventtype, eventtypedata, customerdatamap,
						customeralertmap, alerttypechecktypeparamvaluemap, alerttypetoattributemap);

			}

		}
		return events;
	}

	private static JsonObject putParamsInEventObject(JsonElement eachdata, String eventtype, JsonObject otherdata,
			String eventsubtype) {
		JsonObject customParams = eachdata.getAsJsonObject().get(Constants.EVENTDATA).getAsJsonObject();
		JsonObject eventdata = new JsonObject();
		JsonObject eventdetails = new JsonObject();

		eventdata.add(Constants.CUSTOMPARAMS, customParams);
		eventdetails.addProperty(Constants.EVENTTYPE, eventtype);
		eventdetails.addProperty(Constants.EVENTSUBTYPE, eventsubtype);
		eventdetails.addProperty(Constants.STATUS, "SID_EVENT_SUCCESS");
		eventdetails.add(Constants.OTHERDATA, otherdata);
		eventdetails.add(Constants.EVENTDATA, eventdata);
		return eventdetails;
	}

	public static Map<String, Set<String>> getObjectAlertType() throws SQLException {
		Map<String, JsonArray> alertdefinitiontablemap = AlertsSubscribers.getAlertTypesFromDB();
		Map<String, Set<String>> objectalerttypemap = new HashMap<>();

		for (Entry<String, JsonArray> entry : alertdefinitiontablemap.entrySet()) {
			objectalerttypemap.put(entry.getKey(), new HashSet<String>());
			for (JsonElement ele : entry.getValue()) {
				objectalerttypemap.get(entry.getKey())
						.add(ele.getAsJsonObject().get(Constants.ALERTTYPECOLUMN).getAsString());
			}
		}
		return objectalerttypemap;
	}

	public static Result processBatchAndCallQueueMaster(JsonArray events, ServicesManager servicesmanager) {
		diagnostic.prepareDebug("Dispatching events from core..").log();
		int batchLimit = 100;
		try {
			batchLimit = Integer
					.parseInt(HelperMethods.getConfigProperty(Constants.BATCH_PROCESSING_ENGINE_BATCHLIMIT));
		} catch (Exception e) {
			alert.prepareError("Error while fetching configuration parameter", e).log();
		}
		int count = 0;
		int loopcount = 0;
		JsonArray batchPayload = new JsonArray();
		Map<String, Object> inputmap = new HashMap<>();
		StringBuilder eventsarray = new StringBuilder();

		for (JsonElement element : events) {
			count += 1;
			batchPayload.add(element.getAsJsonObject());
			if (count == batchLimit) {
				loopcount++;
				JsonArray sendPayload = batchPayload;
				eventsarray.append(sendPayload).append(Constants.LOOP_SEPARATOR_VAL);
				count = 0;
				batchPayload = new JsonArray();
			}
		}
		if (count > 0) {
			loopcount++;
			JsonArray sendPayload = batchPayload;
			eventsarray.append(sendPayload);
		}
		inputmap.put(Constants.EVENTS, eventsarray);
		inputmap.put(Constants.LOOPCOUNT, loopcount);
		inputmap.put(Constants.LOOP_SEPARATOR, Constants.LOOP_SEPARATOR_VAL);
		try {
			DBPServiceInvocationWrapper.invokeServiceAndGetJSON(Constants.CALLQUEUEMASTERSERVICE_ORCH, null,
					Constants.QUEUEMASTEROPERATION, inputmap, new HashMap<String, Object>(), "");
		} catch (Exception e) {
			alert.prepareError("Exception occurred while calling queuemaster" + e).log();
		}
		alert.prepareError("Dispatching of events is completed.").log();
		Result result = new Result();
		result.addStringParam(Constants.SUCCESS, "true");
		return result;
	}

	public static JsonArray addSubcriberServicePayloadAndCall(String alerttypes, String customerids,
			DataControllerRequest request) {

		Map<String, Object> inputmap = new HashMap<>();
		Map<String, Object> headermap = new HashMap<>();

		inputmap.put(Constants.ALERTTYPESKEY, alerttypes);
		inputmap.put(Constants.CORECUSTOMERIDS, customerids);
		return CallServices.callSubscriberService(request, inputmap, headermap);
	}

	public static String getTestPayload() {
		return null;
	}

	public static String getSubscriberTestPayload() {
		return null;
	}

}
