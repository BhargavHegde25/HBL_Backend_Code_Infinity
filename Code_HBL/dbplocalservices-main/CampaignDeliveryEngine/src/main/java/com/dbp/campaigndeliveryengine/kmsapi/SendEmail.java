package com.dbp.campaigndeliveryengine.kmsapi;

import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.dbp.campaigndeliveryengine.dtoclasses.CommunicationNodeDTO;
import com.dbp.campaigndeliveryengine.dtoclasses.EventDTO;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineConstants;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineUtils;

public class SendEmail {
	private SendEmail() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static JsonObject sendMail(EventDTO event, CommunicationNodeDTO commdata) throws Exception {
		JsonObject res = new JsonObject();
		Map<String, Object> inputparams = buildPayload(event, commdata);
		JsonObject logparams = CampaignDeliveryEngineUtils.buildlogparams();
		inputparams.put("logparams", logparams);
		if (inputparams.isEmpty())
			return res;
		try {
			res = new JsonParser()
					.parse(ResultToJSON.convert(CampaignDeliveryEngineUtils.callInternalService(inputparams,
							CampaignDeliveryEngineConstants.KMSINVOKESERVICE,
							CampaignDeliveryEngineConstants.SENDEMAILOPERATION, null)))
					.getAsJsonObject();
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured in submitting mail", e).log();
		}
		diagnostic.prepareDebug("Response for mail:" + res).log();
		return res;
	}

	private static Map<String, Object> buildPayload(EventDTO event, CommunicationNodeDTO commdata) {

		Map<String, Object> inputParams = new HashMap<>();
		JsonObject inputObj = new JsonObject();

		try {
			JsonObject emailservicerequest = new JsonObject();
			/*
			 * if (commdata.getEmail() == null) return inputParams;
			 */
			String text = event.getEmailtext();
			String subject = event.getEmailsubject();
			String sendername = "DBP";
			JsonObject email = new JsonObject();
			JsonObject emails = new JsonObject();
			JsonObject recipients = new JsonObject();
			JsonArray recipient = new JsonArray();
			recipients.add(CampaignDeliveryEngineConstants.RECIPIENT, recipient);
			JsonObject recipientto = new JsonObject();
			recipientto.addProperty(CampaignDeliveryEngineConstants.TYPE, CampaignDeliveryEngineConstants.TO);
			recipientto.addProperty(CampaignDeliveryEngineConstants.EMAILID, commdata.getEmail());
			recipient.add(recipientto);
			email.add(CampaignDeliveryEngineConstants.RECIPIENTS, recipients);
			if (!sendername.equals(CampaignDeliveryEngineConstants.NULL)
					&& !sendername.equals(CampaignDeliveryEngineConstants.EMPTYSTRING))
				email.addProperty(CampaignDeliveryEngineConstants.SENDERNAME_LOWER, sendername);
			email.addProperty(CampaignDeliveryEngineConstants.SUBJECT_LOWER, (subject));
			email.addProperty(CampaignDeliveryEngineConstants.CONTENT, (text));
			email.addProperty(CampaignDeliveryEngineConstants.PRIORITY, CampaignDeliveryEngineConstants.TRUE);
			emails.add(CampaignDeliveryEngineConstants.EMAIL, email);

			emailservicerequest.add(CampaignDeliveryEngineConstants.EMAILS, emails);
			inputObj.add(CampaignDeliveryEngineConstants.EMAILSERVICEREQUEST, emailservicerequest);
			inputParams.put(CampaignDeliveryEngineConstants.INPUTPARAMS, inputObj);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		return inputParams;
	}

}
