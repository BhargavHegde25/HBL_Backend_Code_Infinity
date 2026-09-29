package com.dbp.campaigndeliveryengine.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.campaigndeliveryengine.businessdelegate.api.ServeEventBusinessDelegate;
import com.dbp.campaigndeliveryengine.dtoclasses.CommunicationNodeDTO;
import com.dbp.campaigndeliveryengine.dtoclasses.CustomerInfoDTO;
import com.dbp.campaigndeliveryengine.dtoclasses.EventDTO;
import com.dbp.campaigndeliveryengine.kmsapi.SendEmail;
import com.dbp.campaigndeliveryengine.kmsapi.SendPushNotification;
import com.dbp.campaigndeliveryengine.kmsapi.SendSMS;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineConstants;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineUtils;
import com.dbp.campaigndeliveryengine.utils.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServeEventBusinessDelegateImpl implements ServeEventBusinessDelegate {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void serveEvent(JsonElement eventjson, String customerid, String corecustomerid,
			CommunicationNodeDTO commdata, CustomerInfoDTO customer) {

		Map<String, String> alertcontentfieldsfromeventdata = new HashMap<>();
		JsonUtils.getAllPairsFromJson(eventjson, alertcontentfieldsfromeventdata);
		EventDTO event = new EventDTO(null, corecustomerid, customerid);
		updateActiveChannels(alertcontentfieldsfromeventdata, event, customer);
		pushtoKMS(event, commdata);
	}

	private static void pushtoKMS(EventDTO event, CommunicationNodeDTO commdata) {
		try {
			diagnostic.prepareDebug("event.isSms()" + event.isSms()).log();
			diagnostic.prepareDebug("event.isEmail()"+ event.isEmail()).log();
			diagnostic.prepareDebug("event.isPush()"+ event.isPush()).log();

			if (event.isSms()) {
				SendSMS.sendSMS(event, commdata);
			}
			if (event.isEmail()) {
				SendEmail.sendMail(event, commdata);
			}
			if (event.isPush()) {
				SendPushNotification.sendPushNotification(event);
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in pushing to KMS", e).log();
		}

	}

	private static void updateActiveChannels(Map<String, String> alertcontentfieldsfromeventdata, EventDTO event,
			CustomerInfoDTO customer) {
		try {
			if (!alertcontentfieldsfromeventdata.containsKey(CampaignDeliveryEngineConstants.MESSAGE_CONTENT)
					|| alertcontentfieldsfromeventdata.get(CampaignDeliveryEngineConstants.MESSAGE_CONTENT) == null)
				return;
			String messagecontent = alertcontentfieldsfromeventdata
					.get(CampaignDeliveryEngineConstants.MESSAGE_CONTENT);
			JsonElement messageelement = new JsonParser().parse(messagecontent);
			if (!messageelement.isJsonObject())
				return;
			JsonObject messageobj = messageelement.getAsJsonObject();
			JsonObject channel;
			channel = (messageobj.get(CampaignDeliveryEngineConstants.EMAIL_LOWER) != null
					&& messageobj.get(CampaignDeliveryEngineConstants.EMAIL_LOWER).isJsonObject())
							? messageobj.get(CampaignDeliveryEngineConstants.EMAIL_LOWER).getAsJsonObject()
							: null;
			if (channel != null) {
				updateChannelCommunicationData(event, channel, CampaignDeliveryEngineConstants.EMAIL_LOWER,
						alertcontentfieldsfromeventdata, customer);
				event.setEmail(true);
			}
			channel = (messageobj.get(CampaignDeliveryEngineConstants.SMS_LOWER) != null
					&& messageobj.get(CampaignDeliveryEngineConstants.SMS_LOWER).isJsonObject())
							? messageobj.get(CampaignDeliveryEngineConstants.SMS_LOWER).getAsJsonObject()
							: null;
			if (channel != null) {
				updateChannelCommunicationData(event, channel, CampaignDeliveryEngineConstants.SMS_LOWER,
						alertcontentfieldsfromeventdata, customer);
				event.setSms(true);

			}
			channel = (messageobj.get(CampaignDeliveryEngineConstants.PUSH_LOWER) != null
					&& messageobj.get(CampaignDeliveryEngineConstants.PUSH_LOWER).isJsonObject())
							? messageobj.get(CampaignDeliveryEngineConstants.PUSH_LOWER).getAsJsonObject()
							: null;
			if (channel != null) {
				updateChannelCommunicationData(event, channel, CampaignDeliveryEngineConstants.PUSH_LOWER,
						alertcontentfieldsfromeventdata, customer);
				event.setPush(true);
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in parsing external communication template:", e).log();
		}
	}

	private static void updateChannelCommunicationData(EventDTO event, JsonObject externalchanneldata, String medium,
			Map<String, String> alertcontentfieldsfromeventdata, CustomerInfoDTO customer) {
		String subject = null;
		String text = null;
		StringBuilder texttofetchdynamics = new StringBuilder();
		if (externalchanneldata.get(CampaignDeliveryEngineConstants.SUBJECT) != null) {
			subject = externalchanneldata.get(CampaignDeliveryEngineConstants.SUBJECT).getAsString();
			subject = CampaignDeliveryEngineUtils.decodeFromBase64(subject);
		}
		if (externalchanneldata.get(CampaignDeliveryEngineConstants.TEXT) != null) {
			text = externalchanneldata.get(CampaignDeliveryEngineConstants.TEXT).getAsString();

			text = CampaignDeliveryEngineUtils.decodeFromBase64(text);
		}
		if (subject != null)
			texttofetchdynamics.append(subject);
		if (text != null)
			texttofetchdynamics.append(text);
		List<String> dynamicfields = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\[#\\](.*?)\\[/#\\]");
		Matcher matcher = pattern.matcher(texttofetchdynamics.toString());
		while (matcher.find()) {
			dynamicfields.add(matcher.group(1));
		}

		if (medium.equals(CampaignDeliveryEngineConstants.EMAIL_LOWER)) {
			event.setEmailsubject(
					replaceDynamicFeilds(subject, alertcontentfieldsfromeventdata, dynamicfields, customer));
			event.setEmailtext(replaceDynamicFeilds(text, alertcontentfieldsfromeventdata, dynamicfields, customer));
		} else if (medium.equals(CampaignDeliveryEngineConstants.SMS_LOWER)) {
			event.setSmssubject(
					replaceDynamicFeilds(subject, alertcontentfieldsfromeventdata, dynamicfields, customer));
			event.setSmstext(replaceDynamicFeilds(text, alertcontentfieldsfromeventdata, dynamicfields, customer));
		} else if (medium.equals(CampaignDeliveryEngineConstants.PUSH_LOWER)) {
			event.setPushsubject(
					replaceDynamicFeilds(subject, alertcontentfieldsfromeventdata, dynamicfields, customer));
			event.setPushtext(replaceDynamicFeilds(text, alertcontentfieldsfromeventdata, dynamicfields, customer));
		}
	}

	private static String replaceDynamicFeilds(String text, Map<String, String> alertcontentfieldsfromeventdata,
			List<String> alertcontentfieldsname, CustomerInfoDTO customer) {
		if (text == null)
			return text;
		if (alertcontentfieldsfromeventdata != null && !alertcontentfieldsfromeventdata.isEmpty())
			for (String key : alertcontentfieldsname) {
				if (key.equalsIgnoreCase(("firstname"))) {
					text = text.replace("[#]" + key + "[/#]", customer.getFname());
				} else if (key.equalsIgnoreCase("lastname")) {
					text = text.replace("[#]" + key + "[/#]", customer.getLname());
				} else if (alertcontentfieldsfromeventdata.containsKey(key.toLowerCase())) {
					text = text.replace("[#]" + key + "[/#]", alertcontentfieldsfromeventdata.get(key.toLowerCase()));
				} else {
					text = text.replace("[#]" + key + "[/#]", "");
				}
			}
		return text;
	}

}
