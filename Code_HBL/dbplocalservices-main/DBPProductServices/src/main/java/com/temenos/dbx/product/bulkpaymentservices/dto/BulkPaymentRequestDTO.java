package com.temenos.dbx.product.bulkpaymentservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkPaymentRequestDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6050902048652053059L;

	private String paymentrequestId;
	private String templateId;
	private String templateName;
	private String processingMode;
	private String confirmationNumber;
	private String paymentId;
	private String description;
	private String featureActionId;
	private String paymentDate;
	private String scheduledDate;
	private String fileId;
	private String reviewedBy;
	private String initiatedBy;
	private String companyId;
	private String roleId;
	private String status;
	private String fromAccount;
	private Double totalAmount;
	private Double totalTransactions;
	private String paymentStatus;
	private String currency;
	private String bulkType;
	private String updateReference;
	private String creditReference;
	private String debitReference;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;

	private ErrorCodeEnum dbpErrorCode;
	@JsonAlias({ "errorMessage", "errmsg", "ErrorMessage" })
	private String dbpErrMsg;

	public BulkPaymentRequestDTO() {
		super();
	}

	public BulkPaymentRequestDTO(String paymentrequestId, String templateId, String templateName, String processingMode,
			String confirmationNumber, String paymentId, String description, String featureActionId, String paymentDate,
			String scheduledDate, String fileId, String reviewedBy, String initiatedBy, String companyId, String roleId,
			String status, String fromAccount, Double totalAmount, Double totalTransactions, String paymentStatus,
			String currency, String bulkType, String updateReference, String creditReference, String debitReference,
			String createdby, String modifiedby, String createdts, String lastmodifiedts, String synctimestamp,
			ErrorCodeEnum dbpErrorCode, String dbpErrMsg) {
		super();
		this.paymentrequestId = paymentrequestId;
		this.templateId = templateId;
		this.templateName = templateName;
		this.processingMode = processingMode;
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
		this.paymentStatus = paymentStatus;
		this.currency = currency;
		this.bulkType = bulkType;
		this.updateReference = updateReference;
		this.creditReference = creditReference;
		this.debitReference = debitReference;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.dbpErrorCode = dbpErrorCode;
		this.dbpErrMsg = dbpErrMsg;
	}

	public String getPaymentrequestId() {
		return paymentrequestId;
	}

	public void setPaymentrequestId(String paymentrequestId) {
		this.paymentrequestId = paymentrequestId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getProcessingMode() {
		return processingMode;
	}

	public void setProcessingMode(String processingMode) {
		this.processingMode = processingMode;
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
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getScheduledDate() {
		return scheduledDate;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalTransactions() {
		return totalTransactions;
	}

	public void setTotalTransactions(Double totalTransactions) {
		this.totalTransactions = totalTransactions;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBulkType() {
		return bulkType;
	}

	public void setBulkType(String bulkType) {
		this.bulkType = bulkType;
	}

	public String getUpdateReference() {
		return updateReference;
	}

	public void setUpdateReference(String updateReference) {
		this.updateReference = updateReference;
	}

	public String getCreditReference() {
		return creditReference;
	}

	public void setCreditReference(String creditReference) {
		this.creditReference = creditReference;
	}

	public String getDebitReference() {
		return debitReference;
	}

	public void setDebitReference(String debitReference) {
		this.debitReference = debitReference;
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
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bulkType == null) ? 0 : bulkType.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((creditReference == null) ? 0 : creditReference.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((debitReference == null) ? 0 : debitReference.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		result = prime * result + ((initiatedBy == null) ? 0 : initiatedBy.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((paymentDate == null) ? 0 : paymentDate.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((paymentStatus == null) ? 0 : paymentStatus.hashCode());
		result = prime * result + ((paymentrequestId == null) ? 0 : paymentrequestId.hashCode());
		result = prime * result + ((processingMode == null) ? 0 : processingMode.hashCode());
		result = prime * result + ((reviewedBy == null) ? 0 : reviewedBy.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
		result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
		result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
		result = prime * result + ((totalTransactions == null) ? 0 : totalTransactions.hashCode());
		result = prime * result + ((updateReference == null) ? 0 : updateReference.hashCode());
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
		BulkPaymentRequestDTO other = (BulkPaymentRequestDTO) obj;
		if (paymentrequestId == null) {
			if (other.paymentrequestId != null)
				return false;
		} else if (!paymentrequestId.equals(other.paymentrequestId))
			return false;
		return true;
	}

}
