package com.temenos.infinity.api.arrangements.dto;
import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomViewDTO implements DBPDTO {

	private static final long serialVersionUID = 4660929522082832213L;
	
	private String id;
	private String name;
	private String customerId;
	private String accountIds;
	private String legalEntityId;
	private String coreCustomerId;
	
	public CustomViewDTO() {
		super();
	}

/*	public CustomViewDTO(String id, String name, String customerId, String accountIds, String legalEntityId) {
		super();
		this.id = id;
		this.name = name;
		this.customerId = customerId;
		this.accountIds = accountIds;
		this.legalEntityId = legalEntityId;
	}*/

	public CustomViewDTO(String id, String name, String customerId, String accountIds, String legalEntityId,String coreCustomerId) {
		super();
		this.id = id;
		this.name = name;
		this.customerId = customerId;
		this.accountIds = accountIds;
		this.legalEntityId = legalEntityId;
		this.coreCustomerId = coreCustomerId;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(String accountIds) {
		this.accountIds = accountIds;
	}
	
	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	
	
	public String getCoreCustomerId() {
		return coreCustomerId;
	}

	public void setCoreCustomerId(String coreCustomerId) {
		this.coreCustomerId = coreCustomerId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((legalEntityId == null) ? 0 : legalEntityId.hashCode());
		result = prime * result + ((accountIds == null) ? 0 : accountIds.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CustomViewDTO other = (CustomViewDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
