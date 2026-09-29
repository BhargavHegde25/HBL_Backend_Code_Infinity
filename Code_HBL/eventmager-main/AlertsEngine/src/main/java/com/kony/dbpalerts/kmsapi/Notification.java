package com.kony.dbpalerts.kmsapi;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonObject;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.NotificationQueryClass;

public class Notification {
	private Notification() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String replacespecials(String text) {
		if (text == null)
			return text;
		text = text.replaceAll("\'", "\\\\'");
		text = text.replaceAll("\"", "\\\\\"");
		return text;
	}

	public static void invokeNotification(Event event, Map<String, JsonObject> commtemplate) {

		Map<String, String> inputParams = buildPayload(event, commtemplate);
		diagnostic.prepareDebug("inputparams in notification "+inputParams).log();
		//System.out.println("#######NOTIFAlert"+inputParams);
		if (inputParams == null)
			return;
		try {
			pushToDataBase(inputParams, event);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error in recording notification", e).log();
		}

	}

	private static void pushToDataBase(Map<String, String> inmap, Event event) {
		try {
			String notificationSubject = null;
			String notificationText = null;
			String isRead = null;
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			String user = null;

			notificationSubject = inmap.get(AlertConstants.NOTIFY_SUB);
			notificationText = inmap.get(AlertConstants.NOTIFY_TEXT);
			isRead = inmap.get(AlertConstants.ISREAD);
			user = inmap.get(AlertConstants.USER);
			Map<String, Object> notoficationquery = new HashMap<>();
			notoficationquery.put("notificationSubject", notificationSubject);
			notoficationquery.put("notificationText", notificationText);
			notoficationquery.put("isRead", isRead);
			notoficationquery.put("receivedDate", ourJavaTimestampObject);
			notoficationquery.put("user", user);
			notoficationquery.put("notificationCategory", event.getAlertcategory());
			notoficationquery.put("companyLegalUnit", event.getCompanyLegalUnit());
			Map<String, Object> usernotificationquery = new HashMap<>();
			usernotificationquery.put("notification_id", "");
			usernotificationquery.put("user_id", event.getCustomerid());
			usernotificationquery.put("isRead", "0");
			usernotificationquery.put("receivedDate", ourJavaTimestampObject);
			usernotificationquery.put("companyLegalUnit", event.getCompanyLegalUnit());
			NotificationQueryClass no = new NotificationQueryClass(notoficationquery, usernotificationquery);
			event.setNotificationobj2(no);
		} catch (Exception e) {
			alert.prepareError("Exception occured:", e).log();
		}
	}

	private static Map<String, String> buildPayload(Event event, Map<String, JsonObject> commtemplate) {

		new JsonObject();
		if (!commtemplate.containsKey(AlertConstants.CH_NOTIFICATION_CENTER))
			return null;
		JsonObject notifyparams = commtemplate.get(AlertConstants.CH_NOTIFICATION_CENTER);
		String subject = replacespecials(AlertsUtils.getJsonObjects(notifyparams, AlertConstants.SUBJECT, false));
		String text = replacespecials(AlertsUtils.getJsonObjects(notifyparams, AlertConstants.TEXT, false));
		Map<String, String> inputparams = new HashMap<>();
		inputparams.put(AlertConstants.NOTIFY_SUB, subject);
		inputparams.put(AlertConstants.NOTIFY_TEXT, text);
		inputparams.put(AlertConstants.ISREAD, AlertConstants.ZERO);
		inputparams.put(AlertConstants.USER, event.getUsername());
		return inputparams;
	}

}
