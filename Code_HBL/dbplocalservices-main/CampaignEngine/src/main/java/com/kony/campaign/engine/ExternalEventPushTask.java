package com.kony.campaign.engine;

import java.util.List;
import java.util.concurrent.Callable;

import com.kony.campaign.ExternalResponseProcessor;
import com.kony.campaign.dto.CampaignRequest;
import com.konylabs.middleware.dataobject.Record;

public class ExternalEventPushTask implements Callable<Boolean> {
	
	private CampaignRequest campaignRequest;
	private Record campaignRecord;
	private List<String> channelList;	

	public ExternalEventPushTask(CampaignRequest campaignRequest, Record campaignRecord, List<String> channelList) {
		super();
		this.campaignRequest = campaignRequest;
		this.campaignRecord = campaignRecord;
		this.channelList = channelList;
	}

	@Override
	public Boolean call() throws Exception {
		ExternalResponseProcessor.pushToExternalChannels(campaignRequest, campaignRecord, channelList);
		return Boolean.TRUE;
	}

}
