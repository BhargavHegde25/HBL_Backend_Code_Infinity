package com.temenos.infinity.api.srmstransactions.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class SRMSTransactionDTO implements DBPDTO {

    private static final long serialVersionUID = 6597925078705187784L;

    private String transactionPeriod;
    private String firstRecordNumber;
    private String lastRecordNumber;
    private String accountId;
    private String type;
    private String subtype;
    private String dateFrom;
    private String dateTo;
    private String transactionId;
    private String chargeBearer;
    private String amount;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String transactionCurrency;
    private String currentStatus;
    
    @JsonAlias({"e2eReference"})
    private String endToEndReference;
    private String scheduledDate;
    private String transactionType;
    
    @JsonAlias({"transactionCurrency"})
    private String paymentCurrencyId;
    private String ExternalAccountNumber;
    
    @JsonAlias({"userId"})
    private String orderingCustomerId;
    private String frequencyType;
    private String serviceName;
    private String isScheduled;
    private String statusDescription;
    private String swiftCode;
    private String creditValueDate;
    private String clearingCode;
    private String intermediaryBicCode;
    private String exchangeRate;
    private String totalAmount;
    private boolean pendingApproval;
    private String description;
    private String charges;
    private String externalId;
    
    @JsonAlias({"transactionsNotes"})
    private String transactionNotes;
    private String referenceId;
    private String paymentType;
    private String personId;
    private String fromAccountName;
    private String toAccountName;
    private String numberOfRecurrences;
    private String beneficiaryName;
    private String beneficiaryAddressLine1;
    private String beneficiaryBankName;
    private String beneficiaryAddressLine2;
    private String beneficiaryPhone;
    private String beneficiaryEmail;
    private String beneficiaryState;
    private String beneficiaryCity;
    private String beneficiaryZipcode;
    private String beneficiarycountry;
    private String numberOfAuthorisers;
    

    public SRMSTransactionDTO() {
        super();
    }

    public SRMSTransactionDTO(String transactionPeriod, String firstRecordNumber, String lastRecordNumber,
            String accountId, String type, String subtype, String dateFrom, String dateTo, String transactionId,
            String chargeBearer, String amount, String fromAccountNumber, String toAccountNumber,
            String transactionCurrency, String currentStatus, String endToEndReference, String scheduledDate,
            String transactionType, String paymentCurrencyId, String externalAccountNumber, String orderingCustomerId,
            String frequencyType, String serviceName, String isScheduled, String statusDescription, String swiftCode,
            String creditValueDate, String clearingCode, String intermediaryBicCode, String exchangeRate,
            String totalAmount, boolean pendingApproval, String description, String charges, String externalId,
            String transactionNotes, String referenceId, String paymentType, String personId,
            String beneficiaryBankName, String beneficiaryAddressLine2,String beneficiaryAddressLine1, String beneficiaryPhone,
            String beneficiaryEmail, String beneficiaryState, String beneficiaryCity, String beneficiaryZipcode, String beneficiarycountry,
            String fromAccountName, String toAccountName, String numberOfRecurrences, String beneficiaryName, String numberOfAuthorisers) {

        super();
        this.transactionPeriod = transactionPeriod;
        this.firstRecordNumber = firstRecordNumber;
        this.lastRecordNumber = lastRecordNumber;
        this.accountId = accountId;
        this.type = type;
        this.subtype = subtype;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.transactionId = transactionId;
        this.chargeBearer = chargeBearer;
        this.amount = amount;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.transactionCurrency = transactionCurrency;
        this.currentStatus = currentStatus;
        this.endToEndReference = endToEndReference;
        this.scheduledDate = scheduledDate;
        this.transactionType = transactionType;
        this.paymentCurrencyId = paymentCurrencyId;
        ExternalAccountNumber = externalAccountNumber;
        this.orderingCustomerId = orderingCustomerId;
        this.frequencyType = frequencyType;
        this.serviceName = serviceName;
        this.isScheduled = isScheduled;
        this.statusDescription = statusDescription;
        this.swiftCode = swiftCode;
        this.creditValueDate = creditValueDate;
        this.clearingCode = clearingCode;
        this.intermediaryBicCode = intermediaryBicCode;
        this.exchangeRate = exchangeRate;
        this.totalAmount = totalAmount;
        this.pendingApproval = pendingApproval;
        this.description = description;
        this.charges = charges;
        this.externalId = externalId;
        this.transactionNotes = transactionNotes;
        this.referenceId = referenceId;
        this.paymentType = paymentType;
        this.personId = personId;
        this.beneficiaryBankName = beneficiaryBankName;
        this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
        this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
        this.beneficiaryPhone = beneficiaryPhone;
        this.beneficiaryEmail = beneficiaryEmail;
        this.beneficiaryState = beneficiaryState;
        this.beneficiaryZipcode = beneficiaryZipcode;
        this.beneficiaryCity = beneficiaryCity;
        this.beneficiarycountry = beneficiarycountry;
        this.fromAccountName = fromAccountName;
        this.toAccountName = toAccountName;
        this.numberOfRecurrences = numberOfRecurrences;
        this.beneficiaryName = beneficiaryName;
        this.numberOfAuthorisers = numberOfAuthorisers;
    }

    public String getTransactionPeriod() {
        return transactionPeriod;
    }

    public void setTransactionPeriod(String transactionPeriod) {
        this.transactionPeriod = transactionPeriod;
    }

    public String getFirstRecordNumber() {
        return firstRecordNumber;
    }

    public void setFirstRecordNumber(String firstRecordNumber) {
        this.firstRecordNumber = firstRecordNumber;
    }

    public String getLastRecordNumber() {
        return lastRecordNumber;
    }

    public void setLastRecordNumber(String lastRecordNumber) {
        this.lastRecordNumber = lastRecordNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getChargeBearer() {
        return chargeBearer;
    }

    public void setChargeBearer(String chargeBearer) {
        this.chargeBearer = chargeBearer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getEndToEndReference() {
        return endToEndReference;
    }

    public void setEndToEndReference(String endToEndReference) {
        this.endToEndReference = endToEndReference;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        if(scheduledDate.contains("T")){
            scheduledDate = scheduledDate.split("T")[0];
        }
        this.scheduledDate = scheduledDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPaymentCurrencyId() {
        return paymentCurrencyId;
    }

    public void setPaymentCurrencyId(String paymentCurrencyId) {
        this.paymentCurrencyId = paymentCurrencyId;
    }

    public String getExternalAccountNumber() {
        return ExternalAccountNumber;
    }

    public void setExternalAccountNumber(String externalAccountNumber) {
        ExternalAccountNumber = externalAccountNumber;
    }

    public String getOrderingCustomerId() {
        return orderingCustomerId;
    }

    public void setOrderingCustomerId(String orderingCustomerId) {
        this.orderingCustomerId = orderingCustomerId;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(String isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCreditValueDate() {
        return creditValueDate;
    }

    public void setCreditValueDate(String creditValueDate) {
        this.creditValueDate = creditValueDate;
    }

    public String getClearingCode() {
        return clearingCode;
    }

    public void setClearingCode(String clearingCode) {
        this.clearingCode = clearingCode;
    }

    public String getIntermediaryBicCode() {
        return intermediaryBicCode;
    }

    public void setIntermediaryBicCode(String intermediaryBicCode) {
        this.intermediaryBicCode = intermediaryBicCode;
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

    public boolean isPendingApproval() {
        return pendingApproval;
    }

    public void setPendingApproval(boolean pendingApproval) {
        this.pendingApproval = pendingApproval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTransactionNotes() {
        return transactionNotes;
    }

    public void setTransactionNotes(String transactionNotes) {
        this.transactionNotes = transactionNotes;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * @return the fromAccountName
     */
    public String getFromAccountName() {
        return fromAccountName;
    }

    /**
     * @param fromAccountName the fromAccountName to set
     */
    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }

    /**
     * @return the toAccountName
     */
    public String getToAccountName() {
        return toAccountName;
    }

    /**
     * @param toAccountName the toAccountName to set
     */
    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    /**
     * @return the numberOfRecurrences
     */
    public String getNumberOfRecurrences() {
        return numberOfRecurrences;
    }

    /**
     * @param numberOfRecurrences the numberOfRecurrences to set
     */
    public void setNumberOfRecurrences(String numberOfRecurrences) {
        this.numberOfRecurrences = numberOfRecurrences;
    }

    /**
     * @return the beneficiaryName
     */
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    /**
     * @param beneficiaryName the beneficiaryName to set
     */
    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    /**
     * @return the beneficiaryAddressLine1
     */
    public String getBeneficiaryAddressLine1() {
        return beneficiaryAddressLine1;
    }

    /**
     * @param beneficiaryAddressLine1 the beneficiaryAddressLine1 to set
     */
    public void setBeneficiaryAddressLine1(String beneficiaryAddressLine1) {
        this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
    }

    /**
     * @return the beneficiaryBankName
     */
    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    /**
     * @param beneficiaryBankName the beneficiaryBankName to set
     */
    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    /**
     * @return the beneficiaryAddressLine2
     */
    public String getBeneficiaryAddressLine2() {
        return beneficiaryAddressLine2;
    }

    /**
     * @param beneficiaryAddressLine2 the beneficiaryAddressLine2 to set
     */
    public void setBeneficiaryAddressLine2(String beneficiaryAddressLine2) {
        this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
    }

    /**
     * @return the beneficiaryPhone
     */
    public String getBeneficiaryPhone() {
        return beneficiaryPhone;
    }

    /**
     * @param beneficiaryPhone the beneficiaryPhone to set
     */
    public void setBeneficiaryPhone(String beneficiaryPhone) {
        this.beneficiaryPhone = beneficiaryPhone;
    }

    /**
     * @return the beneficiaryEmail
     */
    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }

    /**
     * @param beneficiaryEmail the beneficiaryEmail to set
     */
    public void setBeneficiaryEmail(String beneficiaryEmail) {
        this.beneficiaryEmail = beneficiaryEmail;
    }

    /**
     * @return the beneficiaryState
     */
    public String getBeneficiaryState() {
        return beneficiaryState;
    }

    /**
     * @param beneficiaryState the beneficiaryState to set
     */
    public void setBeneficiaryState(String beneficiaryState) {
        this.beneficiaryState = beneficiaryState;
    }

    /**
     * @return the beneficiaryCity
     */
    public String getBeneficiaryCity() {
        return beneficiaryCity;
    }

    /**
     * @param beneficiaryCity the beneficiaryCity to set
     */
    public void setBeneficiaryCity(String beneficiaryCity) {
        this.beneficiaryCity = beneficiaryCity;
    }

    /**
     * @return the beneficiaryZipcode
     */
    public String getBeneficiaryZipcode() {
        return beneficiaryZipcode;
    }

    /**
     * @param beneficiaryZipcode the beneficiaryZipcode to set
     */
    public void setBeneficiaryZipcode(String beneficiaryZipcode) {
        this.beneficiaryZipcode = beneficiaryZipcode;
    }

    /**
     * @return the beneficiarycountry
     */
    public String getBeneficiarycountry() {
        return beneficiarycountry;
    }

    /**
     * @param beneficiarycountry the beneficiarycountry to set
     */
    public void setBeneficiarycountry(String beneficiarycountry) {
        this.beneficiarycountry = beneficiarycountry;
    }

    /**
     * @return the numberOfAuthorisers
     */
    public String getNumberOfAuthorisers() {
        return numberOfAuthorisers;
    }

    /**
     * @param numberOfAuthorisers the numberOfAuthorisers to set
     */
    public void setNumberOfAuthorisers(String numberOfAuthorisers) {
        this.numberOfAuthorisers = numberOfAuthorisers;
    }

   

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ExternalAccountNumber == null) ? 0 : ExternalAccountNumber.hashCode());
        result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((beneficiaryAddressLine1 == null) ? 0 : beneficiaryAddressLine1.hashCode());
        result = prime * result + ((beneficiaryAddressLine2 == null) ? 0 : beneficiaryAddressLine2.hashCode());
        result = prime * result + ((beneficiaryBankName == null) ? 0 : beneficiaryBankName.hashCode());
        result = prime * result + ((beneficiaryCity == null) ? 0 : beneficiaryCity.hashCode());
        result = prime * result + ((beneficiaryEmail == null) ? 0 : beneficiaryEmail.hashCode());
        result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
        result = prime * result + ((beneficiaryPhone == null) ? 0 : beneficiaryPhone.hashCode());
        result = prime * result + ((beneficiaryState == null) ? 0 : beneficiaryState.hashCode());
        result = prime * result + ((beneficiaryZipcode == null) ? 0 : beneficiaryZipcode.hashCode());
        result = prime * result + ((beneficiarycountry == null) ? 0 : beneficiarycountry.hashCode());
        result = prime * result + ((chargeBearer == null) ? 0 : chargeBearer.hashCode());
        result = prime * result + ((charges == null) ? 0 : charges.hashCode());
        result = prime * result + ((clearingCode == null) ? 0 : clearingCode.hashCode());
        result = prime * result + ((creditValueDate == null) ? 0 : creditValueDate.hashCode());
        result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
        result = prime * result + ((dateFrom == null) ? 0 : dateFrom.hashCode());
        result = prime * result + ((dateTo == null) ? 0 : dateTo.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((endToEndReference == null) ? 0 : endToEndReference.hashCode());
        result = prime * result + ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
        result = prime * result + ((externalId == null) ? 0 : externalId.hashCode());
        result = prime * result + ((firstRecordNumber == null) ? 0 : firstRecordNumber.hashCode());
        result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
        result = prime * result + ((fromAccountName == null) ? 0 : fromAccountName.hashCode());
        result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
        result = prime * result + ((intermediaryBicCode == null) ? 0 : intermediaryBicCode.hashCode());
        result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
        result = prime * result + ((lastRecordNumber == null) ? 0 : lastRecordNumber.hashCode());
        result = prime * result + ((numberOfAuthorisers == null) ? 0 : numberOfAuthorisers.hashCode());
        result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
        result = prime * result + ((orderingCustomerId == null) ? 0 : orderingCustomerId.hashCode());
        result = prime * result + ((paymentCurrencyId == null) ? 0 : paymentCurrencyId.hashCode());
        result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
        result = prime * result + (pendingApproval ? 1231 : 1237);
        result = prime * result + ((personId == null) ? 0 : personId.hashCode());
        result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
        result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = prime * result + ((statusDescription == null) ? 0 : statusDescription.hashCode());
        result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
        result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
        result = prime * result + ((toAccountName == null) ? 0 : toAccountName.hashCode());
        result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
        result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        result = prime * result + ((transactionNotes == null) ? 0 : transactionNotes.hashCode());
        result = prime * result + ((transactionPeriod == null) ? 0 : transactionPeriod.hashCode());
        result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        SRMSTransactionDTO other = (SRMSTransactionDTO) obj;
        if (transactionId != other.transactionId)
            return false;
        return true;
    }

}
