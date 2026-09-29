package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

import org.apache.commons.lang3.StringUtils;

/**
 * @author naveen.yerra
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DashboardDTO implements DBPDTO {
    private String product;
    private String exportLCId;
    private String exportlcSRMSRequestId;
    private String lcSrmsReqOrderID;
    private String srmsReqOrderID;
    private String guaranteeSrmsId;
    private String guaranteesSRMSId;
    private String collectionSrmsId;
    private String collectionReference;
    private String claimsSRMSId;
    @JsonAlias({"serviceRequestSrmsId"})
    private String transactionReference;
    @JsonAlias({"drawingAmount","claimAmount","lcAmount"})
    private String amount;
    @JsonAlias({"status","amendStatus","amendmentStatus","claimStatus"})
    private String status;
    @JsonAlias({"creditAccount","claimCreditAccount"})
    private String creditAccount;
    @JsonAlias({"debitAccount","accountToBeDebited","debitAmountFrom"})
    private String debitAccount;
    private String tradeCurrency;
    private String availableBalance;
    private String paymentStatus;
    @JsonAlias({"serviceRequestTime","amendRequestedDate","amendmentReceivedDate","drawingCreatedDate","lcCreatedOn","drawingCreationDate","amendmentDate","createdDate","receivedOn", "updatedOn"})
    private String date;
    @JsonAlias({"expiryDate", "maturityDate", "lcExpiryDate"})
    private String expiryDate;
    private String accountCurrency;
    private String balanceWithCurrency;
    private String customerId;
    private String account;
    private String maskedDebitAccount;
    private String maskedCreditAccount;
    private String formattedDate;
    private String tradeStatus;
    @JsonIgnore
    private String amountFormatted;

    public String getAmountFormatted() {
        return StringUtils.isNotBlank(String.valueOf(amount)) ? formatAmount(String.valueOf(amount)) : String.valueOf(amount);
    }
    
    public DashboardDTO() {
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getBalanceWithCurrency() {
        return balanceWithCurrency;
    }

    public void setExportLCId(String exportLCId) {
        this.exportLCId = exportLCId;
    }

    public void setExportlcSRMSRequestId(String exportlcSRMSRequestId) {
        this.exportlcSRMSRequestId = exportlcSRMSRequestId;
    }

    public void setLcSrmsReqOrderID(String lcSrmsReqOrderID) {
        this.lcSrmsReqOrderID = lcSrmsReqOrderID;
    }

    public void setSrmsReqOrderID(String srmsReqOrderID) {
        this.srmsReqOrderID = srmsReqOrderID;
    }

    public void setGuaranteeSrmsId(String guaranteeSrmsId) {
        this.guaranteeSrmsId = guaranteeSrmsId;
    }

    public void setGuaranteesSRMSId(String guaranteesSRMSId) {
        this.guaranteesSRMSId = guaranteesSRMSId;
    }

    public void setCollectionSrmsId(String collectionSrmsId) {
        this.collectionSrmsId = collectionSrmsId;
    }

    public void setCollectionReference(String collectionReference) {
        this.collectionReference = collectionReference;
    }

    public void setBalanceWithCurrency(String balanceWithCurrency) {
        this.balanceWithCurrency = balanceWithCurrency;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMaskedDebitAccount(String maskedDebitAccount) {
        this.maskedDebitAccount = maskedDebitAccount;
    }

    public void setMaskedCreditAccount(String maskedCreditAccount) {
        this.maskedCreditAccount = maskedCreditAccount;
    }

    public String getClaimsSRMSId() {
        return claimsSRMSId;
    }

    public void setClaimsSRMSId(String claimsSRMSId) {
        this.claimsSRMSId = claimsSRMSId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMaskedDebitAccount() {
        if(getCustomerId()!=null && getDebitAccount()!=null)
            return getMaskedAccountDetails(getCustomerId(), getDebitAccount());
        return maskedDebitAccount;
    }

    public String getMaskedCreditAccount() {
        if(getCustomerId()!=null && getCreditAccount()!=null)
            return getMaskedAccountDetails(getCustomerId(), getCreditAccount());
        return maskedCreditAccount;
    }

    public String getExportLCId() {
        return exportLCId;
    }

    public String getExportlcSRMSRequestId() {
        return exportlcSRMSRequestId;
    }

    public String getLcSrmsReqOrderID() {
        return lcSrmsReqOrderID;
    }

    public String getSrmsReqOrderID() {
        return srmsReqOrderID;
    }

    public String getGuaranteeSrmsId() {
        return guaranteeSrmsId;
    }

    public String getGuaranteesSRMSId() {
        return guaranteesSRMSId;
    }

    public String getCollectionSrmsId() {
        return collectionSrmsId;
    }

    public String getCollectionReference() {
        return collectionReference;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getAmount() {
        return amount!=null ? amount: "N/A";
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public String getAccount() {
        return account;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormattedDate() {
        return _formatDate(getDate());
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}
