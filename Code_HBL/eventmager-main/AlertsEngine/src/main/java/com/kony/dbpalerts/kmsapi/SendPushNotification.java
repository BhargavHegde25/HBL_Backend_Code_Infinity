package com.kony.dbpalerts.kmsapi;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;

public class SendPushNotification {
	private SendPushNotification() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static JsonObject invokePush(Event event, Map<String, JsonObject> commtemplate) throws Exception {
		JsonObject res = null;
		Map<String, Object> inputparams = buildPayload(event, commtemplate);
		if (inputparams == null || inputparams.isEmpty()) {
			event.setPushmessage("Error in building KMS Push notification payload");
			return null;
		}
		try {
			diagnostic.prepareDebug("inputparams in pushnotification "+inputparams).log();
			//System.out.println("#######PUSHNOTIFAlert"+inputparams);
			String responseString = AlertsUtils.callInternalServiceAndGetJson(inputparams,
					AlertConstants.KMSINVOKESERVICE, AlertConstants.SENDPUSHNOTIFICATIONOPERATION, null);
			res = new JsonParser().parse(responseString).getAsJsonObject();
			if (res != null && res.has(AlertConstants.DBPERRMSG)) {
				event.setPushmessage(res.get(AlertConstants.DBPERRMSG).getAsString());
				return null;
			}
			if (res != null && res.has(AlertConstants.REFERENCENUMBER)) {
				res.addProperty("id", res.get(AlertConstants.REFERENCENUMBER).getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured in submitting push notification", e).log();
		}
		return res;
	}

	private static Map<String, Object> buildPayload(Event event, Map<String, JsonObject> commtemplate) {
		Map<String, Object> inputparams = new HashMap<>();
		JsonObject inputObj = new JsonObject();
		JsonObject logObj = new JsonObject();
		try {
			String key = null;
			if (event.getAppid() == null)
				key = AlertConstants.DBX_KMS_APPKEY;
			else
				key = event.getAppid() + "_KMS_APPKEY";

			String ufid = event.getCustomerid();
			JsonObject messagerequest = new JsonObject();

			if (!commtemplate.containsKey(AlertConstants.CH_PUSH_NOTIFICATION))
				return null;
			JsonObject pushparams = commtemplate.get(AlertConstants.CH_PUSH_NOTIFICATION);
			String text = AlertsUtils.getJsonObjects(pushparams, AlertConstants.TEXT, false);
			String subject = AlertsUtils.getJsonObjects(pushparams, AlertConstants.SUBJECT, false);
			JsonObject messages = new JsonObject();
			JsonObject message = new JsonObject();
			JsonObject subscribers = new JsonObject();
			JsonObject platformspecificprops = new JsonObject();
			JsonObject subscriber = new JsonObject();
			JsonObject content = new JsonObject();
			content.addProperty(AlertConstants.MIMETYPE, AlertConstants.TEXT_PLAIN);
			content.addProperty(AlertConstants.PRIORITYSERVICE, AlertConstants.TRUE);
			content.addProperty(AlertConstants.DATA, text);

			platformspecificprops.addProperty(AlertConstants.TITLE, subject);
			subscriber.addProperty(AlertConstants.UFID, ufid);
			subscribers.add(AlertConstants.SUBSCRIBER, subscriber);
			message.add(AlertConstants.CONTENT, content);
			message.addProperty(AlertConstants.TYPE, AlertConstants.PUSH);
			message.add(AlertConstants.SUBSCRIBERS, subscribers);

			message.add(AlertConstants.PLATFORMSPECIFICPROPS, platformspecificprops);
			messages.add(AlertConstants.MESSAGE, message);
			messagerequest.add(AlertConstants.MESSAGES, messages);
			messagerequest.addProperty(AlertConstants.APPID, AlertsUtils.getConfigProperty(key));
			inputObj.add(AlertConstants.MESSAGEREQUEST, messagerequest);
			inputparams.put(AlertConstants.INPUTPARAMS, inputObj);
			logObj.addProperty(AlertConstants.LEGALENTITYID, event.getCompanyLegalUnit());
			inputparams.put(AlertConstants.LOGPARAMS, logObj);
		} catch (Exception e) {
			alert.prepareError("Error in building payload", e).log();
		}
		return inputparams;
	}

}