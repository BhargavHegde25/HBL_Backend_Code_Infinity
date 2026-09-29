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
public class AccountsDTO implements DBPDTO{

	/** AccountsDTO is a serializable class, to serialize the getting and setting.
     */
	private static final long serialVersionUID = -6570098244266993919L;
	
	/**
     * These are fields in accounts Table
     * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
     */
	@JsonProperty("Account_id")
	private String accountId;
	@JsonProperty("Type_id")
	private String typeId;
	@JsonProperty("User_id")
	private String userId;
	@JsonProperty("Organization_id")
	private String organizationId;
	
	public AccountsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AccountsDTO(String accountId, String typeId) {
		super();
		this.accountId = accountId;
		this.typeId = typeId;
	}
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
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
		AccountsDTO other = (AccountsDTO) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		return true;
	}
	
	
	
	
}