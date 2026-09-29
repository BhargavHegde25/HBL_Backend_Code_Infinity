package com.kony.dbpalerts.kmsapi;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;

public class SendSMS {
	private SendSMS() {
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static JsonObject invokeSms(Event event, Map<String, JsonObject> commtemplate) throws Exception {
		JsonObject res = null;
		diagnostic.prepareDebug("###SendSMS.invokeSms EventData###" + event).log();
		//System.out.println("###SendSMS.invokeSms EventData###"+event);
		Map<String, Object> inputparams = buildPayload(event, commtemplate);
		if (inputparams == null || inputparams.isEmpty()) {
			event.setSmsmessage("Error in building KMS sms payload");
			return null;
		}
		try {
			diagnostic.prepareDebug("###SendSMS.invokeSms InputParams### " + inputparams).log();
			//System.out.println("###SendSMS.invokeSms InputParams###"+inputparams);
			String responseString = AlertsUtils.callInternalServiceAndGetJson(inputparams,
					AlertConstants.KMSINVOKESERVICE, AlertConstants.SENDSMSOPERATION, null);
			res = new JsonParser().parse(responseString).getAsJsonObject();
			if (res != null && res.has(AlertConstants.DBPERRMSG)) {
				event.setSmsmessage(res.get(AlertConstants.DBPERRMSG).getAsString());
				return null;
			}
			if (res != null && res.has(AlertConstants.REFERENCENUMBER)) {
				res.addProperty("id", res.get(AlertConstants.REFERENCENUMBER).getAsString());
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured in submitting sms", e).log();
		}
		return res;
	}

	private static Map<String, Object> buildPayload(Event event, Map<String, JsonObject> commtemplate) {
		Map<String, Object> inputparams = new HashMap<>();
		JsonObject inputObj = new JsonObject();
		JsonObject logObj = new JsonObject();
		try {
			JsonArray messagearray = new JsonArray();
			JsonObject smsservicerequest = new JsonObject();
			if (event.getPhone() == null || !commtemplate.containsKey(AlertConstants.CH_SMS))
				return null;

			JsonObject smsparams = commtemplate.get(AlertConstants.CH_SMS);
			String text = "";
			text = AlertsUtils.getJsonObjects(smsparams, AlertConstants.TEXT, true);
			JsonObject message = new JsonObject();
			JsonObject recipients = new JsonObject();
			JsonArray recipientTo = new JsonArray();
			JsonObject messages = new JsonObject();
			for (String phone : event.getPhone()) {
				JsonObject recipient = new JsonObject();
				recipient.addProperty(AlertConstants.MOBILE, phone);
				recipientTo.add(recipient);
			}
			recipients.add(AlertConstants.RECIPIENT, recipientTo);
			message.add(AlertConstants.RECIPIENTS, recipients);
			message.addProperty(AlertConstants.STARTTIMESTAMP, "0");
			message.addProperty(AlertConstants.EXPIRYTIMESTAMP, "0");
			message.addProperty(AlertConstants.PRIORITYSERVICE, "true");
			message.addProperty(AlertConstants.CONTENT, text);
			messages.add(AlertConstants.MESSAGE, message);

			smsservicerequest.add(AlertConstants.MESSAGES, messages);
			messagearray.add(messages);
			inputObj.add(AlertConstants.SMSSERVICEREQUEST, smsservicerequest);
			inputparams.put(AlertConstants.INPUTPARAMS, inputObj);
			logObj.addProperty(AlertConstants.LEGALENTITYID, event.getCompanyLegalUnit());
			inputparams.put(AlertConstants.LOGPARAMS, logObj);
		} catch (Exception e) {
			alert.prepareError("Error occured in building payload").log();
		}
		return inputparams;
	}

}