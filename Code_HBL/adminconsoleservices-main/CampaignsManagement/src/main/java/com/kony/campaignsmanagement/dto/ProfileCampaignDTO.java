package com.kony.campaignsmanagement.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.utilities.ErrorCodeEnum;


@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileCampaignDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3340366803186392409L;
	
	private String campaignId;
	private String campaignName;
	private String campaignDescription;
	private String campaignStatus;
	private String startDate;
	private String endDate;
	private String dbpErrMsg;
	private String dbpErrCode;
	private ErrorCodeEnum errCode;
	
	
	public ProfileCampaignDTO() {
		super();
	}


	public ProfileCampaignDTO(String campaignId, String campaignName, String campaignDescription, String campaignStatus,
			String startDate, String endDate, String dbpErrMsg, String dbpErrCode, ErrorCodeEnum errCode) {
		super();
		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.campaignDescription = campaignDescription;
		this.campaignStatus = campaignStatus;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dbpErrMsg = dbpErrMsg;
		this.dbpErrCode = dbpErrCode;
		this.errCode = errCode;
	}

	public String getCampaignId() {
		return campaignId;
	}


	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}


	public String getCampaignName() {
		return campaignName;
	}


	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}


	public String getCampaignDescription() {
		return campaignDescription;
	}


	public void setCampaignDescription(String campaignDescription) {
		this.campaignDescription = campaignDescription;
	}


	public String getCampaignStatus() {
		return campaignStatus;
	}


	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getDbpErrMsg() {
		return dbpErrMsg;
	}


	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}


	public String getDbpErrCode() {
		return dbpErrCode;
	}


	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}


	public ErrorCodeEnum getErrCode() {
		return errCode;
	}


	public void setErrCode(ErrorCodeEnum errCode) {
		this.errCode = errCode;
	}


	@Override
	public int hashCode() {
		return Objects.hash(campaignDescription, campaignId, campaignName, campaignStatus, dbpErrCode, dbpErrMsg,
				endDate, errCode, startDate);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileCampaignDTO other = (ProfileCampaignDTO) obj;
		return Objects.equals(campaignDescription, other.campaignDescription)
				&& Objects.equals(campaignId, other.campaignId) && Objects.equals(campaignName, other.campaignName)
				&& Objects.equals(campaignStatus, other.campaignStatus) && Objects.equals(dbpErrCode, other.dbpErrCode)
				&& Objects.equals(dbpErrMsg, other.dbpErrMsg) && Objects.equals(endDate, other.endDate)
				&& errCode == other.errCode && Objects.equals(startDate, other.startDate);
	}
	
}


