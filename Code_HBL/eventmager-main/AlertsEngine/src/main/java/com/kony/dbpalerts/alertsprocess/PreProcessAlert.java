package com.kony.dbpalerts.alertsprocess;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.*;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.HTMLParsingEngine;
import com.kony.dbpalerts.alertsutils.AlertsUtils;

@SuppressWarnings("unused")
public class PreProcessAlert {
	private PreProcessAlert() {
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static void getAllPairsFromJson(JsonElement eventjsonelem, Map<String, String> resmap, String typeofdata) {
		try {
			if (eventjsonelem == null || eventjsonelem.isJsonNull() || !eventjsonelem.isJsonObject())
				return;
			JsonObject eventobj = eventjsonelem.getAsJsonObject();
			JsonElement tempelem = null;
			if (typeofdata.equals(AlertConstants.EVENTDATA)) {
				tempelem = eventobj.get(AlertConstants.EVENTDATA);
			} else if (typeofdata.equals(AlertConstants.OTHERDATA)) {
				tempelem = eventobj.get(AlertConstants.OTHERDATA);
			} else if (typeofdata.equals(AlertConstants.SESSION)) {
				tempelem = eventobj.get(AlertConstants.SESSION);
			}
			JsonObject resobj = new JsonObject();
			if (tempelem == null)
				return;
			auxGetAllPairsFromJson(tempelem, resobj);
			insertIntoMap(resmap, resobj);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
	}

	private static void insertIntoMap(Map<String, String> resmap, JsonObject resobj) {
		Set<Map.Entry<String, JsonElement>> entries = resobj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			try {
				resmap.put(entry.getKey().toLowerCase(), resobj.get(entry.getKey()).getAsString());
			} catch (Exception e) {
				alert.prepareError("Error occured", e).log();
			}
		}
	}

	private static void buildingFromJsonObject(JsonElement element, JsonObject resobj) {
		JsonObject obj = element.getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			String key = entry.getKey();
			JsonElement val = entry.getValue();
			if (val == null || val.isJsonNull())
				continue;
			try {
				resobj.addProperty(key, val.getAsString());
			} catch (Exception e) {
				resobj.addProperty(key, val.toString());
			}
			if (val.isJsonObject()) {
				auxGetAllPairsFromJson(entry.getValue(), resobj);
			} else {
				try {
					JsonElement eventsElement = new JsonParser().parse(val.getAsString());
					auxGetAllPairsFromJson(eventsElement, resobj);
				} catch (Exception e) {
					//Do Nothing
				}
			}
		}
	}

	private static void buildingFromNonJsonObject(JsonElement element, JsonObject resobj) {
		try {

			JsonElement eventsElement = new JsonParser().parse(element.getAsString());
			if (eventsElement.isJsonArray()) {
				JsonArray eventsjsonarray = eventsElement.getAsJsonArray();
				callAuxService(eventsjsonarray, resobj);
			} else if (eventsElement.isJsonObject()) {
				auxGetAllPairsFromJson(eventsElement, resobj);
			}
		} catch (Exception e) {
			diagnostic.prepareInfo("").log();
		}

	}

	private static void callAuxService(JsonArray eventsjsonarray, JsonObject resobj) {
		for (int i = 0; i < eventsjsonarray.size(); i++) {
			try {
				auxGetAllPairsFromJson(eventsjsonarray.get(i), resobj);
			} catch (Exception e) {
				diagnostic.prepareInfo("").log();
			}
		}
	}

	private static void auxGetAllPairsFromJson(JsonElement element, JsonObject resobj) {
		if (element == null || element.isJsonNull())
			return;
		if (element.isJsonObject()) {
			buildingFromJsonObject(element, resobj);
		} else {
			buildingFromNonJsonObject(element, resobj);
		}
	}

	public static Map<String, String> fetchAlertContentFieldsFromOthertData(JsonObject otherdata) {
		Map<String, String> alertcontentfields = new HashMap<>();
		try {
			Set<Map.Entry<String, JsonElement>> entries = otherdata.entrySet();// will return members of your object
			for (Map.Entry<String, JsonElement> entry : entries) {
				alertcontentfields.put(entry.getKey().toLowerCase(),
						AlertsUtils.getJsonObjects(otherdata, entry.getKey(), false));
			}
			return alertcontentfields;
		} catch (Exception e) {
			alert.prepareError("Error in fetching OtherParams from event:", e).log();
			return alertcontentfields;
		}
	}

	public static String foundValueFromEvent(Event event, String key) throws IllegalAccessException {
		for (Field field : event.getClass().getDeclaredFields()) {
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			String name = field.getName();
			Object value = field.get(event);
			field.setAccessible(accessible);
			if (name.equalsIgnoreCase(key))
				return (String) value;
		}
		return null;
	}

	private static String getFinalTextForDynamicFields(String name, String text, String subject, String sendername,
			String senderemail) {
		String finaltext = "";
		finaltext = (name == null) ? finaltext : finaltext + name;
		finaltext = (text == null) ? finaltext : finaltext + text;
		finaltext = (subject == null) ? finaltext : finaltext + subject;
		finaltext = (sendername == null) ? finaltext : finaltext + sendername;
		finaltext = (senderemail == null) ? finaltext : finaltext + senderemail;
		return finaltext;
	}

	public static List<String> fetchDynamicFields(Map<String, JsonObject> commdata) {
		List<String> dynamicfields = new ArrayList<>();
		try {
			if (commdata != null && !commdata.isEmpty())
				for (Map.Entry<String, JsonObject> entry : commdata.entrySet()) {
					String name = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.NAME, false);
					String text = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.TEXT, false);
					String subject = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.SUBJECT, false);
					String sendername = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.SENDERNAME,
							false);
					String senderemail = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.SENDERMAIL,
							false);
					String finaltext = getFinalTextForDynamicFields(name, text, subject, sendername, senderemail);
					Pattern pattern = Pattern.compile("\\[#\\](.*?)\\[/#\\]");
					Matcher matcher = pattern.matcher(finaltext);
					while (matcher.find()) {
						dynamicfields.add(matcher.group(1));
					}
				}
		} catch (Exception e) {
			alert.prepareError("Exception Occured:", e).log();
		}
		return dynamicfields;
	}

	public static String replaceText(String text, Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata, Event event, List<String> alertcontentfieldsname)
			throws IllegalAccessException {
		if (alertcontentfieldsfromeventdata != null && !alertcontentfieldsfromeventdata.isEmpty())
			for (String key : alertcontentfieldsname) {
				String x = null;
				x = foundValueFromEvent(event, key);
				if (x != null) {
					text = text.replace("[#]" + key + "[/#]", x);
				} else if (alertcontentfieldsfromeventdata.containsKey(key.toLowerCase())) {
					text = text.replace("[#]" + key + "[/#]", alertcontentfieldsfromeventdata.get(key.toLowerCase()));
				} else if (alertcontentfieldsfromotherdata.containsKey(key.toLowerCase())) {
					text = text.replace("[#]" + key + "[/#]", alertcontentfieldsfromotherdata.get(key));
				} else {
					text = text.replace("[#]" + key + "[/#]", "");
				}
			}
		return text;
	}

	public static Map<String, JsonObject> replaceDynamics(Event event, List<String> keys,
			Map<String, JsonObject> commdata, Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata) {
		commdata = replaceFields(keys, commdata, alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata,
				event);
		return commdata;
	}

	public static Map<String, JsonObject> replaceFields(List<String> alertcontentfieldsname,
			Map<String, JsonObject> commdata, Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata, Event event) {
		try {
			Map<String, JsonObject> newmap = new HashMap<>();
			for (Map.Entry<String, JsonObject> entry : commdata.entrySet()) {
				String name = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.NAME, false);
				String text = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.TEXT, false);
				String subject = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.SUBJECT, false);
				String sendername = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.SENDERNAME, false);
				String senderemail = AlertsUtils.getJsonObjects(entry.getValue(), AlertConstants.SENDERMAIL, false);
				if (name != null) {
					name = replaceText(name, alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata, event,
							alertcontentfieldsname);
				}
				if (text != null) {
					text = replaceText(text, alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata, event,
							alertcontentfieldsname);
				}
				if (subject != null) {
					subject = replaceText(subject, alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata,
							event, alertcontentfieldsname);
				}
				if (sendername != null) {
					sendername = replaceText(sendername, alertcontentfieldsfromeventdata,
							alertcontentfieldsfromotherdata, event, alertcontentfieldsname);
				}
				if (senderemail != null) {
					senderemail = replaceText(senderemail, alertcontentfieldsfromeventdata,
							alertcontentfieldsfromotherdata, event, alertcontentfieldsname);
				}
				JsonObject js = new JsonObject();
				js.addProperty(AlertConstants.NAME, name);
				js.addProperty(AlertConstants.TEXT, text);
				js.addProperty(AlertConstants.SUBJECT, subject);
				js.addProperty(AlertConstants.SENDERNAME, sendername);
				js.addProperty(AlertConstants.SENDERMAIL, senderemail);
				newmap.put(entry.getKey(), js);
			}
			return newmap;
		} catch (Exception e) {
			alert.prepareError("Exeception occured", e).log();
		}
		return commdata;
	}

	public static Map<String, JsonObject> replaceDynamicText(Event event, Map<String, JsonObject> commdata,
			Map<String, String> alertcontentfieldsfromeventdata, Map<String, String> alertcontentfieldsfromotherdata) {
		List<String> dynamicfileds = fetchDynamicFields(commdata);
		try {
			commdata = replaceDynamics(event, dynamicfileds, commdata, alertcontentfieldsfromeventdata,
					alertcontentfieldsfromotherdata);
		} catch (Exception e) {

			return commdata;
		}
		return commdata;
	}

	private static void replaceCommunicationMap(Map<String, JsonObject> communicationdata,
			JsonObject externalchanneldata, String channel) {
		JsonObject channeldata = communicationdata.get(channel);
		if (channeldata == null || externalchanneldata == null)
			return;

		if (externalchanneldata.get(AlertConstants.SUBJECT) != null) {
			channeldata.addProperty(AlertConstants.SUBJECT,
					externalchanneldata.get(AlertConstants.SUBJECT).getAsString());
		}
		if (externalchanneldata.get(AlertConstants.TEXT) != null) {
			channeldata.addProperty(AlertConstants.TEXT, externalchanneldata.get(AlertConstants.TEXT).getAsString());
		}

	}

	private static void replaceMessageContentIfPassed(Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, JsonObject> communicationdata) {
		try {
			if (!alertcontentfieldsfromeventdata.containsKey(AlertConstants.MESSAGE_CONTENT)
					|| alertcontentfieldsfromeventdata.get(AlertConstants.MESSAGE_CONTENT) == null)
				return;

			String messagecontent = alertcontentfieldsfromeventdata.get(AlertConstants.MESSAGE_CONTENT);
			JsonElement messageelement = new JsonParser().parse(messagecontent);
			if (!messageelement.isJsonObject())
				return;
			JsonObject messageobj = messageelement.getAsJsonObject();

			JsonObject mail = (messageobj.get(AlertConstants.EMAIL_LOWER) != null
					&& messageobj.get(AlertConstants.EMAIL_LOWER).isJsonObject())
							? messageobj.get(AlertConstants.EMAIL_LOWER).getAsJsonObject()
							: null;
			JsonObject sms = (messageobj.get(AlertConstants.SMS_LOWER) != null
					&& messageobj.get(AlertConstants.SMS_LOWER).isJsonObject())
							? messageobj.get(AlertConstants.SMS_LOWER).getAsJsonObject()
							: null;
			JsonObject notification = (messageobj.get(AlertConstants.NOTIFICATION_LOWER) != null
					&& messageobj.get(AlertConstants.NOTIFICATION_LOWER).isJsonObject())
							? messageobj.get(AlertConstants.NOTIFICATION_LOWER).getAsJsonObject()
							: null;
			JsonObject push = (messageobj.get(AlertConstants.PUSH_LOWER) != null
					&& messageobj.get(AlertConstants.PUSH_LOWER).isJsonObject())
							? messageobj.get(AlertConstants.PUSH_LOWER).getAsJsonObject()
							: null;

			replaceCommunicationMap(communicationdata, mail, AlertConstants.CH_EMAIL);
			replaceCommunicationMap(communicationdata, sms, AlertConstants.CH_SMS);
			replaceCommunicationMap(communicationdata, notification, AlertConstants.CH_NOTIFICATION_CENTER);
			replaceCommunicationMap(communicationdata, push, AlertConstants.CH_PUSH_NOTIFICATION);
		} catch (Exception e) {
			alert.prepareError("Error occured in parsing external communication template:", e).log();
		}
	}

	public static Map<String, JsonObject> fetchCommunicationTemplate(Event event,
			Map<String, Map<String, JsonObject>> communicationdata, Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata, Map<String, String> sessiondata) {
		Map<String, JsonObject> eventcommtemplate;
		String key = event.getLanguagecode() + "_##_" + event.getAlertsubtype() + "_##_" + event.getCommstatusid();
		eventcommtemplate = communicationdata.get(key);

		if (eventcommtemplate == null || eventcommtemplate.isEmpty())
			return eventcommtemplate;
		/*
		 * Need to check message content passed as parameter, if passed need to replace
		 * the content with new passed content
		 */
		replaceMessageContentIfPassed(alertcontentfieldsfromeventdata, eventcommtemplate);

		if (eventcommtemplate.containsKey(AlertConstants.CH_EMAIL))
			HTMLParsingEngine.setBulkTemplates(alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata,
					sessiondata, eventcommtemplate);
		eventcommtemplate = replaceDynamicText(event, eventcommtemplate, alertcontentfieldsfromeventdata,
				alertcontentfieldsfromotherdata);
		return eventcommtemplate;
	}

}
