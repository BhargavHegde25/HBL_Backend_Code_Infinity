package com.kony.campaign;

import java.util.List;

import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.konylabs.middleware.dataobject.Result;

public interface CampaignEvalutor {
	
	Result fetchCampaigns(CampaignRequest campaignRequest) throws CampaignException;
	
	List<Campaign> processCampaignResponse (CampaignRequest campaignRequest, Result campaignResult) throws CampaignException;
	
	Result hitAnalytics(CampaignRequest campaignRequest,List<Campaign> campaignList) throws CampaignException;
	
	List<Campaign> processAnalyticsResponse(CampaignRequest campaignRequest, Result analyticsCampaignResult,
			List<Campaign> totalcampaignList) throws CampaignException;
	
	Result processEligibleCampaigns(CampaignRequest campaignRequest,List<Campaign> campList, Result campaignResult) throws CampaignException;
	
    void processDefaultCampaigns(CampaignRequest campaignRequest,Result campaignResult,Result campaignsSelected) throws CampaignException;
}
