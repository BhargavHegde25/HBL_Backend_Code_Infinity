package com.temenos.dbx.product.payeeservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalPayeeDTO implements DBPDTO {

	private static final long serialVersionUID = 3608269080343682614L;
	
	private String id;
	private String typeId;
	private String payeeId;
	private String customerId;
	private String companyId;
	private String cif;
	@JsonIgnore
	private String isBusinessPayee;
	
	public ExternalPayeeDTO() {
		super();
	}

	public ExternalPayeeDTO(String id, String typeId, String payeeId, String customerId, String companyId, String cif,
			String isBusinessPayee) {
		super();
		this.id = id;
		this.typeId = typeId;
		this.payeeId = payeeId;
		this.customerId = customerId;
		this.companyId = companyId;
		this.cif = cif;
		this.isBusinessPayee = isBusinessPayee;
	}

	public ExternalPayeeDTO convert(BillPayPayeeBackendDTO dto) {
		this.payeeId = dto.getId();
		this.customerId = dto.getUserId();
		this.companyId = dto.getCompanyId();
		this.isBusinessPayee = dto.getIsBusinessPayee();
		return this;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getIsBusinessPayee() {
		return isBusinessPayee;
	}

	public void setIsBusinessPayee(String isBusinessPayee) {
		this.isBusinessPayee = isBusinessPayee;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isBusinessPayee == null) ? 0 : isBusinessPayee.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
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
		ExternalPayeeDTO other = (ExternalPayeeDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}