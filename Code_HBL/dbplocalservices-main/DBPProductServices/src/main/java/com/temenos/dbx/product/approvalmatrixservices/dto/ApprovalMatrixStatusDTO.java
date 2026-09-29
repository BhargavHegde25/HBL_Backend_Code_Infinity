package com.temenos.dbx.product.approvalmatrixservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author KH2387
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ApprovalMatrixStatusDTO implements DBPDTO {
	
	private static final long serialVersionUID = -4163937463557522482L;
	private String contractId;
	private String coreCustomerId;
	private boolean isDisabled;	
	
	public ApprovalMatrixStatusDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApprovalMatrixStatusDTO(String contractId, String coreCustomerId, boolean isDisabled) {
		super();
		this.contractId = contractId;
		this.coreCustomerId = coreCustomerId;
		this.isDisabled = isDisabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coreCustomerId == null) ? 0 : coreCustomerId.hashCode());
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + (isDisabled ? 1231 : 1237);
		return result;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApprovalMatrixStatusDTO other = (ApprovalMatrixStatusDTO) obj;
		if (coreCustomerId == null) {
			if (other.coreCustomerId != null)
				return false;
		} else if (!coreCustomerId.equals(other.coreCustomerId))
			return false;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (isDisabled != other.isDisabled)
			return false;
		return true;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getCoreCustomerId() {
		return coreCustomerId;
	}

	public void setCoreCustomerId(String coreCustomerId) {
		this.coreCustomerId = coreCustomerId;
	}

	public boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

}