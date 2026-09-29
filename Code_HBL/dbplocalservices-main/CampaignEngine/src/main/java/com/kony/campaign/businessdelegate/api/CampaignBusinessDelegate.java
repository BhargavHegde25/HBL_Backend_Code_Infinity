package com.kony.campaign.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.campaign.CampaignException;
import com.kony.campaign.dto.EventDTO;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface CampaignBusinessDelegate extends BusinessDelegate {
	
	Result getInternalCampaigns(EventDTO event);

	Result getExternalCampaigns(EventDTO event);

	Result getCampaignsForJob();
	
	Result getAllCampaignsForAnEvent(DataControllerRequest request);
	
	Result getDefaultCampaigns(DataControllerRequest request);
	
	boolean insertCustCompletedCampaigns(String userId , String campaignId) throws CampaignException;
}
