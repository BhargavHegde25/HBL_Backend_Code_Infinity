package com.kony.campaignsmanagement.dto;

import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.campaignsmanagement.utils.CMConstants;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDTO implements DBPDTO{

	private static final long serialVersionUID = -8860205768975022142L;

	private String id;
	private String profileId;
	private String profileName;
	private String profileDescription;
	private String profileCreationDate;
	private String profileDeactivatedDate;
	private String profileStatus;
	private JSONArray profileConditions;
	private JSONArray associatedCampaignDetails;
	private int numberOfUsers;
	private int numberOfCampaigns;
	private String dbpErrMsg;
	private String dbpErrCode;
	private ErrorCodeEnum errCode;

	public ProfileDTO() {
		super();
	}

	public ProfileDTO(String id, String profileId, String profileName, String profileDescription,
			String profileCreationDate, String profileStatus, JSONArray profileConditions,
			JSONArray associatedCampaignDetails, int numberOfCampaigns, ErrorCodeEnum errCode, String dbpErrCode, String dbpErrMsg, String profileDeactivatedDate, int numberOfUsers) {
		super();
		this.id = id;
		this.profileId = profileId;
		this.profileName = profileName;
		this.profileDescription = profileDescription;
		this.profileCreationDate = profileCreationDate;
		this.profileDeactivatedDate=  profileDeactivatedDate;
		this.profileStatus = profileStatus;
		this.numberOfUsers = numberOfUsers;
		this.profileConditions = profileConditions;
		this.associatedCampaignDetails = associatedCampaignDetails;
		this.numberOfCampaigns = numberOfCampaigns;
		this.dbpErrMsg = dbpErrMsg;
		this.dbpErrCode = dbpErrCode;
		this.errCode = errCode;
	}
    
	public String getProfileDeactivatedDate() {
		return profileDeactivatedDate;
	}

	public void setProfileDeactivatedDate(String profileDeactivatedDate) {
		this.profileDeactivatedDate = profileDeactivatedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileDescription() {
		return profileDescription;
	}

	public void setProfileDescription(String profileDescription) {
		this.profileDescription = profileDescription;
	}

	public String getProfileCreationDate() {
		return profileCreationDate;
	}

	public void setProfileCreationDate(String profileCreationDate) {
		this.profileCreationDate = profileCreationDate;
	}

	public String getProfileStatus() {
		return profileStatus;
	}

	public void setProfileStatus(String profileStatus) {
		this.profileStatus = profileStatus;
	}

	public JSONArray getProfileConditions() {
		return profileConditions;
	}

	public void setProfileConditions(JSONArray profileConditions) {
		this.profileConditions = profileConditions;
	}

	public JSONArray getAssociatedCampaignDetails() {
		return associatedCampaignDetails;
	}

	public void setAssociatedCampaignDetails(JSONArray associatedCampaignDetails) {
		this.associatedCampaignDetails = associatedCampaignDetails;
	}

	public int getNumberOfCampaigns() {
		return numberOfCampaigns;
	}

	public void setNumberOfCampaigns(int numberOfCampaigns) {
		this.numberOfCampaigns = numberOfCampaigns;
	}
	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}

	public ErrorCodeEnum getErrCode() {
		return errCode;
	}
	public JSONObject convertProfileDTOToJSONObject () {
		JSONObject profileObj = new JSONObject();
		profileObj.put(CMConstants.PROFILE_ID, profileId);
		profileObj.put(CMConstants.PROFILE_NAME, profileName);
		profileObj.put(CMConstants.PROFILE_DESC, profileDescription);
		profileObj.put(CMConstants.PROFILE_STATUS, profileStatus);
		profileObj.put(CMConstants.PROFILE_CREATION_DATE, profileCreationDate);
		profileObj.put(CMConstants.PROFILE_DEACTIVATED_DATE, profileDeactivatedDate);
		profileObj.put(CMConstants.PROFILE_NUMBER_OF_USERS, numberOfUsers);
		profileObj.put(CMConstants.PROFILE_NUMBER_OF_CAMPAIGN, numberOfCampaigns);
		profileObj.put(CMConstants.PROFILE_CONDITION, profileConditions);
	    profileObj.put(CMConstants.ASSOCIATED_CAMPAIGN_DETAILS, associatedCampaignDetails);
	    return profileObj;
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((profileCreationDate == null) ? 0 : profileCreationDate.hashCode());
		result = prime * result + ((profileDeactivatedDate == null) ? 0 : profileDeactivatedDate.hashCode());
		result = prime * result + ((profileDescription == null) ? 0 : profileDescription.hashCode());
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		result = prime * result + ((profileName == null) ? 0 : profileName.hashCode());
		result = prime * result + ((associatedCampaignDetails == null) ? 0 : associatedCampaignDetails.hashCode());
		result = prime * result + ((profileConditions == null) ? 0 : profileConditions.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((profileStatus == null) ? 0 : profileStatus.hashCode());
		return result;
		//return Objects.hash(associatedCampaignDetails, id, numberOfCampaigns, profileConditions, profileCreationDate,
			//	profileDescription, profileId, profileName, profileStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileDTO other = (ProfileDTO) obj;
		return Objects.equals(associatedCampaignDetails, other.associatedCampaignDetails)
				&& Objects.equals(id, other.id) && Objects.equals(numberOfCampaigns, other.numberOfCampaigns)
				&& Objects.equals(profileConditions, other.profileConditions)
				&& Objects.equals(profileCreationDate, other.profileCreationDate)
				&& Objects.equals(profileDescription, other.profileDescription)
				&& Objects.equals(profileId, other.profileId) && Objects.equals(profileName, other.profileName)
				&& Objects.equals(profileStatus, other.profileStatus);
	}
}






