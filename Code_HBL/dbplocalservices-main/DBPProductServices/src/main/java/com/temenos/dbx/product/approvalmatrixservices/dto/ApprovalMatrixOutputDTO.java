package com.temenos.dbx.product.approvalmatrixservices.dto;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;

/**
 * 
 * @author KH9450
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
public class ApprovalMatrixOutputDTO implements DBPDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3451596611338896167L;
	private String companyId;
	private String contractId;
	private List<ApprovalMatrixCifDTO> cifs = new ArrayList<>();
	public ApprovalMatrixOutputDTO(){		
	}
	public ApprovalMatrixOutputDTO(String companyId, String contractId, List<ApprovalMatrixCifDTO> cifs)
	{
		this.companyId=companyId;
		this.contractId=contractId;
		this.cifs = cifs;
	}	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}	
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	public List<ApprovalMatrixCifDTO> getCifs() {
		return cifs;
	}
	public void setCifs(List<ApprovalMatrixCifDTO> cifs) {
		this.cifs = cifs;
	}
	public void add(ApprovalMatrixCifDTO cif)
	{
		this.cifs.add(cif);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cifs == null) ? 0 : cifs.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
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
		ApprovalMatrixOutputDTO other = (ApprovalMatrixOutputDTO) obj;
		if (cifs == null) {
			if (other.cifs != null)
				return false;
		} else if (!cifs.equals(other.cifs))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;		
		return true;
	}
}
