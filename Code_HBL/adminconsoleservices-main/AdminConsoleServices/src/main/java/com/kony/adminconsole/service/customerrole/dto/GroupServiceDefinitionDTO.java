package com.kony.adminconsole.service.customerrole.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupServiceDefinitionDTO implements DBPDTO{

	private static final long serialVersionUID = 8878981885376418632L;
	
	@JsonAlias({"Group_id"})
	private String groupId;
	private String serviceDefinitionId;
	private String isDefaultGroup;
	@JsonAlias({"createdby"})
	private String createdBy;
	@JsonAlias({"modifiedby"})
	private String modifiedBy;
	private String createdts;
	private String lastmodifiedts;
	@JsonAlias({"softdeleteflag"})
	private String softdeleteFlag;
	private String companyLegalUnit;
	
	
	public GroupServiceDefinitionDTO() {
		super();
		// TODO Auto-generated constructor stub
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
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((softdeleteFlag == null) ? 0 : softdeleteFlag.hashCode());
		result = prime * result + ((companyLegalUnit == null) ? 0 : companyLegalUnit.hashCode());
		return result;
	}

	public GroupServiceDefinitionDTO(String groupId, String serviceDefinitionId, String isDefaultGroup, String createdBy,
			String modifiedBy, String createdts, String lastmodifiedts, String softdeleteFlag, String companyLegalUnit) {
		super();
		this.groupId = groupId;
		this.serviceDefinitionId = serviceDefinitionId;
		this.isDefaultGroup = isDefaultGroup;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.softdeleteFlag = softdeleteFlag;
		this.companyLegalUnit = companyLegalUnit;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	public String getLastmodifiedts() {
		return lastmodifiedts;
	}
	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}
	public String getSoftdeleteFlag() {
		return softdeleteFlag;
	}
	public void setSoftdeleteFlag(String softdeleteFlag) {
		this.softdeleteFlag = softdeleteFlag;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
	}

    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }
	
		

}
