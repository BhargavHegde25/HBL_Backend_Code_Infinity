package com.temenos.dbx.product.approvalmatrixservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author KH2387
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerApprovalMatrixDTO implements DBPDTO {

	private static final long serialVersionUID = -4163937463557522482L;
	
	private String customerId;
	private int approvalMatrixId;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + approvalMatrixId;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
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
		CustomerApprovalMatrixDTO other = (CustomerApprovalMatrixDTO) obj;
		if (approvalMatrixId != other.approvalMatrixId)
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		return true;
	}
	public int getApprovalMatrixId() {
		return approvalMatrixId;
	}
	public void setApprovalMatrixId(int approvalMatrixId) {
		this.approvalMatrixId = approvalMatrixId;
	}
	
	
	
}
