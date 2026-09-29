package com.bct.custom.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantFieldsDTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4249400708101991770L;
	
	private Integer id;
	private String merchantCode;
	private String fieldname;
	private String fieldi18n;
	private String fieldtype;
	private Integer fieldindex;
	private String fieldvalue;
	@JsonProperty("isMandatory")
	private Boolean isMandatory;
	private String fieldname_np;
	private String placeholder;
	private String fieldcategory;
	private String paramKey;
	@JsonProperty("isNew")
	private Boolean isNew;
	
	public MerchantFieldsDTO() {
		super();
	}

	public MerchantFieldsDTO(Integer id, String merchantCode, String fieldname, String fieldi18n, String fieldtype,
			Integer fieldindex, String fieldvalue, Boolean isMandatory, String fieldname_np, String placeholder,
			String fieldcategory, Boolean isNew, String paramKey) {
		super();
		this.id = id;
		this.merchantCode = merchantCode;
		this.fieldname = fieldname;
		this.fieldi18n = fieldi18n;
		this.fieldtype = fieldtype;
		this.fieldindex = fieldindex;
		this.fieldvalue = fieldvalue;
		this.isMandatory = isMandatory;
		this.fieldname_np = fieldname_np;
		this.placeholder = placeholder;
		this.fieldcategory = fieldcategory;
		this.paramKey = paramKey;
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

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getFieldi18n() {
		return fieldi18n;
	}

	public void setFieldi18n(String fieldi18n) {
		this.fieldi18n = fieldi18n;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

	public Integer getFieldindex() {
		return fieldindex;
	}

	public void setFieldindex(Integer fieldindex) {
		this.fieldindex = fieldindex;
	}

	public String getFieldvalue() {
		return fieldvalue;
	}

	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getFieldname_np() {
		return fieldname_np;
	}

	public void setFieldname_np(String fieldname_np) {
		this.fieldname_np = fieldname_np;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getFieldcategory() {
		return fieldcategory;
	}

	public void setFieldcategory(String fieldcategory) {
		this.fieldcategory = fieldcategory;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldcategory, fieldi18n, fieldindex, fieldname, fieldname_np, fieldtype, fieldvalue, id,
				isMandatory, isNew, merchantCode, paramKey, placeholder);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MerchantFieldsDTO other = (MerchantFieldsDTO) obj;
		return Objects.equals(fieldcategory, other.fieldcategory) && Objects.equals(fieldi18n, other.fieldi18n)
				&& Objects.equals(fieldindex, other.fieldindex) && Objects.equals(fieldname, other.fieldname)
				&& Objects.equals(fieldname_np, other.fieldname_np) && Objects.equals(fieldtype, other.fieldtype)
				&& Objects.equals(fieldvalue, other.fieldvalue) && Objects.equals(id, other.id)
				&& Objects.equals(isMandatory, other.isMandatory) && Objects.equals(isNew, other.isNew)
				&& Objects.equals(merchantCode, other.merchantCode) && Objects.equals(paramKey, other.paramKey)
				&& Objects.equals(placeholder, other.placeholder);
	}

	@Override
	public String toString() {
		return "MerchantFieldsDTO [id=" + id + ", merchantCode=" + merchantCode + ", fieldname=" + fieldname
				+ ", fieldi18n=" + fieldi18n + ", fieldtype=" + fieldtype + ", fieldindex=" + fieldindex
				+ ", fieldvalue=" + fieldvalue + ", isMandatory=" + isMandatory + ", fieldname_np=" + fieldname_np
				+ ", placeholder=" + placeholder + ", fieldcategory=" + fieldcategory + ", paramKey=" + paramKey
				+ ", isNew=" + isNew + "]";
	}

	
	
	
	
}
