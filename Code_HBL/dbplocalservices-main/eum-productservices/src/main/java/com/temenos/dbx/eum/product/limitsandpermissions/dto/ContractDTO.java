package com.temenos.dbx.eum.product.limitsandpermissions.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDTO implements DBPDTO {
	
	private static final long serialVersionUID = 6472269632834619421L;
	
	private String id;
	private String serviceDefinitionId;
	private String serviceType;
	private String name;
	private String description;
	private String statusId;
	private String faxId;
	
	public ContractDTO() {
		super();
	}

	public ContractDTO(String id, String serviceDefinitionId, String serviceType, String name, String description,
			String statusId, String faxId) {
		super();
		this.id = id;
		this.serviceDefinitionId = serviceDefinitionId;
		this.serviceType = serviceType;
		this.name = name;
		this.description = description;
		this.statusId = statusId;
		this.faxId = faxId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getFaxId() {
		return faxId;
	}

	public void setFaxId(String faxId) {
		this.faxId = faxId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((faxId == null) ? 0 : faxId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((serviceDefinitionId == null) ? 0 : serviceDefinitionId.hashCode());
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
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
		ContractDTO other = (ContractDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}