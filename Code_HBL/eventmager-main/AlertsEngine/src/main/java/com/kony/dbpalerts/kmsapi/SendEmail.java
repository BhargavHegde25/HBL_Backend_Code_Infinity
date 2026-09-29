package com.kony.dbpalerts.kmsapi;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class SendEmail {
	private SendEmail() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static JsonObject invokeMail(Event event, Map<String, JsonObject> commtemplate) throws Exception {
		JsonObject res = null;
		Map<String, Object> inputparams = buildPayload(event, commtemplate);
		if (inputparams == null || inputparams.isEmpty()) {
			event.setMailmessage("Error in building KMS email payload");
			return null;
		}
		try {
			diagnostic.prepareDebug("inputparams in email " + inputparams).log();
			//System.out.println("#######Alert"+inputparams);
			String responseString = AlertsUtils.callInternalServiceAndGetJson(inputparams,
					AlertConstants.KMSINVOKESERVICE, AlertConstants.SENDEMAILOPERATION, null);
			res = new JsonParser().parse(responseString).getAsJsonObject();
			if (res != null && res.has(AlertConstants.DBPERRMSG)) {
				event.setMailmessage(res.get(AlertConstants.DBPERRMSG).getAsString());
				return null;
			}
			if (res != null && res.has(AlertConstants.REFERENCENUMBER)) {
				res.addProperty("id", res.get(AlertConstants.REFERENCENUMBER).getAsString());
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured in submitting mail", e).log();
		}
		return res;
	}

	private static Map<String, Object> buildPayload(Event event, Map<String, JsonObject> commtemplate) {

		Map<String, Object> inputParams = new HashMap<>();
		JsonObject inputObj = new JsonObject();
		JsonObject logObj = new JsonObject();
		try {
			JsonObject emailservicerequest = new JsonObject();
			if (event.getEmail() == null || !commtemplate.containsKey(AlertConstants.CH_EMAIL))
				return null;
			JsonObject mailparams = commtemplate.get(AlertConstants.CH_EMAIL);

			String text = AlertsUtils.getJsonObjects(mailparams, AlertConstants.TEXT, false);
			String subject = AlertsUtils.getJsonObjects(mailparams, AlertConstants.SUBJECT, false);
			String sendername = AlertsUtils.getJsonObjects(mailparams, AlertConstants.SENDERNAME, false);
			String sendermail = AlertsUtils.getJsonObjects(mailparams, AlertConstants.SENDERMAIL, false);
			JsonObject email = new JsonObject();
			JsonObject emails = new JsonObject();
			JsonObject recipients = new JsonObject();
			JsonArray recipient = new JsonArray();
			recipients.add(AlertConstants.RECIPIENT, recipient);
			for (String emailid : event.getEmail()) {
				JsonObject recipientto = new JsonObject();
				recipientto.addProperty(AlertConstants.TYPE, AlertConstants.TO);
				recipientto.addProperty(AlertConstants.EMAILID, emailid);
				recipient.add(recipientto);
			}
			email.add(AlertConstants.RECIPIENTS, recipients);
			if (sendermail != null && !sendermail.equals(AlertConstants.NULL)
					&& !sendermail.equals(AlertConstants.EMPTYSTRING))
				email.addProperty(AlertConstants.SENDEREMAIL_LOWER, sendermail);
			if (sendername == null || sendername.equals(AlertConstants.NULL)
					|| sendername.equals(AlertConstants.EMPTYSTRING))
				sendername = AlertsUtils.getConfigProperty("ALERT_EMAIL_SENDER_NAME");
			if (sendername != null)
				email.addProperty(AlertConstants.SENDERNAME_LOWER, sendername);
			email.addProperty(AlertConstants.SUBJECT_LOWER, subject);
			email.addProperty(AlertConstants.CONTENT, text);
			email.addProperty(AlertConstants.PRIORITY, AlertConstants.TRUE);
			emails.add(AlertConstants.EMAIL, email);

			emailservicerequest.add(AlertConstants.EMAILS, emails);
			inputObj.add(AlertConstants.EMAILSERVICEREQUEST, emailservicerequest);
			inputParams.put(AlertConstants.INPUTPARAMS, inputObj);
			logObj.addProperty(AlertConstants.LEGALENTITYID, event.getCompanyLegalUnit());
			inputParams.put(AlertConstants.LOGPARAMS, logObj);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
		return inputParams;
	}

}