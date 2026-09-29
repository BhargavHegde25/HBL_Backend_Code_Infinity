package com.temenos.infinity.api.chequemanagement.dto;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChequeBookStatusDTO implements DBPDTO{

   private static final long serialVersionUID = 2351459360219657311L;
	
	private List<String> approvalMatrixIds;
	private String status;
	private String requestId;
	private String confirmationNumber;
	private String customerId;
	private String companyId;
	private String accountId;
	private String featureActionID;
	private Double amount;
	private String serviceCharge;
	private String convertedAmount;
	private boolean isBusinessUser;
	private String transactionStatus;
	private String date;
	private String transactionCurrency;
	private Map<String, Double> offsetDetails;
	private String dbpErrCode;
	private String dbpErrMsg;
	private boolean isSelfApproved;
	
	public ChequeBookStatusDTO() {
		super();
	}

	public ChequeBookStatusDTO(List<String> approvalMatrixIds, String status, String requestId,
			String confirmationNumber, String customerId, String companyId, String accountId, String featureActionID,
			Double amount, String serviceCharge, String convertedAmount, boolean isBusinessUser,
			String transactionStatus, String date, String transactionCurrency, Map<String, Double> offsetDetails,
			String dbpErrCode, String dbpErrMsg, boolean isSelfApproved) {
		super();
		this.approvalMatrixIds = approvalMatrixIds;
		this.status = status;
		this.requestId = requestId;
		this.confirmationNumber = confirmationNumber;
		this.customerId = customerId;
		this.companyId = companyId;
		this.accountId = accountId;
		this.featureActionID = featureActionID;
		this.amount = amount;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.isBusinessUser = isBusinessUser;
		this.transactionStatus = transactionStatus;
		this.date = date;
		this.transactionCurrency = transactionCurrency;
		this.offsetDetails = offsetDetails;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.isSelfApproved = isSelfApproved;
	}

	public List<String> getApprovalMatrixIds() {
		return approvalMatrixIds;
	}

	public void setApprovalMatrixIds(List<String> approvalMatrixIds) {
		this.approvalMatrixIds = approvalMatrixIds;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getFeatureActionID() {
		return featureActionID;
	}

	public void setFeatureActionID(String featureActionID) {
		this.featureActionID = featureActionID;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(String convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

	public boolean isBusinessUser() {
		return isBusinessUser;
	}

	public void setBusinessUser(boolean isBusinessUser) {
		this.isBusinessUser = isBusinessUser;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public Map<String, Double> getOffsetDetails() {
		return offsetDetails;
	}

	public void setOffsetDetails(Map<String, Double> offsetDetails) {
		this.offsetDetails = offsetDetails;
	}

	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}

	public boolean isSelfApproved() {
		return isSelfApproved;
	}

	public void setSelfApproved(boolean isSelfApproved) {
		this.isSelfApproved = isSelfApproved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((approvalMatrixIds == null) ? 0 : approvalMatrixIds.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((featureActionID == null) ? 0 : featureActionID.hashCode());
		result = prime * result + (isBusinessUser ? 1231 : 1237);
		result = prime * result + (isSelfApproved ? 1231 : 1237);
		result = prime * result + ((offsetDetails == null) ? 0 : offsetDetails.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionStatus == null) ? 0 : transactionStatus.hashCode());
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
		ChequeBookStatusDTO other = (ChequeBookStatusDTO) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
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
		if (confirmationNumber == null) {
			if (other.confirmationNumber != null)
				return false;
		} else if (!confirmationNumber.equals(other.confirmationNumber))
			return false;
		if (convertedAmount == null) {
			if (other.convertedAmount != null)
				return false;
		} else if (!convertedAmount.equals(other.convertedAmount))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (dbpErrCode == null) {
			if (other.dbpErrCode != null)
				return false;
		} else if (!dbpErrCode.equals(other.dbpErrCode))
			return false;
		if (dbpErrMsg == null) {
			if (other.dbpErrMsg != null)
				return false;
		} else if (!dbpErrMsg.equals(other.dbpErrMsg))
			return false;
		if (featureActionID == null) {
			if (other.featureActionID != null)
				return false;
		} else if (!featureActionID.equals(other.featureActionID))
			return false;
		if (isBusinessUser != other.isBusinessUser)
			return false;
		if (isSelfApproved != other.isSelfApproved)
			return false;
		if (offsetDetails == null) {
			if (other.offsetDetails != null)
				return false;
		} else if (!offsetDetails.equals(other.offsetDetails))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (serviceCharge == null) {
			if (other.serviceCharge != null)
				return false;
		} else if (!serviceCharge.equals(other.serviceCharge))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (transactionCurrency == null) {
			if (other.transactionCurrency != null)
				return false;
		} else if (!transactionCurrency.equals(other.transactionCurrency))
			return false;
		if (transactionStatus == null) {
			if (other.transactionStatus != null)
				return false;
		} else if (!transactionStatus.equals(other.transactionStatus))
			return false;
		return true;
	}
	
}
