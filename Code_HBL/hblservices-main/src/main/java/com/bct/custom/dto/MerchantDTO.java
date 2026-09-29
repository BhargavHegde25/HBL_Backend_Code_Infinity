package com.bct.custom.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5621079492661160669L;

	private Integer id;
	private String code;
	private String categoryof;
	@JsonAlias({ "logourl" })
	private String logoUrl;
	private String paymentaggregator;
	@JsonProperty("isActive")
	private Boolean isActive;
	private String labelText;
	private String dbpErrCode;
	private String dbpErrMsg;
	private String fieldSetup;
	private String disclimerMessage;
	private String outageMessage;
	private String subcategory;
	private String subcategoryCode;
	private String categoryCode;
	@JsonAlias({ "placeholder" })
	private String placeholderText;
	private String categoryLogoUrl;
	private String subCategoryLogoUrl;
	private String maxTransactionLimit;
	public MerchantDTO() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCategoryof() {
		return categoryof;
	}
	public void setCategoryof(String categoryof) {
		this.categoryof = categoryof;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getPaymentaggregator() {
		return paymentaggregator;
	}
	public void setPaymentaggregator(String paymentaggregator) {
		this.paymentaggregator = paymentaggregator;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getLabelText() {
		return labelText;
	}
	public void setLabelText(String labelText) {
		this.labelText = labelText;
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
	public String getFieldSetup() {
		return fieldSetup;
	}
	public void setFieldSetup(String fieldSetup) {
		this.fieldSetup = fieldSetup;
	}
	public String getDisclimerMessage() {
		return disclimerMessage;
	}
	public void setDisclimerMessage(String disclimerMessage) {
		this.disclimerMessage = disclimerMessage;
	}
	public String getOutageMessage() {
		return outageMessage;
	}
	public void setOutageMessage(String outageMessage) {
		this.outageMessage = outageMessage;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	public String getSubcategoryCode() {
		return subcategoryCode;
	}
	public void setSubcategoryCode(String subcategoryCode) {
		this.subcategoryCode = subcategoryCode;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getPlaceholderText() {
		return placeholderText;
	}
	public void setPlaceholderText(String placeholderText) {
		this.placeholderText = placeholderText;
	}
	public String getCategoryLogoUrl() {
		return categoryLogoUrl;
	}
	public void setCategoryLogoUrl(String categoryLogoUrl) {
		this.categoryLogoUrl = categoryLogoUrl;
	}
	public String getSubCategoryLogoUrl() {
		return subCategoryLogoUrl;
	}
	public void setSubCategoryLogoUrl(String subCategoryLogoUrl) {
		this.subCategoryLogoUrl = subCategoryLogoUrl;
	}
	public String getMaxTransactionLimit() {
		return maxTransactionLimit;
	}
	public void setMaxTransactionLimit(String maxTransactionLimit) {
		this.maxTransactionLimit = maxTransactionLimit;
	}
	@Override
	public int hashCode() {
		return Objects.hash(categoryCode, categoryLogoUrl, categoryof, code, dbpErrCode, dbpErrMsg, disclimerMessage,
				fieldSetup, id, isActive, labelText, logoUrl, maxTransactionLimit, outageMessage, paymentaggregator,
				placeholderText, subCategoryLogoUrl, subcategory, subcategoryCode);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MerchantDTO other = (MerchantDTO) obj;
		return Objects.equals(categoryCode, other.categoryCode)
				&& Objects.equals(categoryLogoUrl, other.categoryLogoUrl)
				&& Objects.equals(categoryof, other.categoryof) && Objects.equals(code, other.code)
				&& Objects.equals(dbpErrCode, other.dbpErrCode) && Objects.equals(dbpErrMsg, other.dbpErrMsg)
				&& Objects.equals(disclimerMessage, other.disclimerMessage)
				&& Objects.equals(fieldSetup, other.fieldSetup) && Objects.equals(id, other.id)
				&& Objects.equals(isActive, other.isActive) && Objects.equals(labelText, other.labelText)
				&& Objects.equals(logoUrl, other.logoUrl)
				&& Objects.equals(maxTransactionLimit, other.maxTransactionLimit)
				&& Objects.equals(outageMessage, other.outageMessage)
				&& Objects.equals(paymentaggregator, other.paymentaggregator)
				&& Objects.equals(placeholderText, other.placeholderText)
				&& Objects.equals(subCategoryLogoUrl, other.subCategoryLogoUrl)
				&& Objects.equals(subcategory, other.subcategory)
				&& Objects.equals(subcategoryCode, other.subcategoryCode);
	}
	
	

	
	
	
	
	
	
	
	
	
}
