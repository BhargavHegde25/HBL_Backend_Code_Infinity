package com.kony.campaign;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.campaign.common.ErrorCodes;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.engine.CampaignThreadPoolExecutor;
import com.kony.campaign.engine.RealTimeCacheUpdateTask;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Result;

public class CacheCampaignEvalutor extends AbstractCampaignEvalutor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<Campaign> processCampaignResponse(CampaignRequest cReq, Result campaignResult)
			throws CampaignException {		
		try {
			List<Campaign> campList = super.processCampaignResponse(cReq, campaignResult);			
			if(campList!= null && !campList.isEmpty()) {			
				RealTimeCacheUpdateTask offlineCacheTask = new RealTimeCacheUpdateTask(cReq, campList, campaignResult);
				executeOfflineCacheUpdateTask(offlineCacheTask);
			}
			return campList;
		} catch (Exception e) {
			alert.prepareError("error while processing campaign reponse"+ e).log();
			throw new CampaignException("error while processing campaign reponse",e,ErrorCodes.ERR_17002.getErrorCode());
		}
	}	

	@Override
	public Result hitAnalytics(CampaignRequest campaignRequest, List<Campaign> campaignList) {
		return null;
	}

	@Override
	public List<Campaign> processAnalyticsResponse(CampaignRequest campaignRequest, Result analyticsCampaignResult,
			List<Campaign> totalcampaignList) throws CampaignException {
		List<Campaign> campaigneligibleList = null;
		try {
			String eigibleCampaignIdstr = totalcampaignList.stream().filter(cd -> cd.getDcList() == null)
					.map(Campaign::getCampaignId).collect(Collectors.joining(","));
			Object cacheObj =CampaignUtil.getFromCache(campaignRequest.getEvent().getCoreCustId());
			final String eligibleCampStr =cacheObj != null ? new StringBuilder(eigibleCampaignIdstr).append(cacheObj).toString() :
					eigibleCampaignIdstr;	
			logCachedAndDefaultEligibleCampaigns(eligibleCampStr);
			campaigneligibleList = totalcampaignList.stream().
					filter(c ->  eligibleCampStr.contains(c.getCampaignId())).collect(Collectors.toList());
			campaigneligibleList = campaigneligibleList == null ?  new ArrayList<>() : campaigneligibleList;			
			campaigneligibleList.sort(Comparator.comparingLong(Campaign::getPriority));		
		} catch (Exception e) {
			alert.prepareError("Error while processing cache analytics response " + e.getMessage()).log();
			throw new CampaignException("Error while processing cache analytics response ",
					e,ErrorCodes.ERR_17010.getErrorCode());
		}
		return campaigneligibleList;		
		}	


	private void logCachedAndDefaultEligibleCampaigns(final String eligibleCampStr) {
		if(diagnostic.isDebugEnabled()){
		diagnostic.prepareDebug("Campaign eligibleCampStr is "+ eligibleCampStr).log();
		}
	}
	
	private void executeOfflineCacheUpdateTask(RealTimeCacheUpdateTask offlineCacheTask) {
		try {
			CampaignThreadPoolExecutor.execute(offlineCacheTask);
		} catch (Exception e) {				
			diagnostic.prepareInfo("error while sending offline update cache task" + e.getMessage()).log();		
		}
	}

}
