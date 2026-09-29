package com.kony.campaignsmanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.Date;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.campaignsmanagement.backenddelegate.api.CampaignsManagementBackendDelegate;
import com.kony.campaignsmanagement.dto.Campaignchanneldetails;
import com.kony.campaignsmanagement.dto.Campaignchanneltype;
import com.kony.campaignsmanagement.dto.Campaigndefinition;
import com.kony.campaignsmanagement.dto.Campaigneventtrigger;
import com.kony.campaignsmanagement.dto.Campaignprofile;
import com.kony.campaignsmanagement.dto.Datacontext;
import com.kony.campaignsmanagement.dto.Eventtriggers;
import com.kony.campaignsmanagement.dto.Offlinetemplate;
import com.kony.campaignsmanagement.dto.Onlinecontent;
import com.kony.campaignsmanagement.dto.Placeholder;
import com.kony.campaignsmanagement.dto.profile;
import com.kony.campaignsmanagement.dto.profilecondition;
import com.kony.campaignsmanagement.utils.ErrorCodesEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class CampaignsManagementBackendDelegateImpl implements CampaignsManagementBackendDelegate {
	private static final String CAMPAIGN_ID = "campaignId";
	private static final Alert alert = Logger.forAlert().forModule(CMConstants.INFINITY, CMConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(CMConstants.INFINITY, CMConstants.SPOTLIGHT);

	@Override
	public JSONArray getAllPlaceHolders(DataControllerRequest request) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_PLACEHOLDER_GET;
		JSONArray responseArray = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(null).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			if (responseObj != null && responseObj.has(CMConstants.PLACEHOLDER_DBXDB)
					&& responseObj.optJSONArray(CMConstants.PLACEHOLDER_DBXDB).length() > 0) {
				responseArray = responseObj.optJSONArray(CMConstants.PLACEHOLDER_DBXDB);
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch placeholders from database: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAllPlaceHolders: " + e.getMessage()).log();
			return null;
		}
		return responseArray;
	}

	@Override
	public JSONArray getAllPlaceHoldersMS(DataControllerRequest request) {
		String serviceName = ServiceId.CAMPAIGNSMS;
		String operationName = OperationName.GET_PLACEHOLDERS;
		
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		
		headerMap.put("Authorization", request.getParameter("Authorization"));

		JSONArray responseArray = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
								.withOperationId(operationName)
								.withDataControllerRequest(request)
								.withRequestParameters(inputBodyMap)
								.withRequestHeaders(headerMap)
								.build()
								.getResponse();

			JSONObject responseObj = new JSONObject(response);
			if (responseObj != null && responseObj.has(CMConstants.PLACEHOLDERS_MS)
					&& responseObj.optJSONArray(CMConstants.PLACEHOLDERS_MS).length() > 0) {
				responseArray = responseObj.optJSONArray(CMConstants.PLACEHOLDERS_MS);
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch placeholders from  Campaign MS: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAllPlaceHoldersMS: " + e.getMessage()).log();
			return null;
		}
		return responseArray;
	}

	@Override
	public JSONArray getEventTriggers(DataControllerRequest request) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_EVENTTRIGGERS_GET;
		JSONArray responseArray = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(null).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			if (responseObj != null && responseObj.has(CMConstants.EVENTTRIGGERS_DBXDB)
					&& responseObj.optJSONArray(CMConstants.EVENTTRIGGERS_DBXDB).length() > 0) {
				responseArray = responseObj.optJSONArray(CMConstants.EVENTTRIGGERS_DBXDB);
			}			
			
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch eventtriggers from database: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getEventTriggers: " + e.getMessage()).log();
			return null;
		}
		return responseArray;
	}

	@Override
	public JSONArray getEventTriggersMS(DataControllerRequest request) {
		String serviceName = ServiceId.CAMPAIGNSMS;
		String operationName = OperationName.GET_EVENTS;
		
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		
		headerMap.put("Authorization", request.getParameter("Authorization"));

		JSONArray responseArray = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
								.withOperationId(operationName)
								.withDataControllerRequest(request)
								.withRequestParameters(inputBodyMap)
								.withRequestHeaders(headerMap)
								.build()
								.getResponse();

			JSONObject responseObj = new JSONObject(response);
			if (responseObj != null && responseObj.has(CMConstants.EVENTTRIGGERS_MS)
					&& responseObj.optJSONArray(CMConstants.EVENTTRIGGERS_MS).length() > 0) {
				responseArray = responseObj.optJSONArray(CMConstants.EVENTTRIGGERS_MS);
			}			
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch event triggers from  Campaign MS: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getEventTriggersMS: " + e.getMessage()).log();
			return null;
		}
		return responseArray;
	}

	@Override
	public JSONObject createCampaign(Map<String, Object> inputMap, Map<String, Object> headerMap)
			throws DBPApplicationException {
		String campaignBackend = null;
		String serviceResponse;

		try {
			campaignBackend = (EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND"));

			if (StringUtils.isNotBlank(campaignBackend) && "MS".equalsIgnoreCase(campaignBackend)) {
				String token = "";
				if (headerMap.get("x-kony-authorization") != null)
					token = headerMap.get("x-kony-authorization").toString();
				return createCampaignMS(inputMap, token);

			} else {
				serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CRUDLAYER, null,
						OperationName.DB_CREATE_CAMPAIGN_PROC, inputMap, headerMap, "");
				JSONObject response = CommonUtilities.getStringAsJSONObject(serviceResponse);
				if (response != null && response.has("opstatus")
						&& response.get("opstatus").toString().equalsIgnoreCase("0")) {
					response.put("id", inputMap.get(CAMPAIGN_ID).toString());
				}
				return response;
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in create campaign: ", e).log();

		}
		return new JSONObject();
	}
	
	@Override
	public JSONObject updateCampaign(Map<String, Object> inputMap, Map<String, Object> headerMap)
			throws DBPApplicationException {
		String campaignBackend = null;
		String serviceResponse;

		try {
			campaignBackend = (EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND"));

			if (StringUtils.isNotBlank(campaignBackend) && "MS".equalsIgnoreCase(campaignBackend)) {
				String token = "";
				if (headerMap.get("x-kony-authorization") != null)
					token = headerMap.get("x-kony-authorization").toString();
				return updateCampaignMS(inputMap, token);

			} else {
				serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CRUDLAYER, null,
						OperationName.DB_UPDATE_CAMPAIGN_PROC, inputMap, headerMap, "");
				JSONObject response = CommonUtilities.getStringAsJSONObject(serviceResponse);
				if (response != null && response.has("opstatus")
						&& response.get("opstatus").toString().equalsIgnoreCase("0")) {
					response.put("id", inputMap.get(CAMPAIGN_ID).toString());
				}
				return response;
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in create campaign: ", e).log();

		}
		return new JSONObject();
	}
	
	@Override
	public JSONObject getCampaigns(Map<String, Object> inputMap, Map<String, Object> headerMap)
			throws DBPApplicationException {
		String campaignBackend = null;
		String serviceResponse;

		try {
			campaignBackend = (EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND"));

			if (StringUtils.isNotBlank(campaignBackend) && "MS".equalsIgnoreCase(campaignBackend)) {
				String token = "";
				if (headerMap.get("x-kony-authorization") != null)
					token = headerMap.get("x-kony-authorization").toString();
				return getCampaignsMS(inputMap, token);

			} else {
				serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CRUDLAYER, null,
						OperationName.DB_GET_CAMPAIGNS_PROC, inputMap, headerMap, "");
				JSONObject response = CommonUtilities.getStringAsJSONObject(serviceResponse);
				response  = processDataBaseResponse(response);
				
				return response;
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in create campaign: ", e).log();

		}
		return new JSONObject();
	}

	private JSONObject processDataBaseResponse(JSONObject response) {

		Map<String, List<Offlinetemplate>> campaignofflineTemplate = new HashMap<>();
		Map<String, List<Campaignchanneltype>> campaignchannelType = new HashMap<>();
		Map<String, List<Onlinecontent>> campaignonlineContent = new HashMap<>();
		Map<String, List<Campaignchanneldetails>> campaignchannelDetails = new HashMap<>();
		Map<String, List<Campaignprofile>> campaignprofiles = new HashMap<>();
		Map<String, List<Campaigneventtrigger>> campaigneventTrigger = new HashMap<>();
		Map<String, Datacontext> datacontextMap = new HashMap<>();
		Map<String, Eventtriggers> eventtriggersMap = new HashMap<>();
		Map<String, Placeholder> placeholderMap = new HashMap<>();
		Map<String, profile> profileMap = new HashMap<>();
		Map<String, List<profilecondition>> profileconditionMap = new HashMap<>();

		List<Campaigndefinition> campaigns = processCampaignBasicInfo(response);

		processCampaignofflineTemplate(campaignofflineTemplate, response);
		processCampaignchannelType(campaignchannelType, response);
		processCampaignonlineContent(campaignonlineContent, response);
		processCampaignchannelDetails(campaignchannelDetails, response);
		processCampaignprofileDetails(campaignprofiles, response);
		processCampaigneventTriggerDetails(campaigneventTrigger, response);

		processStaticData(response, datacontextMap, eventtriggersMap, placeholderMap, profileMap, profileconditionMap);

		JSONObject resultObj = new JSONObject();
		JSONArray returnArray = new JSONArray();
		for (Campaigndefinition campaigndef : campaigns) {
			JSONObject campaignJson = new JSONObject();
			addBasicInfo(campaigndef, campaignJson);
			addCampaignofflineTemplate(campaignofflineTemplate.get(campaigndef.getcampaignId()), campaignJson);
			addCampaignchannelType(campaignchannelType.get(campaigndef.getcampaignId()), campaignJson);
			addCampaignonlineContent(campaignonlineContent.get(campaigndef.getcampaignId()), placeholderMap,
					campaignJson);
			addCampaignchannelDetails(campaignchannelDetails.get(campaigndef.getcampaignId()), campaignJson);
			addCampaignprofileDetails(campaignprofiles.get(campaigndef.getcampaignId()), profileMap,
					profileconditionMap, datacontextMap, campaignJson);
			addCampaigneventTriggerDetails(campaigneventTrigger.get(campaigndef.getcampaignId()), eventtriggersMap,
					campaignJson);
			returnArray.put(campaignJson);
		}
		resultObj.put("campaignList", returnArray);
		resultObj.put("opstatus", 0);
		return resultObj;
	}

	private void addCampaigneventTriggerDetails(List<Campaigneventtrigger> campaignEventTriggers,
			Map<String, Eventtriggers> eventtriggersInfoMap, JSONObject campaignJson) {
		
		JSONArray eventTriggerDetailsarray = new JSONArray();
		if (campaignEventTriggers == null) {
			campaignJson.put("eventTriggerDetails", eventTriggerDetailsarray);
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();

			for (int index = 0; index < campaignEventTriggers.size(); index++) {
				Campaigneventtrigger campaigneventTrigger = campaignEventTriggers.get(index);
				Eventtriggers eventtriggers = eventtriggersInfoMap.get(campaigneventTrigger.geteventTriggerId());
				String json = mapper.writeValueAsString(eventtriggers);
				JSONObject parsedeventtriggers = new JSONObject(json);
				eventTriggerDetailsarray.put(parsedeventtriggers);
			}
		} catch (JsonProcessingException e) {
			alert.prepareError("Error while reading campaigns  from DB").log();
		}
		campaignJson.put("eventTriggerDetails", eventTriggerDetailsarray);
	}

	private void addCampaignprofileDetails(List<Campaignprofile> profileList,
			Map<String, profile> profileMap, Map<String, List<profilecondition>> profileconditionMap,
			Map<String, Datacontext> datacontextMap, JSONObject campaignJson) {
		
		JSONArray profileDetailsArray = new JSONArray();
		if (profileList == null) {
			campaignJson.put("profileDetails", profileDetailsArray);
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();

			for (int index = 0; index < profileList.size(); index++) {
				Campaignprofile campaignProfile = profileList.get(index);
				List<profilecondition> profileConditions = profileconditionMap.get(campaignProfile.getprofileId());
				
				profile profile = profileMap.get(campaignProfile.getprofileId());
				String json = mapper.writeValueAsString(profile);
				JSONObject parsedprofile = new JSONObject(json);
				JSONArray profileConditionsArray = new JSONArray();
				if (profileConditions != null) {
					for (int cond = 0; cond < profileConditions.size(); cond++) {
						profilecondition profilecondition = profileConditions.get(cond);

						json = mapper.writeValueAsString(profilecondition);
						JSONObject profileconditionJSON = new JSONObject(json);
						Datacontext datacontext = datacontextMap.get(profilecondition.getdataContextId());
						if (datacontext != null) {
							json = mapper.writeValueAsString(datacontext);
							JSONObject dataContextJSON = new JSONObject(json);
							Iterator itr = dataContextJSON.keys();// copying json from source to destinationJson
							while (itr.hasNext()) {
								String key = (String) itr.next();
								profileconditionJSON.put(key, dataContextJSON.get(key));
							}
						}
						profileConditionsArray.put(profileconditionJSON);
					}
				}
				
				if (profile.getprofileCreationDate() != null) {
					java.util.Date dateVariable = new java.util.Date(profile.getprofileCreationDate().getTime());
					String dateString = CommonUtilities.getFormattedTimeStamp(dateVariable, "yyyy-MM-dd HH:mm:ss.S");
					parsedprofile.put("profileCreationDate", dateString);
				}
				if (profile.getprofileDeactivatedDate() != null) {
					java.util.Date dateVariable = new java.util.Date(profile.getprofileDeactivatedDate().getTime());
					String dateString = CommonUtilities.getFormattedTimeStamp(dateVariable, "yyyy-MM-dd HH:mm:ss.S");
					parsedprofile.put("profileDeactivatedDate", dateString);
				}
				parsedprofile.put("profileConditions", profileConditionsArray);
				profileDetailsArray.put(parsedprofile);
			}
		} catch (JsonProcessingException e) {
			alert.prepareError("Error while reading campaigns from DB ").log();
		}
		campaignJson.put("profileDetails", profileDetailsArray);
	}

	private void addCampaignchannelDetails(List<Campaignchanneldetails> campaignChannelDetails,
			JSONObject campaignJson) {
		JSONArray channelDetailsArray = new JSONArray();
		if (campaignChannelDetails == null) {
			campaignJson.put("channelDetails", channelDetailsArray);
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			for (int index = 0; index < campaignChannelDetails.size(); index++) {
				Campaignchanneldetails channelDetails = campaignChannelDetails.get(index);
				String json = mapper.writeValueAsString(channelDetails);
				JSONObject channelDetailsJSON = new JSONObject(json);
				channelDetailsJSON.remove("campaignId");
				channelDetailsArray.put(channelDetailsJSON);
			}
		} catch (JsonProcessingException e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
		campaignJson.put("channelDetails", channelDetailsArray);
		
	}

	private void addCampaignonlineContent(List<Onlinecontent> campaignOnlineContentList,
			Map<String, Placeholder> placeholderMap, JSONObject campaignJson) {
		JSONArray onlineContentArray = new JSONArray();
		if (campaignOnlineContentList == null) {
			campaignJson.put("onlineContent", onlineContentArray);
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			for (int index = 0; index < campaignOnlineContentList.size(); index++) {
				Onlinecontent onlineContent = campaignOnlineContentList.get(index);
				String json = mapper.writeValueAsString(onlineContent);
				JSONObject onlineContentJson = new JSONObject(json);
				Placeholder placeHolder = placeholderMap.get(onlineContent.getplaceholderId());
				json = mapper.writeValueAsString(placeHolder);
				JSONObject parsedPlaceHolder = new JSONObject(json);
				Iterator itr = parsedPlaceHolder.keys();// copying json from source to destinationJson
				while (itr.hasNext()) {
					String key = (String) itr.next();
					onlineContentJson.put(key, parsedPlaceHolder.get(key));
				}
				onlineContentJson.remove("campaignId");
				onlineContentArray.put(onlineContentJson);
			}
		} catch (JsonProcessingException e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
		campaignJson.put("onlineContent", onlineContentArray);
		
	}

	private void addCampaignchannelType(List<Campaignchanneltype> campaignchanneltypeSet,
			JSONObject campaignJson) {
		JSONArray campaignChannelArray = new JSONArray();
		if (campaignchanneltypeSet == null) {
			campaignJson.put("channelType", campaignChannelArray);
			return;
		}
		try {
			for (int index = 0; index < campaignchanneltypeSet.size(); index++) {
				Campaignchanneltype campaignchanneltype = campaignchanneltypeSet.get(index);
				campaignChannelArray.put(campaignchanneltype.getchannelType());
			}
		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
		campaignJson.put("channelType", campaignChannelArray);
	}
		
	

	private void addCampaignofflineTemplate(List<Offlinetemplate> campaignOfflineTemplates,
			JSONObject campaignJson) {
		JSONArray offlineTemplatesarray = new JSONArray();
		if (campaignOfflineTemplates == null) {
			campaignJson.put("offlineTemplate", offlineTemplatesarray);
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();

			for (int index = 0; index < campaignOfflineTemplates.size(); index++) {
				Offlinetemplate offlineTemplate = campaignOfflineTemplates.get(index);
				String json = mapper.writeValueAsString(offlineTemplate);
				JSONObject parsedofflineTemplate = new JSONObject(json);
				parsedofflineTemplate.remove("campaignId");
				parsedofflineTemplate.remove("content");
				parsedofflineTemplate.put("messageContent",offlineTemplate.getcontent());
				offlineTemplatesarray.put(parsedofflineTemplate);
			}
		} catch (JsonProcessingException e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
		campaignJson.put("offlineTemplate", offlineTemplatesarray);

	}

	private void addBasicInfo(Campaigndefinition campaigndef, JSONObject campaignJson) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(campaigndef);
			JSONObject parsedCampaignDef = new JSONObject(json);
			Iterator itr = parsedCampaignDef.keys();//copying json from source to destinationJson
			while(itr.hasNext()) {
			String key = (String) itr.next();
			campaignJson.put(key, parsedCampaignDef.get(key));
			}
			java.util.Date dateVariable;
			String dateString;
			if (campaigndef.getstartDate() != null) {
				dateVariable = new java.util.Date(campaigndef.getstartDate().getTime());
				dateString = CommonUtilities.getFormattedTimeStamp(dateVariable, "yyyy-MM-dd HH:mm:ss.S");
				campaignJson.put("startDate", dateString);
			}
			if (campaigndef.getendDate() != null) {
				dateVariable = new java.util.Date(campaigndef.getendDate().getTime());
				dateString = CommonUtilities.getFormattedTimeStamp(dateVariable, "yyyy-MM-dd HH:mm:ss.S");
				campaignJson.put("endDate", dateString);
			}
		} catch (JsonProcessingException e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
	}

	private List<Campaigndefinition> processCampaignBasicInfo(JSONObject response) {
		
		if(response.get(CMConstants.CAMPAIGNDEFINITION) == null)
			return new ArrayList<>();
		formatDateForDTOConversion((JSONArray)response.get(CMConstants.CAMPAIGNDEFINITION));
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper
					.readValue(response.get(CMConstants.CAMPAIGNDEFINITION).toString(), new TypeReference<>() {

					});
		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB",e).log();
		}
		return new ArrayList<>();

	}
	

	private void formatDateForDTOConversion(JSONArray array) {
		String[] keys= {"startDate","endDate","profileCreationDate"};
		
		for (int i = 0; i < array.length(); i++) {
			
			
			JSONObject jsonObjWithDate = (JSONObject) array.get(i);
			
			for(String key: keys)
			{
				if(jsonObjWithDate.has(key)) {
				String dateToBeConverted = jsonObjWithDate.get(key).toString();
				Date dateobj = CommonUtilities.getFormattedTimeStamp(dateToBeConverted);
				dateToBeConverted = CommonUtilities.getFormattedTimeStamp(dateobj, "yyyy-MM-dd'T'HH:mm");
				jsonObjWithDate.put(key, dateToBeConverted);
				}
			}
		}
	}

	private void processCampaigneventTriggerDetails(Map<String, List<Campaigneventtrigger>> campaigneventTrigger,
			JSONObject response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Campaigneventtrigger> campaigneventtriggerData = objectMapper

					.readValue(response.get(CMConstants.CAMPAIGNEVENTTRIGGER).toString(), new TypeReference<>() {

					});
			for (Campaigneventtrigger campaigneventtrigger : campaigneventtriggerData) {
				if (campaigneventTrigger.containsKey(campaigneventtrigger.getcampaignId())) {
					campaigneventTrigger.get(campaigneventtrigger.getcampaignId()).add(campaigneventtrigger);
				} else {
					List<Campaigneventtrigger> campaigneventtriggerLocal = new ArrayList<>();
					campaigneventtriggerLocal.add(campaigneventtrigger);
					campaigneventTrigger.put(campaigneventtrigger.getcampaignId(), campaigneventtriggerLocal);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
	}

	private void processCampaignprofileDetails(Map<String, List<Campaignprofile>> campaignprofiles,
			JSONObject response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Campaignprofile> campaignprofileData = objectMapper

					.readValue(response.get(CMConstants.CAMPAIGNPROFILE).toString(), new TypeReference<>() {

					});
			for (Campaignprofile campaignprofile : campaignprofileData) {
				if (campaignprofiles.containsKey(campaignprofile.getcampaignId())) {
					campaignprofiles.get(campaignprofile.getcampaignId()).add(campaignprofile);
				} else {
					List<Campaignprofile> campaignprofileLocal = new ArrayList<>();
					campaignprofileLocal.add(campaignprofile);
					campaignprofiles.put(campaignprofile.getcampaignId(), campaignprofileLocal);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
	}

	private void processCampaignchannelDetails(Map<String, List<Campaignchanneldetails>> campaignchannelDetails, JSONObject response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Campaignchanneldetails> campaignchanneldetailsData = objectMapper
					.readValue(response.get(CMConstants.CAMPAIGNCHANNELDETAILS).toString(), new TypeReference<>() {
					});
			for (Campaignchanneldetails campaignchanneldetails : campaignchanneldetailsData) {
				if (campaignchannelDetails.containsKey(campaignchanneldetails.getcampaignId())) {
					campaignchannelDetails.get(campaignchanneldetails.getcampaignId()).add(campaignchanneldetails);
				} else {
					List<Campaignchanneldetails> campaignchanneldetailsLocal = new ArrayList<>();
					campaignchanneldetailsLocal.add(campaignchanneldetails);
					campaignchannelDetails.put(campaignchanneldetails.getcampaignId(), campaignchanneldetailsLocal);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}

		
	}

	private void processCampaignonlineContent(Map<String, List<Onlinecontent>> campaignonlineContent,
			JSONObject response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Onlinecontent> onlinecontentData = objectMapper
					.readValue(response.get(CMConstants.ONLINECONTENT).toString(), new TypeReference<>() {
					});
			for (Onlinecontent onlinecontent : onlinecontentData) {
				if (campaignonlineContent.containsKey(onlinecontent.getcampaignId())) {
					campaignonlineContent.get(onlinecontent.getcampaignId()).add(onlinecontent);
				} else {
					List<Onlinecontent> onlinecontentLocal = new ArrayList<>();
					onlinecontentLocal.add(onlinecontent);
					campaignonlineContent.put(onlinecontent.getcampaignId(), onlinecontentLocal);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
	}

	private void processCampaignchannelType(Map<String, List<Campaignchanneltype>> campaignchannelType, JSONObject response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Campaignchanneltype> campaignchanneltypeData = objectMapper
					.readValue(response.get(CMConstants.CAMPAIGNCHANNELTYPE).toString(), new TypeReference<>() {
					});
			for (Campaignchanneltype campaignchanneltype : campaignchanneltypeData) {
				if (campaignchannelType.containsKey(campaignchanneltype.getcampaignId())) {
					campaignchannelType.get(campaignchanneltype.getcampaignId()).add(campaignchanneltype);
				} else {
					List<Campaignchanneltype> campaignchanneltypeLocal = new ArrayList<>();
					campaignchanneltypeLocal.add(campaignchanneltype);
					campaignchannelType.put(campaignchanneltype.getcampaignId(), campaignchanneltypeLocal);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
	}

	private void processCampaignofflineTemplate(Map<String, List<Offlinetemplate>> campaignofflineTemplate,
			JSONObject response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Offlinetemplate> offlinetemplateData = objectMapper
					.readValue(response.get(CMConstants.OFFLINETEMPLATE).toString(), new TypeReference<>() {
					});
			for (Offlinetemplate offlinetemplate : offlinetemplateData) {
				if (campaignofflineTemplate.containsKey(offlinetemplate.getcampaignId())) {
					campaignofflineTemplate.get(offlinetemplate.getcampaignId()).add(offlinetemplate);
				} else {
					List<Offlinetemplate> offlineTemplateLocal = new ArrayList<>();
					offlineTemplateLocal.add(offlinetemplate);
					campaignofflineTemplate.put(offlinetemplate.getcampaignId(), offlineTemplateLocal);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB").log();
		}
	}

	private void processStaticData(JSONObject response, Map<String, Datacontext> datacontextMap,
			Map<String, Eventtriggers> eventtriggersMap, Map<String, Placeholder> placeholderMap,
			Map<String, com.kony.campaignsmanagement.dto.profile> profileMap,
			Map<String, List<profilecondition>> profileconditionMap) {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			
			if (response.get(CMConstants.PROFILE) != null) {
				formatDateForDTOConversion((JSONArray) response.get(CMConstants.PROFILE));
				List<profile> profilesData = objectMapper.readValue(response.get(CMConstants.PROFILE).toString(),
						new TypeReference<>() {
						});

				for (profile profile : profilesData) {
					profileMap.put(profile.getprofileId(), profile);
				}
			}
			List<profilecondition> profileconditionData = objectMapper
					.readValue(response.get(CMConstants.PROFILECONDITIONS).toString(), new TypeReference<>() {
					});
			for (profilecondition profilecondition : profileconditionData) {
				if (profileconditionMap.containsKey(profilecondition.getprofileId())) {
					profileconditionMap.get(profilecondition.getprofileId()).add(profilecondition);
				} else {
					List<profilecondition> profileconditionList = new ArrayList<>();
					profileconditionList.add(profilecondition);
					profileconditionMap.put(profilecondition.getprofileId(), profileconditionList);
				}
			}
			List<Placeholder> placeholderData = objectMapper.readValue(response.get(CMConstants.PLACEHOLDER).toString(),
					new TypeReference<>() {
					});
			for (Placeholder placeholder : placeholderData) {
				placeholderMap.put(placeholder.getplaceholderId(), placeholder);
			}
			List<Eventtriggers> eventtriggersData = objectMapper
					.readValue(response.get(CMConstants.EVENTTRIGGERS).toString(), new TypeReference<>() {
					});
			for (Eventtriggers eventtriggers : eventtriggersData) {
				eventtriggersMap.put(eventtriggers.geteventTriggerId(), eventtriggers);
			}
			List<Datacontext> datacontextData = objectMapper.readValue(response.get(CMConstants.DATACONTEXT).toString(),
					new TypeReference<>() {
					});
			for (Datacontext datacontext : datacontextData) {
				datacontextMap.put(datacontext.getdataContextId(), datacontext);
			}

		} catch (Exception e) {
			alert.prepareError("Error while reading campaigns from DB",e).log();
		}
	}

	private JSONObject updateCampaignMS(Map<String, Object> postParametersMap, String token) throws Exception
	{
			String serviceResponse = "";
			serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CAMPAIGN_MANAGEMENT_MS, null, OperationName.UPDATE_CAMPAIGN, postParametersMap, null, token);
					return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	private JSONObject createCampaignMS(Map<String, Object> postParametersMap, String token) throws Exception
	{
			String serviceResponse = "";
			serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CAMPAIGN_MANAGEMENT_MS, null, OperationName.CREATE_CAMPAIGN, postParametersMap, null, token);
					return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	private JSONObject getCampaignsMS(Map<String, Object> postParametersMap, String token) throws Exception
	{
			String serviceResponse = "";
			serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CAMPAIGN_MANAGEMENT_MS, null, OperationName.GET_CAMPAIGNS, postParametersMap, null, token);
					return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	
	@Override
	public JSONObject createProfileInDBXDB(DataControllerRequest request) {
		String createProfileResponse =null;
	    String serviceName = ServiceId.CRUDLAYER;
	    String operationName = OperationName.DB_PROFILE_CREATE;
		try {
			
			Map<String,Object> requestParametersMap = new HashMap<String, Object>();
			String profileName = request.getParameter(CMConstants.PROFILE_NAME);
            String profileDesc = request.getParameter(CMConstants.PROFILE_DESC);
            String profileConditions = request.getParameter(CMConstants.PROFILE_CONDITION);
            String profileId = request.getParameter(CMConstants.PROFILE_ID);;
            requestParametersMap.put(CMConstants.PROFILE_ID, profileId);
            requestParametersMap.put(CMConstants.PROFILE_NAME, profileName);
            requestParametersMap.put(CMConstants.PROFILE_DESC, profileDesc);
            requestParametersMap.put(CMConstants.PROFILE_STATUS, CMConstants.ACTIVE_STATUS);
            requestParametersMap.put("numberOfUsers",0);
            requestParametersMap.put("profileCreationDate", CommonUtilities.getISOFormattedLocalTimestamp());
            requestParametersMap.put("profileDeactivatedDate", null);
            
            //create profile
			createProfileResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParametersMap)
					.build()
					.getResponse();
			JSONObject responseObj = new JSONObject(createProfileResponse);
			if (responseObj == null|| !responseObj.has(FabricConstants.OPSTATUS)
                    || responseObj.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Error while creating profile in DB").log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Profile validation failed");
            	return ErrorCodesEnum.ERR_10006.setErrorCode(responseObj);
				
			} else {
				//create profile conditions
				JSONObject createProfileConditionResponse = createProfileConditionInDBXDB(request, profileConditions, profileId);
				
				if(createProfileConditionResponse == null || !createProfileConditionResponse.has(FabricConstants.OPSTATUS)
	                    || createProfileConditionResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					alert.prepareError("Error creating profile conditions in DB").log();
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
	                        ActivityStatusEnum.FAILED, "Profile condition creation failed");
	            	return ErrorCodesEnum.ERR_10003.setErrorCode(createProfileConditionResponse);
				} else {
					responseObj.put("id", profileId);
					return responseObj;
				}
			}
		}
		catch (Exception e) {
			alert.prepareError("Error creating profile conditions in DB").log();
            diagnostic.prepareDebug("Exception while creating profile in DB.Exception Trace: ", e).log();
            return ErrorCodesEnum.ERR_10003.setErrorCode(new JSONObject());
		}
	}


	@Override
	public JSONObject createProfileConditionInDBXDB(DataControllerRequest request, String profileConditions, String profileId) {
		String serviceName = ServiceId.CRUDLAYER;
	    String operationName = OperationName.DB_PROFILECONDITION_CREATE;
	    JSONObject finalResponseObj = null;
	    try {
	    	JSONArray profileCondsArray = new JSONArray(profileConditions);
	    	
	    	for(int i=0; i<profileCondsArray.length(); i++) {
	    		JSONObject profileCondObject = profileCondsArray.getJSONObject(i);
	    		String dataContextId = profileCondObject.get(CMConstants.DATACONTEXT_ID).toString();
	            String profileCondId = "PC" + Long.toString(CommonUtilities.getNumericId());// profileCondObject.get(CMConstants.PROFILE_CONDITION_ID).toString();
	            String conditionExpression = profileCondObject.get(CMConstants.CONDITION_EXPRESSION).toString();
	            //validate profile condition
	            if(StringUtils.isBlank(dataContextId) || StringUtils.isBlank(profileCondId) ||
	            		StringUtils.isBlank(conditionExpression)) {
	            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
	                        ActivityStatusEnum.FAILED, "Profile condition validation failed");
	            	
	            	return ErrorCodesEnum.ERR_10006.setErrorCode(new JSONObject());
	            }
	            Map<String,Object> requestParametersMap = new HashMap<String, Object>();
	            requestParametersMap.put(CMConstants.PROFILE_ID, profileId);
	            requestParametersMap.put(CMConstants.DATACONTEXT_ID, dataContextId);
	            requestParametersMap.put(CMConstants.PROFILE_CONDITION_ID, profileCondId);
	            requestParametersMap.put(CMConstants.CONDITION_EXPRESSION, conditionExpression);
	            //create profile condition
	            String createProfileCondResponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(serviceName)
						.withObjectId(null)
						.withOperationId(operationName)
						.withRequestParameters(requestParametersMap)
						.build()
						.getResponse();
				JSONObject responseObj = new JSONObject(createProfileCondResponse);
				if (responseObj == null || !responseObj.has(FabricConstants.OPSTATUS)
	                    || responseObj.getInt(FabricConstants.OPSTATUS) != 0) {
					alert.prepareError("Error creating profile conditions in DB").log();
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
	                        ActivityStatusEnum.FAILED, "Profile Condition creation failed");
	            	return ErrorCodesEnum.ERR_10004.setErrorCode(responseObj);
					
				} else {
					finalResponseObj = responseObj;
				}
	    	}
	    	return finalResponseObj;
			
	    }catch (Exception e) {
	    	alert.prepareError("Exception while creating profile conditions in db " + e.getMessage()).log();
	    	diagnostic.prepareDebug("Exception while creating profile conditions in DB.Exception Trace: ", e).log();
	            return ErrorCodesEnum.ERR_10004.setErrorCode(new JSONObject());
		}
	}
	
	@Override
	public JSONObject createProfileInMS(DataControllerRequest request) {
		String createProfileResponse =null;
	    String serviceName = ServiceId.CAMPAIGNSMS;
	    String operationName = OperationName.OP_CREATE_PROFILE;
		try {
			
			Map<String,Object> requestParametersMap = new HashMap<String, Object>();
			Map<String, Object> headerMap = new HashMap<String, Object>();
			
			String profileName = request.getParameter(CMConstants.PROFILE_NAME);
            String profileDesc = request.getParameter(CMConstants.PROFILE_DESC);
            String profileConditions = request.getParameter(CMConstants.PROFILE_CONDITION);
           // String profileId = null;
            
            headerMap.put("Authorization", request.getParameter("Authorization"));
            requestParametersMap.put(CMConstants.PROFILE_NAME, profileName);
            requestParametersMap.put(CMConstants.PROFILE_DESC, profileDesc);
            
            //validate profile condition
            JSONArray profileCondsArray = new JSONArray(profileConditions);
	    	for(int i=0; i<profileCondsArray.length(); i++) {
	    		JSONObject profileCondObject = profileCondsArray.getJSONObject(i);
	    		String dataContextId = profileCondObject.get(CMConstants.DATACONTEXT_ID).toString();
	            String conditionExpression = profileCondObject.get(CMConstants.CONDITION_EXPRESSION).toString();
	            //validate profile condition
	            if(StringUtils.isBlank(dataContextId) || StringUtils.isBlank(conditionExpression)) {
	            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
	                        ActivityStatusEnum.FAILED, "Profile validation failed for MS");
	            	return ErrorCodesEnum.ERR_10006.setErrorCode(new JSONObject());
	            }
	    	}
	    	requestParametersMap.put(CMConstants.PROFILE_CONDITION, profileCondsArray);
	    	
            //create profile
			createProfileResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withDataControllerRequest(request)
					.withOperationId(operationName)
					.withRequestParameters(requestParametersMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			JSONObject responseObj = new JSONObject(createProfileResponse);
			if (responseObj == null|| !responseObj.has(FabricConstants.OPSTATUS)
                    || responseObj.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Error creating profile in MS").log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Profile creation failed for MS");
            	return ErrorCodesEnum.ERR_10003.setErrorCode(responseObj);
				
			} else {
				return responseObj;
			}
		}
		catch (Exception e) {
			alert.prepareError("Exception while creating profile in MS " + e.getMessage()).log();
            diagnostic.prepareDebug("Runtime Exception while creating profile in MS.Exception Trace:", e).log();
            return ErrorCodesEnum.ERR_10003.setErrorCode(new JSONObject());
		}
	}

	
	@Override
	public JSONArray getProfilesDB(String profileName, String profileId) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_PROFILE_GET;
		JSONArray responseArray = new JSONArray();
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("$select", "profileName, profileStatus");
		if(StringUtils.isBlank(profileId)) {
			inputMap.put("$filter", "profileName eq " + profileName);
		}
		else if(StringUtils.isBlank(profileName)) {
			inputMap.put("$filter", "profileId eq " + profileId);
		}
		else {
			inputMap.put("$filter", "profileName eq " + profileName + " and profileId ne "+ profileId);
		}
		try {
			//fetch profile
			String getProfileResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			
			JSONObject responseObj = new JSONObject(getProfileResponse);
			if (responseObj != null && responseObj.has(CMConstants.PROFILE_DBXDB)&&
					responseObj.optJSONArray(CMConstants.PROFILE_DBXDB).length() > 0) {
				responseArray = responseObj.optJSONArray(CMConstants.PROFILE_DBXDB);
			}else {
				alert.prepareError("Failed to fetch profiles from DB").log();
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch profiles from DB: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error fetching profile in DB").log();
			diagnostic.prepareDebug("Exception while fetching profile in DB.Exception Trace: ", e).log();
            return null;
		}
		return responseArray;
	}
	
	@Override
	public JSONArray getAllDefaultCampaignsDBXDB(DataControllerRequest request) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_DEFAULT_CAMPAIGNS_GET_PROC;
		JSONArray responseArray = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(null).build().getResponse();
			
			//response = URLDecoder.decode(response, "UTF-8");
			
			JSONObject responseObj = new JSONObject(response);
			if (responseObj != null &&  responseObj.has("records")
					&& responseObj.optJSONArray("records").length() > 0) {
				responseArray = responseObj.optJSONArray("records");
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch Default Campaigns from database: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAllDefaultCampaignsDBXDB: " + e.getMessage()).log();
			return null;
		}
		return responseArray;
	}

	@Override
	public JSONArray getAllDefaultCampaignsMS(DataControllerRequest request) {
		String serviceName = ServiceId.CAMPAIGNSMS;
		String operationName = OperationName.GET_ALL_DEFAULT_CAMPAIGNS;
		
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		
		headerMap.put("Authorization", request.getParameter("Authorization"));

		JSONArray responseArray = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
								.withOperationId(operationName)
								.withDataControllerRequest(request)
								.withRequestParameters(inputBodyMap)
								.withRequestHeaders(headerMap)
								.build()
								.getResponse();
			response = URLDecoder.decode(response, "UTF-8");
			JSONObject responseObj = new JSONObject(response);
			if (responseObj != null && responseObj.has(CMConstants.DEFAULT_CAMPAIGNS_MS)
					&& responseObj.optJSONArray(CMConstants.DEFAULT_CAMPAIGNS_MS).length() > 0) {
				responseArray = responseObj.optJSONArray(CMConstants.DEFAULT_CAMPAIGNS_MS);
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch Default Campaigns from  Campaign MS: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAllDefaultCampaignsMS: " + e.getMessage()).log();
			return null;
		}
		return responseArray;
	}

	@Override
	public JSONObject updateProfile(JSONObject profile, DataControllerRequest request) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_PROFILE_UPDATE;
		JSONArray profileConditions = profile.optJSONArray(CMConstants.PROFILE_CONDITION);
		String profileId = profile.optString(CMConstants.PROFILE_ID);
		
		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		requestParameters.put(CMConstants.PROFILE_ID, profileId);
		requestParameters.put(CMConstants.PROFILE_NAME, profile.optString(CMConstants.PROFILE_NAME));
		requestParameters.put(CMConstants.PROFILE_DESC, profile.optString(CMConstants.PROFILE_DESC));
		requestParameters.put(CMConstants.PROFILE_STATUS, profile.optString(CMConstants.PROFILE_STATUS));
        
		String response = null;
		JSONObject responseObj;
		try {
			
			JSONObject updateProfileConditionResponse = updateProfileConditions(profileId, profileConditions, request);
			
			if(updateProfileConditionResponse == null || !updateProfileConditionResponse.has(FabricConstants.OPSTATUS)
                    || updateProfileConditionResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Error updating profile conditions in DB").log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Profile condition updation failed");
            	return ErrorCodesEnum.ERR_10010.setErrorCode(updateProfileConditionResponse);
			} else {
				response = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						withRequestParameters(requestParameters).
						build().getResponse();
				
				responseObj = new JSONObject(response);
				if (responseObj == null|| !responseObj.has(FabricConstants.OPSTATUS)
	                    || responseObj.getInt(FabricConstants.OPSTATUS) != 0) {
					alert.prepareError("Error while updating profile in DB").log();
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
	                        ActivityStatusEnum.FAILED, "Error while updating profile in DB");
	            	return ErrorCodesEnum.ERR_10009.setErrorCode(responseObj);
					
				} else {
					responseObj.put(CMConstants.PROFILE_ID, profileId);
					return responseObj;	
				}	
			}
		} catch (Exception e) {
			alert.prepareError("Error updating profile in DB").log();
            diagnostic.prepareDebug("Exception while updating profile in DB.Exception Trace: ", e).log();
            return ErrorCodesEnum.ERR_10009.setErrorCode(new JSONObject());
		}
	}

	@Override
	public JSONObject updateProfileMS(DataControllerRequest request, JSONObject profile) {
		String serviceName = ServiceId.CAMPAIGNSMS;
		String operationName = OperationName.UPDATE_PROFILE;
		
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		JSONArray profileConditions = new JSONArray(profile.optJSONArray(CMConstants.PROFILE_CONDITION));
		inputBodyMap.put(CMConstants.PROFILE_ID, profile.optString(CMConstants.PROFILE_ID));
		inputBodyMap.put(CMConstants.PROFILE_NAME, profile.optString(CMConstants.PROFILE_NAME));
		inputBodyMap.put(CMConstants.PROFILE_DESC, profile.optString(CMConstants.PROFILE_DESC));
		inputBodyMap.put(CMConstants.PROFILE_STATUS, profile.optString(CMConstants.PROFILE_STATUS));
		inputBodyMap.put(CMConstants.PROFILE_CONDITION, profileConditions);

		headerMap.put(CMConstants.AUTHORIZATION, request.getParameter(CMConstants.AUTHORIZATION));
		JSONObject responseObj = null;
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
								.withOperationId(operationName)
								.withDataControllerRequest(request)
								.withRequestParameters(inputBodyMap)
								.withRequestHeaders(headerMap)
								.build()
								.getResponse();

			responseObj = new JSONObject(response);
			if (responseObj == null|| !responseObj.has(FabricConstants.OPSTATUS)
                    || responseObj.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Error updating profile in MS").log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Profile updation failed for MS");
            	return ErrorCodesEnum.ERR_10009.setErrorCode(responseObj);
				
			} else {
				return responseObj;
			}
		}
		catch (Exception e) {
			alert.prepareError("Exception while updating profile in MS " + e.getMessage()).log();
            diagnostic.prepareDebug("Runtime Exception while updating profile in MS.Exception Trace:", e).log();
            return ErrorCodesEnum.ERR_10009.setErrorCode(new JSONObject());
		}
	}
	
	public JSONObject updateProfileConditions(String profileId, JSONArray profileConditions, DataControllerRequest request) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName;
		JSONObject result = new JSONObject();
		try {
		for(int i=0;i<profileConditions.length();i++) {
			JSONObject profileCondition = profileConditions.optJSONObject(i);
			Map<String, Object> requestParameters =  new HashMap<String, Object>();
			requestParameters.put(CMConstants.PROFILE_ID, profileId);
			requestParameters.put(CMConstants.DATACONTEXT_ID, profileCondition.optString(CMConstants.DATACONTEXT_ID));
			requestParameters.put(CMConstants.CONDITION_EXPRESSION, profileCondition.optString(CMConstants.CONDITION_EXPRESSION));
			if(!StringUtils.isBlank(profileCondition.optString(CMConstants.PROFILE_CONDITION_ID))) {
				requestParameters.put(CMConstants.PROFILE_CONDITION_ID, profileCondition.optString(CMConstants.PROFILE_CONDITION_ID));
				operationName = OperationName.DB_PROFILECONDITION_UPDATE;
			}
			else {
				String conditionId = "PC" + Long.toString(CommonUtilities.getNumericId());
				requestParameters.put(CMConstants.PROFILE_CONDITION_ID, conditionId);
				operationName = OperationName.DB_PROFILECONDITION_CREATE;
			}
			String response = null;
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();

			JSONObject responseObj = new JSONObject(response);
			if (responseObj == null || !responseObj.has(FabricConstants.OPSTATUS)
					|| responseObj.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Error updating profile conditions in DB").log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.CAMPAIGN, EventEnum.CREATE,
						ActivityStatusEnum.FAILED, "Profile Condition updation failed");
				return ErrorCodesEnum.ERR_10010.setErrorCode(responseObj);

			} else {
				result = responseObj;
			}
		}
		return result;
		} catch (Exception e) {
	    	alert.prepareError("Exception while updating profile conditions in db " + e.getMessage()).log();
	    	diagnostic.prepareDebug("Exception while updating profile conditions in DB.Exception Trace: ", e).log();
	        return ErrorCodesEnum.ERR_10010.setErrorCode(new JSONObject());
		}
	}
	
	@Override
	public JSONArray getProfileConditionsDB(String profileId) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_PROFILECONDITION_GET;
		JSONArray responseArray = new JSONArray();
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(CMConstants.SELECT, CMConstants.PROFILE_CONDITION_ID);
		inputMap.put(CMConstants.FILTER, "profileId eq " + profileId);
		try {
			//fetch profile
			String getProfileResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			
			JSONObject responseObj = new JSONObject(getProfileResponse);
			if (responseObj != null && responseObj.has(CMConstants.PROFILECONDITION)&&
					responseObj.optJSONArray(CMConstants.PROFILECONDITION).length() > 0) {
				responseArray = responseObj.optJSONArray(CMConstants.PROFILECONDITION);
			}else {
				alert.prepareError("Failed to fetch profile conditions from DB").log();
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch profile conditions from DB: " + e.getMessage()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Error fetching profile conditions in DB").log();
			diagnostic.prepareDebug("Exception while fetching profile conditions in DB.Exception Trace: ", e).log();
            return null;
		}
		return responseArray;
	}

	@Override
	public JSONObject getProfilesDBXDB(DataControllerRequest request) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FETCH_PROFILES_PROC;
		try {
			//fetch profile
			String getProfileResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(null)
					.build()
					.getResponse();
			
			JSONObject responseObj = new JSONObject(getProfileResponse);
			
			if (responseObj != null && responseObj.has(CMConstants.RECORDS) &&
     				responseObj.has(CMConstants.RECORDS1)) {
				return responseObj;
			}else {
				alert.prepareError("Error while fetching profiles from DB").log();
				return ErrorCodesEnum.ERR_10016.setErrorCode(new JSONObject());
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch profiles from DB: " + e.getMessage()).log();
			return ErrorCodesEnum.ERR_10016.setErrorCode(new JSONObject());
		} catch (Exception e) {
			alert.prepareError("Exception fetching profile in DB").log();
			diagnostic.prepareDebug("Exception while fetching profile in DB.Exception Trace: ", e).log();
			return ErrorCodesEnum.ERR_10016.setErrorCode(new JSONObject());
			}
	}
	
	@Override
	public JSONArray getProfilesMS(DataControllerRequest request) {
	    String serviceName = ServiceId.CAMPAIGNSMS;
	    String operationName = OperationName.OP_GET_PROFILES;
	    JSONArray responseArray = new JSONArray();
		try {
			Map<String, Object> requestParamMap = new HashMap<String, Object>();
			Map<String, Object> headerMap = new HashMap<String, Object>();
            headerMap.put("Authorization", request.getParameter("Authorization"));
            
            JSONObject getProfileResponse = new JSONObject();
            String getProfileRes = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withDataControllerRequest(request)
					.withOperationId(operationName)
					.withRequestParameters(requestParamMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
            
            getProfileResponse = new JSONObject(getProfileRes);
            if (getProfileResponse != null && getProfileResponse.has(CMConstants.ALL_PROFILES)
					&& getProfileResponse.optJSONArray(CMConstants.ALL_PROFILES).length() > 0) {
				responseArray = getProfileResponse.optJSONArray(CMConstants.ALL_PROFILES);
			}	
		} catch (Exception e) {
			alert.prepareError("Exception while fetching profile from MS " + e.getMessage()).log();
            diagnostic.prepareDebug("Runtime Exception while fetching profile from MS.Exception Trace:", e).log();
			 return null;
		}
		return responseArray;
	}
	
	@Override
	public JSONObject deleteRemovedProfileConditions(String profileId, List<String> profileConditions) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_PROFILECONDITION_DELETE;
		JSONObject result = new JSONObject();
		Map<String, Object> inputMap = new HashMap<>();
		String filterQuery = "";
		filterQuery = "profileConditionId eq " +String.join(" or profileConditionId eq ", profileConditions);
		inputMap.put(CMConstants.FILTER, filterQuery);
		try {
			//fetch profile
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			
			JSONObject responseObj = new JSONObject(response);
			if (responseObj != null && responseObj.has("deletedRecords")&&
					responseObj.optInt("deletedRecords") > 0) {
				result.put("success", true);
			}else {
				result.put("success", false);
				alert.prepareError("Failed to fetch profile conditions from DB").log();
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch profile conditions from DB: " + e.getMessage()).log();
			result.put("success", false);
		} catch (Exception e) {
			alert.prepareError("Error fetching profile conditions in DB").log();
			diagnostic.prepareDebug("Exception while fetching profile conditions in DB.Exception Trace: ", e).log();
			result.put("success", false);
		}
		return result;
	}

	@Override
	public JSONObject updateDefaultCampaignsMS(DataControllerRequest request) {
		String serviceName = ServiceId.CAMPAIGNSMS;
		String operationName = OperationName.UPDATE_DEFAULT_CAMPAIGNS;
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		inputBodyMap.put(CMConstants.DEFAULT_CAMPAIGN, request.getParameter("defaultCampaign"));
		headerMap.put("Authorization", request.getParameter("Authorization"));
		JSONObject responseObj;
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withDataControllerRequest(request)
					.withRequestParameters(inputBodyMap).withRequestHeaders(headerMap).build().getResponse();
			responseObj = new JSONObject(response);
		} catch (JSONException e) {
			alert.prepareError("Failed to update Default Campaigns from  Campaign MS: " +e.getStackTrace()[0].toString()).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at updateDefaultCampaignsMS: " + e.getStackTrace()[0].toString()).log();
			return null;
		}
		return responseObj;
	}

	@Override
	public JSONObject updateDefaultCampaignsDBXDB(DataControllerRequest request) {
		JSONObject finalResponse = new JSONObject();
		try {
			String serviceName = ServiceId.CRUDLAYER;
			String operationName = OperationName.DB_DEFAULT_CAMPAIGNS_UPDATE_PROC;
			StringBuilder inputdata = new StringBuilder();
			String defaultCampaign = request.getParameter("defaultCampaign");
			JSONArray defaultCampaignArray = null;

			defaultCampaignArray = new JSONArray(defaultCampaign);

			for (int i = 0; i < defaultCampaignArray.length(); i++) {
				String onlineContentId = defaultCampaignArray.getJSONObject(i).getString("onlineContentId");
				String placeholderId = defaultCampaignArray.getJSONObject(i).getString("placeholderId");
				String imageURL = defaultCampaignArray.getJSONObject(i).getString("imageURL");
				String targetURL = defaultCampaignArray.getJSONObject(i).getString("targetURL");
				String imageIndex = defaultCampaignArray.getJSONObject(i).getString("imageIndex");
				inputdata.append(onlineContentId);
				inputdata.append("<>");
				inputdata.append(placeholderId);
				inputdata.append("<>");
				inputdata.append(imageURL);
				inputdata.append("<>");
				inputdata.append(targetURL);
				inputdata.append("<>");
				inputdata.append(imageIndex);
				if (i < defaultCampaignArray.length() - 1)
					inputdata.append("|");
			}
			String campaignData = URLDecoder.decode(inputdata.toString(), "UTF-8");

			Map<String, Object> requestParametersMap = new HashMap<String, Object>();
			requestParametersMap.put("_campaignData", campaignData);

			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParametersMap).build().getResponse();

			JSONObject responseObj = CommonUtilities.getStringAsJSONObject(response);
			if (responseObj != null && responseObj.has("opstatus") && responseObj.has("records")
					&& responseObj.get("records") != null
					&& responseObj.get("opstatus").toString().equalsIgnoreCase("0")) {
				finalResponse.put("onlineContentId",
						responseObj.getJSONArray("records").getJSONObject(0).getString("onlineContentId"));
				finalResponse.put("message", "Default campaigns updated successfully");
				finalResponse.put("status", "Success");
			}

		} catch (JSONException e) {
			alert.prepareError("Failed to fetch Default Campaigns from database: " + e.getStackTrace()[0].toString()).log();

		} catch (Exception e) {
			alert.prepareError("Caught exception at getAllDefaultCampaignsDBXDB: " + e.getStackTrace()[0].toString()).log();

		}
		return finalResponse;
	}

}
