package com.kony.campaign.dto;

import com.kony.campaign.common.CampaignConstants;

public enum CampaignRequestType {	
	
	ONLINE_PLACEHOLDER_EVENT("online_placeholder",true,CampaignConstants.SERVICE_ORCH_NAME,CampaignConstants.ORCH_OPERATION_ONLINE_PLACEHOLDER),
	ONLINE_NONPLACEHOLDER_EVENT("online_nonplaceholder",true,CampaignConstants.SERVICE_ORCH_NAME,CampaignConstants.ORCH_OPERATION_OFFLINE),
	OFFLINE_EVENT("offline",true,CampaignConstants.SERVICE_ORCH_NAME,CampaignConstants.ORCH_OPERATION_OFFLINE),
	PRELOGIN_EVENT("prelogin",false,CampaignConstants.SERVICE_ORCH_NAME,CampaignConstants.ORCH_OPERATION_PRELOGIN),
	CACHE_REALTIME("cacherealtime",true,null,null),
	JOB("job",false,CampaignConstants.CAMPAIGN_PROCESSING_SERVICE,CampaignConstants.GET_ALL_CAMPAIGNS);

	private String requestType;
	private boolean filterOnCustomer;
	private String serviceName;
	private String operationName;

	private CampaignRequestType(String requestType, boolean filterOnCustomer, String serviceName,
			String operationName) {
		this.requestType = requestType;
		this.filterOnCustomer = filterOnCustomer;
		this.serviceName = serviceName;
		this.operationName = operationName;		
	}

	public String getRequestType() {
		return requestType;
	}
	
	public boolean isFilterOnCustomer() {
		return filterOnCustomer;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getOperationName() {
		return operationName;
	}
}
