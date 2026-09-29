package com.kony.campaign;

import java.util.List;

import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.konylabs.middleware.dataobject.Result;

public class CacheRealTimeCampaignEvalutor extends RealTimeCampaignEvalutor {
	
	@Override
	public Result fetchCampaigns(CampaignRequest campaignRequest) throws CampaignException {		
		return getCampaignResult();
	}		

	@Override
	public List<Campaign> processCampaignResponse(CampaignRequest cReq, Result campaignResult)
			throws CampaignException {
		return getCampaignList();
	}
	
	@Override
	public Result processEligibleCampaigns(CampaignRequest campaignRequest, List<Campaign> campList,
			Result campaignResult) throws CampaignException {		
		return new Result();
	}

}
