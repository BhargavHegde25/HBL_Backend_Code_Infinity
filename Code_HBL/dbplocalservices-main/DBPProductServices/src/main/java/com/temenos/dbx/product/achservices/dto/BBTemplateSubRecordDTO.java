package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BBTemplateSubRecordDTO implements DBPDTO{

	private static final long serialVersionUID = 1710982374100294948L;

	//@JsonAlias("TemplateSubRecord_id")
	private long templateSubRecord_id;
	
	@JsonProperty("TemplateSubRecord_id")
	private long tempSubRecord_id;
	
	//@JsonAlias("Amount")
	private double amount;
	
	@JsonProperty("Amount")
	private double amt;
	
	//@JsonAlias("TemplateRecord_id")
	private long templateRecord_id;
	
	@JsonProperty("TemplateRecord_id")
	private long tempRecord_id;
	
	//@JsonAlias("TaxSubCategory_id")
	private long taxSubCategory_id;
	
	@JsonProperty("TaxSubCategory_id")
	private long taxSubCat_id;
	
	private int softDelete;

	private String taxSubType;
	
	public BBTemplateSubRecordDTO() {
		super();
	}

	public BBTemplateSubRecordDTO(long templateSubRecord_id, double amount,long templateRecord_id,String taxSubType,
			long taxSubCategory_id, int softDelete) {
		super();
		this.templateSubRecord_id = templateSubRecord_id;
		this.amount = amount;
		this.templateRecord_id = templateRecord_id;
		this.taxSubCategory_id = taxSubCategory_id;
		this.softDelete = softDelete;
		this.taxSubType = taxSubType;
	}
	
	public long getTemplateSubRecord_id() {
		return templateSubRecord_id;
	}

	public void setTemplateSubRecord_id(long templateSubRecord_id) {
		this.templateSubRecord_id = templateSubRecord_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTemplateRecord_id() {
		return templateRecord_id;
	}

	public void setTemplateRecord_id(long templateRecord_id) {
		this.templateRecord_id = templateRecord_id;
	}

	public long getTaxSubCategory_id() {
		return taxSubCategory_id;
	}

	public void setTaxSubCategory_id(long taxSubCategory_id) {
		this.taxSubCategory_id = taxSubCategory_id;
	}

	public void setTempSubRecord_id(long tempSubRecord_id) {
		this.tempSubRecord_id = 0;
		this.setTemplateSubRecord_id(tempSubRecord_id);
	}

	public void setAmt(double amt) {
		this.amt = 0.0;
		this.setAmount(amt);
	}

	public void setTempRecord_id(long tempRecord_id) {
		this.tempRecord_id = 0;
		this.setTemplateRecord_id(tempRecord_id);
	}

	public void setTaxSubCat_id(long taxSubCat_id) {
		this.taxSubCat_id = 0;
		this.setTaxSubCategory_id(taxSubCat_id);
	}

	public int getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(int softDelete) {
		this.softDelete = softDelete;
	}

	public String getTaxSubType() {
		return taxSubType;
	}

	public void setTaxSubType(String taxSubType) {
		this.taxSubType = taxSubType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + softDelete;
		result = prime * result + (int) (taxSubCategory_id ^ (taxSubCategory_id >>> 32));
		result = prime * result + (int) (templateRecord_id ^ (templateRecord_id >>> 32));
		result = prime * result + (int) (templateSubRecord_id ^ (templateSubRecord_id >>> 32));
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
		BBTemplateSubRecordDTO other = (BBTemplateSubRecordDTO) obj;
		if (templateSubRecord_id != other.templateSubRecord_id)
			return false;
		return true;
	}
	
}
