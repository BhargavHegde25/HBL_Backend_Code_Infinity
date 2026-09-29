package com.kony.campaign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.common.CampaignConstants.ExternalChannels;
import com.kony.campaign.common.ErrorCodes;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.dto.CampaignRequestType;
import com.kony.campaign.dto.EventDTO;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.exceptions.MiddlewareException;

public final class ExternalResponseProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static boolean pushToExternalChannels(CampaignRequest campaignRequest, Record campaignRecord,
			List<String> channelList) throws CampaignException {
		try {
			if(diagnostic.isDebugEnabled()){
			 diagnostic.prepareDebug("Entered external Response proessor").log();
			}
			JsonObject messageContentObj = 
					prepareMessageContentForEvent(campaignRecord, channelList);
			if(!messageContentObj.entrySet().isEmpty()) {
				JsonObject event = prepareEvent(campaignRequest, messageContentObj,
						campaignRecord.getParamByName(CampaignConstants.GETCAMPAIGNS_CAMPAIGN_ID).getValue());				
				CampaignUtil.invokeService(CampaignConstants.PUSH_EVENT_SERVICENAME,
						CampaignConstants.PUSH_EVENT_OPERATION_NAME,
						getInputParamsToPushToQueue(campaignRequest, event));
			}
		} catch(MiddlewareException e) {
			alert.prepareError(ErrorCodes.ERR_17007.getMessage()+ e).log();
			throw new CampaignException(ErrorCodes.ERR_17007.getMessage(),e,ErrorCodes.ERR_17007.getErrorCode());
		}catch(Exception e) {
			alert.prepareError(ErrorCodes.ERR_17008.getMessage()+ e).log();
			throw new CampaignException(ErrorCodes.ERR_17008.getMessage(),e,ErrorCodes.ERR_17008.getErrorCode());
		}
		return true;
	}

	private static Map<String, Object> getInputParamsToPushToQueue(CampaignRequest campaignRequest, JsonObject event) {
		Map<String, Object> inputMap;
		JsonArray eventsArr = new JsonArray();
		eventsArr.add(event);
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("Input for queue master"+eventsArr.toString() ).log();
		}
		inputMap = new HashMap<>();
		String derivedToken = deriveToken( eventsArr.toString());
		inputMap.put(CampaignConstants.PARAM_TOKEN, derivedToken);
		inputMap.put(CampaignConstants.PARAM_EVENTS, eventsArr);
		inputMap.put(CampaignConstants.PARAM_PRODUCER, campaignRequest.getEvent().getEventId());
		return inputMap;
	}

	private static JsonObject prepareEvent(CampaignRequest campaignRequest, JsonObject messageContentObj, String campaignId) {
		JsonObject event = new JsonObject();
		event.addProperty(CampaignConstants.EVENT_TYPE, CampaignConstants.EVENT_CAMPAIGN_TYPE);
		event.addProperty(CampaignConstants.EVENT_SUBTYPE, CampaignConstants.EVENT_CAMPAIGN_SUB_TYPE);
		event.addProperty(CampaignConstants.EVENT_STATUS, CampaignConstants.EVENT_STATUS_VALUE);
		event.add(CampaignConstants.EVENT_DATA, prepareEventDataObj(campaignRequest, messageContentObj, campaignId));
		event.add(CampaignConstants.EVENT_OTHER_DATA, prepareOtherDataObj(campaignRequest));
		return event;
	}

	private static JsonObject prepareEventDataObj(CampaignRequest campaignRequest, JsonObject messageContentObj,
			String campaignId) {
		JsonObject eventData = new JsonObject();
		eventData.add(CampaignConstants.EVENT_MESSAGE_CONTENT, messageContentObj);
		addCustomParamsObj(campaignRequest, campaignId, eventData);
		return eventData;
	}

	private static JsonObject prepareOtherDataObj(CampaignRequest campaignRequest) {
		JsonObject otherData = new JsonObject();
		otherData.addProperty(CampaignConstants.EVENT_CUSTOMER_ID, campaignRequest.getEvent().getCoreCustId());
		return otherData;
	}


	private static void addCustomParamsObj(CampaignRequest campaignRequest, String campaignId,
			JsonObject eventData) {
		JsonObject customParams = null;
		EventDTO eventFromCReq = campaignRequest.getEvent();
		if(eventFromCReq.getEventData() != null) {
			JsonObject inputEventData = eventFromCReq.getEventData();
			if(inputEventData.has(CampaignConstants.EVENT_CUSTOM_PARAMS)) {
				customParams = inputEventData.get(CampaignConstants.EVENT_CUSTOM_PARAMS).getAsJsonObject();
			}
		}
		customParams = customParams != null ? customParams : new JsonObject();
		customParams.addProperty(CampaignConstants.RESPONSE_CAMPAIGN_ID, campaignId);
		if(CampaignRequestType.ONLINE_PLACEHOLDER_EVENT.equals(campaignRequest.getRequestType())) {
			customParams.addProperty(CampaignConstants.RESPONSE_PLACEHOLDER_CODE, eventFromCReq.getPlaceholderCode());
			customParams.addProperty(CampaignConstants.RESPONSE_SCALE_CODE, eventFromCReq.getScale());
		}
		eventData.add(CampaignConstants.EVENT_CUSTOM_PARAMS, 
				customParams);
	}

	private static JsonObject prepareMessageContentForEvent(Record campaignRecord, List<String> channelList) {
		JsonObject messageContentObj = new JsonObject();
		for (ExternalChannels extchl : ExternalChannels.values()) {
			if(channelList.contains(extchl.getExternalName())) {
				Optional<Record> optRecord = campaignRecord.getDatasetById(CampaignConstants.GETCAMPAIGNS_OFFLINE_TEMPLATE).getAllRecords().stream()
						.filter(offlinerec -> offlinerec.getParamByName(
								CampaignConstants.GETCAMPAIGNS_CHANNEL_SUB_TYPE).getValue().equalsIgnoreCase(extchl.getExternalName()))
						.findAny();
		        if (optRecord.isPresent()) {
					Record channelRec = optRecord.get();
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty(CampaignConstants.EVENT_SUBJECT,
							channelRec.getParam(CampaignConstants.GETCAMPAIGNS_SUBJECT).getValue());
					jsonObject.addProperty(CampaignConstants.EVENT_TEXT,
							channelRec.getParam(CampaignConstants.GETCAMPAIGNS_CONTENT).getValue());
					messageContentObj.add(extchl.name().toLowerCase(), jsonObject);
				}
			}
		}
		return messageContentObj;
	}

	private static String deriveToken(String events) {
		String secret = CampaignUtil.getServerProperty("QUEUEMASTER_SHARED_SECRET", null);
		if (secret == null || secret.length() == 0) {
			throw new RuntimeException("QueueMaster shared secret has not been configured!");
		}
		// Hashing using Guava lib
		String eventsHash = Hashing.sha512().hashString(events, Charsets.UTF_8).toString(); 
		String saltedSecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
	}

	private ExternalResponseProcessor() { }
}


