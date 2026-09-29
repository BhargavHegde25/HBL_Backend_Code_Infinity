package com.kony.adminconsole.service.servicedefinition.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberGroupDTO implements DBPDTO{
	
	private static final long serialVersionUID = 6472269131837613801L;
	
	private String id;
	private String name;
	private String description;
	@JsonAlias({"Type_id"})
	private String typeId;
	@JsonAlias({"Status_id"})
	private String statusId;
	private String isEagreementActive;
	private String createdBy;
	private String modifiedBy;
	private String createdts;
	private String modifiedts;
	private String synctimestamp;
	private String softDeleteFlag;
	private String isApplicableForAll;
	
	public MemberGroupDTO() {
		super();
	}

	public MemberGroupDTO(String id, String name, String description, String typeId, String statusId,
			String isEagreementActive, String createdBy, String modifiedBy, String createdts, String modifiedts,
			String synctimestamp, String softDeleteFlag, String isApplicableForAll) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.typeId = typeId;
		this.statusId = statusId;
		this.isEagreementActive = isEagreementActive;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdts = createdts;
		this.modifiedts = modifiedts;
		this.synctimestamp = synctimestamp;
		this.softDeleteFlag = softDeleteFlag;
		this.isApplicableForAll = isApplicableForAll;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getIsEagreementActive() {
		return isEagreementActive;
	}

	public void setIsEagreementActive(String isEagreementActive) {
		this.isEagreementActive = isEagreementActive;
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

	public String getIsApplicableForAll() {
		return isApplicableForAll;
	}

	public void setIsApplicableForAll(String isApplicableForAll) {
		this.isApplicableForAll = isApplicableForAll;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isApplicableForAll == null) ? 0 : isApplicableForAll.hashCode());
		result = prime * result + ((isEagreementActive == null) ? 0 : isEagreementActive.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((modifiedts == null) ? 0 : modifiedts.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((softDeleteFlag == null) ? 0 : softDeleteFlag.hashCode());
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
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
		MemberGroupDTO other = (MemberGroupDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
