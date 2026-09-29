package com.kony.adminconsole.service.servicedefinition.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionLimitDTO implements DBPDTO{
	
	private static final long serialVersionUID = 6472269187834613801L;
	
	@JsonAlias({"Action_id"})
	private String actionId;
	@JsonAlias({"LimitType_id"})
	private String limitTypeId;
	private String value;
	private String createdBy;
	private String modifiedBy;
	private String createdts;
	private String modifiedts;
	private String synctimestamp;
	private String softDeleteFlag;
	private String companyLegalUnit;
	
	public ActionLimitDTO() {
		super();
	}

	public ActionLimitDTO(String actionId, String limitTypeId, String value, String createdBy, String modifiedBy,
			String createdts, String modifiedts, String synctimestamp, String softDeleteFlag, String companyLegalUnit) {
		super();
		this.actionId = actionId;
		this.limitTypeId = limitTypeId;
		this.value = value;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdts = createdts;
		this.modifiedts = modifiedts;
		this.synctimestamp = synctimestamp;
		this.softDeleteFlag = softDeleteFlag;
		this.companyLegalUnit= companyLegalUnit;
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
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((modifiedts == null) ? 0 : modifiedts.hashCode());
		result = prime * result + ((softDeleteFlag == null) ? 0 : softDeleteFlag.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
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
		ActionLimitDTO other = (ActionLimitDTO) obj;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		return true;
	}
	
	

}