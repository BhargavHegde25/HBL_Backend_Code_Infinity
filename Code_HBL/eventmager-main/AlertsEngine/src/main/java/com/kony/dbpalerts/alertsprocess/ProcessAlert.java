package com.kony.dbpalerts.alertsprocess;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertConstants.ALERTCONDITIONS;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.dbconnectionutils.CustomerSwitch;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class ProcessAlert {

	private ProcessAlert() {
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static ConcurrentMap<String, String> customerSwitchData(List<Event> events) {
		return CustomerSwitch.processCustomerSwitchData(events);
	}

	private static int isAlertattrAlertcondValid(Event e) {
		try {
			if (e.getAttributeid() == null && e.getAlertconditionid() == null)
				return 0;
			if (e.getAttributeid() != null && e.getAlertconditionid() != null && e.getValue1() != null)
				return 1;
			
		} catch (Exception e1) {
			return 2;
		}
		return 2;
	}

	private static boolean isnotContainAlertAttribute(Event event, Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata) {
		return (!iscontain(alertcontentfieldsfromeventdata, event.getAttributeid().toLowerCase())
				&& !iscontain(alertcontentfieldsfromotherdata, event.getAttributeid().toLowerCase())
				&& !iscontain(event, event.getAttributeid())
				&& !iscontain(event, event.getAttributeid().toLowerCase()));
	}

	private static String fetchAlertAttribute(Event event, Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata) {
		String alertsattributenamevalue = null;
		if (iscontain(alertcontentfieldsfromeventdata, event.getAttributeid().toLowerCase()))
			alertsattributenamevalue = alertcontentfieldsfromeventdata.get(event.getAttributeid().toLowerCase());
		if (iscontain(alertcontentfieldsfromotherdata, event.getAttributeid().toLowerCase()))
			alertsattributenamevalue = alertcontentfieldsfromotherdata.get(event.getAttributeid().toLowerCase());
		if (iscontain(event, event.getAttributeid()))
			alertsattributenamevalue = fetchValueFromEvent(event, event.getAttributeid());
		return alertsattributenamevalue;
	}

	public static boolean isConditionMet(Event event, Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata) {
		if (event.getIsExternalSystem())
			return true;
		JsonElement eventjson = new JsonParser().parse(event.getEventJson());
		JsonObject eventdata = null;
		JsonObject otherdata = null;
		String alertsattributenamevalue = null;
		int testreturn = isAlertattrAlertcondValid(event);
		if (testreturn == 0) {
			return true;
		}
		if (testreturn == 2) {
			return false;
		}
		try {
			eventdata = AlertsUtils.getJsonObjects(AlertConstants.EVENTDATA, eventjson.getAsJsonObject(), false);
		} catch (Exception e1) {
			diagnostic.prepareDebug("There is no eventData parameter.").log();
		}
		try {
			otherdata = AlertsUtils.getJsonObjects(AlertConstants.OTHERDATA, eventjson.getAsJsonObject(), false);
		} catch (Exception e1) {
			diagnostic.prepareDebug("There is no otherData parameter.").log();
		}
		try {
			if (eventdata != null)
				PreProcessAlert.getAllPairsFromJson(new JsonParser().parse(event.getEventJson()), alertcontentfieldsfromeventdata,
						AlertConstants.EVENTDATA);
		} catch (Exception e1) {
			diagnostic.prepareDebug("Error in fetching eventdata", e1).log();
		}
		try {
			if (otherdata != null)
				PreProcessAlert.getAllPairsFromJson(new JsonParser().parse(event.getEventJson()), alertcontentfieldsfromotherdata,
						AlertConstants.OTHERDATA);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error in fetching eventdata", e).log();
		}
		if (isnotContainAlertAttribute(event, alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata)) {
			return false;
		} else {
			alertsattributenamevalue = fetchAlertAttribute(event, alertcontentfieldsfromeventdata,
					alertcontentfieldsfromotherdata);
		}
		String symbol = null;
		symbol = fetchSymbolFromAlertCondition(event.getAlertconditionid());
		if (symbol == null) {
			alert.prepareError("Invalid Alert Condition").log();
			return false;
		}
		if (alertsattributenamevalue == null || alertsattributenamevalue.equals(""))
			return false;
		return (processAlertCondion(alertsattributenamevalue, symbol, event));

	}

	private static boolean processAlertCondion(String alertsattributenamevalue, String symbol, Event e) {
		try {
			switch (symbol) {
			case "GREATER_THAN":
				return (e.getValue1() != null
						&& Double.parseDouble(alertsattributenamevalue) > Double.parseDouble(e.getValue1()));

			case "GREATER_EQUAL_TO":
				return (e.getValue1() != null
						&& Double.parseDouble(alertsattributenamevalue) >= Double.parseDouble(e.getValue1()));

			case "LESS_THAN":
				return (e.getValue1() != null
						&& Double.parseDouble(alertsattributenamevalue) < Double.parseDouble(e.getValue1()));

			case "LESS_EQUAL_TO":
				return (e.getValue1() != null
						&& Double.parseDouble(alertsattributenamevalue) <= Double.parseDouble(e.getValue1()));

			case "NOT_EQUAL_TO":
				return (e.getValue1() != null
						&& Double.parseDouble(alertsattributenamevalue) != Double.parseDouble(e.getValue1()));

			case "EQUALS_TO":
				return (e.getValue1() != null
						&& Double.parseDouble(alertsattributenamevalue) == Double.parseDouble(e.getValue1()));

			case "CONTAINS":
				return (e.getValue1() != null
						&& alertsattributenamevalue.toLowerCase().contains(e.getValue1().toLowerCase()));

			case "IN_BETWEEN":
				return (e.getValue1() != null && e.getValue2() != null
						&& Double.parseDouble(alertsattributenamevalue) >= Double.parseDouble(e.getValue1())
						&& Double.parseDouble(alertsattributenamevalue) <= Double.parseDouble(e.getValue2()));
			default:
				return false;
			}
		} catch (Exception e1) {
			alert.prepareError("Error occured" , e1).log();
			return false;
		}
	}

	private static String fetchSymbolFromAlertCondition(String alertconditionid) {
		try {
			for (ALERTCONDITIONS cond : ALERTCONDITIONS.values()) {
				if (cond.getName().equals(alertconditionid))
					return cond.getName();
			}
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}
		return null;
	}

	private static boolean iscontain(Map<String, String> map, String text) {
		return (map != null && map.containsKey(text));

	}

	private static boolean iscontain(Event e, String text) {
		try {
			for (Field field : e.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				String name = field.getName();
				if (name.equalsIgnoreCase(text))
					return true;
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	private static String fetchValueFromEvent(Event e, String text) {
		try {
			for (Field field : e.getClass().getDeclaredFields()) {
			    boolean accessible = field.isAccessible();
	            field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(e);
				field.setAccessible(accessible);
				if (name.equalsIgnoreCase(text))
					return (String) value;
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}

}
