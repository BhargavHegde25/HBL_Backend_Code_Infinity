package com.kony.adminconsole.service.customerrole.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupsViewDTO implements DBPDTO{

	private static final long serialVersionUID = -1284265100650384573L;
	
	@JsonAlias({"Group_id"})
	private String groupId;
	
	@JsonAlias({"Type_Name"})
	private String typeName;
	
	@JsonAlias({"Group_Desc"})
	private String groupDesc;
	
	@JsonAlias({"Type_id"})
	private String typeId;
	
	@JsonAlias({"Status_id"})
	private String statusId;
	
	@JsonAlias({"Group_Name"})
	private String groupName;
	
	@JsonAlias({"Entitlements_Count"})
	private String entitlementsCount;
	
	@JsonAlias({"Customers_Count"})
	private String customersCount;
	
	@JsonAlias({"Status"})
	private String status;
	private String isEAgreementActive;
	private String isApplicabletoAllServices;
	private String companyLegalUnit;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getEntitlementsCount() {
		return entitlementsCount;
	}
	public void setEntitlementsCount(String entitlementsCount) {
		this.entitlementsCount = entitlementsCount;
	}
	public String getCustomersCount() {
		return customersCount;
	}
	public void setCustomersCount(String customersCount) {
		this.customersCount = customersCount;
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
	public String getIsApplicabletoAllServices() {
		return isApplicabletoAllServices;
	}
	public void setIsApplicabletoAllServices(String isApplicabletoAllServices) {
		this.isApplicabletoAllServices = isApplicabletoAllServices;
	}
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}
	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	
	public GroupsViewDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GroupsViewDTO(String groupId, String typeName, String groupDesc, String typeId, String statusId,
			String groupName, String entitlementsCount, String customersCount, String status, String isEAgreementActive,
			String isApplicabletoAllServices, String companyLegalUnit) {
		super();
		this.groupId = groupId;
		this.typeName = typeName;
		this.groupDesc = groupDesc;
		this.typeId = typeId;
		this.statusId = statusId;
		this.groupName = groupName;
		this.entitlementsCount = entitlementsCount;
		this.customersCount = customersCount;
		this.status = status;
		this.isEAgreementActive = isEAgreementActive;
		this.isApplicabletoAllServices = isApplicabletoAllServices;
		this.companyLegalUnit = companyLegalUnit;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customersCount == null) ? 0 : customersCount.hashCode());
		result = prime * result + ((entitlementsCount == null) ? 0 : entitlementsCount.hashCode());
		result = prime * result + ((groupDesc == null) ? 0 : groupDesc.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((isApplicabletoAllServices == null) ? 0 : isApplicabletoAllServices.hashCode());
		result = prime * result + ((isEAgreementActive == null) ? 0 : isEAgreementActive.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		GroupsViewDTO other = (GroupsViewDTO) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		
		return true;
	}

	

}