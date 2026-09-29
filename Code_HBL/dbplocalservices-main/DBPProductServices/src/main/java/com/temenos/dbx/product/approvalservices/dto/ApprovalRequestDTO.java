package com.temenos.dbx.product.approvalservices.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.constants.Constants;
import com.dbp.core.util.JSONUtils;

import net.minidev.json.JSONObject;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalRequestDTO implements DBPDTO{

	private static final long serialVersionUID = 4974760643383086307L;
	private String compositeRequestIds;
	private String assocStatus;
	private String requestId;
	private String assocRequestId;
	private String featureName;
	private String featureActionId;
	private String limitGroupId;
	private String limitGroupName;
	
	@JsonAlias({"transactionId", "recordId", "transaction_id", "achFile_id"})
	private String transactionId;
	private String contractId;
	private String companyId;
	private String companyName;
	@JsonAlias({"accountId", "fromAccountNumber", "fromAccount","debitAccounts"})
	private String accountId;
	private String status;	
	
	@JsonAlias({"sentBy", "createdby", "fullName", "createdBy"})
	private String sentBy;
	private String amIApprover;
	private String amICreator;
	private String requiredApprovals;
	private String receivedApprovals;
	private String actedByMeAlready;
	private String noOfBooks;
	private String address;
	@JsonAlias({"templateRequestTypeName"})
	private String requestType;
	
	@JsonAlias({"scheduledDate", "paymentDate", "effectiveDate"})
	private String processingDate;
	
	@JsonAlias({"sentDate", "createdts"})
	private String sentDate;
	
	private String approvalDate;
	
	@JsonAlias({"amount", "debitAmount","totalAmount"})
	private String amount;
	
	@JsonAlias({"beneficiaryName", "payeeName", "toAccountNumber"})
	private String payee;
	
	@JsonAlias({"frequencyTypeId"})
	private String frequency;
	
	@JsonAlias({"numberOfRecurrences"})
	private String recurrence;
	private String reference;
	
	@JsonAlias({"customerId","coreCustomerId"})
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
	private String companyLegalUnit;
	private String templateName;
	private String description;
	private String paymentId;
	private String totalTransactions;
	@JsonAlias({"batchMode"})
	private String processingMode;
	private String paidBy;
	private String swiftCode;
	private List<UploadedAttachments> fileNames;
	
	private String charges;
	private String indicativeRate;
    private String fromAccountCurrency;
    private String creditValueDate;
    private String totalDebitAmount;
    private String toAccountName;
    private String paymentType;
    private String frequencyEndDate;
    private String beneficiaryBankAddress;
    
    private String isGroupMatrix;
    
  //tradefinance loc details
	
  	private String lcReferenceNo;
  	private double lcAmount;
  	private String lcCurrency;

  	private String availableWith1;
  	private String availableWith2;
  	private String availableWith3;
  	private String availableWith4;
  	private String issueDate;
  	private String expiryDate;
  	private String expiryPlace;

  	private String beneficiaryName;
  	private String beneficiaryAddressLine1;
  	private String beneficiaryAddressLine2;
  	private String beneficiaryPostCode;
  	private String beneficiaryCountry;
  	private String beneficiaryCity;
  	private String beneficiaryState;
  	private String beneficiaryBank;
  	private String beneficiaryBankAdressLine1;
  	private String beneficiaryBankAdressLine2;

  	private String beneficiaryBankCity;
  	private String beneficiaryBankState;

  	private String partialShipments;
  	private String incoTerms;

  	private String confirmationInstruction;
  	private String transferable;
  	private String standByLC;
  	private String isDraft;
  	private String additionalPayableCurrency;
  	private String code;
  	private String msg;
  	private String flowType;
  	private String id;

  	private String ErrorCode;
  	private String ErrorMessage;
  	private String srmsReqOrderID;
  	private String lcCreatedOn;
  	private boolean referenceNomatch;
  	private String ErrorCodeSRMSmatch;
  	private String ErrorMsgSRMSmatch;

  	private String screenNumber;
  	private String draftCount;
  	private String draftAmount;
  	private String deletedCount;
  	private String deletedAmount;
  	private String selfApprovedCount;
  	private String selfApprovedAmount;
  	private String totalCount;
  	private String totalAmount;
  	private String signatoryApprovalRequired;

	  // beneficiary record variables
	private String accountNumber;
	private String accountType;
	private String bankName;
	private String countryName;
	private String createdOn;
	private String firstName;
	private String lastName;
	private String nickName;
	private String isInternationalAccount;
	private String isSameBankAccount;
	private String isVerified;
	private String notes;
	private String routingNumber;

	@JsonAlias({"IBAN"})
	private String iban;
	private String sortCode;
	private String phoneCountryCode;
	private String phoneNumber;
	private String phoneExtension;
	private String isBusinessPayee;

	@JsonAlias({"id", "Id", "ID"})
	private String payeeId;
	private Object cif;
	private Integer noOfCustomersLinked;
	private String phone;
	private String email;
	private String country;
	private String city;
	private String zipcode;
	private String addressLine1;
	private String addressLine2;
	private Object oldInfo;
	private String additionalMeta;
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
  	
	public ApprovalRequestDTO() {}
	
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

	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
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

	public List<UploadedAttachments> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<UploadedAttachments> fileNames) {
		this.fileNames = fileNames;
	}

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

	public String getIndicativeRate() {
		return indicativeRate;
	}

	public void setIndicativeRate(String indicativeRate) {
		this.indicativeRate = indicativeRate;
	}

	public String getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(String fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}
	
	public String getCreditValueDate() {
		return creditValueDate;
	}

	public void setCreditValueDate(String creditValueDate) {
		this.creditValueDate = creditValueDate;
	}

	public String getTotalDebitAmount() {
		return totalDebitAmount;
	}

	public void setTotalDebitAmount(String totalDebitAmount) {
		this.totalDebitAmount = totalDebitAmount;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getFrequencyEndDate() {
		try {
			return HelperMethods.convertDateFormat(frequencyEndDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setFrequencyEndDate(String frequencyEndDate) {
		this.frequencyEndDate = frequencyEndDate;
	}

	public String getBeneficiaryBankAddress() {
		return beneficiaryBankAddress;
	}

	public void setBeneficiaryBankAddress(String beneficiaryBankAddress) {
		this.beneficiaryBankAddress = beneficiaryBankAddress;
	}

	public String getIsGroupMatrix() {
		return isGroupMatrix;
	}

	public void setIsGroupMatrix(String isGroupMatrix) {
		this.isGroupMatrix = isGroupMatrix;
	}
	
	public String getLcReferenceNo() {
		return lcReferenceNo;
	}

	public void setLcReferenceNo(String lcReferenceNo) {
		this.lcReferenceNo = lcReferenceNo;
	}

	public double getLcAmount() {
		return lcAmount;
	}

	public void setLcAmount(double lcAmount) {
		this.lcAmount = lcAmount;
	}

	public String getLcCurrency() {
		return lcCurrency;
	}

	public void setLcCurrency(String lcCurrency) {
		this.lcCurrency = lcCurrency;
	}

	public String getAvailableWith1() {
		return availableWith1;
	}

	public void setAvailableWith1(String availableWith1) {
		this.availableWith1 = availableWith1;
	}

	public String getAvailableWith2() {
		return availableWith2;
	}

	public void setAvailableWith2(String availableWith2) {
		this.availableWith2 = availableWith2;
	}

	public String getAvailableWith3() {
		return availableWith3;
	}

	public void setAvailableWith3(String availableWith3) {
		this.availableWith3 = availableWith3;
	}

	public String getAvailableWith4() {
		return availableWith4;
	}

	public void setAvailableWith4(String availableWith4) {
		this.availableWith4 = availableWith4;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getExpiryPlace() {
		return expiryPlace;
	}

	public void setExpiryPlace(String expiryPlace) {
		this.expiryPlace = expiryPlace;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryAddressLine1() {
		return beneficiaryAddressLine1;
	}

	public void setBeneficiaryAddressLine1(String beneficiaryAddressLine1) {
		this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
	}

	public String getBeneficiaryAddressLine2() {
		return beneficiaryAddressLine2;
	}

	public void setBeneficiaryAddressLine2(String beneficiaryAddressLine2) {
		this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
	}

	public String getBeneficiaryPostCode() {
		return beneficiaryPostCode;
	}

	public void setBeneficiaryPostCode(String beneficiaryPostCode) {
		this.beneficiaryPostCode = beneficiaryPostCode;
	}

	public String getBeneficiaryCountry() {
		return beneficiaryCountry;
	}

	public void setBeneficiaryCountry(String beneficiaryCountry) {
		this.beneficiaryCountry = beneficiaryCountry;
	}

	public String getBeneficiaryCity() {
		return beneficiaryCity;
	}

	public void setBeneficiaryCity(String beneficiaryCity) {
		this.beneficiaryCity = beneficiaryCity;
	}

	public String getBeneficiaryState() {
		return beneficiaryState;
	}

	public void setBeneficiaryState(String beneficiaryState) {
		this.beneficiaryState = beneficiaryState;
	}

	public String getBeneficiaryBank() {
		return beneficiaryBank;
	}

	public void setBeneficiaryBank(String beneficiaryBank) {
		this.beneficiaryBank = beneficiaryBank;
	}

	public String getBeneficiaryBankAdressLine1() {
		return beneficiaryBankAdressLine1;
	}

	public void setBeneficiaryBankAdressLine1(String beneficiaryBankAdressLine1) {
		this.beneficiaryBankAdressLine1 = beneficiaryBankAdressLine1;
	}

	public String getBeneficiaryBankAdressLine2() {
		return beneficiaryBankAdressLine2;
	}

	public void setBeneficiaryBankAdressLine2(String beneficiaryBankAdressLine2) {
		this.beneficiaryBankAdressLine2 = beneficiaryBankAdressLine2;
	}

	public String getBeneficiaryBankCity() {
		return beneficiaryBankCity;
	}

	public void setBeneficiaryBankCity(String beneficiaryBankCity) {
		this.beneficiaryBankCity = beneficiaryBankCity;
	}

	public String getBeneficiaryBankState() {
		return beneficiaryBankState;
	}

	public void setBeneficiaryBankState(String beneficiaryBankState) {
		this.beneficiaryBankState = beneficiaryBankState;
	}

	public String getPartialShipments() {
		return partialShipments;
	}

	public void setPartialShipments(String partialShipments) {
		this.partialShipments = partialShipments;
	}

	public String getIncoTerms() {
		return incoTerms;
	}

	public void setIncoTerms(String incoTerms) {
		this.incoTerms = incoTerms;
	}

	public String getConfirmationInstruction() {
		return confirmationInstruction;
	}

	public void setConfirmationInstruction(String confirmationInstruction) {
		this.confirmationInstruction = confirmationInstruction;
	}

	public String getTransferable() {
		return transferable;
	}

	public void setTransferable(String transferable) {
		this.transferable = transferable;
	}

	public String getStandByLC() {
		return standByLC;
	}

	public void setStandByLC(String standByLC) {
		this.standByLC = standByLC;
	}

	public String getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(String isDraft) {
		this.isDraft = isDraft;
	}

	public String getAdditionalPayableCurrency() {
		return additionalPayableCurrency;
	}

	public void setAdditionalPayableCurrency(String additionalPayableCurrency) {
		this.additionalPayableCurrency = additionalPayableCurrency;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

	public String getSrmsReqOrderID() {
		return srmsReqOrderID;
	}

	public void setSrmsReqOrderID(String srmsReqOrderID) {
		this.srmsReqOrderID = srmsReqOrderID;
	}

	public String getLcCreatedOn() {
		return lcCreatedOn;
	}

	public void setLcCreatedOn(String lcCreatedOn) {
		this.lcCreatedOn = lcCreatedOn;
	}

	public boolean isReferenceNomatch() {
		return referenceNomatch;
	}

	public void setReferenceNomatch(boolean referenceNomatch) {
		this.referenceNomatch = referenceNomatch;
	}

	public String getErrorCodeSRMSmatch() {
		return ErrorCodeSRMSmatch;
	}

	public void setErrorCodeSRMSmatch(String errorCodeSRMSmatch) {
		ErrorCodeSRMSmatch = errorCodeSRMSmatch;
	}

	public String getErrorMsgSRMSmatch() {
		return ErrorMsgSRMSmatch;
	}

	public void setErrorMsgSRMSmatch(String errorMsgSRMSmatch) {
		ErrorMsgSRMSmatch = errorMsgSRMSmatch;
	}

	public String getScreenNumber() {
		return screenNumber;
	}

	public void setScreenNumber(String screenNumber) {
		this.screenNumber = screenNumber;
	}

	public String getDraftCount() {
		return draftCount;
	}

	public void setDraftCount(String draftCount) {
		this.draftCount = draftCount;
	}

	public String getDraftAmount() {
		return draftAmount;
	}

	public void setDraftAmount(String draftAmount) {
		this.draftAmount = draftAmount;
	}

	public String getDeletedCount() {
		return deletedCount;
	}

	public void setDeletedCount(String deletedCount) {
		this.deletedCount = deletedCount;
	}

	public String getDeletedAmount() {
		return deletedAmount;
	}

	public void setDeletedAmount(String deletedAmount) {
		this.deletedAmount = deletedAmount;
	}

	public String getSelfApprovedCount() {
		return selfApprovedCount;
	}

	public void setSelfApprovedCount(String selfApprovedCount) {
		this.selfApprovedCount = selfApprovedCount;
	}

	public String getSelfApprovedAmount() {
		return selfApprovedAmount;
	}

	public void setSelfApprovedAmount(String selfApprovedAmount) {
		this.selfApprovedAmount = selfApprovedAmount;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSignatoryApprovalRequired() {
		return signatoryApprovalRequired;
	}

	public void setSignatoryApprovalRequired(String signatoryApprovalRequired) {
		this.signatoryApprovalRequired = signatoryApprovalRequired;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIsInternationalAccount() {
		return isInternationalAccount;
	}

	public void setIsInternationalAccount(String isInternationalAccount) {
		this.isInternationalAccount = isInternationalAccount;
	}

	public String getIsSameBankAccount() {
		return isSameBankAccount;
	}

	public void setIsSameBankAccount(String isSameBankAccount) {
		this.isSameBankAccount = isSameBankAccount;
	}

	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public String getIsBusinessPayee() {
		return isBusinessPayee;
	}

	public void setIsBusinessPayee(String isBusinessPayee) {
		this.isBusinessPayee = isBusinessPayee;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public Object getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public Integer getNoOfCustomersLinked() {
		return noOfCustomersLinked;
	}

	public void setNoOfCustomersLinked(Integer noOfCustomersLinked) {
		this.noOfCustomersLinked = noOfCustomersLinked;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public Object getOldInfo() throws IOException {
		return JSONUtils.stringify(oldInfo);
	}

	public void setOldInfo(Object oldInfo) {
		this.oldInfo = oldInfo;
	}

	public String getAdditionalMeta() {
		return additionalMeta;
	}

	public void setAdditionalMeta(String additionalMeta) {
		this.additionalMeta = additionalMeta;
	}
	
	
	
	public String getCompositeRequestIds() {
		return compositeRequestIds;
	}

	public void setCompositeRequestIds(String compositeRequestIds) {
		this.compositeRequestIds = compositeRequestIds;
	}

	public String getAssocStatus() {
		return assocStatus;
	}

	public void setAssocStatus(String assocStatus) {
		this.assocStatus = assocStatus;
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
							  List<UploadedAttachments> fileNames, String charges, String indicativeRate, String fromAccountCurrency,
							  String creditValueDate, String totalDebitAmount, String toAccountName, String paymentType, String frequencyEndDate,
							  String beneficiaryBankAddress, String isGroupMatrix, String lcReferenceNo, double lcAmount, String lcCurrency, String availableWith1,
							  String availableWith2, String availableWith3, String availableWith4, String issueDate, String expiryDate,
							  String expiryPlace, String beneficiaryName, String beneficiaryAddressLine1, String beneficiaryAddressLine2,
							  String beneficiaryPostCode, String beneficiaryCountry, String beneficiaryCity, String beneficiaryState,
							  String beneficiaryBank, String beneficiaryBankAdressLine1, String beneficiaryBankAdressLine2, String beneficiaryBankCity,
							  String beneficiaryBankState, String partialShipments, String incoTerms, String confirmationInstruction,
							  String transferable, String standByLC, String isDraft, String additionalPayableCurrency, String code, String msg,
							  String flowType, String id, String ErrorCode, String ErrorMessage, String srmsReqOrderID, String lcCreatedOn,
							  boolean referenceNomatch, String ErrorCodeSRMSmatch, String ErrorMsgSRMSmatch, String screenNumber, String draftCount,
							  String draftAmount, String deletedCount, String deletedAmount, String selfApprovedCount, String selfApprovedAmount,
							  String totalCount, String totalAmount, String signatoryApprovalRequired) {
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
		this.fileNames = fileNames;
		this.charges = charges;
		this.indicativeRate = indicativeRate;
		this.fromAccountCurrency = fromAccountCurrency;
		this.creditValueDate = creditValueDate;
		this.totalDebitAmount = totalDebitAmount;
		this.toAccountName = toAccountName;
		this.paymentType = paymentType;
		this.frequencyEndDate = frequencyEndDate;
		this.beneficiaryBankAddress = beneficiaryBankAddress;
		this.isGroupMatrix = isGroupMatrix;
		this.lcReferenceNo=lcReferenceNo;
		this.lcAmount=lcAmount;
		this.lcCurrency=lcCurrency;
		this.availableWith1=availableWith1;
		this.availableWith2=availableWith2;
		this.availableWith3=availableWith3;
		this.availableWith4=availableWith4;
		this.issueDate=issueDate;
		this.expiryDate=expiryDate;
		this.expiryPlace=expiryPlace;
		this.beneficiaryName=beneficiaryName;
		this.beneficiaryAddressLine1=beneficiaryAddressLine1;
		this.beneficiaryAddressLine2=beneficiaryAddressLine2;
		this.beneficiaryPostCode=beneficiaryPostCode;
		this.beneficiaryCountry=beneficiaryCountry;
		this.beneficiaryCity=beneficiaryCity;
		this.beneficiaryState=beneficiaryState;
		this.beneficiaryBank=beneficiaryBank;
		this.beneficiaryBankAdressLine1=beneficiaryBankAdressLine1;
		this.beneficiaryBankAdressLine2=beneficiaryBankAdressLine2;
		this.beneficiaryBankCity=beneficiaryBankCity;
		this.beneficiaryBankState=beneficiaryBankState;
		this.partialShipments=partialShipments;
		this.incoTerms=incoTerms;
		this.confirmationInstruction=confirmationInstruction;
		this.transferable=transferable;
		this.standByLC=standByLC;
		this.isDraft=isDraft;
		this.additionalPayableCurrency=additionalPayableCurrency;
		this.code=code;
		this.msg=msg;
		this.flowType=flowType;
		this.id=id;
		this.ErrorCode=ErrorCode;
		this.ErrorMessage=ErrorMessage;
		this.srmsReqOrderID=srmsReqOrderID;
		this.lcCreatedOn=lcCreatedOn;
		this.referenceNomatch=referenceNomatch;
		this.ErrorCodeSRMSmatch=ErrorCodeSRMSmatch;
		this.ErrorMsgSRMSmatch=ErrorMsgSRMSmatch;
		this.screenNumber=screenNumber;
		this.draftCount=draftCount;
		this.draftAmount=draftAmount;
		this.deletedCount=deletedCount;
		this.deletedAmount=deletedAmount;
		this.selfApprovedCount=selfApprovedCount;
		this.selfApprovedAmount=selfApprovedAmount;
		this.totalCount=totalCount;
		this.totalAmount=totalAmount;
		this.signatoryApprovalRequired=signatoryApprovalRequired;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ErrorCode == null) ? 0 : ErrorCode.hashCode());
		result = prime * result + ((ErrorCodeSRMSmatch == null) ? 0 : ErrorCodeSRMSmatch.hashCode());
		result = prime * result + ((ErrorMessage == null) ? 0 : ErrorMessage.hashCode());
		result = prime * result + ((ErrorMsgSRMSmatch == null) ? 0 : ErrorMsgSRMSmatch.hashCode());
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((actedByMeAlready == null) ? 0 : actedByMeAlready.hashCode());
		result = prime * result + ((additionalPayableCurrency == null) ? 0 : additionalPayableCurrency.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((approvalDate == null) ? 0 : approvalDate.hashCode());
		result = prime * result + ((availableWith1 == null) ? 0 : availableWith1.hashCode());
		result = prime * result + ((availableWith2 == null) ? 0 : availableWith2.hashCode());
		result = prime * result + ((availableWith3 == null) ? 0 : availableWith3.hashCode());
		result = prime * result + ((availableWith4 == null) ? 0 : availableWith4.hashCode());
		result = prime * result + ((beneficiaryAddressLine1 == null) ? 0 : beneficiaryAddressLine1.hashCode());
		result = prime * result + ((beneficiaryAddressLine2 == null) ? 0 : beneficiaryAddressLine2.hashCode());
		result = prime * result + ((beneficiaryBank == null) ? 0 : beneficiaryBank.hashCode());
		result = prime * result + ((beneficiaryBankAddress == null) ? 0 : beneficiaryBankAddress.hashCode());
		result = prime * result + ((beneficiaryBankAdressLine1 == null) ? 0 : beneficiaryBankAdressLine1.hashCode());
		result = prime * result + ((beneficiaryBankAdressLine2 == null) ? 0 : beneficiaryBankAdressLine2.hashCode());
		result = prime * result + ((beneficiaryBankCity == null) ? 0 : beneficiaryBankCity.hashCode());
		result = prime * result + ((beneficiaryBankState == null) ? 0 : beneficiaryBankState.hashCode());
		result = prime * result + ((beneficiaryCity == null) ? 0 : beneficiaryCity.hashCode());
		result = prime * result + ((beneficiaryCountry == null) ? 0 : beneficiaryCountry.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((beneficiaryPostCode == null) ? 0 : beneficiaryPostCode.hashCode());
		result = prime * result + ((beneficiaryState == null) ? 0 : beneficiaryState.hashCode());
		result = prime * result + ((charges == null) ? 0 : charges.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((confirmationInstruction == null) ? 0 : confirmationInstruction.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((creditValueDate == null) ? 0 : creditValueDate.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((deletedAmount == null) ? 0 : deletedAmount.hashCode());
		result = prime * result + ((deletedCount == null) ? 0 : deletedCount.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((draftAmount == null) ? 0 : draftAmount.hashCode());
		result = prime * result + ((draftCount == null) ? 0 : draftCount.hashCode());
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((expiryPlace == null) ? 0 : expiryPlace.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((featureActionName == null) ? 0 : featureActionName.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((fileNames == null) ? 0 : fileNames.hashCode());
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((fileTypeId == null) ? 0 : fileTypeId.hashCode());
		result = prime * result + ((flowType == null) ? 0 : flowType.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
		result = prime * result + ((fromAccountCurrency == null) ? 0 : fromAccountCurrency.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((incoTerms == null) ? 0 : incoTerms.hashCode());
		result = prime * result + ((indicativeRate == null) ? 0 : indicativeRate.hashCode());
		result = prime * result + ((isDraft == null) ? 0 : isDraft.hashCode());
		result = prime * result + ((isGroupMatrix == null) ? 0 : isGroupMatrix.hashCode());
		result = prime * result + ((issueDate == null) ? 0 : issueDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lcAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((lcCreatedOn == null) ? 0 : lcCreatedOn.hashCode());
		result = prime * result + ((lcCurrency == null) ? 0 : lcCurrency.hashCode());
		result = prime * result + ((lcReferenceNo == null) ? 0 : lcReferenceNo.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((limitGroupName == null) ? 0 : limitGroupName.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((noOfBooks == null) ? 0 : noOfBooks.hashCode());
		result = prime * result + ((numberOfCredits == null) ? 0 : numberOfCredits.hashCode());
		result = prime * result + ((numberOfDebits == null) ? 0 : numberOfDebits.hashCode());
		result = prime * result + ((numberOfPrenotes == null) ? 0 : numberOfPrenotes.hashCode());
		result = prime * result + ((numberOfRecords == null) ? 0 : numberOfRecords.hashCode());
		result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
		result = prime * result + ((partialShipments == null) ? 0 : partialShipments.hashCode());
		result = prime * result + ((payee == null) ? 0 : payee.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
		result = prime * result + ((processingDate == null) ? 0 : processingDate.hashCode());
		result = prime * result + ((processingMode == null) ? 0 : processingMode.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((recurrence == null) ? 0 : recurrence.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + (referenceNomatch ? 1231 : 1237);
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + ((screenNumber == null) ? 0 : screenNumber.hashCode());
		result = prime * result + ((selfApprovedAmount == null) ? 0 : selfApprovedAmount.hashCode());
		result = prime * result + ((selfApprovedCount == null) ? 0 : selfApprovedCount.hashCode());
		result = prime * result + ((sentBy == null) ? 0 : sentBy.hashCode());
		result = prime * result + ((sentDate == null) ? 0 : sentDate.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((signatoryApprovalRequired == null) ? 0 : signatoryApprovalRequired.hashCode());
		result = prime * result + ((srmsReqOrderID == null) ? 0 : srmsReqOrderID.hashCode());
		result = prime * result + ((standByLC == null) ? 0 : standByLC.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
		result = prime * result + ((toAccountName == null) ? 0 : toAccountName.hashCode());
		result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
		result = prime * result + ((totalCount == null) ? 0 : totalCount.hashCode());
		result = prime * result + ((totalCreditAmount == null) ? 0 : totalCreditAmount.hashCode());
		result = prime * result + ((totalDebitAmount == null) ? 0 : totalDebitAmount.hashCode());
		result = prime * result + ((totalTransactions == null) ? 0 : totalTransactions.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((transferable == null) ? 0 : transferable.hashCode());
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
		ApprovalRequestDTO other = (ApprovalRequestDTO) obj;
		if (ErrorCode == null) {
			if (other.ErrorCode != null)
				return false;
		} else if (!ErrorCode.equals(other.ErrorCode))
			return false;
		if (ErrorCodeSRMSmatch == null) {
			if (other.ErrorCodeSRMSmatch != null)
				return false;
		} else if (!ErrorCodeSRMSmatch.equals(other.ErrorCodeSRMSmatch))
			return false;
		if (ErrorMessage == null) {
			if (other.ErrorMessage != null)
				return false;
		} else if (!ErrorMessage.equals(other.ErrorMessage))
			return false;
		if (ErrorMsgSRMSmatch == null) {
			if (other.ErrorMsgSRMSmatch != null)
				return false;
		} else if (!ErrorMsgSRMSmatch.equals(other.ErrorMsgSRMSmatch))
			return false;
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
		if (additionalPayableCurrency == null) {
			if (other.additionalPayableCurrency != null)
				return false;
		} else if (!additionalPayableCurrency.equals(other.additionalPayableCurrency))
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
		if (availableWith1 == null) {
			if (other.availableWith1 != null)
				return false;
		} else if (!availableWith1.equals(other.availableWith1))
			return false;
		if (availableWith2 == null) {
			if (other.availableWith2 != null)
				return false;
		} else if (!availableWith2.equals(other.availableWith2))
			return false;
		if (availableWith3 == null) {
			if (other.availableWith3 != null)
				return false;
		} else if (!availableWith3.equals(other.availableWith3))
			return false;
		if (availableWith4 == null) {
			if (other.availableWith4 != null)
				return false;
		} else if (!availableWith4.equals(other.availableWith4))
			return false;
		if (beneficiaryAddressLine1 == null) {
			if (other.beneficiaryAddressLine1 != null)
				return false;
		} else if (!beneficiaryAddressLine1.equals(other.beneficiaryAddressLine1))
			return false;
		if (beneficiaryAddressLine2 == null) {
			if (other.beneficiaryAddressLine2 != null)
				return false;
		} else if (!beneficiaryAddressLine2.equals(other.beneficiaryAddressLine2))
			return false;
		if (beneficiaryBank == null) {
			if (other.beneficiaryBank != null)
				return false;
		} else if (!beneficiaryBank.equals(other.beneficiaryBank))
			return false;
		if (beneficiaryBankAddress == null) {
			if (other.beneficiaryBankAddress != null)
				return false;
		} else if (!beneficiaryBankAddress.equals(other.beneficiaryBankAddress))
			return false;
		if (beneficiaryBankAdressLine1 == null) {
			if (other.beneficiaryBankAdressLine1 != null)
				return false;
		} else if (!beneficiaryBankAdressLine1.equals(other.beneficiaryBankAdressLine1))
			return false;
		if (beneficiaryBankAdressLine2 == null) {
			if (other.beneficiaryBankAdressLine2 != null)
				return false;
		} else if (!beneficiaryBankAdressLine2.equals(other.beneficiaryBankAdressLine2))
			return false;
		if (beneficiaryBankCity == null) {
			if (other.beneficiaryBankCity != null)
				return false;
		} else if (!beneficiaryBankCity.equals(other.beneficiaryBankCity))
			return false;
		if (beneficiaryBankState == null) {
			if (other.beneficiaryBankState != null)
				return false;
		} else if (!beneficiaryBankState.equals(other.beneficiaryBankState))
			return false;
		if (beneficiaryCity == null) {
			if (other.beneficiaryCity != null)
				return false;
		} else if (!beneficiaryCity.equals(other.beneficiaryCity))
			return false;
		if (beneficiaryCountry == null) {
			if (other.beneficiaryCountry != null)
				return false;
		} else if (!beneficiaryCountry.equals(other.beneficiaryCountry))
			return false;
		if (beneficiaryName == null) {
			if (other.beneficiaryName != null)
				return false;
		} else if (!beneficiaryName.equals(other.beneficiaryName))
			return false;
		if (beneficiaryPostCode == null) {
			if (other.beneficiaryPostCode != null)
				return false;
		} else if (!beneficiaryPostCode.equals(other.beneficiaryPostCode))
			return false;
		if (beneficiaryState == null) {
			if (other.beneficiaryState != null)
				return false;
		} else if (!beneficiaryState.equals(other.beneficiaryState))
			return false;
		if (charges == null) {
			if (other.charges != null)
				return false;
		} else if (!charges.equals(other.charges))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
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
		if (confirmationInstruction == null) {
			if (other.confirmationInstruction != null)
				return false;
		} else if (!confirmationInstruction.equals(other.confirmationInstruction))
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
		if (creditValueDate == null) {
			if (other.creditValueDate != null)
				return false;
		} else if (!creditValueDate.equals(other.creditValueDate))
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
		if (deletedAmount == null) {
			if (other.deletedAmount != null)
				return false;
		} else if (!deletedAmount.equals(other.deletedAmount))
			return false;
		if (deletedCount == null) {
			if (other.deletedCount != null)
				return false;
		} else if (!deletedCount.equals(other.deletedCount))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (draftAmount == null) {
			if (other.draftAmount != null)
				return false;
		} else if (!draftAmount.equals(other.draftAmount))
			return false;
		if (draftCount == null) {
			if (other.draftCount != null)
				return false;
		} else if (!draftCount.equals(other.draftCount))
			return false;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		if (expiryPlace == null) {
			if (other.expiryPlace != null)
				return false;
		} else if (!expiryPlace.equals(other.expiryPlace))
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
		if (fileNames == null) {
			if (other.fileNames != null)
				return false;
		} else if (!fileNames.equals(other.fileNames))
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
		if (flowType == null) {
			if (other.flowType != null)
				return false;
		} else if (!flowType.equals(other.flowType))
			return false;
		if (frequency == null) {
			if (other.frequency != null)
				return false;
		} else if (!frequency.equals(other.frequency))
			return false;
		if (frequencyEndDate == null) {
			if (other.frequencyEndDate != null)
				return false;
		} else if (!frequencyEndDate.equals(other.frequencyEndDate))
			return false;
		if (fromAccountCurrency == null) {
			if (other.fromAccountCurrency != null)
				return false;
		} else if (!fromAccountCurrency.equals(other.fromAccountCurrency))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (incoTerms == null) {
			if (other.incoTerms != null)
				return false;
		} else if (!incoTerms.equals(other.incoTerms))
			return false;
		if (indicativeRate == null) {
			if (other.indicativeRate != null)
				return false;
		} else if (!indicativeRate.equals(other.indicativeRate))
			return false;
		if (isDraft == null) {
			if (other.isDraft != null)
				return false;
		} else if (!isDraft.equals(other.isDraft))
			return false;
		if (isGroupMatrix == null) {
			if (other.isGroupMatrix != null)
				return false;
		} else if (!isGroupMatrix.equals(other.isGroupMatrix))
			return false;
		if (issueDate == null) {
			if (other.issueDate != null)
				return false;
		} else if (!issueDate.equals(other.issueDate))
			return false;
		if (Double.doubleToLongBits(lcAmount) != Double.doubleToLongBits(other.lcAmount))
			return false;
		if (lcCreatedOn == null) {
			if (other.lcCreatedOn != null)
				return false;
		} else if (!lcCreatedOn.equals(other.lcCreatedOn))
			return false;
		if (lcCurrency == null) {
			if (other.lcCurrency != null)
				return false;
		} else if (!lcCurrency.equals(other.lcCurrency))
			return false;
		if (lcReferenceNo == null) {
			if (other.lcReferenceNo != null)
				return false;
		} else if (!lcReferenceNo.equals(other.lcReferenceNo))
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
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
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
		if (partialShipments == null) {
			if (other.partialShipments != null)
				return false;
		} else if (!partialShipments.equals(other.partialShipments))
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
		if (paymentType == null) {
			if (other.paymentType != null)
				return false;
		} else if (!paymentType.equals(other.paymentType))
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
		if (referenceNomatch != other.referenceNomatch)
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
		if (screenNumber == null) {
			if (other.screenNumber != null)
				return false;
		} else if (!screenNumber.equals(other.screenNumber))
			return false;
		if (selfApprovedAmount == null) {
			if (other.selfApprovedAmount != null)
				return false;
		} else if (!selfApprovedAmount.equals(other.selfApprovedAmount))
			return false;
		if (selfApprovedCount == null) {
			if (other.selfApprovedCount != null)
				return false;
		} else if (!selfApprovedCount.equals(other.selfApprovedCount))
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
		if (signatoryApprovalRequired == null) {
			if (other.signatoryApprovalRequired != null)
				return false;
		} else if (!signatoryApprovalRequired.equals(other.signatoryApprovalRequired))
			return false;
		if (srmsReqOrderID == null) {
			if (other.srmsReqOrderID != null)
				return false;
		} else if (!srmsReqOrderID.equals(other.srmsReqOrderID))
			return false;
		if (standByLC == null) {
			if (other.standByLC != null)
				return false;
		} else if (!standByLC.equals(other.standByLC))
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
		if (toAccountName == null) {
			if (other.toAccountName != null)
				return false;
		} else if (!toAccountName.equals(other.toAccountName))
			return false;
		if (totalAmount == null) {
			if (other.totalAmount != null)
				return false;
		} else if (!totalAmount.equals(other.totalAmount))
			return false;
		if (totalCount == null) {
			if (other.totalCount != null)
				return false;
		} else if (!totalCount.equals(other.totalCount))
			return false;
		if (totalCreditAmount == null) {
			if (other.totalCreditAmount != null)
				return false;
		} else if (!totalCreditAmount.equals(other.totalCreditAmount))
			return false;
		if (totalDebitAmount == null) {
			if (other.totalDebitAmount != null)
				return false;
		} else if (!totalDebitAmount.equals(other.totalDebitAmount))
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
		if (transferable == null) {
			if (other.transferable != null)
				return false;
		} else if (!transferable.equals(other.transferable))
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

	@Override
	public String toString() {
		return "ApprovalRequestDTO [compositeRequestIds=" + compositeRequestIds + ", assocStatus=" + assocStatus
				+ ", requestId=" + requestId + ", assocRequestId=" + assocRequestId + ", featureName=" + featureName
				+ ", featureActionId=" + featureActionId + ", limitGroupId=" + limitGroupId + ", limitGroupName="
				+ limitGroupName + ", transactionId=" + transactionId + ", contractId=" + contractId + ", companyId="
				+ companyId + ", companyName=" + companyName + ", accountId=" + accountId + ", status=" + status
				+ ", sentBy=" + sentBy + ", amIApprover=" + amIApprover + ", amICreator=" + amICreator
				+ ", requiredApprovals=" + requiredApprovals + ", receivedApprovals=" + receivedApprovals
				+ ", actedByMeAlready=" + actedByMeAlready + ", noOfBooks=" + noOfBooks + ", address=" + address
				+ ", requestType=" + requestType + ", processingDate=" + processingDate + ", sentDate=" + sentDate
				+ ", approvalDate=" + approvalDate + ", amount=" + amount + ", payee=" + payee + ", frequency="
				+ frequency + ", recurrence=" + recurrence + ", reference=" + reference + ", customerId=" + customerId
				+ ", totalCreditAmount=" + totalCreditAmount + ", fileName=" + fileName + ", customerName="
				+ customerName + ", serviceCharge=" + serviceCharge + ", convertedAmount=" + convertedAmount
				+ ", transactionAmount=" + transactionAmount + ", transactionCurrency=" + transactionCurrency
				+ ", featureActionName=" + featureActionName + ", fileType=" + fileType + ", fileTypeId=" + fileTypeId
				+ ", numberOfCredits=" + numberOfCredits + ", numberOfDebits=" + numberOfDebits + ", numberOfPrenotes="
				+ numberOfPrenotes + ", numberOfRecords=" + numberOfRecords + ", confirmationNumber="
				+ confirmationNumber + ", companyLegalUnit=" + companyLegalUnit + ", templateName=" + templateName
				+ ", description=" + description + ", paymentId=" + paymentId + ", totalTransactions="
				+ totalTransactions + ", processingMode=" + processingMode + ", paidBy=" + paidBy + ", swiftCode="
				+ swiftCode + ", fileNames=" + fileNames + ", charges=" + charges + ", indicativeRate=" + indicativeRate
				+ ", fromAccountCurrency=" + fromAccountCurrency + ", creditValueDate=" + creditValueDate
				+ ", totalDebitAmount=" + totalDebitAmount + ", toAccountName=" + toAccountName + ", paymentType="
				+ paymentType + ", frequencyEndDate=" + frequencyEndDate + ", beneficiaryBankAddress="
				+ beneficiaryBankAddress + ", isGroupMatrix=" + isGroupMatrix + ", lcReferenceNo=" + lcReferenceNo
				+ ", lcAmount=" + lcAmount + ", lcCurrency=" + lcCurrency + ", availableWith1=" + availableWith1
				+ ", availableWith2=" + availableWith2 + ", availableWith3=" + availableWith3 + ", availableWith4="
				+ availableWith4 + ", issueDate=" + issueDate + ", expiryDate=" + expiryDate + ", expiryPlace="
				+ expiryPlace + ", beneficiaryName=" + beneficiaryName + ", beneficiaryAddressLine1="
				+ beneficiaryAddressLine1 + ", beneficiaryAddressLine2=" + beneficiaryAddressLine2
				+ ", beneficiaryPostCode=" + beneficiaryPostCode + ", beneficiaryCountry=" + beneficiaryCountry
				+ ", beneficiaryCity=" + beneficiaryCity + ", beneficiaryState=" + beneficiaryState
				+ ", beneficiaryBank=" + beneficiaryBank + ", beneficiaryBankAdressLine1=" + beneficiaryBankAdressLine1
				+ ", beneficiaryBankAdressLine2=" + beneficiaryBankAdressLine2 + ", beneficiaryBankCity="
				+ beneficiaryBankCity + ", beneficiaryBankState=" + beneficiaryBankState + ", partialShipments="
				+ partialShipments + ", incoTerms=" + incoTerms + ", confirmationInstruction=" + confirmationInstruction
				+ ", transferable=" + transferable + ", standByLC=" + standByLC + ", isDraft=" + isDraft
				+ ", additionalPayableCurrency=" + additionalPayableCurrency + ", code=" + code + ", msg=" + msg
				+ ", flowType=" + flowType + ", id=" + id + ", ErrorCode=" + ErrorCode + ", ErrorMessage="
				+ ErrorMessage + ", srmsReqOrderID=" + srmsReqOrderID + ", lcCreatedOn=" + lcCreatedOn
				+ ", referenceNomatch=" + referenceNomatch + ", ErrorCodeSRMSmatch=" + ErrorCodeSRMSmatch
				+ ", ErrorMsgSRMSmatch=" + ErrorMsgSRMSmatch + ", screenNumber=" + screenNumber + ", draftCount="
				+ draftCount + ", draftAmount=" + draftAmount + ", deletedCount=" + deletedCount + ", deletedAmount="
				+ deletedAmount + ", selfApprovedCount=" + selfApprovedCount + ", selfApprovedAmount="
				+ selfApprovedAmount + ", totalCount=" + totalCount + ", totalAmount=" + totalAmount
				+ ", signatoryApprovalRequired=" + signatoryApprovalRequired + ", accountNumber=" + accountNumber
				+ ", accountType=" + accountType + ", bankName=" + bankName + ", countryName=" + countryName
				+ ", createdOn=" + createdOn + ", firstName=" + firstName + ", lastName=" + lastName + ", nickName="
				+ nickName + ", isInternationalAccount=" + isInternationalAccount + ", isSameBankAccount="
				+ isSameBankAccount + ", isVerified=" + isVerified + ", notes=" + notes + ", routingNumber="
				+ routingNumber + ", iban=" + iban + ", sortCode=" + sortCode + ", phoneCountryCode=" + phoneCountryCode
				+ ", phoneNumber=" + phoneNumber + ", phoneExtension=" + phoneExtension + ", isBusinessPayee="
				+ isBusinessPayee + ", payeeId=" + payeeId + ", cif=" + cif + ", noOfCustomersLinked="
				+ noOfCustomersLinked + ", phone=" + phone + ", email=" + email + ", country=" + country + ", city="
				+ city + ", zipcode=" + zipcode + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
				+ ", oldInfo=" + oldInfo + ", additionalMeta=" + additionalMeta + "]";
	}

	
	

}
