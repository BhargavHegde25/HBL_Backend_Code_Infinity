package com.kony.campaignsmanagement.dto;

public class Campaigndefinition {
	private String campaignId;

	public String getcampaignId() {
		return this.campaignId;
	}

	public void setcampaignId(String value) {
		this.campaignId = value;
	}

	private String campaignName;

	public String getcampaignName() {
		return this.campaignName;
	}

	public void setcampaignName(String value) {
		this.campaignName = value;
	}

	private String campaignDescription;

	public String getcampaignDescription() {
		return this.campaignDescription;
	}

	public void setcampaignDescription(String value) {
		this.campaignDescription = value;
	}

	private String objectiveType;

	public String getobjectiveType() {
		return this.objectiveType;
	}

	public void setobjectiveType(String value) {
		this.objectiveType = value;
	}

	private String productId;

	public String getproductId() {
		return this.productId;
	}

	public void setproductId(String value) {
		this.productId = value;
	}

	private String productGroupId;

	public String getproductGroupId() {
		return this.productGroupId;
	}

	public void setproductGroupId(String value) {
		this.productGroupId = value;
	}

	private int campaignPriority;

	public int getcampaignPriority() {
		return this.campaignPriority;
	}

	public void setcampaignPriority(int value) {
		this.campaignPriority = value;
	}

	private String campaignType;

	public String getcampaignType() {
		return this.campaignType;
	}

	public void setcampaignType(String value) {
		this.campaignType = value;
	}

	private String campaignStatus;

	public String getcampaignStatus() {
		return this.campaignStatus;
	}

	public void setcampaignStatus(String value) {
		this.campaignStatus = value;
	}

	private java.sql.Date startDate;

	public java.sql.Date getstartDate() {
		return this.startDate;
	}

	public void setstartDate(java.sql.Date value) {
		this.startDate = value;
	}

	private java.sql.Date endDate;

	public java.sql.Date getendDate() {
		return this.endDate;
	}

	public void setendDate(java.sql.Date value) {
		this.endDate = value;
	}

}