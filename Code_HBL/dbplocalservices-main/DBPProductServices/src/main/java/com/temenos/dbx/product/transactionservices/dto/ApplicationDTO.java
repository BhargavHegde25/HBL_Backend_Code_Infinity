package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author KH2144
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ApplicationDTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4567373638080006370L;
	
	private int id;
	private int bwFileTransactionsLimit;
	public ApplicationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ApplicationDTO(int id, int bwFileTransactionsLimit) {
		super();
		this.id = id;
		this.bwFileTransactionsLimit = bwFileTransactionsLimit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBwFileTransactionsLimit() {
		return bwFileTransactionsLimit;
	}
	public void setBwFileTransactionsLimit(int bwFileTransactionsLimit) {
		this.bwFileTransactionsLimit = bwFileTransactionsLimit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bwFileTransactionsLimit;
		result = prime * result + id;
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
		ApplicationDTO other = (ApplicationDTO) obj;
		if (bwFileTransactionsLimit != other.bwFileTransactionsLimit)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}