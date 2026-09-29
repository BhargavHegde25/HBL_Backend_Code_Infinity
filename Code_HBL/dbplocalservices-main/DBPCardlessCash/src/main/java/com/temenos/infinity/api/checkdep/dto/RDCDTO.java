package com.temenos.infinity.api.checkdep.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class RDCDTO implements DBPDTO{

	private static final long serialVersionUID = -3729887742298002379L;

	
	@JsonAlias({"Amount","amount"})
	private String amount;
	
	@JsonAlias({"Amount","amount"})
	@JsonProperty("Amount")
	private String amt;
	
	private String transactionsNotes;
	private String scheduledDate;
	private String fromAccountNumber;
	private String toAccountNumber;
	private String toAccountType;
	private String profileId;
	private String isScheduled;
	private String transactionType;
	private String personId;
	private String payeeId;
	private String frequencyStartDate;
	private String frequencyEndDate;
	private String numberOfRecurrences;
	private String frequencyType;
	private String checkImage;
	private String checkImageBack;
	private String cashlessMode;
	private String cashlessPersonName;
	private String cashWithdrawalTransactionStatus;
	private String category;
	private String p2pRequiredDate;
	private String p2pContact;
	private String penaltyFlag;
	private String payoffFlag;
	private String overdraft;
	private String fromAccountBalance;
	private String payeeCurrency;
	private String payeeName;
	private String payeeAccountNumber;
	private String zipCode;
	private String payeeNickName;
	private String cityName;
	private String state;
	private String payeeAddressLine1;
	private String addressLine2;
	private String payeeType;
	private String country;
	private String swiftCode;
	private String routingNumber;
	private String internationalRoutingCode;
	private String bankName;
	private String bankAddressLine1;
	private String bankAddressLine2;
	private String bankCity;
	private String bankState;
	private String bankZip;
	private String IBAN;

	private String wireAccountType;
	private String checkNumber1;
	private String checkNumber2;
	private String checkDateOfIssue;
	private String checkReason;
	private String requestValidityInMonths;
	private String feeCurrency;
	private String feePaidByReceipent;
	private String convertedAmount;
	private String transactionCurrency;
	private String baseCurrency;
	private String beneficiaryName;

	private String consentid;
	private String ConsentId;

	private String initiation_instructionIdentification;
	private String Initiation_InstructionIdentification;

	private String debtorAccountSchemeName;
	private String debtorAccountName;
	private String creditorAccountSchemeName;

	private String ri_unstructured;
	private String RI_Unstructured;

	private String ri_reference;
	private String RI_Reference;

	private String initiation_endToEndIdentification;
	private String Initiation_EndToEndIdentification;

	private String riskpaymentContextCode;
	private String RiskPaymentContextCode;

	private String serviceName;
	private String payPersonName;
	private String payPersonNickName;
	private String p2pAlternateContact;
	private String billerId;
	private String errmsg;
	private String success;
	private String referenceId;
	private String otp;
	private String validDate;
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	
	public RDCDTO() {
		super();
	}
	
	public RDCDTO(String amount, String amt, String transactionsNotes, String scheduledDate, String fromAccountNumber,
			String toAccountNumber,String toAccountType, String profileId, String isScheduled, String transactionType, String personId, String payeeId,
			String frequencyStartDate, String frequencyEndDate, String numberOfRecurrences, String frequencyType,
			String checkImage, String checkImageBack, String cashlessMode, String cashlessPersonName,
			String cashWithdrawalTransactionStatus, String cashlessSecurityCode, String cashlessEmail,
			String cashlessPhone, String cashlessPin, String billid, String category, String p2pRequiredDate,
			String p2pContact, String penaltyFlag, String payoffFlag, String overdraft, String fromAccountBalance,
			String payeeCurrency, String payeeName, String payeeAccountNumber, String zipCode, String payeeNickName,
			String cityName, String state, String payeeAddressLine1, String addressLine2, String payeeType,
			String country, String swiftCode, String routingNumber, String internationalRoutingCode, String bankName,
			String bankAddressLine1, String bankAddressLine2, String bankCity, String bankState, String bankZip,
			String iBAN, String wireAccountType, String checkNumber1, String checkNumber2,
			String checkDateOfIssue, String checkReason, String requestValidityInMonths, String feeCurrency,
			String feePaidByReceipent, String convertedAmount, String transactionCurrency, String baseCurrency,
			String beneficiaryName, String consentid, String consentId2, String initiation_instructionIdentification,
			String initiation_InstructionIdentification2, String debtorAccountSchemeName, String debtorAccountName,
			String creditorAccountSchemeName, String ri_unstructured, String rI_Unstructured2, String ri_reference,
			String rI_Reference2, String initiation_endToEndIdentification, String initiation_EndToEndIdentification2,
			String riskpaymentContextCode, String riskPaymentContextCode2,String serviceName, String payPersonName,
			String payPersonNickName, String p2pAlternateContact,String billerId, String errmsg, String success,
			String referenceId, String otp, String validDate, ErrorCodeEnum DbpErrorCode, String DbpErrMsg) {
		super();
		this.amount = amount;
		this.amt = amt;
		this.transactionsNotes = transactionsNotes;
		this.scheduledDate = scheduledDate;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.toAccountType = toAccountType;
		this.profileId = profileId;
		this.isScheduled = isScheduled;
		this.transactionType = transactionType;
		this.personId = personId;
		this.payeeId = payeeId;
		this.frequencyStartDate = frequencyStartDate;
		this.frequencyEndDate = frequencyEndDate;
		this.numberOfRecurrences = numberOfRecurrences;
		this.frequencyType = frequencyType;
		this.checkImage = checkImage;
		this.checkImageBack = checkImageBack;
		this.cashlessMode = cashlessMode;
		this.cashlessPersonName = cashlessPersonName;
		this.cashWithdrawalTransactionStatus = cashWithdrawalTransactionStatus;
		this.category = category;
		this.p2pRequiredDate = p2pRequiredDate;
		this.p2pContact = p2pContact;
		this.penaltyFlag = penaltyFlag;
		this.payoffFlag = payoffFlag;
		this.overdraft = overdraft;
		this.fromAccountBalance = fromAccountBalance;
		this.payeeCurrency = payeeCurrency;
		this.payeeName = payeeName;
		this.payeeAccountNumber = payeeAccountNumber;
		this.zipCode = zipCode;
		this.payeeNickName = payeeNickName;
		this.cityName = cityName;
		this.state = state;
		this.payeeAddressLine1 = payeeAddressLine1;
		this.addressLine2 = addressLine2;
		this.payeeType = payeeType;
		this.country = country;
		this.swiftCode = swiftCode;
		this.routingNumber = routingNumber;
		this.internationalRoutingCode = internationalRoutingCode;
		this.bankName = bankName;
		this.bankAddressLine1 = bankAddressLine1;
		this.bankAddressLine2 = bankAddressLine2;
		this.bankCity = bankCity;
		this.bankState = bankState;
		this.bankZip = bankZip;
		this.IBAN = iBAN;
		this.wireAccountType = wireAccountType;
		this.checkNumber1 = checkNumber1;
		this.checkNumber2 = checkNumber2;
		this.checkDateOfIssue = checkDateOfIssue;
		this.checkReason = checkReason;
		this.requestValidityInMonths = requestValidityInMonths;
		this.feeCurrency = feeCurrency;
		this.feePaidByReceipent = feePaidByReceipent;
		this.convertedAmount = convertedAmount;
		this.transactionCurrency = transactionCurrency;
		this.baseCurrency = baseCurrency;
		this.beneficiaryName = beneficiaryName;
		this.consentid = consentid;
		this.ConsentId = consentId2;
		this.initiation_instructionIdentification = initiation_instructionIdentification;
		this.Initiation_InstructionIdentification = initiation_InstructionIdentification2;
		this.debtorAccountSchemeName = debtorAccountSchemeName;
		this.debtorAccountName = debtorAccountName;
		this.creditorAccountSchemeName = creditorAccountSchemeName;
		this.ri_unstructured = ri_unstructured;
		this.RI_Unstructured = rI_Unstructured2;
		this.ri_reference = ri_reference;
		this.RI_Reference = rI_Reference2;
		this.initiation_endToEndIdentification = initiation_endToEndIdentification;
		this.Initiation_EndToEndIdentification = initiation_EndToEndIdentification2;
		this.riskpaymentContextCode = riskpaymentContextCode;
		this.RiskPaymentContextCode = riskPaymentContextCode2;
		this.serviceName = serviceName;
		this.payPersonName = payPersonName;
		this.payPersonNickName = payPersonNickName;
		this.p2pAlternateContact = p2pAlternateContact;
		this.billerId = billerId;
		this.errmsg = errmsg;
		this.success = success;
		this.referenceId = referenceId;
		this.otp = otp;
		this.validDate = validDate;
		this.dbpErrorCode = DbpErrorCode;
        this.dbpErrMsg = DbpErrMsg;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getTransactionsNotes() {
		return transactionsNotes;
	}

	public void setTransactionsNotes(String transactionsNotes) {
		this.transactionsNotes = transactionsNotes;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
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

	public String getIsScheduled() {
		return isScheduled;
	}

	public void setIsScheduled(String isScheduled) {
		this.isScheduled = isScheduled;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public String getFrequencyStartDate() {
		return frequencyStartDate;
	}

	public void setFrequencyStartDate(String frequencyStartDate) {
		this.frequencyStartDate = frequencyStartDate;
	}

	public String getFrequencyEndDate() {
		return frequencyEndDate;
	}

	public void setFrequencyEndDate(String frequencyEndDate) {
		this.frequencyEndDate = frequencyEndDate;
	}

	public String getNumberOfRecurrences() {
		return numberOfRecurrences;
	}

	public void setNumberOfRecurrences(String numberOfRecurrences) {
		this.numberOfRecurrences = numberOfRecurrences;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getCheckImage() {
		return checkImage;
	}

	public void setCheckImage(String checkImage) {
		this.checkImage = checkImage;
	}

	public String getCheckImageBack() {
		return checkImageBack;
	}

	public void setCheckImageBack(String checkImageBack) {
		this.checkImageBack = checkImageBack;
	}

	public String getCashlessMode() {
		return cashlessMode;
	}

	public void setCashlessMode(String cashlessMode) {
		this.cashlessMode = cashlessMode;
	}

	public String getCashlessPersonName() {
		return cashlessPersonName;
	}

	public void setCashlessPersonName(String cashlessPersonName) {
		this.cashlessPersonName = cashlessPersonName;
	}

	public String getCashWithdrawalTransactionStatus() {
		return cashWithdrawalTransactionStatus;
	}

	public void setCashWithdrawalTransactionStatus(String cashWithdrawalTransactionStatus) {
		this.cashWithdrawalTransactionStatus = cashWithdrawalTransactionStatus;
	}
	
	public String getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getP2pRequiredDate() {
		return p2pRequiredDate;
	}

	public void setP2pRequiredDate(String p2pRequiredDate) {
		this.p2pRequiredDate = p2pRequiredDate;
	}

	public String getP2pContact() {
		return p2pContact;
	}

	public void setP2pContact(String p2pContact) {
		this.p2pContact = p2pContact;
	}

	public String getPenaltyFlag() {
		return penaltyFlag;
	}

	public void setPenaltyFlag(String penaltyFlag) {
		this.penaltyFlag = penaltyFlag;
	}

	public String getPayoffFlag() {
		return payoffFlag;
	}

	public void setPayoffFlag(String payoffFlag) {
		this.payoffFlag = payoffFlag;
	}

	public String getOverdraft() {
		return overdraft;
	}

	public void setOverdraft(String overdraft) {
		this.overdraft = overdraft;
	}

	public String getFromAccountBalance() {
		return fromAccountBalance;
	}

	public void setFromAccountBalance(String fromAccountBalance) {
		this.fromAccountBalance = fromAccountBalance;
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

	public String getPayeeAccountNumber() {
		return payeeAccountNumber;
	}

	public void setPayeeAccountNumber(String payeeAccountNumber) {
		this.payeeAccountNumber = payeeAccountNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPayeeNickName() {
		return payeeNickName;
	}

	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
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

	public String getPayeeAddressLine1() {
		return payeeAddressLine1;
	}

	public void setPayeeAddressLine1(String payeeAddressLine1) {
		this.payeeAddressLine1 = payeeAddressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getPayeeType() {
		return payeeType;
	}

	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getInternationalRoutingCode() {
		return internationalRoutingCode;
	}

	public void setInternationalRoutingCode(String internationalRoutingCode) {
		this.internationalRoutingCode = internationalRoutingCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getIBAN() {
		return this.IBAN;
	}

	public void setIBAN(String iBAN) {
		this.IBAN = iBAN;
	}

	public String getWireAccountType() {
		return wireAccountType;
	}

	public void setWireAccountType(String wireAccountType) {
		this.wireAccountType = wireAccountType;
	}

	public String getCheckNumber1() {
		return checkNumber1;
	}

	public void setCheckNumber1(String checkNumber1) {
		this.checkNumber1 = checkNumber1;
	}

	public String getCheckNumber2() {
		return checkNumber2;
	}

	public void setCheckNumber2(String checkNumber2) {
		this.checkNumber2 = checkNumber2;
	}

	public String getCheckDateOfIssue() {
		return checkDateOfIssue;
	}

	public void setCheckDateOfIssue(String checkDateOfIssue) {
		this.checkDateOfIssue = checkDateOfIssue;
	}

	public String getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(String checkReason) {
		this.checkReason = checkReason;
	}

	public String getRequestValidityInMonths() {
		return requestValidityInMonths;
	}

	public void setRequestValidityInMonths(String requestValidityInMonths) {
		this.requestValidityInMonths = requestValidityInMonths;
	}

	public String getFeeCurrency() {
		return feeCurrency;
	}

	public void setFeeCurrency(String feeCurrency) {
		this.feeCurrency = feeCurrency;
	}

	public String getFeePaidByReceipent() {
		return feePaidByReceipent;
	}

	public void setFeePaidByReceipent(String feePaidByReceipent) {
		this.feePaidByReceipent = feePaidByReceipent;
	}

	public String getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(String convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getConsentid() {
		return this.getConsentId();
	}

	public void setConsentid(String consentid) {
		this.setConsentId(consentid);
		this.consentid = null;
	}

	public String getConsentId() {
		return this.ConsentId;
	}

	public void setConsentId(String consentId) {
		this.ConsentId = consentId;
	}

	public String getInitiation_instructionIdentification() {
		return this.getInitiation_InstructionIdentification();
	}

	public void setInitiation_instructionIdentification(String initiation_instructionIdentification) {
		this.setInitiation_InstructionIdentification(initiation_instructionIdentification);
		this.initiation_instructionIdentification = null;
	}

	public String getInitiation_InstructionIdentification() {
		return this.Initiation_InstructionIdentification;
	}

	public void setInitiation_InstructionIdentification(String initiation_InstructionIdentification) {
		this.setInitiation_InstructionIdentification(initiation_InstructionIdentification);
		this.Initiation_InstructionIdentification = null;
	}

	public String getDebtorAccountSchemeName() {
		return debtorAccountSchemeName;
	}

	public void setDebtorAccountSchemeName(String debtorAccountSchemeName) {
		this.debtorAccountSchemeName = debtorAccountSchemeName;
	}

	public String getDebtorAccountName() {
		return debtorAccountName;
	}

	public void setDebtorAccountName(String debtorAccountName) {
		this.debtorAccountName = debtorAccountName;
	}

	public String getCreditorAccountSchemeName() {
		return creditorAccountSchemeName;
	}

	public void setCreditorAccountSchemeName(String creditorAccountSchemeName) {
		this.creditorAccountSchemeName = creditorAccountSchemeName;
	}

	public String getRi_unstructured() {
		return this.getRI_Unstructured();
	}

	public void setRi_unstructured(String ri_unstructured) {
		this.setRI_Unstructured(ri_unstructured);
		this.ri_unstructured = null;
	}

	public String getRI_Unstructured() {
		return this.RI_Unstructured;
	}

	public void setRI_Unstructured(String rI_Unstructured) {
		this.RI_Unstructured = rI_Unstructured;
	}

	public String getRi_reference() {
		return this.getRI_Reference();
	}

	public void setRi_reference(String ri_reference) {
		this.setRI_Reference(ri_reference);
		this.ri_reference = null;
	}

	public String getRI_Reference() {
		return this.RI_Reference;
	}

	public void setRI_Reference(String rI_Reference) {
		this.RI_Reference = rI_Reference;
	}

	public String getInitiation_endToEndIdentification() {
		return this.getInitiation_EndToEndIdentification();
	}

	public void setInitiation_endToEndIdentification(String initiation_endToEndIdentification) {
		this.setInitiation_EndToEndIdentification(initiation_endToEndIdentification);
		this.initiation_endToEndIdentification = null;
	}

	public String getInitiation_EndToEndIdentification() {
		return this.Initiation_EndToEndIdentification;
	}

	public void setInitiation_EndToEndIdentification(String initiation_EndToEndIdentification) {
		this.Initiation_EndToEndIdentification = initiation_EndToEndIdentification;
	}

	public String getRiskpaymentContextCode() {
		return this.getRiskPaymentContextCode();
	}

	public void setRiskpaymentContextCode(String riskpaymentContextCode) {
		this.setRiskPaymentContextCode(riskpaymentContextCode);
		this.riskpaymentContextCode = null;
	}

	public String getRiskPaymentContextCode() {
		return this.RiskPaymentContextCode;
	}

	public void setRiskPaymentContextCode(String riskPaymentContextCode) {
		this.RiskPaymentContextCode = riskPaymentContextCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPayPersonName() {
		return payPersonName;
	}

	public void setPayPersonName(String payPersonName) {
		this.payPersonName = payPersonName;
	}

	public String getPayPersonNickName() {
		return payPersonNickName;
	}

	public void setPayPersonNickName(String payPersonNickName) {
		this.payPersonNickName = payPersonNickName;
	}

	public String getP2pAlternateContact() {
		return p2pAlternateContact;
	}

	public void setP2pAlternateContact(String p2pAlternateContact) {
		this.p2pAlternateContact = p2pAlternateContact;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	
	public ErrorCodeEnum getDbpErrorCode() {
		return dbpErrorCode;
	}

	public void setDbpErrorCode(ErrorCodeEnum dbpErrorCode) {
		this.dbpErrorCode = dbpErrorCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ConsentId == null) ? 0 : ConsentId.hashCode());
		result = prime * result + ((IBAN == null) ? 0 : IBAN.hashCode());
		result = prime * result
				+ ((Initiation_EndToEndIdentification == null) ? 0 : Initiation_EndToEndIdentification.hashCode());
		result = prime * result + ((Initiation_InstructionIdentification == null) ? 0
				: Initiation_InstructionIdentification.hashCode());
		result = prime * result + ((RI_Reference == null) ? 0 : RI_Reference.hashCode());
		result = prime * result + ((RI_Unstructured == null) ? 0 : RI_Unstructured.hashCode());
		result = prime * result + ((RiskPaymentContextCode == null) ? 0 : RiskPaymentContextCode.hashCode());
		result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((amt == null) ? 0 : amt.hashCode());
		result = prime * result + ((bankAddressLine1 == null) ? 0 : bankAddressLine1.hashCode());
		result = prime * result + ((bankAddressLine2 == null) ? 0 : bankAddressLine2.hashCode());
		result = prime * result + ((bankCity == null) ? 0 : bankCity.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankState == null) ? 0 : bankState.hashCode());
		result = prime * result + ((bankZip == null) ? 0 : bankZip.hashCode());
		result = prime * result + ((baseCurrency == null) ? 0 : baseCurrency.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((billerId == null) ? 0 : billerId.hashCode());
		result = prime * result
				+ ((cashWithdrawalTransactionStatus == null) ? 0 : cashWithdrawalTransactionStatus.hashCode());
		result = prime * result + ((cashlessMode == null) ? 0 : cashlessMode.hashCode());
		result = prime * result + ((cashlessPersonName == null) ? 0 : cashlessPersonName.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((checkDateOfIssue == null) ? 0 : checkDateOfIssue.hashCode());
		result = prime * result + ((checkImage == null) ? 0 : checkImage.hashCode());
		result = prime * result + ((checkImageBack == null) ? 0 : checkImageBack.hashCode());
		result = prime * result + ((checkNumber1 == null) ? 0 : checkNumber1.hashCode());
		result = prime * result + ((checkNumber2 == null) ? 0 : checkNumber2.hashCode());
		result = prime * result + ((checkReason == null) ? 0 : checkReason.hashCode());
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((consentid == null) ? 0 : consentid.hashCode());
		result = prime * result + ((convertedAmount == null) ? 0 : convertedAmount.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((creditorAccountSchemeName == null) ? 0 : creditorAccountSchemeName.hashCode());
		result = prime * result + ((debtorAccountName == null) ? 0 : debtorAccountName.hashCode());
		result = prime * result + ((debtorAccountSchemeName == null) ? 0 : debtorAccountSchemeName.hashCode());
		result = prime * result + ((errmsg == null) ? 0 : errmsg.hashCode());
		result = prime * result + ((feeCurrency == null) ? 0 : feeCurrency.hashCode());
		result = prime * result + ((feePaidByReceipent == null) ? 0 : feePaidByReceipent.hashCode());
		result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
		result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
		result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
		result = prime * result + ((fromAccountBalance == null) ? 0 : fromAccountBalance.hashCode());
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((toAccountType == null) ? 0 : toAccountType.hashCode());
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		result = prime * result
				+ ((initiation_endToEndIdentification == null) ? 0 : initiation_endToEndIdentification.hashCode());
		result = prime * result + ((initiation_instructionIdentification == null) ? 0
				: initiation_instructionIdentification.hashCode());
		result = prime * result + ((internationalRoutingCode == null) ? 0 : internationalRoutingCode.hashCode());
		result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
		result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
		result = prime * result + ((otp == null) ? 0 : otp.hashCode());
		result = prime * result + ((overdraft == null) ? 0 : overdraft.hashCode());
		result = prime * result + ((p2pAlternateContact == null) ? 0 : p2pAlternateContact.hashCode());
		result = prime * result + ((p2pContact == null) ? 0 : p2pContact.hashCode());
		result = prime * result + ((p2pRequiredDate == null) ? 0 : p2pRequiredDate.hashCode());
		result = prime * result + ((payPersonName == null) ? 0 : payPersonName.hashCode());
		result = prime * result + ((payPersonNickName == null) ? 0 : payPersonNickName.hashCode());
		result = prime * result + ((payeeAccountNumber == null) ? 0 : payeeAccountNumber.hashCode());
		result = prime * result + ((payeeAddressLine1 == null) ? 0 : payeeAddressLine1.hashCode());
		result = prime * result + ((payeeCurrency == null) ? 0 : payeeCurrency.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((payeeNickName == null) ? 0 : payeeNickName.hashCode());
		result = prime * result + ((payeeType == null) ? 0 : payeeType.hashCode());
		result = prime * result + ((payoffFlag == null) ? 0 : payoffFlag.hashCode());
		result = prime * result + ((penaltyFlag == null) ? 0 : penaltyFlag.hashCode());
		result = prime * result + ((personId == null) ? 0 : personId.hashCode());
		result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
		result = prime * result + ((requestValidityInMonths == null) ? 0 : requestValidityInMonths.hashCode());
		result = prime * result + ((ri_reference == null) ? 0 : ri_reference.hashCode());
		result = prime * result + ((ri_unstructured == null) ? 0 : ri_unstructured.hashCode());
		result = prime * result + ((riskpaymentContextCode == null) ? 0 : riskpaymentContextCode.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((success == null) ? 0 : success.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
		result = prime * result + ((validDate == null) ? 0 : validDate.hashCode());
		result = prime * result + ((wireAccountType == null) ? 0 : wireAccountType.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
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
		RDCDTO other = (RDCDTO) obj;
		if (ConsentId == null) {
			if (other.ConsentId != null)
				return false;
		} else if (!ConsentId.equals(other.ConsentId))
			return false;
		if (IBAN == null) {
			if (other.IBAN != null)
				return false;
		} else if (!IBAN.equals(other.IBAN))
			return false;
		if (Initiation_EndToEndIdentification == null) {
			if (other.Initiation_EndToEndIdentification != null)
				return false;
		} else if (!Initiation_EndToEndIdentification.equals(other.Initiation_EndToEndIdentification))
			return false;
		if (Initiation_InstructionIdentification == null) {
			if (other.Initiation_InstructionIdentification != null)
				return false;
		} else if (!Initiation_InstructionIdentification.equals(other.Initiation_InstructionIdentification))
			return false;
		if (RI_Reference == null) {
			if (other.RI_Reference != null)
				return false;
		} else if (!RI_Reference.equals(other.RI_Reference))
			return false;
		if (RI_Unstructured == null) {
			if (other.RI_Unstructured != null)
				return false;
		} else if (!RI_Unstructured.equals(other.RI_Unstructured))
			return false;
		if (RiskPaymentContextCode == null) {
			if (other.RiskPaymentContextCode != null)
				return false;
		} else if (!RiskPaymentContextCode.equals(other.RiskPaymentContextCode))
			return false;
		if (addressLine2 == null) {
			if (other.addressLine2 != null)
				return false;
		} else if (!addressLine2.equals(other.addressLine2))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (amt == null) {
			if (other.amt != null)
				return false;
		} else if (!amt.equals(other.amt))
			return false;
		if (bankAddressLine1 == null) {
			if (other.bankAddressLine1 != null)
				return false;
		} else if (!bankAddressLine1.equals(other.bankAddressLine1))
			return false;
		if (bankAddressLine2 == null) {
			if (other.bankAddressLine2 != null)
				return false;
		} else if (!bankAddressLine2.equals(other.bankAddressLine2))
			return false;
		if (bankCity == null) {
			if (other.bankCity != null)
				return false;
		} else if (!bankCity.equals(other.bankCity))
			return false;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		if (bankState == null) {
			if (other.bankState != null)
				return false;
		} else if (!bankState.equals(other.bankState))
			return false;
		if (bankZip == null) {
			if (other.bankZip != null)
				return false;
		} else if (!bankZip.equals(other.bankZip))
			return false;
		if (baseCurrency == null) {
			if (other.baseCurrency != null)
				return false;
		} else if (!baseCurrency.equals(other.baseCurrency))
			return false;
		if (beneficiaryName == null) {
			if (other.beneficiaryName != null)
				return false;
		} else if (!beneficiaryName.equals(other.beneficiaryName))
			return false;
		if (billerId == null) {
			if (other.billerId != null)
				return false;
		} else if (!billerId.equals(other.billerId))
			return false;
		if (cashWithdrawalTransactionStatus == null) {
			if (other.cashWithdrawalTransactionStatus != null)
				return false;
		} else if (!cashWithdrawalTransactionStatus.equals(other.cashWithdrawalTransactionStatus))
			return false;
		if (cashlessMode == null) {
			if (other.cashlessMode != null)
				return false;
		} else if (!cashlessMode.equals(other.cashlessMode))
			return false;
		if (cashlessPersonName == null) {
			if (other.cashlessPersonName != null)
				return false;
		} else if (!cashlessPersonName.equals(other.cashlessPersonName))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (checkDateOfIssue == null) {
			if (other.checkDateOfIssue != null)
				return false;
		} else if (!checkDateOfIssue.equals(other.checkDateOfIssue))
			return false;
		if (checkImage == null) {
			if (other.checkImage != null)
				return false;
		} else if (!checkImage.equals(other.checkImage))
			return false;
		if (checkImageBack == null) {
			if (other.checkImageBack != null)
				return false;
		} else if (!checkImageBack.equals(other.checkImageBack))
			return false;
		if (checkNumber1 == null) {
			if (other.checkNumber1 != null)
				return false;
		} else if (!checkNumber1.equals(other.checkNumber1))
			return false;
		if (checkNumber2 == null) {
			if (other.checkNumber2 != null)
				return false;
		} else if (!checkNumber2.equals(other.checkNumber2))
			return false;
		if (checkReason == null) {
			if (other.checkReason != null)
				return false;
		} else if (!checkReason.equals(other.checkReason))
			return false;
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		if (consentid == null) {
			if (other.consentid != null)
				return false;
		} else if (!consentid.equals(other.consentid))
			return false;
		if (convertedAmount == null) {
			if (other.convertedAmount != null)
				return false;
		} else if (!convertedAmount.equals(other.convertedAmount))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (creditorAccountSchemeName == null) {
			if (other.creditorAccountSchemeName != null)
				return false;
		} else if (!creditorAccountSchemeName.equals(other.creditorAccountSchemeName))
			return false;
		if (debtorAccountName == null) {
			if (other.debtorAccountName != null)
				return false;
		} else if (!debtorAccountName.equals(other.debtorAccountName))
			return false;
		if (debtorAccountSchemeName == null) {
			if (other.debtorAccountSchemeName != null)
				return false;
		} else if (!debtorAccountSchemeName.equals(other.debtorAccountSchemeName))
			return false;
		if (errmsg == null) {
			if (other.errmsg != null)
				return false;
		} else if (!errmsg.equals(other.errmsg))
			return false;
		if (feeCurrency == null) {
			if (other.feeCurrency != null)
				return false;
		} else if (!feeCurrency.equals(other.feeCurrency))
			return false;
		if (feePaidByReceipent == null) {
			if (other.feePaidByReceipent != null)
				return false;
		} else if (!feePaidByReceipent.equals(other.feePaidByReceipent))
			return false;
		if (frequencyEndDate == null) {
			if (other.frequencyEndDate != null)
				return false;
		} else if (!frequencyEndDate.equals(other.frequencyEndDate))
			return false;
		if (frequencyStartDate == null) {
			if (other.frequencyStartDate != null)
				return false;
		} else if (!frequencyStartDate.equals(other.frequencyStartDate))
			return false;
		if (frequencyType == null) {
			if (other.frequencyType != null)
				return false;
		} else if (!frequencyType.equals(other.frequencyType))
			return false;
		if (fromAccountBalance == null) {
			if (other.fromAccountBalance != null)
				return false;
		} else if (!fromAccountBalance.equals(other.fromAccountBalance))
			return false;
		if (fromAccountNumber == null) {
			if (other.fromAccountNumber != null)
				return false;
		} else if (!fromAccountNumber.equals(other.fromAccountNumber))
			return false;
		if (initiation_endToEndIdentification == null) {
			if (other.initiation_endToEndIdentification != null)
				return false;
		} else if (!initiation_endToEndIdentification.equals(other.initiation_endToEndIdentification))
			return false;
		if (initiation_instructionIdentification == null) {
			if (other.initiation_instructionIdentification != null)
				return false;
		} else if (!initiation_instructionIdentification.equals(other.initiation_instructionIdentification))
			return false;
		if (internationalRoutingCode == null) {
			if (other.internationalRoutingCode != null)
				return false;
		} else if (!internationalRoutingCode.equals(other.internationalRoutingCode))
			return false;
		if (isScheduled == null) {
			if (other.isScheduled != null)
				return false;
		} else if (!isScheduled.equals(other.isScheduled))
			return false;
		if (numberOfRecurrences == null) {
			if (other.numberOfRecurrences != null)
				return false;
		} else if (!numberOfRecurrences.equals(other.numberOfRecurrences))
			return false;
		if (otp == null) {
			if (other.otp != null)
				return false;
		} else if (!otp.equals(other.otp))
			return false;
		if (overdraft == null) {
			if (other.overdraft != null)
				return false;
		} else if (!overdraft.equals(other.overdraft))
			return false;
		if (p2pAlternateContact == null) {
			if (other.p2pAlternateContact != null)
				return false;
		} else if (!p2pAlternateContact.equals(other.p2pAlternateContact))
			return false;
		if (p2pContact == null) {
			if (other.p2pContact != null)
				return false;
		} else if (!p2pContact.equals(other.p2pContact))
			return false;
		if (p2pRequiredDate == null) {
			if (other.p2pRequiredDate != null)
				return false;
		} else if (!p2pRequiredDate.equals(other.p2pRequiredDate))
			return false;
		if (payPersonName == null) {
			if (other.payPersonName != null)
				return false;
		} else if (!payPersonName.equals(other.payPersonName))
			return false;
		if (payPersonNickName == null) {
			if (other.payPersonNickName != null)
				return false;
		} else if (!payPersonNickName.equals(other.payPersonNickName))
			return false;
		if (payeeAccountNumber == null) {
			if (other.payeeAccountNumber != null)
				return false;
		} else if (!payeeAccountNumber.equals(other.payeeAccountNumber))
			return false;
		if (payeeAddressLine1 == null) {
			if (other.payeeAddressLine1 != null)
				return false;
		} else if (!payeeAddressLine1.equals(other.payeeAddressLine1))
			return false;
		if (payeeCurrency == null) {
			if (other.payeeCurrency != null)
				return false;
		} else if (!payeeCurrency.equals(other.payeeCurrency))
			return false;
		if (payeeId == null) {
			if (other.payeeId != null)
				return false;
		} else if (!payeeId.equals(other.payeeId))
			return false;
		if (payeeName == null) {
			if (other.payeeName != null)
				return false;
		} else if (!payeeName.equals(other.payeeName))
			return false;
		if (payeeNickName == null) {
			if (other.payeeNickName != null)
				return false;
		} else if (!payeeNickName.equals(other.payeeNickName))
			return false;
		if (payeeType == null) {
			if (other.payeeType != null)
				return false;
		} else if (!payeeType.equals(other.payeeType))
			return false;
		if (payoffFlag == null) {
			if (other.payoffFlag != null)
				return false;
		} else if (!payoffFlag.equals(other.payoffFlag))
			return false;
		if (penaltyFlag == null) {
			if (other.penaltyFlag != null)
				return false;
		} else if (!penaltyFlag.equals(other.penaltyFlag))
			return false;
		if (personId == null) {
			if (other.personId != null)
				return false;
		} else if (!personId.equals(other.personId))
			return false;
		if (referenceId == null) {
			if (other.referenceId != null)
				return false;
		} else if (!referenceId.equals(other.referenceId))
			return false;
		if (requestValidityInMonths == null) {
			if (other.requestValidityInMonths != null)
				return false;
		} else if (!requestValidityInMonths.equals(other.requestValidityInMonths))
			return false;
		if (ri_reference == null) {
			if (other.ri_reference != null)
				return false;
		} else if (!ri_reference.equals(other.ri_reference))
			return false;
		if (ri_unstructured == null) {
			if (other.ri_unstructured != null)
				return false;
		} else if (!ri_unstructured.equals(other.ri_unstructured))
			return false;
		if (riskpaymentContextCode == null) {
			if (other.riskpaymentContextCode != null)
				return false;
		} else if (!riskpaymentContextCode.equals(other.riskpaymentContextCode))
			return false;
		if (routingNumber == null) {
			if (other.routingNumber != null)
				return false;
		} else if (!routingNumber.equals(other.routingNumber))
			return false;
		if (scheduledDate == null) {
			if (other.scheduledDate != null)
				return false;
		} else if (!scheduledDate.equals(other.scheduledDate))
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (success == null) {
			if (other.success != null)
				return false;
		} else if (!success.equals(other.success))
			return false;
		if (swiftCode == null) {
			if (other.swiftCode != null)
				return false;
		} else if (!swiftCode.equals(other.swiftCode))
			return false;
		if (toAccountNumber == null) {
			if (other.toAccountNumber != null)
				return false;
		} else if (!toAccountNumber.equals(other.toAccountNumber))
			return false;
		if (toAccountType == null) {
			if (other.toAccountType != null)
				return false;
		} else if (!toAccountType.equals(other.toAccountType))
			return false;
		if (profileId == null) {
			if (other.profileId != null)
				return false;
		} else if (!profileId.equals(other.profileId))
			return false;
		if (transactionCurrency == null) {
			if (other.transactionCurrency != null)
				return false;
		} else if (!transactionCurrency.equals(other.transactionCurrency))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		if (transactionsNotes == null) {
			if (other.transactionsNotes != null)
				return false;
		} else if (!transactionsNotes.equals(other.transactionsNotes))
			return false;
		if (validDate == null) {
			if (other.validDate != null)
				return false;
		} else if (!validDate.equals(other.validDate))
			return false;
		if (wireAccountType == null) {
			if (other.wireAccountType != null)
				return false;
		} else if (!wireAccountType.equals(other.wireAccountType))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}
	
}