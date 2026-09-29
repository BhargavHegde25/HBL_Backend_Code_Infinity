package com.kony.dbpalerts.alertsutils;

import java.util.List;
import java.util.Map;

public class AccountHelper {
	private Map<String, String> accounttypeinfo;
	private Map<String, List<String>> accountcustomerinfo;

	public AccountHelper(Map<String, String> accounttypeinfo, Map<String, List<String>> accountcustomerinfo) {
		setAccountcustomerinfo(accountcustomerinfo);
		setAccounttypeinfo(accounttypeinfo);
	}

	public Map<String, String> getAccounttypeinfo() {
		return accounttypeinfo;
	}

	public void setAccounttypeinfo(Map<String, String> accounttypeinfo) {
		this.accounttypeinfo = accounttypeinfo;
	}

	public Map<String, List<String>> getAccountcustomerinfo() {
		return accountcustomerinfo;
	}

	public void setAccountcustomerinfo(Map<String, List<String>> accountcustomerinfo) {
		this.accountcustomerinfo = accountcustomerinfo;
	}
}
