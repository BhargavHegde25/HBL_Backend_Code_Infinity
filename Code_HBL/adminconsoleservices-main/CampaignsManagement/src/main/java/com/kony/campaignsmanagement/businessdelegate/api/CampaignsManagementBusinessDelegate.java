package com.kony.campaignsmanagement.businessdelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface CampaignsManagementBusinessDelegate extends BusinessDelegate {
	
	public JSONArray getAllPlaceHolders(DataControllerRequest request);
	public JSONArray getAllPlaceHoldersMS(DataControllerRequest request);
	public JSONArray getEventTriggersMS(DataControllerRequest request);
	public JSONArray getEventTriggers(DataControllerRequest request);
	public JSONObject createCampaign(Map<String, Object> postParametersMap,  Map<String, Object> headerMap)
            throws DBPApplicationException;
	public JSONObject updateCampaign(Map<String, Object> postParametersMap,  Map<String, Object> headerMap)
            throws DBPApplicationException;
	public JSONObject updateProfile(JSONObject profile, DataControllerRequest request);
	public JSONObject updateProfileMS(DataControllerRequest request, JSONObject profile);

	public JSONObject createProfileDBXDB(DataControllerRequest dataControllerRequest);
	public JSONObject createProfileMS(DataControllerRequest dataControllerRequest);
	public JSONArray getProfilesDB(String profileName, String profileId);
	public JSONArray getProfileConditionsDB(String profileId);
	public JSONObject getCampaigns(Map<String, Object> postParametersMap,  Map<String, Object> headerMap)
            throws DBPApplicationException;
			
	public JSONArray getProfilesDBXDB(DataControllerRequest request);
	public JSONArray getProfilesMS(DataControllerRequest request);
	public JSONObject deleteRemovedProfileConditions(String profileId, List<String> profileConditions);

	public JSONArray getAllDefaultCampaignsMS(DataControllerRequest dataControllerRequest);
	public JSONArray getAllDefaultCampaignsDBXDB(DataControllerRequest dataControllerRequest);
	public JSONObject updateDefaultCampaignsMS(DataControllerRequest dataControllerRequest);
	public JSONObject updateDefaultCampaignsDBXDB(DataControllerRequest dataControllerRequest);
}
