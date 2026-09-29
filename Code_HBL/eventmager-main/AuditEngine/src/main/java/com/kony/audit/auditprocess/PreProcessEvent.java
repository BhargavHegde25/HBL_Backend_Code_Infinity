package com.kony.audit.auditprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.*;
import com.kony.audit.auditutils.AuditConstants;
import com.kony.audit.auditutils.Event;
import com.kony.audit.auditutils.AuditUtils;
import com.kony.audit.dbconnectionutils.FetchCustomerData;
import com.kony.audit.dbconnectionutils.FetchCustomerFromAccount;
import com.kony.audit.dbconnectionutils.FetchCustomerFromCoreCustomerId;

@SuppressWarnings("unused")
public class PreProcessEvent {
	private PreProcessEvent() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static List<Event> preProcess(JsonArray eventsjsonarray) {
		List<Event> events = new ArrayList<>();
		processEventSpecificData(eventsjsonarray, events);
		if (events.isEmpty())
			return events;
		processCustomerSpecificDataFromEvent(events);
		fillCustomerIdFromCoreId(events);
		fillCustomerIdFromAccount(events);
		fillCustomerDetails(events);
		return events;
	}

	private static void fillCustomerIdFromCoreId(List<Event> events) {
		Map<String, Set<String>> coreidcustomermap = FetchCustomerFromCoreCustomerId
				.coreCustomerIdCustomerIdMapping(events);
		List<Event> newevents = new ArrayList<>();
		if (coreidcustomermap == null)
			return;
		for (Event x : events) {
			if (x.getCustomerId() == null && x.getUserName() == null && x.getCorecustomerid() != null
					&& coreidcustomermap.containsKey(x.getCorecustomerid())) {
				Set<String> custlist = coreidcustomermap.get(x.getCorecustomerid());
				if (custlist == null || custlist.isEmpty())
					continue;
				String customers = custlist.stream().collect(Collectors.joining(","));
				String[] custarr = customers.split(",");
				x.setCustomerId(custarr[0]);
				for (int i = 1; i < custarr.length; i++) {
					Event newevent = new Event(x);
					newevent.setCustomerId(custarr[i]);
					newevents.add(newevent);
				}
			}
		}
		if (!newevents.isEmpty())
			events.addAll(newevents);
	}

	private static void fillCustomerIdFromAccount(List<Event> events) {
		Map<String, List<String>> accountcustmap = FetchCustomerFromAccount.accountCustomerIdMapping(events);
		for (Event event : events) {
			if (event.getCustomerId() == null && event.getaccountId() != null
					&& accountcustmap.containsKey(event.getaccountId())
					&& accountcustmap.get(event.getaccountId()) != null
					&& !accountcustmap.get(event.getaccountId()).isEmpty()) {
				event.setCustomerId(accountcustmap.get(event.getaccountId()).get(0));
			}
		}
	}

	public static void fetchCustomerData(Event event) {
		String eventid = null;
		try {
			JsonObject otherdata = AuditUtils.getJsonObjects(AuditConstants.OTHERDATA,
					event.getJsonElement().getAsJsonObject(), false);
			eventid = AuditUtils.getJsonObjects(event.getJsonElement().getAsJsonObject(), AuditConstants.EVENTID, true);
			if (otherdata != null && eventid != null) {
				event.setUserName(AuditUtils.getJsonObjects(otherdata, AuditConstants.USER, false));
				event.setCustomerId(AuditUtils.getJsonObjects(otherdata, AuditConstants.CUSTOMERID, false));
				event.setaccountId(AuditUtils.getJsonObjects(otherdata, AuditConstants.ACCOUNTNUMBER, false));
				event.setCorecustomerid(AuditUtils.getJsonObjects(otherdata, AuditConstants.CORECUSTOMERID, false));
			}
		} catch (Exception e) {
			alert.prepareError("Error in fetching other data for event:" + event + "is:", e).log();
		}

	}

	private static void processCustomerSpecificDataFromEvent(List<Event> events) {

		try {
			for (int i = 0; i < events.size(); i++) {
				fetchCustomerData(events.get(i));
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured", e).log();

		}
	}

	private static List<Event> processEventSpecificData(JsonArray eventsarray, List<Event> events) {
		if (eventsarray == null || eventsarray.size() == 0)
			return events;

		try {
			for (JsonElement event : eventsarray) {
				processEventData(event, events);
			}
		} catch (Exception e) {
			alert.prepareError("Error occured: ", e).log();
		}
		return events;
	}

	private static void processEventData(JsonElement event, List<Event> eventlist) {
		Event e = new Event(event);
		eventlist.add(e);
	}

	public static Event processEventData(JsonElement event) {
		Event e = null;
		try {
			String eventid = AuditUtils.getJsonObjects(event.getAsJsonObject(), AuditConstants.EVENTID, true);
			String eventtype = AuditUtils.getJsonObjects(event.getAsJsonObject(), AuditConstants.EVENTTYPE, true);
			String eventsubtype = AuditUtils.getJsonObjects(event.getAsJsonObject(), AuditConstants.EVENTSUBTYPE, true);
			String commstatusid = AuditUtils.getJsonObjects(event.getAsJsonObject(), AuditConstants.STATUS, true);
			e = new Event(eventid, eventtype, eventsubtype, commstatusid);
			e.setJsonElement(event);
		} catch (Exception ex) {
			diagnostic.prepareDebug("Error occured", ex).log();
		}
		return e;
	}

	private static void fillCustomerDetails(List<Event> events) {
		Map<String, String> customeruser;
		customeruser = FetchCustomerData.getCustomerDataMap(events);
		for (Event x : events) {
			if (x.getCustomerId() != null || x.getUserName() != null) {
				if (x.getUserName() == null && customeruser.containsKey(x.getCustomerId())) {
					x.setUserName(customeruser.get(x.getCustomerId()));

				} else if (x.getCustomerId() == null && customeruser.containsKey(x.getUserName())) {
					x.setCustomerId(customeruser.get(x.getUserName()));
				}
			}
		}

	}

}
