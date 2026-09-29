package com.temenos.dbx.product.transactionservices.dto;

import java.text.ParseException;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.constants.Constants;


public class TransferDTO  implements DBPDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonAlias("Id")
	protected String transactionId;
	protected String referenceId;
	protected String message;
	protected String confirmationNumber;
	protected String status;
	protected String requestId;
	
	protected String featureActionId;
	protected String transactionType;
	protected String companyId;
	protected String roleId;
	protected String transactionCurrency;
	protected String fromAccountCurrency;
	protected String toAccountCurrency;
	
	//@JsonAlias({"frequencyType"})
	protected String frequencyTypeId;
	
	//alias for frequencyTypeId
	protected String frequencyType;
	
	protected String fromAccountNumber;
	protected String toAccountNumber;
	protected double amount;
	
	//@JsonAlias({"transactionsNotes"})
	protected String notes;
	
	//alias for notes
	protected String transactionsNotes;
	
	//Alias for PayeeName({"payeeName"})
	@JsonProperty("PayeeName")
	protected String payeName;
	
	protected String transactionts;
	protected String frequencyEndDate;
	
	protected String numberOfRecurrences;
	protected String scheduledDate;
	protected String createdby;
	protected String modifiedby;
	protected String createdts;
	protected String lastmodifiedts;
	protected String synctimestamp;
	protected boolean softdeleteflag;

	protected String processingDate;
	protected String personId;
	protected String fromNickName;
	protected String fromAccountType;
	protected String day1;
	protected String day2;
	protected String toAccountType;
	protected String payPersonName;
	protected String securityQuestion;
	protected String SecurityAnswer;
	protected String checkImageBack;
	protected String payeeName;
	protected String profileId;
	protected String cardNumber;
	protected String cardExpiry;
	protected String isScheduled;
	protected String frequencyStartDate;
	protected  String deliverBy;
	protected String overrides;
	protected String overrideList;
	protected String charges;
	protected String validate;
	protected String exchangeRate;
	protected String totalAmount;
	protected String serviceCharge;
	protected String convertedAmount;
	protected String transactionAmount;
	protected String paidBy;
	protected String swiftCode;
	protected String creditValueDate;
	
	protected String dbpErrCode;
	@JsonAlias({"errorMessage", "errmsg"})
	protected String dbpErrMsg;
	protected String errorDetails;
	protected String messageDetails;
	protected String paymentMethod;
	protected String zipCode;
	protected String pinCode;
	protected String paymentNote;
	
	//BackendDTO variables
	protected String serviceName;
	protected String dbxtransactionId;
	protected String transferLocator;
	protected String transferId;
	protected String paymentId;
	protected String frequency;
	protected String numberOfPayments;
	protected String noofRecurrences;
	protected String accountCode;
	protected String deliveryDate;
	protected String recipientId;
	protected String nickName;
	protected String name;
	protected String question;
	protected String answer;
	protected String checkBackImage;
	protected String accountHolderNumber;
	protected String beneficiaryId;
	protected String payeeId;
	protected String quoteCurrency;
	protected String legalEntityId;
	public TransferDTO() {
	    super();
	}
	
	//Update Values in DTO
	public TransferDTO updateValues(TransferDTO dto) {
		try {
			
			dto.pinCode=(dto.pinCode !=null && !"".equals(dto.pinCode))? dto.pinCode:this.pinCode;
			dto.zipCode=(dto.zipCode !=null && !"".equals(dto.zipCode))? dto.zipCode:this.zipCode;
			dto.fromAccountCurrency = (dto.fromAccountCurrency != null && !"".equals(dto.fromAccountCurrency)) ? dto.fromAccountCurrency : this.fromAccountCurrency;
			dto.transactionCurrency = (dto.transactionCurrency != null && !"".equals(dto.transactionCurrency)) ? dto.transactionCurrency : this.transactionCurrency;
			dto.frequencyTypeId = (dto.frequencyTypeId != null && !"".equals(dto.frequencyTypeId)) ? dto.frequencyTypeId : this.frequencyTypeId;
			dto.fromAccountNumber = (dto.fromAccountNumber != null && !"".equals(dto.fromAccountNumber)) ? dto.fromAccountNumber : this.fromAccountNumber;
			dto.toAccountNumber = (dto.toAccountNumber != null && !"".equals(dto.toAccountNumber)) ? dto.toAccountNumber : this.toAccountNumber;
			dto.scheduledDate = (dto.scheduledDate != null && !"".equals(dto.scheduledDate)) ? dto.scheduledDate : HelperMethods.convertDateFormat(this.scheduledDate, Constants.TIMESTAMP_FORMAT);
			dto.notes = (dto.notes != null && !"".equals(dto.notes)) ? dto.notes : null;
			dto.frequencyEndDate = (dto.frequencyEndDate != null && !"".equals(dto.frequencyEndDate)) ? dto.frequencyEndDate : null;
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
			dto.overrides = (dto.overrides != null && !"".equals(dto.overrides)) ? dto.overrides : this.overrides;
			dto.overrideList = (dto.overrideList != null && !"".equals(dto.overrideList)) ? dto.overrideList : this.overrideList;
			dto.charges = (dto.charges != null && !"".equals(dto.charges)) ? dto.charges : this.charges;
			dto.validate = (dto.validate != null && !"".equals(dto.validate)) ? dto.validate : this.validate;
			dto.exchangeRate = (dto.exchangeRate != null && !"".equals(dto.exchangeRate)) ? dto.exchangeRate : this.exchangeRate;
			dto.totalAmount = (dto.totalAmount != null && !"".equals(dto.totalAmount)) ? dto.totalAmount : this.totalAmount;
			dto.transactionCurrency = (dto.transactionCurrency != null && !"".equals(dto.transactionCurrency)) ? dto.transactionCurrency : this.transactionCurrency;
			dto.transactionAmount = (dto.transactionAmount != null && !"".equals(dto.transactionAmount)) ? dto.transactionAmount : this.transactionAmount;
			dto.paidBy = (dto.paidBy != null && !"".equals(dto.paidBy)) ? dto.paidBy : this.paidBy;
			dto.swiftCode = (dto.swiftCode != null && !"".equals(dto.swiftCode)) ? dto.swiftCode : this.swiftCode;
			dto.creditValueDate = (dto.creditValueDate != null && !"".equals(dto.creditValueDate)) ? dto.creditValueDate : this.creditValueDate;
			dto.paymentNote = (dto.paymentNote != null && !"".equals(dto.paymentNote)) ? dto.paymentNote : null;
			dto.quoteCurrency = (dto.quoteCurrency != null && !"".equals(dto.quoteCurrency)) ? dto.quoteCurrency : this.quoteCurrency;
        } catch (ParseException e) {
			return null;
		}
		
		return dto;
	}
	//Get set method for benificiary id and payee id from OwnAccountFundTransferBackendDTO
	public String getLegalEntityId() {
        return legalEntityId;
    }

    public void setLegalEntityId(String legalEntityId) {
        this.legalEntityId = legalEntityId;
    }
	public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }
    
	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
		this.payeeId = beneficiaryId;
	}

	public String getPayeeId() {
		return payeeId;
	}
 
	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
		this.beneficiaryId = payeeId;
	}
	public String getPaymentNote()
	{
		return paymentNote;
	}
	public void setPaymentNote(String paymentNote) {
		this.paymentNote=paymentNote;
	}
	public String getpinCode()
	{
		return pinCode;
	}
	public void setpinCode(String pinCode) {
		this.pinCode=pinCode;
	}
	public String getzipCode(){
		return zipCode;
	}
	public void setzipCode(String zipCode) {
		this.zipCode=zipCode;
	}
	public String getpaymentMethod(){
		return paymentMethod;
	}
	public void setpaymentMethod(String paymentMethod) {
		this.paymentMethod=paymentMethod;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	
	public String getOverrides() {
		return overrides;
	}

	public void setOverrides(String overrides) {
		this.overrides = overrides;
	}
	
	public String getOverrideList() {
		return overrideList;
	}

	public void setOverrideList(String overrideList) {
		this.overrideList = overrideList;
	}

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
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
		this.transactionsNotes = transactionsNotes;
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

	public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

	public String getToAccountCurrency() {
		return toAccountCurrency;
	}

	public void setToAccountCurrency(String toAccountCurrency) {
		this.toAccountCurrency = toAccountCurrency;
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

	public String getCreditValueDate() {
	    try {
            return HelperMethods.convertDateFormat(creditValueDate, Constants.TIMESTAMP_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setCreditValueDate(String creditValueDate) {
        this.creditValueDate = creditValueDate;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(String messageDetails) {
        this.messageDetails = messageDetails;
    }
    
    public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	//TransferDTO

	public TransferDTO(String transactionId, String referenceId, String message, String confirmationNumber,
			String status, String requestId, String featureActionId, String transactionType, String companyId, String roleId,
			String transactionCurrency, String fromAccountCurrency, String toAccountCurrency, String frequencyTypeId,
			String frequencyType, String fromAccountNumber, String toAccountNumber, double amount, String notes,
			String transactionsNotes, String transactionts, String frequencyEndDate, String numberOfRecurrences,
			String scheduledDate, String createdby, String modifiedby, String createdts, String lastmodifiedts,
			String synctimestamp, boolean softdeleteflag,	 String processingDate, String personId, String fromNickName,
		 	String fromAccountType, String day1, String day2, String toAccountType, String payPersonName,
		 	String securityQuestion, String SecurityAnswer, String checkImageBack, String payeeName, String profileId,
			String cardNumber, String cardExpiry, String isScheduled, String frequencyStartDate, String deliverBy, String payeName, String dbpErrCode, String dbpErrMsg, String overrides, String charges, String validate, String exchangeRate, String totalAmount,
			String serviceCharge, String convertedAmount, String transactionAmount, String paidBy, String swiftCode, String overrideList, String creditValueDate, String errorDetails, String messageDetails,String zipCode,String pinCode,String paymentNote,String paymentMethod,String beneficiaryId,
	String payeeId,String quoteCurrency,String legalEntityId) {
		super();
		this.legalEntityId = legalEntityId;
		this.quoteCurrency=quoteCurrency;
		this.beneficiaryId = beneficiaryId;
		this.payeeId = payeeId;
		this.paymentNote=paymentNote;
		this.pinCode=pinCode;
		this.zipCode=zipCode;
		this.exchangeRate = exchangeRate;
		this.totalAmount = totalAmount;
		this.overrides = overrides;
		this.overrideList = overrideList;
		this.charges = charges;
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
		this.toAccountCurrency = toAccountCurrency;
		this.frequencyTypeId = frequencyTypeId;
		this.frequencyType = frequencyType;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
		this.notes = notes;
		this.transactionsNotes = transactionsNotes;
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
		this.validate = validate;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.serviceCharge = serviceCharge;
		this.convertedAmount = convertedAmount;
		this.transactionAmount = transactionAmount;
		this.paidBy  = paidBy;
		this.swiftCode = swiftCode;
		this.creditValueDate = creditValueDate;
		this.errorDetails = errorDetails;
		this.messageDetails = messageDetails;
		this.paymentMethod= paymentMethod;
	}
    
    
    //HashCodes generation
    
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((SecurityAnswer == null) ? 0 : SecurityAnswer.hashCode());
	long temp;
	temp = Double.doubleToLongBits(amount);
	//benificiary id and payee id hash code generation
	
	result = prime * result + ((beneficiaryId == null) ? 0 : beneficiaryId.hashCode());
	result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
	
	//other hashcode
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
	result = prime * result + ((toAccountCurrency == null) ? 0 : toAccountCurrency.hashCode());
	result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
	result = prime * result + ((toAccountType == null) ? 0 : toAccountType.hashCode());
	result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
	result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
	result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
	result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
	result = prime * result + ((transactionts == null) ? 0 : transactionts.hashCode());
	result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
	result = prime * result + ((deliverBy == null) ? 0 : deliverBy.hashCode());
	result = prime * result + ((overrides == null) ? 0 : overrides.hashCode());
	result = prime * result + ((overrideList == null) ? 0 : overrideList.hashCode());
	result = prime * result + ((charges == null) ? 0 : charges.hashCode());
	result = prime * result + ((validate == null) ? 0 : validate.hashCode());
	result = prime * result + ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
	result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
	result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
	result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
	result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
	result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
	result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
	result = prime * result + ((creditValueDate == null) ? 0 : creditValueDate.hashCode());
	result = prime * result + ((errorDetails == null) ? 0 : errorDetails.hashCode());
	result = prime * result + ((messageDetails == null) ? 0 : messageDetails.hashCode());
	result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
	result = prime * result + ((pinCode == null) ? 0 : pinCode.hashCode());
	result = prime * result + ((paymentNote == null) ? 0 : paymentNote.hashCode());
	result = prime * result + ((quoteCurrency == null) ? 0 : quoteCurrency.hashCode());
	result = prime * result + ((legalEntityId == null) ? 0 : legalEntityId.hashCode());
	return result;
}

//Convert operation in Backend DTO
    
public TransferDTO convert(TransferDTO dto) {
		
		this.setFrequencyType(dto.getFrequencyTypeId());
		this.paymentNote=dto.getPaymentNote();
		this.pinCode=dto.getpinCode();
		this.zipCode=dto.getzipCode();
		this.transactionId = dto.getTransactionId();
		this.featureActionId = dto.getFeatureActionId();
		this.companyId = dto.getCompanyId();
		this.transactionCurrency = dto.getTransactionCurrency();
		this.fromAccountCurrency = dto.getFromAccountCurrency();
		this.frequencyType = dto.getFrequencyTypeId();
		this.fromAccountNumber = dto.getFromAccountNumber();
		this.toAccountNumber = dto.getToAccountNumber();
		this.amount = dto.getAmount();
		this.transactionsNotes = dto.getNotes();
		this.transactionts = dto.getTransactionts();
		this.frequencyEndDate = dto.getFrequencyEndDate();
		this.numberOfRecurrences = dto.getNumberOfRecurrences();
		this.scheduledDate = (dto.getScheduledDate() == null || (dto.getScheduledDate()).isEmpty())?dto.getFrequencyStartDate():dto.getScheduledDate();
		this.toAccountCurrency = dto.getToAccountCurrency();
		this.transactionType = dto.getTransactionType();
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
        this.validate = dto.getValidate();
		this.overrides = dto.getOverrides();
		this.overrideList = dto.getOverrideList();
		this.charges = dto.getCharges();
		this.exchangeRate = dto.getExchangeRate();
		this.totalAmount = dto.getTotalAmount();
		this.serviceCharge = dto.getServiceCharge();
		this.convertedAmount = dto.getConvertedAmount();
		this.transactionAmount = dto.getTransactionAmount();
		this.swiftCode = dto.getSwiftCode();
		this.paidBy = dto.getPaidBy();
		this.creditValueDate = dto.getCreditValueDate();
		this.legalEntityId = dto.getLegalEntityId();
		return this;
	}


public boolean equals(Object obj) {
	
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	TransferDTO other = (TransferDTO) obj;
	if (transactionId != other.transactionId)
		return false;
	return true;
}

}
