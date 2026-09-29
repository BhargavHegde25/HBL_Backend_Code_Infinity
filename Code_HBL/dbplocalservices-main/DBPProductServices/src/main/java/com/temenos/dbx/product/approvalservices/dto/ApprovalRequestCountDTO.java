package com.temenos.dbx.product.approvalservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * Stores the Approvals and Request counts for a given user
 * @author KH1755
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalRequestCountDTO implements DBPDTO {

	private static final long serialVersionUID = 5647509752319049710L;
	
	long achTransactionsForMyApproval;
	
	long achFilesForMyApproval;
	
	long generalTransactionsForMyApproval;
	
	long myRequestsWaiting;
	long myRequestsRejected;
	long myRequestsApproved;
	
	
	public ApprovalRequestCountDTO() {
		super();
	}
	
	public ApprovalRequestCountDTO(long achTransactionsForMyApproval, long achFilesForMyApproval,
			long generalTransactionsForMyApproval, long myRequestsWaiting, long myRequestsRejected,
			long myRequestsApproved) {
		super();
		this.achTransactionsForMyApproval = achTransactionsForMyApproval;
		this.achFilesForMyApproval = achFilesForMyApproval;
		this.generalTransactionsForMyApproval = generalTransactionsForMyApproval;
		this.myRequestsWaiting = myRequestsWaiting;
		this.myRequestsRejected = myRequestsRejected;
		this.myRequestsApproved = myRequestsApproved;
	}
	
	public long getAchTransactionsForMyApproval() {
		return achTransactionsForMyApproval;
	}

	public void setAchTransactionsForMyApproval(long achTransactionsForMyApproval) {
		this.achTransactionsForMyApproval = achTransactionsForMyApproval;
	}

	public long getAchFilesForMyApproval() {
		return achFilesForMyApproval;
	}

	public void setAchFilesForMyApproval(long achFilesForMyApproval) {
		this.achFilesForMyApproval = achFilesForMyApproval;
	}

	public long getGeneralTransactionsForMyApproval() {
		return generalTransactionsForMyApproval;
	}
	public void setGeneralTransactionsForMyApproval(long generalTransactionsForMyApproval) {
		this.generalTransactionsForMyApproval = generalTransactionsForMyApproval;
	}
	public long getMyRequestsWaiting() {
		return myRequestsWaiting;
	}
	public void setMyRequestsWaiting(long myRequestsWaiting) {
		this.myRequestsWaiting = myRequestsWaiting;
	}
	public long getMyRequestsRejected() {
		return myRequestsRejected;
	}
	public void setMyRequestsRejected(long myRequestsRejected) {
		this.myRequestsRejected = myRequestsRejected;
	}
	public long getMyRequestsApproved() {
		return myRequestsApproved;
	}
	public void setMyRequestsApproved(long myRequestsApproved) {
		this.myRequestsApproved = myRequestsApproved;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (achFilesForMyApproval ^ (achFilesForMyApproval >>> 32));
		result = prime * result + (int) (achTransactionsForMyApproval ^ (achTransactionsForMyApproval >>> 32));
		result = prime * result + (int) (generalTransactionsForMyApproval ^ (generalTransactionsForMyApproval >>> 32));
		result = prime * result + (int) (myRequestsApproved ^ (myRequestsApproved >>> 32));
		result = prime * result + (int) (myRequestsRejected ^ (myRequestsRejected >>> 32));
		result = prime * result + (int) (myRequestsWaiting ^ (myRequestsWaiting >>> 32));
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
		ApprovalRequestCountDTO other = (ApprovalRequestCountDTO) obj;
		if (achFilesForMyApproval != other.achFilesForMyApproval)
			return false;
		if (achTransactionsForMyApproval != other.achTransactionsForMyApproval)
			return false;
		if (generalTransactionsForMyApproval != other.generalTransactionsForMyApproval)
			return false;
		if (myRequestsApproved != other.myRequestsApproved)
			return false;
		if (myRequestsRejected != other.myRequestsRejected)
			return false;
		if (myRequestsWaiting != other.myRequestsWaiting)
			return false;
		return true;
	}
	
	
	
}
