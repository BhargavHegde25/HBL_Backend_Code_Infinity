package com.kony.campaign.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.dto.EventDTO;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class RealTimeCacheUpdateTask implements Callable<Object> {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	private Result campaignResult;
	private List<Campaign> campaignList;
	private EventDTO cacheEvent;

	public RealTimeCacheUpdateTask(CampaignRequest creq, List<Campaign> campList,Result campaignResult) {
		super();
		this.campaignResult = campaignResult.getCopy();
		this.campaignList = new ArrayList<>(campList);	
		this.cacheEvent = getEvent(creq.getEvent());		
	}

	private EventDTO getEvent(EventDTO event) {
		EventDTO newEvent = new EventDTO();
		newEvent.setEventId(event.getEventId());
		newEvent.setCoreCustId(event.getCoreCustId());
		return newEvent;
	}

	@Override
	public Object call() throws Exception {		
		diagnostic.prepareDebug("cacheTask camp result is" +(campaignResult != null ? ResultToJSON.convert(campaignResult) : "null campaignResponse")).log();
		diagnostic.prepareDebug("cacheTask campaignList is " + campaignList).log();
		CampaignInternalBusinessDelegate.getCampaignsForCacheUpdateRealTime(
				cacheEvent, new ArrayList<Campaign>(campaignList), campaignResult);
		return true;
	}
	
}
