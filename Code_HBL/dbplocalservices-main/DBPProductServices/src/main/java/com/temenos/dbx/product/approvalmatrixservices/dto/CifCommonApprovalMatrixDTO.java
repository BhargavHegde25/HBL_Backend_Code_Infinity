package com.temenos.dbx.product.approvalmatrixservices.dto;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;

public class CifCommonApprovalMatrixDTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9000993943315855172L;
	private List<ApprovalMatrixLimitTypeDTO> limitTypes = new ArrayList<>();
	
	public CifCommonApprovalMatrixDTO(){}	
	public CifCommonApprovalMatrixDTO(List<ApprovalMatrixLimitTypeDTO> limitTypes)
	{
		this.limitTypes = limitTypes;
	}

	public List<ApprovalMatrixLimitTypeDTO> getLimitTypes() {
		return limitTypes;
	}
	public void setLimitTypes(List<ApprovalMatrixLimitTypeDTO> limitTypes) {
		this.limitTypes = limitTypes;
	}
	public void add(ApprovalMatrixLimitTypeDTO limiType)
	{
		this.limitTypes.add(limiType);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((limitTypes == null) ? 0 : limitTypes.hashCode());
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
		CifCommonApprovalMatrixDTO other = (CifCommonApprovalMatrixDTO) obj;
		if (limitTypes == null) {
			if (other.limitTypes != null)
				return false;
		} else if (!limitTypes.equals(other.limitTypes))
			return false;
		return true;		
	}
}
