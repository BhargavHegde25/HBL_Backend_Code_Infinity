package com.kony.adminconsole.service.servicedefinition.dto;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.CommonUtilities;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDefinitionFeatureActionViewDTO implements DBPDTO{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final long serialVersionUID = 6472269131882613801L;
	
	private String serviceDefinitionId;
	private String serviceDefinitionName;
	private String serviceDefinitionDescription;
	private String serviceType;

	private String actionId;
	private String limitTypeId;
	private String value;
	private String serviceDefinitionActionLimitId;
	private String actionName;
	private String actionDescription;
	private String actionTypeId;
	private String actionDisplaysequence;
	private String actionDependency;
	private String featureId;
	private String isMFAApplicable;
	private String isAccountLevel;
	private String isPrimary;
	private String accessPolicy;
	private String accessPolicyId;
	private String actionlevel;
	private String actionlevelId;
	private String limitGroup;
	private String limitGroupId;
	private String dependentactionId;
	private String dependentFeatureId;
	private String dependentFeatureName;
	private String dependentActionName;
	private String featureName;
	private String featureDescription;
	private String featureTypeId;
	private String featureStatusId;
	private String featureDisplaysequence;
	private String featureIsPrimary;
	private String actionStatus;
	private String softdelete;
	private String companyLegalUnit;
	private Map<String,String> limits;
	private Map<String,String> featureActions;
	private Map<String,String> dependentActions;
	private Map<String,Map<String,String>> dependentFeatureAndActions;
	private boolean isSelected = false;
	
	public ServiceDefinitionFeatureActionViewDTO(String serviceDefinitionId, String serviceDefinitionName,
			String serviceDefinitionDescription, String serviceType, String actionId, String limitTypeId, String value,
			String serviceDefinitionActionLimitId, String actionName, String actionDescription, String actionTypeId,
			String actionDisplaysequence, String actionDependency, String featureId, String isMFAApplicable,
			String isAccountLevel, String isPrimary, String accessPolicy, String accessPolicyId, String actionlevel,
			String actionlevelId, String limitGroup, String limitGroupId, String dependentactionId,
			String dependentFeatureId, String dependentFeatureName, String dependentActionName, String featureName,
			String featureDescription, String featureTypeId, String featureStatusId, String featureDisplaysequence,
			String featureIsPrimary, String actionStatus, String softdelete, Map<String, String> limits,
			Map<String, String> featureActions, Map<String, String> dependentActions,
			Map<String, Map<String, String>> dependentFeatureAndActions, boolean isSelected) {
		super();
		this.serviceDefinitionId = serviceDefinitionId;
		this.serviceDefinitionName = serviceDefinitionName;
		this.serviceDefinitionDescription = serviceDefinitionDescription;
		this.serviceType = serviceType;
		this.actionId = actionId;
		this.limitTypeId = limitTypeId;
		this.value = value;
		this.serviceDefinitionActionLimitId = serviceDefinitionActionLimitId;
		this.actionName = actionName;
		this.actionDescription = actionDescription;
		this.actionTypeId = actionTypeId;
		this.actionDisplaysequence = actionDisplaysequence;
		this.actionDependency = actionDependency;
		this.featureId = featureId;
		this.isMFAApplicable = isMFAApplicable;
		this.isAccountLevel = isAccountLevel;
		this.isPrimary = isPrimary;
		this.accessPolicy = accessPolicy;
		this.accessPolicyId = accessPolicyId;
		this.actionlevel = actionlevel;
		this.actionlevelId = actionlevelId;
		this.limitGroup = limitGroup;
		this.limitGroupId = limitGroupId;
		this.dependentactionId = dependentactionId;
		this.dependentFeatureId = dependentFeatureId;
		this.dependentFeatureName = dependentFeatureName;
		this.dependentActionName = dependentActionName;
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
		this.dependentActions = dependentActions;
		this.dependentFeatureAndActions = dependentFeatureAndActions;
		this.isSelected = isSelected;
	}
	
	public ServiceDefinitionFeatureActionViewDTO() {
		limits = new HashMap<>();
		featureActions = new HashMap<>();
		dependentActions = new HashMap<>();
		dependentFeatureAndActions = new HashMap<>();
	}
	
	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}
	public void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
	}
	public String getServiceDefinitionName() {
		return serviceDefinitionName;
	}
	public void setServiceDefinitionName(String serviceDefinitionName) {
		this.serviceDefinitionName = serviceDefinitionName;
	}
	public String getServiceDefinitionDescription() {
		return serviceDefinitionDescription;
	}
	public void setServiceDefinitionDescription(String serviceDefinitionDescription) {
		this.serviceDefinitionDescription = serviceDefinitionDescription;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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
	public String getServiceDefinitionActionLimitId() {
		return serviceDefinitionActionLimitId;
	}
	public void setServiceDefinitionActionLimitId(String serviceDefinitionActionLimitId) {
		this.serviceDefinitionActionLimitId = serviceDefinitionActionLimitId;
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

	public String getAccessPolicy() {
		return accessPolicy;
	}
	
	public void setAccessPolicy(String accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	public String getActionStatus() {
		return actionStatus;
	}
	
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}
		
	public void insertLimit(String limitId, String value) {
		if(limits.containsKey(limitId)) {
			limits.put(limitId, CommonUtilities.doubleToStringWithoutScientificNotation(Math.min(Double.parseDouble(limits.get(limitId)), Double.parseDouble(value))));	
		}else {
			limits.put(limitId, CommonUtilities.doubleToStringWithoutScientificNotation(Double.parseDouble(value)));
		}
	}
	
	public String getSoftdelete() {
		return softdelete;
	}
	
	public void setSoftdelete(String softdelete) {
		this.softdelete = softdelete;
	}

	public Map<String,Map<String,String>> getDependentFeatureAndActions() {
		return dependentFeatureAndActions;
	}

	public void setDependentFeatureAndActions(Map<String,Map<String,String>> dependentFeatureAndActions) {
		this.dependentFeatureAndActions = dependentFeatureAndActions;
	}

	public Map<String,String> getDependentActions() {
		return dependentActions;
	}

	public void setDependentActions(Map<String,String> dependentActions) {
		this.dependentActions = dependentActions;
	}

	public String getDependentActionName() {
		return dependentActionName;
	}

	public void setDependentActionName(String dependentActionName) {
		this.dependentActionName = dependentActionName;
	}

	public String getDependentFeatureName() {
		return dependentFeatureName;
	}

	public void setDependentFeatureName(String dependentFeatureName) {
		this.dependentFeatureName = dependentFeatureName;
	}

	public String getDependentactionId() {
		return dependentactionId;
	}

	public void setDependentactionId(String dependentactionId) {
		this.dependentactionId = dependentactionId;
	}

	public String getDependentFeatureId() {
		return dependentFeatureId;
	}

	public void setDependentFeatureId(String dependentFeatureId) {
		this.dependentFeatureId = dependentFeatureId;
	}
	public String getCompanyLegalUnit() {
        return companyLegalUnit; 
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
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
		result = prime * result + ((dependentActionName == null) ? 0 : dependentActionName.hashCode());
		result = prime * result + ((dependentFeatureId == null) ? 0 : dependentFeatureId.hashCode());
		result = prime * result + ((dependentFeatureName == null) ? 0 : dependentFeatureName.hashCode());
		result = prime * result + ((dependentactionId == null) ? 0 : dependentactionId.hashCode());
		result = prime * result + ((featureActions == null) ? 0 : featureActions.hashCode());
		result = prime * result + ((featureDescription == null) ? 0 : featureDescription.hashCode());
		result = prime * result + ((featureDisplaysequence == null) ? 0 : featureDisplaysequence.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureIsPrimary == null) ? 0 : featureIsPrimary.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((featureStatusId == null) ? 0 : featureStatusId.hashCode());
		result = prime * result + ((featureTypeId == null) ? 0 : featureTypeId.hashCode());
		result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
		result = prime * result + ((isMFAApplicable == null) ? 0 : isMFAApplicable.hashCode());
		result = prime * result + ((isPrimary == null) ? 0 : isPrimary.hashCode());
		result = prime * result + (isSelected ? 1231 : 1237);
		result = prime * result + ((limitGroup == null) ? 0 : limitGroup.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((limits == null) ? 0 : limits.hashCode());
		result = prime * result + ((serviceDefinitionActionLimitId == null) ? 0 : serviceDefinitionActionLimitId.hashCode());
		result = prime * result + ((serviceDefinitionDescription == null) ? 0 : serviceDefinitionDescription.hashCode());
		result = prime * result + ((serviceDefinitionId == null) ? 0 : serviceDefinitionId.hashCode());
		result = prime * result + ((serviceDefinitionName == null) ? 0 : serviceDefinitionName.hashCode());
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((softdelete == null) ? 0 : softdelete.hashCode());
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
		ServiceDefinitionFeatureActionViewDTO other = (ServiceDefinitionFeatureActionViewDTO) obj;
		if (serviceDefinitionActionLimitId == null) {
			if (other.serviceDefinitionActionLimitId != null)
				return false;
		} else if (!serviceDefinitionActionLimitId.equals(other.serviceDefinitionActionLimitId))
			return false;
		return true;
	}
	

	
}