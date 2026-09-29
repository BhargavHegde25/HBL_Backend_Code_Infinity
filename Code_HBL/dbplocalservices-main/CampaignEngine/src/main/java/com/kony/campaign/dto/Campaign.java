package com.kony.campaign.dto;

import java.util.List;
import java.util.Set;

public class Campaign {
	
	private String campaignId;
	private long priority;
	private List<DataContext> dcList; 
	private String eligable;
	private boolean error;
	private Set<String> customers;
	
	public Campaign(String campaignId) {
		this.campaignId = campaignId;
	}
	
	public Campaign(String campaignId, long priority, List<DataContext> dcList) {
		super();
		this.campaignId = campaignId;
		this.priority = priority;
		this.dcList = dcList;
		
	}
	
	@Override
	public String toString() {
		return campaignId;
	}

	public List<DataContext> getDcList() {
		return dcList;
	}

	public void setDcList(List<DataContext> dcList) {
		this.dcList = dcList;
	}

	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public long getPriority() {
		return priority;
	}
	public void setPriority(long priority) {
		this.priority = priority;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getEligable() {
		return eligable;
	}	
	
	public Set<String> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<String> customers) {
		this.customers = customers;
	}

}
