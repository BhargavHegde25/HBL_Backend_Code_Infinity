package com.temenos.dbx.product.achservices.dto;

import java.text.ParseException;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;

/**
 * @author KH2638
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHFileDTO implements DBPDTO{

	private static final long serialVersionUID = 886403890008328614L;

	private String achFile_id;
	private String achFileName;
	private String featureActionId;
	private double debitAmount;
	private String createdts;
	private String requestType;
	private String numberOfCredits;
	private String numberOfDebits;
	private String numberOfPrenotes;
	private String requestId;
	private String createdby;
	private String contents;
	private String fileSize;
	private double creditAmount;
	private String numberOfRecords;
	private String achFileFormatType_id;
	private String status;
	private String companyId;
	private String roleId;
	private String actedBy;
	private String updatedts;
	private String confirmationNumber;
	private String achFileFormatType;
	private String companyName;
	private String userName;
	private String receivedApprovals;
	private String requiredApprovals;
	private String debitAccounts;
	private String approvalAccounts;
	private String serviceCharge;
	private String transactionAmount;
	private String transactionCurrency;
	
	private String amICreator;
	private String amIApprover;
	
	@JsonProperty("Status")
	private String statusValue;
	
	@JsonAlias({"ReferenceID", "referenceID", "referenceId"})
	private String referenceID;
	@JsonProperty("EffectiveDate")
	private String effectiveDate;
	
	private ErrorCodeEnum dbpErrorCode;
	
	@JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	
	private String dbpErrCode;
	
	List<ACHFileRecordDTO> fileRecords;

	public ACHFileDTO() {
		super();
	}

	public ACHFileDTO(String achFile_id, String achFileName, String featureActionId, double debitAmount,
			String createdts, String requestType, String numberOfCredits, String numberOfDebits,
			String numberOfPrenotes, String requestId, String createdby, String contents, String fileSize,
			double creditAmount, String numberOfRecords, String achFileFormatType_id, String status, String companyId, String roleId,
			String actedBy, String updatedts, String confirmationNumber, String achFileFormatType, String companyName,
			String userName, String receivedApprovals, String requiredApprovals, String debitAccounts,
			String statusValue, String referenceID, String effectiveDate, ErrorCodeEnum dbpErrorCode,
			String approvalAccounts, List<ACHFileRecordDTO> fileRecords, String amICreator, String amIApprover, String dbpErrMsg,  String dbpErrCode) {
		super();
		this.achFile_id = achFile_id;
		this.achFileName = achFileName;
		this.featureActionId = featureActionId;
		this.debitAmount = debitAmount;
		this.createdts = createdts;
		this.requestType = requestType;
		this.numberOfCredits = numberOfCredits;
		this.numberOfDebits = numberOfDebits;
		this.numberOfPrenotes = numberOfPrenotes;
		this.requestId = requestId;
		this.createdby = createdby;
		this.contents = contents;
		this.fileSize = fileSize;
		this.creditAmount = creditAmount;
		this.numberOfRecords = numberOfRecords;
		this.achFileFormatType_id = achFileFormatType_id;
		this.status = status;
		this.companyId = companyId;
		this.roleId = roleId;
		this.actedBy = actedBy;
		this.updatedts = updatedts;
		this.confirmationNumber = confirmationNumber;
		this.achFileFormatType = achFileFormatType;
		this.companyName = companyName;
		this.userName = userName;
		this.receivedApprovals = receivedApprovals;
		this.requiredApprovals = requiredApprovals;
		this.debitAccounts = debitAccounts;
		this.statusValue = statusValue;
		this.referenceID = referenceID;
		this.effectiveDate = effectiveDate;
		this.dbpErrorCode = dbpErrorCode;
		this.fileRecords = fileRecords;
		this.approvalAccounts = approvalAccounts;
		this.amIApprover = amIApprover;
		this.amICreator = amICreator;
		this.dbpErrMsg = dbpErrMsg;
		this.dbpErrCode = dbpErrCode;
	}
	
	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}
	
	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAmICreator() {
		return amICreator;
	}

	public void setAmICreator(String amICreator) {
		this.amICreator = amICreator;
	}

	public String getAmIApprover() {
		return amIApprover;
	}

	public void setAmIApprover(String amIApprover) {
		this.amIApprover = amIApprover;
	}

	public String getAchFile_id() {
		return achFile_id;
	}

	public void setAchFile_id(String achFile_id) {
		this.achFile_id = achFile_id;
	}

	public String getAchFileName() {
		return achFileName;
	}

	public void setAchFileName(String achFileName) {
		this.achFileName = achFileName;
	}

	public String getFeatureActionId() {
		return featureActionId;
	}

	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}

	public double getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(double debitAmount) {
		this.debitAmount = debitAmount;
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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getNumberOfCredits() {
		return numberOfCredits;
	}

	public void setNumberOfCredits(String numberOfCredits) {
		this.numberOfCredits = numberOfCredits;
	}

	public String getNumberOfDebits() {
		return numberOfDebits;
	}

	public void setNumberOfDebits(String numberOfDebits) {
		this.numberOfDebits = numberOfDebits;
	}

	public String getNumberOfPrenotes() {
		return numberOfPrenotes;
	}

	public void setNumberOfPrenotes(String numberOfPrenotes) {
		this.numberOfPrenotes = numberOfPrenotes;
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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(String numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public String getAchFileFormatType_id() {
		return achFileFormatType_id;
	}

	public void setAchFileFormatType_id(String achFileFormatType_id) {
		this.achFileFormatType_id = achFileFormatType_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getActedBy() {
		return actedBy;
	}

	public void setActedBy(String actedBy) {
		this.actedBy = actedBy;
	}

	public String getUpdatedts() {
		try {
			return HelperMethods.convertDateFormat(updatedts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setUpdatedts(String updatedts) {
		this.updatedts = updatedts;
	}
	
	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getAchFileFormatType() {
		return achFileFormatType;
	}

	public void setAchFileFormatType(String achFileFormatType) {
		this.achFileFormatType = achFileFormatType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReceivedApprovals() {
		return receivedApprovals;
	}

	public void setReceivedApprovals(String receivedApprovals) {
		this.receivedApprovals = receivedApprovals;
	}

	public String getRequiredApprovals() {
		return requiredApprovals;
	}

	public void setRequiredApprovals(String requiredApprovals) {
		this.requiredApprovals = requiredApprovals;
	}

	public String getDebitAccounts() {
		return debitAccounts;
	}

	public void setDebitAccounts(String debitAccounts) {
		this.debitAccounts = debitAccounts;
	}
	
	public String getApprovalAccounts() {
		return approvalAccounts;
	}

	public void setApprovalAccounts(String approvalAccounts) {
		this.approvalAccounts = approvalAccounts;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getReferenceID() {
		return referenceID;
	}

	public void setReferenceID(String referenceID) {
		this.referenceID = referenceID;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<ACHFileRecordDTO> getFileRecords() {
		return fileRecords;
	}

	public void setFileRecords(List<ACHFileRecordDTO> fileRecords) {
		this.fileRecords = fileRecords;
	}
	
	public ErrorCodeEnum getDbpErrorCode() {
		return dbpErrorCode;
	}

	public void setDbpErrorCode(ErrorCodeEnum dbpErrorCode) {
		this.dbpErrorCode = dbpErrorCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((achFileFormatType == null) ? 0 : achFileFormatType.hashCode());
		result = prime * result + ((achFileFormatType_id == null) ? 0 : achFileFormatType_id.hashCode());
		result = prime * result + ((achFileName == null) ? 0 : achFileName.hashCode());
		result = prime * result + ((achFile_id == null) ? 0 : achFile_id.hashCode());
		result = prime * result + ((actedBy == null) ? 0 : actedBy.hashCode());
		result = prime * result + ((approvalAccounts == null) ? 0 : approvalAccounts.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((contents == null) ? 0 : contents.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		long temp;
		temp = Double.doubleToLongBits(creditAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((debitAccounts == null) ? 0 : debitAccounts.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		temp = Double.doubleToLongBits(debitAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((fileRecords == null) ? 0 : fileRecords.hashCode());
		result = prime * result + ((fileSize == null) ? 0 : fileSize.hashCode());
		result = prime * result + ((numberOfCredits == null) ? 0 : numberOfCredits.hashCode());
		result = prime * result + ((numberOfDebits == null) ? 0 : numberOfDebits.hashCode());
		result = prime * result + ((numberOfPrenotes == null) ? 0 : numberOfPrenotes.hashCode());
		result = prime * result + ((numberOfRecords == null) ? 0 : numberOfRecords.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((referenceID == null) ? 0 : referenceID.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((statusValue == null) ? 0 : statusValue.hashCode());
		result = prime * result + ((updatedts == null) ? 0 : updatedts.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
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
		ACHFileDTO other = (ACHFileDTO) obj;
		if (achFile_id == null) {
			if (other.achFile_id != null)
				return false;
		} else if (!achFile_id.equals(other.achFile_id))
			return false;
		return true;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}
}