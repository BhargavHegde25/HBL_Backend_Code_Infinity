package com.kony.campaign;

import static com.kony.campaign.common.ErrorCodes.ERR_17009;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class PreLoginCampaignEvalutor extends AbstractCampaignEvalutor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result hitAnalytics(CampaignRequest campaignRequest, List<Campaign> campaignList) throws CampaignException {
		return null;		
	}

	@Override
	public List<Campaign> processAnalyticsResponse(CampaignRequest campaignRequest, Result analyticsCampaignResult,
			List<Campaign> totalcampaignList) throws CampaignException {
		return totalcampaignList;
	}

	@Override
	public Result processEligibleCampaigns(CampaignRequest campaignRequest, List<Campaign> campList,
			Result campaignResult) throws CampaignException {

	Result res =  InternalResponseProcessor.getEmptyCampaignResult();
	try {				
		campList.sort(Comparator.comparingLong(Campaign::getPriority));
		int respCampaignCount = campList.size() > campaignRequest.getCampaignCountInResponse() ? campaignRequest.getCampaignCountInResponse() : campList.size();
		for(int i=0;i<respCampaignCount;i++) {
			Campaign campaign = campList.get(i);
			 Optional<Record> optRecord = getSelctedCampaignFromResult(campaignResult, campaign);
				if (optRecord.isPresent()) {
					Record campaignRecord = optRecord.get();
					List<String> channelList = campaignRecord
							.getDatasetById(CampaignConstants.GETCAMPAIGNS_CHANNEL_DETAILS).getAllRecords().stream()
							.filter(recimage -> campaignRequest.getEvent().getChannel()
									.equals(recimage.getParamByName(CampaignConstants.GETCAMPAIGNS_CHANNEL_SUB_TYPE)
											.getValue()))
							.map(recimage -> recimage.getParamByName(CampaignConstants.GETCAMPAIGNS_CHANNEL_SUB_TYPE)
									.getValue())
							.collect(Collectors.toList());
 					if (campaignRequest.getEvent().getChannel() != null
							&& channelList.contains(campaignRequest.getEvent().getChannel())) {
						InternalResponseProcessor.getResponseForEligibleCamapaign(campaign,
								campaignRequest.getEvent(), campaignRecord, res);
					} 
				}	
			}			
		} catch (Exception e) {
			alert.prepareError(ERR_17009.getMessage(), e).log();
			throw new CampaignException(ERR_17009.getMessage(), e ,
					ERR_17009.getErrorCode());
		}
		return res;
	}

	@Override
	protected List<Campaign> filterIgnoredCampaigns(List<Campaign> campList, Result campResult)
			throws CampaignException {
		return campList;		
	}
	
}
