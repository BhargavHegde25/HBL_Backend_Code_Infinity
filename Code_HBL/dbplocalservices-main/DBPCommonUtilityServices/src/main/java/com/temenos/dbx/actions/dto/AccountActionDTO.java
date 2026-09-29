package com.temenos.dbx.actions.dto;

import java.util.Set;

import com.dbp.core.api.DBPDTO;

public class AccountActionDTO implements DBPDTO {
	
	private static final long serialVersionUID = -4424310022186366168L;
	private String accountNumber;
	private boolean isBusinessAccount;
	private Set<String> actions;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public boolean isBusinessAccount() {
		return isBusinessAccount;
	}

	public void setBusinessAccount(boolean isBusinessAccount) {
		this.isBusinessAccount = isBusinessAccount;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(accountNumber).append("]");
		sb.append(isBusinessAccount);
		sb.append(actions);
		return sb.toString();
	}
}
