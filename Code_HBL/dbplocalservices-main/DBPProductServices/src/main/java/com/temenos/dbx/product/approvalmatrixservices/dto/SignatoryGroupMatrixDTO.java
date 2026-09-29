package com.temenos.dbx.product.approvalmatrixservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatoryGroupMatrixDTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1644800766024384191L;
	
	private String signatoryGroupMatrixId;
	private String approvalMatrixId;
	private String groupList;
	private String groupRule;
	private boolean isGroupMatrix;

	public SignatoryGroupMatrixDTO(String signatoryGroupMatrixId, String approvalMatrixId, String groupList,
			String groupRule, boolean isGroupMatrix) {
		super();
		this.signatoryGroupMatrixId = signatoryGroupMatrixId;
		this.approvalMatrixId = approvalMatrixId;
		this.groupList = groupList;
		this.groupRule = groupRule;
		this.isGroupMatrix = isGroupMatrix;
	}

	public SignatoryGroupMatrixDTO() {
		super();
	}

	public String getSignatoryGroupMatrixId() {
		return signatoryGroupMatrixId;
	}

	public void setSignatoryGroupMatrixId(String signatoryGroupMatrixId) {
		this.signatoryGroupMatrixId = signatoryGroupMatrixId;
	}

	public String getApprovalMatrixId() {
		return approvalMatrixId;
	}

	public void setApprovalMatrixId(String approvalMatrixId) {
		this.approvalMatrixId = approvalMatrixId;
	}

	public String getGroupList() {
		return groupList;
	}

	public void setGroupList(String groupList) {
		this.groupList = groupList;
	}

	public String getGroupRule() {
		return groupRule;
	}

	public void setGroupRule(String groupRule) {
		this.groupRule = groupRule;
	}

	public boolean isGroupMatrix() {
		return isGroupMatrix;
	}

	public void setGroupMatrix(boolean isGroupMatrix) {
		this.isGroupMatrix = isGroupMatrix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approvalMatrixId == null) ? 0 : approvalMatrixId.hashCode());
		result = prime * result + ((groupList == null) ? 0 : groupList.hashCode());
		result = prime * result + ((groupRule == null) ? 0 : groupRule.hashCode());
		result = prime * result + (isGroupMatrix ? 1231 : 1237);
		result = prime * result + ((signatoryGroupMatrixId == null) ? 0 : signatoryGroupMatrixId.hashCode());
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
		SignatoryGroupMatrixDTO other = (SignatoryGroupMatrixDTO) obj;
		if (approvalMatrixId == null) {
			if (other.approvalMatrixId != null)
				return false;
		} else if (!approvalMatrixId.equals(other.approvalMatrixId))
			return false;
		if (groupList == null) {
			if (other.groupList != null)
				return false;
		} else if (!groupList.equals(other.groupList))
			return false;
		if (groupRule == null) {
			if (other.groupRule != null)
				return false;
		} else if (!groupRule.equals(other.groupRule))
			return false;
		if (isGroupMatrix != other.isGroupMatrix)
			return false;
		if (signatoryGroupMatrixId == null) {
			if (other.signatoryGroupMatrixId != null)
				return false;
		} else if (!signatoryGroupMatrixId.equals(other.signatoryGroupMatrixId))
			return false;
		return true;
	}
	
	
	

}
