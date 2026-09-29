package com.temenos.dbx.product.dto;

import java.util.Map;

import com.temenos.dbx.product.api.DBPDTOEXT;

public class AccountTypeDTO implements DBPDTOEXT{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2391191242184869174L;
	
	private String accountTypeId;
	private String accountTypeDescription;
	private String accountTypeDisplayName;

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		return false;
	}

	public String getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(String accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public String getAccountTypeDescription() {
		return accountTypeDescription;
	}

	public void setAccountTypeDescription(String accountTypeDescription) {
		this.accountTypeDescription = accountTypeDescription;
	}

	public String getAccountTypeName() {
		return accountTypeDisplayName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeDisplayName = accountTypeName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Object loadDTO(String id) {
		return null;
	}

	@Override
	public Object loadDTO() {
		return null;
	}

}
