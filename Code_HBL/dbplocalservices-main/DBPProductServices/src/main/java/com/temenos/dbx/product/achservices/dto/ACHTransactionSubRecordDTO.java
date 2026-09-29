package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author KH2317
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHTransactionSubRecordDTO implements DBPDTO {

	private static final long serialVersionUID = 6729762644233634004L;

	private long transactionSubRecord_id;
	
	//@JsonAlias({"Amount"})
	private double amount;
	
	@JsonProperty("Amount")
	private double amnt;
	
	//@JsonAlias({"TransactionRecord_id"})
	private long transactionRecord_id;
	
	@JsonProperty("TransactionRecord_id")
	private long trRecrdId;
	
	//@JsonAlias({"TaxSubCategory_id"})
	private long taxSubCategory_id;
	
	@JsonProperty("TaxSubCategory_id")
	private long taxSbCatId;
	
	private int softDelete;

	private String taxSubType;

	public ACHTransactionSubRecordDTO() {
		super();
	}

	public ACHTransactionSubRecordDTO(long transactionSubRecord_id, double amount, long transactionRecord_id,
			long taxSubCategory_id, int softDelete, String taxSubType) {
		super();
		this.transactionSubRecord_id = transactionSubRecord_id;
		this.amount = amount;
		this.transactionRecord_id = transactionRecord_id;
		this.taxSubCategory_id = taxSubCategory_id;
		this.softDelete = softDelete;
		this.taxSubType = taxSubType;
	}

	public long getTransactionSubRecord_id() {
		return transactionSubRecord_id;
	}

	public void setTransactionSubRecord_id(long transactionSubRecord_id) {
		this.transactionSubRecord_id = transactionSubRecord_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTransactionRecord_id() {
		return transactionRecord_id;
	}

	public void setTransactionRecord_id(long transactionRecord_id) {
		this.transactionRecord_id = transactionRecord_id;
	}

	public long getTaxSubCategory_id() {
		return taxSubCategory_id;
	}

	public void setTaxSubCategory_id(long taxSubCategory_id) {
		this.taxSubCategory_id = taxSubCategory_id;
	}

	public int getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(int softDelete) {
		this.softDelete = softDelete;
	}
	
	public void setAmnt(double amnt) {
		this.amnt = 0;
		this.setAmount(amnt);
	}

	public void setTrRecrdId(long trRecrdId) {
		this.trRecrdId = 0;
		this.setTransactionRecord_id(trRecrdId);
	}

	public void setTaxSbCatId(long taxSbCatId) {
		this.taxSbCatId = 0;
		this.setTaxSubCategory_id(taxSbCatId);
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
		result = prime * result + ((taxSubType == null) ? 0 : taxSubType.hashCode());
		result = prime * result + (int) (transactionRecord_id ^ (transactionRecord_id >>> 32));
		result = prime * result + (int) (transactionSubRecord_id ^ (transactionSubRecord_id >>> 32));
		
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
		ACHTransactionSubRecordDTO other = (ACHTransactionSubRecordDTO) obj;
		if (transactionSubRecord_id != other.transactionSubRecord_id)
			return false;

		return true;
	}
		
}
