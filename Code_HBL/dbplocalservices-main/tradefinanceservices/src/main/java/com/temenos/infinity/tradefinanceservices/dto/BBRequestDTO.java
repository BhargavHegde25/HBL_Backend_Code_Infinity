package com.temenos.infinity.tradefinanceservices.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BBRequestDTO implements DBPDTO{

	private static final long serialVersionUID = 6897637723208972343L;
	
	private String requestId;
	private String transactionId; 
	private List<String> approvalMatrixIds;
	private String featureActionId;
	private String companyId;
	private String accountId;
	private String status;
	private String createdby;
	private int requiredSets;
	private int receivedSets;
	
	private String amIApprover;
	private String amICreator;
	private String requiredApprovals;
	private String receivedApprovals;
	private String actedByMeAlready;
	
	private String isSelfApproved;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public List<String> getApprovalMatrixIds() {
		return approvalMatrixIds;
	}
	public void setApprovalMatrixIds(List<String> approvalMatrixIds) {
		this.approvalMatrixIds = approvalMatrixIds;
	}
	public String getFeatureActionId() {
		return featureActionId;
	}
	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public int getRequiredSets() {
		return requiredSets;
	}
	public void setRequiredSets(int requiredSets) {
		this.requiredSets = requiredSets;
	}
	public int getReceivedSets() {
		return receivedSets;
	}
	public void setReceivedSets(int receivedSets) {
		this.receivedSets = receivedSets;
	}
	public String getAmIApprover() {
		return amIApprover;
	}
	public void setAmIApprover(String amIApprover) {
		this.amIApprover = amIApprover;
	}
	public String getAmICreator() {
		return amICreator;
	}
	public void setAmICreator(String amICreator) {
		this.amICreator = amICreator;
	}
	public String getRequiredApprovals() {
		return requiredApprovals;
	}
	public void setRequiredApprovals(String requiredApprovals) {
		this.requiredApprovals = requiredApprovals;
	}
	public String getReceivedApprovals() {
		return receivedApprovals;
	}
	public void setReceivedApprovals(String receivedApprovals) {
		this.receivedApprovals = receivedApprovals;
	}
	public String getActedByMeAlready() {
		return actedByMeAlready;
	}
	public void setActedByMeAlready(String actedByMeAlready) {
		this.actedByMeAlready = actedByMeAlready;
	}
	public String getIsSelfApproved() {
		return isSelfApproved;
	}
	public void setIsSelfApproved(String isSelfApproved) {
		this.isSelfApproved = isSelfApproved;
	}
	
	public BBRequestDTO() {
		super();
	}
	public BBRequestDTO(String requestId, String transactionId, List<String> approvalMatrixIds, String featureActionId,
			String companyId, String accountId, String status, String createdBy, int requiredSets, int receivedSets, String amICreator, String amIApprover, String requiredApprovals,
			String receivedApprovals, String actedByMeAlready, String isSelfApproved) {
		super();
		this.requestId = requestId;
		this.transactionId = transactionId;
		this.approvalMatrixIds = approvalMatrixIds;
		this.featureActionId = featureActionId;
		this.companyId = companyId;
		this.accountId = accountId;
		this.status = status;
		this.createdby = createdBy;
		this.requiredSets = requiredSets;
		this.receivedSets = receivedSets;
		this.amIApprover = amIApprover;
		this.amICreator = amICreator; 
		this.requiredApprovals = requiredApprovals;
		this.receivedApprovals = receivedApprovals;
		this.actedByMeAlready = actedByMeAlready;
		this.isSelfApproved = isSelfApproved;
	}
	public BBRequestDTO(String requestId, String transactionId, List<String> approvalMatrixIds, String featureActionId,
			String companyId, String accountId, String status, String createdBy, int requiredSets, int receivedSets) {
		super();
		this.requestId = requestId;
		this.transactionId = transactionId;
		this.approvalMatrixIds = approvalMatrixIds;
		this.featureActionId = featureActionId;
		this.companyId = companyId;
		this.accountId = accountId;
		this.status = status;
		this.createdby = createdBy;
		this.requiredSets = requiredSets;
		this.receivedSets = receivedSets;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((approvalMatrixIds == null) ? 0 : approvalMatrixIds.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + receivedSets;
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + ((actedByMeAlready == null) ? 0 : actedByMeAlready.hashCode());
		result = prime * result + requiredSets;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((isSelfApproved == null) ? 0 : isSelfApproved.hashCode());
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
		BBRequestDTO other = (BBRequestDTO) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (approvalMatrixIds == null) {
			if (other.approvalMatrixIds != null)
				return false;
		} else if (!approvalMatrixIds.equals(other.approvalMatrixIds))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (createdby == null) {
			if (other.createdby != null)
				return false;
		} else if (!createdby.equals(other.createdby))
			return false;
		if (featureActionId == null) {
			if (other.featureActionId != null)
				return false;
		} else if (!featureActionId.equals(other.featureActionId))
			return false;
		if (receivedSets != other.receivedSets)
			return false;
		if (requestId != other.requestId)
			return false;
		if (requiredSets != other.requiredSets)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}	
}
