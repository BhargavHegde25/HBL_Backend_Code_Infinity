package com.temenos.dbx.product.approvalservices.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.approvalmatrixservices.dto.SignatoryGroupMatrixDTO;

/**
 * 
 * @author KH2174
 * @version
 * implements {@link DBPDTO}
 * 
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BBRequestDTO implements DBPDTO{

	private static final long serialVersionUID = 6897637723208972393L;
	
	private String requestId;
	private String assocRequestId;
	private String transactionId; 
	private List<String> approvalMatrixIds;
	private Map<String, SignatoryGroupMatrixDTO> signatoryGroupMatrices;
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
	
	private String requestApprovalMatrixId;
    private String numberOfApprovals;
    
	private String isSelfApproved;
	
	private String isGroupMatrix;
	private String additionalMeta;
	private String companyLegalUnit;
	@Nullable
	private String sentByName;
	@Nullable
	private String sentByUserName;
	@Nullable
	private String createdts;
	
	

	public String getSentByName() {
		return sentByName;
	}
	public void setSentByName(String sentByName) {
		this.sentByName = sentByName;
	}
	public String getSentByUserName() {
		return sentByUserName;
	}
	public void setSentByUserName(String sentByUserName) {
		this.sentByUserName = sentByUserName;
	}
	public String getCreatedts() {
		return createdts;
	}
	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}
	public String getAdditionalMeta(){
		return this.additionalMeta;
	}
	public void setAdditionalMeta(String additionalMeta){
		this.additionalMeta = additionalMeta;
	}
	public String getIsGroupMatrix() {
		return isGroupMatrix;
	}
	public void setIsGroupMatrix(String isGroupMatrix) {
		this.isGroupMatrix = isGroupMatrix;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getAssocRequestId() {
		return assocRequestId;
	}

	public void setAssocRequestId(String assocRequestId) {
		this.assocRequestId = assocRequestId;
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
	
	public Map<String, SignatoryGroupMatrixDTO> getSignatoryGroupMatrices() {
		return signatoryGroupMatrices;
	}
	public void setSignatoryGroupMatrices(Map<String, SignatoryGroupMatrixDTO> signatoryGroupMatrices) {
		this.signatoryGroupMatrices = signatoryGroupMatrices;
	}
	

	public String getRequestApprovalMatrixId() {
		return requestApprovalMatrixId;
	}

	public void setRequestApprovalMatrixId(String requestApprovalMatrixId) {
		this.requestApprovalMatrixId = requestApprovalMatrixId;
	}

	public String getNumberOfApprovals() {
		return numberOfApprovals;
	}

	public void setNumberOfApprovals(String numberOfApprovals) {
		this.numberOfApprovals = numberOfApprovals;
	}
	
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}
	
	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	
	public BBRequestDTO() {
		super();
	}
	public BBRequestDTO(String requestId, String transactionId, List<String> approvalMatrixIds, String featureActionId,
			String companyId, String accountId, String status, String createdBy, int requiredSets, int receivedSets, String amICreator, String amIApprover, String requiredApprovals,
			String receivedApprovals, String actedByMeAlready, String isSelfApproved, Map<String, SignatoryGroupMatrixDTO>signatoryGroupMatrices,
			String requestApprovalMatrixId, String numberOfApprovals, String isGroupMatrix, String companyLegalUnit) {
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
		this.signatoryGroupMatrices=signatoryGroupMatrices;
		this.requestApprovalMatrixId = requestApprovalMatrixId;
		this.numberOfApprovals = numberOfApprovals;
		this.isGroupMatrix = isGroupMatrix;
		this.companyLegalUnit = companyLegalUnit;
	}
	public BBRequestDTO(String requestId, String transactionId, List<String> approvalMatrixIds, String featureActionId,
			String companyId, String accountId, String status, String createdBy, int requiredSets, int receivedSets, String companyLegalUnit) {
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
		this.companyLegalUnit = companyLegalUnit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((actedByMeAlready == null) ? 0 : actedByMeAlready.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((approvalMatrixIds == null) ? 0 : approvalMatrixIds.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((isGroupMatrix == null) ? 0 : isGroupMatrix.hashCode());
		result = prime * result + ((isSelfApproved == null) ? 0 : isSelfApproved.hashCode());
		result = prime * result + ((numberOfApprovals == null) ? 0 : numberOfApprovals.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + receivedSets;
		result = prime * result + ((requestApprovalMatrixId == null) ? 0 : requestApprovalMatrixId.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + requiredSets;
		result = prime * result + ((signatoryGroupMatrices == null) ? 0 : signatoryGroupMatrices.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((companyLegalUnit == null) ? 0 : companyLegalUnit.hashCode());
		result = prime * result + ((sentByName == null) ? 0 : sentByName.hashCode());
		result = prime * result + ((sentByUserName == null) ? 0 : sentByUserName.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
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
		if (actedByMeAlready == null) {
			if (other.actedByMeAlready != null)
				return false;
		} else if (!actedByMeAlready.equals(other.actedByMeAlready))
			return false;
		if (amIApprover == null) {
			if (other.amIApprover != null)
				return false;
		} else if (!amIApprover.equals(other.amIApprover))
			return false;
		if (amICreator == null) {
			if (other.amICreator != null)
				return false;
		} else if (!amICreator.equals(other.amICreator))
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
		if (isGroupMatrix == null) {
			if (other.isGroupMatrix != null)
				return false;
		} else if (!isGroupMatrix.equals(other.isGroupMatrix))
			return false;
		if (isSelfApproved == null) {
			if (other.isSelfApproved != null)
				return false;
		} else if (!isSelfApproved.equals(other.isSelfApproved))
			return false;
		if (numberOfApprovals == null) {
			if (other.numberOfApprovals != null)
				return false;
		} else if (!numberOfApprovals.equals(other.numberOfApprovals))
			return false;
		if (receivedApprovals == null) {
			if (other.receivedApprovals != null)
				return false;
		} else if (!receivedApprovals.equals(other.receivedApprovals))
			return false;
		if (receivedSets != other.receivedSets)
			return false;
		if (requestApprovalMatrixId == null) {
			if (other.requestApprovalMatrixId != null)
				return false;
		} else if (!requestApprovalMatrixId.equals(other.requestApprovalMatrixId))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (requiredApprovals == null) {
			if (other.requiredApprovals != null)
				return false;
		} else if (!requiredApprovals.equals(other.requiredApprovals))
			return false;
		if (requiredSets != other.requiredSets)
			return false;
		if (signatoryGroupMatrices == null) {
			if (other.signatoryGroupMatrices != null)
				return false;
		} else if (!signatoryGroupMatrices.equals(other.signatoryGroupMatrices))
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
		if (companyLegalUnit == null) {
			if (other.companyLegalUnit != null)
				return false;
		} else if (!companyLegalUnit .equals(other.companyLegalUnit))
			return false;
		if (sentByName == null) {
			if (other.sentByName != null)
				return false;
		} else if (!sentByName .equals(other.sentByName))
			return false;
		if (sentByUserName == null) {
			if (other.sentByUserName != null)
				return false;
		} else if (!sentByUserName .equals(other.sentByUserName))
			return false;
		if (createdts == null) {
			if (other.createdts != null)
				return false;
		} else if (!createdts .equals(other.createdts))
			return false;
		return true;
	}
}