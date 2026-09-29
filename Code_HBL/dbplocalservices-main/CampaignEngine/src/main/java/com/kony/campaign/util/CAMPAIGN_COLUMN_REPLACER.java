package com.kony.campaign.util;

import com.kony.campaign.common.CampaignConstants;

public enum CAMPAIGN_COLUMN_REPLACER {
	OC_TARGET_URL(CampaignConstants.GETCAMPAIGNS_TARGET_URL,true),
	OC_BANNER_DESCRIPTION(CampaignConstants.BANNER_DESCRIPTION,true),
	OC_BANNER_TITLE(CampaignConstants.BANNER_TITLE,true),
	OC_BUTTON_LABEL(CampaignConstants.BUTTON_LABEL,true),
	OC_ACTION_TARGET_URL(CampaignConstants.ACTION_TARGET_URL,true),
	OC_SHOW_CLOSEON(CampaignConstants.SHOW_CLOSEON,false),
	OC_SHOW_READ_LATER_BUTTON(CampaignConstants.SHOW_READ_LATER_BUTTON,false);		
	
	private String colName ;
	private boolean decodable ;	
	
	private CAMPAIGN_COLUMN_REPLACER(String colName, boolean decodable) {
		this.colName = colName;
		this.decodable = decodable;
	}	

	public boolean isDecodable() {
		return decodable;
	}

	public String getColName() {
		return colName;
	}		
}


