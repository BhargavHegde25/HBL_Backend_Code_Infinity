package com.temenos.dbx.product.bulkpaymentservices.dto;

import java.text.ParseException;
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
public class BulkPaymentFileDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7402234489127740356L;
	
	private String fileId;
	private String confirmationNumber;
	private String fileName;
	private String description;
	private String featureActionId;
	
	@JsonAlias({"userName"})
	private String uploadedBy;
	private String companyId;
	private String roleId;
	private String uploadedDate;
	private String status;
	private String fromAccount;
	private double totalAmount;
	private double totalTransactions;
	private String requestId;
	private String content;
	private String fileSize;
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
	private String amIAprover;
	private String amICreator;
	private String recievedApprovals;
	private String requiredApprovals;
	private String approvers;
	private String batchMode;	
	private String paymentDate;
	private String requestStatus;
	private String uploadTypeId;
	private String fileType;
	private String sysGeneratedFileName;
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg", "ErrorMessage"})
	private String dbpErrMsg;
	
	
	List<BulkPaymentPODTO> paymentOrders;
	BulkPaymentRecordDTO bulkPaymentRecord;
	
	public BulkPaymentFileDTO() {
		super();
	}
	
	public BulkPaymentFileDTO(String fileId, String confirmationNumber, String fileName, String description,
			String featureActionId, String uploadedBy, String companyId, String roleId, String uploadedDate,
			String status, String fromAccount, double totalAmount, double totalTransactions, String requestId,
			String content, String fileSize, String createdby, String modifiedby, String createdts,
			String lastmodifiedts, String synctimestamp, String sortByParam, String sortOrder, String searchString,
			String pageSize, String pageOffset, String filterByParam, String filterByValue, String amIAprover,
			String amICreator, String recievedApprovals, String requiredApprovals, String approvers, String batchMode,
			String paymentDate,ErrorCodeEnum DbpErrorCode, String DbpErrMsg, List<BulkPaymentPODTO> paymentOrders,
			String sysGeneratedFileName, BulkPaymentRecordDTO bulkPaymentRecord) {
		super();
		this.fileId = fileId;
		this.confirmationNumber = confirmationNumber;
		this.fileName = fileName;
		this.description = description;
		this.featureActionId = featureActionId;
		this.uploadedBy = uploadedBy;
		this.companyId = companyId;
		this.roleId = roleId;
		this.uploadedDate = uploadedDate;
		this.status = status;
		this.fromAccount = fromAccount;
		this.totalAmount = totalAmount;
		this.totalTransactions = totalTransactions;
		this.requestId = requestId;
		this.content = content;
		this.fileSize = fileSize;
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
		this.amIAprover = amIAprover;
		this.amICreator = amICreator;
		this.recievedApprovals = recievedApprovals;
		this.requiredApprovals = requiredApprovals;
		this.approvers = approvers;
		this.batchMode = batchMode;
		this.paymentDate = paymentDate;
		this.paymentOrders = paymentOrders;
		this.sysGeneratedFileName = sysGeneratedFileName; 
		this.bulkPaymentRecord = bulkPaymentRecord;
        this.dbpErrorCode = DbpErrorCode;
        this.dbpErrMsg = DbpErrMsg;
	}
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
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

	public String getUploadedDate() {		
		try {
			return HelperMethods.changeDateFormat(uploadedDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
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

	public String getAmIAprover() {
		return amIAprover;
	}

	public void setAmIAprover(String amIAprover) {
		this.amIAprover = amIAprover;
	}

	public String getAmICreator() {
		return amICreator;
	}

	public void setAmICreator(String amICreator) {
		this.amICreator = amICreator;
	}

	public String getRecievedApprovals() {
		return recievedApprovals;
	}

	public void setRecievedApprovals(String recievedApprovals) {
		this.recievedApprovals = recievedApprovals;
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
	
	public String getBatchMode() {
		return batchMode;
	}

	public void setBatchMode(String batchMode) {
		this.batchMode = batchMode;
	}

	public String getSysGeneratedFileName() {
		return sysGeneratedFileName;
	}

	public void setSysGeneratedFileName(String sysGeneratedFileName) {
		this.sysGeneratedFileName = sysGeneratedFileName;
	}

	public List<BulkPaymentPODTO> getPaymentOrders() {
		return paymentOrders;
	}

	public void setPaymentOrders(List<BulkPaymentPODTO> paymentOrders) {
		this.paymentOrders = paymentOrders;
	}
	
	public BulkPaymentRecordDTO getBulkPaymentRecord() {
		return bulkPaymentRecord;
	}

	public void setBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecord) {
		this.bulkPaymentRecord = bulkPaymentRecord;
	}
	
	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
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
		result = prime * result + ((amIAprover == null) ? 0 : amIAprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((approvers == null) ? 0 : approvers.hashCode());
		result = prime * result + ((batchMode == null) ? 0 : batchMode.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((fileSize == null) ? 0 : fileSize.hashCode());
		result = prime * result + ((filterByParam == null) ? 0 : filterByParam.hashCode());
		result = prime * result + ((filterByValue == null) ? 0 : filterByValue.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((pageOffset == null) ? 0 : pageOffset.hashCode());
		result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
		result = prime * result + ((recievedApprovals == null) ? 0 : recievedApprovals.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((searchString == null) ? 0 : searchString.hashCode());
		result = prime * result + ((sortByParam == null) ? 0 : sortByParam.hashCode());
		result = prime * result + ((sortOrder == null) ? 0 : sortOrder.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((requestStatus == null) ? 0 : requestStatus.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
        result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
        result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
        result = prime * result + ((sysGeneratedFileName == null) ? 0 : sysGeneratedFileName.hashCode());
		long temp;
        temp = Double.doubleToLongBits(totalAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalTransactions);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((uploadedBy == null) ? 0 : uploadedBy.hashCode());
		result = prime * result + ((uploadedDate == null) ? 0 : uploadedDate.hashCode());
		result = prime * result + ((paymentOrders == null) ? 0 : paymentOrders.hashCode());
		result = prime * result + ((bulkPaymentRecord == null) ? 0 : bulkPaymentRecord.hashCode());
		result = prime * result + ((paymentDate == null) ? 0 : paymentDate.hashCode());
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
		BulkPaymentFileDTO other = (BulkPaymentFileDTO) obj;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		return true;
	}


	public String getUploadTypeId() {
		return uploadTypeId;
	}

	public void setUploadTypeId(String uploadTypeId) {
		this.uploadTypeId = uploadTypeId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}