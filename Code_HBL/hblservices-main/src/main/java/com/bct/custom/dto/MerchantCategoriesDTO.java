package com.bct.custom.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantCategoriesDTO {
	
	private Integer id;
	private String category;
	@JsonProperty("isActive")
	private Boolean isActive;
	@JsonAlias({ "logourl" })
	private String logoUrl;
	private String subcategoryof;
	private String labelText;
	private String code;
	private String dbpErrCode;
	private String dbpErrMsg;
	private Integer sequence;
	private String merchantType;
	private String paymentaggregator;
	
	
	public MerchantCategoriesDTO() {
		super();
	}

	public MerchantCategoriesDTO(Integer id, String category, Boolean isActive, String logoUrl, String subcategoryof,
			String labelText, String code, String dbpErrCode, String dbpErrMsg, Integer sequence, String merchantType, String paymentaggregator) {
		super();
		this.id = id;
		this.category = category;
		this.isActive = isActive;
		this.logoUrl = logoUrl;
		this.subcategoryof = subcategoryof;
		this.labelText = labelText;
		this.code = code;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.sequence = sequence;
		this.merchantType = merchantType;
		this.paymentaggregator = paymentaggregator;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getSubcategoryof() {
		return subcategoryof;
	}

	public void setSubcategoryof(String subcategoryof) {
		this.subcategoryof = subcategoryof;
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}
	

	public String getPaymentaggregator() {
		return paymentaggregator;
	}

	public void setPaymentaggregator(String paymentaggregator) {
		this.paymentaggregator = paymentaggregator;
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, code, dbpErrCode, dbpErrMsg, id, isActive, labelText, logoUrl, merchantType,
				sequence, subcategoryof, paymentaggregator);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MerchantCategoriesDTO other = (MerchantCategoriesDTO) obj;
		return Objects.equals(category, other.category) && Objects.equals(code, other.code)
				&& Objects.equals(dbpErrCode, other.dbpErrCode) && Objects.equals(dbpErrMsg, other.dbpErrMsg)
				&& Objects.equals(id, other.id) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(labelText, other.labelText) && Objects.equals(logoUrl, other.logoUrl)
				&& Objects.equals(merchantType, other.merchantType) && Objects.equals(sequence, other.sequence)
				&& Objects.equals(subcategoryof, other.subcategoryof) && Objects.equals(paymentaggregator, other.paymentaggregator);
	}

	@Override
	public String toString() {
		return "MerchantCategoriesDTO [id=" + id + ", category=" + category + ", isActive=" + isActive + ", logoUrl="
				+ logoUrl + ", subcategoryof=" + subcategoryof + ", labelText=" + labelText + ", code=" + code
				+ ", dbpErrCode=" + dbpErrCode + ", dbpErrMsg=" + dbpErrMsg + ", sequence=" + sequence
				+ ", merchantType=" + merchantType + ", paymentaggregator=" + paymentaggregator + "]";
	}

	

	
	
	
	
}
