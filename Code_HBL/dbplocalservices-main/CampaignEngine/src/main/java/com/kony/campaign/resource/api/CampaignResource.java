package com.kony.campaign.resource.api;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.dbp.core.api.Resource;

public interface CampaignResource extends Resource {

	Result getCampaignsForInternalEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	Result getCampaignsForExternalEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
  
	Result insertCustCompletedCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getAllCampaignsForAnEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getDefaultCampaigns(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}

