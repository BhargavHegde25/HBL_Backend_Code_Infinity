package com.temenos.dbx.product.signatorygroupservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerSignatoryGroupDTO implements DBPDTO {

	private static final long serialVersionUID = -739457349705281716L;
	
	private String customerSignatoryGroupId;
	private String signatoryGroupId; 
	private String signatoryGroupName;	
	private String customerId; 
	private String createdby;
	private String modifiedby;
	private String createdts; 
	private String lastmodifiedts;
	private String synctimestamp; 
	private String softdeleteflag;
	
	public CustomerSignatoryGroupDTO() {
		super();
	}

	public CustomerSignatoryGroupDTO(String customerSignatoryGroupId, String signatoryGroupId,
			String signatoryGroupName, String customerId, String createdby, String modifiedby, String createdts,
			String lastmodifiedts, String synctimestamp, String softdeleteflag) {
		super();
		this.customerSignatoryGroupId = customerSignatoryGroupId;
		this.signatoryGroupId = signatoryGroupId;
		this.signatoryGroupName = signatoryGroupName;
		this.customerId = customerId;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
	}

	public String getCustomerSignatoryGroupId() {
		return customerSignatoryGroupId;
	}

	public void setCustomerSignatoryGroupId(String customerSignatoryGroupId) {
		this.customerSignatoryGroupId = customerSignatoryGroupId;
	}

	public String getSignatoryGroupId() {
		return signatoryGroupId;
	}

	public void setSignatoryGroupId(String signatoryGroupId) {
		this.signatoryGroupId = signatoryGroupId;
	}

	public String getSignatoryGroupName() {
		return signatoryGroupName;
	}

	public void setSignatoryGroupName(String signatoryGroupName) {
		this.signatoryGroupName = signatoryGroupName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(String softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerSignatoryGroupId == null) ? 0 : customerSignatoryGroupId.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((signatoryGroupId == null) ? 0 : signatoryGroupId.hashCode());
		result = prime * result + ((signatoryGroupName == null) ? 0 : signatoryGroupName.hashCode());
		result = prime * result + ((softdeleteflag == null) ? 0 : softdeleteflag.hashCode());
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
		CustomerSignatoryGroupDTO other = (CustomerSignatoryGroupDTO) obj;
		if (createdby == null) {
			if (other.createdby != null)
				return false;
		} else if (!createdby.equals(other.createdby))
			return false;
		if (createdts == null) {
			if (other.createdts != null)
				return false;
		} else if (!createdts.equals(other.createdts))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (customerSignatoryGroupId == null) {
			if (other.customerSignatoryGroupId != null)
				return false;
		} else if (!customerSignatoryGroupId.equals(other.customerSignatoryGroupId))
			return false;
		if (lastmodifiedts == null) {
			if (other.lastmodifiedts != null)
				return false;
		} else if (!lastmodifiedts.equals(other.lastmodifiedts))
			return false;
		if (modifiedby == null) {
			if (other.modifiedby != null)
				return false;
		} else if (!modifiedby.equals(other.modifiedby))
			return false;
		if (signatoryGroupId == null) {
			if (other.signatoryGroupId != null)
				return false;
		} else if (!signatoryGroupId.equals(other.signatoryGroupId))
			return false;
		if (signatoryGroupName == null) {
			if (other.signatoryGroupName != null)
				return false;
		} else if (!signatoryGroupName.equals(other.signatoryGroupName))
			return false;
		if (softdeleteflag == null) {
			if (other.softdeleteflag != null)
				return false;
		} else if (!softdeleteflag.equals(other.softdeleteflag))
			return false;
		if (synctimestamp == null) {
			if (other.synctimestamp != null)
				return false;
		} else if (!synctimestamp.equals(other.synctimestamp))
			return false;
		return true;
	}
}
