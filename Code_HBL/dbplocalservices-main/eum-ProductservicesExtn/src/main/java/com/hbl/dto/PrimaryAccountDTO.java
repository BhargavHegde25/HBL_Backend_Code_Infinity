package com.hbl.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;

public class PrimaryAccountDTO implements DBPDTO{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 9215432381183298206L;
	private String accountId;
	 private String productId;
	 private boolean isPrimary;
	 
	 
	public PrimaryAccountDTO() {
		super();
	}
	
	
	public PrimaryAccountDTO(String accountId, String productId, boolean isPrimary) {
		super();
		this.accountId = accountId;
		this.productId = productId;
		this.isPrimary = isPrimary;
	}


	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public boolean isPrimary() {
		return isPrimary;
	}
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}


	@Override
	public String toString() {
		return "PrimaryAccountDTO [accountId=" + accountId + ", productId="
				+ productId + ", isPrimary=" + isPrimary + "]";
	}


	@Override
	public int hashCode() {
		return Objects.hash(accountId, isPrimary, productId);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrimaryAccountDTO other = (PrimaryAccountDTO) obj;
		return Objects.equals(accountId, other.accountId) && isPrimary == other.isPrimary && Objects.equals(productId, other.productId);
	}
	 

}
