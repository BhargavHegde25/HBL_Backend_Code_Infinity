package com.kony.dbpalerts.kmsapi;

import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonObject;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.JsonParsingEngine;

public class KmsInvoke {
	private KmsInvoke() {
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static void runForMail(Event event, Map<String, JsonObject> commtemplate) {
		try {
			if (event.getChemail()) {
				JsonObject mailresponce = SendEmail.invokeMail(event, commtemplate);
				if (mailresponce != null)
					pushResponceToEvents(mailresponce, AlertConstants.CH_EMAIL, event);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured :", e).log();
		}
	}

	private static void runForSMS(Event event, Map<String, JsonObject> commtemplate) {
		try {
			if (event.getChsms()) {
				JsonObject smsresponce = SendSMS.invokeSms(event, commtemplate);
				if (smsresponce != null)
					pushResponceToEvents(smsresponce, AlertConstants.CH_SMS, event);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured:", e).log();
		}
	}

	private static void runForPUSH(Event event, Map<String, JsonObject> commtemplate) {
		try {
			if (event.getAppid() != null && event.getChpush()) {
				JsonObject pushresponce = SendPushNotification.invokePush(event, commtemplate);
				if (pushresponce != null)
					pushResponceToEvents(pushresponce, AlertConstants.CH_PUSH_NOTIFICATION, event);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured : ", e).log();
		}
	}

	private static void runForNotification(Event event, Map<String, JsonObject> commtemplate) {
		try {
			if (event.getAppid() != null && event.getChnotification()) {
				Notification.invokeNotification(event, commtemplate);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured  :", e).log();
		}
	}

	public static void processChannels(String channeltype, Event event, Map<String, JsonObject> commtemplate) {
		if (channeltype.equals(AlertConstants.CH_EMAIL)) {
			runForMail(event, commtemplate);
		}
		if (channeltype.equals(AlertConstants.CH_SMS)) {
			runForSMS(event, commtemplate);
		}
		if (channeltype.equals(AlertConstants.CH_PUSH_NOTIFICATION)) {
			runForPUSH(event, commtemplate);
		}
		if (channeltype.equals(AlertConstants.CH_NOTIFICATION_CENTER)) {
			runForNotification(event, commtemplate);
		}
	}

	private static void pushResponceToEvents(JsonObject je, String type, Event event) {
		try {
			String id = "";
			String response = "";
			response = JsonParsingEngine.getStringFromJsonObject(je, "message", true);
			id = JsonParsingEngine.getStringFromJsonObject(je, "id", true);
			if (type.equals(AlertConstants.CH_SMS)) {
				event.setSmsrefid(id);
				event.setSmsmessage(response);
			}
			if (type.equals(AlertConstants.CH_EMAIL)) {
				event.setMailrefid(id);
				event.setMailmessage(response);
			}
			if (type.equals(AlertConstants.CH_PUSH_NOTIFICATION)) {
				event.setPushrefid(id);
				event.setPushmessage(response);
			}
			if (type.equals(AlertConstants.CH_NOTIFICATION_CENTER)) {
				event.setNotificationrefid(id);
				event.setNotificationmessage(response);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured    ", e).log();
		}
	}
}