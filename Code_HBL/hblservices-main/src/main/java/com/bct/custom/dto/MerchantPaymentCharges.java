package com.bct.custom.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantPaymentCharges implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String merchantCode;
	private String minAmount; 
	private String maxAmount; 
	private Double fee; 
	private String chargesType;
	private Double maxTransactionAmount;
	@JsonProperty("isNew")
	private Boolean isNew;
	
	public MerchantPaymentCharges() {
		super();
	}

	public MerchantPaymentCharges(Integer id, String merchantCode, String minAmount, String maxAmount, Double fee,
			String chargesType, Double maxTransactionAmount, Boolean isNew) {
		super();
		this.id = id;
		this.merchantCode = merchantCode;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.fee = fee;
		this.chargesType = chargesType;
		this.maxTransactionAmount = maxTransactionAmount;
		this.isNew = isNew;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
	}

	public String getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getChargesType() {
		return chargesType;
	}

	public void setChargesType(String chargesType) {
		this.chargesType = chargesType;
	}

	public Double getMaxTransactionAmount() {
		return maxTransactionAmount;
	}

	public void setMaxTransactionAmount(Double maxTransactionAmount) {
		this.maxTransactionAmount = maxTransactionAmount;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chargesType, fee, id, isNew, maxAmount, maxTransactionAmount, merchantCode, minAmount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MerchantPaymentCharges other = (MerchantPaymentCharges) obj;
		return Objects.equals(chargesType, other.chargesType) && Objects.equals(fee, other.fee)
				&& Objects.equals(id, other.id) && Objects.equals(isNew, other.isNew)
				&& Objects.equals(maxAmount, other.maxAmount)
				&& Objects.equals(maxTransactionAmount, other.maxTransactionAmount)
				&& Objects.equals(merchantCode, other.merchantCode) && Objects.equals(minAmount, other.minAmount);
	}

	@Override
	public String toString() {
		return "MerchantPaymentCharges [id=" + id + ", merchantCode=" + merchantCode + ", minAmount=" + minAmount
				+ ", maxAmount=" + maxAmount + ", fee=" + fee + ", chargesType=" + chargesType
				+ ", maxTransactionAmount=" + maxTransactionAmount + ", isNew=" + isNew + "]";
	}

	
}
