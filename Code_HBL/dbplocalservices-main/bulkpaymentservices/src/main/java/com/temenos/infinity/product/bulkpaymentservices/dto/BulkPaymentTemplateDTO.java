package com.temenos.infinity.product.bulkpaymentservices.dto;

import java.text.ParseException;

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
public class BulkPaymentTemplateDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3401974764978005259L;

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
	
	@JsonAlias({"userName"})
	private String initiatedBy;
	private String companyId;
	private String roleId;
	private String status;
	private String fromAccount;
	private Double totalAmount;
	private Double totalTransactions;
	private String requestId;
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
	private String sortByParam;
	private String sortOrder;
	private String searchString;
	private String pageSize;
	private String pageOffset;
	private String filterByParam;
	private String filterByValue;
	
	private String POs;
	private String totalBeneficiaries;

	private ErrorCodeEnum dbpErrorCode;
	@JsonAlias({ "errorMessage", "errmsg", "ErrorMessage" })
	private String dbpErrMsg;

	public BulkPaymentTemplateDTO() {
		super();
	}

	public BulkPaymentTemplateDTO(String paymentrequestId, String templateId, String templateName, String processingMode,
			String confirmationNumber, String paymentId, String description, String featureActionId, String paymentDate,
			String scheduledDate, String fileId, String reviewedBy, String initiatedBy, String companyId, String roleId,
			String status, String fromAccount, Double totalAmount, Double totalTransactions, String requestId,
			String paymentStatus, String currency, String bulkType, String updateReference, String creditReference,
			String debitReference, String createdby, String modifiedby, String createdts, String lastmodifiedts,
			String synctimestamp, ErrorCodeEnum dbpErrorCode, String dbpErrMsg,String sortByParam, String sortOrder, String searchString,
			String pageSize, String pageOffset, String filterByParam, String filterByValue, String POs,String totalBeneficiaries) {
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
		this.requestId = requestId;
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
		this.POs = POs;
		this.totalBeneficiaries = totalBeneficiaries;
		this.dbpErrorCode = dbpErrorCode;
		this.dbpErrMsg = dbpErrMsg;
		this.sortByParam = sortByParam;
		this.sortOrder = sortOrder;
		this.searchString = searchString;
		this.pageSize = pageSize;
		this.pageOffset = pageOffset;
		this.filterByParam = filterByParam;
		this.filterByValue = filterByValue;
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
	
	public String getTotalBeneficiaries() {
		return totalBeneficiaries;
	}

	public void setTotalBeneficiaries(String totalBeneficiaries) {
		this.totalBeneficiaries = totalBeneficiaries;
	}

	public String getPOs() {
		return POs;
	}

	public void setPOs(String POs) {
		this.POs = POs;
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
		try {
			return HelperMethods.convertDateFormat(paymentDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getScheduledDate() {
		try {
			return HelperMethods.convertDateFormat(scheduledDate, Constants.TIMESTAMP_FORMAT);
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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
		try {
			return HelperMethods.convertDateFormat(createdts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getLastmodifiedts() {
		try {
			return HelperMethods.convertDateFormat(lastmodifiedts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getSynctimestamp() {
		try {
			return HelperMethods.convertDateFormat(synctimestamp, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
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
		result = prime * result + ((processingMode == null) ? 0 : processingMode.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((reviewedBy == null) ? 0 : reviewedBy.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
		result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
		result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
		result = prime * result + ((POs == null) ? 0 : POs.hashCode());
		result = prime * result + ((totalTransactions == null) ? 0 : totalTransactions.hashCode());
		result = prime * result + ((updateReference == null) ? 0 : updateReference.hashCode());
		result = prime * result + ((totalBeneficiaries == null) ? 0 : totalBeneficiaries.hashCode());
		result = prime * result + ((searchString == null) ? 0 : searchString.hashCode());
		result = prime * result + ((sortByParam == null) ? 0 : sortByParam.hashCode());
		result = prime * result + ((sortOrder == null) ? 0 : sortOrder.hashCode());
		result = prime * result + ((pageOffset == null) ? 0 : pageOffset.hashCode());
		result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
		result = prime * result + ((filterByParam == null) ? 0 : filterByParam.hashCode());
		result = prime * result + ((filterByValue == null) ? 0 : filterByValue.hashCode());
		result = prime * result + ((paymentrequestId == null) ? 0 : paymentrequestId.hashCode());
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
		BulkPaymentTemplateDTO other = (BulkPaymentTemplateDTO) obj;
		if (templateId == null) {
			if (other.templateId != null)
				return false;
		} else if (!templateId.equals(other.templateId))
			return false;
		return true;
	}

}
