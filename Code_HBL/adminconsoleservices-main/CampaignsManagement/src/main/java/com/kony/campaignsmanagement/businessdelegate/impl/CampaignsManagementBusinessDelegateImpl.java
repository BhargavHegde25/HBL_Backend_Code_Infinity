package com.kony.campaignsmanagement.businessdelegate.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.campaignsmanagement.backenddelegate.api.CampaignsManagementBackendDelegate;
import com.kony.campaignsmanagement.businessdelegate.api.CampaignsManagementBusinessDelegate;
import com.kony.campaignsmanagement.dto.ProfileCampaignDTO;
import com.kony.campaignsmanagement.dto.ProfileConditionDTO;
import com.kony.campaignsmanagement.dto.ProfileDTO;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class CampaignsManagementBusinessDelegateImpl implements CampaignsManagementBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule(CMConstants.INFINITY, CMConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(CMConstants.INFINITY, CMConstants.SPOTLIGHT);

	private static final String EVENT_TRIGGERID_LIST = "eventTriggerIdList";
	private static final String PROFILEID_LIST = "profileIdList";
	private static final String CHANNEL_TYPE = "channelType";
	private static final String OFFLINE_TEMPLATE = "offlineTemplate";
	private static final String ONLINE_CONTENT = "onlineContent";
	private static final String CHANNEL_DETAILS = "channelDetails";

	private static final String CAMPAIGN_NAME = "campaignName";
	private static final String CAMPAIGN_DESCRIPTION = "campaignDescription";
	private static final String CAMPAIGN_PRIORITY = "campaignPriority";
	private static final String START_DATE = "startDate";
	private static final String END_DATE = "endDate";
	private static final String CAMPAIGN_TYPE = "campaignType";
	private static final String OBJECTIVE_TYPE = "objectiveType";
	private static final String PRODUCTID = "productId";
	private static final String PRODUCT_GROUPID = "productGroupId";
	private static final String CAMPAIGN_STATUS = "campaignStatus";
	private static final String CAMPAIGN_ID = "campaignId";

	@Override
	public JSONArray getAllPlaceHolders(DataControllerRequest request) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getAllPlaceHolders(request);
	}

	@Override
	public JSONArray getAllPlaceHoldersMS(DataControllerRequest request) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getAllPlaceHoldersMS(request);
	}

	@Override
	public JSONArray getEventTriggersMS(DataControllerRequest request) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getEventTriggersMS(request);
	}

	@Override
	public JSONArray getEventTriggers(DataControllerRequest request) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getEventTriggers(request);
	}
	
	public JSONObject createCampaign(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
			throws DBPApplicationException {

		Map<String, Object> inputMap = new HashMap<>();
		try {
			String campaignBackend = (EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND"));

			if (StringUtils.isNotBlank(campaignBackend) && "MS".equalsIgnoreCase(campaignBackend)) {
				inputMap = postParametersMap;
			} else {
				processCampaignMetaData(postParametersMap, inputMap,true);
				processEventTriggerIdList(postParametersMap, inputMap);
				processProfileIdList(postParametersMap, inputMap);
				processChannelType(postParametersMap, inputMap);
				processOfflineTemplate(postParametersMap, inputMap);
				processOnlineContent(postParametersMap, inputMap);
				processChannelDetails(postParametersMap, inputMap);
			}
			CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BackendDelegateFactory.class)
					.getBackendDelegate(CampaignsManagementBackendDelegate.class);

			return backendDelegate.createCampaign(inputMap, headerMap);

		} catch (Exception e) {

			alert.prepareError("Unexpected Error in create infinity campaign: ", e).log();
		}
		return new JSONObject();
	}
	
	@Override
	public JSONObject updateCampaign(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
			throws DBPApplicationException {

		Map<String, Object> inputMap = new HashMap<>();
		try {
			String campaignBackend = (EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND"));

			if (StringUtils.isNotBlank(campaignBackend) && "MS".equalsIgnoreCase(campaignBackend)) {
				inputMap = postParametersMap;
			} else {
				processCampaignMetaData(postParametersMap, inputMap,false);
				processEventTriggerIdList(postParametersMap, inputMap);
				processProfileIdList(postParametersMap, inputMap);
				processChannelType(postParametersMap, inputMap);
				processOfflineTemplate(postParametersMap, inputMap);
				processOnlineContent(postParametersMap, inputMap);
				processChannelDetails(postParametersMap, inputMap);
			}
			CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BackendDelegateFactory.class)
					.getBackendDelegate(CampaignsManagementBackendDelegate.class);

			return backendDelegate.updateCampaign(inputMap, headerMap);

		} catch (Exception e) {

			alert.prepareError("Unexpected Error in create infinity campaign: ", e).log();
		}
		return new JSONObject();
	}
	
	
	public JSONObject getCampaigns(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
			throws DBPApplicationException {

		try {
			CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BackendDelegateFactory.class)
					.getBackendDelegate(CampaignsManagementBackendDelegate.class);

			return backendDelegate.getCampaigns(postParametersMap, headerMap);

		} catch (Exception e) {

			alert.prepareError("Unexpected Error in create infinity campaign: ", e).log();
		}
		return new JSONObject();
	}

	private void processChannelDetails(Map<String, Object> postParametersMap, Map<String, Object> inputMap) {
		if (!postParametersMap.containsKey(CHANNEL_DETAILS)) {
			inputMap.put(CHANNEL_DETAILS, "");
			return;
		}
		StringBuilder channelDetailsParam = new StringBuilder();
		String channelDetailsList = (String) postParametersMap.get(CHANNEL_DETAILS);

		JsonElement channelDetailsElement = new JsonParser().parse(channelDetailsList);
		if (!channelDetailsElement.isJsonNull() && channelDetailsElement.isJsonArray()) {
			for (JsonElement channelDetailselem : channelDetailsElement.getAsJsonArray()) {
				JsonObject channelDetailsobj = channelDetailselem.getAsJsonObject();
				if (!channelDetailsobj.has("channelSubType") || !channelDetailsobj.has("channelPriority"))
					continue;
				String channelSubType = channelDetailsobj.get("channelSubType").getAsString();
				String channelPriority = channelDetailsobj.get("channelPriority").getAsString();
				if (channelDetailsParam.length() == 0) {
					channelDetailsParam.append(channelSubType + "$" + channelPriority);
				} else {
					channelDetailsParam.append("|");
					channelDetailsParam.append(channelSubType + "$" + channelPriority);
				}
			}
			inputMap.put(CHANNEL_DETAILS, channelDetailsParam.toString());
		}

	}

	private void processOnlineContent(Map<String, Object> postParametersMap, Map<String, Object> inputMap) {
		if (!postParametersMap.containsKey(ONLINE_CONTENT)) {
			inputMap.put(ONLINE_CONTENT, "");
			return;
		}

		String onlineContent = (String) postParametersMap.get(ONLINE_CONTENT);
		try {
			onlineContent = URLDecoder.decode(onlineContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			alert.prepareError("Unexpected Error in create infinity campaign: ", e).log();
		}

		StringBuilder onlineContentParam = new StringBuilder();
		JsonElement onlineContentElement = new JsonParser().parse(onlineContent);
		if (!onlineContentElement.isJsonNull() && onlineContentElement.isJsonArray()) {
			for (JsonElement onlineContentelem : onlineContentElement.getAsJsonArray()) {
				JsonObject onlineContentobj = onlineContentelem.getAsJsonObject();
				if (!onlineContentobj.has("placeholderId") || !onlineContentobj.has("targetURL")
						|| !onlineContentobj.has("imageURL"))
					continue;
			String input=generateOnlineContentQueryParam(onlineContentobj, inputMap);
				if (onlineContentParam.length() == 0) {
					onlineContentParam.append(input);
				} else {
					onlineContentParam.append("|");
					onlineContentParam.append(input);
				}
				
			}
			inputMap.put(ONLINE_CONTENT, onlineContentParam.toString());
		}
	}

	private String generateOnlineContentQueryParam(JsonObject onlineContentobj, Map<String, Object> inputMap) {
		
		String placeholderId = onlineContentobj.get("placeholderId").getAsString();
		String targetURL = onlineContentobj.get("targetURL").getAsString();
		String imageURL = onlineContentobj.get("imageURL").getAsString();
		String callToActionButtonLabel = onlineContentobj.has("callToActionButtonLabel")
				? onlineContentobj.get("callToActionButtonLabel").getAsString()
				: "";
		String callToActionTargetURL = onlineContentobj.has("callToActionTargetURL")
				? onlineContentobj.get("callToActionTargetURL").getAsString()
				: "";
		String showReadLaterButton = onlineContentobj.has("showReadLaterButton")
				? onlineContentobj.get("showReadLaterButton").getAsString()
				: "";
		String showCloseIcon = onlineContentobj.has("showCloseIcon")
				? onlineContentobj.get("showCloseIcon").getAsString()
				: "";
		String bannerTitle = onlineContentobj.has("bannerTitle") ? onlineContentobj.get("bannerTitle").getAsString()
				: "";
		String bannerDescription = onlineContentobj.has("bannerDescription")
				? onlineContentobj.get("bannerDescription").getAsString()
				: "";
		String onlineContentId = "OC" + CommonUtilities.getUniqueNumericString(10);
		return onlineContentId + "$" + placeholderId + "$" + targetURL + "$" + imageURL + "$"
				+ callToActionButtonLabel + "$" + callToActionTargetURL + "$" + showReadLaterButton + "$"
				+ showCloseIcon + "$" + bannerTitle + "$" + bannerDescription;
	}

	private void processOfflineTemplate(Map<String, Object> postParametersMap, Map<String, Object> inputMap) {
		if (!postParametersMap.containsKey(OFFLINE_TEMPLATE)) {
			inputMap.put(OFFLINE_TEMPLATE, "");
			return;
		}
		StringBuilder offlineTemplateParam = new StringBuilder();
		String offlineTemplateList = (String) postParametersMap.get(OFFLINE_TEMPLATE);

		JsonElement offlineTemplateElement = new JsonParser().parse(offlineTemplateList);
		if (!offlineTemplateElement.isJsonNull() && offlineTemplateElement.isJsonArray()) {
			for (JsonElement offlineTemplateelem : offlineTemplateElement.getAsJsonArray()) {
				JsonObject offlineTemplateobj = offlineTemplateelem.getAsJsonObject();
				if (!offlineTemplateobj.has("channelSubType") || !offlineTemplateobj.has("subject")
						|| !offlineTemplateobj.has("messageContent"))
					continue;
				String channelSubType = offlineTemplateobj.get("channelSubType").getAsString();
				String subject = offlineTemplateobj.get("subject").getAsString();
				String messageContent = offlineTemplateobj.get("messageContent").getAsString();
				String offlineTemplateId = "OT" + CommonUtilities.getUniqueNumericString(10);
				if (offlineTemplateParam.length() == 0) {
					offlineTemplateParam.append(
							offlineTemplateId + "$" + channelSubType + "$" + subject + "$" + messageContent);
				} else {
					offlineTemplateParam.append("|");
					offlineTemplateParam.append(
							offlineTemplateId + "$" + channelSubType + "$" + subject + "$" + messageContent);
				}

			}
			inputMap.put(OFFLINE_TEMPLATE, offlineTemplateParam.toString());
		}

	}

	private void processChannelType(Map<String, Object> postParametersMap, Map<String, Object> inputMap) {
		if (!postParametersMap.containsKey(CHANNEL_TYPE)) {
			inputMap.put(CHANNEL_TYPE, "");
			return;
		}
		StringBuilder channelTypeParam = new StringBuilder();
		String channelTypeList = (String) postParametersMap.get(CHANNEL_TYPE);

		JsonElement channelTypeElement = new JsonParser().parse(channelTypeList);
		if (!channelTypeElement.isJsonNull() && channelTypeElement.isJsonArray()) {
			for (JsonElement channelTypeelem : channelTypeElement.getAsJsonArray()) {
				JsonObject channelTypeobj = channelTypeelem.getAsJsonObject();
				if (!channelTypeobj.has("type"))
					continue;

				String type = channelTypeobj.get("type").getAsString();

				if (channelTypeParam.length() == 0) {
					channelTypeParam.append(type);
				} else {
					channelTypeParam.append("|");
					channelTypeParam.append(type);
				}

			}
			inputMap.put(CHANNEL_TYPE, channelTypeParam.toString());
		}

	}

	private void processProfileIdList(Map<String, Object> postParametersMap, Map<String, Object> inputMap) {
		if (!postParametersMap.containsKey(PROFILEID_LIST)) {
			inputMap.put(PROFILEID_LIST, "");
			return;
		}
		StringBuilder profileIdListParam = new StringBuilder();
		String profileIdListList = (String) postParametersMap.get(PROFILEID_LIST);

		JsonElement profileIdListElement = new JsonParser().parse(profileIdListList);
		if (!profileIdListElement.isJsonNull() && profileIdListElement.isJsonArray()) {
			for (JsonElement profileIdListelem : profileIdListElement.getAsJsonArray()) {
				JsonObject profileIdListobj = profileIdListelem.getAsJsonObject();
				if (!profileIdListobj.has("id"))
					continue;
				String id = profileIdListobj.get("id").getAsString();

				if (profileIdListParam.length() == 0) {
					profileIdListParam.append(id);
				} else {
					profileIdListParam.append("|");
					profileIdListParam.append(id);
				}

			}
			inputMap.put(PROFILEID_LIST, profileIdListParam.toString());
		}

	}

	private void processEventTriggerIdList(Map<String, Object> postParametersMap, Map<String, Object> inputMap) {

		if (!postParametersMap.containsKey(EVENT_TRIGGERID_LIST)) {
			inputMap.put(EVENT_TRIGGERID_LIST, "");
			return;
		}
		StringBuilder eventsListParam = new StringBuilder();
		String eventTriggerIdList = (String) postParametersMap.get(EVENT_TRIGGERID_LIST);

		JsonElement eventTriggerIdListElement = new JsonParser().parse(eventTriggerIdList);
		if (!eventTriggerIdListElement.isJsonNull() && eventTriggerIdListElement.isJsonArray()) {
			for (JsonElement eventTriggerIdelem : eventTriggerIdListElement.getAsJsonArray()) {
				JsonObject eventTriggerIdobj = eventTriggerIdelem.getAsJsonObject();
				if (!eventTriggerIdobj.has("id"))
					continue;
				String id = eventTriggerIdobj.get("id").getAsString();

				if (eventsListParam.length() == 0) {
					eventsListParam.append(id);
				} else {
					eventsListParam.append("|");
					eventsListParam.append(id);
				}

			}
			inputMap.put(EVENT_TRIGGERID_LIST, eventsListParam.toString());
		}

	}

	private void processCampaignMetaData(Map<String, Object> postParametersMap, Map<String, Object> inputMap,boolean isCreateflow) {

		Date startDate = CommonUtilities.getFormattedTimeStamp((String) postParametersMap.get(START_DATE));
		Date endDate = CommonUtilities.getFormattedTimeStamp((String) postParametersMap.get(END_DATE));
		
		if (isCreateflow) {
			inputMap.put(CAMPAIGN_ID, "CP" + CommonUtilities.getUniqueNumericString(10));
		} else {
			inputMap.put(CAMPAIGN_ID, postParametersMap.get(CAMPAIGN_ID));
		}
		inputMap.put(CAMPAIGN_NAME,  postParametersMap.get(CAMPAIGN_NAME));
		inputMap.put(CAMPAIGN_DESCRIPTION,  postParametersMap.get(CAMPAIGN_DESCRIPTION));
		inputMap.put(CAMPAIGN_PRIORITY, postParametersMap.get(CAMPAIGN_PRIORITY));
		inputMap.put(START_DATE, CommonUtilities.getFormattedTimeStamp(startDate, null));
		inputMap.put(END_DATE, CommonUtilities.getFormattedTimeStamp(endDate, null));
		inputMap.put(CAMPAIGN_TYPE,  postParametersMap.get(CAMPAIGN_TYPE));
		inputMap.put(OBJECTIVE_TYPE,  postParametersMap.get(OBJECTIVE_TYPE));
		inputMap.put(PRODUCTID,  postParametersMap.get(PRODUCTID));
		inputMap.put(PRODUCT_GROUPID,  postParametersMap.get(PRODUCT_GROUPID));
		inputMap.put(CAMPAIGN_STATUS,  postParametersMap.get(CAMPAIGN_STATUS));

	}

	@Override
	public JSONObject createProfileDBXDB(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.createProfileInDBXDB(dataControllerRequest);
	}

	@Override
	public JSONObject createProfileMS(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.createProfileInMS(dataControllerRequest);
	}

	@Override
	public JSONArray getProfilesDB(String profileName, String profileId) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getProfilesDB(profileName, profileId);
	}
	
	@Override
	public JSONArray getProfilesDBXDB(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		JSONArray allProfilesArr = new JSONArray();
		try {
			
			JSONObject responseObj = backendDelegate.getProfilesDBXDB(dataControllerRequest);

			if (responseObj != null) {
				JSONArray profiles = responseObj.optJSONArray(CMConstants.RECORDS);
				JSONArray profileCampaigns = responseObj.optJSONArray(CMConstants.RECORDS1);
				
				Map<String, JSONArray> profileCondMap = new HashMap<String, JSONArray>();
				Map<String, ProfileDTO> profileMap = new HashMap<String, ProfileDTO>();
				
				for (int i = 0; i < profiles.length(); i++) {
					ProfileConditionDTO profileCondDTO = new ProfileConditionDTO();
					JSONObject profileObj = profiles.getJSONObject(i);
					String profileId = profileObj.getString(CMConstants.PROFILE_ID);
                    //construct profile conditions DTO and map with profileId
					profileCondDTO = JSONUtils.parse(profileObj.toString(), ProfileConditionDTO.class);
					JSONArray profileCondArr = new JSONArray();
					if (profileCondMap.get(profileId) != null) {
						profileCondArr = profileCondMap.get(profileId);
					}
					String conditionJsonString = new ObjectMapper().writeValueAsString(profileCondDTO);
					JSONObject conditionObj = new JSONObject(conditionJsonString);
					profileCondArr.put(conditionObj);
					profileCondMap.put(profileId, profileCondArr);
					
					//initialize profile DTO
					ProfileDTO profileDTO = new ProfileDTO();
					profileDTO = JSONUtils.parse(profileObj.toString(), ProfileDTO.class);
					if(profileMap.get(profileId) == null) {
						profileMap.put(profileId, profileDTO);
					}
					
				}
				//construct profile campaign DTO and map with profileId
				Map<String, JSONArray> profileCampMap = new HashMap<String,JSONArray>();
				profileCampMap = processProfileCampaigns(profileCampaigns);
				
				//create profile response array
				allProfilesArr = processAllProfileResponse(profileMap, profileCondMap, profileCampMap);
				
			    return allProfilesArr;
				
			} else {
				alert.prepareError("Error while processing profiles").log();
			}
		} catch (Exception e) {
			alert.prepareError("Exception while processing profiles: " + e.getMessage()).log();
			diagnostic.prepareDebug("Exception while processing profiles: " + e.getMessage()).log();
		}
		return allProfilesArr;
	}

	
	private Map<String, JSONArray> processProfileCampaigns(JSONArray profileCampaigns) {
		Map<String,JSONArray> profileCampaignMap = new HashMap<String, JSONArray>();
		try {
			for (int i = 0; i < profileCampaigns.length(); i++) {
				ProfileCampaignDTO profileCampaignDTO = new ProfileCampaignDTO();
				JSONObject profileCampaignObj = profileCampaigns.getJSONObject(i);
				String profileId = profileCampaignObj.getString(CMConstants.PROFILE_ID);

				JSONArray profileCampArr = new JSONArray();

				profileCampaignDTO = JSONUtils.parse(profileCampaignObj.toString(), ProfileCampaignDTO.class);
				if (profileCampaignMap.get(profileId) != null) {
					profileCampArr = profileCampaignMap.get(profileId);
				}
				// add campaign object if campaign exists for profile id
				if (profileCampaignObj.has(CMConstants.CAMPAIGN_ID)) {
					String campaignJsonString = new ObjectMapper().writeValueAsString(profileCampaignDTO);
					JSONObject campaignJSObj = new JSONObject(campaignJsonString);
					profileCampArr.put(campaignJSObj);
				}
				profileCampaignMap.put(profileId, profileCampArr);
			}
		} catch (Exception e) {
			alert.prepareError("Exception while processing profile campaigns: " + e.getMessage()).log();
			diagnostic.prepareDebug("Exception while processing profile campaigns: " + e.getMessage()).log();
		}
		return profileCampaignMap;
	}
	
	private JSONArray processAllProfileResponse(Map<String, ProfileDTO> profileMap, Map<String, JSONArray> profileCondMap, Map<String, JSONArray> profileCampMap) {
	 try {
		 JSONArray profilesArr = new JSONArray();
		 for (Map.Entry<String, ProfileDTO> entry : profileMap.entrySet()) {
	            String profileId = entry.getKey();
	            ProfileDTO profileValueDTO = entry.getValue();
	            JSONArray currProfileConditionsArr =  profileCondMap.get(profileId);
	            JSONArray currProfileCampaignArr =  profileCampMap.get(profileId);
	            int noOfAssociateCampaign = currProfileCampaignArr.length();
	            
	            profileValueDTO.setProfileConditions(currProfileConditionsArr);
	            profileValueDTO.setAssociatedCampaignDetails(currProfileCampaignArr);
	            profileValueDTO.setNumberOfCampaigns(noOfAssociateCampaign);
	            
	            JSONObject finalProfileObj = profileValueDTO.convertProfileDTOToJSONObject();
	            profilesArr.put(finalProfileObj);
	        }
		return profilesArr;
	} catch (Exception e) {
		alert.prepareError("Exception while processing profiles response: " + e.getMessage()).log();
		diagnostic.prepareDebug("Exception while processing profiles response: " + e.getMessage()).log();
	}
	return null;
	}
	
	@Override
	public JSONArray getProfilesMS(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getProfilesMS(dataControllerRequest);
	}

	@Override
	public JSONObject updateProfile(JSONObject profile, DataControllerRequest request) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.updateProfile(profile, request);
	}

	@Override
	public JSONObject updateProfileMS(DataControllerRequest request, JSONObject profile) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.updateProfileMS(request, profile);
	}

	@Override
	public JSONArray getProfileConditionsDB(String profileId) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getProfileConditionsDB(profileId);
	}
	public JSONArray getAllDefaultCampaignsMS(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getAllDefaultCampaignsMS(dataControllerRequest);
	}

	@Override
	public JSONArray getAllDefaultCampaignsDBXDB(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.getAllDefaultCampaignsDBXDB(dataControllerRequest);
	}
	@Override
	public JSONObject deleteRemovedProfileConditions(String profileId, List<String> profileConditions) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.deleteRemovedProfileConditions(profileId, profileConditions);
	}
	
	@Override
	public JSONObject updateDefaultCampaignsMS(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.updateDefaultCampaignsMS(dataControllerRequest );
	}

	@Override
	public JSONObject updateDefaultCampaignsDBXDB(DataControllerRequest dataControllerRequest) {
		CampaignsManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(CampaignsManagementBackendDelegate.class);
		return backendDelegate.updateDefaultCampaignsDBXDB(dataControllerRequest);
	}
}
