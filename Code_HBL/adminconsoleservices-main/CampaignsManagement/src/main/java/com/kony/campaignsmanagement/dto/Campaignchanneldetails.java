package com.kony.campaignsmanagement.dto;

public class Campaignchanneldetails
{
	private String campaignId;
	public String getcampaignId()
	{
		return this.campaignId;
	}
	public void setcampaignId(String value)
	{
		this.campaignId = value;
	}

	private int channelPriority;
	public int getchannelPriority()
	{
		return this.channelPriority;
	}
	public void setchannelPriority(int value)
	{
		this.channelPriority = value;
	}

	private String channelSubType;
	public String getchannelSubType()
	{
		return this.channelSubType;
	}
	public void setchannelSubType(String value)
	{
		this.channelSubType = value;
	}



}