package com.temenos.infinity.api.srmstransactions.dto;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionBackendDTO;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class SRMSP2PTransferDTO implements DBPDTO {

    private static final long serialVersionUID = 6597925078728487784L;

    private String fromAccountNumber;
    private String toAccountNumber;
    private String amount;
    private String transactionsNotes;
    private String transactionType;
    private String scheduledDate;
    private String isScheduled;
    private String transactionCurrency;
    private String fromAccountCurrency;
    private String toAccountCurrency;
    private String transactionId;
    private String frequencyType;
    private String serviceName;
    private String frequencyStartDate;
    private String frequencyEndDate;
    private String p2pContact;
    private String personId;
    private String numberOfRecurrences;
    private String dbxtransactionId;
    private String status;
    private String paidBy;
    private String beneficiaryId;
    private String ExternalAccountNumber;
    private String beneficiaryName;
    private String beneficiaryPhone;
    private String beneficiaryEmail;

    /**
     * 
     */
    public SRMSP2PTransferDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SRMSP2PTransferDTO(String fromAccountNumber, String toAccountNumber, String amount, String transactionsNotes,
            String transactionType, String scheduledDate, String isScheduled, String transactionCurrency,
            String fromAccountCurrency, String toAccountCurrency, String transactionId, String frequencyType,
            String serviceName, String frequencyStartDate, String frequencyEndDate, String p2pContact, String personId,
            String numberOfRecurrences, String dbxtransactionId, String status, String paidBy, String beneficiaryId,
            String externalAccountNumber, String beneficiaryName, String beneficiaryPhone, String beneficiaryEmail) {
        super();
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.transactionsNotes = transactionsNotes;
        this.transactionType = transactionType;
        this.scheduledDate = scheduledDate;
        this.isScheduled = isScheduled;
        this.transactionCurrency = transactionCurrency;
        this.fromAccountCurrency = fromAccountCurrency;
        this.toAccountCurrency = toAccountCurrency;
        this.transactionId = transactionId;
        this.frequencyType = frequencyType;
        this.serviceName = serviceName;
        this.frequencyStartDate = frequencyStartDate;
        this.frequencyEndDate = frequencyEndDate;
        this.p2pContact = p2pContact;
        this.personId = personId;
        this.numberOfRecurrences = numberOfRecurrences;
        this.dbxtransactionId = dbxtransactionId;
        this.status = status;
        this.paidBy = paidBy;
        this.beneficiaryId = beneficiaryId;
        ExternalAccountNumber = externalAccountNumber;
        this.beneficiaryEmail = beneficiaryEmail;
        this.beneficiaryName = beneficiaryName;
        this.beneficiaryPhone = beneficiaryPhone;
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

    public String getP2pContact() {
        return p2pContact;
    }

    public void setP2pContact(String p2pContact) {
        this.p2pContact = p2pContact;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getNumberOfRecurrences() {
        return numberOfRecurrences;
    }

    public void setNumberOfRecurrences(String numberOfRecurrences) {
        this.numberOfRecurrences = numberOfRecurrences;
    }

    public String getDbxtransactionId() {
        return dbxtransactionId;
    }

    public void setDbxtransactionId(String dbxtransactionId) {
        this.dbxtransactionId = dbxtransactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getExternalAccountNumber() {
        return ExternalAccountNumber;
    }

    public void setExternalAccountNumber(String externalAccountNumber) {
        ExternalAccountNumber = externalAccountNumber;
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

    public SRMSP2PTransferDTO convert(P2PTransactionBackendDTO dto) {

        this.amount = String.valueOf(dto.getAmount());
        this.fromAccountNumber = dto.getFromAccountNumber();
        this.isScheduled = dto.getIsScheduled();
        this.frequencyType = dto.getFrequencyType();
        this.personId = dto.getPersonId();
        this.transactionCurrency = dto.getTransactionCurrency();
        if (!dto.getFrequencyType().equalsIgnoreCase("Once")) {
            this.frequencyEndDate = dto.getFrequencyEndDate();
        }
        // Null check on non mandatory fields
        if (StringUtils.isNotBlank(dto.getServiceName()))
            this.serviceName = dto.getServiceName();
        if (StringUtils.isNotBlank(dto.getToAccountNumber()))
            this.toAccountNumber = dto.getToAccountNumber();
        if (StringUtils.isNotBlank(dto.getTransactionId()))
            this.transactionId = dto.getTransactionId();
        if (StringUtils.isNotBlank(dto.getScheduledDate()))
            this.scheduledDate = dto.getScheduledDate();
        if (StringUtils.isNotBlank(dto.getTransactionType()))
            this.transactionType = dto.getTransactionType();
        if (StringUtils.isNotBlank(dto.getTransactionsNotes()))
            this.transactionsNotes = dto.getTransactionsNotes();
        if (StringUtils.isNotBlank(dto.getP2pContact()))
            this.p2pContact = dto.getP2pContact();
        if (StringUtils.isNotBlank(dto.getFromAccountCurrency()))
            this.fromAccountCurrency = dto.getFromAccountCurrency();
        if (StringUtils.isNotBlank(dto.getPaidBy()))
            this.paidBy = dto.getPaidBy();
        if (StringUtils.isNotBlank(dto.getBeneficiaryName()))
            this.beneficiaryName = dto.getBeneficiaryName();
        if (StringUtils.isNotBlank(dto.getBeneficiaryPhone()))
            this.beneficiaryPhone = dto.getBeneficiaryPhone();
        if (StringUtils.isNotBlank(dto.getBeneficiaryEmail()))
            this.beneficiaryEmail = dto.getBeneficiaryEmail();
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ExternalAccountNumber == null) ? 0 : ExternalAccountNumber.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((beneficiaryId == null) ? 0 : beneficiaryId.hashCode());
        result = prime * result + ((dbxtransactionId == null) ? 0 : dbxtransactionId.hashCode());
        result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
        result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
        result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
        result = prime * result + ((fromAccountCurrency == null) ? 0 : fromAccountCurrency.hashCode());
        result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
        result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
        result = prime * result + ((numberOfRecurrences == null) ? 0 : numberOfRecurrences.hashCode());
        result = prime * result + ((p2pContact == null) ? 0 : p2pContact.hashCode());
        result = prime * result + ((paidBy == null) ? 0 : paidBy.hashCode());
        result = prime * result + ((personId == null) ? 0 : personId.hashCode());
        result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((toAccountCurrency == null) ? 0 : toAccountCurrency.hashCode());
        result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
        result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
        result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
        result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
        result = prime * result + ((beneficiaryPhone == null) ? 0 : beneficiaryPhone.hashCode());
        result = prime * result + ((beneficiaryEmail == null) ? 0 : beneficiaryEmail.hashCode());
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
        SRMSP2PTransferDTO other = (SRMSP2PTransferDTO) obj;
        if (transactionId != other.transactionId)
            return false;
        return true;
    }

}
