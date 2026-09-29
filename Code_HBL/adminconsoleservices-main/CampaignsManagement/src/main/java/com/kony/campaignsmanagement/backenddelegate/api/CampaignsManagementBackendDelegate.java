package com.kony.campaignsmanagement.backenddelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface CampaignsManagementBackendDelegate extends BackendDelegate {
	
	public JSONObject createProfileInDBXDB(DataControllerRequest request);
	public JSONObject createProfileInMS(DataControllerRequest request);
	JSONArray getProfilesDB(String profileName, String profileId);
	public JSONObject createProfileConditionInDBXDB(DataControllerRequest request, String profileConditions, String profileId);
	public JSONArray getAllPlaceHolders(DataControllerRequest request);
	public JSONArray getAllPlaceHoldersMS(DataControllerRequest request);
	public JSONArray getEventTriggers(DataControllerRequest request);
	public JSONArray getEventTriggersMS(DataControllerRequest request);
	public JSONObject createCampaign(Map<String, Object> postParametersMap,  Map<String, Object> headerMap)
            throws DBPApplicationException;
	public JSONObject updateProfile(JSONObject profile, DataControllerRequest request);
	public JSONObject updateProfileMS(DataControllerRequest request, JSONObject profile);
	JSONArray getProfileConditionsDB(String profileId);
	
	public JSONObject updateCampaign(Map<String, Object> postParametersMap,  Map<String, Object> headerMap)
            throws DBPApplicationException;
	
	public JSONObject getCampaigns(Map<String, Object> postParametersMap,  Map<String, Object> headerMap)
            throws DBPApplicationException;
	public JSONObject getProfilesDBXDB(DataControllerRequest request);
	public JSONArray getProfilesMS(DataControllerRequest request);
	
	public JSONArray getAllDefaultCampaignsMS(DataControllerRequest request);
	public JSONArray getAllDefaultCampaignsDBXDB(DataControllerRequest request);
	
	public JSONObject deleteRemovedProfileConditions(String profileId, List<String> profileConditions);
	
	public JSONObject updateDefaultCampaignsMS(DataControllerRequest request );
	public JSONObject updateDefaultCampaignsDBXDB(DataControllerRequest request);
	
}
