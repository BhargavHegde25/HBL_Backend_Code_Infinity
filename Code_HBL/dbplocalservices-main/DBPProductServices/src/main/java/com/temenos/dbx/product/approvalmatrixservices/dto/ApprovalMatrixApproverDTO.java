package com.temenos.dbx.product.approvalmatrixservices.dto;


import com.dbp.core.api.DBPDTO;
/**
 * 
 * @author KH9450
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
public class ApprovalMatrixApproverDTO implements DBPDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9127052122494324221L;
	private String approverId;
	private String approverName;
	public ApprovalMatrixApproverDTO(){		
	}
	public ApprovalMatrixApproverDTO (String approverId,String approverName)
	{
		this.approverId =approverId;
		this.approverName = approverName;
	}
	public String getApproverId() {
		return approverId;
	}
	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approverId == null) ? 0 : approverId.hashCode());
		result = prime * result + ((approverName == null) ? 0 : approverName.hashCode());
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
		ApprovalMatrixApproverDTO other = (ApprovalMatrixApproverDTO) obj;
		if (approverId == null) {
			if (other.approverId != null)
				return false;
		} else if (!approverId.equals(other.approverId))
			return false;
		if (approverName == null) {
			if (other.approverName != null)
				return false;
		} else if (!approverName.equals(other.approverName))
			return false;
		return true;
	}
}
