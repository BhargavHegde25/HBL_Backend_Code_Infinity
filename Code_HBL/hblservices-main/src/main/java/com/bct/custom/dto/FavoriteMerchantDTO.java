package com.bct.custom.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteMerchantDTO {
	
	private String accountNumber;
	private String payeeNickName;
	private String companyName;
	@JsonProperty("isFavoriteMerchant")
	private Boolean isManuallyAdded;
	@JsonAlias({"merchantCode","merchantId"})
	private String billerId;
	private String contractId;
	private String coreCustomerId;
	private String paymentAggregator;
	private String logoUrl;
	public FavoriteMerchantDTO() {
		super();
	}
	public FavoriteMerchantDTO(String accountNumber, String payeeNickName, String companyName, Boolean isManuallyAdded,
			String billerId, String contractId, String coreCustomerId, String paymentAggregator, String logoUrl) {
		super();
		this.accountNumber = accountNumber;
		this.payeeNickName = payeeNickName;
		this.companyName = companyName;
		this.isManuallyAdded = isManuallyAdded;
		this.billerId = billerId;
		this.contractId = contractId;
		this.coreCustomerId = coreCustomerId;
		this.paymentAggregator = paymentAggregator;
		this.logoUrl = logoUrl;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getPayeeNickName() {
		return payeeNickName;
	}
	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Boolean getIsManuallyAdded() {
		return isManuallyAdded;
	}
	public void setIsManuallyAdded(Boolean isManuallyAdded) {
		this.isManuallyAdded = isManuallyAdded;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getCoreCustomerId() {
		return coreCustomerId;
	}
	public void setCoreCustomerId(String coreCustomerId) {
		this.coreCustomerId = coreCustomerId;
	}
	public String getPaymentAggregator() {
		return paymentAggregator;
	}
	public void setPaymentAggregator(String paymentAggregator) {
		this.paymentAggregator = paymentAggregator;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	@Override
	public int hashCode() {
		return Objects.hash(accountNumber, billerId, companyName, contractId, coreCustomerId, isManuallyAdded, logoUrl,
				payeeNickName, paymentAggregator);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FavoriteMerchantDTO other = (FavoriteMerchantDTO) obj;
		return Objects.equals(accountNumber, other.accountNumber) && Objects.equals(billerId, other.billerId)
				&& Objects.equals(companyName, other.companyName) && Objects.equals(contractId, other.contractId)
				&& Objects.equals(coreCustomerId, other.coreCustomerId)
				&& Objects.equals(isManuallyAdded, other.isManuallyAdded) && Objects.equals(logoUrl, other.logoUrl)
				&& Objects.equals(payeeNickName, other.payeeNickName)
				&& Objects.equals(paymentAggregator, other.paymentAggregator);
	}
	@Override
	public String toString() {
		return "FavoriteMerchantDTO [accountNumber=" + accountNumber + ", payeeNickName=" + payeeNickName
				+ ", companyName=" + companyName + ", isManuallyAdded=" + isManuallyAdded + ", billerId=" + billerId
				+ ", contractId=" + contractId + ", coreCustomerId=" + coreCustomerId + ", paymentAggregator="
				+ paymentAggregator + ", logoUrl=" + logoUrl + "]";
	}
	
	
	
	

}
