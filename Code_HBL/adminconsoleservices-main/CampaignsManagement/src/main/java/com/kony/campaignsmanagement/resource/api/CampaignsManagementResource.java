package com.kony.campaignsmanagement.resource.api;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CampaignsManagementResource extends Resource {
	
	public Result getAllPlaceHolders(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

	public Result getEventTriggers(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result createCampaign(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

	public Result createProfile(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);
	
	public JSONArray getProfilesInDB(String profileName, String profileId);
	
	public Result updateProfile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result getCampaigns(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

	public Result getProfiles(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
			
	public Result getAllDefaultCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateCampaign(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	public JSONObject deleteRemovedProfileConditions(String profileId, JSONArray profileConditions);

	public Result updateDefaultCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
}
