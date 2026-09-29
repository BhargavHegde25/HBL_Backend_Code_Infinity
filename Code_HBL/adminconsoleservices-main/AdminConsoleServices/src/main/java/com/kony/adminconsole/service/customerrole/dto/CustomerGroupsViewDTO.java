package com.kony.adminconsole.service.customerrole.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerGroupsViewDTO implements DBPDTO{

	private static final long serialVersionUID = -2813099478951434293L;
	private String customerCount;
	private String groupId;
	private String servicedefinitionId;
	private String companyLegalUnit;
	
	public CustomerGroupsViewDTO(String customerCount, String groupId, String servicedefinitionId, String companyLegalUnit) {
		super();
		this.customerCount=customerCount; 
		this.groupId=groupId; 
		this.servicedefinitionId=servicedefinitionId; 
		this.companyLegalUnit = companyLegalUnit;
	}
	
	public CustomerGroupsViewDTO() {
		super();
	}
	
	public String getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(String customerCount) {
		this.customerCount = customerCount;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getServicedefinitionId() {
		return servicedefinitionId;
	}
	public void setServicedefinitionId(String servicedefinitionId) {
		this.servicedefinitionId = servicedefinitionId;
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
		result = prime * result + ((customerCount == null) ? 0 : customerCount.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((servicedefinitionId == null) ? 0 : servicedefinitionId.hashCode());
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
		CustomerGroupsViewDTO other = (CustomerGroupsViewDTO) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (servicedefinitionId == null) {
			if (other.servicedefinitionId != null)
				return false;
		} else if (!servicedefinitionId.equals(other.servicedefinitionId))
			return false;
		return true;
	}

	
}
