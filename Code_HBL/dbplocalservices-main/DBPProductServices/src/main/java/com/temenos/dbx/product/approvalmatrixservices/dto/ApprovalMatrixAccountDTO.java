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
public class ApprovalMatrixAccountDTO implements DBPDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5494717454043474523L;
	private String accountId;
	private String accountName;
	private String accountType;
	private String ownershipType;
	private List<ApprovalMatrixLimitTypeDTO> limitTypes = new ArrayList<>();
	public ApprovalMatrixAccountDTO(){}	
	public ApprovalMatrixAccountDTO(String accountId,String accountName,List<ApprovalMatrixLimitTypeDTO> limitTypes, String accountType, String ownershipType)
	{
		this.accountId = accountId;
		this.limitTypes = limitTypes;
		this.accountName = accountName;
		this.accountType = accountType;
		this.ownershipType = ownershipType;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getOwnershipType() {
		return ownershipType;
	}
	public void setOwnershipType(String ownershipType) {
		this.ownershipType = ownershipType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((ownershipType == null) ? 0 : ownershipType.hashCode());
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
		ApprovalMatrixAccountDTO other = (ApprovalMatrixAccountDTO) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (limitTypes == null) {
			if (other.limitTypes != null)
				return false;
		} else if (!limitTypes.equals(other.limitTypes))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (ownershipType == null) {
			if (other.ownershipType != null)
				return false;
		} else if (!ownershipType.equals(other.ownershipType))
			return false;
		
		
		return true;
	}
}
