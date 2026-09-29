package com.kony.adminconsole.service.featuresandactions.dto;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionDependencyDTO implements DBPDTO{
	
	private static final long serialVersionUID = 8472269387344613901L;
	
	private String actionName;
	private String dependencyAction;
	private String featureId;
	private String actionStatus;
	private String featureStatus;
	private String companyLegalUnit;	
	private List<String> dependentActions;
	
	public ActionDependencyDTO() {
		dependentActions = new ArrayList<>();
	}

	public ActionDependencyDTO(String actionName, String dependencyAction, String featureId, String actionStatus,
			String featureStatus, List<String> dependentActions) {
		super();
		this.actionName = actionName;
		this.dependencyAction = dependencyAction;
		this.featureId = featureId;
		this.actionStatus = actionStatus;
		this.featureStatus = featureStatus;
		this.dependentActions = dependentActions;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getDependencyAction() {
		return dependencyAction;
	}

	public void setDependencyAction(String dependencyAction) {
		this.dependencyAction = dependencyAction;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getFeatureStatus() {
		return featureStatus;
	}

	public void setFeatureStatus(String featureStatus) {
		this.featureStatus = featureStatus;
	}

	public List<String> getDependentActions() {
		return dependentActions;
	}

	public void setDependentActions(List<String> dependentActions) {
		this.dependentActions = dependentActions;
	}
	
	public void addDepedency(String id) {
		dependentActions.add(id);
	}
	public String getCompanyLegalUnit() {
        return companyLegalUnit; 
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((actionStatus == null) ? 0 : actionStatus.hashCode());
		result = prime * result + ((dependencyAction == null) ? 0 : dependencyAction.hashCode());
		result = prime * result + ((dependentActions == null) ? 0 : dependentActions.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureStatus == null) ? 0 : featureStatus.hashCode());
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
		ActionDependencyDTO other = (ActionDependencyDTO) obj;
		if (actionName == null) {
			if (other.actionName != null)
				return false;
		} else if (!actionName.equals(other.actionName))
			return false;
		return true;
	}
	

}