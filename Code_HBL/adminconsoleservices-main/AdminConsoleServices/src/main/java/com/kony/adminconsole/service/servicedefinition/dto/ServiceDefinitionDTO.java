package com.kony.adminconsole.service.servicedefinition.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDefinitionDTO implements DBPDTO{
	
	private static final long serialVersionUID = 6272269111835918801L;
	
	private String id;
	private String name;
	private String description;
	private String status;
	private String serviceType;
	private String numberOfFeatures;
	private String numberOfRoles;
	private String numberOfActiveRoles;
	private String numberOfContracts;
	@JsonAlias({"defaultRole"})
	private String defaultGroup;
	private String createdby;
	private String modifiedby;
	private String dbpErrCode;
	private String dbpErrMsg;
	private String companyLegalUnit;
	private ErrorCodeEnum errCode;
	
	public ServiceDefinitionDTO() {
		super();
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getNumberOfFeatures() {
		return numberOfFeatures;
	}

	public void setNumberOfFeatures(String numberOfFeatures) {
		this.numberOfFeatures = numberOfFeatures;
	}

	public String getNumberOfRoles() {
		return numberOfRoles;
	}

	public void setNumberOfRoles(String numberOfRoles) {
		this.numberOfRoles = numberOfRoles;
	}

	public String getNumberOfContracts() {
		return numberOfContracts;
	}

	public void setNumberOfContracts(String numberOfContracts) {
		this.numberOfContracts = numberOfContracts;
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

	public void setModifiedby(String modifiedBy) {
		this.modifiedby = modifiedBy;
	}

	public String getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(String defaultGroup) {
		this.defaultGroup = defaultGroup;
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
	
	public String getNumberOfActiveRoles() {
		return numberOfActiveRoles;
	}

	public void setNumberOfActiveRoles(String numberOfActiveRoles) {
		this.numberOfActiveRoles = numberOfActiveRoles;
	}
	 public String getCompanyLegalUnit() {
	        return companyLegalUnit; 
	    }

	    public void setCompanyLegalUnit(String companyLegalUnit) {
	        this.companyLegalUnit = companyLegalUnit;
	    }

	public ServiceDefinitionDTO(String id, String name, String description, String status, String serviceType,
			String numberOfFeatures, String numberOfRoles,String numberOfActiveRoles, String numberOfContracts,String createdby,ErrorCodeEnum errCode, String defaultGroup, String modifiedby, String dbpErrCode, String dbpErrMsg, String companyLegalUnit) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.serviceType = serviceType;
		this.numberOfFeatures = numberOfFeatures;
		this.numberOfRoles = numberOfRoles;
		this.numberOfActiveRoles = numberOfActiveRoles;
		this.numberOfContracts = numberOfContracts;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.defaultGroup = defaultGroup;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.errCode = errCode;
		this.companyLegalUnit = companyLegalUnit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((numberOfContracts == null) ? 0 : numberOfContracts.hashCode());
		result = prime * result + ((numberOfFeatures == null) ? 0 : numberOfFeatures.hashCode());
		result = prime * result + ((numberOfRoles == null) ? 0 : numberOfRoles.hashCode());
		result = prime * result + ((numberOfActiveRoles == null) ? 0 : numberOfActiveRoles.hashCode());
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((errCode == null) ? 0 : errCode.hashCode());
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
		ServiceDefinitionDTO other = (ServiceDefinitionDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}