package com.kony.dbp.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccountLimitDTO implements Serializable{
	
	private static final long serialVersionUID = -5651395077551481017L;
	
	String accountId;
	boolean isBusinessAccount = false;
	Map<String, Double> limits = new HashMap<>();

	public AccountLimitDTO() {
		
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public boolean isBusinessAccount() {
		return isBusinessAccount;
	}

	public void setBusinessAccount(boolean isBusinessAccount) {
		this.isBusinessAccount = isBusinessAccount;
	}

	public Map<String, Double> getLimits() {
		return limits;
	}

	public void setLimits(Map<String, Double> limits) {
		this.limits = limits;
	}

}
