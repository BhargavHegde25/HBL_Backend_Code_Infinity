package com.temenos.dbx.product.commons.dto;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureActionDTO implements DBPDTO{

	private static final long serialVersionUID = -990237819867392247L;

	@JsonAlias({"id","featureActionId"})
	private String featureActionId;
	
	@JsonAlias({"Feature_id","featureId"})
	private String featureId;
	
	@JsonAlias({"name","featureActionName"})
	private String featureActionName;
	
	@JsonAlias({"limitGroupId","limitgroupId"})
	private String limitGroupId;
	
	private String limitGroupName;
	
	private String approveFeatureAction;
	
	private String featureName;
	
	@JsonAlias({"Type_id","typeId"})
	private String typeId;
	private String isAccountLevel;
	private String actionlevelId;
	
	public FeatureActionDTO() {
		super();
	}

	public FeatureActionDTO(String featureActionId, String featureId, String featureActionName, String limitGroupId, 
			String limitGroupName, String approveFeatureAction, String featureName, String typeId) {
		super();
		this.featureActionId = featureActionId;
		this.featureId = featureId;
		this.featureActionName = featureActionName;
		this.limitGroupId = limitGroupId;
		this.limitGroupName = limitGroupName;
		this.approveFeatureAction = approveFeatureAction;
		this.featureName = featureName;
		this.typeId = typeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((featureActionName == null) ? 0 : featureActionName.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((limitGroupName == null) ? 0 : limitGroupName.hashCode());
		result = prime * result + ((approveFeatureAction == null) ? 0 : approveFeatureAction.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((actionlevelId == null) ? 0 : actionlevelId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureActionDTO other = (FeatureActionDTO) obj;
		if (featureActionId == null) {
			if (other.featureActionId != null)
				return false;
		} else if (!featureActionId.equals(other.featureActionId))
			return false;
		if (featureActionName == null) {
			if (other.featureActionName != null)
				return false;
		} else if (!featureActionName.equals(other.featureActionName))
			return false;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		if (limitGroupId == null) {
			if (other.limitGroupId != null)
				return false;
		} else if (!limitGroupId.equals(other.limitGroupId))
			return false;
		if (limitGroupName == null) {
			if (other.limitGroupName != null)
				return false;
		} else if (!limitGroupName.equals(other.limitGroupName))
			return false;
		if (featureName == null) {
			if (other.featureName != null)
				return false;
		} else if (!featureName.equals(other.featureName))
			return false;
		if (typeId == null) {
			if (other.typeId != null)
				return false;
		} else if (!typeId.equals(other.typeId))
			return false;
		return true;
	}

	public String getFeatureActionId() {
		return featureActionId;
	}

	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getFeatureActionName() {
		return featureActionName;
	}

	public void setFeatureActionName(String featureActionName) {
		this.featureActionName = featureActionName;
	}

	public String getLimitGroupId() {
		return limitGroupId;
	}

	public void setLimitGroupId(String limitGroupId) {
		if(StringUtils.isEmpty(limitGroupId))
			this.limitGroupId = "OTHER_REQUESTS";
		else
			this.limitGroupId = limitGroupId;
	}
	
	public String getLimitGroupName() {
		return limitGroupName;
	}

	public void setLimitGroupName(String limitGroupName) {
		
		if(StringUtils.isEmpty(limitGroupName))
			this.limitGroupName = "Other Requests";
		else
			this.limitGroupName = limitGroupName;
	}

	public String getApproveFeatureAction() {
		return approveFeatureAction;
	}

	public void setApproveFeatureAction(String approveFeatureAction) {
		this.approveFeatureAction = approveFeatureAction;
	}
	
	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getIsAccountLevel() {
		return isAccountLevel;
	}

	public void setIsAccountLevel(String accountLevel) {
		isAccountLevel = accountLevel;
	}

	public String getActionlevelId() {
		return actionlevelId;
	}

	public void setActionlevelId(String actionlevelId) {
		this.actionlevelId = actionlevelId;
	}
}