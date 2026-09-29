package com.kony.campaign.dto;

import com.kony.campaign.CampaignEvalutor;
import com.kony.campaign.common.CampaignConstants.CampaignFilterTypes;

public class CampaignRequest {

	public CampaignRequest(CampaignRequestType requestType) {
		super();
		this.requestType = requestType;
	}	

	public CampaignRequest(CampaignRequestType requestType, EventDTO event, boolean updateCache) {
		super();
		this.requestType = requestType;
		this.event = event;
		this.updateCache = updateCache;
		this.campaignCountInResponse = 1;
	}

	private CampaignRequestType requestType;

	private EventDTO event;

	private CampaignEvalutor campaignEvalutor;
	
	private boolean updateCache;
	
	private int campaignCountInResponse;

	private String campaignFilter = CampaignFilterTypes.NO_FILTER.name();

	public CampaignRequestType getRequestType() {
		return requestType;
	}	

	public void setRequestType(CampaignRequestType requestType) {
		this.requestType = requestType;
	}

	public EventDTO getEvent() {
		return event;
	}

	public void setEvent(EventDTO event) {
		this.event = event;
	}

	public CampaignEvalutor getCampaignEvalutor() {
		return campaignEvalutor;
	}

	public void setCampaignEvalutor(CampaignEvalutor campaignEvalutor) {
		this.campaignEvalutor = campaignEvalutor;
	}
	public String getCampaignFilter() {
		return campaignFilter;
	}

	public void setCampaignFilter(String campaignFilter) {
		this.campaignFilter = campaignFilter;
	}

	public boolean isUpdateCache() {
		return updateCache;
	}

	public void setUpdateCache(boolean updateCache) {
		this.updateCache = updateCache;
	}

	public int getCampaignCountInResponse() {
		return campaignCountInResponse;
	}

	public void setCampaignCountInResponse(int campaignCountInResponse) {
		this.campaignCountInResponse = campaignCountInResponse;
	}		
}
