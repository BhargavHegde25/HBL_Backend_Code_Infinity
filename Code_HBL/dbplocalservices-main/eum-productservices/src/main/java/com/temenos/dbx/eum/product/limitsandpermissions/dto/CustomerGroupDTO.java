package com.temenos.dbx.eum.product.limitsandpermissions.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerGroupDTO implements DBPDTO{
	
	private static final long serialVersionUID = 6472269632290419421L;
	
	@JsonAlias({"Customer_id"})
	private String customerId;
	private String coreCustomerId;
	private String contractId;
	@JsonAlias({"Group_id"})
	private String groupId;
	
	public CustomerGroupDTO() {
		super();
	}

	public CustomerGroupDTO(String customerId, String coreCustomerId, String contractId, String groupId) {
		super();
		this.customerId = customerId;
		this.coreCustomerId = coreCustomerId;
		this.contractId = contractId;
		this.groupId = groupId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCoreCustomerId() {
		return coreCustomerId;
	}

	public void setCoreCustomerId(String coreCustomerId) {
		this.coreCustomerId = coreCustomerId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((coreCustomerId == null) ? 0 : coreCustomerId.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		CustomerGroupDTO other = (CustomerGroupDTO) obj;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (coreCustomerId == null) {
			if (other.coreCustomerId != null)
				return false;
		} else if (!coreCustomerId.equals(other.coreCustomerId))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}
	
}
