package com.kony.adminconsole.service.customerrole.dto;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.commons.utils.CommonUtilities;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupFeatureActionViewDTO implements DBPDTO{

	private static final long serialVersionUID = -2165403819564207227L;
	
	@JsonAlias({"Group_id"})
	private String groupId;
	
	@JsonAlias({"Group_name"})
	private String groupName;
	
	@JsonAlias({"Group_description"})
	private String groupDescription;
	
	@JsonAlias({"Type_id"})
	private String typeId;
	
	@JsonAlias({"Action_id"})
	private String actionId;
	
	@JsonAlias({"LimitType_id"})
	private String limitTypeId;
	private String value;
	
	@JsonAlias({"groupactionlimit_id"})
	private String groupActionLimitId;
	
	@JsonAlias({"Action_name"})
	private String actionName;
	
	@JsonAlias({"Action_description"})
	private String actionDescription;
	
	@JsonAlias({"Action_Type_id"})
	private String actionTypeId;
	
	@JsonAlias({"Action_displaysequence"})
	private String actionDisplaysequence;
	
	@JsonAlias({"Action_dependency"})
	private String actionDependency;
	
	@JsonAlias({"Feature_id"})
	private String featureId;
	private String isMFAApplicable;
	private String isAccountLevel;
	private String isPrimary;
	
	@JsonAlias({"Feature_name"})
	private String featureName;
	
	@JsonAlias({"Feature_description"})
	private String featureDescription;
	
	@JsonAlias({"Feature_Type_id"})
	private String featureTypeId;
	
	@JsonAlias({"Feature_Status_id"})
	private String featureStatusId;
	
	@JsonAlias({"Feature_displaysequence"})
	private String featureDisplaysequence;
	
	@JsonAlias({"Feature_isPrimary"})
	private String featureIsPrimary;
	
	private String actionStatus;
	private String softdelete;
	private Map<String,String> limits;
	private Map<String,String> featureActions;
	private boolean isSelected = false;
	private String accessPolicy;
	private String accessPolicyId;
	private String actionlevel;
	private String actionlevelId;
	private String limitGroup;
	private String limitGroupId;
	private String companyLegalUnit;	

	public String getAccessPolicy() {
		return accessPolicy;
	}
	public void setAccessPolicy(String accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getLimitTypeId() {
		return limitTypeId;
	}
	public void setLimitTypeId(String limitTypeId) {
		this.limitTypeId = limitTypeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getGroupActionLimitId() {
		return groupActionLimitId;
	}
	public void setGroupActionLimitId(String groupActionLimitId) {
		this.groupActionLimitId = groupActionLimitId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionDescription() {
		return actionDescription;
	}
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}
	public String getActionTypeId() {
		return actionTypeId;
	}
	public void setActionTypeId(String actionTypeId) {
		this.actionTypeId = actionTypeId;
	}
	public String getActionDisplaysequence() {
		return actionDisplaysequence;
	}
	public void setActionDisplaysequence(String actionDisplaysequence) {
		this.actionDisplaysequence = actionDisplaysequence;
	}
	public String getActionDependency() {
		return actionDependency;
	}
	public void setActionDependency(String actionDependency) {
		this.actionDependency = actionDependency;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getIsMFAApplicable() {
		return isMFAApplicable;
	}
	public void setIsMFAApplicable(String isMFAApplicable) {
		this.isMFAApplicable = isMFAApplicable;
	}
	public String getIsAccountLevel() {
		return isAccountLevel;
	}
	public void setIsAccountLevel(String isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}
	public String getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getFeatureDescription() {
		return featureDescription;
	}
	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}
	public String getFeatureTypeId() {
		return featureTypeId;
	}
	public void setFeatureTypeId(String featureTypeId) {
		this.featureTypeId = featureTypeId;
	}
	public String getFeatureStatusId() {
		return featureStatusId;
	}
	public void setFeatureStatusId(String featureStatusId) {
		this.featureStatusId = featureStatusId;
	}
	public String getFeatureDisplaysequence() {
		return featureDisplaysequence;
	}
	public void setFeatureDisplaysequence(String featureDisplaysequence) {
		this.featureDisplaysequence = featureDisplaysequence;
	}
	public String getFeatureIsPrimary() {
		return featureIsPrimary;
	}
	public void setFeatureIsPrimary(String featureIsPrimary) {
		this.featureIsPrimary = featureIsPrimary;
	}
	public String getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}
	public String getSoftdelete() {
		return softdelete;
	}
	public void setSoftdelete(String softdelete) {
		this.softdelete = softdelete;
	}
	public Map<String, String> getLimits() {
		return limits;
	}
	public void setLimits(Map<String, String> limits) {
		this.limits = limits;
	}
	public Map<String, String> getFeatureActions() {
		return featureActions;
	}
	public void setFeatureActions(Map<String, String> featureActions) {
		this.featureActions = featureActions;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getAccessPolicyId() {
		return accessPolicyId;
	}
	public void setAccessPolicyId(String accessPolicyId) {
		this.accessPolicyId = accessPolicyId;
	}
	public String getActionlevel() {
		return actionlevel;
	}
	public void setActionlevel(String actionlevel) {
		this.actionlevel = actionlevel;
	}
	public String getActionlevelId() {
		return actionlevelId;
	}
	public void setActionlevelId(String actionlevelId) {
		this.actionlevelId = actionlevelId;
	}
	public String getLimitGroup() {
		return limitGroup;
	}
	public void setLimitGroup(String limitGroup) {
		this.limitGroup = limitGroup;
	}
	public String getLimitGroupId() {
		return limitGroupId;
	}
	public void setLimitGroupId(String limitGroupId) {
		this.limitGroupId = limitGroupId;
	}
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}
	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	
	
	public  GroupFeatureActionViewDTO() {
		limits = new HashMap<>();
		featureActions = new HashMap<>();
	}
		
	public void insertLimit(String limitId, String value) {
		if(limits.containsKey(limitId)) {
			limits.put(limitId, CommonUtilities.doubleToStringWithoutScientificNotation(
					Math.min(Double.parseDouble(limits.get(limitId)), Double.parseDouble(value))));
		}else {
			limits.put(limitId, CommonUtilities.doubleToStringWithoutScientificNotation(Double.parseDouble(value)));
		}
	}
	
	public GroupFeatureActionViewDTO(String groupId, String groupName, String groupDescription, String typeId,
			String actionId, String limitTypeId, String value, String groupActionLimitId, String actionName,
			String actionDescription, String actionTypeId, String actionDisplaysequence, String actionDependency,
			String featureId, String isMFAApplicable, String isAccountLevel, String isPrimary, String featureName,
			String featureDescription, String featureTypeId, String featureStatusId, String featureDisplaysequence,
			String featureIsPrimary, String actionStatus, String softdelete, Map<String, String> limits,
			Map<String, String> featureActions, boolean isSelected, String accessPolicy, String accessPolicyId,
			String actionlevel, String actionlevelId, String limitGroup, String limitGroupId, String companyLegalUnit) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.typeId = typeId;
		this.actionId = actionId;
		this.limitTypeId = limitTypeId;
		this.value = value;
		this.groupActionLimitId = groupActionLimitId;
		this.actionName = actionName;
		this.actionDescription = actionDescription;
		this.actionTypeId = actionTypeId;
		this.actionDisplaysequence = actionDisplaysequence;
		this.actionDependency = actionDependency;
		this.featureId = featureId;
		this.isMFAApplicable = isMFAApplicable;
		this.isAccountLevel = isAccountLevel;
		this.isPrimary = isPrimary;
		this.featureName = featureName;
		this.featureDescription = featureDescription;
		this.featureTypeId = featureTypeId;
		this.featureStatusId = featureStatusId;
		this.featureDisplaysequence = featureDisplaysequence;
		this.featureIsPrimary = featureIsPrimary;
		this.actionStatus = actionStatus;
		this.softdelete = softdelete;
		this.limits = limits;
		this.featureActions = featureActions;
		this.isSelected = isSelected;
		this.accessPolicy = accessPolicy;
		this.accessPolicyId = accessPolicyId;
		this.actionlevel = actionlevel;
		this.actionlevelId = actionlevelId;
		this.limitGroup = limitGroup;
		this.limitGroupId = limitGroupId;
		this.companyLegalUnit = companyLegalUnit;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPolicy == null) ? 0 : accessPolicy.hashCode());
		result = prime * result + ((accessPolicyId == null) ? 0 : accessPolicyId.hashCode());
		result = prime * result + ((actionDependency == null) ? 0 : actionDependency.hashCode());
		result = prime * result + ((actionDescription == null) ? 0 : actionDescription.hashCode());
		result = prime * result + ((actionDisplaysequence == null) ? 0 : actionDisplaysequence.hashCode());
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((actionStatus == null) ? 0 : actionStatus.hashCode());
		result = prime * result + ((actionTypeId == null) ? 0 : actionTypeId.hashCode());
		result = prime * result + ((actionlevel == null) ? 0 : actionlevel.hashCode());
		result = prime * result + ((actionlevelId == null) ? 0 : actionlevelId.hashCode());
		result = prime * result + ((featureActions == null) ? 0 : featureActions.hashCode());
		result = prime * result + ((featureDescription == null) ? 0 : featureDescription.hashCode());
		result = prime * result + ((featureDisplaysequence == null) ? 0 : featureDisplaysequence.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureIsPrimary == null) ? 0 : featureIsPrimary.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((featureStatusId == null) ? 0 : featureStatusId.hashCode());
		result = prime * result + ((featureTypeId == null) ? 0 : featureTypeId.hashCode());
		result = prime * result + ((groupActionLimitId == null) ? 0 : groupActionLimitId.hashCode());
		result = prime * result + ((groupDescription == null) ? 0 : groupDescription.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
		result = prime * result + ((isMFAApplicable == null) ? 0 : isMFAApplicable.hashCode());
		result = prime * result + ((isPrimary == null) ? 0 : isPrimary.hashCode());
		result = prime * result + (isSelected ? 1231 : 1237);
		result = prime * result + ((limitGroup == null) ? 0 : limitGroup.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((limits == null) ? 0 : limits.hashCode());
		result = prime * result + ((softdelete == null) ? 0 : softdelete.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((companyLegalUnit == null) ? 0 : companyLegalUnit.hashCode());
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
		GroupFeatureActionViewDTO other = (GroupFeatureActionViewDTO) obj;
		if (groupActionLimitId == null) {
			if (other.groupActionLimitId != null)
				return false;
		} else if (!groupActionLimitId.equals(other.groupActionLimitId))
			return false;
		return true;
	}
	
	

}