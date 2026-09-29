package com.kony.adminconsole.service.usermanagement.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalUserActionsViewDTO implements DBPDTO{
	
	private static final long serialVersionUID = 780945362904325677L;
	
	private String actionId;
	private String featureId;
	private String actionName;
	private String featureName;
	private String featureStatus;
	private String featureDescription;
	private String actionDescription;
	private String isPrimary;
	private String actionStatus;
	private String accesspolicyId;
	private String actionDisplaySequence;
	private String localeId;
	private String displayName;
	private String displayDescription;
	
	private String dependentactionId;
	private String dependentActionName;
	private String dependentFeatureId;
	private String dependentFeatureName;
	
	private Map<String,Map<String,String>> actionDisplayName;
	private Set<DependentActionsDTO> dependentFeatureAndActions;
	
	public InternalUserActionsViewDTO() {
		actionDisplayName = new HashMap<>();
		dependentFeatureAndActions = new HashSet<>();
	}
	
	public InternalUserActionsViewDTO(String actionId, String featureId, String actionName, String featureStatus, String actionDescription,
			String isPrimary, String actionStatus, String accesspolicyId, String actionDisplaySequence, String localeId,
			String displayName, String displayDescription, String dependentactionId, String dependentActionName,
			String dependentFeatureId, String dependentFeatureName, Map<String, String> dependentActions,
			Map<String, Map<String, String>> actionDisplayName,
			Set<DependentActionsDTO> dependentFeatureAndActions) {
		
		super();
		
		this.actionId = actionId;
		this.featureId = featureId;
		this.actionName = actionName;
		this.featureStatus = featureStatus;
		this.actionDescription = actionDescription;
		this.isPrimary = isPrimary;
		this.actionStatus = actionStatus;
		this.accesspolicyId = accesspolicyId;
		this.actionDisplaySequence = actionDisplaySequence;
		this.localeId = localeId;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.dependentactionId = dependentactionId;
		this.dependentActionName = dependentActionName;
		this.dependentFeatureId = dependentFeatureId;
		this.dependentFeatureName = dependentFeatureName;
		this.actionDisplayName = actionDisplayName;
		this.dependentFeatureAndActions = dependentFeatureAndActions;
	}

	/**
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}

	/**
	 * @param featureId the featureId to set
	 */
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the actionDescription
	 */
	public String getActionDescription() {
		return actionDescription;
	}

	/**
	 * @param actionDescription the actionDescription to set
	 */
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	/**
	 * @return the isPrimary
	 */
	public String getIsPrimary() {
		return isPrimary;
	}

	/**
	 * @param isPrimary the isPrimary to set
	 */
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * @return the actionStatus
	 */
	public String getActionStatus() {
		return actionStatus;
	}

	/**
	 * @param actionStatus the actionStatus to set
	 */
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	/**
	 * @return the accesspolicyId
	 */
	public String getAccesspolicyId() {
		return accesspolicyId;
	}

	/**
	 * @param accesspolicyId the accesspolicyId to set
	 */
	public void setAccesspolicyId(String accesspolicyId) {
		this.accesspolicyId = accesspolicyId;
	}

	/**
	 * @return the actionDisplaySequence
	 */
	public String getActionDisplaySequence() {
		return actionDisplaySequence;
	}

	/**
	 * @param actionDisplaySequence the actionDisplaySequence to set
	 */
	public void setActionDisplaySequence(String actionDisplaySequence) {
		this.actionDisplaySequence = actionDisplaySequence;
	}

	/**
	 * @return the localeId
	 */
	public String getLocaleId() {
		return localeId;
	}

	/**
	 * @param localeId the localeId to set
	 */
	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayDescription
	 */
	public String getDisplayDescription() {
		return displayDescription;
	}

	/**
	 * @param displayDescription the displayDescription to set
	 */
	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	/**
	 * @return the dependentactionId
	 */
	public String getDependentactionId() {
		return dependentactionId;
	}

	/**
	 * @param dependentactionId the dependentactionId to set
	 */
	public void setDependentactionId(String dependentactionId) {
		this.dependentactionId = dependentactionId;
	}

	/**
	 * @return the dependentActionName
	 */
	public String getDependentActionName() {
		return dependentActionName;
	}

	/**
	 * @param dependentActionName the dependentActionName to set
	 */
	public void setDependentActionName(String dependentActionName) {
		this.dependentActionName = dependentActionName;
	}

	/**
	 * @return the dependentFeatureId
	 */
	public String getDependentFeatureId() {
		return dependentFeatureId;
	}

	/**
	 * @param dependentFeatureId the dependentFeatureId to set
	 */
	public void setDependentFeatureId(String dependentFeatureId) {
		this.dependentFeatureId = dependentFeatureId;
	}

	/**
	 * @return the dependentFeatureName
	 */
	public String getDependentFeatureName() {
		return dependentFeatureName;
	}

	/**
	 * @param dependentFeatureName the dependentFeatureName to set
	 */
	public void setDependentFeatureName(String dependentFeatureName) {
		this.dependentFeatureName = dependentFeatureName;
	}

	/**
	 * @return the actionDisplayName
	 */
	public Map<String, Map<String, String>> getActionDisplayName() {
		return actionDisplayName;
	}

	/**
	 * @param actionDisplayName the actionDisplayName to set
	 */
	public void setActionDisplayName(Map<String, Map<String, String>> actionDisplayName) {
		this.actionDisplayName = actionDisplayName;
	}

	/**
	 * @return the dependentFeatureAndActions
	 */
	public Set<DependentActionsDTO> getDependentFeatureAndActions() {
		return dependentFeatureAndActions;
	}
	
	public void insertDependentActions(String actionId,String actionName,String featureName, String featureId) {
		
		dependentFeatureAndActions.add(new DependentActionsDTO(actionId,actionName,featureId,featureName));
	}
	
	public void insertActionDisplayName(String languageId,String id, String name) {
		Map<String,String> displayMap = new HashMap<>();
		displayMap.put(id,name);
		actionDisplayName.put(languageId,displayMap);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accesspolicyId == null) ? 0 : accesspolicyId.hashCode());
		result = prime * result + ((actionDescription == null) ? 0 : actionDescription.hashCode());
		result = prime * result + ((actionDisplayName == null) ? 0 : actionDisplayName.hashCode());
		result = prime * result + ((actionDisplaySequence == null) ? 0 : actionDisplaySequence.hashCode());
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((actionStatus == null) ? 0 : actionStatus.hashCode());
		result = prime * result + ((dependentActionName == null) ? 0 : dependentActionName.hashCode());
		result = prime * result + ((dependentFeatureAndActions == null) ? 0 : dependentFeatureAndActions.hashCode());
		result = prime * result + ((dependentFeatureId == null) ? 0 : dependentFeatureId.hashCode());
		result = prime * result + ((dependentFeatureName == null) ? 0 : dependentFeatureName.hashCode());
		result = prime * result + ((dependentactionId == null) ? 0 : dependentactionId.hashCode());
		result = prime * result + ((displayDescription == null) ? 0 : displayDescription.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureStatus == null) ? 0 : featureStatus.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((featureDescription == null) ? 0 : featureDescription.hashCode());
		result = prime * result + ((isPrimary == null) ? 0 : isPrimary.hashCode());
		result = prime * result + ((localeId == null) ? 0 : localeId.hashCode());
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
		InternalUserActionsViewDTO other = (InternalUserActionsViewDTO) obj;
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
