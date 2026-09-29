package com.kony.adminconsole.service.servicedefinition.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDefinitionActionLimitDTO implements DBPDTO{

	private static final long serialVersionUID = 6472269111837618801L;
	
	private String id;
	private String serviceDefinitionId;
	private String actionId;
	private String limitTypeId;
	private String value;
	@JsonAlias({"createdby"})
	private String createdBy;
	private String modifiedBy;
	private String softDeleteFlag;
	private String companyLegalUnit;
	private boolean isNewAction;
	
	public ServiceDefinitionActionLimitDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getSoftDeleteFlag() {
		return softDeleteFlag;
	}

	public void setSoftDeleteFlag(String softDeleteFlag) {
		this.softDeleteFlag = softDeleteFlag;
	}
	
	public boolean getIsNewAction() {
		return isNewAction;
	}

	public void setIsNewAction(boolean isNewAction) {
		this.isNewAction = isNewAction;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit; 
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }
	    
	public ServiceDefinitionActionLimitDTO(String id, String serviceDefinitionId, String actionId, String limitTypeId,
			String value, String createdBy, String modifiedBy, String softDeleteFlag, boolean isNewAction) {
		super();
		this.id = id;
		this.serviceDefinitionId = serviceDefinitionId;
		this.actionId = actionId;
		this.limitTypeId = limitTypeId;
		this.value = value;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.softDeleteFlag = softDeleteFlag;
		this.isNewAction = isNewAction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((serviceDefinitionId == null) ? 0 : serviceDefinitionId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((softDeleteFlag == null) ? 0 : softDeleteFlag.hashCode());
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
		ServiceDefinitionActionLimitDTO other = (ServiceDefinitionActionLimitDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}