package com.temenos.dbx.product.signatorygroupservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatoryGroupRequestMatrixDTO implements DBPDTO {
	
	private static final long serialVersionUID = 4974760643383086307L;
	
	private String signatoryGroupRequestMatrixId;
	private String requestId;
	private String approvalMatrixId;
	private String groupList;
	private String groupRuleValue;
	private String pendingGroupList;
	private String limitTypeId;
	private boolean isApproved;
	
	public SignatoryGroupRequestMatrixDTO() {
		super();
	}
	
	public SignatoryGroupRequestMatrixDTO(String signatoryGroupRequestMatrixId, String requestId, String  approvalMatrixId,
			String groupList,String groupRuleValue,String pendingGroupList,boolean isApproved,String limitTypeId) {
		super();
		this.signatoryGroupRequestMatrixId = signatoryGroupRequestMatrixId;
		this.requestId = requestId;
		this.approvalMatrixId = approvalMatrixId;
		this.groupList = groupList;
		this.groupRuleValue = groupRuleValue;
		this.pendingGroupList = pendingGroupList;
		this.isApproved = isApproved;
		this.limitTypeId = limitTypeId;
	}
	
	public String getSignatoryGroupRequestMatrixId() {
		return signatoryGroupRequestMatrixId;
	}

	public void setSignatoryGroupRequestMatrixId(String signatoryGroupRequestMatrixId) {
		this.signatoryGroupRequestMatrixId = signatoryGroupRequestMatrixId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	public String getGroupRuleValue() {
		return groupRuleValue;
	}

	public void setGroupRuleValue(String groupRuleValue) {
		this.groupRuleValue = groupRuleValue;
	}

	public String getPendingGroupList() {
		return pendingGroupList;
	}

	public void setPendingGroupList(String pendingGroupList) {
		this.pendingGroupList = pendingGroupList;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	public String getLimitTypeId() {
		return limitTypeId;
	}

	public void setLimitTypeId(String limitTypeId) {
		this.limitTypeId = limitTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approvalMatrixId == null) ? 0 : approvalMatrixId.hashCode());
		result = prime * result + ((groupList == null) ? 0 : groupList.hashCode());
		result = prime * result + ((groupRuleValue == null) ? 0 : groupRuleValue.hashCode());
		result = prime * result + (isApproved ? 1231 : 1237);
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((pendingGroupList == null) ? 0 : pendingGroupList.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result
				+ ((signatoryGroupRequestMatrixId == null) ? 0 : signatoryGroupRequestMatrixId.hashCode());
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
		SignatoryGroupRequestMatrixDTO other = (SignatoryGroupRequestMatrixDTO) obj;
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
		if (groupRuleValue == null) {
			if (other.groupRuleValue != null)
				return false;
		} else if (!groupRuleValue.equals(other.groupRuleValue))
			return false;
		if (isApproved != other.isApproved)
			return false;
		if (limitTypeId == null) {
			if (other.limitTypeId != null)
				return false;
		} else if (!limitTypeId.equals(other.limitTypeId))
			return false;
		if (pendingGroupList == null) {
			if (other.pendingGroupList != null)
				return false;
		} else if (!pendingGroupList.equals(other.pendingGroupList))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (signatoryGroupRequestMatrixId == null) {
			if (other.signatoryGroupRequestMatrixId != null)
				return false;
		} else if (!signatoryGroupRequestMatrixId.equals(other.signatoryGroupRequestMatrixId))
			return false;
		return true;
	}
}
