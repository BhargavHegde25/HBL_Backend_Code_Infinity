package com.temenos.dbx.product.payeeservices.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author KH2661
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class P2PPayeeDTO implements DBPDTO{

	private static final long serialVersionUID = 886403890008328614L;
	private String id;
	private String typeId;
	private String payeeId;
    private String createdBy;
    private String contractId;
    private String cif;
    private String noOfCustomersLinked;
    private String legalEntityId;
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	List<P2PPayeeDTO> fileRecords;

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
	
	public String getNoOfCustomersLinked() {
		return noOfCustomersLinked;
	}

	public void setNoOfCustomersLinked(String noOfCustomersLinked) {
		this.noOfCustomersLinked = noOfCustomersLinked;
	}
	
	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}


	public P2PPayeeDTO() {
		super();
	}

	
	
	public P2PPayeeDTO(String id, String typeId, String payeeId, String createdBy, String contractId,String cif, String noOfCustomersLinked, String legalEntityId) {
		this.id = id;
		this.typeId = typeId;
		this.payeeId = payeeId;
		this.createdBy = createdBy;
		this.contractId = contractId;
		this.cif = cif;
		this.noOfCustomersLinked = noOfCustomersLinked;
		this.legalEntityId = legalEntityId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((noOfCustomersLinked == null) ? 0 : noOfCustomersLinked.hashCode());
		result = prime * result + ((legalEntityId == null) ? 0 : legalEntityId.hashCode());
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
		P2PPayeeDTO other = (P2PPayeeDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}