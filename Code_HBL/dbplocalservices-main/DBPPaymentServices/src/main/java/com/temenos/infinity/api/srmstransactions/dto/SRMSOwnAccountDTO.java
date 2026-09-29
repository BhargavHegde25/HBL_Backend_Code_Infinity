package com.temenos.infinity.api.srmstransactions.dto;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferBackendDTO;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class SRMSOwnAccountDTO implements DBPDTO {

    private static final long serialVersionUID = 6597925078728487784L;

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
    private String beneficiaryNickname;
    private String fromAccountCurrency;
    private String toAccountCurrency;
    private String uploadedattachments;
    private String totalAmount;
    private String numberOfRecurrences;

    /**
     * 
     */
    public SRMSOwnAccountDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SRMSOwnAccountDTO(String fromAccountNumber, String toAccountNumber, String amount, String transactionsNotes,
            String transactionType, String scheduledDate, String isScheduled, String transactionCurrency,
            String transactionId, String frequencyType, String serviceName, String paymentType, String userId,
            String numberOfAuthorisers, String frequencyStartDate, String frequencyEndDate, String beneficiaryNickname,
            String fromAccountCurrency, String toAccountCurrency, String uploadedattachments, String totalAmount, String numberOfRecurrences) {
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
        this.beneficiaryNickname = beneficiaryNickname;
        this.fromAccountCurrency = fromAccountCurrency;
        this.toAccountCurrency = toAccountCurrency;
        this.uploadedattachments = uploadedattachments;
        this.totalAmount = totalAmount;
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

    public SRMSOwnAccountDTO convert(OwnAccountFundTransferBackendDTO dto) {

        this.amount = String.valueOf(dto.getAmount());
        this.fromAccountNumber = dto.getFromAccountNumber();
        this.isScheduled = dto.getIsScheduled();
        this.frequencyType = dto.getFrequencyTypeId();
        this.serviceName = dto.getServiceName();
        this.toAccountNumber = dto.getToAccountNumber();
        this.transactionCurrency = dto.getTransactionCurrency();
        //if (!dto.getFrequencyType().equalsIgnoreCase("Once")) {
        if (!dto.getFrequencyTypeId().equalsIgnoreCase("Once")) {
            this.frequencyEndDate = dto.getFrequencyEndDate();
        }
        // Null check on non mandatory fields
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
        if(StringUtils.isNotBlank(dto.getNumberOfRecurrences())){
            this.numberOfRecurrences = dto.getNumberOfRecurrences();
        }
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((beneficiaryNickname == null) ? 0 : beneficiaryNickname.hashCode());
        result = prime * result + ((frequencyEndDate == null) ? 0 : frequencyEndDate.hashCode());
        result = prime * result + ((frequencyStartDate == null) ? 0 : frequencyStartDate.hashCode());
        result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
        result = prime * result + ((fromAccountCurrency == null) ? 0 : fromAccountCurrency.hashCode());
        result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
        result = prime * result + ((isScheduled == null) ? 0 : isScheduled.hashCode());
        result = prime * result + ((numberOfAuthorisers == null) ? 0 : numberOfAuthorisers.hashCode());
        result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
        result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = prime * result + ((toAccountCurrency == null) ? 0 : toAccountCurrency.hashCode());
        result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
        result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
        result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
        result = prime * result + ((uploadedattachments == null) ? 0 : uploadedattachments.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        SRMSOwnAccountDTO other = (SRMSOwnAccountDTO) obj;
        if (transactionId != other.transactionId)
            return false;
        return true;
    }

}
