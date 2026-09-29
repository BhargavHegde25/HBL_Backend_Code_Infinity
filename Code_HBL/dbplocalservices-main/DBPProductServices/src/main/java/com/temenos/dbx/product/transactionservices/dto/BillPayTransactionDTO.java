package com.temenos.dbx.product.transactionservices.dto;

import java.text.ParseException;
import java.util.HashMap;

import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.HelperMethods;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPayTransactionDTO implements DBPDTO {

	private static final long serialVersionUID = 7202932613050164264L;
    @JsonAlias("Id")
	private String transactionId;
	private String deliverBy;
	private String transactionCurrency;
	private String transactionType;
	private String referenceId;
	private String initiationId;
	private String message;
	private String zipCode;
	private String featureActionId;
	private String companyId;
	private String roleId;
	
	//@JsonAlias({"frequencyType"})
	private String frequencyTypeId;
	
	//Alias for frequencyTypeId
	private String frequencyType;
	
	private String payeeId;
	private String requestId;
	private String fromAccountNumber;
	private String toAccountNumber;
	private String billerId;
	private double amount;
	private String status;
	private String confirmationNumber;
	private String description;
	
	//@JsonAlias({"transactionsNotes"})
	private String notes;
	
	//Alias for notes({"transactionsNotes"})
	private String transactionsNotes;
	
	private String transactionts;
	
	//@JsonAlias({"frequencyStartDate"})
	private String frequencystartdate;
	
	//Alias for frequencystartdate({""})
	private String frequencyStartDate;
	
	//@JsonAlias({"frequencyEndDate"})
	private String frequencyenddate;
	
	//Alias for frequencyenddate({"frequencyEndDate"})
	private String frequencyEndDate;
	
	//Alias for PayeeName({"payeeName"})
	@JsonProperty("PayeeName")
	private String payeName;
	
	private String numberOfRecurrences;
	private String scheduledDate;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private boolean softdeleteflag;

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
	private String payeeName;
	private String profileId;
	private String cardNumber;
	private String cardExpiry;
	private String isScheduled;
	
	private String dbpErrCode;
	@JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	private String serviceCharge;
	private String convertedAmount;
	private String transactionAmount;
	private String paidBy;
	private String swiftCode;
	private String legalEntityId;
	private HashMap<String, Object> externalApiPayload;
	private JSONObject externalServiceResponse;
	private JSONObject serviceResponse;

	public BillPayTransactionDTO() {
		super();
	}

	public BillPayTransactionDTO(String transactionId, String deliverBy, String transactionCurrency,
			String transactionType, String referenceId, String initiationId, String message, String zipCode,
			String featureActionId, String companyId, String roleId, String frequencyTypeId, String frequencyType, String payeeId,
			String requestId, String fromAccountNumber, String toAccountNumber, String billerId, double amount,
			String status, String confirmationNumber, String description, String notes, String transactionsNotes,
			String transactionts, String frequencystartdate, String frequencyStartDate2, String frequencyenddate,
			String frequencyEndDate2, String numberOfRecurrences, String scheduledDate, String createdby,
			String modifiedby, String createdts, String lastmodifiedts, String synctimestamp,
			boolean softdeleteflag,	 String processingDate, String personId, String fromNickName,
		 	String fromAccountType, String day1, String day2, String toAccountType, String payPersonName,
		 	String securityQuestion, String SecurityAnswer, String checkImageBack, String payeeName, String profileId,
	 		String cardNumber, String cardExpiry, String isScheduled, String payeName, String dbpErrCode, String dbpErrMsg,
	 		String serviceCharge, String convertedAmount, String transactionAmount, String paidBy, String swiftCode, String legalEntityId,
	 		HashMap<String, Object> externalApiPayload, JSONObject externalServiceResponse) {
		super();
		this.transactionId = transactionId;
		this.deliverBy = deliverBy;
		this.transactionCurrency = transactionCurrency;
		this.transactionType = transactionType;
		this.referenceId = referenceId;
		this.initiationId = initiationId;
		this.message = message;
		this.zipCode = zipCode;
		this.featureActionId = featureActionId;
		this.companyId = companyId;
		this.roleId = roleId;
		this.frequencyTypeId = frequencyTypeId;
		this.frequencyType = frequencyType;
		this.payeeId = payeeId;
		this.requestId = requestId;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.billerId = billerId;
		this.amount = amount;
		this.status = status;
		this.confirmationNumber = confirmationNumber;
		this.description = description;
		this.notes = notes;
		this.transactionsNotes = transactionsNotes;
		this.transactionts = transactionts;
		this.frequencystartdate = frequencystartdate;
		frequencyStartDate = frequencyStartDate2;
		this.frequencyenddate = frequencyenddate;
		frequencyEndDate = frequencyEndDate2;
		this.numberOfRecurrences = numberOfRecurrences;
		this.scheduledDate = scheduledDate;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
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
		this.payeName = payeName;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.transactionAmount = transactionAmount;
		this.paidBy  = paidBy;
		this.swiftCode = swiftCode;
		this.legalEntityId = legalEntityId;
		this.externalApiPayload = externalApiPayload;
		this.externalServiceResponse = externalServiceResponse;
	}
	
	public BillPayTransactionDTO updateValues(BillPayTransactionDTO dto) {
		try {
			dto.deliverBy = (dto.deliverBy != null && !"".equals(dto.deliverBy)) ? dto.deliverBy : HelperMethods.convertDateFormat(this.deliverBy, Constants.TIMESTAMP_FORMAT);
			dto.transactionCurrency = (dto.transactionCurrency != null && !"".equals(dto.transactionCurrency)) ? dto.transactionCurrency : this.transactionCurrency;
			dto.zipCode = (dto.zipCode != null && !"".equals(dto.zipCode)) ? dto.zipCode : this.zipCode;
			dto.frequencyTypeId = (dto.frequencyTypeId != null && !"".equals(dto.frequencyTypeId)) ? dto.frequencyTypeId : this.frequencyTypeId;
			dto.payeeId = (dto.payeeId != null && !"".equals(dto.payeeId)) ? dto.payeeId : this.payeeId;
			dto.fromAccountNumber = (dto.fromAccountNumber != null && !"".equals(dto.fromAccountNumber)) ? dto.fromAccountNumber : this.fromAccountNumber;
			dto.toAccountNumber = (dto.toAccountNumber != null && !"".equals(dto.toAccountNumber)) ? dto.toAccountNumber : this.toAccountNumber;
			dto.billerId = (dto.billerId != null && !"".equals(dto.billerId)) ? dto.billerId : this.billerId;
			dto.amount = dto.amount != 0 ? dto.amount : this.amount;
			dto.notes = (dto.notes != null && !"".equals(dto.notes)) ? dto.notes : this.notes;
			dto.frequencystartdate = (dto.frequencystartdate != null && !"".equals(dto.frequencystartdate)) ? dto.frequencystartdate : HelperMethods.convertDateFormat(this.frequencystartdate, Constants.TIMESTAMP_FORMAT);
			dto.frequencyenddate = (dto.frequencyenddate != null && !"".equals(dto.frequencyenddate)) ? dto.frequencyenddate : HelperMethods.convertDateFormat(this.frequencyenddate, Constants.TIMESTAMP_FORMAT);
			dto.numberOfRecurrences = (dto.numberOfRecurrences != null && !"".equals(dto.numberOfRecurrences)) ? dto.numberOfRecurrences : this.numberOfRecurrences;
			dto.scheduledDate = (dto.scheduledDate != null && !"".equals(dto.scheduledDate)) ? dto.scheduledDate : HelperMethods.convertDateFormat(this.scheduledDate, Constants.TIMESTAMP_FORMAT);
			dto.featureActionId = this.featureActionId;
			dto.transactionType = this.transactionType;
			dto.processingDate = (dto.processingDate != null && !"".equals(dto.processingDate)) ? dto.processingDate : HelperMethods.convertDateFormat(this.processingDate, Constants.TIMESTAMP_FORMAT);
			dto.personId = (dto.personId != null && !"".equals(dto.personId)) ? dto.personId : this.personId;
			dto.fromNickName = (dto.fromNickName != null && !"".equals(dto.fromNickName)) ? dto.fromNickName : this.fromNickName;
			dto.fromAccountType = (dto.fromAccountType != null && !"".equals(dto.fromAccountType)) ? dto.fromAccountType : this.fromAccountType;
			dto.day1 = (dto.day1 != null && !"".equals(dto.day1)) ? dto.day1 : this.day1;
			dto.day2 = (dto.day2 != null && !"".equals(dto.day2)) ? dto.day2 : this.day2;
			dto.toAccountType = (dto.toAccountType != null && !"".equals(dto.toAccountType)) ? dto.toAccountType : this.toAccountType;
			dto.payPersonName = (dto.payPersonName != null && !"".equals(dto.payPersonName)) ? dto.payPersonName : this.payPersonName;
			dto.securityQuestion = (dto.securityQuestion != null && !"".equals(dto.securityQuestion)) ? dto.securityQuestion : this.securityQuestion;
			dto.SecurityAnswer = (dto.SecurityAnswer != null && !"".equals(dto.SecurityAnswer)) ? dto.SecurityAnswer : this.SecurityAnswer;
			dto.checkImageBack = (dto.checkImageBack != null && !"".equals(dto.checkImageBack)) ? dto.checkImageBack : this.checkImageBack;
			dto.payeeName = (dto.payeeName != null && !"".equals(dto.payeeName)) ? dto.payeeName : this.payeeName;
			dto.profileId = (dto.profileId != null && !"".equals(dto.profileId)) ? dto.profileId : this.profileId;
			dto.cardNumber = (dto.cardNumber != null && !"".equals(dto.cardNumber)) ? dto.cardNumber : this.cardNumber;
			dto.cardExpiry = (dto.cardExpiry != null && !"".equals(dto.cardExpiry)) ? dto.cardExpiry : this.cardExpiry;
			dto.isScheduled = (dto.isScheduled != null && !"".equals(dto.isScheduled)) ? dto.isScheduled : this.isScheduled;
			dto.transactionCurrency = (dto.transactionCurrency != null && !"".equals(dto.transactionCurrency)) ? dto.transactionCurrency : this.transactionCurrency;
			dto.paidBy = (dto.paidBy != null && !"".equals(dto.paidBy)) ? dto.paidBy : this.paidBy;
			dto.swiftCode = (dto.swiftCode != null && !"".equals(dto.swiftCode)) ? dto.swiftCode : this.swiftCode;
			dto.legalEntityId = (dto.legalEntityId != null && !"".equals(dto.legalEntityId)) ? dto.legalEntityId : this.legalEntityId;
			dto.externalApiPayload = (dto.externalApiPayload != null )? dto.externalApiPayload: this.externalApiPayload;
			dto.externalServiceResponse = (dto.externalServiceResponse != null )? dto.externalServiceResponse: this.externalServiceResponse;
			
		} catch (ParseException e) {
			return null;
		};

		return dto;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPayeName() {
		return payeName;
	}

	public void setPayeName(String payeName) {
		this.payeName = null;
		this.setPayeeName(payeName);
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = null;
		this.setFrequencyTypeId(frequencyType);
	}

	public String getTransactionsNotes() {
		return transactionsNotes;
	}

	public void setTransactionsNotes(String transactionsNotes) {
		this.transactionsNotes = null;
		this.setNotes(transactionsNotes);
	}

	public String getFrequencyStartDate() {
		return frequencyStartDate;
	}

	public void setFrequencyStartDate(String frequencyStartDate) {
		this.frequencyStartDate = null;
		this.setFrequencystartdate(frequencyStartDate);
	}

	public String getFrequencyEndDate() {
		return frequencyEndDate;
	}

	public void setFrequencyEndDate(String frequencyEndDate) {
		this.frequencyEndDate = null;
		this.setFrequencyenddate(frequencyEndDate);
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFeatureActionId() {
		return featureActionId;
	}

	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getFrequencyTypeId() {
		return frequencyTypeId;
	}

	public void setFrequencyTypeId(String frequencyTypeId) {
		this.frequencyTypeId = frequencyTypeId;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTransactionts() {
		return transactionts;
	}

	public void setTransactionts(String transactionts) {
		this.transactionts = transactionts;
	}

	public String getFrequencystartdate() {
		return frequencystartdate;
	}

	public void setFrequencystartdate(String frequencystartdate) {
		if("".equals(frequencystartdate)) {
			frequencystartdate = null;
		}
		this.frequencystartdate = frequencystartdate;
	}

	public String getFrequencyenddate() {
		return frequencyenddate;
	}

	public void setFrequencyenddate(String frequencyenddate) {
		if("".equals(frequencyenddate)) {
			frequencyenddate = null;
		}
		this.frequencyenddate = frequencyenddate;
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
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
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

	public boolean isSoftdeleteflag() {
		return softdeleteflag;
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

	public String getPayeeName() {
		return payeeName;
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

	public String getIsScheduled() {
		return isScheduled;
	}

	public void setIsScheduled(String isScheduled) {
		this.isScheduled = isScheduled;
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
	

	public JSONObject getServiceResponse() {
		return serviceResponse;
	}

	public void setServiceResponse(JSONObject serviceResponse) {
		this.serviceResponse = serviceResponse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SecurityAnswer == null) ? 0 : SecurityAnswer.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((billerId == null) ? 0 : billerId.hashCode());
		result = prime * result + ((cardExpiry == null) ? 0 : cardExpiry.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((checkImageBack == null) ? 0 : checkImageBack.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((day1 == null) ? 0 : day1.hashCode());
		result = prime * result + ((day2 == null) ? 0 : day2.hashCode());
		result = prime * result + ((deliverBy == null) ? 0 : deliverBy.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
		result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
		result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
		result = prime * result + ((frequencyTypeId == null) ? 0 : frequencyTypeId.hashCode());
		result = prime * result + ((frequencyenddate == null) ? 0 : frequencyenddate.hashCode());
		result = prime * result + ((frequencystartdate == null) ? 0 : frequencystartdate.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((fromAccountType == null) ? 0 : fromAccountType.hashCode());
		result = prime * result + ((fromNickName == null) ? 0 : fromNickName.hashCode());
		result = prime * result + ((initiationId == null) ? 0 : initiationId.hashCode());
		result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
		result = prime * result + ((payPersonName == null) ? 0 : payPersonName.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((payeName == null) ? 0 : payeName.hashCode());
		result = prime * result + ((personId == null) ? 0 : personId.hashCode());
		result = prime * result + ((processingDate == null) ? 0 : processingDate.hashCode());
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((securityQuestion == null) ? 0 : securityQuestion.hashCode());
		result = prime * result + (softdeleteflag ? 1231 : 1237);
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + ((toAccountType == null) ? 0 : toAccountType.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
		result = prime * result + ((transactionts == null) ? 0 : transactionts.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
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
		BillPayTransactionDTO other = (BillPayTransactionDTO) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}
	

}