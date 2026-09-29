package com.temenos.infinity.api.srmstransactions.dto;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class SRMSIntraBankDTO implements DBPDTO {

    private static final long serialVersionUID = 6597922978728487784L;

    private String fromAccountNumber;
    private String toAccountNumber;
    private String amount;
    private String transactionsNotes;
    private String transactionType;
    private String scheduledDate;
    private String isScheduled;
    private String transactionCurrency;
    private String transactionId;
    private String frequencyType;
    private String serviceName;
    private String paymentType;
    private String userId;
    private String numberOfAuthorisers;
    private String frequencyStartDate;
    private String frequencyEndDate;
    private String beneficiaryName;
    private String beneficiaryNickname;
    private String fromAccountCurrency;
    private String toAccountCurrency;
    private String uploadedattachments;
    private String totalAmount;
    private String paidBy;
    private String ExternalAccountNumber;
    private String transactionAmount;
    private String charges;
    private String beneficiaryAddress;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String beneficiaryBankName;
    private String beneficiaryCountryCode;
    private String creditValueDate;
    private String exchangeRate;
    private String beneficiaryAddressLine1;
    private String beneficiaryAddressLine2;
    private String beneficiaryState;
    private String beneficiaryCity;
    private String beneficiaryZipcode;
    private String beneficiarycountry;
    private String numberOfRecurrences;

    /**
     * 
     */
    public SRMSIntraBankDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SRMSIntraBankDTO(String fromAccountNumber, String toAccountNumber, String amount, String transactionsNotes,
            String transactionType, String scheduledDate, String isScheduled, String transactionCurrency,
            String transactionId, String frequencyType, String serviceName, String paymentType, String userId,
            String numberOfAuthorisers, String frequencyStartDate, String frequencyEndDate, String beneficiaryName,
            String beneficiaryNickname, String fromAccountCurrency, String toAccountCurrency,
            String uploadedattachments, String totalAmount, String paidBy, String externalAccountNumber,
            String transactionAmount, String charges, String beneficiaryAddress, String beneficiaryEmail,
            String beneficiaryPhone, String beneficiaryBankName, String beneficiaryCountryCode, String creditValueDate,
            String exchangeRate, String beneficiaryAddressLine1, String beneficiaryAddressLine2,
            String beneficiaryState, String beneficiaryCity, String beneficiaryZipcode, String beneficiarycountry, String numberOfRecurrences) {
        super();
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.transactionsNotes = transactionsNotes;
        this.transactionType = transactionType;
        this.scheduledDate = scheduledDate;
        this.isScheduled = isScheduled;
        this.transactionCurrency = transactionCurrency;
        this.transactionId = transactionId;
        this.frequencyType = frequencyType;
        this.serviceName = serviceName;
        this.paymentType = paymentType;
        this.userId = userId;
        this.numberOfAuthorisers = numberOfAuthorisers;
        this.frequencyStartDate = frequencyStartDate;
        this.frequencyEndDate = frequencyEndDate;
        this.beneficiaryName = beneficiaryName;
        this.beneficiaryNickname = beneficiaryNickname;
        this.fromAccountCurrency = fromAccountCurrency;
        this.toAccountCurrency = toAccountCurrency;
        this.uploadedattachments = uploadedattachments;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.ExternalAccountNumber = externalAccountNumber;
        this.transactionAmount = transactionAmount;
        this.charges = charges;
        this.beneficiaryAddress = beneficiaryAddress;
        this.beneficiaryEmail = beneficiaryEmail;
        this.beneficiaryPhone = beneficiaryPhone;
        this.beneficiaryBankName = beneficiaryBankName;
        this.beneficiaryCountryCode = beneficiaryCountryCode;
        this.creditValueDate = creditValueDate;
        this.exchangeRate = exchangeRate;
        this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
        this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
        this.beneficiaryState = beneficiaryState;
        this.beneficiaryCity = beneficiaryCity;
        this.beneficiaryZipcode = beneficiaryZipcode;
        this.beneficiarycountry = beneficiarycountry;
        this.numberOfRecurrences = numberOfRecurrences;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionsNotes() {
        return transactionsNotes;
    }

    public void setTransactionsNotes(String transactionsNotes) {
        this.transactionsNotes = transactionsNotes;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(String isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNumberOfAuthorisers() {
        return numberOfAuthorisers;
    }

    public void setNumberOfAuthorisers(String numberOfAuthorisers) {
        this.numberOfAuthorisers = numberOfAuthorisers;
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

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryNickname() {
        return beneficiaryNickname;
    }

    public void setBeneficiaryNickname(String beneficiaryNickname) {
        this.beneficiaryNickname = beneficiaryNickname;
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

    public String getUploadedattachments() {
        return uploadedattachments;
    }

    public void setUploadedattachments(String uploadedattachments) {
        this.uploadedattachments = uploadedattachments;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getExternalAccountNumber() {
        return ExternalAccountNumber;
    }

    public void setExternalAccountNumber(String externalAccountNumber) {
        ExternalAccountNumber = externalAccountNumber;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }

    public void setBeneficiaryEmail(String beneficiaryEmail) {
        this.beneficiaryEmail = beneficiaryEmail;
    }

    public String getBeneficiaryPhone() {
        return beneficiaryPhone;
    }

    public void setBeneficiaryPhone(String beneficiaryPhone) {
        this.beneficiaryPhone = beneficiaryPhone;
    }

    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    public String getBeneficiaryCountryCode() {
        return beneficiaryCountryCode;
    }

    public void setBeneficiaryCountryCode(String beneficiaryCountryCode) {
        this.beneficiaryCountryCode = beneficiaryCountryCode;
    }

    public String getCreditValueDate() {
        return creditValueDate;
    }

    public void setCreditValueDate(String creditValueDate) {
        this.creditValueDate = creditValueDate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public String getBeneficiaryState() {
        return beneficiaryState;
    }

    public void setBeneficiaryState(String beneficiaryState) {
        this.beneficiaryState = beneficiaryState;
    }

    public String getBeneficiaryCity() {
        return beneficiaryCity;
    }

    public void setBeneficiaryCity(String beneficiaryCity) {
        this.beneficiaryCity = beneficiaryCity;
    }

    public String getBeneficiaryZipcode() {
        return beneficiaryZipcode;
    }

    public void setBeneficiaryZipcode(String beneficiaryZipcode) {
        this.beneficiaryZipcode = beneficiaryZipcode;
    }

    public String getBeneficiarycountry() {
        return beneficiarycountry;
    }

    public void setBeneficiarycountry(String beneficiarycountry) {
        this.beneficiarycountry = beneficiarycountry;
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

    public SRMSIntraBankDTO convert(IntraBankFundTransferBackendDTO dto) {

        this.amount = String.valueOf(dto.getAmount());
        this.fromAccountNumber = dto.getFromAccountNumber();
        this.isScheduled = dto.getIsScheduled();
        this.frequencyType = dto.getFrequencyTypeId();
        this.serviceName = dto.getServiceName();
        this.toAccountNumber = dto.getToAccountNumber();
        this.transactionCurrency = dto.getTransactionCurrency();
        if (!dto.getFrequencyTypeId().equalsIgnoreCase("Once")) {
            this.frequencyEndDate = dto.getFrequencyEndDate();
        }
        if (StringUtils.isNotBlank(dto.getTransactionId()))
            this.transactionId = dto.getTransactionId();
        if (StringUtils.isNotBlank(dto.getScheduledDate()))
            this.scheduledDate = dto.getScheduledDate();
        if (StringUtils.isNotBlank(dto.getTransactionType()))
            this.transactionType = dto.getTransactionType();
        if (StringUtils.isNotBlank(dto.getNotes()))
            this.transactionsNotes = dto.getNotes();
        if (StringUtils.isNotBlank(dto.getTotalAmount()))
            this.totalAmount = dto.getTotalAmount();
        if (StringUtils.isNotBlank(dto.getPaidBy()))
            this.paidBy = dto.getPaidBy();
        if (StringUtils.isNotBlank(dto.getCharges()))
            this.charges = dto.getCharges();
        if (StringUtils.isNotBlank(dto.getBeneficiaryName()))
            this.beneficiaryName = dto.getBeneficiaryName();
        if (StringUtils.isNotBlank(dto.getExchangeRate()))
            this.exchangeRate = dto.getExchangeRate();
        if (StringUtils.isNotBlank(dto.getTransactionAmount()))
            this.transactionAmount = dto.getTransactionAmount();
        if (StringUtils.isNotBlank(dto.getBeneficiaryAddressLine1()))
            this.beneficiaryAddressLine1 = dto.getBeneficiaryAddressLine1();
        if (StringUtils.isNotBlank(dto.getBeneficiaryAddressLine2()))
            this.beneficiaryAddressLine2 = dto.getBeneficiaryAddressLine2();
        if (StringUtils.isNotBlank(dto.getBeneficiaryState()))
            this.beneficiaryState = dto.getBeneficiaryState();
        if (StringUtils.isNotBlank(dto.getBeneficiaryCity()))
            this.beneficiaryCity = dto.getBeneficiaryCity();
        if (StringUtils.isNotBlank(dto.getBeneficiaryZipcode()))
            this.beneficiaryZipcode = dto.getBeneficiaryZipcode();
        if (StringUtils.isNotBlank(dto.getBeneficiarycountry()))
            this.beneficiarycountry = dto.getBeneficiarycountry();
        if (StringUtils.isNotBlank(dto.getBeneficiaryEmail()))
            this.beneficiaryEmail = dto.getBeneficiaryEmail();
        if (StringUtils.isNotBlank(dto.getBeneficiaryPhone()))
            this.beneficiaryPhone = dto.getBeneficiaryPhone();
        if (StringUtils.isNotBlank(dto.getBeneficiaryBankName()))
            this.beneficiaryBankName = dto.getBeneficiaryBankName();
        if(StringUtils.isNotBlank(dto.getNumberOfRecurrences())){
            this.numberOfRecurrences = dto.getNumberOfRecurrences();
        }
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ExternalAccountNumber == null) ? 0 : ExternalAccountNumber.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((beneficiaryAddress == null) ? 0 : beneficiaryAddress.hashCode());
        result = prime * result + ((beneficiaryBankName == null) ? 0 : beneficiaryBankName.hashCode());
        result = prime * result + ((beneficiaryCountryCode == null) ? 0 : beneficiaryCountryCode.hashCode());
        result = prime * result + ((beneficiaryEmail == null) ? 0 : beneficiaryEmail.hashCode());
        result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
        result = prime * result + ((beneficiaryNickname == null) ? 0 : beneficiaryNickname.hashCode());
        result = prime * result + ((beneficiaryPhone == null) ? 0 : beneficiaryPhone.hashCode());
        result = prime * result + ((charges == null) ? 0 : charges.hashCode());
        result = prime * result + ((creditValueDate == null) ? 0 : creditValueDate.hashCode());
        result = prime * result + ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
        result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
        result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
        result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
        result = prime * result + ((fromAccountCurrency == null) ? 0 : fromAccountCurrency.hashCode());
        result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
        result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
        result = prime * result + ((numberOfAuthorisers == null) ? 0 : numberOfAuthorisers.hashCode());
        result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
        result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
        result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = prime * result + ((toAccountCurrency == null) ? 0 : toAccountCurrency.hashCode());
        result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
        result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
        result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
        result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
        result = prime * result + ((uploadedattachments == null) ? 0 : uploadedattachments.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((beneficiaryAddressLine1 == null) ? 0 : beneficiaryAddressLine1.hashCode());
        result = prime * result + ((beneficiaryAddressLine2 == null) ? 0 : beneficiaryAddressLine2.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((beneficiaryState == null) ? 0 : beneficiaryState.hashCode());
        result = prime * result + ((beneficiaryCity == null) ? 0 : beneficiaryCity.hashCode());
        result = prime * result + ((beneficiaryZipcode == null) ? 0 : beneficiaryZipcode.hashCode());
        result = prime * result + ((beneficiarycountry == null) ? 0 : beneficiarycountry.hashCode());
        result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
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
        SRMSIntraBankDTO other = (SRMSIntraBankDTO) obj;
        if (transactionId != other.transactionId)
            return false;
        return true;
    }

}
