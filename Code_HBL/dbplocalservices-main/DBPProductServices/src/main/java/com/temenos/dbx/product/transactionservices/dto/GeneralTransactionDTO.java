package com.temenos.dbx.product.transactionservices.dto;

import java.text.ParseException;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.HelperMethods;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralTransactionDTO implements DBPDTO {

	private static final long serialVersionUID = 6803632477929246063L;
	
	private String transactionId;
	private String confirmationNumber;
	private String featureActionId;
	private String fromAccountNumber;
	private double amount;
	private String requestId;
	private String createdby;
	private String createdts;
	private String frequencyTypeId;
	private String status;
	private String numberOfRecurrences;
	private String payeeId;
	private String toAccountNumber;
	private String accountName;
	private String userName;
	private String companyName;
	private String companyId;
	private String scheduledDate;
	private String requestCreatedby;
	private String receivedApprovals;
	private String requiredApprovals;
	private String featureName;
	private String payeeName;
	private String amICreator;
	private String amIApprover;
	private String swiftCode;
	private String frequencyEndDate;
	
	public GeneralTransactionDTO() {
		super();
	}


	public GeneralTransactionDTO(String transactionId, String confirmationNumber, String featureActionId, String fromAccountNumber, double amount,
			String requestId, String createdby, String createdts, String frequencyTypeId, String status,
			String numberOfRecurrences, String payeeId, String toAccountNumber, String accountName, String userName,
			String companyName, String companyId, String scheduledDate, String requestCreatedby, String receivedApprovals, String requiredApprovals,
			String featureName, String payeeName, String amICreator, String amIApprover, String swiftCode, String frequencyEndDate) {
		super();
		this.transactionId = transactionId;
		this.featureActionId = featureActionId;
		this.fromAccountNumber = fromAccountNumber;
		this.amount = amount;
		this.requestId = requestId;
		this.createdby = createdby;
		this.createdts = createdts;
		this.frequencyTypeId = frequencyTypeId;
		this.status = status;
		this.numberOfRecurrences = numberOfRecurrences;
		this.payeeId = payeeId;
		this.toAccountNumber = toAccountNumber;
		this.accountName = accountName;
		this.userName = userName;
		this.companyName = companyName;
		this.companyId = companyId;
		this.scheduledDate = scheduledDate;
		this.requestCreatedby = requestCreatedby;
		this.receivedApprovals = receivedApprovals;
		this.requiredApprovals = requiredApprovals;
		this.payeeName = payeeName;
		this.featureName = featureName;
		this.amIApprover = amIApprover;
		this.amICreator = amICreator;
		this.swiftCode = swiftCode;
		this.frequencyEndDate = frequencyEndDate;
	}
	
	public String getConfirmationNumber() {
		return confirmationNumber;
	}


	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getSwiftCode() {
		return swiftCode;
	}


	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
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


	public String getFeatureName() {
		return featureName;
	}


	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public String getFeatureActionId() {
		return featureActionId;
	}


	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}


	public String getFromAccountNumber() {
		return fromAccountNumber;
	}


	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
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


	public String getCreatedts() {
		return createdts;
	}


	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}


	public String getFrequencyTypeId() {
		return frequencyTypeId;
	}


	public void setFrequencyTypeId(String frequencyTypeId) {
		this.frequencyTypeId = frequencyTypeId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getNumberOfRecurrences() {
		return numberOfRecurrences;
	}


	public void setNumberOfRecurrences(String numberOfRecurrences) {
		this.numberOfRecurrences = numberOfRecurrences;
	}


	public String getPayeeId() {
		return payeeId;
	}


	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}


	public String getToAccountNumber() {
		return toAccountNumber;
	}


	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getCompanyId() {
		return companyId;
	}


	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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


	public String getRequestCreatedby() {
		return requestCreatedby;
	}


	public void setRequestCreatedby(String requestCreatedby) {
		this.requestCreatedby = requestCreatedby;
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
	
	public String getPayeeName() {
		return payeeName;
	}


	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	
	public String getFrequencyEndDate() {
		return frequencyEndDate;
	}


	public void setFrequencyEndDate(String frequencyEndDate) {
		this.frequencyEndDate = frequencyEndDate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((frequencyTypeId == null) ? 0 : frequencyTypeId.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((requestCreatedby == null) ? 0 : requestCreatedby.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
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
		GeneralTransactionDTO other = (GeneralTransactionDTO) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}
	
	
}