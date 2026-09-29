package com.kony.adminconsole.service.usermanagement.dto;

public class DependentActionsDTO{
	
	private String dependentactionId;
	private String dependentActionName;
	private String dependentFeatureId;
	private String dependentFeatureName;
	
	public DependentActionsDTO(String dependentactionId, String dependentActionName, String dependentFeatureId,
			String dependentFeatureName) {
		this.dependentactionId = dependentactionId;
		this.dependentActionName = dependentActionName;
		this.dependentFeatureId = dependentFeatureId;
		this.dependentFeatureName = dependentFeatureName;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dependentActionName == null) ? 0 : dependentActionName.hashCode());
		result = prime * result + ((dependentFeatureId == null) ? 0 : dependentFeatureId.hashCode());
		result = prime * result + ((dependentFeatureName == null) ? 0 : dependentFeatureName.hashCode());
		result = prime * result + ((dependentactionId == null) ? 0 : dependentactionId.hashCode());
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
		DependentActionsDTO other = (DependentActionsDTO) obj;
		if (dependentActionName == null) {
			if (other.dependentActionName != null)
				return false;
		} else if (!dependentActionName.equals(other.dependentActionName))
			return false;
		if (dependentFeatureId == null) {
			if (other.dependentFeatureId != null)
				return false;
		} else if (!dependentFeatureId.equals(other.dependentFeatureId))
			return false;
		if (dependentFeatureName == null) {
			if (other.dependentFeatureName != null)
				return false;
		} else if (!dependentFeatureName.equals(other.dependentFeatureName))
			return false;
		if (dependentactionId == null) {
			if (other.dependentactionId != null)
				return false;
		} else if (!dependentactionId.equals(other.dependentactionId))
			return false;
		return true;
	}
	
	
	

}
