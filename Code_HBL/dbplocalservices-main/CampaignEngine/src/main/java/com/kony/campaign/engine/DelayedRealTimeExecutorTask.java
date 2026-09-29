package com.kony.campaign.engine;

import java.util.concurrent.Callable;

import com.kony.campaign.dto.EventDTO;

public class DelayedRealTimeExecutorTask implements Callable<Boolean> {
	
	private EventDTO inputevent;		

	public DelayedRealTimeExecutorTask(EventDTO inputevent) {
		super();
		this.inputevent = inputevent;
	}
	
	@Override
	public Boolean call() throws Exception {
		CampaignInternalBusinessDelegate.executeInternalNonPlaceHolderReq(inputevent);
		return Boolean.TRUE;
	}

	public EventDTO getInputevent() {
		return inputevent;
	}

	public void setInputevent(EventDTO inputevent) {
		this.inputevent = inputevent;
	}
	
}
