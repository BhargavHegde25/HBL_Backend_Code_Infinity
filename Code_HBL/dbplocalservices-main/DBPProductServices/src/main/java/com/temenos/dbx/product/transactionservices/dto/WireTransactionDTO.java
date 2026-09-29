package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class WireTransactionDTO implements DBPDTO{

	private static final long serialVersionUID = -29155091189876177L;

	private String referenceId;
	private String initiationId;
    @JsonAlias("Id")
	private String transactionId;
	private String confirmationNumber;
	private String message;
	private String companyId;
	private String roleId;
	private String requestId;
	private String status;
	
	private String featureActionId;
	
	private long onetime_id;
	private int wireFileExecution_id;
	private int wireTemplateExecution_id;
	private String transactionts;
	
	//@JsonAlias({"transactionsNotes"})
	private String notes;
	
	//this is an alias for notes property once moved to V9, remove this property
	private String transactionsNotes;
	
	//Alias for PayeeName({"payeeName"})
	@JsonProperty("PayeeName")
	private String payeName;
	
	private double amount;
	private String fromAccountNumber;
	private String payeeAccountNumber;
	
	private String transactionType;
	private String payeeId;
	private String payeeCurrency;
	
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private boolean softdeleteflag;
	
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

	private String processingDate;
	private String personId;
	private String fromNickName;
	private String fromAccountType;
	private String day1;
	private String day2;
	private String toAccountType;
	private String payPersonName;
	private String securityQuestion;
	private String SecurityAnswer;
	private String checkImageBack;
	private String profileId;
	private String cardNumber;
	private String cardExpiry;
	private String isScheduled;
	private String frequencyStartDate;
	private String deliverBy;
	private String frequencyType;
	private String scheduledDate;
	private String numberOfRecurrences;
	private String toAccountNumber;
	
	private String dbpErrCode;
	@JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	private String serviceCharge;
	private String convertedAmount;
	private String transactionAmount;
	private String transactionCurrency;
	private String paidBy;	

	public WireTransactionDTO() {
		super();
	}

	public WireTransactionDTO(String referenceId, String initiationId, String transactionId, String confirmationNumber,
			String message, String companyId, String roleId, String requestId, String status, String featureActionId, long onetime_id,
			int wireFileExecution_id, int wireTemplateExecution_id, String transactionts, String notes, String transactionsNotes, double amount,
			String fromAccountNumber, String payeeAccountNumber, String transactionType, String payeeId,
			String payeeCurrency, String createdby, String modifiedby, String createdts, String lastmodifiedts,
			String synctimestamp, boolean softdeleteflag, String payeeNickName, String payeeType,
			String wireAccountType, String swiftCode, String routingNumber, String zipCode, String cityName,
			String state, String country, String payeeAddressLine1, String payeeAddressLine2, String bankName,
			String internationalRoutingCode, String bankAddressLine1, String bankAddressLine2, String bankCity,
			String bankState, String bankZip,	 String processingDate, String personId, String fromNickName,
		  	String fromAccountType, String day1, String day2, String toAccountType, String payPersonName,
	  		String securityQuestion, String SecurityAnswer, String checkImageBack, String payeeName, String profileId,
		 	String cardNumber, String cardExpiry, String isScheduled,String frequencyType, String frequencyStartDate,
		  	String deliverBy,String scheduledDate,String numberOfRecurrences, String payeName, String toAccountNumber,
		  	String dbpErrCode, String dbpErrMsg, String serviceCharge, String convertedAmount, String transactionAmount, String paidBy,
		  	String transactionCurrency) {
		super();
		this.referenceId = referenceId;
		this.initiationId = initiationId;
		this.transactionId = transactionId;
		this.confirmationNumber = confirmationNumber;
		this.message = message;
		this.companyId = companyId;
		this.roleId = roleId;
		this.requestId = requestId;
		this.status = status;
		this.featureActionId = featureActionId;
		this.onetime_id = onetime_id;
		this.wireFileExecution_id = wireFileExecution_id;
		this.wireTemplateExecution_id = wireTemplateExecution_id;
		this.transactionts = transactionts;
		this.notes = notes;
		this.transactionsNotes = transactionsNotes;
		this.amount = amount;
		this.fromAccountNumber = fromAccountNumber;
		this.payeeAccountNumber = payeeAccountNumber;
		this.transactionType = transactionType;
		this.payeeId = payeeId;
		this.payeeCurrency = payeeCurrency;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
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
		this.processingDate	 = processingDate;
		this.personId		 = personId;
		this.fromNickName	 = fromNickName;
		this.fromAccountType = fromAccountType;
		this.day1  = day1;
		this.day2  = day2;
		this.toAccountType   = toAccountType;
		this.payPersonName	 = payPersonName;
		this.securityQuestion  = securityQuestion;
		this.SecurityAnswer	  = SecurityAnswer;
		this.checkImageBack	  = checkImageBack;
		this.payeeName = payeeName;
		this.profileId = profileId;
		this.cardNumber = cardNumber;
		this.cardExpiry	 = cardExpiry;
		this.isScheduled = isScheduled;
		this.frequencyStartDate= frequencyStartDate;
		this.deliverBy = deliverBy;
		this.frequencyType = frequencyType;
		this.scheduledDate = scheduledDate;
		this.numberOfRecurrences = numberOfRecurrences;
		this.payeName = payeName;
		this.toAccountNumber = toAccountNumber;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.transactionAmount = transactionAmount;
		this.paidBy  = paidBy;
		this.transactionCurrency = transactionCurrency;
	}

	public void addAll(OneTimePayeeDTO dto) {
		
		this.onetime_id = dto.getOnetime_id();
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
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	
	public String getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}

	public String getPayeName() {
		return payeName;
	}

	public void setPayeName(String payeName) {
		this.payeName = null;
		this.setPayeeName(payeName);
	}

	public String getTransactionsNotes() {
		return transactionsNotes;
	}

	public void setTransactionsNotes(String transactionsNotes) {
		this.transactionsNotes = null;
		this.setNotes(transactionsNotes);
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getInitiationId() {
		return initiationId;
	}

	public void setInitiationId(String initiationId) {
		this.initiationId = initiationId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeatureActionId() {
		return featureActionId;
	}

	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}

	public long getOnetime_id() {
		return onetime_id;
	}

	public void setOnetime_id(long onetime_id) {
		this.onetime_id = onetime_id;
	}

	public int getWireFileExecution_id() {
		return wireFileExecution_id;
	}

	public void setWireFileExecution_id(int wireFileExecution_id) {
		this.wireFileExecution_id = wireFileExecution_id;
	}
	
	public int getWireTemplateExecution_id() {
		return wireTemplateExecution_id;
	}

	public void setWireTemplateExecution_id(int wireTemplateExecution_id) {
		this.wireTemplateExecution_id = wireTemplateExecution_id;
	}

	public String getTransactionts() {
		return transactionts;
	}

	public void setTransactionts(String transactionts) {
		this.transactionts = transactionts;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public boolean getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(boolean softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
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

	public String getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(String processingDate) {
		this.processingDate = processingDate;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getFromNickName() {
		return fromNickName;
	}

	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
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

	public String getPayPersonName() {
		return payPersonName;
	}

	public void setPayPersonName(String payPersonName) {
		this.payPersonName = payPersonName;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer() {
		return SecurityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		SecurityAnswer = securityAnswer;
	}

	public String getCheckImageBack() {
		return checkImageBack;
	}

	public void setCheckImageBack(String checkImageBack) {
		this.checkImageBack = checkImageBack;
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

	public String getIsScheduled() {
		return isScheduled;
	}

	public void setIsScheduled(String isScheduled) {
		this.isScheduled = isScheduled;
	}

	public String getFrequencyStartDate() {
		return frequencyStartDate;
	}

	public void setFrequencyStartDate(String frequencyStartDate) {
		this.frequencyStartDate = frequencyStartDate;
	}

	public String getDeliverBy() {
		return deliverBy;
	}

	public void setDeliverBy(String deliverBy) {
		this.deliverBy = deliverBy;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getNumberOfRecurrences() {
		return numberOfRecurrences;
	}

	public void setNumberOfRecurrences (String noofRecurrences) {
		if("".equals(noofRecurrences)) {
			noofRecurrences = null;
		}
		this.numberOfRecurrences = noofRecurrences;
	}
	
	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
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

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SecurityAnswer == null) ? 0 : SecurityAnswer.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((bankAddressLine1 == null) ? 0 : bankAddressLine1.hashCode());
		result = prime * result + ((bankAddressLine2 == null) ? 0 : bankAddressLine2.hashCode());
		result = prime * result + ((bankCity == null) ? 0 : bankCity.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankState == null) ? 0 : bankState.hashCode());
		result = prime * result + ((bankZip == null) ? 0 : bankZip.hashCode());
		result = prime * result + ((cardExpiry == null) ? 0 : cardExpiry.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((checkImageBack == null) ? 0 : checkImageBack.hashCode());
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((day1 == null) ? 0 : day1.hashCode());
		result = prime * result + ((day2 == null) ? 0 : day2.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((fromAccountType == null) ? 0 : fromAccountType.hashCode());
		result = prime * result + ((fromNickName == null) ? 0 : fromNickName.hashCode());
		result = prime * result + ((initiationId == null) ? 0 : initiationId.hashCode());
		result = prime * result + ((internationalRoutingCode == null) ? 0 : internationalRoutingCode.hashCode());
		result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + (int) (onetime_id ^ (onetime_id >>> 32));
		result = prime * result + ((payPersonName == null) ? 0 : payPersonName.hashCode());
		result = prime * result + ((payeeAccountNumber == null) ? 0 : payeeAccountNumber.hashCode());
		result = prime * result + ((payeeAddressLine1 == null) ? 0 : payeeAddressLine1.hashCode());
		result = prime * result + ((payeeAddressLine2 == null) ? 0 : payeeAddressLine2.hashCode());
		result = prime * result + ((payeeCurrency == null) ? 0 : payeeCurrency.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((payeeNickName == null) ? 0 : payeeNickName.hashCode());
		result = prime * result + ((payeName == null) ? 0 : payeName.hashCode());
		result = prime * result + ((payeeType == null) ? 0 : payeeType.hashCode());
		result = prime * result + ((personId == null) ? 0 : personId.hashCode());
		result = prime * result + ((processingDate == null) ? 0 : processingDate.hashCode());
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + ((securityQuestion == null) ? 0 : securityQuestion.hashCode());
		result = prime * result + (softdeleteflag ? 1231 : 1237);
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((toAccountType == null) ? 0 : toAccountType.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
		result = prime * result + ((transactionts == null) ? 0 : transactionts.hashCode());
		result = prime * result + ((wireAccountType == null) ? 0 : wireAccountType.hashCode());
		result = prime * result + wireFileExecution_id;
		result = prime * result + wireTemplateExecution_id;
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
		result = prime * result + ((deliverBy == null) ? 0 : deliverBy.hashCode());
		result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
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
		WireTransactionDTO other = (WireTransactionDTO) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

}