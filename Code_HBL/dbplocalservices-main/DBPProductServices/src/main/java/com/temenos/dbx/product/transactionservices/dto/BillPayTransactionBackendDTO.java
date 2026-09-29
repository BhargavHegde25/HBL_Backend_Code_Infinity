package com.temenos.dbx.product.transactionservices.dto;

import java.text.ParseException;
import java.util.HashMap;

import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.constants.Constants;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPayTransactionBackendDTO implements DBPDTO {

	private static final long serialVersionUID = 7202932613050164264L;

	private String transactionId;
	private String initiationId;
	private String dbxtransactionId;
	
	private double amount;
	private String payeeId;
	private String billerId;
	private String fromAccountNumber;
	private String toAccountNumber;
	
	private String deliverBy;
	private String transactionCurrency;
	private String transactionType;
	private String zipCode;
	
	private String numberOfRecurrences;
	private String scheduledDate;
	private String serviceName;
	private String status;
	
	//@JsonAlias({"frequencyTypeId"})
	private String frequencyType;
	
	//Alias frequencyType ({""})
	private String frequencyTypeId;
	
	//@JsonAlias({"notes"})
	private String transactionsNotes;
	
	//Alias for transactionsNotes({"notes"})
	private String notes;
	
	//@JsonAlias({"frequencystartdate"})
	private String frequencyStartDate;
	
	//Alias for frequencyStartDate({""})
	private String frequencystartdate;
	
	//@JsonAlias({"frequencyenddate"})
	private String frequencyEndDate;
	
	//Alias for frequencyEndDate({""})
	private String frequencyenddate;

	private String deliveryDate;
	private String processingDate;
	private String recipientId;
	private String accountCode;
	private String nickName;
	private String fromAccountType;
	private String day1;
	private String day2;
	private String toAccountType;
	private String name;
	private String numberOfPayments;
	private String message;
	private String frequency;
	private String question;
	private String answer;
	private String checkBackImage;
	
	@JsonProperty("AccountHolderNumber")
	private String accountHolderNumber;
	
	@JsonProperty("PayeeName")
	private String payeName;
	
	private String payeeName;
	
	private String profileId;
	private String cardNumber;
	private String cardExpiry;
	
	@JsonProperty("TransferLocator")
	private String transferLocator;
	
	private String isScheduled;
	private String transferId;
	private String paymentId;
	private String noofRecurrences;
	private String serviceCharge;
	private String convertedAmount;
	private String transactionAmount;
	private String paidBy;
	private String swiftCode;
	private String legalEntityId;
	private HashMap<String, Object> externalApiPayload;
	private JSONObject externalServiceResponse;

	public BillPayTransactionBackendDTO() {
		super();
	}

	public BillPayTransactionBackendDTO(String transactionId, String initiationId, String dbxtransactionId, double amount,
			String payeeId, String billerId, String fromAccountNumber, String toAccountNumber, String deliverBy,
			String transactionCurrency, String transactionType, String zipCode, String numberOfRecurrences,
			String scheduledDate, String frequencyType, String frequencyTypeId, String transactionsNotes, String notes,
			String frequencyStartDate, String frequencystartdate2, String frequencyEndDate, String frequencyenddate2,
			String serviceName,	 String deliveryDate, String processingDate, String recipientId, String accountCode,
			String nickName, String fromAccountType, String day1, String day2, String toAccountType, String name,
			String numberOfPayments, String message, String frequency, String question, String answer, String checkBackImage,
			String accountHolderNumber, String payeeName, String profileId, String cardNumber, String cardExpiry, String transferLocator,
			String isScheduled, String transferId, String paymentId, String noofRecurrences, String payeName,
			String serviceCharge, String convertedAmount, String transactionAmount, String paidBy, String swiftCode, String status, String legalEntityId,
			HashMap<String, Object> externalApiPayload, JSONObject externalServiceResponse) {
		super();
		this.transactionId = transactionId;
		this.initiationId = initiationId;
		this.dbxtransactionId = dbxtransactionId;
		this.amount = amount;
		this.payeeId = payeeId;
		this.billerId = billerId;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.deliverBy = deliverBy;
		this.transactionCurrency = transactionCurrency;
		this.transactionType = transactionType;
		this.zipCode = zipCode;
		this.numberOfRecurrences = numberOfRecurrences;
		this.scheduledDate = scheduledDate;
		this.frequencyType = frequencyType;
		this.frequencyTypeId = frequencyTypeId;
		this.transactionsNotes = transactionsNotes;
		this.notes = notes;
		this.serviceName = serviceName;
		this.frequencyStartDate = frequencyStartDate;
		frequencystartdate = frequencystartdate2;
		this.frequencyEndDate = frequencyEndDate;
		frequencyenddate = frequencyenddate2;
		this.deliveryDate =	deliveryDate;
		this.processingDate = processingDate;
		this.recipientId = 	recipientId;
		this.accountCode =	accountCode;
		this.nickName =	 nickName;
		this.fromAccountType =	fromAccountType;
		this.day1 =	day1;
		this.day2 =	day2;
		this.toAccountType = toAccountType;
		this.name =	name;
		this.numberOfPayments =	numberOfPayments;
		this.message =	message;
		this.frequency = frequency;
		this.question =	question;
		this.answer = answer;
		this.checkBackImage = checkBackImage;
		this.accountHolderNumber =	accountHolderNumber;
		this.payeeName = payeeName;
		this.profileId = profileId;
		this.cardNumber = cardNumber;
		this.cardExpiry = cardExpiry;
		this.transferLocator = transferLocator;
		this.isScheduled = isScheduled;
		this.transferId = transferId;
		this.paymentId = paymentId;
		this.noofRecurrences = noofRecurrences;
		this.payeName = payeName;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.transactionAmount = transactionAmount;
		this.paidBy  = paidBy;
		this.swiftCode = swiftCode;
		this.status = status;
		this.legalEntityId = legalEntityId;
		this.externalApiPayload = externalApiPayload;
		this.externalServiceResponse =externalServiceResponse;
	}

	public BillPayTransactionBackendDTO convert(BillPayTransactionDTO dto) {
		this.transactionId = dto.getTransactionId();
		this.initiationId = dto.getInitiationId();
		this.amount = dto.getAmount();
		this.payeeId = dto.getPayeeId();
		this.payeeName = dto.getPayeeName();
		this.billerId = dto.getBillerId();
		this.fromAccountNumber = dto.getFromAccountNumber();
		this.toAccountNumber = dto.getToAccountNumber();
		this.deliverBy = dto.getDeliverBy();
		this.transactionCurrency = dto.getTransactionCurrency();
		this.transactionType = dto.getTransactionType();
		this.zipCode = dto.getZipCode();
		this.numberOfRecurrences = dto.getNumberOfRecurrences();
		this.scheduledDate = (dto.getScheduledDate() == null || (dto.getScheduledDate()).isEmpty()) ? dto.getFrequencystartdate() : dto.getScheduledDate();
		this.frequencyType = dto.getFrequencyTypeId();
		this.transactionsNotes = dto.getNotes();
		this.frequencyStartDate = dto.getFrequencystartdate();
		this.frequencyEndDate = dto.getFrequencyenddate();
		this.dbxtransactionId = dto.getTransactionId();
		this.serviceName = dto.getFeatureActionId();
		this.transferLocator = dto.getTransactionId();
		this.isScheduled = dto.getIsScheduled();
		this.transferId = dto.getTransactionId();
		this.paymentId = dto.getTransactionId();
		this.frequency = dto.getFrequencyType();
		this.numberOfPayments = dto.getNumberOfRecurrences();
		this.noofRecurrences = dto.getNumberOfRecurrences();
		this.accountCode = dto.getFromAccountNumber();
		this.deliveryDate = dto.getDeliverBy();
		this.processingDate = (dto.getProcessingDate() == null || (dto.getProcessingDate()).isEmpty())?dto.getScheduledDate():dto.getProcessingDate();
		this.recipientId = dto.getPersonId();
		this.nickName = dto.getFromNickName();
		this.fromAccountType = dto.getFromAccountType();
		this.day1 = dto.getDay1();
		this.day2 = dto.getDay2();
		this.toAccountType = dto.getToAccountType();
		this.name = dto.getPayPersonName();
		this.message = dto.getTransactionsNotes();
		this.question = dto.getSecurityQuestion();
		this.answer = dto.getSecurityAnswer();
		this.checkBackImage = dto.getCheckImageBack();
		this.accountHolderNumber = dto.getToAccountNumber();
		this.payeeName = dto.getPayeeName();
		this.payeName = dto.getPayeeName();
		this.profileId = dto.getProfileId();
		this.cardNumber = dto.getCardNumber();
		this.cardExpiry = dto.getCardExpiry();
		this.serviceCharge = dto.getServiceCharge();
		this.convertedAmount = dto.getConvertedAmount();
		this.transactionAmount = dto.getTransactionAmount();
		this.paidBy = dto.getPaidBy();
		this.swiftCode = dto.getSwiftCode();
		this.legalEntityId = dto.getLegalEntityId();
		this.externalApiPayload = dto.getExternalApiPayload();
		this.externalServiceResponse = dto.getExternalServiceResponse();
		return this;
	}
	
	public String getDbxtransactionId() {
		return dbxtransactionId;
	}

	public void setDbxtransactionId(String dbxtransactionId) {
		this.dbxtransactionId = dbxtransactionId;
	}

	public String getPayeName() {
		return payeName;
	}

	public void setPayeName(String payeName) {
		this.payeName = payeName;
	}

	public String getFrequencyTypeId() {
		return frequencyTypeId;
	}

	public void setFrequencyTypeId(String frequencyTypeId) {
		this.frequencyTypeId = null;
		this.setFrequencyType(frequencyTypeId);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = null;
		this.setTransactionsNotes(notes);
	}

	public String getFrequencystartdate() {
		try {
			return HelperMethods.convertDateFormat(frequencystartdate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setFrequencystartdate(String frequencystartdate) {
		this.frequencystartdate = null;
		this.setFrequencyStartDate(frequencystartdate);
	}

	public String getFrequencyenddate() {
		try {
			return HelperMethods.convertDateFormat(frequencyenddate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setFrequencyenddate(String frequencyenddate) {
		this.frequencyenddate = null;
		this.setFrequencyEndDate(frequencyenddate);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
		this.setDbxtransactionId(transactionId);
	}

	public String getInitiationId() {
		return initiationId;
	}

	public void setInitiationId(String initiationId) {
		this.initiationId = initiationId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getDeliverBy() {
		return deliverBy;
	}

	public void setDeliverBy(String deliverBy) {
		this.deliverBy = deliverBy;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getNumberOfRecurrences() {
		return numberOfRecurrences;
	}

	public void setNumberOfRecurrences(String numberOfRecurrences) {
		if("".equals(numberOfRecurrences)) {
			numberOfRecurrences = null;
		}
		this.numberOfRecurrences = numberOfRecurrences;
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

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getTransactionsNotes() {
		return transactionsNotes;
	}

	public void setTransactionsNotes(String transactionsNotes) {
		this.transactionsNotes = transactionsNotes;
	}

	public String getFrequencyStartDate() {
		try {
			return HelperMethods.convertDateFormat(frequencyStartDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setFrequencyStartDate(String frequencyStartDate) {
		if("".equals(frequencyStartDate)) {
			frequencyStartDate = null;
		}
		this.frequencyStartDate = frequencyStartDate;
	}

	public String getFrequencyEndDate() {
		try {
			return HelperMethods.convertDateFormat(frequencyEndDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setFrequencyEndDate(String frequencyEndDate) {
		if("".equals(frequencyEndDate)) {
			frequencyEndDate = null;
		}
		this.frequencyEndDate = frequencyEndDate;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDeliveryDate() {
		try {
			return HelperMethods.convertDateFormat(deliveryDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getDay1() {
		return day1;
	}

	public void setDay1(String day1) {
		this.day1 = day1;
	}

	public String getDay2() {
		return day2;
	}

	public void setDay2(String day2) {
		this.day2 = day2;
	}

	public String getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(String numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCheckBackImage() {
		return checkBackImage;
	}

	public void setCheckBackImage(String checkBackImage) {
		this.checkBackImage = checkBackImage;
	}

	public String getAccountHolderNumber() {
		return accountHolderNumber;
	}

	public void setAccountHolderNumber(String accountHolderNumber) {
		this.accountHolderNumber = accountHolderNumber;
	}

	public String getPayeeName() {
		return this.payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardExpiry() {
		return cardExpiry;
	}

	public void setCardExpiry(String cardExpiry) {
		this.cardExpiry = cardExpiry;
	}

	public String getTransferLocator() {
		return transferLocator;
	}

	public void setTransferLocator(String transferLocator) {
		this.transferLocator = transferLocator;
	}

	public String getIsScheduled() {
		return isScheduled;
	}

	public void setIsScheduled(String isScheduled) {
		this.isScheduled = isScheduled;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getNoofRecurrences() {
		return noofRecurrences;
	}

	public void setNoofRecurrences(String noofRecurrences) {
		this.noofRecurrences = noofRecurrences;
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

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}
	
	
	public HashMap<String, Object> getExternalApiPayload() {
		return externalApiPayload;
	}

	public void setExternalApiPayload(HashMap<String, Object> externalApiPayload) {
		this.externalApiPayload = externalApiPayload;
	}

	public JSONObject getExternalServiceResponse() {
		return externalServiceResponse;
	}

	public void setExternalServiceResponse(JSONObject externalServiceResponse) {
		this.externalServiceResponse = externalServiceResponse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountHolderNumber == null) ? 0 : accountHolderNumber.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((payeName == null) ? 0 : payeName.hashCode());
		result = prime * result + ((transferLocator == null) ? 0 : transferLocator.hashCode());
		result = prime * result + ((accountCode == null) ? 0 : accountCode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((billerId == null) ? 0 : billerId.hashCode());
		result = prime * result + ((cardExpiry == null) ? 0 : cardExpiry.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((checkBackImage == null) ? 0 : checkBackImage.hashCode());
		result = prime * result + ((day1 == null) ? 0 : day1.hashCode());
		result = prime * result + ((day2 == null) ? 0 : day2.hashCode());
		result = prime * result + ((dbxtransactionId == null) ? 0 : dbxtransactionId.hashCode());
		result = prime * result + ((deliverBy == null) ? 0 : deliverBy.hashCode());
		result = prime * result + ((deliveryDate == null) ? 0 : deliveryDate.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
		result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
		result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
		result = prime * result + ((frequencyTypeId == null) ? 0 : frequencyTypeId.hashCode());
		result = prime * result + ((frequencyenddate == null) ? 0 : frequencyenddate.hashCode());
		result = prime * result + ((frequencystartdate == null) ? 0 : frequencystartdate.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((fromAccountType == null) ? 0 : fromAccountType.hashCode());
		result = prime * result + ((initiationId == null) ? 0 : initiationId.hashCode());
		result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((noofRecurrences == null) ? 0 : noofRecurrences.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((numberOfPayments == null) ? 0 : numberOfPayments.hashCode());
		result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((processingDate == null) ? 0 : processingDate.hashCode());
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((recipientId == null) ? 0 : recipientId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + ((toAccountType == null) ? 0 : toAccountType.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
		result = prime * result + ((transferId == null) ? 0 : transferId.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((legalEntityId == null) ? 0 : legalEntityId.hashCode());
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
		BillPayTransactionBackendDTO other = (BillPayTransactionBackendDTO) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

}