package com.kony.adminconsole.service.featuresandactions.dto;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.commons.utils.CommonUtilities;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureActionsViewDTO implements DBPDTO {
	
	private static final long serialVersionUID = 8472930145134613901L;
	
	private String actionId;
	private String featureId;
	private String actionName;
	private String actionDescription;
	private String isAccountLevel;
	private String isMFAApplicable;
	private String isPrimary;
	private String notes;
	private String typeId;
	private String typeName;
	private String actionDisplaySequence;
	private String actionDependency;
	private String actionType;
	private String actionStatus;
	private String roleTypeName;
	private String accessPolicy;
	private String limitGroup;
	private String actionlevel;
	private String accessPolicyId;
	private String actionlevelId;
	private String limitGroupId;
	private String localeId;
	private String displayName;
	private String displayDescription;
	private String limitTypeId;
	private String value;
	private String dependentactionId;
	private String dependentFeatureId;
	private String dependentFeatureName;
	private String dependentActionName;
	private String featureGroup;
	private String featureName;
	private String featureDescription;
	private String featureStatus;
	private String featureType;
	private String featureDisplaySequence;
	private String isFeaturePrimary;
	private String termsAndConditionCode;
	private String termsAndConditionTitle;
	private String termsAndConditionDescription;
	private String companyLegalUnit;
	private Map<String,String> dependentActions;
	private Map<String,String> roleTypes;
	private Map<String,Map<String,String>> actionDisplayName;
	private Map<String,Map<String,String>> dependentFeatureAndActions;
	private Map<String,String> termsAndConditions;
	private Map<String,String> actionLimits;
	private String accesspolicyId;
	private String accesspolicy;
	private String limitgroupId;
    private String limitgroup;

	public FeatureActionsViewDTO() {
		roleTypes = new HashMap<>();
		actionDisplayName = new HashMap<>();
		termsAndConditions = new HashMap<>();
		actionLimits = new HashMap<>();
		dependentActions = new HashMap<>();
		dependentFeatureAndActions = new HashMap<>();
	}

	public FeatureActionsViewDTO(String actionId, String featureId, String actionName, String actionDescription,
			String isAccountLevel, String isMFAApplicable, String isPrimary, String notes, String typeId,
			String typeName, String actionDisplaySequence, String actionDependency, String actionType,
			String actionStatus, String roleTypeName, String accessPolicy, String limitGroup, String actionlevel,
			String accessPolicyId, String actionlevelId, String limitGroupId, String localeId, String displayName,
			String displayDescription, String limitTypeId, String value, String dependentactionId,
			String dependentFeatureId, String dependentFeatureName, String dependentActionName, String featureGroup,
			String featureName, String featureDescription, String featureStatus, String featureType,
			String featureDisplaySequence, String isFeaturePrimary, String termsAndConditionCode,
			String termsAndConditionTitle, String termsAndConditionDescription, Map<String, String> dependentActions,
			Map<String, String> roleTypes, Map<String, Map<String, String>> actionDisplayName,
			Map<String, Map<String, String>> dependentFeatureAndActions, Map<String, String> termsAndConditions,
			Map<String, String> actionLimits, String accesspolicyId, String accesspolicy, String limitgroupId,String limitgroup) {
		super();
		this.actionId = actionId;
		this.featureId = featureId;
		this.actionName = actionName;
		this.actionDescription = actionDescription;
		this.isAccountLevel = isAccountLevel;
		this.isMFAApplicable = isMFAApplicable;
		this.isPrimary = isPrimary;
		this.notes = notes;
		this.typeId = typeId;
		this.typeName = typeName;
		this.actionDisplaySequence = actionDisplaySequence;
		this.actionDependency = actionDependency;
		this.actionType = actionType;
		this.actionStatus = actionStatus;
		this.roleTypeName = roleTypeName;
		this.accessPolicy = accessPolicy;
		this.limitGroup = limitGroup;
		this.actionlevel = actionlevel;
		this.accessPolicyId = accessPolicyId;
		this.actionlevelId = actionlevelId;
		this.limitGroupId = limitGroupId;
		this.localeId = localeId;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.limitTypeId = limitTypeId;
		this.value = value;
		this.dependentactionId = dependentactionId;
		this.dependentFeatureId = dependentFeatureId;
		this.dependentFeatureName = dependentFeatureName;
		this.dependentActionName = dependentActionName;
		this.featureGroup = featureGroup;
		this.featureName = featureName;
		this.featureDescription = featureDescription;
		this.featureStatus = featureStatus;
		this.featureType = featureType;
		this.featureDisplaySequence = featureDisplaySequence;
		this.isFeaturePrimary = isFeaturePrimary;
		this.termsAndConditionCode = termsAndConditionCode;
		this.termsAndConditionTitle = termsAndConditionTitle;
		this.termsAndConditionDescription = termsAndConditionDescription;
		this.dependentActions = dependentActions;
		this.roleTypes = roleTypes;
		this.actionDisplayName = actionDisplayName;
		this.dependentFeatureAndActions = dependentFeatureAndActions;
		this.termsAndConditions = termsAndConditions;
		this.actionLimits = actionLimits;
		this.accesspolicyId = accesspolicyId;
		this.accesspolicy = accesspolicy;
		this.limitgroupId = limitgroupId;
		this.limitgroup = limitgroup;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
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

	public String getIsMFAApplicable() {
		return isMFAApplicable;
	}

	public void setIsMFAApplicable(String isMFAApplicable) {
		this.isMFAApplicable = isMFAApplicable;
	}

	public String getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getActionDisplaySequence() {
		return actionDisplaySequence;
	}

	public void setActionDisplaySequence(String actionDisplaySequence) {
		this.actionDisplaySequence = actionDisplaySequence;
	}

	public String getActionDependency() {
		return actionDependency;
	}

	public void setActionDependency(String actionDependency) {
		this.actionDependency = actionDependency;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}

	public String getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(String accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public String getLimitGroup() {
		return limitGroup;
	}

	public void setLimitGroup(String limitGroup) {
		this.limitGroup = limitGroup;
	}

	public String getActionlevel() {
		return actionlevel;
	}

	public void setActionlevel(String actionlevel) {
		this.actionlevel = actionlevel;
	}

	public String getLocaleId() {
		return localeId;
	}

	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
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

	public String getFeatureGroup() {
		return featureGroup;
	}

	public void setFeatureGroup(String featureGroup) {
		this.featureGroup = featureGroup;
	}

	public String getTermsAndConditionCode() {
		return termsAndConditionCode;
	}

	public void setTermsAndConditionCode(String termsAndConditionCode) {
		this.termsAndConditionCode = termsAndConditionCode;
	}

	public String getTermsAndConditionTitle() {
		return termsAndConditionTitle;
	}

	public void setTermsAndConditionTitle(String termsAndConditionTitle) {
		this.termsAndConditionTitle = termsAndConditionTitle;
	}

	public String getTermsAndConditionDescription() {
		return termsAndConditionDescription;
	}

	public void setTermsAndConditionDescription(String termsAndConditionDescription) {
		this.termsAndConditionDescription = termsAndConditionDescription;
	}

	public String getIsAccountLevel() {
		return isAccountLevel;
	}

	public void setIsAccountLevel(String isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}

	public String getAccessPolicyId() {
		return accessPolicyId;
	}

	public void setAccessPolicyId(String accessPolicyId) {
		this.accessPolicyId = accessPolicyId;
	}

	public String getActionlevelId() {
		return actionlevelId;
	}

	public void setActionlevelId(String actionlevelId) {
		this.actionlevelId = actionlevelId;
	}

	public String getLimitGroupId() {
		return limitGroupId;
	}

	public void setLimitGroupId(String limitGroupId) {
		this.limitGroupId = limitGroupId;
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

	public String getFeatureStatus() {
		return featureStatus;
	}

	public void setFeatureStatus(String featureStatus) {
		this.featureStatus = featureStatus;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureDisplaySequence() {
		return featureDisplaySequence;
	}

	public void setFeatureDisplaySequence(String featureDisplaySequence) {
		this.featureDisplaySequence = featureDisplaySequence;
	}

	public String getIsFeaturePrimary() {
		return isFeaturePrimary;
	}

	public void setIsFeaturePrimary(String isFeaturePrimary) {
		this.isFeaturePrimary = isFeaturePrimary;
	}

	public Map<String, String> getRoleTypes() {
		return roleTypes;
	}

	public void setRoleTypes(Map<String, String> roleTypes) {
		this.roleTypes = roleTypes;
	}

	public Map<String, Map<String, String>> getActionDisplayName() {
		return actionDisplayName;
	}

	public void setActionDisplayName(Map<String, Map<String, String>> actionDisplayName) {
		this.actionDisplayName = actionDisplayName;
	}

	public Map<String, String> getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(Map<String, String> termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
	
	public String getDependentactionId() {
		return dependentactionId;
	}

	public void setDependentactionId(String dependentactionId) {
		this.dependentactionId = dependentactionId;
	}

	public String getDependentFeatureName() {
		return dependentFeatureName;
	}

	public void setDependentFeatureName(String dependentfeatureName) {
		this.dependentFeatureName = dependentfeatureName;
	}

	public String getDependentFeatureId() {
		return dependentFeatureId;
	}

	public void setDependentFeatureId(String dependentFeatureId) {
		this.dependentFeatureId = dependentFeatureId;
	}

	public String getDependentActionName() {
		return dependentActionName;
	}

	public void setDependentActionName(String dependentActionName) {
		this.dependentActionName = dependentActionName;
	}

	public Map<String, String> getDependentActions() {
		return dependentActions;
	}

	public void setDependentActions(Map<String, String> dependentActions) {
		this.dependentActions = dependentActions;
	}

	public Map<String, Map<String, String>> getDependentFeatureAndActions() {
		return dependentFeatureAndActions;
	}

	public void setDependentFeatureAndActions(Map<String, Map<String, String>> dependentFeatureAndActions) {
		this.dependentFeatureAndActions = dependentFeatureAndActions;
	}

	public void insertRoleType(String id,String name) {
		roleTypes.put(id,name);
	}
	public void insertActionDisplayName(String languageId,String id, String name) {
		Map<String,String> displayMap = new HashMap<>();
		displayMap.put(id,name);
		actionDisplayName.put(languageId,displayMap);
	}

	public void insertTermsAndConditions(String title,String description) {
		termsAndConditions.put(title,description);
	}
	
	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}
	
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	
	public Map<String, String> getActionLimits() {
		return actionLimits;
	}

	public void setActionLimits(Map<String, String> actionLimits) {
		this.actionLimits = actionLimits;
	}
	public String getAccesspolicyId() {
		return accesspolicyId;
	}

	public void setAccesspolicyId(String accesspolicyId) {
		this.accesspolicyId = accesspolicyId;
	}
	public String getAccesspolicy() {
		return accesspolicy;
	}

	public void setAccesspolicy(String accesspolicy) {
		this.accesspolicy = accesspolicy;
	}
	public String getLimitgroupId() {
		return limitgroupId;
	}

	public void setLimitgroupId(String limitgroupId) {
		this.limitgroupId = limitgroupId;
	}
	public String getLimitgroup() {
		return limitgroup;
	}

	public void setLimitgroup(String limitgroup) {
		this.limitgroup = limitgroup;
	}
	
	public void insertDependentActions(String actionId,String actionName,String featureName) {
		if(dependentFeatureAndActions.containsKey(featureName)) {
			Map<String,String> display = dependentFeatureAndActions.get(featureName);
			display.put(actionId,actionName);
		}else {
			Map<String,String> displayMap = new HashMap<>();
			displayMap.put(actionId,actionName);
		dependentFeatureAndActions.put(featureName,displayMap);
		}
		
	}
	
	public void insertLimit(String limitId, String value) {
		if(actionLimits.containsKey(limitId)) {
			actionLimits.put(limitId, CommonUtilities.doubleToStringWithoutScientificNotation(
					Math.min(Double.parseDouble(actionLimits.get(limitId)), Double.parseDouble(value))));	
		}else {
			actionLimits.put(limitId, CommonUtilities.doubleToStringWithoutScientificNotation(Double.parseDouble(value)));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPolicy == null) ? 0 : accessPolicy.hashCode());
		result = prime * result + ((accessPolicyId == null) ? 0 : accessPolicyId.hashCode());
		result = prime * result + ((actionDependency == null) ? 0 : actionDependency.hashCode());
		result = prime * result + ((actionDescription == null) ? 0 : actionDescription.hashCode());
		result = prime * result + ((actionDisplayName == null) ? 0 : actionDisplayName.hashCode());
		result = prime * result + ((actionDisplaySequence == null) ? 0 : actionDisplaySequence.hashCode());
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((actionLimits == null) ? 0 : actionLimits.hashCode());
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((actionStatus == null) ? 0 : actionStatus.hashCode());
		result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((actionlevel == null) ? 0 : actionlevel.hashCode());
		result = prime * result + ((actionlevelId == null) ? 0 : actionlevelId.hashCode());
		result = prime * result + ((dependentActionName == null) ? 0 : dependentActionName.hashCode());
		result = prime * result + ((dependentActions == null) ? 0 : dependentActions.hashCode());
		result = prime * result + ((dependentactionId == null) ? 0 : dependentactionId.hashCode());
		result = prime * result + ((dependentFeatureId == null) ? 0 : dependentFeatureId.hashCode());
		result = prime * result + ((dependentFeatureName == null) ? 0 : dependentFeatureName.hashCode());
		result = prime * result + ((displayDescription == null) ? 0 : displayDescription.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((featureDescription == null) ? 0 : featureDescription.hashCode());
		result = prime * result + ((featureDisplaySequence == null) ? 0 : featureDisplaySequence.hashCode());
		result = prime * result + ((featureGroup == null) ? 0 : featureGroup.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((featureStatus == null) ? 0 : featureStatus.hashCode());
		result = prime * result + ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
		result = prime * result + ((isFeaturePrimary == null) ? 0 : isFeaturePrimary.hashCode());
		result = prime * result + ((isMFAApplicable == null) ? 0 : isMFAApplicable.hashCode());
		result = prime * result + ((isPrimary == null) ? 0 : isPrimary.hashCode());
		result = prime * result + ((limitGroup == null) ? 0 : limitGroup.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((localeId == null) ? 0 : localeId.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((roleTypeName == null) ? 0 : roleTypeName.hashCode());
		result = prime * result + ((roleTypes == null) ? 0 : roleTypes.hashCode());
		result = prime * result + ((termsAndConditionCode == null) ? 0 : termsAndConditionCode.hashCode());
		result = prime * result
				+ ((termsAndConditionDescription == null) ? 0 : termsAndConditionDescription.hashCode());
		result = prime * result + ((termsAndConditionTitle == null) ? 0 : termsAndConditionTitle.hashCode());
		result = prime * result + ((termsAndConditions == null) ? 0 : termsAndConditions.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		FeatureActionsViewDTO other = (FeatureActionsViewDTO) obj;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		return true;
	}

}