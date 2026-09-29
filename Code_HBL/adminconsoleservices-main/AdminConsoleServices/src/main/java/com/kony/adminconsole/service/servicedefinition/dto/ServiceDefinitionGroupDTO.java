package com.kony.adminconsole.service.servicedefinition.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDefinitionGroupDTO implements DBPDTO{
	
	private static final long serialVersionUID = 6472269167837613801L;
	
	@JsonAlias({"Group_id"})
	private String groupId;
	private String serviceDefinitionId;
	private String isDefaultGroup;
	@JsonAlias({"createdby"})
	private String createdBy;
	private String modifiedBy;
	private String createdts;
	private String modifiedts;
	private String synctimestamp;
	private String softDeleteFlag;
	private String companyLegalUnit;
	public ServiceDefinitionGroupDTO() {
		super();
	}

	public ServiceDefinitionGroupDTO(String groupId, String serviceDefinitionId, String isDefaultGroup, String createdBy,
			String modifiedBy, String createdts, String modifiedts, String synctimestamp, String softDeleteFlag) {
		super();
		this.groupId = groupId;
		this.serviceDefinitionId = serviceDefinitionId;
		this.isDefaultGroup = isDefaultGroup;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdts = createdts;
		this.modifiedts = modifiedts;
		this.synctimestamp = synctimestamp;
		this.softDeleteFlag = softDeleteFlag;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
	}

	public String getIsDefaultGroup() {
		return isDefaultGroup;
	}

	public void setIsDefaultGroup(String isDefaultGroup) {
		this.isDefaultGroup = isDefaultGroup;
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

	public String getCreatedts() {
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getModifiedts() {
		return modifiedts;
	}

	public void setModifiedts(String modifiedts) {
		this.modifiedts = modifiedts;
	}

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}

	public String getSoftDeleteFlag() {
		return softDeleteFlag;
	}

	public void setSoftDeleteFlag(String softDeleteFlag) {
		this.softDeleteFlag = softDeleteFlag;
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
		result = prime * result + ((serviceDefinitionId == null) ? 0 : serviceDefinitionId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((isDefaultGroup == null) ? 0 : isDefaultGroup.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((modifiedts == null) ? 0 : modifiedts.hashCode());
		result = prime * result + ((softDeleteFlag == null) ? 0 : softDeleteFlag.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
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
		ServiceDefinitionGroupDTO other = (ServiceDefinitionGroupDTO) obj;
		if (serviceDefinitionId == null) {
			if (other.serviceDefinitionId != null)
				return false;
		} else if (!serviceDefinitionId.equals(other.serviceDefinitionId))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}
	
	

}