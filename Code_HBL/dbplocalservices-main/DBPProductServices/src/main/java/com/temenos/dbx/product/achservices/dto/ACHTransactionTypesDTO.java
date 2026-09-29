package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author KH2381
 * @version 1.0
 */

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHTransactionTypesDTO implements DBPDTO {

	private static final long serialVersionUID = 8182849227351165288L;

	@JsonProperty("TransactionType_id")
	private int transactionTypeId;
	
	private int transactionType_id ;
	
	@JsonProperty("TransactionTypeName")
	private String transactionTName;
	
	private String transactionTypeName ;
	
	public ACHTransactionTypesDTO () {
		super();
	}
	
	public ACHTransactionTypesDTO ( int transactionTypeId, String transactionTypeName ) {
		super();
		this.transactionType_id = transactionTypeId ; 
		this.transactionTypeName = transactionTypeName ;
	}
	
	public int getTransactionType_id() {
		return transactionType_id ;
	}

	public void setTransactionType_id(int transactionTypeId) {
		this.transactionType_id = transactionTypeId ;
	}

	public String getTransactionTypeName() {
		return transactionTypeName ;
	}

	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName ;
	}
	
	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = 0;
		this.setTransactionType_id(transactionTypeId);
	}

	public void setTransactionTName(String transactionTName) {
		this.transactionTName = null;
		this.setTransactionTypeName(transactionTName);
	}

	@Override 
	public int hashCode() {
		final int prime = 31 ;
		int result = 1 ;
		result = prime * result + (int) ( transactionType_id ^ ( transactionType_id >>> 32 )) ;
		result = prime * result + (( transactionTypeName == null ) ? 0 : transactionTypeName.hashCode() ) ;
		return result ;
				
	}
	
	@Override
	public boolean equals( Object ob) {
		if (this == ob)
			return true;
		if (ob == null)
			return false;
		if (getClass() != ob.getClass())
			return false;
		ACHTransactionTypesDTO other = (ACHTransactionTypesDTO) ob;
		if (transactionType_id != other.transactionType_id)
			return false;
		return true;
		
	}
	
}
