package com.temenos.dbx.product.transactionservices.dto;

import java.text.ParseException;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.constants.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class P2PTransactionDTO implements DBPDTO{

	/**
	 *
	 */
	private static final long serialVersionUID = -5265254626892277694L;
    @JsonAlias("Id")
	private String transactionId;
	private String referenceId;
	private String message;
	private String confirmationNumber;
	private String status;
	private String personId;
	private String requestId;
	
	private String featureActionId;
	private String transactionType;
	private String companyId;
	private String roleId;
	private String transactionCurrency;
	private String fromAccountCurrency;
	private String p2pContact;
	private String legalEntityId;
	
	//@JsonAlias({"frequencyType"})
	private String frequencyTypeId;
	
	//alias for frequencyTypeId, remove after upgrade to V9
	private String frequencyType;
	
	private String fromAccountNumber;
	private String toAccountNumber;
	private double amount;
	
	//@JsonAlias({"transactionsNotes"})
	private String notes;
	
	//alias for notes, remove after upgrade to V9
	private String transactionsNotes;
	
	//Alias for PayeeName({"payeeName"})
	@JsonProperty("PayeeName")
	private String payeName;
	
	private String transactionts;
	private String frequencyEndDate;
	
	private String numberOfRecurrences;
	private String scheduledDate;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private boolean softdeleteflag;

	private String processingDate;
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
	private String frequencyStartDate;
	private  String deliverBy;
	
	private String dbpErrCode;
	@JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	private String serviceCharge;
	private String convertedAmount;
	private String transactionAmount;
	private String paidBy;
	private String swiftCode;
	private String beneficiaryName;
	private String beneficiaryPhone;
	private String beneficiaryEmail;

	public P2PTransactionDTO() {
		super();
	}

	public P2PTransactionDTO(String transactionId, String referenceId, String message, String confirmationNumber,
			String status, String requestId, String featureActionId, String transactionType, String companyId, String roleId,
			String transactionCurrency, String fromAccountCurrency, String p2pContact, String frequencyTypeId,
			String fromAccountNumber, String toAccountNumber, double amount, String notes, String transactionts,
			String frequencyEndDate, String numberOfRecurrences, String scheduledDate, String createdby,
			String modifiedby, String createdts, String lastmodifiedts, String synctimestamp,
			boolean softdeleteflag, String frequencyType, String transactionsNotes, String personId,
			String processingDate, String fromNickName, String fromAccountType, String day1, String day2,
		 	String toAccountType, String payPersonName, String securityQuestion, String SecurityAnswer,
		 	String checkImageBack, String payeeName, String profileId, String cardNumber, String cardExpiry, String isScheduled,
			String frequencyStartDate, String deliverBy, String payeName, String dbpErrCode, String dbpErrMsg,
			String serviceCharge, String convertedAmount, String transactionAmount, String paidBy, String swiftCode,
			String beneficiaryName, String beneficiaryPhone, String beneficiaryEmail, String legalEntityId) {
		super();
		this.transactionId = transactionId;
		this.referenceId = referenceId;
		this.message = message;
		this.confirmationNumber = confirmationNumber;
		this.status = status;
		this.requestId = requestId;
		this.featureActionId = featureActionId;
		this.transactionType = transactionType;
		this.companyId = companyId;
		this.roleId = roleId;
		this.transactionCurrency = transactionCurrency;
		this.fromAccountCurrency = fromAccountCurrency;
		this.p2pContact = p2pContact;
		this.frequencyTypeId = frequencyTypeId;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
		this.notes = notes;
		this.transactionts = transactionts;
		this.frequencyEndDate = frequencyEndDate;
		this.numberOfRecurrences = numberOfRecurrences;
		this.scheduledDate = scheduledDate;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
		this.transactionsNotes = transactionsNotes;
		this.frequencyType = frequencyType;
		this.personId = personId;
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
		this.payeName = payeName;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.transactionAmount = transactionAmount;
		this.paidBy  = paidBy;
		this.beneficiaryName = beneficiaryName;
		this.beneficiaryPhone = beneficiaryPhone;
		this.beneficiaryEmail = beneficiaryEmail;
		this.swiftCode = swiftCode;
		this.legalEntityId = legalEntityId;
		
	}
	
	public P2PTransactionDTO updateValues(P2PTransactionDTO dto) {
		try {
			dto.p2pContact = (dto.p2pContact != null && !"".equals(dto.p2pContact)) ? dto.p2pContact : this.p2pContact;
			dto.personId = (dto.personId != null && !"".equals(dto.personId)) ? dto.personId : this.personId;
			dto.fromAccountCurrency = (dto.fromAccountCurrency != null && !"".equals(dto.fromAccountCurrency)) ? dto.fromAccountCurrency : this.fromAccountCurrency;
			dto.transactionCurrency = (dto.transactionCurrency != null && !"".equals(dto.transactionCurrency)) ? dto.transactionCurrency : this.transactionCurrency;
			dto.frequencyTypeId = (dto.frequencyTypeId != null && !"".equals(dto.frequencyTypeId)) ? dto.frequencyTypeId : this.frequencyTypeId;
			dto.fromAccountNumber = (dto.fromAccountNumber != null && !"".equals(dto.fromAccountNumber)) ? dto.fromAccountNumber : this.fromAccountNumber;
			dto.toAccountNumber = (dto.toAccountNumber != null && !"".equals(dto.toAccountNumber)) ? dto.toAccountNumber : this.toAccountNumber;
			dto.scheduledDate = (dto.scheduledDate != null && !"".equals(dto.scheduledDate)) ? dto.scheduledDate : HelperMethods.convertDateFormat(this.scheduledDate, Constants.TIMESTAMP_FORMAT);
			dto.notes = (dto.notes != null && !"".equals(dto.notes)) ? dto.notes : this.notes;
			dto.frequencyEndDate = (dto.frequencyEndDate != null && !"".equals(dto.frequencyEndDate)) ? dto.frequencyEndDate : HelperMethods.convertDateFormat(this.frequencyEndDate, Constants.TIMESTAMP_FORMAT);
			dto.numberOfRecurrences = (dto.numberOfRecurrences != null && !"".equals(dto.numberOfRecurrences)) ? dto.numberOfRecurrences : this.numberOfRecurrences;
			dto.amount = dto.amount != 0 ? dto.amount : this.amount;
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
			dto.transactionAmount = (dto.transactionAmount != null && !"".equals(dto.transactionAmount)) ? dto.transactionAmount : this.transactionAmount;
			dto.paidBy = (dto.paidBy != null && !"".equals(dto.paidBy)) ? dto.paidBy : this.paidBy;
			dto.swiftCode = (dto.swiftCode != null && !"".equals(dto.swiftCode)) ? dto.swiftCode : this.swiftCode;
			dto.beneficiaryName = (dto.beneficiaryName != null && !"".equals(dto.beneficiaryName)) ? dto.beneficiaryName : this.beneficiaryName;
			dto.beneficiaryPhone = (dto.beneficiaryPhone != null && !"".equals(dto.beneficiaryPhone)) ? dto.beneficiaryPhone : this.beneficiaryPhone;
			dto.beneficiaryEmail = (dto.beneficiaryEmail != null && !"".equals(dto.beneficiaryEmail)) ? dto.beneficiaryEmail : this.beneficiaryEmail;
			dto.legalEntityId = (dto.legalEntityId != null && !"".equals(dto.legalEntityId)) ? dto.legalEntityId : this.legalEntityId;
			
		} catch (ParseException e) {
			return null;
		}
		
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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(String fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}

	public String getP2pContact() {
		return p2pContact;
	}

	public void setP2pContact(String p2pContact) {
		this.p2pContact = p2pContact;
	}

	public String getFrequencyTypeId() {
		return frequencyTypeId;
	}

	public void setFrequencyTypeId(String frequencyTypeId) {
		this.frequencyTypeId = frequencyTypeId;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public String getFrequencyEndDate() {
		return frequencyEndDate;
	}

	public void setFrequencyEndDate(String frequencyEndDate) {
		if("".equals(frequencyEndDate)) {
			frequencyEndDate = null;
		}
		this.frequencyEndDate = frequencyEndDate;
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
		if("".equals(scheduledDate)) {
			scheduledDate = null;
		}
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

	public String getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(String processingDate) {
		this.processingDate = processingDate;
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

	public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryPhone() {
        return beneficiaryPhone;
    }

    public void setBeneficiaryPhone(String beneficiaryPhone) {
        this.beneficiaryPhone = beneficiaryPhone;
    }

    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }

    public void setBeneficiaryEmail(String beneficiaryEmail) {
        this.beneficiaryEmail = beneficiaryEmail;
    }
    
    public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SecurityAnswer == null) ? 0 : SecurityAnswer.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
		result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
		result = prime * result + ((frequencyTypeId == null) ? 0 : frequencyTypeId.hashCode());
		result = prime * result + ((fromAccountCurrency == null) ? 0 : fromAccountCurrency.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((fromAccountType == null) ? 0 : fromAccountType.hashCode());
		result = prime * result + ((fromNickName == null) ? 0 : fromNickName.hashCode());
		result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
		result = prime * result + ((p2pContact == null) ? 0 : p2pContact.hashCode());
		result = prime * result + ((payPersonName == null) ? 0 : payPersonName.hashCode());
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
		result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
		result = prime * result + ((deliverBy == null) ? 0 : deliverBy.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((beneficiaryPhone == null) ? 0 : beneficiaryPhone.hashCode());
		result = prime * result + ((beneficiaryEmail == null) ? 0 : beneficiaryEmail.hashCode());
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
		P2PTransactionDTO other = (P2PTransactionDTO) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

}