package com.temenos.infinity.api.chequemanagement.dto;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.infinity.api.chequemanagement.constants.Constants;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalRequestDTO implements DBPDTO{
	
	private static final long serialVersionUID = 4974760643383086307L;
	
	private String requestId;
	private String featureName;
	private String featureActionId;
	private String limitGroupId;
	private String limitGroupName;
	
	@JsonAlias({"transactionId", "recordId", "transaction_id", "achFile_id", "serviceReqId"})
	private String transactionId;
	private String contractId;
	private String companyId;
	private String companyName;
	@JsonAlias({"accountId", "fromAccountNumber", "fromAccount","debitAccounts"})
	private String accountId;
	private String status;	
	
	@JsonAlias({"sentBy", "createdby", "fullName", "createdBy"})
	private String sentBy;
	@JsonAlias({"signatoryApprovalRequired"})
	private String amIApprover;
	private String amICreator;
	private String requiredApprovals;
	@JsonAlias({"currrentSignatoryApprovedCount"})
	private String receivedApprovals;
	private String actedByMeAlready;
	
	private String noOfBooks;

	private String address;
	
	@JsonAlias({"templateRequestTypeName","type"})
	private String requestType;
	
	@JsonAlias({"scheduledDate", "paymentDate", "effectiveDate"})
	private String processingDate;
	
	@JsonAlias({"sentDate", "createdts"})
	private String sentDate;
	
	private String approvalDate;
	
	@JsonAlias({"amount", "debitAmount","totalAmount"})
	private String amount;
	
	@JsonAlias({"payeeName", "toAccountNumber"})
	private String payee;
	
	@JsonAlias({"frequencyTypeId"})
	private String frequency;
	
	@JsonAlias({"numberOfRecurrences"})
	private String recurrence;
	private String reference;
	
	@JsonAlias({"customerId","coreCustomerId","partyId"})
	private String customerId;
	
	@JsonAlias({"creditAmount"})
	private String totalCreditAmount;
	
	@JsonAlias({"achFileName"})
	private String fileName;
	
	@JsonAlias({"customerName","coreCustomerName"})
	private String customerName;
	private String serviceCharge;
	private String convertedAmount;
	private String transactionAmount;
	private String transactionCurrency;
	private String featureActionName;
	
	private String fileType;
	@JsonAlias({"achFileFormatType_id", "templateRequestType_id", "fileTypeId"})
	private String fileTypeId;
	private String numberOfCredits;
	private String numberOfDebits;
	private String numberOfPrenotes;
	private String numberOfRecords;
	private String confirmationNumber;
	private String templateName;
	private String description;
	private String paymentId;
	private String totalTransactions;
	@JsonAlias({"batchMode"})
	private String processingMode;
	private String paidBy;
	private String swiftCode;
	private String fromAccountCurrency;
	
	
	public ApprovalRequestDTO() {}
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getFeatureActionId() {
		return featureActionId;
	}
	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}
	public String getLimitGroupId() {
		return limitGroupId;
	}
	public void setLimitGroupId(String limitGroupId) {
		this.limitGroupId = limitGroupId;
	}
	public String getLimitGroupName() {
		return limitGroupName;
	}
	public void setLimitGroupName(String limitGroupName) {
		this.limitGroupName = limitGroupName;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getSentBy() {
		return sentBy;
	}
	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
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
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getProcessingDate() {
		try {
			return HelperMethods.convertDateFormat(processingDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}
	public void setProcessingDate(String processingDate) {
		this.processingDate = processingDate;
	}
	public String getSentDate() {
		try {
			return HelperMethods.convertDateFormat(sentDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public String getApprovalDate() {
		try {
			return HelperMethods.convertDateFormat(approvalDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		try {
			this.amount = String.format("%.2f", new BigDecimal(amount));
		}catch (Exception e) {
			this.amount = amount;
		}
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getRecurrence() {
		return recurrence;
	}
	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(String serviceCharge) {
		if(StringUtils.isEmpty(serviceCharge))
			this.serviceCharge = "0.00";
		else
			this.serviceCharge = serviceCharge;
	}
	public String getConvertedAmount() {
		return convertedAmount;
	}
	public void setConvertedAmount(String convertedAmount) {
		if(StringUtils.isEmpty(convertedAmount))
			this.convertedAmount = "0.00";
		else
			this.convertedAmount = convertedAmount;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		if(StringUtils.isEmpty(transactionAmount))
			this.transactionAmount = this.getAmount();
		else
			this.transactionAmount = transactionAmount;
	}
	public String getTransactionCurrency() {
		return transactionCurrency;
	}
	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}
	public String getFeatureActionName() {
		return featureActionName;
	}
	public void setFeatureActionName(String featureActionName) {
		this.featureActionName = featureActionName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTotalCreditAmount() {
		return totalCreditAmount;
	}

	public void setTotalCreditAmount(String totalCreditAmount) {
		this.totalCreditAmount = totalCreditAmount;
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

	public String getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(String numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getTotalTransactions() {
		return totalTransactions;
	}

	public void setTotalTransactions(String totalTransactions) {
		this.totalTransactions = totalTransactions;
	}

	public String getProcessingMode() {
		return processingMode;
	}

	public void setProcessingMode(String processingMode) {
		this.processingMode = processingMode;
	}
	
	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	
	public String getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	public String getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(String noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(String fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}

	public ApprovalRequestDTO(String requestId, String featureName, String featureActionId, String limitGroupId,
			String limitGroupName, String transactionId, String contractId, String companyId, String companyName,
			String accountId, String status, String sentBy, String amIApprover, String amICreator,
			String requiredApprovals, String receivedApprovals, String actedByMeAlready, String noOfBooks,
			String address, String requestType, String processingDate, String sentDate, String approvalDate,
			String amount, String payee, String frequency, String recurrence, String reference, String customerId,
			String totalCreditAmount, String fileName, String customerName, String serviceCharge,
			String convertedAmount, String transactionAmount, String transactionCurrency, String featureActionName,
			String fileType, String fileTypeId, String numberOfCredits, String numberOfDebits, String numberOfPrenotes,
			String numberOfRecords, String confirmationNumber, String templateName, String description,
			String paymentId, String totalTransactions, String processingMode, String paidBy, String swiftCode,
			String fromAccountCurrency) {
		super();
		this.requestId = requestId;
		this.featureName = featureName;
		this.featureActionId = featureActionId;
		this.limitGroupId = limitGroupId;
		this.limitGroupName = limitGroupName;
		this.transactionId = transactionId;
		this.contractId = contractId;
		this.companyId = companyId;
		this.companyName = companyName;
		this.accountId = accountId;
		this.status = status;
		this.sentBy = sentBy;
		this.amIApprover = amIApprover;
		this.amICreator = amICreator;
		this.requiredApprovals = requiredApprovals;
		this.receivedApprovals = receivedApprovals;
		this.actedByMeAlready = actedByMeAlready;
		this.noOfBooks = noOfBooks;
		this.address = address;
		this.requestType = requestType;
		this.processingDate = processingDate;
		this.sentDate = sentDate;
		this.approvalDate = approvalDate;
		this.amount = amount;
		this.payee = payee;
		this.frequency = frequency;
		this.recurrence = recurrence;
		this.reference = reference;
		this.customerId = customerId;
		this.totalCreditAmount = totalCreditAmount;
		this.fileName = fileName;
		this.customerName = customerName;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.transactionAmount = transactionAmount;
		this.transactionCurrency = transactionCurrency;
		this.featureActionName = featureActionName;
		this.fileType = fileType;
		this.fileTypeId = fileTypeId;
		this.numberOfCredits = numberOfCredits;
		this.numberOfDebits = numberOfDebits;
		this.numberOfPrenotes = numberOfPrenotes;
		this.numberOfRecords = numberOfRecords;
		this.confirmationNumber = confirmationNumber;
		this.templateName = templateName;
		this.description = description;
		this.paymentId = paymentId;
		this.totalTransactions = totalTransactions;
		this.processingMode = processingMode;
		this.paidBy = paidBy;
		this.swiftCode = swiftCode;
		this.fromAccountCurrency = fromAccountCurrency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((actedByMeAlready == null) ? 0 : actedByMeAlready.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((approvalDate == null) ? 0 : approvalDate.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((featureActionName == null) ? 0 : featureActionName.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((fileTypeId == null) ? 0 : fileTypeId.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((fromAccountCurrency == null) ? 0 : fromAccountCurrency.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((limitGroupName == null) ? 0 : limitGroupName.hashCode());
		result = prime * result + ((noOfBooks == null) ? 0 : noOfBooks.hashCode());
		result = prime * result + ((numberOfCredits == null) ? 0 : numberOfCredits.hashCode());
		result = prime * result + ((numberOfDebits == null) ? 0 : numberOfDebits.hashCode());
		result = prime * result + ((numberOfPrenotes == null) ? 0 : numberOfPrenotes.hashCode());
		result = prime * result + ((numberOfRecords == null) ? 0 : numberOfRecords.hashCode());
		result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
		result = prime * result + ((payee == null) ? 0 : payee.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((processingDate == null) ? 0 : processingDate.hashCode());
		result = prime * result + ((processingMode == null) ? 0 : processingMode.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((recurrence == null) ? 0 : recurrence.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + ((sentBy == null) ? 0 : sentBy.hashCode());
		result = prime * result + ((sentDate == null) ? 0 : sentDate.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
		result = prime * result + ((totalCreditAmount == null) ? 0 : totalCreditAmount.hashCode());
		result = prime * result + ((totalTransactions == null) ? 0 : totalTransactions.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
		ApprovalRequestDTO other = (ApprovalRequestDTO) obj;
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
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
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
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (approvalDate == null) {
			if (other.approvalDate != null)
				return false;
		} else if (!approvalDate.equals(other.approvalDate))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (confirmationNumber == null) {
			if (other.confirmationNumber != null)
				return false;
		} else if (!confirmationNumber.equals(other.confirmationNumber))
			return false;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
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
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (featureActionId == null) {
			if (other.featureActionId != null)
				return false;
		} else if (!featureActionId.equals(other.featureActionId))
			return false;
		if (featureActionName == null) {
			if (other.featureActionName != null)
				return false;
		} else if (!featureActionName.equals(other.featureActionName))
			return false;
		if (featureName == null) {
			if (other.featureName != null)
				return false;
		} else if (!featureName.equals(other.featureName))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (fileType == null) {
			if (other.fileType != null)
				return false;
		} else if (!fileType.equals(other.fileType))
			return false;
		if (fileTypeId == null) {
			if (other.fileTypeId != null)
				return false;
		} else if (!fileTypeId.equals(other.fileTypeId))
			return false;
		if (frequency == null) {
			if (other.frequency != null)
				return false;
		} else if (!frequency.equals(other.frequency))
			return false;
		if (fromAccountCurrency == null) {
			if (other.fromAccountCurrency != null)
				return false;
		} else if (!fromAccountCurrency.equals(other.fromAccountCurrency))
			return false;
		if (limitGroupId == null) {
			if (other.limitGroupId != null)
				return false;
		} else if (!limitGroupId.equals(other.limitGroupId))
			return false;
		if (limitGroupName == null) {
			if (other.limitGroupName != null)
				return false;
		} else if (!limitGroupName.equals(other.limitGroupName))
			return false;
		if (noOfBooks == null) {
			if (other.noOfBooks != null)
				return false;
		} else if (!noOfBooks.equals(other.noOfBooks))
			return false;
		if (numberOfCredits == null) {
			if (other.numberOfCredits != null)
				return false;
		} else if (!numberOfCredits.equals(other.numberOfCredits))
			return false;
		if (numberOfDebits == null) {
			if (other.numberOfDebits != null)
				return false;
		} else if (!numberOfDebits.equals(other.numberOfDebits))
			return false;
		if (numberOfPrenotes == null) {
			if (other.numberOfPrenotes != null)
				return false;
		} else if (!numberOfPrenotes.equals(other.numberOfPrenotes))
			return false;
		if (numberOfRecords == null) {
			if (other.numberOfRecords != null)
				return false;
		} else if (!numberOfRecords.equals(other.numberOfRecords))
			return false;
		if (paidBy == null) {
			if (other.paidBy != null)
				return false;
		} else if (!paidBy.equals(other.paidBy))
			return false;
		if (payee == null) {
			if (other.payee != null)
				return false;
		} else if (!payee.equals(other.payee))
			return false;
		if (paymentId == null) {
			if (other.paymentId != null)
				return false;
		} else if (!paymentId.equals(other.paymentId))
			return false;
		if (processingDate == null) {
			if (other.processingDate != null)
				return false;
		} else if (!processingDate.equals(other.processingDate))
			return false;
		if (processingMode == null) {
			if (other.processingMode != null)
				return false;
		} else if (!processingMode.equals(other.processingMode))
			return false;
		if (receivedApprovals == null) {
			if (other.receivedApprovals != null)
				return false;
		} else if (!receivedApprovals.equals(other.receivedApprovals))
			return false;
		if (recurrence == null) {
			if (other.recurrence != null)
				return false;
		} else if (!recurrence.equals(other.recurrence))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (requestType == null) {
			if (other.requestType != null)
				return false;
		} else if (!requestType.equals(other.requestType))
			return false;
		if (requiredApprovals == null) {
			if (other.requiredApprovals != null)
				return false;
		} else if (!requiredApprovals.equals(other.requiredApprovals))
			return false;
		if (sentBy == null) {
			if (other.sentBy != null)
				return false;
		} else if (!sentBy.equals(other.sentBy))
			return false;
		if (sentDate == null) {
			if (other.sentDate != null)
				return false;
		} else if (!sentDate.equals(other.sentDate))
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
		if (swiftCode == null) {
			if (other.swiftCode != null)
				return false;
		} else if (!swiftCode.equals(other.swiftCode))
			return false;
		if (templateName == null) {
			if (other.templateName != null)
				return false;
		} else if (!templateName.equals(other.templateName))
			return false;
		if (totalCreditAmount == null) {
			if (other.totalCreditAmount != null)
				return false;
		} else if (!totalCreditAmount.equals(other.totalCreditAmount))
			return false;
		if (totalTransactions == null) {
			if (other.totalTransactions != null)
				return false;
		} else if (!totalTransactions.equals(other.totalTransactions))
			return false;
		if (transactionAmount == null) {
			if (other.transactionAmount != null)
				return false;
		} else if (!transactionAmount.equals(other.transactionAmount))
			return false;
		if (transactionCurrency == null) {
			if (other.transactionCurrency != null)
				return false;
		} else if (!transactionCurrency.equals(other.transactionCurrency))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}

}
