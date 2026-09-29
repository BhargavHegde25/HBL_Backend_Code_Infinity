package com.kony.adminconsole.service.termandcondition.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TermAndConditionAppDTO implements DBPDTO{
	
	private static final long serialVersionUID = 6472269167837613801L;
	
	private String id;
	private String TermAndConditionId;
	private String AppId;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String modifiedts;
	private String synctimestamp;
	private String softDeleteFlag;
	private String companyLegalUnit;
	
	public TermAndConditionAppDTO() {
		super();
	}

	public TermAndConditionAppDTO(String groupId, String serviceDefinitionId, String isDefaultGroup, String createdby,
			String modifiedby, String createdts, String modifiedts, String synctimestamp, String softDeleteFlag) {
		super();
		
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.modifiedts = modifiedts;
		this.synctimestamp = synctimestamp;
		this.softDeleteFlag = softDeleteFlag;
	}

	
	public String getCreatedBy() {
		return createdby;
	}

	public void setCreatedBy(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedBy() {
		return modifiedby;
	}

	public void setModifiedBy(String modifiedby) {
		this.modifiedby = modifiedby;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((TermAndConditionId == null) ? 0 : TermAndConditionId.hashCode());
		result = prime * result + ((AppId == null) ? 0 : AppId.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
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
		TermAndConditionAppDTO other = (TermAndConditionAppDTO) obj;
		if (AppId == null) {
			if (other.AppId != null)
				return false;
		} else if (!AppId.equals(other.AppId))
			return false;
		if (TermAndConditionId == null) {
			if (other.TermAndConditionId != null)
				return false;
		} else if (!TermAndConditionId.equals(other.TermAndConditionId))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTermAndConditionId() {
		return TermAndConditionId;
	}

	public void setTermAndConditionId(String termAndConditionId) {
		TermAndConditionId = termAndConditionId;
	}

	public String getAppId() {
		return AppId;
	}

	public void setAppId(String appId) {
		AppId = appId;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

}