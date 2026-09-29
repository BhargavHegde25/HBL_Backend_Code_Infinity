package com.temenos.dbx.product.bulkpaymentservices.dto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkPaymentRecordDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1983945165932754717L;
	
	private String recordId;
	private String confirmationNumber;
	private String paymentId;
	private String description;
	private String featureActionId;
	private String paymentDate;
	private String scheduledDate;
	private String fileId;
	private String reviewedBy;
	
	@JsonAlias({"uploadedBy"})
	private String initiatedBy;
	
	private String companyId;
	private String roleId;
	private String status;
	
	@JsonAlias({"beneficiaryIBAN"})
	private String fromAccount;
	
	private double totalAmount;
	private double totalTransactions;
	private String requestId;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String sortByParam;
	private String sortOrder;
	private String searchString;
	private String pageSize;
	private String pageOffset;
	private String filterByParam;
	private String filterByValue;
	private String amIApprover;
	private String amICreator;
	private String receivedApprovals;
	private String requiredApprovals;
	private String approvers;
	private String comments;
	private String cancellationreason;
	private String rejectioncomments;
	private String rejectionreason;
	private String paymentStatus; 
	private String paymentOrderProduct;
	private String cancellationReasonId;
	private String statusCode;
	private String isGroupMatrix;
	
	private String actedByMeAlready;
	private String currency;
	private String batchMode;
	private String fileName;
	private String transactionCurrency;
	private String transactionAmount;
	private String serviceCharge;	
	private List<BulkPaymentErrorDTO> bulkErrorDetails = new ArrayList<>();
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg", "ErrorMessage"})
	private String dbpErrMsg;
    private String messageDetails;
    private String errorDetails;
	
	public BulkPaymentRecordDTO() {
		super();
	}
	
	public BulkPaymentRecordDTO(BulkPaymentRecordDTO dto) {
		super();
		this.recordId = dto.recordId;
		this.confirmationNumber = dto.confirmationNumber;
		this.paymentId = dto.paymentId;
		this.description = dto.description;
		this.featureActionId = dto.featureActionId;
		this.paymentDate = dto.paymentDate;
		this.scheduledDate = dto.scheduledDate;
		this.fileId = dto.fileId;
		this.reviewedBy = dto.reviewedBy;
		this.initiatedBy = dto.initiatedBy;
		this.companyId = dto.companyId;
		this.roleId = dto.roleId;
		this.status = dto.status;
		this.fromAccount = dto.fromAccount;
		this.totalAmount = dto.totalAmount;
		this.totalTransactions = dto.totalTransactions;
		this.requestId = dto.requestId;
		this.createdby = dto.createdby;
		this.modifiedby = dto.modifiedby;
		this.createdts = dto.createdts;
		this.lastmodifiedts = dto.lastmodifiedts;
		this.synctimestamp = dto.synctimestamp;
		this.sortByParam = dto.sortByParam;
		this.sortOrder = dto.sortOrder;
		this.searchString = dto.searchString;
		this.pageSize = dto.pageSize;
		this.pageOffset = dto.pageOffset;
		this.filterByParam = dto.filterByParam;
		this.filterByValue = dto.filterByValue;
		this.amIApprover = dto.amIApprover;
		this.amICreator = dto.amICreator;
		this.receivedApprovals = dto.receivedApprovals;
		this.requiredApprovals = dto.requiredApprovals;
		this.approvers = dto.approvers;
		this.comments = dto.comments;
		this.cancellationreason = dto.cancellationreason;
		this.rejectioncomments = dto.rejectioncomments;
		this.rejectionreason = dto.rejectionreason;
		this.paymentStatus = dto.paymentStatus;
		this.actedByMeAlready = dto.actedByMeAlready;
		this.currency = dto.currency;
		this.batchMode = dto.batchMode;
		this.paymentOrderProduct = dto.paymentOrderProduct;
		this.fileName = dto.fileName;
		this.dbpErrorCode = dto.dbpErrorCode;
        this.dbpErrMsg = dto.dbpErrMsg;
        this.cancellationReasonId = dto.cancellationReasonId;
		this.transactionCurrency = dto.transactionCurrency;
		this.transactionAmount = dto.transactionAmount;
		this.serviceCharge = dto.serviceCharge;		
		this.bulkErrorDetails = dto.bulkErrorDetails;
		this.statusCode = dto.statusCode;
		this.errorDetails = dto.errorDetails;
		this.messageDetails = dto.messageDetails;
		this.isGroupMatrix = dto.isGroupMatrix;
	}

	public BulkPaymentRecordDTO(String recordId, String confirmationNumber, String paymentId, String description,
			String featureActionId, String paymentDate, String scheduledDate, String fileId, String reviewedBy,
			String initiatedBy, String companyId, String roleId, String status, String fromAccount, double totalAmount,
			double totalTransactions, String requestId, String createdby, String modifiedby, String createdts,
			String lastmodifiedts, String synctimestamp, String sortByParam, String sortOrder, String searchString,
			String pageSize, String pageOffset, String filterByParam, String filterByValue, String amIApprover,
			String amICreator, String receivedApprovals, String requiredApprovals, String approvers, String comments,
			String paymentStatus, ErrorCodeEnum dbpErrorCode, String dbpErrMsg, String actedByMeAlready,
			String currency, String batchMode, String paymentOrderProduct, String fileName, String cancellationreason, String rejectioncomments, String rejectionreason, String cancellationReasonId, String statusCode,
			String transactionCurrency,String transactionAmount, String serviceCharge , String errorDescription , List<BulkPaymentErrorDTO> bulkErrorDetails, String errorDetails, String messageDetails,
			String isGroupMatrix) {
		super();
		this.recordId = recordId;
		this.confirmationNumber = confirmationNumber;
		this.paymentId = paymentId;
		this.description = description;
		this.featureActionId = featureActionId;
		this.paymentDate = paymentDate;
		this.scheduledDate = scheduledDate;
		this.fileId = fileId;
		this.reviewedBy = reviewedBy;
		this.initiatedBy = initiatedBy;
		this.companyId = companyId;
		this.roleId = roleId;
		this.status = status;
		this.fromAccount = fromAccount;
		this.totalAmount = totalAmount;
		this.totalTransactions = totalTransactions;
		this.requestId = requestId;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.sortByParam = sortByParam;
		this.sortOrder = sortOrder;
		this.searchString = searchString;
		this.pageSize = pageSize;
		this.pageOffset = pageOffset;
		this.filterByParam = filterByParam;
		this.filterByValue = filterByValue;
		this.amIApprover = amIApprover;
		this.amICreator = amICreator;
		this.receivedApprovals = receivedApprovals;
		this.requiredApprovals = requiredApprovals;
		this.approvers = approvers;
		this.comments = comments;
		this.cancellationreason = cancellationreason;
		this.rejectioncomments = rejectioncomments;
		this.rejectionreason = rejectionreason;
		this.paymentStatus = paymentStatus;
		this.actedByMeAlready = actedByMeAlready;
		this.currency = currency;
		this.batchMode = batchMode;
		this.paymentOrderProduct = paymentOrderProduct;
		this.fileName = fileName;
		this.dbpErrorCode = dbpErrorCode;
        this.dbpErrMsg = dbpErrMsg;
        this.cancellationReasonId=cancellationReasonId;
        this.transactionCurrency = transactionCurrency;
		this.transactionAmount = transactionAmount;
		this.serviceCharge = serviceCharge;	
		this.bulkErrorDetails = bulkErrorDetails;
		this.errorDetails = errorDetails;
		this.errorDetails = messageDetails;
		this.isGroupMatrix = isGroupMatrix;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeatureActionId() {
		return featureActionId;
	}

	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}

	public String getPaymentDate() {
		try {
			return HelperMethods.changeDateFormat(paymentDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getScheduledDate() {
		try {
			return HelperMethods.changeDateFormat(scheduledDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalTransactions() {
		return totalTransactions;
	}

	public void setTotalTransactions(double totalTransactions) {
		this.totalTransactions = totalTransactions;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getCreatedts() {
		try {
			return HelperMethods.changeDateFormat(createdts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getLastmodifiedts() {
		try {
			return HelperMethods.changeDateFormat(lastmodifiedts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getSynctimestamp() {
		try {
			return HelperMethods.changeDateFormat(synctimestamp, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}

	public String getSortByParam() {
		return sortByParam;
	}

	public void setSortByParam(String sortByParam) {
		this.sortByParam = sortByParam;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(String pageOffset) {
		this.pageOffset = pageOffset;
	}

	public String getFilterByParam() {
		return filterByParam;
	}

	public void setFilterByParam(String filterByParam) {
		this.filterByParam = filterByParam;
	}

	public String getFilterByValue() {
		return filterByValue;
	}

	public void setFilterByValue(String filterByValue) {
		this.filterByValue = filterByValue;
	}

	public String getAmIApprover() {
		return amIApprover;
	}

	public void setAmIApprover(String amIAprover) {
		this.amIApprover = amIAprover;
	}

	public String getAmICreator() {
		return amICreator;
	}

	public void setAmICreator(String amICreator) {
		this.amICreator = amICreator;
	}

	public String getReceivedApprovals() {
		return receivedApprovals;
	}

	public void setReceivedApprovals(String recievedApprovals) {
		this.receivedApprovals = recievedApprovals;
	}

	public String getRequiredApprovals() {
		return requiredApprovals;
	}

	public void setRequiredApprovals(String requiredApprovals) {
		this.requiredApprovals = requiredApprovals;
	}

	public String getApprovers() {
		return approvers;
	}

	public void setApprovers(String approvers) {
		this.approvers = approvers;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getCancellationreason() {
		return cancellationreason;
	}

	public void setCancellationreason(String cancellationreason) {
		this.cancellationreason = cancellationreason;
	}
	
	public String getCancellationReasonId() {
		return cancellationReasonId;
	}

	public void setCancellationReasonId(String cancellationReasonId) {
		this.cancellationReasonId = cancellationReasonId;
	}
	
	public String getstatusCode() {
		return statusCode;
	}

	public void setstatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getRejectionComments() {
		return rejectioncomments;
	}

	public void setRejectionComments(String rejectioncomments) {
		this.rejectioncomments = rejectioncomments;
	}
	
	public String getRejectionreason() {
		return rejectionreason;
	}

	public void setRejectionreason(String rejectionreason) {
		this.rejectionreason = rejectionreason;
	}
	
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getActedByMeAlready() {
		return actedByMeAlready;
	}

	public void setActedByMeAlready(String actedByMeAlready) {
		this.actedByMeAlready = actedByMeAlready;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentOrderProduct() {
		return paymentOrderProduct;
	}

	public void setPaymentOrderProduct(String paymentOrderProduct) {
		this.paymentOrderProduct = paymentOrderProduct;
	}
	
	public String getBatchMode() {
		return batchMode;
	}

	public void setBatchMode(String batchMode) {
		this.batchMode = batchMode;
	}

	public ErrorCodeEnum getDbpErrorCode() {
		return dbpErrorCode;
	}

	public void setDbpErrorCode(ErrorCodeEnum dbpErrorCode) {
		this.dbpErrorCode = dbpErrorCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}		
	
	public List<BulkPaymentErrorDTO> getBulkErrorDetails() {
		return bulkErrorDetails;
	}

	public void setBulkErrorDetails(List<BulkPaymentErrorDTO> errorDetails) {
		this.bulkErrorDetails = errorDetails;
	}	

	public String getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(String messageDetails) {
		this.messageDetails = messageDetails;
	}
	
	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getIsGroupMatrix() {
		return isGroupMatrix;
	}

	public void setIsGroupMatrix(String isGroupMatrix) {
		this.isGroupMatrix = isGroupMatrix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((approvers == null) ? 0 : approvers.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((paymentStatus == null) ? 0 : paymentStatus.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((filterByParam == null) ? 0 : filterByParam.hashCode());
		result = prime * result + ((filterByValue == null) ? 0 : filterByValue.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		result = prime * result + ((initiatedBy == null) ? 0 : initiatedBy.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((pageOffset == null) ? 0 : pageOffset.hashCode());
		result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
		result = prime * result + ((paymentDate == null) ? 0 : paymentDate.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((recordId == null) ? 0 : recordId.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + ((reviewedBy == null) ? 0 : reviewedBy.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((searchString == null) ? 0 : searchString.hashCode());
		result = prime * result + ((sortByParam == null) ? 0 : sortByParam.hashCode());
		result = prime * result + ((sortOrder == null) ? 0 : sortOrder.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((actedByMeAlready == null) ? 0 : actedByMeAlready.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((batchMode == null) ? 0 : batchMode.hashCode());
		result = prime * result + ((paymentOrderProduct == null) ? 0 : paymentOrderProduct.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
        result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
        result = prime * result + ((cancellationreason == null) ? 0 : cancellationreason.hashCode());
        result = prime * result + ((cancellationReasonId == null) ? 0 : cancellationReasonId.hashCode());
        result = prime * result + ((rejectioncomments == null) ? 0 : rejectioncomments.hashCode());
        result = prime * result + ((rejectionreason == null) ? 0 : rejectionreason.hashCode());
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((statusCode == null) ? 0 : statusCode.hashCode());
        result = prime * result + ((errorDetails == null) ? 0 : errorDetails.hashCode());
        result = prime * result + ((messageDetails == null) ? 0 : messageDetails.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalTransactions);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());	
		result = prime * result + ((bulkErrorDetails == null) ? 0 : bulkErrorDetails.hashCode());
		result = prime * result + ((isGroupMatrix == null) ? 0 : isGroupMatrix.hashCode());	
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
		BulkPaymentRecordDTO other = (BulkPaymentRecordDTO) obj;
		if (recordId == null) {
			if (other.recordId != null)
				return false;
		} else if (!recordId.equals(other.recordId))
			return false;
		return true;
	}
	
}
