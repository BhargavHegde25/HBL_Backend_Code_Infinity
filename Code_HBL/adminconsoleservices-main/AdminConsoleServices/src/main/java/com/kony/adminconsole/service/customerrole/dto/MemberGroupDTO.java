package com.kony.adminconsole.service.customerrole.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberGroupDTO implements DBPDTO {
	
	private static final long serialVersionUID = -5353265235536108814L;
	private String id;
	@JsonAlias({"Name"})
	private String name;
	@JsonAlias({"Description"})
	private String description;
	@JsonAlias({"Type_id"})
	private String typeId;
	@JsonAlias({"Status_id"})
	private String status;
	private String isEAgreementActive;
	private String createdBy;
	@JsonAlias({"modifiedby"})
	private String modifiedBy;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	@JsonAlias({"softdeleteflag"})
	private String softDeleteFlag;
	private String isApplicabletoAllServices;
	private String dbpErrCode;
	private String dbpErrMsg;
	private ErrorCodeEnum errCode;
	private String companyLegalUnit;
	
	public MemberGroupDTO(String id, String name, String description, String typeId, String status, String isEAgreementActive, String createdBy, String modifiedBy, String createdts, String lastmodifiedts, String synctimestamp, String softDeleteFlag, String isApplicabletoAllServices, String dbpErrCode, String dbpErrMsg, ErrorCodeEnum errCode, String companyLegalUnit) {
		super();
		this.id=id; 
		this.name=name; 
		this.description=description; 
		this.typeId=typeId;
		this.status=status; 
		this.isEAgreementActive=isEAgreementActive; 
		this.createdBy=createdBy;
        this.modifiedBy=modifiedBy; 
        this.createdts=createdts; 
        this.lastmodifiedts=lastmodifiedts; 
        this.synctimestamp=synctimestamp; 
        this.softDeleteFlag=softDeleteFlag; 
        this.isApplicabletoAllServices=isApplicabletoAllServices; 
        this.dbpErrCode=dbpErrCode; 
        this.dbpErrMsg=dbpErrMsg; 
        this.errCode=errCode;
		this.companyLegalUnit = companyLegalUnit;
	}
	
	public MemberGroupDTO() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isApplicabletoAllServices == null) ? 0 : isApplicabletoAllServices.hashCode());
		result = prime * result + ((isEAgreementActive == null) ? 0 : isEAgreementActive.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((softDeleteFlag == null) ? 0 : softDeleteFlag.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
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
		MemberGroupDTO other = (MemberGroupDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
				return true;
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
	public String getIsApplicabletoAllServices() {
		return isApplicabletoAllServices;
	}
	public void setIsApplicabletoAllServices(String isApplicabletoAllServices) {
		this.isApplicabletoAllServices = isApplicabletoAllServices;
	}

	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}

	public ErrorCodeEnum getErrCode() {
		return errCode;
	}

	public void setErrCode(ErrorCodeEnum errCode) {
		this.errCode = errCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsEAgreementActive() {
		return isEAgreementActive;
	}

	public void setIsEAgreementActive(String isEAgreementActive) {
		this.isEAgreementActive = isEAgreementActive;
	}
	
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }
    
}
