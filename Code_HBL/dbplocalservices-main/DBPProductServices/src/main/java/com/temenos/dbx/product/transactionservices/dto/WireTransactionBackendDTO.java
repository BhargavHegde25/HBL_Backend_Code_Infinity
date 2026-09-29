package com.temenos.dbx.product.transactionservices.dto;

import java.text.ParseException;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.HelperMethods;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class WireTransactionBackendDTO implements DBPDTO{

	private static final long serialVersionUID = -1415772032838912752L;
	
	private String transactionId;
	private String dbxtransactionId;
	private String status;
	
	private int wireFileExecution_id;
	
	//@JsonAlias({"notes"})
	private String transactionsNotes;
	
	//this is an alias for transactionsNotes property once moved to V9, remove this property
	private String notes;
	
	private double amount;
	private String fromAccountNumber;
	private String payeeAccountNumber;

	private String transactionType;
	private String payeeId;
	private String payeeCurrency;
	
	private String payeeName;
	private String payeeNickName;
	private String payeeType;
	private String wireAccountType;
	private String swiftCode;
	private String routingNumber;
	private String zipCode;
	private String cityName;
	private String state;
	private String country;
	private String payeeAddressLine1;
	private String payeeAddressLine2;
	private String bankName;
	private String internationalRoutingCode;
	private String bankAddressLine1;
	private String bankAddressLine2;
	private String bankCity;
	private String bankState;
	private String bankZip;
	private String serviceName;

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
	private String toAccountNumber;
	
	@JsonProperty("AccountHolderNumber")
	private String accountHolderNumber;
	
	@JsonProperty("PayeeName")
	private String payeName;
	
	private String profileId;
	private String cardNumber;
	private String cardExpiry;
	
	@JsonProperty("AccountHolderNumber")
	private String transferLocator;
	
	private String isScheduled;
	private String transferId;
	private String paymentId;
	private String noofRecurrences;
	private String scheduledDate;
	private String serviceCharge;
	private String convertedAmount;
	private String transactionAmount;
	private String paidBy;	


	public WireTransactionBackendDTO() {
		super();
	}

	public WireTransactionBackendDTO(String transactionId, String dbxtransactionId, int wireFileExecution_id,
			String transactionsNotes, String notes, double amount, String fromAccountNumber, String payeeAccountNumber,
			String transactionType, String payeeId, String payeeCurrency, String payeeName, String payeeNickName,
			String payeeType, String wireAccountType, String swiftCode, String routingNumber, String zipCode,
			String cityName, String state, String country, String payeeAddressLine1, String payeeAddressLine2,
			String bankName, String internationalRoutingCode, String bankAddressLine1, String bankAddressLine2,
			String bankCity, String bankState, String bankZip, String serviceName,	 String deliveryDate, String processingDate, String recipientId, String accountCode,
		 	String nickName, String fromAccountType, String day1, String day2, String toAccountType, String name,
			String numberOfPayments, String message, String frequency, String question, String answer, String checkBackImage,
			String accountHolderNumber, String profileId, String cardNumber, String cardExpiry, String transferLocator,
		 	String isScheduled, String transferId, String paymentId, String noofRecurrences,String scheduledDate, String payeName, String toAccountNumber,
		 	String serviceCharge, String convertedAmount, String transactionAmount, String paidBy, String status) {
		super();
		this.transactionId = transactionId;
		this.dbxtransactionId = dbxtransactionId;
		this.wireFileExecution_id = wireFileExecution_id;
		this.transactionsNotes = transactionsNotes;
		this.notes = notes;
		this.amount = amount;
		this.fromAccountNumber = fromAccountNumber;
		this.payeeAccountNumber = payeeAccountNumber;
		this.transactionType = transactionType;
		this.payeeId = payeeId;
		this.payeeCurrency = payeeCurrency;
		this.payeeName = payeeName;
		this.payeeNickName = payeeNickName;
		this.payeeType = payeeType;
		this.wireAccountType = wireAccountType;
		this.swiftCode = swiftCode;
		this.routingNumber = routingNumber;
		this.zipCode = zipCode;
		this.cityName = cityName;
		this.state = state;
		this.country = country;
		this.payeeAddressLine1 = payeeAddressLine1;
		this.payeeAddressLine2 = payeeAddressLine2;
		this.bankName = bankName;
		this.internationalRoutingCode = internationalRoutingCode;
		this.bankAddressLine1 = bankAddressLine1;
		this.bankAddressLine2 = bankAddressLine2;
		this.bankCity = bankCity;
		this.bankState = bankState;
		this.bankZip = bankZip;
		this.serviceName = serviceName;
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
		this.scheduledDate = scheduledDate;
		this.payeName = payeName;
		this.toAccountNumber = toAccountNumber;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.transactionAmount = transactionAmount;
		this.paidBy = paidBy;
		this.status = status;
	}

	public WireTransactionBackendDTO convert(WireTransactionDTO dto) {
		this.transactionId = dto.getTransactionId();
		this.wireFileExecution_id = dto.getWireFileExecution_id();
		this.transactionsNotes = dto.getNotes();
		this.amount = dto.getAmount();
		this.fromAccountNumber = dto.getFromAccountNumber();
		this.payeeAccountNumber = (dto.getPayeeAccountNumber() == null || (dto.getPayeeAccountNumber()).isEmpty())? dto.getToAccountNumber() : dto.getPayeeAccountNumber();
		this.transactionType = dto.getTransactionType();
		this.payeeId = dto.getPayeeId();
		this.payeeCurrency = dto.getPayeeCurrency();
		this.payeeName = dto.getPayeeName();
		this.payeeNickName = dto.getPayeeNickName();
		this.payeeType = dto.getPayeeType();
		this.wireAccountType = dto.getWireAccountType();
		this.swiftCode = dto.getSwiftCode();
		this.routingNumber = dto.getRoutingNumber();
		this.zipCode = dto.getZipCode();
		this.cityName = dto.getCityName();
		this.state = dto.getState();
		this.country = dto.getCountry();
		this.payeeAddressLine1 = dto.getPayeeAddressLine1();
		this.payeeAddressLine2 = dto.getPayeeAddressLine2();
		this.bankName = dto.getBankName();
		this.internationalRoutingCode = dto.getInternationalRoutingCode();
		this.bankAddressLine1 = dto.getBankAddressLine1();
		this.bankAddressLine2 = dto.getBankAddressLine2();
		this.bankCity = dto.getBankCity();
		this.bankState = dto.getBankState();
		this.bankZip = dto.getBankZip();
		this.dbxtransactionId = dto.getTransactionId();
		this.serviceName = dto.getFeatureActionId();
		this.transferLocator = dto.getTransactionId();
		this.isScheduled = dto.getIsScheduled();
		this.transferId = dto.getTransactionId();
		this.paymentId = dto.getTransactionId();
		this.frequency = dto.getFrequencyType();
		this.scheduledDate = (dto.getScheduledDate() == null || (dto.getScheduledDate()).isEmpty())?dto.getFrequencyStartDate():dto.getScheduledDate();
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
		this.accountHolderNumber = dto.getPayeeAccountNumber();
		this.payeeName = dto.getPayeeName();
		this.payeName = dto.getPayeeName();
		this.profileId = dto.getProfileId();
		this.cardNumber = dto.getCardNumber();
		this.cardExpiry = dto.getCardExpiry();
		this.serviceCharge = dto.getServiceCharge();
		this.convertedAmount = dto.getConvertedAmount();
		this.transactionAmount = dto.getTransactionAmount();
		this.paidBy = dto.getPaidBy();
		
		return this;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getPayeName() {
		return payeName;
	}

	public void setPayeName(String payeName) {
		this.payeName = payeName;
	}

	public String getDbxtransactionId() {
		return dbxtransactionId;
	}

	public void setDbxtransactionId(String dbxtransactionId) {
		this.dbxtransactionId = dbxtransactionId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = null;
		this.setTransactionsNotes(notes);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
		this.setDbxtransactionId(transactionId);
	}

	public String getTransactionsNotes() {
		return transactionsNotes;
	}

	public void setTransactionsNotes(String transactionsNotes) {
		this.transactionsNotes = transactionsNotes;
	}

	public int getWireFileExecution_id() {
		return wireFileExecution_id;
	}

	public void setWireFileExecution_id(int wireFileExecution_id) {
		this.wireFileExecution_id = wireFileExecution_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getPayeeAccountNumber() {
		return payeeAccountNumber;
	}

	public void setPayeeAccountNumber(String payeeAccountNumber) {
		this.payeeAccountNumber = payeeAccountNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public String getPayeeCurrency() {
		return payeeCurrency;
	}

	public void setPayeeCurrency(String payeeCurrency) {
		this.payeeCurrency = payeeCurrency;
	}


	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeNickName() {
		return payeeNickName;
	}

	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
	}

	public String getPayeeType() {
		return payeeType;
	}

	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}

	public String getWireAccountType() {
		return wireAccountType;
	}

	public void setWireAccountType(String wireAccountType) {
		this.wireAccountType = wireAccountType;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPayeeAddressLine1() {
		return payeeAddressLine1;
	}

	public void setPayeeAddressLine1(String payeeAddressLine1) {
		this.payeeAddressLine1 = payeeAddressLine1;
	}

	public String getPayeeAddressLine2() {
		return payeeAddressLine2;
	}

	public void setPayeeAddressLine2(String payeeAddressLine2) {
		this.payeeAddressLine2 = payeeAddressLine2;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getInternationalRoutingCode() {
		return internationalRoutingCode;
	}

	public void setInternationalRoutingCode(String internationalRoutingCode) {
		this.internationalRoutingCode = internationalRoutingCode;
	}

	public String getBankAddressLine1() {
		return bankAddressLine1;
	}

	public void setBankAddressLine1(String bankAddressLine1) {
		this.bankAddressLine1 = bankAddressLine1;
	}

	public String getBankAddressLine2() {
		return bankAddressLine2;
	}

	public void setBankAddressLine2(String bankAddressLine2) {
		this.bankAddressLine2 = bankAddressLine2;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankState() {
		return bankState;
	}

	public void setBankState(String bankState) {
		this.bankState = bankState;
	}

	public String getBankZip() {
		return bankZip;
	}

	public void setBankZip(String bankZip) {
		this.bankZip = bankZip;
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
		result = prime * result + ((bankAddressLine1 == null) ? 0 : bankAddressLine1.hashCode());
		result = prime * result + ((bankAddressLine2 == null) ? 0 : bankAddressLine2.hashCode());
		result = prime * result + ((bankCity == null) ? 0 : bankCity.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankState == null) ? 0 : bankState.hashCode());
		result = prime * result + ((bankZip == null) ? 0 : bankZip.hashCode());
		result = prime * result + ((cardExpiry == null) ? 0 : cardExpiry.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((checkBackImage == null) ? 0 : checkBackImage.hashCode());
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((day1 == null) ? 0 : day1.hashCode());
		result = prime * result + ((day2 == null) ? 0 : day2.hashCode());
		result = prime * result + ((dbxtransactionId == null) ? 0 : dbxtransactionId.hashCode());
		result = prime * result + ((deliveryDate == null) ? 0 : deliveryDate.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((fromAccountType == null) ? 0 : fromAccountType.hashCode());
		result = prime * result + ((internationalRoutingCode == null) ? 0 : internationalRoutingCode.hashCode());
		result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((noofRecurrences == null) ? 0 : noofRecurrences.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((numberOfPayments == null) ? 0 : numberOfPayments.hashCode());
		result = prime * result + ((payeeAccountNumber == null) ? 0 : payeeAccountNumber.hashCode());
		result = prime * result + ((payeeAddressLine1 == null) ? 0 : payeeAddressLine1.hashCode());
		result = prime * result + ((payeeAddressLine2 == null) ? 0 : payeeAddressLine2.hashCode());
		result = prime * result + ((payeeCurrency == null) ? 0 : payeeCurrency.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((payeeNickName == null) ? 0 : payeeNickName.hashCode());
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + ((payeeType == null) ? 0 : payeeType.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((processingDate == null) ? 0 : processingDate.hashCode());
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((recipientId == null) ? 0 : recipientId.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((toAccountType == null) ? 0 : toAccountType.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
		result = prime * result + ((transferId == null) ? 0 : transferId.hashCode());
		result = prime * result + ((wireAccountType == null) ? 0 : wireAccountType.hashCode());
		result = prime * result + wireFileExecution_id;
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		WireTransactionBackendDTO other = (WireTransactionBackendDTO) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

}